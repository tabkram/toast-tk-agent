package com.synaptix.toast.swing.agent.web;

import com.synaptix.toast.core.agent.interpret.WebEventRecord;

public interface IAgentServer {

	void sendEvent(WebEventRecord adjustedEvent);

	void close();

}
