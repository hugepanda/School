import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;

public class SendBack {
 	void sendSuccess(HttpExchange httpExchange, String responseString) {
		try {
			httpExchange.sendResponseHeaders(200, responseString.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	    OutputStream os = httpExchange.getResponseBody();    
	    try {
			os.write(responseString.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	    try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	    
    void sendFailure(HttpExchange httpExchange, String responseString) {
		try {
			httpExchange.sendResponseHeaders(404, responseString.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	    OutputStream os = httpExchange.getResponseBody();    
	    try {
			os.write(responseString.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	    try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
