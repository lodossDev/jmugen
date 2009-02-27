package org.lee.mugen.core;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lee.mugen.core.renderer.game.StageBackgroundRender;
import org.lee.mugen.core.renderer.game.select.SelectRender;
import org.lee.mugen.fight.select.Characters;
import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.fight.system.elem.StageDisplay;
import org.lee.mugen.input.CmdProcDispatcher;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.Renderable;
import org.lee.mugen.renderer.jogl.JoglMugenDrawer;
import org.lee.mugen.sprite.character.SpriteDef;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Teammode.TeamMode;
import org.lee.mugen.stage.Stage;

public class GameSelect implements Game {
	public Stage getStage() {
		return stage;
	}
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
	private boolean fireSpriteOne;
	private boolean fireSpriteTwo;
	
	private boolean fireStage;
	
	public boolean isFireStage() {
		return fireStage;
	}
	public boolean isFireOne() {
		return fireSpriteOne;
	}

	public boolean isFireTwo() {
		return fireSpriteTwo;
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
	public boolean backToMenu = false;
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
				
				if (!isPress || now - lastPress < 100)
					return;
				if (KeyEvent.VK_ESCAPE == key) {
					backToMenu = true;
				}
				CmdProcDispatcher cmdOne = CmdProcDispatcher.getSpriteDispatcherMap().get("1");
				if (cmdOne.getDown() == key && !fireSpriteOne) {
					lastPress = System.currentTimeMillis();
					addToPositionOne(1, 0);
				
				} else if (cmdOne.getUp() == key && !fireSpriteOne) {
					lastPress = System.currentTimeMillis();
					addToPositionOne(-1, 0);
				} else if (cmdOne.getBack() == key && !fireSpriteOne) {
					lastPress = System.currentTimeMillis();
					addToPositionOne(0, -1);
				} else if (cmdOne.getForward() == key && !fireSpriteOne) {
					lastPress = System.currentTimeMillis();
					addToPositionOne(0, 1);
				}
				////////
				else if (cmdOne.getBack() == key && fireSpriteOne && !fireStage && !processStage) {
					lastPress = System.currentTimeMillis();
					indexOfStage--;
					if (indexOfStage < 0)
						indexOfStage = MugenSystem.getInstance().getFiles().getSelect().getExtraStages().getStages().size() - 1;
					changeStage = true;
				} else if (cmdOne.getForward() == key && fireSpriteOne && !fireStage && !processStage) {
					lastPress = System.currentTimeMillis();
					indexOfStage++;
					if (indexOfStage > MugenSystem.getInstance().getFiles().getSelect().getExtraStages().getStages().size() - 1)
						indexOfStage = 0;
					changeStage = true;
				} else if (isButtonConfirm(cmdOne, key) && !processStage && fireSpriteOne) {
					lastPress = System.currentTimeMillis();
					fireStage = true;
				} else if (isButtonCancel(cmdOne, key) && !processStage && fireSpriteOne && fireStage) {
					lastPress = System.currentTimeMillis();
					fireStage = false;
				} else if (isButtonConfirm(cmdOne, key) && !fireSpriteOne) {
					if (getSelectedSprite(position) != null)
						fireSpriteOne = true;
				} else if (isButtonCancel(cmdOne, key) && fireSpriteOne) {
					if (getSelectedSprite(position) != null)
						fireSpriteOne = false;
				}
				
				
				//////////////////////////////////////////////////
				
				CmdProcDispatcher cmdTwo = CmdProcDispatcher.getSpriteDispatcherMap().get("2");
				if (cmdTwo.getDown() == key && !fireSpriteTwo) {
					lastPress = System.currentTimeMillis();
					addToPositionTwo(1, 0);
				
				} else if (cmdTwo.getUp() == key && !fireSpriteTwo) {
					lastPress = System.currentTimeMillis();
					addToPositionTwo(-1, 0);
				} else if (cmdTwo.getBack() == key && !fireSpriteTwo) {
					lastPress = System.currentTimeMillis();
					addToPositionTwo(0, -1);
				} else if (cmdTwo.getForward() == key && !fireSpriteTwo) {
					lastPress = System.currentTimeMillis();
					addToPositionTwo(0, 1);
				} else if (isButtonConfirm(cmdTwo, key) && !fireSpriteTwo) {
					if (getSelectedSprite(position2) != null)
						fireSpriteTwo = true;
				} else if (isButtonCancel(cmdTwo, key) & !fireStage) {
					if (getSelectedSprite(position2) != null)
						fireSpriteTwo = false;
				}
					
				else if (cmdTwo.getBack() == key && fireSpriteTwo && !fireStage && !processStage) {
					lastPress = System.currentTimeMillis();
					indexOfStage--;
					if (indexOfStage < 0)
						indexOfStage = MugenSystem.getInstance().getFiles().getSelect().getExtraStages().getStages().size() - 1;
					changeStage = true;
				} else if (cmdTwo.getForward() == key && fireSpriteTwo && !fireStage && !processStage) {
					lastPress = System.currentTimeMillis();
					indexOfStage++;
					if (indexOfStage > MugenSystem.getInstance().getFiles().getSelect().getExtraStages().getStages().size() - 1)
						indexOfStage = 0;
					changeStage = true;
				} else if (isButtonConfirm(cmdTwo, key) && !processStage && fireSpriteTwo) {
					lastPress = System.currentTimeMillis();
					fireStage = true;
				} else if (isButtonCancel(cmdTwo, key) && !processStage && fireSpriteTwo && fireStage) {
					lastPress = System.currentTimeMillis();
					fireStage = false;
				}  else if (isButtonConfirm(cmdTwo, key) && !fireSpriteTwo) {
					if (getSelectedSprite(position2) != null)
						fireSpriteTwo = true;
				} else if (isButtonCancel(cmdTwo, key)) {
					if (getSelectedSprite(position2) != null)
						fireSpriteTwo = false;
				}
				
			}

