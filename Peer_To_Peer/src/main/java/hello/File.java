package hello;

import java.util.Base64;


public class File {
	
	String fileId;
	String name;
	Float size;
	
	
	public File(String fileId ,String name , Float size)
	{
		this.name=name;
		this.size=size;
		this.fileId= fileId;
		
		if (fileId.equals("")) this.fileId= Base64.getEncoder().encodeToString((name+size).getBytes());
		
	}
	
	
	//getters and setters
	
	public String get_fileId() {
		return fileId;
	}
	
	public String get_name() {
		return name;
	}
	
	public Float get_size() {
		return size;
	}
	
	public void set_fileId(String fileId) {
		this.fileId=fileId;
	}
	
	public void set_name(String name) {
		this.name=name;
	}
	
	
	public void set_size(Float size) {
		this.size=size;
	}

}
