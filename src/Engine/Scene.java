package Engine;


import java.util.ArrayList;
import java.util.List;

import Engine.renderer.Camera;

public abstract class Scene {

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
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject object){
        if(!isRunning){
            gameObjects.add(object);
        } else {
            gameObjects.add(object);
            object.start();
        }
    }

    public abstract void update(float dt);
}