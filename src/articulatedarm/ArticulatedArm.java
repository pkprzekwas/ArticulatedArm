package articulatedarm;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
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
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
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
 * // * @author Patryk and Kuba
 */
public class ArticulatedArm extends Applet implements ActionListener, KeyListener {
//przyciski

    private final Button pozPoczatkowa = new Button("Ustawienie poczatkowe");
    private final Button naukaPocz = new Button("Rozpocznij naukę");
    private final Button naukaKon = new Button("Zakończ naukę");
    private final Button odtwarzaj = new Button("Odtwórz zapisaną trasę");
    //Timer    
    private final Timer Timer1;
    //Klawisze   
    private boolean klawisz_9 = false, klawisz_8 = false, klawisz_7 = false;
    private boolean klawisz_6 = false, klawisz_5 = false, klawisz_4 = false;
    private boolean klawisz_3 = false, klawisz_2 = false, klawisz_1 = false;
    private boolean klawisz_up = false, klawisz_down = false, klawisz_left = false, klawisz_right = false;
    //Flagi  
    private boolean recording = false;
    private boolean playing = false;
    private boolean waiting = false;
    //Zmienne do nagrywania ruchów
    public int[] steps;
    public int steps_counter = 0;
    public int steps_count;
    //Wartość rotacji obrotów dla każdego stopnia swobody
    double Rotation1 = 1;
    double Rotation2 = 1;
    double Rotation3 = 1;
    double Rotation4 = 1;
    double Rotation5 = 1;
    float Move6 = 0.3f;
    boolean GripLocked; //informacja czy chwytak jest zaciśnięty
    boolean CollisionDetected = false;  //flaga wykrycia kolizji
    int BallGrabbed = 0; //flaga informująca czy piłeczka jest złapana
    int BallFall = 0;     //flaga informująca czy piłeczka spada
    //Transformgrupy wszystkich elementów
    TransformGroup TransformFloor;  //Transfor Group podłogo
    TransformGroup TransformBase;       //Transfor Group podstawki
    TransformGroup Transform_1stPart;       //Transfor Group pierwszej części
    TransformGroup Transform_1stPartRotation;        //Transfor Group rotacji pierwszej części
    TransformGroup Transform_1stConnector;       //Transfor Group pierwszego konektora
    TransformGroup Transform_2ndPart;       //Transfor Group drugiej części
    TransformGroup Transform_2ndPartRotation;       //Transfor Group rotacji drugiej częśći
    TransformGroup Transform_2ndConnector;        //Transfor Group drugiego konektora
    TransformGroup Transform_3rdPart;       //Transfor Group trzeciej częśći
    TransformGroup Transform_3rdPartRotation;       //Transfor Group rotacji trzeciej części
    TransformGroup Transform_3rdConnector;       //Transfor Group trzeciego konektora
    TransformGroup Transform_4thPart;       //Transfor Group czwartej części
    TransformGroup Transform_4thPartRotationX;       //Transfor Group rotacji 4 części w płaszczyźnie x
    TransformGroup Transform_4thPartRotationZ;       //Transfor Group rotacji czwartej części w płaszczyźnie z
    TransformGroup Transform_4thConnector;       //Transfor Group czwartego konektora
    TransformGroup Transform_5thPart;       //Transfor Group piątej części
    TransformGroup Transform_5thPartRotation;       //Transfor Group rotacji piątej części
    TransformGroup Transform_5thConnector;       //Transfor Group piątego konektora
    TransformGroup TransformGrip;        //Transfor Group  zacisku kiści
    TransformGroup TransformGripRotation;       //Transfor Group rotacji kiści
    TransformGroup TransformHolder1;        //Transfor Group pierwszego palca
    TransformGroup TransformHolder2;        //Transfor Group drugiego palca
    TransformGroup TransformHolder1Move;       //Transfor Group ruchu pierwszego palca
    TransformGroup TransformHolder2Move;       //Transfor Group ruchu drugiego palca
    TransformGroup TransformBall;       //Transfor Group piłeczki
    TransformGroup TransformBallMove;       //Transfor Group przemieszczenia piłeczki
    TransformGroup TransformEnd;       //Transfor Group końcówki manipulatora

