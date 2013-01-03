/*
 * Doula Company Client Manager
 * Copyright (c) 2013 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 */

package com.pointyspoon.webbeans.filters;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.context.ConversationContext;
import org.apache.webbeans.conversation.ConversationImpl;
import org.apache.webbeans.conversation.ConversationManager;
import org.apache.webbeans.spi.ContextsService;
import org.apache.webbeans.spi.ConversationService;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * Adds long-running conversation support to non JSF web applications running in an Apache OpenWebBeans CDI
 * environment. This filter will activate the conversational context at the start of a request and de-activate
 * the context after.
 *
 * Long-running conversations can be resumed between requests by passing the conversation ID as a
 * request parameter. By default, this filter will look for a request parameter named "cid", but this
 * can be configured using a filter <code>init-param</code>.
 *
 * <code>
 *      <filter>
 *          <filter-name>Seam Conversation Support</filter-name>
 *          <filter-class>com.pointyspoon.webbeans.filters.WebBeansConversationFilter</filter-class>
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
 * @since 02-01-2013
 */
public class WebBeansConversationFilter implements Filter {

    private static Boolean webBeansApplication = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /* noop */
    }

    @Override
    public void destroy() {
        /* noop */
    }

    /**
     * Start and stops the Open Web Beans conversation context for each request, restoring long-running
     * conversations when the "cid" request parameter is present, otherwise starting a new transient conversation.
     *
     * Non transient conversations are passivated after the filter chain has been executed, while transient
     * conversations are simply discarded.
     *
     * @param request request
     * @param response response
     * @param chain next filter in chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!isWebBeansApplication()) return;

        WebBeansContext webBeansContext = WebBeansContext.currentInstance();
        webBeansContext.registerService(ConversationService.class, new HttpConversationService((HttpServletRequest) request));

        startConversation(webBeansContext);
        chain.doFilter(request, response);
        endConversation(webBeansContext);
    }

    private void startConversation(WebBeansContext webBeansContext) {
        ContextsService contextsService = webBeansContext.getContextsService();
        ConversationManager conversationManager = webBeansContext.getConversationManager();
        Conversation conversation =  conversationManager.getConversationBeanReference();

        if (conversation.isTransient()) {
            contextsService.startContext(ConversationScoped.class, null);

        } else {
            // restore passivated conversation context for this request
            ConversationImpl webBeanConversation = (ConversationImpl) conversation;

            // conversation can only be used by a single thread at a time
            if (!webBeanConversation.getInUsed().compareAndSet(false, true)) {
                contextsService.startContext(ConversationScoped.class, null);

            } else {
                ConversationContext conversationContext = conversationManager.getConversationContext(conversation);
                contextsService.startContext(ConversationScoped.class, conversationContext);
            }
        }
    }

    private void endConversation(WebBeansContext webBeansContext) {
        ContextsService contextsService = webBeansContext.getContextsService();
        ConversationManager conversationManager = webBeansContext.getConversationManager();
        Conversation conversation =  conversationManager.getConversationBeanReference();

        if (conversation.isTransient()) {
            // destroy transient conversation context
            contextsService.endContext(ConversationScoped.class, null);

        } else {
            // passivate conversation context for later restoration
            ConversationImpl webBeanConversation = (ConversationImpl) conversation;
            webBeanConversation.updateTimeOut();
            webBeanConversation.setInUsed(false);
        }
    }

    private boolean isWebBeansApplication() {
        if (webBeansApplication == null) {
            webBeansApplication = WebBeansContext.currentInstance().getBeanManagerImpl().isInUse();
        }
        return webBeansApplication;
    }
}
