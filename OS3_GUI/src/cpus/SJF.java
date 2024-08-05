package cpus;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.*;

public class SJF extends JFrame{
	//frame
	public final int PWIDTH=600; //width of panel
	public final int PHEIGHT=500;//height of panel
	public int timewidth;//width of unit of time in panel
	public JPanel panel;//display schedueling
	public JTextPane textPane; //display names and colors
	//class
	public ArrayList<Process> ogarr;//original array
	public ArrayList<Process> arr;//array used for editing
	public LinkedList<Process> readyQueue;
	public int runningtime;
	public int contextswitch;
	public Process curruntProcess;
	SJF(ArrayList<Process> p) {
		runningtime = 0;
		timewidth=0;
		arr = new ArrayList<Process>();
		ogarr=new ArrayList<Process>();
		for (int i = 0; i < p.size(); i++) {
			arr.add(p.get(i));
			ogarr.add(p.get(i));
			timewidth+=p.get(i).burst+contextswitch;
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
			textPane.setText(textPane.getText()+ogarr.get(i).name+"\t"+(3380+i)+"\t"+ogarr.get(i).priority+"\t"+ogarr.get(i).colorstr+"\n");}
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
				g.setColor(ogarr.get(j).color);
				//color used time for process
				g.fillRect(50+ogarr.get(j).start*timewidth, 50+50*(j+1)-10, timewidth*ogarr.get(j).burst, 20);
				
			}
		}

	public void addProcess(Process process) {
		readyQueue.add(process);
	}

	public void run() {	
		System.out.print(runningtime + " " + arr.get(0).name + " ");
		//take first arrived and put it in cpu
		arr.get(0).start=runningtime;
		runningtime = arr.get(0).arrival + arr.get(0).burst + contextswitch;
		arr.get(0).completion = runningtime - contextswitch;
		arr.get(0).turnaround = arr.get(0).completion - arr.get(0).arrival;
		arr.get(0).waiting = arr.get(0).turnaround - arr.get(0).burst;
		arr.remove(0);
		Collections.sort(arr, new ProcessComparatorBurst());//sort data according to burst
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).arrival < runningtime) {//check if any process arrived
				addProcess(arr.get(i));
				arr.remove(arr.get(i));
			}
			while (!readyQueue.isEmpty()) {
				curruntProcess = readyQueue.poll();//takes head of queue which is sorted
				System.out.print(runningtime + " " + curruntProcess.name + " ");
				curruntProcess.start=runningtime;
				runningtime += curruntProcess.burst + contextswitch;
				curruntProcess.completion = runningtime - contextswitch;
				curruntProcess.turnaround = curruntProcess.completion - curruntProcess.arrival;
				curruntProcess.waiting = curruntProcess.turnaround - curruntProcess.burst;
				for (int j = 0; j < arr.size(); j++) {//check for arrival
					if (arr.get(j).arrival < runningtime) {
						addProcess(arr.get(j));//add to queue
						arr.remove(arr.get(j));//remove from array
					}
				}
			}
		}
		repaint();
	}
}
