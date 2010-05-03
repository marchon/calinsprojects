package ro.calin.narma;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RTRL {
	public static final double BETA = 0.5;
	public static final double ETA = 0.01;
	
	/**
	 * input vector
	 */
	private double[] s;
	
	/**
	 * perceptron input
	 * 
	 * len(u) = q + p + 1
	 * u(k) = [s(k-1), ..., s(k-p), 1, y(k-1), ..., y(k-q)]
	 */
	private double[] u;
	
	/**
	 * weight vector
	 * len(w) = p + q + 1
	 */
	private double[] w;
	
	/**
	 * output vector
	 */
	private double[] y;
	
	/**
	 * error vector
	 */
	private double[] e;
	
	/**
	 * PI matrix
	 * 
	 * 			(pi[1][k-1] ... pi[p+q+1][k-1])
	 * pi(k) = 	 .............................
	 * 			(pi[1][k-q] ... pi[p+q+1][k-q])
	 */
	private double[][] pi;
	
	/**
	 * nb of iterations
	 */
	private int ages;
	
	/**
	 * perceptron input parameters
	 */
	private int p;
	private int q;
	
	/**
	 * error after learning
	 */
	private double error;

	public double getError() {
		return error;
	}

	public RTRL(Scanner source, int p, int q, int ages) {
		this.p = p;
		this.q = q;
		this.ages = ages;
		
		List<Double> input = new ArrayList<Double>();
		
		//load numbers
		double num, num_min, num_max;
		num = num_min = num_max = source.nextDouble();
		input.add(num);
		
		while(source.hasNextDouble()) {
			num = source.nextDouble();
			if(num < num_min)
				num_min = num;
			if(num > num_max)
				num_max = num;
			
			input.add(num);
		}
		
		s = new double[input.size()];
		int i = 0;
		//move in (0, 1]
		for (Double nb : input) {
			s[i++] = (nb - num_min) / (num_max - num_min);
		}
		
		y = new double[input.size()];
		e = new double[input.size()];
		
		//init weight vector
		w = new double[p + q + 1];
		for (int j = 0; j < w.length; j++) {
			w[j] = 1.0 / (p + q + 1.0);
		}
		
		//init perceptron input
		// 0	   p		 p+q
		//[0,...0, 1, 0, ..., 0]
		u = new double[p + q + 1];
		u[p] = 1;
		
		pi = new double[q][p + q + 1];
	}
	
	/**
	 * @return error after learning
	 * purpose: minimize error
	 */
	public double learn() {
		double[] pi_temp = new double[p + q + 1];
		
		for (int l = 0; l < ages; l++) {
			error = 0;
			
			for (int t = 0; t < s.length; t++) {
				double net_k = 0;
				
				for (int i = 0; i < p + q + 1; i++)
					net_k += (w[i] * u[i]);
				
				//y[k]=phi(net(k))
				//phi(x) = 1 / [1 + e^(-beta * x)] - logistic function
				y[t] = 1.0 / (double) (1 + Math.exp((-1) * net_k * BETA));
				
				//phi'(x)= beta * exp(-beta * x) / (1 + exp(-beta * x))^2
				//phi'(net_k)
				double exp = Math.exp(-BETA * net_k);
				double phi_deriv = (BETA * exp) / Math.pow(1 + exp, 2);
				
				//calculate error as input - output
				e[t] = s[t] - y[t];
				
				//refresh pi matrix using new error
				for (int i = 0; i < p + q + 1; i++) {
					// pi[k]=phi'(net_k)(sum(j=1,q)w[p+j+1]pi[k-j][i]+u[k])
					double temp = 0;

					for (int j = 0; j < q; j++)
						temp += (w[j + p + 1] * pi[j][i]);
					pi_temp[i] = phi_deriv * (temp + u[i]);

					// update weight vector w[i][k+1]=w[i][k]+eta*e[k]*pi[i][k]
					w[i] = w[i] + ETA * e[t] * pi_temp[i];
				}

				// move lines down by one
				for (int i = q - 2; i >= 0; i--)
					for (int j = 0; j < p + q + 1; j++)
						pi[i + 1][j] = pi[i][j];
				
				// first line in pi matrix becomes pi_temp 
				for (int i = 0; i < p + q + 1; i++)
					pi[0][i] = pi_temp[i];

				//insert new input value in perceptron
				for (int i = p - 1; i > 0; i--)
					u[i] = u[i - 1];
				u[0] = s[t];

				//insert new output value in perceptron
				for (int i = p + q; i > p + 1; i--)
					u[i] = u[i - 1];
				u[p + 1] = y[t];

				//calculate instantaneous square error 
				error += e[t] * e[t] / 2;
			}
			error /= s.length;
			System.out.println("Error at age " + (l + 1) + ": " + error);
		}
		
		return error;
	}
}
