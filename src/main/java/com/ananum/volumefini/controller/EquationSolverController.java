package com.ananum.volumefini.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ananum.volumefini.model.EquationParameters;
import com.ananum.volumefini.model.SolutionResult;
import com.ananum.volumefini.service.FiniteVolumeSolver;

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
            // Pour l'exemple, utilisons une fonction constante f(x) = 0.
            java.util.function.Function<Double, Double> f = x -> 0.0; // Exemple: f(x) = x
            java.util.function.Function<Double, Double> g = x -> 0.0 ;

            int maxIterations = 1000;
            double tolerance = 1e-6;

            SolutionResult result = solver.solve(params, f, maxIterations, tolerance);
            for(int i = 0 ; i < result.getxValues().size() ; i++) {
            	System.out.println("Valeur exacte : "+g.apply(result.getxValues().get(i))+" Valeur approchee : "+result.getuValues().get(i));
            }
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | ArithmeticException e) {
            return ResponseEntity.badRequest().body(null); // Ou un objet d'erreur plus détaillé
        }
    }
}

