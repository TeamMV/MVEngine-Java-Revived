package dev.mv.engine.test;

import dev.mv.engine.ApplicationConfig;
import dev.mv.engine.MVEngine;
import dev.mv.engine.parsing.Parser;
import dev.mv.engine.parsing.XMLParser;
import dev.mv.engine.render.WindowCreateInfo;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.resources.types.SpriteSheet;
import dev.mv.engine.utils.misc.Version;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationConfig config = new ApplicationConfig();
        config
            .setName("FactoryIsland")
            .setVersion(Version.parse("v0.0.1"));
        try (MVEngine engine = MVEngine.init(config)) {
            WindowCreateInfo info = new WindowCreateInfo();
            info.resizeable = true;
            info.appendFpsToTitle = false;
            info.title = "FactoryIsland";
            info.vsync = true;
            info.maxFPS = 60;
            info.maxUPS = 30;
            info.decorated = true;
            info.width = 1000;
            info.height = 800;
            Window window = engine.createWindow(info);
            window.run(Test.INSTANCE);
        }
    }
}
