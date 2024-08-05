package cpus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class ProcessComparatorBurst implements Comparator<Process> {
	@Override
	public int compare(Process p1, Process p2) {
		if (p1.burst < p2.burst) {
			return -1;
		} else if (p1.burst == p2.burst) {
			return Integer.compare(p1.arrival, p2.arrival);
		}
		return 1;
	}
}
class ProcessComparatorPriority implements Comparator<Process> {
	@Override
	public int compare(Process p1, Process p2) {
		if (p1.priority < p2.priority) {
			return -1;
		} else if (p1.priority == p2.priority) {
			return Integer.compare(p1.burst, p2.burst);
		}
		return 1;
	}
}
public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("1-SJF\n2-SRTF\n3-priority\n4-AG\n");
		int n = sc.nextInt();
		System.out.println("enter number of processess: ");
		int num = sc.nextInt();
		ArrayList<Process> processes = new ArrayList<Process>();
		int q = 0;
		if (n == 4) {
			System.out.println("enter quantum: ");
			q = sc.nextInt();
		}
		System.out.println("enter each process name,burst time, arrival time, priority:\n");

		for (int i = 0; i < num; i++) {
			if (n == 4) {
				processes.add(new Process(sc.next(), sc.nextInt(), sc.nextInt(), sc.nextInt(), q));
			} else {
				processes.add(new Process(sc.next(), sc.nextInt(), sc.nextInt(), sc.nextInt()));
			}
		}
		if (n == 1) {
			System.out.println("enter context switch: ");
			int c = sc.nextInt();
			SJF Sjf = new SJF(processes);
			Sjf.contextswitch = c;
			Sjf.run();
			System.out.println("to completion");
			print(processes);
		} else if (n == 2) {
			SRTF srtf = new SRTF(processes);
			srtf.run();
			print(processes);
		}else if (n == 3) {
			Priority pr= new Priority(processes);
			pr.run();
			System.out.println("to completion");
			print(processes);
		}
		else if (n == 4) {
			AGScheduling agscheduler = new AGScheduling(processes,q);
			agscheduler.start();
			print(processes);
		}
	}

	private static void print(ArrayList<Process> processes) {
		Float avrwait = 0f;
		Float avrturn = 0f;
		System.out.println("name\tcomp\tturn\twait");
		for (int i = 0; i < processes.size(); i++) {
			System.out.println(processes.get(i).name + '\t' + processes.get(i).completion + '\t'
					+ processes.get(i).turnaround + '\t' + processes.get(i).waiting + '\t');
			avrwait += processes.get(i).waiting;
			avrturn += processes.get(i).turnaround;
		}
		System.out.println("average waiting time= " + avrwait / processes.size());
		System.out.println("average turnaround time= " + avrturn / processes.size());
	}
}
