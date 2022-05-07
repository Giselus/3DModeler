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

    private float MovementSpeed = 2.5f;
    private float MouseSensitivity = 0.1f;
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

    enum Camera_Movement{
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }
    public void ProcessMovement(Camera_Movement direction, float deltaTime){
        float velocity = MovementSpeed * deltaTime;
        if(direction == Camera_Movement.FORWARD)
            Position.add(new Vector3f(Front).mul(velocity));
        else if(direction == Camera_Movement.BACKWARD)
            Position.sub(new Vector3f(Front).mul(velocity));
        else if(direction == Camera_Movement.LEFT)
            Position.sub(new Vector3f(Right).mul(velocity));
        else
            Position.add(new Vector3f(Right).mul(velocity));
    }

    public void ProcessMousePosition(float offsetX, float offsetY, boolean constraintPitch){
        offsetX *= MouseSensitivity;
        offsetY *= MouseSensitivity;
        Yaw += offsetX;
        Pitch += offsetY;

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
        Zoom -= offset;
        if(Zoom < 1.0f)
            Zoom = 1.0f;
        if(Zoom > 45.0f)
            Zoom = 45.0f;
    }

    private void updateCameraVectors(){
        Vector3f front = new Vector3f();
        front.x = (float)(Math.cos(Math.toRadians(Yaw)) * Math.cos(Math.toRadians(Pitch)));
        front.y = (float)(Math.sin(Math.toRadians(Pitch)));
        front.z = (float)(Math.sin(Math.toRadians(Yaw)) * Math.cos(Math.toRadians(Pitch)));
        Front = front.normalize();
        Right = new Vector3f(Front).cross(WorldUp).normalize();
        Up = new Vector3f(Right).cross(Front).normalize();
    }
}
