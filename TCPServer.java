/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author HP G4
 * @last_modified 2014/05/01
 */
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {
    //sockets
    static ServerSocket serverSocket = null;
    static Socket socket = null;
    
    //to setup a connection to the file
    static String fileName = null;
    static File theFile = null;
    
    //streams for data transfer
    static DataInputStream distream = null; //get request from client    
    static BufferedInputStream bistream = null; //get streams from file
    static OutputStream ostream = null; //get stream to server
    
    //data holder
    static byte[] odata = null;
    
    public static void main(String[] args) throws IOException
    {
        //setup a server exclusive socket and tie it to port 1234, this is to be fed to the client program.
        
        serverSocket = new ServerSocket(1234);

        while(true){
            //accept a connection from a client
            
            socket = serverSocket.accept();
            System.out.println("Connected to client");

            //get the request from the client and process it
            distream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                        
            //split the file into tokens, the 2nd item is the file name *1st item is "Send"*
            String clientIn = distream.readUTF();
            String[] tokens = clientIn.split(" ");
            fileName = tokens[1];
           
            System.out.println(fileName);
           
            
            //open a channel to read the file
            theFile = new File(fileName);
            
            //set up a data holder to hold the data from the file
            odata = new byte[(int) theFile.length()];
            
            //read the bytes from the file into the data holder            
                
            bistream = new BufferedInputStream(new FileInputStream(theFile));
            
            

            bistream.read(odata, 0, odata.length);

            
            //get the output stream to the client

            ostream = socket.getOutputStream();

            
            //write the data onto the stream

            ostream.write(odata, 0, odata.length);
            
            System.out.println("Sending file.");

            //send the data down the stream to the client
            ostream.flush();
            
            //close socket
            socket.close();
        }
        
        
    }
}
