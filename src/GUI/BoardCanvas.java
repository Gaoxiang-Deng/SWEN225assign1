package GUI;

import javax.swing.*;

import board.Board;
import game.Player;
import board.Square;

import java.awt.*;
import java.util.ArrayList;

public class BoardCanvas extends Observer {

    Board board;
    ArrayList<Player> players;

    public BoardCanvas(Subject subject) {
        super();
        board = new Board();
        players = new ArrayList<>();
        this.subject = subject;
        this.subject.attach(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);

        int squareSize = getWidth() / 24;

        g.setColor(Color.black);

        for (int x = 0; x < 24; x++) {
            for (int y = 0; y < 24; y++) {
                if (board.getSquare(x, y).getIsRoom()) {
                    rect(g, squareSize, x, y, Color.gray);
                }
                else {
                    rect(g, squareSize, x, y, Color.white);
                }

                g.setColor(Color.red);
                if (!board.getSquare(x, y).getUp()) {
                    g.drawLine(squareSize * x, squareSize * y, squareSize * (x + 1), squareSize * y);
                }
                if (!board.getSquare(x, y).getLeft()) {
                    g.drawLine(squareSize * x, squareSize * y, squareSize * x, squareSize * (y + 1));
                }
                if (!board.getSquare(x, y).getRight()) {
                    g.drawLine(squareSize * (x + 1), squareSize * y, squareSize * (x + 1), squareSize * (y + 1));
                }
                if (!board.getSquare(x, y).getDown()) {
                    g.drawLine(squareSize * x, squareSize * (y + 1), squareSize * (x + 1), squareSize * (y + 1));
                }
            }
        }

        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        for (Player p : players) {
            g.translate(squareSize/4, -squareSize/4);
            g.drawString(p.getCharacter().name().substring(0, 1), squareSize * p.getLocX(), squareSize * p.getLocY());
            g.translate(-squareSize / 4, squareSize / 4);
        }
    }

    private void rect(Graphics g, int squareSize, int x, int y, Color col) {
        g.setColor(Color.gray);
        g.drawRect(x * squareSize, y * squareSize, (x + 1) * squareSize, (y + 1) * squareSize);
        g.setColor(col);
        g.fillRect(x * squareSize, y * squareSize, (x + 1) * squareSize, (y + 1) * squareSize);
    }

    @Override
    public void update() {
        board = subject.getBoard();
        if (subject.getAllPlayers() != null) {
            players = subject.getAllPlayers();
        }
        repaint();
    }
}
