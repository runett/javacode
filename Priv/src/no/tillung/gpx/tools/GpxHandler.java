/*******************************************************************************
 * Copyright (c) MOBAC developers 
 *  
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 2 of the License, or 
 * (at your option) any later version. 
 *  
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 *  
 * You should have received a copy of the GNU General Public License 
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 ******************************************************************************/
package no.tillung.gpx.tools;

import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream; 
import java.io.OutputStream; 

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException; 
import javax.xml.bind.Marshaller; 
import javax.xml.bind.Unmarshaller; 
import javax.xml.bind.util.JAXBResult; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.transform.Source; 

import no.tillung.gpx.*; 
//import mobac.program.Logging; 
//import mobac.utilities.Utilities; 

import org.w3c.dom.Document; 

public class GpxHandler {

	/*
 public static boolean checkJAXBVersion() { 
  boolean res = Utilities.checkJAXBVersion(); 
  if (!res) 
   JOptionPane.showMessageDialog(null, 
     "Outdated Java Runtime Environment and JAXB version", 
     "Mobile Atlas Creator has detected that your used " 
       + "Java Runtime Environment is too old.\n Please update " 
       + "the Java Runtime Environment to at least \nversion " 
       + "1.6.0_14 and restart Mobile Atlas Creator.", 
     JOptionPane.ERROR_MESSAGE); 
  return res; 
 } 
	 */

	public static GpxType loadGpxFile(File f) throws JAXBException { 
		// Create GPX 1.1 JAXB context 
		JAXBContext context = JAXBContext.newInstance(GpxType.class); 

		Unmarshaller unmarshaller = context.createUnmarshaller(); 
		InputStream is = null; 
		try { 
			is = new FileInputStream(f); 
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			factory.setNamespaceAware(true); 
			DocumentBuilder loader = factory.newDocumentBuilder(); 
			Document document = loader.parse(is); 
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String namespace = document.getDocumentElement().getNamespaceURI(); 
			if ("http://www.topografix.com/GPX/1/1".equals(namespace)) { 
				JAXBElement jaxBElem =  (JAXBElement) unmarshaller.unmarshal(document);
				GpxType gpx = (GpxType) jaxBElem.getValue();
				return gpx;
			} 
			if ("http://www.topografix.com/GPX/1/0".equals(namespace)) { 
				Source xmlSource = new javax.xml.transform.dom.DOMSource(document); 

				//Source xsltSource = new StreamSource(Utilities.loadResourceAsStream("xsl/gpx10to11.xsl")); 
				JAXBResult result = new JAXBResult(unmarshaller); 
				//TransformerFactory transFact = TransformerFactory.newInstance(); 
				//Transformer trans = transFact.newTransformer(xsltSource); 
				//trans.transform(xmlSource, result); 
				return (GpxType) result.getResult(); 
			} 
			throw new JAXBException("Expected GPX 1.0 or GPX1.1 namespace but found \n\"" 
					+ namespace + "\""); 
		} catch (JAXBException e) { 
			throw e; 
		} catch (Exception e) { 
			throw new JAXBException(e); 
		} finally { 
			//Utilities.closeStream(is);
		} 
	} 

	public static void saveGpxFile(GpxType gpx, File f) throws JAXBException { 
		// Create GPX 1.1 JAXB context 
		JAXBContext context = JAXBContext.newInstance(GpxType.class); 

		Marshaller marshaller = context.createMarshaller(); 
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); 
		OutputStream os = null; 
		try { 
			os = new FileOutputStream(f); 
			marshaller.marshal(gpx, os); 
		} catch (FileNotFoundException e) { 
			throw new JAXBException(e); 
		} finally { 
			//Utilities.closeStream(os); 
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	} 

	public static void main(String[] args) { 
		//Logging.configureConsoleLogging(); 
		try { 
			GpxType gpx = loadGpxFile(new File("c:/temp/test-track.gpx")); 
			
			GpxHandler.saveGpxFile(gpx, new File("c:/temp/copy.gpx"));
			
		} catch (JAXBException e) { 
			e.printStackTrace(); 
		} 
	} 
}