package com.synaptix.toast.core;


/**
 * 
 * @author skokaina
 * 
 */
public interface ISwingElement {

	public void setName(String name);

	public String getName();

	public String getLocator();

	public void setLocator(String locator);

	public AutoSwingType getType();

	public void setType(AutoSwingType type);
}