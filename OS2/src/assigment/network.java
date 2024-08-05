package assigment;
/*
 Name:Youssef Mohamed Mohamed Ezzat
 ID:20200688
 Name:Youssef Ehab Maher Mohamed
 ID:20210465
 */
import java.io.File;
import java.io.FileWriter;
import java.util.*; 
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
public class network 
{
	public static String logFile = "logs.txt";
    public static File file = new File(logFile);//initializing the log file
    public static void writeLog(String print)//function to write in the log file
    {
        try
        {
            
            FileWriter writer = new FileWriter(file, true);

            writer.write(print + "\r\n");
            writer.close();
        }
        catch(Exception e) 
        {
            System.out.println("trouble opening file:" + logFile);
            System.exit(1);
        }
   }
    public static void main(String[] args) 
    {
    	
        Scanner scannerInt     = new Scanner(System.in);
        Scanner scannerString  = new Scanner(System.in);
        ArrayList<String> devices=new ArrayList<String>();
        
        int connectionNumber;
        int devicesNumber;
        
        System.out.println("What is number of WI-FI Connections?");
        connectionNumber= scannerInt.nextInt();
        
        System.out.println("What is number of devices Clients want to connect?");
        devicesNumber = scannerInt.nextInt();
       
        String deviceString;//string for device name and type
        for (int  i = 0 ; i < devicesNumber ; i++)
        {
            deviceString = scannerString.nextLine();
            devices.add(deviceString);
        }
        router router = new router(connectionNumber, devices);
        router.run();//starts the connections
        scannerInt.close();
        scannerString.close();
    }
}
class semaphore 
{
    int value = 0;
    
      semaphore() 
    {
        this.value = 0;
    }

     semaphore(int val) 
    {
        this.value = val;
    }
     
    public synchronized void P(device d) //wait
    {
        this.value--;
        if(this.value < 0)
        {
            try 
            { 
            	System.out.println("- " + d.getDeviceName() + "(" + d.getDeviceType() + ") arrived and waiting.");
            	network.writeLog("- " + d.getDeviceName() + "(" + d.getDeviceType() + ") arrived and waiting.");
            	wait();
            }
            catch(Exception e) 
            {
                System.out.println(e.getMessage());
            }
        }
        else
        {
            System.out.println("- " + d.getDeviceName() + "(" + d.getDeviceType() + ") arrived.");
            network.writeLog("- " + d.getDeviceName() + "(" + d.getDeviceType() + ") arrived");
        }
    }

    public synchronized void V() //signal
    {
        this.value++;
        if(this.value <= 0)
        {
            notify();
        }
    }
}
class device extends Thread
{
    int min = 2;//minimum time for activity
    int max = 4;//maximum time for activity
    private String deviceName;
    private String deviceType;
    private router router;
    
    public device(String name, String type, router rout) 
    {
         deviceName = name;
         deviceType = type;
         router     = rout;
    }
    
    public String getDeviceName()
    {
        return  deviceName;
    }
    
    public String getDeviceType()
    {
        return  deviceType;
    }
    public void connect(int connectionNumber)
    {
    	try 
    	{
    		TimeUnit.MILLISECONDS.sleep(400);//wait for everyone to arrive before starting any connection
    	}
    	catch(InterruptedException e) 
    	{
    		System.out.println(e.getMessage());
    	}
    	System.out.println("- Connection " + connectionNumber + ": " +  deviceName +"(" + deviceType + ") "+ "Occupied.");
        network.writeLog("- Connection " + connectionNumber + ": " +  deviceName +"(" + deviceType + ") "+ "Occupied.");
    	System.out.println("- Connection " + connectionNumber + ": " +  deviceName +"(" + deviceType + ") "+ "Login.");
        network.writeLog("- Connection " + connectionNumber + ": " +  deviceName +"(" + deviceType + ") "+ "Login.");
                
    }
    
    public void performOnlineActivity(int connectionNumber)
    {
        System.out.println("- Connection " + connectionNumber + ": " +  deviceName +"(" + deviceType + ") "+"Performs online activity.");
         network.writeLog("- Connection " + connectionNumber + ": " +  deviceName +"(" + deviceType + ") "+"Performs online activity.");
        
        try 
        {
            Thread.sleep(1000*(int)((min + (Math.random() * ((max - min))))));//random time for the activity
        }
        catch(InterruptedException e) 
        {
            System.out.println(e.getMessage());
        }
    }
    
    public void logout(int connectionNumber)
    {
        System.out.println("- Connection " + connectionNumber + ": " +  deviceName +"(" + deviceType + ") "+ " Logged out.");
         network.writeLog("- Connection " + connectionNumber + ": " +  deviceName+" "+deviceType+ " Logged out.");
        
        router.releaseConnection(connectionNumber);
    }
    
    public void run()//called when router calls thread.start()
    {
        int connectionNumber = router.occupyConnection(this);
        
        connect(connectionNumber);
        performOnlineActivity(connectionNumber);
        logout(connectionNumber);
    }
     
};
class router 
{
	public ArrayList<String> devices;
	public Boolean[] emptyConnection;
	public semaphore semaphore;
	public int size;
	
	public router(int s, ArrayList<String> dev) 
	{
		devices              = dev;
		semaphore            = new semaphore(s);
		emptyConnection = new Boolean[s];
		size                 = s;
		
		for(int i = 0; i < size; i++) 
		{
			emptyConnection[i] = true;//initializes all connections to empty
		}
	}
	
	public int occupyConnection(device dev) 
	{
		semaphore.P(dev);
		
		int connectionNumber = 0;
		
		for(int i = 0; i < emptyConnection.length; i++) 
		{
			if(emptyConnection[i]) 
			{
				connectionNumber = i + 1;
				emptyConnection[i] = false;//connection occupied by a device
				break;
			}
		}
		
		return connectionNumber;
	}
	
	public void releaseConnection(int connectionNumber) 
	{
		emptyConnection[connectionNumber - 1] = true;//connection is now empty after log out
		semaphore.V();
	}
	
	public void run() 
	{
		
		for(int i = 0;i < devices.size();i++) 
		{
			String[] arr= devices.get(i).split(" ");//split to device name and type
			String deviceName = arr[0];
			String deviceType = arr[1];
			new Thread(new device(deviceName, deviceType, this)).start();//calls device.run()
			try {
				TimeUnit.MILLISECONDS.sleep(100);//wait for device to arrive before another device enters
			} catch (InterruptedException e) {
				e.printStackTrace();
			}            	
		}
	}
	
}