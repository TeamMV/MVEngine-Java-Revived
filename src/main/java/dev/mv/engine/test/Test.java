package dev.mv.engine.test;

import dev.mv.engine.ApplicationLoop;
import dev.mv.engine.MVEngine;
import dev.mv.engine.audio.Sound;
import dev.mv.engine.files.Directory;
import dev.mv.engine.files.FileManager;
import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.gui.elements.GuiTextLine;
import dev.mv.engine.gui.style.BorderStyle;
import dev.mv.engine.gui.style.value.GuiValueJust;
import dev.mv.engine.gui.style.value.GuiValueMeasurement;
import dev.mv.engine.logic.unit.Unit;
import dev.mv.engine.render.shared.graphics.Scale;
import dev.mv.engine.render.shared.*;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.engine.resources.ProgressAction;
import dev.mv.engine.resources.R;
import dev.mv.engine.resources.ResourceLoader;
import dev.mv.engine.resources.ResourceManager;
import dev.mv.engine.resources.types.animation.FrameAnimation;
import dev.mv.engine.resources.types.drawable.Drawable;
import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.logger.Logger;

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

    private GuiElement guiElement;

    private Test() {
    }

    @Override
    public void start(MVEngine engine, Window window) {
        engine.loadResources(ProgressAction.quiet());
        gameDirectory = FileManager.getDirectory("factoryisland");
        ctx = new DrawContext(window);
        camera = window.getCamera();
        camera.setSpeed(0.2f);
        cameraController = new DefaultCameraController(camera);

        GuiTextLine textLine = new GuiTextLine();
        textLine.setText("Hello, World!");
        textLine.style.text.size = new GuiValueMeasurement<>(5, Unit.CM);
        textLine.style.text.color = new GuiValueJust<>(Color.WHITE);
        textLine.style.border.style = new GuiValueJust<>(BorderStyle.ROUND);
        textLine.style.border.radius = new GuiValueJust<>(new Scale(100, 75));
        textLine.style.border.width = new GuiValueMeasurement<>(0.3f, Unit.CM);
        textLine.style.border.color = new GuiValueJust<>(Color.WHITE);
        textLine.style.backgroundColor = new GuiValueJust<>(Color.parse("#00b3ff"));
        textLine.style.padding.setInts(40);

        textLine.moveTo(100, 100);
        guiElement = textLine;

        //engine.getAudio().getDJ().loop("bestSong");
    }

    @Override
    public void update(MVEngine engine, Window window) {

    }

    @Override
    public void draw(MVEngine engine, Window window) {
        ctx.background(69, 69, 69);
        ctx.color(Color.WHITE);
        ctx.font(R.font.get("mvengine.default"));
        ctx.text(false, 100, 100, 16, "Hello");

        ctx.beginClip();
        ctx.color(Color.RED);
        ctx.circle(150, 150, 100, 50);
        ctx.endClip();
        ctx.color(Color.BLUE);
        ctx.rectangle(0, 100, 300, 50);
        ctx.clearClip();

        rot += 0.1f;

        //guiElement.draw(ctx);
        //rot += 0.05f;
    }

    @Override
    public void exit(MVEngine engine, Window window) {
        Logger.info("Exiting application \"" + engine.getApplicationConfig().getName() + "\"");
    }
}