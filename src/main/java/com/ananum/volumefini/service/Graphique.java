package com.ananum.volumefini.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Service
public class Graphique {

    public void generateFunctionChart(double[] xValues, double[] yNumericValues, double[] yTheoreticValues, String fonction, int points) throws IOException {
        XYSeries solutionNumerique = new XYSeries("Numérique");
        for (int i = 0; i < xValues.length ; i++) {
            solutionNumerique.add(xValues[i], yNumericValues[i]);
        }
        XYSeries solutionTheorique = new XYSeries("Théorique");
        for (int i = 0; i < xValues.length ; i++) {
            solutionTheorique.add(xValues[i], yTheoreticValues[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(solutionNumerique);
        dataset.addSeries(solutionTheorique);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Fonctions Numérique et Théorique",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);  // Pas de ligne pour la série numérique
        renderer.setSeriesShapesVisible(0, true);  // Afficher les points pour la série numérique
        renderer.setSeriesPaint(0, Color.BLUE);    // Couleur bleue pour la série numérique
        renderer.setSeriesLinesVisible(1, true);   // Ligne pour la série théorique
        renderer.setSeriesShapesVisible(1, false); // Pas de points pour la série théorique
        renderer.setSeriesPaint(1, Color.RED);     // Couleur rouge pour la série théorique
        plot.setRenderer(renderer);
        try {
            String filePath = "C:/Users/PICSOU/Documents/CAPTURE/"+fonction+points+".png" ;
            File chartImage = new File(filePath);
            ChartUtils.saveChartAsPNG(chartImage, chart, 800, 800) ;
            System.out.println("Graphique enregistré dans : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void generateErrorChart(double[] xValues, double[] errorValues,  String fonction, int points) throws IOException {
        XYSeries errorSeries = new XYSeries("Erreur Absolue");
        for (int i = 0; i < xValues.length; i++) {
            errorSeries.add(xValues[i], errorValues[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(errorSeries);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Fonction d'Erreur",
                "X",
                "Erreur",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        try {
            String filePath = "C:/Users/PICSOU/Documents/CAPTURE/"+fonction+"erreur"+points+".png" ;
            File chartImage = new File(filePath);
            ChartUtils.saveChartAsPNG(chartImage, chart, 800, 800);
            System.out.println("Graphique d'erreur enregistré dans : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
