package com.synaptix.toast.runtime.core.runtime.block;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

public class ActionCommandDescriptor {
	public Method method;

	public Matcher matcher;

	public ActionCommandDescriptor(
		Method method,
		Matcher matcher) {
		this.method = method;
		this.matcher = matcher;
	}
}
