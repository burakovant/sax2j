// Main.java
// (C) COPYRIGHT METASWITCH NETWORKS 2014
package org.openmobilealliance.arc.sax2j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.openmobilealliance.arc.sax2j.extract.Extractor;
import org.openmobilealliance.arc.sax2j.json.JsonValue;
import org.openmobilealliance.arc.sax2j.json.RenderParams;
import org.openmobilealliance.arc.sax2j.xml.Translator.TranslationMode;
import org.openmobilealliance.arc.sax2j.xml.XmlDocument;
import org.openmobilealliance.arc.sax2j.xml.XmlSchema;

public class Main
{
  private ProgressWriter mProgress = new ConsoleProgressWriter();

  private String[] mArgs;

  private XmlSchema mSchema;

  public static void main(String[] args) throws Exception
  {
    new Main(args).run();
  }

  public Main()
  {
  }
  
  public Main(String[] args)
  {
    mArgs = args;
  }

  public void run() throws Exception
  { 
    if ((mArgs.length == 3) && "--extract".equals(mArgs[0]))
    {
      Extractor lExtractor = new Extractor(mArgs[1], mArgs[2], ".xml.json");
      lExtractor.setProgressWriter(new ConsoleProgressWriter());
      lExtractor.extract();
    }
    else if (mArgs.length >= 2 && !"--extract".equals(mArgs[0]))
    {
      InputStream lSchemaStream = new FileInputStream(new File(mArgs[0]));
      mSchema = new XmlSchema(lSchemaStream);
      mSchema.setProgressWriter(mProgress);
      mSchema.parse();
  
      for (int i = 1; i < mArgs.length; i++)
      {
        InputStream lXmlStream = new FileInputStream(new File(mArgs[1]));
        convert(lXmlStream);
      }
    }
    else
    {
      System.out.println("Usage: sax2j <schema.xsd> <source.xml> ...\n" +
                         "   or  sax2j --extract <source.txt> <targetprefix>\n");
    }
  }

  public void convert(InputStream xiStream) throws Exception
  {
    XmlDocument mDoc = new XmlDocument(mSchema, xiStream);
    mDoc.setProgressWriter(mProgress);
    mDoc.parse();

    JsonValue lJson = mDoc.toJson(TranslationMode.STRUCTURE_AWARE);

    StringBuilder lBuffer = new StringBuilder();
    RenderParams lParams = RenderParams.createPretty();
    lJson.render(lBuffer, lParams);
    lBuffer.append("\n");

    System.out.print(lBuffer.toString());
  }
  
  public String convert(InputStream schemaStream, InputStream xmlStream) throws Exception
  {
    mSchema = new XmlSchema(schemaStream);
    mSchema.setProgressWriter(mProgress);
    mSchema.parse();
      
    XmlDocument mDoc = new XmlDocument(mSchema, xmlStream);
    mDoc.setProgressWriter(mProgress);
    mDoc.parse();

    JsonValue lJson = mDoc.toJson(TranslationMode.STRUCTURE_AWARE);

    StringBuilder lBuffer = new StringBuilder();
    RenderParams lParams = RenderParams.createPretty();
    lJson.render(lBuffer, lParams);
    lBuffer.append("\n");

    return lBuffer.toString();
  }
}
