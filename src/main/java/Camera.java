import org.joml.*;
import org.joml.Math;

public class Camera {
    private Vector3f Position = new Vector3f(0f);
    private Vector3f Front;
    private Vector3f Up;
    private Vector3f Right;
    private Vector3f WorldUp = new Vector3f(0f,1f,0f);

    private float Yaw = -90f;
    private float Pitch = 0f;

    private float MovementSpeed = 2.5f;
    private float MouseSensitivity = 0.1f;
    private float Zoom = 45f;

    public Camera(){
        updateCameraVectors();
    }
    public Matrix4f getViewMatrix(){
        return new Matrix4f().lookAt(Position,Position.add(Front),Up);
    }

    private void updateCameraVectors(){
        Vector3f front = new Vector3f();
        front.x = (float)(Math.sin(Math.toRadians(Yaw)) * Math.cos(Math.toRadians(Pitch)));
        front.y = (float)(Math.sin(Math.toRadians(Pitch)));
        front.z = (float)(Math.sin(Math.toRadians(Yaw)) * Math.cos(Math.toRadians(Pitch)));
        Front = front.normalize();
        Right = Front.cross(WorldUp).normalize();
        Up = Right.cross(Front).normalize();
    }
}
