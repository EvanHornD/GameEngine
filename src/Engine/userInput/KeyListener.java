package Engine.userInput;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {

    private static KeyListener instance;
    private boolean[] keyPressed = new boolean[350];

    public KeyListener(){
    }

    public static KeyListener get(){
        if(instance == null){
            instance = new KeyListener();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS){
            if(KeyBinds.containsKeyBind(key)){
                String actionName = KeyBinds.keyBoardBindings.get(key);
                KeyBinds.Actions.replace(actionName,1);
            }
            get().keyPressed[key] = true;
        } else if(action == GLFW_RELEASE) {
            if(KeyBinds.containsKeyBind(key)){
                String actionName = KeyBinds.keyBoardBindings.get(key);
                KeyBinds.Actions.replace(actionName,-1);
            }
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key){
        return key<get().keyPressed.length && get().keyPressed[key];
    }
}