
/*
* Copyright (C) 1997-2016 European Space Agency
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/






package hipparcos.hipi;

import hipparcos.plot.*;
import hipparcos.tools.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;


/* Interactive plotter for HIPI data */
public class PlotHipi extends Applet implements ActionListener{
   private HIPI hipi=new HIPI();
   private PlotFit theCurve;
   private Label info1,info2;
   private TextField hipfield;
   private String dataDir;
   private double step=(double)1;
   private boolean started=false;
   private Button getStats;
   private Button getTimeStats;
   private CardLayout cards = new CardLayout();
   private boolean stated=false;
   private Histogram residualn;
   private Histogram residualf;
   private Histogram stdern,stderf;
   private Histogram normErrn,normErrf;
   private SimplePlot timeNormn,timeNormf;
   private PhasePlot phaseNormn,phaseNormf;
   private Panel mainPanel;
   //private PhaseField phase;
   private PhasePanel phase;

  public void init () {
	// Set the browser in the tools
    Browser.setAppletContext(this.getAppletContext());

    setLayout (new BorderLayout());
    mainPanel=new Panel();
    mainPanel.setLayout(cards);
    add("Center",mainPanel);

    Panel stats=new Panel();
    stats.setLayout(new GridLayout(2,3));
    residualf = new Histogram("Residual (mas)","FAST",-10,10,1,Color.blue);
    stats.add(residualf);
    stderf = new Histogram("Standard Error (mas) [bin 0.5]","FAST",0,10,(0.5),Color.blue);
    stats.add(stderf);
    normErrf = new Histogram("Normalised Error [bin 0.5]","FAST",-5,5,(0.5),Color.blue);
    stats.add(normErrf);
    residualn = new Histogram("Residual (mas)","NDAC",-10,10,1,Color.red);
    stats.add(residualn);
    stdern = new Histogram("Standard Error (mas) [bin 0.5]","NDAC",0,10,(0.5),Color.red);
    stats.add(stdern);
    normErrn = new Histogram("Normalised Error [bin 0.5]","NDAC",-5,5,(0.5),Color.red);
    stats.add(normErrn);
    mainPanel.add("stats",stats);

    Panel timestats = new Panel();
    timestats.setLayout(new GridLayout(2,2));
    timeNormf = new SimplePlot("Normalised Error (mas) over time","FAST",1989.75,1993.25,-5,5,Color.blue);
    timestats.add(timeNormf);
    phaseNormf = new PhasePlot("Normalised Error (mas) Folded","FAST",-5,5,Color.blue);
    timestats.add(phaseNormf);
    timeNormn = new SimplePlot("Normalised Error (mas) over time","NDAC",1989.75,1993.25,-5,5,Color.red);
    timestats.add(timeNormn);
    phaseNormn = new PhasePlot("Normalised Error (mas) Folded","NDAC",-5,5,Color.red);
    timestats.add(phaseNormn);

    Panel stats2 = new Panel();
    stats2.setLayout(new BorderLayout());
    stats2.add("Center",timestats);
    phase = new PhasePanel ("Enter required folding period (years):",
				"0.5000", this);
    stats2.add("South",phase);
    
    mainPanel.add("timestats",stats2);

    // Information
    Panel info = new Panel ();
    info.setLayout(new GridLayout(2,1));
    info1 = new Label("No Data ........");
    info2 = new Label("........");
    info.add("1",info1);
    info.add("2",info2);
    add("North",info);

    // Plotting area
    Panel curvePanel = new Panel();
    curvePanel.setLayout(new BorderLayout());
    theCurve = new PlotFit();
    curvePanel.add("Center",theCurve);
    curvePanel.add ("East",new Legend(theCurve.baryColour));

    mainPanel.add("curve",curvePanel);
    //mainPanel.add("curve",theCurve);
    cards.show(mainPanel,"curve");

    // controls
    Panel p = new Panel ();
    Label t2 = new Label ("HIP");
    p.add ("West",t2);
    hipfield = new TextField(7);
    hipfield.addActionListener(this);
    p.add ("East",hipfield);

    Button upButton=new Button("prev");
    p.add ("West",upButton);
    Button downButton=new Button("next");
    p.add ("East",downButton);

    Checkbox FAST = new Checkbox("FAST",null,true);
    theCurve.showFAST();
    p.add ("East",FAST);
    Checkbox NDAC = new Checkbox("NDAC",null,false);
    theCurve.hideNDAC();
    p.add ("East",NDAC);

    Button getInfo=new Button("Get ASCII Data");
    p.add ("East",getInfo);

    getStats=new Button("Statistics 1");
    p.add ("East",getStats);

    getTimeStats=new Button("Statistics 2");
    p.add ("East",getTimeStats);

    add ("South",p);

    //add ("East",new Legend(theCurve.baryColour));
    try {
       int  id = checkForId();
       hipi=  HIPIFactory.get(id); 
       newhipi();
    } catch (Exception e) {};

  };

