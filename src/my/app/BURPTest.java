/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.app;

//import com.jogamp.opengl.math.Quaternion;
//import com.jme3.math.Quaternion;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
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
import my.app.math.Quaternion;


/**
 *
 * @author Peter
 */
public class BURPTest extends BasicApp implements ActionListener{

    
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
        BURPTest app = new BURPTest(left, top, width, height);
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
    
    
	static final int X = 0;//pitch
	static final int Y = 1;//yaw
	static final int Z = 2;//roll
/*
    static final int ORBIT_NONE  = 0;
    static final int ORBIT_PROGRADE = 1;
	static final int ORBIT_RETROGRADE = 2;
	static final int ORBIT_NORMAL = 3;
	static final int ORBIT_ANTINORMAL = 4;
	static final int ORBIT_LEVEL = 5;
*/
	public static final int AUTOPILOT_PRIMARY = 1;
	public static final int AUTOPILOT_SECONDARY = 2;
	public static final int AUTOPILOTMODE_FLAT = 1;
	public static final int AUTOPILOTMODE_EDGE = 2;

    static final int AUTOPILOT_NONE  = 0;
    static final int AUTOPILOT_PROGRADE = 1;
	static final int AUTOPILOT_RETROGRADE = 2;
	static final int AUTOPILOT_NORMAL = 3;
	static final int AUTOPILOT_ANTINORMAL = 4;
	static final int AUTOPILOT_AUTOLEVEL = 5;

    static final float PI = (float)Math.PI;
	static final float PI2 = (float)Math.PI * 2f;

    protected BURPTest app;
	protected Image offscreenImage;
	protected Graphics offscr;
    protected ButtonGroup autopilotGroup;
    protected JPanel autopilotPanel;
    //
    protected Ship ship = null;
    protected Orbit orbit = null;
    protected Planet planet = null;
    //
    protected float gravity = 6.673e-11f;
	//protected boolean autopilot = false;
    //protected int autopilotMode = AUTOPILOT_NONE;
    //protected int orbitalPlaneModifier = AUTOPILOT_PRIMARY;
	//protected Quaternion currentRotation = new Quaternion();
	//protected Quaternion endRotation = new Quaternion();
    //protected float autopilotValue = 0f;
    //protected int orbitalPlane = Y;
    
    BURPTest(){
        this(0, 0, 50, 50);
    }
    
