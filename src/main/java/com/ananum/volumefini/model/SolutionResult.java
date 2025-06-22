package com.ananum.volumefini.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolutionResult {

	private List<Double> xValues; // Discretisation des valeurs su rle domaine
    private List<Double> uValues; // Valeurs discretes de la fonction sur le domaine
    private int iterations; // Nombre d'iterations effectue pour attendre la convergence
    private double tolerance; // Tolerance a l'erreur admise

	public List<Double> getxValues() {
		return xValues;
	}

	public void setxValues(List<Double> xValues) {
		this.xValues = xValues;
	}

	public List<Double> getuValues() {
		return uValues;
	}

	public void setuValues(List<Double> uValues) {
		this.uValues = uValues;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public SolutionResult(List<Double> xValues, List<Double> uValues, int iterations, double tolerance) {
		this.xValues = xValues;
		this.uValues = uValues;
		this.iterations = iterations;
		this.tolerance = tolerance;
	}
	public SolutionResult() {
	}


}
