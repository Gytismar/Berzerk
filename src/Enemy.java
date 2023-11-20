import java.util.Random;

public class Enemy extends GameObject {
    private char lastDirection = 'd';
    private Random random = new Random();

    public Enemy(int x, int y) {
        super(x, y);
    }

    @Override
    public void move() {
        int newX = x, newY = y;
        switch (random.nextInt(4)) {
            case 0: newY--; lastDirection = 'w'; break;
            case 1: newY++; lastDirection = 's'; break;
            case 2: newX--; lastDirection = 'a'; break;
            case 3: newX++; lastDirection = 'd'; break;
        }
        x = newX;
        y = newY;
    }

    public Bullet shoot(char[][] map) {
        int bulletX = x;
        int bulletY = y;
        switch (lastDirection) {
            case 'w': bulletY--; break;
            case 's': bulletY++; break;
            case 'a': bulletX--; break;
            case 'd': bulletX++; break;
        }

        if (bulletX < 0 || bulletX >= map[0].length || bulletY < 0 || bulletY >= map.length || map[bulletY][bulletX] == '#') {
            return null;
        }

        return new Bullet(bulletX, bulletY, lastDirection);
    }
}
