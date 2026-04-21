package Open.Upgrades;

import java.awt.Color;
import java.awt.Graphics2D;

import Open.Weapons.Weapon;
import main.GameObject;
import main.MouseInput;
import main.enums.WeaponRarity;

public class UpgradeBox {

    private int type;
    private final GameObject gameObj;

    public int x, y, w, h;

    // Animation variables
    private float animationProgress = 1f; // 1 = fully visible, 0 = invisible
    private int startY = 0;                // starting Y for slide-in
    
    WeaponRarity rarity;
    Weapon weapon;

    public UpgradeBox(GameObject gameObj, WeaponRarity rarity, Weapon weapon, int x, int y) {
        this.gameObj = gameObj;
        this.type = type;
        this.x = x;
        this.y = y;
        this.startY = y - 50; // start slightly above
        this.animationProgress = 1f;
        this.weapon = weapon;
        this.rarity = rarity;
        
    }
}
