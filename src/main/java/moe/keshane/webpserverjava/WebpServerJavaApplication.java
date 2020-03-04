package moe.keshane.webpserverjava;

import com.luciad.imageio.webp.WebPWriteParam;
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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
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
    private static Logger log = LoggerFactory.getLogger(WebpServerJavaApplication.class);

    public static void main(String[] args) {
        String configPath = args[0];
        log.debug(args.toString());
        try {
            config = ApplicationConfig.readConfig(configPath);
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
        String uri = request.getRequestURI();
        String imageName = uri.split("/")[uri.split("/").length-1];
        String fileExtension = uri.split("\\.")[uri.split("\\.").length-1];
        if(!config.isAllowed(fileExtension)) throw new RuntimeException("File Not Allowed.");
        String realImageDirectory = config.getRealImageDirectory(uri);
        String realImagePath = Paths.get(realImageDirectory,imageName).toString();
        String cacheDir = Paths.get(realImageDirectory,".webp").toString();
        String webpImageName = imageName.split("\\.")[0]+".webp";
        String cacheImagePath = Paths.get(cacheDir,webpImageName).toString();
        if(!FileUtils.isExist(realImagePath)){
            try {
                Files.deleteIfExists(Paths.get(cacheImagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }

        String ua = request.getHeader("user-agent");
        if(ua.indexOf("Safari")!=-1&&ua.indexOf("Chrome")==-1&&ua.indexOf("Firefox")==-1){
            log.info("this is safari");
            return outputImage(realImagePath,request.getServletContext().getMimeType(new File(realImagePath).getAbsolutePath()));
        }

        FileUtils.createDir(cacheDir);
        if(FileUtils.isExist(cacheImagePath)){
            return outputImage(cacheImagePath,"image/webp");
        }
        try {
            WebpUtils.webpEncoder(realImagePath,cacheImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputImage(cacheImagePath,"image/webp");
    }

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
