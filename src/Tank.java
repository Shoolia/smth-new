import java.awt.*;


public class Tank implements Drawable,Destroyable{

    private int x;
    private int y;
    private int speed;
    private Direction direction;
    private Bullet bullet;
    private ActionField af;
    private boolean destroyed;

    private Thread moveThread;
    private Thread fireThread;

    public Tank(int x, int y, Direction direction, ActionField af) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.af = af;
        speed = 10;
        bullet = new Bullet(-100, -100);
    }

    public void moveRandom() throws Exception {

    }

    public void kill(Tank tank) throws Exception {
        while (!this.destroyed && !tank.destroyed) {

            if (this.getX() == tank.getX()) {
                if (this.getY() > tank.getY()) {
                    this.turn(Direction.UP);


                } else {
                    this.turn(Direction.DOWN);

                }

                this.fire(tank);
            }
            if (this.getY() == tank.getY()) {
                if (this.getX() > tank.getX()) {
                    this.turn(Direction.LEFT);
                }
                if (this.getX() > tank.getX()) {
                    this.turn(Direction.RIGHT);

                }
                this.fire(tank);

            }
            if (this.getX() > tank.getX()) {
                //  while (this.getX()!=tank.getX()){
                this.move(Direction.LEFT);
                //  }
            } else {
                // while (this.getX() != tank.getX()) {
                this.move(Direction.RIGHT);
                //  }
            }


            }
        }



    public void move(Direction direction) throws Exception {


        int cover = 0;
        turn(direction);

        if ((y == 0 && direction == Direction.UP) || (y >= 512 && direction == Direction.DOWN)
                || (x == 0 && direction == Direction.LEFT) || (x >= 512 && direction == Direction.RIGHT)) {
            System.out.println("I can't go");
            return;
        }
        if (direction == Direction.LEFT && af.battleField[y/64][x/64-1].getClass() == Brick.class
                && !((Brick)af.battleField[y/64][x/64-1]).isDestroyed()) {
            System.out.println("Next Brick! I can't go!!!");
        return;
        }
        if (direction == Direction.RIGHT && af.battleField[y/64][x/64+1].getClass() == Brick.class
                && !((Brick)af.battleField[y/64][x/64+1]).isDestroyed()) {
            System.out.println("Next Brick! I can't go!!!");
            return;
        }
        if (direction == Direction.UP && af.battleField[y/64-1][x/64].getClass() == Brick.class
                && !((Brick)af.battleField[y/64-1][x/64]).isDestroyed()) {
            System.out.println("Next Brick! I can't go!!!");
            return;
        }
        if (direction == Direction.DOWN && af.battleField[y/64+1][x/64].getClass() == Brick.class
                && !((Brick)af.battleField[y/64+1][x/64]).isDestroyed()) {
            System.out.println("Next Brick! I can't go!!!");
            return;
        }

        while (cover < 64) {
            if (direction == Direction.UP) {
                y -= 1;
                System.out.println(direction + "tankX:" + x + "tankY:" + y);
            } else if (direction == Direction.DOWN) {
                y += 1;
                System.out.println(direction + "tankX:" + x + "tankY:" + y);
            } else if (direction == Direction.LEFT) {
                x -= 1;
                System.out.println(direction + "tankX:" + x + "tankY:" + y);
            } else {
                x += 1;
                System.out.println(direction + "tankX:" + x + "tankY:" + y);
            }

            cover++;
            af.repaint();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public void turn(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
        }
        af.repaint();
    }

    public void moveToQuadrant(int v, int h) throws Exception {
        int[] cordinates = ActionField.getQuadrantXY(v, h);
        int y = cordinates[0];
        int x = cordinates[1];

        if (this.x < x) {
            while (this.x != x) {
                move(Direction.RIGHT);

            }
        }
        else {
            while (this.x != x) {
                move(Direction.LEFT);

            }
        }
        if (this.y < y) {
            while (this.y != y) {
                move(Direction.DOWN);

            }
        }
        else  {
            while (this.y != y) {
                move(Direction.UP);
            }
        }



    }

    public void fire(Tank tank) throws Exception {

                    bullet.setY(y + 25);
                    bullet.setX(x + 25);

                    bullet.setDirection(this.getDirection());

                    while (!bullet.isDestroyed()) {

                        if (bullet.getDirection() == Direction.UP) {
                            bullet.updateY(-1);
                            ;
                        } else if (bullet.getDirection() == Direction.DOWN) {
                            bullet.updateY(1);
                        } else if (bullet.getDirection() == Direction.LEFT) {
                            bullet.updateX(-1);
                        } else if (bullet.getDirection() == Direction.RIGHT) {
                            bullet.updateX(1);
                        }

                        if (af.processInterception(bullet.getX(), bullet.getY())) {
                            bullet.destroy();
                        }
                        if (af.interceptionTank(bullet, tank)) {
                            tank.destroy();
                            bullet.destroy();
                        }


                        af.repaint();
                        try {
                            Thread.sleep(bullet.getSpeed());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    bullet.destroy();


    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void draw(Graphics g) {
        if (!this.destroyed) {
            g.setColor(new Color(255, 0, 0));
            g.fillRect(x, y, 64, 64);

            g.setColor(new Color(0, 255, 0));
            if (direction == Direction.UP) {
                g.fillRect(x + 20, y, 24, 34);
            } else if (direction == Direction.DOWN) {
                g.fillRect(x + 20, y + 30, 24, 34);
            } else if (direction == Direction.LEFT) {
                g.fillRect(x, y + 20, 34, 24);
            } else {
                g.fillRect(x + 30, y + 20, 34, 24);
            }
        }

            bullet.draw(g);
        }


    @Override
    public void destroy() {
        destroyed = true;

    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

}
