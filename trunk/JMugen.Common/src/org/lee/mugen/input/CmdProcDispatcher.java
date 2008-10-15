package org.lee.mugen.input;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lee.mugen.input.KeyLockCommand.State;

/**
 * A Command dispatcher to sprite(s) that it hold
 * 
 * TODO : the static bloc is tempory and when configuration is done, 
 * insert keys controlers has to be done by a method
 * 
 * @author Dr Wong
 *
 */
public class CmdProcDispatcher {
	private static Map<String, CmdProcDispatcher> spriteDispatcherMap = new HashMap<String, CmdProcDispatcher>();
	

	// TODO : just here to Test => Make a method that read this in a config file
	static {
		CmdProcDispatcher cd = null;

		cd = new CmdProcDispatcher(
				KeyEvent.VK_Z
				,KeyEvent.VK_S
				,KeyEvent.VK_Q
				,KeyEvent.VK_D
	
				,KeyEvent.VK_U
				,KeyEvent.VK_I
				,KeyEvent.VK_O
				,KeyEvent.VK_J
				,KeyEvent.VK_K
				,KeyEvent.VK_L
				,KeyEvent.VK_P
				,KeyEvent.VK_M
		);
//		if (StateMachine.getInstance().getRenderType() == RenderType.LWJGL)
//		cd = new CmdProcDispatcher(
//				Keyboard.KEY_Z
//				,Keyboard.KEY_S
//				,Keyboard.KEY_Q
//				,Keyboard.KEY_D
//	
//				,Keyboard.KEY_U
//				,Keyboard.KEY_I
//				,Keyboard.KEY_O
//				,Keyboard.KEY_J
//				,Keyboard.KEY_K
//				,Keyboard.KEY_L
//				
//				,Keyboard.KEY_P
//				,Keyboard.KEY_M
//		);

		spriteDispatcherMap.put("1", cd);
		cd = new CmdProcDispatcher(
				KeyEvent.VK_UP
				,KeyEvent.VK_DOWN
				,KeyEvent.VK_LEFT
				,KeyEvent.VK_RIGHT
	
				,KeyEvent.VK_NUMPAD4
				,KeyEvent.VK_NUMPAD5
				,KeyEvent.VK_NUMPAD6
				,KeyEvent.VK_NUMPAD1
				,KeyEvent.VK_NUMPAD2
				,KeyEvent.VK_NUMPAD3
				
				,KeyEvent.VK_PLUS
				,KeyEvent.VK_ENTER
		);
//		if (StateMachine.getInstance().getRenderType() == RenderType.LWJGL)
//		cd = new CmdProcDispatcher(
//				Keyboard.KEY_UP
//				,Keyboard.KEY_DOWN
//				,Keyboard.KEY_LEFT
//				,Keyboard.KEY_RIGHT
//	
//				,Keyboard.KEY_NUMPAD4
//				,Keyboard.KEY_NUMPAD5
//				,Keyboard.KEY_NUMPAD6
//				,Keyboard.KEY_NUMPAD1
//				,Keyboard.KEY_NUMPAD2
//				,Keyboard.KEY_NUMPAD3
//				
//				,Keyboard.KEY_NUMPAD7
//				,Keyboard.KEY_NUMPAD8
//		);
		spriteDispatcherMap.put("2", cd);

	}
		

	
	public static final int bufferTime = 50;
	private EnumMap<Key, KeyLockCommand> keyLockFactory;
	
	private Map<Integer, Key> keyCodeToKeyMap;// = new HashMap<Integer, Key>();

	private List<SingleCmdProcessor> mugenKeyEvents = Collections.synchronizedList(new LinkedList<SingleCmdProcessor>());

//	private Sprite sprite;
	static final Map<Key, Key> inverseMap = new HashMap<Key, Key>();
	static { 
		inverseMap.put(Key.B, Key.F);
		inverseMap.put(Key.F, Key.B);
		
		inverseMap.put(Key.DB, Key.DF);
		inverseMap.put(Key.UB, Key.UF);
		inverseMap.put(Key.DF, Key.DB);
		inverseMap.put(Key.UF, Key.UB);
	}

