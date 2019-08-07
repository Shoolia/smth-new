import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.*;


public class ActionField extends JPanel {

    final boolean COLORDED_MODE = true;

    final int BF_WIDTH = 576;
    final int BF_HEIGHT = 576;

    private Tank tank;
    private Tank tank2;


    BFObject[][] battleField = {
            {new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground()},
            {new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground()},
            {new Brick(), new Brick(), new Brick(),new Brick(),new Brick(),new Brick(), new Brick(), new Brick(), new Brick()},
            {new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground()},
            {new Brick(), new Ground(), new Brick(), new Ground(), new Brick(), new Ground(),  new Brick(), new Ground(), new Brick()},
            {new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground()},
            {new Brick(), new Brick(), new Brick(), new Brick(), new Brick(),new Brick(), new Brick(), new Brick(), new Brick()},
            {new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground()},
            {new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground(), new Ground()}
    };


    void runTheGame() throws Exception {

        Thread threadDestroyed = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!tank.isDestroyed() || !tank2.isDestroyed()) {
                repaint();
            }}
        });


        Thread threadKill = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    tank2.kill(tank);

                } catch (Exception e) {

                }
            }
        });
        threadDestroyed.start();
        threadKill.start();


    }

    boolean processInterception(int x, int y) {

        int[] cordinates = getQuadrant(x, y);
        int fieldX = cordinates[1];
        int fieldY = cordinates[0];

        if (fieldX < 9 && fieldX >= 0 && fieldY >= 0 && fieldY < 9) {
            BFObject object = battleField[fieldY][fieldX];
            if (object.getClass() == Brick.class &&
                    !((Destroyable)object).isDestroyed()) {
                Destroyable destroyable = (Destroyable) battleField[fieldY][fieldX];
                destroyable.destroy();
                return true;
            }
        }
        return false;
    }
    public boolean interceptionTank(Bullet bullet, Tank tank){
        int bulletQuadrantX ;
        int bulletQuadrantY;
        int tankQuadrantX;
        int tankQuadrantY;

         int[] bulletQuadrant = getQuadrant(bullet.getX(), bullet.getY());
         int[] tankQuadrant=getQuadrant(tank.getX(),tank.getY());
         bulletQuadrantX=bulletQuadrant[1];
         bulletQuadrantY=bulletQuadrant[0];
         tankQuadrantX=tankQuadrant[1];
         tankQuadrantY=tankQuadrant[0];
         if(tank.isDestroyed()==false&&bulletQuadrantX==tankQuadrantX&&bulletQuadrantY==tankQuadrantY){
             return true;

         }

         return false;
    }

    public static int[] getQuadrant(int x, int y) {
        int vert = y/64, gor = x/64;
        return new int[] { vert , gor };
    }


    public static int[] getQuadrantXY(int v, int h) {
        int vertical = (v-1)*64;
        int gorisontal = (h-1)*64;

        return new int[] {vertical, gorisontal};
    }


    public static void main(String[] args) throws Exception {
        ActionField bf = new ActionField();
        bf.runTheGame();
    }


    public ActionField()  {
        JFrame frame = new JFrame("BATTLE FIELD, DAY X");
        frame.setLocation(750, 150);
        frame.setMinimumSize(new Dimension(BF_WIDTH, BF_HEIGHT + 22));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
        frame.addKeyListener(new KeyListener() {
            Thread threadStart;
            Thread fireThread;

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if ( threadStart == null){
            threadStart = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (tank){
                    switch (e.getKeyCode()){
                        case KeyEvent.VK_UP:
                            try {
                                tank.move(Direction.UP);
                            } catch (Exception e1){
                                e1.printStackTrace();
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            try {
                                tank.move(Direction.DOWN);
                            } catch (Exception e1){
                                e1.printStackTrace();
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            try {
                                tank.move(Direction.RIGHT);
                            } catch (Exception e1){
                                e1.printStackTrace();
                            }
                            break;
                        case KeyEvent.VK_LEFT:
                            try {
                                tank.move(Direction.LEFT);
                            } catch (Exception e1){
                                e1.printStackTrace();
                            }
                            break;

                    }
                    threadStart = null;
                }}
            });
            threadStart.start();
            }}

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && fireThread == null){
                    fireThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tank.fire(tank2);
                            } catch (Exception e1) {
                            e1.printStackTrace();
                            }
                            fireThread = null;
                        }
                    });
                    fireThread.start();
                }

            }
        });

        tank = new Tank(448, 448, Direction.LEFT, this);
        tank2= new Tank(128,64,Direction.RIGHT,this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        for (int j = 0; j < battleField.length; j++) {
            for (int k = 0; k < battleField.length; k++) {
                    BFObject object = battleField[j][k];
                    int[] coordinates = getQuadrantXY(j + 1, k + 1);
                    int y = coordinates[0];
                    int x = coordinates[1];
                    object.setY(y);
                    object.setX(x);
                    object.draw(g);
            }
        }

        tank.draw(g);
        tank2.draw(g);

    }

}