   protected int checkForId() throws Exception {
	String idString = getParameter("id");
	return (Integer.parseInt(idString.trim()));
   }

    public boolean action(Event evt, Object obj) {
	switch (evt.id) {
	   case Event.ACTION_EVENT: {
	      if (evt.target instanceof Checkbox) {
		Checkbox theButton= (Checkbox)evt.target;
                String label= theButton.getLabel();
		boolean state= theButton.getState();
		if (label=="FAST") {
		   if (state) theCurve.showFAST(); else theCurve.hideFAST();
		}
		if (label=="NDAC") {
		   if (state) theCurve.showNDAC(); else theCurve.hideNDAC();
		}

	      }
	      if (evt.target instanceof TextField) {
	        Integer hipid= Integer.valueOf((String) evt.arg);
		 	try {
		    	hipi= HIPIFactory.get(hipid.intValue()); 
		    	newhipi();
		 	} catch (Exception e) {
		   	 	info1.setText("Bad Hipnumber "+hipid.toString());
		 	}
	      }
	      if (evt.target instanceof Button) {
		 Button theButton= (Button)evt.target;
		 String label= theButton.getLabel();
		 int hipid=hipi.getHipNumber();
		 boolean found =false;
		 if (label=="Statistics 2") {
		    if (hipi.getHipNumber()==0) {
			info1.setText("Enter a HIP number first");
		    } else {
			theCurve.stop();
			cards.show(mainPanel,"timestats") ;
			provideStats();
			getStats.setLabel("Statistics 1");
			getTimeStats.setLabel("Show Curve");
		    }
	         }
		 if (label=="Show Curve") {
		    if (hipi.getHipNumber()==0) {
			info1.setText("Enter a HIP number first");
		    } else {
			cards.show(mainPanel,"curve") ;
			theCurve.start();
			getStats.setLabel("Statistics 1");
			getTimeStats.setLabel("Statistics 2");
		    }
	         }
		 if (label=="Statistics 1") {
		    if (hipi.getHipNumber()==0) {
			info1.setText("Enter a HIP number first");
		    } else {
			theCurve.stop();
			cards.show(mainPanel,"stats") ;
			provideStats();
			getStats.setLabel("Show Curve");
			getTimeStats.setLabel("Statistics 2");
		    }
		 }
		 if (label=="Get ASCII Data") {
		    if (hipi.getHipNumber()==0) {
				info1.setText("Enter a HIP number first");
		    } else {
				Browser.goTo(makeUrl());
		    }
		 }
		 if (label=="next") {
		    if (hipid>=120415) { hipid=0; };
		    if (hipid==120404) {hipid=120414;};
		    if (hipid==118322) {hipid=120000;};
		    if (hipid==120006) {hipid=120026;};
		    if (hipid==120027) {hipid=120045;};
		    if (hipid==120047) {hipid=120070;};
		    if (hipid==120082) {hipid=120120;};
		    while (!found) {
				hipid++;
				try {
					hipi= HIPIFactory.get(hipid);
					found = (hipi.getHipNumber() > 0);
				} catch (Exception nf) {};
		    }
		    newhipi();
		 }
		 if (label=="prev") {
		    if (hipid<=1) { hipid=120416; };
		    if (hipid==120415) {hipid=120405;};
		    if (hipid==120000) {hipid=118321;};
		    if (hipid==120026) {hipid=120007;};
		    if (hipid==120046) {hipid=120027;};
		    if (hipid==120071) {hipid=120047;};
		    if (hipid==120121) {hipid=120083;};
		    while (!found) {
				hipid--;
				try {
					hipi= HIPIFactory.get(hipid);
					found = (hipi.getHipNumber() > 0);
				} catch (Exception nf) {};
		    }
		    newhipi();
		 }
	      }
	   }
	}
        return true;
    } 

