/*
 * Doula Company Client Manager
 * Copyright (c) 2012 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 */

package com.pointyspoon.struts2.components;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.jboss.seam.international.status.Messages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@StrutsTag(name="message", tldBodyContent="empty", tldTagClass="com.pointyspoon.struts2.tags.MessageTag", description="Render Seam UI messages if they exist.")
public class Message extends UIBean {

    public static final String TEMPLATE = "message";
    private boolean escape = true;

    public Message(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    protected void evaluateExtraParams() {
        Messages messages = (Messages) findValue("messages");
        addParameter("allMessages", messages.getAll());
        addParameter("escape", escape);
    }

    @StrutsTagAttribute(description=" Whether to escape HTML", type="Boolean", defaultValue="true")
    public void setEscape(boolean escape) {
        this.escape = escape;
    }
}
