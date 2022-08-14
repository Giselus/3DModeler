package Scene;

import EntityTree.EntityModel;
import UtilsCommon.Camera;
import UtilsCommon.Picker;
import UtilsCommon.Ray;
import UtilsModel.EditOperations;
import UtilsModel.Face;
import UtilsModel.VertexInstance;
import UtilsModel.VertexPosition;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Editor {
    private final IInput input;
    private final EditOperations editOperations;
    private final Picker picker;
    private final SceneState sceneState;


    public Editor(IInput input, SceneState sceneState) {
        this.input = input;
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        this.sceneState = sceneState;
        editOperations = new EditOperations(input, pickedVertices, sceneState);
        picker = new Picker(input, pickedVertices, sceneState);
        registerInput();
    }

    private void registerInput() {
        input.addMouseMoveCallback(sceneState.getCamera()::ProcessMousePosition);
        input.addMouseScrollCallback(sceneState.getCamera()::ProcessMouseScroll);
        input.addMouseKeyCallback(IInput.MouseKeyCode.MOUSE_LEFT, picker::pickVertex);
        input.addMouseMoveCallback(editOperations::movePoints);
        input.addKeyCallback(IInput.KeyCode.KEY_A, editOperations::createFace);
        input.addKeyCallback(IInput.KeyCode.KEY_C, editOperations::copyVertices);
        input.addKeyCallback(IInput.KeyCode.KEY_D, editOperations::deleteVertex);
    }
}
