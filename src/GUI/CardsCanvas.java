package GUI;

import javax.swing.event.MouseInputListener;

import cards.Card;
import game.Player;

import java.awt.*;
import java.awt.event.MouseEvent;

public class CardsCanvas extends Observer implements MouseInputListener {

    Player player;
    int cardTopLeftY;
    int cardWidth;
    int cardHeight;
    int padding;

    public CardsCanvas(Subject subject){
        super();
        player = new Player();
        this.subject = subject;
        this.subject.attach(this);
        addMouseListener(this);
        
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);

        updateSizes();

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

    private void updateSizes() {
        cardTopLeftY = getHeight() / 8;
        cardWidth = getWidth() / 4;
        cardHeight = getHeight() - getHeight() / 6;
        padding = 10;
    }

    @Override
    public void update() {
        if(subject.getPlayer() != null){
            player = subject.getPlayer();
        }
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int index;
        index = e.getX() / (cardWidth + padding);
        System.out.print(index);

        if(e.getX() < (cardWidth + padding) * player.getHand().size()){
            subject.setRevealedCard(player.getHand().get(index));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
    }
}
