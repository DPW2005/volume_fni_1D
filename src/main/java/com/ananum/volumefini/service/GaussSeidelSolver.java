package com.ananum.volumefini.service;

import org.springframework.stereotype.Service;

@Service
public class GaussSeidelSolver {
	
	public int nombreIterations = 0 ;

    public double[] solve(double[][] A, double[] b, double[] initialGuess, int maxIterations, double tolerance) {
        if (A == null || b == null || initialGuess == null) {
            throw new IllegalArgumentException("Les entrées ne peuvent pas être nulles.");
        }
        int n = A.length;
        if (n == 0 || A[0].length != n || b.length != n || initialGuess.length != n) {
            throw new IllegalArgumentException("Dimensions de matrice/vecteur incompatibles.");
        }

        double[] x = initialGuess.clone(); // Utilise une copie de l'estimation initiale
        double[] prevX = new double[n];
        int k = 0;

        for (k = 0; k < maxIterations; k++) {
            System.arraycopy(x, 0, prevX, 0, n); // Sauvegarde l'itération précédente

            for (int i = 0; i < n; i++) {
                double sum1 = 0.0;
                for (int j = 0; j < i; j++) {
                    sum1 += A[i][j] * x[j];
                }

                double sum2 = 0.0;
                for (int j = i + 1; j < n; j++) {
                    sum2 += A[i][j] * prevX[j]; // Utilise prevX pour les éléments non encore mis à jour
                }

                if (A[i][i] == 0) {
                    throw new ArithmeticException("La diagonale de la matrice A contient un zéro, Gauss-Seidel ne peut pas continuer.");
                }
                x[i] = (b[i] - sum1 - sum2) / A[i][i];
            }

            // Vérification de la convergence
            double error = 0.0;
            for (int i = 0; i < n; i++) {
                error += Math.abs(x[i] - prevX[i]);
            }

            if (error < tolerance) {
                System.out.println("Gauss-Seidel a convergé après " + (k + 1) + " itérations.");
                break;
            }
            nombreIterations++ ;
        }

        if (k == maxIterations) {
            System.out.println("Gauss-Seidel n'a pas convergé après " + maxIterations + " itérations.");
        }
        return x;
    }

}
