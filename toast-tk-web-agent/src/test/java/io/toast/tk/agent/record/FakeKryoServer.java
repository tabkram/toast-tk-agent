package io.toast.tk.agent.record;

import io.toast.tk.agent.web.IAgentServer;
import io.toast.tk.core.agent.interpret.WebEventRecord;

public class FakeKryoServer implements IAgentServer{

	public WebEventRecord event;
	
	@Override
	public void sendEvent(WebEventRecord adjustedEvent) {
		this.event = adjustedEvent;
	}

	@Override
	public void register(String ApiKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unRegister() {
		// TODO Auto-generated method stub
		
	}


}
