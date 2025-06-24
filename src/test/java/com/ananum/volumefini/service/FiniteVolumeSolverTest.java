package com.ananum.volumefini.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ananum.volumefini.model.EquationParameters;
import com.ananum.volumefini.model.SolutionResult;

public class FiniteVolumeSolverTest {

	@Mock
    private GaussSeidelSolver gaussSeidelSolver; // On mocke le solveur Gauss-Seidel pour tester FVSolvers isolément

    @InjectMocks
    private FiniteVolumeSolver finiteVolumeSolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    private static final int NUM_POINTS = 51;

    @Test
    void testTheoreticalSolutionSineX() throws IOException {
        String testName = "SineX";
        // Equation: -u'' = (pi)^2*sin(pi*x), u(0)=0, u(PI/2)=1
        // Analytical solution: u(x) = sin(x)
        double a = -1.0;
        double b = 0.0;
        double c = 0.0;
        double xMin = 0.0;
        double xMax = 1.0 / 2.0;
        double uBoundaryLeft = 0.0; // sin(0) = 0
        double uBoundaryRight = 1.0; // sin(PI/2) = 1
        EquationParameters params = new EquationParameters(a, b, c, xMin, xMax, NUM_POINTS, uBoundaryLeft, uBoundaryRight);
        Function<Double, Double> f = x -> Math.PI*Math.PI*Math.sin(Math.PI*x);
        Function<Double, Double> uTheoretical = x -> Math.sin(Math.PI*x); // Analytical solution u(x) = sin(x)
        double h = (xMax - xMin) / (NUM_POINTS - 1);
        double[] expectedInternalSolution = new double[NUM_POINTS - 2];
        for (int i = 0; i < NUM_POINTS - 2; i++) {
            double x_i = xMin + (i + 1) * h;
            expectedInternalSolution[i] = uTheoretical.apply(x_i);
        }
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenReturn(expectedInternalSolution);
        SolutionResult result = finiteVolumeSolver.solve(params, f, 1000, 1e-6);
        assertNotNull(result);
        assertEquals(NUM_POINTS, result.getxValues().size());
        assertEquals(NUM_POINTS, result.getuValues().size());
        for (int i = 0; i < NUM_POINTS; i++) {
            double x = result.getxValues().get(i);
            double expectedU = uTheoretical.apply(x);
            assertEquals(expectedU, result.getuValues().get(i), 1e-4);
        }
    }

    @Test
    void testTheoreticalSolutionXCubed() throws IOException {
        String testName = "XCubed";
        // Equation: -u'' = -6x, u(0)=0, u(1)=1
        // Analytical solution: u(x) = x^3
        double a = -1.0;
        double b = 0.0;
        double c = 0.0;
        double xMin = 0.0;
        double xMax = 1.0;
        double uBoundaryLeft = 0.0;
        double uBoundaryRight = 1.0;
        EquationParameters params = new EquationParameters(a, b, c, xMin, xMax, NUM_POINTS, uBoundaryLeft, uBoundaryRight);
        Function<Double, Double> f = x -> -6*x;
        Function<Double, Double> uTheoretical = x -> x*x*x;
        double h = (xMax - xMin) / (NUM_POINTS - 1);
        double[] expectedInternalSolution = new double[NUM_POINTS - 2];
        for (int i = 0; i < NUM_POINTS - 2; i++) {
            double x_i = xMin + (i + 1) * h;
            expectedInternalSolution[i] = uTheoretical.apply(x_i);
        }
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenReturn(expectedInternalSolution);
        SolutionResult result = finiteVolumeSolver.solve(params, f, 1000, 1e-6);
        assertNotNull(result);
        assertEquals(NUM_POINTS, result.getxValues().size());
        assertEquals(NUM_POINTS, result.getuValues().size());
        for (int i = 0; i < NUM_POINTS; i++) {
            double x = result.getxValues().get(i);
            double expectedU = uTheoretical.apply(x);
            assertEquals(expectedU, result.getuValues().get(i), 1e-6);
        }
    }

    @Test
    void testTheoreticalSolutionXSquared() throws IOException {
        String testName = "XSquared";
        // Equation: -u'' = -2, u(0)=0, u(1)=1
        // Analytical solution: u(x) = x^2
        double a = -1.0;
        double b = 0.0;
        double c = 0.0;
        double xMin = 0.0;
        double xMax = 1.0;
        double uBoundaryLeft = 0.0;
        double uBoundaryRight = 1.0;
        EquationParameters params = new EquationParameters(a, b, c, xMin, xMax, NUM_POINTS, uBoundaryLeft, uBoundaryRight);
        Function<Double, Double> f = x -> -2.0;
        Function<Double, Double> uTheoretical = x -> x*x;
        double h = (xMax - xMin) / (NUM_POINTS - 1);
        double[] expectedInternalSolution = new double[NUM_POINTS - 2];
        for (int i = 0; i < NUM_POINTS - 2; i++) {
            double x_i = xMin + (i + 1) * h;
            expectedInternalSolution[i] = uTheoretical.apply(x_i);
        }
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenReturn(expectedInternalSolution);
        SolutionResult result = finiteVolumeSolver.solve(params, f, 1000, 1e-6);
        assertNotNull(result);
        assertEquals(NUM_POINTS, result.getxValues().size());
        assertEquals(NUM_POINTS, result.getuValues().size());
        for (int i = 0; i < NUM_POINTS; i++) {
            double x = result.getxValues().get(i);
            double expectedU = uTheoretical.apply(x);
            assertEquals(expectedU, result.getuValues().get(i), 1e-6);
        }
    }

}
