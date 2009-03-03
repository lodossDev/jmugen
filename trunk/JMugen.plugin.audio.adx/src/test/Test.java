package test;

import java.io.IOException;
import java.util.ServiceLoader;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

public class Test {
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, ClassNotFoundException {
        Class.forName("org.lee.mugen.audio.adx.sample.file.AdxAudioFileReader");
        Class.forName("org.lee.mugen.audio.adx.sample.convert.AdxFormatConversionProvider");
        ServiceLoader<AudioFileReader> codecSetLoader   = ServiceLoader.load(AudioFileReader.class);
        System.out.println(codecSetLoader);
     
	}
}
