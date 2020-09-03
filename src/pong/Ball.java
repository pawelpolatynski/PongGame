package pong;

import java.awt.*;
import java.util.Random;

public class Ball {

    public int x, y, width = 20, height = 20;
    public int motionX, motionY;
    public Random random;
    public Pong pong;
    public int ballSpeed = 8;
    public int numberOfHits = 0;

    public Ball(Pong pong) {
        this.random = new Random();
        this.pong = pong;
        spawnBall();
    }

    public void spawnBall() {
        this.x = pong.width / 2 + this.width / 2;
        this.y = pong.height / 2 + this.height / 2;

        this.motionY = -2 + random.nextInt(4);

        if(random.nextBoolean()) {
            this.motionX = 1;
        } else {
            this.motionX = -1;
        }
    }
    public void update(Paddle paddle1, Paddle paddle2) {

        if (numberOfHits == 0) {
            this.x = this.x + motionX * (ballSpeed / 2);
            this.y = this.y + motionY * (ballSpeed / 2);
        } else {
            this.x = this.x + motionX * ballSpeed;
            this.y = this.y + motionY * ballSpeed;
        }

        if (checkCollision(paddle1) == 1) {
            this.motionX = 1;
            this.motionY = -2 + random.nextInt(4);
        } else if (checkCollision(paddle2) == 1) {
            this.motionX = -1;
            this.motionY = -2 + random.nextInt(4);
        }

        if (checkCollision(paddle1) == 2) {
            this.spawnBall();
            paddle2.score++;
        }

        if (checkCollision(paddle2) == 2) {
            this.spawnBall();
            paddle1.score++;
        }

        if (this.y <= 0 || this.y + this.height > pong.height) {
            motionY = motionY * -1;
        }

    }

    public static void restart(Paddle player1, Paddle player2, Ball ball) {
        player1.score = 0;
        player2.score = 0;
        ball.spawnBall();
    }
    public int checkCollision(Paddle paddle) {
        if (this.x < paddle.x + paddle.width && this.x + width > paddle.x && this.y < paddle.y + paddle.height && this.y + height > paddle.y)
        {
            this.numberOfHits = 1;
            return 1; //bounce
        }
        else if ((paddle.x > x && paddle.paddleNumber == 1) || (paddle.x < x - width && paddle.paddleNumber == 2))
        {
            this.numberOfHits = 0;
            return 2; //score
        }

        return 0;
    }

    public void render(Graphics g){
        g.setColor(Color.WHITE);
        g.fillOval(x, y, width, height);
    }
}
