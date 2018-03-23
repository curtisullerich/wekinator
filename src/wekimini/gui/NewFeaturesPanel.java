/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekimini.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jdesktop.swingworker.SwingWorker;
import wekimini.SupervisedLearningManager;
import wekimini.Wekinator;
import wekimini.kadenze.FeaturnatorLogger;
import wekimini.kadenze.KadenzeLogging;
import wekimini.modifiers.Feature;

/**
 *
 * @author louismccallum
 */
public class NewFeaturesPanel extends javax.swing.JPanel implements WekiTokenFieldDelegate {
    
    private Wekinator w;
    ArrayList<String> selectedFilters = new ArrayList();
    private Feature[] currentResults = new Feature[0];
    public FeatureEditorDelegate delegate;
    private int selectedRow = -1;
    private int outputIndex = 0;
    private boolean ignoreSliderUpdate = true;

    public NewFeaturesPanel() {
        initComponents();
        selectedFiltersTokenField.setDelegate(this);
        selectedFiltersTokenField.repaint();
        resultsTable.setRowSelectionAllowed(false);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setTableHeader(null);
        availableFiltersTable.setTableHeader(null);
        resultsTable.setDefaultRenderer(Feature.class, new FeatureTableRenderer());
        resultsTable.setModel(new FeatureTableModel(currentResults));
        
        boolean showSliders = true;
        windowLabel.setVisible(showSliders);
        bufferLabel.setVisible(false);
        windowSlider.setVisible(showSliders);
        bufferSlider.setVisible(false);
        
        resultsLabel.setVisible(true);
        resultsTable.setVisible(false);
    }

    private void setUpResultsTable() 
    {
        resultsTable.setSelectionBackground(Color.BLUE);
        int tW = resultsTable.getWidth();
        TableColumn column;
        float[] columnWidthPercentage = {0.75f, 0.15f};
        TableColumnModel jTableColumnModel = resultsTable.getColumnModel();
        int cantCols = jTableColumnModel.getColumnCount();
        for (int i = 0; i < cantCols; i++) {
            column = jTableColumnModel.getColumn(i);
            int pWidth = Math.round(tW - 40);
            column.setPreferredWidth(pWidth);
            if(i==1)
            {
                column.setPreferredWidth(40);
                column.setCellRenderer(new ImageTableCellRenderer("add.png"));
            }
        }
    }
    
    @Override
    public void onTokenPressed(String token)
    {
        selectedFilters.remove(token);
        updateFilters();
    }
    
