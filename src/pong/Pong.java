package pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Pong implements ActionListener, KeyListener {

    public int width = 700, height = 700;
    public Renderer renderer;
    public static Pong pong;
    public Paddle player1;
    public Paddle player2;
    public boolean bot = false;
    public boolean w,s,o,l;
    public Ball ball;
    public int gameState;

    public Pong() {
        Timer timer = new Timer(20, this);
        JFrame jframe = new JFrame("Pong");
        gameState = 0;

        renderer = new Renderer();

        jframe.setSize(width + 16, height + 39);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.addKeyListener(this);
        jframe.add(renderer);
        start();

        timer.start();
    }

    public void start() {
        player1 = new Paddle(this, 1);
        player2 = new Paddle(this, 2);
        ball = new Ball(this);
    }

    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        if (gameState != 0) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(5));
            g.drawLine(width / 2, 0, width / 2, height);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 30));
            g. drawString(String.valueOf(player1.score), width / 4, 50);
            g. drawString(String.valueOf(player2.score), 3 * width / 4, 50);

            player1.render(g);
            player2.render(g);
            ball.render(g);
            if (gameState == 2) {

                g.setColor(Color.RED);
                g.setFont(new Font("Arial", 1, 40));
                g. drawString("Press R to restart the game", width / 8 , height / 2 - 50);
                g.setFont(new Font("Arial", 1, 40));
                g. drawString("Press Space to resume the game", width / 8 - 50, height / 2 + 30);
            }

        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 70));
            g. drawString("Pong", width / 2 - 80, height / 2 - 50);
            g.setFont(new Font("Arial", 1, 40));
            g. drawString("Press Space to start", width / 4 , height / 2 + 30);
        }

    }

    public void update() {

        if (gameState == 1) {
            if (w) {
                player1.move(true);
            }
            if (s) {
                player1.move(false);
            }
            if (o) {
                player2.move(true);
            }
            if (l) {
                player2.move(false);
            }
            ball.update(player1, player2);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();

        renderer.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W ) {
            w = true;
        }

        if (id == KeyEvent.VK_S ) {
            s = true;
        }

        if (id == KeyEvent.VK_UP ) {
            o = true;
        }

        if (id == KeyEvent.VK_DOWN ) {
            l = true;
        }
        if (id == KeyEvent.VK_SPACE) {
            switch (gameState){
                case 0: gameState = 1; break;
                case 1: gameState = 2; break;
                case 2: gameState = 1; break;
                default: break;
            }
        }

        if (id == KeyEvent.VK_R) {
            if (gameState == 2) {
                Ball.restart(this.player1, this.player2, this.ball);
                gameState = 0;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W ) {
            w = false;
        }

        if (id == KeyEvent.VK_S ) {
            s = false;
        }

        if (id == KeyEvent.VK_UP ) {
            o = false;
        }

        if (id == KeyEvent.VK_DOWN ) {
            l = false;
        }

    }

    public static void main(String[] args) {
        pong = new Pong();
    }
}