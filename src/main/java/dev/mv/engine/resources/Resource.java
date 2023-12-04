package dev.mv.engine.resources;

import dev.mv.engine.audio.Album;
import dev.mv.engine.audio.Music;
import dev.mv.engine.audio.Sound;
import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.engine.resources.types.SpriteSheet;
import dev.mv.engine.resources.types.animation.Animation;
import dev.mv.engine.resources.types.custom.CustomResource;
import dev.mv.engine.resources.types.drawable.Drawable;
import dev.mv.engine.resources.types.drawable.StaticDrawable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

public interface Resource {
    String NO_R = "";
    
    enum Type {
        RESOURCE(CustomResource.class),
        COLOR(Color.class),
        TEXTURE(TextureRegion.class),
        FONT(BitmapFont.class),
        SOUND(Sound.class),
        MUSIC(Music.class),
        ALBUM(Album.class),
        DRAWABLE(StaticDrawable.class),
        SPRITE_SHEET(SpriteSheet.class),
        ANIMATION(Animation.class),
        ;

        private Class<?> clazz;

        Type(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> clazz() {
            return clazz;
        }
    }

    String resId();
    Type type();

    default void register() {
        switch (type()) {
            default -> {}
            case RESOURCE -> R.resource.register(resId(), (CustomResource) this);
            case DRAWABLE -> R.drawable.register(resId(), (Drawable) this);
            case COLOR -> R.color.register(resId(), (Color) this);
            case SOUND -> R.sound.register(resId(), (Sound) this);
            case MUSIC -> R.music.register(resId(), (Music) this);
            case ALBUM -> R.album.register(resId(), (Album) this);
            case FONT -> R.font.register(resId(), (BitmapFont) this);
            case TEXTURE -> R.texture.register(resId(), (TextureRegion) this);
            case ANIMATION -> R.animation.register(resId(), (Animation) this);
            case SPRITE_SHEET -> R.spriteSheet.register(resId(), (SpriteSheet) this);
        }
    }

    default void load(InputStream inputStream) throws IOException {
        load(inputStream, NO_R);
    }

    void load(InputStream inputStream, String resId) throws IOException;

    static Resource create(Type type, InputStream inputStream) throws IOException {
        return create(type, inputStream, NO_R);
    }

    static Resource create(Type type, InputStream inputStream, String resId) throws IOException {
        try {
            if (type != Type.RESOURCE) {
                Resource res = (Resource) type.clazz().getDeclaredConstructor().newInstance();
                res.load(inputStream, resId);
                res.register();
                return res;
            } else {
                char c = (char) inputStream.read();
                StringBuilder className = new StringBuilder();
                while (c != '\n') {
                    className.append(c);
                    c = (char) inputStream.read();
                }

                Class<? extends CustomResource> clazz = (Class<? extends CustomResource>) Class.forName(className.toString());
                CustomResource res = clazz.getDeclaredConstructor().newInstance();
                res.load(inputStream, resId);
                res.register();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Exceptions.send("CUSTOM_RESOURCE_CREATION", Resource.class.getName());
        } catch (NoSuchMethodException e) {
            Exceptions.send("NO_EMPTY_CONSTRUCTOR", Resource.class.getName());
        } catch (ClassNotFoundException e) {
            Exceptions.send(e);
        }
        return null;
    }
}
