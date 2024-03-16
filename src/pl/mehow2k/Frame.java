package pl.mehow2k;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    Frame(){
        super("Auto Clicker");
        Image icon = new ImageIcon(getClass().getClassLoader().getResource("icon.png")).getImage();
        setIconImage(icon);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(C.FRAME_WIDTH,C.FRAME_HEIGHT);
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - getSize().width) / 2,
                (Toolkit.getDefaultToolkit().getScreenSize().height - getSize().height) / 2);
        Panel panel=new Panel();
        setContentPane(panel);
        panel.setFocusable(true);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
