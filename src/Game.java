import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private boolean running = true;
    private boolean playerAlive = true;
    private boolean gameWon = false;
    private char[][] map;
    private Player player = new Player(3, 5);
    private List<Bullet> bullets = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private Random random = new Random();

    private boolean evilOttoActive = false;
    private EvilOtto evilOtto;

    private int inputCounter;


    public Game() {
        setMapLayout();
        enemies.add(new Enemy(10, 10));
        enemies.add(new Enemy(20, 3));
        enemies.add(new Enemy(35, 2));
        enemies.add(new Enemy(37, 10));
        inputCounter = 0;
    }

    void setMapLayout (){
        map  = new char[][]{
                "############################################".toCharArray(),
                "#                             #            #".toCharArray(),
                "#                             #            #".toCharArray(),
                "#                             #            #".toCharArray(),
                "#          #                  #            #".toCharArray(),
                "#          #                               |".toCharArray(),
                "#          #                               |".toCharArray(),
                "#          #                               |".toCharArray(),
                "#          #                               |".toCharArray(),
                "#          #                  #            #".toCharArray(),
                "#          #                  #            #".toCharArray(),
                "#          #                  #            #".toCharArray(),
                "############################################".toCharArray(),
        };
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (running && playerAlive) {
            printMap();
            System.out.print("Enter command (w/a/s/d to move, q to quit): ");
            String input = scanner.nextLine();

            if (evilOttoActive) {
                evilOtto.chasePlayer(player);
            }

            if ("q".equalsIgnoreCase(input)) {
                running = false;
            } else {
                handlePlayerInput(input);
            }

            updateBullets();
            updateEnemies();
            checkCollisions();
        }

        if (!playerAlive) {
            printDeathMap();
            System.out.println("Game Over! You died.");
        } else if (gameWon) {
            System.out.println("Game Over! You won!");
        }

        scanner.close();
    }

    private void printMap() {
        setMapLayout();
        map[player.getY()][player.getX()] = 'A';


        if (evilOttoActive) {
            map[evilOtto.getY()][evilOtto.getX()] = '☺';
        }

        for (Enemy enemy : enemies) {
            map[enemy.getY()][enemy.getX()] = 'E';
        }

        for (Bullet bullet : bullets) {
            if (bullet.isValid(map.length, map[0].length)) {
                map[bullet.getY()][bullet.getX()] = '*';
            }
        }

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                System.out.print(map[y][x]);
            }
            System.out.println();
        }
    }

    private void printDeathMap() {
        setMapLayout();


        for (Enemy enemy : enemies) {
            map[enemy.getY()][enemy.getX()] = 'E';
        }

        for (Bullet bullet : bullets) {
            if (bullet.isValid(map.length, map[0].length)) {
                map[bullet.getY()][bullet.getX()] = '*';
            }
        }

        map[player.getY()][player.getX()] = 'X';

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                System.out.print(map[y][x]);
            }
            System.out.println();
        }
    }




    public void handlePlayerInput(String input) {
        switch (input.toLowerCase()) {
            case "w":
            case "s":
            case "a":
            case "d":
                player.setLastDirection(input.charAt(0));
                checkAndMovePlayer();
                break;
            case "e":
                bullets.add(player.shoot());
                break;
            case "":
                break;
            default:
                System.out.println("Incorrect input!");
        }

        inputCounter++;


        if (inputCounter == 25) {
            evilOtto = new EvilOtto(25, 5);
            evilOttoActive = true;
        }
    }


    private void checkAndMovePlayer() {
        int dx = 0, dy = 0;
        char lastDirection = player.getLastDirection();

        switch (lastDirection) {
            case 'w': dy = -1; break;
            case 's': dy = 1; break;
            case 'a': dx = -1; break;
            case 'd': dx = 1; break;
        }

        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        //System.out.print("Test " + newX + " " + newY + " " + map[newY][newX] + "\n");
        if (newX > 0 && newX < map[0].length - 1 && newY > 0 && newY < map.length - 1) {
            if (map[newY][newX] != '#') {
                player.move();
            } else {
                playerAlive = false;
            }

        }
        else {
            if (map[newY][newX] == '|') {
                gameWon = true;
                running = false;
            } else {
                playerAlive = false;
            }
        }
    }

    private void updateBullets() {
        List<Bullet> toRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            bullet.move();

            if (!bullet.isValid(map.length, map[0].length) || isWall(bullet.getX(), bullet.getY())) {
                toRemove.add(bullet);
            } else {
                if (hitEnemy(bullet)) {
                    toRemove.add(bullet);
                }
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            for (int j = i + 1; j < bullets.size(); j++) {
                Bullet b1 = bullets.get(i);
                Bullet b2 = bullets.get(j);

                if (b1.getX() == b2.getX() && b1.getY() == b2.getY()) {
                    toRemove.add(b1);
                    toRemove.add(b2);
                }
            }
        }
        
        bullets.removeAll(toRemove);
    }

    private boolean hitEnemy(Bullet bullet) {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (bullet.getX() == enemy.getX() && bullet.getY() == enemy.getY()) {
                enemyIterator.remove();
                return true;
            }
        }
        return false;
    }

    private void updateEnemies() {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.move();

            if (isWall(enemy.getX(), enemy.getY())) {
                enemyIterator.remove();
            } else if (random.nextBoolean()) {
                Bullet bullet = enemy.shoot(map);
                if (bullet != null) {
                    bullets.add(bullet);
                }
            }
        }
    }

    private void checkCollisions() {
        if (evilOttoActive && player.getX() == evilOtto.getX() && player.getY() == evilOtto.getY()) {
            playerAlive = false;
        }

        for (Enemy enemy : enemies) {
            if (player.getX() == enemy.getX() && player.getY() == enemy.getY()) {
                playerAlive = false;
            }
        }

        for (Bullet bullet : bullets) {
            if (bullet.getX() == player.getX() && bullet.getY() == player.getY()) {
                playerAlive = false;
            }
        }

    }

    private boolean isWall(int x, int y) {
        return map[y][x] == '#';
    }


}
