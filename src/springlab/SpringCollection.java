package springlab;


import java.math.BigDecimal;
import java.util.ArrayList;

public class SpringCollection {
	
	ArrayList<Spring> springs;
	Spring activeSpring; 
	ResearchData rData;
	Hapkit hapkit;
	
	public SpringCollection(ResearchData rData, Hapkit _hapkit){
		this.springs = new ArrayList<Spring>();
		this.rData = rData;
		this.hapkit = _hapkit;
	}
	
	public boolean add(Spring p){
		return springs.add(p);
	}
	
	public BigDecimal getActiveForce() {
		return this.activeSpring.getForce();
	}
	
	public void displayForces(boolean display_on) {
		for (Spring p : springs) {
			p.displayForce(display_on);
		}
	}
	
	public void displayStiffness(boolean b) {
		for (Spring p : springs) {
			p.displayK(b);
		}
	}
	
	public void draw() {
		for (Spring p : springs) {
			if(p != null){
				p.draw();
			}
		}
	}
	
	public void setActive(Spring p){
		if(activeSpring == null){
			activeSpring = p;
			activeSpring.getHand().swapIcon();
		}else{
			activeSpring.getHand().swapIcon();
			p.hand.swapIcon();
			activeSpring = p;
		}
		if(rData.getInputMode() == ResearchData.HAPKIT_MODE){
			System.out.println("Setting Hapkit k-constant to:");
			System.out.println(p.getK());
			this.hapkit.setKConstant(p.getK());
			// MAKES ALL OTHER SPRING ACT NORMALLY AGAIN:
			destroyOldHapkitJoints();
		}
	}
	
	public void updateActiveSpring(int mx, int my, boolean pressed, Hapkit hapkit) {
		for (Spring p : springs) {
			if (p != null && p.getHand().contains(mx, my)) {
				this.setActive(p);
				rData.logEvent(p.getK(), -1, "SWITCHING BETWEEN SPRINGS");
				break;
			}
		}
		
		if(rData.getInputMode() == ResearchData.MOUSE_MODE){
			this.activeSpring.mouseUpdate(mx, my, pressed);
		}else{
			// Why was the following line included?
			//this.activeSpring.hapkitUpdate(my);
		}
	}
	
	private void destroyOldHapkitJoints() {
		for (Spring p : springs) {
			if(p != null && !p.equals(activeSpring)){
				p.hand.destroy();
			}
		}
	}

	public void updateActiveSpringPosition(double hapkitPos) {
		int currentPos = (int) this.activeSpring.getX()+this.activeSpring.originalLen+10;
		int newPos = (int) (currentPos + hapkitPos);
		//System.out.println(hapkitPos);
		this.activeSpring.hapkitUpdate(newPos);	
	}

	public void delete(int value) {
		springs.remove(value);
		springs.add(value, null);
	}

	public void add(int x_i, Spring p) {
		springs.add(x_i, p);
	}

}
