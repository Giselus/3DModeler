package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityEmpty;
import EntityTree.EntityModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OBJLoaderTest {

    @Test
    void testBadFilePath(){
        OBJLoader objLoader = new OBJLoader();
        Entity result = objLoader.load("some-non-existing-path");

        assertThat(result).isInstanceOf(EntityEmpty.class);
        assertThat(result.getChildren().size()).isEqualTo(0);
    }

    @Test
    void testOpenFileWithoutErrors() {
        OBJLoader objLoader = new OBJLoader();
        Entity result = objLoader.load("src/main/resources/TestFiles/incorrectFile.obj");

        assertThat(result).isInstanceOf(EntityEmpty.class);
        assertThat(result.getChildren().size()).isEqualTo(0);
    }

    @Test
    void testCorrectlyReturnEntitiesWithoutStructure() {
        OBJLoader objLoader = new OBJLoader();
        Entity result = objLoader.load("src/main/resources/TestFiles/multipleMeshesNoStructure.obj");

        assertThat(result.getChildren().size()).isEqualTo(4);

        var children = result.getChildren().toArray();
        for(int i=0; i<4; i++){
            assertThat(children[i]).isInstanceOf(EntityModel.class);
            assertThat(((EntityModel)children[i]).getChildren().size()).isEqualTo(0);
        }
    }

    @Test
    void testCorrectlyReturnEntitiesWithStructure() {
        OBJLoader objLoader = new OBJLoader();
        Entity result = objLoader.load("src/main/resources/TestFiles/multipleMeshesWithStructure.obj");

        Entity node1 = (Entity) result.getChildren().toArray()[0];
        Entity node2 = (Entity) node1.getChildren().toArray()[0];
        Entity node3 = (Entity) node1.getChildren().toArray()[1];
        Entity node4 = (Entity) node2.getChildren().toArray()[0];

        assertThat(result.getChildren()).containsExactly(node1);
        assertThat(node1.getChildren()).containsExactlyInAnyOrder(node2, node3);
        assertThat(node2.getChildren()).containsExactly(node4);
        assertThat(node3.getChildren()).isEmpty();
        assertThat(node4.getChildren()).isEmpty();
    }
}