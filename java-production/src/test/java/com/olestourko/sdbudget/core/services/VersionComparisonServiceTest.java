package com.olestourko.sdbudget.core.services;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.olestourko.sdbudget.core.services.VersionComparisonService.Status.*;

/**
 *
 * @author oles
 */
public class VersionComparisonServiceTest {

    VersionComparisonService versionComparisonService;

    public VersionComparisonServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        versionComparisonService = new VersionComparisonService();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of compare method, of class VersionComparisonService.
     */
    @Test
    public void testCompare() {       
        assertEquals(CURRENTVERSION, versionComparisonService.compare("0.1.0", "0.1.0"));
        assertEquals(OLDVERSION, versionComparisonService.compare("0.1.0", "0.2.0"));
        assertEquals(OLDVERSION, versionComparisonService.compare("0.1.0", "0.1.1"));
        assertEquals(OLDVERSION, versionComparisonService.compare("0.1.0", "0.1.0b1"));
        assertEquals(OLDVERSION, versionComparisonService.compare("0.1.0b1", "0.1.0b2"));
        assertEquals(UNRELEASED, versionComparisonService.compare("0.2.0", "0.1.0"));
        assertEquals(UNRELEASED, versionComparisonService.compare("0.2.0", "0.1.1"));
        assertEquals(UNRELEASED, versionComparisonService.compare("0.2.0b1", "0.2.0"));
        assertEquals(UNRELEASED, versionComparisonService.compare("0.2.0b2", "0.2.0b1"));
    }

}
