package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

public class Assets {

	// initializes all the images

	// Weapons
	public static BufferedImage aura;
	public static BufferedImage bone;
//	public static BufferedImage axe;
//	public static BufferedImage cross;
//	public static BufferedImage kingBible;
//	public static BufferedImage magicWand;
//	public static BufferedImage fireWand;

	public static BufferedImage WeaponBanana;
	public static BufferedImage ProjectileBanana;
	public static BufferedImage ProjectileBullet;
	
	public static BufferedImage AuraIcon;
	public static BufferedImage BananaIcon;
	public static BufferedImage FireStaffIcon;
	
	public static BufferedImage oatsIcon;
	public static BufferedImage watchIcon;
	public static BufferedImage magnetIcon;
	public static BufferedImage keyIcon;
	public static BufferedImage batteryIcon;
	public static BufferedImage cloverIcon;
	public static BufferedImage midasIcon;

	// Player
	public static BufferedImage playerSheet;

	public static BufferedImage[] exp;
	
	public static BufferedImage[] impact;

	// Enemy GIF frames: walking and death
	public static BufferedImage[] zombieWalk;
	public static BufferedImage[] zombieDeath;

	public static BufferedImage[] skeletonWalk;
	public static BufferedImage[] skeletonDeath;

	public static BufferedImage[] mudmanWalk;
	public static BufferedImage[] mudmanDeath;

	public static BufferedImage[] batWalk;
	public static BufferedImage[] batDeath;

	public static BufferedImage[] glowingBatWalk;
	public static BufferedImage[] glowingBatDeath;

	public static void load() {
		try {
			//link to effect assets : https://untiedgames.itch.io/super-pixel-effects-gigapack
	
			oatsIcon = ImageIO.read(Assets.class.getResource("/Images/Artifacts/Common/Item_Oats.png"));
			watchIcon = ImageIO.read(Assets.class.getResource("/Images/Artifacts/Common/Item_Time_Bracelet.png"));
			cloverIcon = ImageIO.read(Assets.class.getResource("/Images/Artifacts/Common/Item_Clover.png"));
			batteryIcon = ImageIO.read(Assets.class.getResource("/Images/Artifacts/Common/Item_Battery.png"));
			keyIcon = ImageIO.read(Assets.class.getResource("/Images/Artifacts/Common/Item_Key.png"));
			midasIcon = ImageIO.read(Assets.class.getResource("/Images/Artifacts/Common/Item_Golden_Glove.png"));
			
			magnetIcon = ImageIO.read(Assets.class.getResource("/Images/Artifacts/Legendary/Item_Sucky_Magnet.png"));
			
			
			
			
			
			AuraIcon = ImageIO.read(Assets.class.getResource("/Images/Items/Aura_Icon.png"));
			BananaIcon = ImageIO.read(Assets.class.getResource("/Images/Items/Banana_Icon.png"));
			FireStaffIcon = ImageIO.read(Assets.class.getResource("/Images/Items/Fire_Staff.png"));

			ProjectileBanana = ImageIO.read(Assets.class.getResource("/Images/Projectiles/Banana projectile.png"));
			ProjectileBullet = ImageIO.read(Assets.class.getResource("/Images/Projectiles/newBulletProjectile.png"));
			
			exp = splitSpriteSheet(ImageIO.read(Assets.class.getResource("/Images/Coins/spr_coin_azu.png")), 16, 16);
			
			impact = splitSpriteSheet(ImageIO.read(Assets.class.getResource("/Images/Projectiles/Impact.png")), 48, 48);

			// Player
			
			playerSheet = ImageIO.read(Assets.class.getResource("/Images/Player/antonio_spritesheet.png"));

			// Enemy GIFs
			zombieWalk = loadGifFrames("/Images/Enemy/z.gif");
			zombieDeath = loadGifFrames("/Images/Enemy/zDead.gif");

			skeletonWalk = loadGifFrames("/Images/Enemy/s.gif");
			skeletonDeath = loadGifFrames("/Images/Enemy/sDead.gif");

			mudmanWalk = loadGifFrames("/Images/Enemy/m.gif");
			mudmanDeath = loadGifFrames("/Images/Enemy/mDead.gif");

			batWalk = loadGifFrames("/Images/Enemy/bat.gif");
			batDeath = loadGifFrames("/Images/Enemy/batDead.gif");

			glowingBatWalk = loadGifFrames("/Images/Enemy/glowBat.gif");
			glowingBatDeath = loadGifFrames("/Images/Enemy/glowBatDead.gif");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * translates gif to buffered image array
	 * 
	 * @param string of the image path for getting file
	 * @return returns array of buffered images of the gif
	 */
	public static BufferedImage[] loadGifFrames(String path) {
		try (InputStream stream = Assets.class.getResourceAsStream(path)) {
			if (stream == null) {
				System.err.println("Cannot find resource: " + path);
				return new BufferedImage[0];
			}

			ImageInputStream iis = ImageIO.createImageInputStream(stream);
			var readers = ImageIO.getImageReadersByFormatName("gif");
			if (!readers.hasNext()) {
				System.err.println("No GIF reader available for: " + path);
				return new BufferedImage[0];
			}

			var reader = readers.next();
			reader.setInput(iis, false);

			int frameCount = reader.getNumImages(true);
			if (frameCount == 0) {
				System.err.println("No frames found in GIF: " + path);
				return new BufferedImage[0];
			}

			BufferedImage[] frames = new BufferedImage[frameCount];
			BufferedImage firstFrame = reader.read(0);
			int width = firstFrame.getWidth();
			int height = firstFrame.getHeight();

			for (int i = 0; i < frameCount; i++) {
				BufferedImage frame = reader.read(i);
				if (frame == null) {
					// Replace null frames with a transparent placeholder
					frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				} else if (frame.getWidth() != width || frame.getHeight() != height) {
					// Resize inconsistent frames to match first frame
					BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
					tmp.getGraphics().drawImage(frame, 0, 0, null);
					frame = tmp;
				}
				frames[i] = frame;
			}

			return frames;

		} catch (Exception e) {
			e.printStackTrace();
			return new BufferedImage[0];
		}
	}

	public static BufferedImage[] splitSpriteSheet(BufferedImage sheet, int frameWidth, int frameHeight) {

	    int cols = sheet.getWidth() / frameWidth;

	    BufferedImage[] output = new BufferedImage[cols];

	    for (int x = 0; x < cols; x++) {

	        if (x * frameWidth + frameWidth > sheet.getWidth()) break;
	        if (frameHeight > sheet.getHeight()) break;

	        BufferedImage frame = sheet.getSubimage(
	            x * frameWidth,
	            0,
	            frameWidth,
	            frameHeight
	        );

	        output[x] = frame;
	    }

	    return output;
	}
}