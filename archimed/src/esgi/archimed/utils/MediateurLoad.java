/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.utils;

import esgi.archimed.Mediateur;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author mike
 */
public class MediateurLoad {
    
    public Mediateur load (File xmlFile) {
        Mediateur mediateur = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(xmlFile);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(fileInputStream);
            XMLParser parser = new XMLParser();
            mediateur = parser.parse(reader);
        } catch (FileNotFoundException | XMLStreamException ex) {
            Logger.getLogger(MediateurLoad.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mediateur;
    }
    
}
