package com.ananum.volumefini.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SolutionResult {

	private List<Double> xValues;
    private List<Double> uValues;
    private int iterations;
    private double toleranceAchieved;
    
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
	public double getToleranceAchieved() {
		return toleranceAchieved;
	}
	public void setToleranceAchieved(double toleranceAchieved) {
		this.toleranceAchieved = toleranceAchieved;
	}
	
	public SolutionResult(List<Double> xValues, List<Double> uValues, int iterations, double toleranceAchieved) {
		super();
		this.xValues = xValues;
		this.uValues = uValues;
		this.iterations = iterations;
		this.toleranceAchieved = toleranceAchieved;
	}
	public SolutionResult() {
		super();
	}

    
}
