package pl.szinton.gk.utils;

import pl.szinton.gk.Application;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceUtils {

    public static File getFileFromResources(String filepath) {
        try {
            URL resource = Application.class.getClassLoader().getResource("normalmap.jpg");
            if (resource == null) {
                throw new IllegalArgumentException("File located at \"" + filepath + "\" does not exist!");
            }
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Error encountered while opening \"" + filepath + "\"!");
        }
    }
}
