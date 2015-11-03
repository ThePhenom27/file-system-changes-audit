package ssu.csit.fileservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.commons.codec.digest.DigestUtils;

import ssu.csit.fileservice.core.FileService;
import ssu.csit.fileservice.core.HashedFile;

/**
 * Represents a file directory, provides methods for getting file changes in it.
 * 
 * @author Alexandra Fomina
 */
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

    /**
     * Returns file system changes in the directory.
     * 
     * @return the result table
     * @throws IOException if an I/O error occurs.
     */
    public Vector<Vector<String>> getFileSystemChanges() throws IOException {
        
        Map<String, String> currentDirState = hashFiles();
        FileService fileService = FileService.get();
        Vector<Vector<String>> changes = new Vector<Vector<String>>();
        List<HashedFile> files = fileService.listFiles(directory);
        if (files != null) {
            for (HashedFile file: files) {
                String state = "";
                String path = file.getPath();
                String hash = file.getHash();
                if (currentDirState.containsKey(path)) {
                    String newHash = currentDirState.get(path);
                    if (!newHash.equals(hash)) { //If hashes aren't equal file is modified
                        state = "Modified";
                        file.setHash(newHash);
                        fileService.update(file);
                    }
                    currentDirState.remove(path);
                    if (newHash.equals(hash)) {
                    	continue;
                    }
                } else { //Directory doesn't contains file - it's deleted
                    state = "Deleted";
                    fileService.delete(file);
                }
                
                Vector<String> vector = new Vector<String>();
                vector.add(path);
                vector.add(state);
                changes.add(vector);
            }
        }
        
        for (Map.Entry<String, String> entry: currentDirState.entrySet()) { //Remaining files in the directory are new files
            Vector<String> vector = new Vector<String>();
            String path = entry.getKey();
            vector.add(path);
            vector.add("New file");
            changes.add(vector);
            File file = new File(path);
            fileService.save(new HashedFile(relativePath(file), entry.getValue(), directory.getPath()));
        }
        
        return changes;
    }

    private Map<String, String> hashFiles() throws IOException {
    	return hashFiles(directory);
    }
    
    private Map<String, String> hashFiles(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("directory is empty");
            return Collections.emptyMap();
        }
        Map<String, String> hashes = new LinkedHashMap<String, String>();
        for (File file: files) {
            if (file.isHidden()) {
                continue;
            }
            if (file.isDirectory()) {
                hashes.putAll(hashFiles(file));
            } else {
                String md5 = md5(file);
                hashes.put(relativePath(file), md5 == null? "access denied" : md5);
            }
        }
        return hashes;
    }
    
    private static String md5(File file) throws IOException {
        FileInputStream input = null;
        byte[] data = new byte[0];
        try {
            data = new byte[100000];
            input = new FileInputStream(file);
            input.read(data);
        } catch (FileNotFoundException e) {
            System.out.println("error: " + e.getMessage());
            return null;
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return DigestUtils.md5Hex(data);
    }
    
    public String relativePath(File file) {
    	return file.getPath().replace(String.format("%s%s", directory.getPath(), "/"), "");
    }
}
