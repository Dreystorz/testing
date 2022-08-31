import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.util.Random;

import NeuralNetwork.Matrix;
import NeuralNetwork.NeuralNetwork;

public class Level extends JPanel implements Runnable{
    Thread gameThread;
    private Sprite[] sp;
    private final int height = 600;
    private final int width = 1400;
    private NeuralNetwork n[];
    private final int nnSize = 20;
    private Random random;
    private boolean running = false;
    private boolean rPressed = false;
    private int epochs = 100;

    public Level(){
        createLevel();
    }

    private void createLevel(){
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(width,height));
        this.addKeyListener(new myAdapter());
        random = new Random();
        sp = new Sprite[nnSize+1];
        sp[0] = new Sprite(random.nextInt(width),height-90);
        n = new NeuralNetwork[nnSize];
        for (int i = 0; i < nnSize; i++) {
            n[i] = new NeuralNetwork(4, "20,10", 4);
            sp[i+1] = new Sprite(random.nextInt(width),height-90);
        }
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void restart(){
        for (int i = 0; i < n.length; i++) {
            n[i].setScore(sp[i+1].getScore());
        }
        if(n.length > 0) n = NeuralNetwork.geneticTrain(n);

        random = new Random();
        sp[0] = new Sprite(random.nextInt(width),height-90);
        for (int i = 0; i < nnSize; i++) {
            sp[i+1].reset();
        }
        epochs--;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g){
        for (Sprite sprite : sp) {
            if(sprite.isAlive()){
                g.drawImage(sprite.getImage(), sprite.getX(), sprite.getY(), this);
                g.drawImage(sprite.getWeapon().getImage(), sprite.getWeapon().getX(), sprite.getWeapon().getY(), this);
                if(sprite.getWeapon().bulletExists()){
                    g.drawImage(sprite.getWeapon().getBulletImage(), sprite.getWeapon().getBulletX(), sprite.getWeapon().getBulletY(), this);
                }
            }
        }
    }

    public void run(){
        int test = 1000;
        long lastTime = System.nanoTime();
        double amountOfTicks = 6000.0;
        double ns = (1000000000/amountOfTicks);
        double delta = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime)/(1000000000/amountOfTicks);
            lastTime = now;
            if(delta >= 1){
                if(!rPressed){
                    neuralNetworkMove();
                    move();
                    checkCollisions();
                    checkHits();
                    repaint();
                    if(test <= 0){
                        restart();
                        test = 1000;
                    }else {
                        test--;
                    }
                }
                delta--;
                if(epochs <= 0){
                    amountOfTicks = 60;
                }
            }
        }
    }

    public void neuralNetworkMove(){
        try{
            for(int i = 0; i < n.length; i++){
                if(sp[i+1].isAlive() && n[i] != null){

                    double input[] = new double[4];
                    int temp[] = sp[i+1].vision(i+1, sp);
                    input[0] = temp[0];
                    input[0] = temp[1];
                    input[2] = sp[i+1].getDirection();
                    input[3] = sp[i+1].getSpeed();
                    Matrix m = n[i].predict(input);
                    sp[i+1].neuralNetworkInput(m.toArray());
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void move(){
        for (Sprite sprite : sp) {
            if(sprite != null && sprite.isAlive()){
                sprite.addScore(1);
                sprite.move();
                sprite.getWeapon().moveBullet();
                sprite.gravity();
            }
        }
    }

    public void checkHits(){
        for(int i = 0; i < sp.length; i++){
            if(sp[i] != null)
            sp[i].hit(i, sp);
        }
    }

    public void checkCollisions(){
        for (Sprite sprite : sp) {
            if(sprite != null && sprite.getY() >= height-90){
                sprite.setLanded();
                sprite.setY(height-90);
            }
            if(sprite != null && sprite.getX() > width){
                sprite.setX(-60);
            }
            if(sprite != null && sprite.getX() < -60){
                sprite.setX(width);
            }
        }
    }

    private class myAdapter extends KeyAdapter{
        @Override
        public void keyReleased(KeyEvent e){
            if(KeyEvent.VK_R == e.getKeyCode()){
                rPressed = false;
            }
            sp[0].keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e){
            if(KeyEvent.VK_R == e.getKeyCode() && !rPressed){
                rPressed = true;
                restart();
            }
            sp[0].keyPressed(e); 
        }
    }
}
