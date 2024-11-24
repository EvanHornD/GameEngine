package Engine.components;

import org.joml.Vector2f;

public class Transform {
    
    public Vector2f position;
    public Vector2f scale;

    public Transform(){
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f pos){
        init(pos, new Vector2f());
    }

    public Transform(Vector2f pos, Vector2f scale){
        init(pos, scale);
    }

    public void init(Vector2f pos, Vector2f scale){
        this.position = pos;
        this.scale = scale;
    }

}
