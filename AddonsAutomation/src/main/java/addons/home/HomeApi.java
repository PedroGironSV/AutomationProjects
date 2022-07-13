package addons.home;

import io.restassured.RestAssured;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import configuracion.Configuracion;
import generate.report.Driver;
import generate.report.Reporte;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.xml.sax.SAXException;

/**
 *
 * @author pagiron
 */
public class HomeApi {

    Configuracion conf = new Configuracion();
    Reporte rep = new Reporte();
    Driver driver = new Driver();

    protected ArrayList<String> datos;
    public boolean showAvailableAddons, showActiveAddons, menageAddons, cbsValidation, urnValidation;
    public String apiID, request, offerID, response, resultado, filePath, screenName;
    public String accountId, action, offerCBS, serie, urn, target = "", endpoint = "", completeURL = "";
    protected String offerRef, responseMessage, value, type;
    public NodeList offerRefs, descriptions, amounts, currencys, cbsOfferingID;
    protected DocumentBuilder builder = null;
    protected InputSource src = new InputSource();
    protected Document doc;

    public String setMatrixData(String filePath, int sheetIndex, int fila) {
        String datosPrueba = "";
        datos = new ArrayList<>();
        //Cargar y filtrar datos de prueba de la Matriz seleccionada
        datosPrueba = rep.readWorkbook(filePath, sheetIndex, fila);
        if (!datosPrueba.isEmpty()) {
            datos = rep.getTestData(datosPrueba);
            if (menageAddons) {
                accountId = datos.get(0);
                offerID = datos.get(1);
                value = datos.get(2);
                offerCBS = datos.get(3);
                serie = datos.get(4);
            } else {
                if (urnValidation) {
                    value = datos.get(0);
                    urn = datos.get(1);
                } else {
                    value = datos.get(0);
                    offerID = datos.get(1);
                }
            }
        } else {
            showAvailableAddons = false;
            showActiveAddons = false;
            menageAddons = false;
        }
        return datosPrueba;
    }

    public String setApiRequest() {
        //Validar categoría type
        if (HomeScreen.category.equals("Home")) {
            type = "ACCOUNTID";
        } else if (HomeScreen.category.equals("Mobile")) {
            type = "MSISDN";
        }
        //Validar el API que se ejecutará
        if (showAvailableAddons) {
            apiID = "getAvailableAddonsCatalog";
        } else {
            if (showActiveAddons) {
                apiID = "getActiveCustomerAddons";
            } else if (menageAddons) {
                apiID = "manageAddons";
            }
        }

        if (menageAddons) {
            request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.manageAddonsService.ea.tigo.com/\">\r\n"
                    + "   <soapenv:Header/>\r\n"
                    + "   <soapenv:Body>\r\n"
                    + "      <ser:" + apiID + ">\r\n"
                    + "         <!--Optional:-->\r\n"
                    + "         <" + apiID + "Request>\r\n"
                    + "            <!--Optional:-->\r\n"
                    + "            <accountId>" + accountId + "</accountId>\r\n"
                    + "            <!--Optional:-->\r\n"
                    + "            <acquisitionMethodId>PURCHASE</acquisitionMethodId>\r\n"
                    + "            <!--Optional:-->\r\n"
                    + "            <action>" + action + "</action>\r\n"
                    + "            <!--Optional: 2065-->\r\n"
                    + "            <offerId>" + offerID + "</offerId>\r\n"
                    + "            <!--Optional:-->\r\n"
                    + "            <searchType>HOMEID</searchType>\r\n"
                    + "            <!--Optional:-->\r\n"
                    + "            <searchValue>" + value + "</searchValue>\r\n"
                    + "         </" + apiID + "Request>\r\n"
                    + "      </ser:" + apiID + ">\r\n"
                    + "   </soapenv:Body>\r\n"
                    + "</soapenv:Envelope>";
        } else {

            request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.manageAddonsService.ea.tigo.com/\">\r\n"
                    + "   <soapenv:Header/>\r\n"
                    + "   <soapenv:Body>\r\n"
                    + "      <ser:" + apiID + ">\r\n"
                    + "         <!--Optional:-->\r\n"
                    + "         <" + apiID + "Request>\r\n"
                    + "            <!--Optional:-->\r\n"
                    + "            <searchType>" + type + "</searchType>\r\n"
                    + "            <!--Optional:-->\r\n"
                    + "            <searchValue>" + value + "</searchValue>\r\n"
                    + "         </" + apiID + "Request>\r\n"
                    + "      </ser:" + apiID + ">\r\n"
                    + "   </soapenv:Body>\r\n"
                    + "</soapenv:Envelope>";
        }

        showAvailableAddons = false;
        showActiveAddons = false;
        menageAddons = false;

        return request;
    }

