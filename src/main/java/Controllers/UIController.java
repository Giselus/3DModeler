package Controllers;

import EntityTree.Entity;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.*;
import imgui.type.ImBoolean;
import org.joml.Vector3f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.opengl.GL;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class UIController{
    private UIController(){}

    private static UIController Instance;

    public long getMainWindow(){
        return mainWindow;
    }

    public static UIController getInstance(){
        if(Instance == null)
            Instance = new UIController();
        return Instance;
    }

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private long mainWindow;
    private String glslVersion;

    private int width = 1200;
    private int height = 800;

    private Entity selectedEntity = null;

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    private void initWindow(){
        if(!glfwInit())
            return;
        glslVersion = "#version 130";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);

        mainWindow = glfwCreateWindow(width,height,"Title==null",NULL,NULL);

        glfwMakeContextCurrent(mainWindow);
        glfwSwapInterval(1);
        glfwShowWindow(mainWindow);

        GL.createCapabilities();
    }

    private void initImGui(){
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        ImGui.styleColorsDark();

    }

    public void initialize(){
        initWindow();
        initImGui();

        imGuiGlfw.init(mainWindow, true);
        imGuiGl3.init(glslVersion);
    }

    public void destroy(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(mainWindow);
        glfwDestroyWindow(mainWindow);
        glfwTerminate();
    }

    public void update(){
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        //TODO: extract this part and move it
        ImGuiIO io = ImGui.getIO();

        ImGui.setNextWindowPos(0,0);
        ImGui.setNextWindowSize(io.getDisplaySizeX(),io.getDisplaySizeY());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding,0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize,0);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding,0,0);

        ImGui.begin("MainWindow", new ImBoolean(), ImGuiWindowFlags.MenuBar |
                ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus |
                ImGuiWindowFlags.NoNavFocus | ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoDecoration);

        ImGui.popStyleVar(3);

        int dockspace_id = ImGui.getID("MyDockSpace");
        ImGui.dockSpace(dockspace_id, 0,0, ImGuiDockNodeFlags.PassthruCentralNode);

        if(ImGui.beginMainMenuBar()) {
            if(ImGui.beginMenu("File")){
                ImGui.endMenu();
            }
            if(ImGui.beginMenu("Windows")){
                ImGui.endMenu();
            }
            if(ImGui.beginMenu("Help")){
                ImGui.endMenu();
            }
        }
        ImGui.endMainMenuBar();

        ImGui.end();

        ImGui.begin("SceneWindow", new ImBoolean(true), 0);
        ImGui.getWindowDrawList().addImage(
                RenderingController.getInstance().getSceneTexture(),
                0,0,
                width,
                height,
                0,1,1,0
        );
        ImGui.end();

        ImGui.begin("Entities", new ImBoolean(), 0);
        int baseFlags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick |
                ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen;
        showEntitiesTree(SceneController.getInstance().getRoot(), baseFlags);
        ImGui.end();

        ImGui.begin("Inspector", new ImBoolean(), 0);
        if(selectedEntity != null)
            showInspector(selectedEntity);
        ImGui.end();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private void showEntitiesTree(final Entity entity, final int baseFlags) {
        int nodeFlags = baseFlags;
        if(entity.getUnmodifiableChildren().size() == 0)
            nodeFlags |= ImGuiTreeNodeFlags.Bullet;
        if(entity == selectedEntity)
            nodeFlags |= ImGuiTreeNodeFlags.Selected;
        if(ImGui.treeNodeEx(entity.getName(), nodeFlags)) {
            if(ImGui.isItemClicked()){
                selectedEntity = entity;
            }
            for(Entity child : entity.getUnmodifiableChildren())
                showEntitiesTree(child, baseFlags);

            ImGui.treePop();
        }
    }

    public void showInspector(Entity entity) {
        final int baseFlags = ImGuiTreeNodeFlags.DefaultOpen;
        final float[] tmp = new float[1];
        if(ImGui.treeNodeEx("Transform",baseFlags)) {
            if(ImGui.treeNodeEx("Position",baseFlags)) {
                float x,y,z;
                tmp[0] = entity.getTransform().getLocalTranslation().x();
                ImGui.dragFloat("x",tmp,0.005f);
                x = tmp[0];
                tmp[0] = entity.getTransform().getLocalTranslation().y();
                ImGui.dragFloat("y",tmp,0.005f);
                y = tmp[0];
                tmp[0] = entity.getTransform().getLocalTranslation().z();
                ImGui.dragFloat("z",tmp,0.005f);
                z = tmp[0];
                entity.getTransform().setLocalTranslation(new Vector3f(x,y,z));
                entity.updateSelfAndChildren();
                ImGui.treePop();
            }
            if(ImGui.treeNodeEx("Scale",baseFlags)) {
                float x,y,z;
                tmp[0] = entity.getTransform().getLocalScale().x();
                ImGui.dragFloat("x",tmp,0.005f);
                x = tmp[0];
                tmp[0] = entity.getTransform().getLocalScale().y();
                ImGui.dragFloat("y",tmp,0.005f);
                y = tmp[0];
                tmp[0] = entity.getTransform().getLocalScale().z();
                ImGui.dragFloat("z",tmp,0.005f);
                z = tmp[0];
                entity.getTransform().setLocalScale(new Vector3f(x,y,z));
                entity.updateSelfAndChildren();
                ImGui.treePop();
            }
            if(ImGui.treeNodeEx("Rotation",baseFlags)) {
                float x,y,z;
                tmp[0] = entity.getTransform().getLocalRotation().x();
                ImGui.dragFloat("x",tmp,0.005f);
                x = tmp[0];
                tmp[0] = entity.getTransform().getLocalRotation().y();
                ImGui.dragFloat("y",tmp,0.005f);
                y = tmp[0];
                tmp[0] = entity.getTransform().getLocalRotation().z();
                ImGui.dragFloat("z",tmp,0.005f);
                z = tmp[0];
                entity.getTransform().setLocalRotation(new Vector3f(x,y,z));
                entity.updateSelfAndChildren();
                ImGui.treePop();
            }
            ImGui.treePop();
        }
    }
}
