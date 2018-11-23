package hello;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Peer {

    private  String url;
    private  List <String> peer_list ;
    private List<file> file_list ;
    
    
    
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getPeer_list() {
		return peer_list;
	}

	public void setPeer_list(List<String> peer_list) {
		this.peer_list = peer_list;
	}

	public List<file> getFile_list() {
		return file_list;
	}

	public void setFile_list(List<file> file_list) {
		this.file_list = file_list;
	}

	
    
    public Peer(String url , List <String> peer_list, List <file> file_list) {
    	
    	this.url=url;
    	this.peer_list=peer_list;
    	this.file_list=file_list;
    	
    }
    	
    public Peer() {
    	 
    	String systemipaddress = ""; 
         try
         { 
             URL url_name = new URL("http://bot.whatismyipaddress.com"); 
   
             BufferedReader sc = 
             new BufferedReader(new InputStreamReader(url_name.openStream())); 
   
             // reads system IPAddress 
             systemipaddress = sc.readLine().trim(); 
         } 
         catch (Exception e) 
         { 
             systemipaddress = "Cannot Execute Properly"; 
         } 
       
        url = systemipaddress;
        
        peer_list = new ArrayList<String>();
        peer_list.add(url);
        
        file_list = new ArrayList <file>();
        


        File[] files = new File("/home/kharraz/Bureau").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null. 

        for (File file : files) {
        	
            if (file.isFile()) {
            	
            	file f = new file (file.getName(),file.getTotalSpace());
            	
                file_list.add(f);
            }
        }
                
    }

}