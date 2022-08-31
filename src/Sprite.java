import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.util.Random;

public class Sprite{
    private int x;
    private int y;
    private int sx;
    private int sy;
    private final int speed = 7;
    private final int verticalSpeed = (int)(speed*1.5);
    private BufferedImage image;
    private Weapon weapon;
    private boolean landed = false;
    private int cooldown = 0;
    private char direction = 'r';
    private int score = 0;
    private boolean alive = true;
    private int health = 5;
    private Random random;

    public Sprite(){
        loadSprite();
    }

    public Sprite(int x, int y){
        loadSprite();
        random = new Random();
        if(random.nextBoolean()){
            direction = 'l';
        }
        this.x = x;
        this.y = y;
    }

    public void reset(){
        direction = 'r';
        score = 0;
        alive = true;
        health = 5;
    }

    private void loadSprite(){
        try{
            image = ImageIO.read(new File("./lib/sprite.png"));
            weapon = new Weapon();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void move(){
        x += sx;
        weapon.setX(x+30);
        y += sy;
        weapon.setY(y);
        if(health <= 0){
            killed();
            health = 0;
        }
    }

    public void gravity(){
        if(!landed && cooldown == 0){
            sy++;
        } else cooldown--;
    }

    public void setLanded(){
        landed = true;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
        weapon.setY(this.y);
    }

    public int getY(){
        return y;
    }

    public Image getImage(){
        Image img;
        img = image.getSubimage(0, 0, 60, 90);
        return img;
    }

    public Weapon getWeapon(){
        return weapon;
    }

    public void setWeapon(Weapon weapon){
        this.weapon = weapon;
    }

    public void attack(){
        addScore(0);
        weapon.shoot();
    }

    public void addScore(int points){
        score += points;
    }

    public int getScore(){
        return score;
    }

    public void killed(){
        alive = false;
    }

    public boolean isAlive(){
        return alive;
    }

    public int getSpeed(){
        return sx;
    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch(key){
            case(KeyEvent.VK_LEFT):
                sx = -speed;
                weapon.setDirection('l');
                break;
            case(KeyEvent.VK_RIGHT):
                sx = speed;
                weapon.setDirection('r');
                break;
            case(KeyEvent.VK_UP):
                if(landed){
                    sy = -verticalSpeed;
                    landed = false;
                    cooldown = (int)(verticalSpeed*0.7);
                }
                break;
            case(KeyEvent.VK_SPACE):
                attack();
                break;
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        switch(key){
            case(KeyEvent.VK_LEFT):
                sx = 0;
                break;
            case(KeyEvent.VK_RIGHT):
                sx = 0;
                break;
        }
    }

    public void neuralNetworkInput(ArrayList a){
        if((double)a.get(0) > 0.5){
            sx = -speed;
            weapon.setDirection('l');
        }else sx = 0;
        if((double)a.get(1) > 0.5){
            sx = speed;
            weapon.setDirection('r');
        } else sx = 0;
        if((double)a.get(2) > 0.5){
            if(landed){
                sy = -verticalSpeed;
                landed = false;
                cooldown = (int)(verticalSpeed*0.7);
            }
        }
        if((double)a.get(3) > 0.5){
            attack();
        }
    }

    public int[] vision(int player, Sprite enimies[]){
        int temp[] = new int[2];
        temp[0] = 100000;
        temp[1] = 3;
        for(int i = 0; i < enimies.length; i++){
            if(i != player && enimies[i].isAlive()){
                if(direction == 'r'){
                    if(enimies[i].getX() > x && enimies[i].getX() < x+225 ){
                        if(enimies[i].getY() > y-90 && enimies[i].getY() < y+90 ){
                            temp[0] = x - enimies[i].getX();
                            temp[1] = enimies[i].getDirection();
                        }
                    }
                } else{
                    if(enimies[i].getX() < x && enimies[i].getX() > x-225){
                        if(enimies[i].getY() > y-90 && enimies[i].getY() < y+90 ){
                            temp[0] =  enimies[i].getX() - x;
                            temp[1] = enimies[i].getDirection();
                        }
                    }
                }
            }
        }
        return temp;
    }

    public int getDirection(){
        if(direction == 'r'){
            return 1;
        } return 0;
    }

    public void takeDamage(){
        if(alive) health--;
    }

    public void hit(int player, Sprite enimies[]){
        for(int i = 0; i < enimies.length; i++){
            if(weapon.bulletExists()){
                if(i != player && enimies[i].isAlive()){
                    if(direction == 'r'){
                        if(weapon.getBulletX() <= enimies[i].getX()+60 && weapon.getBulletX() >= enimies[i].getX()){
                            if(weapon.getBulletY() <= enimies[i].getY()+90 && weapon.getBulletY() >= enimies[i].getY()){
                                this.addScore(20);
                                enimies[i].takeDamage();
                                weapon.bulletDestroyed();
                            }
                        }
                    }else{
                        if(weapon.getBulletX() <= enimies[i].getX()+60 && weapon.getBulletX() >= enimies[i].getX()){
                            if(weapon.getBulletY() <= enimies[i].getY()+90 && weapon.getBulletY() >= enimies[i].getY()){
                                this.addScore(20);
                                enimies[i].takeDamage();
                                weapon.bulletDestroyed();
                            }
                        }
                    }
                }
            }
        }
    }
}
