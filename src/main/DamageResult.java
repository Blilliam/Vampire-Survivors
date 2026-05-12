package main;

public class DamageResult {
    public final int damage;
    public final boolean isCrit;

    public DamageResult(int damage, boolean isCrit) {
        this.damage = damage;
        this.isCrit = isCrit;
    }
}