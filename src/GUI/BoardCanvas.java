import javax.swing.*;
import java.awt.*;


public class BoardCanvas extends JPanel{
    
    
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.setBackground(Color.black);

        g.setColor(Color.white);
        g.drawString("This is where the\n board will go!", 20, 20);
    }
}
