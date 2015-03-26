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

Creation date: 6 févr. 2015
@author Sallah Kokaina <sallah.kokaina@gmail.com>

*/

package com.synaptix.toast.plugin.swing.agent.record;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.fest.swing.input.InputState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synaptix.toast.core.Property;
import com.synaptix.toast.core.guice.FilteredAWTEventListener;
import com.synaptix.toast.core.interpret.EventCapturedObject;
import com.synaptix.toast.core.record.AwtEventProcessor;
import com.synaptix.toast.core.record.IEventRecorder;

public abstract class AbstractEventRecorder implements FilteredAWTEventListener, AwtEventProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractEventRecorder.class);
	
	private InputState state;

	protected IEventRecorder eventRecorder;

	AbstractEventRecorder(InputState state, IEventRecorder eventRecorder){
		this.state = state;
		this.eventRecorder = eventRecorder;
	}

	@Override
	public void eventDispatched(AWTEvent event) {
		processEvent(event);
	}

	protected String getEventComponentLocator(AWTEvent aEvent) {
		String componentLocator = null;
		Component component = ((ComponentEvent) aEvent).getComponent();
		if (component == null) {
			component = state.deepestComponentUnderMousePointer();
		}
		componentLocator = eventRecorder.getComponentLocator(component);
		return componentLocator;
	}

	protected String getEventValue(AWTEvent aEvent) {
		if (aEvent instanceof KeyEvent) {
			KeyEvent event = (KeyEvent) aEvent;
			if (event.getID() == KeyEvent.KEY_RELEASED) {
				return Character.toString(event.getKeyChar());
			}
		} else if (aEvent instanceof MouseEvent) {
			MouseEvent mEvent = (MouseEvent) aEvent;
			if (mEvent.getComponent() instanceof JTextField) {
				return ((JTextField) mEvent.getComponent()).getText();
			} else if (mEvent.getComponent() instanceof JTable) {
				JTable jSyTable = (JTable) mEvent.getComponent();
				int selectedRowIndex = jSyTable.getSelectedRow();
				List<String> criteria = new ArrayList<String>();
				if(jSyTable.getSelectedColumns().length > 0){
					for(int columnIndex: jSyTable.getSelectedColumns()){
						String columnName = jSyTable.getModel().getColumnName(columnIndex);
						Object cellValue = jSyTable.getModel().getValueAt(selectedRowIndex, columnIndex);
						criteria.add(columnName + Property.TABLE_KEY_VALUE_SEPARATOR + cellValue);
					}
					return StringUtils.join(criteria, Property.TABLE_CRITERIA_SEPARATOR);
				}
				return "No Cell Selected";
			}
		} else if (aEvent instanceof FocusEvent) {
			FocusEvent fEvent = (FocusEvent) aEvent;
			if (fEvent.getComponent() instanceof JCheckBox)
				return Boolean.toString(((JCheckBox) fEvent.getComponent()).isSelected());
			if (fEvent.getComponent() instanceof JTextField)
				return ((JTextField) fEvent.getComponent()).getText();
			if (fEvent.getComponent() instanceof JTextComponent) {
				return ((JTextComponent) fEvent.getComponent()).getText();
			}
			if (fEvent.getComponent() instanceof JComboBox) {
				Object selectedItem = ((JComboBox) fEvent.getComponent()).getSelectedItem();
				JList list = new JList(((JComboBox) fEvent.getComponent()).getModel());
				Component listCellRendererComponent = ((JComboBox) fEvent.getComponent()).getRenderer().getListCellRendererComponent(list,
						selectedItem, 0, false, false);
				if (selectedItem != null) {
					String val = "";
					if (listCellRendererComponent instanceof JTextField) {
						val = ((JTextField) listCellRendererComponent).getText();
					} else if (listCellRendererComponent instanceof JLabel) {
						val = ((JLabel) listCellRendererComponent).getText();
					} else if (selectedItem instanceof String) {
						val = selectedItem.toString();
					} else {
						val = "unknowObjectType";
					}
					return val;
				}
			}
		} 
		return null;
	}

	protected String getEventComponentLabel(AWTEvent aEvent) {
		Component component = ((ComponentEvent) aEvent).getComponent();
		if (component == null) {
			component = state.deepestComponentUnderMousePointer();
		}
		return getComponentName(component);
	}

	protected String getComponentName(Component component) {
		if (component instanceof AbstractButton) {
			AbstractButton b = (AbstractButton) component;
			return b.getText();
		} else {
			return component!=null ? component.getName() : null;
		}
	}
	
	protected String getEventComponentContainer(AWTEvent event) {
		Container ancestorOfClass = null;
		String ancestorLocator = null;
		Component component = (Component) event.getSource();
		ancestorOfClass = SwingUtilities.getAncestorOfClass(JDialog.class, component);
		if(ancestorOfClass != null){
			ancestorLocator = ((JDialog)ancestorOfClass).getTitle();
		}
		if(ancestorOfClass == null){
			ancestorOfClass = SwingUtilities.getAncestorOfClass(JLayeredPane.class, component);
			if(ancestorOfClass != null){
				ancestorLocator = ancestorOfClass.getClass().getSimpleName();
			}
		}
		if (ancestorOfClass == null) {
			ancestorOfClass = SwingUtilities.getAncestorOfClass(JTabbedPane.class, component);
			if((ancestorOfClass instanceof JTabbedPane)){
				ancestorLocator = ((JTabbedPane)ancestorOfClass).getTitleAt(((JTabbedPane)ancestorOfClass).getSelectedIndex());
			}
		}
		if (ancestorOfClass == null) {
			ancestorOfClass = SwingUtilities.getAncestorOfClass(JFrame.class, component);
			if((ancestorOfClass instanceof JFrame)){
				ancestorLocator = ((JFrame)ancestorOfClass).getTitle();
			}
		}
		return ancestorLocator;
	}
	

	 protected void appendEventRecord(EventCapturedObject captureEvent) {
		LOG.info("New record event captured: " + ToStringBuilder.reflectionToString(captureEvent, ToStringStyle.SIMPLE_STYLE));
		eventRecorder.appendInfo(captureEvent);
	}

}