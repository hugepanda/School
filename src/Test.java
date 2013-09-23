import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test 
{
	/**
	 * @throws IOException 
	 * @note This is the test case for the server. To start, just run this as a java application in eclipse with the input of ip address.
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
        System.out.println("Please input the ip address of this server:");
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 
        String st = br.readLine(); 
		MyServer ms = new MyServer();
        ms.startService(st);       
	}
}
