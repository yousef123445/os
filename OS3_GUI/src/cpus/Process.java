package cpus;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class Process {
	// input
	public String name;
	public int burst;
	public int arrival;
	public int priority;
	public String colorstr;
	public Color color;
	// AG
	public int quantum;
	public int agfactor;
	public int remainingquantum;
	// output
	public int start;
	ArrayList<Integer> startarr;
	public int remainingburst;
	public int completion;
	public int turnaround;
	public int waiting;

	Process(String n, int b, int a, int p,String c) {
		//hashmap to change color string to color in GUI
		HashMap<String, Color> myColorMap = new HashMap<String,Color>();
	    myColorMap.put("red", Color.RED);
	    myColorMap.put("orange", Color.ORANGE);
	    myColorMap.put("yellow", Color.YELLOW);
	    myColorMap.put("green", Color.GREEN);
	    myColorMap.put("blue", Color.BLUE);
	    myColorMap.put("magenta", Color.MAGENTA);
	    myColorMap.put("pink", Color.PINK);
	    myColorMap.put("cyan", Color.CYAN);
	    myColorMap.put("gray", Color.GRAY);
	    startarr=new ArrayList<Integer>();
		name = n;
		burst = b;
		arrival = a;
		priority = p;
		colorstr=c;
		color=myColorMap.get(c);
		remainingburst = burst;
	}

	Process(String n, int b, int a, int p, int q,String c) {
		HashMap<String, Color> myColorMap = new HashMap<String,Color>();
	    myColorMap.put("red", Color.RED);
	    myColorMap.put("orange", Color.ORANGE);
	    myColorMap.put("yellow", Color.YELLOW);
	    myColorMap.put("green", Color.GREEN);
	    myColorMap.put("blue", Color.BLUE);
	    myColorMap.put("magenta", Color.MAGENTA);
	    myColorMap.put("pink", Color.PINK);
	    myColorMap.put("cyan", Color.CYAN);
	    myColorMap.put("gray", Color.GRAY);
		name = n;
		burst = b;
		colorstr=c;
		color=myColorMap.get(c);
		remainingburst = burst;
		arrival = a;
		priority = p;
		quantum = q;
	}

}
