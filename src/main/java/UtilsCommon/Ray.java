package UtilsCommon;

import org.joml.Vector3f;

import java.util.List;

public class Ray {
    static float epsilon = 0.01f;
    //TODO: change to get/set
    private Vector3f origin;
    private Vector3f direction;
    public Ray(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
        this.direction.normalize();
    }

    //returns negative value when plane doesn't intersect with ray, distance otherwise
    public float distanceFromPlane(List<Vector3f> vertices){
        Vector3f v0v1 = new Vector3f();
        Vector3f v0v2 = new Vector3f();
        vertices.get(1).sub(vertices.get(0),v0v1);
        vertices.get(2).sub(vertices.get(0),v0v2);
        Vector3f normal = new Vector3f();
        v0v1.cross(v0v2,normal);

        float normalDotRayDirection = normal.dot(direction);
        if(Math.abs(normalDotRayDirection) < epsilon)
            return -1f;

        float d = -(normal.dot(vertices.get(0)));
        float distance = -(normal.dot(origin) + d) / normalDotRayDirection;

        if(distance < 0)
            return -1f;

        Vector3f intersection = new Vector3f();
        direction.mul(distance,intersection);
        intersection.add(origin);

        for(int i = 0; i < vertices.size(); i++){
            Vector3f edge = new Vector3f();
            vertices.get((i+1)%(vertices.size())).sub(vertices.get(i),edge);
            Vector3f vp = new Vector3f();
            intersection.sub(vertices.get(i),vp);
            Vector3f C = new Vector3f();
            edge.cross(vp,C);
            if(normal.dot(C) < 0)
                return -1;
        }
        return distance;
    }

    public float distanceFromSphere(Vector3f center, float radius){
        Vector3f L = new Vector3f();
        center.sub(origin,L);
        float tca = L.dot(direction);
        float d2 = L.dot(L) - tca * tca;
        if(d2 > radius) return -1;
        float thc = (float)Math.sqrt(radius - d2);
        float t0 = tca - thc;
        float t1 = tca + thc;

        t0 = t0 < 0 ? t1 : t0;
        return t0 > 0 ? t0 : -1;
    }
    
    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }
}
