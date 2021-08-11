package GUI;

import javax.swing.*;

import cards.Card;
import game.Player;

import java.awt.*;

public class CardsCanvas extends Observer {

    Player player;

    public CardsCanvas(Subject subject){
        super();
        player = new Player();
        this.subject = subject;
        this.subject.attach(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);

        int cardTopLeftY = getHeight() / 8;
        int cardWidth = getWidth() / 4;
        int cardHeight = getHeight() - getHeight() / 6;
        int padding = 10;
        int i = 0;
        for(Card c : player.getHand()){
            g.translate(i * cardWidth + i * padding, cardTopLeftY);
            g.setColor(Color.black);
            g.drawRect(0, 0, cardWidth, cardHeight);
            g.setColor(Color.red);
            g.drawString(c.getValue(), 20, 20);
            g.translate(-i * cardWidth - i * padding, -cardTopLeftY);
            i++;
        }
    }

    @Override
    public void update() {
        if(subject.getPlayer() != null){
            player = subject.getPlayer();
        }
        repaint();
    }
}
