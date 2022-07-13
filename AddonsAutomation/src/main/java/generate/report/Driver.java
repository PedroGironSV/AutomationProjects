package generate.report;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author pagiron
 */
public class Driver {

    public WebDriver wdri;

    public void runWebDriver(String screenName) {
        String url = "", urlDriver;
        Path path, pathDriver;
        try {
            //Indicar ubicación del Path del URL de los archivos XML
            path = Paths.get(System.getProperty("user.dir") + "\\responses\\" + screenName + ".xml");
            url = path.toString();
            //Indicar ubicación del Path del URL de los archivos XML
            pathDriver = Paths.get(System.getProperty("user.dir") + "\\driver\\chromedriver.exe");
            urlDriver = pathDriver.toString();
            System.setProperty("webdriver.chrome.driver", urlDriver);
            WebDriverManager.chromedriver().setup();
            wdri = new ChromeDriver();
            wdri.manage().window().maximize();
            wdri.get(url);
        } catch (Exception ex) {
            System.out.println("Validar runWebDriver: " + ex.getMessage() + "\nRuta: " + url);
        }
    }

    public void quit() {
        wdri.quit();
    }
}
