package org.lee.mugen.core;

import static org.lee.mugen.util.Logger.log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lee.mugen.core.command.SpriteCmdProcess;
import org.lee.mugen.core.renderer.game.AfterimageRender;
import org.lee.mugen.core.renderer.game.BackgroundRender;
import org.lee.mugen.core.renderer.game.CnsRender;
import org.lee.mugen.core.renderer.game.DebugExplodRender;
import org.lee.mugen.core.renderer.game.DebugRender;
import org.lee.mugen.core.renderer.game.LifeBarRenderNormal;
import org.lee.mugen.core.renderer.game.MakedustRender;
import org.lee.mugen.core.renderer.game.SpriteRender;
import org.lee.mugen.core.renderer.game.SpriteShadowRender;
import org.lee.mugen.fight.FightDef;
import org.lee.mugen.input.CmdProcDispatcher;
import org.lee.mugen.input.ISpriteCmdProcess;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.renderer.MugenTimer;
import org.lee.mugen.renderer.Renderable;
import org.lee.mugen.sprite.background.Stage;
import org.lee.mugen.sprite.base.AbstractSprite;
import org.lee.mugen.sprite.character.Sprite;
import org.lee.mugen.sprite.character.SpriteDef;
import org.lee.mugen.sprite.character.SpriteHelper;
import org.lee.mugen.sprite.cns.StateDef;
import org.lee.mugen.sprite.cns.eval.trigger.function.spriteCns.Roundstate;
import org.lee.mugen.sprite.cns.type.function.Assertspecial;
import org.lee.mugen.sprite.cns.type.function.Assertspecial.Flag;
import org.lee.mugen.sprite.common.resource.FontParser;
import org.lee.mugen.sprite.common.resource.FontProducer;
import org.lee.mugen.sprite.entity.AfterImageSprite;
import org.lee.mugen.sprite.entity.ExplodSprite;
import org.lee.mugen.sprite.entity.MakeDustSpriteManager;
import org.lee.mugen.sprite.entity.ProjectileSprite;

/**
 * 
 * @author Dr Wong
 * 
 * StateMachine is the core of the program
 */
public class StateMachine implements Game {
	// TeamMate
	public static final int TEAMSIDE_ONE = 1;
	public static final int TEAMSIDE_TWO = 2;
	private Map<String, Sprite> _teamOne = new HashMap<String, Sprite>();
	private Map<String, Sprite> _teamTwo = new HashMap<String, Sprite>();
	
	public Map<String, Sprite> getTeamOne() {
		return _teamOne;
	}
	public Map<String, Sprite> getTeamTwo() {
		return _teamTwo;
	}
	public boolean isSpriteInSameTeam(String id, String id2) {
		return (_teamOne.containsKey(id) && _teamOne.containsKey(id2)) || (_teamTwo.containsKey(id) && _teamTwo.containsKey(id2));
	}

	// frame rate



//	 Core

	private GameWindow gameWindows;
	private FightEngine _fightEngine = new FightEngine();
	private GameState gameState = new GameState();

	public GameWindow getWindow() {
		return gameWindows;
	}
	public void setWindow(GameWindow gameWindows) {
		this.gameWindows = gameWindows;
	}

	public FightEngine getFightEngine() {
		return _fightEngine;
	}
	public GameState getGameState() {
		return gameState;
	}
	
	// Singleton
	private StateMachine() {
	}
	private static StateMachine stateMachine = null;
	public static StateMachine getInstance() {
		if (stateMachine == null) {
			stateMachine = new StateMachine();
		}
		return stateMachine;
	}
	
	// global events Events 
	private GameGlobalEvents globalEvents;
	public GameGlobalEvents getGlobalEvents() {
		return globalEvents;
	}
	
	
	
	// Stage
	private Stage instanceOfStage;
	private String stage;
	public void preloadStage(String fileDef) throws FileNotFoundException,
			IOException {
		stage = fileDef;
	}

	private void loadStage() throws Exception {
		removeBackgroundRender();
		instanceOfStage = Stage.buildStage(stage);
		addRender(new BackgroundRender());

	}

	public Stage getInstanceOfStage() {
		return instanceOfStage;
	}
	
	// Sprite Player Management
	// Main Sprite
	private Map<String, Sprite> _spriteMap = new HashMap<String, Sprite>();
	private Map<String, SpriteCmdProcess> spriteCmdProcessMap = new HashMap<String, SpriteCmdProcess>();
	private Map<String, Renderable> spriteMapRenderable = new HashMap<String, Renderable>();
	private Map<String, SpriteDef> spriteDefs = new HashMap<String, SpriteDef>();
	
