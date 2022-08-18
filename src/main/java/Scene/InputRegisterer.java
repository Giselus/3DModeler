package Scene;

import EntityTree.EntityOperations;
import UtilsCommon.Picker;
import UtilsModel.EditOperations;
import UtilsModel.Face;
import UtilsModel.VertexPosition;

import java.util.ArrayList;

public class InputRegisterer {
    private final IInput input;
    private final EditOperations editOperations;
    private final Picker picker;
    private final SceneState sceneState;
    private final EntityOperations entityOperations;

    public InputRegisterer(IInput input, SceneState sceneState) {
        this.input = input;
        ArrayList<VertexPosition> pickedVertices = new ArrayList<>();
        ArrayList<Face> pickedFaces = new ArrayList<>();
        this.sceneState = sceneState;
        this.editOperations = new EditOperations(input, pickedVertices, sceneState);
        this.entityOperations = new EntityOperations(input, pickedVertices, sceneState);
        this.picker = new Picker(input, pickedVertices, pickedFaces, sceneState);
        registerInput();
    }

    private void registerInput() {
        input.addMouseMoveCallback(sceneState.getCamera()::processMousePosition);
        input.addMouseScrollCallback(sceneState.getCamera()::processMouseScroll);
        input.addMouseKeyCallback(IInput.MouseKeyCode.MOUSE_LEFT, picker::pick);
        input.addMouseMoveCallback(editOperations::movePoints);
        input.addKeyCallback(IInput.KeyCode.KEY_A, editOperations::createFace);
        input.addKeyCallback(IInput.KeyCode.KEY_C, editOperations::copyVertices);
        input.addKeyCallback(IInput.KeyCode.KEY_D, editOperations::deleteVertex);
        input.addKeyCallback(IInput.KeyCode.KEY_S, entityOperations::pickAllEntityVertices);
        input.addKeyCallback(IInput.KeyCode.KEY_M, entityOperations::mergeTwoEntities);
        input.addKeyCallback(IInput.KeyCode.KEY_Z, entityOperations::deleteEntities);
    }
}
