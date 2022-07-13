package com.tigosv.cargoxinstalacion;

import configuracion.Configuracion;
import generate.report.Driver;
import generate.report.Reporte;
import io.restassured.RestAssured;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author pagiron
 */
public class WebService {

    Configuracion conf = new Configuracion();
    Request req = new Request();
    DocumentBuilder builder = null;
    InputSource src = new InputSource();
    Document doc;
    //BufferedImage screenShot;
    Driver driver = new Driver();
    Reporte repo = new Reporte();
    private String request, response, screenName, baseUri, post;
    private boolean promoExistente, promo1, promo2;

    public boolean hasConnection() {
        boolean isConected;

        do {
            Socket sock = new Socket();
            InetSocketAddress addres = new InetSocketAddress("qa.api.tigo.com", 80);
            isConected = false;
            try {
                sock.connect(addres, 0);
                if (sock.isConnected()) {
                    isConected = true;
                }
            } catch (Exception ex) {
                System.out.println("\n\nError en testConection: " + ex);
            }
            if (isConected == false) {
                System.out.println("\n\nConexión a VPN fallida, reintentando...");
            }else{
                System.out.println("\n\nConexión a VPN restablecida, reanudando pruebas...");
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (isConected == false);

        return isConected;
    }

    public void setPostResponse(String request, String baseUri, String post, String reqType, String resType) {
        if (hasConnection()) {
            String res;
            setBaseUri(baseUri);
            setPost(post);
            generateEvidencesFiles(request, reqType);
            RestAssured.baseURI = getBaseUri();
            res = RestAssured.given().headers(conf.headers).body(request).when().post(getPost()).then().extract()
                    .asString();
            setResponse(res);
            generateEvidencesFiles(getResponse(), resType);
        }
    }

    public void generateEvidencesFiles(String xmlText, String fileName) {
        setScreenName(fileName);
        String[] nodes = xmlText.split("<");
        String xmlFormat = "", finalPath;
        Path path;

        for (String node : nodes) {
            if (!node.equals("")) {
                xmlFormat += "<" + node + "\n";
            }
        }

        try {

            path = Paths.get(System.getProperty("user.dir") + "\\files\\" + fileName + ".xml");
            finalPath = path.toString();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(finalPath), "utf-8"));
            out.write(xmlFormat);
            out.close();
            //Se abre el archivo XML en el navegador
            driver.runWebDriver(getScreenName());
            //Espera a que carguen los elementos en la nueva pestaña
            repo.scape();
            JavascriptExecutor js = (JavascriptExecutor) driver.wdri;
            js.executeScript("document.title='" + getBaseUri() + "'");
            js.executeScript("history.pushState({}, '', '#" + getPost() + "')");
            waitPage();
            //Sacar captura de pantalla y almacenarla en el array de Reporte
            repo.screenShot(fileName);
            //Finalizar proceso ChromeDriver
            driver.quit();
        } catch (IOException e) {
            System.out.println("\nError en generateXMLFile: " + e);
        }
    }

    public String validateCxIResponse(String messageCode, float price, boolean ifSalesPersonProm, String messageDescription) {
        String tagData, messCode, messDes;
        float pri;
        //Valores por defecto: success, message
        String observaciones = "", errorMessage = "";
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            src.setCharacterStream(new StringReader(getResponse()));
            doc = builder.parse(src);

            //Extrae del XML unicamente la parte que está en formato JSON
            tagData = doc.getElementsByTagName("ns1:data").item(0).getTextContent();
            //Objeto individual para leer unicamente keys especificas, se le pasa el String que contiene TODO el formato JSON
            JSONObject data = new JSONObject(tagData);

            //Extraer los datos correspondientes
            messCode = data.getString("messageCode");
            pri = data.optFloat("price");
            setPromo1(data.getBoolean("ifSalesPersonProm"));
            messDes = data.getString("messageDescription");

            //Realizar las validaciones correspondientes por cada parametro
            //1. Validar messageCode
            if (!messCode.equals(messageCode)) {
                observaciones += "messageCode obtenido: \"" + messCode + "\"";
            }

            //2. Validar price
            if (pri != price) {
                observaciones += "\nprice obtenido: " + pri;
            }

            //3. Validar ifSalesPersonProm
            if (isPromo1() != ifSalesPersonProm) {
                if (isPromo1()) {
                    observaciones += "\nifSalesPersonProm obtenido: true";
                } else {
                    observaciones += "\nifSalesPersonProm obtenido: false";
                }
            }

            //4. Validar messageDescription
            if (!messDes.equals(messageDescription)) {
                observaciones += "\nmessageDescription obtenido: \"" + messDes + "\"";
            }

        } catch (IOException | ParserConfigurationException | JSONException | DOMException | SAXException ex) {
            System.out.println("\nError en validateCxIResponse: " + ex);
            errorMessage = doc.getElementsByTagName("ns1:serviceDescription").item(0).getTextContent();
            observaciones = errorMessage;
        }
        System.out.println("\n\nObservaciones: " + observaciones);
        return observaciones;
    }

    public String validateCPResponse(String observaciones) {
        String tagData, persCode = "", errorMessage = "";
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            src.setCharacterStream(new StringReader(getResponse()));
            doc = builder.parse(src);
            //Extrae del XML unicamente la parte que está en formato JSON
            tagData = doc.getElementsByTagName("ns1:data").item(0).getTextContent();
            //Objeto individual para leer unicamente keys especificas, se le pasa el String que contiene TODO el formato JSON
            JSONObject data = new JSONObject(tagData);
            setPromo2(data.getBoolean("ifSalesPersonProm"));
            persCode = data.getString("salesPersonCode");
            

            //Validar que el valor ifSalesPersonProm sea igual en ambos Web Services:
            if (isPromo1() != isPromo2()) {
                if (isPromo1()) {
                    observaciones += "\nifSalesPersonProm debería ser FALSE para el vendedor " + persCode;
                } else {
                    observaciones += "\nifSalesPersonProm debería ser TRUE para el vendedor " + persCode;
                }
            }

        } catch (IOException | ParserConfigurationException | SAXException ex) {
            System.out.println("\nError en validateCPResponse: " + ex);
            errorMessage = doc.getElementsByTagName("ns1:serviceDescription").item(0).getTextContent();
            observaciones = errorMessage;
        }

        return observaciones;
    }

    public void waitPage() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            System.out.println("\nError en waitPage: " + ex);
        }
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public boolean isPromoExistente() {
        return promoExistente;
    }

    public void setPromoExistente(boolean promoExistente) {
        this.promoExistente = promoExistente;
    }

    public boolean isPromo1() {
        return promo1;
    }

    public void setPromo1(boolean promo1) {
        this.promo1 = promo1;
    }

    public boolean isPromo2() {
        return promo2;
    }

    public void setPromo2(boolean promo2) {
        this.promo2 = promo2;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