	public SpriteDef getSpriteDef(String spriteId) {
		return spriteDefs.get(spriteId);
	}
	public Collection<Sprite> getSprites() {
		List<Sprite> sprites = new LinkedList<Sprite>();
		sprites.addAll(_spriteMap.values());
		return sprites;
	}
	public Collection<String> getPlayersIds() {
		return _spriteMap.keySet();
	}
	public Sprite getSpriteInstance(String spriteId) {
		Sprite spr = _spriteMap.get(spriteId);
		return spr;
	}
	public Renderable getSpriteRenderInstance(String spriteId) {
		return spriteMapRenderable.get(spriteId);
	}
	
	
	// Helper Sprite
	private List<SpriteHelper> _spriteHelperAddList = new LinkedList<SpriteHelper>();
	private List<String> _spriteHelperRemoveList = new ArrayList<String>();
	
	public synchronized void addSpriteHelper(SpriteHelper sprClone) {
		_spriteHelperAddList.add(sprClone);
		
		_spriteMap.put(sprClone.getSpriteId(), sprClone);
		spriteMapRenderable.put(sprClone.getSpriteId(), new SpriteRender(sprClone));
		addRender(spriteMapRenderable.get(sprClone.getSpriteId()));
		ISpriteCmdProcess scp = getSpriteCmdProcessMap().get(sprClone.getHelperSub().getSpriteFrom().getSpriteId());
//		if (scp != null)
//			scp.addSprite(sprClone.getSpriteId());


		sprClone.getSpriteState().changeStateDef(sprClone.getHelperSub().getStateno());
		addPartner(sprClone, sprClone.getHelperSub().getSpriteFrom());

		
//		helperManagement();
	}
	public void removeSpriteHelper(String sprCloneId) {
		_spriteHelperRemoveList.add(sprCloneId);
//		helperManagement();
	}
	
	private void helperManagement() {
		if (!_spriteHelperAddList.isEmpty()) {
			
			SpriteHelper[] helpers = _spriteHelperAddList.toArray(new SpriteHelper[0]);
			_spriteHelperAddList.clear();
			for (SpriteHelper sprClone: helpers) {
				_spriteMap.put(sprClone.getSpriteId(), sprClone);
				spriteMapRenderable.put(sprClone.getSpriteId(), new SpriteRender(sprClone));
				addRender(spriteMapRenderable.get(sprClone.getSpriteId()));
				ISpriteCmdProcess scp = getSpriteCmdProcessMap().get(sprClone.getHelperSub().getSpriteFrom().getSpriteId());
//				if (scp != null)
//					scp.addSprite(sprClone.getSpriteId());


				sprClone.getSpriteState().changeStateDef(sprClone.getHelperSub().getStateno());
				addPartner(sprClone, sprClone.getHelperSub().getSpriteFrom());
			}
			
		}

		if (!_spriteHelperRemoveList.isEmpty()) {
			for (String spriteId: _spriteHelperRemoveList) {
				Sprite spr = getSpriteInstance(spriteId);
				removePartner(spr);
				
				renderableList.remove(spriteMapRenderable.get(spriteId));
				spriteMapRenderable.remove(spriteId);
				_spriteMap.remove(spriteId);
				
				if (spr instanceof SpriteHelper) {
//					log("destroy " + spriteId);
					SpriteHelper sprH = (SpriteHelper) spr;
					ISpriteCmdProcess scp = getSpriteCmdProcessMap().get(sprH.getHelperSub().getSpriteFrom().getSpriteId());
					if (scp != null)
						scp.remove(sprH.getSpriteId());
				}
			}
			_spriteHelperRemoveList.clear();
		}
	}
	
	// Sprite Loader
	
	// Later
	//	private Map<String, Sprite> _cachingSprite = new HashMap<String, Sprite>();
	
	
	protected static class SpriteLoader {

		private String spriteId;
		private String def;
		private int pal;
		private int teamSide;
		
		public SpriteLoader(String spriteId, String def, int pal, int teamSide) {
			this.spriteId = spriteId;
			this.def = def;
			this.pal = pal;
			this.teamSide = teamSide;
		}
		
