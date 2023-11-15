package dev.mv.engine.resources;

public abstract class CustomResource implements Resource{
    protected String resId;

    public CustomResource() {}

    @Override
    public Type type() {
        return Type.RESOURCE;
    }

    @Override
    public String resId() {
        return resId;
    }
}
