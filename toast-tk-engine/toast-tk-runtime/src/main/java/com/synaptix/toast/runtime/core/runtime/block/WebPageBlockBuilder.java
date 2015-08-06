package com.synaptix.toast.runtime.core.runtime.block;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.synaptix.toast.adapter.web.DefaultWebPage;
import com.synaptix.toast.core.dao.ITestPage;
import com.synaptix.toast.core.runtime.IRepositorySetup;
import com.synaptix.toast.dao.domain.impl.test.WebPageConfigLine;
import com.synaptix.toast.dao.domain.impl.test.block.WebPageBlock;

public  class WebPageBlockBuilder implements IBlockRunner<WebPageBlock>{
	
	@Inject
	IRepositorySetup objectRepository;
	
	@Override
	public void run(ITestPage page, WebPageBlock block) {
		objectRepository.addPage(block.getFixtureName());
		DefaultWebPage webPage = (DefaultWebPage) objectRepository.getPage(block.getFixtureName());
		for(WebPageConfigLine line : block.getBlockLines()) {
			webPage.addElement(
				line.getElementName(),
				line.getType(),
				line.getMethod(),
				line.getLocator(),
				line.getPosition());
		}		
	}

	@Override
	public void setInjector(Injector injector) {
		// TODO Auto-generated method stub
		
	}

}