		public String getDef() {
			return def;
		}
		public void setDef(String def) {
			this.def = def;
		}
		public int getPal() {
			return pal;
		}
		public void setPal(int pal) {
			this.pal = pal;
		}
		public String getSpriteId() {
			return spriteId;
		}
		public void setSpriteId(String spriteId) {
			this.spriteId = spriteId;
		}
		public int getTeamSide() {
			return teamSide;
		}
		public void setTeamSide(int teamSide) {
			this.teamSide = teamSide;
		}
		
	}
	private List<SpriteLoader> spriteLoader = new ArrayList<SpriteLoader>();
	public void preloadSprite(int teamSide, String spriteId, String def, int pal) {
		SpriteLoader sl = new SpriteLoader(spriteId, def, pal, teamSide);
		spriteLoader.add(sl);
	}
	private void loadSprites() throws Exception {
		if (!spriteLoader.isEmpty()) {
			for (SpriteLoader sl: spriteLoader) {
				log("Load Sprite " + sl.def);
				loadingText += "\nloading Sprite " + sl.getSpriteId() + " : ";
				loadSprite(sl);
			}
			spriteLoader.clear();			
		}
	}

	
	private void loadSprite(SpriteLoader sprLoader)
		throws Exception {
		Sprite spr = _spriteMap.get(sprLoader.getSpriteId());
		if (spr == null) {
			log("Load Sprite def");
			SpriteDef sprDef = Sprite.parseSpriteDef(sprLoader.getDef(), sprLoader.getSpriteId());
			log("End Load Sprite def");

			spriteDefs.put(sprLoader.getSpriteId(), sprDef);
			log("Load Sprite");
			spr = new Sprite(sprLoader.getSpriteId(), sprDef, sprLoader.getPal());
			log("End Load Sprite");
			if (sprLoader.getTeamSide() == TEAMSIDE_ONE) {
				getTeamOne().put(sprLoader.getSpriteId(), spr);
			} else if (sprLoader.getTeamSide() == TEAMSIDE_TWO) {
				getTeamTwo().put(sprLoader.getSpriteId(), spr);
				
			}
			_spriteMap.put(sprLoader.getSpriteId(), spr);
			spriteMapRenderable.put(sprLoader.getSpriteId(), new SpriteRender(spr));
			addRender(spriteMapRenderable.get(sprLoader.getSpriteId()));
		} else {
			Sprite old = getSpriteInstance(sprLoader.getSpriteId());
			int teamSide = 0;
			if (getTeamOne().containsKey(old.getSpriteId())) {
				teamSide = TEAMSIDE_ONE;
			} else if (getTeamTwo().containsKey(old.getSpriteId())) {
				teamSide = TEAMSIDE_TWO;
			}
			
			SpriteDef sprDef = Sprite.parseSpriteDef(sprLoader.getDef(), sprLoader.getSpriteId());
			spriteDefs.put(sprLoader.getSpriteId(), sprDef);
			spr = new Sprite(sprLoader.getSpriteId(), sprDef, sprLoader.getPal());
			
			if (teamSide == TEAMSIDE_ONE) {
				getTeamOne().put(spr.getSpriteId(), spr);
			} else if (teamSide == TEAMSIDE_TWO) {
				getTeamTwo().put(spr.getSpriteId(), spr);
			}
			_spriteMap.put(sprLoader.getSpriteId(), spr);
			renderableList.remove(spriteMapRenderable.get(sprLoader.getSpriteId()));
			spriteMapRenderable.put(sprLoader.getSpriteId(), new SpriteRender(spr));
			addRender(spriteMapRenderable.get(sprLoader.getSpriteId()));
			
		}
	}
	
	// Others Sprite
	
	private List<AbstractSprite> _otherSprites = new LinkedList<AbstractSprite>();
	public List<AbstractSprite> getOtherSprites() {
		return _otherSprites;
	}



	
	List<Renderable> renderableList = new ArrayList<Renderable>();
	List<Renderable> backgroundRenderList = new ArrayList<Renderable>();
	
	public void addRender(Renderable r) {
		synchronized (renderableList) {
			if (r instanceof BackgroundRender) {
				backgroundRenderList.add((BackgroundRender) r);
				return;
			}
			if (!renderableList.contains(r))
				renderableList.add(r);
		}
	}
	private void removeBackgroundRender() {
		backgroundRenderList.clear();
	}


	
	public List<Renderable> getRenderables() {
		return renderableList;
	}


	private Comparator<Renderable> _DEFAULT_COMPARATOR_FOR_RENDERER = new Comparator<Renderable>() {
		public int compare(Renderable o1, Renderable o2) {
			return o1.getPriority() - o2.getPriority();
		}
	};
	private boolean qPress;

	public void orderRenderList() {
		Collections.sort(renderableList, _DEFAULT_COMPARATOR_FOR_RENDERER);
	}




	
	private FightDef fightDef = new FightDef();
	
