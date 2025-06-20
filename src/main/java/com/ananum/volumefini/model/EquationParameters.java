package com.ananum.volumefini.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquationParameters {
	
	private double a; // Coefficient de u''
    private double b; // Coefficient de u'
    private double c; // Coefficient de u
    private double xMin; // Borne inférieure du domaine
    private double xMax; // Borne supérieure du domaine
    private int numPoints; // Nombre de points de discrétisation
    private double uBoundaryLeft; // Condition limite à xMin
    private double uBoundaryRight; // Condition limite à xMax
	public double getA() {
		return a;
	}
	public void setA(double a) {
		this.a = a;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getC() {
		return c;
	}
	public void setC(double c) {
		this.c = c;
	}
	public double getxMin() {
		return xMin;
	}
	public void setxMin(double xMin) {
		this.xMin = xMin;
	}
	public double getxMax() {
		return xMax;
	}
	public void setxMax(double xMax) {
		this.xMax = xMax;
	}
	public int getNumPoints() {
		return numPoints;
	}
	public void setNumPoints(int numPoints) {
		this.numPoints = numPoints;
	}
	public double getuBoundaryLeft() {
		return uBoundaryLeft;
	}
	public void setuBoundaryLeft(double uBoundaryLeft) {
		this.uBoundaryLeft = uBoundaryLeft;
	}
	public double getuBoundaryRight() {
		return uBoundaryRight;
	}
	public void setuBoundaryRight(double uBoundaryRight) {
		this.uBoundaryRight = uBoundaryRight;
	}
	public EquationParameters(double a, double b, double c, double xMin, double xMax, int numPoints,
			double uBoundaryLeft, double uBoundaryRight) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.xMin = xMin;
		this.xMax = xMax;
		this.numPoints = numPoints;
		this.uBoundaryLeft = uBoundaryLeft;
		this.uBoundaryRight = uBoundaryRight;
	}
	public EquationParameters() {
		super();
	}

    
}
