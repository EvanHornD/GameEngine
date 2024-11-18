package Engine.userInput;


import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class KeyBinds {

    private static KeyBinds instance;

    static Map<Integer, String> keyBoardBindings = new HashMap<>(Map.of(
        GLFW_KEY_UP, "Up",
        GLFW_KEY_W, "Up",
        GLFW_KEY_DOWN, "Down",
        GLFW_KEY_S, "Down",
        GLFW_KEY_LEFT, "Left",
        GLFW_KEY_A, "Left",
        GLFW_KEY_RIGHT, "Right",
        GLFW_KEY_D, "Right",
        GLFW_KEY_SPACE, "test"
    ));

    static Map<Integer, String> mouseBindings = new HashMap<>(Map.of(
        GLFW_MOUSE_BUTTON_1, "Input"
    ));

    static Map<String, Integer> Actions = new HashMap<>(Map.of(
        "Up", 0,
        "Down", 0,
        "Left", 0,
        "Right", 0,
        "Input", 0,
        "test",0
    ));

    public KeyBinds() {
    }

    public static KeyBinds get(){
        if(instance == null){
            instance = new KeyBinds();
        }
        return instance;
    }

    public static boolean containsKeyBind(int key){
        get();
        return KeyBinds.keyBoardBindings.containsKey(key);
    }

    public static boolean containsMouseBind(int key){
        get();
        return KeyBinds.mouseBindings.containsKey(key);
    }

    public static int getActionFrame(String action){
        get();
        return KeyBinds.Actions.get(action);
    }

    public static void addKeyBind(int key, String action){
        KeyBinds.keyBoardBindings.put(key, action);
    }

    public static void addMouseBind(int button, String action){
        KeyBinds.mouseBindings.put(button, action);
    }

    public static void addAction(String action){
        KeyBinds.Actions.put(action,0);
    }

    public static void updateKeyActions(){
        get();
        for (Map.Entry<String,Integer> action : KeyBinds.Actions.entrySet()) {
            String actionName = action.getKey();
            int actionFrame = action.getValue();
            if(actionFrame>0){
                KeyBinds.Actions.replace(actionName, actionFrame+1);
            } else if(actionFrame<0){
                KeyBinds.Actions.replace(actionName, 0);
            }
        }
    }
}
