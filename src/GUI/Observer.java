
// ATTRIBUTION:
// This code was taken from TutorialsPoint.com
//https://www.tutorialspoint.com/design_pattern/observer_pattern.htm

package GUI;

import javax.swing.JPanel;

public abstract class Observer extends JPanel{
    protected Subject subject;
    public abstract void update();
}