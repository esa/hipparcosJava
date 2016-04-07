
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
// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.

package hipparcos.sky3d;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.vecmath.*;


import hipparcos.tools.*;

/** Panel to Display Star Attribtes */
public class StarPanel extends Panel implements ActionListener {
  protected Star3D  star; 
  Label hip,pos,alpha,delta,paralax,dist,mag,b_v,status;
  
  /** initialiser */ 
  protected void init() {
	 hip=new Label("Select",Label.RIGHT);
	 pos=new Label("a star ",Label.RIGHT);
	 alpha=new Label("  by",Label.RIGHT);
	 delta=new Label("clicking",Label.RIGHT);
	 paralax=new Label("on it. ",Label.RIGHT);
	 dist=new Label("     ",Label.RIGHT);
	 mag=new Label("     ",Label.RIGHT);
	 b_v=new Label("     ",Label.RIGHT);
	 Label hipl = new Label ("HIP:");
	 hipl.setForeground(Color.blue);
	 Label posl = new Label ("Pos:");
	 posl.setForeground(Color.blue);
	 Label alphal = new Label ("alpha:");
	 alphal.setForeground(Color.blue);
	 Label deltal = new Label ("delta:");
	 deltal.setForeground(Color.blue);
	 Label paralaxl = new Label ("parallax(mas):");
	 paralaxl.setForeground(Color.blue);
	 Label distl = new Label ("dist(pc):");
	 distl.setForeground(Color.blue);
	 Label magl = new Label ("V(mag):");
	 magl.setForeground(Color.blue);
	 Label b_vl = new Label ("B-V(mag):");
	 b_vl.setForeground(Color.blue);
	 setLayout(new BorderLayout(0,0));
	 Label title = new Label("Last Star Selected", Label.CENTER);
	 title.setForeground(Color.blue);
	 add("North",title);
	 Button view = new Button("View details");
	 view.addActionListener(this);
	 add("South",view);
	 Panel data= new Panel();
	 data.setLayout(new GridLayout(8,2,0,0));
	 data.add(hipl);
	 data.add(hip);
	 data.add(posl);
	 data.add(pos);
	 data.add(alphal);
	 data.add(alpha);
	 data.add(deltal);
	 data.add(delta);
	 data.add(paralaxl);
	 data.add(paralax);
	 data.add(distl);
	 data.add(dist);
	 data.add(magl);
	 data.add(mag);
	 data.add(b_vl);
	 data.add(b_v);
	 add("Center",data);
	 setBackground(Color.white); 
  }
    
	/** Set Star */ 
	public void setStatus(Label status) {
		this.status = status;
    }
	/** Set Star */ 
	public void setStar(Star3D star) {
		this.star = star;
		refresh();
    }
	protected void refresh() {
		if (star != null) {
			hip.setText(star.getStar().getId());
			float dis = (float)(1000/star.getStar().getParalax());
			Vector3d p = Sky3D.makeVec(star.getStar());
			pos.setText(""+(int)p.x+","+(int)p.y+","+(int)p.z);
			alpha.setText(""+star.getStar().getAlpha());
			delta.setText(""+star.getStar().getDelta());
			mag.setText(""+star.getStar().getMag());
			b_v.setText(""+star.getStar().getB_V());
			paralax.setText(""+star.getStar().getParalax());
			dist.setText(""+dis);
		}
	}

  public StarPanel () {
	init();
  }
  public Star3D getStar() {
		return star;
	}

    /** This is required to implement ActionListener - this gets
        called when someone hits a button */
    public void actionPerformed(ActionEvent e){
         String label= e.getActionCommand();
         if (label=="View details") {
			String res = Browser.goTo(star.getStar().makeUrl());
			if (status !=null) status.setText(res);
         }
	}
}; 
