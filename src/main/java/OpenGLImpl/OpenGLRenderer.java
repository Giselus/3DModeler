package OpenGLImpl;

import EntityTree.EntityModel;
import Scene.IRenderer;
import Scene.SceneState;
import UtilsCommon.Camera;
import Shaders.Shader;
import UtilsModel.Mesh;
import Windows.EntitiesWindow;
import Windows.InspectorWindow;
import Windows.MainWindow;
import Windows.SceneWindow;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.glTexImage2DMultisample;

public class OpenGLRenderer implements IRenderer {

    private ImGuiImplGlfw imGuiGlfw;
    private ImGuiImplGl3 imGuiGl3;

    private SceneState sceneState;
    private GLFWAppWindow appWindow;

    private String glslVersion;

    private Shader mainShader;
    private Shader wireframeShader;
    private Shader pointsShader;
    private Shader activeShader;

    private int drawingMode = GL_TRIANGLES;

    private int multiSampleFbo;
    private int fbo;
    private int rbo;
    private int multiSampleTexture;

    public OpenGLRenderer(SceneState sceneState, GLFWAppWindow appWindow, String glslVersion){
        this.sceneState = sceneState;
        this.appWindow = appWindow;
        this.glslVersion = glslVersion;
    }
    @Override
    public void initialize() {
        initImGui();
        prepareBuffers();
    }

    @Override
    public void destroy(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    @Override
    public void startFrame() {
        glBindFramebuffer(GL_FRAMEBUFFER,multiSampleFbo);

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void render(EntityModel entityModel) {
        Mesh mesh = entityModel.getMesh();
        if(mesh.getMeshDrawer() == null){
            mesh.setMeshDrawer(new OpenGLMeshDrawer(mesh.getFaces()));
        }else{
            mesh.getMeshDrawer().recalculate();
        }
        setActiveShader(mainShader);
        setDrawingMode(GL_TRIANGLES);

        drawMeshWithActiveShader(entityModel);

        setActiveShader(wireframeShader);
        drawMeshWithActiveShader(entityModel);

        setActiveShader(pointsShader);
        setDrawingMode(GL_POINTS);
        drawMeshWithActiveShader(entityModel);

        //
//        setDrawingMode(GL_TRIANGLES);
//        setActiveShader(mainShader);
//
//        setActiveShader(mainShader);
//        setDrawingMode(GL_TRIANGLES);
//
//        sceneState.getRoot().drawSelfAndChildren(); //todo
//        if(MainController.getInstance().getMode() == MainController.Mode.EDIT){
//            setActiveShader(wireframeShader);
//            SceneController.getInstance().getRoot().update();
//            setActiveShader(pointsShader);
//            setDrawingMode(GL_POINTS);
//            SceneController.getInstance().getRoot().update();
//        }
    }

    //TODO: logic should be moved outside, there should be function to rather render one window
    @Override
    public void renderGUI() {
        glBindFramebuffer(GL_READ_FRAMEBUFFER,multiSampleFbo);
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER,fbo);
        glBlitFramebuffer(0,0,sceneState.getSceneWindowWidth(), sceneState.getSceneWindowHeight(),
                0,0,sceneState.getSceneWindowWidth(), sceneState.getSceneWindowHeight(),
                GL_COLOR_BUFFER_BIT,GL_NEAREST);

        glBindFramebuffer(GL_FRAMEBUFFER,0);

        glClearColor(0.6f, 0.6f, 0.6f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        imGuiGlfw.newFrame();
        ImGui.newFrame();

        MainWindow.show();
        SceneWindow.show(sceneState.getSceneTexture());
        EntitiesWindow.show(sceneState.getRoot(), sceneState);
        InspectorWindow.show(sceneState);

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        glfwSwapBuffers(appWindow.getWindowID());

    }

    //TODO: this should be moved somewhere else

    private void setDrawingMode(int mode) {
        drawingMode = mode;
    }

    private void setActiveShader(Shader shader) {
        shader.use();
        activeShader = shader;
        prepareShader();
    }

    private void drawMeshWithActiveShader(EntityModel entity){
        OpenGLMeshDrawer drawer = (OpenGLMeshDrawer) entity.getMesh().getMeshDrawer();
        activeShader.setMatrix4("model",entity.getTransform().getGlobalModelMatrix());
        drawer.draw(drawingMode);
    }

    private void prepareShader() {
        Camera camera = sceneState.getCamera();
        Matrix4f projection = camera.getProjectionMatrix();
        Matrix4f view = camera.getViewMatrix();
        activeShader.setMatrix4("projection",projection);
        activeShader.setMatrix4("view",view);
        activeShader.setVector3f("viewPos", camera.getPosition());
        activeShader.setVector3f("pointLights[0].position", new Vector3f(5.0f,1.0f,5.0f));
        activeShader.setVector3f("pointLights[0].ambient", new Vector3f(0.05f));
        activeShader.setVector3f("pointLights[0].diffuse", new Vector3f(0.8f));
        activeShader.setVector3f("pointLights[0].specular", new Vector3f(1f));
        activeShader.setFloat("pointLights[0].constant", 1.0f);
        activeShader.setFloat("pointLights[0].linear", 0.09f);
        activeShader.setFloat("pointLights[0].quadratic", 0.032f);
        activeShader.setFloat("material.shininess",32.0f);
        activeShader.setFloat("material.diffuse",0.1f);
        activeShader.setFloat("material.specular",0.5f);
    }

    private void initImGui(){
        imGuiGlfw = new ImGuiImplGlfw();
        imGuiGl3 = new ImGuiImplGl3();
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        ImGui.styleColorsDark();
        imGuiGlfw.init(appWindow.getWindowID(), true);
        imGuiGl3.init(glslVersion);
    }

    private void prepareBuffers() {
        glEnable(GL_DEPTH_TEST);

        mainShader = new Shader("src/main/shaders/mainVertexShader.vs","src/main/shaders/mainFragmentShader.fs",
                "src/main/shaders/mainGeometryShader.gs");
        wireframeShader = new Shader("src/main/shaders/wireframeVertexShader.vs",
                "src/main/shaders/wireframeFragmentShader.fs", "src/main/shaders/wireframeGeometryShader.gs");
        pointsShader = new Shader("src/main/shaders/pointsVertexShader.vs",
                "src/main/shaders/pointsFragmentShader.fs", "src/main/shaders/pointsGeometryShader.gs");


        multiSampleFbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,multiSampleFbo);

        multiSampleTexture = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,multiSampleTexture);
        glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE,4,GL_RGB,
                sceneState.getSceneWindowWidth(),sceneState.getSceneWindowHeight(), true);
        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE,0);

        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D_MULTISAMPLE,multiSampleTexture,0);

        rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER,rbo);
        glRenderbufferStorageMultisample(GL_RENDERBUFFER,4,GL_DEPTH24_STENCIL8,
                sceneState.getSceneWindowWidth(), sceneState.getSceneWindowHeight());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_STENCIL_ATTACHMENT,GL_RENDERBUFFER,rbo);

        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,fbo);

        sceneState.setSceneTexture(glGenTextures());
        glBindTexture(GL_TEXTURE_2D,sceneState.getSceneTexture());
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,sceneState.getSceneWindowWidth(), sceneState.getSceneWindowHeight(),
                0,GL_RGB,GL_UNSIGNED_BYTE,(ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,sceneState.getSceneTexture(),0);
        glBindFramebuffer(GL_FRAMEBUFFER,0);

        glLineWidth(1f);
        glPointSize(5);

        //setDrawers();
    }



}
