package org.lee.mugen.audio.adx;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class Adx {

	public static class ADXHeader3 {
		public static final int V3 = 0x03;
		private int head0x80;
		private int head0x00;
		private int copyrightOffset; // ushort
		private int encodingType; // ubyte
		private int blockSize; // ubyte
		private int sampleBitDepth; // ubyte
		private int channelCount; // ubyte
		private int sampleRate; // int
		private int totalSample; // int
		/*************************************/
		private int highpassFrequency; // short
		private int version; // byte
		private int flag; // byte
		private int unknown; // int
		private int loopEnable; // int
		private int loopBeginSampleIndex; // int

		/************************************/
		protected int loopBeginByteIndex; // int
		protected int loopEndSampleIndex; // int
		protected int endByteIndex; // int

		// //////////////////////
		public int getHead0x80() {
			return head0x80;
		}

		public void setHead0x80(int head0x80) {
			this.head0x80 = head0x80;
		}

		public int getHead0x00() {
			return head0x00;
		}

		public void setHead0x00(int head0x00) {
			this.head0x00 = head0x00;
		}

		public int getCopyrightOffset() {
			return copyrightOffset;
		}

		public void setCopyrightOffset(int copyrightOffset) {
			this.copyrightOffset = copyrightOffset;
		}

		public int getEncodingType() {
			return encodingType;
		}

		public void setEncodingType(int encodingType) {
			this.encodingType = encodingType;
		}

		public int getBlockSize() {
			return blockSize;
		}

		public void setBlockSize(int blockSize) {
			this.blockSize = blockSize;
		}

		public int getSampleBitDepth() {
			return sampleBitDepth;
		}

		public void setSampleBitDepth(int sampleBitDepth) {
			this.sampleBitDepth = sampleBitDepth;
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

		public int getTotalSample() {
			return totalSample;
		}

		public void setTotalSample(int totalSample) {
			this.totalSample = totalSample;
		}

		public int getHighpassFrequency() {
			return highpassFrequency;
		}

		public void setHighpassFrequency(int highpassFrequency) {
			this.highpassFrequency = highpassFrequency;
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

		public int getUnknown() {
			return unknown;
		}

		public void setUnknown(int unknown) {
			this.unknown = unknown;
		}

		public boolean getLoopEnable() {
			return loopEnable != 0;
		}

		public void setLoopEnable(int loopEnable) {
			this.loopEnable = loopEnable;
		}

		public int getLoopBeginSampleIndex() {
			return loopBeginSampleIndex;
		}

		public void setLoopBeginSampleIndex(int loopBeginSampleIndex) {
			this.loopBeginSampleIndex = loopBeginSampleIndex;
		}

		public int getLoopBeginByteIndex() {
			return loopBeginByteIndex;
		}

		public void setLoopBeginByteIndex(int loopBeginByteIndex) {
			this.loopBeginByteIndex = loopBeginByteIndex;
		}

		public int getLoopEndSampleIndex() {
			return loopEndSampleIndex;
		}

		public void setLoopEndSampleIndex(int loopEndSampleIndex) {
			this.loopEndSampleIndex = loopEndSampleIndex;
		}

		public int getEndByteIndex() {
			return endByteIndex;
		}

		public void setEndByteIndex(int endByteIndex) {
			this.endByteIndex = endByteIndex;
		}

		// ///////////////////////

	}

	public static class ADXHeader4 extends ADXHeader3 {
		public static final int V4 = 0x04;

		/*************************************/
		private int loopEndSampleIndex; // int
		private int loopEndByteIndex; // int
		private int notUse; // int

		// ///////////////////////////////////
		public int getLoopEndSampleIndex() {
			return loopEndSampleIndex;
		}

		public void setLoopEndSampleIndex(int loopEndSampleIndex) {
			this.loopEndSampleIndex = loopEndSampleIndex;
		}

		public int getLoopEndByteIndex() {
			return loopEndByteIndex;
		}

		public void setLoopEndByteIndex(int loopEndByteIndex) {
			this.loopEndByteIndex = loopEndByteIndex;
		}

		public int getNotUse() {
			return notUse;
		}

		public void setNotUse(int notUse) {
			this.notUse = notUse;
		}

		// /////////////////////////////////

		public boolean getLoopEnabled() {
			return loopBeginByteIndex != 0;
		}

		public void setLoopEnabled(int loopEnabled) {
			this.loopBeginByteIndex = loopEnabled;
		}

		public int getLoopBeginByteIndex() {
			return endByteIndex;
		}

		public void setLoopBeginByteIndex(int loopBeginByteIndex) {
			this.endByteIndex = loopBeginByteIndex;
		}

		// /////////////////////////////
	}

	public static ADXHeader3 getNewInstanceOfADXHeader(RandomAccessFile in)
			throws IOException {
//		LittleEndianDataInputStream in = null;
//		if (!(in instanceof LittleEndianDataInputStream)) {
//			in = new LittleEndianDataInputStream(pIn);
//		} else {
//			in = (LittleEndianDataInputStream) pIn;
//		}
		int head0x80 = in.read();
		int head0x00 = in.read();
		int copyrightOffset = in.readUnsignedShort(); // ushort
		int encodingType = in.read(); // ubyte
		int blockSize = in.read(); // ubyte
		int sampleBitDepth = in.read(); // ubyte
		int channelCount = in.read(); // ubyte
		int sampleRate = in.readInt(); // int
		int totalSample = in.readInt(); // int

		int highpassFrequency = in.readUnsignedShort(); // short
		int version = in.read();
		if (version == Adx.ADXHeader3.V3) {
			return getNewInstanceOfADXHeaderV3(in, head0x80, head0x00,
					copyrightOffset, encodingType, blockSize, sampleBitDepth,
					channelCount, sampleRate, totalSample, highpassFrequency,
					version);
		} else if (version == Adx.ADXHeader4.V4) {
			return getNewInstanceOfADXHeaderV4(in, head0x80, head0x00,
					copyrightOffset, encodingType, blockSize, sampleBitDepth,
					channelCount, sampleRate, totalSample, highpassFrequency,
					version);
		}

		throw new IllegalArgumentException("Only ADX V3 and V4 are decoded.");
	}

	private static ADXHeader3 getNewInstanceOfADXHeaderV3(
			RandomAccessFile in, int head0x80, int head0x00,
			int copyrightOffset, int encodingType, int blockSize,
			int sampleBitDepth, int channelCount, int sampleRate,
			int totalSample, int highpassFrequency, int version)
			throws IOException {

		ADXHeader3 header = new ADXHeader3();
		header.setHead0x80(head0x80);
		header.setHead0x00(head0x00);
		header.setCopyrightOffset(copyrightOffset);
		header.setEncodingType(encodingType);
		header.setBlockSize(blockSize);
		header.setSampleBitDepth(sampleBitDepth);
		header.setChannelCount(channelCount);
		header.setSampleRate(sampleRate);
		header.setTotalSample(totalSample);
		header.setHighpassFrequency(highpassFrequency);
		header.setVersion(version);

		int flag = in.read(); // byte
		int unknown = in.readInt(); // int
		int loopEnable = in.readInt(); // int
		int loopBeginSampleIndex = in.readInt(); // int

		int loopBeginByteIndex = in.readInt(); // int
		int loopEndSampleIndex = in.readInt(); // int
		int endByteIndex = in.readInt(); // int

		header.setFlag(flag);
		header.setUnknown(unknown);
		header.setLoopEnable(loopEnable);
		header.setLoopBeginSampleIndex(loopBeginSampleIndex);

		header.setLoopBeginByteIndex(loopBeginByteIndex);
		header.setLoopEndSampleIndex(loopEndSampleIndex);
		header.setEndByteIndex(endByteIndex);

		return header;
	}

	private static ADXHeader4 getNewInstanceOfADXHeaderV4(
			RandomAccessFile in, int head0x80, int head0x00,
			int copyrightOffset, int encodingType, int blockSize,
			int sampleBitDepth, int channelCount, int sampleRate,
			int totalSample, int highpassFrequency, int version)
			throws IOException {

		ADXHeader4 header = new ADXHeader4();
		header.setHead0x80(head0x80);
		header.setHead0x00(head0x00);
		header.setCopyrightOffset(copyrightOffset);
		header.setEncodingType(encodingType);
		header.setBlockSize(blockSize);
		header.setSampleBitDepth(sampleBitDepth);
		header.setChannelCount(channelCount);
		header.setSampleRate(sampleRate);
		header.setTotalSample(totalSample);
		header.setHighpassFrequency(highpassFrequency);
		header.setVersion(version);

		int flag = in.read(); // byte
		int unknown = in.readInt(); // int
		int loopEnable = in.readInt(); // int
		int loopBeginSampleIndex = in.readInt(); // int

		int loopBeginByteIndex = in.readInt(); // int
		@SuppressWarnings("unused")
		int loopEndSampleIndexV3 = in.readInt(); // int
		int endByteIndex = in.readInt(); // int

		int loopEndSampleIndex = in.readInt(); // int
		int loopEndByteIndex = in.readInt(); // int
		int notUse = in.readInt(); // int

		header.setFlag(flag);
		header.setUnknown(unknown);
		header.setLoopEnable(loopEnable);
		header.setLoopBeginSampleIndex(loopBeginSampleIndex);

		header.setLoopBeginByteIndex(loopBeginByteIndex);
		header.setEndByteIndex(endByteIndex);

		header.setLoopEndSampleIndex(loopEndSampleIndex);
		header.setLoopEndByteIndex(loopEndByteIndex);
		header.setNotUse(notUse);

		return header;
	}

	private ADXHeader3 header;
	private RandomAccessFile file;

	public Adx(File pFile) throws IOException {
		header = getNewInstanceOfADXHeader(file);
		file = new RandomAccessFile(pFile, "r");
		init();
	}
//	 Previously	decoded samples from each channel,	 
//	 zeroed at	 start (size =2*channel_count)
	private int[] past_samples = new int[header.getChannelCount() * 2]; 
	private int sample_index = 0; // sample_index is the index of sample set that needs
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
	    	
	    return ((res[0] & 0xff) << 24) |
	           ((res[1] & 0xff) << 16) |
	           ((res[2] & 0xff) << 8) |
	           (res[3] & 0xff);
	}
	
	private void init() {
		double a, b, c;
		double frequency_cutoff = ntohs(header.getHighpassFrequency());
		a = Math.sqrt(2.0) - Math.cos(2.0 * Math.PI * (frequency_cutoff / header.getSampleRate()));
		b = Math.sqrt(2.0) - 1.0;
		c = (a - Math.sqrt((a + b) * (a - b))) / b;    //(a+b)*(a-b) = a*a+b*b, however in floating point the "simpler" way is less accurate

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
				/ header.getSampleBitDepth();
		int[] scale = new int[header.getChannelCount()];

		if (loopingEnabled && !header.getLoopEnable())
			loopingEnabled = false;

		// Loop until the requested number of samples is decoded, or the end of
		// file is reached
		while (samples_needed != 0 && sample_index < header.getTotalSample()) {
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
							.getLoopEndSampleIndex())
				samples_can_get = header.getLoopEndSampleIndex() - sample_index;
			else if (sample_index + samples_can_get > header.getTotalSample())
				samples_can_get = header.getTotalSample() - sample_index;

			// Find the start of the frame that sample_index resides in and
			// record the location
			long started_at = (long) ((header.getCopyrightOffset() + 4 + sample_index
					/ samples_per_block
					* header.getBlockSize()
					* header.getChannelCount()) * 8);

			// Read the scale values from the start of each block in the frame
			for (int i = 0; i < header.getChannelCount(); ++i) {
				file.seek(started_at + header.getBlockSize() * i * 8);
				// scale[i] = ntohs( bitstream_read( 16 ) );
				scale[i] = ntohs(file.readShort());
			}

			long sample_endoffset = samples_can_get + sample_offset;

			// Save the bitstream address of the first sample immediately after
			// the scale in the first block of the frame
			started_at += 16;
			for (; sample_offset < sample_endoffset; ++sample_offset, ++sample_index, --samples_needed) {
				for (int i = 0; i < header.getChannelCount(); ++i) {
					// Seek to the sample offset, read and sign extend it to a 32 integer
					// Implementing sign extension is left as an exercise for
					// the reader
					// The sign extension will also need to include a endian
					// adjustment if there are more than 8 bits
					// bitstream_seek( started_at + header.getSampleBitDepth() *
					// sample_offset +
					// header.getBlockSize() * 8 * i );
					file.seek(started_at + header.getSampleBitDepth()
							* sample_offset + header.getBlockSize() * 8 * i);
					// int sample = bitstream_read( header.getSampleBitDepth()
					// );
					int sample = 0;
					if (header.getSampleBitDepth() == 8)
						sample = file.read();
					else if (header.getSampleBitDepth() == 16)
						sample = file.readShort();
					else if (header.getSampleBitDepth() == 32)
						sample = file.readInt();
					
					
					sample = sign_extend(sample, header.getSampleBitDepth());

					// Scale the error correction value
					sample *= scale[i];

					// Perform the sample prediction and add it to the error
					// correction value
					// This is fixed-point arithmetic with a 12bit fraction
					sample += (coefficient[0] * past_samples[i * 2 + 0] + coefficient[1]
							* past_samples[i * 2 + 1]) >> 12;

					// Update the past samples with the newer sample
					past_samples[i * 2 + 1] = past_samples[i * 2 + 0];
					past_samples[i * 2 + 0] = sample;

					// Clamp the decoded sample to the valid range for a 16bit
					// integer
					if (sample > 32767)
						sample = 32767;
					else if (sample < -32768)
						sample = -32768;

					// Save the sample to the buffer then advance one place // short = int 16
					buffer.write((sample >>>  8) & 0xFF);
					buffer.write((sample >>>  0) & 0xFF);
				}
			}

			// Check if we hit the loop end marker, if we did we need to jump to
			// the loop start
			if (loopingEnabled
					&& sample_index == header.getLoopEndSampleIndex())
				sample_index = header.getLoopBeginSampleIndex();
		}

		return samples_needed;
	}

	static int sign_extend(int x, int len) {
		int signbit = (1 << (len - 1));
		int mask = (signbit << 1) - 1;
		return ((x & mask) ^ signbit) - signbit;
	}

}
