package GUI;

import BL.Value;
import Query.Query;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Matthias
 */
public class OverlayedBarGraph extends javax.swing.JPanel {

    private double sidePadding = 0.2;
    private double topBotPadding = 0.15;

    private int barWidth = 50;

    private List<Double> values = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    private String companySymbol;

    private double maxValue;
    private double minValue;

    /**
     * Creates new panel OverlayedBarGraph that displays multiple values as a
     * colored, overlayed graph (e.g.min, max as to graphs on top of each other)
     * graph (e.g.min, max as to graphs on top of each other)
     *
     * @param value the query result to display
     * @param companySymbol The stock symbol of the company that will be shown
     */
    public OverlayedBarGraph(Value value, String companySymbol) {
        if (values.size() != names.size()) {
            throw new IllegalArgumentException("values and names are not the same length");
        }

        initComponents();

        this.values = new ArrayList<>();
        this.names = new ArrayList<>();
        this.companySymbol = companySymbol;

        values.add(value.getHigh());
        names.add(String.format("high - %.2f$", value.getHigh()));

        if (value.getClose() > value.getOpen()) {
            values.add(value.getClose());
            names.add(String.format("close - %.2f$", value.getClose()));
            values.add(value.getOpen());
            names.add(String.format("open - %.2f$", value.getOpen()));
        } else {
            values.add(value.getOpen());
            names.add(String.format("open - %.2f$", value.getOpen()));
            values.add(value.getClose());
            names.add(String.format("close - %.2f$", value.getClose()));
        }

        values.add(value.getLow());
        names.add(String.format("low - %.2f$", value.getLow()));

        Random rand = new Random();
        maxValue = minValue = values.get(0);
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) > maxValue) {
                maxValue = values.get(i);
            }
            if (values.get(i) < minValue) {
                minValue = values.get(i);
            }
            colors.add(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        int w = (int) (getWidth() * (1 - sidePadding * 2));
        int h = (int) (getHeight() * (1 - topBotPadding * 2));
        int sidePxl = (int) (getWidth() * sidePadding);
        int topPxl = (int) (getHeight() * topBotPadding);
        double yAxisStartValue = ((int) (maxValue - 2 * (maxValue - minValue))) / 10 * 10;
        double valuePerPxl = (maxValue - yAxisStartValue) / h;
        double stepvaluesize = ((int) (valuePerPxl * h / 5));
        if(stepvaluesize > 5) {
            stepvaluesize = ((int) stepvaluesize) / 5 * 5;
        }
        if(stepvaluesize < 1) stepvaluesize = 1;
        double steppixelsize = 1.0 / (valuePerPxl / stepvaluesize);

        // title
        g2d.setColor(Color.black);
        int fontSize = (int) Math.min(topPxl * 0.6, 25);
        g2d.setFont(new Font("Calibri", Font.PLAIN, fontSize));
        String companyName = "unknown";
        try {
            companyName = Query.companyName(companySymbol);
        } catch (SQLException e) {
        }
        g2d.drawString(companySymbol + " - " + companyName, sidePxl, (int) (topPxl * 0.65));

        // y axis
        g2d.setStroke(new BasicStroke(3));
        g2d.setFont(new Font("Calibri", Font.PLAIN, 13));
        g2d.setColor(Color.black);
        g2d.drawLine(sidePxl, topPxl + h, sidePxl, topPxl);
        double value = yAxisStartValue;
        for (double i = topPxl + h; i >= topPxl; i -= steppixelsize) {
            g2d.drawLine(sidePxl - 5, (int) i, sidePxl + 5, (int) i);
            g2d.drawString(String.format("%.2f", value), sidePxl - 40, (int) i);
            value += stepvaluesize;
        }

        // x axis
        g2d.drawLine(sidePxl, topPxl + h, sidePxl + w, topPxl + h);

        // draw bar
        for (int i = 0; i < values.size(); i++) {
            g2d.setColor(colors.get(i));
            int barHeight = (int) ((values.get(i) - yAxisStartValue) / valuePerPxl);
            g2d.fillRect(sidePxl + 50, topPxl + h - barHeight, barWidth, barHeight);
        }

        // draw legend
        int lx = sidePxl + 200;
        int ly = topPxl + h - 30;
        for (int i = values.size() - 1; i >= 0; i--) {
            g2d.setColor(colors.get(i));
            g2d.fillRect(lx - 20, ly - 10, 10, 10);
            g2d.setColor(Color.black);
            g2d.drawString(names.get(i), lx, ly);
            ly -= 20;
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
