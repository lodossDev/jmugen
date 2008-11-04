package org.lee.mugen.fight.section;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.core.GameState.WinType;
import org.lee.mugen.fight.section.elem.SimpleElement;
import org.lee.mugen.fight.section.elem.Start;
import org.lee.mugen.fight.section.elem.Type;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Roundstate;

public class Round extends SimpleElement implements Section, Cloneable {
	public static class Match implements Cloneable {
		int wins;
		int maxdrawgames;
		public int getWins() {
			return wins;
		}
		public void setWins(int wins) {
			this.wins = wins;
		}
		public int getMaxdrawgames() {
			return maxdrawgames;
		}
		public void setMaxdrawgames(int maxdrawgames) {
			this.maxdrawgames = maxdrawgames;
		}
	}
	public static class Sub implements Section, Cloneable {
		int time;
		Type _default;
		List<Type> round = new LinkedList<Type>();
		int sndtime;
		
		public int getTime() {
			return time;
		}
		public void setTime(int time) {
			this.time = time;
		}
		public Type getDefault() {
			return _default;
		}
		public void setDefault(Type default1) {
			_default = default1;
		}
		public List<Type> getRound() {
			return round;
		}
		public void setRound(List<Type> round) {
			this.round = round;
		}
		public int getSndtime() {
			return sndtime;
		}
		public void setSndtime(int sndtime) {
			this.sndtime = sndtime;
		}
		@Override
		public void parse(String name, String value) {
			if (name.startsWith("default.")) {
				if (_default == null) {
					_default = new Type();
				}
				_default.setType(Type.getNext(name), _default, value);
				_default.parse(Type.getNext(name), value);
			} else if (name.equals("time")) {
				time = Integer.parseInt(value);
			} 
			
		}
	}
	public static class TimeTest {
		int time;

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}
	}
	
	public static class OverTime extends TimeTest {
		int waittime;
		int hittime;
		int wintime;
		public int getWaittime() {
			return waittime;
		}
		public void setWaittime(int waittime) {
			this.waittime = waittime;
		}
		public int getHittime() {
			return hittime;
		}
		public void setHittime(int hittime) {
			this.hittime = hittime;
		}
		public int getWintime() {
			return wintime;
		}
		public void setWintime(int wintime) {
			this.wintime = wintime;
		}
		public void parse(String name, String value) {
			if (name.equals("waittime")) {
				waittime = Integer.parseInt(value);
			} else if (name.equals("hittime")) {
				hittime = Integer.parseInt(value);
				
			} else if (name.equals("wintime")) {
				wintime = Integer.parseInt(value);
				
			} else if (name.equals("time")) {
				time = Integer.parseInt(value);
				
			}
			
		}

	}
	
	
	Match match = new Match();
	Start start = new Start();
	Point pos = new Point();
	Sub round = new Sub();
	Map<Integer, Type> rounds = new HashMap<Integer, Type>();
	Type fight;
	TimeTest ctrl = new TimeTest();
	Type KO;
	Type DKO;
	Type TO;
	TimeTest slow = new TimeTest();
	OverTime over = new OverTime();
	Type win;
	Type win2;
	Type draw;
	

	
	public void parse(String name, String value) {
		super.parse(name, value);
		if (name.equals("match.wins")) {
			match.setWins(Integer.parseInt(value));
		} else if (name.equals("match.maxdrawgames")) {
			match.setMaxdrawgames(Integer.parseInt(value));
		} else if (name.equals("start.waittime")) {
			start.setTime(Integer.parseInt(value));
		} else if (name.startsWith("round.")) {
			round.parse(Type.getNext(name), value);
		} else if (Pattern.matches("round[0-9]+\\.", name)) {
			String sNum = name.substring(5, name.indexOf("."));
			int num = 0;
			if (sNum.length() > 0) {
				num = Integer.parseInt(sNum);
			}
			Type elem = rounds.get(num);
			if (elem == null) {
				elem = new Type();
				rounds.put(num, elem);
			}
			elem.setType(Type.getNext(name), elem, value);
			elem.parse(Type.getNext(name), value);
			
		} else if (name.equals("ctrl.time")) {
			ctrl.setTime(Integer.parseInt(value));
		} else if (name.equals("slow.time")) {
			slow.setTime(Integer.parseInt(value));
		} else if (name.startsWith("over.")) {
			over.parse(Type.getNext(name), value);
		} else if (name.startsWith("fight.")) {
			if (fight == null) {
				fight = new Type();
			}
			fight.setType(Type.getNext(name), fight, value);
			fight.parse(Type.getNext(name), value);
		} else if (name.startsWith("ko.")) {
			if (KO == null) {
				KO = new Type();
			}
			KO.setType(Type.getNext(name), KO, value);
			KO.parse(Type.getNext(name), value);
		} else if (name.startsWith("dko.")) {
			if (DKO == null) {
				DKO = new Type();
			}
			DKO.setType(Type.getNext(name), DKO, value);
			DKO.parse(Type.getNext(name), value);
		} else if (name.startsWith("to.")) {
			if (TO == null) {
				TO = new Type();
			}
			TO.setType(Type.getNext(name), TO, value);
			TO.parse(Type.getNext(name), value);
		} else if (name.startsWith("win.")) {
			if (win == null) {
				win = new Type();
			}
			win.setType(Type.getNext(name), win, value);
			win.parse(Type.getNext(name), value);
		} else if (name.startsWith("win2.")) {
			if (win2 == null) {
				win2 = new Type();
			}
			win2.setType(Type.getNext(name), win2, value);
			win2.parse(Type.getNext(name), value);
		} else if (name.startsWith("draw.")) {
			if (draw == null) {
				draw = new Type();
			}
			draw.setType(Type.getNext(name), draw, value);
			draw.parse(Type.getNext(name), value);
		}
	}
	
	
	
	public Match getMatch() {
		return match;
	}
	public void setMatch(Match match) {
		this.match = match;
	}
	public Start getStart() {
		return start;
	}
	public void setStart(Start start) {
		this.start = start;
	}
	public Point getPos() {
		return pos;
	}
	public void setPos(Point pos) {
		this.pos = pos;
	}
	public Sub getRound() {
		return round;
	}
	public void setRound(Sub round) {
		this.round = round;
	}
	public Type getFight() {
		return fight;
	}
	public void setFight(Type fight) {
		this.fight = fight;
	}
	public TimeTest getCtrl() {
		return ctrl;
	}
	public void setCtrl(TimeTest ctrl) {
		this.ctrl = ctrl;
	}
	public Type getKO() {
		return KO;
	}
	public void setKO(Type ko) {
		KO = ko;
	}
	public Type getDKO() {
		return DKO;
	}
	public void setDKO(Type dko) {
		DKO = dko;
	}
	public Type getTO() {
		return TO;
	}
	public void setTO(Type to) {
		TO = to;
	}
	public TimeTest getSlow() {
		return slow;
	}
	public void setSlow(TimeTest slow) {
		this.slow = slow;
	}
	public OverTime getOver() {
		return over;
	}
	public void setOver(OverTime over) {
		this.over = over;
	}
	public Type getWin() {
		return win;
	}
	public void setWin(Type win) {
		this.win = win;
	}
	public Type getWin2() {
		return win2;
	}
	public void setWin2(Type win2) {
		this.win2 = win2;
	}
	public Type getDraw() {
		return draw;
	}
	public void setDraw(Type draw) {
		this.draw = draw;
	}



	public void process() {
		if (StateMachine.getInstance().getGameState().getRoundState() <= Roundstate.COMBAT) {
			getStart().decrease();
			if (getRound().getDefault() != null)
				getRound().getDefault().decreaseDisplayTime();
		}
		
		if (StateMachine.getInstance().getGameState().getRoundState() == Roundstate.VICTORY) {
			if (StateMachine.getInstance().getGameState().getLastWin() == WinType.KO)
				getKO().getType().process();
			if (StateMachine.getInstance().getGameState().getLastWin() == WinType.DKO)
				getDKO().getType().process();
			if (StateMachine.getInstance().getGameState().getLastWin() == WinType.TO)
				getTO().getType().process();
			
		}
	}



	
}
