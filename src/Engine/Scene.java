package Engine;


import java.util.ArrayList;
import java.util.List;

import Engine.renderer.Camera;
import Engine.renderer.Renderer;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;

    protected List<GameObject> gameObjects;

    public Scene(){
        gameObjects = new ArrayList<>();
    }

    public void init(){}

    public void start(){
        for (GameObject object : gameObjects) {
            object.start();
            this.renderer.add(object);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject object){
        if(!isRunning){
            gameObjects.add(object);
        } else {
            gameObjects.add(object);
            object.start();
            this.renderer.add(object);
        }
    }

    public Camera getCamera(){
        return this.camera;
    }

    public abstract void update(float dt);
}