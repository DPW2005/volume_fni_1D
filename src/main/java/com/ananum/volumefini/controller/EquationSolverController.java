package com.ananum.volumefini.controller;

import com.ananum.volumefini.model.GraphiqueErreur;
import com.ananum.volumefini.model.GraphiqueFonctions;
import com.ananum.volumefini.service.GaussSeidelSolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ananum.volumefini.model.EquationParameters;
import com.ananum.volumefini.model.SolutionResult;
import com.ananum.volumefini.service.FiniteVolumeSolver;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

@RestController
@RequestMapping("/solve")
public class EquationSolverController {

    private final FiniteVolumeSolver solver;

    public EquationSolverController(FiniteVolumeSolver solver) {
        this.solver = solver;
    }
    
    @GetMapping
    public String saluer() {
    	return "Bienvenue sur notre site de reolution par les volumes finis" ;
    }

    @PostMapping
    public ResponseEntity<SolutionResult> solveEquation(@RequestBody EquationParameters params) {
        try {
            GaussSeidelSolver gaussSeidelSolver = new GaussSeidelSolver();
            FiniteVolumeSolver finiteVolumeSolver = new FiniteVolumeSolver(gaussSeidelSolver) ;
            Function<Double, Double> f = x -> Math.PI*Math.PI*Math.sin(Math.PI*x);
            Function<Double, Double> uTheoretical = x -> Math.sin(Math.PI*x);
            SolutionResult result = finiteVolumeSolver.solve(params, f, 1000, 1e-6);
            final int NUM_POINTS = params.getNumPoints();
            double[] yTheoricalValues = new double[NUM_POINTS];
            double[] errorValues = new double[NUM_POINTS];
            for (int i = 0; i < NUM_POINTS; i++) {
                double x = result.getxValues().get(i);
                double expectedU = uTheoretical.apply(x);
                yTheoricalValues[i] = expectedU;
                errorValues[i] = Math.abs(expectedU - result.getuValues().get(i)) ;
            }
            double[] xValues = result.getxValues().stream().mapToDouble(Double::doubleValue).toArray();
            double[] yNumericValues = result.getuValues().stream().mapToDouble(Double::doubleValue).toArray();
            for (int i = 0; i < NUM_POINTS; i++) {
                System.out.println("Valeur theorique : "+yTheoricalValues[i]+" Valeur numerique : "+yNumericValues[i]+" Erreur : "+errorValues[i] );
            }
            System.out.println("Convergence atteinte apres : "+result.getIterations()+" iterations");
            SwingUtilities.invokeLater(() -> {
                try {
                    JFrame frameFunctions = new JFrame("Fonctions Numérique et Théorique (u(x) = x^3)");
                    frameFunctions.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameFunctions.add(new GraphiqueFonctions(xValues, yNumericValues, yTheoricalValues));
                    frameFunctions.pack();
                    frameFunctions.setLocationRelativeTo(null);
                    frameFunctions.setVisible(true);

                    JFrame frameError = new JFrame("Fonction d'Erreur");
                    frameError.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameError.add(new GraphiqueErreur(xValues, errorValues));
                    frameError.pack();
                    frameError.setLocation(frameFunctions.getX() + frameFunctions.getWidth() + 20, frameFunctions.getY());
                    frameError.setVisible(true);
                } catch (HeadlessException e) {
                    e.printStackTrace();
                }
            });
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | ArithmeticException e) {
            return ResponseEntity.badRequest().body(null); // Ou un objet d'erreur plus détaillé
        }
    }
}

