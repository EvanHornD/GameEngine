package Engine.components;

import Engine.GameObject;

public abstract class Component {

    public GameObject gameObject;

    public void start(){}

    public abstract void update(float dt);
}
