package GUI;

import javax.swing.*;
import java.awt.*;

public class MovementCanvas extends Observer{

    public MovementCanvas(Subject subject){
        super();
        this.subject = subject;
        this.subject.attach(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.orange);

        g.setColor(Color.green);
        g.drawString("This is where the movement buttons will go!", 20, 20);
    }


    @Override
    public void update() {
        //subject.getState();
    }
}
