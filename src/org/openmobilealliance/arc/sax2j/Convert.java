package org.openmobilealliance.arc.sax2j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class Convert {

	public static String convert(String schemaContent, String xmlString) throws Exception {
		InputStream schemaStream = new ByteArrayInputStream(schemaContent.getBytes(Charset.defaultCharset()));
		InputStream xmlStream = new ByteArrayInputStream(xmlString.getBytes(Charset.defaultCharset()));

		return (new Main()).convert(schemaStream, xmlStream);
	}
}
