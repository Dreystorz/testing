package NeuralNetwork;
import java.util.Random;

public class NeuralNetwork {
    Matrix[] weights;
    Matrix[] results;
    int iSize;
    int oSize;
    int wSizes[];
    double weightUpperBound = 1;
    double weightLowerBound = -1;
    double learning_rate = 0.1;
    double mutationChance = 0.01, offspringChance = 0.5;
    int score = 0;
    Random random;

    public NeuralNetwork(int iSize, String hidden,int oSize){
        String sizes[] = hidden.split(",");
        wSizes = new int[sizes.length+1];
        for(int i = 0; i < sizes.length; i++){
            wSizes[i] = Integer.parseInt(sizes[i]);
        }
        wSizes[sizes.length] = oSize;
        this.iSize = iSize;
        this.oSize = oSize;
        weights = new Matrix[wSizes.length];
        results = new Matrix[wSizes.length];
        int lastSize = iSize;
        for(int i = 0; i < wSizes.length; i++){
            int currSize = wSizes[i];
            weights[i] = new Matrix(currSize,lastSize);
            lastSize = currSize;
            weights[i].loadRandom(weightLowerBound, weightUpperBound);
        }
        random = new Random();
    }

    public NeuralNetwork(NeuralNetwork n){
        iSize = n.iSize;
        oSize = n.oSize;
        wSizes = new int[n.wSizes.length];
        for(int i = 0; i < n.wSizes.length; i++){
            this.wSizes[i] = n.wSizes[i];
        }
        weights = new Matrix[n.weights.length];
        for(int i = 0; i < weights.length; i++){
            weights[i] = n.weights[i].copy();
        }
        results = new Matrix[n.results.length];
        for(int i = 0; i < results.length; i++){
            results[i] = n.results[i].copy();
        }
        random = new Random();
    }

    public NeuralNetwork(){

    }

    public void displayNetwork(){
        System.out.println("Input layer size: " + iSize);
        for(int i = 0; i < wSizes.length; i++){
            weights[i].display();
            System.out.println();
        }
        System.out.println("\nOutput layer size: " + oSize);
    }

    public Matrix predict(double input[]) throws Exception{
        try{
            if(input.length != iSize) System.out.println("Incorrect input size!");
            Matrix in = Matrix.fromArray(input);
            Matrix out = Matrix.multiply(weights[0], in);
            out.sigmoid();
            results[0] = out;
            for(int i = 1; i < weights.length; i++){
                out = Matrix.multiply(weights[i], out);
                out.sigmoid();
                results[i] = out;
            }
            return out;
        }catch(Exception e){
            System.out.println("Error in predict");
            return new Matrix(oSize,0);
        }
    }

    public void train(double input[], double expectedOut[]){
        try{
            if(expectedOut.length != oSize) System.out.println("Incorrect output size!");
            this.predict(input);

            Matrix target = Matrix.fromArray(expectedOut);
            Matrix error = Matrix.subtract(target, results[results.length-1]);
            Matrix gradient;
            Matrix hidden;
            Matrix delta;

            for(int i = 1; i < results.length; i++){
                gradient = results[results.length-i].dsigmoid();
                gradient.multiply(error);
                gradient.multiply(learning_rate);

                hidden = results[results.length-i-1].transpose();
                delta = Matrix.multiply(gradient, hidden);

                weights[weights.length-i] = Matrix.add(weights[weights.length-i], delta);
        
                hidden = weights[weights.length-i].transpose();
                error = Matrix.multiply(hidden, error);
            }
            Matrix in = Matrix.fromArray(input);
            gradient = results[0].dsigmoid();
            gradient.multiply(error);
            gradient.multiply(learning_rate);

            hidden = in.transpose();
            delta = Matrix.multiply(gradient, hidden);

            weights[0] = Matrix.add(weights[0], delta);
        } catch(Exception e){ System.out.println("Error");}
    }

    public void fit(double[][]input,double[][]output,int epochs) throws Exception {
        for(int i=0;i<epochs;i++)
        {    
            int sampleN =  (int)(Math.random() * input.length );
            this.train(input[sampleN], output[sampleN]);
        }
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void mutate(){
        for(int i = 0; i < weights.length; i++){
            for(int r = 0; r < weights[i].row; r++){
                for(int c = 0; c < weights[i].col; c++){
                    if(random.nextInt((int)(1/mutationChance)+1) == 0){
                        weights[i].data[r][c] = weights[i].data[r][c]+random.nextGaussian();
                    }
                }
            }
        }
    }

    public static NeuralNetwork combine(NeuralNetwork a, NeuralNetwork b){
        try{
            NeuralNetwork temp = new NeuralNetwork();
            temp.iSize = a.iSize;
            temp.oSize = a.oSize;
            temp.wSizes = new int[a.wSizes.length];
            for(int i = 0; i < a.wSizes.length; i++){
                temp.wSizes[i] = a.wSizes[i];
            }
            temp.weights = new Matrix[a.weights.length];
            for(int i = 0; i < temp.weights.length; i++){
                temp.weights[i] = Matrix.combineRandom(a.weights[i], b.weights[i]);
            }
            temp.results = new Matrix[a.results.length];
            for(int i = 0; i < temp.results.length; i++){
                temp.results[i] = a.results[i].copy();
            }
            temp.random = new Random();

            return temp;
        }catch(Exception e){
            System.out.println(e);
            return new NeuralNetwork(a);
        }
    }

    public static NeuralNetwork[] geneticTrain(NeuralNetwork a[]){
        NeuralNetwork temp[] = new NeuralNetwork[a.length];
        Random rand = new Random();

        int topScore = a[0].getScore(), bottomScore = a[0].getScore();
        for (NeuralNetwork n : a) {
            if(n.getScore() > topScore) topScore = n.getScore();
            if(n.getScore() < bottomScore) bottomScore = n.getScore();
        }

        int networks = 0;
        while(networks < temp.length){
            for (int i = 0; i < temp.length && networks < temp.length; i++) {
                if(a[i].getScore() > (topScore-bottomScore)/2+bottomScore+(bottomScore/2)){
                    if(0 == rand.nextInt((int)(1/a[i].offspringChance))){
                        temp[networks] = NeuralNetwork.combine(a[i], a[rand.nextInt(a.length)]);
                    }else{
                        temp[networks] = new NeuralNetwork(a[i]);
                    }
                    temp[networks].mutate();
                    networks++;
                }
            }
        }

        for (NeuralNetwork n : a) {
            if (n == null) {
                System.out.println("temp has a null");
            }
        }

        return temp;
    }
}
