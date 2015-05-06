package com.synaptix.toast.dao.service.dao.access.project;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.synaptix.toast.core.dao.ICampaign;
import com.synaptix.toast.core.dao.ITestPage;
import com.synaptix.toast.dao.domain.impl.report.Campaign;
import com.synaptix.toast.dao.domain.impl.test.TestPage;
import com.synaptix.toast.dao.service.dao.access.test.TestPageDaoService;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class CampaignDaoService extends AbstractMongoDaoService<ICampaign> {

	public interface Factory {
		CampaignDaoService create(@Assisted String dbName);
	}

	TestPageDaoService tService;

	@Inject
	public CampaignDaoService(DbStarter starter, CommonMongoDaoService cService, @Assisted String dbName, @Named("default_db") String default_db, TestPageDaoService.Factory tDaoServiceFactory) {
		super(ICampaign.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
		tService = tDaoServiceFactory.create(dbName);
	}

	public ICampaign getByName(String name) {
		Query<ICampaign> query = createQuery();
		query.field("name").equal(name).order("-iteration");
		return find(query).get();
	}

	public ICampaign saveAsNewIteration(ICampaign c) {
		c.setId(null);
		for (ITestPage t : c.getTestCases()) {
			tService.saveAsNewIteration(t);
		}
		save(c);
		return c;
	}

}