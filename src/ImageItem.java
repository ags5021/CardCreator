import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageItem {

	private BufferedImage _image;
	private int height;
	private int width;
	
	public ImageItem(BufferedImage img, int height, int width)
	{
		this._image = img;
		this.height = height;
		this.width = width;
	}
	
	public BufferedImage get_image() {
		return _image;
	}
	public void set_image(BufferedImage _image) {
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
