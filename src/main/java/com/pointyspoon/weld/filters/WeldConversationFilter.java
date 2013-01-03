/*
 * Doula Company Client Manager
 * Copyright (c) 2012 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 */

package com.pointyspoon.weld.filters;

import org.jboss.weld.context.ConversationContext;
import org.jboss.weld.context.http.Http;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Adds long-running conversation support to non JSF web applications running in a Weld CDI environment.
 * This filter will activate the conversational context at the start of a request and de-activate the context after.
 *
 * Long-running conversations can be resumed between requests by passing the conversation ID as a
 * request parameter. By default, this filter will look for a request parameter named "cid", but this
 * can be configured using a filter <code>init-param</code>.
 *
 * <code>
 *      <filter>
 *          <filter-name>Seam Conversation Support</filter-name>
 *          <filter-class>com.pointyspoon.weld.filters.WeldConversationFilter</filter-class>
 *          <init-param>
 *              <param-name>conversationIdParam</param-name>
 *              <param-value>cid</param-value>
 *          </init-param>
 *      </filter>
 *
 *      <filter-mapping>
 *          <filter-name>Seam Conversation Support</filter-name>
 *          <url-pattern>/*</url-pattern>
 *      </filter-mapping>
 * </code>
 *
 * @author Brian Cowdery
 * @since 10-Oct-2012
 */
public class WeldConversationFilter implements Filter {

    public static final String CONVERSATION_ID_INIT_PARAM = "conversationIdParam";
    public static final String DEFAULT_CONVERSATION_ID_PARAM = "cid";

    @Inject @Http ConversationContext conversationContext;

    /**
     * Sets the preferred parameter name for conversation ID request parameter. This can be
     * configured by setting the "conversationIdParam" filter init-param. If not set, the
     * filter will look the default "cid" request parameter to identify long-running conversations.
     *
     * @param filterConfig filter configuration
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        String paramName = filterConfig.getInitParameter(CONVERSATION_ID_INIT_PARAM);
        conversationContext.setParameterName(!isBlank(paramName) ? paramName : DEFAULT_CONVERSATION_ID_PARAM);
    }

    public void destroy() {
        /* noop */
    }

    /**
     * Re-activates a long-running conversation when a conversation ID is provided as
     * a request parameter. If there is no active conversation a new transient conversation
     * will be started for this request cycle.
     *
     * @param request request
     * @param response response
     * @param chain next filter in chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        conversationContext.activate(getConversationId(request));
        chain.doFilter(request, response);
        conversationContext.deactivate();
    }

    /**
     * Returns the conversation ID of a long-running conversation if provided in the request
     * parameters. Returns null if there is no active conversation.
     *
     * @param request request
     * @return conversation ID, or null if not found.
     */
    protected String getConversationId(ServletRequest request) {
        String cid = request.getParameter(conversationContext.getParameterName());
        return !isBlank(cid) && cid.matches("^\\d+$") ? cid : null;
    }

    /**
     * Returns true if the string is null, empty "", or contains only whitespace.
     *
     * @param value string to test
     * @return true if empty, false if not.
     */
    private boolean isBlank(String value) {
        return value == null || "".equals(value.trim());
    }
}
