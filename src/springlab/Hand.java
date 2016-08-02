package springlab;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import shiffman.box2d.Box2DProcessing;

public class Hand implements PConstants {

	PApplet parent;
	Box2DProcessing box2d;
	PImage inactive_hand_img;
	PImage active_hand_img;
	PImage current_hand_img;
	
	ResearchData rData;
	
	Body body;
	MouseJoint mj;

	int x;
	int fixed_y;
	int y;
	int w;
	int h;
	boolean fixed;
	
	int left_limit = 50;
	int right_limit = 1000;

	public Hand(int _x, int _y, boolean _fixed, PApplet p, Box2DProcessing b2, ResearchData rData) {

		//constructor
		this.parent = p;
		this.box2d = b2;
		this.x = _x;
		this.y = _y;
		this.fixed_y = this.y;
		this.fixed = _fixed;
		this.rData = rData;

		this.inactive_hand_img = p.loadImage("hand.png");
		this.active_hand_img = p.loadImage("active_hand.png");
		this.current_hand_img = inactive_hand_img;

		this.w = this.inactive_hand_img.width / 6;
		this.h = this.inactive_hand_img.height / 6;

		this.inactive_hand_img.resize(this.w, this.h);
		this.active_hand_img.resize(this.w, this.h);

		BodyDef bd = new BodyDef();
		bd.position.set(box2d.coordPixelsToWorld(new Vec2((int) x,(int) y)));
		bd.type = BodyType.DYNAMIC;
		bd.fixedRotation = true;

		//bd.angularDamping = 0;
		bd.linearDamping = (float) 0.3;

		this.body = box2d.createBody(bd);
		//body.setGravityScale(0);

		PolygonShape sd = new PolygonShape();

		float box2dW = box2d.scalarPixelsToWorld(this.w/2);
		float box2dH = box2d.scalarPixelsToWorld(this.h/2);

		sd.setAsBox(box2dW, box2dH);

		// Define a fixture
		FixtureDef fd = new FixtureDef();
		fd.shape = sd;

		// Parameters that affect physics
		fd.density = 0.3f; 
		fd.friction = 0.01f;
		fd.restitution = 0.9f;
//		fd.filter.categoryBits = Main.HAND_BITS;
//		fd.filter.maskBits = Main.ANCHOR_BITS;
		//fd.setSensor(true);

		this.body.createFixture(fd);
		this.body.resetMassData();
	}

	public void draw() {
		if (mj != null) {
			
			if(!rData.isHapkitMode()){
				if (!this.fixed) {
					this.mousePosUpdate(parent.mouseX, parent.mouseY);
				} else {
					this.mousePosUpdate(parent.mouseX, this.fixed_y);
				}
			}
			
			// We can get the two anchor points
			Vec2 v1 = new Vec2(0,0);
			mj.getAnchorA(v1);
			Vec2 v2 = new Vec2(0,0);
			mj.getAnchorB(v2);
			// Convert them to screen coordinates
			v1 = box2d.coordWorldToPixels(v1);
			v2 = box2d.coordWorldToPixels(v2);
			// And just draw a line
			parent.stroke(0);
			parent.strokeWeight(1);
			parent.line(v1.x,v1.y,v2.x,v2.y);
		}
		
		//parent.image(hand_img, this.x, this.y);
		Vec2 pos = this.box2d.getBodyPixelCoord(this.body);
		this.x = (int)pos.x;
		this.y = (int)pos.y;
		
		parent.pushMatrix();
		int new_y = this.y;
		if (this.fixed) {
			new_y = this.fixed_y;
		}
		parent.translate(this.x, new_y); // Move the center of the new coordinate system to the place where we want the hand image to be.
		parent.rotate(PApplet.radians(90)); // Rotate the new coordinate system by 90 degrees.
		parent.imageMode(PConstants.CENTER);
		parent.image(current_hand_img, 0, 0);
		parent.popMatrix(); // When we pop the matrix, the hand image is rotated and in the correct position.
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setW(double scale) {
		this.w *= scale;
		current_hand_img.resize(this.w, this.h);
	}

	public void setH(double scale) {
		this.h *= scale;
		current_hand_img.resize(this.w, this.h);
	}

	public void bind(float mx, float my) {
		MouseJointDef mjd = new MouseJointDef();
		mjd.bodyA = box2d.getGroundBody();
		mjd.bodyB = this.body;
		Vec2 mp = this.box2d.coordPixelsToWorld(mx, my);
		mjd.target.set(mp);

		mjd.frequencyHz = 3000;
		mjd.dampingRatio = (float) 0.1;
	    mjd.maxForce = (float) (10000.0 * this.body.m_mass);

		this.mj = (MouseJoint) box2d.world.createJoint(mjd);
	}

	public void mouseUpdate(int mx, int my, boolean pressed) {
		if (pressed == false) {
			this.destroy();
		} else if (this.contains(mx, my)) {
			if (!this.fixed) {
				this.bind(mx, my);
			} else {
				this.bind(mx, this.fixed_y);
			}
		}
	}
	
	public void hapkitUpdate(int pos){
		if(this.mj !=null){
			Vec2 mp = this.box2d.coordPixelsToWorld(pos, this.fixed_y);
			this.mj.setTarget(mp);
		}else{
			this.bind(pos, this.fixed_y);
		}
	}
	
	public void hapkitUpdate(int mx, int originalLen) {
		// Currently this method is never called.
		if(this.mj !=null){
			//System.out.println(originalLen+my);
			Vec2 mp = this.box2d.coordPixelsToWorld(originalLen+mx, this.fixed_y);
			this.mj.setTarget(mp);
		}else{
			this.bind(originalLen+mx, this.fixed_y);
		}
		
	}
	
	public void mousePosUpdate(int mx, int my) {
		// Update the position
		if (mx >= left_limit && mx <= right_limit) {
			Vec2 mouseWorld = box2d.coordPixelsToWorld(mx,my);
			this.mj.setTarget(mouseWorld);
		}
	}

	public void destroy() {
		// We can get rid of the joint when the mouse is released
		if (this.mj != null) {
			box2d.world.destroyJoint(this.mj);
			this.mj = null;
			System.out.println("Mouse Joint Destroyed");
		}
	}

	public boolean contains(int x, int y) {
		Vec2 worldPoint = this.box2d.coordPixelsToWorld(x, y);
		Fixture f = this.body.getFixtureList();
		boolean inside = f.testPoint(worldPoint);
		return inside;
	}

	public void swapIcon() {
		if(current_hand_img == inactive_hand_img){
			current_hand_img = active_hand_img;
		}else{
			current_hand_img = inactive_hand_img;
		}
	}
}
