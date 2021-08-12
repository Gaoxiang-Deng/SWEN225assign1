package GUI;
import java.util.ArrayList;

import javax.swing.*;

import game.Player;

/**
 * Creates and displays the panel that allows users to select a character
 * (using JRadioButton objects)
 */
public class RadioButton{
    final JPanel panel;
    ButtonGroup bg;
    ArrayList<JRadioButton> buttons;

    public RadioButton(String text) {

        panel = new JPanel();
        panel.add(new JLabel("Choose a character:"));
        buttons = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            JRadioButton button = new JRadioButton(Player.Character.values()[i].name()); // create buttons with character names
            buttons.add(button); // add to list
            button.setEnabled(!Player.Character.values()[i].getSelected()); // if character is already selected, grey out option
        }
        bg = new ButtonGroup();
        for(JRadioButton b : buttons){
            bg.add(b);
            panel.add(b);
        }
    }
    
    /**
     * Draws a JOptionPane object that contains the charcater options
     * @return The selected option
     */
    public Player.Character show(){
        Player.Character selected = null;
        while(selected == null){
            int jpn = JOptionPane.showConfirmDialog(null, panel);
            if(jpn == JOptionPane.OK_OPTION){
                for(int i = 0; i < 4; i++){
                    if(buttons.get(i).isSelected()) {
                        System.out.println(Player.Character.values()[i]);
                        selected = Player.Character.values()[i];
                    }
                }
            }
        }
        return selected;
    }
}