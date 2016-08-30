package springlab;

import java.awt.Font;

import org.jbox2d.common.Vec2;

import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import shiffman.box2d.Box2DProcessing;

public class Canvas extends Component {

	static Box2DProcessing box2d;
	PApplet parent;
	Hapkit hapkit;
	
	double hapkitPos;
	
	Hand hand;
//	Boundary ceiling;
//	Boundary floor;
	
	PImage wood_plank_img_01;
	PImage wood_plank_img_02;
	
//	PImage next_img;
	
//	PImage spring_x;
//	PImage spring_y;
//	PImage spring_x_active;
//	PImage spring_y_active;
	
	Button next, X, Y;
	
//	int springx_img_x;
//	int springx_img_y;
//	
//	int springy_img_x;
//	int springy_img_y;	
	
//	int spring_img_w;
//	int spring_img_h;
	
	int numSprings;
//	private Ruler ruler;
	ResearchData rData;
	
	Spring s1, s2, s3, s4, s5;
	SpringCollection sc;
	
//	PImage dotted_line_img;
//	boolean draw_dotted_line;
	
//	Button delete1;
//    Button delete2;
//	Button delete3;

	static int Y1, Y2, Y3, Y4, Y5, X_ALL;
	
	int lab_number = 1;
	
	public Canvas(Main main, ControlP5 cp5, int _x, int _y, int _w, int _h, Hapkit _hapkit, ResearchData rData) {
		
		super(_x,_y,_w,_h);
		
		if (lab_number == 1) {
			Y1 = y+50;
			Y2 = y+150;
			Y3 = y+300;
			Y4 = y+400;
			X_ALL = this.x + this.w - 100;
		} else if (lab_number == 2) {
			Y1 = y+50;
			Y2 = y+150;
			Y3 = y+250;
			Y4 = y+350;
			Y5 = y+450;
			X_ALL = this.x + this.w - 100;
		}
		
		this.hapkit = _hapkit;
		if (lab_number == 1) {
			this.numSprings = 4;
		} else if (lab_number == 2) {
			this.numSprings = 5;
		}
		this.rData = rData;
		
//		spring_img_w = 231;
//		spring_img_h = 80;
		
//		springx_img_x = this.x+(this.w/4)-(spring_img_w/2);
//		springx_img_y = this.y+150;
//		
//		springy_img_x = this.x+(3*(this.w/4))-(spring_img_w/2);
//		springy_img_y = this.y+150;	
		
		parent = main; 
		
		wood_plank_img_01 = parent.loadImage("wood-plank.jpg");
		wood_plank_img_02 = parent.loadImage("wood-plank.jpg");
//		next_img = parent.loadImage("arrow-next.png");
//		spring_x = parent.loadImage("springx.jpg");
//		spring_y = parent.loadImage("springy.jpg");
//		spring_x_active = parent.loadImage("springx-active.jpg");
//		spring_y_active = parent.loadImage("springy-active.jpg");
		
		box2d = new Box2DProcessing(parent);
		box2d.createWorld();
		box2d.setScaleFactor(400); // 500 pixels is 1 meter
		box2d.setGravity(0, 0);
		
		// This prevents dynamic bodies from sticking to static ones
		org.jbox2d.common.Settings.velocityThreshold = 0.2f;
		
		if (lab_number == 1) {
			s1 = new Spring(X_ALL, Y1, 25, 270, 55, "Spring A", this.parent, box2d, rData, "spring_red.png");
			s2 = new Spring(X_ALL, Y2, 25, 170, 25, "Spring B", this.parent, box2d, rData, "spring_blue.png");
			s3 = new Spring(X_ALL, Y3, 55, 200, 15, "Spring C", this.parent, box2d, rData, "spring_gray.png");
			s4 = new Spring(X_ALL, Y4, 55, 250, 50, "Spring D", this.parent, box2d, rData, "spring_yellow.png");
			sc = new SpringCollection(rData, hapkit);
			sc.add(s1);
			sc.add(s2);
			sc.add(s3);
			sc.add(s4);
			sc.setActive(s1);
		} else if (lab_number == 2) {
			s1 = new Spring(X_ALL, Y1, 55, 150, 35, "Spring A", this.parent, box2d, rData, "spring_purple.png");
			s2 = new Spring(X_ALL, Y2, 25, 250, 20, "Spring B", this.parent, box2d, rData, "spring_green.png");
			s3 = new Spring(X_ALL, Y3, 55, 150, 45, "Spring C", this.parent, box2d, rData, "spring_yellow.png");
			s4 = new Spring(X_ALL, Y4, 15, 225, 50, "Spring D", this.parent, box2d, rData, "spring_gray.png");
			s5 = new Spring(X_ALL, Y5, 55, 150, 15, "Spring E", this.parent, box2d, rData, "spring_yellow.png");
			sc = new SpringCollection(rData, hapkit);
			sc.add(s1);
			sc.add(s2);
			sc.add(s3);
			sc.add(s4);
			sc.add(s5);
			sc.setActive(s1);
		}
		
		
//		this.draw_dotted_line = true;
//		this.dotted_line_img = parent.loadImage("dotted_line.png");
		
		if(rData.isHapkitMode()){
			rData.logEvent(-1, -1, "Initial K value sent to hapkit");
			//hapkit.setKConstant(sc.activeSpring.getK());
		}
		
//		floor = new Boundary(this.x + this.w/2, this.h - 20, this.w - 20, 20, parent, box2d);
//		ceiling = new Boundary(this.x+10, this.y+30, this.w - 20, 30, parent, box2d);
		
		// Get ruler spacing from Box2d world.
		// Spacing is determined by the ruler height in pixels (300) divided by the spacing in pixels.
		Vec2 spacing1 = box2d.coordWorldToPixels(new Vec2(0, 0));
		Vec2 spacing2 = box2d.coordWorldToPixels(new Vec2(0, 1));
		float one_meter = spacing2.sub(spacing1).length();
		int spacing = (int) (one_meter/10);
		//ruler = new Ruler(parent, cp5, this.x + spacing, this.y+500, (int) one_meter + 50, 40, spacing);
		
//		delete1 = cp5.addButton("Delete1")
//			     .setValue(0)
//			     .setPosition(x+77,y+25)
//			     .setSize(55,20)
//			     .setCaptionLabel("Delete")
//			     .setId(1);
//		
//		delete2 = cp5.addButton("Delete2")
//			     .setValue(1)
//			     .setPosition(x+220,y+25)
//			     .setSize(55,20)
//			     .setCaptionLabel("Delete")
//			     .setId(1);
//		
//		delete3 = cp5.addButton("Delete3")
//			     .setValue(2)
//			     .setPosition(x+370,y+25)
//			     .setSize(55,20)
//			     .setCaptionLabel("Delete")
//			     .setId(1);
		
		cp5.addListener(this);
		
	}
	
