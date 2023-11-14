package dev.mv.engine.resources;

import dev.mv.engine.audio.Album;
import dev.mv.engine.audio.Music;
import dev.mv.engine.audio.Sound;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.engine.resources.types.SpriteSheet;
import dev.mv.engine.resources.types.animation.Animation;
import dev.mv.engine.resources.types.drawable.Drawable;

public interface Resource {
    String NO_R = "noR";
    
    enum Type {
        RESOURCE,
        COLOR,
        TEXTURE,
        FONT,
        SOUND,
        MUSIC,
        ALBUM,
        DRAWABLE,
        SPRITE_SHEET,
        ANIMATION,
    }

    String resId();
    Type type();

    default void register() {
        switch (type()) {
            default -> {}
            case RESOURCE -> R.resource.register(resId(), this);
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
}
