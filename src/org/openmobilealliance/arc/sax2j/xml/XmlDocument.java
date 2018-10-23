// XmlDocument.java
// (C) COPYRIGHT METASWITCH NETWORKS 2014
package org.openmobilealliance.arc.sax2j.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openmobilealliance.arc.sax2j.ProgressWriter;
import org.openmobilealliance.arc.sax2j.json.JsonObject;
import org.openmobilealliance.arc.sax2j.json.JsonValue;
import org.openmobilealliance.arc.sax2j.xml.Translator.TranslationMode;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlDocument
{
  /**
   * The progress writer we use.
   */
  private ProgressWriter mProgress = new ProgressWriter.NullProgressWriter();

  /**
   * The schema to parse with.
   */
  private final XmlSchema mSchema;
  
  /**
   * The XmlStream to parse.
   */
  private final InputStream mDocStream;

  /**
   * The parsed DOM document.
   */
  private Document mDoc;
  
  /**
   * Construct an XML document. Use {@link #parse()} to parse it.
   *
   * @param xiSchema
   * @param xiStream
   */
  public XmlDocument(XmlSchema xiSchema, InputStream xiStream)
  {
    mSchema = xiSchema;
    mDocStream = xiStream;
  }
  
  /**
   * Construct an XML document. Use {@link #parse()} to parse it.
   *
   * @param xiSchema
   * @param xiFile
   * @throws FileNotFoundException 
   */
  public XmlDocument(XmlSchema xiSchema, File xiFile) throws FileNotFoundException
  {
    mSchema = xiSchema;
    mDocStream = new FileInputStream(xiFile);
  }

  public void setProgressWriter(ProgressWriter xiProgress)
  {
    mProgress = xiProgress;
  }

  /**
   * Parse the given XML document using the given schema.
   *
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   */
  public void parse()
      throws ParserConfigurationException, SAXException, IOException
  {
    if (mDoc != null)
    {
      throw new RuntimeException("Already parsed");
    }

    // Get a parser.
    DocumentBuilderFactory lFactory = mSchema.getDocumentBuilderFactory();
    DocumentBuilder lBuilder = lFactory.newDocumentBuilder();
    ThrowingErrorHandler lErrors = new ThrowingErrorHandler();
    lErrors.setProgressWriter(mProgress);
    lBuilder.setErrorHandler(lErrors);
    mProgress.log("Parsing " + mDocStream);

    // Parse.
    mDoc = lBuilder.parse(mDocStream);

    // Check we're all hunky-dory.
    if (!mDoc.getDocumentElement().isSupported("psvi", "1.0"))
    {
      throw new RuntimeException("PSVI not supported by document");
    }

    mProgress.log("Successfully parsed " + mDocStream);
  }
  
  /**
   * Convert to JSON following the rules of
   * OMA-TS-REST_NetAPI_Common-V1_0-20140604-D.doc.
   *
   * @param xiMode the translation mode to use
   * @return the JSON object.
   */
  public JsonValue toJson(TranslationMode xiMode)
  {
    mProgress.log("Translating " + mDocStream + " to " + xiMode + " JSON");
    JsonObject lObject = JsonObject.create();
    Translator.toJson(xiMode, lObject, mDoc.getDocumentElement());
    mProgress.log("Successfully translated " + mDocStream);
    return lObject;
  }
}
