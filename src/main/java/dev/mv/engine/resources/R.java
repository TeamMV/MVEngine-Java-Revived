package dev.mv.engine.resources;

import dev.mv.engine.audio.Album;
import dev.mv.engine.audio.Music;
import dev.mv.engine.audio.Sound;
import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.engine.resources.types.SpriteCollection;
import dev.mv.engine.resources.types.SpriteSheet;
import dev.mv.engine.resources.types.animation.Animation;
import dev.mv.engine.resources.types.drawable.Drawable;
import dev.mv.utils.collection.Vec;

import java.util.HashMap;
import java.util.Map;

public class R {
    public static Res<Resource> resource = new Res<>();
    public static Res<TextureRegion> texture = new Res<>();
    public static Res<Color> color = new Res<>();
    public static Res<BitmapFont> font = new Res<>();
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

    public static class Res<T extends Resource> {
        private Map<String, T> map = new HashMap<>();

        Res() {
        }

        public T get(String id) {
            try {
                T t = map.get(id);
                if (t instanceof HeavyResource h) {
                    h.load();
                }
                return t;
            } catch (Exception e) {
                Exceptions.send(new ResourceNotFoundException("There is no resource with resource-id of \"" + id + "\"!"));
                return null;
            }
        }

        public T[] all() {
            return new Vec<T>(map.values()).asArray();
        }

        public void register(String id, T res) {            
            if (!id.equals(Resource.NO_R)) {
                if (!map.containsKey(id)) {
                    map.put(id, res);
                } else {
                    Exceptions.send("DUPLICATE_RESOURCE_ID", id);
                }
            }
        }
    }
}