	public FightDef getFightDef() {
		return fightDef;
	}
	public void setFightDef(FightDef fightDef) {
		this.fightDef = fightDef;
	}
	////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	List<CnsRender> cnsRenderList = new ArrayList<CnsRender>();
	public void init(GameWindow container) throws Exception {
		setWindow(container);

		loadingText += "\nloading Fight.def";
		
		fightDef.init();
		try {
			
			loadSprites();
			
			loadingText += "\nloading Stage ";

			loadStage();
			globalEvents = new GameGlobalEvents();
			
			addRender(new SpriteShadowRender(getSpriteInstance("1"), false));
			addRender(new SpriteShadowRender(getSpriteInstance("2"), false));
			
			cnsRenderList.add(new CnsRender(getSpriteInstance("1")));
			cnsRenderList.add(new CnsRender(getSpriteInstance("2")));

			addRender(cnsRenderList.get(0));
			addRender(cnsRenderList.get(1));
			
			if (gameState.getGameType() == 1)
				addRender(new LifeBarRenderNormal());

		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new IllegalStateException();
		}

		for (Sprite s : getSprites()) {
			if (spriteCmdProcessMap.get(s) == null) {
				SpriteCmdProcess scp = new SpriteCmdProcess(CmdProcDispatcher.getSpriteDispatcherMap().get(s.getSpriteId()));
				scp.addSprite(s.getSpriteId());
				spriteCmdProcessMap.put(s.getSpriteId(), scp);
				((GameWindow)container).addSpriteKeyProcessor(scp);
			}
		}
		
		initRound();
	}

	
	int stateSpr1 = Roundstate.PRE_INTRO;
	int stateSpr2 = Roundstate.PRE_INTRO;


	public void update(int delta) throws Exception {
		if (systemPause && !forceUpdate)
			return;
		if (getGlobalEvents().isAssertSpecial("1", Flag.intro))
			stateSpr1 = Roundstate.INTRO;
		if (getGlobalEvents().isAssertSpecial("2", Flag.intro))
			stateSpr2 = Roundstate.INTRO;
		
		if (stateSpr1 == Roundstate.INTRO && !getGlobalEvents().isAssertSpecial("1", Flag.intro))
			stateSpr1 = Roundstate.COMBAT;
		if (stateSpr2 == Roundstate.INTRO && !getGlobalEvents().isAssertSpecial("2", Flag.intro))
			stateSpr2 = Roundstate.COMBAT;
			
		if (stateSpr1 == stateSpr2) {
			getGameState().setRoundState(stateSpr1);
			if (getGameState().getRoundState() == Roundstate.COMBAT 
					&& getGameState().getRoundsExisted() == 0) {
				getSpriteInstance("1").getInfo().setCtrl(1);
				getSpriteInstance("2").getInfo().setCtrl(1);
				getGameState().setRoundsExisted(1);
			}
		}

		
		globalEvents.enter();

		for (Sprite s : getSprites()) {
			ISpriteCmdProcess sprCmdProc = spriteCmdProcessMap.get(s.getSpriteId());
			if (sprCmdProc != null) {
				sprCmdProc.process();
			}

			
			if (globalEvents.canGameProcessWithPause(s)) {
				s.process();
			} else {
//				s.processPause();
			}
		}

		if (!globalEvents.isPause()) {
			getFightEngine().process();
		}
		
//		if (!globalEvents.isSuperPauseOrPause()) {
			for (Iterator<AbstractSprite> iter = getOtherSprites().iterator(); iter.hasNext();) {
				AbstractSprite spr = iter.next();
				if (spr instanceof ExplodSprite) {
					ExplodSprite es = (ExplodSprite) spr;
					if (es.getExplod().getSupermovetime() > 0 && globalEvents.isSuperPause()) {
						es.process();
					} else if (es.getExplod().getPausemovetime() > 0 && globalEvents.isPause()) {
						es.process();
					} else if (es.getExplod().isSupermove() && globalEvents.isSuperPause()) {
						es.process();
					} else if (!globalEvents.isSuperPause() && !globalEvents.isPause()) {
						es.process();
					} 
					else {
						es.process();
					}
						
				} else {
					spr.process();
					
				}
				if (spr.remove())
					iter.remove();
			}
//		}
		if ( globalEvents.isPause()) {
//			instanceOfStage.process();
		} else if (globalEvents.isSuperPause()) {
			instanceOfStage.process();
			
		} else {
			instanceOfStage.process();
			
		}
		getGameState().process();
		// 
		globalEvents.leave();
		
		loadSprites();
		helperManagement();
		fightDef.process();
	}



	private void render(List<Renderable> rendererList) {
		orderRenderList();
		List<Renderable> toRemove = new ArrayList<Renderable>();
		for (Renderable renderable : rendererList) {
			if (globalEvents.getEnvcolor().isUse() && !globalEvents.getEnvcolor().isUnder()) {
				if (!globalEvents.canDisplayThisRender(renderable.getClass())) {
					continue;
				}
			}
			if (renderable.isProcess()) {
//				if (renderable instanceof ExplodRender)
					renderable.render();
			}
			if (renderable.remove()) {
				toRemove.add(renderable);
			}
		}
		rendererList.removeAll(toRemove);
	}

