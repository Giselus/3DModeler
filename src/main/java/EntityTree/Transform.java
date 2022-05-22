package EntityTree;

import org.joml.*;

public class Transform {
    private Vector3fc localTranslation = new Vector3f(0, 0, 0);
    private Vector3fc localRotation = new Vector3f(0, 0, 0);
    private Vector3fc localScale = new Vector3f(1, 1, 1);

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
        return new Matrix4f().identity().translate(localTranslation).scale(localScale)
                .rotateY(localRotation.y()).rotateZ(localRotation.z()).rotateY(localRotation.x());
    }
}
