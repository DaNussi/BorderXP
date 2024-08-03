package net.nussi.borderXP.util;

public class LevelResult {
    private int level;
    private double progress;

    public LevelResult(int level, double progress) {
        this.level = level;
        this.progress = progress;
    }

    public int getLevel() {
        return level;
    }

    public double getProgress() {
        return progress;
    }
}
