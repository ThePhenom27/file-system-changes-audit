package ssu.csit.fileservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.commons.codec.digest.DigestUtils;

import ssu.csit.fileservice.core.FileService;
import ssu.csit.fileservice.core.HashedFile;
import ssu.csit.gui.TableFrame;

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
    public void getFileSystemChanges(TableFrame frame) throws IOException {
        
        Map<String, String> currentDirState = hashFiles();
        FileService fileService = FileService.get();
//        List<HashedFile> toStore = new ArrayList<HashedFile>();
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
//                        fileService.update(file);
                    }

                    if (newHash.equals(hash)) {
                        if ("access denied".equals(hash)) {
                            state = "access denied";
                        } else {
                            currentDirState.remove(path);
                            continue;
                           // state = file.getState();
                        }
                    }

                    currentDirState.remove(path);
                } else { //Directory doesn't contains file - it's deleted
                    state = "Deleted";
                    //fileService.delete(file);
                }
                
                frame.addRow(path, state);
//                System.out.println(path + "     " + state);

                file.setState(state);
//                fileService.update(file);
            }
        }
        
        for (Map.Entry<String, String> entry: currentDirState.entrySet()) { //Remaining files in the directory are new files
            String path = entry.getKey();
            String state = currentDirState.get(path).equals("access denied")? "access denied": "New file";
//            System.out.println(path + "     " + state);
            frame.addRow(path, state);
//            File file = new File(path);
//            toStore.add(new HashedFile(relativePath(file), entry.getValue(), directory.getPath()));
        }

//        fileService.save(toStore);
        
    }

    public void markFilesNew(TableFrame frame) throws IOException {

        Map<String, String> currentDirState = hashFiles();
        FileService fileService = FileService.get();
        List<HashedFile> toStore = new ArrayList<HashedFile>();

        for (Map.Entry<String, String> entry: currentDirState.entrySet()) {
            String path = entry.getKey();
            String state = currentDirState.get(path).equals("access denied")? "access denied": "New file";
            System.out.println(path + "     " + state);
            if (!"New file".equals(state)) {
                frame.addRow(path, state);
            }
            File file = new File(path);
            toStore.add(new HashedFile(relativePath(file), entry.getValue(), directory.getPath(), "New file"));
        }

        fileService.save(toStore);

    }

    private Map<String, String> hashFiles() throws IOException {
    	return hashFiles(directory);
    }
    
    private Map<String, String> hashFiles(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files == null) {
//            System.out.println("directory is empty");
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
//            System.out.println("error: " + e.getMessage());
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
