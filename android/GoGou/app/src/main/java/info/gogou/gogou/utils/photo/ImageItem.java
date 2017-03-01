package info.gogou.gogou.utils.photo;

import java.io.Serializable;


public class ImageItem implements Serializable {

	private String mImageId;
	private String mThumbnailPath;
	private String mImagePath;
	private boolean mIsSelected = false;
	
	public String getImageId() {
		return mImageId;
	}

	public void setImageId(String imageId) {
		this.mImageId = imageId;
	}

	public String getThumbnailPath() {
		return mThumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.mThumbnailPath = thumbnailPath;
	}
    
	public String getImagePath() {
		return mImagePath;
	}

	public void setImagePath(String imagePath) {
		this.mImagePath = imagePath;
	}

	public boolean isSelected() {
		return mIsSelected;
	}

	public void setSelected(boolean isSelected) {
		this.mIsSelected = isSelected;
	}
}
