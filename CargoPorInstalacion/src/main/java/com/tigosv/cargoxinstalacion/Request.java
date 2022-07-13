package com.tigosv.cargoxinstalacion;

import generate.report.Reporte;
import java.util.ArrayList;

/**
 *
 * @author pagiron
 */
public class Request {

    private Reporte repo = new Reporte();

    private String productType, months, node, user, documentType, documentNumber, baseUri, post;
    private ArrayList<String> datos;
    private String casoUso, casoPrueba, sheetName;

    //Colocar los datos encontrados en la Matriz de Pruebas
    public String setTestData(int sheetIndex, int fila) {
        String datosPrueba = "";
        datos = new ArrayList<>();
        //Cargar y filtrar datos de prueba de la Matriz seleccionada
        datosPrueba = repo.readWorkbook(sheetIndex, fila);
        if (!datosPrueba.isEmpty()) {
            datos = repo.getTestData(datosPrueba);

            setProductType(datos.get(0));
            setMonths(datos.get(1));
            setNode(datos.get(2));
            setUser(datos.get(3));
            setDocumentType(datos.get(4));
            setDocumentNumber(datos.get(5));
        }
        return datosPrueba;
    }

    //Request del Web Service: queryInstallationChargesService
    public String getInstallationChargesRequest() {
        if (HomeScreen.environment.equals("UAT")) {
            setBaseUri("https://qa.api.tigo.com");
            setPost("sv_test/soa-infra/services/ea-uat/esb-core/ServiceConsumer");
        } else if (HomeScreen.environment.equals("PRD")) {
            setBaseUri("N/A");
            setPost("N/A");
        }

        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://consumer.esb.ea.tigo.com\">\n"
                + "   <soapenv:Header/>\n"
                + "   <soapenv:Body>\n"
                + "      <con:ServiceRequest>\n"
                + "         <con:serviceId>queryInstallationChargesService</con:serviceId>\n"
                + "         <con:accessToken>123</con:accessToken>\n"
                + "         <con:country>SV</con:country>\n"
                + "         <con:applicationRefId>actdb-provider</con:applicationRefId>\n"
                + "         <con:requestDate>${=new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSS\").format(new Date())}</con:requestDate>\n"
                + "         <con:dataType>json-pretty</con:dataType>\n"
                + "         <con:data>{\n"
                + "			\"offerType\": \"HOME\",\n"
                + "			\"productType\": \"" + getProductType() + "\",\n"
                + "			\"monthsContract\": \"" + getMonths() + "\",\n"
                + "			\"node\": \"" + getNode() + "\",\n"
                + "			\"user\": \"" + getUser() + "\",\n"
                + "			\"documentType\": \"" + getDocumentType() + "\",\n"
                + "			\"documentNumber\": \"" + getDocumentNumber() + "\"\n"
                + "		}</con:data>\n"
                + "         <con:environmentId>development</con:environmentId>\n"
                + "      </con:ServiceRequest>\n"
                + "   </soapenv:Body>\n"
                + "</soapenv:Envelope>";
        return request;
    }

    //Request del Web Service: consultaPromocionalCXIService
    public String getConsultaPromocionalCXIRequest() {
        if (HomeScreen.environment.equals("UAT")) {
            setBaseUri("https://qa.api.tigo.com");
            setPost("sv_test/soa-infra/services/ea-uat/esb-core/ServiceConsumer");
        } else if (HomeScreen.environment.equals("PRD")) {
            setBaseUri("N/A");
            setPost("N/A");
        }
        String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://consumer.esb.ea.tigo.com\">\n"
                + "   <soapenv:Header/>\n"
                + "   <soapenv:Body>\n"
                + "      <con:ServiceRequest>\n"
                + "         <con:serviceId>consultaPromocionalCXIService</con:serviceId>\n"
                + "         <con:accessToken>123</con:accessToken>\n"
                + "         <con:country>SV</con:country>\n"
                + "         <con:applicationRefId>actdb-provider</con:applicationRefId>\n"
                + "         <con:requestDate>${=new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss.SSS\").format(new Date())}</con:requestDate>\n"
                + "         <con:dataType>json-pretty</con:dataType>\n"
                + "         <con:data>{\n"
                + "		  \"salesPersonCode\": \"" + getUser() + "\"\n"
                + "		}</con:data>\n"
                + "         <con:environmentId>development</con:environmentId>\n"
                + "      </con:ServiceRequest>\n"
                + "   </soapenv:Body>\n"
                + "</soapenv:Envelope>";
        return request;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
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

    public Reporte getRepo() {
        return repo;
    }

    public void setRepo(Reporte repo) {
        this.repo = repo;
    }

    public String getCasoUso() {
        return casoUso;
    }

    public void setCasoUso(String casoUso) {
        this.casoUso = casoUso;
    }

    public String getCasoPrueba() {
        return casoPrueba;
    }

    public void setCasoPrueba(String casoPrueba) {
        this.casoPrueba = casoPrueba;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
