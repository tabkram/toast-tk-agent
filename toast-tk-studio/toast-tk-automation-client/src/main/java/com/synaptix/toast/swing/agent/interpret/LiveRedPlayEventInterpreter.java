package com.synaptix.toast.swing.agent.interpret;

import com.synaptix.toast.adapter.SentenceBuilder;
import com.synaptix.toast.core.adapter.ActionAdapterSentenceRef.Types;
import com.synaptix.toast.core.agent.interpret.AWTEventCapturedObject;
import com.synaptix.toast.dao.domain.impl.repository.RepositoryImpl;


/**
 * 1. Connect to MongoDB throught toast web app api and check for existing types
 * 2. store or update repository with new objects
 * 3. query known syntax to build and record automation sentence
 * 
 * 
 * TODO: check if webapp is up
 */
public class LiveRedPlayEventInterpreter extends DefaultEventInterpreter{

	SentenceBuilder sentenceBuilder = new SentenceBuilder();
	private MongoRepoManager mongoRepoManager;
	
	public LiveRedPlayEventInterpreter(MongoRepoManager mongoRepoManager){
		this.mongoRepoManager= mongoRepoManager;
		mongoRepoManager.initCache();
	}
	
	@Override
	public String onWindowDisplay(AWTEventCapturedObject eventObject) {
		return super.onWindowDisplay(eventObject);
	}

	@Override
	public String onBringOnTop(AWTEventCapturedObject eventObject) {
		return super.onBringOnTop(eventObject);
	}

	@Override
	public String onClick(AWTEventCapturedObject eventObject) {
		RepositoryImpl container = mongoRepoManager.findContainer(eventObject.container);
		String label = mongoRepoManager.find(container, convertToKnowType(eventObject.componentType), eventObject.componentName);
		return sentenceBuilder.ofType(Types.CLICK_ON)
				.withPage(container.getName())
				.withComponent(label).build(); 
	}
	
	@Override
	public String onCheckBoxClick(AWTEventCapturedObject eventObject) {
		eventObject.componentType = "checkbox";
		return onClick(eventObject);
	}
	
	@Override
	public String onButtonClick(AWTEventCapturedObject eventObject) {
		eventObject.componentType = "button";
		return onClick(eventObject);
	}
	
	@Override
	public String onKeyInput(AWTEventCapturedObject eventObject) {
		RepositoryImpl container = mongoRepoManager.findContainer(eventObject.container);
		String label = mongoRepoManager.find(container, convertToKnowType(eventObject.componentType), eventObject.componentName);
		return sentenceBuilder.ofType(Types.TYPE_IN_INPUT)
				.withPage(container.getName())
				.withComponent(label)
				.withValue(eventObject.businessValue).build(); 
	}
	
	/**
	 * Convert the type to a known type 
	 * (currently hosted on webapp side:
	 * "button", "input", "menu", "table", "timeline", "date", "list", "checkbox", "other"
	 * To expose through webservice
	 * 
	 * @param type
	 * @return
	 */
	private String convertToKnowType(String type){
		if(type.toLowerCase().contains("button")) return "button";
		if(type.toLowerCase().contains("checkbox")) return "checkbox";
		if(type.toLowerCase().contains("text")) return "input";
		if(type.toLowerCase().contains("table")) return "table";
		if(type.toLowerCase().contains("combo")) return "list";
		return "other";
	}

	@Override
	public String onTableClick(AWTEventCapturedObject eventObject) {
		RepositoryImpl container = mongoRepoManager.findContainer(eventObject.container);
		String label = mongoRepoManager.find(container, convertToKnowType(eventObject.componentType), eventObject.componentLocator);
		return sentenceBuilder.ofType(Types.SELECT_TABLE_ROW)
				.withPage(container.getName())
				.withComponent(label)
				.withValue(eventObject.businessValue).build(); 
	}

	@Override
	public String onMenuClick(AWTEventCapturedObject eventObject) {
		return sentenceBuilder.ofType(Types.SELECT_SUB_MENU).withValue(eventObject.componentName).build(); 
	}

	@Override
	public String onComboBoxClick(AWTEventCapturedObject eventObject) {
		RepositoryImpl container = mongoRepoManager.findContainer(eventObject.container);
		String label = mongoRepoManager.find(container, convertToKnowType(eventObject.componentType), eventObject.componentName);
		return sentenceBuilder.ofType(Types.SELECT_VALUE_IN_LIST)
				.withPage(container.getName())
				.withComponent(label)
				.withValue(eventObject.businessValue).build(); 
	}

	@Override
	public String onPopupMenuClick(AWTEventCapturedObject eventObject) {
		return sentenceBuilder.ofType(Types.SELECT_CONTEXTUAL_MENU)
				.withValue(eventObject.componentName).build();
	}

	public boolean saveObjectsToRepository() {
		return mongoRepoManager.saveCache();
	}
	

}