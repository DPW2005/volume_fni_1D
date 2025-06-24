package com.ananum.volumefini;

import com.ananum.volumefini.model.EquationParameters;
import com.ananum.volumefini.model.GraphiqueErreur;
import com.ananum.volumefini.model.GraphiqueFonctions;
import com.ananum.volumefini.model.SolutionResult;
import com.ananum.volumefini.service.FiniteVolumeSolver;
import com.ananum.volumefini.service.GaussSeidelSolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;

@SpringBootApplication
public class VolumefiniApplication {

	public static void main(String[] args) {
		SpringApplication.run(VolumefiniApplication.class, args);
	}

}
