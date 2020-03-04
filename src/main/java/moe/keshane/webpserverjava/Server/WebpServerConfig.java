package moe.keshane.webpserverjava.Server;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WebpServerConfig {
    public boolean isAllowed(String fileExtension){
        return allowedTypes.indexOf(fileExtension)==-1?false:true;
    }

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
    public WebpServerConfig(Map<String, String> imgMap, List<String> allowedTypes) {
        this.imgMap = imgMap;
        this.allowedTypes = allowedTypes;
    }

    public WebpServerConfig() {
    }

    public void setImgMap(Map<String, String> imgMap) {
        this.imgMap = imgMap;
    }

    public void setAllowedTypes(List<String> allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    private Map<String,String> imgMap;
    private List<String> allowedTypes;



}
