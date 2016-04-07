
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

import java.awt.*;
/* Control panel for adjusting phase on a PhasePlot */
public class PhasePanel extends Panel {
   private PlotHipi	app;
   //private PhaseField phaseField;
   private TextField phaseField;
   private double step;

   public PhasePanel(String title,String startValue, PlotHipi app) {
        super();
	this.setLayout(new FlowLayout());

        Label enterPhase = new Label (title);
        this.add("West",enterPhase);

	//phaseField = new PhaseField (startValue,app);
	phaseField = new TextField(7) ;
	phaseField.setText(startValue);
	this.add("East",phaseField);
        Button upButton=new Button("inc");
        this.add ("East",upButton);
        Button downButton=new Button("dec");
        this.add ("East",downButton);

        Label t3 = new Label ("Step:");
        this.add ("East",t3);
        Choice choice = new Choice();
        choice.addItem(" 3");
        choice.addItem(" 2");
        choice.addItem(" 1");
        choice.addItem(" 0.1");
        choice.addItem(" 0.01");
        choice.addItem(" 0.001");
        choice.addItem(" 0.0001");
        choice.addItem(" 0.00001");
        this.add ("East",choice);
        choice.select(" 0.1");
	step=0.1;
	
        this.app=app;
   }
    public boolean action(Event evt, Object obj) {
        switch (evt.id) {
           case Event.ACTION_EVENT: {
              if (evt.target instanceof TextField) {
                 Double period= Double.valueOf((String) evt.arg);
                 app.setPhase(period.doubleValue());
              }
              if (evt.target instanceof Choice) {
                 Double val= Double.valueOf((String) evt.arg);
                 step=val.doubleValue();
                 //System.out.println("New Step : "+val.toString());
              }
              if (evt.target instanceof Button) {
                 Button theButton= (Button)evt.target;
                 String label= theButton.getLabel();
                 double presper= app.getPhase();
                 Double period= new Double (0);
                 
                 if (label=="inc") period= new Double(presper+step +(.000005));
                 if (label=="dec") period= new Double(presper-step -(.000005));
                 
                 String tmpS= period.toString();
                 if (tmpS.length() > 7) tmpS=tmpS.substring(0,7);

                 phaseField.setText(tmpS);
                 app.setPhase(new Double(tmpS).doubleValue());
              }
           }
        }
        return true;
    } 

}

