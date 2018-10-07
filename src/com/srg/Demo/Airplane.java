package com.srg.Demo;

import com.srg.Abstract.FlyObject;
import com.srg.Interface.Enemy;
import com.srg.Main;

import java.util.Random;

public class Airplane extends FlyObject implements Enemy {

    private int speed;

    public Airplane(){
        speed = 2;
        this.image = Main.airplane;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();

        y = 0;
        Random random = new Random();
        x = random.nextInt(Main.WIDTH - this.width);
    }

    @Override
    public void move() {
        y += speed;
    }

    @Override
    public int getScore() {
        return 1;
    }

    @Override
    public boolean isOutBound() {
        return y > Main.HEIGHT;
    }
}
