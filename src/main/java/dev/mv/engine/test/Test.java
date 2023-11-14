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
import dev.mv.engine.resources.R;
import dev.mv.engine.resources.types.SpriteSheet;
import dev.mv.engine.resources.types.animation.Animation;
import dev.mv.engine.resources.types.animation.FrameAnimation;
import dev.mv.engine.resources.types.border.CircleBorder;
import dev.mv.engine.resources.types.border.RoundRectangleBorder;
import dev.mv.engine.resources.types.drawable.Drawable;
import dev.mv.engine.resources.types.drawable.StaticDrawable;
import dev.mv.utils.Utils;

import java.io.IOException;

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
    public void start(MVEngine engine, Window window) {
        gameDirectory = FileManager.getDirectory("factoryisland");
        ctx = new DrawContext(window);
        camera = window.getCamera();
        camera.setSpeed(0.2f);
        cameraController = new DefaultCameraController(camera);
        Music music = engine.getAudio().newMusic("/assets/mvengine/sound/11.wav");
        R.music.register("bestSong", music);

        try {
            Texture sheetTex = RenderBuilder.newTexture(Test.class.getResourceAsStream("/anim-test.png"));
            SpriteSheet sheet = new SpriteSheet(sheetTex, Test.class.getResourceAsStream("/anim-test.xml"));
            animation = FrameAnimation.fromSprites(sheet.getCollection("index"));
            animation.setDuration(3);
            animation.computeDelay();
            animation.setInfinite(false);

            region = sheetTex.cutRegion(0, 0, 8, 8);

            drawable = new StaticDrawable(0, 0).parse(new XMLParser(Test.class.getResourceAsStream("/drawable-test.xml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //engine.getAudio().getDJ().loop("bestSong");
    }

    @Override
    public void update(MVEngine engine, Window window) {

    }

    @Override
    public void draw(MVEngine engine, Window window) {
        ctx.color(Color.BLUE);

        drawable.draw(ctx, 10, 10, window.getWidth() - 20, window.getHeight() - 20, rot, window.getWidth() / 2, window.getHeight() / 2);

        //rot += 0.05f;
    }

    @Override
    public void exit(MVEngine engine, Window window) {

    }
}