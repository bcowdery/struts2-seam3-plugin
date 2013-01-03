/*
 * Doula Company Client Manager
 * Copyright (c) 2013 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
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
