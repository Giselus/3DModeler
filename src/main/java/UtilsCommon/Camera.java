package UtilsCommon;

import org.joml.*;
import org.joml.Math;

public class Camera {
    private Vector3f Position;
    private Vector3f Front;
    private Vector3f Up;
    private Vector3f Right;
    private Vector3f WorldUp;

    private float Yaw = -90f;
    private float Pitch = 0f;

    private float MouseSensitivity = 0.3f;
    private float ScrollSensitivity = 1.04f;
    private float ScrollLinear = 0.35f;
    private float Distance = 15f;
    private float Zoom = 45f;

    public Vector3f getPosition(){
        return new Vector3f(Position);
    }
    public float getZoom(){
        return Zoom;
    }

    public Camera(){
        Position = new Vector3f(0f);
        WorldUp = new Vector3f(0f,1f,0f);
        updateCameraVectors();
    }
    public Matrix4f getViewMatrix(){
        Vector3f dir = new Vector3f(Position);
        dir.add(Front);
        return new Matrix4f().lookAt(Position,dir,Up);
    }

    public void ProcessMousePosition(float offsetX, float offsetY, boolean constraintPitch){
        offsetX *= MouseSensitivity;
        offsetY *= MouseSensitivity;
        Yaw += offsetX;
        Pitch -= offsetY;

        if(constraintPitch){
            if(Pitch > 89.0f)
                Pitch = 89.0f;
            if(Pitch < -89.0f)
                Pitch = -89.0f;
        }

        updateCameraVectors();
    }

    public void ProcessMousePosition(float offsetX, float offsetY){
        ProcessMousePosition(offsetX,offsetY,true);
    }

    public void ProcessMouseScroll(float offset){
        while(offset > 0.5f || offset < -0.5f) {
            if (offset < 0f) {
                Distance *= ScrollSensitivity;
                Distance += ScrollLinear;
                offset += 1f;
            } else {
                Distance /= ScrollSensitivity;
                Distance -= ScrollLinear;
                offset -= 1f;
            }
        }
        if(Distance < 0.1f)
            Distance = 0.1f;
        if(Distance > 180.0f)
            Distance = 180.0f;
        Position.normalize();
        Position.mul(Distance);
    }

    private void updateCameraVectors(){
        Vector3f front = new Vector3f();
        front.x = -(float)(Math.cos(Math.toRadians(Yaw)) * Math.cos(Math.toRadians(Pitch)));
        front.y = -(float)(Math.sin(Math.toRadians(Pitch)));
        front.z = -(float)(Math.sin(Math.toRadians(Yaw)) * Math.cos(Math.toRadians(Pitch)));
        Vector3f position = new Vector3f();
        position.x = -front.x;
        position.y = -front.y;
        position.z = -front.z;
        Position = position;
        Position.normalize();
        Position.mul(Distance);
        Front = front.normalize();
        Right = new Vector3f(Front).cross(WorldUp).normalize();
        Up = new Vector3f(Right).cross(Front).normalize();
    }
}
