package moe.keshane.webpserverjava;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Keshane
 * config class of webp server
 */
public class Config {
    public String host;
    public int port;
    public Map<String,String> imgMap;
    public List<String> allowedTypes;

    public Config(){}

    @Override
    public String toString() {
        return "Config{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", imgMap=" + imgMap +
                ", allowedTypes=" + allowedTypes +
                '}';
    }

    /**
     * @param path config.json file path
     * @return This config object
     * @throws IOException
     */
    public static Config readConfig(String path) throws IOException {
        ObjectMapper json = new ObjectMapper();
        Config config = json.readValue(new File(path), Config.class);
        return config;
    }

    /**
     * @param requestUri
     * @return real image directory the request get
     */
    public String getRealImageDirectory(String requestUri){
        String[] splitUri = requestUri.split("/");
//        int to = splitUri.length>2?splitUri.length - 1:1;
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
     * @return the server runs on this port
     */
    public String getPort(){
        return port!=0?String.valueOf(port):"8080";
    }

    /**
     *
     * @param fileExtension request file extension name
     * @return is the request be allowed
     */
    public boolean isAllowed(String fileExtension){
        return allowedTypes.indexOf(fileExtension)==-1?false:true;
    }

}
