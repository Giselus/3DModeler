package EntityTree;

import org.joml.*;
import org.joml.Math;

public class Transform {
    private Vector3fc localTranslation = new Vector3f(0f, 0f, 0f);
    private Vector3fc localRotation = new Vector3f(0f, 0f, 0f);
    private Vector3fc localScale = new Vector3f(1f, 1f, 1f);

    private Matrix4f globalModelMatrix = new Matrix4f().identity();

    public Vector3fc getLocalTranslation() {
        return localTranslation;
    }

    public void setLocalTranslation(Vector3fc localTranslation) {
        this.localTranslation = localTranslation;
    }

    public Vector3fc getLocalRotation() {
        return localRotation;
    }

    public void setLocalRotation(Vector3fc localRotation) {
        this.localRotation = localRotation;
    }

    public Vector3fc getLocalScale() {
        return localScale;
    }

    public void setLocalScale(Vector3fc localScale) {
        this.localScale = localScale;
    }

    public void updateGlobalModelMatrix() {
        globalModelMatrix = getLocalModelMatrix();
    }

    public void updateGlobalModelMatrix(Matrix4fc parentGlobalModelMatrix) {
        parentGlobalModelMatrix.mul(getLocalModelMatrix(), globalModelMatrix);
    }

    public Matrix4fc getGlobalModelMatrix() {
        return globalModelMatrix;
    }

    private Matrix4f getLocalModelMatrix() {

        return new Matrix4f().identity()
                .translate(localTranslation)
                .rotateY((float)Math.toRadians(localRotation.y()))
                .rotateZ((float)Math.toRadians(localRotation.z()))
                .rotateX((float)Math.toRadians(localRotation.x()))
                .scale(localScale);
    }
}