    Transform3D Transform3dBase;                 //Transform 3d podstawki
    Transform3D Transform3d_1stPart;                 //Transform 3d pierwszej części
    Transform3D Transform3d_1stPartRotation;                 //Transform 3d rotacji pierwszej części
    Transform3D Transform3d_1stConnector;                 //Transform 3d pierwszego konektora
    Transform3D Transform3d_2ndPart;                 //Transform 3d drugiej części
    Transform3D Transform3d_2ndPartRotation;                 //Transform 3d rotacji drugiej części
    Transform3D Transform3d_2ndConnector;                 //Transform 3d drugiego konektora
    Transform3D Transform3d_3rdPart;                 //Transform 3d trzeciej części
    Transform3D Transform3d_3rdPartRotation;                 //Transform 3d rotacji trzeciej części
    Transform3D Transform3d_3rdConnector;                 //Transform 3d trzeciego konektora
    Transform3D Transform3d_4thPart;                  //Transform 3d czwartej części
    Transform3D Transform3d_4thPartRotationX;                 //Transform 3d czwartej części w płaszczyźnie x
    Transform3D Transform3d_4thPartRotationZ;                 //Transform 3dczwartej części w płaszczyźnie z
    Transform3D Transform3d_4thConnector;                 //Transform 3d czwartego konnektora
    Transform3D Transform3d_5thPart;                 //Transform 3d piątej części
    Transform3D Transform3d_5thPartRotation;                 //Transform 3d rotacji piątej części
    Transform3D Transform3d_5thConnector;                 //Transform 3d piątego konektora
    Transform3D Transform3dGrip;                 //Transform 3d kiści
    Transform3D Transform3dGripRotation;                    //Transform 3d rotacji kości
    Transform3D Transform3dHolder1;                 //Transform 3d pierwszego palca
    Transform3D Transform3dHolder2;                  //Transform 3d drugiego palca
    Transform3D Transform3dHolder1Move;                 //Transform 3d ruchu pierwszego palca
    Transform3D Transform3dHolder2Move;                 //Transform 3d ruchu drugiego palca
    Transform3D Transform3dBall;                 //Transform 3d piłeczki
    Transform3D Transform3dBallMove;                 //Transform 3d ruchu piłeczki
    Transform3D Transform3dEnd;                 //Transform 3d końcówki
    //elementy robota
    Cylinder Floor;     // pierwszy cylinder będący podłogą robota
    Cylinder Floor1;    //zapasowa podłoga
    Cylinder Floor2;    //zapas zapasowej podłogi
    Sphere Ball;              //piłeczka
    Cylinder Base;            //podstawka do ustawienia na podłodze
    Cylinder _1stPart;        //pierwszy stopień swobody
    Sphere _1stConnector;     //pierwszy przegub
    Cylinder _2ndPart;        //drugi stopień swobody
    Cylinder _2ndPartBegine;  // nakładka
    Sphere _2ndConnector;     //drugi przegub
    Cylinder _3rdPart;        //trzeci stopień swobody
    Sphere _3rdConnector;     //trzeci przegub
    Cylinder _4thPart;        //czwarty stopień swobody
    Box Grip;                 //podstawa chwytaka
    Box Holder1;              //Palec 1
    Box Holder2;              //palec 2
    Sphere End;               //celownik

    Vector3f GripPosition;      //wektor pozycji chwytaka
    Vector3f BallPosition;      //wektor pozycji piłeczki
    Vector3f GripToObjectVevtor;  //wektor od kiści do piłeczki

    OrbitBehavior orbit; //obrót kamerą

