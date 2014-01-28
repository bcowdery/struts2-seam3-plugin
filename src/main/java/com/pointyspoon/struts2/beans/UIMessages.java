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

package com.pointyspoon.struts2.beans;

import com.pointyspoon.struts2.annotations.DefaultMessageBundle;
import org.jboss.seam.international.status.Message;
import org.jboss.seam.international.status.MessageFactory;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;
import org.jboss.seam.international.status.builder.BundleTemplateMessage;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
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
        template.level(null); // no such Level.SUCCESS, let our MessageTag treat null as success.
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
