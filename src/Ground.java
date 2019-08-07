import java.awt.*;


public class Ground extends BFObject {

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(220, 220, 255));
        g.fillRect(getX(), getY(), 64, 64);
    }

}