    ArticulatedArm() {

        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        add(BorderLayout.CENTER, canvas);
        canvas.addKeyListener(this);

        // Stworzenie przycisków interfejsu
        Panel p = new Panel();

        p.add(naukaPocz);   //przycisk nauki
        add("North", p);
        naukaPocz.addActionListener(this);
        naukaPocz.addKeyListener(this);

        p.add(naukaKon);    //przycisk końca nauki
        add("North", p);
        naukaKon.addActionListener(this);
        naukaKon.addKeyListener(this);

        p.add(odtwarzaj);       //przycisk play
        add("North", p);
        odtwarzaj.addActionListener(this);
        odtwarzaj.addKeyListener(this);

        p.add(pozPoczatkowa);       //przycisk resetu robota
        add("North", p);
        pozPoczatkowa.addActionListener(this);
        pozPoczatkowa.addKeyListener(this);

        SimpleUniverse uni = new SimpleUniverse(canvas);
        //sterowanie obserwatorem
        orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL | OrbitBehavior.STOP_ZOOM | OrbitBehavior.DISABLE_TRANSLATE); //sterowanie myszką
        orbit.setMinRadius(2);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 50);
        orbit.setSchedulingBounds(bounds);
        ViewingPlatform vp = uni.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f, 0.9f, 9.0f));
        uni.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        BranchGroup mainScene = Scena(uni);
        uni.addBranchGraph(mainScene);
        Timer1 = new Timer(20, this);        //ustawienie timera
        Timer1.start();             //start timera
        GripPosition = new Vector3f();  //tworzenie wektora pozycji kiści
        BallPosition = new Vector3f();  //tworzenie wektora pozycji piłeczki
    }

    public BranchGroup Scena(SimpleUniverse uni) {

        BranchGroup scena = new BranchGroup();
        // Ustawienie wyglądu wszystkich elementów
        // Matriał i wygląd robota
        Material mat = new Material();          //materiał nr1 
        mat.setAmbientColor(new Color3f(0.9f, 0.9f, 0.9f));

        Appearance wyglad = new Appearance();       //apearance materiału nr 1
        wyglad.setColoringAttributes(new ColoringAttributes(0.9f, 0.9f, 0.9f, ColoringAttributes.NICEST));
        wyglad.setMaterial(mat);

        Material bl_mat = new Material();   // materiał nr 2
        bl_mat.setAmbientColor(new Color3f(0.0f, 0.0f, 0.0f));

        Appearance bl_app = new Appearance();   //Appearance materiału nr 2
        bl_app.setColoringAttributes(new ColoringAttributes(0.0f, 0.0f, 0.0f, ColoringAttributes.NICEST));
        bl_app.setMaterial(bl_mat);

        // Materiał i wygląd podstawy
        Material mat_podst = new Material();
        mat_podst.setAmbientColor(new Color3f(0.3f, 0.2f, 1.0f));

        Appearance wyglad2 = new Appearance(); //apperance wyglądu nr3
        wyglad2.setColoringAttributes(new ColoringAttributes(0.5f, 0.5f, 0.9f, ColoringAttributes.NICEST));
        wyglad2.setMaterial(mat_podst);

        // Materiał i wygląd klatki
        Material mat_cage = new Material();
        mat_cage.setAmbientColor(new Color3f(0.0f, 0.0f, 0.0f));

        Appearance floor_app = new Appearance();
        floor_app.setColoringAttributes(new ColoringAttributes(0.8f, 0.8f, 0.9f, ColoringAttributes.NICEST));
        floor_app.setMaterial(mat_cage);

        // Tekstury
        Appearance cage_tex = new Appearance();
        Appearance robo_tex = new Appearance();

        TextureLoader loader = new TextureLoader("obrazki/biolab.jpg", null);       //odczyt tekstury z obrazka w formacie jpg
        ImageComponent2D image = loader.getImage();                                     //załadowanie obrazka do image loadera

        Texture2D cage_t = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                image.getWidth(), image.getHeight());       ///stworzenie tekstury

        cage_t.setImage(0, image);
        cage_t.setBoundaryModeS(Texture.WRAP);      //ustawienie właściwośći tekstur
        cage_t.setBoundaryModeT(Texture.WRAP);

        loader = new TextureLoader("obrazki/murek.jpg", this); //odczyt tekstury z obrazka w formacie jpg
        image = loader.getImage(); //załadowanie obrazka do image loadera

        Texture2D murek = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                image.getWidth(), image.getHeight());///stworzenie tekstury
        murek.setImage(0, image);
        murek.setBoundaryModeS(Texture.WRAP);
        murek.setBoundaryModeT(Texture.WRAP);//ustawienie właściwośći tekstur

        cage_tex.setTexture(cage_t);
        robo_tex.setTexture(murek);

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
        // Rysowanie tła, statywu i podstawy robota   
        // Klatka
        Sphere cage = new Sphere(14f, Sphere.GENERATE_NORMALS_INWARD | Sphere.GENERATE_TEXTURE_COORDS, cage_tex);
        scena.addChild(cage);

        //Podłoga
        Floor = new Cylinder(5.0f, 0.01f, wyglad2);     //tworzenie obiektu podłogi
        TransformFloor = new TransformGroup();              //tworzenie transforma poidłogi
        TransformFloor.addChild(Floor);         //dodanie cylindra do podłogi
        TransformFloor.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); //ustawienie pojemności
        scena.addChild(TransformFloor); //dodanie podłogi do sceny

        Floor1 = new Cylinder(5.0f, 6.0f, wyglad);      //stwodzenie drugiej podłogi
        Transform3D floor_move = new Transform3D();         //stworzenie transforma przesunięcia podłogi
        floor_move.set(new Vector3f(0.0f, -3.0f, 0.0f));        //przenieszczenie podłogi
        TransformGroup TransformFloor1 = new TransformGroup(floor_move);    //tworzenie Tgrupy z T3d
        TransformFloor1.addChild(Floor1);   //dodanie podłogi do transforma
        TransformFloor1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);    //ustawienie pojemności
        scena.addChild(TransformFloor1);// dodanie podłogi 2 do sceny

        Floor2 = new Cylinder(13.5f, 3.0f, floor_app);      //stworzenie drugiej podłogi
        Transform3D floor_move2 = new Transform3D();        //stworzenie transformgrupy kolejnej zapasowej podłogi
        floor_move2.set(new Vector3f(0.0f, -2.0f, 0.0f));   //przemieszczenie zapasowej podłogi
        TransformGroup TransformFloor2 = new TransformGroup(floor_move2);       //stworzenie TG z T3d
        TransformFloor2.addChild(Floor2);   //dodanie trzeciej podłogi do transforma 3 podłogi
        TransformFloor2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);    //ustawienie pojemności
        scena.addChild(TransformFloor2);        //dodanie transforma trzeciej podłogi do drugiej

        //Kulka
        Ball = new Sphere(0.10f, 1, 20, bl_app);        //sworzenie obiektu kulistego
        Transform3dBall = new Transform3D();        //stworzenie transformu 3d determinującego lokację ładunku
        Transform3dBall.set(new Vector3f(1.5f, 0.10f, 1.5f));       //ustawienie transforma 3d jako wektor o jawnych składowych
        TransformBall = new TransformGroup(Transform3dBall);        //utworzenie tg z t3d
        Transform3dBallMove = new Transform3D();        //stworzenie transformgrupa dodającego modyfikację położenia wynikającego z przesunięcia ładunku za pomocą ramienia robota
        TransformBall.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);//ustawienie pojemności
        TransformBallMove = new TransformGroup(Transform3dBallMove);    //utworzenie tg z t3d
        TransformBallMove.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);      //ustawienie pojemności
        TransformBall.addChild(TransformBallMove); //dodanie dziecka do rodzica 
        TransformBallMove.addChild(Ball);//dodanie piłeczki do jej przesunięcia
        TransformFloor.addChild(TransformBall); // dodanie przemieszczenia do podłogi

        // Podstawka
        Base = new Cylinder(0.5f, 0.4f, bl_app); // stworzenie walca będącego podstawką pod robota
        Transform3dBase = new Transform3D();        //stworzenie transforma 3d determinującego położenie podftawki
        TransformBase = new TransformGroup(); /// stworzenie transformgrupa
        Transform3dBase.set(new Vector3f(0.0f, 0.2f, 0.0f));// ustawienie przesunięcia podstawki
        TransformBase.setTransform(Transform3dBase); //zrobienie transformgrupa z transforma3d
        TransformBase.addChild(Base); // dodanie elementów do podłoża
        TransformBase.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);// ustawieni pojemności
        scena.addChild(TransformBase);// dodanie podłoża do podłogi

        // Pierwsza część
        _1stPart = new Cylinder(0.2f, 1.0f, wyglad); // stworzenie ramienia
        Transform3d_1stPart = new Transform3D(); // stworzenie transform3d pierwszej częśći
        Transform3d_1stPartRotation = new Transform3D();//stworzenie transform3d rotacji
        Transform_1stPartRotation = new TransformGroup();//stworzenie transformgrupa rotacji
        Transform_1stPart = new TransformGroup();// stworzenie transform3d pierwszej częśći
        Transform3d_1stPart.set(new Vector3f(0.0f, 0.5f, 0)); // ustawienie wektora przesunięcia pierwszej części
        Transform_1stPart.setTransform(Transform3d_1stPart); // przełożenie t3d do tg
        Transform_1stPartRotation.setTransform(Transform3d_1stPartRotation);// przełożenie t3d do tg
        Transform_1stPart.addChild(_1stPart);//dodanie cylindra do świata widocznego 
        Transform_1stPartRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);// ustawienie pojemności
        Transform_1stPart.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform_1stPartRotation.addChild(Transform_1stPart); // dodanie transformów do ich rodziców
        TransformBase.addChild(Transform_1stPartRotation);

        // Pierwsza nakładka
        _1stConnector = new Sphere(0.28f, 1, 20, bl_app); //pierwszy konektor i jego rozmiary
        Transform3d_1stConnector = new Transform3D();
        Transform3d_1stConnector.set(new Vector3f(0.0f, 0.5f, 0.0f));// przesunięcie bazowe ppierwszego konektora
        Transform_1stConnector = new TransformGroup(Transform3d_1stConnector);
        Transform_1stConnector.addChild(_1stConnector); //dodanie kulki pierwszego konektora do ramienia
        Transform_1stPart.addChild(Transform_1stConnector);

        // Druga część
        _2ndPart = new Cylinder(0.19f, 1.0f, wyglad);// stworzenie części
        _2ndPartBegine = new Cylinder(0.24f, 0.1f, bl_app);//stworzenie pierścienia nakładki
        Transform3D Transform3d_2ndPartBegine;////transformy przemieszczeń 2 stopnia swobody
        TransformGroup Transform_2ndPartBegine;
        Transform3d_2ndPart = new Transform3D();// patrz 1 element^^
        Transform3d_2ndPartRotation = new Transform3D();
        Transform_2ndPartRotation = new TransformGroup();
        Transform_2ndPart = new TransformGroup();
        Transform3d_2ndPart.set(new Vector3f(0.0f, 0.6f, 0));// ustawienie bazowego przesunięcia 2 części
        Transform_2ndPart.setTransform(Transform3d_2ndPart);
        Transform3d_2ndPartBegine = new Transform3D();
        Transform_2ndPartBegine = new TransformGroup();
        Transform3d_2ndPartBegine.set(new Vector3f(0.0f, 0.5f, 0));// ustawienie bazowego przesunięcia pierścienia nakładkowego
        // dodanie łańcucha transformacji wszystkich elementów składających się na 2 stopień
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
        _2ndConnector = new Sphere(0.24f, 1, 20, bl_app); // stworzenie sfery będącej 2 łącznikiem
        Transform3d_2ndConnector = new Transform3D();
        Transform3d_2ndConnector.set(new Vector3f(0.0f, 0.63f, 0.0f));// ustawienie przesunięcia względem poprzedniego elementu
        Transform_2ndConnector = new TransformGroup(Transform3d_2ndConnector);
        Transform_2ndConnector.addChild(_2ndConnector);// dodsanie jako potomek poprzedniego elementu
        Transform_2ndPart.addChild(Transform_2ndConnector);

        // Trzecia część
        _3rdPart = new Cylinder(0.16f, 1.2f, wyglad);// stworzenie przegubu trzeciej części
        Transform3d_3rdPart = new Transform3D();
        Transform3d_3rdPartRotation = new Transform3D();
        Transform_3rdPartRotation = new TransformGroup();
        Transform_3rdPart = new TransformGroup();//patrz 1 element^^
        Transform3d_3rdPart.set(new Vector3f(0.0f, 0.6f, 0));// ustawienie przesunięcia względem poprzedniego elementu
        Transform_3rdPart.setTransform(Transform3d_3rdPart);
        Transform_3rdPartRotation.setTransform(Transform3d_3rdPartRotation);
        //tworzenie sumy wszystkich transformów składających się na obroty tego elementu
        Transform_3rdPart.addChild(_3rdPart);
        Transform_3rdPartRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform_3rdPart.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        //dodawanie potomków do rodziców
        Transform_3rdPartRotation.addChild(Transform_3rdPart);
        Transform_2ndConnector.addChild(Transform_3rdPartRotation);

        // trzecia nakładka
        _3rdConnector = new Sphere(0.15f, 1, 20, bl_app); //stworzenie kuli będącej łącznikiem między ramionami
        Transform3d_3rdConnector = new Transform3D();
        Transform3d_3rdConnector.set(new Vector3f(0.0f, 0.61f, 0.0f)); // przesunięcie względem poprzedniego elementu
        Transform_3rdConnector = new TransformGroup(Transform3d_3rdConnector);
        Transform_3rdConnector.addChild(_3rdConnector); //dodanie elementu do transforma i transforma do rodzica
        Transform_3rdPart.addChild(Transform_3rdConnector);

        //Czwarta część
        _4thPart = new Cylinder(0.1f, 0.5f, wyglad);// czwarta część robota
        Transform3d_4thPart = new Transform3D();
        Transform3d_4thPartRotationX = new Transform3D(); // transformy rotacji podstawowej jaki i rotacji będących
        Transform3d_4thPartRotationZ = new Transform3D();// efektem sterowania robotem
        Transform_4thPartRotationX = new TransformGroup();// przesunięcia w osi x i z
        Transform_4thPartRotationZ = new TransformGroup();// tak samo jak w pierwszym elemencie^^
        Transform_4thPart = new TransformGroup();
        Transform3d_4thPart.set(new Vector3f(0.0f, 0.25f, 0));// przesunięcie bazowe
        Transform_4thPart.setTransform(Transform3d_4thPart);
        Transform_4thPartRotationX.setTransform(Transform3d_4thPartRotationX);// przerobienie t3d na tg
        Transform_4thPartRotationZ.setTransform(Transform3d_4thPartRotationX);
        Transform_4thPart.addChild(_4thPart); // dodanie elementu do sumy wszystkich przesunięć
        Transform_4thPartRotationX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform_4thPartRotationZ.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform_4thPart.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform_4thPartRotationZ.addChild(Transform_4thPartRotationX);
        Transform_4thPartRotationX.addChild(Transform_4thPart);
        Transform_3rdConnector.addChild(Transform_4thPartRotationZ);

        // Chwytak
        Grip = new Box(0.2f, 0.01f, 0.08f, bl_app);// nieruchoma część chwytaka 
        Transform3dGrip = new Transform3D();
        Transform3dGripRotation = new Transform3D();
        TransformGripRotation = new TransformGroup();
        TransformGrip = new TransformGroup();
        Transform3dGrip.set(new Vector3f(0.0f, 0.5f, 0)); // przesunięcie względem poprzedającego elemetu w tym przypadku transformgrupa
        TransformGrip.setTransform(Transform3dGrip);
        TransformGripRotation.setTransform(Transform3dGripRotation);/// patrz 1 element
        TransformGrip.addChild(Grip);
        TransformGripRotation.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformGrip.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformGripRotation.addChild(TransformGrip);// dodanie do poprzedającego transforma
        Transform_4thPartRotationX.addChild(TransformGripRotation);

        // Palce
        Holder1 = new Box(0.01f, 0.1f, 0.08f, bl_app);
        Holder2 = new Box(0.01f, 0.1f, 0.08f, bl_app);// stworzenie palców robota 
        // Tworzenie transformó ruchów oraz pozycji bazowych wszystkich palców
        Transform3dHolder1 = new Transform3D();
        Transform3dHolder2 = new Transform3D();

        Transform3dHolder1Move = new Transform3D();
        Transform3dHolder2Move = new Transform3D();

        TransformHolder1 = new TransformGroup();
        TransformHolder2 = new TransformGroup();

        TransformHolder1Move = new TransformGroup();
        TransformHolder2Move = new TransformGroup();

        Transform3dHolder1.set(new Vector3f(0.17f, 0.1f, 0));// bazowe przesunięcie palców 
        Transform3dHolder2.set(new Vector3f(-0.17f, 0.1f, 0));
        TransformHolder1.setTransform(Transform3dHolder1);  //ustawienie Tg
        TransformHolder2.setTransform(Transform3dHolder2);
        TransformHolder1Move.setTransform(Transform3dHolder1Move);
        TransformHolder2Move.setTransform(Transform3dHolder2Move);
        TransformHolder1.addChild(Holder1);// ustawienie dziedziczenia
        TransformHolder2.addChild(Holder2);
        TransformHolder1Move.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformHolder2Move.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformHolder1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformHolder2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        TransformHolder1Move.addChild(TransformHolder1);// ustawienie dziedziczenia
        TransformHolder2Move.addChild(TransformHolder2);
        TransformGrip.addChild(TransformHolder1Move);
        TransformGrip.addChild(TransformHolder2Move);

        // Końcówka
        End = new Sphere(0.01f, 1, 20, wyglad); // mały celownik na końcu ramienia
        Transform3dEnd = new Transform3D();
        Transform3dEnd.set(new Vector3f(0.0f, 0.2f, 0.0f));
        TransformEnd = new TransformGroup(Transform3dEnd);
        TransformEnd.addChild(End);
        Grip.addChild(TransformEnd);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        //ŚWIATŁO KIERUNKOWE
        Color3f light1Color = new Color3f(0.7f, 0.7f, 0.7f); //kolor światła
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);//kierunek światła
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        scena.addChild(light1);

        //ŚWIATŁO AMBIENTOWE
        Color3f ambientColor = new Color3f(0.7f, 0.7f, 0.7f); //kolor drugiego światła
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
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
        //ustawienie odpowiednich flag po wciścięciu przycisku
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
            klawisz_1 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            klawisz_2 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            klawisz_3 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
            klawisz_4 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
            klawisz_5 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
            klawisz_6 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
            klawisz_7 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
            klawisz_8 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
            klawisz_9 = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            klawisz_up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            klawisz_down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            klawisz_left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            klawisz_right = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //ustawienie odpowiednich flag po Puszczeniu przycisku
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD1) {
            klawisz_1 = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            klawisz_2 = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            klawisz_3 = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
            klawisz_4 = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
            klawisz_5 = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
            klawisz_6 = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD7) {
            klawisz_7 = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
            klawisz_8 = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
            klawisz_9 = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            klawisz_up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            klawisz_down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            klawisz_left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            klawisz_right = false;
        }
    }

    public void ResetArm() {          //zerowanie pozycji ramienia
        Transform3dBall.set(new Vector3f(1.5f, 0.10f, 1.5f));// zerowanie położenia piłeczki
        TransformBall.setTransform(Transform3dBall);
        Rotation1 = 1;// zerowanie rotacji
        Rotation2 = 1;
        Rotation3 = 1;
        Rotation4 = 1;
        Rotation5 = 1;
        Move6 = 0.3f;
        GripLocked = false;// zerowanie flag
        CollisionDetected = false;
        BallGrabbed = 0;
        BallFall = 0;
        steps_counter = 0;
        steps_count = 0;
        playing = false;

    }

    @Override
    public void actionPerformed(ActionEvent e) {     //wykonuję się z każdym sygnałem timera
        this.Collision();    //wykrycie kolizji
        this.GrabBall();      //chwycenie piłeczki
        this.MoveBall();        //przesunięcie piłeczki
        // wpisywanie pauz
        if (Move6 < 0.3) {
            GripLocked = true;
        } else {
            GripLocked = false;
        }

        // ustawienie robota w pozycji początkowej
        if (e.getSource() == pozPoczatkowa) {
            ResetArm();
        }

        if (e.getSource() == naukaPocz) {
            ResetArm();
            recording = true;
            steps = new int[100000];
            steps[0] = 0;
        }

        if (e.getSource() == naukaKon) {
            recording = false;
        }

        if (e.getSource() == odtwarzaj) {
            ResetArm();
            if (playing) {
                System.out.print("Nagrywanie jest aktywne");
            } else {
                playing = true;
                steps_count = steps_counter;
                steps_counter = 1;
            }
        }

        if (playing || (steps_counter < steps_count)) {
            if (steps[steps_counter] == 0) {
                Rotation1 = Rotation1;
            }
            if (steps[steps_counter] == 1) {
                Rotation1 = Rotation1 - 0.03;
            }
            if (steps[steps_counter] == 2) {
                Rotation1 = Rotation1 + 0.03;
            }
            if (steps[steps_counter] == 3) {
                Rotation2 = Rotation2 - 0.03;
            }
            if (steps[steps_counter] == 4) {
                Rotation2 = Rotation2 + 0.03;
            }
            if (steps[steps_counter] == 5) {
                Rotation3 = Rotation3 - 0.03;
            }
            if (steps[steps_counter] == 6) {
                Rotation3 = Rotation3 + 0.03;
            }
            if (steps[steps_counter] == 7) {
                Rotation4 = Rotation4 - 0.03;
            }
            if (steps[steps_counter] == 8) {
                Rotation4 = Rotation4 + 0.03;
            }
            if (steps[steps_counter] == 9) {
                Rotation5 = Rotation5 - 0.03;
            }
            if (steps[steps_counter] == 10) {
                Rotation5 = Rotation5 + 0.03;
            }
            if (steps[steps_counter] == 11) {
                Move6 = Move6 - 0.03f;
            }
            if (steps[steps_counter] == 12) {
                Move6 = Move6 + 0.03f;
            }
            steps_counter++;
        }

//wykonanie ruchów w zależności od wciśniętych przycisków 
        // 1 stopień
        if (Rotation1 > -7.65) {//ograniczenie rotacji
            if (klawisz_9 == true) {
                Rotation1 = Rotation1 - 0.03;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 1;
                    waiting = true;
                }
            }
        }
        if (Rotation1 < 2.65) {// ograniczenie ruchu
            if (klawisz_7 == true) {
                Rotation1 = Rotation1 + 0.03;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 2;
                    waiting = true;
                }
            }
        }
        // 2 Stopień
        if (Rotation2 > 0.2) {// sprawdzenie zakresu
            if (klawisz_4 == true) {
                Rotation2 = Rotation2 - 0.03;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 3;
                    waiting = true;
                }
            }
        }
        if (Rotation2 < 1.6) {// sprawdzenie zakresu
            if (klawisz_1 == true) {
                Rotation2 = Rotation2 + 0.03;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 4;
                    waiting = true;
                }
            }
        }
        // 3 Stopień
        if (Rotation3 > -1.0) {// sprawdzenie zakresu
            if (klawisz_5 == true) {
                Rotation3 = Rotation3 - 0.03;//przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 5;
                    waiting = true;
                }
            }
        }
        if (Rotation3 < 1.7) {// sprawdzenie zakresu
            if (klawisz_2 == true) {
                Rotation3 = Rotation3 + 0.03;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 6;
                    waiting = true;
                }
            }
        }
        // 4 Stopień
        if (Rotation4 > -1) {// sprawdzenie zakresu
            if (klawisz_up == true) {
                Rotation4 = Rotation4 - 0.03;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 7;
                    waiting = true;
                }
            }
        }
        if (Rotation4 < 1) {// sprawdzenie zakresu
            if (klawisz_down == true) {
                Rotation4 = Rotation4 + 0.03;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 8;
                    waiting = true;
                }
            }
        }
        // 5Stopień
        if (Rotation5 > -1) {// sprawdzenie zakresu
            if (klawisz_left == true) {
                Rotation5 = Rotation5 - 0.03;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 9;
                    waiting = true;
                }
            }
        }
        if (Rotation5 < 1) {// sprawdzenie zakresu 
            if (klawisz_right == true) {
                Rotation5 = Rotation5 + 0.03;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 10;
                    waiting = true;
                }
            }
        }
        // Chwytak
        if (Move6 > 0.28) {// sprawdzenie zaktesu
            if (klawisz_6 == true) {
                Move6 = Move6 - 0.03f;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 11;
                    waiting = true;
                }
            }
        }
        if (Move6 < 0.32) {// sprawdzenie zakresu
            if (klawisz_3 == true) {
                Move6 = Move6 + 0.03f;// przesunięcie
                if (recording) {
                    steps_counter++;
                    steps[steps_counter] = 12;
                    waiting = true;
                }
            }
        }
        if (recording && !waiting) {
            steps[steps_counter] = 0;
            steps_counter++;
        }

        waiting = false;
        //przesunięcie odpowiednich elementów na ekranie
        Transform3d_1stPartRotation.rotY(Rotation1); // ustawienie rotacji 1 stopnia swobody
        Transform_1stPartRotation.setTransform(Transform3d_1stPartRotation);// ustawienie transforma

        Transform3d_2ndPartRotation.rotX(Rotation2); // ustawienie rotacji 2 stopnia swobody
        Transform_2ndPartRotation.setTransform(Transform3d_2ndPartRotation);

        Transform3d_3rdPartRotation.rotX(Rotation3); // ustawienie rotacji 3 stopnia swobody
        Transform_3rdPartRotation.setTransform(Transform3d_3rdPartRotation);

        Transform3d_4thPartRotationX.rotX(Rotation4); // ustawienie rotacji 4 stopnia swobody
        Transform3d_4thPartRotationZ.rotZ(Rotation5); // ustawienie rotacji 5 stopnia swobody
        Transform_4thPartRotationX.setTransform(Transform3d_4thPartRotationX);
        Transform_4thPartRotationZ.setTransform(Transform3d_4thPartRotationZ);

        Transform3dHolder1Move.set(new Vector3f(-Move6, 0, 0));// otwieranie/ zamykanie palca
        TransformHolder1Move.setTransform(Transform3dHolder1Move);
        Transform3dHolder2Move.set(new Vector3f(Move6, 0, 0));// otwieranie/ zamykanie palca
        TransformHolder2Move.setTransform(Transform3dHolder2Move);

    }

    private void Collision() {            //wykrycie kolizji 
        Transform3D Transform3dTemp;// tworzenie tymczasowego transforma
        Transform3dTemp = new Transform3D();
        End.getLocalToVworld(Transform3dTemp);// pobranie pozycji końcówki
        Transform3dTemp.get(GripPosition);// ustawienie tymczasowego transforma na końcówce manipulatora
        Transform3D Transform3dTemp2 = new Transform3D(); //pobranie z obiektu informacji o pozycji chwytaka 
        Ball.getLocalToVworld(Transform3dTemp2);// pobranie pozycji piłeczki
        Transform3dTemp2.get(BallPosition); //ustawienie drugiego tymczasowego transforma w miejscu piłeczki
        if (GripPosition.y < 0.01) //w przypadku kolizji z położem wymuszone jest podniesienie chwytaka do pozycji powyżej ziemi
        {
            CollisionDetected = true;// ustawienie stanu kolizji
            klawisz_up = false;// wymuszenie puszczenia przycisku
            klawisz_down = false;
            klawisz_left = false;
            klawisz_right = false;

            if (klawisz_1) {
                klawisz_4 = true;// podnoszenie chwytaka
                klawisz_1 = false;
            }
            if (klawisz_2) {
                klawisz_5 = true;// podnoszenie chwytaka
                klawisz_2 = false;
            }
        }
        if (CollisionDetected && GripPosition.y > 0.03) {// zniesienie rozkazu podniesienia ramienia z powodu wykonanej kolizji
            klawisz_1 = false;
            klawisz_4 = false;
            klawisz_5 = false;
            klawisz_2 = false;
            CollisionDetected = false;

        }

    }

    private void GrabBall() {// funkcja łapania piłeczki
        if (GripLocked && BallGrabbed == 0) {// piłeczka kiść musi być zamknięta 
            //liczenie odległości
            float BallGripDistance;
            //w przypadku gdy chwytak jest zaciśnięty i odległość 
            //między chwytakiem i piłeczką jest dostatecznie mała następuję przyłączenie piłeczki do chwytaka
            //w celu ułatwienia sterowania aby złapać piłeczkę wystarczy przejechać koło niej mając zamknięty chwytak
            Point3f _1point = new Point3f(BallPosition.getX(), BallPosition.getY(), BallPosition.getZ());
            Point3f _2point = new Point3f(GripPosition.getX(), GripPosition.getY(), GripPosition.getZ());
            BallGripDistance = _1point.distance(_2point);
            if (BallGripDistance < 0.1f) {// jeżeli dystans jest mniejszy- łap
                BallGrabbed = 1;
            }

        }
        if (!GripLocked) {// jeżeli dystans nie jest dostateczny 
            BallGrabbed = 0;
        }
    }

    private void MoveBall()//przesunięcie piłeczki gdy ta podłączona jest do chwytaka lub spada
    {
        if (BallGrabbed == 1) {
            GripToObjectVevtor = new Vector3f(BallPosition);// podłączenie piłeczki do końcówki 
            GripToObjectVevtor.sub(GripPosition);
            BallGrabbed = 2;
        }
        if (BallGrabbed == 2) {
            Vector3f temp = new Vector3f();// piłeczka podłączona, wykonuj ruchy razem z końcówką manipulatora
            temp.add(GripPosition);
            Transform3dBall.set(temp);
            TransformBall.setTransform(Transform3dBall);
        }

        if (BallGrabbed == 0) {// jeżeli piłeczka nie jest złapana

            if (BallPosition.y > 0.1f) {// jeżeli nie leży na ziemi piłeczka spada
                BallFall++;
                Transform3dBall.set(new Vector3f(BallPosition.x, BallPosition.y - 0.0003f * BallFall * BallFall, BallPosition.z));// fizyka swobodnego spadku
                TransformBall.setTransform(Transform3dBall);
            } else {
                BallFall = 0;// jeżeli jest na ziemi lub pod nią- ustaw ją na ziemi i zostaw
                Transform3dBall.set(new Vector3f(BallPosition.x, 0.1f, BallPosition.z));
                TransformBall.setTransform(Transform3dBall);
            }

        }
    }
}
