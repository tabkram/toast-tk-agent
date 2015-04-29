/**
 * 
 */
package com.synaptix.toast.dao.domain.impl.test;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.synaptix.toast.core.setup.TestResult;

/**
 * @author E413544
 * 
 */
@Entity(value = "test", noClassnameStored = true)
@Embedded
public class SwingPageConfigLine {

	private String elementName;
	private String type;
	private String locator;
	private TestResult result;

	public TestResult getTestResult() {
		return result;
	}

	public SwingPageConfigLine(String name, String type,  String locator) {
		this.elementName = name;
		this.type = type;
		this.locator = locator;
	}

	public SwingPageConfigLine() {
	}

	/**
	 * @param result
	 */
	public void setResult(TestResult result) {
		this.result = result;
	}

	public String getElementName() {
		return elementName;
	}

	public String getType() {
		return type;
	}


	public String getLocator() {
		return locator;
	}


	public TestResult getResult() {
		return result;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public void setType(String type) {
		this.type = type;
	}


	public void setLocator(String locator) {
		this.locator = locator;
	}


}
