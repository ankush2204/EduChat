import java.net.*;
import java.io.*;
 
public class ChatServer implements Runnable
{  Socket          socket   = null;
   ServerSocket    server   = null;
   DataInputStream streamIn =  null;
   Thread thread = null;
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
 
   public ChatServer(int port)
   {  try
      {  System.out.println("Binding to port " + port + ", please wait  ...");
         server = new ServerSocket(port);  
         System.out.println("Server started: " + server);
         start();
      }
      catch(IOException ioe)
      {  System.out.println(ioe); 
      }
   }
   public void run()
   {  while (thread != null)
      {   try
         {  System.out.println("Waiting for a client ..."); 
            socket = server.accept();
            System.out.println("Client accepted: " + socket);
            open();
            BufferedReader br = null;
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            boolean done = false;
            while (!done)
            {  try
               {  
                String str;
               br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               str = br.readLine();
 
               int len = str.length();
               String str2 = str.substring(5,len-9);
               
               int flag = 0;
               String str3="favicon.ico";
               for(int i=0;i<str3.length()&&i<str2.length()&&flag<1;i++){
                  if(str3.charAt(i)!=str2.charAt(i)){
                     flag=1;     
                  }
               }
               System.out.println(flag);  

               
               if(flag>0){
                  System.out.println("Client sent - " + str2); 
                  
                  out.println("HTTP/1.1 200 OK");
                  out.println("Content-Type: text/html");
                  out.println("\r\n");
                  out.println(readFile("webpage.html"));
                  out.flush();
                  out.close();
                  }
               if(flag == 0){System.out.println("Ignoring favicon.ico");}
               }
               catch(IOException ioe)
               {  done = true;
                  System.out.println(ioe);
               }
            }
            out.flush();
            out.close();
            close();
         }
         catch(IOException ie)
         {  System.out.println("Acceptance Error: " + ie);  }
      }
   }
   public void start()
   {  if (thread == null)
      {  thread = new Thread(this); 
         thread.start();
      }
   }
   public void stop()
   {  if (thread != null)
      {  thread.stop(); 
         thread = null;
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
   {  ChatServer server = null;
      if (args.length != 1)
         System.out.println("Usage: java ChatServer port");
      else
         server = new ChatServer(Integer.parseInt(args[0]));
   }
}