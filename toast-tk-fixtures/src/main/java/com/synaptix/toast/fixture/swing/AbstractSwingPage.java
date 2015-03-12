package com.synaptix.toast.fixture.swing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.synaptix.toast.core.AutoSwingType;
import com.synaptix.toast.core.IFeedableSwingPage;
import com.synaptix.toast.core.ISwingElement;
import com.synaptix.toast.fixture.facade.ClientDriver;

/**
 * 
 * Page fixture abstraction, initializes fixture elements's locators based on wiki definitions
 * 
 * @author skokaina
 * 
 */
public abstract class AbstractSwingPage implements IFeedableSwingPage {

	public String beanClassName; // the bean class name
	Map<String, ISwingElement> elements = new HashMap<String, ISwingElement>();
	protected Map<String, SwingAutoElement> autoElements = new HashMap<String, SwingAutoElement>();
	private String pageName;

	/**
	 * 
	 * @param elementDefinition
	 */
	@Override
	public void initElement(ISwingElement e) {
		initElement(e.getName(), e.getType().name(), e.getLocator());
	}

	/**
	 * Done for each page element based on a feeder
	 * 
	 * @param name
	 * @param method
	 * @param locator
	 * @param position
	 */
	protected void initElement(String name, String type,  String locator) {
		/**
		 * used to locate an element
		 */
		DefaultSwingElement defaultWebElement = new DefaultSwingElement(name, AutoSwingType.valueOf(type), locator);
		elements.put(name, defaultWebElement);

		/**
		 * selenium wrapper field initalizarion when it comes to greenpepper
		 */
		try {
			ISwingElement iWebElement = elements.get(name);
			if (iWebElement != null) {
				SwingAutoElement execAutoClass = ElementFactory.getElement(iWebElement);
				// for this abstract page, init fields (for java classes only
				for (Field f : this.getClass().getFields()) {
					Class<?> automationClass = f.getType();
					if (SwingAutoElement.class.isAssignableFrom(automationClass)) {
						if (f.getName().equals(name)) {
							try {
								BeanUtils.setProperty(this, name, execAutoClass);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
				autoElements.put(name, execAutoClass);
			} else {
				// throw something
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convenient method to call an element based on the page enclosed fields' enum
	 */
	public ISwingElement getElement(String token) {
		return elements.get(token);
	}

	/**
	 * Convenient method to call an element based on the page enclosed fields' enum
	 */
	public SwingAutoElement getAutoElement(String token) {
		return autoElements.get(token);
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public List<ISwingElement> getLocationElements() {
		return new ArrayList<ISwingElement>(elements.values());
	}

	/**
	 * set the driver that will be used by the automation elements
	 */
	public void setDriver(ClientDriver sDvr) {
		for (SwingAutoElement el : autoElements.values()) {
			el.setFrontEndDriver(sDvr);
		}
	}

}