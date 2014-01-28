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

package com.pointyspoon.webbeans.filters;

import org.apache.webbeans.spi.ConversationService;

import javax.servlet.http.HttpServletRequest;

/**
 * Simple conversation service that resolves the conversation ID and session ID from
 * the active HttpServletRequest.
 *
 * @author Brian Cowdery
 * @since 02-01-2013
 */
public class HttpConversationService implements ConversationService {

    private HttpServletRequest request;

    public HttpConversationService(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Returns the conversation ID if provided in the request parameters. Returns null
     * if there is no active conversation.
     *
     * @return conversation ID, or null if not found.
     */
    @Override
    public String getConversationId() {
        String cid = request.getParameter("cid");
        return cid != null && cid.matches("^\\d+$") ? cid : null;
    }

    /**
     * Returns the session ID of the current request. Returns null if there is no
     * session active (which should never happen).
     *
     * @return session ID, null if no session.
     */
    @Override
    public String getConversationSessionId() {
        return request.getSession() != null ? request.getSession().getId() : null;
    }
}
