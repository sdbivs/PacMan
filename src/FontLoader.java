import java.awt.*;
import java.io.*;

public class FontLoader{
    public static Font registerCustomFont(){
        try(InputStream is = FontLoader.class.getResourceAsStream("/Font/Emulogic-zrEw.ttf")){
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge =  GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont;
        }catch(IOException | FontFormatException e){
            e.printStackTrace();
            return null;
        }
    }
}