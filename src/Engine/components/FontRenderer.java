package Engine.components;

public class FontRenderer extends Component {

    private boolean firstTime = true;

    @Override
    public void start(){
        if ( gameObject.getComponent(SpriteRenderer.class) != null){
            System.out.println("found font renderer");
        }
    }

    @Override
    public void update(float dt) {
        if(firstTime){
            System.out.println("I am Updattin");
            firstTime = false;
        }
    }
}
