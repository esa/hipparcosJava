// Written by William O'Mullane for the 
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.sky;
import hipparcos.tools.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.net.*;
import java.util.Properties;

/** Show are of the sky and allow animation and interaction 
*/

public class ShowSky extends Applet implements ActionListener, TextListener , ItemListener {
  public SkyArea sky;
  public boolean stoped=true;
  public Label info1,info2,infoRA,infoDEC,infoHMS,infoDMS;
  public Label width,threshold;
  public Panel info,p2,controlp;
  public TextField raField,decField;
  public TextField thresField;
  public Choice choice;
  public String entry="Enter RA Dec and field width in Degrees";
  public MagScale ms ;
  public Checkbox tyc; 
  public StarMover theMover=null;
  public Label info3,infoYear;
  public boolean started=false;
  public String dataDir;
  public Checkbox tails,animate;
  public int factor=2,tol=4;
  public Choice speed;
  public StarList lister;
  protected String[] args=null;

   public ShowSky() {
	   //super("ESA - Hipparcos Sky Viewer");
	}
   
   public ShowSky(String args[]) {
	   this.args=args;
	   //super("ESA - Hipparcos Sky Viewer");
	}
   public static void main(String argv[]) {
	 
      ShowSky ap = new ShowSky();
	  new MainFrame(ap,null,990,800);
  }

  /** Window manager sets the size of the window officially but we can 
        suggest which size we would like */
  public Dimension getPreferredSize() {
        return new Dimension(700,650);
  }

 public void init() {
    setLayout (new BorderLayout());     
    info2=new Label(entry);
    sky=new SkyArea();  
    ms = new MagScale();
    info = new Panel();
    info.setLayout(new BorderLayout());
    Panel scale= new Panel();
    Label scaleText= new Label("Scale:");
    scaleText.setText("Scale:");
    scale.add(scaleText);
    scale.add(ms);

    // controls
    controlp = new Panel ();
    Label t2 = new Label ("RA:");
    controlp.add ("West",t2);
    raField = new TextField("00000000");
    if (args!=null) {
    	raField.setText(args[0]);
    }
    controlp.add ("East",raField);
    Label t3 = new Label ("Dec:");
    controlp.add ("East",t3);
    decField = new TextField("00000");
    if (args!=null) {
    	decField.setText(args[1]);
    }

    controlp.add ("East",decField);

    width = new Label ("Width:");
    controlp.add ("East",width);
    choice = new Choice();
    choice.addItem("1");
    choice.addItem("2");
    choice.addItem("3");
    choice.addItem("4");
    choice.addItem("5");
    choice.addItem("6");
    choice.addItem("8");
    choice.addItem("10");
    choice.addItem("15");
    choice.addItem("20");
    choice.addItem("25");
    choice.addItem("30");
    choice.addItem("35");
    controlp.add ("East",choice);
    choice.select("6");
	choice.addItemListener(this);
	Mag.setFactor(3);

    threshold = new Label ("V(lim):");
    controlp.add ("East",threshold);

    thresField = new TextField("99");
    controlp.add ("East",thresField);
	thresField.addTextListener(this);

    Button go = new Button("View");
    controlp.add ("East",go);
	go.addActionListener(this);

    Button list = new Button("List");
	list.addActionListener(this);
    controlp.add ("East",list);

    Button get = new Button("Get Info");
    get.addActionListener(this);
    tyc = new Checkbox("View TYC",null,true);
	tyc.addItemListener(this);
	

	tails= new Checkbox("Tails",null,true);
	tails.addItemListener(this);
    sky.setTails();
    infoYear = new Label("1991.0 (ICRS)");
	animate = new Checkbox("Animate",null,false);
	animate.addItemListener(this);
        Panel yearp = new Panel();
        yearp.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        Label day =new Label("Year:");
        yearp.add(day);
        yearp.add(infoYear);

	theMover= new StarMover(sky,2000);
    theMover.setInfo(infoYear);
    theMover.setDelay(490);


	Button reset = new Button("Reset");
	reset.addActionListener(this);
        speed = new Choice();
        speed.addItem("10");
        speed.addItem("100");
        speed.addItem("1000");
        speed.addItem("10000");
        speed.select("100");
		speed.addItemListener(this);
        Star2D.setYearStep(100);
	Label sp= new Label("Step:");
	Panel spPan = new Panel();
	spPan.add(sp);
	spPan.add(speed);

	
    p2= new Panel();
    p2.setLayout(new GridLayout(17,1));
    Label infoRed=new Label("Red=HIP only");
    Label infoWhite=new Label("White=TYC only");
    Label infoBlue=new Label("Blue=HIP+TYC");
    Label alpha=new Label("Alpha:");
    Label delta=new Label("Delta:");
    Label HMS=new Label("RA  :");
    Label DMS=new Label("Dec:");
    Label JD=new Label("J1991.25 (ICRS)");
	Label anim = new Label("   Animation");
    infoRA =new Label("000.000000000");
    infoDEC=new Label(" 00.000000000");
    infoHMS=new Label("+00 00 00");
    infoDMS=new Label("+00 00 00");

    p2.add (infoRed);
    p2.add (infoWhite);
    p2.add (infoBlue);
    p2.add (get);
    p2.add (tyc);
    Label blank= new Label(" ");
    p2.add (blank);
    p2.add (blank);
    p2.add (anim);
	p2.add(tails);
    p2.add (animate);
    p2.add (reset);
	p2.add (spPan);
	p2.add(yearp);
    p2.add (blank);
    p2.add (blank);
    Panel al= new Panel();
    al.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
    al.add (alpha);
    al.add (infoRA);
    p2.add (al);
    Panel dl= new Panel();
    dl.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
    dl.add (delta);
    dl.add (infoDEC);
    p2.add (dl);
    Panel hms= new Panel();
    hms.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
    hms.add (HMS);
    hms.add (infoHMS);
    p2.add (hms);
    Panel dms= new Panel();
    dms.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
    dms.add (DMS);
    dms.add (infoDMS);
    p2.add (dms);
    sky.infoInit(infoRA,infoDEC,infoHMS,infoDMS);

    add("North",scale);
    add("East",p2);
    add("Center",sky);
    //info.add("North",info1);
    info.add("Center",controlp);
    info.add("South",info2);
    add("South",info);

	lister = new StarList(sky);

 }


