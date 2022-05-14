package Controllers;

import ModelLoader.OBJLoader;
import UtilsCommon.Camera;
import UtilsCommon.Model;
import UtilsCommon.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;


public class RenderingController {
    private RenderingController(){}

    private static RenderingController instance;

    public static  RenderingController getInstance(){
        if(instance == null)
            instance = new RenderingController();
        return instance;
    }

    private Camera camera;
    private Shader mainShader;
    private Shader wireframeShader;
    private Shader pointsShader;
    private Shader activeShader;

    private int drawingMode = GL_TRIANGLES;

    private LinkedList<Model> models;

    private int fbo;
    private int rbo;
    private int sceneTexture;

    public void setActiveShader(Shader shader){
        ;
        shader.use();
        activeShader = shader;
        prepareShader();
    }

    public void setDrawingMode(int mode){
        drawingMode = mode;
    }

    public int getDrawingMode(){
        return drawingMode;
    }

    public int getSceneTexture(){
        return sceneTexture;
    }

    public Camera getCamera(){
        return camera;
    }

    public void initialize(){
        glEnable(GL_DEPTH_TEST);

        OBJLoader loader = new OBJLoader();
        models = loader.load("src/main/data/cube.obj");

        camera = new Camera();

        mainShader = new Shader("src/main/shaders/vertexShader.vs","src/main/shaders/fragmentShader.fs");
        wireframeShader = new Shader("src/main/shaders/wireframeVertexShader.vs",
                "src/main/shaders/wireframeFragmentShader.fs", "src/main/shaders/wireframeGeometryShader.gs");
        pointsShader = new Shader("src/main/shaders/pointsVertexShader.vs",
                "src/main/shaders/pointsFragmentShader.fs", "src/main/shaders/pointsGeometryShader.gs");


        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,fbo);

        sceneTexture = glGenTextures();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D,sceneTexture);
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGB,UIController.getInstance().getWidth(),UIController.getInstance().getHeight(),
                0,GL_RGB,GL_UNSIGNED_BYTE,(ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,sceneTexture,0);

        rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER,rbo);
        glRenderbufferStorage(GL_RENDERBUFFER,GL_DEPTH24_STENCIL8,UIController.getInstance().getWidth(),
                UIController.getInstance().getHeight());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_STENCIL_ATTACHMENT,GL_RENDERBUFFER,rbo);

        glBindFramebuffer(GL_FRAMEBUFFER,0);

        glLineWidth(0.2f);
        glPointSize(3);
    }

    public void update(){
        render();
    }

    public void render() {
        glBindFramebuffer(GL_FRAMEBUFFER,fbo);

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        setDrawingMode(GL_TRIANGLES);
        setActiveShader(mainShader);
        for(Model m: models)
            m.Draw(activeShader);
        if(MainController.getInstance().getMode() == MainController.Mode.Edit) {
            setActiveShader(wireframeShader);
            for (Model m : models) {
                m.Draw(activeShader);
            }
            setDrawingMode(GL_POINTS);
            setActiveShader(pointsShader);
            for(Model m : models){
                m.Draw(activeShader);
            }
        }

        glBindFramebuffer(GL_FRAMEBUFFER,0);

        glClearColor(0.6f, 0.6f, 0.6f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void prepareShader(){
        Matrix4f projection = new Matrix4f().setPerspective((float)Math.toRadians(camera.getZoom()),
                (float)UIController.getInstance().getWidth()/UIController.getInstance().getHeight(), 0.1f, 100.0f);
        Matrix4f view = camera.getViewMatrix();
        Matrix4f model = new Matrix4f().identity();
        model.translate(new Vector3f(0,0,0));
        activeShader.setMatrix4("projection",projection);
        activeShader.setMatrix4("view",view);
        activeShader.setMatrix4("model",model);
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
}