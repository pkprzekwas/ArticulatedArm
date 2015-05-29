
package articulatedarm;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GraphicsConfiguration;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.Timer;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 *
// * @author Patryk and Kuba
 */
public class ArticulatedArm extends Applet implements ActionListener, KeyListener {

    private final Button pozPoczatkowa         = new Button("Ustawienie poczatkowe");
    private final Button naukaPocz             = new Button("Rozpocznij naukę");
    private final Button naukaKon              = new Button("Zakończ naukę");
    private final Button odtwarzaj             = new Button("Odtwórz zapisaną trasę");
    private Timer Timer1;
    private boolean klawisz_9=false, klawisz_8=false, klawisz_7=false;
    private boolean klawisz_6=false, klawisz_5=false, klawisz_4=false;
    private boolean klawisz_3=false, klawisz_2=false, klawisz_1=false;
    private boolean klawisz_up=false, klawisz_down=false, klawisz_left=false, klawisz_right=false;
    
    double Rotation1=1;
    double Rotation2=1;
    double Rotation3=1;   
    double Rotation4=1;
    double Rotation5=1;
    float Move6=0.3f;
    boolean GripLocked;
    boolean CollisionDetected = false;
    int BallGrabbed= 0;
    int BallFall=0;
    
    TransformGroup TransformFloor;
    TransformGroup TransformBase;
    TransformGroup Transform_1stPart;
    TransformGroup Transform_1stPartRotation;
    TransformGroup Transform_1stConnector;
    TransformGroup Transform_2ndPart;
    TransformGroup Transform_2ndPartRotation;
    TransformGroup Transform_2ndConnector;
    TransformGroup Transform_3rdPart;
    TransformGroup Transform_3rdPartRotation;
    TransformGroup Transform_3rdConnector;
    TransformGroup Transform_4thPart;
    TransformGroup Transform_4thPartRotationX;
    TransformGroup Transform_4thPartRotationZ;
    TransformGroup Transform_4thConnector;
    TransformGroup Transform_5thPart;
    TransformGroup Transform_5thPartRotation;
    TransformGroup Transform_5thConnector;
    TransformGroup TransformGrip;
    TransformGroup TransformGripRotation;
    TransformGroup TransformHolder1;
    TransformGroup TransformHolder2;
    TransformGroup TransformHolder1Move;
    TransformGroup TransformHolder2Move;
    TransformGroup TransformBall;
    TransformGroup TransformBallMove;
    TransformGroup TransformEnd;
    
    Transform3D Transform3dBase;
    Transform3D Transform3d_1stPart;
    Transform3D Transform3d_1stPartRotation;
    Transform3D Transform3d_1stConnector;
    Transform3D Transform3d_2ndPart;
    Transform3D Transform3d_2ndPartRotation;
    Transform3D Transform3d_2ndConnector;
    Transform3D Transform3d_3rdPart;
    Transform3D Transform3d_3rdPartRotation;
    Transform3D Transform3d_3rdConnector;
    Transform3D Transform3d_4thPart;
    Transform3D Transform3d_4thPartRotationX;
    Transform3D Transform3d_4thPartRotationZ;
    Transform3D Transform3d_4thConnector;
    Transform3D Transform3d_5thPart;
    Transform3D Transform3d_5thPartRotation;
    Transform3D Transform3d_5thConnector;
    Transform3D Transform3dGrip;
    Transform3D Transform3dGripRotation;   
    Transform3D Transform3dHolder1;
    Transform3D Transform3dHolder2;
    Transform3D Transform3dHolder1Move;
    Transform3D Transform3dHolder2Move;
    Transform3D Transform3dBall;
    Transform3D Transform3dBallMove;
    Transform3D Transform3dEnd;
    
    Cylinder Floor;
    Sphere Ball;
    Cylinder Base;
    Cylinder _1stPart;
    Sphere _1stConnector;
    Cylinder _2ndPart;
    Cylinder _2ndPartBegine;
    Sphere _2ndConnector;
    Cylinder _3rdPart;
    Sphere _3rdConnector;
    Cylinder _4thPart;
    Box Grip;
    Box Holder1;
    Box Holder2;
    Sphere End;
    
