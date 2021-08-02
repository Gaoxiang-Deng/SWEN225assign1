import javax.swing.*;
import java.awt.*;

public class CardsCanvas extends JPanel {

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.MAGENTA);

        g.setColor(Color.black);
        g.drawString("This is where player's hand will go!", 20, 20);
    }
}
