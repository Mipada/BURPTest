/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.app;

import java.awt.BorderLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import javax.swing.JPanel;

/**
 *
 * @author Peter
 */
public class BasicTwoDimensions extends BasicApp implements ActionListener{

    
    /**
     * @param args the command line arguments
     */
    static JFrame frame;
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("BURPTest.main()");
        frame = new JFrame("BURP Test");
        BorderLayout layout = new BorderLayout();
        frame.setLayout(layout);
		frame.setBounds(500, 50, 800, 600);//x, y, w, h
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultLookAndFeelDecorated(true);
		//frame.setMinimumSize(new Dimension(4 * 300, 3 * 300));//
        //frame.setLayout(layout);
        //
        int top = 50;
        int left = 500;
        int width = 800;
        int height = 600;
        BasicTwoDimensions app = new BasicTwoDimensions(left, top, width, height);
        app.setName("BURP Test");
		app.setMinimumSize(new Dimension(50, 50));//x, y, w, h
        //app.setLocation(100, 100);
        //app.setSize(50, 50);
        //app.setBounds(left, top, width, height);//not inited yet
        frame.setVisible(true);
        //
        //add first panal
        frame.add(app, BorderLayout.CENTER);
        //app.setVisible(true);
        frame.setVisible(true);
         
        app.init();
        app.start();
        app.setVisible(true);
        //System.exit(1);
    }
    
    
    protected BasicTwoDimensions app;
	protected Image offscreenImage;
	protected Graphics offscr;
    protected ButtonGroup autopilotGroup;
    protected JPanel autopilotPanel;
    //
    
    BasicTwoDimensions(){
        this(0, 0, 50, 50);
    }
    
    BasicTwoDimensions(int left, int top, int width, int height){
        super(left, top, width, height);
        System.out.println("BURPTest.BURPTest() ");
        
        app = this;
    }
    
    
    @Override
    public void init(){
        System.out.println("BURPTest.init()");
        this.app = this;
        setFPS(30);
        setBackground(Color.LIGHT_GRAY);
        //setBounds(top, left, width, height);
        setFocusTraversalKeysEnabled(false);
        this.setFocusable(true);
        this.requestFocus();
        setVisible(true);
        //doMakeOffscr(getWidth(), getHeight());
        addKeyListener(this);
        //add2DObjects();
        addGUI();
    }//end init
    
    
    public Insets getInsets(){
        return(new Insets(5, 25, 5, 5));
    }
   
    
    public void addGUI(){
        System.out.println("BURPTest.addGUI()");
        JButton button = null;
        autopilotPanel = new JPanel();
        autopilotGroup = new ButtonGroup();
        //
        frame.add(autopilotPanel, BorderLayout.NORTH);
        //
        button = getAutopilotButton("Turn off autopilot");
        autopilotGroup.add(button);
        autopilotPanel.add(button);
        
        button = getAutopilotButton("Prograde");
        autopilotGroup.add(button);
        autopilotPanel.add(button);
        
        button = getAutopilotButton("Retrograde");
        autopilotGroup.add(button);
        autopilotPanel.add(button);
        
        button = getAutopilotButton("Orbut Normal (+)");
        autopilotGroup.add(button);
        autopilotPanel.add(button);
        
        button = getAutopilotButton("Oribt Anti-normal (-)");
        autopilotGroup.add(button);
        autopilotPanel.add(button);
        
        button = getAutopilotButton("Auto Level");
        autopilotGroup.add(button);
        autopilotPanel.add(button);
        
    }
    
    
    public JButton getAutopilotButton(String s){
        JButton button = new JButton(s);
        button.addActionListener(this);
        return button;
    }
    
    
    @Override
    public void input(){
    }


    @Override
    public void move(float tpf){
    }


    @Override
    public void draw(Graphics g){
        update(g);
    }//draw
        
        

	@Override
	public void update(Graphics g){
        if (drawOnScreen){
            if (clearScreen){
                g.setColor(getBackground());
                g.fillRect(0, 0, width, height);
                g.setColor(getForeground());
            }
        }
		paint(g);
	}


	@Override
	public void paint(Graphics g){
        //System.out.println("paint");
        //int width = getWidth();
        //int height = getHeight();
        if (offscr == null){
            //System.out.println("OrbitTest.paint() " + "w=" + width + ",h=" + height);
            offscreenImage = createImage(width, height);
            offscr = offscreenImage.getGraphics();
        }

        if (drawOnScreen){
            g.translate(width/2, height/2);
            paintObjects(g);
            g.translate(-width/2, -height/2);
        }
        else{
            if (clearScreen){//clearScreen
                offscr.setColor(getBackground());
                offscr.fillRect(0,0, width, height);
                offscr.setColor(getForeground());
                //clearScreen = trailing;
            }
            offscr.translate(width/2, height/2);
            paintObjects(offscr);
            offscr.translate(-width/2, -height/2);
            g.drawImage(offscreenImage, 0, 0, width, height, null);
        }
	}//end paint
    
    
    public void actionPerformed(ActionEvent actionEvent){
        
    }

    
    @Override
    public void keyPressed(KeyEvent e){
        //System.out.println("OrbitTest.keyPressed() " + "got it (" + e.getKeyChar() + ")");
        String action = "NOP";
        switch(e.getKeyCode()){
            case KeyEvent.VK_NUMPAD0:
                action = "Numpad0";
                break;
            case KeyEvent.VK_NUMPAD1:
                action = "Numpad1";
                break;
            case KeyEvent.VK_NUMPAD2:
                action = "Numpad2";
                break;
            case KeyEvent.VK_NUMPAD3:
                action = "Numpad3";
                break;
            case KeyEvent.VK_NUMPAD4:
                action = "Numpad4";
                break;
            case KeyEvent.VK_NUMPAD5:
                action = "Numpad5";
                break;
            case KeyEvent.VK_NUMPAD6:
                action = "Numpad6";
                break;
            case KeyEvent.VK_NUMPAD7:
                action = "Numpad7";
                break;
            case KeyEvent.VK_NUMPAD8:
                action = "Numpad8";
                break;
            case KeyEvent.VK_NUMPAD9:
                action = "Numpad9";
                break;
            case KeyEvent.VK_SHIFT:
                action = "Shift";
                break;
            case KeyEvent.VK_CONTROL:
                action = "Control";
                break;
            case KeyEvent.VK_ESCAPE:
                action = "Esc";
                break;
            case KeyEvent.VK_TAB:
                action = "Tab";
                break;
            case KeyEvent.VK_UP:
                action = "Up Arrow";
                //System.out.println("OrbitText.keyPressed()" + " key=" + e.getKeyCode());
                break;
            case KeyEvent.VK_DOWN:
                action = "Down Arrow";
                //System.out.println("OrbitText.keyPressed()" + " key=" + e.getKeyCode());
                break;
            case KeyEvent.VK_PAGE_UP:
                action = "Page Up";
                break;
            case KeyEvent.VK_PAGE_DOWN:
                action = "Page Down";
                break;
            case KeyEvent.VK_F1:
                action = "F1";
                break;
            case KeyEvent.VK_F2:
                action = "F2";
                break;
            case KeyEvent.VK_F3:
                action = "F3";
                break;
            default:
                System.out.println("OrbitText.keyPressed() " + "not found (" + e.getKeyChar() + "/" + e.getKeyCode() + ")");
                break;
        }
        onAction(action, true, e.getModifiers(), getTPF());
        
    }//keyPressed

    
    public void keyReleased(KeyEvent e){
        String action = "NOP";
        switch(e.getKeyCode()){
            case KeyEvent.VK_NUMPAD0:
                action = "Numpad0";
                break;
            case KeyEvent.VK_NUMPAD1:
                action = "Numpad1";
                break;
            case KeyEvent.VK_NUMPAD2:
                action = "Numpad2";
                break;
            case KeyEvent.VK_NUMPAD3:
                action = "Numpad3";
                break;
            case KeyEvent.VK_NUMPAD4:
                action = "Numpad4";
                break;
            case KeyEvent.VK_NUMPAD5:
                action = "Numpad5";
                break;
            case KeyEvent.VK_NUMPAD6:
                action = "Numpad6";
                break;
            case KeyEvent.VK_NUMPAD7:
                action = "Numpad7";
                break;
            case KeyEvent.VK_NUMPAD8:
                action = "Numpad8";
                break;
            case KeyEvent.VK_NUMPAD9:
                action = "Numpad9";
                break;
            case KeyEvent.VK_SHIFT:
                action = "Shift";
                break;
            case KeyEvent.VK_CONTROL:
                action = "Control";
                break;
            case KeyEvent.VK_ESCAPE:
                action = "Esc";
                //System.exit(0);
                break;
            case KeyEvent.VK_TAB:
                action = "Tab";
                break;
            case KeyEvent.VK_UP:
                action = "Up Arrow";
                break;
            case KeyEvent.VK_DOWN:
                action = "Down Arrow";
                break;
            case KeyEvent.VK_PAGE_UP:
                action = "Page Up";
                break;
            case KeyEvent.VK_PAGE_DOWN:
                action = "Page Down";
                break;
            case KeyEvent.VK_F1:
                action = "F1";
                break;
            case KeyEvent.VK_F2:
                action = "F2";
                break;
            case KeyEvent.VK_F3:
                action = "F3";
                break;
            default:
                System.out.println("OrbitText.keyreleased() " + "not found (" + e.getKeyChar() + ")");
                break;
        }
        onAction(action, false, e.getModifiers(), getTPF());
        
    }//keyReleased
    
    
    public void keyTyped(KeyEvent e){
        //System.out.println("OrbitTest.keyTyped() " + "e=" + e);
        
    }

