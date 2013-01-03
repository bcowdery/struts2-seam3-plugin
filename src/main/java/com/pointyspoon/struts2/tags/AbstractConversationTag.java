/*
 * Doula Company Client Manager
 * Copyright (c) 2012 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 */

package com.pointyspoon.struts2.tags;

import com.pointyspoon.struts2.cdi.BeanManagerUtils;
import org.apache.struts2.views.jsp.ComponentTagSupport;
//import org.jboss.weld.context.ConversationContext;

import javax.enterprise.context.Conversation;

/**
 * Provides access to the CDI Conversation object and the Weld ConversationContext for
 * Struts 2 tag libraries.
 *
 * @author Brian Cowdery
 * @since 11-Oct-2012
 */
public abstract class AbstractConversationTag extends ComponentTagSupport {

    private Conversation conversation;

    public String getConversationIdParam() {
        // todo: fetch this value from the configured conversation filter...
        return "cid";
    }

    public Conversation getConversation() {
        if (conversation == null)
            conversation = BeanManagerUtils.getBean(Conversation.class);

        return conversation;
    }
}