	public void step(){
		this.box2d.step();
		
		// Why is this here?
		if(rData.isHapkitMode()){
		  updateSpringPosition();
		  readHapkitPos();
		}
	}
	
	public void draw(){

		parent.fill(255);
		parent.stroke(0);
		parent.strokeWeight(1);
		parent.rect(x, y, w, h);
		parent.textSize(18); 
		parent.fill(0);
		
//		parent.pushMatrix();
		if (lab_number == 1) {
			parent.imageMode(PConstants.CORNER);
			parent.image(wood_plank_img_01, X_ALL, Y1-25, wood_plank_img_01.width/2, Y2-Y1+40);
	//		parent.popMatrix();
			
			parent.imageMode(PConstants.CORNER);
			parent.image(wood_plank_img_01, X_ALL, Y3-25, wood_plank_img_02.width/2, Y4-Y3+50);
		} else if (lab_number == 2) {
			parent.imageMode(PConstants.CORNER);
			parent.image(wood_plank_img_01, X_ALL, Y1-25, wood_plank_img_01.width/2, Y5-Y1+40);
		}
		
		sc.draw();
//		floor.draw();
		
		// Draw neutral line
//		parent.imageMode(PConstants.CENTER);
		// Hand-tweaked coordinates that put the neutral line at the exact right place (ugly)
//		if (this.draw_dotted_line) {
//			parent.image(dotted_line_img, this.x + s1.originalLen + 23, Y2-10);
//		}
		
		//ruler.draw();
		
		if (lab_number == 1) {
			// Hack alert. Create boxes to surround groups of springs.
			parent.fill(0, 0, 0, 0);
			parent.stroke(111, 71, 0);
			parent.strokeWeight(1);
			// Top box
			int top_x = x+10;
			int top_y = y+10;
			int top_w = w-20;
			int top_h = Y2-Y1+115;
			parent.rect(top_x, top_y, top_w, top_h);
			Font p1 = parent.getFont();
			PFont p2 = parent.createFont("Verdana", 12);
			parent.fill(120);
			parent.textFont(p2);
			parent.textSize(18);
			parent.setFont(p1);
			parent.text("Weak Spring Co.", top_x+10, top_y+20);
			
			// Bottom box
			int bottom_x = x+10;
			int bottom_y = y+(Y2-Y1+115)+45;
			int bottom_w = w-20;
			int bottom_h = Y4-Y3+115;
			parent.fill(0, 0, 0, 0);
			parent.rect(bottom_x, bottom_y, bottom_w, bottom_h);
			parent.fill(120);
			parent.textSize(18);
			parent.text("Strong Spring Co.", bottom_x+10, bottom_y+20);
		} else if (lab_number == 2) {
			// Nothing for now
		}
		
	}
	
	private void updateSpringPosition() {
		sc.updateActiveSpringPosition(hapkitPos);
	}
	
	public void readHapkitPos() {
		hapkitPos = this.hapkit.getPos();
	}
	
	public void displayForces(boolean on) {
		this.sc.displayForces(on);
	}
	
	public void mousePressed() {
		sc.updateActiveSpring(parent.mouseX, parent.mouseY, true, hapkit);
	}
	
	public void mouseReleased() {
		sc.updateActiveSpring(parent.mouseX, parent.mouseY, false, hapkit);
	}
	
	public SpringCollection getSpringCollection() {
		return this.sc;
	}

	@Override
	public void controlEvent(ControlEvent event) {
//		System.out.println(event.getValue());
//		if(event.isFrom(delete1)){
//			sc.delete((int)event.getValue());
//		}else if(event.isFrom(delete2)){
//			sc.delete((int)event.getValue());
//		}else if(event.isFrom(delete3)){
//			sc.delete((int)event.getValue());
//		}
	}

	public void displayStiffness(boolean b) {
		this.sc.displayStiffness(b);
	}
	
	public void displayEquilibriumPosition(boolean b) {
//		if (b) {
//			this.draw_dotted_line = true;
//		} else {
//			this.draw_dotted_line = false;
//		}
	}

}
