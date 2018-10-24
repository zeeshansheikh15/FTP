package javaapplication5;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class udpserver
{
    public static DatagramSocket serversocket;
    public static DatagramPacket dp;
    public static BufferedReader dis;

public static int attempts = 1;
    public static InetAddress ia;
    public static byte buf[] = new byte[1024];
    public static int cport = 789,sport=790;
    

    public static int host;
    public static int port;
    public static String directory;
    public static String command;

    static int serverPort;
    static String filename;
    
    
    
 public static void putfile(String dir) throws SocketException, UnknownHostException, IOException{
        File file;
        FileInputStream fis = null;
        int count=0;
        int MAX_SIZE = 48;
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IpAddress = InetAddress.getByName("localhost");
       byte[] sendData = new byte[MAX_SIZE];
        String filePath = dir;
        try{
        file = new File(filePath);
        fis = new FileInputStream(file);
        }catch(Exception e){
            String str = "INVALID DIRECTORY";
        serversocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, cport));
        return;
        }
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
            String packno = Integer.toString(noOfPackets- (totLength/MAX_SIZE));
            String str = "SYN"+packno;
            
            serversocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, cport));
            
            serversocket.setSoTimeout(500);
            try{
            serversocket.receive(dp);
            }catch(Exception e){
                System.out.println("Ack not received in Attempt  "+attempts+"  for packet  "+ (noOfPackets- (totLength/MAX_SIZE)));
                noOfPackets++;
                if(attempts>2){
                    break;
                }else{
                    attempts++;
                continue;
                }
            }
            String str2 = new String(dp.getData(), 0,dp.getLength());
            System.out.println("CLIENT: " + str2);
        }
        lastPack = Arrays.copyOf(sendData, lastPackLen);
         DatagramPacket sendPacket1 = new DatagramPacket(lastPack, lastPack.length, IpAddress,     9876);
        clientSocket.send(sendPacket1);
 }

    
    public static void getfile() throws IOException{
          
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] recData = new byte[48];
        int i =0;

        FileWriter file = new FileWriter("C:\\Users\\zeesh\\Desktop\\mmmm.txt");
        PrintWriter out = new PrintWriter(file);

        while(true)
        {
            DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
            serverSocket.receive(recPacket);
            String line = new String(recPacket.getData());
            out.println(line);
            out.flush();
            
            serversocket.setSoTimeout(3000);
            try{
            serversocket.receive(dp);
            }catch(Exception e){
                System.out.println("SYN not received : Connection Terminating");
                break;
            }
            String str = new String(dp.getData(), 0,dp.getLength());            
            System.out.println("SERVER: " + str);
            String str1 = "ACK";
            buf = str1.getBytes();
            serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
        }
    }
    
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
        host = Integer.parseInt(str1);
        buf = str1.getBytes();
        serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
       
        serversocket.receive(dp);
        str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        str1 = new String(dis.readLine());
        port = Integer.parseInt(str1);
        buf = str1.getBytes();
        serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
             
        serversocket.receive(dp);
        str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        str1 = new String(dis.readLine());
        command = str1;
        buf = str1.getBytes();
        serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
             
        serversocket.receive(dp);
        str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        str1 = new String(dis.readLine());
        directory = str1;
        buf = str1.getBytes();
        serversocket.send(new DatagramPacket(buf,str1.length(), ia, cport));
       
//        serversocket.setSoTimeout(500);
//        serversocket.receive(dp);
        str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        if(str.equalsIgnoreCase("INVALID DIRECTORY"))
            System.exit(0);
        
       if(command.equalsIgnoreCase("put")){
        udpserver.putfile(directory);
        }else if(command.equalsIgnoreCase("get")){
         udpserver.getfile();       
        }else if(command.equalsIgnoreCase("ls")){
               serversocket.receive(dp);
        str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        
                }else if(command.equalsIgnoreCase("cd")){
               serversocket.receive(dp);
        str = new String(dp.getData(), 0,dp.getLength());            
        System.out.println("SERVER: " + str);
        
                }
    }
}