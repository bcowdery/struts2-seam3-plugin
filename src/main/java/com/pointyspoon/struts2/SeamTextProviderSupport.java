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
import com.opensymphony.xwork2.LocaleProvider;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.util.ValueStack;
import org.jboss.solder.core.Client;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provides support for a custom TextProvider injected through JEE6 CDI.
 *
 * Note that while Struts 2 "should" provide injection of custom implementation of TextProvider through
 * configuration constants, it appears to be broken as you will only ever get the DefaultTextProvider.
 *
 * This Action implementation injects TextProvider through JEE6 CDI instead of Struts 2's broken
 * dependency injection. {@link SeamTextProvider} is the default implementation that will be injected
 * by default.
 *
 * @author Brian Cowdery
 * @since  28-Sept-2012
 */
public class SeamTextProviderSupport implements TextProvider, LocaleProvider, Serializable {

    @Inject @Client Locale locale;
    @Inject @DefaultTextProvider TextProvider textProvider;

    /* LocaleProvider */

    public Locale getLocale() {
        return locale;
    }

    /* TextProvider */

    public TextProvider getTextProvider() {
        return textProvider;
    }

    public boolean hasKey(String key) {
        return getTextProvider().hasKey(key);
    }

    public String getText(String key) {
        return getTextProvider().getText(key);
    }

    public String getText(String key, String defaultValue) {
        return getTextProvider().getText(key, defaultValue);
    }

    public String getText(String key, String defaultValue, String obj) {
        return getTextProvider().getText(key, defaultValue, obj);
    }

    public String getText(String key, List<?> args) {
        return getTextProvider().getText(key, args);
    }

    public String getText(String key, String[] args) {
        return getTextProvider().getText(key, args);
    }

    public String getText(String key, String defaultValue, List<?> args) {
        return getTextProvider().getText(key, defaultValue, args);
    }

    public String getText(String key, String defaultValue, String[] args) {
        return getTextProvider().getText(key, defaultValue, args);
    }

    public String getText(String key, String defaultValue, List<?> args, ValueStack stack) {
        return getTextProvider().getText(key, defaultValue, args, stack);
    }

    public String getText(String key, String defaultValue, String[] args, ValueStack stack) {
        return getTextProvider().getText(key, defaultValue, args, stack);
    }

    public ResourceBundle getTexts(String bundleName) {
        return getTextProvider().getTexts();
    }

    public ResourceBundle getTexts() {
        return getTextProvider().getTexts();
    }
}
