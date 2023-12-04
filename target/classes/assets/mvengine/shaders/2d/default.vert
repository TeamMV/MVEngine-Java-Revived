#version 450

precision highp float;

layout (location = 0) in vec3 aVertPos;
layout (location = 1) in float aRotation;
layout (location = 2) in vec2 aRotationOrigin;
layout (location = 3) in vec4 aColor;
layout (location = 4) in vec2 aTexCoords;
layout (location = 5) in float aTexID;
layout (location = 6) in float aUseCam;
layout (location = 7) in float aTransRot;
layout (location = 8) in vec2 aTransTrans;
layout (location = 9) in vec2 aTransOrigin;
layout (location = 10) in float aIsFont;
layout (location = 0) out vec4 fColor;
layout (location = 1) out vec2 fTexCoords;
layout (location = 2) out float fTexID;
layout (location = 3) out vec2 fRes;
layout (location = 4) out float fIsFont;

uniform mat4 uProjection;
uniform mat4 uView;

uniform float uResX;
uniform float uResY;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexID = aTexID;
    fRes = vec2(uResX, uResY);
    fIsFont = aIsFont;

    vec2 pos = aVertPos.xy;

    if (aRotation != 0) {
        mat2 rot;
        rot[0] = vec2(cos(aRotation), - sin(aRotation));
        rot[1] = vec2(sin(aRotation), cos(aRotation));
        pos -= aRotationOrigin;
        pos = rot * pos;
        pos += aRotationOrigin;
    }

    if (aTransRot != 0) {
        mat2 rot;
        rot[0] = vec2(cos(aTransRot), - sin(aTransRot));
        rot[1] = vec2(sin(aTransRot), cos(aTransRot));
        pos -= aTransOrigin;
        pos = rot * pos;
        pos += aTransOrigin;
    }

    pos = pos + aTransTrans;

    if (aUseCam == 1) {
        gl_Position = uProjection * uView * vec4(pos, aVertPos.z, 1.0);
    } else {
        gl_Position = uProjection * vec4(pos, aVertPos.z, 1.0);
    }
}