package com.srg.Demo;

import com.srg.Abstract.FlyObject;
import com.srg.Interface.Award;
import com.srg.Main;

import java.util.Random;

public class Bee extends FlyObject implements Award {

    private int xSpeed;
    private int ySpeed;
    private int awardType;

    /**
     * 蜜蜂的构造方法，随机生成x的坐标
     */
    public Bee() {
        xSpeed = 1;
        ySpeed = 2;
        this.image = Main.bee;
        this.width = this.image.getWidth();
        this.height = this.image.getHeight();

        y = 0;
        Random random = new Random();
        x = random.nextInt(Main.WIDTH - this.width);
        awardType = random.nextInt(2);
    }

    /**
     * 进行蜜蜂的移动
     */
    @Override
    public void move() {
        x += xSpeed;
        y += ySpeed;
        if (x > Main.WIDTH - width) {
            xSpeed = -1;
        }
        if (x < 0) {
            xSpeed = 1;
        }
    }

    /**
     * 获得蜜蜂所具有的属性
     * @return
     */
    @Override
    public int getType() {
        return awardType;
    }

    /**
     * 判断蜜蜂是否越界
     * @return
     */
    @Override
    public boolean isOutBound() {
        return y > Main.HEIGHT;
    }
}
