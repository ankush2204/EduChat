import java.net.*;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import edupack.EditFile;
import java.lang.*;
public class ChatServer	implements Runnable
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
   public void set_field(int j,String str,String field[],int n,int flag)
   {
   	          for(int i=n-6;i>=0;i--)
		             {
                        if(str.charAt(i)!= '=')
                        {
                           field[j] = str.charAt(i) + field[j]; 
                        }
                        else if (str.charAt(i)=='=')
                        {
                           j--;
                           if(j==-1)
                           	break;
                           if(flag==0)
                           i = i-2;
                           else i=i-3;
                        }
		             }
   }
   
   public void run()
   { 
    while (thread != null)
      {   try
         {  
         	System.out.println("Waiting for a client ..."); 
            socket = server.accept();
            System.out.println("Client accepted: " + socket);
            open();

            String str="";
	              BufferedReader br;
		          br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		          String field[] = new String[10];
		             for(int i=0;i<10;i++)
		             {
		             	field[i] = "";
		             }
		          int fl=0;
		          String sd="";

		          try
		          {
		             int flag = 0;
		             int f=0;
		             while(true){ 

		          		str += (char)br.read();
                        
                        
		          		int n= str.length();
		          		if (n>5){
		          		 	if(str.contains("POST") && str.charAt(n-3)=='n' && str.charAt(n-2)=='d' && str.charAt(n-1)=='l')
		          		 		break;
		          		 	else if(str.contains("POST") && str.charAt(n-3)=='n' && str.charAt(n-2)=='d' && str.charAt(n-1)=='s')
		          		 		{
		          		 			flag=1;
		          		 		    break;
		          		 		}
		          		 	else if(str.contains("POST") && str.charAt(n-3)=='n' && str.charAt(n-2)=='d' && str.charAt(n-1)=='d')
		          		 	{
		          		 		flag = 2;
		          		 		break;
		          		 	}
		          		 	else if(str.contains("POST") && str.charAt(n-3)=='n' && str.charAt(n-2)=='d' && str.charAt(n-1)=='g')
		          		 	{
                               
		          		 		flag=3;
		          		 		fl = 1;
		          		 		break;
		          		 	}	
		          		 	if(str.contains("Cookie") && str.charAt(n-3)=='m' && str.charAt(n-2)=='e' && str.charAt(n-1)=='=')
		          		 	{
                               f=1;
                               System.out.println("ok");
		          		 	}
		          		 	else if(f==1)
		          		     {
		          		 		if(str.charAt(n-1) == '\n')
		          		 			f=0;
                                else sd = sd + str.charAt(n-1);
		          		 	}
		          		 	if (str.contains("GET")){
		          		 		if (n>20)
		          		 			break;
		          		 	}
		          		 }
		          		
		             }
		             int n = str.length();
		             int j=0;
		             
		             if(flag==0)
		             	j=1;
		             else if(flag ==1)
		             	j=5;
		             else if(flag == 2)
		             	j=1;
		             else if(flag==3)
		             	j=0;
                        
                     set_field(j,str,field,n,flag);
                     	if(flag==3)field[0] = URLDecoder.decode(field[0], "UTF-8");

		          }  
		          catch(Exception e){
		          		System.out.println("Exception");
		          }

		          System.out.println("str = "+str);
		          System.out.println(field[1]);
		          System.out.println(field[0]);
		          PrintWriter out = new PrintWriter(socket.getOutputStream());
		          out.println("HTTP/1.1 200 OK");
		          out.println("Content-Type: text/html");
		          
		          
		          if(field[0].equals("Shivam"))
		          {
                     out.println("Set-Cookie: name="+field[0]);
                     out.println("\r\n");
                     out.println(readFile("chat.html"));
                     out.flush();
		             out.close();
		          }
		          else if(field[0].equals("10")){
		          	out.println("\r\n");
                    out.println(readFile("grp.html"));
                     out.flush();
		             out.close();   
		          }
		          else if(fl==1)
		          {
		          	System.out.println(field[0]);
		          	String id = "10";
		          	
		          	System.out.println(sd);
		          	edupack.EditFile.addMessage(id,sd,field[0]);
		          	out.println("\r\n");	
		          	out.println(readFile("grp.html"));
		          	out.println(readFile(id+".txt"));
		          	out.println("</body></html>");
		          	out.println();
		          	out.flush();
		            out.close();
		          }
		          else{
		          	out.println("\r\n");
                    out.println(readFile("index.html"));
                     out.flush();
		             out.close(); 
		          }

		          /*str = br.readLine();
		          System.out.println(str);
		          int len = str.length();
		          String str2 = str.substring(5,len-9);
		          System.out.println("Client sent - " + str2); */
		        
		          
		          
		       
             
	         // boolean done = false;
	         // while (!done)
	         // {  try
	         //    {  
	         //     String str;
	         //     BufferedReader br;
		        //  br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		        //  str = br.readLine();
		         
		        //  int len = str.length();
		        //  String str2 = str.substring(5,len-9);
		        //  System.out.println("Client sent - " + str2); 
		        //  PrintWriter out = new PrintWriter(socket.getOutputStream());

		         
		        //  out.println("HTTP/1.1 200 OK");
		        //  out.println("Content-Type: text/html");
		        //  out.println("\r\n");
		        //  out.println(readFile(str2));
		        //  out.flush();
		        //  out.close();
	         //    }
	         //    catch(IOException ioe)
	         //    {  done = true;
	         //    }
	         // }
	   
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
