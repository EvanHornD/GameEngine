package Engine;
import java.util.ArrayList;
import java.util.List;
import Engine.components.*;

public class GameObject {

    private String name;
    private List<Component> components;
    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for(Component c : components){
            if(componentClass.isAssignableFrom(c.getClass())){
                try {
                    return (componentClass).cast(c);
                } catch (ClassCastException e) {
                    System.out.println("Error: Casting component");
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for(int i = 0; i < components.size(); i++){
            Component component = components.get(i);
            if(componentClass.isAssignableFrom(component.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component){
        this.components.add(component);
        component.gameObject = this;
    }

    public void update(float dt){
        for(int i = 0; i < components.size(); i++){
            components.get(i).update(dt);
        }
    }

    public void start(){
        for(int i = 0; i < components.size(); i++){
            components.get(i).start();
        }
    }
}
