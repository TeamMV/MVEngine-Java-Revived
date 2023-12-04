package dev.mv.engine.render.utils;

public class RenderConstants {
    public static final float FONT_SMOOTHING = 1f / 64f;
    public static final String DUMMY_VERT = "#version 450\nlayout(location=0)out vec2 a;vec2 p[4]=vec2[](vec2(-1.0,-1.0),vec2(-1.0,1.0),vec2(1.0,-1.0),vec2(1.0,1.0));vec2 t[4]=vec2[](vec2(0.0,1.0),vec2(0.0,0.0),vec2(1.0,1.0),vec2(1.0,0.0));void main(){a=t[gl_VertexIndex];gl_Position=vec4(p[gl_VertexIndex],0.0,1.0);}";
}
