package NeuralNetwork;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.*;

public class SimPanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HIGHT = 600;
    static final int UNIT_SIZE = 5;
    static final int SIM_UNITS = (SCREEN_WIDTH*SCREEN_HIGHT)/UNIT_SIZE;
    static final int DELAY = 5;
    boolean running = false, begin = false;
    Timer timer;
    NeuralNetwork n;
    int x_width, y_hight;
    double[][] input, expectedOut;
    int epochs, curEpoch = 0;

    SimPanel(NeuralNetwork n, double[][] input, double[][] expectedOut, int epochs){
        this.n = n;
        this.input = input;
        this.expectedOut = expectedOut;
        this.epochs = epochs;
        x_width = SCREEN_WIDTH;
        y_hight = SCREEN_HIGHT;
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startSim();
    }

    public void startSim(){
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        Dimension curSize = getSize();
        x_width = curSize.width;
        y_hight = curSize.height;
        if(!running){
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Finished", (x_width-metrics.stringWidth("Finished"))/2, 20);
        }
        g.setColor(Color.green);
        for(int i = 1; i < n.iSize+1; i++){
            g.fillOval((x_width/(n.wSizes.length+2)), i*(y_hight/(n.iSize+1)), UNIT_SIZE, UNIT_SIZE);
        }
        int prev_size = n.iSize;
        for(int i = 2; i < n.wSizes.length+2; i++){
            for(int j = 1; j < n.wSizes[i-2]+1; j++){
                if(i == n.wSizes.length+1){
                    g.setColor(Color.red);
                }else g.setColor(Color.blue);
                g.fillOval(i*(x_width/(n.wSizes.length+2)), j*(y_hight/(n.wSizes[i-2]+1)), UNIT_SIZE, UNIT_SIZE);
                for(int k = 1; k < prev_size+1; k++){
                    int c = (int)(255*(1/(1+Math.exp(-n.weights[i-2].data[j-1][k-1]))));
                    if(begin&&running){
                        int f = (int)(255*(1/(1+Math.exp(-n.results[i-2].data[j-1][0]))));
                        if(f > 180) g.setColor(new Color(204,0,0));
                        else if(f > 160 && f <= 180) g.setColor(new Color(255,0,0));
                        else if(f > 155 && f <= 160) g.setColor(new Color(255,51,51));
                        else if(f > 150 && f <= 155) g.setColor(new Color(255,102,102));
                        else if(f > 145 && f <= 150) g.setColor(new Color(255,153,153));
                        else if(f > 140 && f <= 145) g.setColor(new Color(255,204,204));
                        else g.setColor(new Color(0,0,0));
                    } else g.setColor(new Color(c,c,c));
                    g.drawLine(((x_width/(n.wSizes.length+2))*(i-1))+(UNIT_SIZE/2), (k*(y_hight/(prev_size+1)))+(UNIT_SIZE/2), 
                    ((x_width/(n.wSizes.length+2))*i)+(UNIT_SIZE/2), (j*(y_hight/(n.wSizes[i-2]+1)))+(UNIT_SIZE/2));
                }
            }
            prev_size = n.wSizes[i-2];
        }
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free",Font.BOLD,20));
        FontMetrics m = getFontMetrics(g.getFont());
        g.drawString("Epoch: "+(curEpoch), (x_width-m.stringWidth("Epoch: "+(curEpoch)))/2, 50);
    }

    public void simulate(){
        if((curEpoch<epochs) && begin){
            int sampleN =  (int)(Math.random() * input.length );
            n.train(input[sampleN], expectedOut[sampleN]);
            repaint();
            curEpoch++;
        } else if(begin)running = false;
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        //if(running){
            simulate();
        //}
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            if(KeyEvent.VK_SPACE == e.getKeyCode()){
                if(!begin) begin = true;
                else begin = false;
            }
        }
    }
}