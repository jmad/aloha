package cern.accsoft.steering.aloha.read;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.List;

public interface Reader {

	/**
	 * @return the file filter which can be used for a file-open dialog.
	 */
	public abstract FileFilter getFileFilter();

	/**
	 * @return a description of the files to handle
	 */
	public abstract String getDescription();

	/**
	 * @param files
	 *            the files to test
	 * @return true if this Reader can handle all the given files, false if not
	 */
	public abstract boolean isHandling(List<File> files);

}