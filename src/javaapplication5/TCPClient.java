/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;

/**
 *
 * @author zeesh
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;
public class GossipClient
{
    
  
    
  public static void main(String[] args) throws Exception
  {
       Scanner input = new Scanner(System.in);
     Socket sock = new Socket("127.0.0.1", 3000);
                               // reading from keyboard (keyRead object)
     BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
                              // sending to client (pwrite object)
     OutputStream ostream = sock.getOutputStream(); 
     PrintWriter pwrite = new PrintWriter(ostream, true);
 
                              // receiving from server ( receiveRead  object)
     InputStream istream = sock.getInputStream();
     BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
 
     System.out.println("Start the chitchat, type and press Enter key");
 
     String receiveMessage, sendMessage;               
     String command, directory, file;
        sendMessage = "clent connected";  // keyboard reading
        pwrite.println(sendMessage);       // sending to server
        pwrite.flush();                    // flush the data
        if((receiveMessage = receiveRead.readLine()) != null) //receive from server
        {
            System.out.println(receiveMessage); // displaying at DOS prompt
        }   
        
        sendMessage = input.nextLine();  // keyboard reading
        command= sendMessage;
        pwrite.println(sendMessage);       // sending to server
        pwrite.flush();                    // flush the data
        if((receiveMessage = receiveRead.readLine()) != null) //receive from server
        {
            System.out.println(receiveMessage); // displaying at DOS prompt
        }  
        
        sendMessage = input.nextLine();  // keyboard reading
        directory = sendMessage;
        pwrite.println(sendMessage);       // sending to server
        pwrite.flush();                    // flush the data
        
        
        if(command.equalsIgnoreCase("put")){
             GossipClient.putfile(directory);
        }else if(command.equalsIgnoreCase("get")){
             GossipClient.getfile();       
        }else if(command.equalsIgnoreCase("ls")){
           if((receiveMessage = receiveRead.readLine()) != null) //receive from server
            {
                System.out.println(receiveMessage); // displaying at DOS prompt
            }  
        }else if(command.equalsIgnoreCase("cd")){
           if((receiveMessage = receiveRead.readLine()) != null) //receive from server
            {
                System.out.println(receiveMessage); // displaying at DOS prompt
            } 
        }
       
              
  
    }   
  
  public static void getfile() throws UnknownHostException, IOException{
      System.out.println("RECEIVING...");
       //Initialize socket
        Socket socket = new Socket(InetAddress.getByName("localhost"), 5000);
        
      byte[] contents = new byte[10000];
        //Initialize the FileOutputStream to the output file's full path.
        FileOutputStream fos = new FileOutputStream("C:\\Users\\zeesh\\Desktop\\server-to-client.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        InputStream is = socket.getInputStream();
        
        //No of bytes read in one read() call
        int bytesRead = 0; 
        
        while((bytesRead=is.read(contents))!=-1)
            bos.write(contents, 0, bytesRead); 
        
        bos.flush(); 
        socket.close(); 
        
        System.out.println("File saved successfully!");
  }
  
  
     public static void putfile(String dir) throws IOException{
       System.out.println("function working  1");
         ServerSocket ssock = new ServerSocket(9999);
        Socket socket = ssock.accept();
        System.out.println("function working   2");
        //The InetAddress specification
        InetAddress IA = InetAddress.getByName("localhost"); 
        System.out.println("function working    3");
        //Specify the file
        File file = new File(dir);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis); 
        System.out.println("function working   4");  
        //Get socket's output stream
        OutputStream os = socket.getOutputStream();
                
        //Read File Contents into contents array 
        byte[] contents;
        long fileLength = file.length(); 
        long current = 0;
         
        long start = System.nanoTime();
        while(current!=fileLength){ 
            int size = 10000;
            if(fileLength - current >= size)
                current += size;    
            else{ 
                size = (int)(fileLength - current); 
                current = fileLength;
            } 
            contents = new byte[size]; 
            bis.read(contents, 0, size); 
            os.write(contents);
            System.out.print("Sending file ... "+(current*100)/fileLength+"% complete!");
        }   
        
        os.flush(); 
        //File transfer done. Close the socket connection!
        socket.close();
        ssock.close();
        System.out.println("File sent succesfully!");
       
   }
}