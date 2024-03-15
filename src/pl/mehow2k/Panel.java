package pl.mehow2k;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Panel extends JPanel {
    Thread thread;
    public static boolean isRunning =false;
    public static boolean isClicksNumberEnabled =false;
    static Robot robot;
    static int button;
    JLabel labelButton,labelSleepPress,labelSleepRelease,labelClicks,labelRemainingClicksCounter,
            labelRemainingClicksCounterText,labelAuthor;
    JButton buttonMouseRight,buttonMouseScroll,buttonMouseLeft,buttonStart,buttonStop;
    JTextArea textSLEEP_PRESS,textSLEEP_RELEASE,textCLICKS_NUMBER;
    JCheckBox checkBoxClicksNumber;
    Panel() {
        super(null);
        labelAuthor = new JLabel("AutoClicker by MeHow2k                                           " +
                "github.com/MeHow2k ");
        labelAuthor.setBounds(10,C.FRAME_HEIGHT-70,400,30);
        add(labelAuthor);

        labelButton = new JLabel("Choose mouse button: ");
        labelButton.setBounds(20,20,200,30);
        add(labelButton);

        buttonMouseLeft= new JButton("Left");
        buttonMouseLeft.setBounds(180,15,70,40);
        buttonMouseLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                C.BUTTON_ID=1;
                buttonMouseLeft.setEnabled(false);
                buttonMouseRight.setEnabled(true);
                buttonMouseScroll.setEnabled(true);
                System.out.println("Switched mouse button to LEFT");
            }
        });
        add(buttonMouseLeft);
        buttonMouseScroll= new JButton("Scroll");
        buttonMouseScroll.setBounds(250,15,70,40);
        buttonMouseScroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                C.BUTTON_ID=2;
                buttonMouseScroll.setEnabled(false);
                buttonMouseRight.setEnabled(true);
                buttonMouseLeft.setEnabled(true);
                System.out.println("Switched mouse button to SCROLL");
            }
        });
        add(buttonMouseScroll);
        buttonMouseRight= new JButton("Right");
        buttonMouseRight.setBounds(320,15,70,40);
        buttonMouseRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                C.BUTTON_ID=3;
                buttonMouseRight.setEnabled(false);
                buttonMouseLeft.setEnabled(true);
                buttonMouseScroll.setEnabled(true);
                System.out.println("Switched mouse button to RIGHT");
            }
        });
        add(buttonMouseRight);


        labelSleepPress = new JLabel("Choose sleep time after press (ms): ");
        labelSleepPress.setBounds(20,70,300,30);
        add(labelSleepPress);

        textSLEEP_PRESS=new JTextArea("400");
        textSLEEP_PRESS.setBounds(250,75,100,20);
        textSLEEP_PRESS.setToolTipText("Time in milliseconds after which mouse button will release.\n" +
                "Time at which button is being pressed.");
        add(textSLEEP_PRESS);

        labelSleepRelease = new JLabel("Choose sleep time after release (ms): ");
        labelSleepRelease.setBounds(20,100,300,30);
        add(labelSleepRelease);

        textSLEEP_RELEASE=new JTextArea("400");
        textSLEEP_RELEASE.setBounds(250,105,100,20);
        textSLEEP_RELEASE.setToolTipText("Time in milliseconds after which mouse button will be pressed again.\n" +
                "Delay between clicks.");
        add(textSLEEP_RELEASE);

        labelClicks = new JLabel("Choose number of clicks: ");
        labelClicks.setBounds(20,140,300,30);
        add(labelClicks);

        textCLICKS_NUMBER=new JTextArea("0");
        textCLICKS_NUMBER.setBounds(250,145,100,20);
        textCLICKS_NUMBER.setToolTipText("Number of clicks, that will be performed.");
        textCLICKS_NUMBER.setEditable(false);
        textCLICKS_NUMBER.setEnabled(false);
        add(textCLICKS_NUMBER);

        checkBoxClicksNumber = new JCheckBox();
        checkBoxClicksNumber.setBounds(380,145,20,20);
        checkBoxClicksNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkBoxClicksNumber.isSelected()){
                    textCLICKS_NUMBER.setEnabled(true);
                    textCLICKS_NUMBER.setEditable(true);
                    labelRemainingClicksCounterText.setEnabled(true);
                    labelRemainingClicksCounter.setText(String.valueOf(C.CLICKS_NUMBER));
                    isClicksNumberEnabled=true;

                }else{
                    textCLICKS_NUMBER.setEnabled(false);
                    textCLICKS_NUMBER.setEditable(false);
                    labelRemainingClicksCounterText.setEnabled(false);
                    isClicksNumberEnabled=false;
                    labelRemainingClicksCounter.setText("");
                    C.CLICKS_NUMBER=0;
                }
            }
        });
        add(checkBoxClicksNumber);

        labelRemainingClicksCounterText = new JLabel("Remaining clicks: ");
        labelRemainingClicksCounterText.setBounds(40,190,300,30);
        labelRemainingClicksCounterText.setFont(new Font("Arial",Font.CENTER_BASELINE,20));
        labelRemainingClicksCounterText.setEnabled(false);
        add(labelRemainingClicksCounterText);

        labelRemainingClicksCounter = new JLabel("");
        labelRemainingClicksCounter.setBounds(240,190,300,30);
        labelRemainingClicksCounter.setFont(new Font("Arial",Font.CENTER_BASELINE,20));
        labelRemainingClicksCounter.setForeground(Color.BLUE);
        add(labelRemainingClicksCounter);

        buttonStart = new JButton("Start");
        buttonStart.setBounds(C.FRAME_HEIGHT-300,C.FRAME_WIDTH-200,100,50);
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StartClicker();
            }
        });
        add(buttonStart);

        buttonStop = new JButton("Stop");
        buttonStop.setBounds(C.FRAME_HEIGHT-150,C.FRAME_WIDTH-200,100,50);
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StopClicker();
            }
        });
        add(buttonStop);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if(robot!=null){
                System.out.println("Releasing on exit!");
                robot.mouseRelease(button);
                }
            }
        });
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if(isRunning) {
                            System.out.println("Running...");
                            try {
                                if(isClicksNumberEnabled) {
                                    if( C.CLICKS_NUMBER<=0){
                                        StopClicker();
                                        System.out.println("Stopped: all clicks has been performed.");
                                    }
                                }
                                robot = new Robot();
                                if(C.BUTTON_ID==3) button = InputEvent.BUTTON3_DOWN_MASK;//right click
                                if(C.BUTTON_ID==2) button = InputEvent.BUTTON2_DOWN_MASK;//scroll click
                                if(C.BUTTON_ID==1) button = InputEvent.BUTTON1_DOWN_MASK;//left click
                                if(isRunning) {
                                    robot.mousePress(button);
                                    System.out.println("Pressed");
                                    Thread.sleep(C.SLEEP_AFTER_PRESS);
                                    robot.mouseRelease(button);
                                    System.out.println("Released");
                                    Thread.sleep(C.SLEEP_AFTER_RELEASE);
                                }
                                if(C.CLICKS_NUMBER!=0) {
                                    C.CLICKS_NUMBER--;
                                    System.out.println("Remaining clicks: "+C.CLICKS_NUMBER);
                                    labelRemainingClicksCounter.setText(String.valueOf(C.CLICKS_NUMBER));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
            thread.start();
    }
    public void StartClicker(){
        boolean err=false;
        if(C.BUTTON_ID==0) {
            JOptionPane.showMessageDialog(this, "You need to select mouse button before start!",
                    "Warning",JOptionPane.ERROR_MESSAGE);
        }
        else {
            try {
                C.SLEEP_AFTER_PRESS = Integer.parseInt(textSLEEP_PRESS.getText());
                C.SLEEP_AFTER_RELEASE = Integer.parseInt(textSLEEP_RELEASE.getText());
                if(isClicksNumberEnabled) {
                    if(Integer.parseInt(textCLICKS_NUMBER.getText())==0) {
                        JOptionPane.showMessageDialog(this, "Number of clicks must be greater than 0!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        err=true;
                    }
                    if(!err) C.CLICKS_NUMBER = Integer.parseInt(textCLICKS_NUMBER.getText());
                }
            } catch (NumberFormatException nfe) {
                err=true;
                JOptionPane.showMessageDialog(this, "You need write number!",
                        "Warning",JOptionPane.ERROR_MESSAGE);
                System.out.println("Bad input error: " + nfe);
            }
            if(!err){
            if(isClicksNumberEnabled) labelRemainingClicksCounter.setText(String.valueOf(C.CLICKS_NUMBER));
            isRunning = true;
            buttonStop.setEnabled(true);
            buttonStart.setEnabled(false);
            buttonMouseLeft.setEnabled(false);
            buttonMouseScroll.setEnabled(false);
            buttonMouseRight.setEnabled(false);
            checkBoxClicksNumber.setEnabled(false);
            }
        }
    }
    public void StopClicker(){
        isRunning=false;
        buttonStop.setEnabled(false);
        buttonStart.setEnabled(true);
        checkBoxClicksNumber.setEnabled(true);
        if(C.BUTTON_ID==1){
            buttonMouseScroll.setEnabled(true);
            buttonMouseRight.setEnabled(true);
        }
        if(C.BUTTON_ID==2){
            buttonMouseLeft.setEnabled(true);
            buttonMouseRight.setEnabled(true);
        }
        if(C.BUTTON_ID==3){
            buttonMouseScroll.setEnabled(true);
            buttonMouseLeft.setEnabled(true);
        }

    }

}
