package cpus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.awt.*;
import javax.swing.*;

public class SRTF extends JFrame{
	public ArrayList<Process> ogarr;//original array
	public ArrayList<Process> arr;//array used for editing
	public LinkedList<Process> readyQueue;
	int runningtime;
	
	public final int PWIDTH=600; //width of panel
	public final int PHEIGHT=500;//height of panel
	public int timewidth;//width of unit of time in panel
	public JPanel panel;//display schedueling
	public JTextPane textPane; //display names and colors
	SRTF(ArrayList<Process> p) {
		runningtime = 0;
		ogarr=new ArrayList<Process>();
		arr = new ArrayList<Process>();
		for (int i = 0; i < p.size(); i++) {
			arr.add(p.get(i));
			ogarr.add(p.get(i));
			timewidth+=p.get(i).burst;
		}
		timewidth=(int) Math.floor((PWIDTH-100)/timewidth);
		readyQueue = new LinkedList<Process>();
		
		panel=new JPanel();
		panel.setBackground(Color.black);
		panel.setBounds(10,10, PWIDTH,PHEIGHT);
		panel.setFocusable(false);
		textPane= new JTextPane();
		textPane.setBackground(Color.black);
		textPane.setForeground(Color.white);
		textPane.setFocusable(false);
		textPane.setFont(new Font("Calibri", Font.PLAIN, 12));
		textPane.setBounds(PWIDTH+10, 10, 280, PHEIGHT);
		textPane.setText("Name\tpid\tpriority\tColor\n");
		for (int i = 0; i < ogarr.size(); i++) {
			textPane.setText(textPane.getText()+ogarr.get(i).name+"\t"+(3380+i)+"\t"+ogarr.get(i).priority+"\t"+ogarr.get(i).colorstr+"\n");
		}
		textPane.setEditable(false);
		
		this.pack();
		this.setLayout(null);
		this.setBackground(Color.green);
		this.setResizable(false);
		this.setBounds(100, 100, 900, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(panel);
		this.add(textPane);
		this.setVisible(true);
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.white);
		g.setFont(new Font(null,Font.PLAIN,20));
		g.drawString("time", PWIDTH-50, 50);
		g.drawLine(50, 50, PWIDTH-50, 50);
		g.drawLine(50, 50, 50, PHEIGHT-50);
		for (int i = 1; i < ogarr.size()+1;i++) {
			g.drawString(ogarr.get(i-1).name, 25, 50+50*i);
			g.drawLine(50, 50+50*i, PWIDTH-50, 50+50*i);
		}
		for (int j = 0; j < ogarr.size(); j++) {
			//color used time for process
			for (int i = 0; i < ogarr.get(j).startarr.size(); i++) {
				g.setColor(ogarr.get(j).color);
				g.fillRect(50+ogarr.get(j).startarr.get(i)*timewidth, 50+50*(j+1)-10, timewidth, 20);
			}
		}
	}
	public Process leastProcess() {//get process with least remaining burst
		int min = readyQueue.get(0).remainingburst;
		int ind = 0;
		for (int i = 0; i < readyQueue.size(); i++) {
			if (min > readyQueue.get(i).remainingburst) {
				min = readyQueue.get(i).remainingburst;
				ind = i;
			}
		}
		return readyQueue.get(ind);
	}

	public void addProcess(Process process) {
		readyQueue.add(process);
	}

	public void run() {
		runningtime = arr.get(0).arrival;
		addProcess(arr.get(0));//add first process queue
		arr.remove(arr.get(0));//remove it from array
		Collections.sort(arr, new ProcessComparatorBurst());//sort data according to burst
		while (!readyQueue.isEmpty()) {
			Process currProcess = leastProcess();//get process with least remaining burst
			readyQueue.remove(currProcess);//remove it from queue
			currProcess.startarr.add(runningtime);
			runningtime++;//move 1 sec
			currProcess.remainingburst--;//decrease remaining burst
			for (int j = 0; j < arr.size(); j++) {//check for arrival
				if (arr.get(j).arrival <= runningtime) {
					addProcess(arr.get(j));//add to queue
					arr.remove(arr.get(j));//remove from array
				}
			}
			System.out.println(runningtime + " " + currProcess.name + " " + currProcess.remainingburst);
			if (currProcess.remainingburst != 0) {//if process didnt finish 
				readyQueue.add(currProcess);//return to queue
			} else {
				currProcess.completion = runningtime;
				currProcess.turnaround = currProcess.completion - currProcess.arrival;
				currProcess.waiting = currProcess.turnaround - currProcess.burst;
			}
		}
	}
}
