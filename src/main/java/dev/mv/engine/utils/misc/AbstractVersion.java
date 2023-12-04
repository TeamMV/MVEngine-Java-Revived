package dev.mv.engine.utils.misc;

import org.jetbrains.annotations.NotNull;

/**
 * AbstractVersion, parent of {@link Version} and {@link FinalVersion}.
 */
public abstract class AbstractVersion implements Comparable<AbstractVersion>, Cloneable {

    protected static int[] parseString(String version) {
        if (version == null) {
            return null;
        }
        if (version.startsWith("#version")) {
            version = version.replaceAll("#version", "").replaceAll(" ", "");
            char[] versionChars = version.toCharArray();
            if (versionChars.length == 1) {
                int major = versionChars[0] - 48;
                return new int[] {major, 0, 0};
            }
            else if (versionChars.length == 2) {
                int major = versionChars[0] - 48;
                int minor = versionChars[1] - 48;
                return new int[] {major, minor, 0};
            }
            else if (versionChars.length == 3) {
                int major = versionChars[0] - 48;
                int minor = versionChars[1] - 48;
                int patch = versionChars[2] - 48;
                return new int[] {major, minor, patch};
            }
            return null;
        }

        version = version.replaceAll("v", "").replaceAll(" ", "");
        String[] parts = version.split("\\.");
        try {
            if (parts.length == 1) {
                int major = Integer.parseInt(parts[0]);
                return new int[] {major, 0, 0};
            }
            else if (parts.length == 2) {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                return new int[] {major, minor, 0};
            }
            else if (parts.length == 3) {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                int patch = Integer.parseInt(parts[2]);
                return new int[] {major, minor, patch};
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static int[] parseVulkan(int version) {
        return new int[] {version >>> 22, (version >>> 12) & 0x3FF, version & 0xFFF};
    }

    /**
     * Stringifies the version in the style of "1.0.0".
     *
     * @return the version as a string.
     */
    public String toString() {
        return String.format("%d.%d.%d", getMajor(), getMinor(), getPatch());
    }

    /**
     * Stringifies the version in the style of "1.0.0", with the "." replaced by a custom separator.
     *
     * @param separator the custom separator.
     * @return the version as a string.
     */
    public String toString(String separator) {
        return String.format("%d%s%d%s%d", getMajor(), separator, getMinor(), separator, getPatch());
    }

    /**
     * Stringifies the version in the style of "v1.0.0", with the "." replaced by a custom separator,
     * and the "v" being replaced by a custom prefix.
     *
     * @param prefix the prefix to put before the version.
     * @param separator the custom separator.
     * @return the version as a string.
     */
    public String toString(String separator, String prefix) {
        return String.format("%s%d%s%d%s%d", prefix, getMajor(), separator, getMinor(), separator, getPatch());
    }

    /**
     * Stringifies the version in the style of "#version 100".
     *
     * @return the version as a string.
     */
    public String toGlslVersion() {
        return String.format("#version %d%d%d", getMajor(), getMinor(), getPatch());
    }

    /**
     * Converts the version to a single int, in the way Vulkan stores versions.
     *
     * @return the version as an int.
     */
    public int toVulkanVersion() {
        return getMajor() << 22 | getMinor() << 12 | getPatch();
    }

    /**
     * Compares this version to another version based on the major, minor, and patch components.
     * The comparison is performed in the following order of importance:
     * 1. Major: If the major components are different, the one with the higher major component is considered greater.
     * 2. Minor: If the major components are equal, but the minor components are different, the one with the higher minor component is considered greater.
     * 3. Patch: If both major and minor components are equal, but the patch components are different, the one with the higher patch component is considered greater.
     *
     * @param other The other version to compare with
     * @return A negative integer, zero, or a positive integer if this version is less than, equal to, or greater than the specified version, respectively.
     */
    @Override
    public int compareTo(@NotNull AbstractVersion other) {
        int cmp = getMajor() - other.getMajor();
        if (cmp != 0) return cmp;
        cmp = getMinor() - other.getMinor();
        if (cmp != 0) return cmp;
        cmp = getPatch() - other.getPatch();
        return cmp;
    }

    /**
     * Returns the latest of the two versions.
     * @param a version A.
     * @param b version B.
     * @return the latest of the two versions.
     */
    public static AbstractVersion latest(AbstractVersion a, AbstractVersion b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    /**
     * Returns the earliest of the two versions.
     * @param a version A.
     * @param b version B.
     * @return the earliest of the two versions.
     */
    public static AbstractVersion earliest(AbstractVersion a, AbstractVersion b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    @Override
    public int hashCode() {
        return toVulkanVersion();
    }

    /**
     * Checks if another version is equal to this version.
     * Note: this only takes into account the version itself, not whether it is an implementation of
     * {@link FinalVersion} or {@link Version}, comparing a {@link FinalVersion} to a {@link Version}
     * with the same major, minor and patch will return {@code true}.
     *
     * @param obj other version to compare this to.
     * @return {@code true} if the other version is equal to this version, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof AbstractVersion version) {
            return compareTo(version) == 0;
        }
        return false;
    }

    public abstract int getMajor();

    public abstract int getMinor();

    public abstract int getPatch();

    @Override
    public abstract AbstractVersion clone();
    
}
