public class Player extends GameObject {
    private char lastDirection = 'd';

    public Player(int x, int y) {
        super(x, y);
    }

    @Override
    public void move() {
        switch (lastDirection) {
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

    public void setLastDirection(char direction) {
        this.lastDirection = direction;
    }

    public Bullet shoot() {
        return new Bullet(x, y, lastDirection);
    }

    public char getLastDirection() {
        return lastDirection;
    }
}
