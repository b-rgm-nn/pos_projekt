
import com.ibm.watson.assistant.v1.model.MessageResponse;
import com.ibm.watson.assistant.v1.model.RuntimeEntity;
import com.ibm.watson.assistant.v1.model.RuntimeIntent;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;


public class GUI extends javax.swing.JFrame {

    private WatsonAssistant assistant = new WatsonAssistant();

    public GUI() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tfQuery = new javax.swing.JTextField();
        pnResult = new javax.swing.JPanel();
        btQuery = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tfQuery.setText("What was the maxmimum value of Apple in 2015");
        tfQuery.setToolTipText("query");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(tfQuery, gridBagConstraints);

        javax.swing.GroupLayout pnResultLayout = new javax.swing.GroupLayout(pnResult);
        pnResult.setLayout(pnResultLayout);
        pnResultLayout.setHorizontalGroup(
            pnResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        pnResultLayout.setVerticalGroup(
            pnResultLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnResult, gridBagConstraints);

        btQuery.setText("Query");
        btQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btQueryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(btQuery, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void querySingleIfValid(MessageResponse response, RuntimeIntent intent) throws UnknownQueryException{
        if(!intent.getIntent().equals(Intent.query_single_value.getName()))
            return;
        
        
    }
    
    private void btQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btQueryActionPerformed
        String querytext = tfQuery.getText();
        MessageResponse response = assistant.query(querytext);
        for(RuntimeIntent intent : response.getIntents()) {
//            assistant.(response, intent);
        }
    }//GEN-LAST:event_btQueryActionPerformed

    public static void main(String args[]) {
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btQuery;
    private javax.swing.JPanel pnResult;
    private javax.swing.JTextField tfQuery;
    // End of variables declaration//GEN-END:variables

}
