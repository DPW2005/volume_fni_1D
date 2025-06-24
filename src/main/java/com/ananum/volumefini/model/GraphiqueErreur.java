package com.ananum.volumefini.model;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class GraphiqueErreur extends JPanel {

    private double[] xValues;
    private double[] errorValues;

    public GraphiqueErreur(double[] x, double[] error) {
        this.xValues = x;
        this.errorValues = error;
        setPreferredSize(new Dimension(800, 600));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int padding = 50;
        int labelPadding = 25;
        double xScale = ((double) getWidth() - 2 * padding - labelPadding) / (xValues[xValues.length - 1] - xValues[0]);
        double minY = getMin(errorValues);
        double maxY = getMax(errorValues);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxY - minY);
        // Dessiner les axes
        g2d.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2d.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);
        // Tracer la fonction d'erreur
        g2d.setColor(Color.GREEN);
        Path2D.Double pathError = new Path2D.Double();
        pathError.moveTo(padding + labelPadding + (xValues[0] - xValues[0]) * xScale,
                getHeight() - padding - labelPadding - (errorValues[0] - minY) * yScale);
        for (int i = 1; i < xValues.length; i++) {
            pathError.lineTo(padding + labelPadding + (xValues[i] - xValues[0]) * xScale,
                    getHeight() - padding - labelPadding - (errorValues[i] - minY) * yScale);
        }
        g2d.draw(pathError);
        // Ajouter une légende
        g2d.setColor(Color.GREEN);
        g2d.drawString("Erreur", getWidth() - 150, padding + 20);
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
