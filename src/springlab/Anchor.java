package springlab;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import shiffman.box2d.Box2DProcessing;

public class Anchor {
	
	PApplet proc;
	Box2DProcessing box2d;
	//PImage hand_img;
//	PImage hook_img;
	Body body;
	
	int x;
	int y;
	int w;
	int h;
	
	BodyDef bd = new BodyDef();

	public Anchor(int _x, int _y, PApplet p, Box2DProcessing b2){
		
		//constructor
		this.proc = p;
		this.box2d = b2;
		this.x = _x;
		this.y = _y;
		this.w = 20;
		this.h = 20;
		
//		hook_img = p.loadImage("hook.png");
		
	    bd.position.set(box2d.coordPixelsToWorld(new Vec2((int) this.x,(int) this.y)));
	    bd.type = BodyType.STATIC;
	    bd.fixedRotation = true;
	    
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
	    fd.density = 1f;
	    fd.friction = 0.3f;
	    fd.restitution = 0.1f;
//	    fd.filter.categoryBits = Main.ANCHOR_BITS;
//	    fd.filter.maskBits = Main.HAND_BITS;
	    
	    this.body.createFixture(fd);
	}
	
	public void draw() {
		//proc.image(hand_img, this.x, this.y);
		Vec2 pos = this.box2d.getBodyPixelCoord(this.body);
		this.x = (int)pos.x;
		this.y = (int)pos.y;
		proc.fill(255);
		proc.stroke(0);
		proc.strokeWeight(1);
		
//		proc.pushMatrix();
//		proc.imageMode(PConstants.CENTER);
//		proc.image(hook_img, this.x, this.y, this.w+20, this.h+20);
//		proc.popMatrix();
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
	
}
