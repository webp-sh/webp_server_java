package moe.keshane.webpserverjava.Server;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Keshane
 * The server config class
 */
public class WebpServerConfig {
    @Override
    public String toString() {
        return "WebpServerConfig{" +
                "imgMap=" + imgMap +
                ", allowedTypes=" + allowedTypes +
                '}';
    }

    /**
     *
     * @param fileExtension the file extension name of request
     * @return
     */
    public boolean isAllowed(String fileExtension){
        return allowedTypes.indexOf(fileExtension)==-1?false:true;
    }

    /**
     *
     * @param requestUri the request uri like "/i/xxx.png"
     * @return
     */
    public String getRealImageDirectory(String requestUri){
        String[] splitUri = requestUri.split("/");
        int to = splitUri.length - 1;
        String[] dPath = Arrays.copyOfRange(splitUri, 0, to);
        String requestPath;
        if(dPath.length == 1){
            requestPath = "/"+dPath[0];
        }else{
            requestPath = String.join("/",dPath);
        }
        return imgMap.get(requestPath);
    }

    /**
     *
     * @param imgMap a map of request uri and real image directory
     *               uri:dir like {"/i":"/home/ubuntu/pic"}
     * @param allowedTypes a list of image file extension name
     *                     like ["png","jpg","webp"]
     */
    public WebpServerConfig(Map<String, String> imgMap, List<String> allowedTypes) {
        this.imgMap = imgMap;
        this.allowedTypes = allowedTypes;
    }

    public WebpServerConfig() {
    }

    /**
     *
     * @param imgMap a map of request uri and real image directory
     *               uri:dir like {"/i":"/home/ubuntu/pic"}
     */
    public void setImgMap(Map<String, String> imgMap) {
        this.imgMap = imgMap;
    }

    /**
     *
     * @param allowedTypes a list of image file extension name
     *                     like ["png","jpg","webp"]
     */
    public void setAllowedTypes(List<String> allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    private Map<String,String> imgMap;
    private List<String> allowedTypes;

}
