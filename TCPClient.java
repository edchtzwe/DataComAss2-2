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

//gets user to input the IP, port number and file name as cmd line args

public class TCPClient {
    //to set up connection
    static Socket socket = null;
    
    //vars for cmd line args
    static String IP = null;
    static int port = 0;
    static String fileName = null;    
    
    //streams for data transfer
    static DataOutputStream ostream = null;
    static InputStream istream = null;
    static FileOutputStream ofstream = null;
    static BufferedOutputStream bostream = null;     
    
    //byte data holders
    static byte[] idata = null;
    static int bytes = 0;
    
    public static void main(String[] args) throws IOException
    {
        //initialize variables with cmd line args
        IP = args[0];
        while(port == 0){
            try{
                port = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException e){
                System.out.println(e.toString());
                port = 0;
            }
        }
        fileName = args[2];
        
        while(socket == null){
            try{
                //set up connection
                socket = new Socket(IP, port);
            }
            catch(SocketException e){
                System.out.println(e.toString());
                socket = null;
            }
        }        
        
        //if the socket has been successfuly opened, there should be no reason
        //any exception will be thrown at this point.
        //get the stream that leads to the server
        ostream = new DataOutputStream(socket.getOutputStream());
        
        //send the request to the server
        ostream.writeUTF("Send " + fileName);                        
       
        //download the file
        idata = new byte[100000];        
        
        //get stream to server
        istream = socket.getInputStream();
        
        while(ofstream == null){
            try{
                //stream to output file
                ofstream = new FileOutputStream(fileName);
            }
            catch(FileNotFoundException e){
                System.out.println(e.toString());
                ofstream = null;
            }
        }
        
        //stream to write to output file
        bostream = new BufferedOutputStream(ofstream);
        
        //read contents of file into byte holder
        int len = 0;
        //as long as there is something left to be read, read it       
        while((len = istream.read(idata)) > 0){
            //write current content into file instantaneously 
            //and on the next read, the container will be empty
            //read for the next big chunk of data
            bostream.write(idata, 0, len); 
        }
       
                       
        //close the streams        
        bostream.close();
        socket.close();
        istream.close();
        ostream.close();

    }
}
