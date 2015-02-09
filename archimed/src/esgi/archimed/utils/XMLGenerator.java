/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.utils;

import java.io.File;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author mike
 */
public class XMLGenerator {
    
    public void generate(Document doc, File file) {
        Source source = new DOMSource(doc);
        /* le résultat de cette transformation sera un flux d'écriture dans un fichier */
        Result resultat = new StreamResult(file);

        /* création du transformateur XML */
        Transformer transfo = null;
        try {
            transfo = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            System.err.println("Impossible de créer un transformateur XML.");
            System.exit(1);
        }

        /* configuration du transformateur */

        /* sortie en XML */
        transfo.setOutputProperty(OutputKeys.METHOD, "xml");

        /* inclut une déclaration XML (recommandé) */
        transfo.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        /* codage des caractères : UTF-8. Ce pourrait être également ISO-8859-1 */
        transfo.setOutputProperty(OutputKeys.ENCODING, "utf-8");

        /* idente le fichier XML */
        transfo.setOutputProperty(OutputKeys.INDENT, "yes");

        try {
            transfo.transform(source, resultat);
        } catch (TransformerException e) {
            System.err.println("La transformation a échoué : " + e);
            System.exit(1);
        }
    }
    
}
