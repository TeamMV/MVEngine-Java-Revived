package dev.mv.engine.resources;

import dev.mv.engine.Env;
import dev.mv.engine.exceptions.Exceptions;
import dev.mv.utils.Utils;
import dev.mv.utils.collection.Vec;

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
        loadAll(ProgressAction.simple());
    }

    public void loadAll(ProgressAction progressAction) {
        if (!Env.isResourceReady()) {
            Exceptions.send(new IllegalStateException("ResourceLoader not ready yet!"));
            return;
        }

        if (progressAction == null) {
            progressAction = ProgressAction.quiet();
        }
        for (int i = 0; i < refs.len(); i++) {
            ResourceReference ref = refs.get(i);
            if (ref.inputStream == null) {
                progressAction.failed(ref.id);
                continue;
            }
            progressAction.loading(ref.id);
            Resource.Type type = ref.type;
            try {
                Resource.create(type, ref.inputStream, ref.id);
            } catch (Exception e) {
                Exceptions.send(e);
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
