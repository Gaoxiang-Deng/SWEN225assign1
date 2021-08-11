package GUI;

import javax.swing.*;

import board.Board.Direction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftCanvas extends Observer implements ActionListener {

    JTextArea jta;

    public LeftCanvas(Subject subject) {
        super();
        this.subject = subject;
        this.subject.attach(this);
        panelSetup();
    }

    private void panelSetup() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addButton("Guess");
        addButton("Solve");
        jta = new JTextArea();
        add(jta);
    }

    private void addButton(String name) {
        JButton b = new JButton(name);
        b.setActionCommand(name);
        b.addActionListener(this);
        add(b);
    }

    @Override
    public void update() {
        jta.setText(subject.getInfo());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Guess"))
            subject.guess();
        else if(e.getActionCommand().equals("Solve"))
            subject.solve();
        
    }
}
