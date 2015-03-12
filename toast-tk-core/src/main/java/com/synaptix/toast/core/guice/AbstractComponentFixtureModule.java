package com.synaptix.toast.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

public abstract class AbstractComponentFixtureModule extends AbstractModule {

	Multibinder<ICustomFixtureHandler> uriCustomFixtureHandlerBinder;

	Multibinder<FilteredAWTEventListener> uriAwtEventListenerBinder;

	@Override
	protected void configure() {
		this.uriCustomFixtureHandlerBinder = Multibinder.newSetBinder(binder(), ICustomFixtureHandler.class);
		this.uriAwtEventListenerBinder = Multibinder.newSetBinder(binder(), FilteredAWTEventListener.class);
		configureModule();
	}

	protected abstract void configureModule();

	protected final void addCustomFilteredAWTEventListener(Class<? extends FilteredAWTEventListener> customFilteredAWTEventListenerClass) {
		bind(customFilteredAWTEventListenerClass).in(Singleton.class);
		uriAwtEventListenerBinder.addBinding().to(customFilteredAWTEventListenerClass);
	}

	protected final void addTypeHandler(Class<? extends ICustomFixtureHandler> typeHandlerClass) {
		bind(typeHandlerClass).in(Singleton.class);
		uriCustomFixtureHandlerBinder.addBinding().to(typeHandlerClass);
	}
}