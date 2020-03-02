package moe.keshane.webpserverjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

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


    @ResponseBody
    @RequestMapping("/**")
    public String output(HttpServletRequest request){
        String uri = request.getRequestURI();
//        String[] sarr = Arrays.stream(uri.split("/"))
//                .filter(value -> value != null && value.length() > 0 && value!="")
//                .toArray(size -> new String[size]);
//        String s = String.join("/", sarr);
        String realImagePath = config.getRealImagePath(uri);
        return realImagePath;
    }

    public boolean isImageExist(String imagePath){
        return new File(imagePath).exists();
    }
}
