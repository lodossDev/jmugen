package org.lee.mugen.fight.section;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import org.lee.mugen.fight.section.elem.Start;
import org.lee.mugen.fight.section.elem.Type;

public class Round {
	public static class Match {
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
	public static class Sub {
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

	}
	
	
	Match match = new Match();
	Start start = new Start();
	Point pos = new Point();
	Sub round = new Sub();
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
		if (name.startsWith("fight.")) {
			if (fight == null) {
				fight = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (fight == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), fight, value);
			fight.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("ko.")) {
			if (KO == null) {
				KO = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (KO == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), KO, value);
			KO.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("dko.")) {
			if (DKO == null) {
				DKO = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (DKO == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), DKO, value);
			DKO.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("to.")) {
			if (TO == null) {
				TO = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (TO == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), TO, value);
			TO.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("win.")) {
			if (win == null) {
				win = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (win == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), win, value);
			win.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("win2.")) {
			if (win2 == null) {
				win2 = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (win2 == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), win2, value);
			win2.parse(name.substring(name.indexOf(".") + 1), value);
		} else if (name.startsWith("draw.")) {
			if (draw == null) {
				draw = Type.buildType(name.substring(name.indexOf(".") + 1));
				if (draw == null) {
					throw new IllegalStateException("You Must specifie type anim, font, or spr first");
				}
			}
			Type.setValue(name.substring(name.indexOf(".") + 1), draw, value);
			draw.parse(name.substring(name.indexOf(".") + 1), value);
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
	
	
	
	
}
