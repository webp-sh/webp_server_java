package moe.keshane.webpserverjava;

import moe.keshane.webpserverjava.Server.WebpServer;
import moe.keshane.webpserverjava.Server.WebpServerConfig;
import moe.keshane.webpserverjava.Utils.FileUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class WebpServerJavaApplicationTests {

    static Logger log = LoggerFactory.getLogger(WebpServerJavaApplicationTests.class);

    @Mock
    MockHttpServletRequest request;

    static String picPath = "src/test/resources/i";

    static WebpServer server;
    @BeforeAll
    static void initTest(){
        Map<String,String> map = new HashMap<>();
        map.put("/",picPath);
        List<String> li = Arrays.asList("jpg","png","webp");
        WebpServerConfig config = new WebpServerConfig(map,li);
        server = WebpServer.init(config);
    }

    @Test
    @DisplayName("SafariTest")
    void mockSafari() {
        File[] files = new File(picPath).listFiles();
        for(File file : files){
            if(file.isDirectory()){
                continue;
            }
            String name = file.getName();
            Mockito.when(request.getRequestURI()).thenReturn("/"+name);
            Mockito.when(request.getHeader("user-agent")).thenReturn("thisisSafari");
            File ff = server.request(this.request);
            String[] f = ff.toString().split("\\.");
            String extensionName = f[f.length-1];
            Assertions.assertNotEquals(extensionName,"webp");
        }
    }

    @Test
    @DisplayName("OtherTest")
    void mockOther() {
        File[] files = new File(picPath).listFiles();
        for(File file : files){
            if(file.isDirectory()){
                continue;
            }
            String name = file.getName();
            Mockito.when(request.getRequestURI()).thenReturn("/"+name);
            Mockito.when(request.getHeader("user-agent")).thenReturn("thisisChrome");
            File ff = server.request(this.request);
            log.info("webp file size:",ff.length());
            String[] f = ff.toString().split("\\.");
            String extensionName = f[f.length-1];
            Assertions.assertEquals(extensionName,"webp");
        }
    }

    @AfterEach
    void deleteWebp(){
        for(int i=1;i<8;i++) {
            String path = Paths.get(picPath,".webp/",String.valueOf(i)+".webp").toString();
            try {
                Files.deleteIfExists(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
