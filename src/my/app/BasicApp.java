/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.app;

import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Peter
 */
public class BasicApp extends JPanel implements Runnable, KeyListener{

    public static final int MASK_ANY      =   -1;    //1^32
    public static final int MASK_NONE     =    0;    //0000 0000 0000 0000
    
    public static final int MASK_SHIFT    =    1;    //0000 0000 0000 0001
    public static final int MASK_CONTROL  =    2;    //0000 0000 0000 0010
    public static final int MASK_META     =    4;    //0000 0000 0000 0100
    public static final int MASK_ALT      =    8;    //0000 0000 0000 1000
    
    public static final int MASK_LSHIFT   =   16;    //0000 0000 0001 0000
    public static final int MASK_RSHIFT   =   32;    //0000 0000 0010 0000
    public static final int MASK_LCONTROL =   64;    //0000 0000 0100 0000
    public static final int MASK_RCONTROL =  128;    //0000 0000 1000 0000
    
    public static final int MASK_LMETA   =   256;    //0000 0001 0000 0000
    public static final int MASK_RMETA   =   512;    //0000 0010 0000 0000
    public static final int MASK_LALT    =  1024;    //0000 0100 0000 0000
    public static final int MASK_RALT    =  2048;    //0000 1000 0000 0000
    
    public static final int MASK_CAPLOCK =  4096;    //0001 0000 0000 0000
    public static final int MASK_NUMLOCK =  8192;    //0010 0000 0000 0000
    public static final int MASK_OTHER   = 16384;    //0100 0000 0000 0000
    public static final int MASK_NONE2   = 32768;    //1000 0000 0000 0000
    
    public static final int MASK_OTHER2  = 65535;    //1111 1111 1111 1111
    //1000 0000 0000 0000 currentModifier
    //1000 0000 0000 0000 modifier
    //1000 0000 0000 0000 ANDed
    public static final int RIGHT =           1;    //1111 1111 1111 1111
    public static final int LEFT  =           2;    //1111 1111 1111 1111
    
	static private final String MAPPING_ROTATE = "Rotate";
	//static private final Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    
    public static final int MODE_CONTROL_TRANSLATION =     1;    //1111 1111 1111 1111
    public static final int MODE_CONTROL_ROTATION    =     2;    //1111 1111 1111 1111
    
    /**
     * @param args the command line arguments
     */
    static JFrame frame;
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("BasicApp.main()");
        int top = 50;
        int left = 500;
        int width = 800;
        int height = 600;
        //PercentageLayout layout = new PercentageLayout(new float[] {0.50f, 0.50f}, new float[] {1.00f}, false);//
        frame = new JFrame("BasicApp");
		frame.setBounds(left, top, width, height);//x, y, w, h
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultLookAndFeelDecorated(true);
		//frame.setMinimumSize(new Dimension(4 * 300, 3 * 300));//
        //frame.getContentPane().setLayout(layout);
        //
        BasicApp app = new BasicApp(left, top, width, height);
        app.setName("Basic App");
		app.setMinimumSize(new Dimension(50, 50));//x, y, w, h
        //app.setLocation(100, 100);
        //app.setSize(50, 50);
        //app.setBounds(50, 50, 500, 500);
        frame.setVisible(true);
        //
        //add first panal
        frame.getContentPane().add(app);
        app.setVisible(true);
        frame.setVisible(true);
         