    public void update(Wekinator w, int output)
    {
        this.w = w;
        this.outputIndex = output;
        availableFiltersTable.setModel(new FiltersTableModel(w.getDataManager().featureManager.getFeatureGroups().get(outputIndex).getTags()));
        availableFiltersTable.setDefaultRenderer(String.class, new FiltersTableRenderer());
        MouseListener tableMouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = availableFiltersTable.rowAtPoint(e.getPoint());
                int col = availableFiltersTable.columnAtPoint(e.getPoint());
                String tag = (String)availableFiltersTable.getModel().getValueAt(row, col);
                if(selectedFilters.contains(tag))
                {
                    //System.out.println("Deselected " + tag);
                    selectedFilters.remove(tag);
                }
                else
                {
                    //System.out.println("Selected " + tag);
                    selectedFilters.add(tag);
                }
                updateFilters();
            }
        };
        availableFiltersTable.addMouseListener(tableMouseListener);
        
        MouseListener resultsMouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = resultsTable.rowAtPoint(e.getPoint());
                int column = resultsTable.columnAtPoint(e.getPoint());
                Feature ft = (Feature)resultsTable.getModel().getValueAt(row, 0);
                
                switch(column)
                {
                    case 0: newFeatureSelected(ft); selectRow(row);break;
                    case 1: addFeature(ft); deselectRows(); break;
                }
                
            }
        };
        resultsTable.addMouseListener(resultsMouseListener);
        resultsTable.setDefaultRenderer(Feature.class, new FeatureTableRenderer());
        
        List<Feature> features = w.getDataManager().featureManager.getAllFeaturesGroup().getLibrary();
        Feature[] f = new Feature[features.size()];
        f = features.toArray(f);
        //updateResultsTable(f);
        
        ignoreSliderUpdate = true;
        
        windowSlider.setValue(w.getDataManager().featureManager.getFeatureWindowSize());
        windowLabel.setText("Window:" + windowSlider.getValue());
        bufferSlider.setValue(w.getDataManager().featureManager.getFeatureBufferSize());
        bufferLabel.setText("Buffer:" + bufferSlider.getValue());
        
        ignoreSliderUpdate = false;
    }
    
    private void selectRow(int row)
    {
        selectedRow = row;
        ((FeatureTableModel)resultsTable.getModel()).selectedRow = selectedRow;
        resultsTable.repaint();
    }
    
    public void deselectRows()
    {
        selectedRow = -1;
        ((FeatureTableModel)resultsTable.getModel()).selectedRow = selectedRow;
        resultsTable.repaint();
    }
    
    public void newFeatureSelected(Feature ft)
    {
        delegate.newFeatureSelected(ft);
    }
    
    public void addFeature(Feature ft)
    {
        if(w.getSupervisedLearningManager().getRunningState() == SupervisedLearningManager.RunningState.NOT_RUNNING)
        {
            ((FeaturnatorLogger)KadenzeLogging.getLogger()).logFeatureAdded(w);
            w.getDataManager().featureManager.getFeatureGroups().get(outputIndex).addFeatureForKey(ft.name);
            delegate.featureListUpdated();
            updateResultsTable(currentResults);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Cannot edit features whilst Running");
        }
    }
    
    public void featureListUpdated()
    {
        updateResultsTable(currentResults);
    }
    
    public void searchForFeature(String searchTerm)
    {
        if(!searchTerm.isEmpty())
        {
            SwingWorker worker = new SwingWorker<Feature[] ,Void>()
            {     
                Feature[] f;

                @Override
                public Feature[]  doInBackground()
                {
                    f = w.getDataManager().featureManager.getAllFeaturesGroup().getFeaturesForKeyword(searchTerm);
                    return f;
                }

                @Override
                public void done()
                {
                    ((FeaturnatorLogger)KadenzeLogging.getLogger()).logFeatureSearch(w, searchTerm, f);
                    updateResultsTable(f);
                }
            };
            worker.execute();
        }
    }
    
    public void updateFilters()
    {
        selectedFiltersTokenField.setTokens(selectedFilters);
        availableFiltersTable.repaint();
        SwingWorker worker = new SwingWorker<Feature[] ,Void>()
        {   
            Feature[] f;
            String[] sf;

            @Override
            public Feature[]  doInBackground()
            {
                sf = new String[selectedFilters.size()];
                sf = selectedFilters.toArray(sf);
                if(sf.length > 0)
                {
                    f = w.getDataManager().featureManager.getAllFeaturesGroup().getFeaturesForTags(sf);
                }
                else
                {
                    List<Feature> allFeatures = w.getDataManager().featureManager.getAllFeaturesGroup().getLibrary();
                    f = new Feature[allFeatures.size()];
                    f = allFeatures.toArray(f);
                }
                return f;
            }

            @Override
            public void done()
            {
                if(sf.length > 0)
                {
                    ((FeaturnatorLogger)KadenzeLogging.getLogger()).logFeatureTagSearch(w, sf, f);
                }
                updateResultsTable(f);
            }
        };
        worker.execute();
    }
    
    private Feature[] removeAddedFeatures(Feature[] results)
    {
        Feature[] added = w.getDataManager().featureManager.getFeatureGroups().get(outputIndex).getCurrentFeatures();
        ArrayList<Feature> filteredList = new ArrayList<>();
        for(Feature toShow:results)
        {
            boolean remove = false;
            for(Feature match:added)
            {
                if(match.equals(toShow))
                {
                    remove = true;
                    break;
                }
            }
            if(!remove)
            {
                filteredList.add(toShow);
            }
        }
        Feature[] newResults = new Feature[filteredList.size()];
        return filteredList.toArray(newResults);
    }
    
    private void updateResultsTable(Feature[] results)
    {
        if(results.length == 0)
        {
            resultsLabel.setText("No Features Found Matching Criteria");
            resultsLabel.setVisible(true);
            resultsTable.setVisible(false);
        }
        else
        {
            resultsLabel.setVisible(false);
            resultsTable.setVisible(true);
        }
        
        currentResults = results.clone();
        results = removeAddedFeatures(results);
        resultsTable.setModel(new FeatureTableModel(results));
        setUpResultsTable();
        deselectRows();
    }
    
    class FiltersTableModel extends AbstractTableModel
    {
        private String[] tags;
        
        public FiltersTableModel(String[] tags)
        {
            this.tags = tags;
        }

        @Override
        public int getRowCount() {
            return (int)Math.ceil(tags.length/getColumnCount());
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getValueAt(int rowIndex, int columnIndex) {
            int r = rowIndex * getColumnCount();
            int c = columnIndex % getColumnCount();
            //System.out.println("rowIndex:"+rowIndex+" columnIndex:" + columnIndex + " r:" + r + " c:" + c + " index:" + (r+c));
            return tags[c + r];
        }
        
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }
    
    public class FiltersTableRenderer extends JLabel implements TableCellRenderer 
    {
        FiltersTableRenderer()
        {
            setOpaque(true);
        }

        @Override
        public JLabel getTableCellRendererComponent(
                        JTable table, Object value,
                        boolean isSelected, boolean hasFocus,
                        int row, int column) {
            String tag = (String)value;
            FiltersTableModel model = (FiltersTableModel)table.getModel();
            setBackground(selectedFilters.contains(tag) ? Color.DARK_GRAY  :Color.WHITE);
            setForeground(selectedFilters.contains(tag) ? Color.WHITE : Color.DARK_GRAY);
            setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            setText(tag);
            return this;
        }
    }
    
    private void prepareForLibraryUpdate(boolean isRunning, boolean isPlotting)
    {
        if(isRunning)
        {
            w.getSupervisedLearningManager().setRunningState(SupervisedLearningManager.RunningState.NOT_RUNNING);
        }
        if(isPlotting)
        {
            w.getSupervisedLearningManager().isPlotting = false;
        }
    }
    
    private void resetFollowingLibraryUpdate(boolean isRunning, boolean isPlotting, boolean buffers)
    {
        if(isRunning)
        {
            w.getSupervisedLearningManager().setRunningState(SupervisedLearningManager.RunningState.RUNNING);
        }
        if(isPlotting)
        {
            w.getSupervisedLearningManager().isPlotting = isPlotting;
        }
        updateResultsTable(w.getDataManager().featureManager.getAllFeaturesGroup().getFeaturesForFeatures(currentResults));
        delegate.featureLibraryUpdated(buffers);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        searchBar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        availableFiltersTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        selectedFiltersTokenField = new wekimini.gui.WekiTokenField();
        jLabel3 = new javax.swing.JLabel();
        bufferSlider = new javax.swing.JSlider();
        windowSlider = new javax.swing.JSlider();
        windowLabel = new javax.swing.JLabel();
        bufferLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        resultsLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        resultsTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(204, 204, 204), null, new java.awt.Color(204, 204, 204)));

        searchBar.setText("Search");
        searchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBarActionPerformed(evt);
            }
        });
        searchBar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchBarKeyTyped(evt);
            }
        });

        availableFiltersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        availableFiltersTable.setRowHeight(25);
        availableFiltersTable.setRowMargin(3);
        jScrollPane2.setViewportView(availableFiltersTable);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setText("Search Features By Tag");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setText("Search Results");

        selectedFiltersTokenField.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 2, 10)); // NOI18N
        jLabel3.setText("Select one or more tags to search with");

        bufferSlider.setMajorTickSpacing(5);
        bufferSlider.setMinimum(5);
        bufferSlider.setMinorTickSpacing(1);
        bufferSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bufferSliderStateChanged(evt);
            }
        });

        windowSlider.setMajorTickSpacing(5);
        windowSlider.setMinimum(5);
        windowSlider.setMinorTickSpacing(1);
        windowSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                windowSliderStateChanged(evt);
            }
        });

        windowLabel.setText("Window Size:");

        bufferLabel.setText("Buffer Size:");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setText("Search Features By Keyword");

        resultsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        resultsLabel.setText("<html> Search by keyword or filter by tags <br> to explore features </html> ");
        resultsLabel.setToolTipText("");

        resultsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        resultsTable.setRowHeight(30);
        resultsTable.setRowMargin(3);
        jScrollPane4.setViewportView(resultsTable);

        jLayeredPane1.setLayer(resultsLabel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jScrollPane4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(resultsLabel))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(resultsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(21, 21, 21)))
        );

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setText("Parameters (these apply to all features)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(windowSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(windowLabel)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(bufferLabel)
                                            .addComponent(bufferSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(selectedFiltersTokenField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(searchBar)
                            .addComponent(jLayeredPane1)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectedFiltersTokenField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(windowLabel)
                    .addComponent(bufferLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bufferSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(windowSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchBarActionPerformed

    private void searchBarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchBarKeyTyped
        
        searchForFeature(searchBar.getText());
        
    }//GEN-LAST:event_searchBarKeyTyped

    private void bufferSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bufferSliderStateChanged
        // TODO add your handling code here:
        if(ignoreSliderUpdate)
        {
            return;
        }
        bufferLabel.setText("Buffer:" + bufferSlider.getValue());
        boolean isRunning = w.getSupervisedLearningManager().getRunningState() != SupervisedLearningManager.RunningState.NOT_RUNNING;
        boolean isPlotting = w.getSupervisedLearningManager().isPlotting;
        prepareForLibraryUpdate(isRunning, isPlotting);
        w.getDataManager().featureManager.setFeatureWindowSize(windowSlider.getValue(), bufferSlider.getValue());
        resetFollowingLibraryUpdate(isRunning, isPlotting, true);
    }//GEN-LAST:event_bufferSliderStateChanged

    private void windowSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_windowSliderStateChanged
        // TODO add your handling code here:
        if(ignoreSliderUpdate)
        {
            return;
        }
        windowLabel.setText("Window Size:" + windowSlider.getValue());
        boolean isRunning = w.getSupervisedLearningManager().getRunningState() != SupervisedLearningManager.RunningState.NOT_RUNNING;
        boolean isPlotting = w.getSupervisedLearningManager().isPlotting;
        prepareForLibraryUpdate(isRunning, isPlotting);
        w.getDataManager().featureManager.setFeatureWindowSize(windowSlider.getValue(), bufferSlider.getValue());
        resetFollowingLibraryUpdate(isRunning, isPlotting, false);
    }//GEN-LAST:event_windowSliderStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable availableFiltersTable;
    private javax.swing.JLabel bufferLabel;
    private javax.swing.JSlider bufferSlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel resultsLabel;
    private javax.swing.JTable resultsTable;
    private javax.swing.JTextField searchBar;
    private wekimini.gui.WekiTokenField selectedFiltersTokenField;
    private javax.swing.JLabel windowLabel;
    private javax.swing.JSlider windowSlider;
    // End of variables declaration//GEN-END:variables
}
