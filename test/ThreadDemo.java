import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class Clock {
   
    
     public static DatagramSocket serversocket;
    public static DatagramPacket dp;
    public static BufferedReader dis;

    public static InetAddress ia;
    public static byte buf[] = new byte[1024];
    public static int cport = 789,sport=790;
    
    
    public static void main(String[] args) throws IOException {
        int x = 2; // wait 2 seconds at most
        
         serversocket = new DatagramSocket(sport);
        dp = new DatagramPacket(buf,buf.length);
        dis = new BufferedReader
        (new InputStreamReader(System.in));
        ia = InetAddress.getLocalHost();
        System.out.println(" CLIENT...");
      
        
//        String str = new String(dp.getData(), 0,dp.getLength());            
//        System.out.println("SERVER: " + str);
//        String str1 = new String(dis.readLine());
//        buf = str1.getBytes();
//        serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));

BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
long startTime = System.currentTimeMillis();
while ((System.currentTimeMillis() - startTime) < x * 1000 && !serversocket.receive(dp)) {
}

if (in.ready()) {
    System.out.println("You entered: " + in.readLine());
} else {
    System.out.println("You did not enter data");
} 
     }
}