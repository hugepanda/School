import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HandlerAll implements HttpHandler {
	
	
	public String myip = "";
	//System.out.println("myip-----" + myip);
	
	public HandlerAll(String ip)
	{
		myip=ip;
	}
	    
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String rootDir = "F:/DBoxServer";
		String rawPath = httpExchange.getRequestURI().getRawPath();
		//String path = rootDir + rawPath;
		String last = rawPath.substring(rawPath.length()-1);
		String reqMethod = httpExchange.getRequestMethod();
		String responseString = "";
		String regXmlPath = "userXml\\registration.passwd";
		String success = "success";
		String fail = "fail";
		XmlOperation xo = new XmlOperation();
		FileOperation fo = new FileOperation();
		List<FileNode> fnList = new ArrayList<FileNode>();
		FileNode fn = new FileNode();
		System.out.println("rawpath-----" + rawPath);
			
		/* GET method begin */
        if(reqMethod.equals("GET")) {
			System.out.println("Handler GET method!");
            
            // @GET registration first 1/2 handler
            if(rawPath.equals("/register/")) {
                System.out.println("Registration message 1!");
                byte[] nonce = createRandom(0, 9);
                String sNonce = arrayToString(nonce);
                httpExchange.sendResponseHeaders(200, nonce.length);  
                System.out.println(sNonce);
                System.out.println(sNonce.length());
                OutputStream os = httpExchange.getResponseBody();    
                os.write(sNonce.getBytes());  
                os.close();                 
            }
            
            // @GET registration first 2/2 handler
            if(rawPath.equals("/register")) {
                System.out.println("Registration message 2!");               
                String queryStr = parseGetParameters(httpExchange);
                System.out.println(queryStr);
                
                String pairs1[] = queryStr.split("[&]");
                String userName = ""; 
                userName = pairs1[0].substring(9, pairs1[0].length());
                String passWord = ""; 
                passWord = pairs1[1].substring(4, pairs1[1].length());              
                String md5 = "";
                md5 = pairs1[2].substring(4, pairs1[2].length());
                String nonce = "";
                nonce = pairs1[3].substring(6, pairs1[3].length());
                
                System.out.println(userName);
                System.out.println(passWord);
                System.out.println(md5);
                System.out.println(nonce);
                
                String mymd5 = calculateMD5(userName + passWord + nonce);
                try {
					if(mymd5.equals(md5) && xo.checkWhetherUsernameExists(regXmlPath, userName))
					{
					    try {
							xo.modifyRegistrationXml(regXmlPath, userName, passWord);
						} catch (DocumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    System.out.println("DBoxServer/" + userName);
					    File newUserFolder = new File("DBoxServer/" + userName);
						if(!newUserFolder.exists()) 
						{
							newUserFolder.mkdir(); 
							System.out.println("dsfasdfdsfsdafsdafdasfsdafsadf");
						}
							
					                 
					    httpExchange.sendResponseHeaders(200, success.length());    
					    OutputStream os = httpExchange.getResponseBody();    
					    os.write(success.getBytes());  
					    os.close(); 
					}
					else
					{
					    httpExchange.sendResponseHeaders(404, fail.length());    
					    OutputStream os = httpExchange.getResponseBody();    
					    os.write(fail.getBytes());  
					    os.close();  
					}
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}             
            }
                       
            // @GET login first 1/2 handler
            else if(rawPath.equals("/login/")) {
                System.out.println("Login message 1!");
                byte[] nonce = createRandom(0, 9);
                String sNonce = arrayToString(nonce);
                httpExchange.sendResponseHeaders(200, nonce.length);  
                System.out.println(sNonce);
                System.out.println(sNonce.length());
                OutputStream os = httpExchange.getResponseBody();    
                os.write(sNonce.getBytes());  
                os.close();                     
            }
            
            // @GET login second 2/2 handler
            else if(rawPath.equals("/login")) {
                System.out.println("Login message 2!");               
                String queryStr = parseGetParameters(httpExchange);
                System.out.println(queryStr);
                
                String pairs1[] = queryStr.split("[&]");
                String userName = ""; 
                userName = pairs1[0].substring(9, pairs1[0].length());
                String md5 = "";
                md5 = pairs1[1].substring(4, pairs1[1].length());
                String nonce = "";
                nonce = pairs1[2].substring(6, pairs1[2].length());
                
                System.out.println(userName);
                System.out.println(md5);
                System.out.println(nonce);
                
                String pwd = "";
				try {
					pwd = xo.findPassword(regXmlPath, userName);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}           
                String mymd5 = calculateMD5(userName + pwd + nonce);
                if(mymd5.equals(md5))
                {
                    httpExchange.sendResponseHeaders(200, success.length());    
                    OutputStream os = httpExchange.getResponseBody();    
                    os.write(success.getBytes());  
                    os.close();
                }
                else
                {
                    httpExchange.sendResponseHeaders(404, fail.length());    
                    OutputStream os = httpExchange.getResponseBody();    
                    os.write(fail.getBytes());  
                    os.close();
                }                                       
            }
            
            // @GET normal folder or public folder handler
            else if(last.equals("/")) {
            	
                // public folder handler
                if(rawPath.equals("/public/")) {
                	System.out.println("Enter the public folder!");
                	String path1 = "E:\\JavaProject\\dBoxServer\\public\\test.xml";
                	String sXml = readXML(path1);
                	System.out.println("SxML---" +sXml);
                	httpExchange.sendResponseHeaders(200, sXml.length());    
           	        OutputStream os = httpExchange.getResponseBody();    
           	        os.write(sXml.getBytes());  
           	        os.close();
           	    }
                
                // normal folder handler
                else { 
                    System.out.println("enter a normal folder!");
                    System.out.println("para that sent to getDir is------------" + rawPath.substring(1,rawPath.length()));
                    fnList = fo.getDir(rawPath.substring(1,rawPath.length()), myip);                      
                    responseString = xo.writeDirXml(fnList);
                    System.out.println(responseString);
                    httpExchange.sendResponseHeaders(200, responseString.length());    
                    OutputStream os = httpExchange.getResponseBody();    
                    os.write(responseString.getBytes());    
                    //System.out.println(responseString);
                    os.close(); 
                }
            }
            
            // @GET download handler
            else {  
            	System.out.println("Enter download handler!");           	
            	File inFile = new File(rawPath.substring(1, rawPath.length()));
             	FileInputStream inStream = new FileInputStream(inFile);
     			byte[] inOutb = new byte[inStream.available()];
     			inStream.read(inOutb); //store in byte[]
     			inStream.close();
            	String ss = new String(inOutb); 
            	
            	fn.setName(inFile.getName());
            	fn.setSize(String.valueOf(inFile.length()));
            	int dot = (inFile.getName()).lastIndexOf(".");
            	fn.setType(fn.name.substring(dot+1, fn.name.length()));
            	fn.setAccess("private");
            	Calendar rightNow = Calendar.getInstance();
     	        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
     	        String sysDatetime = fmt.format(rightNow.getTime());   
     		    System.out.println(sysDatetime);
            	fn.setModified(sysDatetime);

            	fn.setUrl("http://" + myip + ":10001" + rawPath);
            	           	
     			fn.setContent(ss);
     			System.out.println("ss" + ss);
                responseString = xo.WriteXml(fn);              
                httpExchange.sendResponseHeaders(200, responseString.length());    
       	        OutputStream os = httpExchange.getResponseBody();    
       	        os.write(responseString.getBytes());    
       	        os.close(); 
            }
		}
        /* GET method begin */		
		
        /* PUT method begin */
        if(reqMethod.equals("PUT")) {
            System.out.println("Handler PUT method!");	
			InputStream is = httpExchange.getRequestBody(); 			
			String teString = null;
			try {
				teString = InputStreamTOString(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
			try {
				fn = xo.readFileXml(teString);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			
			if(fn.getType().equals("folder")) {
				System.out.println("Upload a folder!");
				fo.newFolder(rawPath.substring(1, rawPath.length()));
		        SendBack sb = new SendBack();  
	            responseString = "Upload Success!";
	            sb.sendSuccess(httpExchange, responseString);
			}

            else {
            	System.out.println("Upload a file!");
                String fileContent = fn.getContent();
                System.out.println(fileContent);
                String path1 = fn.getUrl().replace("http://" + myip + ":10001/", "/DBoxServer/");
                String path2 = path1.replaceAll("/", "\\\\");
                fo.newFile(rawPath.substring(1, rawPath.length()), fileContent);
                SendBack sb = new SendBack();  
                responseString = "Upload Success!";
                sb.sendSuccess(httpExchange, responseString);
            }
		}
        /* PUT method end */

		if(reqMethod.equals("DELETE")) {
			System.out.println("enter delete!");
	        			
			fo.delFile(rawPath.substring(1, rawPath.length()));
	        SendBack sb = new SendBack();  
            responseString = "Delete Success!";
            sb.sendSuccess(httpExchange, responseString);
		}
		else {
			System.out.println("Error in HTTP Request Header!");
			SendBack sb = new SendBack();
            responseString = "Error in HTTP Request Header!";
            sb.sendFailure(httpExchange, responseString);
		}
	}
	
    public String InputStreamTOString(InputStream in) throws Exception
    {      
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[10];  
        int count = -1;  
        while((count = in.read(data,0,10)) != -1)  
        outStream.write(data, 0, count);  
          
        data = null;  
        return new String(outStream.toByteArray(),"UTF-8");  
    } 
    
    public String readXML(String filename)
	{
		SAXReader reader = new SAXReader(); 
		String s = ""; 
		
		Document doc;  
        try {  
            doc = reader.read(new File(filename));  
            ByteArrayOutputStream out = new ByteArrayOutputStream(); 
            OutputFormat format = new OutputFormat("  ", true, "UTF-8"); 
            XMLWriter writer = new XMLWriter(out, format); 
            writer.write(doc); 
            s = out.toString("GB2312"); 
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
		return s; 
	}
     
    byte[] createRandom(int min, int max) {
        Random random = new Random();
        byte[] byteArr = new byte[5];
        for(int i=0;i<5;i++) {
        	byteArr[i] = (byte)((byte)random.nextInt(max-min+1)+ min);
        }
        return byteArr;        
    }     
    
    public static final String arrayToString(byte[] bytes)
    {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            buff.append(bytes[i]);
        }
        return buff.toString();
    }
    
    private String parseGetParameters(HttpExchange exchange) throws UnsupportedEncodingException 
    {

            Map<String, Object> parameters = new HashMap<String, Object>();
            URI requestedUri = exchange.getRequestURI();
            String query = requestedUri.getRawQuery();
            parseQuery(query, parameters);
            exchange.setAttribute("parameters", parameters);
            return query;
    }
    
    private void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException 
    {
        if (query != null) 
        {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) 
            {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                        System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                        System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if(obj instanceof List<?>) {
                        @SuppressWarnings("unchecked")
						List<String> values = (List<String>)obj;
                        values.add(value);
                    } else if(obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String)obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } 
                else 
                {
                    parameters.put(key, value);
                }
            }
        }
    }
    
    public String calculateMD5(String str)
	{
		System.out.println("enter md5");
        MessageDigest md5 = null;  
        try  
        {  
            md5 = MessageDigest.getInstance("MD5"); 
        }
        catch(Exception e)  
        {  
            e.printStackTrace();  
            return "";  
        }  
              
        char[] charArray = str.toCharArray();  
        byte[] byteArray = new byte[charArray.length];  
              
        for(int i = 0; i < charArray.length; i++)  
        {  
        	byteArray[i] = (byte)charArray[i];  
        }  
        byte[] md5Bytes = md5.digest(byteArray);  
              
        StringBuffer hexValue = new StringBuffer();  
        for( int i = 0; i < md5Bytes.length; i++)  
        {  
            int val = ((int)md5Bytes[i])&0xff;  
            if(val < 16)
            {  
                hexValue.append("0");  
            }  
            hexValue.append(Integer.toHexString(val));  
        }  
        System.out.println(hexValue.toString());
        return hexValue.toString();     
	}

    
}
