import java.net.*;
import java.io.*;

public class BasicServer
{  Socket          socket   = null;
   ServerSocket    server   = null;
   DataInputStream streamIn =  null;

   private String readFile(String filename)
	{
	  String records = "";
	  try
	  {
	    BufferedReader reader = new BufferedReader(new FileReader(filename));
	    String line;
	    while ((line = reader.readLine()) != null)
	    {
	      records+=line;
	    }
	    reader.close();
	    return records;
	  }
	  catch (Exception e)
	  {
	    System.err.format("Exception occurred trying to read '%s'.", filename);
	    e.printStackTrace();
	    return null;
	  }
	}

   public BasicServer(int port)
   {  try
      {  System.out.println("Binding to port " + port + ", please wait  ...");
         server = new ServerSocket(port);  
         System.out.println("Server started: " + server);
         System.out.println("Waiting for a client ..."); 
         socket = server.accept();
         System.out.println("Client accepted: " + socket);
         open();

         BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         String str = br.readLine();
         int len = str.length();
         String str2 = str.substring(5,len-9);
         System.out.println("Client sent - " + str2); 
         /*
         boolean done = false;
         while (!done)
         {  try
            {  String line = streamIn.readUTF();
               System.out.println(line);
               done = line.equals(".bye");
            }
            catch(IOException ioe)
            {  done = true;
            }
         }
         */
         PrintWriter out = new PrintWriter(socket.getOutputStream());


         out.println("HTTP/1.1 200 OK");
         out.println("Content-Type: text/html");
         out.println("\r\n");
         out.println(readFile("webpage.html"));
         out.flush();

         out.close();
         
         close();
      }
      catch(IOException ioe)
      {  System.out.println(ioe); 
      }
   }
   public void open() throws IOException
   {  streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
   }
   public void close() throws IOException
   {  if (socket != null)    socket.close();
      if (streamIn != null)  streamIn.close();
   }
   public static void main(String args[])
   {  BasicServer server = null;
      if (args.length != 1)
         System.out.println("Usage: java BasicServer port");
      else
         server = new BasicServer(Integer.parseInt(args[0]));
   }
}