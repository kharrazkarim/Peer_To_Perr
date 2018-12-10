package hello;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class PeerController {

   
    public final Peer peer;
    private final Path rootLocation;

 	

    @Autowired
    private PeerController(Peer peer,StorageProperties properties ) {
        this.rootLocation = Paths.get(properties.getLocation()) ;
        this.peer=peer.getInstance();
     
    }
    
    
    @RequestMapping(method=RequestMethod.GET, value ="/peers" )
    @ResponseBody
	public String peer_list( HttpServletResponse response) {
    	
    	JSONObject jObject = new JSONObject();
    	int nb = peer.getPeer_list().size();
    	
    		
    	try
    	{
    	    JSONArray jArray = new JSONArray();
    	    for (int i =0 ; i< nb ;i++)
    	    {
    	         //JSONObject URL = new JSONObject();
    	         //URL.put("url", peer.getPeer_list().get(i));
    	         jArray.put(peer.getPeer_list().get(i));
    	    }
    	    jObject.put("list", jArray);
    	} catch (JSONException jse) {
    	    jse.printStackTrace();
    	}
    	response.setStatus( HttpServletResponse.SC_OK );
    	return jObject.toString();

    	} 
    
    
    @RequestMapping(method=RequestMethod.POST, value ="/peers" )
    public void register_peer (@RequestBody Map<String, String> body , HttpServletResponse response) {
    	
    	 boolean rep ;
    	 String url = body.get("url");
    	rep= peer.addPeer(url);
    	
    	if (rep== true) response.setStatus( HttpServletResponse.SC_OK );
    	else response.setStatus( HttpServletResponse.SC_CONFLICT);
    	 	 
    }
     
    
    @RequestMapping(method=RequestMethod.DELETE, value ="/peers" )
    public void delete_peer (@RequestBody Map<String, String> body , HttpServletResponse response) {
    	
    	boolean rep ;
    	String url = body.get("url");
    	rep= peer.deletePeer(url);
    	
    	if (rep== true) response.setStatus( HttpServletResponse.SC_OK );
    	else response.setStatus( HttpServletResponse.SC_EXPECTATION_FAILED);
    }
    
    
    
    @RequestMapping(method= RequestMethod.GET, value="/files/{fileId}")
    @ResponseBody
    public String serveFile(@PathVariable String fileId, HttpServletResponse response) {
    	
        Path path =rootLocation.resolve(fileId);
        byte[] fileContent = null;
		try {
			fileContent = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			return null;
			
		}
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        
        response.setStatus( HttpServletResponse.SC_OK );
        return encodedString;
    }
   
    @RequestMapping(method= RequestMethod.POST, value="/files/{fileId}")
    @ResponseBody
    public void uploadFile(@PathVariable String fileId, HttpServletResponse response ,@RequestBody String encodedString) {
    	
    	boolean rep = false;
    	byte[] decodedBytes=null;
    	
    	for (File file : peer.getFile_list())
  	    {
     		 if (file.get_fileId().equals(fileId))	 rep = true;	
     			
  	    	}
    	
		if (rep) response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
    	
    	else 
    	{ 	 
    		 
			 decodedBytes = Base64.getDecoder().decode(encodedString);
			 InputStream targetStream = new ByteArrayInputStream(decodedBytes);
			 try {
				Files.copy(targetStream, rootLocation.resolve(fileId),
				            StandardCopyOption.REPLACE_EXISTING);
				response.setStatus( HttpServletResponse.SC_OK );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
    	}
    	
       
    }
   
    
    @RequestMapping(method=RequestMethod.DELETE, value="/files/{fileId}") 
    public void delete_File (HttpServletResponse response, @PathVariable String fileId)
    {
    	boolean rep= false;
    	int index = 0;
    	 for (File file : peer.getFile_list())
 	    {
    		 if (file.get_fileId().equals(fileId))
    			 {	
    			 	rep=true;
    			 	index= peer.getFile_list().indexOf(file);
    			 	
    			 }
 	    }
    	
    	if (rep)
	    	
    	{		
    			peer.getFile_list().remove(index);
    			Path path =rootLocation.resolve(fileId);
    			java.io.File f = path.toFile();
    			FileSystemUtils.deleteRecursively(f);
    			response.setStatus( HttpServletResponse.SC_OK );
	    	}	
    	
    	else  response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
    	
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/files")    
    public void File_Meta(@RequestBody Map<String, String> newFile , HttpServletResponse response) throws NumberFormatException, JSONException {
    	
    	boolean rep = false;
    	
    	for (File file : peer.getFile_list())
  	    {
     		 if (file.get_fileId().equals(newFile.get("fileId").toString()))	 rep = true;	
     			
  	    	}
    	
		if (rep) response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
    	
    	else
    		
    	{	
    		File f = new File (newFile.get("name").toString(),Float.valueOf(newFile.get("size").toString()));
    		peer.addFile(f);
    		response.setStatus( HttpServletResponse.SC_OK);
    	}
		
    }
    	
   
    @RequestMapping(method=RequestMethod.GET, value="/files")    
    public String list_Files(HttpServletResponse response) {
    	
    	JSONObject jObject = new JSONObject();
    	
    	try
    	{
    	    JSONArray jArray = new JSONArray();

    	    for (File file : peer.getFile_list())
    	    {
    	         JSONObject FILE = new JSONObject();
    	         FILE.put("fileId", file.get_fileId());
    	         FILE.put("name", file.get_name());
    	         FILE.put("size", file.get_size());
    	         jArray.put(FILE);
    	    }
    	    jObject.put("list", jArray);
    	} catch (JSONException jse) {
    	    jse.printStackTrace();
    	}
    	response.setStatus( HttpServletResponse.SC_OK );
    	return jObject.toString();

   
    }
   
    
    @RequestMapping(method=RequestMethod.GET, value="/")    
    public String welcome(Model model, HttpServletRequest request) throws IOException {
    		
     model.addAttribute("peers", peer.getPeer_list());
     model.addAttribute("files",peer.getFile_List_Names());
     
     return "welcome";
     
    }
    
  

    @RequestMapping(method=RequestMethod.GET, value ="/list" )
    public String list (HttpServletRequest request, @RequestParam String p ) throws JSONException {
    
    	String url;
    	RestTemplate restTemplate = new RestTemplate();
    	 
         //Send request with GET method and default Headers.
    	JSONArray result = restTemplate.getForObject("http://"+p+"/files", JSONArray.class);
       
    	
        return "redirect:/";
    }
    
    
    @RequestMapping(method=RequestMethod.GET, value ="/add" )
    public String add (HttpServletRequest request, @RequestParam String p ) throws JSONException {
    
    	String url;
    	RestTemplate restTemplate = new RestTemplate();
    	 
         //Send request with GET method and default Headers.
    	JSONArray result = restTemplate.getForObject("http://"+p+"/peers", JSONArray.class);
       
    	
    	for (int i=0 ; i< result.length();i++)
    	{
    	 url =result.getJSONObject(i).toString();
    	peer.addPeer(url);
    	}
        return "redirect:/";
    }
    
    @RequestMapping(method= RequestMethod.POST, value="/upload" , produces = MediaType.APPLICATION_JSON_VALUE)
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
    	
    	File newfile = new File(file.getOriginalFilename(),(float) file.getSize());
    	peer.addFile(newfile);
    	
    	
      
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(newfile.get_fileId()),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        
        catch (IOException e) {
            throw new Exception("Failed to store file ");
}
       
        
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
    
        

  
    
}