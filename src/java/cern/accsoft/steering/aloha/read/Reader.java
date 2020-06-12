package cern.accsoft.steering.aloha.read;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.List;

public interface Reader {

    /**
     * @return the file filter which can be used for a file-open dialog.
     */
    FileFilter getFileFilter();

    /**
     * @return a description of the files to handle
     */
    String getDescription();

    /**
     * @param files the files to test
     * @return true if this Reader can handle all the given files, false if not
     */
    boolean isHandling(List<File> files);

}