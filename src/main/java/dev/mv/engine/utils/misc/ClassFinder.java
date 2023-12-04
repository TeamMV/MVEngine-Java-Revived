package dev.mv.engine.utils.misc;

import dev.mv.engine.utils.Utils;
import dev.mv.engine.utils.collection.FastIter;
import dev.mv.engine.utils.collection.Vec;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ClassFinder {

    private static FastIter<String> foundClasses = null;


    public static FastIter<String> findAllClasses() {
        if (foundClasses != null) return foundClasses;
        try {
            Set<File> files = new HashSet<>();
            Set<String> classes = new HashSet<>();
            for (String library : System.getProperty("java.class.path").split(System.getProperty("path.separator"))) {
                URL url;
                try {
                    url = new File(library).toURI().toURL();
                } catch (SecurityException e) {
                    url = new URL("file", null, new File(library).getAbsolutePath());
                }
                if (url.getProtocol().equals("file")) {
                    File file = toFile(url);
                    files.add(file);
                }
            }
            findClasses(files, classes);
            foundClasses = new Vec<>(classes).fastIter()
                .filter(name -> !Utils.containsAny(name, "module-info", "package-info", "META-INF"));
            return foundClasses;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void findClasses(Set<File> files, Set<String> classes) throws Exception {
        for (File file : files) {
            scanFile(files, classes, file, file.isDirectory());
        }
    }

    private static void scanFile(Set<File> files, Set<String> classes, File file, boolean directory) throws Exception {
        try {
            if (!file.exists()) return;
        } catch (SecurityException e) {
            return;
        }
        if (directory) {
            Set<File> sFiles = new HashSet<>();
            sFiles.add(file);
            findDirClasses(file, "", sFiles, classes);
        } else {
            findJarClasses(file, files, classes);
        }
    }

    private static void findJarClasses(File file, Set<File> files, Set<String> classes) throws Exception {
        JarFile jar;
        try {
            jar = new JarFile(file);
        } catch (IOException e) {
            return;
        }
        try {
            Manifest manifest = jar.getManifest();
            if (manifest != null) {
                String classPath = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
                if (classPath != null) {
                    for (String path : classPath.split(";")) {
                        try {
                            URL url = new URL(file.toURI().toURL(), path);
                            if (url.getProtocol().equals("file")) {
                                File extraFile = toFile(url);
                                if (!files.contains(extraFile)) {
                                    files.add(extraFile);
                                    scanFile(files, classes, extraFile, file.isDirectory());
                                }
                            }
                        } catch (MalformedURLException ignore) {
                        }
                    }
                }
            }
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory() || entry.getName().equals(JarFile.MANIFEST_NAME) || entry.getName().contains("META_INF"))
                    continue;
                if (!entry.getName().contains(".class")) continue;
                classes.add(toClassName(entry.getName()));
            }
        } finally {
            try {
                jar.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static void findDirClasses(File directory, String packagePrefix, Set<File> scannedPath, Set<String> classes) throws Exception {
        File[] files = directory.listFiles();
        if (files == null) return;
        for (File f : files) {
            String name = f.getName();
            if (f.isDirectory()) {
                File deref = f.getCanonicalFile();
                if (scannedPath.add(deref)) {
                    findDirClasses(deref, packagePrefix + name + "/", scannedPath, classes);
                    scannedPath.remove(deref);
                }
            } else {
                String resourceName = packagePrefix + name;
                if (!resourceName.equals(JarFile.MANIFEST_NAME) && resourceName.endsWith(".class")) {
                    classes.add(toClassName(resourceName));
                }
            }
        }
    }

    private static File toFile(URL url) {
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            return new File(url.getPath());
        }
    }

    private static String toClassName(String filename) {
        return filename.substring(0, filename.length() - ".class".length()).replace('/', '.');
    }

}