    public boolean hasConnection(String endpoint, int port) {
        boolean isConected;

        do {
            Socket sock = new Socket();
            InetSocketAddress addres = new InetSocketAddress(endpoint, port);
            isConected = false;

            try {
                sock.connect(addres, 0);
                if (sock.isConnected()) {
                    isConected = true;
                }
            } catch (Exception ex) {
                System.out.println("\n\nError en hasConnection: " + ex);
            }
            if (isConected == false) {
                System.out.println("\n\nConexión a VPN fallida, reintentando...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("\n\nConexión a VPN restablecida, reanudando pruebas...");
            }
        } while (isConected == false);

        return isConected;
    }

    public String getPostResponse(String request) {
        try {
            if (HomeScreen.environment.equals("UAT")) {
                if (cbsValidation) {
                    if (hasConnection("192.168.119.132", 8080)) {
                        RestAssured.baseURI = "http://192.168.119.132:8080";
                        response = RestAssured.given().headers(conf.headers).body(request).when().post("services/BcServices?wsdl").then().extract()
                                .asString();
                        cbsValidation = false;
                    }

                } else {
                    if (urnValidation) {
                        if (hasConnection("192.168.215.17", 7018)) {
                            RestAssured.baseURI = "http://192.168.215.17:7018";
                            response = RestAssured.given().headers(conf.headers).body(request).when().post("SearchHomeCustomersFacade/ApiTigoPlay").then().extract()
                                    .asString();
                            urnValidation = false;
                        }

                    } else {
                        if (hasConnection("192.168.215.17", 7018)) {
                            RestAssured.baseURI = "http://192.168.215.17:7018";
                            response = RestAssured.given().headers(conf.headers).body(request).when().post("TigoSelfCareManageAddons/ManageAddonsService").then().extract()
                                    .asString();
                        }
                    }
                }
            } else if (HomeScreen.environment.equals("PRD")) {
                if (cbsValidation) {
                    if (hasConnection("192.168.128.41", 8080)) {
                        RestAssured.baseURI = "http://192.168.128.41:8080";
                        response = RestAssured.given().headers(conf.headers).body(request).when().post("services/BcServices?wsdl").then().extract()
                                .asString();
                        cbsValidation = false;
                    }

                } else {
                    if (urnValidation) {
                        if (hasConnection("tsbprod.sv.tigo.com", 80)) {
                            RestAssured.baseURI = "http://tsbprod.sv.tigo.com";
                            response = RestAssured.given().headers(conf.headers).body(request).when().post("soa-infra/services/tsb/esb-core/ServiceConsumer").then().extract()
                                    .asString();
                            urnValidation = false;
                        }

                    } else {
                        if (hasConnection("", 80)) {
                            RestAssured.baseURI = "https://api.tigo.com.sv";
                            response = RestAssured.given().headers(conf.headers).body(request).when().post("TigoSelfCareManageAddons/ManageAddonsService?WSDL=").then().extract()
                                    .asString();
                        }
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("Validar Rest POST: " + ex);
        }

        return response;
    }

    public void setApiReader(String response) {

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            src.setCharacterStream(new StringReader(response));
            doc = builder.parse(src);
        } catch (Exception e) {
            System.out.println("\n\nOcurrio el siguiente error: " + e.getMessage());
        }
    }

    public void runApi(String nameReq, String nameRes) {
        if (cbsValidation) {
            request = consultCBSRequest();
            target = offerCBS;
            if (HomeScreen.environment.equals("UAT")) {
                endpoint = "http://192.168.119.132:8080";
                completeURL = "services/BcServices?wsdl";
            } else {
                endpoint = "http://192.168.128.41:8080";
                completeURL = "services/BcServices?wsdl";
            }
        } else {
            if (urnValidation) {
                request = setURNRequest();
                target = "";
                if (HomeScreen.environment.equals("UAT")) {
                    endpoint = "http://192.168.215.17:7018";
                    completeURL = "SearchHomeCustomersFacade/ApiTigoPlay";
                } else {
                    endpoint = "http://tsbprod.sv.tigo.com";
                    completeURL = "soa-infra/services/tsb/esb-core/ServiceConsumer";
                }
            } else {
                request = setApiRequest();
                target = offerID;
                if (HomeScreen.environment.equals("UAT")) {
                    endpoint = "http://192.168.215.17:7018";
                    completeURL = "TigoSelfCareManageAddons/ManageAddonsService";
                } else {
                    endpoint = "https://api.tigo.com.sv";
                    completeURL = "TigoSelfCareManageAddons/ManageAddonsService?WSDL=";
                }
            }
        }
        screenName = generateXML(request, nameReq);
        //No se especifica valor buscado para el request
        saveEvidence(screenName, "");
        response = getPostResponse(request);
        screenName = generateXML(response, nameRes);
        //Se indica el tipo de target que debe ubicar dentro del response
        saveEvidence(screenName, target);
        setApiReader(response);
    }

    public String generateXML(String xmlText, String screenName) {
        String[] nodes = xmlText.split("<");
        String xmlFormat = "", finalPath = "";
        Path path;

        for (int i = 0; i < nodes.length; i++) {
            if (!nodes[i].equals("")) {
                xmlFormat += "<" + nodes[i] + "\n";
            }
        }

        try {

            path = Paths.get(System.getProperty("user.dir") + "\\responses\\" + screenName + ".xml");
            finalPath = path.toString();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(finalPath), "utf-8"));
            out.write(xmlFormat);
            out.close();
        } catch (Exception e) {
            System.out.println("\nError al generar XML: " + e.getMessage() + "\nRuta: " + finalPath);
        }
        return screenName;
    }

    public void centerElement(String targetValue) {
        if (!targetValue.isEmpty()) {
            ArrayList<WebElement> element = new ArrayList<>();
            By xpath = By.xpath("//*[normalize-space(text())='" + targetValue + "']");
            element = (ArrayList<WebElement>) driver.wdri.findElements(xpath);
            JavascriptExecutor js = (JavascriptExecutor) driver.wdri;
            String moveTo = "arguments[0].scrollIntoView({"
                    + "behavior : 'auto',"
                    + "block: 'center',"
                    + "inline: 'center'});";

            try {
                js.executeScript(moveTo, element.get(1));
            } catch (Exception ex) {
                System.out.println("Elemento " + targetValue + " NO encontrado ---> " + ex);
            }
            //Limpiar el target luego de analizar cada response
            target = "";
        }
    }

    public void saveEvidence(String screenName, String textSearched) {
        //Sacar captura
        driver.runWebDriver(screenName);
        rep.scape();
        JavascriptExecutor js = (JavascriptExecutor) driver.wdri;
        js.executeScript("document.title='" + endpoint + "'");
        js.executeScript("history.pushState({}, '', '#" + completeURL + "')");
        centerElement(textSearched);
        espera();
        rep.screenShot(screenName);
        driver.quit();
        //finalReport = rep.addEvidence(screenName);
    }

    public String getResponseMessage(String nameReq, String nameRes) {
        //Ejecutar el API
        runApi(nameReq, nameRes);

        responseMessage = doc.getElementsByTagName("responseMessage").item(0).getTextContent();
        if (!responseMessage.equals("00") && !responseMessage.equals("Operación satisfactoria")) {
            resultado = responseMessage;
        } else {
            //Validar los respectivos datos
            offerRefs = doc.getElementsByTagName("offerId");
            descriptions = doc.getElementsByTagName("description");
            amounts = doc.getElementsByTagName("amount");
            resultado = "Ejecución exitosa";
        }
        return resultado;
    }

    public String activateOffers(String nameReq, String nameRes, String act) {
        action = act;
        runApi(nameReq, nameRes);
        //menageAddons = false;
        responseMessage = doc.getElementsByTagName("responseMessage").item(0).getTextContent();
        if (!responseMessage.equals("0") && !responseMessage.equals("additional service modified succesfully")) {
            resultado = responseMessage;
        } else {
            resultado = "Ejecución exitosa";
        }
        return resultado;
    }

    public String consultCBSRequest() {
        String cbsRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:bcs=\"http://www.huawei.com/bme/cbsinterface/bcservices\" xmlns:cbs=\"http://www.huawei.com/bme/cbsinterface/cbscommon\" xmlns:bcc=\"http://www.huawei.com/bme/cbsinterface/bccommon\">\n"
                + "   <soapenv:Header/>\n"
                + "   <soapenv:Body>\n"
                + "      <bcs:QueryCustomerInfoRequestMsg>\n"
                + "         <RequestHeader>                                                                                                                                                                                                                                           \n"
                + "            <cbs:Version>1</cbs:Version>                                                                                                                                                                                                                           \n"
                + "            <cbs:BusinessCode>1</cbs:BusinessCode>                                                                                                                                                                                                                 \n"
                + "            <cbs:MessageSeq>${=new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSS\").format(new Date())}</cbs:MessageSeq>                                                                                                                                                                                                       \n"
                + "            <cbs:OwnershipInfo>                                                                                                                                                                                                                                    \n"
                + "               <cbs:BEID>101</cbs:BEID>                                                                                                                                                                                                                            \n"
                + "               <cbs:BRID>101</cbs:BRID>                                                                                                                                                                                                                            \n"
                + "            </cbs:OwnershipInfo>                                                                                                                                                                                                                                   \n"
                + "            <cbs:AccessSecurity>                                                                                                                                                                                                                                   \n"
                + "               <cbs:LoginSystemCode>102</cbs:LoginSystemCode>                                                                                                                                                                                                    \n"
                + "               <cbs:Password>xyYSFeOUi5DagegPuCQmUQ==</cbs:Password>                                                                                                                                                                                                                 \n"
                + "               <cbs:RemoteIP>192.168.128.42</cbs:RemoteIP>                                                                                                                                                                                                            \n"
                + "            </cbs:AccessSecurity>                                                                                                                                                                                                                                  \n"
                + "            <cbs:OperatorInfo>                                                                                                                                                                                                                                     \n"
                + "               <cbs:OperatorID>101</cbs:OperatorID>                                                                                                                                                                                                                \n"
                + "               <cbs:ChannelID>1</cbs:ChannelID>                                                                                                                                                                                                                  \n"
                + "            </cbs:OperatorInfo>                                                                                                                                                                                                                                    \n"
                + "            <cbs:TimeFormat>                                                                                                                                                                                                                                       \n"
                + "               <cbs:TimeType>1</cbs:TimeType>                                                                                                                                                                                                                      \n"
                + "               <cbs:TimeZoneID>1</cbs:TimeZoneID>                                                                                                                                                                                                                  \n"
                + "            </cbs:TimeFormat>                                                                                                                                                                                                                                      \n"
                + "         </RequestHeader>\n"
                + "         <QueryCustomerInfoRequest>\n"
                + "            <bcs:QueryObj>\n"
                + "               <!--You have a CHOICE of the next 4 items at this level-->\n"
                + "               <bcs:SubAccessCode>\n"
                + "                  <!--You have a CHOICE of the next 2 items at this level-->\n"
                + "                  <bcc:PrimaryIdentity>" + serie + "</bcc:PrimaryIdentity>\n"
                + "               </bcs:SubAccessCode>\n"
                + "            </bcs:QueryObj>\n"
                + "         </QueryCustomerInfoRequest>\n"
                + "      </bcs:QueryCustomerInfoRequestMsg>\n"
                + "   </soapenv:Body>\n"
                + "</soapenv:Envelope>";
        return cbsRequest;
    }

    public boolean validateCBSResponse() {
        boolean isActive = false;
        try {
            Document responseXML = builder.parse(new ByteArrayInputStream(response.getBytes()));
            cbsOfferingID = responseXML.getElementsByTagName("bcc:OfferingID");

            for (int i = 0; i < cbsOfferingID.getLength(); i++) {
                if (cbsOfferingID.item(i).getTextContent().equals(offerCBS)) {
                    isActive = true;
                    break;
                }
            }
        } catch (SAXException | IOException ex) {
            System.out.println("Error CBS Response: " + ex.getMessage());
        }
        return isActive;
    }

    public String setURNRequest() {
        String urnRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.tigo.com/SearchHomeCustomers/V1\">\n"
                + "   <soapenv:Header/>\n"
                + "   <soapenv:Body>\n"
                + "      <v1:authorizationMultipleURNRequest>\n"
                + "         <subscriberId></subscriberId>\n"
                + "         <countryCode>SV</countryCode>\n"
                + "         <actionId>VIEW</actionId>\n"
                + "         <homeId>SV-" + value + "</homeId>\n"
                + "         <ipAddress></ipAddress>\n"
                + "         <resources>\n"
                + "            <resourceId>urn:tve:" + urn + "</resourceId>\n"
                + "         </resources>\n"
                + "      </v1:authorizationMultipleURNRequest>\n"
                + "   </soapenv:Body>\n"
                + "</soapenv:Envelope>";

        return urnRequest;
    }

    public boolean validateURNResponse() {
        boolean hasAccess = false;
        try {
            Document responseXML = builder.parse(new ByteArrayInputStream(response.getBytes()));
            cbsOfferingID = responseXML.getElementsByTagName("access");
            if (cbsOfferingID.item(0).getTextContent().equals("true")) {
                hasAccess = true;
            }
        } catch (SAXException | IOException ex) {
            System.out.println("Error URN Response: " + ex.getMessage());
        }
        return hasAccess;
    }

    public void espera() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HomeApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
