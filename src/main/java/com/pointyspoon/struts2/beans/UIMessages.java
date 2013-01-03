/*
 * Doula Company Client Manager
 * Copyright (c) 2012 Brian Cowdery
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/.
 */

package com.pointyspoon.struts2.beans;

import com.pointyspoon.struts2.annotations.DefaultMessageBundle;
import org.jboss.seam.international.status.Message;
import org.jboss.seam.international.status.MessageFactory;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;
import org.jboss.seam.international.status.builder.BundleTemplateMessage;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Set;

@Named
@RequestScoped
public class UIMessages implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject MessageFactory factory;
    @Inject Messages messages;
    @Inject @DefaultMessageBundle String messagesBundleClasspath;

    public UIMessages() {
    }

    public Messages getMessages() {
        return messages;
    }

    public Set<Message> getAllMessages() {
        return messages.getAll();
    }

    public void success(String messageKey, Object... args) {
        BundleTemplateMessage template = factory.info(new BundleKey(messagesBundleClasspath, messageKey), args);
        template.level(null); // no such Level.SUCCESS, the MessageTag treats null as success.
        messages.add(template);
    }

    public void info(String messageKey, Object... args) {
        messages.info(new BundleKey(messagesBundleClasspath, messageKey), args);
    }

    public void warn(String messageKey, Object... args) {
        messages.warn(new BundleKey(messagesBundleClasspath, messageKey), args);
    }

    public void error(String messageKey, Object... args) {
        messages.error(new BundleKey(messagesBundleClasspath, messageKey), args);
    }

    public void fatal(String messageKey, Object... args) {
        messages.fatal(new BundleKey(messagesBundleClasspath, messageKey), args);
    }
}
