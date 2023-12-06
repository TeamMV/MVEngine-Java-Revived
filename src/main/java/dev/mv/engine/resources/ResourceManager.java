package dev.mv.engine.resources;

import dev.mv.engine.utils.collection.Vec;

public class ResourceManager {
    private ResourceBundle primaryBundle = new ResourceBundle();
    private ResourceBundle secondaryBundle = new ResourceBundle();

    public ResourceManager() {}

    public void setPrimaryBundle(ResourceBundle bundle) {
        process(bundle, primaryBundle, secondaryBundle);
        primaryBundle = bundle;
    }

    public void setSecondaryBundle(ResourceBundle bundle) {
        process(bundle, secondaryBundle, primaryBundle);
        secondaryBundle = bundle;
    }

    private void process(ResourceBundle load, ResourceBundle unload, ResourceBundle keep) {
        processChange(R.resource, load.resources, unload.resources, keep.resources);
        processChange(R.texture, load.textures, unload.textures, keep.textures);
        processChange(R.font, load.fonts, unload.fonts, keep.fonts);
        processChange(R.sound, load.sounds, unload.sounds, keep.sounds);
        processChange(R.music, load.music, unload.music, keep.music);
    }

    private void processChange(R.Res<? extends Resource> res, Vec<String> load, Vec<String> unload, Vec<String> keep) {
        for (String id : unload) {
            if (keep.contains(id) || load.contains(id)) continue;
            Resource r = res.get(id);
            if (r.isLoaded()) r.drop();
        }

        for (String id : load) {
            Resource r = res.get(id);
            if (!r.isLoaded()) r.load();
        }
    }

}
