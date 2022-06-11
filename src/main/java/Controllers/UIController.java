package Controllers;

import EntityTree.Entity;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.*;
import imgui.type.ImBoolean;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.opengl.GL;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class UIController{
    private UIController(){}

    private static UIController instance;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private long mainWindow;
    private String glslVersion;

    private int width = 1200;
    private int height = 800;

    private Entity selectedEntity = null;

    public long getMainWindow(){
        return mainWindow;
    }

    public static UIController getInstance(){
        if(instance == null)
            instance = new UIController();
        return instance;
    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
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

        frameInit();
        showMainWindow();
        showSceneWindow();
        showEntitiesWindow();
        showInspectorWindow();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
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

    private void frameInit() {
        ImGuiIO io = ImGui.getIO();

        ImGui.setNextWindowPos(0,0);
        ImGui.setNextWindowSize(io.getDisplaySizeX(),io.getDisplaySizeY());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding,0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize,0);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding,0,0);
    }
    private void showMainWindow() {
        ImGui.begin("MainWindow", new ImBoolean(), ImGuiWindowFlags.MenuBar |
                ImGuiWindowFlags.NoDocking | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus |
                ImGuiWindowFlags.NoNavFocus | ImGuiWindowFlags.NoBackground | ImGuiWindowFlags.NoDecoration);

        ImGui.popStyleVar(3);

        int dockSpaceId = ImGui.getID("MyDockSpace");
        ImGui.dockSpace(dockSpaceId, 0,0, ImGuiDockNodeFlags.PassthruCentralNode);

        showMainMenuBar();

        ImGui.end();
    }

    private void showSceneWindow() {
        ImGui.begin("SceneWindow", new ImBoolean(true), 0);
        ImGui.getWindowDrawList().addImage(
                RenderingController.getInstance().getSceneTexture(),
                0,0,
                width,
                height,
                0,1,1,0
        );
        ImGui.end();
    }

    private void showEntitiesWindow() {
        ImGui.begin("Entities", new ImBoolean(), 0);
        int baseFlags = ImGuiTreeNodeFlags.OpenOnArrow | ImGuiTreeNodeFlags.OpenOnDoubleClick |
                ImGuiTreeNodeFlags.SpanAvailWidth | ImGuiTreeNodeFlags.DefaultOpen;
        showEntitiesTree(SceneController.getInstance().getRoot(), baseFlags);
        ImGui.end();
    }

    private void showInspectorWindow() {
        ImGui.begin("Inspector", new ImBoolean(), 0);
        if(selectedEntity != null)
            showInspector(selectedEntity);
        ImGui.end();
    }

    private void showMainMenuBar() {
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

    private void showInspector(Entity entity) {
        final int baseFlags = ImGuiTreeNodeFlags.DefaultOpen;
        if(ImGui.treeNodeEx("Transform",baseFlags)) {
            show3DSetter("Position",
                    () -> entity.getTransform().getLocalTranslation(),
                    (val) -> entity.getTransform().setLocalTranslation(val),
                    baseFlags, 0.005f);
            show3DSetter("Scale",
                    () -> entity.getTransform().getLocalScale(),
                    (val) -> entity.getTransform().setLocalScale(val),
                    baseFlags, 0.005f);
            show3DSetter("Rotation",
                    () -> entity.getTransform().getLocalRotation(),
                    (val) -> entity.getTransform().setLocalRotation(val),
                    baseFlags, 0.1f);
            entity.updateSelfAndChildren();
            ImGui.treePop();
        }
    }

    private void show3DSetter(String name, Supplier<Vector3fc> getter, Consumer<Vector3f> setter,
                              final int baseFlags, final float step) {
        final float[] tmp = new float[1];
        if(ImGui.treeNodeEx(name,baseFlags)) {
            float x,y,z;
            tmp[0] = getter.get().x();
            ImGui.dragFloat("x",tmp,step);
            x = tmp[0];
            tmp[0] = getter.get().y();
            ImGui.dragFloat("y",tmp,step);
            y = tmp[0];
            tmp[0] = getter.get().z();
            ImGui.dragFloat("z",tmp,step);
            z = tmp[0];
            setter.accept(new Vector3f(x,y,z));
            ImGui.treePop();
        }
    }
}
