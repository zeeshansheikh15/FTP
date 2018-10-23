package javaapplication5;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Scanner;
class udpclient
{
    
public static DatagramSocket clientsocket;
public static DatagramPacket dp;
public static BufferedReader dis;
public static InetAddress IpAddress;
public static byte buf[] = new byte[1024];
public static int cport = 789, sport = 790;   
    
static int serverPort;
static String filename;
    public static void main(String args[]) throws SocketException, IOException
    {
        int count=0;
        int MAX_SIZE = 48;
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IpAddress = InetAddress.getByName("localhost");
         
         clientsocket = new DatagramSocket(cport);
         dp = new DatagramPacket(buf, buf.length);         
        System.out.println(" SERVER...");
        
        String str = "ENTER HOST";
        clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
        clientsocket.receive(dp);
        String str2 = new String(dp.getData(), 0,dp.getLength());
        System.out.println("CLIENT: " + str2);    
        
        str = "ENTER PORT";
        clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
        clientsocket.receive(dp);
        str2 = new String(dp.getData(), 0,dp.getLength());
        System.out.println("CLIENT: " + str2);    
        
        str = "ENTER COMMAND";
        clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
        clientsocket.receive(dp);
        str2 = new String(dp.getData(), 0,dp.getLength());
        System.out.println("CLIENT: " + str2); 
        
        str = "ENTER DIRECTORY";
        clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
        clientsocket.receive(dp);
        str2 = new String(dp.getData(), 0,dp.getLength());
        System.out.println("CLIENT: " + str2);
        
        
        
        
           
        byte[] sendData = new byte[MAX_SIZE];
        String filePath = "C:\\Users\\zeesh\\Desktop\\aa.txt";
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        int totLength = 0; 
        while((count = fis.read(sendData)) != -1)    //calculate total length of file
        {
            totLength += count;
        }
        System.out.println("Total Length :" + totLength);
        int noOfPackets = totLength/MAX_SIZE;
        System.out.println("No of packets : " + noOfPackets);
        int off = noOfPackets * MAX_SIZE;  //calculate offset. it total length of file is 1048 and array size is 1000 den starting position of last packet is 1001. this value is stored in off.
        int lastPackLen = totLength - off;
        System.out.println("\nLast packet Length : " + lastPackLen);
        byte[] lastPack = new byte[lastPackLen-1];  //create new array without redundant information
        fis.close();
        FileInputStream fis1 = new FileInputStream(file);
        while((count = fis1.read(sendData)) != -1 )
        { 
            if(noOfPackets<=0)
                break;
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IpAddress, 9876);
            clientSocket.send(sendPacket);
            noOfPackets--;
            
            str = "SENT";
            clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
            clientsocket.receive(dp);
            str2 = new String(dp.getData(), 0,dp.getLength());
            System.out.println("CLIENT: " + str2);
        }
        lastPack = Arrays.copyOf(sendData, lastPackLen);
         DatagramPacket sendPacket1 = new DatagramPacket(lastPack, lastPack.length, IpAddress,     9876);
        clientSocket.send(sendPacket1);

    }
}
