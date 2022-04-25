import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiDockNodeFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
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

    private void initWindow(){
        if(!glfwInit())
            return;
        glslVersion = "#version 130";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);

        mainWindow = glfwCreateWindow(1200,800,"Title==null",NULL,NULL);

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

        ImGui.begin("TestWindow", new ImBoolean(true), 0);
        ImGui.text("test");
        ImGui.end();

        ImGui.begin("TestWindow2", new ImBoolean(), 0);
        ImGui.text("test2");
        ImGui.end();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }
}
