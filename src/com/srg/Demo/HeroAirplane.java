package com.srg.Demo;

import com.srg.Abstract.FlyObject;
import com.srg.Main;

import java.awt.image.BufferedImage;

public class HeroAirplane extends FlyObject {

    private int life;
    private int doubleFire;
    private BufferedImage[] images;
    private int index = 0;

    public HeroAirplane() {
        life = 300;
        doubleFire = 0;
        this.images = new BufferedImage[]{Main.heroOne, Main.heroTwo};
        this.image = Main.heroOne;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();
        this.x = 150;
        this.y = 450;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getDoubleFire() {
        return doubleFire;
    }

    public void setDoubleFire(int doubleFire) {
        this.doubleFire = doubleFire;
    }

    @Override
    public void move() {
        int num = index++ / 2 % images.length;
        image = images[num];
    }

    public Bullet[] shoot() {
        int xStep = width / 4;
        Bullet[] bullets;
        if (doubleFire == 1) {
            bullets = new Bullet[2];
            bullets[0] = new Bullet(x + xStep, y);
            bullets[1] = new Bullet(x + xStep * 3, y);
        } else {
            bullets = new Bullet[1];
            bullets[0] = new Bullet(x + xStep * 2, y);
        }
        return bullets;
    }

    @Override
    public boolean isOutBound() {
        return false;
    }

    public boolean hit(FlyObject flyObject) {
        return this.x + width / 2 > flyObject.x - width / 2 && this.x + width / 2 < flyObject.x + flyObject.width + width / 2 && this.y + height / 2 > flyObject.y - height / 2 && this.y + height / 2 < flyObject.y + flyObject.height + height / 2;
    }

    public void moveTo(int x, int y) {
        this.x = x - width / 2;
        this.y = y - height / 2;
    }
}
