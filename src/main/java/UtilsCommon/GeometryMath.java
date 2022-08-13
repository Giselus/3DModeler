package UtilsCommon;

import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class GeometryMath {
    public static Vector3f meanPoint(ArrayList<VertexPosition> points) {
        Vector3f meanPoint = new Vector3f();
        for(VertexPosition v: points){
            meanPoint.add(v.getValue());
        }
        meanPoint.div((float)points.size());
        return meanPoint;
    }

    public static Vector4f normalPlane(Vector3f vector, Vector3f point) {
        Vector4f normalPlane = new Vector4f(0f);
        //(x + y + z = w)
        normalPlane.x = vector.x;
        normalPlane.y = vector.y;
        normalPlane.z = vector.z;
        normalPlane.w = vector.x *point.x + vector.y * point.y + vector.z * point.z;

        return normalPlane;
    }

    public static Vector3f intersectionVectorPlane(Vector3f startingPoint, Vector3f direction, Vector4f plane) {
        float parameter = (plane.w - plane.x*startingPoint.x
                - plane.y*startingPoint.y - plane.z*startingPoint.z)
                /(plane.x * direction.x + plane.y * direction.y + plane.z * direction.z);
        Vector3f intersectionPoint = new Vector3f(0f);
        direction.mul(parameter, intersectionPoint);

        intersectionPoint.add(startingPoint);
        return intersectionPoint;
    }

    public static Vector3f clickDirection(float x, float y, Camera camera) {
        float clipX = (2f * x) - 1f;
        float clipY = (2f * y) - 1f;

        return camera.getRay(clipX,clipY).direction;
    }

    public static void translatePoints(ArrayList<VertexPosition> points, Vector3f offset) {
        for(VertexPosition v: points){
            v.setValue(v.getValue().add(offset));
        }
    }
}