    BURPTest(int left, int top, int width, int height){
        super(left, top, width, height);
        System.out.println("BURPTest.BURPTest() ");
        
        app = this;
        ship = new Ship(app);
        planet = new Planet();
        orbit = new Orbit();
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
        System.out.println();
    }//end init
    
    
    public Insets getInsets(){
        return(new Insets(5, 25, 5, 5));
    }
   
    
    public void addGUI(){
        System.out.println("BURPTest.addGUI()");
        JButton button = null;
        autopilotPanel = new JPanel();
        autopilotPanel.setSize(500, 50);
        autopilotPanel.setMinimumSize(new Dimension(500, 50));
        autopilotPanel.setBackground(Color.yellow);
        autopilotGroup = new ButtonGroup();
        //
        frame.add(autopilotPanel, BorderLayout.NORTH);
        autopilotPanel.setVisible(true);
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
        
        button = getAutopilotButton("Oribt Anti-Normal (-)");
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
        getShip().update(tpf);
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
        if (actionEvent.getSource() instanceof JButton){
            ship.getAutopilot().setAutopilot(actionEvent.getActionCommand());
        }
    }
    
    
    @Override
    public void keyPressed(KeyEvent e){
        //System.out.println("OrbitTest.keyPressed() " + "got it (" + e.getKeyChar() + ")");
        String action = "NOP";
        switch(e.getKeyCode()){
            case KeyEvent.VK_OPEN_BRACKET:
                action = "Prograde";
                break;
            case KeyEvent.VK_CLOSE_BRACKET:
                action = "Retrograde";
                break;
            case KeyEvent.VK_SEMICOLON:
                action = "Normal";
                break;
            case KeyEvent.VK_QUOTE:
                action = "Anti-Normal";
                break;
            case KeyEvent.VK_L:
                action = "Auto Level";
                break;
            case KeyEvent.VK_P:
                action = "Pause";
                break;
            case KeyEvent.VK_ESCAPE:
                action = "Esc";
                break;
            case KeyEvent.VK_HOME:
                action = "Home";
                break;
            case KeyEvent.VK_END:
                action = "End";
                break;
            case KeyEvent.VK_PAGE_UP:
                action = "Page Up";
                break;
            case KeyEvent.VK_PAGE_DOWN:
                action = "Page Down";
                break;
            default:
                System.out.println("BURPTest.keyPressed() " + "not found (" + e.getKeyChar() + "/" + e.getKeyCode() + "/" + e.paramString() + ")");
                break;
        }
        onAction(action, true, e.getModifiers(), getTPF());
        
    }//keyPressed

    
    public void keyReleased(KeyEvent e){
        String action = "NOP";
        switch(e.getKeyCode()){
            case KeyEvent.VK_OPEN_BRACKET:
                action = "Prograde";
                break;
            case KeyEvent.VK_CLOSE_BRACKET:
                action = "Retrograde";
                break;
            case KeyEvent.VK_SEMICOLON:
                action = "Normal";
                break;
            case KeyEvent.VK_QUOTE:
                action = "Anti-Normal";
                break;
            case KeyEvent.VK_L:
                action = "Auto Level";
                break;
            case KeyEvent.VK_P:
                action = "Pause";
                break;
            case KeyEvent.VK_ESCAPE:
                action = "Esc";
                break;
            case KeyEvent.VK_HOME:
                action = "Home";
                break;
            case KeyEvent.VK_END:
                action = "End";
                break;
            case KeyEvent.VK_PAGE_UP:
                action = "Page Up";
                break;
            case KeyEvent.VK_PAGE_DOWN:
                action = "Page Down";
                break;
            default:
                System.out.println("BURPTest.keyPressed() " + "not found (" + e.getKeyChar() + "/" + e.getKeyCode() + ")");
                break;
        }
        onAction(action, false, e.getModifiers(), getTPF());
    }//keyReleased
    
    
    @Override
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
        else if(action.equals("Esc")){
            running = false;
        }

