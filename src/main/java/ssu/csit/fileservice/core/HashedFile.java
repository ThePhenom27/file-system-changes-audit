package ssu.csit.fileservice.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a file with hash.
 * 
 * @author Alexandra Fomina
 */
@Entity
@Table(name = "HASHES")
public class HashedFile {

    private Integer id;
    private String path;
    private String hash = "";
    private String directory;

    public HashedFile() {}
    
    public HashedFile(String path, String hash, String directory) {
        this.path = path;
        this.hash = hash;
        this.directory = directory;
    }

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }
 
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

}
