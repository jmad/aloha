package cern.accsoft.steering.aloha.report.fit;

import java.io.InputStream;

public class FitImage {

	private InputStream image;
	/**
	 * @return the image
	 */
	public InputStream getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(InputStream image) {
		this.image = image;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	private String caption;

	public FitImage(String caption, InputStream image) {
		this.caption = caption;
		this.image = image;
	}
}
