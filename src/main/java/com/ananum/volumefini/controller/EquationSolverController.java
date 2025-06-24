package com.ananum.volumefini.controller;

import com.ananum.volumefini.service.GaussSeidelSolver;
import com.ananum.volumefini.service.Graphique;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ananum.volumefini.model.EquationParameters;
import com.ananum.volumefini.model.SolutionResult;
import com.ananum.volumefini.service.FiniteVolumeSolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
            Function<Double, Double> f = x -> -Math.exp(x);
            Function<Double, Double> uTheoretical = Math::exp;
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
            Graphique graphique = new Graphique();
            graphique.generateFunctionChart(xValues,yNumericValues,yTheoricalValues,"expo",NUM_POINTS);
            graphique.generateErrorChart(xValues,errorValues,"expo",NUM_POINTS);
            double currentMaxError = 0.0 ;
            for(double error : errorValues) {
                currentMaxError = Math.max(currentMaxError, Math.abs(error));
            }
            System.out.println("L'erreur maximale est : "+ currentMaxError );
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | ArithmeticException | IOException e) {
            return ResponseEntity.badRequest().body(null); // Ou un objet d'erreur plus détaillé
        }
    }

    public void convergence(EquationParameters params,Function<Double,Double> f,Function<Double,Double> uTheorique) throws IOException {
        int[] nombreATester = {10,20,40,80,160,320} ;
        Graphique graphique = new Graphique();
        List<Integer> nombrePoint = new ArrayList<>() ;
        List<Double> maxError = new ArrayList<>() ;
        for (int N : nombreATester) {
            System.out.println("Résolution pour N = " + N + " points internes...");
            params.setNumPoints(N);
            SolutionResult result = solver.solve(params, f, 1000, 1e-6);
            double currentMaxError = 0.0;
            List<Double> xValues = result.getxValues();
            List<Double> yNumericValues = result.getuValues();
            for (int i = 0; i < xValues.size(); i++) {
                double theoreticalValue = uTheorique.apply(xValues.get(i));
                currentMaxError = Math.max(currentMaxError, Math.abs(yNumericValues.get(i) - theoreticalValue));
            }
            nombrePoint.add(N);
            maxError.add(currentMaxError);
            System.out.println("  Erreur max pour N=" + N + " : " + currentMaxError);
        }
        Collections.reverse(maxError);
        graphique.generateErrorChart(nombrePoint.stream().mapToDouble(Integer::intValue).toArray(),maxError.stream().mapToDouble(Double::doubleValue).toArray(),"Convergence", 0) ;
        System.out.println("Analyse de convergence terminée. Graphique enregistré.");

    }
}

