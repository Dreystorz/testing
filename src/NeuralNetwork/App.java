package NeuralNetwork;
public class App {
    public static void main(String[] args) throws Exception {
        final double x[][] = {
            {0,0,0,0},
            {0,1,0,0},
            {1,0,0,0},
            {1,1,0,0},
            {0,0,0,1},
            {0,1,0,1},
            {1,0,0,1},
            {1,1,0,1},
            {0,0,1,0},
            {0,1,1,0},
            {1,0,1,0},
            {1,1,1,0},
            {0,0,1,1},
            {0,1,1,1},
            {1,0,1,1},
            {1,1,1,1}
        };
        final double y[][] = {
            {0,0,0},
            {0,0,1},
            {0,1,0},
            {0,1,1},
            {0,0,1},
            {0,1,0},
            {0,1,1},
            {1,0,0},
            {0,1,0},
            {0,1,1},
            {1,0,0},
            {1,0,1},
            {0,1,1},
            {1,0,0},
            {1,0,1},
            {1,1,0}
        };

        new SimFrame(new NeuralNetwork(4,"7,5",3),x,y,50000);
        new SimFrame(new NeuralNetwork(4,"10,5",3),x,y,50000);
    }
}