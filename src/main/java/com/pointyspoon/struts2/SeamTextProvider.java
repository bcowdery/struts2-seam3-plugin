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

package com.pointyspoon.struts2;

import com.pointyspoon.struts2.annotations.DefaultTextProvider;
import com.pointyspoon.struts2.annotations.DefaultMessageBundle;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.Unchainable;
import com.opensymphony.xwork2.util.ValueStack;
import org.jboss.seam.international.status.MessageFactory;
import org.jboss.seam.international.status.builder.BundleKey;
import org.jboss.seam.international.status.builder.BundleTemplateMessage;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Implementation of the Struts 2 TextProvider that uses the Seam International CDI module
 * to provide messages.
 *
 * @author Brian Cowdery
 * @since  28-Sept-2012
 */
@Named
@RequestScoped
@DefaultTextProvider
public class SeamTextProvider implements TextProvider, Serializable, Unchainable {

    @Inject MessageFactory factory;
    @Inject @DefaultMessageBundle String messagesBundleClasspath;

    public SeamTextProvider() {
    }

    public boolean hasKey(String key) {
        return false;
    }

    public String getText(String key) {
        BundleTemplateMessage template = getInfoBundleTemplate(key);
        return template.build().getText();
    }

    public String getText(String key, String defaultValue) {
        BundleTemplateMessage template = getInfoBundleTemplate(key);
        template.defaults(defaultValue);
        return template.build().getText();
    }

    public String getText(String key, String defaultValue, String obj) {
        BundleTemplateMessage template = getInfoBundleTemplate(key, obj);
        template.defaults(defaultValue);
        return template.build().getText();
    }

    public String getText(String key, List<?> args) {
        BundleTemplateMessage template = getInfoBundleTemplate(key, args);
        return template.build().getText();
    }

    public String getText(String key, String[] args) {
        BundleTemplateMessage template = getInfoBundleTemplate(key, (Object) args);
        return template.build().getText();
    }

    public String getText(String key, String defaultValue, List<?> args) {
        BundleTemplateMessage template = getInfoBundleTemplate(key, args);
        template.defaults(defaultValue);
        return template.build().getText();
    }

    public String getText(String key, String defaultValue, String[] args) {
        BundleTemplateMessage template = getInfoBundleTemplate(key, (Object) args);
        template.defaults(defaultValue);
        return template.build().getText();
    }

    public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
        // not using the stack values here
        return getText(key, defaultValue, args);
    }

    public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
        // not using the stack values here
        return getText(key, defaultValue, args);
    }

    public ResourceBundle getTexts(String bundleName) {
        throw new IllegalArgumentException("No access to resource bundle through Seam International CDI Module.");
    }

    public ResourceBundle getTexts() {
        throw new IllegalArgumentException("No access to resource bundle through Seam International CDI Module.");
    }

    protected BundleTemplateMessage getInfoBundleTemplate(String key, Object... values) {
        BundleTemplateMessage template = factory.info(new BundleKey(messagesBundleClasspath, key), values);
        return template;
    }
}
