package dev.mv.engine.gui.elements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuiParsable {
    String tagName();
    Attrib[] attribs() default @Attrib(name = "", field = "");
}
