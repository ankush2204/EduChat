import java.net.*;
class bserver{
	 public static void main(String[] args) {
        
        try{
        	ServerSocket ser = new ServerSocket(3000);
		System.out.println("Server is running...");
        	ser.accept();
        	System.out.println("connected");
        }
        catch (Exception e){}
	}
}