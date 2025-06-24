package com.ananum.volumefini.service;

// Exemple avec JFreeChart (nécessite d'ajouter la dépendance JFreeChart)
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Graphique {

    public byte[] generateFunctionChart(
            List<Double> xValues,
            List<Double> yNumericValues,
            List<Double> yTheoreticValues) throws IOException {

        XYSeries numericSeries = new XYSeries("Numérique");
        for (int i = 0; i < xValues.size(); i++) {
            numericSeries.add(xValues.get(i), yNumericValues.get(i));
        }

        XYSeries theoreticSeries = new XYSeries("Théorique");
        for (int i = 0; i < xValues.size(); i++) {
            theoreticSeries.add(xValues.get(i), yTheoreticValues.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(numericSeries);
        dataset.addSeries(theoreticSeries);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Fonctions Numérique et Théorique", // Titre
                "X",                           // Axe X
                "Y",                           // Axe Y
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(bas, chart, 800, 600); // Générer l'image PNG
        return bas.toByteArray();
    }

    // Vous auriez une méthode similaire pour le graphique d'erreur
}

// Dans votre RestController, vous pourriez avoir un endpoint comme ceci:
// @GetMapping(value = "/chart/functions.png", produces = MediaType.IMAGE_PNG_VALUE)
// public byte[] getFunctionChart(...) {
//     // ... obtenez vos données ...
//     return chartGeneratorService.generateFunctionChart(x, yNum, yTheo);
// }