			private boolean isButtonCancel(CmdProcDispatcher cmdOne, int key) {
				boolean result = 
					cmdOne.getX() == key
					|| cmdOne.getY() == key
					|| cmdOne.getZ() == key
					|| cmdOne.getXyz() == key;
				return result;
			}
			
			private boolean isButtonConfirm(CmdProcDispatcher cmdOne, int key) {
				boolean result = 
					cmdOne.getA() == key
					|| cmdOne.getB() == key
					|| cmdOne.getB() == key
					|| cmdOne.getAbc() == key;
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
	Stage stage = null;
	StageBackgroundRender stageBackgroundRender;
	int indexOfStage;
	boolean changeStage;
	@Override
	public void reInit(GameWindow container) throws Exception {
		position = (Point) MugenSystem.getInstance().getSelectInfo().getP1().getCursor().getStartcell().clone();
		position2 = (Point) MugenSystem.getInstance().getSelectInfo().getP2().getCursor().getStartcell().clone();	
		fireSpriteOne = false;
		fireSpriteTwo = false;
		if (stage != null)
			stage.free();
		stage = null;
		stageBackgroundRender = null;
		indexOfStage = 0;
		changeStage = true;
		fireStage = false;
		backToMenu = false;
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
	boolean processStage;
	AtomicBoolean threadStart = new AtomicBoolean();
	class ChangeStageThread extends Thread {
		@Override
		public void run() {
			try {
				String path = "resource/" + MugenSystem.getInstance().getFiles().getSelect().getExtraStages().getStages().get(indexOfStage);
				Stage newStage = new Stage(path);
				oldStageToDelete = stage;
				stage = newStage;
				StageDisplay stageDisplay = MugenSystem.getInstance().getSelectInfo().getStagedisplay();
				if (stageDisplay != null) {
					StageBackgroundRender newStageBackgroundRender = new StageBackgroundRender(stage);
					JoglMugenDrawer.createImageToTextPreparer();
					stageBackgroundRender = newStageBackgroundRender;
					stage.getCamera().setForcePos(true);
					
				}
				changeStage = false;
				processStage = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				threadStart.set(false);
			}
		}
	};
	Stage oldStageToDelete;
	@Override
	public void update(int delta) throws Exception {
		if (backToMenu) {
			next = GameMenu.getInstance();
			return;
		}
		if (changeStage) {
			processStage = true;
			if (!threadStart.get()) {
				threadStart.set(true);
				new ChangeStageThread().start();
				
			}
			
		} else {
			if (oldStageToDelete != null) {
				oldStageToDelete.free();
				oldStageToDelete = null;
			}
			StageDisplay stageDisplay = MugenSystem.getInstance().getSelectInfo().getStagedisplay();
			if (stageDisplay != null) {
				stage.getCamera().setX(stageDisplay.getCamera().x);
				stage.getCamera().setY(stageDisplay.getCamera().y);
				stage.process();
			}

		}
		MugenSystem.getInstance().getSelectBackground().process();
		if (fireSpriteOne && fireSpriteTwo && fireStage && !changeStage) {
			GameFight.clear();
			final GameFight gameFight = GameFight.getInstance();
			gameFight.setTeamOneMode(TeamMode.SINGLE);
			gameFight.setTeamTwoMode(TeamMode.SINGLE);
			
			String spr = getSelectedSprite(position);
			SpriteDef def = MugenSystem.getInstance().getFiles().getSelect().getCharacters().getSpritedef(spr);
			gameFight.preloadSprite(GameFight.TEAMSIDE_ONE, "1", def, 0);

			spr = getSelectedSprite(position2);
			def = MugenSystem.getInstance().getFiles().getSelect().getCharacters().getSpritedef(spr);
			gameFight.preloadSprite(GameFight.TEAMSIDE_TWO, "2", def, 0);

			gameFight.setStage(stage);
			
			next = gameFight;
			
		}
		
	}

	
	public StageBackgroundRender getStageBackgroundRender() {
		return stageBackgroundRender;
	}
	private SelectRender selectRender;
	
	private String nextMode;
	String title = "";
	public void setTilte(String title) {
		this.title = title;		
	}
	@Override
	public void free() {
		// TODO Auto-generated method stub
		
	}
}
