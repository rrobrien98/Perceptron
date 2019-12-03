package main;

import main.resources.Params;

public class Lab {

	public static void main(String[] args) {
		
		int input_nodes = Integer.parseInt(args[0]);
		int output_nodes = Integer.parseInt(args[1]);
		String filename = args[2];
		int iterations = Integer.parseInt(args[3]);
		double lr = Double.parseDouble(args[4]);
		
		Params params = new Params(input_nodes, output_nodes, filename, iterations, lr);
		NN test = new NN(params);
		
		
	}

}
