package addons.home;

import generate.report.Reporte;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import matriceshome.BundleTests;
import matriceshome.SingleBBITests;
import org.testng.reporters.jq.Main;

/**
 *
 * @author pagiron
 */
public class HomeScreen extends javax.swing.JFrame {

    HomeApi api = new HomeApi();
    Reporte rep = new Reporte();
    BundleTests bt = new BundleTests();
    SingleBBITests st = new SingleBBITests();
    TextPrompt titulo, tkt, tester;
    public String message, matrixName;
    public static String title, nameQA, tkt_RFC, environment, category, filePath;
    private Icon warning, error, information;
    private int option;

    public HomeScreen() {
        initComponents();
        //Centrar ventana al iniciar
        setLocationRelativeTo(null);
        //Inicializar label con el titulo correspondiente
        lblTitle.setText("Prueba de addons para cuentas " + category);
        //Colocar elementos deshabilitados al iniciar
        btnIniciar.setEnabled(false);
        //Colocar placeholders en JTextField
        titulo = new TextPrompt("Título del proyecto (Opcional)", txtTitle);
        tester = new TextPrompt("Nombre del QA (Opcional)", txtTester);
        tkt = new TextPrompt("Ticket/RFC (Opcional)", txtTkt);
        //Mostrar explorador como en Windows
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Asignar ruta de Iconos
        information = new ImageIcon(getClass().getResource("/icons/info.png"));
        warning = new ImageIcon(getClass().getResource("/icons/warning.png"));
        error = new ImageIcon(getClass().getResource("/icons/error.png"));
        
        //Seleccionar ambiente de pruebas UAT por defecto
        rbUAT.setSelected(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        btnGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        btnRutaMatriz = new javax.swing.JButton();
        txtTitle = new javax.swing.JTextField();
        txtTester = new javax.swing.JTextField();
        txtTkt = new javax.swing.JTextField();
        rbUAT = new javax.swing.JRadioButton();
        rbPRD = new javax.swing.JRadioButton();
        btnReturn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Digital Automation Software");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(23, 23, 224));

        lblTitle.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(27, 250, 244));
        lblTitle.setText("l");

        btnIniciar.setBackground(new java.awt.Color(242, 242, 242));
        btnIniciar.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        btnIniciar.setForeground(new java.awt.Color(0, 0, 51));
        btnIniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/tests.png"))); // NOI18N
        btnIniciar.setText("Iniciar Pruebas");
        btnIniciar.setBorderPainted(false);
        btnIniciar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIniciar.setIconTextGap(14);
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        btnRutaMatriz.setBackground(new java.awt.Color(242, 242, 242));
        btnRutaMatriz.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        btnRutaMatriz.setForeground(new java.awt.Color(0, 0, 51));
        btnRutaMatriz.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/xlsx.png"))); // NOI18N
        btnRutaMatriz.setText("Seleccionar Matriz");
        btnRutaMatriz.setBorderPainted(false);
        btnRutaMatriz.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRutaMatriz.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRutaMatriz.setIconTextGap(14);
        btnRutaMatriz.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRutaMatrizActionPerformed(evt);
            }
        });

        txtTitle.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N

        txtTester.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N

        txtTkt.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N

        rbUAT.setBackground(new java.awt.Color(23, 23, 224));
        btnGroup.add(rbUAT);
        rbUAT.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        rbUAT.setForeground(new java.awt.Color(108, 250, 240));
        rbUAT.setText("Ambiente UAT");
        rbUAT.setContentAreaFilled(false);
        rbUAT.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbUAT.setFocusPainted(false);

        rbPRD.setBackground(new java.awt.Color(23, 23, 224));
        btnGroup.add(rbPRD);
        rbPRD.setFont(new java.awt.Font("Yu Gothic UI", 1, 14)); // NOI18N
        rbPRD.setForeground(new java.awt.Color(108, 250, 240));
        rbPRD.setText("Ambiente PRD");
        rbPRD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnReturn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/return.png"))); // NOI18N
        btnReturn.setToolTipText("Regresar");
        btnReturn.setBorderPainted(false);
        btnReturn.setContentAreaFilled(false);
        btnReturn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTester, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTkt, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbPRD)
                            .addComponent(rbUAT)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(32, Short.MAX_VALUE)
                        .addComponent(btnRutaMatriz)
                        .addGap(29, 29, 29)
                        .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(lblTitle))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnReturn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtTester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbUAT)
                        .addGap(18, 18, 18)
                        .addComponent(rbPRD)))
                .addGap(18, 18, 18)
                .addComponent(txtTkt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRutaMatriz, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnReturn, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public String seleccionarArchivo() {
        String path = "", extension = "";
        File archivo;
        JFileChooser seleccionar = new JFileChooser();
        seleccionar.setDialogTitle("Seleccionar Matriz de Pruebas");
        seleccionar.setFileFilter(new FileNameExtensionFilter("Excel", "xlsx"));
        seleccionar.showOpenDialog(null);
        archivo = seleccionar.getSelectedFile();
        if (archivo != null) {
            path = archivo.getAbsolutePath();
            matrixName = archivo.getName();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor seleccione el archivo de la Matriz de Pruebas para poder continuar.", "ARCHIVO NO SELECCIONADO", JOptionPane.DEFAULT_OPTION, error);
        }

        //Obtener extension del archivo
        int indice = path.lastIndexOf(".");
        if (indice > 0 && indice < (path.length() - 1)) {
            extension = path.substring(indice + 1);
            //Validar que el archivo sea un libro de Excel
            if (!extension.equals("xlsx")) {
                path = "";
                JOptionPane.showMessageDialog(null, "El archivo seleccionado no es un Libro de Excel.\nPor favor seleccione el archivo de la Matriz de Pruebas para poder continuar", "FORMATO NO VALIDO", JOptionPane.DEFAULT_OPTION, error);
            }
        }
        return path;
    }

    public String readOptionalText() {
        //Obtener Titulo del proyecto, si lo hay
        if (!txtTitle.getText().trim().isEmpty()) {
            title = txtTitle.getText().trim();
        } else {
            title = "";
        }
        //Obtener nombre del QA, si lo hay
        if (!txtTester.getText().trim().isEmpty()) {
            nameQA = txtTester.getText().trim();
        } else {
            nameQA = "";
        }
        //Obtener n° de Ticket o RFC, si lo hay
        if (!txtTkt.getText().trim().isEmpty()) {
            tkt_RFC = txtTkt.getText().trim().toUpperCase();
        } else {
            tkt_RFC = "";
        }
        return tkt_RFC;
    }

    public boolean isSetEnvironment() {
        boolean selected = false;
        if (rbUAT.isSelected()) {
            environment = "UAT";
            selected = true;
        } else if (rbPRD.isSelected()) {
            environment = "PRD";
            selected = true;
        }
        return selected;
    }

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed

        if (isSetEnvironment()) {

            readOptionalText();
            bt.filePath = filePath;
            st.filePath = filePath;
            bt.runBundleTestCases();
            st.runSingleTestCases();
            JOptionPane.showConfirmDialog(null, "Set de pruebas finalizado.\n"
                    + "Encontrará los reportes con las evidencias en el Escritorio.", "EJECUCION EXITOSA", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, information);
        }

    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnRutaMatrizActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRutaMatrizActionPerformed
        //Seleccionar archivos desde el explorador
        do {
            filePath = seleccionarArchivo();
            if (!filePath.equals("")) {
                option = JOptionPane.showConfirmDialog(null, "Se ha seleccionado el archivo '" + matrixName + "'\n¿Confirma que es la Matriz correcta?", "CONFIRMAR MATRIZ DE PRUEBAS", JOptionPane.YES_NO_OPTION, JOptionPane.DEFAULT_OPTION, warning);
                if (option == 0) {
                    btnIniciar.setEnabled(true);
                }
            }
        } while (option != 0);

    }//GEN-LAST:event_btnRutaMatrizActionPerformed

    private void btnReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnActionPerformed
        Category cat = new Category();
        cat.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnReturnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeScreen().setVisible(true);
                /*
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
                 */
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnReturn;
    private javax.swing.JButton btnRutaMatriz;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JRadioButton rbPRD;
    private javax.swing.JRadioButton rbUAT;
    private javax.swing.JTextField txtTester;
    private javax.swing.JTextField txtTitle;
    private javax.swing.JTextField txtTkt;
    // End of variables declaration//GEN-END:variables
}