	public void render() {
		
		BackgroundRender br = (BackgroundRender) backgroundRenderList.get(0);
		br.setLayerDisplay(0);
		
		if (!globalEvents.getEnvcolor().isUse()) {
			if (!globalEvents.isAssertSpecial(Assertspecial.Flag.nobg)) {
				render(backgroundRenderList);
			}
		} else {
			globalEvents.getEnvcolorRender().render();
		}
		
		render(renderableList);
		
		br.setLayerDisplay(1);
		if (!globalEvents.getEnvcolor().isUse()) {
			if (!globalEvents.isAssertSpecial(Assertspecial.Flag.nobg)) {
				render(backgroundRenderList);
			}
		} else {
			
		}
	}

	public Map<String, SpriteCmdProcess> getSpriteCmdProcessMap() {
		return spriteCmdProcessMap;
	}


	public Map<String, StateDef> getInstanceOfStatedefFromOther(String spriteId) {
		Map<String, StateDef> stateDefsMap = getSpriteInstance(spriteId).getSpriteState().cloneStateNormalDef();
		return stateDefsMap;
	}
	
//////////////////////////////////////////////////
//////////////////////////////////////////////////
//////////////////////////////////////////////////
//////////////////////////////////////////////////
//////////////////////////////////////////////////
	
	public int countHelper(String spriteId) {
		int count = 0;
		for (Sprite s: getPartners(getSpriteInstance(spriteId))) {
			Sprite spr = getRoot(s);
			if (spr.getSpriteId().equals(spriteId) && !s.equals(spr)) {
				count++;
			}
		}
		return count;
	}

//	Parent : Redirige le trigger vers le parent du personnage (le personnage doit être un helper). Le parent d'un helper est le personnage qui a créé cet helper (et qui peut être soit le personnage de base, soit un autre helper).
	public Sprite getParent(Sprite spr) {
		if (!(spr instanceof SpriteHelper)) {
			return null;
		}
		return ((SpriteHelper) spr).getHelperSub().getSpriteFrom();
	}
	public Sprite getParent(String spriteId) {
		Sprite spr = getSpriteInstance(spriteId);
		return getParent(spr);
	}
	public String getParentId(Sprite spr) {
		if (!(spr instanceof SpriteHelper)) {
			return spr.getSpriteId();
		}
		return ((SpriteHelper) spr).getHelperSub().getSpriteFrom().getSpriteId();
	}
	public String getParentId(String spriteId) {
		Sprite spr = getSpriteInstance(spriteId);
		return getParentId(spr);
	}
//	Root : Redirige le trigger vers la base. Le root d'un helper est le personnage de base (équivaut donc au parent pour un helper directement créé par le personnage de base).

	public Sprite getRoot(Sprite spr) {
		if (!(spr instanceof SpriteHelper)) {
			return spr;
		}
		return getRoot(((SpriteHelper) spr).getHelperSub().getSpriteFrom());
	}
	public Sprite getRoot(String spriteId) {
		Sprite spr = getSpriteInstance(spriteId);
		return getRoot(spr);
	}
	public String getRootId(Sprite sprite) {
		return getRoot(sprite).getSpriteId();
	}
	public String getRootId(String spriteId) {
		Sprite spr = getSpriteInstance(spriteId);
		return getRootId(spr);
	}
	
//	Helper : Redirige le trigger vers le premier helper trouvé. Voir le trigger associé "NumHelper" dans la documentation des trigger.
	public Sprite getHelper(Sprite sprite) {
		for (Sprite s: getPartners(sprite)) {
			if (!s.equals(sprite) && (sprite instanceof SpriteHelper)) {
				SpriteHelper helper = (SpriteHelper) s;
				if (helper.getHelperSub().getSpriteFrom().equals(sprite))
					return helper;
			}
		}
		return null;
	}
	
	public List<SpriteHelper> getHelpers(Sprite sprite) {
		List<SpriteHelper> result = new ArrayList<SpriteHelper>();
		for (Sprite s: getPartners(sprite)) {
			if (!s.equals(sprite) && (sprite instanceof SpriteHelper)) {
				SpriteHelper helper = (SpriteHelper) s;
				if (helper.getHelperSub().getSpriteFrom().equals(sprite))
					result.add(helper);
			}
		}
		return result;
	}
	
	public Sprite getHelper(String spriteId) {
		Sprite spr = getSpriteInstance(spriteId);
		return getHelper(spr);
	}
	public String getHelperId(Sprite sprite) {
		Sprite helper = getHelper(sprite);
		if (helper != null)
			return helper.getSpriteId();
		return null;
	}
	public String getHelperId(String spriteId) {
		Sprite spr = getSpriteInstance(spriteId);
		return getHelperId(spr);
	}
	
//	Helper(ID) : ID doit être une expression correct qui donne un entier positif. Le trigger est alors redirigé vers un helper avec le numéro ID correspondant.

