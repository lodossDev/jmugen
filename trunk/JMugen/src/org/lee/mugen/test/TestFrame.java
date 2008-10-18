package org.lee.mugen.test;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

import org.lee.mugen.core.StateMachine;
import org.lee.mugen.core.sound.SoundSystem;
import org.lee.mugen.renderer.GameWindow;
import org.lee.mugen.renderer.GraphicsWrapper;
import org.lee.mugen.util.debugger.SpriteDebugerUI;

public class TestFrame {


	public static void main(String[] args) throws Exception {

		final StateMachine statemachine = StateMachine.getInstance();

	    String nativeLF = UIManager.getSystemLookAndFeelClassName();
	    
	    // Install the look and feel
	    try {
	        UIManager.setLookAndFeel(nativeLF);
	    } catch (InstantiationException e) {
	    } catch (ClassNotFoundException e) {
	    } catch (UnsupportedLookAndFeelException e) {
	    } catch (IllegalAccessException e) {
	    }

		String p1 = null;
		String p2 = null;
		String stage = null;
		String music = null;
		
		
		JOptionPane.showMessageDialog(null, "Choose the first Char", "JMugen 0.01b", JOptionPane.INFORMATION_MESSAGE);
		JFileChooser fcSelectChar = new JFileChooser(new File(".", "resource/chars"));
		fcSelectChar.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".def") || f.isDirectory();
			}
			@Override
			public String getDescription() {
				return "Sprite *.def";
			}});
		fcSelectChar.showOpenDialog(null);
		File selFile = fcSelectChar.getSelectedFile();
		if (selFile != null) {
			p1 = selFile.getAbsolutePath();
		} else {
			JOptionPane.showMessageDialog(null, "This option is a mandatory");
			System.exit(0);
		}
		
		
		JOptionPane.showMessageDialog(null, "Choose the second Char", "JMugen 0.01b", JOptionPane.INFORMATION_MESSAGE);
		fcSelectChar = new JFileChooser(new File(".", "resource/chars"));
		fcSelectChar.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".def") || f.isDirectory();
			}
			@Override
			public String getDescription() {
				return "Sprite *.def";
			}});
		fcSelectChar.showOpenDialog(null);
		selFile = fcSelectChar.getSelectedFile();
		if (selFile != null) {
			p2 = selFile.getAbsolutePath();
		} else {
			JOptionPane.showMessageDialog(null, "This option is a mandatory");
			System.exit(0);
		}
		
		
		JOptionPane.showMessageDialog(null, "Choose Stage", "JMugen 0.01b", JOptionPane.INFORMATION_MESSAGE);
		fcSelectChar = new JFileChooser(new File(".", "resource/stages"));
		fcSelectChar.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".def") || f.isDirectory();
			}
			@Override
			public String getDescription() {
				return "Stage *.def";
			}});
		fcSelectChar.showOpenDialog(null);
		selFile = fcSelectChar.getSelectedFile();
		if (selFile != null) {
			stage = selFile.getAbsolutePath();
		} else {
			JOptionPane.showMessageDialog(null, "This option is a mandatory");
			System.exit(0);
		}
		
		
		JOptionPane.showMessageDialog(null, "Choose Music", "JMugen 0.01b", JOptionPane.INFORMATION_MESSAGE);
		fcSelectChar = new JFileChooser(new File(".", "resource/sound"));
		fcSelectChar.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".mp3") || f.getName().toLowerCase().endsWith(".wav")  || f.isDirectory();
			}
			@Override
			public String getDescription() {
				return "Music *.mp3|*.wav";
			}});
		fcSelectChar.showOpenDialog(null);
		selFile = fcSelectChar.getSelectedFile();
		if (selFile != null) {
			music = selFile.getAbsolutePath();
		}
		
		
		
		
		
		
		statemachine.getGameState().setGameType(1);
		statemachine.preloadSprite(StateMachine.TEAMSIDE_ONE, "1", p1, 0);
		statemachine.preloadSprite(StateMachine.TEAMSIDE_TWO, "2", p2, 0);
		statemachine.preloadStage(stage);
		if (music != null)
			SoundSystem.SoundBackGround.playMusic(music);

		
		GraphicsWrapper.init();
		final GameWindow gameWindow = GraphicsWrapper.getInstance()
				.getInstanceOfGameWindow();
		gameWindow.setGameWindowCallback(statemachine);
		SpriteDebugerUI debugerUI = new SpriteDebugerUI();
		debugerUI.setVisible(true);
		gameWindow.start();
		


	}

	public static void main2(String[] args) throws Exception {
		final StateMachine statemachine = StateMachine.getInstance();
		if (args.length < 5) {
			System.out
					.println("set character like this:\n javaw [all the classpath] [kind => java ou lwgl] org.lee.mugen.test.TestFrame kfm 0 kfm 1 stage.def [music in sound dir]");
			return;
		}
		statemachine.getGameState().setGameType(1);
		statemachine.preloadSprite(StateMachine.TEAMSIDE_ONE, "1",
				"resource/chars/" + args[0] + "/" + args[1] + ".def", Integer
						.parseInt(args[1]));
		statemachine.preloadSprite(StateMachine.TEAMSIDE_TWO, "2",
				"resource/chars/" + args[2] + "/" + args[3] + ".def", Integer
						.parseInt(args[3]));
		statemachine.preloadStage("resource/stages/" + args[3]);
		if (args.length > 5)
			SoundSystem.SoundBackGround.playMusic("resource/sound/ " + args[5]);

		GameWindow gameWindow = GraphicsWrapper.getInstance()
				.getInstanceOfGameWindow();

		gameWindow.setGameWindowCallback(statemachine);
		gameWindow.start();

	}

}