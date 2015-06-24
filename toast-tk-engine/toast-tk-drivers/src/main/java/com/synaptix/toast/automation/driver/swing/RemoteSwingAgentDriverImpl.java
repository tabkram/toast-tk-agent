package com.synaptix.toast.automation.driver.swing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.synaptix.toast.core.agent.inspection.CommonIOUtils;
import com.synaptix.toast.core.driver.IRemoteSwingAgentDriver;
import com.synaptix.toast.core.net.request.IIdRequest;
import com.synaptix.toast.core.net.request.InitInspectionRequest;
import com.synaptix.toast.core.net.response.ErrorResponse;
import com.synaptix.toast.core.net.response.ExistsResponse;
import com.synaptix.toast.core.net.response.InitResponse;
import com.synaptix.toast.core.net.response.ValueResponse;
import com.synaptix.toast.core.report.TestResult;
import com.synaptix.toast.core.runtime.ErrorResultReceivedException;
import com.synaptix.toast.core.runtime.ITCPClient;
import com.synaptix.toast.core.runtime.ITCPResponseReceivedHandler;

public class RemoteSwingAgentDriverImpl implements IRemoteSwingAgentDriver {
	
	private static final Logger LOG = LogManager.getLogger(RemoteSwingAgentDriverImpl.class);
	protected final ITCPClient client;
	private static final int RECONNECTION_RATE = 10000;
	private static final int WAIT_TIMEOUT = 30000;
	protected Map<String, Object> existsResponseMap;
	private Map<String, Object> valueResponseMap;
	private final Object VOID_RESULT = new Object();
	protected final String host;
	private boolean started;


	@Inject
	public RemoteSwingAgentDriverImpl(@Named("host") String host) {
		this.client = new KryoTCPClient();
		this.started = false;
		this.existsResponseMap = new HashMap<String, Object>();
		this.valueResponseMap = new HashMap<String, Object>();
		this.host = host;
		initListeners();
	}

	private void initListeners() {
		client.addResponseHandler(new ITCPResponseReceivedHandler(){
			@Override
			public void onResponseReceived(Object object) {
				if (object instanceof ExistsResponse) {
					ExistsResponse response = (ExistsResponse) object;
					existsResponseMap.put(response.id, response.exists);
				} 
				else if (object instanceof ValueResponse) {
					ValueResponse response = (ValueResponse) object;
					valueResponseMap.put(response.getId(), response.value);
				}
				else if (object instanceof ErrorResponse){
					ErrorResponse response = (ErrorResponse) object;
					TestResult testResult = new TestResult(response.getMessage(), response.getScreenshot());
					if(valueResponseMap.keySet().contains(response.getId())){
						valueResponseMap.put(response.getId(), testResult);
					}
					else if (existsResponseMap.keySet().contains(response.getId())){
						existsResponseMap.put(response.getId(), testResult);
					}
					else{
						// notify runner
						LOG.error("Error result received {}", response.getMessage());
					}
				}
				if (object instanceof InitResponse) {
					if(LOG.isDebugEnabled()){
						InitResponse response = (InitResponse) object;
						LOG.debug(response);
					}
				}
				else {
					if (object instanceof IIdRequest) {
						handleResponse((IIdRequest)object);
					}else if(!(object instanceof KeepAlive)){
						LOG.warn(String.format("Unhandled response: %s", object));
					}
				}				
			}
		});
	}

	@Override
	public void start(String host) {
		try {
			client.connect(300000, host, CommonIOUtils.TCP_PORT);
			this.started = true;
		} catch (IOException e) {
			startConnectionLoop();
		}
	}
	
	protected void startConnectionLoop() {
		while (!client.isConnected()) {
			connect();
			try {
				Thread.sleep(RECONNECTION_RATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void connect() {
		try {
			client.reconnect();
			this.started = true;
		} catch (Exception e) {
			LOG.error(String.format("Server unreachable, reattempting to connect in %d !", RECONNECTION_RATE/1000));
		}
	}

	@Override
	public void process(IIdRequest request) {
		checkConnection();
		init();
		if (request.getId() != null) {
			existsResponseMap.put(request.getId(), VOID_RESULT);
		}
		//TODO: block any request with No ID !!
		client.sendRequest(request);
	}

	private void checkConnection() {
		if(!started){
			start(host);
		}
		if (!client.isConnected()) {
			connect();
		}
	}

	/**
	 * to call before any request
	 * 
	 * @return
	 */
	public void init() {
		checkConnection();
		InitInspectionRequest request = new InitInspectionRequest();
		client.sendRequest(request);
	}

	@Override
	public boolean waitForExist(String reqId) throws TimeoutException, ErrorResultReceivedException {
		boolean res = false;
		int countTimeOut = WAIT_TIMEOUT;
		int incOffset = 500;
		if (existsResponseMap.containsKey(reqId)) {
			while (VOID_RESULT.equals(existsResponseMap.get(reqId))) {
				try {
					client.keepAlive();
					Thread.sleep(500);
					countTimeOut = countTimeOut - incOffset;
					if(countTimeOut <= 0){
						valueResponseMap.remove(reqId);
						throw new TimeoutException("No Response received for request: " + reqId + " after " + (WAIT_TIMEOUT/1000) +  "s !");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(existsResponseMap.get(reqId) instanceof TestResult){
				throw new ErrorResultReceivedException((TestResult)existsResponseMap.get(reqId));
			}
			res = (Boolean) existsResponseMap.get(reqId);
			existsResponseMap.remove(reqId);
		}
		return res;

	}

	@Override
	public String processAndWaitForValue(IIdRequest request) throws IllegalAccessException, TimeoutException, ErrorResultReceivedException {
		String res = null;
		final String idRequest = request.getId();
		if (idRequest == null) {
			throw new IllegalAccessException("Request requires an Id to wait for a value.");
		}
		init();
		valueResponseMap.put(idRequest, VOID_RESULT);
		client.sendRequest(request);
		res = waitForValue(request);
		return res;
	}

	private String waitForValue(final IIdRequest request) throws TimeoutException, ErrorResultReceivedException {
		final String idRequest = request.getId();
		String res = null;
		int countTimeOut = WAIT_TIMEOUT;
		int incOffset = 500;
		if (valueResponseMap.containsKey(idRequest)) {
			while (VOID_RESULT.equals(valueResponseMap.get(idRequest))) {
				try {
					client.keepAlive();
					Thread.sleep(incOffset);
					countTimeOut = countTimeOut - incOffset;
					if(countTimeOut <= 0){
						valueResponseMap.remove(idRequest);
						throw new TimeoutException("No Response received for request: " + idRequest + " after " + (WAIT_TIMEOUT/1000) +  "s !");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(valueResponseMap.get(idRequest) instanceof TestResult){
				throw new ErrorResultReceivedException((TestResult)valueResponseMap.get(idRequest));
			}
			res = (String) valueResponseMap.get(idRequest);
			valueResponseMap.remove(idRequest);
		}
		return res;
	}
	
	protected void handleResponse(IIdRequest response){
		//nothing here, check children classes
	}

	@Override
	public void stop() {
		client.close();
	}
}