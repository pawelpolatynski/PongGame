package pong;

import java.awt.*;

public class Paddle {
    public int paddleNumber;
    public int x, y, width = 20, height = 250;
    public int score;

    public Paddle(Pong pong, int paddleNumber) {
        this.score = 0;
        this.paddleNumber = paddleNumber;
        if (paddleNumber == 1) {
            this.x = 0;
        } else if (paddleNumber == 2) {
            this.x = pong.width - width;
        }
            this.y = pong.height / 2 - this.height / 2;

    }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    public void move(boolean b) {
        int speed = 5;
        if (b) {
            if (y > 0) {
                y = y - speed;
            } else {
                y = 0;
            }
        } else {
            if (y + height < Pong.pong.height) {
                y += speed;
            }
        }
    }
}
