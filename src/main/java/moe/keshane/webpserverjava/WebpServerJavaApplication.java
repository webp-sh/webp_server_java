package moe.keshane.webpserverjava;

import com.luciad.imageio.webp.WebPWriteParam;
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
import javax.print.attribute.standard.Compression;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Keshane
 * main class of webp server java
 */
@Controller
@SpringBootApplication
public class WebpServerJavaApplication {
    private static Config config;
    private static Logger log = LoggerFactory.getLogger(WebpServerJavaApplication.class);

    public static void main(String[] args) {
        String configPath = args[0];
        log.debug(args.toString());
        try {
            config = Config.readConfig(configPath);
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
        if(!isExist(realImagePath)){
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

        createDir(cacheDir);
        if(isExist(cacheImagePath)){
            return outputImage(cacheImagePath,"image/webp");
        }
        try {
            webpEncoder(realImagePath,cacheImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputImage(cacheImagePath,"image/webp");
    }

    /**
     * @param dirPath directory path
     * @return is the directory exist
     */
    public boolean isExist(String dirPath){
        if(Files.exists(Paths.get(dirPath),new LinkOption[]{ LinkOption.NOFOLLOW_LINKS})){
            return true;
        }
        return false;
    }

    /**
     * @param dirPath directory path
     * @return is the directory be created
     */
    public boolean createDir(String dirPath){
        if(!isExist(dirPath)){
            try {
                Path path = Files.createDirectories(Paths.get(dirPath));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
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

    /**
     * @param originPath origin image file path
     * @param webpPath webp image file path
     * @throws IOException
     */
    public void webpEncoder(String originPath, String webpPath) throws IOException {
        // Obtain an image to encode from somewhere
        BufferedImage image = ImageIO.read(new File(originPath));
        // Obtain a WebP ImageWriter instance
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        // Configure encoding parameters
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(WebPWriteParam.MODE_DEFAULT);
//        writeParam.setCompressionMode(WebPWriteParam.MODE_EXPLICIT);
//        //if compression mode set WebPWriteParam.MODE_EXPLICIT then use this
//        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
//        writeParam.setCompressionQuality(1f);
        // Configure the output on the ImageWriter
        writer.setOutput(new FileImageOutputStream(new File(webpPath)));
        // Encode
        writer.write(null, new IIOImage(image, null, null), writeParam);
    }
}
