package com.ananum.volumefini.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ananum.volumefini.model.EquationParameters;
import com.ananum.volumefini.model.SolutionResult;

@Service
public class FiniteVolumeSolver {

	private final GaussSeidelSolver gaussSeidelSolver;

    public FiniteVolumeSolver(GaussSeidelSolver gaussSeidelSolver) {
        this.gaussSeidelSolver = gaussSeidelSolver;
    }

    /**
     * Résout l'équation différentielle du second ordre 1D au''+bu'+cu=f
     * en utilisant la méthode des volumes finis et Gauss-Seidel.
     *
     * @param params Les paramètres de l'équation et de la discrétisation.
     * @param f La fonction du second membre f(x).
     * @param maxIterations Le nombre maximal d'itérations pour Gauss-Seidel.
     * @param tolerance La tolérance pour la convergence de Gauss-Seidel.
     * @return Un objet SolutionResult contenant les valeurs de x et u.
     */
    public SolutionResult solve(EquationParameters params, java.util.function.Function<Double, Double> f, int maxIterations, double tolerance) {
        double a = params.getA();
        double b = params.getB();
        double c = params.getC();
        double xMin = params.getxMin();
        double xMax = params.getxMax();
        int N = params.getNumPoints(); // Nombre de points internes, N+2 points au total (incluant les bords)
        double uBoundaryLeft = params.getuBoundaryLeft();
        double uBoundaryRight = params.getuBoundaryRight();

        if (N < 3) { // Au moins 3 points pour avoir un point interne (N=1) + 2 bords
             throw new IllegalArgumentException("Le nombre de points de discrétisation doit être au moins 3 (incluant les bords).");
        }

        // h est l'espacement entre les points
        double h = (xMax - xMin) / (N - 1); // Si N est le nombre total de points, N-1 intervalles

        // Le système linéaire sera de taille (N-2) x (N-2) pour les points internes
        // Si N est le nombre total de points (incluant les bords), il y a N-2 points internes.
        int matrixSize = N - 2;

        double[][] A = new double[matrixSize][matrixSize];
        double[] B = new double[matrixSize];
        double[] initialGuess = new double[matrixSize]; // Initialisation à zéro ou à une estimation simple

        List<Double> xValues = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            xValues.add(xMin + i * h);
        }

        // Construire la matrice A et le vecteur B
        for (int i = 0; i < matrixSize; i++) {
            int globalIndex = i + 1; // Indice du point courant dans la grille globale (u_1 à u_{N-2})

            // Coefficient de u_{i-1} (ici u_globalIndex-1)
            if (i > 0) { // Pour les points autres que le premier point interne
                A[i][i - 1] = (a / (h * h)) - (b / (2 * h));
            }

            // Coefficient de u_i (ici u_globalIndex)
            A[i][i] = (-2 * a / (h * h)) + c;

            // Coefficient de u_{i+1} (ici u_globalIndex+1)
            if (i < matrixSize - 1) { // Pour les points autres que le dernier point interne
                A[i][i + 1] = (a / (h * h)) + (b / (2 * h));
            }

            // Second membre B
            double f_xi = f.apply(xValues.get(globalIndex));
            B[i] = f_xi;

            // Traitement des conditions aux limites
            if (i == 0) { // Premier point interne (u_1)
                // Le terme u_0 est connu (uBoundaryLeft) et est déplacé vers le second membre
                B[i] -= ((a / (h * h)) - (b / (2 * h))) * uBoundaryLeft;
            }
            if (i == matrixSize - 1) { // Dernier point interne (u_{N-2})
                // Le terme u_{N-1} est connu (uBoundaryRight) et est déplacé vers le second membre
                B[i] -= ((a / (h * h)) + (b / (2 * h))) * uBoundaryRight;
            }

            initialGuess[i] = 0.0; // Initialisation simple
        }

        // Résolution du système avec Gauss-Seidel
        double[] uInternal = gaussSeidelSolver.solve(A, B, initialGuess, maxIterations, tolerance);

        // Reconstruire le vecteur solution complet incluant les conditions limites
        List<Double> uValues = new ArrayList<>();
        uValues.add(uBoundaryLeft); // Condition limite gauche
        for (double val : uInternal) {
            uValues.add(val);
        }
        uValues.add(uBoundaryRight); // Condition limite droite

        // Simuler le nombre d'itérations et la tolérance réelle de Gauss-Seidel (nécessite une modification de GaussSeidelSolver pour retourner ces valeurs)
        // Pour l'instant, on peut les passer en dur ou modifier GaussSeidelSolver pour les retourner
        int actualIterations = gaussSeidelSolver.nombreIterations; // Placeholder
        double actualTolerance = tolerance; // Placeholder

        return new SolutionResult(xValues, uValues, actualIterations, actualTolerance);
    }

}
