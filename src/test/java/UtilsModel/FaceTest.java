package UtilsModel;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FaceTest {
    @Test
    public void testConstructor(){
        VertexPosition v1 = new VertexPosition(new Vector3f(0f, 1f, 0f));
        VertexPosition v2 = new VertexPosition(new Vector3f(1f, 0f, 0f));
        VertexPosition v3 = new VertexPosition(new Vector3f(0f, 0f, 1f));

        //TODO weird dependencies
    }
}