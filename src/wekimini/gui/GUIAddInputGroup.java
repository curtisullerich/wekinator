/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekimini.gui;

import wekimini.osc.OSCInputGroup;

/**
 *
 * @author rebecca
 */
public class GUIAddInputGroup extends javax.swing.JPanel {
    //TODO: prevent bad inputs
    
    private boolean isEditing = false;
    //TODO: private final Wekinator w;
    /**
     * Creates new form GUIAddInputGroup
     */
    public GUIAddInputGroup() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        buttonDone = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        textGroupName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        textNumInputs = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        textOSCMessage = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaListNames = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        buttonFromFile = new javax.swing.JButton();

        jPanel1.setMinimumSize(new java.awt.Dimension(365, 0));

        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        buttonDone.setText("Done");
        buttonDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDoneActionPerformed(evt);
            }
        });

        jLabel1.setText("Input name:");

        textGroupName.setText("Input 1");
        textGroupName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textGroupNameActionPerformed(evt);
            }
        });
        textGroupName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textGroupNameKeyTyped(evt);
            }
        });

        jLabel2.setText("# Inputs:");

        textNumInputs.setText("5");

        jLabel3.setText("OSC message:");

        textOSCMessage.setText("/Input_1");
        textOSCMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textOSCMessageActionPerformed(evt);
            }
        });

        jLabel4.setText("Individual input names:");

        textAreaListNames.setColumns(20);
        textAreaListNames.setRows(5);
        textAreaListNames.setText("Input_1_1\nInput_1_2\nInput_1_3\nInput_1_4\nInput_1_5");
        jScrollPane1.setViewportView(textAreaListNames);

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        jLabel5.setText("(Enter one input name per line)");

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 2, 13)); // NOI18N
        jLabel6.setText("Send one float per input");

        buttonFromFile.setText("From file...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(textNumInputs, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(textOSCMessage)
                                    .addComponent(textGroupName)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(buttonFromFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonDone)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(textNumInputs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textOSCMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonDone)
                    .addComponent(jButton1)
                    .addComponent(buttonFromFile))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      //  dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void buttonDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDoneActionPerformed
        String groupName = textGroupName.getText();
//TODO
      /*  try {
            OSCInputGroup g = getGroupFromForm();
            if (! isEditing) {
                w.getInputManager.
                w.getInputManager().addOSCInputGroup(g, true);
                dispose();
            } else {
                w.getInputManager().modifyOSCInputGroup(groupName, g, true);
                dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } */
    }//GEN-LAST:event_buttonDoneActionPerformed

    private void textOSCMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textOSCMessageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textOSCMessageActionPerformed

    private void textGroupNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textGroupNameKeyTyped
        System.out.println("1");
    }//GEN-LAST:event_textGroupNameKeyTyped

    private void textGroupNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textGroupNameActionPerformed
        System.out.println("here");
    }//GEN-LAST:event_textGroupNameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDone;
    private javax.swing.JButton buttonFromFile;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea textAreaListNames;
    private javax.swing.JTextField textGroupName;
    private javax.swing.JTextField textNumInputs;
    private javax.swing.JTextField textOSCMessage;
    // End of variables declaration//GEN-END:variables
}
