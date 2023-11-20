public class Bullet extends GameObject {
    private char direction;

    public Bullet(int x, int y, char direction) {
        super(x, y);
        this.direction = direction;
    }

    @Override
    public void move() {
        switch (direction) {
            case 'w':
                y -= 1;
                break;
            case 's':
                y += 1;
                break;
            case 'a':
                x -= 1;
                break;
            case 'd':
                x += 1;
                break;
        }
    }

    public boolean isValid(int mapHeight, int mapWidth) {
        return x > 0 && x < mapWidth - 1 && y > 0 && y < mapHeight - 1;
    }
}
