package com.tigosv.serviceactivation;

import configuracion.Configuracion;
import generate.report.Driver;
import generate.report.Reporte;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import static javax.swing.UIManager.get;
import javax.xml.parsers.DocumentBuilder;
import org.hamcrest.core.Is;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

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
            } else {
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
            setRequest(req.getActivationHomeProcessProdRequest());
            generateEvidencesFiles(getRequest(), "prdRequest");
            RestAssured.baseURI = getBaseUri();
            res = RestAssured.given().headers(conf.headers).body(getRequest()).when().post(getPost()).then().log().all().extract()
                    .asString();
            setResponse(res);
            generateEvidencesFiles(getResponse(), "prdResponse");
        }
    }
    
    public void testPost(String request, String baseUri, String post, String reqType, String resType) {
        Response response;
        setBaseUri(baseUri);
        setPost(post);
        setRequest(req.getActivationHomeProcessProdRequest());
        response = RestAssured.given().headers(conf.headers).body(getRequest()).when().post(getPost());
        response.then().assertThat().body("messageCode", Is.is("1"));
        
    }

    public void validateXMLResponse(Response response) {

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
