package org.lee.mugen.fight;

import java.awt.Point;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.sprite.entity.PointF;

public class Powerbar extends Lifebar {
	public static class Counter {
		private Point offset = new Point();
		private PointF font = new PointF();
		private Integer layer = 0;
		public Point getOffset() {
			return offset;
		}
		public void setOffset(Point offset) {
			this.offset = offset;
		}
		public PointF getFont() {
			return font;
		}
		public void setFont(PointF font) {
			this.font = font;
		}
		public Integer getLayer() {
			return layer;
		}
		public void setLayer(Integer layer) {
			this.layer = layer;
		}
		
	}
	
	@Override
	public void process() {
		{
		float p1CurrentPower = StateMachine.getInstance().getSpriteInstance("1").getInfo().getPower();
		float p1MaxPower = StateMachine.getInstance().getSpriteInstance("1").getInfo().getData().getPower();
		
		float rangeStart = p1.getRange().getX().x;
		float rangeEnd = p1.getRange().getX().y;
		
		int speed = rangeStart > rangeEnd? -1: 1;
		
		float newRangeFrontStart = (p1MaxPower - p1CurrentPower) * rangeEnd / p1MaxPower;
		
		if (p1.getRangeFront().getX().x > (int)newRangeFrontStart && p1.getRangeFront().getX().x >= rangeEnd) {
			p1.getRangeFront().getX().x += speed;
		} else if (p1.getRangeFront().getX().x < (int)newRangeFrontStart && p1.getRangeFront().getX().x <= rangeStart) {
			p1.getRangeFront().getX().x -= speed;
		} 
		}
		p1.process();
		
		{
			float p2CurrentPower = StateMachine.getInstance().getSpriteInstance("2").getInfo().getPower();
			float p2MaxPower = StateMachine.getInstance().getSpriteInstance("2").getInfo().getData().getPower();
			
			float rangeStart = p2.getRange().getX().x;
			float rangeEnd = p2.getRange().getX().y;
			
			int speed = rangeStart > rangeEnd? -1: 1;
			
			float newRangeFrontStart = (p2MaxPower - p2CurrentPower) * rangeEnd / p2MaxPower;
			
			if (p2.getRangeFront().getX().x < (int)newRangeFrontStart && p2.getRangeFront().getX().x <= rangeEnd) {
				p2.getRangeFront().getX().x += speed;
			} else if (p2.getRangeFront().getX().x > (int)newRangeFrontStart && p2.getRangeFront().getX().x > rangeStart) {
				p2.getRangeFront().getX().x -= speed;
			} 
			
		}
		p2.process();
	}
	private Counter counter = new Counter();

	public Counter getCounter() {
		return counter;
	}

	public void setCounter(Counter counter) {
		this.counter = counter;
	}
}
