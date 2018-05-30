import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener 
{
	private static final long serialVersionUID = -8394275588771793527L;
	private final int BOARD_WIDTH = 500;
    private final int BOARD_HEIGHT = 500;
    
    private final int BALL_SIZE = 10;
    private final int MAX_BALLS = 900;
    private final int RANDOM_POSITION = 48;
    private final int x[] = new int[MAX_BALLS];
    private final int y[] = new int[MAX_BALLS];

    private Timer timer;
    private final int DELAY = 125;
    
    private Image ball;
    private Image apple;
    private Image head;
    
    private volatile boolean goingLeft = false;
    private volatile boolean goingRight = true;
    private volatile boolean goingUp = false;
    private volatile boolean goingDown = false;
    private boolean inGame = true;

    private int balls;
    private int appleX;
    private int appleY;
    
    public Board() 
    {        
    	addKeyListener(new MoveListener());
        setBackground(Color.BLACK);
        setFocusable(true);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        ImageIcon ballImage = new ImageIcon("src/artwork/ball.png");
        ball = ballImage.getImage();
        ImageIcon appleImage = new ImageIcon("src/artwork/apple.png");
        apple = appleImage.getImage();
        ImageIcon headImage = new ImageIcon("src/artwork/head.png");
        head = headImage.getImage();
        gameStart();
    }

    private void gameStart() 
    {
        balls = 3;

        for (int z = 0; z < balls; z++) 
        {
            x[z] = 50 - z * BALL_SIZE;
            y[z] = 50;
        }
        addApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        if (inGame) 
        {
            g.drawImage(apple, appleX, appleY, this);

            for (int z = 0; z < balls; z++) 
            {
                if (z == 0) 
                {
                    g.drawImage(head, x[z], y[z], this);
                } 
                else 
                {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } 
        else 
        {
            String msg = "Game Over";
            Font small = new Font("Comic Sans", Font.BOLD, 14);
            FontMetrics metr = getFontMetrics(small);

            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, (BOARD_WIDTH - metr.stringWidth(msg)) / 2, BOARD_HEIGHT / 2);
        }
    }
 
    private void move() 
    {
        for (int z = balls; z > 0; z--) 
        {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (goingLeft) x[0] -= BALL_SIZE;

        if (goingRight) x[0] += BALL_SIZE;
        
        if (goingUp) y[0] -= BALL_SIZE;

        if (goingDown) y[0] += BALL_SIZE;
    }

    private void checkCollision() 
    {
        for (int z = balls; z > 0; z--) 
        {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) inGame = false;
        }

        if (y[0] >= BOARD_HEIGHT) inGame = false;

        if (y[0] < 0) inGame = false;

        if (x[0] >= BOARD_WIDTH) inGame = false;

        if (x[0] < 0) inGame = false;
        
        if (!inGame) timer.stop();
    }

    private void addApple() 
    {
        int rand = (int) (Math.random() * RANDOM_POSITION);
        appleX = ((rand * BALL_SIZE));

        rand = (int) (Math.random() * RANDOM_POSITION);
        appleY = ((rand * BALL_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (inGame) 
        {
            if ((x[0] == appleX) && (y[0] == appleY)) 
            {
                balls++;
                addApple();
            }
            checkCollision();
            move();
        }
        repaint();
    }

    private class MoveListener extends KeyAdapter 
    {
    	@Override
        public void keyPressed(KeyEvent e) 
    	{
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!goingRight)) 
            {
                goingLeft = true;
                goingUp = false;
                goingDown = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!goingLeft)) 
            {
                goingRight = true;
                goingUp = false;
                goingDown = false;
            }

            if ((key == KeyEvent.VK_UP) && (!goingDown)) 
            {
                goingUp = true;
                goingRight = false;
                goingLeft = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!goingUp)) 
            {
                goingDown = true;
                goingRight = false;
                goingLeft = false;
            }
        }
    }
}