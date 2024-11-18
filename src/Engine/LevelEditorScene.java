package Engine;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import Engine.components.*;
import Engine.renderer.Camera;
import Engine.renderer.Shader;
import Engine.renderer.Texture;
import Engine.util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
        // position               // color                  // UV Coordinates
        100f,   0f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f,     1, 1, // Bottom right 0
          0f, 100f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f,     0, 0, // Top left     1
        100f, 100f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f,     1, 0, // Top right    2
          0f,   0f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f,     0, 1  // Bottom left  3
    };

    // IMPORTANT: must be in counter clock wise order
    private int[] elementArray = {
        // top right triangle

        2,1,0,

        // _____
        //  \  |
        //   \ |
        //    \|

        // bottom left triangle

        0,1,3

        // |\  
        // | \ 
        // |  \
        // -----
    };

    private int VAOID, VBOID, EBOID;

    private Shader defaultShader;
    private Texture testTexture;

    GameObject testObject;
    boolean firstTime = true;

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        this.testObject = new GameObject("test object");
        this.testObject.addComponent(new SpriteRenderer());
        this.testObject.addComponent(new FontRenderer());
        this.addGameObjectToScene(testObject);

        this.camera = new Camera(new Vector2f(-200, -300));
        defaultShader = new Shader("assets\\shaders\\default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets\\images\\tetrisGreyScale.png");

        VAOID = glGenVertexArrays();
        glBindVertexArray(VAOID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        VBOID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBOID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        EBOID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
    
        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        // Bind the VAO that we're using
        glBindVertexArray(VAOID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        if(firstTime){
            System.out.println("creating game object");
            this.testObject = new GameObject("test object");
            this.testObject.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(testObject);
            firstTime = false;
        }

        for (GameObject gameObject : this.gameObjects) {
            gameObject.update(dt);
            
        }
    }
}