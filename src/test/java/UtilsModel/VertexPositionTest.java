package UtilsModel;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void testPickVertexDefaultValue(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        VertexPosition vertexPosition = new VertexPosition(position);
        assertFalse(vertexPosition.isPicked());
    }

    @Test
    public void testPickVertex(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        VertexPosition vertexPosition = new VertexPosition(position);
        vertexPosition.pick();
        assertTrue(vertexPosition.isPicked());
    }

    @Test
    public void testPickUnpick(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        VertexPosition vertexPosition = new VertexPosition(position);
        vertexPosition.pick();
        vertexPosition.unpick();
        assertFalse(vertexPosition.isPicked());
    }

    @Test
    public void testGetInstances(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        VertexPosition vertexPosition = new VertexPosition(position);

        ArrayList<VertexInstance> result = vertexPosition.getInstances();

        assertThat(result).isEmpty();
    }

    @Test
    public void testAddInstances(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        VertexPosition vertexPosition = new VertexPosition(position);
        VertexPosition v1 = new VertexPosition(new Vector3f(0f, 1f, 0f));
        VertexPosition v2 = new VertexPosition(new Vector3f(0f, 3f, 1f));
        VertexPosition v3 = new VertexPosition(new Vector3f(0f, 2f, 8f));
        Face face = new Face(new ArrayList<>(List.of(new VertexPosition[]{v1, v2, v3})));
        VertexInstance vertexInstance1 = new VertexInstance(v1, face);
        VertexInstance vertexInstance2 = new VertexInstance(v2, face);

        vertexPosition.addInstance(vertexInstance1);
        vertexPosition.addInstance(vertexInstance2);
        ArrayList<VertexInstance> result = vertexPosition.getInstances();

        assertThat(result).containsExactlyInAnyOrder(vertexInstance1, vertexInstance2);
    }

    @Test
    public void testRemoveInstances(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        VertexPosition vertexPosition = new VertexPosition(position);
        VertexPosition v1 = new VertexPosition(new Vector3f(0f, 1f, 0f));
        VertexPosition v2 = new VertexPosition(new Vector3f(0f, 3f, 1f));
        VertexPosition v3 = new VertexPosition(new Vector3f(0f, 2f, 8f));
        Face face = new Face(new ArrayList<>(List.of(new VertexPosition[]{v1, v2, v3})));
        VertexInstance vertexInstance1 = new VertexInstance(v1, face);
        VertexInstance vertexInstance2 = new VertexInstance(v2, face);

        vertexPosition.addInstance(vertexInstance1);
        vertexPosition.addInstance(vertexInstance2);
        vertexPosition.removeInstance(vertexInstance1);
        ArrayList<VertexInstance> result = vertexPosition.getInstances();

        assertThat(result).containsExactlyInAnyOrder(vertexInstance2);
    }

    @Test
    public void testSetValueRecalculateNormals(){
        Vector3f position = new Vector3f(1f, 1f, 1f);
        VertexPosition vertexPosition = new VertexPosition(position);
        VertexPosition v1 = mock(VertexPosition.class);
        VertexPosition v2 = mock(VertexPosition.class);
        Face face = mock(Face.class);
        VertexInstance vertexInstance1 = new VertexInstance(v1, face);
        VertexInstance vertexInstance2 = new VertexInstance(v2, face);
        Vector3f newVal = new Vector3f(0f, 0f, 0f);
        vertexPosition.addInstance(vertexInstance1);
        vertexPosition.addInstance(vertexInstance2);

        vertexPosition.setValue(newVal);
        Vector3f result = vertexPosition.getValue();

        assertThat(result).isEqualTo(newVal);
        verify(face, times(2)).recalculateNormal();
    }
}