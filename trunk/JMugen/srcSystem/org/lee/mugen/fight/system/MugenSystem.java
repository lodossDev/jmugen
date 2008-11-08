package org.lee.mugen.fight.system;

import java.io.File;

import org.lee.mugen.background.Background;

public class MugenSystem {
	private static MugenSystem instance;
	
	
	public File getCurrentDir() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private Info info;
	private Files files;
	private Music music;
	
	private TitleInfo titleInfo;
	private Background titleBackground;
	
	private SelectInfo selectInfo;
	private Background selectBackground;
	
	private VsScreen vsScreen;
	private Background versusBackground;
	
	private DemoMode demoMode;
	
	private ContinueScreen continueScreen;
	
	private GameOverScreen gameOverScreen;
	
	private WinScreen winScreen;
	
	private DefaultEnding defaultEnding;
	
	private EndCredits endCredits;
	
	private SurvivalResultsScreen survivalResultsScreen;
	
	private OptionInfo optionInfo;
	private Background optionBackground;
	

}