	public Sprite getHelperWithID(Sprite sprite, int id) {
		for (Sprite s: getPartners(sprite)) {
			if (!s.equals(sprite) && (sprite instanceof SpriteHelper)) {
				SpriteHelper helper = (SpriteHelper) s;
				if (helper.getHelperSub().getSpriteFrom().equals(sprite) 
						&& helper.getHelperSub().getId() == id)
					return helper;
			}
		}
		return null;
	}
	public Sprite getHelperWithID(String spriteId, int id) {
		Sprite spr = getSpriteInstance(spriteId);
		return getHelperWithID(spr, id);
	}
	public String getHelperIdWithID(Sprite sprite, int id) {
		Sprite helper = getHelperWithID(sprite, id);
		if (helper != null)
			return helper.getSpriteId();
		return null;
	}
	public String getHelperIdWithID(String spriteId, int id) {
		Sprite spr = getSpriteInstance(spriteId);
		return getHelperIdWithID(spr, id);
	}
//	Target : Redirige le trigger vers la première cible trouvée. La target est la cible, c'est-à-dire le personnage adverse qui vient d'être touché par le personnage.

//	Target(ID) : ID doit être une expression correcte qui donne un entier non négatif. Le trigger est alors redirigé vers une cible avec le targetID correspondant.
//
//	Partner : Redirige le trigger vers le partenaire du personnage. Les helpers normaux et les personnages neutres ne sont pas considérés comme adversaires. Voir le trigger associé "numpartner" dans la documentation des triggers.
//
//	Enemy : Redirige le trigger vers le premier adversaire trouvé. Les helpers normaux et les personnages neutres ne sont pas pris comme des adversaires. Voir le trigger associé "numenemy" dans la documentation des triggers.
//
//	Enemy(n) : n doit être une expression correcte donnant un entier non négatif. Le trigger est redirigé vers le n-ième adversaire.
//
//	EnemyNear : Redirige le trigger vers l'adversaire le plus proche.
//
//	EnemyNear(n) : n doit être une expression correcte donnant un entier non négatif. Le trigger est redirigé vers le n-ième plus proche adversaire.
//
//	PlayerID(ID) : ID doit être une expression correcte donnant un entier non négatif. Le trigger est redirigé vers le personnage dont l'ID unique est égal à ID. Voir les triggers "ID" et "PlayerExistID" dans la documentation des triggers.
//

// Ennemies
	public Sprite getFirstEnnmy(String spriteId) {
		Collection<Sprite> sprites = getEnnmies(getSpriteInstance(spriteId));
		for (Sprite sprite: sprites) {
			if (!(sprite instanceof SpriteHelper))
				return sprite;
		}
		assert false; // ca cas n'arrivera jamais
		return null;
	}
	
	public Collection<Sprite> getEnnmies(Sprite sprite) {
		if (sprite instanceof SpriteHelper)
			return getEnnmiesHelper((SpriteHelper) sprite);
		if (getTeamOne().containsKey(sprite.getSpriteId())) {
			return getTeamTwo().values();
		} else if (getTeamTwo().containsKey(sprite.getSpriteId())) {
			return getTeamOne().values();
		}
		return null;
	}
	private Collection<Sprite> getEnnmiesHelper(SpriteHelper sprite) {
		Sprite spr = getRoot(sprite);
		if (getTeamOne().containsKey(spr.getSpriteId())) {
			return getTeamTwo().values();
		} else if (getTeamTwo().containsKey(spr.getSpriteId())) {
			return getTeamOne().values();
		}
		
		return null;
	}
//	public Collection<Sprite> getEnnmies(String spriteId) {
//
//		
//		if (getTeamOne().containsKey(spriteId)) {
//			return getTeamTwo().values();
//		} else if (getTeamTwo().containsKey(spriteId)) {
//			return getTeamOne().values();
//		}
//		return null;
//	}
	public Collection<Sprite> getEnnmies(AbstractSprite spriteHitter) {
		if (spriteHitter instanceof Sprite) {
			return getEnnmies((Sprite)spriteHitter);
		} else if (spriteHitter instanceof ProjectileSprite) {
			return getEnnmies((ProjectileSprite)spriteHitter);
		}
		assert false;
		return null;
	}
	private Collection<Sprite> getEnnmies(ProjectileSprite projectile) {
		Sprite parent = projectile.getProjectileSub().getSpriteParent();
		return getEnnmies(parent);
	}
//	 Partners
	public Collection<Sprite> getPartners(Sprite sprite) {
		if (sprite instanceof SpriteHelper)
			return getPartnersHelper((SpriteHelper) sprite);
		if (getTeamOne().containsKey(sprite.getSpriteId())) {
			return getTeamOne().values();
		} else if (getTeamTwo().containsKey(sprite.getSpriteId())) {
			return getTeamTwo().values();
		}
		return null;
	}

