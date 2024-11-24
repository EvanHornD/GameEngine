package Engine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import Engine.userInput.*;
import Engine.util.Time;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    int width, height;
    String title;
    private long glfwWindow;
    float r = 1.0f;
    float g = 1.0f;
    float b = 1.0f;

    private static Scene currentScene;

    private static Window window = null;
    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "engine";
    }

    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
               currentScene =  new LevelEditorScene();
               currentScene.init();
               currentScene.start();
            break;

            case 1:
                currentScene =  new LevelScene();
                currentScene.init();
                currentScene.start();
            break;
            default: break;
        }
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // clear the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // closes the application
        glfwTerminate();
        glfwSetErrorCallback(null);
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        // initializes glfw, printing an error if unable to
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        // Configure The GLFW Window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);

        // Create The Window and store its memory address in the GLFWWindow variable
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow==NULL){
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        // set the call backs GLFW Checks for when this method is called glfwPollEvents();
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);

        // allows GLFW to access the openGL C bindings
        GL.createCapabilities();

        // initializes the scene to be the level editor
        Window.changeScene(0);
    }

    float beginTime = Time.getTime();
    float endTime = Time.getTime();
    float dt = -1.0f;

    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();

            // sets and uses the color the window uses to clear the screen
            glClearColor(r,g,b,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            int up = KeyBinds.getActionFrame("Up");
            if(up!=0){
                System.out.println(up);
            }
            int input = KeyBinds.getActionFrame("Input");
            if(input!=0){
                System.out.println(input);
            }

            if(dt>=0){
                currentScene.update(dt);
            }

            glfwSwapBuffers(glfwWindow);

            // get delta time
            KeyBinds.updateKeyActions();
            endTime = Time.getTime();
            dt = endTime-beginTime;
            beginTime = Time.getTime();
        }
    }
}
