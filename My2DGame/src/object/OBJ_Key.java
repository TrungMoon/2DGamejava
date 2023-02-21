package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class OBJ_Key extends SuperObject{
	
	GamePanel gp;

	public OBJ_Key(GamePanel gp) {
		
		this.gp =gp;
		name = "key";
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
			uTool.scaleImage(image, gp.titleSize, gp.titleSize);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
