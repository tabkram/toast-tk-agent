
package com.synaptix.toast.adapter.swing;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.synaptix.toast.adapter.web.HasClickAction;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.CommandRequest;
import com.synaptix.toast.core.net.request.TableCommandRequest;
import com.synaptix.toast.core.net.request.TableCommandRequestQueryCriteria;
import com.synaptix.toast.core.runtime.ErrorResultReceivedException;
import com.synaptix.toast.core.runtime.ISwingElement;

/**
 * input element
 * 
 * @author skokaina
 * 
 */
public class SwingTableElement extends SwingAutoElement implements HasClickAction {

	public SwingTableElement(ISwingElement element, IRemoteSwingAgentDriver driver) {
		super(element, driver);
	}

	public SwingTableElement(ISwingElement element) {
		super(element);
	}

	
	public String find(List<TableCommandRequestQueryCriteria> criteria) throws IllegalAccessException, TimeoutException, ErrorResultReceivedException {
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = new TableCommandRequest.TableCommandRequestBuilder(requestId)
				.find(criteria)
				.with(wrappedElement.getLocator())
				.ofType(wrappedElement.getType().name()).build();
		return frontEndDriver.processAndWaitForValue(request);
	}
	
	public String find(String lookUpColumn, String lookUpValue, String outputColumn) throws IllegalAccessException, TimeoutException, ErrorResultReceivedException {
		outputColumn = outputColumn == null ? lookUpColumn : outputColumn;
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = new TableCommandRequest.TableCommandRequestBuilder(requestId)
				.find(lookUpColumn, lookUpValue, outputColumn)
				.with(wrappedElement.getLocator())
				.ofType(wrappedElement.getType().name()).build();
		frontEndDriver.process(request);
		return frontEndDriver.processAndWaitForValue(request);
	}

	public String count() throws IllegalAccessException, TimeoutException, ErrorResultReceivedException {
		exists();
		final String requestId = UUID.randomUUID().toString();
		CommandRequest request = new TableCommandRequest.TableCommandRequestBuilder(requestId)
			.count().with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).build();
		frontEndDriver.process(request);
		return frontEndDriver.processAndWaitForValue(request);
	}

	@Override
	public boolean click() throws TimeoutException, ErrorResultReceivedException {
		boolean res = exists();
		frontEndDriver.process(new TableCommandRequest.TableCommandRequestBuilder(null)
		.with(wrappedElement.getLocator())
		.ofType(wrappedElement.getType().name())
		.click().build());
		return res;
	}

	@Override
	public void dbClick() {
		throw new IllegalAccessError("Method not implemented !");
	}

	public String doubleClick(String column, String value) throws TimeoutException, ErrorResultReceivedException {
		exists();
		frontEndDriver.process(new TableCommandRequest.TableCommandRequestBuilder(null)
				.doubleClick(column, value).with(wrappedElement.getLocator())
				.ofType(wrappedElement.getType().name()).build());
		return null;
	}

	public String selectMenu(String menu, String column, String value) throws TimeoutException, ErrorResultReceivedException {
		exists();
		frontEndDriver.process(new TableCommandRequest.TableCommandRequestBuilder(null)
			.selectMenu(menu, column, value).with(wrappedElement.getLocator())
			.ofType(wrappedElement.getType().name()).build());
		return null;
	}


}
