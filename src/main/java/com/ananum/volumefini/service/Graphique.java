package com.ananum.volumefini.service;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

@Service
public class Graphique {

    public byte[] generateComparisonChart(List<Double> xValues, List<Double> fonctionNumerique,
                                          Function<Double, Double> fonctionTheorique,
                                          String title, String xAxisLabel, String yAxisLabel) throws IOException {

        XYSeries numericSeries = new XYSeries("Solution Numérique");
        XYSeries theoreticalSeries = new XYSeries("Solution Théorique");

        for (int i = 0; i < xValues.size(); i++) {
            double x = xValues.get(i);
            numericSeries.add(x, fonctionNumerique.get(i));
            theoreticalSeries.add(x, fonctionTheorique.apply(x));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(numericSeries);
        dataset.addSeries(theoreticalSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, chart, 800, 600); // Génère l'image PNG de 800x600 pixels
        return baos.toByteArray();
    }
    public byte[] generateErrorChart(List<Double> xValues, List<Double> fonctionNumerique,
                                     Function<Double, Double> fonctionTheorique,
                                     String title, String xAxisLabel, String yAxisLabel) throws IOException {

        XYSeries errorSeries = new XYSeries("Erreur Absolue");

        for (int i = 0; i < xValues.size(); i++) {
            double x = xValues.get(i);
            double numericVal = fonctionNumerique.get(i);
            double theoreticalVal = fonctionTheorique.apply(x);
            errorSeries.add(x, Math.abs(numericVal - theoreticalVal));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(errorSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, chart, 800, 600);
        return baos.toByteArray();
    }
}
