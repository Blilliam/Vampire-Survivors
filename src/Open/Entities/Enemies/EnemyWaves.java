package Open.Entities.Enemies;

import main.AppPanel;
import main.GameObject;
import java.util.ArrayList;
import java.util.List;

public class EnemyWaves {

    private GameObject gameObj;
    private double credits;
    private double creditGainRate;
    private int tickCounter;

    // A "Spawn Card" defines an enemy's economy stats
    private class SpawnCard {
        int id;
        int cost;   // Credit cost per unit
        int weight; // Probability of being chosen
        int maxGroup; // Max size for one pack

        SpawnCard(int id, int cost, int weight, int maxGroup) {
            this.id = id;
            this.cost = cost;
            this.weight = weight;
            this.maxGroup = maxGroup;
        }
    }

    private List<SpawnCard> pool = new ArrayList<>();

    public EnemyWaves(GameObject gameObj) {
        this.gameObj = gameObj;
        this.credits = 0;
        this.creditGainRate = 0.15; // Starting speed
        this.tickCounter = 0;

        // ID: 1:Zombie, 2:Skeleton, 3:Mudman, 4:Bat, 5:Glowing Bat
        pool.add(new SpawnCard(1, 10, 100, 8)); // Common horde
        pool.add(new SpawnCard(2, 15, 80, 5));  // Slower, tougher horde
        pool.add(new SpawnCard(4, 8, 90, 10));  // Fast swarm
        
        // Elite/Rare Roster
        pool.add(new SpawnCard(3, 50, 15, 2));  // Mudman: Rare, small groups
        pool.add(new SpawnCard(5, 75, 8, 1));   // Glowing Bat: Very Rare, solo
    }

    public double getDifficultyMult() {
        // Increases by 10% every 30 seconds (1800 ticks)
        return 1.0 + (tickCounter / 1800.0) * 0.1;
    }

    public void update() {
        if (gameObj.getState() != gameObj.getStateOpen()) return;

        tickCounter++;
        credits += creditGainRate;

        // RoR2 Difficulty Scaling: Increase gain rate over time
        if (tickCounter % 600 == 0) { // Every 10 seconds
            creditGainRate += 0.02;
        }

        // Try to spawn if the director has enough for the cheapest unit (Bat: 8)
        if (credits >= 8) {
            attemptSpawn();
        }
    }

    private void attemptSpawn() {
        List<SpawnCard> affordable = new ArrayList<>();
        for (SpawnCard sc : pool) {
            if (credits >= sc.cost) affordable.add(sc);
        }

        if (affordable.isEmpty()) return;

        // Weighted Selection
        int totalWeight = 0;
        for (SpawnCard sc : affordable) totalWeight += sc.weight;
        int roll = (int) (Math.random() * totalWeight);
        int cursor = 0;
        SpawnCard selected = affordable.get(0);

        for (SpawnCard sc : affordable) {
            cursor += sc.weight;
            if (roll < cursor) {
                selected = sc;
                break;
            }
        }

        // Spend Budget on a Group
        // Director spends up to 50% of current credits on this specific pack
        double spendLimit = Math.max(selected.cost, credits * 0.5);
        int spawnedCount = 0;
        
        // Central point for the pack
        double spawnAngle = Math.random() * Math.PI * 2;
        double spawnDist = 500 + (Math.random() * 200);
        int centerX = (int) (gameObj.getPlayer().getX() + Math.cos(spawnAngle) * spawnDist);
        int centerY = (int) (gameObj.getPlayer().getY() + Math.sin(spawnAngle) * spawnDist);

        while (credits >= selected.cost && spendLimit >= selected.cost && spawnedCount < selected.maxGroup) {
            // Add jitter so they aren't on the exact same pixel
            int jX = centerX + (int)((Math.random() - 0.5) * 60);
            int jY = centerY + (int)((Math.random() - 0.5) * 60);
            
            // Clamp to map
            jX = Math.max(0, Math.min(jX, gameObj.getMap().WIDTH - 1));
            jY = Math.max(0, Math.min(jY, gameObj.getMap().HEIGHT - 1));

            gameObj.addEnemy(new Enemy(gameObj, jX, jY, selected.id, getDifficultyMult()));
            
            credits -= selected.cost;
            spendLimit -= selected.cost;
            spawnedCount++;
        }
    }
}