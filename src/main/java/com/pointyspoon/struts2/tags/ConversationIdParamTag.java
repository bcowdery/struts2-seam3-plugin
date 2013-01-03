/*
 * Doula Company Client Manager
 * Copyright (c) 2012 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 */

package com.pointyspoon.struts2.tags;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Component;
import org.apache.struts2.components.Param;
//import org.jboss.weld.context.ConversationContext;

import javax.enterprise.context.Conversation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tag that adds a parameter containing long-running conversation with the request parameter
 * name expected by the {@link com.pointyspoon.weld.filters.WeldConversationFilter} for re-activation.
 *
 * @author Brian Cowdery
 * @since 11-Oct-2012
 */
public class ConversationIdParamTag extends AbstractConversationTag {

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Param(stack);
    }

    protected void populateParams() {
        super.populateParams();

        Param param = (Param) component;
        param.setName(getConversationIdParam());

        Conversation conversation = getConversation();
        if (conversation != null) {
            param.setValue(conversation.getId());
        }
    }
}
