package UtilsModel;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class FaceTest {
    @Test
    public void testConstructor(){
        VertexPosition v1 = new VertexPosition(new Vector3f(0f, 1f, 0f));
        VertexPosition v2 = new VertexPosition(new Vector3f(1f, 0f, 0f));
        VertexPosition v3 = new VertexPosition(new Vector3f(0f, 0f, 1f));

        ArrayList<VertexPosition> vertices = new ArrayList<>();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        Face face = new Face(vertices);

        Vector3f expectedNormal = new Vector3f(-.577f, -.577f, -.577f);

        ArrayList<VertexInstance> result = face.getVertices();
        List<VertexPosition> resultPositions = result.stream().map(VertexInstance::getPosition).toList();
        Vector3f resultNormal = face.getNormal();

        assertThat(resultPositions).containsExactly(v1, v2, v3);
        assertThat(resultNormal.x).isEqualTo(expectedNormal.x, withPrecision(.001f));
        assertThat(resultNormal.y).isEqualTo(expectedNormal.y, withPrecision(.001f));
        assertThat(resultNormal.z).isEqualTo(expectedNormal.z, withPrecision(.001f));
    }

    @Test
    public void testIsPicked() {
        VertexPosition v1 = new VertexPosition(new Vector3f(0f, 1f, 0f));
        VertexPosition v2 = new VertexPosition(new Vector3f(1f, 0f, 0f));
        VertexPosition v3 = new VertexPosition(new Vector3f(0f, 0f, 1f));

        ArrayList<VertexPosition> vertices = new ArrayList<>();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        Face face = new Face(vertices);

        boolean expected = false;

        boolean result = face.isPicked();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testPick() {
        VertexPosition v1 = new VertexPosition(new Vector3f(0f, 1f, 0f));
        VertexPosition v2 = new VertexPosition(new Vector3f(1f, 0f, 0f));
        VertexPosition v3 = new VertexPosition(new Vector3f(0f, 0f, 1f));

        ArrayList<VertexPosition> vertices = new ArrayList<>();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        Face face = new Face(vertices);

        face.pick();

        boolean expected = true;

        boolean result = face.isPicked();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testUnPick() {
        VertexPosition v1 = new VertexPosition(new Vector3f(0f, 1f, 0f));
        VertexPosition v2 = new VertexPosition(new Vector3f(1f, 0f, 0f));
        VertexPosition v3 = new VertexPosition(new Vector3f(0f, 0f, 1f));

        ArrayList<VertexPosition> vertices = new ArrayList<>();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        Face face = new Face(vertices);

        face.pick();

        face.unpick();

        boolean expected = false;

        boolean result = face.isPicked();

        assertThat(result).isEqualTo(expected);
    }
}