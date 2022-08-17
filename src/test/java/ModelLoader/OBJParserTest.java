package ModelLoader;

import EntityTree.Entity;
import EntityTree.EntityModel;
import UtilsModel.Face;
import UtilsModel.Mesh;
import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class OBJParserTest {

    @Test
    public void testBadFilePath() {
        OBJParser parser = new OBJParser();
        ParsingResult parsingResult =
                parser.load("some-non-existing-path.obj");
        assertThat(parsingResult).isNull();
    }

    @Test
    public void testFileWithErrorsParseToNull() {
        OBJParser parser = new OBJParser();
        ParsingResult parsingResult =
                parser.load("src/main/resources/TestFiles/TestLoader/incorrectFile.obj");
        assertThat(parsingResult).isNull();
    }

    @Test
    public void testCorrectlyCreateEntitiesForOneMesh() {
        OBJParser parser = new OBJParser();
        ParsingResult parsingResult =
                parser.load("src/main/resources/TestFiles/TestLoader/correctlyReadOneMesh.obj");
        Entity root = parsingResult.readObjects.get(0);

        assertThat(parsingResult.readObjects.size()).isEqualTo(2);
        assertThat(root).isNull();
        assertThat(parsingResult.adjacencyList).isNull();
    }

    @Test
    public void testCorrectlyReadVerticesOneMesh() {
        OBJParser parser = new OBJParser();
        ParsingResult parsingResult =
                parser.load("src/main/resources/TestFiles/TestLoader/correctlyReadOneMesh.obj");
        Vector3f v1 = new Vector3f(1f, 1f, -1f);
        Vector3f v2 = new Vector3f(1f, -1f, -1f);
        Vector3f v3 = new Vector3f(1f, -1f, 1f);
        Vector3f v4 = new Vector3f(-1f, -1f, 1f);
        EntityModel entityModel = (EntityModel) parsingResult.readObjects.get(1);

        assertThat(parsingResult.readObjects.size()).isEqualTo(2);
        assertThat(entityModel.getMesh().getVertices()).extracting(VertexPosition::getValue).containsExactly(v1, v2, v3, v4);
    }

    @Test
    public void testCorrectlyReadFaces() {
        OBJParser parser = new OBJParser();
        ParsingResult parsingResult =
                parser.load("src/main/resources/TestFiles/TestLoader/correctlyReadOneMesh.obj");
        Vector3f v1 = new Vector3f(1f, 1f, -1f);
        Vector3f v2 = new Vector3f(1f, -1f, -1f);
        Vector3f v3 = new Vector3f(1f, -1f, 1f);
        Vector3f v4 = new Vector3f(-1f, -1f, 1f);
        EntityModel entityModel = (EntityModel) parsingResult.readObjects.get(1);

        Face faceTriangle = entityModel.getMesh().getFaces().get(0);
        Face faceQuad = entityModel.getMesh().getFaces().get(1);

        assertThat(entityModel.getMesh().getFaces().size()).isEqualTo(2);
        assertThat(faceTriangle.getVertices()).extracting(v -> v.getPosition().getValue()).containsExactly(v1, v2, v3);
        assertThat(faceQuad.getVertices()).extracting(v -> v.getPosition().getValue()).containsExactly(v1, v2, v3, v4);
    }

    @Test
    public void testManyCalls() {
        OBJParser parser = new OBJParser();
        Vector3f v1 = new Vector3f(1f, 1f, 1f);

        ParsingResult parsingResult =
                parser.load("src/main/resources/TestFiles/TestLoader/manyCalls.obj");
        ParsingResult parsingResult2 =
                parser.load("src/main/resources/TestFiles/TestLoader/manyCalls.obj");

        Mesh mesh1 = ((EntityModel)parsingResult.readObjects.get(1)).getMesh();
        Mesh mesh2 = ((EntityModel)parsingResult2.readObjects.get(1)).getMesh();

        assertThat(mesh1.getVertices()).extracting(VertexPosition::getValue).containsExactly(v1);
        assertThat(mesh2.getVertices()).extracting(VertexPosition::getValue).containsExactly(v1);
    }

    @Test
    public void testEmptyLinesAndUnknownSymbolsDoesNotBreakParsing() {
        OBJParser parser = new OBJParser();
        ParsingResult parsingResult =
                parser.load("src/main/resources/TestFiles/TestLoader/correctlyReadWithEmptyLinesAndUnknownSymbols.obj");
        Vector3f v1 = new Vector3f(1f, 1f, -1f);
        Vector3f v2 = new Vector3f(1f, -1f, -1f);
        Vector3f v3 = new Vector3f(1f, -1f, 1f);
        Vector3f v4 = new Vector3f(-1f, -1f, 1f);
        EntityModel entityModel = (EntityModel) parsingResult.readObjects.get(1);

        assertThat(entityModel.getMesh().getVertices()).extracting(VertexPosition::getValue).containsExactly(v1, v2, v3, v4);

        Face faceTriangle = entityModel.getMesh().getFaces().get(0);
        Face faceQuad = entityModel.getMesh().getFaces().get(1);

        assertThat(faceTriangle.getVertices()).extracting(v -> v.getPosition().getValue()).containsExactly(v1, v2, v3);
        assertThat(faceQuad.getVertices()).extracting(v -> v.getPosition().getValue()).containsExactly(v1, v2, v3, v4);
    }

    @Test
    public void testMultipleMeshesInOneFileNoStructure() {
        OBJParser parser = new OBJParser();
        ParsingResult parsingResult =
                parser.load("src/main/resources/TestFiles/TestLoader/multipleMeshesNoStructure.obj");

        ArrayList<Entity> result = parsingResult.readObjects;
        assertThat(result.size()).isEqualTo(5);
        assertThat(parsingResult.adjacencyList).isNull();
    }

    @Test
    public void testMultipleMeshesCorrectContent() {
        OBJParser parser = new OBJParser();
        ParsingResult parsingResult =
                parser.load("src/main/resources/TestFiles/TestLoader/multipleMeshesCorrectContent.obj");

        Vector3f v1 = new Vector3f(1f, 1f, 1f);
        Vector3f v2 = new Vector3f(-1f, 1f, -1f);
        Vector3f v3 = new Vector3f(-1f, -1f, -1f);
        Vector3f v4 = new Vector3f(-1f, -1f, 1f);
        Vector3f v5 = new Vector3f(1f, -1f, -1f);
        Vector3f v6 = new Vector3f(1f, -1f, 1f);

        Mesh mesh1 = ((EntityModel)parsingResult.readObjects.get(1)).getMesh();
        Mesh mesh2 = ((EntityModel)parsingResult.readObjects.get(2)).getMesh();

        Face face1 = mesh1.getFaces().get(0);
        Face face2 = mesh2.getFaces().get(0);

        assertThat(mesh1.getVertices()).extracting(VertexPosition::getValue).containsExactly(v1, v2, v3);
        assertThat(mesh2.getVertices()).extracting(VertexPosition::getValue).containsExactly(v4, v5, v6);

        assertThat(face1.getVertices()).extracting(v -> v.getPosition().getValue()).contains(v1, v2, v3);
        assertThat(face2.getVertices()).extracting(v -> v.getPosition().getValue()).contains(v4, v5, v6);
    }

    @Test
    public void testMultipleMeshesWithStructure() {
        OBJParser parser = new OBJParser();
        ParsingResult parsingResult =
                parser.load("src/main/resources/TestFiles/TestLoader/multipleMeshesWithStructure.obj");

        assertThat(parsingResult.readObjects.size()).isEqualTo(5);
        assertThat(parsingResult.adjacencyList).containsExactly(-1, 0, 1, 1, 2);
    }
}