public class Level {
    private int levelNumber;
    private int[][] level;
    private int levelRemaining;
    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        if (levelNumber == 1) {
            level = new int[][]{
                    {1, 3, 1, 1, 1, 1, 1, 1, 3, 1},
                    {1, 0, 0, 0, 1, 1, 0, 0, 0, 1},
                    {1, 1, 1, 4, 0, 0, 4, 1, 1, 1},
                    {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };
            levelRemaining = 36;
        }
        if (levelNumber == 2) {
            level = new int[][]{
                    {1, 4, 1, 1, 0, 0, 1, 1, 4, 1},
                    {0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
                    {1, 1, 1, 3, 0, 0, 3, 1, 1, 1},
                    {0, 0, 0, 1, 0, 0, 1, 0, 0, 1},
                    {2, 2, 1, 1, 0, 0, 1, 1, 2, 2}
            };
            levelRemaining = 29;
        }
        if (levelNumber == 3) {
            level = new int[][]{
                    {1, 1, 3, 0, 1, 0, 3, 0, 0, 1},
                    {1, 0, 0, 0, 1, 0, 1, 0, 0, 1},
                    {1, 1, 1, 0, 4, 0, 1, 0, 0, 1},
                    {0, 0, 1, 0, 1, 0, 1, 0, 0, 1},
                    {1, 2, 1, 0, 2, 0, 2, 1, 2, 1}
            };
            levelRemaining = 28;
        }
        if (levelNumber == 4) {
            level = new int[][]{
                    {0, 1, 0, 0, 0, 1, 1, 1, 0, 0},
                    {1, 4, 0, 0, 0, 1, 0, 1, 0, 0},
                    {0, 1, 0, 0, 0, 3, 1, 1, 0, 0},
                    {0, 1, 0, 0, 0, 1, 0, 1, 0, 0},
                    {2, 2, 1, 0, 0, 1, 1, 2, 0, 0}
            };
            levelRemaining = 21;
        }
    }
    public int[][]  getLevel() {
        return level;
    }
    public int getLevelNumber() {
        return levelNumber;
    }
    public int getLevelRemaining() {
        return levelRemaining;
    }
}