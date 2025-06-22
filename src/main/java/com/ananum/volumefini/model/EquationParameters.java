package com.ananum.volumefini.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquationParameters {
	
	private double a; // Coefficient de u''
    private double b; // Coefficient de u'
    private double c; // Coefficient de u
    private double xMin; // Borne inférieure du domaine
    private double xMax; // Borne supérieure du domaine
    private int nombrePoint; // Nombre de points de discrétisation
    private double limiteGauche; // Condition limite à gauche de Dirichlet
    private double limiteDroite; // Condition limite à droite de Dirichlet

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

	public int getNombrePoint() {
		return nombrePoint;
	}

	public void setNombrePoint(int nombrePoint) {
		this.nombrePoint = nombrePoint;
	}

	public double getLimiteGauche() {
		return limiteGauche;
	}

	public void setLimiteGauche(double limiteGauche) {
		this.limiteGauche = limiteGauche;
	}

	public double getLimiteDroite() {
		return limiteDroite;
	}

	public void setLimiteDroite(double limiteDroite) {
		this.limiteDroite = limiteDroite;
	}

	public EquationParameters(double a, double b, double c, double xMin, double xMax, int nombrePoint, double limiteGauche, double limiteDroite) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.xMin = xMin;
		this.xMax = xMax;
		this.nombrePoint = nombrePoint;
		this.limiteGauche = limiteGauche;
		this.limiteDroite = limiteDroite;
	}

	public EquationParameters() {
	}
}
