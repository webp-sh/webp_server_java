package moe.keshane.webpserverjava.Server;

import moe.keshane.webpserverjava.Exception.WebpServerException;
import moe.keshane.webpserverjava.Utils.FileUtils;
import moe.keshane.webpserverjava.Utils.WebpUtils;
import moe.keshane.webpserverjava.WebpServerJavaApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class WebpServer {
    private WebpServerConfig config;
    private static Logger log = LoggerFactory.getLogger(WebpServer.class);

    private WebpServer(WebpServerConfig config){
        this.config = config;
    }

    /**
     *
     * @param config a WebpServerConfig object
     * @return an object of WebpServer class
     * This method is the only way to create WebpServer object
     */
    public static WebpServer init(WebpServerConfig config){
        log.info(config.toString());
        return new WebpServer(config);
    }

    /**
     *
     * @param request HttpServletRequest object
     * @return File object
     * @throws WebpServerException if the extension name of request image will throw a WebpServerException
     * send your request in this method and return a File object
     * if the extension name of request image will throw a WebpServerException
     * if browser is safari would return origin image file
     * if not return webp image file
     */
    public File request(HttpServletRequest request){
        String uri = request.getRequestURI();
        log.info("requestURI:"+uri);
        String imageName = uri.split("/")[uri.split("/").length-1];
        String fileExtension = uri.split("\\.")[uri.split("\\.").length-1];
//        log.info(fileExtension);
        if(!config.isAllowed(fileExtension)) {
            throw new WebpServerException("File Not Allowed.");
        }
        String realImageDirectory = config.getRealImageDirectory(uri);
        String realImagePath = Paths.get(realImageDirectory,imageName).toString();
        String cacheDir = Paths.get(realImageDirectory,".webp").toString();
        String[] f = imageName.split("\\.");
        f[f.length-1]="webp";
        String fileName = String.join(".",f);
        String webpImageName = fileName;
        String cacheImagePath = Paths.get(cacheDir,webpImageName).toString();
        if(!FileUtils.isExist(realImagePath)){
            try {
                Files.deleteIfExists(Paths.get(cacheImagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        String ua = request.getHeader("user-agent");
        if(ua.indexOf("Safari")!=-1&&ua.indexOf("Chrome")==-1&&ua.indexOf("Firefox")==-1){
            return new File(realImagePath);
        }

        FileUtils.createDir(cacheDir);
        if(FileUtils.isExist(cacheImagePath)){
            return new File(cacheImagePath);
        }
        try {
            WebpUtils.webpEncoder(realImagePath,cacheImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(cacheImagePath);
    }

}
