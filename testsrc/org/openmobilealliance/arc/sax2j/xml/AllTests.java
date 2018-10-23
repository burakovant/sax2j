package org.openmobilealliance.arc.sax2j.xml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ InvalidSchemasTest.class, InvalidXmlTest.class, ParseSanityTest.class, SimpleTest.class,
		ValidSchemasTest.class, ValidXmlTest.class })
public class AllTests {

}
