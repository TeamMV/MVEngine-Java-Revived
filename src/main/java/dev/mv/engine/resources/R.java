package dev.mv.engine.resources;

import dev.mv.engine.audio.Album;
import dev.mv.engine.audio.Music;
import dev.mv.engine.audio.Sound;
import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.font.Font;
import dev.mv.engine.render.shared.texture.Texture;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.engine.resources.types.SpriteSheet;
import dev.mv.engine.resources.types.animation.Animation;
import dev.mv.engine.resources.types.custom.CustomResource;
import dev.mv.engine.resources.types.drawable.Drawable;
import dev.mv.engine.utils.collection.Vec;

import java.util.HashMap;
import java.util.Map;

public class R {
    public static Res<CustomResource> resource = new Res<>();
    public static Res<Texture> texture = new Res<>();
    public static Res<TextureRegion> textureRegion = new Res<>();
    public static Res<Color> color = new Res<>();
    public static Res<Font> font = new Res<>();
    public static Res<Sound> sound = new Res<>();
    public static Res<Music> music = new Res<>();
    public static Res<Album> album = new Res<>();
    public static Res<Drawable> drawable = new Res<>();
    public static Res<SpriteSheet> spriteSheet = new Res<>();
    public static Res<Animation> animation = new Res<>();
    private static boolean isReady = false;

    public static boolean isReady() {
        return isReady;
    }

    static void setIsReady(boolean isReady) {
        R.isReady = isReady;
    }

    public static class Res<T> {
        private Map<String, T> map = new HashMap<>();

        Res() {
        }

        public T get(String id) {
            return get0(id);
        }

        public T get(String id, String prefix) {
            if (prefix == null || prefix.isBlank()) return get0(id);
            else return get0(prefix + "." + id);
        }


        private T get0(String id) {
            try {
                if (!map.containsKey(id)) {
                    Exceptions.send("NO_SUCH_RESOURCE", id);
                    return null;
                }
                T t = map.get(id);
                return t;
            } catch (Exception e) {
                Exceptions.send("NO_SUCH_RESOURCE", id);
                return null;
            }
        }

        public T[] all() {
            return new Vec<T>(map.values()).asArray();
        }

        public void register(String id, T res) {
            if (id != null && !id.isBlank()) {
                if (!map.containsKey(id)) {
                    map.put(id, res);
                } else {
                    Exceptions.send("DUPLICATE_RESOURCE_ID", id);
                }
            }
        }
    }
}