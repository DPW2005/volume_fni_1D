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
        int N = params.getNombrePoint();
        double limiteGauche = params.getLimiteGauche();
        double limiteDroite = params.getLimiteDroite();
        // Au moins 3 points pour avoir un point interne et 2 bords
        if (N < 3) {
             throw new IllegalArgumentException("Le nombre de points de discrétisation doit être au moins 3 (incluant les bords).");
        }
        // h est l'espacement entre les points
        double h = (xMax - xMin) / (N - 1);
        // Taille des matrice à manipuler
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
            int indiceActuel = i + 1; // Indice du point courant dans la grille globale (u(1) à u(N-2))
            // Coefficient de u(i-1), pour les points autres que le premier point interne
            if (i > 0) {
                A[i][i - 1] = (a / (h * h)) - (b / (2 * h));
            }
            // Coefficient de u(i)
            A[i][i] = (-2 * a / (h * h)) + c;
            // Coefficient de u(i+1), pour les points autres que le dernier point interne
            if (i < matrixSize - 1) {
                A[i][i + 1] = (a / (h * h)) + (b / (2 * h));
            }
            // Second membre B
            double f_xi = f.apply(xValues.get(indiceActuel));
            B[i] = f_xi;
            // Traitement des conditions aux limites
            if (i == 0) {
                B[i] -= ((a / (h * h)) - (b / (2 * h))) * limiteGauche;
            }
            if (i == matrixSize - 1) {
                B[i] -= ((a / (h * h)) + (b / (2 * h))) * limiteDroite;
            }
            solutionInitial[i] = 0.0; // Initialisation simple
        }

        // Résolution du système avec Gauss-Seidel
        double[] uInternal = gaussSeidelSolver.solve(A, B, solutionInitial, maxIterations, tolerance);
        // Reconstruire le vecteur solution complet incluant les conditions limites
        List<Double> uValues = new ArrayList<>();
        uValues.add(limiteGauche);
        for (double val : uInternal) {
            uValues.add(val);
        }
        uValues.add(limiteDroite);
        int actualIterations = gaussSeidelSolver.nombreIterations;
        double actualTolerance = tolerance;
        return new SolutionResult(xValues, uValues, actualIterations, actualTolerance);
    }
}
