package generate.report;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.TextRenderData;
import com.tigosv.cargoxinstalacion.HomeScreen;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/**
 *
 * @author pagiron
 */
public class Reporte {

    public Cell col;
    public Row row;
    public SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
    public String filePath, casoUso = "", casoPrueba = "", currentCasoUso, currentCasoPrueba, sheetName;
    public ArrayList<String> fullDatos, datosFiltrados;
    public CellRangeAddress region;

    public String readWorkbook(int sheetIndex, int fila) {
        filePath = HomeScreen.filePath;
        String datosPrueba = "";
        try {
            //Ruta de la matriz
            FileInputStream file = new FileInputStream(filePath);
            //Objeto para manejar el Libro de Excel
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Indicar el nombre de la Hoja
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            sheetName = sheet.getSheetName();
            row = sheet.getRow(fila);
            col = row.getCell(9);
            String tempStatus = col.getStringCellValue().trim();
            if (tempStatus.equals("PENDIENTE") || tempStatus.equals("OBSERVADO")) {

                col = row.getCell(1);
                //Extraer caso de prueba
                if (col.getStringCellValue().trim().isEmpty() == false) {
                    casoPrueba = col.getStringCellValue().trim();
                    col = row.getCell(3);
                    casoPrueba += ": " + col.getStringCellValue().trim();
                } else {
                    casoPrueba = "";
                }

                if (casoPrueba.trim().isEmpty() == false) {
                    //Extraer caso de uso
                    col = row.getCell(2);
                    if (!col.getStringCellValue().equals("")) {
                        casoUso = col.getStringCellValue().trim();
                    }

                    //Extraer datos de prueba
                    col = row.getCell(7);
                    if (!col.getStringCellValue().equals("")) {
                        datosPrueba = col.getStringCellValue().trim();
                    }
                } else {
                    casoUso = "";
                    datosPrueba = "";
                }
                //Cerrar el archivo
                file.close();
            }
        } catch (IOException e) {
            System.out.println("\nError en readWorkbook: " + e);
            casoPrueba = "";
            casoUso = "";
            datosPrueba = "";
        }
        return datosPrueba;
    }

    public ArrayList<String> getTestData(String testData) {
        fullDatos = new ArrayList<>();
        datosFiltrados = new ArrayList<>();
        int index = testData.split("[\n]").length;
        for (int i = 0; i < index; i++) {
            fullDatos.add(testData.split("[\n]")[i]);
        }

        for (int i = 0; i < fullDatos.size(); i++) {
            //Obtener el valor de cada Key
            int indice = fullDatos.get(i).lastIndexOf(":");
            if (indice > 0 && indice < (fullDatos.get(i).length() - 1)) {
                datosFiltrados.add(fullDatos.get(i).substring(indice + 1).trim());
            }
        }
        return datosFiltrados;
    }

