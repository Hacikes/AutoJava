package org.qateams.utils;

import org.qateams.constansts.FrameworkConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ReadProperties {

    protected static FileInputStream fileInputStream;
    protected static Properties PROPERTIES;
    static {
        try {
            //указание пути до файла с настройками
            fileInputStream = new FileInputStream(FrameworkConstants.getConfigFilePath());
            PROPERTIES = new Properties();
            PROPERTIES.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("Check value on conf.properties or conf.properties path!");
            e.printStackTrace();
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace(); }
        }
    }
    /**
     * Метод для возврата строки со значением из файла с настройками
     */
    public static String getProperty(String key) throws Exception {
        if(Objects.isNull(PROPERTIES.getProperty(key)) || Objects.isNull(key)) {
            throw new Exception("Property name " + key + " is not found. Please check conf.properties!");
        }
        return PROPERTIES.getProperty(key);
    }
}

