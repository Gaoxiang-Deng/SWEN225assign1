import javax.swing.*;
import java.awt.*;

public class MovementCanvas extends JPanel {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.orange);

        g.setColor(Color.green);
        g.drawString("This is where the movement buttons will go!", 20, 20);
    }
}
