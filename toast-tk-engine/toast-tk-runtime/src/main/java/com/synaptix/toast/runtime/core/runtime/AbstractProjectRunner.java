package com.synaptix.toast.runtime.core.runtime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.synaptix.toast.core.dao.ICampaign;
import com.synaptix.toast.core.dao.ITestPage;
import com.synaptix.toast.core.rest.RestUtils;
import com.synaptix.toast.core.runtime.ITestManager;
import com.synaptix.toast.dao.domain.impl.report.Project;
import com.synaptix.toast.dao.domain.impl.test.TestPage;
import com.synaptix.toast.runtime.core.parse.TestParser;
import com.synaptix.toast.runtime.dao.DAOManager;

public abstract class AbstractProjectRunner {

	private static final Logger LOG = LogManager.getLogger(AbstractProjectRunner.class);

	private ITestManager testEnvManager;

	private Injector injector;

	protected AbstractProjectRunner(
		Injector injector)
		throws Exception {
		try {
			this.testEnvManager = injector.getInstance(ITestManager.class);
		}
		catch(ConfigurationException e) {
			System.out.println("No Test Environement Manager defined !");
		}
		this.injector = injector;
	}

	public final void test(
		String projectName,
		boolean overrideRepoFromWebApp)
		throws Exception {
		Project lastProject = DAOManager.getInstance().getLastProjectByName(projectName);
		Project referenceProject = DAOManager.getInstance().getReferenceProjectByName(projectName);
		if(referenceProject == null){
			throw new IllegalAccessException("No reference project name found for: " + projectName);
		}
		Project newIterationProject = mergeToNewIteration(lastProject, referenceProject);
		execute(newIterationProject, overrideRepoFromWebApp);
		DAOManager.getInstance().saveProject(newIterationProject);
	}

	private Project mergeToNewIteration(
		Project lastIterationProject,
		Project newIterationProject) {
		if(lastIterationProject.getIteration() == newIterationProject.getIteration()){
			return newIterationProject;
		}
		
		//creating a new iteration from history
		newIterationProject.setId(null);
		newIterationProject.setIteration(lastIterationProject.getIteration());
		
		for(ICampaign newCampaign: newIterationProject.getCampaigns()){
			for(ICampaign lastCampaign: lastIterationProject.getCampaigns()){
				if(newCampaign.getIdAsString().equals(lastCampaign.getIdAsString())){
					for(ITestPage newPage: newCampaign.getTestCases()){
						for(ITestPage lastPage: lastCampaign.getTestCases()){
							if(newPage.getIdAsString().equals(lastPage.getIdAsString())){
								newPage.setPreviousIsSuccess(lastPage.isPreviousIsSuccess());
								newPage.setPreviousExecutionTime(lastPage.getPreviousExecutionTime());
							}
						}
					}
				}
			}
		}
		
		return newIterationProject;
	}

	private void execute(
		Project project,
		boolean presetRepoFromWebApp)
		throws Exception {
		TestRunner runner = TestRunner.FromInjector(testEnvManager, injector);
		if(presetRepoFromWebApp) {
			String repoWiki = RestUtils.downloadRepositoyAsWiki();
			TestParser parser = new TestParser();
			TestPage repoAsTestPageForConveniency = parser.readString(repoWiki, "");
			runner.run(repoAsTestPageForConveniency, false);
			if(LOG.isDebugEnabled()) {
				LOG.debug("Preset repository from webapp rest api...");
			}
		}
		execute(project, runner);
	}

	private void execute(
		Project project,
		TestRunner runner)
		throws ClassNotFoundException {
		initEnvironment();
		for(ICampaign campaign : project.getCampaigns()) {
			for(ITestPage testPage : campaign.getTestCases()) {
				try {
					beginTest();
					testPage = runner.run(testPage, true);
					endTest();
				}
				catch(IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		tearDownEnvironment();
	}

	public abstract void tearDownEnvironment();

	public abstract void beginTest();

	public abstract void endTest();

	public abstract void initEnvironment();
}
