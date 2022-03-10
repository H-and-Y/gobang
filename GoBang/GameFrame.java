
package GoBang;

import com.sun.prism.null3d.NULL3DPipeline;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameFrame extends JFrame implements MouseListener {

    //棋盘的X,Y坐标以及宽度和高度
    int chessX = 20;
    int chessY = 40;
    int chessW = 560;  //16*35=560
    int chessH = 560;

    //按钮的X,Y坐标以及宽度和高度
    int buttonX = 635;
    int buttonY = 150;
    int buttonW = 150;
    int buttonH = 50;

    //得分
    //黑方的积分
    int score1 = 0;
    //白方的积分
    int score2 = 0;

    //保存每个棋子的二维数组
    //棋子坐标
    int x;
    int y;
    //0表示当前位置没有棋子,1表示黑棋,2表示白棋
    int[][] chess = new int[16][16];
    //判断当前棋子是黑棋还是白棋
    boolean isBlack = true;
    //判断游戏是否开始
    boolean isStart = true;

    //悔棋操作确定要悔的棋子的位置
    int jx = -1;
    int jy = -1;


    //构造方法
    public GameFrame() {

        setTitle("Java课程设计");
        //窗口大小
        setBounds(550, 200, 850, 620);
        //界面不可拉伸
        setResizable(false);
        //游戏界面可关闭
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //添加鼠标监听
        addMouseListener(this);
        //可见
        setVisible(true);
        //初始化游戏
        init();
    }


    //初始化游戏
    public void init() {
        //遍历数组,让棋盘棋子全部清空
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                chess[i][j] = 0;
            }
        }

        isBlack = true;
        isStart = true;
        //time = 60;
    }


    //游戏界面
    @Override
    //重写paint方法
    public void paint(Graphics g) {

        //双缓冲技术防止屏幕闪烁
        BufferedImage bufferedImage = new BufferedImage(850, 620, BufferedImage.TYPE_INT_ARGB);
        //构建一只新的画笔
        Graphics g2 = bufferedImage.createGraphics();
        super.paint(g2);

        //画棋盘
        //设置画笔颜色
        g2.setColor(Color.ORANGE);
        //矩形背景
        g2.fillRect(chessX, chessY, chessW, chessH);
        //绘制15*15棋盘
        g2.setColor(Color.black);
        for (int i = 0; i <= chessW; i += 35) {
            //画一条横线
            g2.drawLine(chessX, i + chessY, chessX + chessW, i + chessY);
            //画一条竖线
            g2.drawLine(i + chessX, chessY, i + chessX, chessY + chessH);
        }

        //画标志点
        //四个角的点
        for (int i = 4; i <= 12; i += 8) {
            for (int j = 4; j <= 12; j += 8) {
                g2.fillOval(35 * i + chessX - 5, 35 * j + chessY - 5, 10, 10);
            }
        }
        //中心的点
        g2.fillOval(35 * 8 + chessX - 5, 35 * 8 + chessY - 5, 10, 10);


        //游戏操作界面
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("华文行楷", 10, 50));
        g2.drawString("五子棋游戏", 590, 100);

        //开始按钮
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(buttonX, buttonY, buttonW, buttonH);
        g2.setColor(Color.black);
        g2.setFont(new Font("华文行楷", 10, 35));
        g2.drawString("开始", 675, 185);

        //悔棋按钮
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(buttonX, buttonY + 80, buttonW, buttonH);
        g2.setColor(Color.black);
        g2.setFont(new Font("华文行楷", 10, 35));
        g2.drawString("悔棋", 675, 265);

        //认输按钮
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(buttonX, buttonY + 160, buttonW, buttonH);
        g2.setColor(Color.black);
        g2.setFont(new Font("华文行楷", 10, 35));
        g2.drawString("认输", 675, 345);

        //得分
        g2.setColor(Color.black);
        g2.setFont(new Font("微软雅黑", 10, 25));
        g2.drawString("黑棋得分：" + score1, 650, 425);
        g2.drawString("白棋得分：" + score2, 650, 475);


        //画棋子
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                if (chess[i][j] == 1) {
                    //黑子
                    int cx = i * 35 + chessX;
                    int cy = j * 35 + chessY;
                    g2.setColor(Color.BLACK);
                    g2.fillOval(cx - 13, cy - 13, 26, 26);
                    //播放落子音乐
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            playMusic2();
                        }
                    }).start();
                }
                if (chess[i][j] == 2) {
                    //白子
                    int cx = i * 35 + chessX;
                    int cy = j * 35 + chessY;
                    g2.setColor(Color.BLACK);
                    g2.drawOval(cx - 13, cy - 13, 26, 26);
                    g2.setColor(Color.WHITE);
                    g2.fillOval(cx - 13, cy - 13, 26, 26);
                    //播放落子音乐
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            playMusic2();
                        }
                    }).start();
                }
            }
        }
        g.drawImage(bufferedImage, 0, 0, this);
    }


    //判断输赢
    public boolean isWin() {
        boolean flag = false;
        //判断棋子颜色
        int color = chess[x][y];

        //判断横向是否五子连珠
        //判断右边
        int i = 1;
        //棋子计数器
        int count = 1;
        while (color == chess[x + i][y]) {
            count++;
            i++;
        }
        //判断左边
        i = 1;
        while (color == chess[x - i][y]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
            //五子相连则播放音乐
            new Thread(new Runnable() {

                @Override
                public void run() {
                    playMusic1();
                }
            }).start();
        }

        //判断纵向是否五子连珠
        //判断上边
        i = 1;
        count = 1;
        while (color == chess[x][y - i]) {
            count++;
            i++;
        }
        //判断下边
        i = 1;
        while (color == chess[x][y + i]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
            //五子相连则播放音乐
            new Thread(new Runnable() {

                @Override
                public void run() {
                    playMusic1();
                }
            }).start();
        }

        //判断斜向是否五子连珠
        //判断左上边
        i = 1;
        count = 1;
        while (color == chess[x - i][y - i]) {
            count++;
            i++;
        }
        //判断右下边
        i = 1;
        while (color == chess[x + i][y + i]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
            //五子相连则播放音乐
            new Thread(new Runnable() {

                @Override
                public void run() {
                    playMusic1();
                }
            }).start();
        }

        //判断斜向是否五子连珠
        //判断右上边
        i = 1;
        count = 1;
        while (color == chess[x + i][y - i]) {
            count++;
            i++;
        }
        //判断左下边
        i = 1;
        while (color == chess[x - i][y + i]) {
            count++;
            i++;
        }
        if (count >= 5) {
            flag = true;
            //五子相连则播放音乐
            new Thread(new Runnable() {

                @Override
                public void run() {
                    playMusic1();
                }
            }).start();
        }

        return flag;
    }


    @Override
    //鼠标按下操作
    public void mousePressed(MouseEvent e) {
        //获取鼠标位置
        x = e.getX();
        y = e.getY();

        if (isStart == true) {
            //先判断鼠标是否在棋盘上
            if (x > chessX + 20 && x < chessX + chessW && y > chessY + 20 && y < chessY + chessH) {
                if ((x - 20) % 35 > 13) {
                    x = (x - 20) / 35 + 1;
                } else {
                    x = (x - 20) / 35;
                }

                if ((y - 40) % 35 > 3) {
                    y = (y - 40) / 35 + 1;
                } else {
                    y = (y - 40) / 35;
                }

                //判断当前位置是否有棋子
                if (chess[x][y] == 0) {
                    //判断谁该下棋了
                    if (isBlack) {
                        chess[x][y] = 1;
                        jx = x;
                        jy = y;
                        isBlack = false;
                    } else {
                        chess[x][y] = 2;
                        jx = x;
                        jy = y;
                        isBlack = true;
                    }
                    this.repaint();

                    if (isWin()) {
                        //如果一方赢了,积分+1,且重新开始游戏
                        //弹出胜利对话框
                        JOptionPane.showMessageDialog(this, "游戏结束，" + (chess[x][y] == 1 ? "黑方得一分！" : "白方得一分！"),"对局信息",JOptionPane.INFORMATION_MESSAGE);

                        if (chess[x][y] == 1) {
                            score1 = score1 + 1;
                            init();
                            if(score1==5) {
                                JOptionPane.showMessageDialog(this,"黑方胜利","对局信息",JOptionPane.INFORMATION_MESSAGE);
                                isStart=false;
                                score1 = 0;
                                score2 = 0;
                            }
                        } else if (chess[x][y] == 2) {
                            score2 = score2 + 1;
                            init();
                            if(score2==5) {
                                JOptionPane.showMessageDialog(this,"白方胜利","对局信息",JOptionPane.INFORMATION_MESSAGE);
                                isStart=false;
                                score1 = 0;
                                score2 = 0;
                            }
                        }

                        isStart = false;
                        this.repaint();
                    }
                } else {
                    //跳出一个弹窗
                    JOptionPane.showMessageDialog(this, "该位置已经有棋子了，请选择位置重新下！","对局信息",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }


        //开始按钮
        if (e.getX() > buttonX && e.getX() < buttonX + buttonW && e.getY() > buttonY && e.getY() < buttonY + buttonH) {
            //判断游戏是否开始
            if (isStart == false) {
                isStart = true;
                JOptionPane.showMessageDialog(this, "游戏重新开始！","对局信息",JOptionPane.INFORMATION_MESSAGE);
                init();
                this.repaint();

            } else {
                JOptionPane.showMessageDialog(this, "游戏重新开始！","对局信息",JOptionPane.INFORMATION_MESSAGE);
                init();
                this.repaint();

            }
        }


        //悔棋按钮  只能悔一步
        if (e.getX() > buttonX && e.getX() < buttonX + buttonW && e.getY() > buttonY + 80 && e.getY() < buttonY + buttonH + 80) {

            if (jx == -1) {
                JOptionPane.showMessageDialog(this, "没有要悔的棋子哦！","对局信息",JOptionPane.INFORMATION_MESSAGE);
            } else {
                chess[jx][jy] = 0;
                if (isBlack == true) {
                    isBlack = false;
                } else {
                    isBlack = true;
                }
                this.repaint();
                JOptionPane.showMessageDialog(this,"悔棋成功","对局信息",JOptionPane.INFORMATION_MESSAGE);
            }
        }


        //认输按钮
        if (e.getX() > buttonX && e.getX() < buttonX + buttonW && e.getY() > buttonY + 160 && e.getY() < buttonY + buttonH + 160) {
            //判断游戏是否开始
            if (isStart == true) {
                //判断是谁认输
                if (isBlack == true) {
                    score2 = score2 + 1;
                    init();
                    this.repaint();
                    JOptionPane.showMessageDialog(this, "本局黑方认输，白方获胜！","对局信息",JOptionPane.INFORMATION_MESSAGE);

                } else if (isBlack == false) {
                    score1 = score1 + 1;
                    init();
                    this.repaint();
                    JOptionPane.showMessageDialog(this, "本局白方认输，黑方获胜！" ,"对局信息",JOptionPane.INFORMATION_MESSAGE);



                }
            } else {
                JOptionPane.showMessageDialog(this, "请先重新开始游戏！","对局信息",JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    //设置五子相连背景音乐的方法
    public static void playMusic1() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("E:\\Node\\java课程设计/五子相连.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            //value用来设置音量
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {
                nByte = ais.read(buffer, 0, SIZE);
                sdl.write(buffer, 0, nByte);
            }
            sdl.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //设置落子背景音乐的方法
    public static void playMusic2() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("E:\\Node\\java课程设计/落子.wav"));
            AudioFormat aif = ais.getFormat();
            final SourceDataLine sdl;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aif);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(aif);
            sdl.start();
            FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
            //value用来设置音量
            double value = 2;
            float dB = (float) (Math.log(value == 0.0 ? 0.0001 : value) / Math.log(10.0) * 20.0);
            fc.setValue(dB);
            int nByte = 0;
            final int SIZE = 1024 * 64;
            byte[] buffer = new byte[SIZE];
            while (nByte != -1) {
                nByte = ais.read(buffer, 0, SIZE);
                sdl.write(buffer, 0, nByte);
            }
            sdl.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}


