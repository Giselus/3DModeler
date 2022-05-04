// Remove this file when there are other tests

import ModelLoader.Loader;
import ModelLoader.OBJLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DummyTest {
    @Test
    void testBadFilePath(){
        Loader loader = new OBJLoader();
        var result = loader.load("Some none existing path");
        assertNull(result);
    }
}
