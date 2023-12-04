package dev.mv.engine.utils.misc;

/**
 * Version class, allows to create, parse, compare and stringify versions.
 * Unlike {@link FinalVersion}, the values in this class can be modified after creation.
 *
 * @author Maxim Savenkov
 */
public class Version extends AbstractVersion {

    private int major, minor, patch;

    /**
     * Create a 1.0.0 version.
     */
    public Version() {
        this(1, 0, 0);
    }

    /**
     * Create a version with a major, and 0.0 as the rest.
     *
     * @param major the major int.
     */
    public Version(int major) {
        this(major, 0, 0);
    }

    /**
     * Create a version with a major and a minor, patch is set to 0.
     *
     * @param major the major int.
     * @param minor the minor int.
     */
    public Version(int major, int minor) {
        this(major, minor, 0);
    }

    /**
     * Create a full version.
     *
     * @param major the major int.
     * @param minor the minor int.
     * @param patch the patch int.
     */
    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Parse a version string. Can parse versions in the GLSL format "#version 100", or version written as "v1.0.0" or "1.0.0".
     *
     * @param version the version strings.
     * @return the parsed string as a {@link Version}.
     */
    public static Version parse(String version) {
        int[] parsed = parseString(version);
        return parsed == null ? null : new Version(parsed[0], parsed[1], parsed[2]);
    }

    /**
     * Parses a vulkan version (single int) into a {@link Version}.
     * @param version the vulkan version.
     * @return the {@link Version}.
     */
    public static Version parse(int version) {
        int[] parsed = parseVulkan(version);
        return new Version(parsed[0], parsed[1], parsed[2]);
    }

    @Override
    public int getMajor() {
        return major;
    }

    public Version setMajor(int major) {
        this.major = major;
        return this;
    }

    @Override
    public int getMinor() {
        return minor;
    }

    public Version setMinor(int minor) {
        this.minor = minor;
        return this;
    }

    @Override
    public int getPatch() {
        return patch;
    }

    public Version setPatch(int patch) {
        this.patch = patch;
        return this;
    }

    @Override
    public Version clone() {
        return new Version(major, minor, patch);
    }
}
