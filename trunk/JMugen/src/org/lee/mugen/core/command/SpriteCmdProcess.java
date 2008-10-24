package org.lee.mugen.core.command;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.input.CmdProcDispatcher;
import org.lee.mugen.input.MugenCommands;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteCns;

import com.sun.corba.se.pept.transport.ContactInfo;

/**
 * This class process command recognition
 * TODO : remove the interface KeyAdapter beause it's not genereic
 * In the java caller if the caller use AWT events, it has to use wrapper
 * @author Dr Wong
 *
 */
public class SpriteCmdProcess implements org.lee.mugen.input.ISpriteCmdProcess {
	private List<String> spriteIds = new ArrayList<String>();
	private CmdProcDispatcher dispatcher;
	private List<MugenCommands> cmds;
	private StateMachine stateMachine = null;
	
	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ISpriteCmdProcess#addSprite(java.lang.String)
	 */
	public void addSprite(String spriteId) {
		spriteIds.add(spriteId);
	}
	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ISpriteCmdProcess#remove(java.lang.String)
	 */
	public void remove(String spriteId) {
		spriteIds.remove(spriteId);
	}
	public SpriteCmdProcess(CmdProcDispatcher dispatcher) {
		this.dispatcher = dispatcher;
		stateMachine = StateMachine.getInstance();
	}
	
	
	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ISpriteCmdProcess#process()
	 */
	public void process() {
		for (String spriteId: spriteIds) {
			Sprite sprite = StateMachine.getInstance().getSpriteInstance(spriteId);
			
			boolean find = false;
			SpriteCns sprInfo = sprite.getInfo();
			List<MugenCommands> cmds = sprite.getCmds();

			for (MugenCommands mc : cmds) {
				if (dispatcher == null)
					continue;
				if (mc.find(dispatcher, stateMachine.getGameState().getGameTime(), sprInfo.isFlip())) {
					sprInfo.addCommand(mc.getCommandName(), (int) mc.getBufferTime());
					find = true;
				}
			}
		}

	}


	public void keyPressed(KeyEvent e) {
		for (String sid: spriteIds) {
			Sprite sprite = StateMachine.getInstance().getSpriteInstance(sid);
			dispatcher.press(e.getKeyCode(), stateMachine.getGameState().getGameTime(), sprite.isFlip());
		}
		
	}


	public void keyReleased(KeyEvent e) {
		for (String sid: spriteIds) {
			Sprite sprite = StateMachine.getInstance().getSpriteInstance(sid);
			dispatcher.release(e.getKeyCode(), stateMachine.getGameState().getGameTime(), sprite.isFlip());
		}
		
	}

	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ISpriteCmdProcess#keyPressed(int)
	 */
	public void keyPressed(int keycode) {
		for (String sid: spriteIds) {

			Sprite sprite = StateMachine.getInstance().getSpriteInstance(sid);
			if (dispatcher == null)
				continue;
				
			dispatcher.press(keycode, stateMachine.getGameState().getGameTime(), sprite.isFlip());
		}
		
	}


	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ISpriteCmdProcess#keyReleased(int)
	 */
	public void keyReleased(int keycode) {
		for (String sid: spriteIds) {
			Sprite sprite = StateMachine.getInstance().getSpriteInstance(sid);
			if (dispatcher == null)
				continue;
			dispatcher.release(keycode, stateMachine.getGameState().getGameTime(), sprite.isFlip());
		}
		
	}


	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ISpriteCmdProcess#getDispatcher()
	 */
	public CmdProcDispatcher getDispatcher() {
		return dispatcher;
	}
	public int[] getKeys() {
		return dispatcher.getKeys();
	}



}
