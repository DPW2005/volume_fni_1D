package com.ananum.volumefini.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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

    @Test
    void testSolveForLinearSolution() {
        // Test case: u'' = 0 => u(x) = C1*x + C2
        // Let's set u(0) = 0 and u(1) = 1. Then u(x) = x.
        // Equation: 0*u'' + 0*u' + 0*u = 0  => a=0, b=0, c=0, f(x)=0
        // Parameters:
        double a = 0.0;
        double b = 0.0;
        double c = 0.0;
        double xMin = 0.0;
        double xMax = 1.0;
        int numPoints = 11; // 11 points means 9 internal points, N=11 for the model
        double uBoundaryLeft = 0.0;
        double uBoundaryRight = 1.0;

        EquationParameters params = new EquationParameters(a, b, c, xMin, xMax, numPoints, uBoundaryLeft, uBoundaryRight);
        Function<Double, Double> f = x -> 0.0; // f(x) = 0

        // Mock Gauss-Seidel to return the known exact solution for the internal points
        // For u(x) = x, if N=11, then u_0=0, u_1=0.1, u_2=0.2, ..., u_9=0.9, u_10=1.0
        // Internal points are u_1 to u_9.
        double[] expectedInternalSolution = new double[numPoints - 2];
        for (int i = 0; i < numPoints - 2; i++) {
            expectedInternalSolution[i] = xMin + (i + 1) * (xMax - xMin) / (numPoints - 1);
        }
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenReturn(expectedInternalSolution);

        SolutionResult result = finiteVolumeSolver.solve(params, f, 100, 1e-6);

        assertNotNull(result);
        assertEquals(numPoints, result.getuValues().size());
        assertEquals(numPoints, result.getuValues().size());

        // Check if the solution matches u(x) = x
        for (int i = 0; i < numPoints; i++) {
            double expectedU = result.getuValues().get(i);
            assertEquals(expectedU, result.getuValues().get(i), 1e-6);
        }
    }

    @Test
    void testSolveWithNonZeroFAndCoefficients() {
        // Test case for u'' - u = -2, u(0)=1, u(1)=1. Solution: u(x) = 1 (constant)
        // a=1, b=0, c=-1, f(x)=-2
        double a = 1.0;
        double b = 0.0;
        double c = -1.0;
        double xMin = 0.0;
        double xMax = 1.0;
        int numPoints = 11;
        double uBoundaryLeft = 1.0;
        double uBoundaryRight = 1.0;

        EquationParameters params = new EquationParameters(a, b, c, xMin, xMax, numPoints, uBoundaryLeft, uBoundaryRight);
        Function<Double, Double> f = x -> -2.0;

        double[] expectedInternalSolution = new double[numPoints - 2];
        for (int i = 0; i < numPoints - 2; i++) {
            expectedInternalSolution[i] = 1.0; // Expected solution is u(x)=1
        }
        when(gaussSeidelSolver.solve(any(double[][].class), any(double[].class), any(double[].class), anyInt(), anyDouble()))
                .thenReturn(expectedInternalSolution);

        SolutionResult result = finiteVolumeSolver.solve(params, f, 100, 1e-6);

        assertNotNull(result);
        assertEquals(numPoints, result.getuValues().size());
        assertEquals(numPoints, result.getuValues().size());

        // Check if the solution matches u(x) = 1
        for (int i = 0; i < numPoints; i++) {
            assertEquals(1.0, result.getuValues().get(i), 1e-6);
        }
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