	public Collection<Sprite> getPartners(AbstractSprite spriteHitter) {
		if (spriteHitter instanceof Sprite) {
			return getPartners((Sprite)spriteHitter);
		} else if (spriteHitter instanceof ProjectileSprite) {
			return getPartners((ProjectileSprite)spriteHitter);
		}
		assert false;
		return null;
	}
	private Collection<Sprite> getPartners(ProjectileSprite projectile) {
		Sprite parent = projectile.getProjectileSub().getSpriteParent();
		return getPartners(parent);
	}
	private Collection<Sprite> getPartnersHelper(SpriteHelper sprite) {
		Sprite spr = getRoot(sprite);
		if (getTeamOne().containsKey(spr.getSpriteId())) {
			return getTeamTwo().values();
		} else if (getTeamTwo().containsKey(spr.getSpriteId())) {
			return getTeamOne().values();
		}
		
		return null;
	}
///
	private void addPartner(Sprite sprite, Sprite parent) {
		if (getTeamOne().containsKey(parent.getSpriteId())) {
			getTeamOne().put(sprite.getSpriteId(), sprite);
		} else if (getTeamTwo().containsKey(parent.getSpriteId())) {
			getTeamTwo().put(sprite.getSpriteId(), sprite);
		}
	}
	private void removePartner(Sprite sprite) {
		if (sprite != null) {
			getTeamOne().remove(sprite.getSpriteId());
			getTeamTwo().remove(sprite.getSpriteId());

		}
	}
	public boolean isHomeTeam(String spriteId) {
		return getTeamOne().containsKey(spriteId);
	}
	public void removeExplod(String spriteId, int id) {
		for (AbstractSprite s: _otherSprites) {
			if (s instanceof ExplodSprite) {
				ExplodSprite es = (ExplodSprite) s;
				if (es.getExplod().getId() == id && es.getExplod().getSprite().getSpriteId().equals(spriteId)) {
					es.setForceRemove(true);
//					return;
				}
			}
		}
		
	}
	public void removeAfterImageSprite(AfterImageSprite afterimageSprite) {
		if (afterimageSprite != null) {
			_otherSprites.remove(afterimageSprite);
			afterimageSprite.setRemove(true);
			
		}
	}
	public void addAfterImageSprite(AfterImageSprite afterimageSprite) {
		_otherSprites.add(afterimageSprite);
		addRender(new AfterimageRender(afterimageSprite));
	}
	public void setAfterImageTime(String spriteId, int itime) {
		for (AbstractSprite s : getOtherSprites()) {
			if (s instanceof AfterImageSprite) {
				AfterImageSprite spr = (AfterImageSprite) s;
				if (spr.getSprite().getSpriteId().equals(spriteId)) {
					spr.setNewTimeForAll(itime);
					spr.rezetCount();
				}
			}
		}
		
	}
	public void addMakedustSpriteManager(MakeDustSpriteManager manager) {
		_otherSprites.add(manager);
		MakedustRender render = new MakedustRender(manager);
		addRender(render);
		
	}

	public static int countNormalPlayer(Collection<Sprite> sprites) {
		int count = 0;
		for (Sprite s: sprites) {
			if (s instanceof SpriteHelper) {
				SpriteHelper helper = (SpriteHelper) s;
				if (helper.getHelperSub().getHelpertype().equals("player"))
					count++;
			} else {
				count++;
			}
		}
		
		return count;

	}
	public void removeExplod(String spriteId) {
		for (AbstractSprite s: _otherSprites) {
			if (s instanceof ExplodSprite) {
				ExplodSprite es = (ExplodSprite) s;
				if (es.getExplod().getSprite().getSpriteId().equals(spriteId)) {
					es.setForceRemove(true);
//					return;
				}
			}
		}
	}
	public Collection<ExplodSprite> getExplodeSprites(Integer id) {
		Collection<ExplodSprite> sprites = new ArrayList<ExplodSprite>();
		for (AbstractSprite s: getOtherSprites()) {
			if (s instanceof ExplodSprite && 
					(id.intValue() == -1
							|| ((ExplodSprite)s).getExplod().getId() == id.intValue()))
				sprites.add((ExplodSprite) s);
		}
		
		return sprites;
	}
	private boolean systemPause = false;
	private boolean forceUpdate = false;
	
