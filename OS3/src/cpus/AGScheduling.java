package cpus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author Mahmoud Ahmed
 */
public class AGScheduling 
{
    private ArrayList<Process> processes = new ArrayList<Process>();
    private ArrayList<Process> processes2 = new ArrayList<Process>();
    private Queue<Process> readyQueue = new LinkedList<>();
    
    
    private ArrayList<Process> results = new ArrayList<Process>();
    
    public AGScheduling(ArrayList<Process> _processes, int _roundRobinTimeQuantum)
    {
        this.processes = _processes;
    }
    
    public boolean isFinished()
    {
        for(int i = 0; i < processes.size(); i++)
        {
            if(processes.get(i).quantum != 0)
                return false;
        }
        
        return true;
    }
    public Process getleastAG(int _time)
    {
        int minAG = Integer.MAX_VALUE;
        int index = 0;
       
        int i = 0;
        while( (i < this.processes.size()) && (this.processes.get(i).arrival <= _time) )
        {
            if(this.processes.get(i).agfactor < minAG && this.processes.get(i).quantum != 0)
            {
                minAG = this.processes.get(i).agfactor;
                index = i;
            }
            i++;
        }
        
        return this.processes.get(index);
    }
    
    public int getProcessIndex(Process _p)
    {
        for(int i = 0; i < processes.size(); i++)
        {
            if(processes.get(i).name == _p.name)
                return i;
        }
        return -1;
    }
    
    public boolean isLastProcess(Process _p, int _time)
    {
        int index = this.getProcessIndex(_p);
        boolean flag = true;
        
        if(index == this.processes.size() - 1)
            return true;
        
        index++;
        
        while((index < this.processes.size()) && this.processes.get(index).arrival <= _time)
        {
            if(this.processes.get(index).agfactor != 0 )
            {
                flag = false;
            }
            index++;
        }
        //System.out.println("isLastProcess  "+flag+ "  " +this.getProcessIndex(_p) + "isLastProcess");
        return flag;     
    }
    
    public Process getBestProcess(int _time, int _preIndex)
    {
        Process p = processes.get(_preIndex);
        
       
        
        if(this.isLastProcess(p, _time) && readyQueue.size() > 0)
        {
            Process temp = readyQueue.poll();
            
            while(true)
            {
                if(temp.remainingburst != 0)
                {
                    return temp;
                }
                
                temp = readyQueue.poll();
            }

        }
        else
        {
            Process temp = getleastAG(_time);
            return temp;
        }
    }
    
    public void setProcesses()
    {
        // Set Remaining Time for all processes (Remaining Time = burstTime), AG-Factor
        for(int i = 0; i < this.processes.size(); i++)
        {
            // Set Remaining Time
            this.processes.get(i).remainingburst = this.processes.get(i).burst; 
            
            // Set AG-Factor
            this.processes.get(i).agfactor = this.processes.get(i).priority +
                                             this.processes.get(i).arrival + 
                                             this.processes.get(i).burst ; 
        }
    }
    

    
    
    // return time
    public int nonPreemptiveAG(Process _p)
    {
        int nonPreemptiveAGTime = (int) Math.ceil(_p.quantum * 0.5);
        int minValue = Math.min(nonPreemptiveAGTime, _p.remainingburst);
        //System.out.println("nonPreemptiveAG " + minValue + " ---nonPreemptiveAGnonPreemptiveAGnonPreemptiveAG");
        return minValue;
    }
    
    // return time
    public int preemptiveAG(Process _p, int _time, int _executingQuantum)
    {
        int timeForThisProcess = 0;
        
        
        while(_p.remainingburst > 0 )
        {
            Process p = this.getleastAG(_time);
            
            if(p.name != _p.name)
                break;
            
            
            _p.remainingburst--;
            _time++;
            timeForThisProcess++;
            
            if(_p.quantum == timeForThisProcess + _executingQuantum)
                break;
            
            
        }
        
        return timeForThisProcess;
    }
    
    public int getceilOfMeanQuantum() 
    {
        double sum = 0.0;
        int n = 0;
        
        for (Process p : processes) 
        {
            sum += p.quantum;
            
            if(p.quantum != 0)
                ++n;
        }
        
        int ceilOfMeanQuantum = (int) Math.ceil((sum/n) * 0.1);
        
        return ceilOfMeanQuantum;
    }
  
    public void printResults()
    {
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;
        
        
        for(Process p: results)
        {
            // Set Turnaround Time
            p.turnaround = p.waiting + p.burst;
            
            System.out.println(p.name);
        }
      
        for(Process p: processes2)
        {

            totalTurnaroundTime += p.turnaround;
            totalWaitingTime += p.waiting;
        }
        
        System.out.println("AVG - Turnaround Time: " + (double) totalTurnaroundTime/processes2.size());
        System.out.println("AVG - Waiting Time: " + (double) totalWaitingTime/processes2.size());
    }
    public ArrayList<Process> start()
    {
        int processesCount = this.processes.size();
        
        
        
        // Set Remaining Time for all processes (Remaining Time = burstTime), AG-Factor
        this.setProcesses();
        
        int preIndex = -1;
        Process current = null;
        int currentIndex;
        
        
        for(int time  = this.processes.get(0).arrival; !this.isFinished(); )
        {
        	for(Process p: results)
            {
                System.out.println(p.name);
            }
            System.out.println(time);
            //ProcessGUI tempGUI = new ProcessGUI();
            int start = time;
            
            if(preIndex == -1)
            {
                current = this.processes.get(0);
                currentIndex = 0;
            }
            else
            {
                current = this.getBestProcess(time, preIndex);
                currentIndex = this.getProcessIndex(current);
            }
            
            if(this.processes.get(currentIndex).quantum != 0)
            {
                // Set Service Time
                this.processes.get(currentIndex).burst = time;
            }
            
            
            int nonPreemptiveAGTime = this.nonPreemptiveAG(current);
            time += nonPreemptiveAGTime;

            
            
            this.processes.get(currentIndex).remainingburst -= nonPreemptiveAGTime;
                
            
            int preemptiveAGTime = this.preemptiveAG(current, time, nonPreemptiveAGTime);
            time += preemptiveAGTime;

            // Update Quantum
            if(current.remainingburst == 0)
            {
                this.processes2.add(current); 
                // The running process finished its job
                this.processes.get(currentIndex).quantum = 0;
            }
            else if((nonPreemptiveAGTime + preemptiveAGTime) == current.quantum)
            {
                // The running process used all its quantum time
                this.processes.get(currentIndex).quantum += getceilOfMeanQuantum();
                readyQueue.add(current);
            }
            else
            {
                int total = (this.processes.get(currentIndex).quantum - (nonPreemptiveAGTime + preemptiveAGTime));
                this.processes.get(currentIndex).quantum += total;
               
                readyQueue.add(current);
            }
            
            int end = time;
            
            
            // Set waiting Time
            int wT = this.processes.get(currentIndex).burst - this.processes.get(currentIndex).arrival;
            this.processes.get(currentIndex).waiting += wT;
            this.processes.get(currentIndex).arrival = time;
                

            preIndex = this.getProcessIndex(current);
            
            
            
            results.add(current);
        }  
        
        // Print Results with AVG
        this.printResults();
       
        return results;
    }
    
    
}