/*
    public void action(String action, boolean isPressed, int modifiers){
        System.out.println("OrbitTest.action() " + "action=" + action + ", isPressed=" + isPressed + ", modifiers=" + modifiers);
        if (action.equals("Forward")){
            System.out.println("action=" + "forward");
        }
        if (action.equals("Esc")){
            System.exit(0);
        }
    }
*/
        
    @Override
    public void onAction(String action, boolean isPressed, int modifier, float tpf){

        if ((modifier & MASK_CONTROL) == MASK_CONTROL){
        }



        //pause
        if(action.equals("Pause") && isPressed){
            System.out.println("Pause");
        }
        else if (app.isPaused() && isPressed){
            System.out.println("Game is paused.  Hit Control P to unpause the game");
        }
        //else if(action.equals("Esc")){
        //    running = false;
        //}

        else if (modifier == 0){//no modifiers
           // println("CC.onAction() " + "noMods()");
            doNoMods(action, isPressed, modifier);
        }
        else if((modifier & MASK_SHIFT) == MASK_SHIFT){
            //println("CC.onAction() " + "doShift()");
            doShiftMods(action, isPressed, modifier);
        }
        //shift doesn't function the way the other mods do
        else if ((modifier & MASK_CONTROL) == MASK_CONTROL){
            //println("CC.onAction() " + "doControl()");
            doControlMods(action, isPressed, modifier);
        }
        else if ((modifier & MASK_ALT) == MASK_ALT){
            //println("CC.onAction() " + "doControl()");
            doAltMods(action, isPressed, modifier);
        }
        else{
            //System.out.println("CC.onAction(): bad modifier=" + modifier);
        }
        if (!isPressed){
            //Utils.println("OrbitTest.onAction()" + " " + controlled);
            //Utils.println();
        }
    }//onAction


    protected void doNoMods(String action, boolean isPressed, int modifier){
        //Utils.println("OT.doNoMods() " + "controlled=" + controlled);
        System.out.println("OT.doNoMods()" + ", action=" + action + ", isPressed=" + isPressed + ", modifier=" + modifier);
        //system
        //tab
        //int controlMode = controlled.getUserData("controlMode");
        if(action.equals("Tab")){
            if (isPressed){
            }
            if (!isPressed){
                System.out.println("OT.doNoMods() " + "controlMode=" +  (controlMode == MODE_CONTROL_TRANSLATION ? "Translation" : "Rotation"));
            }
        }
        //
        if (action.equals("F1") && isPressed){
        }
        else if (action.equals("F2") && isPressed){
        }
        else if (action.equals("F3") && isPressed){
        }
        //
        if(action.equals("Numpad1")){
        }
        //if (action.equals("TranslationDown")){
        if (action.equals("Numpad2")){
        }
        //if (action.equals("TranslationUp")){
        if (action.equals("Numpad8")){
        }
        //if (action.equals("TranslationRight")){
        if (action.equals("Numpad4")){
        }
        //if (action.equals("TranslationLeft")){
        if (action.equals("Numpad6")){
        }

        //if(action.equals("TranslationBackward")){
        if(action.equals("Numpad3")){
        }
        if(action.equals("Numpad7")){
        }
        if (action.equals("Numpad0")){//ignore modifier
        }
        else if(action.equals("Numpad5")){
        }
        //
        if(action.equals("Page Up") && isPressed){
        }
        if (action.equals("Page Down") && isPressed){
        }
        if(action.equals("Up Arrow")){
        }
        if (action.equals("Down Arrow")){
        }
        //
        //doOrbit
        if (action.equals("Key1") && isPressed){
        }
        else if (action.equals("Key2") && isPressed){
        }
        else if (action.equals("Key3") && isPressed){
        }
        else if (action.equals("Key4") && isPressed){
        }
        else if (action.equals("Key5") && isPressed){
        }
        else if (action.equals("Key6") && isPressed){
        }
        else if (action.equals("Key7") && isPressed){
        }
        else if (action.equals("Key8") && isPressed){
        }
        else if (action.equals("Key9") && isPressed){
        }
        else if (action.equals("Key0") && isPressed){
        }
        else if (action.equals("Key-") && isPressed){
        }
        else if (action.equals("Orbit") && isPressed){
        }


    }//doNone


    protected void doShiftMods(String action, boolean isPressed, int modifier){
        //Utils.println("InputControllableState.doShiftMods()" + ", action=" + action + ", isPressed=" + isPressed);
        //system
        if(action.equals("Up Arrow") && isPressed){
        }
        if (action.equals("Down Arrow") && isPressed){
        }
        if ((action.equals("F1")) && isPressed){//doAttach
        }
        else if ((action.equals("F2")) && isPressed){
        }
        else if ((action.equals("F3"))&& isPressed){
        }

        if (action.equals("Key1") && isPressed){
        }
        else if (action.equals("Key2") && isPressed){
        }
        else if (action.equals("Key3") && isPressed){
        }
        else if (action.equals("Key4") && isPressed){
        }
        else if (action.equals("Key5") && isPressed){
        }
        else if (action.equals("Key6") && isPressed){
        }
        else if (action.equals("Key7") && isPressed){
        }
        else if (action.equals("Key8") && isPressed){
        }
        else if (action.equals("Key9") && isPressed){
        }
        else if (action.equals("Key0") && isPressed){
        }
        else if (action.equals("Key-") && isPressed){
        }
        else if (action.equals("Orbit") && isPressed){
        }
        //tab
        if (action.equals("Numpad4") && isPressed){
        }
        if (action.equals("Numpad6") && isPressed){
        }
        if (action.equals("Numpad8") && isPressed){
        }
        if (action.equals("Ship01") && isPressed){
        }
        if (action.equals("Camera01") && isPressed){
        }
    }//doShift



    protected void doControlMods(String action, boolean isPressed, int modifier){
        //System.out.println("CC.doControl (" + cameraNode.getName() + ")" + ", action=" + action + ", isPressed=" + isPressed + ", modifier=" + modifier);
        //int controlMode = controlled.getUserData("controlMode");
        //int controlMode = controlled.getUserData("controlMode");
        boolean found = false;

        //system
        if ((action.equals("RightControl") || action.equals("LeftControl"))){
        }
        if(action.equals("UpArrow") && isPressed){
        }
        if (action.equals("DownArrow") && isPressed){
        }
        //local
        if (action.equals("F1") && isPressed){
        }
        else if ((action.equals("Camera01") || action.equals("F2")) && isPressed){
        }
        else if ((action.equals("Camera02") || action.equals("F3"))&& isPressed){
        }

        if (action.equals("Key1") && isPressed){
        }
        else if (action.equals("Key2") && isPressed){
        }
        else if (action.equals("Key3") && isPressed){
        }
        else if (action.equals("Key4") && isPressed){
        }
        else if (action.equals("Key5") && isPressed){
        }
        else if (action.equals("Key6") && isPressed){
        }
        else if (action.equals("Key7") && isPressed){
        }
        else if (action.equals("Key8") && isPressed){
        }
        else if (action.equals("Key9") && isPressed){
        }
        else if (action.equals("Key0") && isPressed){
        }
        else if (action.equals("Key-") && isPressed){
        }
        else if (action.equals("Orbit") && isPressed){
        }
        //tab
        if(action.equals("Tab") && isPressed){
        }

        if(action.equals("Numpad1")){
        }
        //if (action.equals("TranslationDown")){
        if (action.equals("Numpad2")){
        }
        //if (action.equals("TranslationUp")){
        if (action.equals("Numpad8")){
        }
        //if (action.equals("TranslationRight")){
        //if (action.equals("TranslationLeft")){
        if (action.equals("Numpad6")){
        }

        //if(action.equals("TranslationBackward")){
        if(action.equals("Numpad3")){
        }
        if(action.equals("Numpad7")){
        }
        if (action.equals("Stop")){//ignore modifier
        }
        else if(action.equals("StopRotation")){
        }

        if (action.equals("F1") && isPressed){
        }
        if (action.equals("F2") && isPressed){
        }
        if (action.equals("F3") && isPressed){
        }
    }//doControl


    protected void doAltMods(String action, boolean isPressed, int modifier){
        if (action.equals("Numpad1") && isPressed){
        }
        if (action.equals("Numpad2") && isPressed){
        }
        if (action.equals("Numpad3") && isPressed){
        }
        if (action.equals("Numpad4") && isPressed){
        }
        if (action.equals("Numpad6") && isPressed){
        }
        if (action.equals("Numpad8") && isPressed){
        }
    }//doAlt


}
