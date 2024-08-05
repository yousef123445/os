package cpus;

public class Process {
	// input
	public String name;
	public int burst;
	public int arrival;
	public int priority;
	// AG
	public int quantum;
	public int agfactor;
	public int remainingquantum;
	// output
	public int remainingburst;
	public int completion;
	public int turnaround;
	public int waiting;

	Process(String n, int b, int a, int p) {
		name = n;
		burst = b;
		arrival = a;
		priority = p;
		remainingburst = burst;
	}

	Process(String n, int b, int a, int p, int q) {
		name = n;
		burst = b;
		remainingburst = burst;
		arrival = a;
		priority = p;
		quantum = q;
		remainingquantum=quantum;
	}

}
