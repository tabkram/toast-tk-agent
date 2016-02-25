package com.synaptix.toast.core.agent.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.google.inject.Provider;
import com.synaptix.toast.constant.Property;
import com.synaptix.toast.core.annotation.craft.FixMe;


@FixMe(todo = "do external default configuration using a setting file")
public class WebConfigProvider implements Provider<WebConfig> {

	private WebConfig webConfig;
	

	public WebConfigProvider() {
		super();
		initConfig();
	}

	private void initConfig() {
		String userHomepath = Config.TOAST_HOME_DIR;
		Properties p = null;
		if(userHomepath != null) {
			p = new Properties();
			try {
				p.load(new FileReader(userHomepath + "toast.web.properties"));
			}
			catch(IOException e) {
			}
		}
		webConfig = new WebConfig();
		String toastWebPropertyDefaultValue = "default value of Web Property/Fixe me";
		webConfig.setToastWebPropertyFixeMe(p.getProperty(Property.TOAST_TEST_WEB_PROPERTY_FILE_FIXE_ME, toastWebPropertyDefaultValue));
	}

	@Override
	public WebConfig get() {
		return webConfig;
	}
}
