// src/test/java/com/votrepackage/volumesfinis/service/FiniteVolumeSolverTest.java
package com.ananum.volumefini.service;

import com.ananum.volumefini.model.EquationParameters;
import com.ananum.volumefini.model.SolutionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

public class FiniteVolumeSolverTest {

    @Mock
    private GaussSeidelSolver gaussSeidelSolver;
    @Mock
    private Graphique graphique;
    @InjectMocks
    private FiniteVolumeSolver finiteVolumeSolver;
    private static final String CHART_OUTPUT_DIR = "C:\\Users\\PICSOU\\Desktop";
    private static final int nombrePoint = 51;
    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        Files.createDirectories(Paths.get(CHART_OUTPUT_DIR));
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenAnswer(invocation -> {
                    double[] b_arg = invocation.getArgument(1);
                    double[] internalSolution = new double[b_arg.length];
                    return internalSolution;
                });
    }

    private void generateAndSaveCharts(String testName, List<Double> xValues,
                                       List<Double> uNumerique,
                                       Function<Double, Double> uTheorique) throws IOException {

        // Générer le graphique de comparaison
        byte[] comparisonChartBytes = graphique.generateComparisonChart(
                xValues, uNumerique, uTheorique,
                "Comparaison (" + testName + ")", "Position (x)", "Valeur (u)"
        );
        Path comparisonPath = Paths.get(CHART_OUTPUT_DIR + testName + "_comparison.png");
        Files.write(comparisonPath, comparisonChartBytes);
        System.out.println("Graphique de comparaison enregistré : " + comparisonPath.toAbsolutePath());


        // Générer le graphique d'erreur
        byte[] errorChartBytes = graphique.generateErrorChart(
                xValues, uNumerique, uTheorique,
                "Erreur Absolue (" + testName + ")", "Position (x)", "Erreur"
        );
        Path errorPath = Paths.get(CHART_OUTPUT_DIR + testName + "_error.png");
        Files.write(errorPath, errorChartBytes);
        System.out.println("Graphique d'erreur enregistré : " + errorPath.toAbsolutePath());
    }

    @Test
    void testTheoreticalSolutionSineX() throws IOException {
        String testName = "Sin(X)";
        // Equation: u'' + u = 0, u(0)=0, u(PI/2)=1
        // Analytical solution: u(x) = sin(x)
        double a = 1.0;
        double b = 0.0;
        double c = 1.0;
        double xMin = 0.0;
        double xMax = Math.PI / 2.0;
        double limiteGauche = 0.0; // sin(0) = 0
        double limiteDroite = 1.0; // sin(PI/2) = 1
        EquationParameters params = new EquationParameters(a, b, c, xMin, xMax, nombrePoint, limiteGauche, limiteDroite);
        Function<Double, Double> f = x -> 0.0;
        Function<Double, Double> uTheorique = Math::sin;
        double h = (xMax - xMin) / (nombrePoint - 1);
        double[] solutionAttendu = new double[nombrePoint - 2];
        for (int i = 0; i < nombrePoint - 2; i++) {
            double x_i = xMin + (i + 1) * h;
            solutionAttendu[i] = uTheorique.apply(x_i);
        }
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenReturn(solutionAttendu);
        SolutionResult result = finiteVolumeSolver.solve(params, f, 1000, 1e-6);
        assertNotNull(result);
        assertEquals(nombrePoint, result.getxValues().size());
        assertEquals(nombrePoint, result.getuValues().size());
        for (int i = 0; i < nombrePoint; i++) {
            double x = result.getxValues().get(i);
            double expectedU = uTheorique.apply(x);
            assertEquals(expectedU, result.getxValues().get(i), 1e-4);
        }
        generateAndSaveCharts(testName, result.getxValues(), result.getuValues(), uTheorique);
    }

    @Test
    void testTheoreticalSolutionXCubed() throws IOException {
        String testName = "XCubed";
        // Equation: u'' + u' + u = x^3 + 3x^2 + 6x, u(0)=0, u(1)=1
        // Analytical solution: u(x) = x^3
        double a = 1.0;
        double b = 1.0;
        double c = 1.0;
        double xMin = 0.0;
        double xMax = 1.0;
        double limiteGauche = 0.0;
        double limiteDroite = 1.0;
        EquationParameters params = new EquationParameters(a, b, c, xMin, xMax, nombrePoint, limiteGauche, limiteDroite);
        Function<Double, Double> f = x -> x*x*x + 3*x*x + 6*x;
        Function<Double, Double> uTheorique = x -> x*x*x;
        double h = (xMax - xMin) / (nombrePoint - 1);
        double[] solutionAttendu = new double[nombrePoint - 2];
        for (int i = 0; i < nombrePoint - 2; i++) {
            double x_i = xMin + (i + 1) * h;
            solutionAttendu[i] = uTheorique.apply(x_i);
        }
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenReturn(solutionAttendu);
        SolutionResult result = finiteVolumeSolver.solve(params, f, 1000, 1e-6);
        assertNotNull(result);
        assertEquals(nombrePoint, result.getxValues().size());
        assertEquals(nombrePoint, result.getuValues().size());
        for (int i = 0; i < nombrePoint; i++) {
            double x = result.getxValues().get(i);
            double expectedU = uTheorique.apply(x);
            assertEquals(expectedU, result.getuValues().get(i), 1e-6);
        }
        generateAndSaveCharts(testName, result.getuValues(), result.getuValues(), uTheorique);
    }

    @Test
    void testTheoreticalSolutionXSquared() throws IOException {
        String testName = "X^2";
        // Equation: u'' + u' + u = x^2 + 2x + 2, u(0)=0, u(1)=1
        // Analytical solution: u(x) = x^2
        double a = 1.0;
        double b = 1.0;
        double c = 1.0;
        double xMin = 0.0;
        double xMax = 1.0;
        double limiteGauche = 0.0;
        double limiteDroite = 1.0;
        EquationParameters params = new EquationParameters(a, b, c, xMin, xMax, nombrePoint, limiteGauche, limiteDroite);
        Function<Double, Double> f = x -> x*x + 2*x + 2;
        Function<Double, Double> uTheorique = x -> x*x;
        double h = (xMax - xMin) / (nombrePoint - 1);
        double[] solutionAttendu = new double[nombrePoint - 2];
        for (int i = 0; i < nombrePoint - 2; i++) {
            double x_i = xMin + (i + 1) * h;
            solutionAttendu[i] = uTheorique.apply(x_i);
        }
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenReturn(solutionAttendu);
        SolutionResult result = finiteVolumeSolver.solve(params, f, 1000, 1e-6);
        assertNotNull(result);
        assertEquals(nombrePoint, result.getxValues().size());
        assertEquals(nombrePoint, result.getuValues().size());
        for (int i = 0; i < nombrePoint; i++) {
            double x = result.getxValues().get(i);
            double expectedU = uTheorique.apply(x);
            assertEquals(expectedU, result.getuValues().get(i), 1e-6);
        }
        generateAndSaveCharts(testName, result.getxValues(), result.getuValues(), uTheorique);
    }

    @Test
    void testTheoreticalSolutionZero() throws IOException {
        String testName = "O";
        // Equation: u'' + u' + u = 0, u(0)=0, u(1)=0
        // Analytical solution: u(x) = 0
        double a = 1.0;
        double b = 1.0;
        double c = 1.0;
        double xMin = 0.0;
        double xMax = 1.0;
        double limiteGauche = 0.0;
        double limiteDroite = 0.0;
        EquationParameters params = new EquationParameters(a, b, c, xMin, xMax, nombrePoint, limiteGauche, limiteDroite);
        Function<Double, Double> f = x -> 0.0;
        Function<Double, Double> uTheorique = x -> 0.0;
        double h = (xMax - xMin) / (nombrePoint - 1);
        double[] solutionAttendu = new double[nombrePoint - 2];
        for (int i = 0; i < nombrePoint - 2; i++) {
            double x_i = xMin + (i + 1) * h;
            solutionAttendu[i] = uTheorique.apply(x_i);
        }
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenReturn(solutionAttendu);
        SolutionResult result = finiteVolumeSolver.solve(params, f, 1000, 1e-6);
        assertNotNull(result);
        assertEquals(nombrePoint, result.getxValues().size());
        assertEquals(nombrePoint, result.getuValues().size());
        for (int i = 0; i < nombrePoint; i++) {
            double x = result.getxValues().get(i);
            double expectedU = uTheorique.apply(x);
            assertEquals(expectedU, result.getuValues().get(i), 1e-6);
        }
        generateAndSaveCharts(testName, result.getxValues(), result.getuValues(), uTheorique);
    }

    @Test
    void testInvalidNumPoints() {
        EquationParameters params = new EquationParameters(1, 0, 0, 0, 1, 2, 0, 0); // numPoints < 3
        Function<Double, Double> f = x -> 0.0;

        assertThrows(IllegalArgumentException.class, () -> {
            finiteVolumeSolver.solve(params, f, 100, 1e-6);
        });
    }
}