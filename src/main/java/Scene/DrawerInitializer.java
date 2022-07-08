package Scene;

import EntityTree.Entity;
import EntityTree.EntityModel;

public class DrawerInitializer {
    public static void initialize(Entity entity, RenderingUpdater renderingUpdater) {
        if(entity instanceof EntityModel)
            ((EntityModel) entity).setDrawer(new ModelDrawer(((EntityModel) entity).getFaces(), renderingUpdater));

        for(Entity child : entity.getChildren())
            initialize(child, renderingUpdater);
    }
}
