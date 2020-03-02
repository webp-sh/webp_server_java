package moe.keshane.webpserverjava;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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

    public static Config readConfig(String path) throws IOException {
        ObjectMapper json = new ObjectMapper();
        Config config = json.readValue(new File(path), Config.class);
        return config;
    }

    public String matchImgMap(String requestUri){
        return imgMap.get(requestUri);
    }

    public String getRealImageDirectory(String requestUri){
        String[] splitUri = requestUri.split("/");
        int to = splitUri.length>2?splitUri.length - 1:2;
        String[] dPath = Arrays.copyOfRange(splitUri, 0, to);
        String requestPath = String.join("/",dPath);
        return imgMap.get(requestPath);
    }

    public String getPort(){
        return port!=0?String.valueOf(port):"8080";
    }

    public boolean isAllowed(String fileExtension){
        return allowedTypes.indexOf(fileExtension)==-1?false:true;
    }

}
