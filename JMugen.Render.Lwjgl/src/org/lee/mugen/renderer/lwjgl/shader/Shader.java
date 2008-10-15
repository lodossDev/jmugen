package org.lee.mugen.renderer.lwjgl.shader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBProgram;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

public abstract class Shader {

	private static final IntBuffer int_buffer = BufferUtils.createIntBuffer(16);
	protected static IntBuffer programBuffer = BufferUtils.createIntBuffer(1);
	protected static ByteBuffer fileBuffer = BufferUtils.createByteBuffer(1024 * 10);

	protected Shader() {
	}

	public abstract void render();
	public void endRender() {
		ARBShaderObjects.glUseProgramObjectARB(0);
	}
	

	
	public abstract void cleanup();

	/**
	 * Obtain a GL integer value from the driver
	 *
	 * @param gl_enum The GL value you want
	 *
	 * @return the integer value
	 */
	public static int glGetInteger(int gl_enum) {
		GL11.glGetInteger(gl_enum, int_buffer);
		return int_buffer.get(0);
	}

	protected static ByteBuffer getShaderText(String file) {
		ByteBuffer shader = null;
		fileBuffer = BufferUtils.createByteBuffer(1024 * 10);
		try {
			ClassLoader loader = Shader.class.getClassLoader();
			InputStream inputStream = loader.getResourceAsStream("org/lee/mugen/renderer/lwjgl/shader/" + file);

//			if ( inputStream == null )
//				kill("A shader source file could not be found: " + file);

			BufferedInputStream stream = new BufferedInputStream(inputStream);

			byte character;
			while ( (character = (byte)stream.read()) != -1 )
				fileBuffer.put(character);

			stream.close();

			fileBuffer.flip();

			shader = BufferUtils.createByteBuffer(fileBuffer.limit());
			shader.put(fileBuffer);

			shader.clear();
			fileBuffer.clear();
		} catch (IOException e) {
//			kill("Failed to read the shader source file: " + file, e);
		}

		return shader;
	}

	protected static void checkProgramError(String programFile, ByteBuffer programSource) {
		if ( GL11.glGetError() == GL11.GL_INVALID_OPERATION ) {
			programSource.clear();
			final byte[] bytes = new byte[programSource.capacity()];
			programSource.get(bytes);

			final int errorPos = glGetInteger(ARBProgram.GL_PROGRAM_ERROR_POSITION_ARB);
			int lineStart = 0;
			int lineEnd = -1;
			for ( int i = 0; i < bytes.length; i++ ) {
				if ( bytes[i] == '\n' ) {
					if ( i <= errorPos ) {
						lineStart = i + 1;
					} else {
						lineEnd = i;
						break;
					}
				}
			}

			if ( lineEnd == -1 )
				lineEnd = bytes.length;

//			kill("Low-level program error in file: " + programFile
//			                 + "\n\tError line: " + new String(bytes, lineStart, lineEnd - lineStart)
//			                 + "\n\tError message: " + GL11.glGetString(ARBProgram.GL_PROGRAM_ERROR_STRING_ARB));
		}
	}

	protected static int getUniformLocation(int ID, String name) {
		fileBuffer.clear();

		int length = name.length();

		char[] charArray = new char[length];
		name.getChars(0, length, charArray, 0);

		for ( int i = 0; i < length; i++ )
			fileBuffer.put((byte)charArray[i]);
		fileBuffer.put((byte)0); // Must be null-terminated.
		fileBuffer.flip();

		final int location = ARBShaderObjects.glGetUniformLocationARB(ID, fileBuffer);

		if ( location == -1 )
			throw new IllegalArgumentException("The uniform \"" + name + "\" does not exist in the Shader Program.");

		return location;
	}

	protected static void printShaderObjectInfoLog(String file, int ID) {
		ARBShaderObjects.glGetObjectParameterARB(ID, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, programBuffer);

		final int logLength = programBuffer.get(0);

		if ( logLength <= 1 )
			return;

		final ByteBuffer log = BufferUtils.createByteBuffer(logLength);

		ARBShaderObjects.glGetInfoLogARB(ID, null, log);

		final char[] charArray = new char[logLength];
		for ( int i = 0; i < logLength; i++ )
			charArray[i] = (char)log.get();

		System.out.println("\nInfo Log of Shader Object: " + file);
		System.out.println("--------------------------");
		System.out.println(new String(charArray, 0, logLength));
	}

	protected static void printShaderProgramInfoLog(int ID) {
		ARBShaderObjects.glGetObjectParameterARB(ID, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, programBuffer);

		final int logLength = programBuffer.get(0);

		if ( logLength <= 1 )
			return;

		final ByteBuffer log = BufferUtils.createByteBuffer(logLength);

		ARBShaderObjects.glGetInfoLogARB(ID, null, log);

		final char[] charArray = new char[logLength];
		for ( int i = 0; i < logLength; i++ )
			charArray[i] = (char)log.get();

		System.out.println("\nShader Program Info Log: ");
		System.out.println("--------------------------");
		System.out.println(new String(charArray, 0, logLength));
	}

	public void rezet() {
		ARBShaderObjects.glUseProgramObjectARB(0);
	}

}
