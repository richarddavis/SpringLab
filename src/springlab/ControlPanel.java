package springlab;


import controlP5.CheckBox;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.RadioButton;
import processing.core.PApplet;
import processing.core.PImage;

public class ControlPanel extends Component {

	Canvas c;
	PApplet p;
//	RadioButton r2, r;
	CheckBox cbs;
	PImage numerical_img, stiff_img;
	
	
	public ControlPanel(Main main, ControlP5 cp5, int _x, int _y, int _w, int _h, Canvas _c) {
		super(_x,_y,_w,_h);
		this.c = _c;
		this.p = main;
		
//		this.numerical_img = this.p.loadImage("numerical_force.png");
		this.stiff_img = this.p.loadImage("stiffness.png");
		
		cbs = cp5.addCheckBox("checkBoxes")
                .setPosition(x+15,y+15)
                .setSize(30, 30)
                .setColorForeground(this.p.color(120))
		        .setColorActive(this.p.color(200))
		        .setColorLabel(this.p.color(0))
                .setItemsPerRow(1)
                .setSpacingRow(20)
                .addItem("Force", 0)
                .addItem("Displacement", 1)
                ;
		cbs.plugTo(this);
		
//		checkboxes.plugTo(this);
		
		// if we need to implement listeners, consider constructing radio
		// buttons, etc. in main class so that listener can be handed
		// all necessary instances of classes to handle input events appropriately. 
//		r2 = cp5.addRadioButton("displayForcesOnCanvasButton")
//		         .setPosition(x+85,y+35)
//		         .setSize(40,20)
//		         .setColorForeground(this.p.color(120))
//		         .setColorActive(this.p.color(200))
//		         .setColorLabel(this.p.color(0))
//		         .setItemsPerRow(1)
//		         .setSpacingColumn(50)
//		         .addItem("Display ON",1)
//		         .addItem("Display OFF",0)
//		         .setNoneSelectedAllowed(false)
//		         .activate(0);
//		
//		r = cp5.addRadioButton("displayStiffness")
//		         .setPosition(x+100,y+135)
//		         .setSize(40,20)
//		         .setColorForeground(this.p.color(120))
//		         .setColorActive(this.p.color(200))
//		         .setColorLabel(this.p.color(0))
//		         .setItemsPerRow(1)
//		         .setSpacingColumn(60)
//		         .addItem("On",1)
//				 .addItem("Off",0)
//				 .setNoneSelectedAllowed(false)
//		         .activate(0);
//		
//		
//		
//		r.plugTo(this);
//		r2.plugTo(this);
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
//		this.p.image(numerical_img, x+45, y+60, (int) (numerical_img.width/1.5), (int) (numerical_img.height/1.5));
//		this.p.image(stiff_img, x+50, y+150, (int) (stiff_img.width), (int) (stiff_img.height));
		
	}
	
	public void checkBoxes(float[] values) {
		if (values[0] == 1) {
			// Tell spring collection to turn on force display
			this.c.displayForces(false);
		} else {
			// Tell spring collection to turn off force display
			this.c.displayForces(true);
		}
		if (values[1] == 1) {
			this.c.displayStiffness(false);
		} else {
			this.c.displayStiffness(true);
		}
	}
	
//	public void displayStiffness(int value){
//		if(value == 0){
//			this.c.displayStiffness(false);
//		}else{
//			this.c.displayStiffness(true);
//		}
//	}
//
//	public void displayForcesOnCanvasButton(int buttonValue) {
//		if (buttonValue == 1) {
//			this.c.displayForces(true);
//			this.r2.activate(0);
//			// Tell spring collection to turn on force display
//		} else if (buttonValue == 0) {
//			// Tell spring collection to turn off force display
//			this.c.displayForces(false);
//			this.r2.activate(1);
//		} else {
//			// Bad value passed to the function.
//			System.out.println("Bad value sent from radiobutton.");
//		}
//	}

	public void controlEvent(ControlEvent theEvent) {
	}	
}