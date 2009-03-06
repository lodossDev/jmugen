package org.lee.mugen.core.gameSelect;

import java.awt.Point;
import java.awt.event.KeyEvent;

import org.lee.mugen.fight.system.MugenSystem;
import org.lee.mugen.input.CmdProcDispatcher;
import org.lee.mugen.input.ISpriteCmdProcess;
import org.lee.mugen.renderer.GameWindow.MugenKeyListener;

public class SelectionController implements MugenKeyListener {
	private static final int P1_SELECTED = 1;
	private static final int P2_SELECTED = 2;
	private static final int P3_SELECTED = 4;
	private static final int P4_SELECTED = 8;
	private static final int STAGE_SELECTED = 16;

	private static int shareState = 0;
	private static int shareIndexOfStage = 0;
	private String id;
	private final Point position = new Point();
	private GameSelect gs;
	
	
	public SelectionController(GameSelect gs, String id, int x, int y) {
		this.id = id;
		position.x = x;
		position.y = y;
		this.gs = gs;
	}
	public SelectionController(GameSelect gs, String id,
			Point startcell) {
		this(gs, id, startcell.x, startcell.y);
	}
	//
	private long lastPress;

	private static Object monitor = new Object();
	public static int getShareIndexOfStage() {
		synchronized (monitor) {
			return shareIndexOfStage;
		}
		
	}
	public static boolean isAllSelectionDone(int playerCount) {
		synchronized (monitor) {
			boolean isDone = false;
			if (!isStageSelected())
				return isDone;
			isDone = true;
			for (int i = 0; i < playerCount; i++) {
				isDone = isDone && isCharSelected(i + 1);
			}
			return isDone;
		}
	};
	public static boolean isAllPlayersSelectedButNoStage(int playerCount) {
		synchronized (monitor) {
			boolean isDone = true;
			for (int i = 0; i < playerCount; i++) {
				isDone = isDone && isCharSelected(i + 1);
			}
			return isDone && !isStageSelected();
		}
	}
	private static int incIndexOfStage() {
		synchronized (monitor) {
			shareIndexOfStage++;
			if (shareIndexOfStage > MugenSystem.getInstance().getFiles().getSelect().getExtraStages().getStages().size() - 1)
				shareIndexOfStage = 0;
			return shareIndexOfStage;
		}
	}
	private static int decIndexOfStage() {
		synchronized (monitor) {
			shareIndexOfStage--;
			if (shareIndexOfStage < 0)
				shareIndexOfStage = MugenSystem.getInstance().getFiles().getSelect().getExtraStages().getStages().size() - 1;
			return shareIndexOfStage;
		}
	}
	
	public static void initShareVaraiable() {
		synchronized (monitor) {
			shareState = 0;
		}
	}
	private void setSelected() {
		synchronized (monitor) {
			shareState = shareState | getCharacterBit();
		}
	}
	private void setUnSelected() {
		synchronized (monitor) {
			shareState = shareState & ~getCharacterBit();
		}	
	}
	private static void setStageSelected() {
		synchronized (monitor) {
			shareState = shareState | STAGE_SELECTED;
		}
	}
	private static void setStageUnSelected() {
		synchronized (monitor) {
			shareState = shareState | STAGE_SELECTED;
		}
	}
	
	private static boolean isCharSelected(int id) {
		synchronized (monitor) {
			return (shareState & getCharacterBit(id)) == getCharacterBit(id);
		}
	}
	

	public boolean isCharSelected() {
		synchronized (monitor) {
			return (shareState & getCharacterBit()) == getCharacterBit();
		}
	}
	public static boolean isStageSelected() {
		synchronized (monitor) {
			return (shareState & STAGE_SELECTED) == STAGE_SELECTED;
		}
	}
	
	private int getCharacterBit() {
		int id = Integer.parseInt(this.id);
		return getCharacterBit(id);
	}
	private static int getCharacterBit(int id) {
		switch (id) {
		case 1:
			return P1_SELECTED;
		case 2:
			return P2_SELECTED;
		case 3:
			return P3_SELECTED;
		case 4:
			return P4_SELECTED;
		}
		throw new IllegalStateException();
	}
	
	private void addToPosition(int x, int y) {
		int row = MugenSystem.getInstance().getSelectInfo().getRows();
		int col = MugenSystem.getInstance().getSelectInfo().getColumns();
		if (position.x + x < row && position.x + x >= 0)
			position.x += x;
		if (position.y + y < col && position.y + y >= 0)
			position.y += y;
		
	}
	
	@Override
	public void action(int key, boolean isPress) {
		long now = System.currentTimeMillis();
		
		if (!isPress || now - lastPress < 100)
			return;
		if (KeyEvent.VK_ESCAPE == key) {
			gs.setBackToMenu();
			
		}
		lastPress = System.currentTimeMillis();
		CmdProcDispatcher cmd = CmdProcDispatcher.getSpriteDispatcherMap().get(id);
		if (!isCharSelected()) {
			if (cmd.getDown() == key) {
				addToPosition(1, 0);
			} else if (cmd.getUp() == key) {
				addToPosition(-1, 0);
			} else if (cmd.getBack() == key) {
				addToPosition(0, -1);
			} else if (cmd.getForward() == key) {
				addToPosition(0, 1);
			} else if (isButtonConfirm(cmd, key)) {
				setSelected();
			}
		} else {
			if (!isStageSelected()) {
				if (cmd.getBack() == key) {
					if (getShareIndexOfStage() != decIndexOfStage())
						gs.setStageChanged();
				} else if (cmd.getForward() == key) {
					if (getShareIndexOfStage() != incIndexOfStage())
						gs.setStageChanged();
				}
				////
				if (isButtonConfirm(cmd, key)) {
					setStageSelected();
				} else if (isButtonCancel(cmd, key)) {
					setUnSelected();
				}
			} else {
				if (isButtonCancel(cmd, key)) {
					setStageUnSelected();
				}
			}
		}
	}
	
	private boolean isButtonCancel(CmdProcDispatcher cmd, int key) {
		boolean result = 
			cmd.getX() == key
			|| cmd.getY() == key
			|| cmd.getZ() == key
			|| cmd.getXyz() == key;
		return result;
	}
	
	private boolean isButtonConfirm(CmdProcDispatcher cmd, int key) {
		boolean result = 
			cmd.getA() == key
			|| cmd.getB() == key
			|| cmd.getB() == key
			|| cmd.getAbc() == key;
		return result;
	}
	public String getId() {
		return id;
	}
	public Point getPosition() {
		return position;
	}

	
	
	
}