    public void modifyWorkbook(String filePath, boolean estado, int sheetIndex, int fila, String message) {
        String oldStatus, finalMessage = message, finalQA = HomeScreen.nameQA;
        int ejecuciones = 1;
        try {

            //Ruta de la matriz
            FileInputStream file = new FileInputStream(filePath);
            //Objeto para manejar el Libro de Excel
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Indicar el nombre de la Hoja
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            sheetName = sheet.getSheetName();
            //Ciclo para recorrer las celdas hacía abajo (por filas) y guardar el texto en el ArrayList
            //fila++;
            row = sheet.getRow(fila);
            //Columna indice 9 corresponde a los Estados de cada caso en la matriz
            col = row.getCell(9);
            oldStatus = col.getStringCellValue().trim();
            //Modificar estado de cada caso de prueba
            if (estado) {
                col.setCellValue("EXITOSO");
            } else {

                col.setCellValue("OBSERVADO");
                col = row.getCell(10); //Observación
                if (oldStatus.equals("OBSERVADO")) {
                    finalMessage = col.getStringCellValue();
                    finalMessage += "\n" + message;
                }
                col.setCellValue(finalMessage);
            }

            if (finalQA.isEmpty()) {
                HomeScreen.nameQA = "Digital Automation Software";
            }

            //Completar los datos de la Matriz
            col = row.getCell(8); //Nombre del Tester
            if (oldStatus.equals("OBSERVADO")) {
                finalQA = col.getStringCellValue().trim();
                finalQA += "\n" + HomeScreen.nameQA;
            }
            col.setCellValue(finalQA);
            col = row.getCell(11); //Ejecuciones
            //Validar las ejecuciones realizadas al momento
            if (oldStatus.equals("OBSERVADO")) {
                ejecuciones = (int) col.getNumericCellValue();
                ejecuciones++;
            }
            col.setCellValue(ejecuciones);
            col = row.getCell(12); //Fecha/Tester (Ejecucion)
            String datos = col.getStringCellValue();
            if (ejecuciones > 1) {
                datos += "\n" + fecha.format(new Date()) + " " + HomeScreen.nameQA + " (" + ejecuciones + ")";
            } else {
                datos = fecha.format(new Date()) + " " + HomeScreen.nameQA + " (" + ejecuciones + ")";
            }
            col.setCellValue(datos);
            //Cerrar el archivo
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            file.close();
            out.close();
        } catch (IOException e) {
            System.out.println("\nError en modifyWorkbook: " + e);
        }
    }

