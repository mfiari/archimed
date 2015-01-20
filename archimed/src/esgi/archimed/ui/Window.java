/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.ui;

import esgi.archimed.Mediateur;
import esgi.archimed.adaptaters.Adapter;
import esgi.archimed.datasources.Datasource;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mike
 */
public class Window extends javax.swing.JFrame {
    
    private final Mediateur mediateur;
    private final Color[] colors = {Color.BLUE, Color.GREEN, Color.ORANGE, Color.RED, Color.YELLOW};
    private final JPanel adapter;
    private final JPanel datasource;
    private final List<AdapterView> adapterViews;
    private final List<DatasourceView> datasourceViews;

    /**
     * Creates new form Window
     */
    public Window() {
        initComponents();
        this.adapterViews = new ArrayList<>();
        this.datasourceViews = new ArrayList<>();
        this.adapter = new JPanel();
        this.datasource = new JPanel();
        this.adapterPanel.setViewportView(this.adapter);
        this.sourcePanel.setViewportView(this.datasource);
        this.mediateur = new Mediateur();
        this.mediateur.addListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()) {
                    case "addAdapter":
                        addAdapter((Adapter)evt.getOldValue());
                        break;
                    case "removeAdapter":
                        removeAdapter((Adapter)evt.getOldValue());
                        break;
                }
            }
        });
    }
    
    private void addAdapterListener (Adapter adapter) {
        adapter.addListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()) {
                    case "addDatasource":
                        addDatasource((Adapter)evt.getOldValue(), (Datasource)evt.getNewValue());
                        break;
                    case "removeDatasource":
                        removeDatasource((Datasource)evt.getOldValue());
                        break;
                }
            }
        });
    }
    
    private class AdapterView {
        
        private final Adapter adapter;
        private final JPanel panel;
        private final Color color;
        
        public AdapterView (Adapter adapter, JPanel panel, Color color) {
            this.adapter = adapter;
            this.panel = panel;
            this.color = color;
        }
        
        public Adapter getAdapter () {
            return this.adapter;
        }
        
        public JPanel getPanel () {
            return this.panel;
        }
        
        public Color getColor () {
            return this.color;
        }
        
    }
    
    private class DatasourceView {
        
        private final Datasource datasource;
        private final JPanel panel;
        
        public DatasourceView (Datasource datasource, JPanel panel) {
            this.datasource = datasource;
            this.panel = panel;
        }
        
        public Datasource getDatasource () {
            return this.datasource;
        }
        
        public JPanel getPanel () {
            return this.panel;
        }
        
    }
    
    private void addAdapter(Adapter adapter) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel panelName = new JPanel ();
        JLabel labelName = new JLabel(adapter.getName());
        panelName.add(labelName);
        int indice = 0;
        if (!this.adapterViews.isEmpty()) {
            Color color = this.adapterViews.get(this.adapterViews.size()-1).getColor();
            for (int i = 0 ; i < this.colors.length ; i++) {
                if (this.colors[i].equals(color)) {
                    if (i < this.colors.length -1) {
                        indice = i+1;
                        break;
                    }
                }
            }
        }
        panelName.setBackground(this.colors[indice]);
        JLabel labelEtat = new JLabel("actif");
        labelEtat.setForeground(Color.GREEN);
        panel.add(panelName);
        panel.add(labelEtat);
        this.adapter.add(panel);
        this.adapter.repaint();
        this.adapter.validate();
        this.adapterViews.add(new AdapterView(adapter, panel, this.colors[indice]));
        this.addAdapterListener(adapter);
    }
    
    private void removeAdapter(Adapter adapter) {
        for (AdapterView adapterView : this.adapterViews) {
            if (adapterView.getAdapter().equals(adapter)) {
                this.adapter.remove(adapterView.getPanel());
                this.adapter.repaint();
                this.adapter.validate();
                this.adapterViews.remove(adapterView);
                break;
            }
        }
    }
    
    private void addDatasource(Adapter adapter, Datasource datasource) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel panelName = new JPanel ();
        JLabel labelName = new JLabel(datasource.getName());
        panelName.add(labelName);
        Color color = Color.BLACK;
        for (AdapterView adapterView : this.adapterViews) {
            if (adapterView.getAdapter().equals(adapter)) {
                color = adapterView.getColor();
                break;
            }
        }
        panelName.setBackground(color);
        JLabel labelEtat = new JLabel("actif");
        labelEtat.setForeground(Color.GREEN);
        panel.add(panelName);
        panel.add(labelEtat);
        this.datasource.add(panel);
        this.datasource.repaint();
        this.datasource.validate();
        this.datasourceViews.add(new DatasourceView(datasource, panel));
    }
    
    private void removeDatasource(Datasource datasource) {
        for (DatasourceView qatasourceView : this.datasourceViews) {
            if (qatasourceView.getDatasource().equals(datasource)) {
                this.datasource.remove(qatasourceView.getPanel());
                this.datasource.repaint();
                this.datasource.validate();
                this.datasourceViews.remove(qatasourceView);
                break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sourcePanel = new javax.swing.JScrollPane();
        adapterPanel = new javax.swing.JScrollPane();
        mediateurPanel = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Archimed");

        adapterPanel.setOpaque(false);

        jTextField1.setText("Request");

        jButton1.setText("OK");

        javax.swing.GroupLayout mediateurPanelLayout = new javax.swing.GroupLayout(mediateurPanel);
        mediateurPanel.setLayout(mediateurPanelLayout);
        mediateurPanelLayout.setHorizontalGroup(
            mediateurPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mediateurPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1)
                .addContainerGap())
            .addGroup(mediateurPanelLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jButton1)
                .addContainerGap(556, Short.MAX_VALUE))
        );
        mediateurPanelLayout.setVerticalGroup(
            mediateurPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mediateurPanelLayout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(8, 8, 8))
        );

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Open");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Save");
        jMenu1.add(jMenuItem2);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        jMenuItem3.setText("Exit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem4.setText("Add adaptater");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Delete adaptater");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("Add datasource");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setText("Delete datasource");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourcePanel)
            .addComponent(adapterPanel)
            .addComponent(mediateurPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(sourcePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(adapterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(mediateurPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        new CreateAdapter(this, this.mediateur).setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        this.processWindowEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSING) );
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new RemoveAdapter(this, this.mediateur).setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        new CreateDatasource(this, this.mediateur).setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        RemoveDatasource removeDatasource = new RemoveDatasource(this, true);
        removeDatasource.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane adapterPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel mediateurPanel;
    private javax.swing.JScrollPane sourcePanel;
    // End of variables declaration//GEN-END:variables
}
