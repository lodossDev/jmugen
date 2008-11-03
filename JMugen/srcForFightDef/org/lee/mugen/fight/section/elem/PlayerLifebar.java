package org.lee.mugen.fight.section.elem;

import org.lee.mugen.fight.section.elem.Bar.Range;

public class PlayerLifebar extends Bar {
	Type mid;
	Type front;
	
	Range rangeMid = new Range();
	Range rangeFront = new Range();

	
	
	public Range getRangeMid() {
		return rangeMid;
	}
	public Range getRangeFront() {
		return rangeFront;
	}
	public Type getMid() {
		return mid;
	}
	public void setMid(Type mid) {
		this.mid = mid;
	}
	public Type getFront() {
		return front;
	}
	public void setFront(Type front) {
		this.front = front;
	}
	public void process(boolean isLeft, int life, int maxLife, boolean processMid) {
		if (isLeft) {
	        float rangeStart = getRange().getX().x;
	        float rangeEnd = getRange().getX().y;
	        
	        int speed = rangeStart > rangeEnd? -1: 1;
	        
	        float newRangeFrontStart = (maxLife - life) * rangeEnd / maxLife;
	        
	        if (getRangeFront().getX().x > (int)newRangeFrontStart && getRangeFront().getX().x >= rangeEnd) {
	                getRangeFront().getX().x += speed;
	        } else if (getRangeFront().getX().x < (int)newRangeFrontStart && getRangeFront().getX().x < rangeStart) {
	                getRangeFront().getX().x -= speed;
	        } 
	        if (processMid) {
	                if (getRangeMid().getX().x > (int)newRangeFrontStart && getRangeMid().getX().x >= rangeEnd) {
	                        getRangeMid().getX().x += speed;
	                } else if (getRangeMid().getX().x < (int)newRangeFrontStart && getRangeMid().getX().x < rangeStart) {
	                        getRangeMid().getX().x -= speed;
	                } 
	        }
		} else {
            
            float rangeStart = getRange().getX().x;
            float rangeEnd = getRange().getX().y;
            
            int speed = rangeStart > rangeEnd? -1: 1;
            
            float newRangeFrontStart = (maxLife - life) * rangeEnd / maxLife;
            
            if (getRangeFront().getX().x < (int)newRangeFrontStart && getRangeFront().getX().x <= rangeEnd) {
                    getRangeFront().getX().x += speed;
            } else if (getRangeFront().getX().x > (int)newRangeFrontStart && getRangeFront().getX().x > rangeStart) {
                    getRangeFront().getX().x -= speed;
            } 
            if (processMid) {
                    if (getRangeMid().getX().x < (int)newRangeFrontStart && getRangeMid().getX().x <= rangeEnd) {
                            getRangeMid().getX().x += speed;
                    } else if (getRangeMid().getX().x > (int)newRangeFrontStart && getRangeMid().getX().x > rangeStart) {
                            getRangeMid().getX().x -= speed;
                    } 
            }               
		}

	}
	@Override
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.startsWith("mid.")) {
			if (mid == null) {
				mid = new Type();
			}
			mid.setType(Type.getNext(name), mid, value);
			mid.parse(Type.getNext(name), value);
		} else if (name.startsWith("front.")) {
			if (front == null) {
				front = new Type();
			}
			front.setType(Type.getNext(name), mid, value);
			front.parse(Type.getNext(name), value);
		}
	}
}
