
package generate.report;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

/**
 *
 * @author pagiron
 */
public class Reporte {
    
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
    
    public void scape() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
        } catch (AWTException a) {
            a.printStackTrace();
        }
    }
}
