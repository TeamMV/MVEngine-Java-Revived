package dev.mv.engine.test;

import dev.mv.engine.ApplicationLoop;
import dev.mv.engine.MVEngine;
import dev.mv.engine.audio.Music;
import dev.mv.engine.files.Directory;
import dev.mv.engine.files.FileManager;
import dev.mv.engine.input.Input;
import dev.mv.engine.parsing.XMLParser;
import dev.mv.engine.render.shared.*;
import dev.mv.engine.render.shared.create.RenderBuilder;
import dev.mv.engine.render.shared.graphics.CircularParticleSystem;
import dev.mv.engine.render.shared.texture.Texture;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.engine.resources.ProgressAction;
import dev.mv.engine.resources.R;
import dev.mv.engine.resources.ResourceLoader;
import dev.mv.engine.resources.types.SpriteSheet;
import dev.mv.engine.resources.types.animation.Animation;
import dev.mv.engine.resources.types.animation.FrameAnimation;
import dev.mv.engine.resources.types.border.CircleBorder;
import dev.mv.engine.resources.types.border.RoundRectangleBorder;
import dev.mv.engine.resources.types.custom.CustomResource;
import dev.mv.engine.resources.types.custom.TestCustomResource;
import dev.mv.engine.resources.types.drawable.Drawable;
import dev.mv.engine.resources.types.drawable.StaticDrawable;
import dev.mv.utils.Utils;
import dev.mv.utils.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class Test implements ApplicationLoop {

    public static final Test INSTANCE = new Test();
    private DrawContext ctx;
    private Camera camera;
    private DefaultCameraController cameraController;
    private Directory gameDirectory;

    private FrameAnimation animation;
    private TextureRegion region;

    private Drawable drawable;

    private float rot = 0;

    private Test() {
    }

    @Override
    public ProgressAction preload(MVEngine engine, Window window) {
        ResourceLoader loader = engine.getResourceLoader();
        loader.markColor("myColor", new ByteArrayInputStream("255,255,0,255".getBytes(StandardCharsets.UTF_8)));
        loader.markTexture("inflatableGuy", Test.class.getResourceAsStream("/assets/mvengine/textures/inflatableGuy.png"));

        InputStream resStream = new ByteArrayInputStream("Hello World".getBytes(StandardCharsets.UTF_8));
        loader.markResource("myCustomRes", CustomResource.resourceStream(resStream, TestCustomResource.class));
        return ProgressAction.simple();
    }

    @Override
    public void start(MVEngine engine, Window window) {
        gameDirectory = FileManager.getDirectory("factoryisland");
        ctx = new DrawContext(window);
        camera = window.getCamera();
        camera.setSpeed(0.2f);
        cameraController = new DefaultCameraController(camera);

        TestCustomResource tcr = R.resource.get("myCustomRes");
        Logger.info(tcr.string);

        //engine.getAudio().getDJ().loop("bestSong");
    }

    @Override
    public void update(MVEngine engine, Window window) {

    }

    @Override
    public void draw(MVEngine engine, Window window) {
        ctx.color(R.color.get("myColor"));
        ctx.rectangle(100, 100, 100, 100);
        ctx.color(R.color.get("transparent"));
        ctx.image(200, 100, 100, 100, R.texture.<TextureRegion>get("inflatableGuy"));
        //rot += 0.05f;
    }

    @Override
    public void exit(MVEngine engine, Window window) {

    }
}