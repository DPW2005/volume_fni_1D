package com.ananum.volumefini.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ThomasAlgorithmSolver {

    public static double[] solve(double[] a, double[] b, double[] c, double[] d) {
        int n = b.length;
        // Vérification des dimensions
        if (a.length != n - 1 || c.length != n - 1 || d.length != n) {
            System.out.println("Les dimensions des tableaux a, b, c, d ne sont pas cohérentes pour ce système tridiagonal");
        }
        // Créer des copies pour éviter de modifier les tableaux originaux passés en argument
        double[] b_copy = Arrays.copyOf(b, n);
        double[] d_copy = Arrays.copyOf(d, n);
        double[] x = new double[n]; // Le tableau pour stocker la solution
        // Les coefficients ai et ci sont utilisés directement.
        // b_i et d_i sont modifiés sur place (sur les copies).
        for (int i = 1; i < n; i++) {
            if (b_copy[i - 1] == 0.0) {
                System.out.println("Division par zéro possible à l'étape de descente");
            }
            double m = a[i - 1] / b_copy[i - 1];
            b_copy[i] = b_copy[i] - m * c[i - 1];
            d_copy[i] = d_copy[i] - m * d_copy[i - 1];
        }
        if (b_copy[n - 1] == 0.0) {
            System.out.println("Division par zéro possible à l'étape de remontée");
        }
        x[n - 1] = d_copy[n - 1] / b_copy[n - 1];
        // Calcul des autres inconnues en remontant
        for (int i = n - 2; i >= 0; i--) {
            if (b_copy[i] == 0.0) {
                System.out.println("Division par zéro possible à l'étape de remontée");
            }
            x[i] = (d_copy[i] - c[i] * x[i + 1]) / b_copy[i];
        }
        return x;
    }
}
