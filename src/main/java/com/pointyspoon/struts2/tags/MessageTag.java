/*
 * Doula Company Client Manager
 * Copyright (c) 2012 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 */

package com.pointyspoon.struts2.tags;

import com.pointyspoon.struts2.components.Message;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MessageTag extends AbstractUITag {

    private boolean escape = true;

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Message(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();

        Message error = (Message) component;
        error.setEscape(escape);
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
    }
}
