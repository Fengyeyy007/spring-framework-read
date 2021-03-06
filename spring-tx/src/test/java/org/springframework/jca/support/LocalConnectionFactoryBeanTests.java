/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jca.support;

import org.junit.Test;

import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ManagedConnectionFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.verify;

/**
 * Unit tests for the {@link LocalConnectionFactoryBean} class.
 *
 * @author Rick Evans
 * @author Chris Beams
 */
public class LocalConnectionFactoryBeanTests {

    @Test(expected = IllegalArgumentException.class)
    public void testManagedConnectionFactoryIsRequired() throws Exception {
        new LocalConnectionFactoryBean().afterPropertiesSet();
    }

    @Test
    public void testIsSingleton() throws Exception {
        LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
        assertTrue(factory.isSingleton());
    }

    @Test
    public void testGetObjectTypeIsNullIfConnectionFactoryHasNotBeenConfigured() throws Exception {
        LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
        assertNull(factory.getObjectType());
    }

    @Test
    public void testCreatesVanillaConnectionFactoryIfNoConnectionManagerHasBeenConfigured() throws Exception {
        final Object CONNECTION_FACTORY = new Object();
        ManagedConnectionFactory managedConnectionFactory = mock(ManagedConnectionFactory.class);
        given(managedConnectionFactory.createConnectionFactory()).willReturn(CONNECTION_FACTORY);
        LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
        factory.setManagedConnectionFactory(managedConnectionFactory);
        factory.afterPropertiesSet();
        assertEquals(CONNECTION_FACTORY, factory.getObject());
    }

    @Test
    public void testCreatesManagedConnectionFactoryIfAConnectionManagerHasBeenConfigured() throws Exception {
        ManagedConnectionFactory managedConnectionFactory = mock(ManagedConnectionFactory.class);
        ConnectionManager connectionManager = mock(ConnectionManager.class);
        LocalConnectionFactoryBean factory = new LocalConnectionFactoryBean();
        factory.setManagedConnectionFactory(managedConnectionFactory);
        factory.setConnectionManager(connectionManager);
        factory.afterPropertiesSet();
        verify(managedConnectionFactory).createConnectionFactory(connectionManager);
    }

}
