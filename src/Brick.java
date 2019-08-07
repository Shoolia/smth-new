import java.awt.*;


public class Brick extends BFObject implements Destroyable {

    private boolean destroyed;

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void destroy() {
        destroyed = true;
    }

    @Override
    public void draw(Graphics g) {
        Color color = !destroyed ? new Color(0, 0, 255) : new Color(220, 220, 255);
        g.setColor(color);
        g.fillRect(getX(), getY(), 64, 64);
    }

}
