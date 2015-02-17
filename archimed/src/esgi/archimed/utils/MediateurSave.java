/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.utils;

import esgi.archimed.Mediateur;
import esgi.archimed.adaptaters.Adapter;
import esgi.archimed.adaptaters.SQLAdapter;
import esgi.archimed.adaptaters.XMLAdapter;
import esgi.archimed.datasources.Datasource;
import esgi.archimed.datasources.SQLDatasource;
import esgi.archimed.datasources.XMLDatasource;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author mike
 */
public class MediateurSave {
    
    public void Save (Mediateur mediateur, String filename) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element mediation = doc.createElement("mediation");
            Element mediateurElem = doc.createElement("mediateur");
            Element mediateurAdresse = doc.createElement("adresse");
            mediateurAdresse.appendChild(doc.createTextNode(mediateur.getAdresse()));
            Element mediateurLangage = doc.createElement("langageMediation");
            mediateurLangage.appendChild(doc.createTextNode("xpath"));
            mediateurElem.appendChild(mediateurAdresse);
            mediateurElem.appendChild(mediateurLangage);
            for (Adapter adapter : mediateur.getAdapters()) {
                Element adaptateurElem = doc.createElement("adaptateur");
                Element adaptateurTypeDonnee = doc.createElement("typeDonnee");
                Element adaptateurAdresse = doc.createElement("adresse");
                adaptateurAdresse.appendChild(doc.createTextNode(adapter.getAdresse()));
                if (adapter instanceof XMLAdapter) {
                    adaptateurTypeDonnee.appendChild(doc.createTextNode("xml"));
                } else if (adapter instanceof SQLAdapter) {
                    adaptateurTypeDonnee.appendChild(doc.createTextNode("sql"));
                }
                adaptateurElem.appendChild(adaptateurTypeDonnee);
                adaptateurElem.appendChild(adaptateurAdresse);
                for (Datasource datasource : adapter.getDatasources()) {
                    Element datasourceElem = doc.createElement("source");
                    Element datasourceTypeDonnee = doc.createElement("typeDonnee");
                    if (datasource instanceof XMLDatasource) {
                        XMLDatasource xmlDatasource = (XMLDatasource) datasource;
                        datasourceTypeDonnee.appendChild(doc.createTextNode("xml"));
                        Element datasourceUrl = doc.createElement("url");
                        datasourceUrl.appendChild(doc.createTextNode(xmlDatasource.getUrl()));
                        datasourceElem.appendChild(datasourceTypeDonnee);
                        datasourceElem.appendChild(datasourceUrl);
                    } else if (datasource instanceof SQLDatasource) {
                        SQLDatasource sqlDatasource = (SQLDatasource) datasource;
                        datasourceTypeDonnee.appendChild(doc.createTextNode("sql"));
                        Element datasourceHost = doc.createElement("host");
                        datasourceHost.appendChild(doc.createTextNode(sqlDatasource.getHost()));
                        Element datasourcePort = doc.createElement("port");
                        datasourcePort.appendChild(doc.createTextNode(String.valueOf(sqlDatasource.getPort())));
                        Element datasourceLogin = doc.createElement("login");
                        datasourceLogin.appendChild(doc.createTextNode(sqlDatasource.getLogin()));
                        Element datasourcePassword = doc.createElement("password");
                        datasourcePassword.appendChild(doc.createTextNode(sqlDatasource.getPassword()));
                        Element datasourceDatabase = doc.createElement("database");
                        datasourceDatabase.appendChild(doc.createTextNode(sqlDatasource.getDatabase()));
                        datasourceElem.appendChild(datasourceTypeDonnee);
                        datasourceElem.appendChild(datasourceHost);
                        datasourceElem.appendChild(datasourcePort);
                        datasourceElem.appendChild(datasourceLogin);
                        datasourceElem.appendChild(datasourcePassword);
                        datasourceElem.appendChild(datasourceDatabase);
                    }
                    adaptateurElem.appendChild(datasourceElem);
                }
                mediateurElem.appendChild(adaptateurElem);
            }
            mediation.appendChild(mediateurElem);
            doc.appendChild(mediation);

            File fichierXml = new File(filename);
            XMLGenerator generator = new XMLGenerator();
            generator.generate(doc, fichierXml);

            // on vérifie si le fichier est valide avec la dtd
            DTDValidator dTDValidator = new DTDValidator();
            if (dTDValidator.validate("ecole.dtd", "mediation.xml")) {
                System.out.println("Save succes");
            } else {
                System.out.println("Save failed");
            }

        } catch (ParserConfigurationException | DOMException e) { // TODO Auto-generated catch block
            System.out.println("Exception: " + e.getMessage());
        }
    }
    
}
