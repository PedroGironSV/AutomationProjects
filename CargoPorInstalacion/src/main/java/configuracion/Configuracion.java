
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
            headers.put("Accept-Encoding", "gzip,deflate");
            headers.put("Content-Type", "text/xml;charset=UTF-8");
            headers.put("SOAPAction", "execute");
            headers.put("Host", "qa.api.tigo.com");
            headers.put("Connection", "Keep-Alive");
            headers.put("User-Agent", "Apache-HttpClient/4.5.5");
        } catch (Exception e) {
            System.out.println("Error setUpPost: " + e.getMessage());
        }
    }

    public Configuracion() {
        setUpPost();
    }
}
