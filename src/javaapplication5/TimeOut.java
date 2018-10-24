package javaapplication5;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import static javaapplication5.udpserver.serversocket;
class udpclient
{
public static int attempts = 1;    
public static DatagramSocket clientsocket;
public static DatagramPacket dp;
public static BufferedReader dis;
public static InetAddress IpAddress;
public static byte buf[] = new byte[1024];
public static int cport = 789, sport = 790;  

public static int host;
public static int port;
public static String directory;
public static String command;
    
static int serverPort;
static String filename;


  
 public static void putfile() throws IOException{
          
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] recData = new byte[48];
        int i =0;

        FileWriter file = new FileWriter("C:\\Users\\zeesh\\Desktop\\server.txt");
        PrintWriter out = new PrintWriter(file);

        while(true)
        {
            DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
            serverSocket.receive(recPacket);
            String line = new String(recPacket.getData());
            out.println(line);
            out.flush();
            
            clientsocket.setSoTimeout(3000);
            try{
            clientsocket.receive(dp);
            }catch(Exception e){
                System.out.println("SYN not received : Connection Terminating");
                break;
            }
            String str = new String(dp.getData(), 0,dp.getLength());            
            System.out.println("SERVER: " + str);
            String str1 = "ACK";
            buf = str1.getBytes();
            clientsocket.send(new DatagramPacket(buf,str1.length(), IpAddress, sport));
        }
    }      

 public static void getfile(String dir) throws SocketException, UnknownHostException, IOException{
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
        clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
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
            
            clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
            
            clientsocket.setSoTimeout(500);
            try{
            clientsocket.receive(dp);
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

    public static void main(String args[]) throws SocketException, IOException
    {
        
         IpAddress = InetAddress.getByName("localhost");
         clientsocket = new DatagramSocket(cport);
         dp = new DatagramPacket(buf, buf.length);         
        System.out.println(" SERVER...");
        
        String str = "ENTER HOST";
        clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
        
        clientsocket.receive(dp);
        String str2 = new String(dp.getData(), 0,dp.getLength());
        System.out.println("CLIENT: " + str2); 
        host = Integer.parseInt(str2);
        
        str = "ENTER PORT";
        clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
        clientsocket.receive(dp);
        str2 = new String(dp.getData(), 0,dp.getLength());
        System.out.println("CLIENT: " + str2); 
        port = Integer.parseInt(str2);
        
        str = "ENTER COMMAND   CD    LS     GET     PUT";
        clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
        clientsocket.receive(dp);
        command = new String(dp.getData(), 0,dp.getLength());
        System.out.println("CLIENT: " + command); 
        
        str = "ENTER DIRECTORY";
        clientsocket.send(new DatagramPacket(str.getBytes(),str.length(), IpAddress, sport));
        clientsocket.receive(dp);
        directory = new String(dp.getData(), 0,dp.getLength());
        System.out.println("CLIENT: " + directory);
        
        
        
        if(command.equalsIgnoreCase("put")){
        udpclient.putfile();
        }else if(command.equalsIgnoreCase("get")){
         udpclient.getfile(directory);       
        }else if(command.equalsIgnoreCase("ls")){
                File f = new File(directory);
                ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
             String out1 = String.join(",", names);
             clientsocket.send(new DatagramPacket(out1.getBytes(),out1.length(), IpAddress, sport));
          
        }else if(command.equalsIgnoreCase("cd")){
            try{
                System.setProperty( "user.dir", directory );
                String cwd = System.getProperty("user.dir");
                 String out1 = "successfull directory change "+cwd;                 
             clientsocket.send(new DatagramPacket(out1.getBytes(),out1.length(), IpAddress, sport));
            }catch(Exception e){
                System.out.println("unsuccessfull directory change");
            }
            
        }         
      

    }
}
