import edupack.*;
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
                String str,str2;
               br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
               str = br.readLine();
               try{
                  str2 = str.substring(5,str.length()-9);
               }
               catch(NullPointerException exc){
                  str2 = "Nothing..";
               }
               
               /*    *****IGNORING FAVICON.ICO*****    */
               int flag = 0;
               String str3="favicon.ico",url,id="",msg="";
               for(int i=0;i<str3.length()&&i<str2.length()&&flag<1;i++){
                  if(str3.charAt(i)!=str2.charAt(i)){
                     flag=1;     
                  }
               }  
               
               /*  *****EXTRACTING URL PARAMETERS*****
               String url[] = str2.split("?");
               String pairs[] = url[1].split("&");
               String pair1[] = pairs[0].split("=");
               String pair2[] = pairs[1].split("=");
               System.out.println(pair1[0]+" = "+pair1[1]);
               System.out.println(pair2[0]+" = "+pair2[1]);
               */
                  if(flag==0){System.out.println("favicon bkl");}
                  if(flag==1){
                    System.out.println("Client sent - " + str2); 
                    url = str2;
                    int i=0;
                    for(i=0;i<url.length()&&flag<1;i++){
                      if(url.charAt(i)=='?')flag=1;
                    }
                    String s1 = url.substring(i,url.length());
                    String pairs[] = s1.split("&");
                    String pair1[] = pairs[0].split("=");
                    String pair2[] = pairs[1].split("=");
                    System.out.println(pair1[0]+" = "+pair1[1]);
                    System.out.println(pair2[0]+" = "+pair2[1]);  
                    id = pair1[1];
                    msg=pair2[1];
                    edupack.EditFile.addMessage(id,msg);
                  }

                  out.println("HTTP/1.1 200 OK");
                  out.println("Content-Type: text/html");
                  out.println("\r\n");
                  out.println(readFile("webpage.html"));
                  out.println(readFile(id+".txt"));
                  out.println("<//body><//html>");
                  out.flush();
                  out.close();
               }
               catch(IOException ioe)
               {  done = true;
                  System.out.println(ioe);
               }
            }
            
         }
         catch(IOException ie)
         {  
          System.out.println("Acceptance Error: " + ie);  }
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