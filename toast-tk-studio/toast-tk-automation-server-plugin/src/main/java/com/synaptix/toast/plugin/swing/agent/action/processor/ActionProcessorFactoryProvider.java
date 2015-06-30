/**Copyright (c) 2013-2015, Synaptix Labs
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Creation date: 29 juin 2015
@author Sallah Kokaina <sallah.kokaina@gmail.com>

 */

package com.synaptix.toast.plugin.swing.agent.action.processor;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JMenu;

import com.synaptix.toast.plugin.swing.agent.action.processor.list.JComboBoxActionProcessorFactory;
import com.synaptix.toast.plugin.swing.agent.action.processor.menu.JMenuActionProcessorFactory;

public class ActionProcessorFactoryProvider {
	static Map<String, ActionProcessorFactory> providers = new HashMap<String, ActionProcessorFactory>();
	
	static{
		providers.put("list", new JComboBoxActionProcessorFactory());
		providers.put("menu", new JMenuActionProcessorFactory());
	}
	
	public static ActionProcessorFactory getFactory(Component component) {
		if(component instanceof JComboBox){
			return providers.get("list");
		}
		if(component instanceof JMenu){
			return providers.get("menu");
		}
		return null;
	}

}