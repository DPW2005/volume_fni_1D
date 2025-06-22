package com.ananum.volumefini.controller;

import com.ananum.volumefini.service.Graphique;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ananum.volumefini.model.EquationParameters;
import com.ananum.volumefini.model.SolutionResult;
import com.ananum.volumefini.service.FiniteVolumeSolver;

import java.util.function.Function;

@RestController
@RequestMapping("/solve")
public class EquationSolverController {

    private final FiniteVolumeSolver solver;
    private final Graphique graphique;

    public EquationSolverController(FiniteVolumeSolver solver, Graphique graphique) {
        this.solver = solver;
        this.graphique = graphique;
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
            //Fonction solution theorique
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

    @PostMapping(value = "/plot/solution", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getSolutionPlot(@RequestBody EquationParameters params) {
        try {
            // Même logique que pour la résolution, il faut définir f(x) et u_theorique(x)
            Function<Double, Double> f = x -> 0.0;
            Function<Double, Double> uTheorique = x -> x;

            int maxIterations = 1000;
            double tolerance = 1e-6;

            SolutionResult result = solver.solve(params, f, maxIterations, tolerance);

            byte[] imageBytes = graphique.generateComparisonChart(
                    result.getxValues(),
                    result.getuValues(),
                    uTheorique,
                    "Comparaison Solution Numérique vs Théorique",
                    "Position (x)",
                    "Valeur (u)"
            );
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);

        } catch (IllegalArgumentException | ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/plot/error", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getErrorPlot(@RequestBody EquationParameters params) {
        try {
            // Même logique que pour la résolution, il faut définir f(x) et u_theorique(x)
            Function<Double, Double> f = x -> 0.0;
            Function<Double, Double> uTheorique = x -> x;

            int maxIterations = 1000;
            double tolerance = 1e-6;

            SolutionResult result = solver.solve(params, f, maxIterations, tolerance);

            byte[] imageBytes = graphique.generateErrorChart(
                    result.getxValues(),
                    result.getuValues(),
                    uTheorique,
                    "Erreur Absolue |U_num - U_théo|",
                    "Position (x)",
                    "Erreur"
            );
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);

        } catch (IllegalArgumentException | ArithmeticException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

