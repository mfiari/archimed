/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.adaptaters;

import esgi.archimed.datasources.Datasource;
import esgi.archimed.datasources.SQLDatasource;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mike
 */
public class SQLAdapter implements Adapter {
    
    private final List<SQLDatasource> sources;
    private final String name;
    private final PropertyChangeSupport pcs;
    
    public SQLAdapter () {
        this.sources = new ArrayList<>();
        this.name = "SQL Adapter";
        this.pcs = new PropertyChangeSupport(this);
    }
    
    public SQLAdapter (String name) {
        this.sources = new ArrayList<>();
        this.name = name;
        this.pcs = new PropertyChangeSupport(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Datasource> getDatasources () {
        List<Datasource> datasources = new ArrayList<>();
        for (SQLDatasource sQLDatasource : sources) {
            datasources.add(sQLDatasource);
        }
        return datasources;
    }
    
    @Override
    public boolean addDatasource(Datasource datasource) {
        if (datasource instanceof SQLDatasource) {
            this.sources.add((SQLDatasource)datasource);
            this.pcs.firePropertyChange("addDatasource", this, datasource);
            return true;
        }
        return false;
    }
    
    @Override
    public void removeDatasource (int index) {
        Datasource datasource = this.sources.remove(index);
        this.pcs.firePropertyChange("removeDatasource", this, datasource);
    }
    
    @Override
    public void removeDatasource (Datasource dataSource) {
        if (dataSource instanceof SQLDatasource) {
            this.sources.remove((SQLDatasource)dataSource);
            this.pcs.firePropertyChange("removeDatasource", this, dataSource);
        }
    }
    
    @Override
    public void removeAllDatasource () {
        while (!this.sources.isEmpty()) {
            Datasource datasource = this.sources.remove(0);
            this.pcs.firePropertyChange("removeDatasource", this, datasource);
        }
    }

    @Override
    public void parse(String xpath) {
        /*
        * bdd magasin {
        *    table produits [id:int, nom:string, description:int, image:string, prix:double]
        *    table users [id:int, nom:string, prenom:string]
        *    table stock [id:produit, quantite:int]
        *    table traduction [id:int, fr:string, en:string]
        *}
        * xml {
        *    <magasin>
        *       <produits>
        *          <produit>
        *          </produit>
        *       <produits>
        *    </magasin>
        *}
        * /produits/produit => /produits =  SELECT * FROM produits
        * /produits/produit/nom => /produits/nom = SELECT nom FROM produits
        * /produits/produit[1] => /produits[1] = SELECT * FROM produits LIMIT 1
        * /produits/produit[last()] => /produits[last()] = SELECT * FROM produits WHERE id = (SELECT MAX(id) FROM produits)
        * /produits/*\/nom => /produits/nom = SELECT nom from produits
        * /*\/*\/nom => /*\/nom =  SELECT p.nom, u.nom FROM produits p, users u
        * /produits/produit/nom/text() => /produits/nom/text() SELECT nom from produits
        * //nom => //nom = SELECT p.nom, u.nom FROM produits p, users u
        * /produits/produit/@id => /produits/@id = SELECT id FROM produits
        * /produits/produit[@id='1']/nom => /produits[@id='1']/nom = SELECT nom FROM produits WHERE id = 1
        * /produits/produit[@*] => /produits[@*] = SELECT * FROM produits
        * /produits/produit[not(@*)] => /produits[not(@*)] = 
        * /produits/produit/description[@xml:lang='FR'] => /produits/description
        * 
        * 
        * /table/element[condition]/[@attribut|champs]/
        * 
        * On abandonne la partie element dans nos requête
        *
        * /table[condition]/@attribut|champs[condition]
        */
        String [] noeuds = xpath.split("/");
        if (noeuds.length < 2) {
            System.out.println("Mauvaise requête xpath");
        } else if (noeuds.length == 2) {
            String racine = noeuds[1];
            if (racine.contains("[") && racine.contains("]")) {
                String table = racine.substring(0, racine.indexOf("["));
                String expression = racine.substring(racine.indexOf("["), racine.indexOf("]"));
                String condition = "";
                switch (expression) {
                    case "last()" :
                        break;
                    default :
                        if (expression.contains("=")) {
                            condition = expression;
                            condition = condition.replace("@", "");
                        }
                        break;
                }
                String sql = "SELECT * FROM " + table + " WHERE " + condition;
                System.out.println(sql);
            } else {
                String sql = "SELECT * FROM " + racine;
                System.out.println(sql);
            }
        } else if (noeuds.length == 3) {
            String field = noeuds[2];
            String racine = noeuds[1];
            if (racine.contains("[") && racine.contains("]")) {
                String table = racine.substring(0, racine.indexOf("["));
                String expression = racine.substring(racine.indexOf("[")+1, racine.indexOf("]"));
                String condition = "";
                switch (expression) {
                    case "last()" :
                        break;
                    default :
                        if (expression.contains("=")) {
                            condition = expression;
                            condition = condition.replace("@", "");
                        }
                        break;
                }
                String sql = "SELECT "+field+" FROM " + table + " WHERE " + condition;
                System.out.println(sql);
            } else {
                String sql = "SELECT "+field+" FROM " + racine;
                System.out.println(sql);
            }
        }
    }
    
    @Override
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
 
}