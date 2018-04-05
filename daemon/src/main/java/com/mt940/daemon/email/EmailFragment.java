/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mt940.daemon.email;

import org.springframework.util.Assert;

import java.io.File;

public class EmailFragment {

    private Object data;
    private String filename;
    private String uniqueFileName;
    private int size;
    private File directory;

    public EmailFragment(Object data, String filename, String uniqueFileName, int size, File directory) {
        this.data = data;
        this.filename = filename;
        this.uniqueFileName = uniqueFileName;
        this.size = size;
        this.directory = directory;
    }

    /**
     * Constructor.
     *
     * @param directory Must not be null
     * @param filename  Must not be null
     * @param data      Must not be null
     */
    public EmailFragment(File directory, String filename, Object data) {
        super();

        Assert.notNull(directory);
        Assert.hasText(filename);
        Assert.notNull(data);

        this.directory = directory;
        this.filename = filename;
        this.uniqueFileName = EmailParserUtils.generateName(filename);
        this.data = data;
    }

    /**
     * The data to save to the file system, e.g. text messages/attachments, binary
     * file attachments etc.
     */
    public Object getData() {
        return data;
    }

    /**
     * The file name to create for the respective {@link EmailFragment}.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * The directory where to store the {@link #getData()} using the specified
     * {@link #getFilename()}.
     */
    public File getDirectory() {
        return this.directory;
    }

    public String getUniqueFileName() {
        return uniqueFileName;
    }

    public int getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((directory == null) ? 0 : directory.hashCode());
        result = prime * result + ((filename == null) ? 0 : filename.hashCode());
        result = prime * result + ((uniqueFileName == null) ? 0 : uniqueFileName.hashCode());
        result = prime * result + size;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EmailFragment other = (EmailFragment) obj;
        if (directory == null) {
            if (other.directory != null) {
                return false;
            }
        } else if (!directory.equals(other.directory)) {
            return false;
        }
        if (filename == null) {
            if (other.filename != null) {
                return false;
            }
        } else if (!filename.equals(other.filename)) {
            return false;
        }
        if (uniqueFileName == null) {
            if (other.uniqueFileName != null) {
                return false;
            }
        } else if (!uniqueFileName.equals(other.uniqueFileName)) {
            return false;
        }
        if (size == 0) {
            if (other.size == 0) {
                return false;
            }
        } else if (size != other.size) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "EmailFragment [filename=" + filename + ", directory="
                + directory + "]";
    }

}
