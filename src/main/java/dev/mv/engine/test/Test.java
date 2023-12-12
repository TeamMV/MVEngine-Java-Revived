package dev.mv.engine.test;

import dev.mv.engine.ApplicationLoop;
import dev.mv.engine.MVEngine;
import dev.mv.engine.files.Directory;
import dev.mv.engine.files.FileManager;
import dev.mv.engine.gui.Gui;
import dev.mv.engine.gui.elements.GuiButton;
import dev.mv.engine.gui.elements.GuiElement;
import dev.mv.engine.gui.elements.GuiTextLine;
import dev.mv.engine.gui.events.listeners.GuiClickListener;
import dev.mv.engine.gui.events.listeners.GuiEnterListener;
import dev.mv.engine.gui.style.BorderStyle;
import dev.mv.engine.gui.style.Cursor;
import dev.mv.engine.gui.style.Positioning;
import dev.mv.engine.gui.style.value.GuiValueJust;
import dev.mv.engine.gui.style.value.GuiValueMeasurement;
import dev.mv.engine.input.Input;
import dev.mv.engine.input.processors.GuiInputProcessor;
import dev.mv.engine.logic.unit.Unit;
import dev.mv.engine.render.shared.graphics.Scale;
import dev.mv.engine.render.shared.*;
import dev.mv.engine.render.shared.texture.TextureRegion;
import dev.mv.engine.resources.ProgressAction;
import dev.mv.engine.resources.types.animation.FrameAnimation;
import dev.mv.engine.resources.types.drawable.Drawable;
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
    private Gui gui;

    private Test() {
    }

    @Override
    public void start(MVEngine engine, Window window) {
        engine.loadResources(ProgressAction.quiet());
        engine.createResources();
        gameDirectory = FileManager.getDirectory("factoryisland");
        ctx = new DrawContext(window);
        camera = window.getCamera();
        camera.setSpeed(0.2f);
        cameraController = new DefaultCameraController(camera);

        GuiTextLine textLine = new GuiTextLine();
        textLine.setText("Hello, World!");
        textLine.style.text.size = new GuiValueMeasurement<>(5, Unit.CM);
        textLine.style.text.color = new GuiValueJust<>(Color.WHITE);
        textLine.style.border.style = new GuiValueJust<>(BorderStyle.SQUARE);
        textLine.style.border.radius = new GuiValueJust<>(new Scale(100, 75));
        textLine.style.border.width = new GuiValueMeasurement<>(0.3f, Unit.CM);
        textLine.style.border.color = new GuiValueJust<>(Color.WHITE);
        textLine.style.backgroundColor = new GuiValueJust<>(Color.parse("#00b3ff"));
        textLine.style.padding.setInts(60);
        //textLine.style.margin.setInts(40);

        textLine.moveTo(120, 120);
        guiElement = textLine;

        textLine.pseudo.hover.backgroundColor = new GuiValueJust<>(Color.RED);

        textLine.event.click((caller, btn, x, y) -> {
            if (btn == Input.MOUSE_LEFT) {
                System.out.println("click");
            }
        });

        textLine.event.release((caller, btn, x, y) -> {
            if (btn == Input.MOUSE_LEFT) {
                System.out.println("release");
            }
        });

        textLine.setTransitionDuration(1000);

        GuiTextLine textLine2 = new GuiTextLine();
        textLine2.setText("Hello 2");
        textLine2.style.x = new GuiValueJust<>(100);
        textLine2.style.y = new GuiValueJust<>(100);
        textLine2.style.positioning = new GuiValueJust<>(Positioning.ABSOLUTE);
        textLine2.style.text.size = new GuiValueMeasurement<>(3, Unit.CM);

        textLine2.pseudo.hover.backgroundColor = new GuiValueJust<>(Color.RED);

        GuiTextLine textLine3 = new GuiTextLine();
        textLine3.setText("Hello 3");
        textLine3.style.x = new GuiValueJust<>(150);
        textLine3.style.y = new GuiValueJust<>(150);
        textLine3.style.positioning = new GuiValueJust<>(Positioning.ABSOLUTE);
        textLine3.style.text.size = new GuiValueMeasurement<>(3, Unit.CM);

        textLine3.pseudo.hover.backgroundColor = new GuiValueJust<>(Color.RED);

        GuiButton button = new GuiButton();
        button.style.border.radius = new GuiValueJust<>(Scale.equal(20));
        button.style.text.size = new GuiValueMeasurement<>(2, Unit.CM);

        button.pseudo.hover.backgroundColor = new GuiValueJust<>(Color.GREEN);
        button.pseudo.hover.cursor = new GuiValueJust<>(Cursor.HAND);

        button.moveTo(250, 150);
        button.setText("Click me");
        button.onclick((caller, btn, x, y) -> {
            System.out.println("button click");
        });

        button.event.enter();

        //textLine.setZIndex(5);

        gui = new Gui();
        gui.addElement(textLine);
        gui.addElement(textLine2);
        gui.addElement(textLine3);
        gui.addElement(button);

        GuiInputProcessor.addGui(gui);

        //engine.getAudio().getDJ().loop("bestSong");
    }

    @Override
    public void update(MVEngine engine, Window window) {

    }

    @Override
    public void draw(MVEngine engine, Window window) {
        ctx.background(69, 69, 69);
        rot += 0.1f;

        gui.draw(ctx);
        //guiElement.drawDebug(ctx);
    }

    @Override
    public void exit(MVEngine engine, Window window) {
        Logger.info("Exiting application \"" + engine.getApplicationConfig().getName() + "\"");
    }
}