	final int up;
	final int down; 
	final int back; 
	final int forward; 
	final int a;
	final int b;
	final int c;
	final int x;
	final int y; 
	final int z;
	final int abc; 
	final int xyz;
	
	
	public CmdProcDispatcher(
			final int up, 
			final int down, 
			final int back, 
			final int forward, 
			int a, int b, int c, int x, int y, int z,
			int abc, int xyz) {
//		this.sprite = pSprite;

		this.up = up;
		this.down = down; 
		this.back = back; 
		this.forward = forward; 
		this.a = a;
		this.b = b;
		this.c = c;
		this.x = x;
		this.y = y; 
		this.z = z;
		this.abc = abc; 
		this.xyz = xyz;
		
		keyCodeToKeyMap = new HashMap<Integer, Key>();
//		{
//			@Override
//			public Key get(Object pkey) {
//				if (pkey == null)
//					return null;
//				int key = new Integer(pkey.toString());
//				if (sprite.getInfo().isFlip()) {
//					if (key == back)
//						key = forward;
//					else if (key == forward)
//						key = back;
//					return super.get(key);
//				} else {
//					return super.get(key);
//					
//				}
//			}
//		};
		keyCodeToKeyMap.put(up, Key.U);
		keyCodeToKeyMap.put(down, Key.D);
		keyCodeToKeyMap.put(back, Key.B);
		keyCodeToKeyMap.put(forward, Key.F);
		

		
		keyLockFactory = 
			new EnumMap<Key, KeyLockCommand>(Key.class);
//		{
//
//				@Override
//				public KeyLockCommand get(Object key) {
//					if (sprite.getInfo().isFlip()) {
//						Key k = inverseMap.get(key);
//						if (k == null)
//							return super.get(key);
//						return super.get(k);
//					} else {
//						return super.get(key);
//						
//					}
//				}
//			
//		};

		keyCodeToKeyMap.put(a, Key.a);
		keyCodeToKeyMap.put(b, Key.b);
		keyCodeToKeyMap.put(c, Key.c);
		keyCodeToKeyMap.put(x, Key.x);
		keyCodeToKeyMap.put(y, Key.y);
		keyCodeToKeyMap.put(z, Key.z);
		

		keyLockFactory.put(Key.U, new KeyLockCommand(Key.U));
		keyLockFactory.put(Key.D, new KeyLockCommand(Key.D));
		keyLockFactory.put(Key.B, new KeyLockCommand(Key.B));
		keyLockFactory.put(Key.F, new KeyLockCommand(Key.F));
		
		keyLockFactory.put(Key.DB, new KeyLockCommand(Key.DB));
		keyLockFactory.put(Key.UB, new KeyLockCommand(Key.UB));
		keyLockFactory.put(Key.DF, new KeyLockCommand(Key.DF));
		keyLockFactory.put(Key.UF, new KeyLockCommand(Key.UF));

		
		keyLockFactory.put(Key.a, new KeyLockCommand(Key.a));
		keyLockFactory.put(Key.b, new KeyLockCommand(Key.b));
		keyLockFactory.put(Key.c, new KeyLockCommand(Key.c));
		keyLockFactory.put(Key.x, new KeyLockCommand(Key.x));
		keyLockFactory.put(Key.y, new KeyLockCommand(Key.y));
		keyLockFactory.put(Key.z, new KeyLockCommand(Key.z));

	}

	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ICmdProcDispatcher#setKey(int, org.lee.mugen.core.command.Key)
	 */
	public void setKey(int keyCode, Key key) {
		keyCodeToKeyMap.put(keyCode, key);
	}

	public boolean diseable;
	
	
	
	private KeyLockCommand getDirKeyLocked() {
		KeyLockCommand result = null;
		if ((result = keyLockFactory.get(Key.B)).isLock()) {
			return result;
		}
		if ((result = keyLockFactory.get(Key.DB)).isLock()) {
			return result;
		}
		if ((result = keyLockFactory.get(Key.D)).isLock()) {
			return result;
		}
		if ((result = keyLockFactory.get(Key.DF)).isLock()) {
			return result;
		}
		if ((result = keyLockFactory.get(Key.F)).isLock()) {
			return result;
		}
		if ((result = keyLockFactory.get(Key.UF)).isLock()) {
			return result;
		}
		if ((result = keyLockFactory.get(Key.U)).isLock()) {
			return result;
		}
		if ((result = keyLockFactory.get(Key.UB)).isLock()) {
			return result;
		}
		return null;
	}
	
