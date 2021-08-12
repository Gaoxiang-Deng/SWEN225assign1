package GUI;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import cards.Card;
import cards.CharacterCard;
import cards.WeaponCard;

import java.util.List;

public class RightCanvas extends Observer implements MouseListener{

    WeaponCard weapon;
    CharacterCard character;
    List<List<? extends Card>> deck;
    int cardSpacing = 23;
    int typeSpacing = cardSpacing*5;

    public RightCanvas(Subject subject){
        super();
        this.subject = subject;
        this.subject.attach(this);
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        deck = subject.getDeck();
        super.paintComponent(g);
        this.setBackground(Color.lightGray);
        g.setColor(Color.black);

        String[] names = {"Weapons", "Characters", "Locations"};
        for(int j = 0; j < names.length; j++){
            // g.setFont(new Font("TimesRoman", Font.BOLD, 20));
            // g.drawString(names[j]+":", 20, j * typeSpacing + 20);
            for(int i = 0; i < deck.get(j).size(); i++){
                g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
                g.drawString(deck.get(j).get(i).getValue(), 20, i * cardSpacing + j * typeSpacing + 20);
                
            }
        }
    }

    @Override
    public void update() {
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        List<List<? extends Card>> deck = subject.getDeck();
        
        int index = e.getY() / cardSpacing;
        int list;
        int card;

        if(index < deck.get(0).size()) {
            list = 0;
            card = index - list;
        }
        else if(index < deck.get(0).size() + deck.get(1).size()) {
            list = 1;
            card = index - deck.get(0).size();
        }
        else return;
        
        if(list == 0) {
            weapon = (WeaponCard) deck.get(list).get(card);
            subject.addToGuess(weapon);
        }
        else if(list == 1) {
            character = (CharacterCard) deck.get(list).get(card);
            subject.addToGuess(character);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
}