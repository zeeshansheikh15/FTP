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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class GossipServer
{
    
     public static void getfile(String dir) throws IOException{
       System.out.println("function working");
         ServerSocket ssock = new ServerSocket(5000);
        Socket socket = ssock.accept();
        
        //The InetAddress specification
        InetAddress IA = InetAddress.getByName("localhost"); 
        
        //Specify the file
        File file = new File(dir);
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis); 
          
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
     
     
  public static void putfile() throws UnknownHostException, IOException{
      System.out.println("RECEIVING...");
       //Initialize socket
        Socket socket = new Socket(InetAddress.getByName("localhost"), 9999);
       System.out.println("RECEIVING...2");  
      byte[] contents = new byte[10000];
        //Initialize the FileOutputStream to the output file's full path.
        FileOutputStream fos = new FileOutputStream("C:\\Users\\zeesh\\Desktop\\client-to-server.jpg");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        InputStream is = socket.getInputStream();
       System.out.println("RECEIVING...3");  
        //No of bytes read in one read() call
        int bytesRead = 0; 
        
        while((bytesRead=is.read(contents))!=-1)
            bos.write(contents, 0, bytesRead); 
        
        bos.flush(); 
        socket.close(); 
        
        System.out.println("File saved successfully!");
  }
    
  public static void main(String[] args) throws Exception
  {
       Scanner input = new Scanner(System.in);
      ServerSocket sersock = new ServerSocket(3000);
      System.out.println("Server  ready for chatting");
      Socket sock = sersock.accept( );                          
                              // reading from keyboard (keyRead object)
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
	                      // sending to client (pwrite object)
      OutputStream ostream = sock.getOutputStream(); 
      PrintWriter pwrite = new PrintWriter(ostream, true);
 
                              // receiving from server ( receiveRead  object)
      InputStream istream = sock.getInputStream();
      BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
 
      String receiveMessage, sendMessage; 
      String command = null, directory = null, file = null; 
      
      
        if((receiveMessage = receiveRead.readLine()) != null)  
        {
           System.out.println(receiveMessage);         
        }         
        sendMessage = "enter one of the commands  CD    LS     GET     PUT  "; 
        pwrite.println(sendMessage);             
        pwrite.flush();
                     

        if((receiveMessage = receiveRead.readLine()) != null)  
        {
            command = receiveMessage;
           System.out.println(receiveMessage); 
           
        }         
        sendMessage = "enter the directory   "; 
        pwrite.println(sendMessage);             
        pwrite.flush();
                     

        if((receiveMessage = receiveRead.readLine()) != null)  
        {
            directory=receiveMessage;
           System.out.println(receiveMessage);         
        }         
          
        //GossipServer.getfile(directory);
          if(command.equalsIgnoreCase("put")){
        GossipServer.putfile();
        }else if(command.equalsIgnoreCase("get")){
         GossipServer.getfile(directory);       
        }else if(command.equalsIgnoreCase("ls")){
                File f = new File(directory);
          ArrayList<String> names = new ArrayList<String>(Arrays.asList(f.list()));
             String out1 = String.join(",", names);
             
            sendMessage = out1;
            pwrite.println(sendMessage);             
            pwrite.flush();
        }else if(command.equalsIgnoreCase("cd")){
            System.setProperty( "user.dir", directory );
                String cwd = System.getProperty("user.dir");
                 String out1 = "successfull directory change "+cwd; 
            sendMessage = out1;
            pwrite.println(sendMessage);             
            pwrite.flush();
        }
        
          
          
    }                    

}