	private List<KeyLockCommand> getDirKeyHidden() {
		List<KeyLockCommand> list = new ArrayList<KeyLockCommand>();
		KeyLockCommand result = null;
		if ((result = keyLockFactory.get(Key.B)).isHidden()) {
			list.add(result);
		}
		if ((result = keyLockFactory.get(Key.DB)).isHidden()) {
			list.add(result);
		}
		if ((result = keyLockFactory.get(Key.D)).isHidden()) {
			list.add(result);
		}
		if ((result = keyLockFactory.get(Key.DF)).isHidden()) {
			list.add(result);
		}
		if ((result = keyLockFactory.get(Key.F)).isHidden()) {
			list.add(result);
		}
		if ((result = keyLockFactory.get(Key.UF)).isHidden()) {
			list.add(result);
		}
		if ((result = keyLockFactory.get(Key.U)).isHidden()) {
			list.add(result);
		}
		if ((result = keyLockFactory.get(Key.UB)).isHidden()) {
			list.add(result);
		}
		return list;
	}
	private boolean isCombinedDirKey(Key key) {
		return key != Key.U && key != Key.D && key != Key.B && key != Key.F;
	}
	
	
	private void releaseDir(int keyCode, long tick, boolean isFlip) {
		if (diseable)
			return;
//		if (isFlip) {
//			if (keyCode == back)
//				keyCode = forward;
//			else if (keyCode == forward)
//				keyCode = back;
//		}

		Key key = keyCodeToKeyMap.get(keyCode);
		releaseDir(key, tick);
	}
	
	private void releaseDir(Key key, long tick) {
		if (diseable)
			return;
		if (key == null)
			return;
		if ((key.bit & (Key.U.bit | Key.D.bit | Key.B.bit | Key.F.bit)) != key.bit)
			return;

		KeyLockCommand newKlc = keyLockFactory.get(key);
		if (!newKlc.isLock() && !newKlc.isHidden())
			return;
		KeyLockCommand klc = getDirKeyLocked();
		if (klc == null)
			return;
		if (isCombinedDirKey(klc.getKey())) {
			klc.setReleasedTick(tick);
			newKlc.unHidden();
			newKlc.setReleasedTick(tick);
			
			KeyProc keyProcNew = new KeyProc(newKlc.getCmd(), newKlc.getTokenTaken());
			KeyProc keyProcKlc = new KeyProc(klc.getCmd(), klc.getTokenTaken());
			
			//
			List<KeyProc> lst = new ArrayList<KeyProc>();
			lst.add(keyProcNew);
			lst.add(keyProcKlc);
			
			for (KeyLockCommand k : getDirKeyHidden()) {
				k.unHidden();
				lst.add(new KeyProc(k.getCmd(), k.getTokenTaken()));
			}
			
			
			SingleCmdProcessor scp = new SingleCmdProcessor(tick, lst.toArray(new KeyProc[lst.size()]));
			mugenKeyEvents.add(scp);
		} else {
			newKlc.setReleasedTick(tick);
			
			KeyProc keyProcNew = new KeyProc(newKlc.getCmd(), newKlc.getTokenTaken());
			SingleCmdProcessor scp = new SingleCmdProcessor(tick, keyProcNew);
			
			mugenKeyEvents.add(scp);
		}
	}
	
