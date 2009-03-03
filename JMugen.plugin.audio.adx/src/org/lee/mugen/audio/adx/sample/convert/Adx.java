package org.lee.mugen.audio.adx.sample.convert;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;

public class Adx {
	/*
	 * Address Size Contains 
	 * 0x00 2 0x8000 
	 * 0x02 2 data offset 
	 * 0x04 1 format (3 for ADX) 
	 * 0x05 1 block size (typically 18) 
	 * 0x06 1 bits per sample? (4)
	 * 0x07 1 channel count 
	 * 0x08 4 sample rate 
	 * 0x0c 4 sample count 
	 * 0x10 2 high-pass cutoff 
	 * 0x12 1 loop data style ("type": 03, 04, or 05) 
	 * 0x13 1 encryption flag (other flags?)
	 */
	public static abstract class AdxHeader {
		protected int head0x8000; // ushort
		protected int dataOffset; // ushort
		protected int format; // ubyte
		protected int blockSize; // ubyte
		protected int bitsPerSample; // ubyte
		protected int channelCount; // ubyte
		protected int sampleRate; // int
		protected int sampleCount; // int
		protected int highPassCutOff; // short
		protected int version; // byte
		protected int flag; // byte

		protected AdxHeader(RandomAccessFile file) throws IOException {
			file.seek(0);
			head0x8000 = file.readShort();
			dataOffset = file.readShort();
			format = file.readByte();
			blockSize = file.readByte();
			bitsPerSample = file.readByte();
			channelCount = file.readByte();
			sampleRate = file.readInt();
			sampleCount = file.readInt();
			highPassCutOff = file.readShort();
			version = file.readByte();
			flag = file.readByte();
		}

		// ////////////////

		protected byte[] unknown;
		protected int loopFlag;
		protected int loopStartSample;
		protected int loopStartByte;
		protected int loopEndSample;
		protected int loopEndByte;

		// ///////////////////////

		public int getHead0x8000() {
			return head0x8000;
		}

		public void setHead0x8000(int head0x8000) {
			this.head0x8000 = head0x8000;
		}

		public int getDataOffset() {
			return dataOffset;
		}

		public void setDataOffset(int dataOffset) {
			this.dataOffset = dataOffset;
		}

		public int getFormat() {
			return format;
		}

		public void setFormat(int format) {
			this.format = format;
		}

		public int getBlockSize() {
			return blockSize;
		}

		public void setBlockSize(int blockSize) {
			this.blockSize = blockSize;
		}

		public int getBitsPerSample() {
			return bitsPerSample;
		}

		public void setBitsPerSample(int bitsPerSample) {
			this.bitsPerSample = bitsPerSample;
		}

		public int getChannelCount() {
			return channelCount;
		}

		public void setChannelCount(int channelCount) {
			this.channelCount = channelCount;
		}

		public int getSampleRate() {
			return sampleRate;
		}

		public void setSampleRate(int sampleRate) {
			this.sampleRate = sampleRate;
		}

		public int getSampleCount() {
			return sampleCount;
		}

		public void setSampleCount(int sampleCount) {
			this.sampleCount = sampleCount;
		}

		public int getHighPassCutOff() {
			return highPassCutOff;
		}

		public void setHighPassCutOff(int highPassCutOff) {
			this.highPassCutOff = highPassCutOff;
		}

		public int getVersion() {
			return version;
		}

		public void setVersion(int version) {
			this.version = version;
		}

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public byte[] getUnknown() {
			return unknown;
		}

		public void setUnknown(byte[] unknown) {
			this.unknown = unknown;
		}

		public int getLoopFlag() {
			return loopFlag;
		}

		public void setLoopFlag(int loopFlag) {
			this.loopFlag = loopFlag;
		}

		public int getLoopStartSample() {
			return loopStartSample;
		}

		public void setLoopStartSample(int loopStartSample) {
			this.loopStartSample = loopStartSample;
		}

		public int getLoopStartByte() {
			return loopStartByte;
		}

		public void setLoopStartByte(int loopStartByte) {
			this.loopStartByte = loopStartByte;
		}

		public int getLoopEndSample() {
			return loopEndSample;
		}

		public void setLoopEndSample(int loopEndSample) {
			this.loopEndSample = loopEndSample;
		}

