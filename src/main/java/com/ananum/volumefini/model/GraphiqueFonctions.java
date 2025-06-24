package com.ananum.volumefini.model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D; // Utile pour tracer des courbes lisses

public class GraphiqueFonctions extends JPanel {

    private double[] xValues;
    private double[] yNumericValues;
    private double[] yTheoreticValues;

    public GraphiqueFonctions(double[] x, double[] yNum, double[] yTheo) {
        this.xValues = x;
        this.yNumericValues = yNum;
        this.yTheoreticValues = yTheo;
        setPreferredSize(new Dimension(800, 600)); // Définir une taille préférée pour le panneau
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Définir les marges et les échelles
        int padding = 50;
        int labelPadding = 25;
        double xScale = ((double) getWidth() - 2 * padding - labelPadding) / (xValues[xValues.length - 1] - xValues[0]);
        double minY = Math.min(getMin(yNumericValues), getMin(yTheoreticValues));
        double maxY = Math.max(getMax(yNumericValues), getMax(yTheoreticValues));
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxY - minY);
        // Dessiner les axes
        g2d.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2d.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);
        // Tracer la fonction numérique
        g2d.setColor(Color.BLUE);
        Path2D.Double pathNumeric = new Path2D.Double();
        pathNumeric.moveTo(padding + labelPadding + (xValues[0] - xValues[0]) * xScale,
                getHeight() - padding - labelPadding - (yNumericValues[0] - minY) * yScale);
        for (int i = 1; i < xValues.length; i++) {
            pathNumeric.lineTo(padding + labelPadding + (xValues[i] - xValues[0]) * xScale,
                    getHeight() - padding - labelPadding - (yNumericValues[i] - minY) * yScale);
        }
        g2d.draw(pathNumeric);
        // Tracer la fonction théorique
        g2d.setColor(Color.RED);
        Path2D.Double pathTheoretic = new Path2D.Double();
        pathTheoretic.moveTo(padding + labelPadding + (xValues[0] - xValues[0]) * xScale,
                getHeight() - padding - labelPadding - (yTheoreticValues[0] - minY) * yScale);
        for (int i = 1; i < xValues.length; i++) {
            pathTheoretic.lineTo(padding + labelPadding + (xValues[i] - xValues[0]) * xScale,
                    getHeight() - padding - labelPadding - (yTheoreticValues[i] - minY) * yScale);
        }
        g2d.draw(pathTheoretic);
        // Ajouter une légende
        g2d.setColor(Color.BLUE);
        g2d.drawString("Numérique", getWidth() - 150, padding + 20);
        g2d.setColor(Color.RED);
        g2d.drawString("Théorique", getWidth() - 150, padding + 40);
    }

    private double getMin(double[] values) {
        double min = Double.MAX_VALUE;
        for (double v : values) {
            if (v < min) min = v;
        }
        return min;
    }

    private double getMax(double[] values) {
        double max = Double.MIN_VALUE;
        for (double v : values) {
            if (v > max) max = v;
        }
        return max;
    }
}
