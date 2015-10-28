package com.synaptix.toast.dao.domain.impl.test.block.line;

import java.util.List;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.synaptix.toast.core.report.TestResult;

@Embedded
public class BlockLine {

	private List<String> cells;

	private TestResult testResult;

	public BlockLine() {
	}

	public BlockLine(
		List<String> cells) {
		this.setCells(cells);
	}

	public List<String> getCells() {
		return cells;
	}

	public void setCells(
		List<String> cells) {
		this.cells = cells;
	}

	public TestResult getTestResult() {
		return testResult;
	}

	public void setTestResult(
		TestResult testResult) {
		this.testResult = testResult;
	}
}