  public void populate(String ras,String decs, String tols) {
    double ra;
    double dec;
    double tol;
    try {
       ra= new Double (ras).doubleValue();
       dec= new Double (decs).doubleValue();
       if (dec > 90 || dec < -90) throw (new Exception());
       tol=new Double (tols).doubleValue();
       tol= tol/2;
       populate (ra,dec,tol);
    } catch (Exception e) {
      info2.setText("Ensure RA(0-360) and Dec(<90 >-90) are valid");
      System.err.println("Ensure RA and Dec are valid :"+e);
    } ; 
  }
 
/*
 public void paint (Graphics g) {
	sky.plotAll();
 }
*/
    public String getInfo() {
        return "Sky Plot by William O'Mullane for the European Space Agency";
    }

   /** This is required to implement ActionListener - this gets
        called when someone hits a button */
    public void actionPerformed(ActionEvent e){
		 info2.setText(entry);
		 String label = e.getActionCommand();
		 if (label=="Quit") {
            System.exit(0);
         }

		 if (label=="Reset") {
             theMover.reset();
             sky.repaint();
         }

		 if (label=="List") {
			lister.setVisible(true);
			lister.refresh();
		 }
		 if (label=="View") {
		   populate (raField.getText(), decField.getText(),
					choice.getSelectedItem());
		 }// end if Button is View
		if (label=="Get Info") {
		   Star theStar=sky.getLastSelected();
		   if (theStar!=null) {
		      System.out.println("Go get "+theStar.getId());	
		      info2.setText(Browser.goTo(theStar.makeUrl()));
		   }
		   else {
		      info2.setText("Select a source first");
		   }
		}
	}

    
    public void populate (double ra, double dec, double tol) {
      try { 
	sky.init(ra,dec,tol);
	StarLoader theFactory = new StarLoader(sky,0,!tyc.getState());  
	// this does the loading of stars from hipSearch
      } catch (Exception e) {
	System.err.println("Could not access Sky data :"+e);
      } ; 
   }

    /** This must be imlpmented to satify the TextListener interface
        it gets called when the text in the box changes 
    */
    public void textValueChanged(TextEvent evt) {
        try {
           double thres= new Double (thresField.getText()).doubleValue();
           Star2D.setThreshold(thres);
           sky.replotAll(thres);
        } catch (Exception e) {
          // System.err.println("Thres Problem :"+e);
        }
    } 

   /** Item listener imp  - for check boxes */
   public void itemStateChanged(ItemEvent evt) {
		info2.setText(entry);
		ItemSelectable cb=evt.getItemSelectable();
		//ItemSelectable cb=(Checkbox)evt.getItemSelectable();
		if (cb == tyc) {
			if (evt.getStateChange() == ItemEvent.SELECTED) {
		   		sky.unSetViewHipOnly();
			} else {
		  	 	sky.setViewHipOnly();
			}
		}
	   
		if (cb == animate) {
			if (evt.getStateChange() == ItemEvent.SELECTED) {
		  	 	//sky.setViewHipOnly();
				//tyc.setState(false);
		   		theMover.start();
			} else {
		   		theMover.stop();
			}
		}

		if (cb == tails) {
			if (evt.getStateChange() == ItemEvent.SELECTED) {
		   		sky.setTails();
			} else {
		  	 	sky.unSetTails();
			}
		}

		if (cb == speed) {
			int ch=new Integer(speed.getSelectedItem()).intValue();
            Star2D.setYearStep(ch);
		}

		if (cb == choice) {
			int ch=new Integer(choice.getSelectedItem()).intValue();
		   	Mag.setFactor(2); 
			switch (ch) {
		   		case 1: {  Mag.setFactor(8); } break;
		   		case 2: {  Mag.setFactor(7); } break;
		   		case 3: {  Mag.setFactor(6); } break;
		   		case 4: {  Mag.setFactor(5); } break;
		   		case 5: {  Mag.setFactor(4); } break;
		   		case 6: {  Mag.setFactor(3); } break;
		   		case 8: {  Mag.setFactor(3); } break;
		   		case 10: {  Mag.setFactor(2); } break;
		   		case 15: {  Mag.setFactor(2); } break;
		   		case 20: {  Mag.setFactor(2); } break;
		   		case 25: {  Mag.setFactor(2); } break;
			}	
			ms.repaint();
		}
   }

}
