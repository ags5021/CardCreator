import java.awt.Image;

public class ImageItem {

	private Image _image;
	private int height;
	private int width;
	
	public ImageItem(Image img, int height, int width)
	{
		this._image = img;
		this.height = height;
		this.width = width;
	}
	
	public Image get_image() {
		return _image;
	}
	public void set_image(Image _image) {
		this._image = _image;
	}
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
}
