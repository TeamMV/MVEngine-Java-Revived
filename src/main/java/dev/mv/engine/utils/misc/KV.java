package dev.mv.engine.utils.misc;

public class KV {
    private String k, v;

    public KV(String src) {
        k = src.split(" *= *")[0].trim();
        v = src.split(" *= *")[1].trim();
    }

    public String k() {return k;}
    public String v() {return v;}
    public void k(String k) {this.k = k;}
    public void v(String v) {this.v = v;}
}
