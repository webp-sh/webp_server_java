package moe.keshane.webpserverjava.Server;

import moe.keshane.webpserverjava.Utils.FileUtils;
import moe.keshane.webpserverjava.Utils.WebpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class WebpServer {
    private WebpServerConfig config;

    private WebpServer(WebpServerConfig config){
        this.config = config;
    }

    public WebpServer init(WebpServerConfig config){
        return new WebpServer(config);
    }

    public File request(HttpServletRequest request){
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
