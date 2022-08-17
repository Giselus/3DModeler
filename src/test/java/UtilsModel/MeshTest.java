package UtilsModel;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MeshTest {

    ArrayList<Face> faces;
    ArrayList<VertexPosition> vertices;

    void setUp(){
        vertices = new ArrayList<>();
        vertices.add(new VertexPosition(new Vector3f(1f, 2f, 3f)));
        vertices.add(new VertexPosition(new Vector3f(3f, 1f, 2f)));
        vertices.add(new VertexPosition(new Vector3f(2f, 3f, 1f)));

        faces = new ArrayList<>();
        faces.add(new Face(vertices));
    }

    @Test
    public void testGetVertices(){
        setUp();
        Mesh mesh = new Mesh(vertices, faces);
        ArrayList<VertexPosition> result = mesh.getVertices();
        ArrayList<VertexPosition> expected = vertices;
        for(int i=0; i<expected.size(); i++){
            assertEquals(expected.get(i), result.get(i));
        }
    }

    @Test
    public void testGetFaces(){
        setUp();
        Mesh mesh = new Mesh(vertices, faces);
        ArrayList<Face> result = mesh.getFaces();
        ArrayList<Face> expected = faces;
        for(int i=0; i<expected.size(); i++){
            assertEquals(expected.get(i), result.get(i));
        }
    }

//    @Test
//    public void testUninitializedMeshDrawer(){
//        Mesh mesh = new Mesh(null, null);
//        IMeshDrawer result = mesh.getMeshDrawer();
//        assertNull(result);
//    }
//
//    @Test
//    public void testSetMeshDrawer(){
//        Mesh mesh = new Mesh(null, null);
//        IMeshDrawer expected = mock(IMeshDrawer.class);
//        mesh.setMeshDrawer(expected);
//        IMeshDrawer result = mesh.getMeshDrawer();
//        assertEquals(expected, result);
//    }

    @Test
    public void testAddVertex(){
        Mesh mesh = new Mesh(new ArrayList<>(), null);
        VertexPosition addedVertex = new VertexPosition(new Vector3f(7f, 8f, 2f));
        mesh.addVertex(addedVertex);
        ArrayList<VertexPosition> verticesList = mesh.getVertices();
        assertEquals(1, verticesList.size());
        assertTrue(verticesList.contains(addedVertex));
    }

    @Test
    public void testAddFace(){
        Mesh mesh = new Mesh(null, new ArrayList<>());

        var tempVertices = new ArrayList<VertexPosition>();
        tempVertices.add(new VertexPosition(new Vector3f(1f, 2f, 3f)));
        tempVertices.add(new VertexPosition(new Vector3f(3f, 1f, 2f)));
        tempVertices.add(new VertexPosition(new Vector3f(2f, 3f, 1f)));

        mesh.addFace(tempVertices);

        ArrayList<Face> facesList = mesh.getFaces();

        var resultFace = facesList.get(0);
        assertThat(facesList).containsExactly(resultFace);

        for(var vertex: resultFace.getVertices()){
            var value = vertex.getPosition();
            assertTrue(tempVertices.contains(value));
        }
    }

    @Test
    public void testIncorrectNumberOfVertices(){
        Mesh mesh = new Mesh(null, new ArrayList<>());

        var tempVertices = new ArrayList<VertexPosition>();
        tempVertices.add(new VertexPosition(new Vector3f(1f, 2f, 3f)));
        tempVertices.add(new VertexPosition(new Vector3f(3f, 1f, 2f)));
        tempVertices.add(new VertexPosition(new Vector3f(2f, 3f, 1f)));
        tempVertices.add(new VertexPosition(new Vector3f(3f, 5f, 2f)));

        mesh.addFace(tempVertices);
        var result = mesh.getFaces();
        assertEquals(0, result.size());
    }

    @Test
    public void testSimpleDelete(){
        var tempVertices = new ArrayList<VertexPosition>();
        tempVertices.add(new VertexPosition(new Vector3f(1f, 2f, 3f)));
        tempVertices.add(new VertexPosition(new Vector3f(3f, 1f, 2f)));
        tempVertices.add(new VertexPosition(new Vector3f(2f, 3f, 1f)));

        VertexPosition toRemove = new VertexPosition(new Vector3f(3f, 5f, 2f));
        tempVertices.add(toRemove);

        Mesh mesh = new Mesh(tempVertices, new ArrayList<>());
        mesh.deleteVertex(toRemove);

        var resultArray = mesh.getVertices();
        assertEquals(3, resultArray.size());
        assertFalse(resultArray.contains(toRemove));
    }

    @Test
    public void testRemoveVertexThatIsInFace(){
        var tempVertices = new ArrayList<VertexPosition>();
        tempVertices.add(new VertexPosition(new Vector3f(1f, 2f, 3f)));
        tempVertices.add(new VertexPosition(new Vector3f(2f, 3f, 1f)));
        VertexPosition toRemove = new VertexPosition(new Vector3f(3f, 5f, 2f));
        tempVertices.add(toRemove);

        Mesh mesh = new Mesh(tempVertices, new ArrayList<>());
        mesh.addFace(tempVertices);

        mesh.deleteVertex(toRemove);
        var resultVertices = mesh.getVertices();
        var resultFaces = mesh.getFaces();

        assertFalse(resultVertices.contains(toRemove));
        assertEquals(0, resultFaces.size());
    }
}