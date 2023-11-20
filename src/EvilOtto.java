public class EvilOtto extends GameObject {
    public EvilOtto(int x, int y) {
        super(x, y);
    }

    @Override
    public void move() {

    }


    public void chasePlayer(Player player) {
        int playerX = player.getX();
        int playerY = player.getY();

        if (this.x < playerX) {
            this.x++;
        } else if (this.x > playerX) {
            this.x--;
        }

        if (this.y < playerY) {
            this.y++;
        } else if (this.y > playerY) {
            this.y--;
        }
    }
}
