
package articulatedarm;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.universe.SimpleUniverse;
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
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
//chuj
/**
 *
// * @author Patryk and Kuba
 */
public class ArticulatedArm extends Applet implements ActionListener, KeyListener {

    private final Button pozPoczatkowa         = new Button("Ustawienie poczatkowe");
    private final Button naukaPocz             = new Button("Rozpocznij naukę");
    private final Button naukaKon              = new Button("Zakończ naukę");
    private final Button odtwarzaj             = new Button("Odtwórz zapisaną trasę");
    
    
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
        
        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.8f,5.0f));
        uni.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        BranchGroup mainScene = Scena(uni);
        uni.addBranchGraph(mainScene);

    }
    
    public void przyciski() {

    }
    
    public BranchGroup Scena (SimpleUniverse uni){
        
        BranchGroup scena = new BranchGroup();
        TransformGroup trans = new TransformGroup();
        trans = uni.getViewingPlatform().getViewPlatformTransform();

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
        
        // Rysowanie statywu i podstawy robota       
        TransformGroup robot = new TransformGroup();
        robot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        Cylinder podstawa = new Cylinder(2.0f, 0.01f, wyglad2);
        TransformGroup trans_podstawa = new TransformGroup();
        trans_podstawa.addChild(podstawa);       
        scena.addChild(trans_podstawa);
        
        Box statyw = new Box(0.25f,0.015f,0.25f, wyglad);        
        Transform3D p_statywu = new Transform3D();
        p_statywu.set(new Vector3f(0.0f,0.005f,0.0f));
        TransformGroup transformacja_s = new TransformGroup(p_statywu);
        transformacja_s.addChild(statyw);
        robot.addChild(transformacja_s);
       
        Cylinder walec = new Cylinder(0.12f, 0.8f, wyglad);
        
        
        Transform3D p_walec = new Transform3D();
        p_walec.set(new Vector3f(0.0f, 0.395f, 0));
        TransformGroup przes_walec = new TransformGroup(p_walec);
        
        przes_walec.addChild(walec);
        robot.addChild(przes_walec);
        
        Box glowica = new Box(0.1f, 0.1f, 0.1f, wyglad);
        Transform3D p_glowicy = new Transform3D();
        p_glowicy.set(new Vector3f(0.0f,0.895f,0.0f));
        TransformGroup przes_glow = new TransformGroup(p_glowicy);
        przes_glow.addChild(glowica);
        robot.addChild(przes_glow);
 
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
        
        scena.addChild(robot);
        scena.compile();
        return scena;        
    }
        
    public static void main(String[] args) {
        ArticulatedArm okno = new ArticulatedArm();
        okno.addKeyListener(okno);
        MainFrame mainFr = new MainFrame(okno, 900, 600); 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
