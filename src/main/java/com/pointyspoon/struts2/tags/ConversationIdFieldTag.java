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
import org.apache.struts2.components.Hidden;
//import org.jboss.weld.context.ConversationContext;

import javax.enterprise.context.Conversation;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Tag that renders a hidden field containing the ID of a long-running conversation using the
 * request parameter name expected by the {@link com.pointyspoon.weld.filters.WeldConversationFilter}
 * for re-activation.
 *
 * @author Brian Cowdery
 * @since 11-Oct-2012
 */
public class ConversationIdFieldTag extends AbstractConversationTag {

    @Override
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Hidden(stack, req, res);
    }

    @Override
    protected void populateParams() {
        super.populateParams();

        Hidden hidden = (Hidden) component;
        hidden.setName(getConversationIdParam());

        Conversation conversation = getConversation();
        if (conversation != null) {
            hidden.setValue(conversation.getId());
        }
    }
}
