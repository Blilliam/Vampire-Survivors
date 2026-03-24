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

	// Player
	public static BufferedImage playerSheet;

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
			// assigns each image variable to an actual image in the file

			// Weapons
			aura = ImageIO.read(Assets.class.getResource("/Images/Items/Sprite-Knife.png"));
			bone = ImageIO.read(Assets.class.getResource("/Images/Items/Sprite-Bone.png"));
//			axe = ImageIO.read(Assets.class.getResource("/Images/Items/Sprite-Axe.png"));
//			cross = ImageIO.read(Assets.class.getResource("/Images/Items/Sprite-Cross.png"));
//			kingBible = ImageIO.read(Assets.class.getResource("/Images/Items/Sprite-King_Bible.png"));
//			magicWand = ImageIO.read(Assets.class.getResource("/Images/Items/Sprite-Magic_Wand.png"));
//			fireWand = ImageIO.read(Assets.class.getResource("/Images/Items/Sprite-Fire_Wand.png"));

			ProjectileBanana = ImageIO.read(Assets.class.getResource("/Images/Items/Banana projectile.png"));

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
}