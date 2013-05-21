package ssu.csit.fileservice.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;
	@Column(name = "PATH", nullable = false, length = 256)
    private String path;
	@Column(name = "HASH", nullable = false, length = 128)
    private String hash = "";
	@Column(name = "DIRECTORY", nullable = false, length = 256)
    private String directory;

    public HashedFile() {}
    
    public HashedFile(String path, String hash, String directory) {
        this.path = path;
        this.hash = hash;
        this.directory = directory;
    }

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

    @Override
    public boolean equals(Object obj) {
    	if (this == obj) {
    		return true;
    	}
    	if (obj instanceof HashedFile) {
    		HashedFile other = (HashedFile) obj;
    		return this.path.equals(other.path);
    	}
    	return false;
    }
    
    @Override
    public int hashCode() {
    	return hash.hashCode();
    }
}
