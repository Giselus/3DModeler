package UtilsCommon;

import Scene.IInput;
import Scene.SceneState;
import org.joml.*;
import org.joml.Math;

public class Camera {
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;

    private float yaw = 0;
    private float pitch = 0f;

    private float mouseSensitivity = 150f;
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

    private IInput input;
    private SceneState sceneState;

    public Camera(IInput input, SceneState sceneState){
        this.input = input;
        this.sceneState = sceneState;
        position = new Vector3f(0f);
        worldUp = new Vector3f(0f,1f,0f);
        updateCameraVectors();
    }

    public Matrix4f getViewMatrix(){
        Vector3f dir = new Vector3f(position);
        dir.add(front);
        return new Matrix4f().lookAt(position,dir, up);
    }

    public Matrix4f getProjectionMatrix(){
        Matrix4f projection = new Matrix4f().setPerspective((float) java.lang.Math.toRadians(zoom),
                (float)sceneState.getSceneWindowWidth()/sceneState.getSceneWindowHeight(), 0.1f, 200.0f);
        return projection;
    }

    public Vector3f getDirection(){
        return front;
    }

    public void ProcessMousePosition(float offsetX, float offsetY){
        if(!input.isMouseKeyPressed(IInput.MouseKeyCode.MOUSE_SCROLL))
            return;
        offsetX *= mouseSensitivity;
        offsetY *= mouseSensitivity;
        yaw += offsetX;
        pitch += offsetY;


        if(pitch > 89.0f)
            pitch = 89.0f;
        if(pitch < -89.0f)
            pitch = -89.0f;


        updateCameraVectors();
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
        front.y = (float)(Math.sin(Math.toRadians(pitch)));
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

    public Ray getRay(float xPos, float yPos){
        Vector4f ray_clip = new Vector4f(xPos,yPos,-1f,1f);

        Vector4f ray_eye = new Vector4f();
        Matrix4f projection = getProjectionMatrix();
        projection.invert();
        projection.mul(new Matrix4f(ray_clip,new Vector4f(0),new Vector4f(0),new Vector4f(0))).getColumn(0,ray_eye);
        ray_eye.z = -1f;
        ray_eye.w = 0f;

       Vector3f ray_wor = new Vector3f();
       Matrix4f view = getViewMatrix();
       view.invert();
       view.mul(new Matrix4f(ray_eye,new Vector4f(0f),new Vector4f(0),new Vector4f(0))).getColumn(0,ray_wor);
       ray_wor.normalize();
       return new Ray(position,ray_wor);
    }
}
