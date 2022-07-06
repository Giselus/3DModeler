//package Controllers;
//
//import Windows.EntitiesWindow;
//import Windows.InspectorWindow;
//import Windows.MainWindow;
//import Windows.SceneWindow;
//import imgui.ImGui;
//import imgui.ImGuiIO;
//import imgui.flag.*;
//import org.lwjgl.glfw.Callbacks;
//import org.lwjgl.opengl.GL;
//import imgui.gl3.ImGuiImplGl3;
//import imgui.glfw.ImGuiImplGlfw;
//
//import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.system.MemoryUtil.NULL;
//
//public class UIController{
//    private UIController(){}
//    private static UIController instance;
//
//    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
//    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
//
//    private long mainWindow;
//    private String glslVersion;
//
//    private final int width = 1200;
//    private final int height = 800;
//
//    public long getMainWindow(){
//        return mainWindow;
//    }
//
//    public static UIController getInstance(){
//        if(instance == null)
//            instance = new UIController();
//        return instance;
//    }
//
//    public int getWidth(){
//        return width;
//    }
//    public int getHeight(){
//        return height;
//    }
//
//    public void initialize(){
//        initWindow();
//        initImGui();
//
//        imGuiGlfw.init(mainWindow, true);
//        imGuiGl3.init(glslVersion);
//    }
//
//    public void destroy(){
//        imGuiGl3.dispose();
//        imGuiGlfw.dispose();
//        ImGui.destroyContext();
//        Callbacks.glfwFreeCallbacks(mainWindow);
//        glfwDestroyWindow(mainWindow);
//        glfwTerminate();
//    }
//
//    public void update(){
//        imGuiGlfw.newFrame();
//        ImGui.newFrame();
//
//        MainWindow.show();
//        SceneWindow.show(width, height);
//        EntitiesWindow.show(SceneController.getInstance().getRoot());
//        InspectorWindow.show();
//
//        ImGui.render();
//        imGuiGl3.renderDrawData(ImGui.getDrawData());
//    }
//
//    private void initWindow(){
//        if(!glfwInit())
//            return;
//        glslVersion = "#version 130";
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
//        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
//        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
//        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
//
//        mainWindow = glfwCreateWindow(width,height,"Title==null",NULL,NULL);
//
//        glfwMakeContextCurrent(mainWindow);
//        glfwSwapInterval(1);
//        glfwShowWindow(mainWindow);
//
//        GL.createCapabilities();
//    }
//
//    private void initImGui(){
//        ImGui.createContext();
//        ImGuiIO io = ImGui.getIO();
//        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
//        ImGui.styleColorsDark();
//    }
//}
