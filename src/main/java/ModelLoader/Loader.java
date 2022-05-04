package ModelLoader;

import UtilsCommon.Model;

import java.util.LinkedList;

public interface Loader{
    LinkedList<Model> load(String path);
}