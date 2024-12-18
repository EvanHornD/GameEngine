package Engine;

import org.joml.Vector2f;
import org.joml.Vector4f;

import Engine.components.SpriteRenderer;
import Engine.components.Transform;
import Engine.renderer.Camera;
import Engine.util.AssetPool;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600-xOffset*2);
        float totalHeight = (float)(300-yOffset*2);
        float sizeX = totalWidth/100.0f;
        float sizeY = totalHeight/100.0f;

        for(int x=0; x<100; x++){
            for(int y=0; y<100; y++){
                float xPos = xOffset+(x*sizeX);
                float yPos = yOffset+(y*sizeY);

                GameObject object = new GameObject("test"+x+" "+y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY))); 
                object.addComponent(new SpriteRenderer(new Vector4f(xPos/totalWidth,yPos/totalHeight,1,1)));
                this.addGameObjectToScene(object);
            }
        }

        loadResources();
    }

    private void loadResources(){
        AssetPool.getShader("assets\\shaders\\default.glsl");
    }

    @Override
    public void update(float dt) {
        System.out.println(1.0f/dt);
        for (GameObject gameObject : this.gameObjects) {
            gameObject.update(dt);
        }
        this.renderer.render();
    }
}