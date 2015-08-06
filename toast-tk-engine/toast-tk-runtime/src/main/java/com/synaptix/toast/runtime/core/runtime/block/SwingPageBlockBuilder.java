package com.synaptix.toast.runtime.core.runtime.block;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.synaptix.toast.core.dao.ITestPage;
import com.synaptix.toast.core.runtime.IFeedableSwingPage;
import com.synaptix.toast.core.runtime.IRepositorySetup;
import com.synaptix.toast.dao.domain.impl.test.SwingPageConfigLine;
import com.synaptix.toast.dao.domain.impl.test.block.SwingPageBlock;

public class SwingPageBlockBuilder implements IBlockRunner<SwingPageBlock>{

	@Inject
	IRepositorySetup objectRepository;
	
	@Override
	public void run(ITestPage page, SwingPageBlock block) {
		objectRepository.addSwingPage(block.getFixtureName());
		IFeedableSwingPage swingPage = objectRepository.getSwingPage(block.getFixtureName());
		for(SwingPageConfigLine line : block.getBlockLines()) {
			swingPage.addElement(line.getElementName(), line.getType(), line.getLocator());
		}		
	}

	@Override
	public void setInjector(Injector injector) {
		// TODO Auto-generated method stub
	}

}
