package eu.ase.httpserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class HTTPSeminarProtocol {
    private String makeOkOutput(String content, String contentType) {
        return "HTTP/1.1 200 OK\r\nContent-Type: " + contentType + "\r\nContent-Length: "
                + (content.length() + 2) + "\r\n\r\n" + content + "\r\n";
    }

    private String makeNotFoundOutput() {
        byte[] buffResp = new byte[4096];
        String fileContent = "";
        String theOutput = "";

        try {
            int bread = 0;
            FileInputStream fis = new FileInputStream("notfound.html");
            while ((bread = fis.read(buffResp)) != -1) {
                fileContent += new String(buffResp, 0, bread);
            }
            fis.close();
            theOutput = makeOkOutput(fileContent, "text/html");
        } catch (IOException e) {
            e.printStackTrace();
            theOutput = "HTTP/1.1 404\r\n\r\n";
        }
        return theOutput;
    }

    public String processInput(String theInput) {
        String theOutput = "";
        byte[] buffResp = new byte[4096];
        if (theInput.indexOf("GET") != 0) {
            theOutput = "HTTP/1.1 200 OK\r\nContent-Length: 19\r\nNU STIU COMANDA\r\n\r\n";
        } else {
            String fileName = theInput.substring(theInput.indexOf("/") + 1, theInput.indexOf(" HTTP/"));
            String fileExt = fileName.substring(fileName.indexOf(".") + 1);
            String contentType = "";
            String fileContent = "";
            if (fileExt.compareToIgnoreCase("txt") == 0)
                contentType = "text/html";
            if (fileExt.compareToIgnoreCase("html") == 0)
                contentType = "text/html";
            if (fileExt.compareToIgnoreCase("htm") == 0)
                contentType = "text/html";
            if (fileExt.compareToIgnoreCase("gif") == 0)
                contentType = "image/gif";
            if (fileExt.compareToIgnoreCase("class") == 0) {
                contentType = "text/html";
                // RUN java class in REFLECTIOn mode => "SERVLET CONTAINER"
                String className = fileName.substring(0, fileName.indexOf("."));

                try {
                    Class<?> class_ = Class.forName("eu.ase.httpserver." + className);
                    Object obj = class_.getConstructors()[0].newInstance();
                    Method method = class_.getMethod("sampleDoGet");

                    String result = method.invoke(obj).toString();
                    theOutput = makeOkOutput(result, contentType);
                } catch (Exception e) {
                    e.printStackTrace();
                    theOutput = makeNotFoundOutput();
                }
            } else {
                try {
                    int bread = 0;
                    FileInputStream fis = new FileInputStream(fileName);
                    while ((bread = fis.read(buffResp)) != -1) {
                        fileContent += new String(buffResp, 0, bread);
                    }
                    fis.close();
                    theOutput = makeOkOutput(fileContent, contentType);
                } catch (IOException ioec) {
                    ioec.printStackTrace();
                    theOutput = makeNotFoundOutput();
                }
            }
        }
        return theOutput;
    }
}
