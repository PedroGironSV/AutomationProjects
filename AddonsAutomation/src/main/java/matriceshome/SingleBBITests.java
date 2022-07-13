package matriceshome;

import addons.home.HomeApi;
import generate.report.Reporte;
import java.util.ArrayList;

/**
 *
 * @author pagiron
 */
public class SingleBBITests {

    HomeApi api = new HomeApi();
    Reporte rep = new Reporte();
    public String message, filePath, datos;
    private int sheetIndex = 3, fila;
    ArrayList<String> nameSEvs, nameCEvs;

    //Casos de prueba Matriz Bundle
    public void casoPrueba01() {
        nameSEvs = new ArrayList<>();
        nameSEvs.add("avRequest");
        nameSEvs.add("avResponse");
        boolean isSuccessful = true;
        fila = 3;
        String cp = "CP001";
        //Leer datos de la Matriz
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            //Cargar catalogo y validar la respuesta del Web Service
            api.showAvailableAddons = true;
            message = api.getResponseMessage(nameSEvs.get(0), nameSEvs.get(1));
            if (message.equals("Ejecución exitosa")) {
                for (int i = 0; i < api.offerRefs.getLength(); i++) {
                    if (api.offerRefs.item(i).getTextContent().equals(api.offerID)) {
                        isSuccessful = false;
                        message = "La promoción " + api.offerID + " se muestra disponible.";
                        break;
                    } else {
                        isSuccessful = true;
                    }
                }
            } else {
                isSuccessful = false;
            }
            if (isSuccessful) {
                rep.addEvidence(nameSEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
               if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameSEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameSEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }
    }

    public void casoPrueba02() {
        nameSEvs = new ArrayList<>();
        nameSEvs.add("avRequest");
        nameSEvs.add("avResponse");
        boolean isSuccessful = true;
        String cp = "CP002";
        fila = 4;
        //Leer datos de la Matriz
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            //Cargar catalogo y validar la respuesta del Web Service
            api.showAvailableAddons = true;
            message = api.getResponseMessage(nameSEvs.get(0), nameSEvs.get(1));
            if (message.equals("Ejecución exitosa")) {
                for (int i = 0; i < api.offerRefs.getLength(); i++) {
                    if (api.offerRefs.item(i).getTextContent().equals(api.offerID)) {
                        isSuccessful = true;
                        break;
                    } else {
                        isSuccessful = false;
                        message = "La promoción " + api.offerID + " no se muestra disponible.";
                    }
                }
            } else {
                isSuccessful = false;
            }
            if (isSuccessful) {
                rep.addEvidence(nameSEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
                if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameSEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameSEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }

    }

    public void casoPrueba03() {
        nameSEvs = new ArrayList<>();
        nameSEvs.add("avRequest");
        nameSEvs.add("avResponse");
        boolean isSuccessful = true;
        String cp = "CP003";
        fila = 5;
        //Leer datos de la Matriz
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            //Cargar catalogo y validar la respuesta del Web Service
            api.showAvailableAddons = true;
            message = api.getResponseMessage(nameSEvs.get(0), nameSEvs.get(1));
            if (message.equals("Ejecución exitosa")) {
                for (int i = 0; i < api.offerRefs.getLength(); i++) {
                    if (api.offerRefs.item(i).getTextContent().equals(api.offerID)) {
                        isSuccessful = true;
                        break;
                    } else {
                        isSuccessful = false;
                        message = "La oferta " + api.offerID + " no se muestra disponible.";
                    }
                }
            } else {
                isSuccessful = false;
            }
            if (isSuccessful) {
                rep.addEvidence(nameSEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
                if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameSEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameSEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }

    }

    public void casoPrueba04() {
        nameCEvs = new ArrayList<>();
        nameCEvs.add("meRequest");
        nameCEvs.add("meResponse");
        nameCEvs.add("acRequest");
        nameCEvs.add("acResponse");
        nameCEvs.add("cbRequest");
        nameCEvs.add("cbResonse");
        boolean isSuccessful = true;
        String cp = "CP004";
        fila = 6;
        //Leer datos de la Matriz
        api.menageAddons = true;
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            //Activar Addon y validar la respuesta del Web Service
            message = api.activateOffers(nameCEvs.get(0), nameCEvs.get(1), "add");
            if (message.equals("Ejecución exitosa")) {
                //Mostrar Addons Activos
                api.showActiveAddons = true;
                api.setMatrixData(filePath, sheetIndex, fila);
                api.runApi(nameCEvs.get(2), nameCEvs.get(3));
                //Validar que está activo en CBS
                api.cbsValidation = true;
                api.espera(); //Espera 1 segundo antes de validar en CBS
                api.runApi(nameCEvs.get(4), nameCEvs.get(5));
                isSuccessful = api.validateCBSResponse();
                if (isSuccessful == false) {
                    message = "La oferta código " + api.offerID + " no se aprovisionó en CBS";
                }
            } else {
                isSuccessful = false;
                message = "La promoción " + api.offerID + " no se activó correctamente";
            }
            if (isSuccessful) {
                rep.addEvidence(nameCEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
               if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameCEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameCEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }

    }

    public void casoPrueba05() {
        nameCEvs = new ArrayList<>();
        nameCEvs.add("meRequest");
        nameCEvs.add("meResponse");
        nameCEvs.add("acRequest");
        nameCEvs.add("acResponse");
        nameCEvs.add("cbRequest");
        nameCEvs.add("cbResonse");
        boolean isSuccessful = true;
        String cp = "CP005";
        fila = 7;
        //Leer datos de la Matriz
        api.menageAddons = true;
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            //Activar Addon y validar la respuesta del Web Service
            message = api.activateOffers(nameCEvs.get(0), nameCEvs.get(1), "add");
            if (message.equals("Ejecución exitosa")) {
                //Mostrar Addons Activos
                api.showActiveAddons = true;
                api.setMatrixData(filePath, sheetIndex, fila);
                api.runApi(nameCEvs.get(2), nameCEvs.get(3));
                //Validar que está activo en CBS
                api.cbsValidation = true;
                api.espera(); //Espera 1 segundo antes de validar en CBS
                api.runApi(nameCEvs.get(4), nameCEvs.get(5));
                isSuccessful = api.validateCBSResponse();
                if (isSuccessful == false) {
                    message = "La oferta código " + api.offerID + " no se aprovisionó en CBS";
                }
            } else {
                isSuccessful = false;
                message = "La promoción " + api.offerID + " no se activó correctamente";
            }
            if (isSuccessful) {
                rep.addEvidence(nameCEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
                if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameCEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameCEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }

    }

    public void casoPrueba06() {
        nameSEvs = new ArrayList<>();
        nameSEvs.add("urRequest");
        nameSEvs.add("urResponse");
        boolean isSuccessful = true;
        String cp = "CP006";
        fila = 8;
        //Leer datos de la Matriz
        api.urnValidation = true;
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            api.runApi(nameSEvs.get(0), nameSEvs.get(1));
            isSuccessful = api.validateURNResponse();
            if (!isSuccessful) {
                message = "No cuenta con acceso a la promoción " + api.urn.toUpperCase();
            }
            if (isSuccessful) {
                rep.addEvidence(nameSEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
                if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameSEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameSEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }

    }

    public void casoPrueba07() {
        nameCEvs = new ArrayList<>();
        nameCEvs.add("meRequest");
        nameCEvs.add("meResponse");
        nameCEvs.add("acRequest");
        nameCEvs.add("acResponse");
        nameCEvs.add("cbRequest");
        nameCEvs.add("cbResonse");
        boolean isSuccessful = true;
        String cp = "CP007";
        fila = 9;
        //Leer datos de la Matriz
        api.menageAddons = true;
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            //Activar Addon y validar la respuesta del Web Service
            message = api.activateOffers(nameCEvs.get(0), nameCEvs.get(1), "delete");
            if (message.equals("Ejecución exitosa") || message.equals("No se puede dar de baja a servicios menores a 30 dias de su activacion.")) {
                //Mostrar Addons Activos
                api.showActiveAddons = true;
                api.setMatrixData(filePath, sheetIndex, fila);
                api.getResponseMessage(nameCEvs.get(2), nameCEvs.get(3));
                //Validar que está activo en CBS
                api.cbsValidation = true;
                api.espera(); //Espera 1 segundo antes de validar en CBS
                api.runApi(nameCEvs.get(4), nameCEvs.get(5));
                isSuccessful = api.validateCBSResponse();
                if (isSuccessful == false) {
                    message = "La oferta código " + api.offerID + " se eliminó antes de cumplir con los 30 días";
                }
            }
            if (isSuccessful) {
                rep.addEvidence(nameCEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
                if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameCEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameCEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }
    }

    public void casoPrueba08() {
        nameCEvs = new ArrayList<>();
        nameCEvs.add("meRequest");
        nameCEvs.add("meResponse");
        nameCEvs.add("acRequest");
        nameCEvs.add("acResponse");
        nameCEvs.add("cbRequest");
        nameCEvs.add("cbResonse");
        boolean isSuccessful = true;
        String cp = "CP008";
        fila = 10;
        //Leer datos de la Matriz
        api.menageAddons = true;
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            //Activar Addon y validar la respuesta del Web Service
            message = api.activateOffers(nameCEvs.get(0), nameCEvs.get(1), "delete");
            if (message.equals("Ejecución exitosa") || message.equals("No se puede dar de baja a servicios menores a 30 dias de su activacion.")) {
                //Mostrar Addons Activos
                api.showActiveAddons = true;
                api.setMatrixData(filePath, sheetIndex, fila);
                api.getResponseMessage(nameCEvs.get(2), nameCEvs.get(3));
                //Validar que está activo en CBS
                api.cbsValidation = true;
                api.espera(); //Espera 1 segundo antes de validar en CBS
                api.runApi(nameCEvs.get(4), nameCEvs.get(5));
                isSuccessful = api.validateCBSResponse();
                if (isSuccessful == false) {
                    isSuccessful = true;
                } else {
                    isSuccessful = false;
                    message = "La oferta código " + api.offerID + " no pudo ser eliminada.";
                }
            }
            if (isSuccessful) {
                rep.addEvidence(nameCEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
                if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameCEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameCEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }

    }

    public void casoPrueba09() {
        nameCEvs = new ArrayList<>();
        nameCEvs.add("meRequest");
        nameCEvs.add("meResponse");
        nameCEvs.add("acRequest");
        nameCEvs.add("acResponse");
        nameCEvs.add("cbRequest");
        nameCEvs.add("cbResonse");
        boolean isSuccessful = true;
        String cp = "CP009";
        fila = 11;
        //Leer datos de la Matriz
        api.menageAddons = true;
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            //Activar Addon y validar la respuesta del Web Service
            message = api.activateOffers(nameCEvs.get(0), nameCEvs.get(1), "delete");
            if (message.equals("Ejecución exitosa") || message.equals("No se puede dar de baja a servicios menores a 30 dias de su activacion.")) {
                //Mostrar Addons Activos
                api.showActiveAddons = true;
                api.setMatrixData(filePath, sheetIndex, fila);
                api.getResponseMessage(nameCEvs.get(2), nameCEvs.get(3));
                //Validar que está activo en CBS
                api.cbsValidation = true;
                api.espera(); //Espera 1 segundo antes de validar en CBS
                api.runApi(nameCEvs.get(4), nameCEvs.get(5));
                isSuccessful = api.validateCBSResponse();
                if (isSuccessful == false) {
                    message = "La oferta código " + api.offerID + " se eliminó antes de cumplir con los 30 días";
                }
            }
            if (isSuccessful) {
                rep.addEvidence(nameCEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
                if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameCEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameCEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }
    }

    public void casoPrueba10() {
        nameCEvs = new ArrayList<>();
        nameCEvs.add("meRequest");
        nameCEvs.add("meResponse");
        nameCEvs.add("acRequest");
        nameCEvs.add("acResponse");
        nameCEvs.add("cbRequest");
        nameCEvs.add("cbResonse");
        boolean isSuccessful = true;
        String cp = "CP010";
        fila = 12;
        //Leer datos de la Matriz
        api.menageAddons = true;
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            //Activar Addon y validar la respuesta del Web Service
            message = api.activateOffers(nameCEvs.get(0), nameCEvs.get(1), "delete");
            if (message.equals("Ejecución exitosa") || message.equals("No se puede dar de baja a servicios menores a 30 dias de su activacion.")) {
                //Mostrar Addons Activos
                api.showActiveAddons = true;
                api.setMatrixData(filePath, sheetIndex, fila);
                api.getResponseMessage(nameCEvs.get(2), nameCEvs.get(3));
                //Validar que está activo en CBS
                api.cbsValidation = true;
                api.espera(); //Espera 1 segundo antes de validar en CBS
                api.runApi(nameCEvs.get(4), nameCEvs.get(5));
                isSuccessful = api.validateCBSResponse();
                if (isSuccessful == false) {
                    isSuccessful = true;
                } else {
                    isSuccessful = false;
                    message = "La oferta código " + api.offerID + " no pudo ser eliminada.";
                }
            }
            if (isSuccessful) {
                rep.addEvidence(nameCEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
                if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameCEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameCEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }
    }

    public void casoPrueba11() {
        nameSEvs = new ArrayList<>();
        nameSEvs.add("urRequest");
        nameSEvs.add("urResponse");
        boolean isSuccessful = true;
        String cp = "CP011";
        fila = 13;
        //Leer datos de la Matriz
        api.urnValidation = true;
        datos = api.setMatrixData(filePath, sheetIndex, fila);
        if (!datos.isEmpty()) {
            api.runApi(nameSEvs.get(0), nameSEvs.get(1));
            isSuccessful = api.validateURNResponse();
            if (isSuccessful == false) {
                isSuccessful = true;
            } else {
                isSuccessful = false;
                message = "Se mantiene el acceso a la promoción " + api.urn.toUpperCase();
            }
            if (isSuccessful) {
                rep.addEvidence(nameSEvs, cp, sheetIndex, fila, getMergeRegIndex(fila));
            } else {
                if (rep.isFailedRevalidation(filePath, cp) == false) {
                    int reqRow = rep.addIssueLog(filePath, message, cp, sheetIndex);
                    rep.addIssuesEvidence(filePath, nameSEvs, reqRow);
                } else {
                    rep.deleteScreenShots(nameSEvs);
                }
            }
            rep.modifyWorkbook(filePath, isSuccessful, sheetIndex, fila, message);
        }
    }

    public void runSingleTestCases() {
        casoPrueba01();
        casoPrueba02();
        casoPrueba03();
        casoPrueba04();
        casoPrueba05();
        casoPrueba06();
        casoPrueba07();
        casoPrueba08();
        casoPrueba09();
        casoPrueba10();
        casoPrueba11();
    }

    public int getMergeRegIndex(int fila) {
        int mergeRegIndex = 0;
        //Indicar indice de celdas combinadas
        if (fila >= 3 && fila <= 6) {
            mergeRegIndex = 0;
        } else if (fila > 6) {
            mergeRegIndex = -1;
        }
        return mergeRegIndex;
    }
}
