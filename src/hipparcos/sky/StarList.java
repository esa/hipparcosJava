package hipparcos.sky;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//import javax.swing.JTextArea;
import hipparcos.tools.*;
import java.util.*;

/** A window to lis all star info in should read stars from iterator */
public class StarList extends Frame implements ActionListener {
	protected  TextArea text;
	protected StarStore sky;
	
	/**
		Constructor - must orovide star source
	 */
	public StarList(StarStore sky) {
		super(" Stars in Current Field ");
		this.sky=sky;
		setSize(600,700);
		text = new TextArea("",50,100,TextArea.SCROLLBARS_BOTH);
		Button b = new Button("Close");
		b.addActionListener(this);
		Button r = new Button("Refresh");
		r.addActionListener(this);
		Panel p = new Panel();
		p.add(b);
		p.add(r);
		add("Center",text);
		add("South",p);
		text.setFont(new Font ("Monospaced",Font.PLAIN,11));
	}
	
	/**
		close or refresh
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.startsWith("Close")) {
				setVisible(false);
				return;
		} 
		if (cmd.startsWith("Refr")) {
				refresh();
				return;
		} 
		
	}

	/* load all data again from the start store */
	public void refresh() {
		Iterator stars = sky.getStars();
		text.setText(Star.header()+"\n");
	 	while (stars.hasNext()) {
			Object star = stars.next();
			text.append(""+star+"\n");
		}	
	}
}