		public int getLoopEndByte() {
			return loopEndByte;
		}

		public void setLoopEndByte(int loopEndByte) {
			this.loopEndByte = loopEndByte;
		}

		// ////////////////

	}

	/*
	 * type 03 loop data Address Size Contains 
	 * 0x14 4 unknown 
	 * 0x18 4 loop flag
	 * 0x1c 4 loop start sample 
	 * 0x20 4 loop start byte 
	 * 0x24 4 loop end sample
	 * 0x28 4 loop end byte type 04 loop data
	 */
	public static class AdxHeader3 extends AdxHeader {

		protected AdxHeader3(RandomAccessFile file) throws IOException {
			super(file);
			unknown = new byte[4];
			file.read(unknown);
			loopFlag = file.readInt();
			loopStartSample = file.readInt();
			loopStartByte = file.readInt();
			loopEndSample = file.readInt();
			loopEndByte = file.readInt();
		}
	}

	/*
	 * Address Size Contains 
	 * 0x14 16 unknown 
	 * 0x24 4 loop flag 
	 * 0x28 4 loop start sample 
	 * 0x2c 4 loop start byte 
	 * 0x30 4 loop end sample 
	 * 0x34 4 loop end byte
	 */
	public static class AdxHeader4 extends AdxHeader {

		protected AdxHeader4(RandomAccessFile file) throws IOException {
			super(file);
			unknown = new byte[16];
			file.read(unknown);
			loopFlag = file.readInt();
			loopStartSample = file.readInt();
			loopStartByte = file.readInt();
			loopEndSample = file.readInt();
			loopEndByte = file.readInt();
		}

	}

	public static AdxHeader getHeader(RandomAccessFile file) throws IOException {
		file.seek(4);
		int version = file.readByte();
		AdxHeader header = null;
		switch (version) {
		case 0x03:
			header = new AdxHeader3(file);
			break;
		case 0x04:
			header = new AdxHeader4(file);
			break;

		default:
			throw new IllegalArgumentException("Unknow version");
		}
		return header;
	}

	private AdxHeader header;
	private RandomAccessFile file;

	public Adx(File pFile) throws IOException {
		file = new RandomAccessFile(pFile, "r");
		header = getHeader(file);
		init();
	}

	// Previously decoded samples from each channel,
	// zeroed at start (size =2*channel_count)
	private PREV[] past_samples;
	private int sample_index = 0; // sample_index is the index of sample set
	// that needs
	// to be decoded next

	private int[] coefficient = new int[2];

	public static int ntohs(int value) {

		int a = (value >>> 24) & 0xff;
		int r = (value >>> 16) & 0xff;
		int g = (value >>> 8) & 0xff;
		int b = value & 0xff;
		int[] res = new int[4];

		res[0] = b;
		res[1] = g;
		res[2] = r;
		res[3] = a;

		return ((res[0] & 0xff) << 24) | ((res[1] & 0xff) << 16)
				| ((res[2] & 0xff) << 8) | (res[3] & 0xff);
	}

	private void init() {
		past_samples = new PREV[2];
		past_samples[0] = new PREV();
		past_samples[1] = new PREV();
		double a, b, c;
		double frequency_cutoff = ntohs(header.getHighPassCutOff());
		a = Math.sqrt(2.0)
				- Math.cos(2.0 * Math.PI
						* (frequency_cutoff / header.getSampleRate()));
		b = Math.sqrt(2.0) - 1.0;
		c = (a - Math.sqrt((a + b) * (a - b))) / b; // (a+b)*(a-b) = a*a+b*b,
		// however in floating point
		// the "simpler" way is less
		// accurate

		// int_fast16_t coefficient[2];
		coefficient[0] = (int) (c * 8192.0);
		coefficient[1] = (int) (c * c * -4096.0);

	}

