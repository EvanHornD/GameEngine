package Engine.components;

public class SpriteRenderer extends Component {

    private boolean firstTime = true;

    @Override
    public void start(){
        System.out.println("im COOming");
    }

    @Override
    public void update(float dt) {
        if(firstTime){
            System.out.println("I am Updattin");
            firstTime = false;
        }
    }

}
