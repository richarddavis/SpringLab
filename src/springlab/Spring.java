package springlab;

import java.awt.Font;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import shiffman.box2d.Box2DProcessing;

public class Spring {

	int x;
	int y;
	int currentLen;
	int originalLen;
	int width;
	int k;
	String label;
	boolean display_forces;
	boolean display_k;

	PApplet parent;
	Hand hand;
	Box2DProcessing box2d;

//	int spring_img_w = 231;
//	int spring_img_h = 80;

	// BOX2D
	DistanceJointDef djd;
	DistanceJoint dj;

	Anchor anchor;
	PImage spring_img;

	public Spring(int _x, int _y, int _k, int _length, int _width, String label, PApplet p, Box2DProcessing box2d,
			ResearchData rData, String image_name) {

		this.x = _x;
		this.y = _y;
		this.k = _k;
		this.width = _width;
		this.originalLen = _length;
		this.label = label;
		this.parent = p;
		this.box2d = box2d;

		this.display_forces = true;
		this.display_k = true;

		this.hand = new Hand(this.x - this.originalLen, this.y, true, parent, box2d, rData);
		this.anchor = new Anchor(this.x, this.y, parent, box2d);

		// Import photo
		this.spring_img = parent.loadImage(image_name);

		// Define the joint
		this.djd = new DistanceJointDef();

		djd.bodyA = this.anchor.body;
		// Body 2 is the Hand's object
		djd.bodyB = this.hand.body;
		// Get the mouse location in world coordinates
		djd.collideConnected = false;
		djd.length = box2d.scalarPixelsToWorld(this.originalLen);

		// Some stuff about how strong and bouncy the spring should be
		// djd.maxForce = (float) (1000.0 * hand.body.m_mass);
		djd.frequencyHz = (float) ((1 / (2 * Math.PI)) * (Math.sqrt(this.k / this.hand.body.m_mass)));
		djd.dampingRatio = 0.001f;

		// Make the joint
		dj = (DistanceJoint) box2d.world.createJoint(djd);
	}

	// TODO: update hand drawing as well.
	public void draw() {
		parent.fill(0);
		parent.text(this.label, this.x + this.originalLen + 80, this.y-50);
		if (dj != null) {
			// We can get the two anchor points
			Vec2 v1 = new Vec2(0, 0);
			dj.getAnchorA(v1);
			Vec2 v2 = new Vec2(0, 0);
			dj.getAnchorB(v2);

			// Convert them to screen coordinates
			v1 = box2d.coordWorldToPixels(v1);
			v2 = box2d.coordWorldToPixels(v2);

			// And draw the container
			parent.imageMode(PConstants.CENTER);
			 this.currentLen = (int) (v1.x - v2.x);
			// int width = 30;
			// spring_img.resize(width, this.spring_img.height);
			// parent.image(spring_img, v1.x, v1.y);
//			 parent.image(spring_img, v2.x + this.spring_img.width/2, this.y, this.spring_img.width, this.spring_img.height);
			 parent.image(spring_img, v2.x + this.currentLen/2, this.y, this.currentLen, this.width);
			 
			// And just draw a line; great for debugging
//			parent.stroke(0);
//			parent.strokeWeight(3);
//			parent.line(v1.x, v1.y, v2.x, v2.y);

			// parent.pushMatrix();
//			parent.imageMode(PConstants.CENTER);
//			parent.image(spring_img, v2.x + this.spring_img.width/2, this.y, this.spring_img.width, this.spring_img.height);
			// parent.popMatrix();

			// (float) (this.y + (0.5*(v2.y-v1.y)))
			// v2.y-v1.y-(hand.current_hand_img.height/2)

			// Hand-tweaked coordinates
			int dfx = this.hand.x - this.hand.w;
			int dfy = this.hand.y + this.hand.h - 10;

			// Hand-tweaked coordinates
			int dsx1 = this.anchor.x + 90;
			int dsy1 = this.hand.y + 45;

			Font p1 = parent.getFont();
			PFont p2 = parent.createFont("Verdana", 12);

			if (display_k) {
				parent.fill(120);
				parent.pushMatrix();
				parent.textFont(p2);
				float d = this.getDisplacement();
				parent.text("X: " + String.format("%.2f", d), dfx + 20, dfy + 20);
				parent.setFont(p1);
				parent.textSize(18);
				parent.popMatrix();
			}

			if (this.display_forces == true) {
				parent.fill(100);
				parent.pushMatrix();
				parent.textFont(p2);
				parent.text("Force: " + String.format("%.2f", this.getForce()), dfx, dfy);
				parent.setFont(p1);
				parent.textSize(18);
				parent.popMatrix();
			}
		}

		this.anchor.draw();
		this.hand.draw();
	}

	public float getLength() {
		Vec2 v1 = new Vec2(0, 0);
		dj.getAnchorA(v1);
		Vec2 v2 = new Vec2(0, 0);
		dj.getAnchorB(v2);

		// Convert them to screen coordinates
		// v1 = box2d.coordWorldToPixels(v1);
		// v2 = box2d.coordWorldToPixels(v2);

		return (v2.sub(v1)).length();
	}
	
	public float getDisplacement() {
		Vec2 v1 = new Vec2(0, 0);
		dj.getAnchorA(v1);
		Vec2 v2 = new Vec2(0, 0);
		dj.getAnchorB(v2);
		float newLength = (v2.sub(v1)).length();
		return djd.length - newLength;
	}

	public void setLength(int len_pixels) {
		dj.setLength(box2d.scalarPixelsToWorld(len_pixels));
	}

	public float getForce() {
		return (this.k * (dj.getLength() - this.getLength()));
	}

	public void setX(int x) {
		this.x = x;
		this.hand.setX(x);
		this.anchor.setX(x);
	}

	public void mouseUpdate(int mx, int my, boolean pressed) {
		this.hand.mouseUpdate(mx, my, pressed);
	}

	public void hapkitUpdate(int pos) {
		this.hand.hapkitUpdate(pos);
	}

	public Hand getHand() {
		return this.hand;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getK() {
		return this.k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public String getLabel() {
		return this.label;
	}

	public void displayForce(boolean on) {
		if (on == true) {
			this.display_forces = true;
			System.out.println("Turning on force display under spring.");
		} else if (on == false) {
			this.display_forces = false;
			System.out.println("Turning off force display under spring.");
		}
	}

	public void setLabel(String stringValue) {
		this.label = stringValue;
	}
	
	public void setK2(int value) {
		// TODO Auto-generated method stub
	}

	public void displayK(boolean b) {
		this.display_k = b;
		
	}
}