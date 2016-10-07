package springlab;

import java.math.BigDecimal;
import java.util.ArrayList;

import controlP5.CheckBox;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.RadioButton;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.Event;

public class GraphPanel extends Component {

	Canvas c;
	PApplet p;
	PImage axes_img;
	SpringCollection sc;
	Spring activeSpring;
	float dotx;
	float doty;
	ArrayList<Float> xdot = new ArrayList<Float>();
	ArrayList<Float> ydot = new ArrayList<Float>();
	int red;
	int green;
	int blue;
	
	public GraphPanel(Main main, ControlP5 cp5, int _x, int _y, int _w, int _h, Canvas _c) {
		super(_x,_y,_w,_h);
		this.c = _c;
		this.p = main;
		this.sc = this.c.getSpringCollection();
		this.axes_img = this.p.loadImage("axes.png");
		
		this.red = (int)this.p.random(0, 255);
		this.green = (int)this.p.random(0, 255);
		this.blue = (int)this.p.random(0, 255);
	}
	
	@Override
	public void step() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void draw() {
		
		this.p.fill(255);
		this.p.strokeWeight(1);
		this.p.stroke(0);
		this.p.rect(x, y, w, h);
		this.p.fill(0);
		
//		this.p.text("Numerical Forces", x+10, y+20);
//		this.p.text("Spring Stiffness", x+10, y+110);
//		
		this.p.image(axes_img, this.w / 2 + 10, this.h / 2 + 10, this.w, this.h);
//		this.p.image(stiff_img, x+50, y+150, (int) (stiff_img.width), (int) (stiff_img.height));
		this.activeSpring = this.sc.activeSpring;
		BigDecimal xpos = activeSpring.getDisplacement();
		BigDecimal ypos = activeSpring.getForce();
		
		dotx = this.p.map(xpos.floatValue(), (float)-0.5, (float)0.5, (float)this.x, (float)this.w);
		doty = this.p.map(ypos.floatValue(), (float)-30, (float)30, (float)this.h, (float)this.y);
		
		this.p.fill(255, 0, 0);
		for (int i=0; i < xdot.size(); i++) {
			  this.p.ellipse(xdot.get(i), ydot.get(i), 5, 5);
		}
		this.p.ellipse(dotx, doty, 5, 5);
		
	}
	
	public void keyPressed(int key) {
		if (key == 32) {
			// Space key was pressed
			xdot.add(dotx); // These names are confusing!
			ydot.add(doty);
		}
	}

	public void clearDots(){
		xdot.clear();
		ydot.clear();
		
	}
	
	public void setRandomColor() {
		
	}
	
	public void controlEvent(ControlEvent theEvent) {
	}	
}