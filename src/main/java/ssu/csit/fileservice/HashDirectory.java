package ssu.csit.fileservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.codec.digest.DigestUtils;

import ssu.csit.fileservice.core.FileService;

public class HashDirectory {

    private File directory;

    public HashDirectory(File directory) {
        this.directory = directory;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public Vector<Vector<String>> getFileSystemChanges() throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException();
        }
        Vector<Vector<String>> changes = new Vector<Vector<String>>();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isHidden()) {
                continue;
            }
            if (file.isDirectory()) {
                changes.addAll(new HashDirectory(file).getFileSystemChanges());
            } else {
                String path = file.getPath();
                String hash = md5(file);
                FileService fileService = new FileService();

                String state = "No changes";
                String oldHash = fileService.getHashForFile(file);
                if (oldHash != null) {
                    if (!oldHash.equals(hash)) {
                        state = "Modified";
                        fileService.updateFile("hash", path, hash);
                    }
                } else {
                    String oldName = fileService.getFileForHash(hash);
                    if (oldName != null) {
                        state = String.format("Renamed from '%s'", oldName.substring(oldName.lastIndexOf("\\") + 1));
                        fileService.updateFile("path", path, hash);
                    } else {
                        state = "New file";
                        fileService.addFile(path, hash);
                    }
                }

                fileService.close();

                Vector<String> vector = new Vector<String>();
                vector.add(path);
                vector.add(state);
                changes.add(vector);
            }
        }
        return changes;
    }

    private static String md5(File file) throws IOException {
        FileInputStream input = null;
        byte[] data;
        try {
            data = new byte[100000];
            input = new FileInputStream(file);
            input.read(data);
        } finally {
            input.close();
        }
        return DigestUtils.md5Hex(data);
    }
}
