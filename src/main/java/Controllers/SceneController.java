package Controllers;

import ModelLoader.OBJLoader;
import EntityTree.EntityEmpty;
import EntityTree.Entity;
import EntityTree.EntityModel;
import UtilsModel.Model;

public class SceneController {
    private SceneController(){}

    private static SceneController instance;

    public static SceneController getInstance(){
        if(instance == null)
            instance = new SceneController();
        return instance;
    }

    private Entity root;

    public Entity getRoot(){
        return root;
    }

    public void initialize(){
        root = new EntityEmpty();
        OBJLoader loader = new OBJLoader();
        var models = loader.load("src/main/data/cube.obj");

        for(Model model: models){
            new EntityModel(model,root);
        }

    }

    public void update(){
        root.update();
    }

}
