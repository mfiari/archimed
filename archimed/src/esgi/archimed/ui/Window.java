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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.transform.OutputKeys;
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
        this.setLocationRelativeTo(null);
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
                    case "writeResult":
                        writeResult((Document)evt.getOldValue());
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
                        removeDatasource((Adapter)evt.getOldValue(), (Datasource)evt.getNewValue());
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
        
        private final Adapter adapter;
        private final Datasource datasource;
        private final JPanel panel;
        
        public DatasourceView (Adapter adapter, Datasource datasource, JPanel panel) {
            this.adapter = adapter;
            this.datasource = datasource;
            this.panel = panel;
        }
        
        public Adapter getAdapter () {
            return this.adapter;
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
        labelEtat.setForeground(Color.green);
        panel.add(panelName);
        panel.add(labelEtat);
        this.datasource.add(panel);
        this.datasource.repaint();
        this.datasource.validate();
        this.datasourceViews.add(new DatasourceView(adapter, datasource, panel));
    }
    
    private void removeDatasource(Adapter adapter, Datasource datasource) {
        for (DatasourceView datasourceView : this.datasourceViews) {
            if (datasourceView.getAdapter().equals(adapter) && datasourceView.getDatasource().equals(datasource)) {
                this.datasource.remove(datasourceView.getPanel());
                this.datasource.repaint();
                this.datasource.validate();
                this.datasourceViews.remove(datasourceView);
                break;
            }
        }
    }
    
    private void writeResult (Document document) {
        this.serealize(document);
    }
    
    public void serealize (Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer tr = null;
        try {
            tr = tf.newTransformer();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
        DOMSource in = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        //StreamResult out = new StreamResult(System.out);
        StreamResult out = new StreamResult(writer);
        try {
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.transform(in, out);
            String output = writer.getBuffer().toString();
            this.jTextArea1.setText(output);
        } catch (TransformerException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
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
        textFieldRequest = new javax.swing.JTextField();
        buttonOk = new javax.swing.JButton();
        labelMediateur = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemOpen = new javax.swing.JMenuItem();
        menuItemSave = new javax.swing.JMenuItem();
        menuItemExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuItemAddAdapter = new javax.swing.JMenuItem();
        menuItemDeleteAdapter = new javax.swing.JMenuItem();
        menuItemAddDatasource = new javax.swing.JMenuItem();
        menuItemDeleteDatasource = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Archimed");

        adapterPanel.setOpaque(false);

        textFieldRequest.setText("Request");

        buttonOk.setText("OK");
        buttonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOkActionPerformed(evt);
            }
        });

        labelMediateur.setText("Médiateur xpath");

        javax.swing.GroupLayout mediateurPanelLayout = new javax.swing.GroupLayout(mediateurPanel);
        mediateurPanel.setLayout(mediateurPanelLayout);
        mediateurPanelLayout.setHorizontalGroup(
            mediateurPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mediateurPanelLayout.createSequentialGroup()
                .addGroup(mediateurPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mediateurPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(textFieldRequest))
                    .addGroup(mediateurPanelLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(buttonOk)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(mediateurPanelLayout.createSequentialGroup()
                .addGap(322, 322, 322)
                .addComponent(labelMediateur)
                .addContainerGap(332, Short.MAX_VALUE))
        );
        mediateurPanelLayout.setVerticalGroup(
            mediateurPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mediateurPanelLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(labelMediateur)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textFieldRequest, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonOk)
                .addGap(8, 8, 8))
        );

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        menuFile.setText("File");

        menuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuItemOpen.setText("Open");
        menuFile.add(menuItemOpen);

        menuItemSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuItemSave.setText("Save");
        menuFile.add(menuItemSave);

        menuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        menuItemExit.setText("Exit");
        menuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemExitActionPerformed(evt);
            }
        });
        menuFile.add(menuItemExit);

        jMenuBar1.add(menuFile);

        jMenu2.setText("Edit");

        menuItemAddAdapter.setText("Add adaptater");
        menuItemAddAdapter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddAdapterActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemAddAdapter);

        menuItemDeleteAdapter.setText("Delete adaptater");
        menuItemDeleteAdapter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDeleteAdapterActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemDeleteAdapter);

        menuItemAddDatasource.setText("Add datasource");
        menuItemAddDatasource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAddDatasourceActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemAddDatasource);

        menuItemDeleteDatasource.setText("Delete datasource");
        menuItemDeleteDatasource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDeleteDatasourceActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemDeleteDatasource);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourcePanel)
            .addComponent(adapterPanel)
            .addComponent(mediateurPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
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
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemAddAdapterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddAdapterActionPerformed
        new CreateAdapter(this, this.mediateur).setVisible(true);
    }//GEN-LAST:event_menuItemAddAdapterActionPerformed

    private void menuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemExitActionPerformed
        this.processWindowEvent( new WindowEvent( this, WindowEvent.WINDOW_CLOSING) );
    }//GEN-LAST:event_menuItemExitActionPerformed

    private void menuItemDeleteAdapterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDeleteAdapterActionPerformed
        new RemoveAdapter(this, this.mediateur).setVisible(true);
    }//GEN-LAST:event_menuItemDeleteAdapterActionPerformed

    private void menuItemAddDatasourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAddDatasourceActionPerformed
        new CreateDatasource(this, this.mediateur).setVisible(true);
    }//GEN-LAST:event_menuItemAddDatasourceActionPerformed

    private void menuItemDeleteDatasourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDeleteDatasourceActionPerformed
        new RemoveDatasource(this, this.mediateur).setVisible(true);
    }//GEN-LAST:event_menuItemDeleteDatasourceActionPerformed

    private void buttonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOkActionPerformed
        String request = this.textFieldRequest.getText();
        this.mediateur.runRequest(request);
    }//GEN-LAST:event_buttonOkActionPerformed

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
    private javax.swing.JButton buttonOk;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel labelMediateur;
    private javax.swing.JPanel mediateurPanel;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuItem menuItemAddAdapter;
    private javax.swing.JMenuItem menuItemAddDatasource;
    private javax.swing.JMenuItem menuItemDeleteAdapter;
    private javax.swing.JMenuItem menuItemDeleteDatasource;
    private javax.swing.JMenuItem menuItemExit;
    private javax.swing.JMenuItem menuItemOpen;
    private javax.swing.JMenuItem menuItemSave;
    private javax.swing.JScrollPane sourcePanel;
    private javax.swing.JTextField textFieldRequest;
    // End of variables declaration//GEN-END:variables
}
