package com.srg;

import com.srg.Abstract.FlyObject;
import com.srg.Demo.Airplane;
import com.srg.Demo.Bee;
import com.srg.Demo.Bullet;
import com.srg.Demo.HeroAirplane;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("MagicConstant")
public class Main extends JPanel {

    /*
     画板属性
     */
    public static final int WIDTH = 380;
    public static final int HEIGHT = 650;

    /*
     需要加载的图片定义
     */
    private static BufferedImage background;
    public static BufferedImage bee;
    public static BufferedImage heroOne;
    public static BufferedImage heroTwo;
    public static BufferedImage airplane;
    public static BufferedImage bullet;
    private static BufferedImage start;
    private static BufferedImage pause;
    private static BufferedImage gameOver;

    /*
    英雄机的两张状态图，需要用数组来装，然后是子弹数组，飞行物数组
     */
    private HeroAirplane heroAirplane = new HeroAirplane();
    private Bullet[] bullets = {};
    private FlyObject[] flyObjects = {};

    private int index = 0;
    private int shootIndex = 0;
    private int score;

    /*
    游戏状态默认值
     */
    private static int state;
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int PAUSE = 2;
    private static final int END = 3;

    /*
    静态加载游戏所需资源
     */
    static {
        try {
            background = ImageIO.read(Main.class.getResource("Resource/background.png"));
            bee = ImageIO.read(Main.class.getResource("Resource/bee.png"));
            heroOne = ImageIO.read(Main.class.getResource("Resource/heroOne.png"));
            heroTwo = ImageIO.read(Main.class.getResource("Resource/heroTwo.png"));
            airplane = ImageIO.read(Main.class.getResource("Resource/airplane.png"));
            bullet = ImageIO.read(Main.class.getResource("Resource/bullet.png"));
            start = ImageIO.read(Main.class.getResource("Resource/start.png"));
            pause = ImageIO.read(Main.class.getResource("Resource/pause.png"));
            gameOver = ImageIO.read(Main.class.getResource("Resource/gameOver.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    整个程序的入口，在入口中定义需要运行的程序
     */
    public static void main(String[] args) {
        JFrame jFrame = new JFrame("飞机大战");
        Main main = new Main();
        jFrame.add(main);
        jFrame.setSize(Main.WIDTH, Main.HEIGHT);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
        main.start();
    }

    /*
    画板中需要添加的对象，以及用来控制游戏的鼠标控制程序
     */
    private void start() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                switch (state) {
                    case START:
                        state = RUNNING;
                        break;
                    case END:
                        heroAirplane = new HeroAirplane();
                        flyObjects = new FlyObject[0];
                        bullets = new Bullet[0];
                        score = 0;
                        state = START;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if (state == PAUSE) state = RUNNING;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if (state != END && state != START) state = PAUSE;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                if (state == RUNNING) heroAirplane.moveTo(e.getX(), e.getY());
            }
        };
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        int interval = 10;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (state == RUNNING) {
                    enterAction();
                    stepAction();
                    shootAction();
                    checkAction();
                    checkOutOfAction();
                    checkHeroAction();
                    checkGameOver();
                }
                repaint();
            }
        }, interval, interval);
    }

    private void checkGameOver() {
        if (isOver()) state = END;
    }

    private boolean isOver() {
        return heroAirplane.getLife() <= 0;
    }

    /**
     * 根据游戏内容，进行游戏英雄机的判断
     */
    private void checkHeroAction() {
        for (int i = 0; i < flyObjects.length; i++) {
            if (heroAirplane.hit(flyObjects[i])) {
                if (heroAirplane.getLife() > 0) {
                    heroAirplane.setLife(heroAirplane.getLife() - 1);
                    heroAirplane.setDoubleFire(0);
                    FlyObject temp = flyObjects[i];
                    flyObjects[i] = flyObjects[flyObjects.length - 1];
                    flyObjects[flyObjects.length - 1] = temp;
                    flyObjects = Arrays.copyOf(flyObjects, flyObjects.length - 1);
                }
            }
        }
    }

    /**
     * 判断飞行物是否出界，若出界，则对相应的数组进行缩容，防止，jvm虚拟机宕机
     */
    private void checkOutOfAction() {
        //飞行物缩容
        for (int i = 0; i < flyObjects.length; i++) {
            if (flyObjects[i].isOutBound()) {
                FlyObject temp = flyObjects[i];
                flyObjects[i] = flyObjects[flyObjects.length - 1];
                flyObjects[flyObjects.length - 1] = temp;
                flyObjects = Arrays.copyOf(flyObjects, flyObjects.length - 1);
            }
        }
        //子弹缩容
        for (int i = 0; i < bullets.length; i++) {
            if (bullets[i].isOutBound()) {
                Bullet temp = bullets[i];
                bullets[i] = bullets[bullets.length - 1];
                bullets[bullets.length - 1] = temp;
                bullets = Arrays.copyOf(bullets, bullets.length - 1);
            }
        }
    }

    /**
     *对子弹进行碰撞，缩容
     */
    private void checkAction() {
        for (int i = 0; i < bullets.length; i++) {
            Bullet bullet = bullets[i];
            if (check(bullet)) {
                Bullet bullet1 = bullets[i];
                bullets[i] = bullets[bullets.length - 1];
                bullets[bullets.length - 1] = bullet1;
                bullets = Arrays.copyOf(bullets, bullets.length - 1);
            }
        }
    }

    /**
     * 检查碰撞效果，如果英雄机碰撞，产生不同的效果
     * @param bullet
     * @return
     */
    private boolean check(Bullet bullet) {
        int indexFly = -1;
        for (int i = 0; i < flyObjects.length; i++) {
            if (flyObjects[i].shootBy(bullet)) {
                indexFly = i;
                break;
            }
        }

        if (indexFly != -1) {
            FlyObject temp = flyObjects[indexFly];
            flyObjects[indexFly] = flyObjects[flyObjects.length - 1];
            flyObjects[flyObjects.length - 1] = temp;
            flyObjects = Arrays.copyOf(flyObjects, flyObjects.length - 1);
            if (temp instanceof Airplane) {
                Airplane airplane = (Airplane) temp;
                score += airplane.getScore();
            } else {
                Bee bee = (Bee) temp;
                if (bee.getType() == 0 && heroAirplane.getDoubleFire() != 1) heroAirplane.setDoubleFire(1);
                else heroAirplane.setLife(heroAirplane.getLife() + 1);
            }
            return true;
        }
        return false;
    }

    /**
     * 对子弹进行入场效果，扩容
     */
    private void shootAction() {
        if (shootIndex++ % 5 == 0) {
            Bullet[] bullets1 = heroAirplane.shoot();
            bullets = Arrays.copyOf(bullets, bullets1.length + bullets.length);
            System.arraycopy(bullets1, 0, bullets, bullets.length - bullets1.length, bullets1.length);
        }
    }

    /**
     * 飞行物进行移动效果
     */
    private void stepAction() {
        for (FlyObject flyObject : flyObjects) {
            flyObject.move();
        }
        heroAirplane.move();//英雄机的动态效果
        for (Bullet bullet : bullets) {
            bullet.move();
        }
    }

    /**
     * 飞行物入场，扩容效果
     */
    private void enterAction() {
        if (index++ % 10 == 0) {
            FlyObject flyObject = nextOne();
            flyObjects = Arrays.copyOf(flyObjects, flyObjects.length + 1);
            flyObjects[flyObjects.length - 1] = flyObject;
        }
    }

    /**
     * 产生下一个对象
     * @return
     */
    private FlyObject nextOne() {
        Random random = new Random();
        if (random.nextInt(10) == 0) return new Bee();
        return new Airplane();

    }

    /**
     * 画板进行绘画的功能
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(background, 0, 0, null);
        g.setColor(Color.BLUE);
        g.setFont(new Font("楷体", 25, 15));
        g.drawString("分数：" + score, 15, 30);
        g.drawString("生命值：" + heroAirplane.getLife(), 15, 45);
        paintHero(g);
        paintBullet(g);
        paintFlyObject(g);
        paintState(g);
    }

    /**
     * 进行程序状态的判断
     * @param g
     */
    private void paintState(Graphics g) {
        switch (state) {
            case START:
                g.drawImage(start, 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(pause, 0, 0, null);
                break;
            case END:
                g.drawImage(gameOver, 0, 0, null);
                break;
            default:
                break;
        }
    }

    /**
     * 进行飞行物的绘画
     * @param g
     */
    private void paintFlyObject(Graphics g) {
        for (FlyObject flyObject : flyObjects) {
            if (flyObject instanceof Airplane) {
                Airplane airplane = (Airplane) flyObject;
                g.drawImage(airplane.image, airplane.x, airplane.y, null);
            } else if (flyObject instanceof Bee) {
                Bee bee = (Bee) flyObject;
                g.drawImage(bee.image, bee.x, bee.y, null);
            }
        }

    }

    /**
     * 进行子弹的绘画
     * @param g
     */
    private void paintBullet(Graphics g) {
        for (Bullet bullet : bullets) {
            g.drawImage(bullet.image, bullet.x, bullet.y, null);
        }
    }

    /**
     * 英雄机的绘画
     * @param g
     */
    private void paintHero(Graphics g) {
        g.drawImage(heroAirplane.image, heroAirplane.x, heroAirplane.y, null);
    }

}
