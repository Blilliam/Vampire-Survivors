package Open.Upgrades;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;
import java.util.*;

import Open.Weapons.*;
import main.AppPanel;
import main.GameButton;
import main.GameObject;
import main.enums.WeaponRarity;
import main.enums.WeaponTypes;
import main.enums.WeaponUpgrades;

public class Upgrades {
	private GameObject gameObj;
	private GameButton[] boxes = new GameButton[3];
	private WeaponRarity[] boxRarities = new WeaponRarity[3];
	private EnumMap<WeaponTypes, Weapon> allWeapons;

	private final int rectWidth = 900;
	private final int rectHeight = 200;

	private List<PixelParticle> particles = new ArrayList<>();
	private float flashAlpha = 0f;
	private long timer = 0;
	private Random rand = new Random();

	public Upgrades(GameObject gameObj) {
		this.gameObj = gameObj;
		allWeapons = new EnumMap<>(WeaponTypes.class);
		allWeapons.put(WeaponTypes.Aura, new AuraWeapon(gameObj));
		allWeapons.put(WeaponTypes.Banana, new BananaWeapon(gameObj));
		allWeapons.put(WeaponTypes.Bone, new BoneWeapon(gameObj));
		allWeapons.put(WeaponTypes.FireStaff, new FireStaffWeapon(gameObj));
		allWeapons.put(WeaponTypes.PewPew, new PewPewWeapon(gameObj));
		allWeapons.put(WeaponTypes.Sword, new SwordWeapon(gameObj));
	}

	public void shuffleUpgrades() {
		startLevelUpEffect();
		Set<WeaponTypes> ownedTypes = gameObj.getPlayer().getWeapons().keySet();

		List<WeaponTypes> availableToBuyPool = new ArrayList<>();
		for (WeaponTypes type : allWeapons.keySet()) {
			if (!ownedTypes.contains(type)) {
				availableToBuyPool.add(type);
			}
		}

		for (int i = 0; i < boxes.length; i++) {
			int yPos = 100 + (i * 300);
			boxRarities[i] = null;

			boolean canBuy = false;
			if (gameObj.getPlayer().getWeapons().size() < gameObj.getPlayer().getMAX_WEAPONS()) {
				if (!availableToBuyPool.isEmpty()) {
					canBuy = true;
				}
			}

			boolean canUpgrade = !ownedTypes.isEmpty();

			if (canBuy && (rand.nextBoolean() || !canUpgrade)) {
				int index = rand.nextInt(availableToBuyPool.size());
				WeaponTypes type = availableToBuyPool.remove(index);
				final WeaponTypes finalType = type;

				boxes[i] = new GameButton(AppPanel.WIDTH / 2 - rectWidth / 2, yPos, rectWidth, rectHeight,
						"NEW WEAPON: " + formatName(type.name()), () -> {
							gameObj.getPlayer().addWeapon(finalType, allWeapons.get(finalType));
							finishUpgrade();
						});
				boxes[i].setTransparent(true);
			} else if (canUpgrade) {
				List<WeaponTypes> ownedList = new ArrayList<>(ownedTypes);
				WeaponTypes typeToUpgrade = ownedList.get(rand.nextInt(ownedList.size()));
				final Weapon playerWeapon = gameObj.getPlayer().getWeapons().get(typeToUpgrade);

				WeaponUpgrades stat = getRandomStatForWeapon(playerWeapon);
				WeaponRarity rarity = WeaponRarity.values()[rand.nextInt(WeaponRarity.values().length)];
				boxRarities[i] = rarity;

				double before = playerWeapon.getStats().getOrDefault(stat, 0.0);
				double after = calculateProjectedValue(playerWeapon, stat, rarity);

				String displayName = getDisplayName(stat);
				String beforeStr, afterStr;

				if (stat == WeaponUpgrades.AttackSpeed) {
					beforeStr = String.format("%.2fs", before / 60.0);
					afterStr = String.format("%.2fs", after / 60.0);
				} else if (isPercentStat(stat)) {
					beforeStr = String.format("%.0f%%", before * 100);
					afterStr = String.format("%.0f%%", after * 100);
				} else {
					beforeStr = String.format("%.1f", before);
					afterStr = String.format("%.1f", after);
				}

				final WeaponUpgrades finalStat = stat;
				final WeaponRarity finalRarity = rarity;

				String buttonText = String.format("[%s] %s %s: %s -> %s", finalRarity, formatName(typeToUpgrade.name()),
						displayName, beforeStr, afterStr);

				boxes[i] = new GameButton(AppPanel.WIDTH / 2 - rectWidth / 2, yPos, rectWidth, rectHeight, buttonText,
						() -> {
							playerWeapon.applyUpgrade(finalStat, finalRarity);
							finishUpgrade();
						});
				boxes[i].setTransparent(true);
			}
		}
	}

