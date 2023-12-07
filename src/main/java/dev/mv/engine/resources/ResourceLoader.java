package dev.mv.engine.resources;

import dev.mv.engine.Env;
import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.utils.ArrayUtils;
import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.collection.Vec;

public class ResourceLoader {
    private static final Vec<ResourceReference> refs = new Vec<>();

    private String prefix;

    public ResourceLoader(String prefix) {
        this.prefix = prefix;
    }

    private String map(String id) {
        return prefix + "." + id;
    }

    private void map(ResourcePath path) {
        path.path = "/assets/" + prefix + path.path;
    }

    public void markResource(String resId, String classpath, ResourcePath... paths) {
        resId = map(resId);
        for (ResourcePath path : paths) {
            map(path);
        }
        if (paths.length > 0) {
            paths[0].setResId(resId);
        }
        refs.push(new ResourceReference(resId, Resource.Type.RESOURCE, ArrayUtils.merge(new ResourcePath[] {ResourcePath.blank(classpath)}, paths)));
    }

    public void markTexture(String resId, ResourcePath path) {
        resId = map(resId);
        map(path);
        refs.push(new ResourceReference(resId, Resource.Type.TEXTURE, path.setResId(resId)));
    }

    public void markFont(String resId, ResourcePath... paths) {
        resId = map(resId);
        for (ResourcePath path : paths) {
            map(path);
        }
        if (paths.length > 0) {
            paths[0].setResId(resId);
        }
        refs.push(new ResourceReference(resId, Resource.Type.FONT, paths));
    }

    public void markSound(String resId, ResourcePath path) {
        resId = map(resId);
        map(path);
        refs.push(new ResourceReference(resId, Resource.Type.SOUND, path.setResId(resId)));
    }

    public void markMusic(String resId, ResourcePath path) {

        map(path);
        refs.push(new ResourceReference(resId, Resource.Type.MUSIC, path.setResId(resId)));
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
            if (ref.paths[i] == null) {
                progressAction.failed(ref.id);
                continue;
            }
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
