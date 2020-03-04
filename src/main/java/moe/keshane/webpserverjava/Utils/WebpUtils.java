package moe.keshane.webpserverjava.Utils;

import com.luciad.imageio.webp.WebPWriteParam;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Keshane
 * a wrapper of Jwebp
 */
public class WebpUtils {
    /**
     * @param originPath origin image file path
     * @param webpPath webp image file path
     * @throws IOException read image may cause IOException
     */
    public static void webpEncoder(String originPath, String webpPath) throws IOException {
        // Obtain an image to encode from somewhere
        BufferedImage image = ImageIO.read(new File(originPath));
        // Obtain a WebP ImageWriter instance
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        // Configure encoding parameters
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(WebPWriteParam.MODE_DEFAULT);
//        writeParam.setCompressionMode(WebPWriteParam.MODE_EXPLICIT);
//        //if compression mode set WebPWriteParam.MODE_EXPLICIT then use this
//        writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
//        writeParam.setCompressionQuality(1f);
        // Configure the output on the ImageWriter
        writer.setOutput(new FileImageOutputStream(new File(webpPath)));
        // Encode
        writer.write(null, new IIOImage(image, null, null), writeParam);
    }
}
