/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.panne;

import esgi.archimed.datasources.Datasource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mike
 */
public class Panne extends Thread {
    
    private final int panneTime;
    private final int panneDuration;
    private int timer;
    private final Datasource datasource;
    public boolean panne;
    
    public Panne (Datasource datasource, int panneTime, int panneDuration) {
        this.datasource = datasource;
        this.panneDuration = panneDuration;
        this.panneTime = panneTime;
        this.timer = 0;
        this.panne = false;
    }
        
    @Override
    public void run() {
        while (true) {
            this.attendre(1000);
            this.timer++;
            if (this.panne) {
                if (this.timer == this.panneDuration) {
                    this.datasource.setAvailable(true);
                    this.panne = false;
                    this.timer = 0;
                }
            } else {
                if (this.timer == this.panneTime) {
                    this.datasource.setAvailable(false);
                    this.panne = true;
                    this.timer = 0;
                }
            }
        }
    }
    
    public void attendre (int milliseconde) {
        try {
            Thread.sleep(milliseconde);
        } catch (InterruptedException ex) {
            Logger.getLogger(Panne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
