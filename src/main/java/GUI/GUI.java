package GUI;

import BL.WatsonAssistant;
import Exceptions.NoDataFoundException;
import Exceptions.UnknownQueryException;
import Query.Query;
import Query.SingleValueQuery;
import BL.Value;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI extends javax.swing.JFrame {

    private WatsonAssistant assistant = new WatsonAssistant();

    private boolean loading = false;

    public GUI() {
        initComponents();
    }

    private void startLoading() {
        loading = true;
        Thread t = new Thread(() -> {
            JLabel label = new JLabel();
            label.setFont(new Font("Calibri", Font.PLAIN, 40));
            clearResultsPanel();
            pnResult.add(label);
            String loadingPoints = ".";
            while (loading) {
                label.setText("Processing" + loadingPoints);
                if (loadingPoints.length() < 3) {
                    loadingPoints += ".";
                } else {
                    loadingPoints = ".";
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                }
            }
        });
        t.start();
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

        tfQuery.setText("What was the maxmimum value of AAPL in 2015");
        tfQuery.setToolTipText("query");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(tfQuery, gridBagConstraints);

        pnResult.setPreferredSize(new java.awt.Dimension(500, 500));
        pnResult.setLayout(new java.awt.GridLayout(1, 0));
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

    private void clearResultsPanel() {
        for (Component component : pnResult.getComponents()) {
            if (component instanceof JPanel || component instanceof JLabel) {
                pnResult.remove(component);
            }
        }
    }

    private void displayQuery(SingleValueQuery query) {
        try {
            Value value = query.queryValue();
            clearResultsPanel();
            pnResult.add(new OverlayedBarGraph(value));
            revalidate();
        } catch (NoDataFoundException ex) {
            System.out.printf("No Entry found for company %s between %s and %s",
                    query.getCompany(), query.getStartDate().format(DateTimeFormatter.ISO_DATE), query.getEndDate().format(DateTimeFormatter.ISO_DATE));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void btQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btQueryActionPerformed
        String querytext = tfQuery.getText();
        Thread t = new Thread(() -> {
            try {
                startLoading();
                Query query = assistant.query(querytext);
                loading = false;
                if (query instanceof SingleValueQuery) {
                    displayQuery((SingleValueQuery) query);
                }
            } catch (UnknownQueryException exception) {
                JOptionPane.showMessageDialog(this, "could not process query: " + exception.getMessage());
            }
        });
        t.start();

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
