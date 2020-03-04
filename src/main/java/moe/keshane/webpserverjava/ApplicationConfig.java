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
public class ApplicationConfig {
    public String host;
    public int port;
    public Map<String,String> imgMap;
    public List<String> allowedTypes;

    public ApplicationConfig(){}

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
     * @throws IOException read file may cause IOException
     */
    public static ApplicationConfig readConfig(String path) throws IOException {
        ObjectMapper json = new ObjectMapper();
        ApplicationConfig config = json.readValue(new File(path), ApplicationConfig.class);
        return config;
    }

    /**
     * @return the server runs on this port
     */
    public String getPort(){
        return port!=0?String.valueOf(port):"8080";
    }

}
