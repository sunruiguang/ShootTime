package com.srg.Abstract;

import com.srg.Demo.Bullet;

import java.awt.image.BufferedImage;

/**
 * 所有飞行物对象共有的属性，集合在抽象类中，再由每个对象具体去继承该类
 */
public abstract class FlyObject {

    public int x;
    public int y;
    public int width;
    public int height;
    public BufferedImage image;

    /**
     * 构造方法
     */
    protected FlyObject() {}

    /**
     * 抽象移动方法
     */
    public abstract void move();

    /**
     *  进行判断子弹是否与飞行物进行碰撞
     * @param bullet
     * @return
     */
    public boolean shootBy(Bullet bullet) {
        return bullet.x > x - bullet.width && bullet.x < x + width && bullet.y > y - bullet.height && bullet.y < y + height;
    }

    /**
     * 抽象，判断是否飞行物是否越界
     * @return
     */
    public abstract boolean isOutBound();

}
