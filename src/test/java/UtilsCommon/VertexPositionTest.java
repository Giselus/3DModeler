package UtilsCommon;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VertexPositionTest {
    @Test
    public void testConstructorAndGet(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        Vector3f result = new VertexPosition(position).getValue();
        assertEquals(position, result);
    }

    @Test
    public void testGetDoesNotReturnReference(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        Vector3f result = new VertexPosition(position).getValue();
        result.x += 1f;
        assertNotEquals(position, result);
    }

    @Test
    public void testSetValue(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        VertexPosition vertexPosition = new VertexPosition(position);
        Vector3f newVal = new Vector3f(0f, 0f, 0f);
        vertexPosition.setValue(newVal);
        Vector3f result = vertexPosition.getValue();
        assertEquals(newVal, result);
    }

    @Test
    public void testSetDoesNotUseReference(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        VertexPosition vertexPosition = new VertexPosition(position);
        Vector3f newVal = new Vector3f(0f, 0f, 0f);
        vertexPosition.setValue(newVal);
        newVal.x = 42f;
        Vector3f result = vertexPosition.getValue();
        assertNotEquals(newVal, result);
    }
}