    Vector3f GripPosition;
    Vector3f BallPosition;
    Vector3f GripToObjectVevtor;
    ArticulatedArm(){

        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        add(BorderLayout.CENTER, canvas);
        canvas.addKeyListener(this);

        // Stworzenie przycisków interfejsu
                
        Panel p = new Panel();
        
        p.add(naukaPocz);
        add("North",p);
        naukaPocz.addActionListener(this);
        naukaPocz.addKeyListener(this);
        
        p.add(naukaKon);
        add("North",p);
        naukaKon.addActionListener(this);
        naukaKon.addKeyListener(this);
        
        p.add(odtwarzaj);
        add("North",p);
        odtwarzaj.addActionListener(this);
        odtwarzaj.addKeyListener(this);
        
        p.add(pozPoczatkowa); 
        add("North", p); 
        pozPoczatkowa.addActionListener(this);
        pozPoczatkowa.addKeyListener(this);
        
        
        SimpleUniverse uni = new SimpleUniverse(canvas);
               //sterowanie obserwatorem
       OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL); //sterowanie myszką
       BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0),500);
       orbit.setSchedulingBounds(bounds);
       ViewingPlatform vp = uni.getViewingPlatform();
       vp.setViewPlatformBehavior(orbit);
        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.9f,9.0f));
        uni.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        BranchGroup mainScene = Scena(uni);
        uni.addBranchGraph(mainScene);
        Timer1 = new Timer(20,this);  
        Timer1.start();
        GripPosition = new Vector3f();
        BallPosition = new Vector3f();
    }
    
    public void przyciski() {

    }
    
    public BranchGroup Scena (SimpleUniverse uni){
        
        BranchGroup scena = new BranchGroup();
    //    TransformGroup trans = new TransformGroup();
      //  trans = uni.getViewingPlatform().getViewPlatformTransform();

        // Ustawienie wyglądu wszystkich elementów
        
        // Matriał i wygląd robota
        Material mat = new Material();
        mat.setAmbientColor(new Color3f(0.1f, 0.1f, 1.0f));
        
        Appearance wyglad = new Appearance();
        wyglad.setColoringAttributes(new ColoringAttributes(0.1f,0.1f,0.1f,ColoringAttributes.NICEST));
        wyglad.setMaterial(mat);
        
        // Materiał i wygląd podstawy
        Material mat_podst = new Material();
        mat_podst.setAmbientColor(new Color3f(0.3f, 0.2f, 1.0f));
        
        Appearance wyglad2 = new Appearance();
        wyglad2.setColoringAttributes(new ColoringAttributes(0.5f,0.5f,0.9f,ColoringAttributes.NICEST));
        wyglad2.setMaterial(mat_podst);
      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
        // Rysowanie statywu i podstawy robota   

        
        
        
        
        //Podłoga
        Floor = new Cylinder(5.0f, 0.01f, wyglad2);
        TransformFloor = new TransformGroup();
        TransformFloor.addChild(Floor);    
        TransformFloor.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        scena.addChild(TransformFloor);
        
        //Kulka
        Ball = new Sphere(0.10f, 1, 20, wyglad);
        Transform3dBall = new Transform3D();
        Transform3dBall.set(new Vector3f(1.5f,0.10f,1.5f));
        TransformBall = new TransformGroup(Transform3dBall);
        Transform3dBallMove = new Transform3D();
        TransformBall.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  
        TransformBallMove= new TransformGroup(Transform3dBallMove);
        TransformBallMove.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
        TransformBall.addChild(TransformBallMove);
        TransformBallMove.addChild(Ball);
        TransformFloor.addChild(TransformBall);
         
        // Podstawka
        Base = new Cylinder(0.5f,0.4f,2, wyglad);        
        Transform3dBase = new Transform3D();
        TransformBase = new TransformGroup();
        Transform3dBase.set(new Vector3f(0.0f,0.2f,0.0f));
        TransformBase.setTransform(Transform3dBase);
        TransformBase.addChild(Base);
        TransformBase.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformFloor.addChild(TransformBase);
       
        // Pierwsza część
        _1stPart  = new Cylinder(0.28f, 1.0f, wyglad);
        Transform3d_1stPart = new Transform3D();  
        Transform3d_1stPartRotation= new Transform3D();
        Transform_1stPartRotation = new TransformGroup();
        Transform_1stPart = new TransformGroup();
        Transform3d_1stPart.set(new Vector3f(0.0f, 0.5f, 0));
        Transform_1stPart.setTransform(Transform3d_1stPart);
        Transform_1stPartRotation.setTransform(Transform3d_1stPartRotation);
        Transform_1stPart.addChild(_1stPart);        
        Transform_1stPartRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);        
        Transform_1stPart.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform_1stPartRotation.addChild(Transform_1stPart);        
        TransformBase.addChild(Transform_1stPartRotation);
        
        // Pierwsza nakładka
        _1stConnector = new Sphere(0.28f, 1, 20, wyglad);
        Transform3d_1stConnector = new Transform3D();
        Transform3d_1stConnector.set(new Vector3f(0.0f,0.5f,0.0f));
        Transform_1stConnector = new TransformGroup(Transform3d_1stConnector);
        Transform_1stConnector.addChild(_1stConnector);
        Transform_1stPart.addChild(Transform_1stConnector);
        
        // Druga część
        _2ndPart = new Cylinder(0.19f, 1.0f, wyglad);
        _2ndPartBegine = new Cylinder(0.24f, 0.1f, wyglad);
        Transform3D Transform3d_2ndPartBegine;
        TransformGroup Transform_2ndPartBegine;
        Transform3d_2ndPart = new Transform3D();  
        Transform3d_2ndPartRotation= new Transform3D();
        Transform_2ndPartRotation = new TransformGroup();
        Transform_2ndPart = new TransformGroup();
        Transform3d_2ndPart.set(new Vector3f(0.0f, 0.6f, 0));
        Transform_2ndPart.setTransform(Transform3d_2ndPart);
        Transform3d_2ndPartBegine = new Transform3D();  
        Transform_2ndPartBegine = new TransformGroup();
        Transform3d_2ndPartBegine.set(new Vector3f(0.0f, 0.5f, 0));
        Transform_2ndPartBegine.setTransform(Transform3d_2ndPartBegine);
        Transform_2ndPartRotation.setTransform(Transform3d_2ndPartRotation);
        Transform_2ndPart.addChild(_2ndPart);    
        Transform_2ndPartBegine.addChild(_2ndPartBegine);    
        Transform_2ndPart.addChild(Transform_2ndPartBegine);      
        Transform_2ndPartRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);        
        Transform_2ndPart.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform_2ndPartRotation.addChild(Transform_2ndPart);        
        Transform_1stConnector.addChild(Transform_2ndPartRotation);
        
        // Druga nakładka
        _2ndConnector = new Sphere(0.24f, 1, 20, wyglad);
        Transform3d_2ndConnector = new Transform3D();
        Transform3d_2ndConnector.set(new Vector3f(0.0f,0.63f,0.0f));
        Transform_2ndConnector = new TransformGroup(Transform3d_2ndConnector);
        Transform_2ndConnector.addChild(_2ndConnector);
        Transform_2ndPart.addChild(Transform_2ndConnector);
        
        // Trzecia część
        _3rdPart = new Cylinder(0.16f, 1.2f, wyglad);
        Transform3d_3rdPart = new Transform3D();  
        Transform3d_3rdPartRotation= new Transform3D();
        Transform_3rdPartRotation = new TransformGroup();
        Transform_3rdPart = new TransformGroup();
        Transform3d_3rdPart.set(new Vector3f(0.0f, 0.6f, 0));
        Transform_3rdPart.setTransform(Transform3d_3rdPart);
        Transform_3rdPartRotation.setTransform(Transform3d_3rdPartRotation);
        Transform_3rdPart.addChild(_3rdPart);        
        Transform_3rdPartRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);        
        Transform_3rdPart.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform_3rdPartRotation.addChild(Transform_3rdPart);        
        Transform_2ndConnector.addChild(Transform_3rdPartRotation);       
 
        // trzecia nakładka
        _3rdConnector = new Sphere(0.15f, 1, 20, wyglad);
        Transform3d_3rdConnector = new Transform3D();
        Transform3d_3rdConnector.set(new Vector3f(0.0f,0.61f,0.0f));
        Transform_3rdConnector = new TransformGroup(Transform3d_3rdConnector);
        Transform_3rdConnector.addChild(_3rdConnector);
        Transform_3rdPart.addChild(Transform_3rdConnector);
        
        //Czwarta część
        _4thPart = new Cylinder(0.1f, 0.5f, wyglad);
        Transform3d_4thPart = new Transform3D();  
        Transform3d_4thPartRotationX= new Transform3D();
        Transform3d_4thPartRotationZ= new Transform3D();
        Transform_4thPartRotationX = new TransformGroup();
        Transform_4thPartRotationZ = new TransformGroup();
        Transform_4thPart = new TransformGroup();
        Transform3d_4thPart.set(new Vector3f(0.0f, 0.25f, 0));
        Transform_4thPart.setTransform(Transform3d_4thPart);
        Transform_4thPartRotationX.setTransform(Transform3d_4thPartRotationX);
         Transform_4thPartRotationZ.setTransform(Transform3d_4thPartRotationX);
        Transform_4thPart.addChild(_4thPart);        
        Transform_4thPartRotationX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);        
        Transform_4thPartRotationZ.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);        
        Transform_4thPart.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform_4thPartRotationZ.addChild(Transform_4thPartRotationX);    
        Transform_4thPartRotationX.addChild(Transform_4thPart);        
        Transform_3rdConnector.addChild(Transform_4thPartRotationZ);  
        
        // Chwytak
        Grip = new Box(0.2f, 0.01f, 0.08f, wyglad);
        Transform3dGrip = new Transform3D();  
        Transform3dGripRotation= new Transform3D();
        TransformGripRotation = new TransformGroup();
        TransformGrip = new TransformGroup();
        Transform3dGrip.set(new Vector3f(0.0f, 0.5f, 0));
        TransformGrip.setTransform(Transform3dGrip);
        TransformGripRotation.setTransform(Transform3dGripRotation);
        TransformGrip.addChild(Grip);        
        TransformGripRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);        
        TransformGrip.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformGripRotation.addChild(TransformGrip);        
        Transform_4thPartRotationX.addChild(TransformGripRotation); 
        
        // Palce
        Holder1 = new Box(0.01f, 0.1f,0.08f, wyglad);
        Holder2 = new Box(0.01f, 0.1f,0.08f, wyglad);
        Transform3dHolder1 = new Transform3D();  
        Transform3dHolder2 = new Transform3D();  
        
        Transform3dHolder1Move= new Transform3D();
        Transform3dHolder2Move= new Transform3D();
        
        TransformHolder1 = new TransformGroup();
        TransformHolder2 = new TransformGroup();
        
        TransformHolder1Move = new TransformGroup();
        TransformHolder2Move = new TransformGroup();
        
        Transform3dHolder1.set(new Vector3f(0.17f, 0.1f, 0));
        Transform3dHolder2.set(new Vector3f(-0.17f, 0.1f, 0));
        TransformHolder1.setTransform(Transform3dHolder1);
        TransformHolder2.setTransform(Transform3dHolder2);
        TransformHolder1Move.setTransform(Transform3dHolder1Move);
        TransformHolder2Move.setTransform(Transform3dHolder2Move);
        TransformHolder1.addChild(Holder1);  
        TransformHolder2.addChild(Holder2);
        TransformHolder1Move.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
        TransformHolder2Move.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); 
        TransformHolder1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformHolder2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformHolder1Move.addChild(TransformHolder1); 
        TransformHolder2Move.addChild(TransformHolder2); 
        TransformGrip.addChild(TransformHolder1Move); 
        TransformGrip.addChild(TransformHolder2Move); 
        
        // Końcówka
        End = new Sphere(0.01f, 1, 20, wyglad);
        Transform3dEnd = new Transform3D();
        Transform3dEnd.set(new Vector3f(0.0f,0.2f,0.0f));
        TransformEnd = new TransformGroup(Transform3dEnd);
        TransformEnd.addChild(End);
        Grip.addChild(TransformEnd);
                
                
                
        BoundingSphere bounds = new BoundingSphere (new Point3d(0.0,0.0,0.0),100.0);
      
        //ŚWIATŁO KIERUNKOWE
        Color3f light1Color = new Color3f(0.7f,0.7f,0.7f);
        Vector3f light1Direction = new Vector3f(4.0f,-7.0f,-12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        scena.addChild(light1);
    
    
        //ŚWIATŁO PUNKTOWE
        Color3f ambientColor = new Color3f(0.7f,0.7f,0.7f);
        AmbientLight ambientLightNode= new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        scena.addChild(ambientLightNode);   
        
        
        scena.compile();
        return scena;        
    }
 
    public static void main(String[] args) {
        ArticulatedArm okno = new ArticulatedArm();
        okno.addKeyListener(okno);
        
        MainFrame mainFr = new MainFrame(okno, 900, 600); 
    }

    @Override

    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD1) {klawisz_1=true;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD2) {klawisz_2=true;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD3) {klawisz_3=true;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD4) {klawisz_4=true;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD5) {klawisz_5=true;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD6) {klawisz_6=true;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD7) {klawisz_7=true;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD8) {klawisz_8=true;}       
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD9) {klawisz_9=true;}
        if (e.getKeyCode()==KeyEvent.VK_UP)      {klawisz_up=true;}
        if (e.getKeyCode()==KeyEvent.VK_DOWN)    {klawisz_down=true;}
        if (e.getKeyCode()==KeyEvent.VK_LEFT)    {klawisz_left=true;}
        if (e.getKeyCode()==KeyEvent.VK_RIGHT)   {klawisz_right=true;}
 
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD1) {klawisz_1=false;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD2) {klawisz_2=false;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD3) {klawisz_3=false;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD4) {klawisz_4=false;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD5) {klawisz_5=false;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD6) {klawisz_6=false;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD7) {klawisz_7=false;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD8) {klawisz_8=false;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD9) {klawisz_9=false;}
        if (e.getKeyCode()==KeyEvent.VK_UP)      {klawisz_up=false;}
        if (e.getKeyCode()==KeyEvent.VK_DOWN) {klawisz_down=false;}
        if (e.getKeyCode()==KeyEvent.VK_LEFT) {klawisz_left=false;}
        if (e.getKeyCode()==KeyEvent.VK_RIGHT) {klawisz_right=false;}
    }

    @Override
   public void actionPerformed(ActionEvent e) {
        Collision();
        GrabBall();
        MoveBall();
        // 1 stopień
        if(Rotation1 > -2.65){
                if(klawisz_9==true){Rotation1=Rotation1-0.03;}
            }
        if(Rotation1 < 2.65){
                if(klawisz_7==true){Rotation1=Rotation1+0.03;}
            }
        // 2 Stopień
        if(Rotation2 > 0.2){
                if(klawisz_4==true){Rotation2=Rotation2-0.03;}
            }
        if(Rotation2 < 1.6){
                if(klawisz_1==true){Rotation2=Rotation2+0.03;}
            }
        // 3 Stopień
        if(Rotation3 > -1.0){
                if(klawisz_5==true){Rotation3=Rotation3-0.03;}
            }
        if(Rotation3 < 1.7){
                if(klawisz_2==true){Rotation3=Rotation3+0.03;}
            }
        // 4 Stopień
       if(Rotation4 > -1){
                if(klawisz_up==true){Rotation4=Rotation4-0.03;}
            }
        if(Rotation4 < 1){
                if(klawisz_down==true){Rotation4=Rotation4+0.03;}
            }
       // 5Stopień
       if(Rotation5 > -1){
                if(klawisz_left==true){Rotation5=Rotation5-0.03;}
            }
       if(Rotation5 < 1){
                if(klawisz_right==true){Rotation5=Rotation5+0.03;}
            }
        // Chwytak
        if(Move6 > 0.28){
                if(klawisz_6==true){
                    Move6=Move6-0.03f;}
            }
        if(Move6 < 0.32){
                if(klawisz_3==true){Move6=Move6+0.03f;}
            }
           Transform3d_1stPartRotation.rotY(Rotation1);
           Transform_1stPartRotation.setTransform(Transform3d_1stPartRotation);
           
           Transform3d_2ndPartRotation.rotX(Rotation2);
           Transform_2ndPartRotation.setTransform(Transform3d_2ndPartRotation);
           
           Transform3d_3rdPartRotation.rotX(Rotation3);
           Transform_3rdPartRotation.setTransform(Transform3d_3rdPartRotation);
        
           Transform3d_4thPartRotationX.rotX(Rotation4);
           Transform3d_4thPartRotationZ.rotZ(Rotation5);
           Transform_4thPartRotationX.setTransform(Transform3d_4thPartRotationX);
           Transform_4thPartRotationZ.setTransform(Transform3d_4thPartRotationZ);
           
           Transform3dHolder1Move.set(new Vector3f(-Move6, 0, 0));
           TransformHolder1Move.setTransform(Transform3dHolder1Move);
           Transform3dHolder2Move.set(new Vector3f(Move6, 0, 0));
           TransformHolder2Move.setTransform(Transform3dHolder2Move);
          
           if(Move6<0.3 )
               GripLocked=true;
           else
               GripLocked=false;
        }
    
