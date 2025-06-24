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

    public SolutionResult solve(EquationParameters params, java.util.function.Function<Double, Double> f, int maxIterations, double tolerance) {
        double a = params.getA();
        double b = params.getB();
        double c = params.getC();
        double xMin = params.getxMin();
        double xMax = params.getxMax();
        int N = params.getNumPoints();
        double limiteGauche = params.getuBoundaryLeft();
        double limiteDroite = params.getuBoundaryRight();

        if (N < 3) { // Au moins 3 points pour avoir un point interne (N=1) + 2 bords
             throw new IllegalArgumentException("Le nombre de points de discrétisation doit être au moins 3 (incluant les bords).");
        }
        // h est l'espacement entre les points
        double h = (xMax - xMin) / (N - 1); // Si N est le nombre total de points, N-1 intervalles
        // Le système linéaire sera de taille (N-2) x (N-2) pour les points internes
        int matrixSize = N - 2;
        double[][] A = new double[matrixSize][matrixSize];
        double[] B = new double[matrixSize];
        double[] solutionInitial = new double[matrixSize];
        List<Double> xValues = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            xValues.add(xMin + i * h);
        }
        // Construire la matrice A et le vecteur B
        for (int i = 0; i < matrixSize; i++) {
            int globalIndex = i + 1;
            // Coefficient de u(i-1)
            if (i > 0) { // Pour les points autres que le premier point interne
                A[i][i - 1] = (a / (h * h)) - (b / (2 * h));
            }
            // Coefficient de u(i)
            A[i][i] = (-2 * a / (h * h)) + c;
            // Coefficient de u(i+1)
            if (i < matrixSize - 1) { // Pour les points autres que le dernier point interne
                A[i][i + 1] = (a / (h * h)) + (b / (2 * h));
            }
            // Second membre B
            double f_xi = f.apply(xValues.get(globalIndex));
            B[i] = f_xi;
            // Traitement des conditions aux limites
            if (i == 0) { // Premier point interne (u_1)
                // Le terme u(0) est connu (limiteGauche) et est déplacé vers le second membre
                B[i] -= ((a / (h * h)) - (b / (2 * h))) * limiteGauche;
            }
            if (i == matrixSize - 1) { // Dernier point interne (u_{N-2})
                // Le terme u(N-1) est connu (limiteDroite) et est déplacé vers le second membre
                B[i] -= ((a / (h * h)) + (b / (2 * h))) * limiteDroite;
            }
            solutionInitial[i] = 0.0; // Initialisation simple
        }
        // Résolution du système avec Gauss-Seidel
        double[] uInternal = gaussSeidelSolver.solve(A, B, solutionInitial, maxIterations, tolerance);
        // Reconstruire le vecteur solution complet incluant les conditions limites
        List<Double> uValues = new ArrayList<>();
        uValues.add(limiteGauche); // Condition limite gauche
        for (double val : uInternal) {
            uValues.add(val);
        }
        uValues.add(limiteDroite); // Condition limite droite
        int actualIterations = gaussSeidelSolver.nombreIterations;
        double actualTolerance = tolerance;
        return new SolutionResult(xValues, uValues, actualIterations, actualTolerance);
    }

}
