package dev.mv.engine.utils.misc;

/**
 * FinalVersion class, allows to create, parse, compare and stringify versions.
 * Unlike {@link Version}, the values in this class cannot be modified after creation.
 *
 * @author Maxim Savenkov
 */
public class FinalVersion extends AbstractVersion {

    private final int major, minor, patch;

    /**
     * Create a 1.0.0 version.
     */
    public FinalVersion() {
        this(1, 0, 0);
    }

    /**
     * Create a version with a major, and 0.0 as the rest.
     *
     * @param major the major int.
     */
    public FinalVersion(int major) {
        this(major, 0, 0);
    }

    /**
     * Create a version with a major and a minor, patch is set to 0.
     *
     * @param major the major int.
     * @param minor the minor int.
     */
    public FinalVersion(int major, int minor) {
        this(major, minor, 0);
    }

    /**
     * Create a full version.
     *
     * @param major the major int.
     * @param minor the minor int.
     * @param patch the patch int.
     */
    public FinalVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Parse a version string. Can parse versions in the GLSL format "#version 100", or version written as "v1.0.0" or "1.0.0".
     *
     * @param version the version strings.
     * @return the parsed string as a {@link FinalVersion}.
     */
    public static FinalVersion parse(String version) {
        int[] parsed = parseString(version);
        return parsed == null ? null : new FinalVersion(parsed[0], parsed[1], parsed[2]);
    }

    /**
     * Parses a vulkan version (single int) into a {@link FinalVersion}.
     * @param version the vulkan version.
     * @return the {@link FinalVersion}.
     */
    public static FinalVersion parse(int version) {
        int[] parsed = parseVulkan(version);
        return new FinalVersion(parsed[0], parsed[1], parsed[2]);
    }

    @Override
    public int getMajor() {
        return major;
    }

    @Override
    public int getMinor() {
        return minor;
    }

    @Override
    public int getPatch() {
        return patch;
    }

    @Override
    public FinalVersion clone() {
        return new FinalVersion(major, minor, patch);
    }

}
