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

package com.pointyspoon.struts2.cdi;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;

/**
 * Utilities for working with CDI managed beans and the BeanManager.
 *
 * @author Brian Cowdery
 * @since 11-Oct-2012
 */
public class BeanManagerUtils {

    private static final ThreadLocal<BeanManager> MANAGER = new ThreadLocal<BeanManager>() {
        @Override
        protected BeanManager initialValue() {
            return BeanManagerLookup.findBeanManager();
        }
    };

    private BeanManagerUtils() {}

    /**
     * Returns the underlying CDI BeanManager instance.
     *
     * @return BeanManager instance.
     */
    public static BeanManager getBeanManager() {
        return MANAGER.get();
    }

    /**
     * Retrieves a reference to a CDI managed bean.
     *
     * @param clazz class of type T to lookup from the CDI BeanManager
     * @param classifiers classifier annotations for lookup
     * @param <T> type of bean to lookup from the CDI BeanManager
     * @return bean reference, or null if not found.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz, Annotation... classifiers) {
        BeanManager bm = getBeanManager();
        Bean<T> bean = (Bean<T>) bm.getBeans(clazz, classifiers).iterator().next();
        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        return (T) bm.getReference(bean, clazz, ctx);
    }
}
