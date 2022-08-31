package NeuralNetwork;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Matrix{
    double data[][];
    int row;
    int col;

    public Matrix(int row, int col){
        this.row = row;
        this.col = col;
        this.data = new double[row][col];
    }

    public Matrix(String file) throws Exception{
        Scanner fromFile = new Scanner(new File(file));
        this.row = fromFile.nextInt();
        this.col = fromFile.nextInt();
        this.data = new double[row][col];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                data[i][j] = fromFile.nextDouble();
            }
            System.out.println();
        }
    }

    public Matrix copy(){
        Matrix temp = new Matrix(this.row, this.col);
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                temp.data[r][c] = this.data[r][c];
            }
        }
        return temp;
    }

    public static Matrix combineRandom(Matrix a, Matrix b) throws Exception{
        if(a.col == b.col && a.row == b.row){
            Random random = new Random();
            Matrix temp = new Matrix(a.row,a.col);
            for(int r = 0; r < temp.row; r++){
                for(int c = 0; c < temp.col; c++){
                    if(random.nextBoolean()){
                        temp .data[r][c] = a.data[r][c];
                    }else temp .data[r][c] = b.data[r][c];
                }
            }
            return temp;
        }else throw new Exception();
    }

    public void loadRandom(double min, double max){
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                data[r][c] = min + (max - min) * Math.random();
            }
        }
    }

    public static Matrix multiply(Matrix a, Matrix b){ //throws Exception{
        if(a.col == b.row){
            Matrix result = new Matrix(a.row,b.col);
            for(int i = 0; i < a.row; i++){
                for(int j = 0; j < b.col; j++){
                    double temp = 0;
                    for(int k = 0; k < a.col; k++){
                        temp += a.data[i][k] * b.data[k][j];
                        result.data[i][j] = temp; 
                    }
                }
            }
            return result;
        }else  System.out.println("ERROR");//throw new Exception();
        return new Matrix(3,3);
    }

    public void multiply(double value){
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                data[r][c] *= value;
            }
        }
    }

    public void multiply(Matrix a) {
        for(int i = 0; i < a.row; i++)
        {
            for(int j = 0; j < a.col; j++)
            {
                this.data[i][j] *= a.data[i][j];
            }
        }
        
    }

    public static Matrix add(Matrix a, Matrix b) throws Exception{
        if((a.row == b.row)&&(a.col == b.col)){
            Matrix result = new Matrix(a.row, a.col);
            for(int r = 0; r < a.row; r++){
                for(int c = 0; c < a.col; c++){
                    result.data[r][c] = a.data[r][c] + b.data[r][c];
                }
            }
            return result;
        }else throw new Exception();
    }

    public void add(double value){
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                data[r][c] += value;
            }
        }
    }

    public static Matrix subtract(Matrix a, Matrix b) throws Exception{
        if((a.row == b.row)&&(a.col == b.col)){
            Matrix result = new Matrix(a.row, a.col);
            for(int r = 0; r < a.row; r++){
                for(int c = 0; c < a.col; c++){
                    result.data[r][c] = a.data[r][c] - b.data[r][c];
                }
            }
            return result;
        }else throw new Exception();
    }

    public Matrix transpose(){
        Matrix result = new Matrix(col,row);
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                result.data[c][r] = data[r][c];
            }
        }
        return result;
    }

    public void display(){
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                System.out.print(data[r][c] + " ");
            }
            System.out.println();
        }
    System.out.println();
    }

    public ArrayList<Double> toArray(){
        ArrayList<Double> output = new ArrayList<Double>();
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                output.add(data[r][c]);
            }
        }
        return output;
    }

    public static Matrix fromArray(double input[]){
        Matrix output = new Matrix(input.length,1);
        for(int i = 0; i < input.length; i++){
            output.data[i][0] = input[i];
        }
        return output;
    }

    public void sigmoid(){
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                data[r][c] = 1/(1 + Math.exp(-data[r][c]));
            }
        }
    }

    public Matrix dsigmoid(){
        Matrix output = new Matrix(row,col);
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                output.data[r][c] = data[r][c] * (1 - data[r][c]);
            }
        }
        return output;
    }
}