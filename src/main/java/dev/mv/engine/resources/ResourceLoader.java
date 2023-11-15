package dev.mv.engine.resources;

import dev.mv.engine.MVEngine;
import dev.mv.engine.render.shared.Color;
import dev.mv.engine.render.shared.font.BitmapFont;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.utils.Utils;
import dev.mv.utils.collection.Vec;
import dev.mv.utils.generic.pair.Pair;
import dev.mv.utils.logger.Logger;

import java.io.InputStream;

public class ResourceLoader {
    private static final Vec<ResourceReference> refs = new Vec<>();

    public void markResource(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.RESOURCE));
    }

    public void markColor(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.COLOR));
    }

    public void markTexture(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.TEXTURE));
    }

    public void markFont(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.FONT));
    }

    public void markSound(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.SOUND));
    }

    public void markMusic(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.MUSIC));
    }

    public void markAlbum(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.ALBUM));
    }

    public void markDrawable(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.DRAWABLE));
    }

    public void markSpriteSheet(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.SPRITE_SHEET));
    }

    public void markAnimation(String resId, InputStream inputStream) {
        refs.push(new ResourceReference(inputStream, resId, Resource.Type.ANIMATION));
    }

    public void loadAll() {
        loadAll(new ProgressAction() {
            @Override
            public void loading(String resId) {
                Logger.info("loading resource \""+resId+"\"...");
            }

            @Override
            public void failed(String resId) {
                Logger.warn("failed to load resource \""+resId+"\"!");
            }

            @Override
            public void loaded(int total, int current, float percentage, String resId) {
                Logger.info(String.format("[%f%% - %d/%d] loaded resource \"%s\".", percentage, current, total, resId));
            }
        });
    }

    public void loadAll(ProgressAction progressAction) {
        for (int i = 0; i < refs.len(); i++) {
            ResourceReference ref = refs.get(i);
            if (ref.inputStream == null) {
                progressAction.failed(ref.id);
                continue;
            }
            progressAction.loading(ref.id);
            Resource.Type type = ref.type;
            try {
                Resource.create(type.clazz(), ref.inputStream, ref.id);
            } catch (Exception e) {
                progressAction.failed(ref.id);
                continue;
            }

            progressAction.loaded(refs.len(), i + 1, Utils.getPercent(i + 1, refs.len()), ref.id);
        }
    }

    private static class ResourceReference {
        private String id;
        private Resource.Type type;
        private InputStream inputStream;

        public ResourceReference(InputStream inputStream, String id, Resource.Type type) {
            this.id = id;
            this.type = type;
            this.inputStream = inputStream;
        }

        String getId() {
            return id;
        }

        Resource.Type getType() {
            return type;
        }

        InputStream getInputStream() {
            return inputStream;
        }
    }
}
