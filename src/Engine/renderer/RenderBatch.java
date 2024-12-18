package Engine.renderer;

import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import Engine.Window;
import Engine.components.SpriteRenderer;
import Engine.util.AssetPool;

public class RenderBatch {

    // Vertex
    // ======
    // Pos                  Color
    // float, float,        float, float, float, float
    
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = (POS_OFFSET + POS_SIZE) * Float.BYTES;

    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;


    public RenderBatch(int maxBatchSize){
        shader = AssetPool.getShader("assets\\shaders\\default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize*4*VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void start(){
        // generate the Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length*Float.BYTES, GL_DYNAMIC_DRAW);

        // create the indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //enable the buffer attribute pointers (the position and color offsets)
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    private int[] generateIndices(){
        // 6 indices per quad(3 per tri)
        int[] elements = new int[6*maxBatchSize];
        for(int i=0; i<maxBatchSize; i++){
            loadQuadIndices(elements, i);
        }
        return elements;
    }

    private void loadQuadIndices(int[] elements, int index){
        int offsetArrayIndex = 6*index;
        int offset = 4*index;

        // first triangle
        elements[offsetArrayIndex] = offset+3;
        elements[offsetArrayIndex+1] = offset+2;
        elements[offsetArrayIndex+2] = offset+0;

        // second triangle
        elements[offsetArrayIndex+3] = offset+0;
        elements[offsetArrayIndex+4] = offset+2;
        elements[offsetArrayIndex+5] = offset+1;
    }

    public void addSprite(SpriteRenderer sprite){

        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;

        loadVertexProperties(index);

        if(numSprites>= this.maxBatchSize){
            this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int index){
        SpriteRenderer sprite = this.sprites[index];

        //find offset within array 
        int offset = index*4*VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            if(i==1){
                yAdd = 0.0f;
            } else if (i==2){
                xAdd = 0.0f;
            } else if (i==3){
                yAdd = 1.0f;
            }

            //load position
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd*sprite.gameObject.transform.scale.x);
            vertices[offset+1] = sprite.gameObject.transform.position.y + (yAdd*sprite.gameObject.transform.scale.y);

            //load Color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            offset+=VERTEX_SIZE;
        }
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

    public void render(){
        // rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        //apply Shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites*6, GL_UNSIGNED_INT,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }
}
