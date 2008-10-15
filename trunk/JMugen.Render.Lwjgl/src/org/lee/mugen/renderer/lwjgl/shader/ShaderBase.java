package org.lee.mugen.renderer.lwjgl.shader;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

public class ShaderBase extends Shader {
	static Map<String, ShaderBase> shaders = new HashMap<String, ShaderBase>();
	public static ShaderBase getShader(String type) {
		ShaderBase shader = shaders.get(type);
		if (shader == null) {
			shader = new ShaderBase(type);
			shaders.put(type, shader);
		}
		return shader;
	}


//	final String vshFile;
//	final ByteBuffer vshSource;

//	final int vshID;

	final String fshFile;
	final ByteBuffer fshSource;

	final int fshID;

	final int programID;

//	final int uniformLocation;

	public ShaderBase(String type) {
		// Initialize the vertex shader.
//		this.vshFile = "add";
//		vshSource = getShaderText("add");

//		vshID = ARBShaderObjects
//				.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
//		ARBShaderObjects.glShaderSourceARB(vshID, vshSource);
//		ARBShaderObjects.glCompileShaderARB(vshID);

//		printShaderObjectInfoLog(this.vshFile, vshID);

//		ARBShaderObjects.glGetObjectParameterARB(vshID,
//				ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB, programBuffer);
//		if (programBuffer.get(0) == GL11.GL_FALSE)
//			System.err.println("A compilation error occured in a vertex shader.");

		// Initialize the fragment shader.
		this.fshFile = type;
		fshSource = getShaderText(type);

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

//		uniformLocation = getUniformLocation(programID, "UNIFORMS");
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