    public int addIssueLog(String filePath, String observaciones, String cPrueba, int currentMatrixSheetIndex) {
        FileInputStream file;
        int issueLogIndex = 11, filaIssueLog = 18, reqRow = 2;
        String hojaRef, arquitecto;
        try {
            //Ruta de la matriz
            file = new FileInputStream(filePath);
            //Objeto para manejar el Libro de Excel
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Indicar el valor de la Hoja
            XSSFSheet sheet = workbook.getSheetAt(issueLogIndex);
            //Ciclo para encontrar una fila libre en el Issue Log y agregar los datos
            boolean isEmpty;
            do {
                isEmpty = false;
                //Colocar valores correspondientes al inicio de la tabla IssueLog: B19
                row = sheet.getRow(filaIssueLog);
                col = row.getCell(1);
                if (col.getStringCellValue().trim().isEmpty()) {
                    isEmpty = true;
                } else {
                    filaIssueLog++;
                    reqRow += 50;
                }
            } while (isEmpty == false);
            //Colocar datos de la Observación en el Issue Log
            col.setCellValue(cPrueba);

            //Obtener hoja de referencia Matriz
            sheet = workbook.getSheetAt(currentMatrixSheetIndex);
            hojaRef = sheet.getSheetName();
            col = row.getCell(2);
            col.setCellValue(hojaRef);

            //Asignar nuevamente a la hoja de IssueLog
            sheet = workbook.getSheetAt(issueLogIndex);
            row = sheet.getRow(filaIssueLog);

            //Colocar la fecha de escalamiento
            col = row.getCell(3);
            col.setCellValue(fecha.format(new Date()));

            //Colocar nombre del tester
            if (HomeScreen.nameQA.isEmpty()) {
                HomeScreen.nameQA = "Digital Automation Software";
            }
            col = row.getCell(4);
            col.setCellValue(HomeScreen.nameQA);

            //Colocar Hypervinculo de la evidencia
            XSSFRow linkRow = sheet.getRow(filaIssueLog);
            XSSFCell linkCol = linkRow.getCell(5);
            CreationHelper createHelper = workbook.getCreationHelper();
            XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.DOCUMENT);
            //Obtener hoja de referencia Matriz
            sheet = workbook.getSheetAt(12);
            link.setAddress("'" + sheet.getSheetName() + "'!B" + reqRow);
            linkCol.setHyperlink((XSSFHyperlink) link);
            linkCol.setCellValue(cPrueba);

            //Asignar nuevamente a la hoja de IssueLog
            sheet = workbook.getSheetAt(issueLogIndex);
            row = sheet.getRow(filaIssueLog);

            //Colocar Tester status
            col = row.getCell(8);
            col.setCellValue("OPEN");

            //Colocar descripcion Issue
            col = row.getCell(9);
            col.setCellValue(observaciones);

            //Colocar Arquitecto Responsable
            sheet = workbook.getSheetAt(1); // Hoja "Status"
            row = sheet.getRow(7); // Celda: "Responsable de Soporte"
            col = row.getCell(4);
            if (col.getStringCellValue().trim().isEmpty()) {
                arquitecto = "Asignación pendiente";
            } else {
                arquitecto = col.getStringCellValue();
            }
            //Asignar nuevamente a la hoja de IssueLog
            sheet = workbook.getSheetAt(issueLogIndex);
            row = sheet.getRow(filaIssueLog);
            col = row.getCell(10);
            col.setCellValue(arquitecto);

            //Colocar estatus support
            col = row.getCell(14);
            col.setCellValue("OPEN");

            //addIssuesEvidence(filePath);
            //Cerrar el archivo
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            file.close();
            out.close();

        } catch (FileNotFoundException ex) {
            System.out.println("\nError en addIssueLog" + ex);
        } catch (IOException ex) {
            System.out.println("\nError en addIssueLog" + ex);
        }
        return reqRow;
    }

    public void deleteScreenShots(ArrayList<String> nameEvs) {
        File evsSS, evsXML;
        for (int i = 0; i < nameEvs.size(); i++) {
            evsXML = new File(System.getProperty("user.dir") + "\\files\\" + nameEvs.get(i) + ".xml");
            evsSS = new File(System.getProperty("user.dir") + "\\files\\" + nameEvs.get(i) + ".png");
            evsSS.delete();
            evsXML.delete();
        }
    }

    public void addIssuesEvidence(String filePath, ArrayList<String> nameEvs, int reqRow) {
        FileInputStream file;
        int issueEvidenceIndex = 12, reqCol = 1, resCol = 1;
        //Almacenar los nombres de las capturas de los WS
        ArrayList<InputStream> evidencias = new ArrayList<>();
        ArrayList<Integer> evsIndex = new ArrayList<>();
        //Almacenar imagenes
        ArrayList<Picture> picEvs = new ArrayList<>();
        //Ancor para colocar las propiedades de las evidencias
        XSSFClientAnchor anchor;
        try {
            for (int i = 0; i < nameEvs.size(); i++) {
                evidencias.add(new FileInputStream(System.getProperty("user.dir") + "\\files\\" + nameEvs.get(i) + ".png"));
            }
            //Ruta de la matriz
            file = new FileInputStream(filePath);
            //Objeto para manejar el Libro de Excel
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Indicar el indice de la Hoja
            XSSFSheet sheet = workbook.getSheetAt(issueEvidenceIndex);

            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

            for (int i = 0; i < evidencias.size(); i++) {
                anchor = new XSSFClientAnchor();
                evsIndex.add(workbook.addPicture(IOUtils.toByteArray(evidencias.get(i)), XSSFWorkbook.PICTURE_TYPE_PNG));
                evidencias.get(i).close();
                //Agregar las evidencias para un Web Service
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                anchor.setRow1(reqRow - 1);
                anchor.setCol1(resCol);
                picEvs.add(drawing.createPicture(anchor, evsIndex.get(i)));
                picEvs.get(i).resize(0.75, 0.75);
                resCol += 15; //Sumar 15 columnas antes de colocar la siguiente evidencia
            }

            //Cerrar el archivo y guardar los cambios
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            file.close();
            out.close();

            //Eliminar capturas de evidencias temporales
            deleteScreenShots(nameEvs);
        } catch (IOException ex) {
            System.out.println("\nError en addIssuesEvidence" + ex);
        }
    }

    public BufferedImage screenShot(String screenName) {
        // obtenemos el tamaño de la pantalla en pixeles
        Rectangle rectangleTam = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = null;
        String nombreFichero = "";
        Path path;
        try {
            path = Paths.get(System.getProperty("user.dir") + "\\files\\" + screenName + ".png");
            nombreFichero = path.toString();
            Robot robot = new Robot();
            // tomamos una captura de pantalla( screenshot )
            bufferedImage = robot.createScreenCapture(rectangleTam);
            FileOutputStream out = new FileOutputStream(nombreFichero);
            // esbribe la imagen a fichero
            ImageIO.write(bufferedImage, "png", out);

        } catch (AWTException | IOException e) {
            System.out.println("Error en screenShot: " + nombreFichero);
        }
        return bufferedImage;
    }
    
    public boolean isFailedRevalidation(String filePath, String cPrueba){
        FileInputStream file;
        boolean isTestCase = false;
        int issueLogIndex = 11, filaIssueLog = 18;
        try{
            //Ruta de la matriz
            file = new FileInputStream(filePath);
            //Objeto para manejar el Libro de Excel
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Indicar el valor de la Hoja
            XSSFSheet sheet = workbook.getSheetAt(issueLogIndex);
            //Ciclo para encontrar el Issue Log correspondiente y agregar los datos
            do {
                isTestCase = false;
                //Colocar valores correspondientes al inicio de la tabla IssueLog: B19
                row = sheet.getRow(filaIssueLog);
                col = row.getCell(1);
                if (col.getStringCellValue().trim().equals(cPrueba)) {
                    isTestCase = true;
                    break;
                }else{
                    filaIssueLog++;
                }
            } while (col.getStringCellValue().trim().isEmpty() == false);
            
            //Cerrar el archivo y guardar los cambios
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            file.close();
            out.close();
        }catch(IOException ex)
        {
            System.out.println("Error en failedRevalidation: " + ex);
        }
        return isTestCase;
    }

    public void successfulRevalidation(ArrayList<String> nameEvs, String filePath, String cPrueba) {
        FileInputStream file;
        int issueLogIndex = 11, filaIssueLog = 18, reqRow = 4002;
        try {
            //Ruta de la matriz
            file = new FileInputStream(filePath);
            //Objeto para manejar el Libro de Excel
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Indicar el valor de la Hoja
            XSSFSheet sheet = workbook.getSheetAt(issueLogIndex);
            //Ciclo para encontrar el Issue Log correspondiente y agregar los datos
            boolean isTestCase;
            do {
                isTestCase = false;
                //Colocar valores correspondientes al inicio de la tabla IssueLog: B19
                row = sheet.getRow(filaIssueLog);
                col = row.getCell(1);
                if (col.getStringCellValue().trim().equals(cPrueba)) {
                    isTestCase = true;
                } else {
                    filaIssueLog++;
                    reqRow += 50;
                }
            } while (isTestCase == false);

            //Colocar Tester que revalidó
            if (HomeScreen.nameQA.isEmpty()) {
                HomeScreen.nameQA = "Digital Automation Software";
            }
            col = row.getCell(6);
            col.setCellValue(HomeScreen.nameQA);

            //Colocar Hypervinculo de la evidencia
            XSSFRow linkRow = sheet.getRow(filaIssueLog);
            XSSFCell linkCol = linkRow.getCell(7);
            CreationHelper createHelper = workbook.getCreationHelper();
            XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.DOCUMENT);
            //Obtener hoja de referencia Matriz
            sheet = workbook.getSheetAt(12);
            link.setAddress("'" + sheet.getSheetName() + "'!B" + reqRow);
            linkCol.setHyperlink((XSSFHyperlink) link);
            linkCol.setCellValue(cPrueba);

            //Asignar nuevamente a la hoja de IssueLog
            sheet = workbook.getSheetAt(issueLogIndex);
            row = sheet.getRow(filaIssueLog);

            //Colocar Tester status
            col = row.getCell(8);
            col.setCellValue("CLOSED");

            //Colocar fecha resolución
            col = row.getCell(11);
            col.setCellValue(fecha.format(new Date()));

            //Colocar estatus support
            col = row.getCell(14);
            col.setCellValue("IN PROGRESS");
            
            //Cerrar el archivo y guardar los cambios
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            file.close();
            out.close();
            
            //Agregar las evidencias del caso exitoso
            addIssuesEvidence(filePath, nameEvs, reqRow);
        } catch (IOException ex) {
            System.out.println("\nError en successfulRevalidation: " + ex);
        }
    }

    public void addEvidence(ArrayList<String> nameEvs, String cPrueba, int sheetIndex, int fila, int mergeRegIndex) {
        String finalReport = "";
        boolean isRevalidation = false;
        final String title;
        Path fuente, destino;
        File home = FileSystemView.getFileSystemView().getHomeDirectory();

        try {
            //Ruta de la matriz
            filePath = HomeScreen.filePath;
            FileInputStream file = new FileInputStream(filePath);
            //Objeto para manejar el Libro de Excel
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            //Indicar el nombre de la Hoja
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            sheetName = sheet.getSheetName();
            //Verificar si es una revalidación 
            row = sheet.getRow(fila);
            col = row.getCell(9);
            String tempStatus = col.getStringCellValue().trim();
            if (tempStatus.equals("OBSERVADO")) {
                isRevalidation = true;
            }

            //Copiar la plantilla al escritorio con su nombre final
            fuente = Paths.get(System.getProperty("user.dir") + "\\files", "Plantilla.docx");
            if (isRevalidation) {
                finalReport = home.getAbsolutePath() + "\\Revalidación " + sheetName + ".docx";
            } else {
                finalReport = home.getAbsolutePath() + "\\Pruebas " + sheetName + ".docx";
            }
            destino = Paths.get(finalReport);
            if (!Files.exists(destino)) {
                Files.copy(fuente, destino);
            }
            // Template Loading and Rendering
            if (HomeScreen.title.isEmpty()) {
                title = "Pruebas - " + sheetName;
            } else {
                title = HomeScreen.title;
            }

            if (HomeScreen.tkt_RFC.isEmpty()) {
                HomeScreen.tkt_RFC = "N/A";
            }

            if (HomeScreen.nameQA.isEmpty()) {
                HomeScreen.nameQA = "Digital Automation Software";
            }
            XWPFTemplate template = XWPFTemplate.compile(finalReport).render(// data model
                    new LinkedHashMap<String, Object>() {
                {
                    //put("valor buscado", "Texto a colocar en su lugar");
                    put("title", new TextRenderData(title));
                    put("sub", new TextRenderData(HomeScreen.tkt_RFC));
                    put("author", new TextRenderData(HomeScreen.nameQA));
                    put("fInicio", fecha.format(new Date()));
                    put("fFin", fecha.format(new Date()));
                }
            });
            // Output Document
            template.writeAndClose(new FileOutputStream(finalReport));
            //Buscar Documento Word existente
            XWPFDocument document = new XWPFDocument(Files.newInputStream(Paths.get(finalReport)));
            //Agregar parrafo al documento
            XWPFParagraph paragraph = document.createParagraph();

            //Configurar parrafo o texto
            XWPFRun runCasoUso = paragraph.createRun();
            XWPFRun runCasoPrueba = paragraph.createRun();
            XWPFRun runText = paragraph.createRun();

            row = sheet.getRow(fila);
            col = row.getCell(1);

            //Extraer caso de prueba
            casoPrueba = col.getStringCellValue().trim();
            col = row.getCell(3);
            casoPrueba += ": " + col.getStringCellValue().trim();

            //Extraer caso de uso
            region = sheet.getMergedRegion(mergeRegIndex);
            row = sheet.getRow(region.getFirstRow());
            col = row.getCell(2);
            casoUso = col.getStringCellValue().trim();

            //Validar si es un nuevo Caso de Uso
            if (casoUso.equals(currentCasoUso) == false) {
                currentCasoUso = casoUso;
                runCasoUso.setBold(true);
                runCasoUso.setFontFamily("Calibri Light (Títulos)");
                runCasoUso.setFontSize(14);
                runCasoUso.setText(casoUso);
                runCasoUso.setColor("00c3f8");
                runCasoUso.addCarriageReturn();
            }

            //Validar si es un nuev Caso de Prueba
            if (casoPrueba.equals(currentCasoPrueba) == false) {
                currentCasoPrueba = casoPrueba;
                //Agregar Caso de Prueba
                runCasoPrueba.setBold(false);
                runCasoPrueba.setFontFamily("Calibri Light (Títulos)");
                runCasoPrueba.setFontSize(12);
                runCasoPrueba.setText(casoPrueba);
                runCasoPrueba.setColor("2271b3");
                runCasoPrueba.addCarriageReturn();
            }

            //Establecer formato de letra para el texto
            runText.setBold(true);
            runText.setFontFamily("Calibri Light (Títulos)");
            runText.setFontSize(12);
            runText.setColor("191970");

            InputStream imageCxIRequest, imageCxIResponse, imageCPRequest, imageCPResponse;
            imageCxIRequest = new FileInputStream(System.getProperty("user.dir") + "\\files\\" + nameEvs.get(0) + ".png");
            imageCxIResponse = new FileInputStream(System.getProperty("user.dir") + "\\files\\" + nameEvs.get(1) + ".png");
            imageCPRequest = new FileInputStream(System.getProperty("user.dir") + "\\files\\" + nameEvs.get(2) + ".png");
            imageCPResponse = new FileInputStream(System.getProperty("user.dir") + "\\files\\" + nameEvs.get(3) + ".png");

            //Indicar las propiedades de la imagen
            int imageType = XWPFDocument.PICTURE_TYPE_PNG;
            //Set image width/height
            int imageWidth = 450;
            int imageHeight = 250;

            //Evidencias del Web Service queryInstallationChargesService
            runText.setText("Request Cargo por Instalación");
            //Agregar la captura al documento
            runText.addPicture(imageCxIRequest, imageType, "img", Units.toEMU(imageWidth), Units.toEMU(imageHeight));
            runText.setText("Response cargo por instalación");
            runText.addPicture(imageCxIResponse, imageType, "img", Units.toEMU(imageWidth), Units.toEMU(imageHeight));
            imageCxIRequest.close();
            imageCxIResponse.close();
            //Evidencias del Web Service consultaPromocionalCXIService
            runText.setText("Request consultar promoción");
            runText.addPicture(imageCPRequest, imageType, "img", Units.toEMU(imageWidth), Units.toEMU(imageHeight));
            runText.setText("Response consultar promoción");
            runText.addPicture(imageCPResponse, imageType, "img", Units.toEMU(imageWidth), Units.toEMU(imageHeight));
            imageCPRequest.close();
            imageCPResponse.close();

            //Set document name and destination
            FileOutputStream fos = new FileOutputStream(finalReport);
            document.write(fos);
            fos.close();
            //Cleanup
            document.close();
            
            if (isRevalidation) {
                successfulRevalidation(nameEvs, filePath, cPrueba);
            } else {
                //Eliminar capturas de evidencias temporales
                deleteScreenShots(nameEvs);
            }
        } catch (IOException | InvalidFormatException ex) {
            System.out.println("Error en addEvidence: " + ex);
        }
        //return finalReport;
    }

    public void scape() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
        } catch (AWTException a) {
            a.printStackTrace();
        }
    }
}
