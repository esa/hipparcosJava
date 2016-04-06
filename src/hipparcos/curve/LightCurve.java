package hipparcos.curve;

import java.applet.*;
import java.net.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import hipparcos.tools.*;

public class LightCurve extends Applet implements ActionListener {
   private PlotCurve theCurve;
   private HIP_EP ep;
   private Label info1,info2;
   private TextField periodField;
   private String dataDir;
   private double step=(double)1;
   private TextField hipfield;
   private int pwidth=10;
   private String fname=null;

   public static void main(String argv[]) {
      LightCurve ap = null;
      if (argv.length > 0) {
        ap = new LightCurve(argv[0]);
      } else {
        ap = new LightCurve();
      }
	  Browser.setMainFrame();
	  new MainFrame(ap,null,800,450);
  }
  public LightCurve() {  //super("Hipparcos Light Curve"); 
		}
  public LightCurve(String fname) {
	//super("Hipparcos Light Curve");
	this.fname=fname;
	}

   public Dimension preferredSize() {
        return new Dimension(800,400);
   }

  public void init () {
	// Set the browser in the tools
    Browser.setAppletContext(this.getAppletContext());

    setLayout (new BorderLayout());

    // Information
    Panel info = new Panel ();
    info.setLayout(new GridLayout(2,1));
    info1 = new Label("No Data ........");
    info2 = new Label("........");
    info.add("1",info1);
    info.add("2",info2);
    add("North",info);

    // Plotting area
    theCurve = new PlotCurve();

    add ("Center",theCurve);

    // controls
    Panel p = new Panel ();
    Label hf = new Label("HIP");

    hipfield = new TextField(7);
    hipfield.addActionListener(this);;
//    hipfield.addTextListener(this);
    System.out.println("Setting up");
       // get Data 
       if (fname !=null) { // take file name if supplied
		  try {
          ep = HIPEPFactory.get(fname);
		  } catch (Exception e) {
				System.err.println("Could not load "+fname );
				e.printStackTrace();
				System.exit(1);
		  }
        } else { // no param specified so add a field to input 
    	  p.add ("West",hf);
    	  p.add ("West",hipfield);
    	  Button hipUp=new Button("prev");
    	  p.add ("West",hipUp);
    	  Button hipDown=new Button("next");
    	  p.add ("West",hipDown);
		  try {
		  	ep = HIPEPFactory.get(1);
	      } catch (Exception e) {};
	};

    Label t2 = new Label ("Trial Period (days):");
    p.add ("West",t2);
    periodField = new TextField(pwidth);
    
  //  periodField.addTextListener(this);
    p.add ("East",periodField);

    Button upButton=new Button("inc");
    p.add ("East",upButton);
    Button downButton=new Button("dec");
    p.add ("East",downButton);

    Label t3 = new Label ("Step:");
    p.add ("East",t3);
    Choice choice = new Choice();
    choice.addItem("10.0");
    choice.addItem(" 1.0");
    choice.addItem(" 0.1");
    choice.addItem(" 0.01");
    choice.addItem(" 0.001");
    choice.addItem(" 0.0001");
    choice.addItem(" 0.00001");
    choice.addItem(" 0.000001");
    p.add ("East",choice);
    choice.select(" 1.0");
    add ("South",p);
    newHipep();
  };

   private void newHipep() {
          info1.setText(ep.getInfoText1());
          info2.setText(ep.getInfoText2());
          theCurve.setEp(ep);
          periodField.setText(new Double(theCurve.getPeriod()).toString());
	  hipfield.setText (" "+ep.getHipNumber());
   }

    public boolean action(Event evt, Object obj) {

	switch (evt.id) {
	   case Event.ACTION_EVENT: {
	      if (evt.target instanceof TextField) {
		 if (evt.target == hipfield) {
		    Integer hipid= Integer.valueOf(((String) evt.arg).trim());
				try {
                    ep= HIPEPFactory.get(hipid.intValue()); 
	      		} catch (Exception e) {
                       info1.setText("Bad Hipnumber "+hipid.toString());
                } 

                       newHipep();
		 } else {
	            Double period= Double.valueOf((String) evt.arg);
	            theCurve.setPeriod(period.doubleValue());
		 }
	      }
	      if (evt.target instanceof Choice) {
	         Double val= Double.valueOf((String) evt.arg);
		 step=val.doubleValue();
		 //System.out.println("New Step : "+val.toString());
	      }
	      if (evt.target instanceof Button) {
		 Button theButton= (Button)evt.target;
		 String label= theButton.getLabel();
	         double presper= theCurve.getPeriod();
	         Double period= new Double (0);
		 int hipid=ep.getHipNumber();
		 boolean found =false;
	
                 if (label=="Quit") {
		    System.exit(0);
		 }
                 if (label=="next") {
                    if (hipid>=120415) { hipid=0; };
                    if (hipid==120404) {hipid=120414;};
                    if (hipid==120313) {hipid=120400;};
                    if (hipid==120082) {hipid=120120;};
                    if (hipid==120047) {hipid=120070;};
                    if (hipid==120027) {hipid=120045;};
                    if (hipid==120006) {hipid=120026;};
                    if (hipid==118322) {hipid=120000;};
                    while (!found) {
                        hipid++;
						try {	
                        	ep= HIPEPFactory.get(hipid);
                        	if (ep != null) found = (ep.getHipNumber() > 0);
						} catch (Exception nf) { }
                    }
                    newHipep();
                 }
                 if (label=="prev") {
                    if (hipid<=1) { hipid=120416; };
                    if (hipid==120000) {hipid=118321;};
                    if (hipid==120026) {hipid=120007;};
                    if (hipid==120046) {hipid=120027;};
                    if (hipid==120071) {hipid=120047;};
                    if (hipid==120121) {hipid=120083;};
                    if (hipid==120401) {hipid=120314;};
                    if (hipid==120415) {hipid=120405;};
                    while (!found) {
                        hipid--;
						try {	
                        	if (ep != null) ep=HIPEPFactory.get(hipid);
                        	 if (ep != null) found = (ep.getHipNumber() > 0);
						} catch (Exception nf) { }
                    }
                    newHipep();
                 }
		 if (label=="inc") {
			period= new Double(presper+step+0.0000001);
              		String tmpS= truncate(period,6);
			periodField.setText(tmpS);
			theCurve.setPeriod(new Double(tmpS).doubleValue());
		 }
		 if (label=="dec") {
			period= new Double(presper-step+0.0000001);
              		String tmpS= truncate(period,6);
			periodField.setText(tmpS);
			theCurve.setPeriod(new Double(tmpS).doubleValue());
		 }
		 
	      }
	   }
	}
        return true;
    } 
    public String getAppletInfo() {
        return "Light Curve by wil";
    }
   public String truncate(Double period,int ad) {
       	String tmpS= period.toString();
	int dot = tmpS.indexOf('.')+1;
	int end = tmpS.length();
	if ((dot+ad) < end) {
	   end = dot+ad;
    	}
	return  tmpS.substring(0,end);
  }

@Override
public void actionPerformed(ActionEvent evt) {
	
    if (evt.getSource() instanceof TextField) {
		 if (evt.getSource() == hipfield) {
		    Integer hipid= Integer.valueOf(hipfield.getText().trim());
				try {
                   ep= HIPEPFactory.get(hipid.intValue()); 
	      		} catch (Exception e) {
                      info1.setText("Bad Hipnumber "+hipid.toString());
               } 

                      newHipep();
		 } else {
	            Double period= Double.valueOf( periodField.getText());
	            theCurve.setPeriod(period.doubleValue());
		 }
	 }
	    
	   
}

}
