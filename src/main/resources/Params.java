package main.resources;
/*
 * class to hold all the parameters needed to construct a neural net
 */
public class Params {

	private int input_nodes;
	private int output_nodes;
	private String filename;
	private int iterations;
	private double lr;
	private int tests;
	private String testfile;
	
	public Params(int input_nodes, int output_nodes, String filename, int iterations, double lr, int tests, String testfile) {
		this.input_nodes = input_nodes;
		this.output_nodes = output_nodes;
		this.filename=filename;
		this.iterations=iterations;
		this.lr = lr;
		this.tests = tests;
		this.testfile = testfile;
		
	}

	public String getFilename() {
		return filename;
	}
	public int getIterations() {
		return iterations;
	}

	public int getOutput_nodes() {
		return output_nodes;
	}

	public int getInput_nodes() {
		return input_nodes;
	}

	public double getLr() {
		return lr;
	}

	public int getTests() {
		return tests;
	}

	public String getTestfile() {
		return testfile;
	}


}
