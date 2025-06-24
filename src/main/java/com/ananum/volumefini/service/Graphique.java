package com.ananum.volumefini.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

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
        String filePath = "C:\\Users\\PICSOU\\Desktop\\CAPTURE"+fonction+points+".png" ;
        File chartImage = new File(filePath);
        ChartUtils.saveChartAsPNG(chartImage, chart, 800, 800) ;
        System.out.println("Graphique enregistré dans : " + filePath);
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
        String filePath = "C:\\Users\\PICSOU\\Desktop\\CAPTURE"+fonction+"erreur"+points+".png" ;
        File chartImage = new File(filePath);
        ChartUtils.saveChartAsPNG(chartImage, chart, 800, 800);
        System.out.println("Graphique d'erreur enregistré dans : " + filePath);
    }
}
