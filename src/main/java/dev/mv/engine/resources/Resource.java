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
import dev.mv.engine.resources.types.drawable.Drawable;

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
        DRAWABLE(Drawable.class),
        SPRITE_SHEET(SpriteSheet.class),
        ANIMATION(Animation.class),
        ;

        private Class<? extends Resource> clazz;

        Type(Class<? extends Resource> clazz) {
            this.clazz = clazz;
        }

        public Class<? extends Resource> clazz() {
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

    static Resource create(Class<?> clazz, InputStream inputStream) throws IOException {
        return create(clazz, inputStream, NO_R);
    }

    static Resource create(Class<?> clazz, InputStream inputStream, String resId) throws IOException {
        try {
            Resource res = (Resource) clazz.getDeclaredConstructor().newInstance();
            res.load(inputStream, resId);
            return res;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Exceptions.send("CUSTOM_RESOURCE_CREATION", Resource.class.getName());
        } catch (NoSuchMethodException e) {
            Exceptions.send("NO_EMPTY_CONSTRUCTOR", Resource.class.getName());
        }
        return null;
    }
}
