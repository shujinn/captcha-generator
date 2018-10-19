package main;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;


/**
 *  My Test captcha: extends DefaultKaptcha, implements producer
 *
 *  create image
 */
public class Test extends DefaultKaptcha implements Producer {

    /**
     * main: draw the captcha
     * @param args
     */
    public static void main(String[] args) {

        // MODE : Test, Batch
        Test.generator("Batch", 10);
    }

    private static void generator(String mode, int num) {

        // load configMap from properties
        Properties configMap = new Properties();
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream("src/resources/captcha.properties"));
            configMap.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set configMap to Configurable
        Test test = new Test();
        Config config = new Config(configMap);
        test.setConfig(config);

        if (mode.equals("Test")) {

            // create test captcha
            String text = test.createText();
            BufferedImage bufferedImage = test.createImage(text);

            // log
            String[] type = test.getConfig().getProperties().getProperty("kaptcha.obscurificator.impl").split("\\.");
            Logger logger = Logger.getLogger("captcha");
            logger.info(String.format("Create %s captcha : %s", type[type.length - 1], text));

            // save captcha
            File file = new File("001.png");
            try {
                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            for (int i = 0; i < num; i++ ) {

                // create captcha
                String text = test.createText();
                BufferedImage bufferedImage = test.createImage(text);

                // log
                String[] type = test.getConfig().getProperties().getProperty("kaptcha.obscurificator.impl").split("\\.");
                Logger logger = Logger.getLogger("captcha");
                logger.info(String.format("Create %s captcha : %d ===> %s", type[type.length - 1], i + 1, text));

                // save captcha
                String typeNum;
                switch (type[type.length - 1]) {
                    case "WaterRipple": typeNum = "1"; break;
                    case "ShadowGimpy": typeNum = "2"; break;
                    default: typeNum = "3";
                }

                File file = new File(String.format("test/%s-%s.png", text, typeNum));
                try {
                    ImageIO.write(bufferedImage, "png", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



