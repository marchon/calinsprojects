package ro.calin.vr;

import java.net.URL;
import java.util.HashMap;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;

public class SoundServer {
	private static SoundServer instance;

	public static SoundServer get() {
		if (instance == null) {
			instance = new SoundServer();
		}

		return instance;
	}

	private SoundServer() {
	}

	private AudioSystem audio;
	private Camera camera;
	private HashMap<String, AudioTrack> sounds = new HashMap<String, AudioTrack>();

	public void initSound(Camera cam) {
		/** Set the 'ears' for the sound API */
		audio = AudioSystem.getSystem();
		audio.getEar().trackOrientation(cam);
		audio.getEar().trackPosition(cam);

		this.camera = cam;
	}

	public void loadSound(String name, URL path, boolean loop) throws IllegalStateException {
		if (audio == null) {
			throw new IllegalStateException("SoundServer not initiated.");
		}

		/** Create program sound */
		AudioTrack sound = audio.createAudioTrack(path, false);

		if (sound != null) {
			sound.setMaxAudibleDistance(1000);
			sound.setVolume(1.0f);
			sound.setLooping(loop);
			
			sounds.put(name, sound);
		} else {
			throw new IllegalArgumentException("Invalid path.");
		}
	}

	public void playSound(String name, Vector3f pos, float volume) {
		AudioTrack sound = sounds.get(name);
		if(sound == null)
			return;
		
		if(pos == null) pos = camera.getLocation();
		sound.setWorldPosition(pos);
		sound.setVolume(volume);
		sound.play();
	}
	
	public void setPitch(String name, float pitch) {
		AudioTrack sound = sounds.get(name);
		if(sound == null)
			return;
		
		sound.setPitch(pitch);
	}
	
	public void playSound(String name, Vector3f pos) {
		playSound(name, pos, 1.0f);
	}
	
	public void stopSound(String name) {
		AudioTrack sound = sounds.get(name);
		if(sound == null)
			return;
		sound.stop();
	}
}
