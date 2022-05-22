package ModelLoader;

import UtilsModel.Model;

import java.util.LinkedList;

public interface Loader{
    LinkedList<Model> load(String path);
}