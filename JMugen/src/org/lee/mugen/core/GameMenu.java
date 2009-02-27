package org.lee.mugen.core;

import java.util.List;

import org.lee.mugen.background.Background;
import org.lee.mugen.core.renderer.game.system.TitleInfoRender;
import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.fight.system.elem.ItemName;
import org.lee.mugen.input.CmdProcDispatcher;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.Renderable;

public class GameMenu implements Game {

	@Override
	public void addRender(Renderable r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayPendingScreeen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Renderable> getRenderables() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void onDebugAction(DebugAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() throws Exception {
		render.render();
		
	}

	@Override
	public void renderDebugInfo() {
		// TODO Auto-generated method stub
		
	}

	private TitleInfoRender render;
	
	
	boolean fire;
	private float lastIndexForMove = 0;
	private int incIndexForMove = 0;
	private int lastStartIndex = 0;
	private float speedSwitch = 0.1f;
	
	@Override
	public void init(GameWindow container) throws Exception {
		render = new TitleInfoRender(this);
		fire = false;
		next = null;
		container.addActionListener(new GameWindow.MugenKeyListener() {
			long lastPress = 0;
			@Override
			public void action(int key, boolean isPress) {
				long now = System.currentTimeMillis();
				
				if (!isPress)
					return;
				
				for (String id: CmdProcDispatcher.getSpriteDispatcherMap().keySet()) {
					CmdProcDispatcher cmdOne = CmdProcDispatcher.getSpriteDispatcherMap().get(id);
					if (cmdOne.getDown() == key) {
						lastPress = System.currentTimeMillis();
						lastStartIndex = MugenSystem.getInstance().getTitleInfo().getMenu().getItemname().startIndex();
						MugenSystem.getInstance().getTitleInfo().getMenu().getItemname().increaseCurrentIndex();
					} else if (cmdOne.getUp() == key) {
						lastPress = System.currentTimeMillis();
						lastStartIndex = MugenSystem.getInstance().getTitleInfo().getMenu().getItemname().startIndex();
						MugenSystem.getInstance().getTitleInfo().getMenu().getItemname().decreaseCurrentIndex();
					} else if (isButton(cmdOne, key)) {
						fire = true;
					}
					
				}
				
				
			}
			private boolean isButton(CmdProcDispatcher cmdOne, int key) {
				boolean result = 
					cmdOne.getA() == key
					|| cmdOne.getB() == key
					|| cmdOne.getB() == key
					|| cmdOne.getX() == key
					|| cmdOne.getY() == key
					|| cmdOne.getZ() == key
					|| cmdOne.getAbc() == key
					|| cmdOne.getXyz() == key;
				return result;
			}});
	}
	int time = 0;
	@Override
	public void update(int delta) throws Exception {
		if (fire) {
			selectNext();
			return;
			
		}
		speedSwitch = 0.3f;
		Background br = MugenSystem.getInstance().getTitleBackground();
		br.process();
		int startIndex = MugenSystem.getInstance().getTitleInfo().getMenu().getItemname().startIndex();
		if (lastStartIndex + lastIndexForMove < startIndex) {
			lastIndexForMove += speedSwitch;
			incIndexForMove -= speedSwitch * 10;
			if (lastStartIndex + lastIndexForMove > lastStartIndex + 1) {
				lastStartIndex = lastStartIndex + 1;
				lastIndexForMove = 0f;
				incIndexForMove = 0;
			}
		
		} else if (lastStartIndex + lastIndexForMove > startIndex) {
			lastIndexForMove -= speedSwitch;
			incIndexForMove += speedSwitch * 10;
			if (lastStartIndex + lastIndexForMove < lastStartIndex - 1) {
				lastStartIndex = lastStartIndex - 1;
				lastIndexForMove = 0f;
				incIndexForMove = 0;
			}
		} else {
			incIndexForMove = 0;
			lastIndexForMove = 0;
			lastStartIndex = MugenSystem.getInstance().getTitleInfo().getMenu().getItemname().startIndex();
		}
//		lastIndexForMove = ((int)(lastIndexForMove * 100))/100f;
		time++;
	}

	public int getTime() {
		return time;
	}

	public int getaddPixel() {
		return incIndexForMove;
	}

	public int getLastStartIndex() {
		return lastStartIndex;
	}

	private void selectNext() {
		if (fire) {
			int index = MugenSystem.getInstance().getTitleInfo().getMenu().getItemname().getCurrentIndex();
			switch (index) {
			case ItemName.arcade:
				next = GameSelect.getInstance();
				GameSelect.getInstance().setTilte("arcade");
				break;
			case ItemName.versus:
				
				break;
			
			case ItemName.teamarcade:
				
				break;
			case ItemName.teamversus:
				
				break;
			case ItemName.teamcoop:
				
				break;
			case ItemName.survival:
				
				break;
			case ItemName.survivalcoop:
				
				break;
			case ItemName.training:
				
				break;
			case ItemName.watch:
				
				break;
			case ItemName.options:
				
				break;
			case ItemName.exit:
				
				break;
			default:
				break;
			}
			fire = false;
		}
	}
	
	Game next;
	@Override
	public Game getNext() throws Exception {
		return next == null?this: next;
	}

	@Override
	public void reInit(GameWindow container) throws Exception {
		fire = false;
		MugenSystem.getInstance().getTitleInfo().getMenu().getItemname().setCurrentIndex(0);
	}
	static GameMenu instance;
	public static GameMenu getInstance() {
		if (instance == null)
			instance = new GameMenu();
		return instance;
	}

	@Override
	public void free() {
		// TODO Auto-generated method stub
		
	}

}
