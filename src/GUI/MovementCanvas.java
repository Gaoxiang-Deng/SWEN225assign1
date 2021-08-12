package GUI;

import javax.swing.*;

import board.Board.Direction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovementCanvas extends Observer implements ActionListener {

    public MovementCanvas(Subject subject){
        super();
        this.subject = subject;
        this.subject.attach(this);
        panelSetup();
    }

    private void panelSetup(){
        setLayout(new GridBagLayout());

        addButton("up", 1, 0);
        addButton("down", 1, 2);
        addButton("left", 0, 1);
        addButton("right", 2, 1);
    }

    private void addButton(String name, int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        JButton b = new JButton(name);
        b.setActionCommand(name);
        b.addActionListener(this);
        add(b, c);
    }




    @Override
    public void update() {
        //subject.getState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("up")) subject.moveCharacter(Direction.UP);
        else if (e.getActionCommand().equals("right")) subject.moveCharacter(Direction.RIGHT);
        else if (e.getActionCommand().equals("down")) subject.moveCharacter(Direction.DOWN);
        else if (e.getActionCommand().equals("left")) subject.moveCharacter(Direction.LEFT);
        else throw new IllegalArgumentException("Invalid direction");
    }
}
