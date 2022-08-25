package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityEmpty;
import EntityTree.EntityModel;
import UtilsModel.Face;
import UtilsModel.Mesh;
import UtilsModel.VertexPosition;
import org.apache.commons.io.FileUtils;
import org.joml.Vector3f;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

class OBJSaverTest {
    private final String DIR = "src/main/resources/TestFiles/TestSaver";
    private final String TEMP_DIR = "src/main/resources/TestFiles/TestSaverTemp";

    @Test
    public void testSaveOneEntityWithoutStructure() throws IOException {
        EntityEmpty root = new EntityEmpty();
        ArrayList<VertexPosition> arr = new ArrayList<>();
        arr.add(new VertexPosition(new Vector3f(1f, 1f, 1f)));
        arr.add(new VertexPosition(new Vector3f(-1f, -1f, -2f)));
        arr.add(new VertexPosition(new Vector3f(-1f, 1f, 1f)));

        ArrayList<Face> faces = new ArrayList<>();
        faces.add(new Face(arr));

        EntityModel child = new EntityModel(new Mesh(arr, faces), root);
        child.setName("OneEntityWithoutStructure");

        Saver saver = new OBJSaver();
        saver.save(root, TEMP_DIR + "/result.obj", SaveMode.OBJ);

        File expectedFile = new File(DIR + "/OneEntityWithoutStructure.obj");
        File resultFile = new File(TEMP_DIR + "/result.obj");

        List<String> expectedLines = FileUtils.readLines(expectedFile, StandardCharsets.UTF_8);
        List<String> resultLines = FileUtils.readLines(resultFile, StandardCharsets.UTF_8);

        assertThat(resultLines.size()).isEqualTo(expectedLines.size());
        for(int i=0; i<expectedLines.size(); i++){
            assertThat(resultLines.get(i).trim()).isEqualTo(expectedLines.get(i).trim());
        }
    }

    @Test
    public void testSaveManyEntityWithoutStructure() throws IOException {
        EntityEmpty root = new EntityEmpty();
        ArrayList<VertexPosition> arr = new ArrayList<>();
        arr.add(new VertexPosition(new Vector3f(1f, 1f, 1f)));
        arr.add(new VertexPosition(new Vector3f(-1f, -1f, -2f)));
        arr.add(new VertexPosition(new Vector3f(-1f, 1f, 1f)));

        ArrayList<Face> faces = new ArrayList<>();
        faces.add(new Face(arr));
        EntityModel child1 = new EntityModel(new Mesh(arr, faces), root);
        child1.setName("a");

        arr = new ArrayList<>();
        arr.add(new VertexPosition(new Vector3f(2f, 2f, 2f)));
        arr.add(new VertexPosition(new Vector3f(-2f, -2f, -2f)));
        arr.add(new VertexPosition(new Vector3f(-2f, 2f, 2f)));

        faces = new ArrayList<>();
        faces.add(new Face(arr));
        EntityModel child2 = new EntityModel(new Mesh(arr, faces), root);
        child2.setName("b");

        Saver saver = new OBJSaver();
        saver.save(root, TEMP_DIR + "/result.obj", SaveMode.OBJ);

        File expectedFile = new File(DIR + "/manyEntitiesWithoutStructure.obj");
        File resultFile = new File(TEMP_DIR + "/result.obj");

        List<String> expectedLines = FileUtils.readLines(expectedFile, StandardCharsets.UTF_8);
        List<String> resultLines = FileUtils.readLines(resultFile, StandardCharsets.UTF_8);

        assertThat(resultLines.size()).isEqualTo(expectedLines.size());
        for(int i=0; i<expectedLines.size(); i++){
            assertThat(resultLines.get(i).trim()).isEqualTo(expectedLines.get(i).trim());
        }
    }

    @Test
    public void testSaveWithStructure() throws IOException {
        Entity root = new EntityEmpty();
        Entity child1 = new EntityModel(new Mesh(new ArrayList<>(), new ArrayList<>()), root);
        child1.setName("a");

        Entity child2 = new EntityModel(new Mesh(new ArrayList<>(), new ArrayList<>()), child1);
        child2.setName("b");

        Entity child3 = new EntityEmpty();
        child3.setName("c");
        child3.setParent(root);

        Saver saver = new OBJSaver();
        saver.save(root, TEMP_DIR + "/result.obj", SaveMode.EXTENDED_OBJ);

        File expectedFile = new File(DIR + "/saveEntityStructure.obj");
        File resultFile = new File(TEMP_DIR + "/result.obj");

        List<String> expectedLines = FileUtils.readLines(expectedFile, StandardCharsets.UTF_8);
        List<String> resultLines = FileUtils.readLines(resultFile, StandardCharsets.UTF_8);

        assertThat(resultLines.size()).isEqualTo(expectedLines.size());
        assertThat(expectedLines).containsAll(resultLines);
    }

    @Test
    public void testWritingToIllegalFile(){
        Entity root = new EntityEmpty();
        Entity child1 = new EntityModel(new Mesh(new ArrayList<>(), new ArrayList<>()), root);
        child1.setName("a");

        Saver saver = new OBJSaver();
        boolean result = saver.save(root, "/sad  adasd  asdasdj", SaveMode.EXTENDED_OBJ);
        assertThat(result).isFalse();
    }

    @BeforeEach
    public void createDir() {
        File dir = new File(TEMP_DIR);
        dir.mkdir();
    }

    @AfterEach
    public void clean(){
        File dir = new File(TEMP_DIR);
        for(File file: Objects.requireNonNull(dir.listFiles())){
            file.delete();
        }
        dir.delete();
    }
}
