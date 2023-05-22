package hr.fer.oprpp1.hw08.jnotepadpp.icons;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class IconLoader {

    public static ImageIcon load(String name){
        try(InputStream is = IconLoader.class.getResourceAsStream("/icons/" + name)){
            if(is == null){
                throw new IOException();
            }
            byte[] unmodifiedBytes = is.readAllBytes();

            return new ImageIcon(unmodifiedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
