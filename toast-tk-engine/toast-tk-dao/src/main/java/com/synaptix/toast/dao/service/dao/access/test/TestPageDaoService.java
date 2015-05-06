package com.synaptix.toast.dao.service.dao.access.test;

import javax.annotation.Nullable;

import com.github.jmkgreen.morphia.query.Query;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import com.synaptix.toast.core.dao.ITestPage;
import com.synaptix.toast.dao.domain.impl.test.TestPage;
import com.synaptix.toast.dao.service.dao.common.AbstractMongoDaoService;
import com.synaptix.toast.dao.service.dao.common.CommonMongoDaoService;
import com.synaptix.toast.dao.service.init.DbStarter;

public class TestPageDaoService extends AbstractMongoDaoService<ITestPage> {

	public interface Factory {
		TestPageDaoService create(@Nullable @Assisted String dbName);
	}

	@Inject
	public TestPageDaoService(DbStarter starter, CommonMongoDaoService cService, @Nullable @Assisted String dbName, @Named("default_db") String default_db) {
		super(ITestPage.class, starter.getDatabaseByName((dbName == null ? default_db : dbName)), cService);
	}

	public ITestPage getByName(String name) {
		Query<ITestPage> query = createQuery();
		query.field("pageName").equals(name);
		return query.get();
	}

	public ITestPage saveAsNewIteration(ITestPage t) {
		t.setId(null);
		save(t);
		return t;
	}
}