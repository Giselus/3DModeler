package Scene;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLFWAppWindow implements IAppWindow{

    private long windowID;
    private int width;
    private int height;

    @Override
    public boolean shouldBeClosed(){
        return glfwWindowShouldClose(windowID);
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void initialize() {
        if(!glfwInit())
            return;
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_FALSE);

        windowID = glfwCreateWindow(width,height,"Title==null",NULL,NULL);

        glfwMakeContextCurrent(windowID);
        glfwSwapInterval(1);
        glfwShowWindow(windowID);

        GL.createCapabilities();
    }

    @Override
    public void destroy(){
        glfwDestroyWindow(windowID);
        glfwTerminate();
    }

    public long getWindowID(){
        return windowID;
    }


}