        else if (modifier == 0){//no modifiers
           // println("BURPTest.onAction() " + "noMods()");
            doNoMods(action, isPressed, modifier);
        }
        else if((modifier & MASK_SHIFT) == MASK_SHIFT){
            //println("BURPTest.onAction() " + "doShift()");
            doShiftMods(action, isPressed, modifier);
        }
        //shift doesn't function the way the other mods do
        else if ((modifier & MASK_CONTROL) == MASK_CONTROL){
            //println("BURPTest.onAction() " + "doControl()");
            doControlMods(action, isPressed, modifier);
        }
        else if ((modifier & MASK_ALT) == MASK_ALT){
            //println("BURPTest.onAction() " + "doControl()");
            doAltMods(action, isPressed, modifier);
        }
        else{
            System.out.println("BURPTest.onAction(): bad modifier=" + modifier);
        }
    }//onAction


    protected void doNoMods(String action, boolean isPressed, int modifier){
        //Utils.println("BURPTest.doNoMods() " + "controlled=" + controlled);
        if (isPressed) System.out.println("BURPTest.doNoMods()" + ", action=" + action + ", isPressed=" + isPressed + ", modifier=" + modifier);
        //system
        //tab
        //int controlMode = controlled.getUserData("controlMode");
        if(action.equals("Tab")){
            if (isPressed){
            }
            if (!isPressed){
                System.out.println("BURPTest.doNoMods() " + "controlMode=" +  (controlMode == MODE_CONTROL_TRANSLATION ? "Translation" : "Rotation"));
            }
        }
        //
        if (action.equals("Prograde") && isPressed){
            ship.getAutopilot().setAutopilot(action);
        }
        else if (action.equals("Retrograde") && isPressed){
            ship.getAutopilot().setAutopilot(action);
        }
        else if (action.equals("Normal") && isPressed){
            ship.getAutopilot().setAutopilot(action);
        }
        else if(action.equals("AntiNormal")){
            ship.getAutopilot().setAutopilot(action);
        }
        else if (action.equals("Auto Level")){
            ship.getAutopilot().setAutopilot(action);
        }

        if (action.equals("Home")){
            ship.getAutopilot().setOrbitalPlane(Y);
        }
        if (action.equals("Auto Level")){
            ship.getAutopilot().setOrbitalPlane(X);
        }
        if (action.equals("Auto Level")){
            ship.getAutopilot().setOrbitalPlaneModifier(AUTOPILOTMODE_FLAT);
        }
        if (action.equals("Auto Level")){
            ship.getAutopilot().setOrbitalPlaneModifier(AUTOPILOTMODE_EDGE);
        }
    }//doNone


    protected void doShiftMods(String action, boolean isPressed, int modifier){
        //Utils.println("InputControllableState.doShiftMods()" + ", action=" + action + ", isPressed=" + isPressed);
        //system
    }//doShift



    protected void doControlMods(String action, boolean isPressed, int modifier){
        //System.out.println("BURPTest.doControl (" + cameraNode.getName() + ")" + ", action=" + action + ", isPressed=" + isPressed + ", modifier=" + modifier);
        //int controlMode = controlled.getUserData("controlMode");
        //int controlMode = controlled.getUserData("controlMode");
    }//doControl


    protected void doAltMods(String action, boolean isPressed, int modifier){
    }//doAlt
    
    
    public Ship getShip(){
        return ship;
    }
    
    
    public Planet getPlanet(){
        return planet;
    }
    
    public Orbit getOrbit(){
        return orbit;
    }
    
    
    public float getGravity(){
        return gravity;
    }
    
    
    public class Ship{
        BURPTest app = null;
        Vector3f localTranslation = null;//new Vector3f(100f, 30f, -450f)
        Vector3f worldTranslation = null;//new Vector3f(100f, 30f, -450f)
        Quaternion localRotation = null;
        Quaternion worldRotation = null;//new Quaternion().fromAngles((float)Math.random() * (PI * 2f), (float)Math.random() * (PI * 2f), (float)Math.random() * (PI * 2f));
        Autopilot autopilot = null;
        //
        
        public Ship(BURPTest app){
            this.app = app;
            this.autopilot = new Autopilot(app, this);
            setLocalTranslation(new Vector3f(50f, 10f, 400f));
            //setLocalRotation(new Quaternion().fromAngles((float)Math.random() * (PI * 2f), (float)Math.random() * (PI * 2f), (float)Math.random() * (PI * 2f)));
            setLocalRotation(new Quaternion().fromAngles(FastMath.DEG_TO_RAD * 5f, FastMath.DEG_TO_RAD * 10f, FastMath.DEG_TO_RAD * 5f));
        }


        public void setLocalTranslation(Vector3f localTranslation){
            this.localTranslation = localTranslation;
            this.worldTranslation = localTranslation.clone();
        }
        
        
        public Vector3f getLocalTranslation(){
            return localTranslation;
        }
        
        
        public Vector3f getWorldTranslation(){
            return worldTranslation;
        }
        
        
        public void setLocalRotation(Quaternion localRotation){
            this.localRotation = localRotation;
            this.worldRotation = localRotation.clone();
        }
        
        
        public Quaternion getLocalRotation(){
            return localRotation;
        }
        
        
        public Quaternion getWorldRotation(){
            return worldRotation;
        }
        
        
        public Autopilot getAutopilot(){
            return autopilot;
        }
        
        
        public Orbit getOrbit(){
            return orbit;
        }
        
        
        public void update(float tpf){
            autopilot.update(tpf);
        }
        
        
    }
    
    
    public class Autopilot{
        BURPTest app = null;
        Ship ship = null;
        boolean autopilot = false;
        String autopilotModeString = new String();
        int autopilotMode = AUTOPILOT_NONE;
        int orbitalPlaneModifier = AUTOPILOT_PRIMARY;
        Quaternion startRotation = new Quaternion();
        Quaternion endRotation = new Quaternion();
        float autopilotValue = 0f;
        int orbitalPlane = Y;
        
        public Autopilot(BURPTest app, Ship ship){
            this.app = app;
            this.ship = ship;
        }
        
        public void setAutopilot(boolean autopilot){
            this.autopilot = autopilot;
        }
        
        
        public boolean getAutopilot(){
            return autopilot;
        }


        public int getAutopilotMode(String s){
            int mode = 0;
            switch(s){
                case "Prograde":
                    mode = 1;
                    break;
                case "Retrograde":
                    mode = 2;
                    break;
                case "Normal":
                    mode = 3;
                    break;
                case "Anti-Normal":
                    mode = 4;
                    break;
                case "Auto Level":
                    mode = 5;
                    break;
                default:
                    break;
            }
            return mode;
        }
        
        
        public boolean setAutopilot(String command){
            return setAutopilot(command, false);
        }


        public boolean setAutopilot(String command, boolean change){
            System.out.println("BRUPTest.setAutopilot(" + command + ") ");
            boolean ap = true;
            if (autopilotModeString.equals(command) && change){
                ap = false;
            }
            else{
                autopilotModeString = command;
                //
                switch(command){
                    case "Prograde":
                        ap = doPrograde(orbitalPlane, orbitalPlaneModifier);
                        break;
                    case "Retrograde":
                        ap = doRetrograde(orbitalPlane, orbitalPlaneModifier);
                        break;
                    case "Normal":
                        ap = doNormal(orbitalPlane, orbitalPlaneModifier);
                        break;
                    case "Anti-Normal":
                        ap = doAntiNormal(orbitalPlane, orbitalPlaneModifier);
                        break;
                    case "Auto Level":
                        ap = doAutoLevel(orbitalPlane, orbitalPlaneModifier);
                        break;
                    default:
                        break;
                }
            }
            autopilot = ap;
            return ap;
        }


        public void setOrbitalPlane(int orbitalPlane){
            if (this.orbitalPlane != orbitalPlane){
                this.orbitalPlane = orbitalPlane;
                setAutopilot(autopilotModeString, true);
            }
        }


        public void setOrbitalPlaneModifier(int orbitalPlaneModifier){
            if (this.orbitalPlaneModifier != orbitalPlaneModifier){
                this.orbitalPlaneModifier = orbitalPlaneModifier;
                setAutopilot(autopilotModeString, true);
            }
        }


        public void prograde(){
            doPrograde(orbitalPlane, orbitalPlaneModifier);
        }


        public boolean doPrograde(int orbitalPlane, int orbitalPlaneModifier){
            System.out.println("BURPTest.doPrograde() " + "orbitalPlane=" + orbitalPlane + ", orbitalPlaneModifier=" + orbitalPlaneModifier);
            boolean ap = true;
            if (false){//does not have gravity well
                System.out.println("BURPTest.doPrograde() " + "Gravity Well not set");
                return(false);
            }

            Matrix3f result = new Matrix3f();
            com.jme3.math.Quaternion q = new com.jme3.math.Quaternion();

            Vector3f walkDirection = new Vector3f();
            Vector3f newRight = new Vector3f();
            Vector3f newForward = new Vector3f();
            Vector3f newUp = new Vector3f();

            Vector3f currentTranslation = ship.getWorldTranslation();
            Vector3f parentTranslation = app.getPlanet().getWorldTranslation();
            //
            startRotation = ship.getWorldRotation().clone();
            boolean testing = true;
            if (testing){
                Quaternion prograde1 = new Quaternion().fromAngles(0f, FastMath.DEG_TO_RAD * 0f, 0f);
                Quaternion prograde = new Quaternion().fromAngles(0f, FastMath.DEG_TO_RAD * 0f, 0f);
                endRotation = prograde;
                System.out.println("BT.doPrograde() " + "endRotation=" + endRotation);
                autopilotValue = 0f;
                return true;
            }
            //
            System.out.println("BRUPTest.doPrograde() " + "startRotation=" + startRotation);
            // *** start with Y
            if (orbitalPlane == X){
    /*
                if (orbitalPlaneModifier == AUTOPILOT_PRIMARY){
                    walkDirection = currentRotation.getRotationColumn(Z);
                    newRight = (currentTranslation.subtract(parentTranslation)).normalize().negate();
                    newRight = newRight.normalize();
                    newForward = walkDirection.cross(newRight);
                    newForward = newForward.normalize();
                    newUp = newRight.cross(newForward);
                    newUp = newUp.normalize();
                }
                else{
                    walkDirection = currentRotation.getRotationColumn(Z);
                    newRight = (currentTranslation.subtract(parentTranslation)).normalize();
                    newRight = newRight.normalize();
                    newForward = walkDirection.cross(newRight);
                    newForward = newForward.normalize();
                    newUp = newRight.cross(newForward);
                    newUp = newUp.normalize();
                }
    */
            }
            else if (orbitalPlane == Y){//belly of ship to planet
                if (orbitalPlaneModifier == AUTOPILOT_PRIMARY){
                    System.out.println("BURPTest.doPrograde() belly" );
                    //walkDirection = currentRotation.getRotationColumn(Z);
                    walkDirection = getShip().getOrbit().getPrograde();
                    //System.out.println("BRUPTest.doPrograde()1 " + "walkDirection=" + walkDirection);
                    walkDirection = walkDirection.normalize();
                    //System.out.println("BRUPTest.doPrograde()2 " + "walkDirection=" + walkDirection);
                    newUp = (currentTranslation.subtract(parentTranslation));//.negate()
                    //System.out.println("BRUPTest.doPrograde()1 " + "newUp=" + newUp);
                    newUp = newUp.normalize();
                    //System.out.println("BRUPTest.doPrograde()2 " + "newUp=" + newUp);
                    newForward = walkDirection.cross(newUp);
                    //System.out.println("BRUPTest.doPrograde()1 " + "newForward=" + newForward);
                    newForward = newForward.normalize();
                    //System.out.println("BRUPTest.doPrograde()2 " + "newForward=" + newForward);
                    newRight = newUp.cross(newForward);
                    //System.out.println("BRUPTest.doPrograde()1 " + "newRight=" + newRight);
                    newRight = newRight.normalize();
                    //System.out.println("BRUPTest.doPrograde()2 " + "newRight=" + newRight);
                }
                else if (orbitalPlaneModifier == AUTOPILOT_SECONDARY){//SECONDARY
                    System.out.println("BURPTest.doPrograde() back" );
                    walkDirection = getShip().getOrbit().getPrograde();
                    newUp = (currentTranslation.subtract(parentTranslation)).negate();
                    newUp = newUp.normalize();
                    newForward = walkDirection.cross(newUp).normalize();
                    newForward = newForward.normalize();
                    newRight = newUp.cross(newForward).normalize();
                    newRight = newRight.normalize();
                }
                else{
                    System.out.println("BURPTest.autopilot() bad orbitalPlaneModifier=(" + orbitalPlaneModifier + ")");
                }
            }
            else{
                System.out.println("BURPTest.autopilot() bad orbitalPlane=(" + orbitalPlane + ")");
            }
            result.setColumn(X, newRight);
            result.setColumn(Y, newUp);
            result.setColumn(Z, newForward);
            //System.out.println("BRUPTest.doPrograde() " + "resut=" + result);
            //q = new Quaternion();
            //q.fromRotationMatrix(result);
            //q = q.normalizeLocal();

            //System.out.println("BURPTest.doPrograde() " + "walkDirection=" + walkDirection + ", newRight=" + newRight + ", newUp=" + newUp + ", newForward=" + newForward + ", result=" + result);

            endRotation.fromRotationMatrix(result);
            endRotation = endRotation.normalizeLocal();
            autopilotValue = 0f;
            //rotationVelocity.set(0f, 0f, 0f);
            //System.out.println("BRUPTest.doPrograde() " + "endRotation=" + endRotation);
            //System.exit(X);
            //autopilot = true;//return true
            return true;
        }//doPrograde


        public boolean doRetrograde(int orbitalPlane, int orbitalPlaneModifier){
            boolean ap = false;
            startRotation = ship.getWorldRotation().clone();
            boolean testing = true;
            if (testing){
                Quaternion retrograde = new Quaternion().fromAngles(0f, FastMath.DEG_TO_RAD * -179f, 0f);
                endRotation = retrograde;
                System.out.println("BT.doRetrograde() " + "endRotation=" + endRotation);
                autopilotValue = 0f;
                ap = true;
            }
            return ap;
        }


        public boolean doNormal(int orbitalPlane, int orbitalPlaneModifier){
            boolean ap = false;
            startRotation = ship.getWorldRotation().clone();
            boolean testing = true;
            if (testing){
                Quaternion normal = new Quaternion().fromAngles(0f, FastMath.DEG_TO_RAD * 90f, 0f);
                endRotation = normal;
                System.out.println("BT.doNormal() " + "endRotation=" + endRotation);
                autopilotValue = 0f;
                ap = true;
            }
            return ap;
        }


        public boolean doAntiNormal(int orbitalPlane, int orbitalPlaneModifier){
            boolean ap = false;
            startRotation = ship.getWorldRotation().clone();
            boolean testing = true;
            if (testing){
                Quaternion antiNormal = new Quaternion().fromAngles(0f, FastMath.DEG_TO_RAD * -90f, 0f);
                endRotation = antiNormal;
                System.out.println("BT.antiNormal() " + "endRotation=" + endRotation);
                autopilotValue = 0f;
                ap = true;
            }
            return ap;
        }


        public boolean doAutoLevel(int orbitalPlane, int orbitalPlaneModifier){
            boolean ap = false;
            return ap;
        }
        
        
        public void update(float tpf){
            //System.out.println("BURPTest.Ship.update() ");
            if (autopilot){
                //System.out.println("BURPTest.Ship.update() " + "autopilotValue=" + autopilotValue);
                //autopilotValue += (0.1f);// * tpf
                Quaternion currentRotation = ship.getLocalRotation();
                if (autopilotValue < 1f){
                    //currentRotation.lerp(currentRotation, endRotation, autopilotValue);
                    //currentRotation.slerp(startRotation, endRotation, autopilotValue);
                    boolean init = autopilotValue == 0.0f ? true : false;
                    currentRotation.burp(currentRotation, endRotation, autopilotValue, init);// * tpf
                    //currentRotation = currentRotation.normalizeLocal();
                    ship.setLocalRotation(currentRotation);
                    System.out.println(" BURPTest.Ship.update() " + "localRotation=" + ship.getLocalRotation() + ", endRotation=" + endRotation + ", autopilotValue=" + autopilotValue + ", init=" + init);
                    autopilotValue += (0.1f);// * tpf
                }
                else{
                    autopilotValue = 1f;
                    //currentRotation.lerp(currentRotation, endRotation, autopilotValue);
                    //currentRotation.slerp(startRotation, endRotation, autopilotValue);
                    currentRotation.burp(startRotation, endRotation, autopilotValue);// * tpf
                    ship.setLocalRotation(currentRotation);
                    System.out.println(" BURPTest.Ship.update() " + "localRotation=" + ship.getLocalRotation() + ", endRotation=" + endRotation + ", autopilotValue=" + autopilotValue);
                    autopilot = false;
                }
            }
        }
        
        
        public Vector3f getPrograde(){
            float G = app.getGravity();
            float m2 = app.getPlanet().getMass();
            float R = app.getPlanet().getRadius();
            
            float v = getOrbitalVelocity();
            //Vector3f v = (getWorldRotation().getRotationColumn(Z)).normalize();
            Vector3f r = (ship.getWorldTranslation().subtract(getPlanet().getWorldTranslation())).normalize();//vector from parent to satellite
            Vector3f h = r.mult(v);
            return h;
        }
        
        
        public Vector3f getAngularMomentum(){
            Vector3f r = (planet.getWorldTranslation().subtract(ship.getWorldTranslation())).normalize();//vector from parent to satellite
            Vector3f v = (ship.getWorldRotation().getRotationColumn(Z)).normalize();
            Vector3f h = r.mult(v);
            return h;
        }
        
        
        public Vector3f ascendingNode(){
            Vector3f r = getAngularMomentum();
            Vector3f K = new Vector3f(0f, 0f, 1f);
            Vector3f n = K.mult(r);
            return n;
        }
        
        
        public float getOrbitalVelocity(){
            float G = app.getGravity();
            float M2 = app.getPlanet().getMass();
            float R = app.getPlanet().getRadius();
            
            float v = (float)Math.sqrt((G * M2)/R);
            return v;
        }
        
        

    }
    
    
    public class Planet{
        float mass = 1.0e10f;
        float radius = 6000f;
        Vector3f localTranslation = new Vector3f(100f, 30f, -450f);
        Vector3f worldTranslation = new Vector3f();
        Quaternion localRotation = new Quaternion();
        Quaternion worldRotation = new Quaternion();
        
        
        public void setLocalTranslation(Vector3f localTranslation){
            this.localTranslation = localTranslation;
            worldTranslation = localTranslation.clone();
        }
        
        
        public Vector3f getWorldTranslation(){
            return worldTranslation;
        }
        
        
        public void setLocalRotation(Quaternion localRotation){
            this.localRotation = localRotation;
            worldRotation = localRotation.clone();
        }
        
        
        public Quaternion getWorldRotation(){
            return worldRotation;
        }
        
        
        public float getMass(){
            return mass;
        }
        public float getRadius(){
            return radius;
        }
    }
    
    
    public class Orbit{
        float semiMajorAxis = 0f;
        float semiMinorAxis = 3000f;
        float inclination = 5.0f;
        float semiVerticalAxis = semiMajorAxis * inclination;
        float eccentricity = 1.1f;
        float altitude = 200f;
        float radialRadian = 0.0025f;
        
        //factor = (float)Math.sqrt(e2);//leaves NAN
        //factor = (float)Math.sqrt(0.21d);
        //ZAxis = XAxis * factor;
        public void setOrbit(Planet p){
            semiMajorAxis = p.getRadius() + altitude;
            semiMinorAxis = semiMajorAxis * (float)Math.sqrt(eccentricity * eccentricity);
            semiVerticalAxis = semiMajorAxis;
        }
        
        public Vector3f getPrograde(){
            Vector3f v = new Vector3f();
            Vector3f r = getRadialTranslation(semiMajorAxis, semiVerticalAxis, semiMinorAxis, radialRadian);
            r = r.normalize();
            Vector3f h = r.mult(v);
            
            return h;
        }
        
        
        public Vector3f getRadialTranslation(float xa, float ya, float za, float radialRadian){
            return new Vector3f((float)(Math.cos((double)radialRadian) * xa), (float)(Math.cos((double)radialRadian) * ya), (float)(Math.sin((double)radialRadian) * za));
        }
        //}
    }


}
