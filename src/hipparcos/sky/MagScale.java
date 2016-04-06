// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.sky;

import java.awt.*;

class MagScale extends Canvas {

   private int mid=11;

   public void init() {
	setBackground(Color.black);
   }
   public MagScale() {
	init();
   }

   public Dimension preferredSize() {
	return new Dimension(400,63);
   }

   public void paint( Graphics g) {
	int i;
	g.setColor(Color.blue);
	g.drawRect(1,1,size().width-2,size().height-2);	
	int offset=10;
	for (i=1; i<=12; i++){
	   Mag mag=new Mag(i);
	   int diameter=mag.getPixelDiameter();
	   g.setColor(Color.white);
	   g.fillOval(offset,mid-((diameter/2)-mid),diameter,diameter);	
	   g.setColor(Color.green);
	   g.drawString(""+i,offset-5+(diameter/2),54);
	   offset= offset+diameter +16;
	   
	}
   }
}
