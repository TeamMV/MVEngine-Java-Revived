package dev.mv.engine.resources;

import dev.mv.engine.game.mod.loader.ModIntegration;
import dev.mv.engine.parsing.JSONParser;
import dev.mv.engine.parsing.Parser;
import dev.mv.engine.parsing.XMLParser;

import java.io.InputStream;

public class AssetManager {

    private String id;

    public AssetManager(String id) {
        this.id = id;
    }

    public void load() {
        InputStream bin = ModIntegration.getResourceAsStream("/assets/" + id + "/assets.b");
        InputStream json = ModIntegration.getResourceAsStream("/assets/" + id + "/assets.json");
        InputStream xml = ModIntegration.getResourceAsStream("/assets/" + id + "/assets.xml");
        if (bin != null) {
            loadBytecode(bin);
            return;
        }
        if (json != null) {
            load(new JSONParser(json));
            return;
        }
        if (xml != null) {
            load(new XMLParser(xml));
        }
    }

    private void loadBytecode(InputStream stream) {

    }

    private void load(Parser parser) {

    }
}