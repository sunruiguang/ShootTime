package com.srg.Demo;

import com.srg.Abstract.FlyObject;
import com.srg.Main;

public class Bullet extends FlyObject {
    private int speed;

    Bullet(int x, int y) {
        speed = 10;
        this.x = x;
        this.y = y;
        this.image = Main.bullet;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    @Override
    public void move() {
        y -= speed;
    }

    @Override
    public boolean isOutBound() {
        return y < 0;
    }
}
