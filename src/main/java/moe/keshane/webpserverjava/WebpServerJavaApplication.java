package moe.keshane.webpserverjava;

import com.luciad.imageio.webp.WebPWriteParam;
import moe.keshane.webpserverjava.Server.WebpServer;
import moe.keshane.webpserverjava.Server.WebpServerConfig;
import moe.keshane.webpserverjava.Utils.FileUtils;
import moe.keshane.webpserverjava.Utils.WebpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * @author Keshane
 * main class of webp server java
 */
@Controller
@SpringBootApplication
public class WebpServerJavaApplication {
    private static ApplicationConfig config;
    private static WebpServer server;
    private static Logger log = LoggerFactory.getLogger(WebpServerJavaApplication.class);

    public static void main(String[] args) {
        String configPath = args[0];
        log.debug(args.toString());
        try {
            config = ApplicationConfig.readConfig(configPath);
            WebpServerConfig webpConfig = new WebpServerConfig(config.imgMap,config.allowedTypes);
            server = WebpServer.init(webpConfig);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("config file error");
        }
        log.debug(config.toString());
        SpringApplication app = new SpringApplication(WebpServerJavaApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", config.getPort()));
        app.run(args);
    }

    @RequestMapping("/**")
    public ResponseEntity<FileSystemResource> output(HttpServletRequest request, HttpServletResponse response){
        File file = server.request(request);
        if(file == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        if(request.getServletContext().getMimeType(file.getAbsolutePath()).equals("image/webp")){
            return outputImage(file.toString(),request.getServletContext().getMimeType(file.getAbsolutePath()));
        }
        return outputImage(file.toString(),"image/webp");
    }

//    request.getServletContext().getMimeType(new File(realImagePath).getAbsolutePath())
//    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            return null;

    /**
     * @param imagePath the image file path
     * @param contentType content-type
     * @return the response return to user
     */
    public ResponseEntity<FileSystemResource> outputImage(String imagePath,String contentType){
        FileSystemResource resource = new FileSystemResource(imagePath);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }


}
