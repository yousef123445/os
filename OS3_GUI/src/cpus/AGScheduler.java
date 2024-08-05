package cpus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.RepaintManager;

public class AGScheduler {
    private LinkedList<Process> readyQueue;//to linked list
    private List<Process> dieList;
    private int currentTime;
    private ArrayList<Process> processes;
    private ArrayList<Process> OGarr;
    public AGScheduler(ArrayList<Process> p) {
        readyQueue = new LinkedList<>();
        dieList = new ArrayList<>();
        currentTime = 0;
        processes=new ArrayList<Process>();
        OGarr=new ArrayList<Process>();
        for (int i = 0; i <p.size(); i++) {
			processes.add(p.get(i));
			OGarr.add(p.get(i));
		}
    }
    public Process getminag()
    {
    	int min=readyQueue.get(0).agfactor;
    	int ind=0;
    	for (int i = 0; i < readyQueue.size(); i++) {
			if(min>readyQueue.get(i).agfactor)
			{
				min=readyQueue.get(i).agfactor;
				ind =i;
			}
			}
    	return readyQueue.get(ind);
    }
    public int meanQuantum()
    {
    	int m=0;
    	for (int i = 0; i < OGarr.size(); i++) {
			m+=OGarr.get(i).quantum;
		}
    	m=(int) Math.ceil((m/OGarr.size())*0.1);
    	return m;
    }
    public void addProcess(Process process) {
        calculateAGFactor(process);
        readyQueue.add(process);
    }
    public void run() {
		currentTime=processes.get(0).arrival;
		int halfquantum= (int) Math.ceil(processes.get(0).quantum*0.5);
		currentTime+=halfquantum;
		processes.get(0).remainingburst-=halfquantum;
		addProcess(processes.get(0));
		processes.remove(processes.get(0));
		for (int j = 0; j < processes.size(); j++) {
			if (processes.get(j).arrival <= currentTime) {
				addProcess(processes.get(j));
				processes.remove(processes.get(j));
			}
		}
		while (!readyQueue.isEmpty()) {
			Process currProcess = getminag();
			readyQueue.remove(getminag());
			System.out.println(currentTime+" "+currProcess.name);
			if (readyQueue.isEmpty()&&!processes.isEmpty()) {
				currentTime++;
				currProcess.remainingburst--;
				currProcess.remainingquantum--;
				addProcess(currProcess);
				for (int j = 0; j < processes.size(); j++) {
					if (processes.get(j).arrival <= currentTime) {
						addProcess(processes.get(j));
						processes.remove(processes.get(j));
					}
				}
			} else if (currProcess.remainingburst == 0) {
				dieList.add(currProcess);
				currProcess.completion=currentTime;
				currProcess.turnaround = currProcess.completion - currProcess.arrival;
				currProcess.waiting = currProcess.turnaround - currProcess.burst;
				
			} else if (currProcess.agfactor > getminag().agfactor) {
				currProcess.quantum += currProcess.remainingquantum;
				currProcess.remainingquantum = currProcess.quantum;
				addProcess(currProcess);
			} else if (currProcess.remainingquantum == 0) {
				currProcess.quantum += meanQuantum();
				currProcess.remainingquantum = currProcess.quantum;
				addProcess(currProcess);
			}
		}
    }
		private void calculateAGFactor(Process p) {
			int rf = ((int) (Math.random() * 20));
			if (rf < 10) {
				p.agfactor = rf + p.arrival + p.burst;
			} else if (rf > 10) {
				p.agfactor = 10 + p.arrival + p.burst;
			} else {
				p.agfactor = p.priority + p.arrival + p.burst;
			}

		}
	}