package dev.mv.engine.resources;

import dev.mv.engine.Env;
import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.utils.ArrayUtils;
import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.collection.Vec;

public class ResourceLoader {
    private static final Vec<ResourceReference> refs = new Vec<>();

    public void markResource(String resId, String classpath, ResourcePath... paths) {
        refs.push(new ResourceReference(resId, Resource.Type.RESOURCE, ArrayUtils.merge(new ResourcePath[] {ResourcePath.blank(classpath)}, paths)));
    }

    public void markTexture(String resId, ResourcePath path) {
        refs.push(new ResourceReference(resId, Resource.Type.TEXTURE, path));
    }

    public void markFont(String resId, ResourcePath png, ResourcePath fnt) {
        refs.push(new ResourceReference(resId, Resource.Type.FONT, png, fnt));
    }

    public void markSound(String resId, ResourcePath path) {
        refs.push(new ResourceReference(resId, Resource.Type.SOUND, path));
    }

    public void markMusic(String resId, ResourcePath path) {
        refs.push(new ResourceReference(resId, Resource.Type.MUSIC, path));
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
            //if (ref.inputStream == null) {
            //    progressAction.failed(ref.id);
            //    continue;
            //}
            progressAction.loading(ref.id);
            try {
                Resource.create(ref.type, ref.id, ref.paths);
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
        private ResourcePath[] paths;

        public ResourceReference(String id, Resource.Type type, ResourcePath... paths) {
            this.id = id;
            this.type = type;
            this.paths = paths;
        }

        String getId() {
            return id;
        }

        Resource.Type getType() {
            return type;
        }

        ResourcePath[] path() {
            return paths;
        }
    }
}
