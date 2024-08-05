package cpus;

import java.util.ArrayList;
import java.util.LinkedList;

public class SRTF {
	public ArrayList<Process> arr;
	public LinkedList<Process> readyQueue;
	int runningtime;

	SRTF(ArrayList<Process> p) {
		runningtime = 0;
		arr = new ArrayList<Process>();
		for (int i = 0; i < p.size(); i++) {
			arr.add(p.get(i));
		}
		readyQueue = new LinkedList<Process>();
	}

	public Process leastProcess() {
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
		addProcess(arr.get(0));
		arr.remove(arr.get(0));
		while (!readyQueue.isEmpty()) {
			Process currProcess = leastProcess();
			readyQueue.remove(currProcess);
			runningtime++;
			currProcess.remainingburst--;
			for (int j = 0; j < arr.size(); j++) {
				if (arr.get(j).arrival <= runningtime) {
					addProcess(arr.get(j));
					arr.remove(arr.get(j));
				}
			}
			System.out.println(runningtime + " " + currProcess.name + " " + currProcess.remainingburst);
			if (currProcess.remainingburst != 0) {
				readyQueue.add(currProcess);
			} else {
				currProcess.completion = runningtime;
				currProcess.turnaround = currProcess.completion - currProcess.arrival;
				currProcess.waiting = currProcess.turnaround - currProcess.burst;
			}
		}
	}
}
