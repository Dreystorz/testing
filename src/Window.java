import javax.swing.*;

public class Window extends JFrame{
    public Window(){
        createWindow();
    }

    private void createWindow(){
        add(new Level());
        setTitle("Testing");
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}
