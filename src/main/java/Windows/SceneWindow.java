package Windows;

//import Controllers.RenderingController;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImVec4;
import imgui.flag.ImGuiWindowFlags;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class SceneWindow {

    private static float windowSizeX;
    private static float windowSizeY;

    private static float positionX;
    private static float positionY;

    private static float textureSizeX;
    private static float textureSizeY;

    public static Vector2f mapPosition(float x, float y){
        float scale = Math.min(textureSizeX/windowSizeX,textureSizeY/windowSizeY);
        float texturePositionX = positionX - (textureSizeX - scale * windowSizeX)/(2f * scale);
        float texturePositionY = positionY - (textureSizeY - scale * windowSizeY)/(2f * scale);
        return new Vector2f((x - texturePositionX) / (textureSizeX/scale),
                (y - texturePositionY) / (textureSizeY/scale));
    }

    static public void show(final int sceneTexture) {
        ImGui.begin("SceneWindow", new ImBoolean(true), ImGuiWindowFlags.NoBackground);
        int textureWidthTmp[] = new int[1];
        int textureHeightTmp[] = new int[1];
        glGetTexLevelParameteriv(GL_TEXTURE_2D,0,GL_TEXTURE_WIDTH,textureWidthTmp);
        glGetTexLevelParameteriv(GL_TEXTURE_2D,0,GL_TEXTURE_HEIGHT,textureHeightTmp);
        textureSizeX = textureWidthTmp[0];
        textureSizeY = textureHeightTmp[0];

        windowSizeX = ImGui.getContentRegionAvailX();
        windowSizeY = ImGui.getContentRegionAvailY();

        positionX = ImGui.getWindowPosX() + ImGui.getWindowContentRegionMinX();
        positionY = ImGui.getWindowPosY() + ImGui.getWindowContentRegionMinY();
        float scale = Math.min(textureSizeX/windowSizeX,textureSizeY/windowSizeY);

        ImGui.image(sceneTexture,windowSizeX,windowSizeY,
                ((textureSizeX - scale * windowSizeX)/2f)/textureSizeX,
                (textureSizeY - (textureSizeY - scale * windowSizeY)/2f)/textureSizeY,
                (textureSizeX - (textureSizeX - scale * windowSizeX)/2f)/textureSizeX,
                ((textureSizeY - scale * windowSizeY)/2f)/textureSizeY);
//        ImGui.getWindowDrawList().addImage(
//                sceneTexture,
//                0,0,
//                width,
//                height,
//                0,1,1,0
//        );
        ImGui.end();
    }
}
