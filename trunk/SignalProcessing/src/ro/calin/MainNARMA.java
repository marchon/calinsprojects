package ro.calin;

import java.util.Scanner;

import ro.calin.narma.RTRL;

/**
 * @author Calin
 * 
 */
public class MainNARMA {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner source = new Scanner(MainNARMA.class
				.getResourceAsStream("/res/narma.txt"));
		RTRL rtrl = new RTRL(source, 7, 3, 200);

		double error = rtrl.learn();
		System.out.println("Final error is: " + error);

		source.close();
	}

}
