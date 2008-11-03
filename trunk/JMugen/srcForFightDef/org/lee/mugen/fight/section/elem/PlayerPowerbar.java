package org.lee.mugen.fight.section.elem;

import java.util.HashMap;
import java.util.Map;

public class PlayerPowerbar extends PlayerLifebar {
	Type counter;
	Map<Integer, Type> level = new HashMap<Integer, Type>();
	public Type getCounter() {
		return counter;
	}
	public void setCounter(Type counter) {
		this.counter = counter;
	}

	
	public Map<Integer, Type> getLevel() {
		return level;
	}
	public void setLevel(Map<Integer, Type> level) {
		this.level = level;
	}
	@Override
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.startsWith("level")) {
			String sNum = name.substring(5, name.indexOf("."));
			int num = 0;
			if (sNum.length() > 0) {
				num = Integer.parseInt(sNum);
			}
			Type elem = level.get(num);
			if (elem == null) {
				elem = new Type();
				level.put(num, elem);
			}
			elem.setType(Type.getNext(name), elem, value);
			elem.parse(Type.getNext(name), value);
			
		}
		
	}
	
	@Override
	public void process(boolean isLeft, int power, int maxPower,
			boolean processMid) {
		if (isLeft) {
	        float rangeStart = getRange().getX().x;
	        float rangeEnd = getRange().getX().y;
	        
	        int speed = rangeStart > rangeEnd? -1: 1;
	        
	        float newRangeFrontStart = (maxPower - power) * rangeEnd / maxPower;
	        
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
            
            float newRangeFrontStart = (maxPower - power) * rangeEnd / maxPower;
            
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
}