public void Collision(){
    Transform3D Transform3dTemp; 
    Transform3dTemp= new Transform3D();
 End.getLocalToVworld(Transform3dTemp);
 Transform3dTemp.get(GripPosition);
 
        
        Transform3D Transform3dTemp2= new Transform3D();
        TransformBallMove.getLocalToVworld(Transform3dTemp2);
        Transform3dTemp2.get(BallPosition);
 
 
 
 
 if (GripPosition.y<0.01)
 {
     CollisionDetected=true;
     klawisz_up=false;
     klawisz_down=false;
     klawisz_left=false;
     klawisz_right=false;
     
     if(klawisz_1)
     {
         klawisz_4=true;
         klawisz_1=false;
     }
     if(klawisz_2)
     {
         klawisz_5=true;
         klawisz_2=false;
     }              
 }
 if(CollisionDetected && GripPosition.y>0.03 )
 {
     klawisz_1=false;
     klawisz_4=false;
     klawisz_5=false;
     klawisz_2=false;
     CollisionDetected=false;
     
 }   
    
}
  public void GrabBall()
  {
      if(GripLocked && BallGrabbed==0)
      {
        //liczenie odległości
          float BallGripDistance;
        
        Point3f _1point= new Point3f(BallPosition.getX(),BallPosition.getY(),BallPosition.getZ());
        Point3f _2point= new Point3f(GripPosition.getX(),GripPosition.getY(),GripPosition.getZ());
        BallGripDistance= _1point.distance(_2point);
        if(BallGripDistance<0.1f)
        {
            BallGrabbed=1;
        }

      }
      if(!GripLocked)
           BallGrabbed=0;
  }
  public void MoveBall()
  {
      if(BallGrabbed==1)
      {
          GripToObjectVevtor= new Vector3f(BallPosition);
          GripToObjectVevtor.sub(GripPosition);
          BallGrabbed=2;
         }
      if(BallGrabbed==2)
      {
          Vector3f temp = new Vector3f(BallPosition);
          temp.sub(GripPosition);
          Transform3dBallMove.set(new Vector3f(-temp.x+GripToObjectVevtor.x,-temp.y+GripToObjectVevtor.y,-temp.z+GripToObjectVevtor.z));
          TransformBallMove.setTransform(Transform3dBallMove);
      }
  
      if(BallGrabbed==0)
      {
          
          
          if(BallPosition.y>0.1f)
          {
          BallFall++;
          Transform3dBallMove.set(new Vector3f(0.0f,-0.001f*BallFall*BallFall, 0.0f));
          TransformBallMove.setTransform(Transform3dBallMove);
          }
          else
              BallFall=0;
      }
  } 
}
