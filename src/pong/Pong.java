package pong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class Pong implements ActionListener, KeyListener {

    public int width = 700, height = 700;
    public Renderer renderer;
    public static Pong pong;
    public Paddle player1;
    public Paddle player2;
    public boolean bot = false; // FALSE - Player vs Player; TRUE - Player vs Computer
    public boolean w,s,up,down; // Variables used to represent state of keys.
    public Ball ball;
    public int gameState;  // 0 - starting screen; 1 - playing game; 2 - paused game; 3 - victory panel;

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
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g. drawString(String.valueOf(player1.score), width / 4, 50);
            g. drawString(String.valueOf(player2.score), 3 * width / 4, 50);

            player1.render(g);
            player2.render(g);
            ball.render(g);
            if (gameState == 2) {
                renderPauseMenu(g);
            }

        } else {
            renderStartScreen(g);
        }

        if(player1.score == 3) {
            gameState = 3;
            renderVictoryScreen(player1, g);
        } else if (player2.score == 3) {
            gameState = 3;
            renderVictoryScreen(player2, g);
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
            if (up) {
                player2.move(true);
            }
            if (down) {
                player2.move(false);
            }
            if(bot && ball.x > this.width / 2 && ball.motionX > 0) {
                if (ball.y > player2.y + player2.height / 2) {
                    player2.move(false);
                }

                if (ball.y < player2.y + player2.height / 2) {
                    player2.move(true);
                }
            }
            ball.update(player1, player2);
        }

    }

    public void renderStartScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 70));
        g. drawString("Pong", width / 2 - 80, height / 2 - 50);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g. drawString("Press Space to start", width / 4 - 20, height / 2 + 30);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        if (bot) {
            g. drawString("Player VS Computer", width / 2 - 10, height / 2 + 60);
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g. drawString("Player VS Player", width / 2 - 180 , height / 2 + 60);
        } else {
            g. drawString("Player VS Player", width / 2 - 180 , height / 2 + 60);
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g. drawString("Player VS Computer", width / 2  - 10, height / 2 + 60);
        }
    }

    public void renderPauseMenu(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g. drawString("Press R to restart the game", width / 8 , height / 2 - 50);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g. drawString("Press Space to resume the game", width / 8 - 50, height / 2 + 30);
    }

    private void renderVictoryScreen(Paddle player, Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 70));

        if (!bot) {
            g.drawString("Player " + player.paddleNumber + " won", width / 4 - 50, height / 2);
        } else {
            if(player.paddleNumber == 1) {
                g.drawString("You won!", width / 2 - 150, height / 2);
            } else {
                g.drawString("You lost!", width / 2 - 150, height / 2);
            }
        }
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Press Space to start new game", width / 3 - 40, height / 2 + 50);
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

        if (id == KeyEvent.VK_UP && !bot) {
            up = true;
        }

        if (id == KeyEvent.VK_DOWN && !bot) {
            down = true;
        }
        if (id == KeyEvent.VK_SPACE) {
            switch (gameState){
                case 0:
                case 2:
                    gameState = 1; break;
                case 1: gameState = 2; break;
                case 3: gameState = 0;
                        Ball.restart(this.player1, this.player2, this.ball);
                        break;
                default: break;
            }
        }

        if (id == KeyEvent.VK_R) {
            if (gameState == 2) {
                Ball.restart(this.player1, this.player2, this.ball);
                gameState = 0;
            }
        }

        if (id == KeyEvent.VK_RIGHT) {
            if (gameState == 0 && !bot) {
                bot = TRUE;
            }
        }

        if (id == KeyEvent.VK_LEFT) {
            if (gameState == 0 && bot) {
                bot = FALSE;
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
            up = false;
        }

        if (id == KeyEvent.VK_DOWN ) {
            down = false;
        }

    }

    public static void main(String[] args) {
        pong = new Pong();
    }
}