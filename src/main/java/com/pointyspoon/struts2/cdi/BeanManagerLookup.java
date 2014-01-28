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

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Internal class for fetching the CDI BeanManager from the JNDI context.
 *
 * @author Brian Cowdery
 * @since 11-Oct-2012
 */
class BeanManagerLookup {

    /** The key under which the BeanManager can be found according to CDI API docs */
    public static final String CDI_JNDIKEY_BEANMANAGER_COMP = "java:comp/BeanManager";
    /** The key under which the BeanManager can be found according to JBoss Weld docs */
    public static final String CDI_JNDIKEY_BEANMANAGER_APP = "java:app/BeanManager";
	/** The key under which the BeanManager can be found in pure Servlet containers according to JBoss Weld docs. */
	public static final String CDI_JNDIKEY_BEANMANAGER_COMP_ENV = "java:comp/env/BeanManager";

    private BeanManagerLookup() {}

    /**
     * Try to find the CDI BeanManager from the JNDI Context. This method will look in several common locations
     * as outlined in the CDI API and JBoss Weld docs.
     *
   	 * @return the BeanManager, if found. <tt>null</tt> otherwise.
   	 */
   	public static BeanManager findBeanManager() {
   		BeanManager bm = null;
   		try {
   			Context initialContext = new InitialContext();
   			if (bm == null) bm = lookup(initialContext, CDI_JNDIKEY_BEANMANAGER_COMP);
   			if (bm == null) bm = lookup(initialContext, CDI_JNDIKEY_BEANMANAGER_APP);
   			if (bm == null) bm = lookup(initialContext, CDI_JNDIKEY_BEANMANAGER_COMP_ENV);
   		} catch ( NamingException e ) {
   			/* noop */
   		}
   		return bm;
   	}

    private static BeanManager lookup(Context context, String jndiKey) {
   		BeanManager result = null;
   		try {
   			result = (BeanManager) context.lookup(jndiKey);
   		} catch (NamingException e) {
            /* noop */
   		}
   		return result;
   	}

}