	private void initRound() {
		getInstanceOfStage().getCamera().init();

		getSpriteInstance("1").getInfo().setXPos(getInstanceOfStage().getPlayerinfo().getP1startx());
		getSpriteInstance("1").getInfo().setYPos(0);
		
		getSpriteInstance("1").getSpriteState().clearVars();
		getSpriteInstance("1").getInfo().setFlip(false);
		getSpriteInstance("1").getSpriteState().selfstate(5900);
		getSpriteInstance("1").getInfo().init();
		
		
		_spriteHelperRemoveList.addAll(getHelpersIds(getSpriteInstance("1")));
		_spriteHelperRemoveList.addAll(getHelpersIds(getSpriteInstance("2")));
		
		getOtherSprites().clear();
		
		getSpriteInstance("2").getInfo().setXPos(getInstanceOfStage().getPlayerinfo().getP2startx());
		getSpriteInstance("2").getInfo().setYPos(0);
		
		getSpriteInstance("2").getInfo().setFlip(true);
		getSpriteInstance("2").getSpriteState().clearVars();
		getSpriteInstance("2").getSpriteState().selfstate(5900);
		getSpriteInstance("2").getInfo().init();
		
		for (Iterator<Sprite> iter = getSprites().iterator();iter.hasNext();) {
			Sprite spr = iter.next();
			if (spr instanceof SpriteHelper)
				iter.remove();
		}
		getGameState().setRoundState(0);
		getGameState().setRoundsExisted(0);
		
		stateSpr1 = Roundstate.PRE_INTRO;
		stateSpr2 = Roundstate.PRE_INTRO;
	}
	
	public void onDebugAction(DebugAction action) {
		switch (action) {
		case SWICTH_PLAYER_DEBUG_INFO:
			DebugRender.debugRender.nextSprite();
			break;
		case INIT_PLAYER:
			initRound();
			break;
		case SHOW_HIDE_CNS:
			for (CnsRender cnsRender: cnsRenderList) {
				cnsRender.setShowCns(!cnsRender.isShowCns());
			}
			break;
		case SHOW_HIDE_ATTACK_CNS:
			for (CnsRender cnsRender: cnsRenderList) {
				cnsRender.setShowAttackCns(!cnsRender.isShowAttackCns());
			}
			break;
		case INCREASE_FPS:
			long famerate = getWindow().getTimer().getFramerate();
			getWindow().getTimer().setFramerate(famerate + 1);
			break;
		case DECREASE_FPS:
			long famerate2 = getWindow().getTimer().getFramerate();
			getWindow().getTimer().setFramerate(famerate2 - 1 < 0? 0: famerate2 - 1);
			break;
		case RESET_FPS:
			getWindow().getTimer().setFramerate(MugenTimer.DEFAULT_FPS);
			break;
		case DISPLAY_HELP:
			DebugRender.debugRender.setDisplayHelp(!DebugRender.debugRender.isDisplayHelp());
			break;
		case DEBUG_PAUSE:
			systemPause = !systemPause;
			break;
		case EXPLOD_DEBUG_INFO:
			DebugExplodRender.debugRender.setDisplay(!DebugExplodRender.debugRender.isDisplay());
			break;
		case PAUSE_PLUS_ONE_FRAME:
			if (systemPause) {
				try {
					forceUpdate = true;
					update(1);
					forceUpdate = false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		
		default:
			break;
		}
		
	}
	private Collection<? extends String> getHelpersIds(Sprite sprite) {
		LinkedList<String> list = new LinkedList<String>();
		for (Sprite s: getPartners(sprite)) {
			if (!s.equals(sprite) && (s instanceof SpriteHelper)) {
				SpriteHelper helper = (SpriteHelper) s;
				if (helper.getHelperSub().getSpriteFrom().equals(sprite))
					list.add(helper.getSpriteId());
			}
		}
		return list;
	}
	public void renderDebugInfo() {
		DebugRender.debugRender.render();
		DebugExplodRender.debugRender.render();
	}
	
	String loadingText = "";
	long aTime = System.currentTimeMillis();
	public void displayPendingScreeen() {
		try {
			if (System.currentTimeMillis() - aTime > 1000 
					&& loadingText != null && loadingText.length() > 0) {
			
				loadingText += ".";
				aTime = System.currentTimeMillis();
			}
			String[] strSpriteInfos = loadingText.split("\n");
			FontProducer fp = FontParser.getFontProducer();
			int addX = 0;
			int x = 10;
			int y = 10;
			for (String s: strSpriteInfos) {
				fp.draw(x, y+=fp.getSize().height, GraphicsWrapper.getInstance(), s);
				addX = Math.max(addX, s.length());
			}
			aTime++;
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
}
