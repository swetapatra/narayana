/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors 
 * as indicated by the @author tags. 
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors. 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 2005-2006,
 * @author JBoss Inc.
 */
package com.hp.mwtests.ts.arjuna.recovery;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.Before;

import com.arjuna.ats.arjuna.common.recoveryPropertyManager;
import com.arjuna.ats.arjuna.recovery.RecoveryDriver;
import com.arjuna.ats.arjuna.recovery.RecoveryManager;

public class RecoveryDriverUnitTest
{
    @Before
    public void enableSocketBasedRecovery()
    {
        recoveryPropertyManager.getRecoveryEnvironmentBean().setRecoveryListener(true);
    }

    @Test
    public void testInvalid () throws Exception
    {
        RecoveryDriver rd = new RecoveryDriver(0, "non-existent-hostname");
        
        try
        {
            rd.asynchronousScan();

            fail("Recovery driver asynchronously calls to a non-existent host:port at 'non-existent-hostname:0'. Failure is expected.");
        }
        catch (final Exception expected)
        {
        }
        
        try
        {
            rd.synchronousScan();

            fail("Recovery driver synchronously calls to a non-existent host:port at 'non-existent-hostname:0'. Failure is expected.");
        }
        catch (final Exception expected)
        {
        }
    }
    
    @Test
    public void testValid () throws Exception
    {
        recoveryPropertyManager.getRecoveryEnvironmentBean().setPeriodicRecoveryPeriod(1);
        recoveryPropertyManager.getRecoveryEnvironmentBean().setRecoveryBackoffPeriod(1);

        RecoveryManager rm = RecoveryManager.manager();       
        
        rm.scan(null);
        
        RecoveryDriver rd = new RecoveryDriver(RecoveryManager.getRecoveryManagerPort(), recoveryPropertyManager.getRecoveryEnvironmentBean().getRecoveryAddress(), 100000);
        
        assertTrue(rd.asynchronousScan());
        assertTrue(rd.synchronousScan());
    }
}
