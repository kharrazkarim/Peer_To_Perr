package hello;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import hello.store.StorageService;


@RestController
public class PeerController {

    public final StorageService storageService;
    public final Peer peer;
	

    @Autowired
    private PeerController(StorageService storageService, Peer peer) {
        this.storageService = storageService;
        this.peer=peer.getInstance();
     
    }
    

    
    @RequestMapping(method=RequestMethod.POST, value ="/peers" )
    @ResponseStatus(value = HttpStatus.OK)
    public void register_peer (HttpServletRequest request) {
    	
    	 
    	 String url = request.getRemoteAddr();
      
    	 	peer.addPeer(url);
    	 	 
    }
 
    @RequestMapping(method=RequestMethod.GET, value ="/add" )
    @ResponseStatus(value = HttpStatus.OK)
    public void add (HttpServletRequest request, @RequestParam String p ) {
    	
    	RestTemplate restTemplate = new RestTemplate();
    	 
        // Send request with GET method and default Headers.
        List result = restTemplate.getForObject("http://"+p+":8880/peers", List.class);
        
         List<String> list = new ArrayList<String>();
         list =peer.getInstance().getPeer_list();
         
         for (int i=0; i< result.size();i++)
         {
        	 if (! list.contains(result.get(0)))
         list.add(result.get(i).toString());
         
         }
         peer.setPeer_list(list);
  
        
    }
    
    
    @RequestMapping(method=RequestMethod.DELETE, value ="/peers" )
    @ResponseStatus(value = HttpStatus.OK)
    public void delete_peer (HttpServletRequest request) {
    	
    	 
    	 String url = request.getRemoteAddr();
    	 
    	 if (peer.getPeer_list().contains(url)==true ) 
    	 	 peer.deletePeer(url);
  	   	        
    }
    
  

    @RequestMapping(method=RequestMethod.GET, value ="/peers" )
    @ResponseBody
	public String peer_list() {
    	
    	JSONObject jObject = new JSONObject();
    	try
    	{
    	    JSONArray jArray = new JSONArray();
    	    for (int i =0 ; i< peer.getPeer_list().size();i++)
    	    {
    	         JSONObject URL = new JSONObject();
    	         URL.put("url", peer.getPeer_list().get(i));
 
    	         jArray.put(URL);
    	    }
    	    jObject.put("List", jArray);
    	} catch (JSONException jse) {
    	    jse.printStackTrace();
    	}
        return jObject.toString();

    	} 
    
    @RequestMapping(method=RequestMethod.GET, value="/files")    
    public String list_Files() {
    	JSONObject jObject = new JSONObject();
    	try
    	{
    	   
    	    for (File file : peer.getFile_list())
    	    {
    	         JSONObject FILE = new JSONObject();
    	         FILE.put("fileId", file.get_fileId());
    	         FILE.put("name", file.get_name());
    	         FILE.put("size", file.get_size());
    	         jObject.put(file.get_fileId(), FILE);
    	     
    	    }
    	    
    	} catch (JSONException jse) {
    	    jse.printStackTrace();
    	}
        return jObject.toString();

   
    }
   
    
}