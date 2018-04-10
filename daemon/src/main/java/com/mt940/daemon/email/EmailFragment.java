package com.mt940.daemon.email;

import lombok.Data;
import org.springframework.util.Assert;

import java.io.File;

@Data
public class EmailFragment {

    /**
     * The data to save to the file system, e.g. text messages/attachments, binary
     * file attachments etc.
     */
    private Object data;
    /**
     * The file name to create for the respective {@link EmailFragment}.
     */
    private String filename;
    /**
     * The unqique file name in format yyyyddMM-UUID-originalFilename
     */
    private String uniqueFileName;
    /**
     * The file name to create for the respective {@link EmailFragment}.
     */
    private int size;
    /**
     * The directory where to store the {@link #getData()} using the specified
     * {@link #getFilename()}.
     */
    private File directory;

    public EmailFragment(Object data, String filename, String uniqueFileName, int size, File directory) {
        this.data = data;
        this.filename = filename;
        this.uniqueFileName = uniqueFileName;
        this.size = size;
        this.directory = directory;
    }

    public EmailFragment(File directory, String filename, Object data) {
        Assert.notNull(directory, "directory must not be null");
        Assert.hasText(filename, "filename must not be null");
        Assert.notNull(data, "body must not be null");
        this.directory = directory;
        this.filename = filename;
        this.uniqueFileName = EmailParserUtils.generateName(filename);
        this.data = data;
    }
}
