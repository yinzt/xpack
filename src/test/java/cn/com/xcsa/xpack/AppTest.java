package cn.com.xcsa.xpack;

import cn.hutool.core.io.FileTypeUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.util.HashMap;

/**
 * Unit test for simple App.
 */
public class AppTest {

    public static void main(String[] args) throws IOException {
        File file = new File("d://ftp.3");
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        System.out.println(mimeType);
        //application/x-msdownload
    }
    public static String getMimeType(InputStream inputStream){
             AutoDetectParser parser = new AutoDetectParser();
             parser.setParsers(new HashMap<MediaType, Parser>());

             Metadata metadata = new Metadata();

             try {
                 parser.parse(inputStream, new DefaultHandler(), metadata, new ParseContext());
                 inputStream.close();
             } catch (TikaException | SAXException | IOException e) {
                 e.printStackTrace();
             }

             return metadata.get(HttpHeaders.CONTENT_TYPE);
         }
}
