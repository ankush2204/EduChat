import java.net.*;
class bclient{
	 public static void main(String[] args) {
        
        try{
        	Socket s = new Socket("127.0.0.1",3000);
        	System.out.println("connected");
        }
        catch (Exception e){}
	}
}