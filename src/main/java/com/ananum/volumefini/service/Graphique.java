package com.ananum.volumefini.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Service
public class Graphique {

    public JFreeChart generateComparisonChart(List<Double> xValues, List<Double> fonctionNumerique,
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
                true,
                true,
                false
        );
        return chart;
    }
    public JFreeChart generateErrorChart(List<Double> xValues, List<Double> fonctionNumerique,
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
        return chart;
    }
}
