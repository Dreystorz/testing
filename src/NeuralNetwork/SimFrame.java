package NeuralNetwork;
import javax.swing.*;;

public class SimFrame extends JFrame{
    SimFrame(NeuralNetwork n, double[][] input, double[][] expectedOut, int epochs){
        this.add(new SimPanel(n,input,expectedOut, epochs));
        this.setTitle("Neural Network");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
