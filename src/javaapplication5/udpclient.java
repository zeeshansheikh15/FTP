package javaapplication5;


import java.io.*;
import java.net.*;
import java.util.Scanner;

class udpserver
{
    public static DatagramSocket serversocket;
    public static DatagramPacket dp;
    public static BufferedReader dis;

    public static InetAddress ia;
    public static byte buf[] = new byte[1024];
    public static int cport = 789,sport=790;
    
    public static void main(String args[]) throws IOException
    {
        
        serversocket = new DatagramSocket(sport);
        dp = new DatagramPacket(buf,buf.length);
        dis = new BufferedReader
        (new InputStreamReader(System.in));
        ia = InetAddress.getLocalHost();
        System.out.println(" CLIENT...");
      
        serversocket.receive(dp);
        String str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        String str1 = new String(dis.readLine());
        buf = str1.getBytes();
        serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
       
        serversocket.receive(dp);
        str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        str1 = new String(dis.readLine());
        buf = str1.getBytes();
        serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
             
        serversocket.receive(dp);
        str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        str1 = new String(dis.readLine());
        buf = str1.getBytes();
        serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
             
        serversocket.receive(dp);
        str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        str1 = new String(dis.readLine());
        buf = str1.getBytes();
        serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
       

            
        
        
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] recData = new byte[1024];
        int i =0;

        FileWriter file = new FileWriter("C:\\Users\\zeesh\\Desktop\\mmmm.txt");
        PrintWriter out = new PrintWriter(file);

        while(true)
        {
            DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
            serverSocket.receive(recPacket);
            String line = new String(recPacket.getData());
            System.out.println("\n Data: " + line);
            out.println(line);
            System.out.println("\nPacket" + ++i + " written to file\n");
            out.flush();
            
            serversocket.receive(dp);
            str = new String(dp.getData(), 0,dp.getLength());            
            System.out.println("SERVER: " + str);
            str1 = "RECEIVED";
            buf = str1.getBytes();
            serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
        }
        
    }
}