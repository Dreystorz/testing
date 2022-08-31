import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

public class Weapon {
    private Image gunImgLeft;
    private Image gunImgRight;
    private Image bulletImg;
    private BufferedImage image;
    private int x, y;
    private int bulletX, bulletY;
    private char direction = 'r';
    private char bulletDirection = 'r';
    private int shooting = 0;
    private boolean bulletExists = false;

    public Weapon(){
        loadWeapon();
    }

    private void loadWeapon(){
        try{
            image = ImageIO.read(new File("./lib/sprite.png"));
            gunImgRight = image.getSubimage(78, 8, 20, 20);
            gunImgLeft = image.getSubimage(125, 8, 20, 20);
            bulletImg = image.getSubimage(82, 36, 8, 5);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Image getImage(){
        if(direction == 'r'){
            return gunImgRight;
        }else{
            return gunImgLeft;
        }
    }

    public Image getBulletImage(){
        return bulletImg;
    }

    public void shoot(){
        if(!bulletExists){
            bulletExists = true;
            bulletX = x;
            bulletY = y;
            shooting = 15;
        }
    }

    public void moveBullet(){
        if(shooting > 0){
            if(bulletDirection == 'r'){
                bulletX += 15;
            }else {
                bulletX -= 15;
            }
            shooting--;
        } else{
            bulletExists = false;
            bulletDirection = direction;
        }
    }

    public boolean bulletExists(){
        return bulletExists;
    }

    public void bulletDestroyed(){
        bulletExists = false;
        shooting = 0;
        bulletDirection = direction;
    }

    public void setX(int x){
        if(direction == 'r'){
            this.x = x+15;
        }else{ 
            this.x = x-32;
        }
    }

    public int getX(){
        return x;
    }

    public void setY(int y){
        this.y = y+43;
    }

    public int getY(){
        return y;
    }

    public int getBulletX(){
        return bulletX;
    }

    public int getBulletY(){
        return bulletY;
    }

    public void setDirection(char direction){
        this.direction = direction;
        if(!bulletExists) bulletDirection = direction;
    }

}
