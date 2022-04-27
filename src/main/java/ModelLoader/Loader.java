package ModelLoader;

import java.util.LinkedList;

public interface Loader{
    LinkedList<UtilsCommon.Mesh> load(String path);
}