	// buffer is where the decoded samples will be put
	// samples_needed states how many 'sets' (one sample from every channel)
	// need to be decoded to fill the buffer
	// looping_enabled is a boolean flag to control use of the builtin loop
	// Returns the number of samples slots in the buffer that could not be
	// filled
	public int decodeAdxStandard(OutputStream buffer, int samples_needed,
			boolean loopingEnabled) throws IOException {
		
		final int samples_per_block = (header.getBlockSize() - 2) * 8
				/ header.getBitsPerSample();
		int[] scale = new int[header.getChannelCount()];

		if (loopingEnabled && !(header.getLoopFlag() != 0))
			loopingEnabled = false;

		// Loop until the requested number of samples is decoded, or the end of
		// file is reached
			// Calculate the number of samples that are left to be decoded in
			// the current block
			int sample_offset = sample_index % samples_per_block;
			int samples_can_get;
			if (samples_per_block - sample_offset > samples_needed)
				samples_can_get = samples_needed;
			else
				samples_can_get = samples_per_block - sample_offset;

			// Clamp the number of samples to be acquired if the stream isn't
			// long enough or the loop trigger is nearby
			if (loopingEnabled
					&& sample_index + samples_can_get > header
							.getLoopEndSample())
				samples_can_get = header.getLoopEndSample() - sample_index;
			else if (sample_index + samples_can_get > header.getSampleCount())
				samples_can_get = header.getSampleCount() - sample_index;

			// Find the start of the frame that sample_index resides in and
			// record the location
			long started_at = (long) ((header.getDataOffset() + 4 + sample_index
					/ samples_per_block
					* header.getBlockSize()
					* header.getChannelCount()) * 8);

			
			// Save the bitstream address of the first sample immediately after
			// the scale in the first block of the frame
			started_at += 16;
			file.seek(started_at);
			int wsize;
			while (samples_needed > 0 && sample_index < header.getSampleCount() && sample_index < header.getLoopEndSample()) {
				short[] tmpbuf = new short[bufferSize * 2 * 2];
				int i;

				convert(0, tmpbuf, file, past_samples[0]);
				convert(bufferSize * 2, tmpbuf, file, past_samples[1]);
				for (i = 0; i < bufferSize * 2; i++) {
					buffer.write((tmpbuf[i] >>> 8) & 0xFF);
					buffer.write((tmpbuf[i + bufferSize * 2] >>> 0) & 0xFF);
				}
				if (samples_needed > bufferSize * 2)
					wsize = bufferSize * 2;
				else
					wsize = samples_needed;
				samples_needed -= wsize;
				sample_index += wsize;

			}

			// Check if we hit the loop end marker, if we did we need to jump to
			// the loop start
			if (loopingEnabled && sample_index >= header.getLoopEndSample())
				sample_index = header.getLoopStartSample();

		return samples_needed;
	}

	static int sign_extend(int x, int len) {
		int signbit = (1 << (len - 1));
		int mask = (signbit << 1) - 1;
		return ((x & mask) ^ signbit) - signbit;
	}

	public void close() throws IOException {
		file.close();
	}

	class PREV {
		int s1, s2;
	}

	// #define BASEVOL 0x11e0
	final int BASEVOL = 0x4000;
	final int bufferSize = 1;
	void convert(int indxOut, short[] out, RandomAccessFile in, PREV prev)
			throws IOException {
		int pos = (int) in.getFilePointer();
		int scale = ((in.read() << 8) | (in.read()));
		int i;
		int s0, s1, s2, d;
		// int over=0;

		in.seek(pos + 2);
		pos += 2;
		s1 = prev.s1;
		s2 = prev.s2;
		for (i = 0; i < bufferSize; i++) {
			in.seek(pos + i);
			d = in.read() >> 4;
			if ((d & 8) != 0)
				d -= 16;
			s0 = (BASEVOL * d * scale + 0x7298 * s1 - 0x3350 * s2) >> 14;
			// if (abs(s0)>32767) over=1;
			if (s0 > 32767)
				s0 = 32767;
			else if (s0 < -32768)
				s0 = -32768;
			out[indxOut++] = (short) s0;
			s2 = s1;
			s1 = s0;

			in.seek(pos + i);
			d = in.read() & 15;
			if ((d & 8) != 0)
				d -= 16;
			s0 = (BASEVOL * d * scale + 0x7298 * s1 - 0x3350 * s2) >> 14;
			// if (abs(s0)>32767) over=1;
			if (s0 > 32767)
				s0 = 32767;
			else if (s0 < -32768)
				s0 = -32768;
			out[indxOut++] = (short) s0;
			s2 = s1;
			s1 = s0;
		}
		prev.s1 = s1;
		prev.s2 = s2;

		// if (over) putchar('.');
	}

}
