package cpus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class SJF {
	public ArrayList<Process> arr;
	public LinkedList<Process> readyQueue;
	int runningtime;
	int contextswitch;

	SJF(ArrayList<Process> p) {
		runningtime = 0;
		arr = new ArrayList<Process>();
		for (int i = 0; i < p.size(); i++) {
			arr.add(p.get(i));
		}
		readyQueue = new LinkedList<Process>();
	}

	public void addProcess(Process process) {
		readyQueue.add(process);
	}

	public void run() {
		System.out.print(runningtime + " " + arr.get(0).name + " ");
		runningtime = arr.get(0).arrival + arr.get(0).burst + contextswitch;
		arr.get(0).completion = runningtime - contextswitch;
		arr.get(0).turnaround = arr.get(0).completion - arr.get(0).arrival;
		arr.get(0).waiting = arr.get(0).turnaround - arr.get(0).burst;
		arr.remove(0);
		Collections.sort(arr, new ProcessComparatorBurst());
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).arrival < runningtime) {
				addProcess(arr.get(i));
				arr.remove(arr.get(i));
			}
			while (!readyQueue.isEmpty()) {
				Process curruntProcess = readyQueue.poll();
				System.out.print(runningtime + " " + curruntProcess.name + " ");
				runningtime += curruntProcess.burst + contextswitch;
				curruntProcess.completion = runningtime - contextswitch;
				curruntProcess.turnaround = curruntProcess.completion - curruntProcess.arrival;
				curruntProcess.waiting = curruntProcess.turnaround - curruntProcess.burst;
				for (int j = 0; j < arr.size(); j++) {
					if (arr.get(j).arrival < runningtime) {
						addProcess(arr.get(j));
						arr.remove(arr.get(j));
					}
				}
			}
		}

	}
}
