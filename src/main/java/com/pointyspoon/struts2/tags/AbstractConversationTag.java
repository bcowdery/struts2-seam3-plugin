/*
 * Copyright (c) 2014 "Brian Cowdery"
 *
 * The struts2-seam3-plugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
