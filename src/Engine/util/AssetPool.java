package Engine.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import Engine.renderer.*;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();

    public static Shader getShader(String shaderPath){
        File file = new File(shaderPath);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())){
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(shaderPath);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String texturePath){
        File file = new File(texturePath);
        if(AssetPool.textures.containsKey(file.getAbsolutePath())){
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture(texturePath);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }
}
