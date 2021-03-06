/*
 * Copyright (c) 2004 by Cosylab
 *
 * The full license specifying the redistribution, modification, usage and other
 * rights and obligations is included with the distribution of this project in
 * the file "LICENSE-CAJ". If the license is not included visit Cosylab web site,
 * <http://www.cosylab.com>.
 *
 * THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND, NOT EVEN THE
 * IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR OF THIS SOFTWARE, ASSUMES
 * _NO_ RESPONSIBILITY FOR ANY CONSEQUENCE RESULTING FROM THE USE, MODIFICATION,
 * OR REDISTRIBUTION OF THIS SOFTWARE.
 */

package com.cosylab.epics.caj.impl.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.cosylab.epics.caj.cas.util.DefaultServerImpl;
import com.cosylab.epics.caj.cas.util.MemoryProcessVariable;

import gov.aps.jca.CAException;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.cas.ServerContext;
import gov.aps.jca.dbr.DBR_Double;
import gov.aps.jca.dbr.DBR_Enum;

/**
 * All tests.
 * 
 * @author <a href="mailto:matej.sekoranjaATcosylab.com">Matej Sekoranja</a>
 * @version $id$
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
    CATransportIT.class, 
    CATransportExtendedHeaderIT.class})
public class AllTests {

    /**
     *
     */
    @BeforeClass
    public static void setup() {
        try {
            initialize();
        } catch (CAException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cleanup
     */
    @AfterClass
    public static void cleanup() {
        destroy();
    }

    /**
     * JCA server context.
     */
    private static ServerContext context = null;

    /**
     * Initialize JCA context.
     * 
     * @throws CAException
     *             throws on any failure.
     */
    private static void initialize() throws CAException {

        // Get the JCALibrary instance.
        JCALibrary jca = JCALibrary.getInstance();

        // Create server implmentation
        DefaultServerImpl server = new DefaultServerImpl();

        // Create a context with default configuration values.
        context = jca.createServerContext(JCALibrary.CHANNEL_ACCESS_SERVER_JAVA, server);

        // Display basic information about the context.
        System.out.println(context.getVersion().getVersionString());
        context.printInfo();
        System.out.println();

        // register process variables
        registerProcessVariables(server);
    }

    /**
     * Register process variables.
     * 
     * @param server
     */
    private static void registerProcessVariables(DefaultServerImpl server) {

        // PV supporting all GR/CTRL info
        MemoryProcessVariable mpv = new MemoryProcessVariable("record1", null, DBR_Double.TYPE, new double[] { 1.23 });

        mpv.setUpperWarningLimit(new Double(3));
        mpv.setLowerWarningLimit(new Double(-3));

        mpv.setUpperAlarmLimit(new Double(5));
        mpv.setLowerAlarmLimit(new Double(-5));

        mpv.setUpperCtrlLimit(new Double(100));
        mpv.setLowerCtrlLimit(new Double(-100));

        mpv.setUnits("units");
        mpv.setPrecision((short) 2);

        server.registerProcessVaribale(mpv);

        // enum in-memory PV
        MemoryProcessVariable enumPV = new MemoryProcessVariable("enum", null, DBR_Enum.TYPE, new short[] { 0 }) {
            private final String[] labels = { "zero", "one", "two", "three", "four", "five", "six", "seven" };

            public String[] getEnumLabels() {
                return labels;
            }

        };
        server.registerProcessVaribale(enumPV);

    }

    /**
     * Destroy JCA server context.
     */
    private static void destroy() {

        try {

            // Destroy the context, check if never initialized.
            if (context != null)
                context.destroy();

        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

}