	private void pressDir(Key key, long tick) {
		if (diseable)
			return;
		if (key == null)
			return;
		if ((key.bit & (Key.U.bit | Key.D.bit | Key.B.bit | Key.F.bit)) != key.bit)
			return;

		if ((key.bit & Key.U.bit) == Key.U.bit) {
			releaseDir(Key.D, tick);
		}
		if ((key.bit & Key.D.bit) == Key.D.bit) {
			releaseDir(Key.U, tick);
		}
		if ((key.bit & Key.F.bit) == Key.F.bit) {
			releaseDir(Key.B, tick);
		}
		if ((key.bit & Key.B.bit) == Key.B.bit) {
			releaseDir(Key.F, tick);
		}

		KeyLockCommand newKlc = keyLockFactory.get(key);
		if (newKlc.isLock()) {
			return;
		}
		if (newKlc.isHidden()) {
			return;
		}
		KeyLockCommand klc = getDirKeyLocked();
		if (klc == null) {
			newKlc.setPressTick(tick);
			KeyProc keyProcNew = new KeyProc(newKlc.getCmd(), newKlc.getTokenTaken());
//				KeyProc keyProcKlc = new KeyProc(klc.getCmd(), klc.getTokenTaken());
			SingleCmdProcessor scp = new SingleCmdProcessor(tick, keyProcNew);

			mugenKeyEvents.add(scp);
			return;
		} else {
			if (isCombinedDirKey(key)) {

			} else {
				Key combinedKey = null;
				int moveDirection = klc.getKey().bit | newKlc.getKey().bit;
				if (moveDirection == Key.DB.bit)
					combinedKey = Key.DB;
				else if (moveDirection == Key.UB.bit)
					combinedKey = Key.UB;
				else if (moveDirection == Key.UF.bit)
					combinedKey = Key.UF;
				else if (moveDirection == Key.DF.bit)
					combinedKey = Key.DF;
				else {
					if (isCombinedDirKey(klc.getKey()))
						return;
					newKlc.setPressTick(tick);
					KeyProc keyProcNew = new KeyProc(newKlc.getCmd(), newKlc.getTokenTaken());
					SingleCmdProcessor scp = new SingleCmdProcessor(tick, keyProcNew);

					mugenKeyEvents.add(scp);
					return;
				}
				KeyLockCommand combinedKeyLock = keyLockFactory
						.get(combinedKey);
				if (combinedKeyLock.isLock())
					return;
				newKlc.setPressTick(tick);
				newKlc.setHidden();
//					klc.setPressTick(tick);
				klc.setHidden();
				combinedKeyLock.setPressTick(tick);

				KeyProc keyProcKlc = new KeyProc(klc.getCmd(), klc.getTokenTaken());
				KeyProc keyProcNew = new KeyProc(newKlc.getCmd(), newKlc.getTokenTaken());
				KeyProc keyProcCombinedKeyLock = new KeyProc(combinedKeyLock.getCmd(), combinedKeyLock.getTokenTaken());
				SingleCmdProcessor scp = new SingleCmdProcessor(tick, keyProcKlc, keyProcNew, keyProcCombinedKeyLock);

				mugenKeyEvents.add(scp);
			}
		}
	}

	private void pressDir(int keyCode, long tick, boolean isFlip) {
		if (diseable)
			return;
//		if (isFlip) {
//			if (keyCode == back)
//				keyCode = forward;
//			else if (keyCode == forward)
//				keyCode = back;
//		}
		Key key = keyCodeToKeyMap.get(keyCode);
		pressDir(key, tick);
	}

	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ICmdProcDispatcher#press(int, long, boolean)
	 */
	public boolean press(int keyCode, long tick, boolean isFlip) {

		pressDir(keyCode, tick, isFlip);
		if (abc == keyCode) {
			press(this.a, tick, isFlip);
			press(this.b, tick, isFlip);
//			press(this.c, tick, isFlip);
			return true;
		}
		if (xyz == keyCode) {
			press(this.x, tick, isFlip);
			press(this.y, tick, isFlip);
//			press(this.z, tick, isFlip);
			return true;
		}
		Key key = keyCodeToKeyMap.get(keyCode);
		if (key == null)
			return true;


		if ((key.bit & (Key.U.bit | Key.D.bit | Key.B.bit | Key.F.bit)) != key.bit) {
			pressButton(key, tick);
		}
		return true;
	}

	
	private void pressButton(Key key, long tick) {
		if (diseable)
			return;
		
		if (key == null)
			return;
		if (!((key.bit & (Key.U.bit | Key.D.bit | Key.B.bit | Key.F.bit)) != key.bit))
			return;
		KeyLockCommand newKlc = keyLockFactory.get(key);
		if (newKlc.isLock()) {
			return;
		}
		if (newKlc.isHidden()) {
			return;
		}
		tick--;
		newKlc.setPressTick(tick);
		KeyProc keyProcNew = new KeyProc(newKlc.getCmd(), newKlc.getTokenTaken());
		SingleCmdProcessor scp = new SingleCmdProcessor(tick, keyProcNew);

		mugenKeyEvents.add(scp);
	}
	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ICmdProcDispatcher#flushBufferCmd()
	 */
	public void flushBufferCmd() {
		mugenKeyEvents.clear();
	}
	
	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ICmdProcDispatcher#release(int, long, boolean)
	 */
	public boolean release(int keyCode, long tick, boolean isFlip) {
		tick--;tick--; // du to a lack of latency ??? Java input ???
		releaseDir(keyCode, tick, isFlip);
		if (abc == keyCode) {
			release(this.a, tick, isFlip);
			release(this.b, tick, isFlip);
			release(this.c, tick, isFlip);
			return true;
		}
		if (xyz == keyCode) {
			release(this.x, tick, isFlip);
			release(this.y, tick, isFlip);
			release(this.z, tick, isFlip);
			return true;
		}
		Key key = keyCodeToKeyMap.get(keyCode);
		if (key == null)
			return true;;
		if ((key.bit & (Key.U.bit | Key.D.bit | Key.B.bit | Key.F.bit)) != key.bit) {
			releaseButton(key, tick);
		}
		return true;
	}

