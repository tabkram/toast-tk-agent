package com.synaptix.toast.automation.driver.selenium;

import com.gargoylesoftware.htmlunit.IncorrectnessListener;

public class SilentIncorrectnessListener implements IncorrectnessListener {

	@Override
	public void notify(
		String message,
		Object origin) {
		// do nuttin' honey!
	}
}
