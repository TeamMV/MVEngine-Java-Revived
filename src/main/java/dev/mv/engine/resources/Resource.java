package dev.mv.engine.resources;

import dev.mv.engine.MVEngine;
import dev.mv.engine.audio.Album;
import dev.mv.engine.audio.Audio;
import dev.mv.engine.audio.Music;
import dev.mv.engine.audio.Sound;
import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.create.RenderBuilder;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.render.shared.font.Font;
import dev.mv.engine.render.shared.font.Fonts;
import dev.mv.engine.render.shared.texture.Texture;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.engine.resources.types.SpriteSheet;
import dev.mv.engine.resources.types.animation.Animation;
import dev.mv.engine.resources.types.custom.CustomResource;
import dev.mv.engine.resources.types.drawable.Drawable;
import dev.mv.engine.resources.types.drawable.StaticDrawable;
import dev.mv.engine.utils.CompositeInputStream;
import dev.mv.engine.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface Resource {

    void load();

    void drop();

    boolean isLoaded();

    String getResId();

    enum Type {
        RESOURCE(CustomResource.class),
        TEXTURE(Texture.class),
        FONT(Font.class),
        SOUND(Sound.class),
        MUSIC(Music.class),
        ;

        private Class<?> clazz;

        Type(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> clazz() {
            return clazz;
        }
    }

    static Resource create(Type type, String resId, ResourcePath... paths) throws IOException {
        try {
            if (type != Type.RESOURCE) {
                switch (type) {
                    case TEXTURE -> {
                        Texture texture = RenderBuilder.newTexture(paths[0]);
                        R.texture.register(resId, texture);
                    }
                    case FONT -> {
                        Fonts.Type t = Fonts.getTypeFromPaths(paths);
                        Font font = switch (t) {
                            case UNSUPPORTED -> {
                                Exceptions.send(new IllegalArgumentException("Unsupported type of font used in resource "+resId+"!"));
                                yield null;
                            }
                            case BMP -> new BitmapFont(paths[0], paths[1]);
                        };
                        R.font.register(resId, font);
                    }
                    case SOUND -> {
                        Audio audio = MVEngine.instance().getAudio();
                        Sound sound = audio.newSound(paths[0]);
                        R.sound.register(resId, sound);
                    }
                    case MUSIC -> {
                        Audio audio = MVEngine.instance().getAudio();
                        Music music = audio.newMusic(paths[0]);
                        R.music.register(resId, music);
                    }
                }
            } else {
                if (!paths[0].isBlank()) {
                    Exceptions.send(new IllegalArgumentException("Custom resources must have a blank ResourcePath as the first component containing the class name!"));
                    return null;
                }
                String className = paths[0].getPath();

                Class<? extends CustomResource> clazz = (Class<? extends CustomResource>) Class.forName(className);
                Constructor<? extends CustomResource> loadMethod = clazz.getDeclaredConstructor(ResourcePath[].class);
                ResourcePath[] args = new ResourcePath[paths.length - 1];
                System.arraycopy(paths, 1, args, 0, paths.length - 1);
                CustomResource res = loadMethod.newInstance((Object) args);
                R.resource.register(resId, res);
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            Exceptions.send("CUSTOM_RESOURCE_CREATION", type.clazz.getName());
        } catch (NoSuchMethodException e) {
            Exceptions.send("NO_RESOURCE_CONSTRUCTOR", type.clazz.getName());
        } catch (ClassNotFoundException e) {
            Exceptions.send(e);
        }
        return null;
    }
}
