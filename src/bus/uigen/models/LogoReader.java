package bus.uigen.models;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class LogoReader {
	public static final String LOGO_FILE = "/data/oelogo.png";
	public static Image openLogoFile() throws IOException {
		String retVal = "";
//		try {
//			return new BufferedReader( new FileReader(dictionaryFile));
			InputStream imageStream = LogoReader.class.getResourceAsStream(LOGO_FILE);
			return  ImageIO.read(imageStream);
//		} catch (Exception e) {
////			e.printStackTrace();
//			return null;
////			System.exit(-1);
////			return null;
//		}
	}

}
