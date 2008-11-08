package org.lee.mugen.stage.section.elem;

import org.lee.mugen.fight.section.Section;
import org.lee.mugen.stage.Stage;

public class StageInfo implements Section {
	private Stage parent = null;
	public StageInfo(Stage stage) {
		parent = stage;
	}
	 //Z offset for drawing
	//Adjust this value to move the ground level up/down in the screen.
	//It's the position where the players stand at.
	//Up - smaller, Down - larger
	private int zoffset = 190;

	//Leave this at 1. It makes the players face each other
	private boolean autoturn = true;

	//Set the following to 1 to have the background be reset between
	//rounds.
	private boolean resetbg = true;
	

	
	public int getZoffset() {
		return zoffset;
	}



	public void setZoffset(int zoffset) {
		this.zoffset = zoffset;
	}



	public boolean isAutoturn() {
		return autoturn;
	}



	public void setAutoturn(boolean autoturn) {
		this.autoturn = autoturn;
	}



	public boolean isResetbg() {
		return resetbg;
	}



	public void setResetbg(boolean resetbg) {
		this.resetbg = resetbg;
	}



	@Override
	public void parse(Object root, String name, String value) throws Exception {
		if (name.equals("zoffset")) {
			zoffset = Integer.parseInt(value);
		} else if (name.equals("autoturn")) {
			autoturn = Integer.parseInt(value) != 0;
		} else if (name.equals("resetbg")) {
			resetbg = Integer.parseInt(value) != 0;
		} else if (name.equals("hires")) {
			if (Integer.parseInt(value) != 0) {
				parent.getScaling().setXscale(0.5f);
				parent.getScaling().setYscale(0.5f);
			}
			
		}
		
	}



}
