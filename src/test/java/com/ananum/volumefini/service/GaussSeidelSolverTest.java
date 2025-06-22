package com.ananum.volumefini.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class GaussSeidelSolverTest {

	private GaussSeidelSolver gaussSeidelSolver;

    @BeforeEach
    void setUp() {
        gaussSeidelSolver = new GaussSeidelSolver();
    }
    
    @Test
    void testSolveSimpleSystem() {
        // Système:
        // 10x + 2y - z = 7
        // 3x + 5y + z = -8
        // -2x + y - 10z = 6
        // Solution exacte (approximative après quelques itérations): x=1, y=-2, z=-1
        double[][] A = {
                {10, 2, -1},
                {3, 5, 1},
                {-2, 1, -10}
        };
        double[] b = {7, -8, 6};
        double[] initialGuess = {0, 0, 0};
        int maxIterations = 100;
        double tolerance = 1e-6;

        double[] solution = gaussSeidelSolver.solve(A, b, initialGuess, maxIterations, tolerance);

        assertNotNull(solution);
        assertEquals(3, solution.length);
        // Vérifiez que la solution est proche de la solution exacte
        assertEquals(1.0, solution[0], 1e-4);
        assertEquals(-2.0, solution[1], 1e-4);
        assertEquals(-1.0, solution[2], 1e-4);
    }

    @Test
    void testSolveIdentityMatrix() {
        // Système I*x = b, donc x = b
        double[][] A = {{1, 0}, {0, 1}};
        double[] b = {5, 10};
        double[] initialGuess = {0, 0};
        int maxIterations = 10;
        double tolerance = 1e-9;

        double[] solution = gaussSeidelSolver.solve(A, b, initialGuess, maxIterations, tolerance);

        assertNotNull(solution);
        assertEquals(2, solution.length);
        assertEquals(5.0, solution[0], 1e-9);
        assertEquals(10.0, solution[1], 1e-9);
    }

    @Test
    void testSolveNonConvergingSystem() {
        // Un système qui ne converge pas facilement avec Gauss-Seidel (matrice non à diagonale dominante)
        double[][] A = {{1, 2}, {3, 1}};
        double[] b = {1, 1};
        double[] initialGuess = {0, 0};
        int maxIterations = 5; // Peu d'itérations
        double tolerance = 1e-9;

        double[] solution = gaussSeidelSolver.solve(A, b, initialGuess, maxIterations, tolerance);
        // Le test ici pourrait être de vérifier que l'erreur n'est pas sous la tolérance,
        // mais pour l'instant, on se contente de vérifier que ça ne plante pas.
        assertNotNull(solution);
    }

    @Test
    void testSolveZeroDiagonalElement() {
        double[][] A = {{1, 1}, {1, 0}};
        double[] b = {1, 1};
        double[] initialGuess = {0, 0};
        int maxIterations = 10;
        double tolerance = 1e-6;

        Exception exception = assertThrows(ArithmeticException.class, () -> {
            gaussSeidelSolver.solve(A, b, initialGuess, maxIterations, tolerance);
        });
        assertTrue(exception.getMessage().contains("zéro"));
    }

}
