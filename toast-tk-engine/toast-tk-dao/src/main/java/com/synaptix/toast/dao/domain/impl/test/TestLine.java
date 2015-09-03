package com.synaptix.toast.dao.domain.impl.test;

import org.joda.time.LocalDateTime;

import com.github.jmkgreen.morphia.annotations.Embedded;
import com.github.jmkgreen.morphia.annotations.Entity;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.dao.domain.api.test.IRunnableTest;

@Entity(value = "test", noClassnameStored = true)
@Embedded
public class TestLine implements IRunnableTest {

	private String test;

	private String expected;

	@Embedded
	private TestResult testResult;

	/**
	 * Test comment
	 */
	private String comment;

	private long startTime = 0;

	private long executionTime = 0;

	public TestLine() {
	}

	public TestLine(
			String test,
			String expected,
			String comment) {
		this.setTest(test);
		this.setExpected(expected);
		this.setComment(comment);
	}

	@Override
	public TestResult getTestResult() {
		return testResult;
	}

	@Override
	public void setTestResult(
			TestResult testResult) {
		this.testResult = testResult;
	}

	public String getTest() {
		return test;
	}

	public void setTest(
			String test) {
		this.test = test != null ? test.trim() : null;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(
			String expected) {
		this.expected = expected != null ? expected.trim() : null;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(
			String comment) {
		this.comment = comment != null ? comment.trim() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synpatix.redpepper.backend.core.IRunnableTest#startExecution()
	 */
	@Override
	public void startExecution() {
		startTime = System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synpatix.redpepper.backend.core.IRunnableTest#stopExecution()
	 */
	@Override
	public void stopExecution() {
		executionTime = System.currentTimeMillis() - startTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synpatix.redpepper.backend.core.IRunnableTest#getExecutionTime()
	 */
	@Override
	public long getExecutionTime() {
		return executionTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.synpatix.redpepper.backend.core.IRunnableTest#getStartDateTime()
	 */
	@Override
	public LocalDateTime getStartDateTime() {
		return new LocalDateTime(startTime);
	}
}