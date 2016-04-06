package hipparcos.hipi;

import hipparcos.plot.*;

import java.awt.*;
/* Right panel for Plot hipi with description of lines and symbols*/

class Legend extends Canvas {

   private Color baryColour;

   public void init() {
	setBackground(Color.white);
   }
   public Legend( Color baryColour) {
	this.baryColour=baryColour;
	init();
   }
   public Dimension preferredSize() {
	return new Dimension(80,400);
   }
   public void paint( Graphics g) {
	int xoff=4;
	int yoff=15; // this wil change beloow; 
	g.setColor(Color.blue);
	Dimension d= size();
	g.drawRect(1,1,d.width-2,d.height-2);	


	g.setColor(Color.blue);
	g.drawString("FAST Data:",xoff,yoff);
	yoff+=15;
	g.drawString("Fitted",xoff,yoff);
	g.fillOval(xoff+60,yoff-8,4,4);
	yoff+=15;
	g.drawString("Rejected",xoff,yoff);
	g.drawOval(xoff+60,yoff-8,4,4);

	g.setColor(Color.red);
	yoff+=35;
	g.drawString("NDAC Data:",xoff,yoff);
	yoff+=15;
	g.drawString("Fitted",xoff,yoff);
	g.fillOval(xoff+60,yoff-8,4,4);
	yoff+=15;
	g.drawString("Rejected",xoff,yoff);
	g.drawOval(xoff+60,yoff-8,4,4);

	g.setColor(baryColour);
	yoff+=45;
	g.drawString("Barycentric",xoff,yoff);
	yoff+=15;
	g.drawString("motion:",xoff,yoff);
	yoff+=5;
	g.drawLine(xoff,yoff,d.width-xoff,yoff);
	yoff+=15;
	g.drawString("step: 0.1yr",xoff,yoff);

	g.setColor(Color.green);
	yoff+=45;
	g.drawString("Solution:",xoff,yoff);
	yoff+=25;
	int step=6;
        int rside = d.width -xoff;
	rside -=xoff;
	for (int i=xoff;i<rside; i++) {
	   double x = (12*i)/57.3;
	   double sinX = Math.sin(x);
	   Double DsinX = new Double (sinX*15);
 	   int y = yoff + DsinX.intValue();  
	   g.fillOval(i,y,4,4);
	}
	yoff+=35;
	g.drawString("step: 0.1yr",xoff,yoff);

	g.setColor(Color.black);
	yoff+=45;
	g.drawString("Fitted Point",xoff,yoff);
	yoff+=15;
	g.drawString("connected",xoff,yoff);
	yoff+=15;
	g.drawString("to 1-d ",xoff,yoff);
	yoff+=15;
	g.drawString("observation:",xoff,yoff);
	yoff+=25;
	g.fillOval(xoff+10,yoff-2,4,4);
	g.drawLine (xoff+50,yoff-7,xoff+50,yoff+7);
	g.drawLine (xoff+10,yoff,xoff+50,yoff);
	
   }
}
