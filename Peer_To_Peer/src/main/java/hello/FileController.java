package hello;

import java.io.IOException;
import java.util.stream.Collectors;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.store.StorageFileNotFoundException;
import hello.store.StorageService;


@Controller
public class FileController {

    public final StorageService storageService;
    public final Peer peer;
    @Autowired
    private FileController(StorageService storageService, Peer peer) {
        
        this.storageService=storageService;
        this.peer=peer;
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/")    
    public String listUploadedFiles(Model model, HttpServletRequest request) throws IOException {
    	

    		
     model.addAttribute("peers", peer.getPeer_list());
     
     model.addAttribute("files", storageService.loadAll().map(
             path -> MvcUriComponentsBuilder.fromMethodName(FileController.class,
                     "serveFile", path.getFileName().toString()).build().toString())
             .collect(Collectors.toList()));
	return "uploadForm";
   
    }

    @RequestMapping(method= RequestMethod.GET, value="/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
 
    @RequestMapping(method= RequestMethod.POST, value="/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
    
 
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
    
   
    
}