	private String formatName(String name) {
		String[] words = name.split("_");
		StringBuilder sb = new StringBuilder();
		for (String w : words) {
			sb.append(w.substring(0, 1).toUpperCase()).append(w.substring(1).toLowerCase()).append(" ");
		}
		return sb.toString().trim();
	}

	private Color getRarityColor(WeaponRarity rarity) {
		if (rarity == null) {
			return new Color(80, 80, 80);
		}

		return switch (rarity) {
		case BRONZE -> new Color(140, 70, 30);
		case SILVER -> new Color(160, 165, 175);
		case GOLD -> new Color(210, 170, 0);
		case DIAMOND -> new Color(0, 180, 220);
		default -> new Color(60, 60, 60);
		};
	}

	public void draw(Graphics2D g) {
		g.setColor(new Color(255, 215, 0, 30));
		for (int i = 0; i < 8; i++) {
			double angle = (timer * 0.02) + (i * Math.PI / 4);
			int[] xPoints = { AppPanel.WIDTH / 2, AppPanel.WIDTH / 2 + (int) (Math.cos(angle - 0.1) * 1500),
					AppPanel.WIDTH / 2 + (int) (Math.cos(angle + 0.1) * 1500) };
			int[] yPoints = { AppPanel.HEIGHT / 2, AppPanel.HEIGHT / 2 + (int) (Math.sin(angle - 0.1) * 1500),
					AppPanel.HEIGHT / 2 + (int) (Math.sin(angle + 0.1) * 1500) };
			g.fillPolygon(xPoints, yPoints, 3);
		}

		for (int i = 0; i < boxes.length; i++) {
			GameButton b = boxes[i];
			if (b == null)
				continue;

			Color baseColor = getRarityColor(boxRarities[i]);
			int pixelSize = 10;
			int half = b.getHeight() / 2;
			for (int py = 0; py < b.getHeight(); py += pixelSize) {
				float dist = Math.abs(py - half) / (float) half;
				float darkFactor = 0.5f + (dist * 0.5f);

				if (b.isHovering()) {
					darkFactor += 0.15f;
				}
				if (darkFactor > 1.0f) {
					darkFactor = 1.0f;
				}

				g.setColor(new Color((int) (baseColor.getRed() * darkFactor), (int) (baseColor.getGreen() * darkFactor),
						(int) (baseColor.getBlue() * darkFactor)));
				g.fillRect(b.getX(), b.getY() + py, b.getWidth(), pixelSize);
			}
			b.draw(g);
		}

		for (PixelParticle p : particles) {
			g.setColor(p.color);
			g.fillRect((int) p.x, (int) p.y, p.size, p.size);
		}

		g.setFont(new Font("Monospaced", Font.BOLD, 75));
		String msg = "LEVEL UP";
		int xText = AppPanel.WIDTH / 2 - g.getFontMetrics().stringWidth(msg) / 2;
		g.setColor(Color.BLACK);
		g.drawString(msg, xText + 5, 85);
		g.setColor(Color.YELLOW);
		g.drawString(msg, xText, 80);

		if (flashAlpha > 0) {
			g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, flashAlpha));
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, AppPanel.WIDTH, AppPanel.HEIGHT);
			g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1f));
		}
	}

	private String getDisplayName(WeaponUpgrades stat) {
		return switch (stat) {
		case AttackSpeed -> "Cooldown";
		case AttackDamage -> "Damage";
		case AttackSize -> "Area";
		case ProjectileCount -> "Amount";
		case ProjectileSpeed -> "Speed";
		case ProjectileBounce -> "Bounces";
		case CriticalChance -> "Crit Rate";
		case CriticalDamage -> "Crit Damage";
		default -> stat.toString();
		};
	}

	private boolean isPercentStat(WeaponUpgrades stat) {
		boolean isPercent = false;
		if (stat == WeaponUpgrades.CriticalChance || stat == WeaponUpgrades.CriticalDamage
				|| stat == WeaponUpgrades.AttackSize) {
			isPercent = true;
		}
		return isPercent;
	}

	private double calculateProjectedValue(Weapon w, WeaponUpgrades upgrade, WeaponRarity rarity) {
		double currentVal = w.getStats().getOrDefault(upgrade, 0.0);
		double baseDamage = w.getBaseStats().getOrDefault(WeaponUpgrades.AttackDamage, 10.0);
		double damageStep = baseDamage / 10.0;

		double multiplier = 1.0;
		if (rarity == WeaponRarity.SILVER) {
			multiplier = 1.2;
		} else if (rarity == WeaponRarity.GOLD) {
			multiplier = 1.4;
		} else if (rarity == WeaponRarity.DIAMOND) {
			multiplier = 2.0;
		}

		if (upgrade == WeaponUpgrades.ProjectileCount || upgrade == WeaponUpgrades.ProjectileBounce) {
			return currentVal + (1.0 * multiplier);
		}
		if (upgrade == WeaponUpgrades.ProjectileSpeed) {
			return currentVal + (2.0 * multiplier);
		}
		if (upgrade == WeaponUpgrades.AttackDamage) {
			return currentVal + (damageStep * multiplier);
		}
		if (upgrade == WeaponUpgrades.AttackSpeed) {
			double reduction = 0.03;
			if (rarity == WeaponRarity.SILVER) {
				reduction = 0.05;
			} else if (rarity == WeaponRarity.GOLD) {
				reduction = 0.08;
			} else if (rarity == WeaponRarity.DIAMOND) {
				reduction = 0.12;
			}
			return currentVal * (1.0 - reduction);
		}
		if (upgrade == WeaponUpgrades.AttackSize) {
			double gain = 0.05;
			if (rarity == WeaponRarity.SILVER) {
				gain = 0.10;
			} else if (rarity == WeaponRarity.GOLD) {
				gain = 0.15;
			} else if (rarity == WeaponRarity.DIAMOND) {
				gain = 0.25;
			}
			return currentVal + gain;
		}
		if (upgrade == WeaponUpgrades.CriticalChance) {
			double gain = 0.03;
			if (rarity == WeaponRarity.SILVER) {
				gain = 0.05;
			} else if (rarity == WeaponRarity.GOLD) {
				gain = 0.07;
			} else if (rarity == WeaponRarity.DIAMOND) {
				gain = 0.10;
			}
			return currentVal + gain;
		}
		if (upgrade == WeaponUpgrades.CriticalDamage) {
			double gain = 0.10;
			if (rarity == WeaponRarity.SILVER) {
				gain = 0.20;
			} else if (rarity == WeaponRarity.GOLD) {
				gain = 0.35;
			} else if (rarity == WeaponRarity.DIAMOND) {
				gain = 0.50;
			}
			return currentVal + gain;
		}

		return currentVal;
	}

	private WeaponUpgrades getRandomStatForWeapon(Weapon w) {
		List<WeaponUpgrades> validStats = new ArrayList<>();
		for (WeaponUpgrades upgrade : WeaponUpgrades.values()) {
			if (w.getStats().containsKey(upgrade) && w.getStats().get(upgrade) > 0) {
				if (upgrade != WeaponUpgrades.Range) {
					validStats.add(upgrade);
				}
			}
		}
		if (validStats.isEmpty()) {
			return WeaponUpgrades.AttackDamage;
		}
		return validStats.get(rand.nextInt(validStats.size()));
	}

	private void startLevelUpEffect() {
		particles.clear();
		flashAlpha = 1.0f;
		for (int i = 0; i < 60; i++) {
			particles.add(new PixelParticle(AppPanel.WIDTH / 2, AppPanel.HEIGHT / 2, rand));
		}
	}

	public void update() {
		timer++;
		if (flashAlpha > 0) {
			flashAlpha -= 0.05f;
		}
		for (int i = particles.size() - 1; i >= 0; i--) {
			particles.get(i).update();
			if (particles.get(i).life <= 0) {
				particles.remove(i);
			}
		}
		for (GameButton b : boxes) {
			if (b != null) {
				b.update();
			}
		}
	}

	private void finishUpgrade() {
		gameObj.setState(gameObj.getStateOpen());
	}

	private static class PixelParticle {
		float x, y, vx, vy;
		int size, life;
		Color color;

		public PixelParticle(int startX, int startY, Random rand) {
			x = startX;
			y = startY;
			vx = (rand.nextFloat() - 0.5f) * 15f;
			vy = (rand.nextFloat() - 0.5f) * 15f;
			size = rand.nextInt(6) + 4;
			life = 30 + rand.nextInt(30);
			if (rand.nextBoolean()) {
				color = Color.YELLOW;
			} else {
				color = Color.WHITE;
			}
		}

		public void update() {
			x += vx;
			y += vy;
			vy += 0.3f;
			life--;
		}
	}
}