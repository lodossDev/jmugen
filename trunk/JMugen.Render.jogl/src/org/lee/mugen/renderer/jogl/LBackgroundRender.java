//package org.lee.mugen.renderer.jogl;
//
//import javax.media.opengl.GL;
//import javax.media.opengl.glu.GLU;
//
//import org.lee.mugen.renderer.GraphicsWrapper;
//
//import net.java.joglutils.model.ModelFactory;
//import net.java.joglutils.model.ModelLoadException;
//import net.java.joglutils.model.iModel3DRenderer;
//import net.java.joglutils.model.examples.DisplayListRenderer;
//import net.java.joglutils.model.geometry.Model;
//
//public class LBackgroundRender {
//	public static LBackgroundRender instance = new LBackgroundRender();
//    private GLU glu = new GLU();
//    private Model model;
//    private iModel3DRenderer modelRenderer;
//    /** Ambient light array */
//    float[] lightAmbient = {0.3f, 0.3f, 0.3f, 1.0f};
//    
//    /** Diffuse light array */
//    float[] lightDiffuse = {0.5f, 0.5f, 0.5f, 1.0f};
//    
//    /** Specular light array */
//    float[] lightSpecular = {0.5f, 0.5f, 0.5f, 1.0f};
//    
//    boolean isInit = false;
//    
//	public void render(float x, float y) {
//		final GL gl = ((JoglGameWindow)GraphicsWrapper.getInstance().getInstanceOfGameWindow()).getGl();
//        if (!isInit) {
//        	
//        	try
//            {
//                // Get an instance of the display list renderer a renderer
//                modelRenderer = DisplayListRenderer.getInstance();
//                
//                // Turn on debugging
//                modelRenderer.debug(true);
//                
//                // Call the factory for a model from a local file
////                model = ModelFactory.createModel("C:\\models\\apollo.3ds");
//                
//                // Call the factory for a model from a jar file
//                model = ModelFactory.createModel("net/java/joglutils/model/examples/models/max3ds/globe.3ds");
//
//                
//                // When loading the model, adjust the center to the boundary center
//                model.centerModelOnPosition(true);
//
//                model.setUseTexture(true);
//
//                // Render the bounding box of the entire model
//                model.setRenderModelBounds(false);
//
//                // Render the bounding boxes for all of the objects of the model
//                model.setRenderObjectBounds(false);
//
//                // Make the model unit size
//                model.setUnitizeSize(true);
//
//                
//            }
//            catch (ModelLoadException ex)
//            {
//                ex.printStackTrace();
//            }
//            
//            
//            // Set the light
//            float lightPosition[] = { 0, 50000000, 0, 1.0f };
//            float[] model_ambient = {0.5f, 0.5f, 0.5f, 1.0f};
//        
//            gl.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, model_ambient, 0);
//            gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPosition, 0);
//            gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, lightDiffuse, 0);
//            gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, lightAmbient, 0);
//            gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, lightSpecular, 0);
//
//            gl.glEnable(GL.GL_LIGHT0);
//            gl.glEnable(GL.GL_LIGHTING);
//            gl.glEnable(GL.GL_NORMALIZE);
//
//            gl.glEnable(GL.GL_CULL_FACE);
//            gl.glShadeModel(GL.GL_SMOOTH);
//            gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//            gl.glClearDepth(1.0f);
//            gl.glEnable(GL.GL_DEPTH_TEST);
//            gl.glDepthFunc(GL.GL_LEQUAL);
//            gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
//            //gl.glLightModeli(GL.GL_LIGHT_MODEL_TWO_SIDE, 0);
//            
//            gl.glMatrixMode(GL.GL_MODELVIEW);
//            gl.glPushMatrix();
//        	isInit = true;
//        }
//        gl.glDisable(GL.GL_LIGHT0);
//        gl.glDisable(GL.GL_LIGHTING);
//        final float h = (float)640 / (float)480;
//        gl.glViewport(0, 0, 640, 480);
//        gl.glMatrixMode(GL.GL_PROJECTION);
//        gl.glLoadIdentity();
//        gl.glOrtho(-1, 1, -1, 1, -500, 500);
//        gl.glOrtho(0, 640, 480, 0, 0, 0);
//        gl.glMatrixMode(GL.GL_MODELVIEW);
//        gl.glLoadIdentity();
//		
//		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
//        gl.glLoadIdentity();
////        gl.glMatrixMode(GL.GL_PROJECTION);
////        gl.glLoadIdentity();
//
//
////        glu.gluLookAt(0,0,10, 0,0,0, 0,1,0);
//        // Make sure the rotations don't keep growing
//        
//        // Draw the scene (by default, the lighting, material and textures 
//        // are enabled/disabled within the renderer for the model)
//        gl.glPushMatrix();
//            // Scale the model (used for zooming purposes)
//
//        float xAdd = -220f/640f;
//        float yAdd = -50f/480f;
//        gl.glTranslatef(xAdd + (x/640f)*2 + ((float) Math.cos(angle)/4)*1.8f,
//        		-yAdd -y/480f * 2+ ((float) Math.sin(angle)/4), (float)Math.cos(angle)*5);
//        angle+=0.05;
//        gl.glScaled(0.4f, 0.5f, 0);
//        gl.glRotated(angle2+=1, -1f, 1, 0); 
//            // Rotate the model based on mouse inputs
//            modelRenderer.render(gl, model);
//        gl.glPopMatrix();
//        
//        gl.glFlush();
//	}
//	float angle = 0;
//	float angle2 = 0;
//}
