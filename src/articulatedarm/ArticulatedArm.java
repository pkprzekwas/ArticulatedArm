
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
    private Timer zegar_1;
    private boolean klawisz_9=false, klawisz_8=false, klawisz_7=false;
    private boolean klawisz_6=false, klawisz_5=false, klawisz_4=false;
    private boolean klawisz_3=false, klawisz_2=false, klawisz_1=false;
   // private boolean klawisz_w=false, klawisz_q=false, klawisz_e=false;
    
    double Rotation1=0;
    
   
    TransformGroup TransformFloor;
    TransformGroup TransformBase;
    TransformGroup Transform_1stPart;
    TransformGroup Transform_1stPartRotation;
    
    
    TransformGroup Transform_1stConnector;
    
    
    Transform3D Transform3d_1stPart;
    Transform3D Transform3d_1stPartRotation;
    Transform3D _1stPartRotation; 
    Transform3D Transform3dBase;
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
        zegar_1 = new Timer(10,this);  
        zegar_1.start();
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
        wyglad.setColoringAttributes(new ColoringAttributes(0.8f,0.5f,0.9f,ColoringAttributes.NICEST));
        wyglad.setMaterial(mat);
        
        // Materiał i wygląd podstawy
        Material mat_podst = new Material();
        mat_podst.setAmbientColor(new Color3f(0.3f, 0.2f, 1.0f));
        
        Appearance wyglad2 = new Appearance();
        wyglad2.setColoringAttributes(new ColoringAttributes(0.5f,0.5f,0.9f,ColoringAttributes.NICEST));
        wyglad2.setMaterial(mat_podst);
      ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
        // Rysowanie statywu i podstawy robota   
        //Rotacje
        _1stPartRotation= new Transform3D();
        
        
        
        
        //Podłoga
        Cylinder Floor = new Cylinder(2.0f, 0.01f, wyglad2);
        TransformFloor = new TransformGroup();
        TransformFloor.addChild(Floor);    
        TransformFloor.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        scena.addChild(TransformFloor);
         
        // Podstawka
        Cylinder Base = new Cylinder(0.5f,0.4f,2, wyglad);        
        Transform3dBase = new Transform3D();
        TransformBase = new TransformGroup();
        Transform3dBase.set(new Vector3f(0.0f,0.2f,0.0f));
        TransformBase.setTransform(Transform3dBase);
        TransformBase.addChild(Base);
        TransformBase.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformFloor.addChild(TransformBase);
       
        // Pierwsza część
        Cylinder _1stPart = new Cylinder(0.2f, 1.0f, wyglad);
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
        Sphere _1stConnector = new Sphere(0.22f, 1, 20, wyglad);
        Transform3D Transform3d_1stConnector = new Transform3D();
        Transform3d_1stConnector.set(new Vector3f(0.0f,0.5f,0.0f));
        Transform_1stConnector = new TransformGroup(Transform3d_1stConnector);
        Transform_1stConnector.addChild(_1stConnector);
        Transform_1stPart.addChild(Transform_1stConnector);
 
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
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD7) {
            klawisz_7=true;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD8) {klawisz_8=true;}
        if (e.getKeyCode()==KeyEvent.VK_NUMPAD9) {klawisz_9=true;}

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
    }

    @Override
   public void actionPerformed(ActionEvent e) {
 
        
        //if(Rotation1 > -2.65){
                if(klawisz_7==true){
                    Rotation1=Rotation1-0.03;}
           // }
       // if(Rotation1 < 2.65){
                if(klawisz_9==true){Rotation1=Rotation1+0.03;}
         //   }
           Transform3d_1stPartRotation.rotY(Rotation1);
           Transform3d_1stPart.add(Transform3d_1stPartRotation);
           Transform_1stPartRotation.setTransform(Transform3d_1stPartRotation);
           
        
        }
    
    
}
