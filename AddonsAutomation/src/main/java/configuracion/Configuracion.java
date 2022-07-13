package configuracion;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author pagiron
 */
public class Configuracion {

    //public boolean cbsValidation, urnValidation;
    public Map<String, Object> headers = new LinkedHashMap<>();

    public void setUpPost() {
        //Colocar los headers correspondientes del Web Service
        try {
            headers.put("User-Agent", "PostmanRuntime/7.29.0");
            headers.put("Accept", "*/*");
            headers.put("Accept-Encoding", "gzip, deflate, br");
            headers.put("Connection", "keep-alive");
            headers.put("Content-Type", "text/xml; charset=utf-8");
        } catch (Exception e) {
            System.out.println("Error setUpPost: " + e.getMessage());
        }
    }

    public Configuracion() {
        setUpPost();
    }
}
