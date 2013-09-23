import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileOperation 
{
// Class starts	
	List<FileNode> filenodeList = null;
	
	public void delFile(String filePathAndName) 
	{
		try 
		{
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();
		}
		catch (Exception e) 
		{
		e.printStackTrace();
		}
	}
		
	public void newFolder(String folderPath) 
	{
		try {
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			if (!myFilePath.exists()) {
			    myFilePath.mkdir();
		    }
		}
		catch (Exception e) {
		  System.out.println("ÐÂ½¨Ä¿Â¼²Ù×÷³ö´í");
		  e.printStackTrace();
		}
	}

	public void newFile(String filePathAndName, String fileContent) 
	{
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) 
			{
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			resultFile.close();
		}
		catch (Exception e) 
		{
			System.out.println("ÐÂ½¨Ä¿Â¼²Ù×÷³ö´í");
			e.printStackTrace();
		}
	}

	public List<FileNode> getDir(String strPath, String ip) throws IOException 
	{ 
		System.out.println("Enter getDir function!");
		File dir = new File(strPath);
		File[] files = dir.listFiles();
		filenodeList = new ArrayList<FileNode>();
		
		for (int i = 0; i < files.length; i++) 
		{ 		
	        String name = files[i].getName();//get file name
	        
	        long size = 0;
	        String type = "";
	        String content = "";
	        
	        if(files[i].isDirectory())
	        {
	        	size = getFileSize(files[i]);
	        	type = "folder";
	        	content = "";
	        }
	        else{
		        size = files[i].length(); 
		        int dot = (files[i].getName()).lastIndexOf(".");
		        type = name.substring(dot+1, name.length());
	        }
	        String strsize = Long.toString(size);
	        

	        String URL1 = files[i].getPath();
	        System.out.println("look at me!!-----------" + URL1);
	        String url3=URL1.replaceAll("\\\\", "/");
	        System.out.println("url3:"+url3);
	        String URL2 = "http://" + ip + ":10001/" + url3;

	        Calendar cal = Calendar.getInstance();   
	        long modifydate = files[i].lastModified();
	        cal.setTimeInMillis(modifydate);
	        
	        FileNode fn=new FileNode();
            fn.setName(name);
            fn.setSize(strsize);
            fn.setType(type);
            fn.setUrl(URL2);
            fn.setModified(cal.getTime().toString());
            fn.setContent("");
            
            System.out.println(fn.getName() + fn.getType() + fn.getSize() +fn.getModified() + fn.getModified() +fn.getUrl());

	         filenodeList.add(fn);	         
	    } 

	    return filenodeList;
	} 

	public long getFileSize(File folder) 
	{
		long foldersize = 0;
		File[] filelist = folder.listFiles();
		for (int j = 0; j < filelist.length; j++) 
		{
			if (filelist[j].isDirectory()) 
			{
				foldersize =foldersize + getFileSize(filelist[j]);
			} 
		    else 
			{
				foldersize += filelist[j].length();
			}
		}
		return foldersize;
	}
		
		
		
	public String readFile(String path) throws IOException 
	{
		File myFile = new File(path);
	    if(!myFile.exists())
	    { 
	        System.err.println("Can't Find " + path);
	    }
	    BufferedReader in = new BufferedReader(new FileReader(myFile));
	    String str = "";
	    String result = "";
	    while ((str = in.readLine()) != null) 
	    {
	        result += str;
	    }
	    in.close();
	    return result;
	}		
// Class ends
}
