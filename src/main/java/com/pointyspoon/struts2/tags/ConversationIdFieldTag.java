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
