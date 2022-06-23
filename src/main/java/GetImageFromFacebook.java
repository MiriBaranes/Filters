import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class GetImageFromFacebook implements Runnable {
    public static final String URL_FACEBOOK = "https://facebook.com/";
    public static final String EMAIL_OR_PASSWORD = "Naamabbitan@gmail.com";
    public static final String PASSWORD = "Abcd12";
    private boolean running;
    private String userInput;
    private URL imageURL;
    private MainPanel panel;

    public GetImageFromFacebook(MainPanel basicJPanel) {
        this.userInput = null;
        this.panel = basicJPanel;
    }

    public void startAgain() {
        this.panel.setMessage("");
        this.running = true;
        run();
    }

    @Override
    public void run() {
        while (running) {
            Util.sleep(1000);
            System.out.println("im here");
            run_();
        }
    }

    public void stop() {
        this.running = false;
    }

    public void run_() {
        List<WebElement> images = panel.getDriver().findElements(By.cssSelector("div > a > div > svg > g > image"));
        if (images.size()>0) {
            try {
                this.imageURL = new URL(images.get(0).getAttribute("xlink:href"));
                boolean change = panel.isSetOriginalImage(imageURL);
                if (change) {
                    stop();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            panel.setMessage("Error! this image/profile not exist!");
            stop();
        }
    }

}
