import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class MyServer {	



	public void startService(String ip) { 
        try {  
            int portNum = 10001;
            int backLog = 10;
            InetSocketAddress inetSock = new InetSocketAddress(portNum); 
            HttpServer httpServer = HttpServer.create(inetSock, backLog);
            HandlerAll myHandler = new HandlerAll(ip);
            httpServer.createContext("/", myHandler); 
            httpServer.setExecutor(null); 
            httpServer.start(); 
            System.out.println("Server has started!"); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
	}
}