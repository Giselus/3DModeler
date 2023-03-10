package Shaders;

import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class Shader {
    public int id;

    public Shader(String vertexPath, String fragmentPath, String geometryPath){
        CharSequence vertexCode;
        CharSequence fragmentCode;
        CharSequence geometryCode = null;
        try {
            vertexCode = Files.readString(Paths.get(vertexPath));
            fragmentCode = Files.readString(Paths.get(fragmentPath));
            if(geometryPath != null)
                geometryCode = Files.readString(Paths.get(geometryPath));
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        int vertex, fragment, geometry = 0;
        vertex = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertex, vertexCode);
        glCompileShader(vertex);

        fragment = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragment,fragmentCode);
        glCompileShader(fragment);

        if(geometryPath != null){
            geometry = glCreateShader(GL_GEOMETRY_SHADER);
            glShaderSource(geometry,geometryCode);
            glCompileShader(geometry);
        }

        id = glCreateProgram();
        glAttachShader(id,vertex);
        glAttachShader(id,fragment);
        if(geometryPath != null)
            glAttachShader(id,geometry);
        glLinkProgram(id);

        glDeleteShader(vertex);
        glDeleteShader(fragment);
        if(geometryPath != null)
            glDeleteShader(geometry);
    }

    public Shader(String vertexPath, String fragmentPath){
        this(vertexPath,fragmentPath,null);
    }

    public void use(){
        glUseProgram(id);
    }

    public void setFloat(String name, float value){
        glUniform1f(glGetUniformLocation(id,name),value);
    }

    public void setInteger(String name, int value){
        glUniform1i(glGetUniformLocation(id,name),value);
    }

    public void setVector2f(String name, float x, float y){
        glUniform2f(glGetUniformLocation(id,name),x,y);
    }

    public void setVector2f(String name, Vector2f value){
        glUniform2f(glGetUniformLocation(id,name),value.x,value.y);
    }

    public void setVector2f(String name, Vector2fc value){
        glUniform2f(glGetUniformLocation(id,name),value.x(),value.y());
    }

    public void setVector3f(String name, float x, float y, float z){
        glUniform3f(glGetUniformLocation(id,name),x,y,z);
    }

    public void setVector3f(String name, Vector3f value){
        glUniform3f(glGetUniformLocation(id,name),value.x,value.y, value.z);
    }

    public void setVector3f(String name, Vector3fc value){
        glUniform3f(glGetUniformLocation(id,name),value.x(),value.y(), value.z());
    }

    public void setVector4f(String name, float x, float y, float z, float w){
        glUniform4f(glGetUniformLocation(id,name),x,y,z,w);
    }

    public void setVector4f(String name, Vector4f value){
        glUniform4f(glGetUniformLocation(id,name),value.x,value.y, value.z, value.w);
    }

    public void setVector4f(String name, Vector4fc value){
        glUniform4f(glGetUniformLocation(id,name),value.x(),value.y(), value.z(), value.w());
    }

    public void setMatrix4(String name, Matrix4f value){
        try(MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(glGetUniformLocation(id, name), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setMatrix4(String name, Matrix4fc value){
        try(MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(glGetUniformLocation(id, name), false, value.get(stack.mallocFloat(16)));
        }
    }

}
