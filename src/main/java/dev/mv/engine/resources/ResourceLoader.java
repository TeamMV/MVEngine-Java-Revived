package dev.mv.engine.resources;

import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.utils.Utils;
import dev.mv.utils.collection.Vec;
import dev.mv.utils.generic.pair.Pair;

public class ResourceLoader {
    private static Vec<Pair<Integer, ResourceReference>> refs = new Vec<Pair<Integer, ResourceReference>>();

    public static void markColor(String resourceId, String colorString) {
        refs.push(new Pair<>(0, new ResourceReference(colorString, resourceId, Resource.Type.COLOR)));
    }

    public static void markTexture(String resourceId, String path) {
        refs.push(new Pair<>(0, new ResourceReference(path, resourceId, Resource.Type.TEXTURE)));
    }

    public static void markTextureRegion(String resourceId, String targetResourceId, int x, int y, int width, int height) {
        refs.push(new Pair<>(0, new ResourceReference(Utils.concat("", targetResourceId, x, y, width, height), resourceId, Resource.Type.TEXTURE_REGION)));
    }

    public static void markModel(String resourceId, String path) {
        refs.push(new Pair<>(1, new ResourceReference(path, resourceId, Resource.Type.MESH)));
    }

    public static void markFont(String resourceId, String pngFile, String fntFile) {
        refs.push(new Pair<>(0, new ResourceReference(Utils.concat(":", pngFile, fntFile), resourceId, Resource.Type.FONT)));
    }

    public static void markSound(String resourceId, String path) {
        refs.push(new Pair<>(0, new ResourceReference(path, resourceId, Resource.Type.SOUND)));
    }

    public static void markLayout(String resourceId, String path) {
        refs.push(new Pair<>(1, new ResourceReference(path, resourceId, Resource.Type.GUI_LAYOUT)));
    }

    public static void markTheme(String resourceId, String path) {
        refs.push(new Pair<>(1, new ResourceReference(path, resourceId, Resource.Type.GUI_THEME)));
    }

    public static void markPage(String resourceId, String path) {
        refs.push(new Pair<>(2, new ResourceReference(path, resourceId, Resource.Type.GUI_PAGE)));
    }

    private static void register(String id, Resource resource) {
        if (resource instanceof TextureRegion t) R.texture.register(id, t);
        if (resource instanceof Color t) R.color.register(id, t);
        if (resource instanceof BitmapFont t) R.font.register(id, t);
    }

    private static class ResourceReference {
        private String path;
        private String id;
        private Resource.Type type;

        public ResourceReference(String path, String id, Resource.Type type) {
            this.path = path;
            this.id = id;
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public String getId() {
            return id;
        }

        public Resource.Type getType() {
            return type;
        }
    }
}
