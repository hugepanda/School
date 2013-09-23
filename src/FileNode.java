public class FileNode 
{
	public String type;
	public String name;
	public String size;
	public String url;
	public String modifiedDate;
	public String access;
	public String content;

	public String getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public String getSize()
	{ 
		return size;
	}

	public String getUrl()
	{
		return url;
	}

	public String getModified()
	{
		return modifiedDate;
	}

	public String getAccess()
	{
		return access;
	}

	public String getContent()
	{
		return content;
	}
	
	public void setType(String stype)
	{
		type=stype;
	}

	public void setName(String sname)
	{
		name=sname;
	}

	public void setSize(String ssize)
	{
		size=ssize;
	}

	public void setUrl(String surl)
	{
		url=surl;
	}

	public void setModified(String smodifiedDate)
	{
		modifiedDate=smodifiedDate;
	}

	public void setAccess(String saccess)
	{
		access=saccess;
	}

	public void setContent(String contents)
	{
		content=contents;
	}
}
