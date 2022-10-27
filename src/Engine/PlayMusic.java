package Engine;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlayMusic {

	// to store current position
	Clip clip;

	// AudioInputStream audioInputStream;
	static String dayMusic;
	static String nightMusic;
	static String damageSound;

	DayMusic dm = new DayMusic();
	NightMusic nm = new NightMusic();
	DamageSound dg = new DamageSound();

	// constructor to initialize streams and clip
	public PlayMusic() {
		dayMusic = "nintendo ds pokemon music.wav";
		nightMusic = "summer-night-forest.wav";
		damageSound = "Bonk - Sound Effect (HD).wav";

	}

	public class DamageSound {

		Clip clip;

		public void setFile(String soundFileName) {
			try {
				File file = new File(Config.RESOURCES_PATH + soundFileName);
				// create AudioInputStream object
				AudioInputStream damage = AudioSystem.getAudioInputStream(file);

				clip = AudioSystem.getClip();
				clip.open(damage);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Method to play the audio
		public void play() {
			// start the clip

			clip.setFramePosition(0);
			clip.start();

		}

		public void loop() {
			clip.loop(Clip.LOOP_CONTINUOUSLY);

		}

		// Method to stop the audio
		public void stop() {

			clip.stop();
			clip.close();

		}

	}

	public class DayMusic {
		Clip clip;

		public void setFile(String soundFileName) {
			try {
				File file = new File(Config.RESOURCES_PATH + soundFileName);
				// create AudioInputStream object
				AudioInputStream day = AudioSystem.getAudioInputStream(file);

				clip = AudioSystem.getClip();
				clip.open(day);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Method to play the audio
		public void play() {
			// start the clip
			// clip.setFramePosition(0);
			clip.start();

		}

		public void loop() {
			clip.loop(Clip.LOOP_CONTINUOUSLY);

		}

		// Method to stop the audio
		public void stop() {
			//clip.stop();
			clip.close();
		}

	}

	public class NightMusic {

		Clip clip;

		public void setFile(String soundFileName) {
			try {
				File file = new File(Config.RESOURCES_PATH + soundFileName);
				// create AudioInputStream object
				AudioInputStream night = AudioSystem.getAudioInputStream(file);

				clip = AudioSystem.getClip();

				clip.open(night);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Method to play the audio
		public void play() {
			// start the clip
			// clip.setFramePosition(0);
			clip.start();

		}

		public void loop() {
			clip.loop(Clip.LOOP_CONTINUOUSLY);

		}

		// Method to stop the audio
		public void stop() {
			//clip.stop();
			clip.close();
		}

	}

	public void playDM() {

		dm.setFile(dayMusic);
		dm.play();

	}

	public void stopDM() {
		
			dm.stop();
		 
	}

	public void playNM() {

		nm.setFile(nightMusic);
		nm.play();

	}

	public void stopNM() {
		
			nm.stop();
		
	}

	public void playDG() {
		try {
			dg.setFile(damageSound);
			dg.play();
		} catch (Exception e) {

		}

	}

	public static void main(String args[]) {

	}

}