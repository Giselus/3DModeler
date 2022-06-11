package UtilsModel;

import UtilsCommon.NormalVector;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VertexInstanceTest {

    VertexPosition v1;
    VertexPosition v2;
    VertexPosition v3;
    NormalVector normal;
    Face face;
    void setUp(){
        v1 = new VertexPosition(new Vector3f(0f, 1f, 0f));
        v2 = new VertexPosition(new Vector3f(0f, 3f, 1f));
        v3 = new VertexPosition(new Vector3f(0f, 2f, 8f));

        normal = new NormalVector(new Vector3f(1f, 0f, 0f));
        face = new Face(new ArrayList<>(List.of(new VertexPosition[]{v1, v2, v3})));
    }
    @Test
    public void testConstructor(){
        setUp();
        VertexPosition result = new VertexInstance(v1, face, normal).getPosition();
        assertEquals(v1, result);
    }

    @Test
    public void testGetBufferedData(){
        setUp();
        float[] expected = {0f, 1f, 0f, 1f, 0f, 0f, -1f};
        var result = new VertexInstance(v1, face, normal).getBufferedData();
        for(int i=0; i< expected.length; i++){
            assertEquals(expected[i], result[i], 0.001f);
        }
    }

    @Test
    public void testGetBufferedDataAfterPick(){
        setUp();
        v1.pick();
        float[] expected = {0f, 1f, 0f, 1f, 0f, 0f, 1f};
        var result = new VertexInstance(v1, face, normal).getBufferedData();
        for(int i=0; i< expected.length; i++){
            assertEquals(expected[i], result[i], 0.001f);
        }
    }

    @Test
    public void testGetBufferedDataAndChangeNormals(){
        setUp();
        var temp = new VertexInstance(v1, face, normal);
        temp.setNormal(new NormalVector(new Vector3f(0.6f, 0.8f, 0f)));
        float[] expected = {0f, 1f, 0f, 0.6f, 0.8f, 0f, -1f};
        var result = temp.getBufferedData();
        for(int i=0; i< expected.length; i++){
            assertEquals(expected[i], result[i], 0.001f);
        }
    }
}