	private void releaseButton(Key key, long tick) {
		if (diseable)
			return;
		if (key == null)
			return;
		if (!((key.bit & (Key.U.bit | Key.D.bit | Key.B.bit | Key.F.bit)) != key.bit))
			return;

		KeyLockCommand newKlc = keyLockFactory.get(key);
		if (!newKlc.isLock() && !newKlc.isHidden())
			return;
		newKlc.setReleasedTick(tick);
		
		KeyProc keyProcNew = new KeyProc(newKlc.getCmd(), newKlc.getTokenTaken());
		SingleCmdProcessor scp = new SingleCmdProcessor(tick, keyProcNew);
		
		mugenKeyEvents.add(scp);
	}
	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ICmdProcDispatcher#getSequence(long)
	 */
	public SingleCmdProcessor[] getSequence(long gameTime, long timeBufferToSearch) {
		long time = gameTime - timeBufferToSearch;
		List<SingleCmdProcessor> result = new LinkedList<SingleCmdProcessor>();
		List<KeyLockCommand> klcs = new LinkedList<KeyLockCommand>();

		for (KeyLockCommand klc : keyLockFactory.values()) {
			if (klc.isHold(time) && (klc.isLock() || klc.isHidden())) {
				klcs.add(klc);
			}
		}
		Collections.sort(klcs, new Comparator<KeyLockCommand>() {
			public int compare(KeyLockCommand o1, KeyLockCommand o2) {
				return (int) (o1.getPressTick() - o2.getPressTick());
			}
		});

		for (KeyLockCommand klc : klcs) {
			AbstractCommand sc = new PressCommand(klc.getKey().bit, klc
					.getPressTick());
			KeyProc kp = new KeyProc(sc, klc.getTokenTaken());
			result.add(new SingleCmdProcessor(klc
					.getPressTick(), kp));
		}

//		for (Iterator<SingleCmdProcessor> iter = mugenKeyEvents.iterator(); iter.hasNext();) {
		List<SingleCmdProcessor> remove = new ArrayList<SingleCmdProcessor>();
		for (SingleCmdProcessor sc: mugenKeyEvents.toArray(new SingleCmdProcessor[0])) {
//			SingleCmdProcessor sc = iter.next();
			if (sc.getTick() >= time) {
				result.add(sc);
			} 
			if (sc.getTick() < gameTime - bufferTime) {
//				iter.remove();
				remove.add(sc);
			}
		}
		mugenKeyEvents.removeAll(remove);
		return result.toArray(new SingleCmdProcessor[result.size()]);
	}
	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ICmdProcDispatcher#invert()
	 */
	public void invert(long gameTime) {
		Map<Key, KeyLockCommand> keyToRevertMap = new HashMap<Key, KeyLockCommand>();
		long time = gameTime;
		
		for (Key key: keyLockFactory.keySet()) {
			KeyLockCommand klc = keyLockFactory.get(key);

			if ((klc.isHidden() || klc.isLock()) && 
					(((key.bit & Key.F.bit) == Key.F.bit) || ((key.bit & Key.B.bit) == Key.B.bit))) {
				Key inverse = inverseMap.get(key);
				
				KeyLockCommand newKlc = new KeyLockCommand(inverse);
				newKlc.setPressTick(time);
				if (klc.isHidden())
					newKlc.setHidden();
				keyToRevertMap.put(inverse, newKlc);
				klc.setTokenTaken(State.NONE);
			}
		}
		
		for (Key key: keyToRevertMap.keySet()) {
			KeyLockCommand klc = keyToRevertMap.get(key);
			keyLockFactory.put(key, klc);
			
			AbstractCommand sc = klc.getCmd();
			KeyProc kp = new KeyProc(sc, klc.getTokenTaken());
			mugenKeyEvents.add(new SingleCmdProcessor(time, kp));
		}
	}
	/* (non-Javadoc)
	 * @see org.lee.mugen.core.command.ICmdProcDispatcher#getKeys()
	 */
	public int[] getKeys() {
		
		return new int[] {
				this.up,
				this.down,
				this.back,
				this.forward,
				this.a,
				this.b,
				this.c,
				this.x,
				this.y,
				this.z,
				this.abc,
				this.xyz

		};
	}
	public static Map<String, CmdProcDispatcher> getSpriteDispatcherMap() {
		return spriteDispatcherMap;
	}
}
