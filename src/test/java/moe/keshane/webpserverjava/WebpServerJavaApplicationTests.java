package moe.keshane.webpserverjava;

import moe.keshane.webpserverjava.Server.WebpServer;
import moe.keshane.webpserverjava.Server.WebpServerConfig;
import moe.keshane.webpserverjava.Utils.FileUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Mock
    MockHttpServletRequest request;

    static String picPath = "/home/atlas/workspace/webp-server-java/i";

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
    @DisplayName("safaritest")
    void mockSafari() {
        for(int i=1;i<8;i++){
            Mockito.when(request.getRequestURI()).thenReturn("/"+i+".jpg");
            Mockito.when(request.getHeader("user-agent")).thenReturn("thisisSafari");
            File file = server.request(this.request);
            String[] f = file.toString().split("\\.");
            String extensionName = f[f.length-1];
            Assertions.assertNotEquals(extensionName,"webp");
        }
    }

    @Test
    @DisplayName("othertest")
    void mockOther() {
        for(int i=1;i<8;i++){
            Mockito.when(request.getRequestURI()).thenReturn("/"+i+".jpg");
//            request.setRequestURI(i+".jpg");
            Mockito.when(request.getHeader("user-agent")).thenReturn("thisisChrome");
            File file = server.request(this.request);
            String[] f = file.toString().split("\\.");
            String extensionName = f[f.length-1];
            Assertions.assertEquals(extensionName,"webp");
        }
    }

    @AfterEach
    void deleteWebp(){
        for(int i=1;i<8;i++) {
            String path = picPath+".webp/"+i+".webp";
            try {
                Files.deleteIfExists(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
