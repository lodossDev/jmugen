package org.lee.mugen.core;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import org.lee.mugen.core.renderer.game.select.SelectRender;
import org.lee.mugen.fight.select.Characters;
import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.input.CmdProcDispatcher;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.Renderable;
import org.lee.mugen.sprite.character.SpriteDef;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Teammode.TeamMode;

public class GameSelect implements Game {
	private static final GameSelect instance = new GameSelect();
	public static GameSelect getInstance() {
		return instance;
	}
	private GameSelect() {
		// TODO Auto-generated constructor stub
	}
	
	public String getNextMode() {
		return nextMode;
	}

	public void setNextMode(String nextMode) {
		this.nextMode = nextMode;
	}

	@Override
	public void addRender(Renderable r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayPendingScreeen() {
		// TODO Auto-generated method stub
		
	}
	private Game next;
	@Override
	public Game getNext() throws Exception {
		return next == null? this: next;
	}

	@Override
	public List<Renderable> getRenderables() {
		// TODO Auto-generated method stub
		return null;
	}

	private Point position = new Point();
	private boolean fireOne;
	private boolean fireTwo;
	
	public boolean isFireOne() {
		return fireOne;
	}

	public boolean isFireTwo() {
		return fireTwo;
	}

	private Point position2 = new Point();
	
	private void addToPositionOne(int x, int y) {
		int row = MugenSystem.getInstance().getSelectInfo().getRows();
		int col = MugenSystem.getInstance().getSelectInfo().getColumns();
		if (position.x + x < row && position.x + x >= 0)
			position.x += x;
		if (position.y + y < col && position.y + y >= 0)
			position.y += y;
	}

	private void addToPositionTwo(int x, int y) {
		int row = MugenSystem.getInstance().getSelectInfo().getRows();
		int col = MugenSystem.getInstance().getSelectInfo().getColumns();
		if (position2.x + x < row && position2.x + x >= 0)
			position2.x += x;
		if (position2.y + y < col && position2.y + y >= 0)
			position2.y += y;
	}

	
	public Point getPosition2() {
		return position2;
	}
	@Override
	public void init(GameWindow container) throws Exception {
		next = null;
		selectRender = new SelectRender(this);
		container.clearListener();
		reInit(container);
		container.clearListener();
		container.addActionListener(new GameWindow.MugenKeyListener() {
			long lastPress = 0;
			@Override
			public void action(int key, boolean isPress) {
				long now = System.currentTimeMillis();
				
				if (!isPress)
					return;
				
				CmdProcDispatcher cmdOne = CmdProcDispatcher.getSpriteDispatcherMap().get("1");
				if (cmdOne.getDown() == key && !fireOne) {
					lastPress = System.currentTimeMillis();
					addToPositionOne(1, 0);
				
				} else if (cmdOne.getUp() == key && !fireOne) {
					lastPress = System.currentTimeMillis();
					addToPositionOne(-1, 0);
				} else if (cmdOne.getBack() == key && !fireOne) {
					lastPress = System.currentTimeMillis();
					addToPositionOne(0, -1);
				} else if (cmdOne.getForward() == key && !fireOne) {
					lastPress = System.currentTimeMillis();
					addToPositionOne(0, 1);
				} else if (isButton(cmdOne, key)) {
					if (getSelectedSprite(position) != null)
						fireOne = !fireOne;
				}
				
				CmdProcDispatcher cmdTwo = CmdProcDispatcher.getSpriteDispatcherMap().get("2");
				if (cmdTwo.getDown() == key && !fireTwo) {
					lastPress = System.currentTimeMillis();
					addToPositionTwo(1, 0);
				
				} else if (cmdTwo.getUp() == key && !fireTwo) {
					lastPress = System.currentTimeMillis();
					addToPositionTwo(-1, 0);
				} else if (cmdTwo.getBack() == key && !fireTwo) {
					lastPress = System.currentTimeMillis();
					addToPositionTwo(0, -1);
				} else if (cmdTwo.getForward() == key && !fireTwo) {
					lastPress = System.currentTimeMillis();
					addToPositionTwo(0, 1);
				} else if (isButton(cmdTwo, key)) {
					if (getSelectedSprite(position2) != null)
						fireTwo = !fireTwo;
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

	public Point getPosition() {
		return position;
	}


	@Override
	public void onDebugAction(DebugAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reInit(GameWindow container) throws Exception {
//		position = (Point) MugenSystem.getInstance().getSelectInfo().getP1().getCursor().getStartcell().clone();
//		position2 = (Point) MugenSystem.getInstance().getSelectInfo().getP2().getCursor().getStartcell().clone();	
		fireOne = false;
		fireTwo = false;
	}

	@Override
	public void render() throws Exception {
		selectRender.render();
		
	}

	@Override
	public void renderDebugInfo() {
		// TODO Auto-generated method stub
		
	}
	private String getSelectedSprite(Point p) {
		int row = MugenSystem.getInstance().getSelectInfo().getRows();
		int col = MugenSystem.getInstance().getSelectInfo().getColumns();
		Characters characters = MugenSystem.getInstance().getFiles().getSelect().getCharacters();
		Iterator<String> iterCharacter = characters.getCharactersOrder().iterator();
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				
				if (iterCharacter.hasNext()) {
					String character = iterCharacter.next();
					if (p.x == r && p.y == c) {
						return character;
					}
				}
				
			}
		}
		return null;
	}
	@Override
	public void update(int delta) throws Exception {
		MugenSystem.getInstance().getSelectBackground().process();
		if (fireOne && fireTwo) {
			GameFight.clear();
			final GameFight gameFight = GameFight.getInstance();
			gameFight.setTeamOneMode(TeamMode.SINGLE);
			gameFight.setTeamTwoMode(TeamMode.SINGLE);
			
			String spr = getSelectedSprite(position);
			SpriteDef def = MugenSystem.getInstance().getFiles().getSelect().getCharacters().getCharactersMap().get(spr);
			gameFight.preloadSprite(GameFight.TEAMSIDE_ONE, "1", def, 0);

			spr = getSelectedSprite(position2);
			def = MugenSystem.getInstance().getFiles().getSelect().getCharacters().getCharactersMap().get(spr);
			gameFight.preloadSprite(GameFight.TEAMSIDE_TWO, "2", def, 0);

			gameFight.preloadStage("resource/stages/stage0.def");
			
			next = gameFight;
			
		}
		
	}

	
	private SelectRender selectRender;
	
	private String nextMode;
}
