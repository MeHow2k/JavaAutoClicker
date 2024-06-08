package pl.mehow2k;

import com.melloware.jintellitype.JIntellitype;

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
            labelRemainingClicksCounterText,labelAuthor,labelStartHotkey,labelStopHotkey;
    JButton buttonMouseRight,buttonMouseScroll,buttonMouseLeft,buttonStart,buttonStop;
    JTextArea textSLEEP_PRESS,textSLEEP_RELEASE,textCLICKS_NUMBER;
    JCheckBox checkBoxClicksNumber;JComboBox<String> comboBoxStartHotkey;JComboBox<String> comboBoxStopHotkey;
    int currentStartHotkey = KeyEvent.VK_F6; // default hotkey
    int currentStopHotkey = KeyEvent.VK_F7; // default hotkey
    Panel() {
        super(null);
        labelAuthor = new JLabel("AutoClicker v1.0.1 by MeHow2k         2024              " +
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
        textSLEEP_PRESS.setToolTipText("Time in milliseconds after which mouse button will release. " +
                "Time at which button is being pressed.");
        add(textSLEEP_PRESS);

        labelSleepRelease = new JLabel("Choose sleep time after release (ms): ");
        labelSleepRelease.setBounds(20,95,300,30);
        add(labelSleepRelease);

        textSLEEP_RELEASE=new JTextArea("400");
        textSLEEP_RELEASE.setBounds(250,100,100,20);
        textSLEEP_RELEASE.setToolTipText("Time in milliseconds after which mouse button will be pressed again. " +
                "Delay between clicks.");
        add(textSLEEP_RELEASE);

        labelClicks = new JLabel("Choose number of clicks: ");
        labelClicks.setBounds(20,125,300,30);
        add(labelClicks);

        textCLICKS_NUMBER=new JTextArea("0");
        textCLICKS_NUMBER.setBounds(250,130,100,20);
        textCLICKS_NUMBER.setToolTipText("Number of clicks, that will be performed.");
        textCLICKS_NUMBER.setEditable(false);
        textCLICKS_NUMBER.setEnabled(false);
        add(textCLICKS_NUMBER);

        checkBoxClicksNumber = new JCheckBox();
        checkBoxClicksNumber.setBounds(380,130,20,20);
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
        labelRemainingClicksCounterText.setBounds(40,200,300,30);
        labelRemainingClicksCounterText.setFont(new Font("Arial",Font.CENTER_BASELINE,20));
        labelRemainingClicksCounterText.setEnabled(false);
        add(labelRemainingClicksCounterText);

        labelRemainingClicksCounter = new JLabel("");
        labelRemainingClicksCounter.setBounds(240,200,300,30);
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

        labelStartHotkey = new JLabel("Start hotkey: ALT +");
        labelStartHotkey.setBounds(20,C.FRAME_WIDTH-295,400,30);
        add(labelStartHotkey);

        comboBoxStartHotkey = new JComboBox<>(new String[]{"ESC", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"});
        comboBoxStartHotkey.setBounds(C.FRAME_WIDTH-320, C.FRAME_HEIGHT-245, 60, 30);
        comboBoxStartHotkey.setSelectedItem("F6");
        comboBoxStartHotkey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedHotkey = KeyEvent.VK_ESCAPE;
                switch ((String) comboBoxStartHotkey.getSelectedItem()) {
                    case "F1":
                        selectedHotkey = KeyEvent.VK_F1;
                        break;
                    case "F2":
                        selectedHotkey = KeyEvent.VK_F2;
                        break;
                    case "F3":
                        selectedHotkey = KeyEvent.VK_F3;
                        break;
                    case "F4":
                        selectedHotkey = KeyEvent.VK_F4;
                        break;
                    case "F5":
                        selectedHotkey = KeyEvent.VK_F5;
                        break;
                    case "F6":
                        selectedHotkey = KeyEvent.VK_F6;
                        break;
                    case "F7":
                        selectedHotkey = KeyEvent.VK_F7;
                        break;
                    case "F8":
                        selectedHotkey = KeyEvent.VK_F8;
                        break;
                    case "F9":
                        selectedHotkey = KeyEvent.VK_F9;
                        break;
                    case "F10":
                        selectedHotkey = KeyEvent.VK_F10;
                        break;
                    case "F11":
                        selectedHotkey = KeyEvent.VK_F11;
                        break;
                    case "F12":
                        selectedHotkey = KeyEvent.VK_F12;
                        break;
                }
                updateStartHotkey(selectedHotkey);
            }
        });
        add(comboBoxStartHotkey);

        labelStopHotkey = new JLabel("Stop hotkey: ALT +");
        labelStopHotkey.setBounds(C.FRAME_WIDTH-250,C.FRAME_WIDTH-295,400,30);
        add(labelStopHotkey);

        comboBoxStopHotkey = new JComboBox<>(new String[]{"ESC", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"});
        comboBoxStopHotkey.setBounds(C.FRAME_WIDTH-140, C.FRAME_HEIGHT-245, 60, 30);
        comboBoxStopHotkey.setSelectedItem("F7");
        comboBoxStopHotkey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedHotkey = KeyEvent.VK_F7;
                switch ((String) comboBoxStopHotkey.getSelectedItem()) {
                    case "F1":
                        selectedHotkey = KeyEvent.VK_F1;
                        break;
                    case "F2":
                        selectedHotkey = KeyEvent.VK_F2;
                        break;
                    case "F3":
                        selectedHotkey = KeyEvent.VK_F3;
                        break;
                    case "F4":
                        selectedHotkey = KeyEvent.VK_F4;
                        break;
                    case "F5":
                        selectedHotkey = KeyEvent.VK_F5;
                        break;
                    case "F6":
                        selectedHotkey = KeyEvent.VK_F6;
                        break;
                    case "F7":
                        selectedHotkey = KeyEvent.VK_F7;
                        break;
                    case "F8":
                        selectedHotkey = KeyEvent.VK_F8;
                        break;
                    case "F9":
                        selectedHotkey = KeyEvent.VK_F9;
                        break;
                    case "F10":
                        selectedHotkey = KeyEvent.VK_F10;
                        break;
                    case "F11":
                        selectedHotkey = KeyEvent.VK_F11;
                        break;
                    case "F12":
                        selectedHotkey = KeyEvent.VK_F12;
                        break;
                }
                updateStopHotkey(selectedHotkey);
            }
        });
        add(comboBoxStopHotkey);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if(robot!=null){
                System.out.println("Releasing on exit!");
                robot.mouseRelease(button);
                }
            }
        });

        JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_ALT, currentStartHotkey);
        JIntellitype.getInstance().addHotKeyListener(identifier -> {
            if (identifier == 1) {
                StartClicker();
            }
        });
        JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT, currentStopHotkey);
        JIntellitype.getInstance().addHotKeyListener(identifier -> {
            if (identifier == 2) {
                StopClicker();
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
    private void updateStartHotkey(int newHotkey) {
        JIntellitype.getInstance().unregisterHotKey(1);
        currentStartHotkey = newHotkey;
        JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_ALT, currentStartHotkey);
    }
    private void updateStopHotkey(int newHotkey) {
        JIntellitype.getInstance().unregisterHotKey(2);
        currentStopHotkey = newHotkey;
        JIntellitype.getInstance().registerHotKey(2, JIntellitype.MOD_ALT, currentStopHotkey);
    }
    public void StartClicker(){
        System.out.println("Call: clicker start!");
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
        System.out.println("Call: clicker stop!");
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
