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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author mike
 */
public class XMLParser {

    private final Mediateur mediateur;
    private Adapter adapter;
    private Datasource datasource;
    private ParentType parentType;
    private DataType dataType;
    private Map<String, String> datas;
    
    private enum ParentType {
        mediateur, adapter, datasource;
    }
    
    private enum DataType {
        xml, sql;
    }

    public XMLParser() {
        this.mediateur = new Mediateur();
        this.datas = new HashMap<>();
    }

    public Mediateur parse(XMLStreamReader reader) {
        try {
            while (reader.hasNext()) {
                int eventType = reader.next();
                switch (eventType) {
                    case XMLEvent.END_ELEMENT:
                        this.endElement(reader);
                        break;
                    case XMLEvent.START_ELEMENT:
                        this.startElement(reader);
                        break;
                }
            }
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.mediateur;
    }

    private void startElement(XMLStreamReader reader) throws XMLStreamException {
        String localName = reader.getLocalName();
        if ("mediateur".equals(localName)) {
            this.parentType = ParentType.mediateur;
        } else if ("adaptateur".equals(localName)) {
            this.parentType = ParentType.adapter;
        } else if ("typeDonnee".equals(localName)) {
            String elementText = reader.getElementText();
            if ("xml".equals(elementText)) {
                this.dataType = DataType.xml;
                if (this.parentType == ParentType.adapter) {
                    this.adapter = new XMLAdapter();
                }
            } else if ("sql".equals(elementText)) {
                this.dataType = DataType.sql;
                if (this.parentType == ParentType.adapter) {
                    this.adapter = new SQLAdapter();
                }
            }
            if (this.parentType == ParentType.datasource) {
                this.datas.put("typeDonnee", elementText);
            }
        } else if ("source".equals(localName)) {
            this.parentType = ParentType.datasource;
            this.datas = new HashMap<>();
        } else if ("url".equals(localName)) {
            this.datas.put("url", reader.getElementText());
        } else if ("host".equals(localName)) {
            this.datas.put("host", reader.getElementText());
        } else if ("port".equals(localName)) {
            this.datas.put("port", reader.getElementText());
        } else if ("login".equals(localName)) {
            this.datas.put("login", reader.getElementText());
        } else if ("password".equals(localName)) {
            this.datas.put("password", reader.getElementText());
        } else if ("database".equals(localName)) {
            this.datas.put("database", reader.getElementText());
        }
    }

    private void endElement(XMLStreamReader reader) {
        if ("source".equals(reader.getLocalName())) {
            if (this.dataType == DataType.xml) {
                this.datasource = new XMLDatasource(this.datas.get("url"));
            } else if (this.dataType == DataType.sql) {
                this.datasource = new SQLDatasource(this.datas.get("host"), Integer.parseInt(this.datas.get("port")), this.datas.get("login"), this.datas.get("password"), this.datas.get("database"));
            }
            this.adapter.addDatasource(this.datasource);
        } else if ("adaptateur".equals(reader.getLocalName())) {
            this.mediateur.addAdapter(this.adapter);
        }
    }

}
