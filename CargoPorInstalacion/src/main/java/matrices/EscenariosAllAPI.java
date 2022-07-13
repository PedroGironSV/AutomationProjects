package matrices;

import com.tigosv.cargoxinstalacion.Request;
import com.tigosv.cargoxinstalacion.WebService;
import generate.report.Reporte;
import java.util.ArrayList;

/**
 *
 * @author pagiron
 */
public class EscenariosAllAPI {

    WebService ws = new WebService();
    Reporte repo = new Reporte();
    Request req = new Request();
    public int sheetIndex = 3, fila;
    public String datos, observaciones, request, response, baseUri, post, filePath;
    private ArrayList<String> nameEvs;
    private ArrayList<Float> prices;
    
    public int getMergeRegIndex(int fila){
        int mergeRegIndex = 0;
        //Indicar indice de celdas combinadas
            if(fila >= 1 && fila <= 2){
                mergeRegIndex = 0;
            }else{
                if(fila >= 3 && fila <= 7){
                    mergeRegIndex = 1;
                }else{
                    if(fila >= 8 && fila <= 12){
                        mergeRegIndex = 2;
                    }else {
                        if(fila >= 13 && fila <= 17){
                            mergeRegIndex = 3;
                        }
                    }
                }
            }
        return mergeRegIndex;
    }

    public void runEscenariosAll() {
        //Indicar cuántos casos de prueba se ejecutarán en total
        int totalCasosPrueba = 17;
        //Colocar el nombre de archivo de las evidencias
        nameEvs = new ArrayList<>();
        nameEvs.add("cxiRequest");
        nameEvs.add("cxiResponse");
        nameEvs.add("cpRequest");
        nameEvs.add("cpResponse");
        //Colocar los precios 
        prices = new ArrayList<>();
        //Precios a validar
        prices.add(10f); //CP001 - CP007 --> Price: 10
        prices.add(20f); //CP008 - C0017 --> Price: 20
        fila = 1;
        float validationPrice = 0;
        String cp = "";
        for (int i = 0; i < totalCasosPrueba; i++) {
            boolean isSuccessful = false;
            //Validar que rango de price se espera
            if(fila <= 7){
                validationPrice = prices.get(0);
            }else{
                validationPrice = prices.get(1);
            }
            //Colocar el formato adecuado en cada caso de prueba
            if(fila <= 9){
                cp = "CP00"+ fila;
            }else{
                cp = "CP0"+ fila;
            }
            
            setDatos(req.setTestData(sheetIndex, fila));
            if (!getDatos().isEmpty()) {
                //Correr el Web Service queryInstallationChargesService
                setRequest(req.getInstallationChargesRequest());
                setBaseUri(req.getBaseUri());
                setPost(req.getPost());
                ws.setPostResponse(getRequest(), getBaseUri(), getPost(), nameEvs.get(0), nameEvs.get(1));
                //Verificar que se cumplan las validaciones establecidas en la Matriz
                setObservaciones(ws.validateCxIResponse("00", validationPrice, false, "Operacion aprobada"));
                //Correr el Web Service consultaPromocionalCXIService
                setRequest(req.getConsultaPromocionalCXIRequest());
                ws.setPostResponse(getRequest(), getBaseUri(), getPost(), nameEvs.get(2), nameEvs.get(3));
                //Verificar que se cumplan las validaciones establecidas en la Matriz
                //setObservaciones(ws.validateCPResponse(getObservaciones()));
                if (getObservaciones().isEmpty()) {
                    isSuccessful = true;
                    repo.addEvidence(nameEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
                } else {
                    if(repo.isFailedRevalidation(filePath, cp) == false){
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

}
