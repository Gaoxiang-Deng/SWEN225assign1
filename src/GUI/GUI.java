
package GUI;
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

import game.Player;

public class GUI {

    static Subject subject;
    public static final int width = 1150;
    public static final int height = 900;

    public GUI(Subject subject){
        GUI.subject = subject;
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void addComponentsToPane(Container pane) {

        JMenu menu;
        JMenuBar menuBar = new JMenuBar();

        pane.setLayout(new GridBagLayout());


        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        menu = new JMenu("Menu");
        JMenuItem quitButton = new JMenuItem("Quit");
        quitButton.setToolTipText("Quit application");
        quitButton.addActionListener((event) -> System.exit(0));
        menu.add(quitButton);
        menuBar.add(menu);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(menuBar, c);


        c = new GridBagConstraints();   
        BoardCanvas board = new BoardCanvas(subject);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 2; 
        pane.add(board, c);

        c = new GridBagConstraints();
        RightCanvas rightPane = new RightCanvas(subject);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.4;
        c.weighty = 0.4;
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 2;
        pane.add(rightPane, c);

        c = new GridBagConstraints();
        LeftCanvas leftCanvas = new LeftCanvas(subject);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 2;
        pane.add(leftCanvas, c);

        c = new GridBagConstraints();
        CardsCanvas cardsPane = new CardsCanvas(subject);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.4;
        c.weighty = 0.4;
        c.gridx = 1;
        c.gridy = 2;
        pane.add(cardsPane, c);

        c = new GridBagConstraints();
        MovementCanvas mvtPane = new MovementCanvas(subject);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 2;
        pane.add(mvtPane, c);
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be invoked
     * from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("GridBagLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        frame.setSize(width, height);

        // Display the window.
        //frame.pack();
        frame.setVisible(true);
    }

    public String inputDialog(String text){
        String s = (String) JOptionPane.showInputDialog(text, JOptionPane.PLAIN_MESSAGE);
        // If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            return s;
        }
        return s;
    }

    public String inputDialog(String text, Object... args) {
        text = String.format(text, args);
        String s = (String) JOptionPane.showInputDialog(text, JOptionPane.PLAIN_MESSAGE);
        // If a string was returned, say so.
        if ((s != null) && (s.length() > 0)) {
            return s;
        }
        return s;
    }

    public void messageDialog(String text) {
        JOptionPane.showConfirmDialog(null, text, "Message", 2);
    }

    public void messageDialog(String text, Object... args) {
        text = String.format(text, args);
        JOptionPane.showConfirmDialog(null, text, "Message", 2);
    }

    public Player.Character radioButton(String text){
        RadioButton rb = new RadioButton(text);
        return rb.show();
    }
}