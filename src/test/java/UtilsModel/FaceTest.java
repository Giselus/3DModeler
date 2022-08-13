package UtilsModel;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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

        ArrayList<VertexInstance> result = face.getVertices();
        List<VertexPosition> resultPositions = result.stream().map(VertexInstance::getPosition).toList();

        assertThat(resultPositions).containsExactly(v1, v2, v3);
    }
}