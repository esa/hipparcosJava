
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

import hipparcos.tools.*;
import hipparcos.plot.*;

import java.applet.*;
import java.awt.*;
import java.io.*;

public class Bary implements Runnable{
  private Thread glow = null;
  private Graphics g;
  PlotFit plot;
  private boolean suspended=true;
  private DPoint[] bary=null;
  private DPoint[] norm=null;
  private int step=0;
  private int delay=600;
  private int xpos,ypos,oldxpos,oldypos;
  private int nxpos,nypos,oldnxpos,oldnypos;

 public Bary(Graphics g,PlotFit plot) {
     this.plot=plot;
     this.g=g;
	step=0;
     glow = new Thread(this);
     glow.start();
     setOldPos(-1,-1,-1,-1);
     suspended=true;
 }

 public Bary(Graphics g,PlotFit plot, DPoint[] bary, DPoint[] norm) {
     this.plot=plot;
     this.g=g;
     this.bary=bary;
     this.norm=norm;
	step=0;
     glow = new Thread(this);
     suspended=false;
     glow.start();
 }

 public void setBary (DPoint[] bary,DPoint[] norm) {
     step=0;
     setOldPos(-1,-1,-1,-1);
     this.bary=bary;
     this.norm=norm;
 }

 public void start() {
   if (glow != null) {
        try { // seems to get out of sync somehow 
        Thread.sleep(delay);
        } catch (Exception e){ System.err.println ("start in Bary- thred problem");};

     suspended=false;
   };
	
 }
  public void start(Graphics g) {
	this.g = g;
        start();
  }

 public void stop() {
   if (glow != null) {
     suspended=true;
     setOldPos(-1,-1,-1,-1);
	//System.out.println("StopCalled - Xolps "+oldxpos+" bary[1] "+bary[1]);
   }
 }
 public void run() {
    while (true) {
        try {Thread.sleep(delay);} catch (InterruptedException e){}
	if (!suspended) redraw();
    }
 } 

 private void redraw() {
   if (bary !=null) {
	Color tmp = g.getColor();
//	System.out.println("Redraw oldXpos "+oldxpos);
	if (oldxpos >= 0) showIt(oldxpos,oldypos);
	if (oldnxpos >= 0) shownIt(oldnxpos,oldnypos);
	step++;
	if (step > (bary.length -1)) step =0;
	xpos = plot.calcX(bary[step].getX());
	ypos = plot.calcY(bary[step].getY());
	nxpos = plot.calcX(norm[step].getX());
	nypos = plot.calcY(norm[step].getY());

	//System.out.println("Redraw step "+step+" xpos "+xpos+" ypos "+ypos);
	showIt(xpos,ypos);
	shownIt(nxpos,nypos);
	setOldPos(xpos,ypos,nxpos,nypos);
	g.setColor(tmp);
   }

 }

 private void showIt (int x,int y) {
	int diameter= 8;
	g.setColor(plot.baryColour);
	g.setXORMode(Color.yellow);
	g.fillOval(x-(diameter/2),y-(diameter/2),diameter,diameter);
 };
 private void shownIt (int x,int y) {
	int diameter= 8;
	g.setColor(Color.green);
	g.setXORMode(Color.yellow);
	g.fillOval(x-(diameter/2),y-(diameter/2),diameter,diameter);
 };


 public boolean setDelay(int delay) {
    this.delay=delay;
    return true;
 }

 public boolean setPosition(int x, int y) {
    xpos=x; ypos=y; 
    return true;
 }

 private void setOldPos(int x, int y, int nx, int ny) {
       oldxpos=x; oldypos=y; 
       oldnxpos=nx; oldnypos=ny; 
 }

}
