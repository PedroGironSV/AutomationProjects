package matrices;

import com.tigosv.cargoxinstalacion.Request;
import com.tigosv.cargoxinstalacion.WebService;
import generate.report.Reporte;
import java.util.ArrayList;

/**
 *
 * @author pagiron
 */
public class EscenariosValoresAPI {

    WebService ws = new WebService();
    Reporte repo = new Reporte();
    Request req = new Request();
    public int sheetIndex = 2, fila;
    public String datos, observaciones, request, response, baseUri, post, filePath;
    private ArrayList<String> nameEvs;
    private ArrayList<Float> prices;

    public void setValidations() {
        //Colocar los precios
        prices = new ArrayList<>();
        //Precios a validar por cada caso de prueba
        prices.add(10f); //CP001 --> Price: 10
        prices.add(3f);  //CP002 --> Price: 3
        prices.add(0f);  //CP003 --> Price: 0
        prices.add(5f);  //CP004 --> Price: 5
        prices.add(10f); //CP005 --> Price: 10
        prices.add(10f); //CP006 --> Price: 10
        prices.add(5f);  //CP007 --> Price: 5
        prices.add(6f);  //CP008 --> Price: 6
        prices.add(7f);  //CP009 --> Price: 7
        prices.add(10f); //CP010 --> Price: 10
        prices.add(0f);  //CP011 --> Price: 0
        prices.add(10f); //CP012 --> Price: 10
        prices.add(0f);  //CP013 --> Price: 0
        prices.add(0f);  //CP014 --> Price: 0
        prices.add(5f);  //CP015 --> Price: 5
        prices.add(10f); //CP016 --> Price: 10
        prices.add(0f);  //CP017 --> Price: 0
        prices.add(0f);  //CP018 --> Price: 0
        prices.add(10f); //CP019 --> Price: 10
        prices.add(10f); //CP020 --> Price: 10
        prices.add(10f); //CP021 --> Price: 10
        prices.add(10f); //CP022 --> Price: 10
        prices.add(10f); //CP023 --> Price: 10
        prices.add(20f); //CP024 --> Price: 20
        prices.add(10f); //CP025 --> Price: 10
        prices.add(10f); //CP026 --> Price: 10
        prices.add(10f); //CP027 --> Price: 10
        prices.add(10f); //CP028 --> Price: 10
    }

    public void runEscenariosValores() {
        setValidations();
        //Indicar cu??ntos casos de prueba se ejecutar??n en total
        int totalCasosPrueba = 28;
        //Colocar el nombre de archivo de las evidencias
        nameEvs = new ArrayList<>();
        nameEvs.add("cxiRequest");
        nameEvs.add("cxiResponse");
        nameEvs.add("cpRequest");
        nameEvs.add("cpResponse");
        fila = 1;
        String cp = "";
        for (int i = 0; i < totalCasosPrueba; i++) {
            boolean isSuccessful = false;
            //Colocar el formato adecuado en cada caso de prueba
            if (fila <= 9) {
                cp = "CP00" + fila;
            } else {
                cp = "CP0" + fila;
            }
            setDatos(req.setTestData(sheetIndex, fila));
            if (!getDatos().isEmpty()) {
                //Correr el Web Service queryInstallationChargesService
                setRequest(req.getInstallationChargesRequest());
                setBaseUri(req.getBaseUri());
                setPost(req.getPost());
                ws.setPostResponse(getRequest(), getBaseUri(), getPost(), nameEvs.get(0), nameEvs.get(1));
                //Verificar que se cumplan las validaciones establecidas en la Matriz
                setObservaciones(ws.validateCxIResponse("00", prices.get(i), false, "Operacion aprobada"));
                //Correr el Web Service consultaPromocionalCXIService
                setRequest(req.getConsultaPromocionalCXIRequest());
                ws.setPostResponse(getRequest(), getBaseUri(), getPost(), nameEvs.get(2), nameEvs.get(3));
                //Verificar que se cumplan las validaciones establecidas en la Matriz
                //setObservaciones(ws.validateCPResponse(getObservaciones()));
                if (getObservaciones().isEmpty()) {
                    isSuccessful = true;
                    repo.addEvidence(nameEvs, cp, sheetIndex, fila, 0);
                } else {
                    if (repo.isFailedRevalidation(filePath, cp) == false) {
                        int reqRow = repo.addIssueLog(filePath, getObservaciones(), cp, sheetIndex);
                        repo.addIssuesEvidence(filePath, nameEvs, reqRow);
                    }else{
                        repo.deleteScreenShots(nameEvs);
                    }
                }
                repo.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, getObservaciones());

            }
            fila++;
        }
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
