package org.lee.mugen.renderer.lwjgl.shader;

import java.nio.ByteBuffer;

import org.lee.mugen.renderer.RGB;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

public class AfterImageShader extends Shader {


//	final String vshFile;
//	final ByteBuffer vshSource;

//	final int vshID;

	final String fshFile;
	final ByteBuffer fshSource;

	final int fshID;

	final int programID;


	private int palbrightUniPos;
	private int palcontrastUniPos;
	private int palpostbrightUniPos;
	
	private int mulUniPos;
	private int addUniPos;
	

	public AfterImageShader() {
		this.fshFile = "afterimage";
		fshSource = getShaderText("afterimage");

		fshID = ARBShaderObjects
				.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		ARBShaderObjects.glShaderSourceARB(fshID, fshSource);
		ARBShaderObjects.glCompileShaderARB(fshID);

		printShaderObjectInfoLog(this.fshFile, fshID);

		ARBShaderObjects.glGetObjectParameterARB(fshID,
				ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB, programBuffer);
		if (programBuffer.get(0) == GL11.GL_FALSE)
			System.err.println("A compilation error occured in a fragment shader.");

		// Initialize the shader program.
		programID = ARBShaderObjects.glCreateProgramObjectARB();

//		ARBShaderObjects.glAttachObjectARB(programID, vshID);
		ARBShaderObjects.glAttachObjectARB(programID, fshID);

		ARBShaderObjects.glLinkProgramARB(programID);

		printShaderProgramInfoLog(programID);

		ARBShaderObjects.glGetObjectParameterARB(programID,
				ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB, programBuffer);
		if (programBuffer.get(0) == GL11.GL_FALSE)
			System.err.println("A linking error occured in a shader program.");
		palbrightUniPos = getUniformLocation(programID, "palbright");
		palcontrastUniPos = getUniformLocation(programID, "palcontrast");
		palpostbrightUniPos = getUniformLocation(programID, "palpostbright");
		
		mulUniPos = getUniformLocation(programID, "mul");
		addUniPos = getUniformLocation(programID, "add");
	}

	public void render(RGB palbright, RGB palcontrast, RGB palpostbright, RGB add, RGB mul) {
		ARBShaderObjects.glUseProgramObjectARB(programID);
		
		ARBShaderObjects.glUniform4fARB(palbrightUniPos, palbright.getR(), palbright.getG(), palbright.getB(), palbright.getA());
		ARBShaderObjects.glUniform4fARB(palcontrastUniPos, palcontrast.getR(), palcontrast.getG(), palcontrast.getB(), palcontrast.getA());
		ARBShaderObjects.glUniform4fARB(palpostbrightUniPos, palpostbright.getR(), palpostbright.getG(), palpostbright.getB(), palpostbright.getA());

		ARBShaderObjects.glUniform4fARB(addUniPos, add.getR(), add.getG(), add.getB(), add.getA());
		ARBShaderObjects.glUniform4fARB(mulUniPos, mul.getR(), mul.getG(), mul.getB(), mul.getA());
	}
	public void render() {
		ARBShaderObjects.glUseProgramObjectARB(programID);

	}
	public void cleanup() {
//		ARBShaderObjects.glDetachObjectARB(programID, vshID);
		ARBShaderObjects.glDetachObjectARB(programID, fshID);

//		ARBShaderObjects.glDeleteObjectARB(vshID);
		ARBShaderObjects.glDeleteObjectARB(fshID);

		ARBShaderObjects.glDeleteObjectARB(programID);
	}



}
