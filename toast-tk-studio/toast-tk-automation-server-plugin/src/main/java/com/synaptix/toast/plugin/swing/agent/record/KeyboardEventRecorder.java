package com.synaptix.toast.plugin.swing.agent.record;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;

import org.fest.swing.input.InputState;

import com.synaptix.toast.core.record.IEventRecorder;

import io.toast.tk.core.agent.interpret.AWTCapturedEvent;

public class KeyboardEventRecorder extends AbstractEventRecorder {

	public KeyboardEventRecorder(
		InputState state,
		IEventRecorder eventRecorder) {
		super(state, eventRecorder);
	}

	@Override
	public void processEvent(
		AWTEvent event) {
		if(isKeyReleasedEvent(event)) {
			final KeyEvent kEvent = (KeyEvent) event;
			final AWTCapturedEvent captureEvent = buildKeyboardEventCapturedObject(event, kEvent);
			if(isCapturedEventUninteresting(captureEvent)) {
				return;
			}
			appendEventRecord(captureEvent);
		}
	}

	private static boolean isKeyReleasedEvent(
		final AWTEvent event) {
		return event.getID() == KeyEvent.KEY_RELEASED;
	}

	private AWTCapturedEvent buildKeyboardEventCapturedObject(
		final AWTEvent event,
		final KeyEvent kEvent) {
		final AWTCapturedEvent captureEvent = new AWTCapturedEvent();
		captureEvent.businessValue = getEventValue(event);
		captureEvent.componentLocator = getEventComponentLocator(event);
		captureEvent.componentName = getEventComponentLabel(event);
		captureEvent.container = getEventComponentContainer(event);
		captureEvent.componentType = kEvent.getComponent().getClass().getName();
		captureEvent.eventLabel = event.getClass().getSimpleName();
		captureEvent.timeStamp = System.nanoTime();
		return captureEvent;
	}

	@Override
	public long getEventMask() {
		return AWTEvent.KEY_EVENT_MASK;
	}
}