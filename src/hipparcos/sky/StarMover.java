
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

package hipparcos.sky;

import java.applet.*;
import java.awt.*;

/** Take a SkyArea and periodically call move stars
	creating a backround animation it has its own thread
	and does not hang up the display 
*/
public class StarMover implements Runnable{
  private Thread move = null;
  private SkyArea sky;
  private int step=0;
  private int year,startYear=1991;
  private int delay=100;
  private Label info=null;

 public StarMover(SkyArea sky, int startYear) {
     this.sky=sky;
     this.startYear=startYear;
     this.year=startYear;
     this.delay=delay;
 }

 public void start() {
     move = new Thread(this);
     move.start();
 }

 public void stop() {
     if (move !=null) move.stop();
     move=null;
 }

 public void run() {
    while (true) {
        try {Thread.sleep(delay);} catch (InterruptedException e){}
	year+=Star2D.yearStep();
        sky.moveStars(step++);
	if (info !=null) info.setText(""+year);
    }
 } 

 public boolean reset() {
    this.year=startYear;
    this.step=0;
    if (info !=null) info.setText(""+year);
    sky.moveStars(step);
    return true;
 }

 public boolean setDelay(int delay) {
    this.delay=delay;
    return true;
 }

 public boolean setInfo(Label info) {
	this.info=info;
 	return true;
 }  

}
