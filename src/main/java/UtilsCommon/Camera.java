package UtilsCommon;

import org.joml.*;
import org.joml.Math;

public class Camera {
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;

    private float yaw = -90f;
    private float pitch = 0f;

    private float mouseSensitivity = 0.3f;
    private float scrollSensitivity = 1.04f;
    private float scrollLinear = 0.35f;
    private float distance = 15f;
    private float zoom = 45f;

    public Vector3f getPosition(){
        return new Vector3f(position);
    }
    public float getZoom(){
        return zoom;
    }

    public Camera(){
        position = new Vector3f(0f);
        worldUp = new Vector3f(0f,1f,0f);
        updateCameraVectors();
    }
    public Matrix4f getViewMatrix(){
        Vector3f dir = new Vector3f(position);
        dir.add(front);
        return new Matrix4f().lookAt(position,dir, up);
    }

    public void ProcessMousePosition(float offsetX, float offsetY, boolean constraintPitch){
        offsetX *= mouseSensitivity;
        offsetY *= mouseSensitivity;
        yaw += offsetX;
        pitch -= offsetY;

        if(constraintPitch){
            if(pitch > 89.0f)
                pitch = 89.0f;
            if(pitch < -89.0f)
                pitch = -89.0f;
        }

        updateCameraVectors();
    }

    public void ProcessMousePosition(float offsetX, float offsetY){
        ProcessMousePosition(offsetX,offsetY,true);
    }

    public void ProcessMouseScroll(float offset){
        while(offset > 0.5f || offset < -0.5f) {
            if (offset < 0f) {
                distance *= scrollSensitivity;
                distance += scrollLinear;
                offset += 1f;
            } else {
                distance /= scrollSensitivity;
                distance -= scrollLinear;
                offset -= 1f;
            }
        }
        if(distance < 0.1f)
            distance = 0.1f;
        if(distance > 180.0f)
            distance = 180.0f;
        position.normalize();
        position.mul(distance);
    }

    private void updateCameraVectors(){
        Vector3f front = new Vector3f();
        front.x = -(float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = -(float)(Math.sin(Math.toRadians(pitch)));
        front.z = -(float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        Vector3f position = new Vector3f();
        position.x = -front.x;
        position.y = -front.y;
        position.z = -front.z;
        this.position = position;
        this.position.normalize();
        this.position.mul(distance);
        this.front = front.normalize();
        right = new Vector3f(this.front).cross(worldUp).normalize();
        up = new Vector3f(right).cross(this.front).normalize();
    }
}
