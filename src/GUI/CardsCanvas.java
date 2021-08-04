package GUI;

import javax.swing.*;
import java.awt.*;

public class CardsCanvas extends Observer {

    public CardsCanvas(Subject subject){
        super();
        this.subject = subject;
        this.subject.attach(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.MAGENTA);

        g.setColor(Color.black);
        g.drawString("This is where player's hand will go!", 20, 20);
    }

    @Override
    public void update() {
        //subject.getState();
    }
}
