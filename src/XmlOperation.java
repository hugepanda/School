import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlOperation {
    
	public String writeDirXml(List<FileNode> files) 
    {
		System.out.println("ENTER-writeDirXml");
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        
        Document document = DocumentHelper.createDocument();
        Element dirEle = document.addElement("Dir");

        for(FileNode fn : files) {
        	Element itemEle = dirEle.addElement("item");
        	
        	Element typeEle = itemEle.addElement("type");
        	typeEle.setText(fn.getType());
        	System.out.println(typeEle.getText());
        	
        	Element nameEle = itemEle.addElement("name");
        	nameEle.setText(fn.getName());
        	System.out.println(nameEle.getText());
        	
        	Element sizeEle = itemEle.addElement("size");
        	sizeEle.setText(fn.getSize());
        	System.out.println(sizeEle.getText());
        	

        	Element urlEle = itemEle.addElement("url");
        	urlEle.setText(fn.getUrl());
        	System.out.println(urlEle.getText());
        	

        	Element modifyDateEle = itemEle.addElement("modifyDate");
        	modifyDateEle.setText(fn.getModified());
        	System.out.println(modifyDateEle.getText());

        	Element accessEle = itemEle.addElement("access");
        	accessEle.setText("private");
        	System.out.println(accessEle.getText());
        }
        
        System.out.println(doc2String(document));
        return doc2String(document);
	}
	
	public String WriteXml(FileNode fn) throws IOException 
    {
	    OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
		Document document = DocumentHelper.createDocument();
		System.out.println("Enter WriteXml!");

             
        Element root = document.addElement("download");
        Element item = root.addElement("file");
        Element name = item.addElement("name");
        name.addText(fn.getName());
        System.out.println(fn.getName());
        
        Element type = item.addElement("type");
        type.addText(fn.getType());
        System.out.println(fn.getType());
        
        Element size = item.addElement("size");
        size.addText(fn.getSize());
        System.out.println(fn.getSize());
        
        Element Access = item.addElement("Access");
        Access.addText(fn.getAccess());
        System.out.println(fn.getAccess());
        
        Element modifyDate = item.addElement("modifyDate");
        modifyDate.addText(fn.getModified());
        System.out.println(fn.getModified());
        
        Element content = item.addElement("content");
        content.addText(fn.getContent());
        System.out.println(fn.getContent());
        
        return doc2String(document);    
    }
       
    public void modifyRegistrationXml(String path, String username, String password) throws DocumentException, IOException
    {
        System.out.println("Enter modifyRegistrationXml function!");
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");     
        File file = new File(path);
        SAXReader reader = new SAXReader();
        Document doc = reader.read(file);
        
        Element rootEle = doc.getRootElement();
        Element userEle = rootEle.addElement("user");
        System.out.println("Enter modifyRegistrationXml function!");
        
        Element usernameEle = userEle.addElement("username");
        usernameEle.setText(username);
        Element passwordEle = userEle.addElement("password");
        passwordEle.setText(password);          

        XMLWriter writer = new XMLWriter(new FileWriter(new File(path)),format); 
        writer.write(doc); 
        writer.close();       
    }
	
    public FileNode readFileXml(String s) throws UnsupportedEncodingException, DocumentException 
    {
    	
    	System.out.println("Enter readDFileXml function!");
        FileNode fn = new FileNode();
    	SAXReader sr = new SAXReader();
        Document document = sr.read(new ByteArrayInputStream(s.getBytes("UTF-8")));
        Element rootEle = document.getRootElement();  
        
        
        for (Iterator it = rootEle.elementIterator();it.hasNext();) 
        {  
            Element elm = (Element)it.next();  
        	
        	Element typeEle = elm.element("type");
        	fn.setType(typeEle.getText());
        	//System.out.println(fn.getType());
        	
        	Element nameEle = elm.element("name");
        	fn.setName(nameEle.getText());
        	//System.out.println(fn.getName());
        	
        	Element urlEle = elm.element("url");
        	fn.setUrl(urlEle.getText());
        	//System.out.println(fn.getUrl());
        	
        	Calendar rightNow = Calendar.getInstance();
	        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	        String sysDatetime = fmt.format(rightNow.getTime());   
        	fn.setModified(sysDatetime);

        	
        	Element accEle = elm.element("access");
        	fn.setAccess(accEle.getText());
        	
        	Element conEle = elm.element("content");
        	fn.setContent(conEle.getText());
        }
        return fn;
    }

    public String findPassword(String path, String username) throws DocumentException
    {
        System.out.println("Enter findPassword function!");
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");     
        File file = new File(path);
        SAXReader reader = new SAXReader();
        Document doc = reader.read(file);
        Element rootEle = doc.getRootElement(); 
        String password = "";
        
        for(Iterator it = rootEle.elementIterator();it.hasNext();) {  
            Element ele = (Element)it.next();
            if(ele.element("username").getText().equals(username))
            {
            	password = ele.element("password").getText();
            	return password;
            }
        }
        
        return "FAIL!";
    }

    public boolean checkWhetherUsernameExists(String path, String sUsername) throws DocumentException
    {
        System.out.println("Enter checkWhetherUsernameExists function!");
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");     
        File file = new File(path);
        SAXReader reader = new SAXReader();
        Document doc = reader.read(file);
        Element rootEle = doc.getRootElement(); 
        
        for(Iterator it = rootEle.elementIterator();it.hasNext();) {  
            Element ele = (Element)it.next();
            if(ele.element("username").getText().equals(sUsername))
            {
                return false;
            }
        }
        
        return true;
    }
			
	public String doc2String(Document document) 
    { 
        String s = ""; 
        try 
        { 
           ByteArrayOutputStream out = new ByteArrayOutputStream(); 
           OutputFormat format = new OutputFormat("  ", true, "UTF-8"); 
           XMLWriter writer = new XMLWriter(out, format); 
           writer.write(document); 
           s = out.toString("UTF-8"); 
        }
        catch(Exception ex) 
        {             
           ex.printStackTrace(); 
        }       
        return s; 
    }
}
