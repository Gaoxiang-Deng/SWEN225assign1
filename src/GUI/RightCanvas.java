package GUI;

import java.awt.*;

public class RightCanvas extends Observer {

    Object data;

    public RightCanvas(Subject subject){
        super();
        this.subject = subject;
        this.subject.attach(this);
        data = "";
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.blue);
        g.setColor(Color.white);
        g.drawString(data.toString(), 20, 20);
        System.out.println("redrawing canvas");
    }

    @Override
    public void update() {
        data = subject.getInfo();
        repaint();
    }
}
