import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainPanel extends BasicJPanel {
    private MyFilters images;
    private BufferedImage originalImage;
    private JTextField userProfile;
    private final GetImageFromFacebook getImageFromFacebook;
    private ChromeDriver driver;
    private JLabel message;

    public MainPanel(int x, int y, int w, int h, Color color) {
        super(x, y, w, h, Constants.BAKE_GROUND);
        this.setBackground(color);
        this.getImageFromFacebook = new GetImageFromFacebook(this);
        try {
            new Thread(getImageFromFacebook).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void init() {
        super.init();
        initUserTextFiled();
        initAllLabel();
        this.images = new MyFilters();
        initAllButton();
        initDriver();
    }

    public void initDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\97252\\Downloads\\chromedriver_win32 (1)\\1\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        this.driver = new ChromeDriver(chromeOptions);
        getInToFaceBook();
    }

    public void getInToFaceBook() {
        driver.get(GetImageFromFacebook.URL_FACEBOOK);
        driver.manage().window().maximize();
        WebElement usernameTextInput = driver.findElement(By.id("email"));//by.path""
        usernameTextInput.sendKeys(GetImageFromFacebook.EMAIL_OR_PASSWORD);

        WebElement passwordTextInput = driver.findElement(By.id("pass"));
        passwordTextInput.sendKeys(GetImageFromFacebook.PASSWORD);

        WebElement enterButton = driver.findElement(By.name("login"));
        enterButton.click();
    }

    public ChromeDriver getDriver() {
        return this.driver;
    }


    public void initAllLabel() {
        addJLabel("WELCOME TO IMAGE PROCESSING APP!", Constants.TITLE_X,
                Constants.TITLE_Y, Constants.TITLE_W, Constants.TITLE_H, Constants.TITLE_SIZE, Color.white);
        JLabel text = addJLabel("Enter a facebook profile for image editing: ",
                Constants.TEXT_X, Constants.TEXT_Y, Constants.TEXT_W, Constants.TEXT_H,
                Constants.TEXT_SIZE, Color.white);
        this.message = addJLabel("", text.getX(), text.getY() - text.getHeight(), text.getWidth(), text.getHeight(), Constants.TEXT_SIZE, Color.red);
    }

    public void setMessage(String message) {
        this.message.setText(message);
        repaint();
    }

    public void initUserTextFiled() {
        this.userProfile = addTextField(Constants.TEXT_FIELD_X, Constants.TEXT_FIELD_Y,
                Constants.TEXT_FIELD_W, Constants.TEXT_FIELD_H);
        userProfile.addActionListener(e -> {
            this.getImageFromFacebook.stop();
            driver.get(GetImageFromFacebook.URL_FACEBOOK + userProfile.getText().replace(" ", "."));
            userProfile.setText("");
            driver.manage();
            this.getImageFromFacebook.startAgain();
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.originalImage != null) {
            g.drawImage(this.originalImage, Constants.IMAGE_X, Constants.IMAGE_Y, this);
            if (this.images != null) {
                g.drawImage(this.images.getImageForEditing(), Constants.WINDOW_W -
                        Constants.IMAGE_X - Constants.IMAGE_W, Constants.IMAGE_Y, this);
            } else {
                g.drawImage(this.originalImage, Constants.WINDOW_W -
                        Constants.IMAGE_X - Constants.IMAGE_W, Constants.IMAGE_Y, this);
            }
        }
    }

    public void initAllButton() {
        int currentY = Constants.FIRST_BUTTON_Y;
        for (int i = 0; i < Constants.AMOUNT_OF_BUTTONS; i++) {
            Button button = addButton(Constants.FILTER_OPTION[i], Constants.TEXT_FIELD_X, currentY,
                    Constants.TEXT_FIELD_W, Constants.TEXT_FIELD_H, Color.white, Color.black);
            addActButton(button, i+1);
            currentY += Constants.TEXT_FIELD_H;
        }
    }

    public void addActButton(Button button, int type) {
        button.addActionListener(e -> {
            BufferedImage image = this.originalImage;
            images.setFilterLoop(type, image);
            repaint();
        });
    }

    public boolean isSetOriginalImage(URL url) {
        boolean change = false;
        if (url != null) {
            try {
                this.originalImage = ImageIO.read(url);
                images.setImageForEditing(url);
                this.repaint();
                change = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("change?" + change);
        return change;
    }


}