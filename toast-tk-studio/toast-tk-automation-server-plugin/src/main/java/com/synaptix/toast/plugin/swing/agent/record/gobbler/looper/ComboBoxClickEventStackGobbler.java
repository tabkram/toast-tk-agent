package com.synaptix.toast.plugin.swing.agent.record.gobbler.looper;

import javax.swing.JComboBox;

import com.synaptix.toast.plugin.swing.agent.record.gobbler.EventStackGobbler;

import io.toast.tk.core.agent.interpret.AWTCapturedEvent;
import io.toast.tk.core.agent.interpret.IEventInterpreter.EventType;

public class ComboBoxClickEventStackGobbler extends EventStackGobbler {

	AWTCapturedEvent finalEvent = null;

	@Override
	public boolean isInterestedIn(
		AWTCapturedEvent capturedEvent) {
		return isMouseClick(capturedEvent.eventLabel) &&
			isComboBoxType(capturedEvent.componentType);
	}

	public static boolean isComboBoxType(
		String targetType) {
		try {
			Class<?> tClass = Class.forName(targetType);
			boolean isCompliant = JComboBox.class.isAssignableFrom(tClass) || targetType.contains("ComboBox");
			return isCompliant;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public EventType getInterpretedEventType(
		AWTCapturedEvent capturedEvent) {
		return EventType.COMBOBOX_CLICK;
	}

	@Override
	public boolean isLooper() {
		return true;
	}

	@Override
	public EventStackGobbler digest(
		AWTCapturedEvent capturedEvent) {
		if(isFocusLostEvent(capturedEvent.eventLabel)) {
			finalEvent = cloneEvent(capturedEvent);
			String name = finalEvent.componentName;
			finalEvent.componentLocator = capturedEvent.componentLocator;
			finalEvent.componentName = name == null || "null".equals(name) ? finalEvent.componentLocator : name;
			finalEvent.businessValue = capturedEvent.businessValue;
		}
		return this;
	}

	@Override
	public boolean isCompleted() {
		return finalEvent != null;
	}

	@Override
	public AWTCapturedEvent getAdjustedEvent() {
		return finalEvent;
	}
	
	@Override
	public void reset() {
		this.finalEvent = null;
	}
}
