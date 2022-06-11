package UtilsCommon;

import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NormalVectorTest {
    @Test
    public void testConstructorWithCalculatedValue(){
        Vector3f normals = new Vector3f(1f, 1f, 1f);
        Vector3f result = new VertexPosition(normals).getValue();
        assertEquals(normals, result);
    }

    @Test
    public void testConstructorWithCalculations(){
        VertexPosition pos1 = new VertexPosition(new Vector3f(1f, 0f, 0f));
        VertexPosition pos2 = new VertexPosition(new Vector3f(0f, 4f, 0f));
        VertexPosition pos3 = new VertexPosition(new Vector3f(2f, 0f, 1f));
        Vector3f result = new NormalVector(pos1, pos2, pos3).getValue();
        Vector3f expected = new Vector3f(0.696f, 0.174f, -0.696f);
        assertEquals(expected.x, result.x, 0.001f);
        assertEquals(expected.y, result.y, 0.001f);
        assertEquals(expected.z, result.z, 0.001f);
    }
}