package moe.keshane.webpserverjava;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

@Controller
@SpringBootApplication
public class WebpServerJavaApplication {
    private static Config config;

    public static void main(String[] args) {
        String configPath = args[0];
        System.out.println(args.toString());
        try {
            config = Config.readConfig(configPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(config.toString());
        SpringApplication app = new SpringApplication(WebpServerJavaApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", config.getPort()));
        app.run(args);
    }


    @ResponseBody
    @RequestMapping("/**")
    public String output(HttpServletRequest request){
        String uri = request.getRequestURI();
        return uri;
    }

    
}
