import javax.swing.*;
import java.awt.*;

public class RightCanvas extends JPanel {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.blue);

        g.setColor(Color.white);
        g.drawString("This is where the list of cards will go!", 20, 20);
    }
}
