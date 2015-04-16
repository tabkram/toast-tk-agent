package com.synaptix.toast.fixture.utils;

import java.awt.Point;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.MouseButton;
import org.fest.swing.core.Robot;

public final class FestRobotInstance {

	private static class FestRobotHolder {
		static final FestRobotInstance INSTANCE = new FestRobotInstance();
	}
	
	public static Robot getRobot() {
		return FestRobotHolder.INSTANCE.rbt;
	}
	
	private final Robot rbt;

	FestRobotInstance() {
		this.rbt = BasicRobot.robotWithCurrentAwtHierarchy();
		rbt.cleanUpWithoutDisposingWindows();
	}
	
	public void doubleClick(final Point where) {
		rbt.click(where, MouseButton.LEFT_BUTTON, 2);
	}
	
	public void rightClick(final Point where) {
		rbt.click(where, MouseButton.RIGHT_BUTTON, 1);
	}
	
	public void leftClick(final Point where) {
		rbt.click(where, MouseButton.LEFT_BUTTON, 1);
	}
}