    private void provideStats(){
	if (!stated) {
	residualn.resetGraph();	
	stdern.resetGraph();	
	normErrn.resetGraph();	
	residualf.resetGraph();	
	stderf.resetGraph();	
	normErrf.resetGraph();	
	timeNormf.resetGraph();	
	timeNormn.resetGraph();	
	phaseNormf.resetGraph();	
	phaseNormn.resetGraph();	
       for (Enumeration e = hipi.abscissae.elements() ; e.hasMoreElements() ;) {
          Abcissa a = (Abcissa) e.nextElement();
	  double normErr= (a.a8/a.a9);
	  DPoint p = new DPoint((Constants.t0+a.tobs),normErr); // point for time order
	  //System.out.println ("a.tobs is "+a.tobs+" "+p);
	  if (a.consortia.startsWith("N")){
	     residualn.addOccurence(a.a8);
	     stdern.addOccurence(a.a9);
	     normErrn.addOccurence(normErr);
	     timeNormn.addPoint(p);
	     phaseNormn.addPoint(p);
	  }
	  if (a.consortia.startsWith("F")) {
	     residualf.addOccurence(a.a8);
	     stderf.addOccurence(a.a9);
	     normErrf.addOccurence(normErr);
	     phaseNormf.addPoint(p);
	     timeNormf.addPoint(p);
	  }
       };
       stated=true;
       }
    }
    private void newhipi() {
	Integer n= new Integer(hipi.getHipNumber());
	hipfield.setText(n.toString());
	info1.setText(hipi.getInfoText1());
	info2.setText(hipi.getInfoText2());
	cards.show(mainPanel,"curve");
	getStats.setLabel("Statistics 1");
	getTimeStats.setLabel("Statistics 2");
        theCurve.setHipi(hipi);
	stated=false;
    }
    public String getAppletInfo() {
        return "Abcissae fit  by  Wil , Lennart Lindegren & Michael Perrymann";
    }

  public URL  makeUrl () {

      try{
        String theUrl=HIPproperties.getProperty("hipurl");
        theUrl+="?hipiId="+hipi.getHipNumber();
		if (Constants.verbose > 3) System.out.println(theUrl);
        return new URL(theUrl);
      } catch (Exception e) {
        System.err.println(e);
      }
        return null;
   }

	public double getPhase() {
	   return (phaseNormn.getPhase());
	}

	public void setPhase(double phase) {
	   phaseNormn.setPhase(phase);
	   phaseNormf.setPhase(phase);
  
	}

	static public void main( String argv[]) {
        // UmSet the browser in the tools
        Browser.setMainFrame();
		new hipparcos.tools.MainFrame( new PlotHipi(), argv, 730, 570);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
	      if (evt.getSource() instanceof TextField) {
		        Integer hipid= Integer.valueOf(hipfield.getText());
			 	try {
			    	hipi= HIPIFactory.get(hipid.intValue()); 
			    	newhipi();
			 	} catch (Exception e) {
			   	 	info1.setText("Bad Hipnumber "+hipid.toString());
			 	}
		      }
		
	}
}