        app.init();
        app.start();
        app.setVisible(true);
        //System.exit(1);
    }
    
    
    protected BasicApp app;
    protected int top = 50;
    protected int left = 50;
    protected int width = 50;
    protected int height = 50;
	protected Image offscreenImage;
	protected Graphics offscr;
    protected int currentView = 0;
    //protected float fieldOfView = 90f;
    //
    protected Thread clock;
    protected boolean running = false;
    protected long time = 0L;
    protected long lastTime = 0L;
    protected long fps = 30L;
            float tpf = 0.1f;
    protected long delay = 0L;
    //protected float tpf = 0f;
    protected Calendar calendar;
    protected boolean drawOnScreen = false;
    protected boolean clearScreen = true;
    protected boolean trailing = false;
    protected JPanel myPanel;
    
    BasicApp(){
        this(0, 0, 50, 50);
    }
    
    BasicApp(int left, int top, int width, int height){
        System.out.println("BasicApp.BasicApp() ");
        
        app = this;
        //calendar = Calendar.getInstance();
        //lastTime = calendar.getTimeInMillis();
    }
    
    
    public void init(){
        System.out.println("BasicApp.init()");
        this.app = this;
        setFPS(30);
        setBackground(Color.black);
        setBounds(top, left, width, height);
        setFocusTraversalKeysEnabled(false);
        this.setFocusable(true);
        this.requestFocus();
        calendar = Calendar.getInstance();
        lastTime = calendar.getTimeInMillis();
        //doMakeOffscr(getWidth(), getHeight());
        addKeyListener(this);
        add2DObjects();
        setVisible(true);
        System.out.println();
    }//end init
    

    @Override
    public void setBounds(int top, int left, int width, int height){
        super.setBounds(top, left, width, height);
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
        if (this.isVisible())
            doMakeOffscr();
    }
    
    
    @Override
    public void setSize(int width, int height){
        super.setSize(width, height);
        this.width = width;
        this.height = height;
        if (this.isVisible())
            doMakeOffscr();
    }
    
    
    public void add2DObjects(){
        //this.getCon
    }
    
    
    public void start(){
    //Utils.println("OrbitTest.start()");
    if (clock==null || !clock.isAlive()){
        clock = new Thread(this);
    }
        clock.start();
        running = true;
    }


    //public void destroy(){
    //}
    

    public void stop(){
        running = false;
    }
    
    
    public void run(){
        //Utils.println("OrbitTest.run()");
        while (running){
            input();
            move(tpf);
            draw(getGraphics());
            //repaint();
            try{
                clock.sleep(delay);//0/fps
            } catch (InterruptedException e){}
            tpf = calcTPF();
            //System.out.println("BasicApp.run() " + " tpf=" + tpf + " milliseconds");
        }
        System.exit(0);
    }


    public void setFPS(long fps){
        this.fps = fps;
        delay = 1000L / fps;
    }


    public float getTPF(){
        return tpf;
    }


    public float calcTPF(){
        calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        tpf = (currentTime - lastTime);
        //System.out.println("OrbitTest.getTPF() " + "currentTime=" + currentTime + ", lastTime=" + lastTime + ", delay=" + delay + ",tpf=" + tpf);
        if (tpf == 0.0f) tpf = 0.1f;
        lastTime = currentTime;
        return (tpf);
    }


    public void input(){
        
    }


    double degTime = 0f;
    public void move(float tpf){
    }


    public void draw(Graphics g){
        update(g);
    }//draw
        
        
    public void paintObjects(Graphics g){
        int level = 0;
        //System.out.println("OrbitTest.paintObjects() ");
    }


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
		//super.paint(g);
        //System.out.println("painting");
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
            //
            paintObjects(offscr);
            offscr.translate(-width/2, -height/2);
            g.drawImage(offscreenImage, 0, 0, width, height, null);

        }
	}//end paint
    
    
    public void doMakeOffscr(){
            offscreenImage = createImage(width, height);
            offscr = offscreenImage.getGraphics();
        //}
        
    }
    
    
    private boolean paused = false;
    public void setPause(boolean b){
        this.paused = b;
    }
    
    
    public boolean isPaused(){
        return paused;
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

    
    @Override
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
    
    
    @Override
    public void keyTyped(KeyEvent e){
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
    boolean oldIsPressed = false;
    //@Override
    int controlMode = MODE_CONTROL_ROTATION;
    public void onAction(String action, boolean isPressed, int modifier, float tpf){
        if (isPressed){ 
            System.out.println("OT.onAction()" + " action=" + action + ", modifier=" + modifier + ", isPressed=" + isPressed + ", tpf=" + tpf);
        }

        if ((modifier & MASK_CONTROL) == MASK_CONTROL){
            if (controlMode == MODE_CONTROL_TRANSLATION)
                controlMode = MODE_CONTROL_ROTATION;
            else
                controlMode = MODE_CONTROL_TRANSLATION;
        }


        //pause
        if(action.equals("Pause") && isPressed){
            System.out.println("Pause");
            boolean pause = app.isPaused();
            pause = (pause == false);
            app.setPause(pause);
        }
        else if (app.isPaused() && isPressed){
            System.out.println("Game is paused.  Hit Control P to unpause the game");
        }
        else if(action.equals("Esc")){
            running = false;
        }

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


    private void doNoMods(String action, boolean isPressed, int modifier){
        //Utils.println("OT.doNoMods() " + "controlled=" + controlled);
        System.out.println("OT.doNoMods()" + ", action=" + action + ", isPressed=" + isPressed + ", modifier=" + modifier);
        if(action.equals("Tab")){
            if (isPressed){
            }
            if (!isPressed) System.out.println("OT.doNoMods() " + "controlMode=" +  (controlMode == MODE_CONTROL_TRANSLATION ? "Translation" : "Rotation"));
        }
        //
        if (action.equals("F1") && isPressed){
            //Node node = (Node)rootNode.getChild("Ship1");
        }
        else if (action.equals("F2") && isPressed){
            //Node node = (Node)rootNode.getChild("Ship1");
        }
        else if (action.equals("F3") && isPressed){
            //Node node = (Node)rootNode.getChild("Ship1");
        }
        //
        if(action.equals("Numpad1")){
        }
        if (action.equals("Numpad2")){
        }
        //if (action.equals("TranslationUp")){
        if (action.equals("Numpad8")){
        }
        if (action.equals("Numpad4")){
        }
        if (action.equals("Numpad6")){
        }

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


    private void doShiftMods(String action, boolean isPressed, int modifier){
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



    private void doControlMods(String action, boolean isPressed, int modifier){
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
        if ((action.equals("Camera01") || action.equals("F2")) && isPressed){
        }
        if ((action.equals("Camera02") || action.equals("F3"))&& isPressed){
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
        if (action.equals("Numpad2")){
        }
        if (action.equals("Numpad8")){
        }
        if (action.equals("Numpad4")){
        }
        if (action.equals("Numpad6")){
        }
        if(action.equals("Numpad3")){
        }
        if(action.equals("Numpad7")){
        }
        if (action.equals("Stop")){//ignore modifier
        }
        if(action.equals("StopRotation")){
        }

        if (action.equals("F1") && isPressed){
        }
        if (action.equals("F2") && isPressed){
        }
        if (action.equals("F3") && isPressed){
        }
    }//doControl


    private void doAltMods(String action, boolean isPressed, int modifier){
        //System.out.println("CC.doAlt (" + cameraNode.getName() + ")" + ", action=" + action + ", isPressed=" + isPressed + ", modifier=" + modifier);
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
