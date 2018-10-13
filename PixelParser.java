/**
 * @author ${hisham_maged10}
 *https://github.com/hisham-maged10
 * ${DesktopApps}
 */
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import java.io.File;
import javax.swing.JFileChooser;
import java.util.ArrayList;
public class PixelParser
{	
	private Image imgFile;
	private ArrayList<Color> pxArr;
	private File imgLocation;
	public PixelParser()
	{
		this.imgFile=getImage();
		getPixels();
	}
	public PixelParser(String str)
	{
		this.imgFile=getImage(str);
		getPixels();
	}
	public PixelParser(File imgFile)
	{
		this.imgFile=getImage(imgFile);
		getPixels();
	}
	public PixelParser(Image imgFile)
	{
		this.imgFile=imgFile;		
		getPixels();
	}
	public Image getImageFile()
	{
		return this.imgFile;
	}
	private Image getImage(String str)
	{
		if(!(str.endsWith(".jpg") || str.endsWith(".png")) || !(new File(str).isFile())) throw new InvalidImageFormatException("Please load a jpg or png format only!");
		if(str.contains(":"))str=changeToRelativePath(str);
		imgLocation=new File(str);
		Image img=new Image(str);
		return img;
	}
	//overload
	private Image getImage(File imgFile)
	{
		String str=imgFile.getPath();
		if(!(str.endsWith(".jpg") || str.endsWith(".png")) || !(imgFile.isFile())) throw new InvalidImageFormatException("Please load a jpg or png format only!");
		if(str.contains(":"))str=changeToRelativePath(str);
		imgLocation=new File(str);
		Image img=new Image(str);
		return img;
	}
	//overload
	private Image getImage()
	{
		String str=getFile().getPath();
		if(!(str.endsWith(".jpg") || str.endsWith(".png")) || !(new File(str).isFile())) throw new InvalidImageFormatException("Please load a jpg or png format only!");
		if(str.contains(":"))str=changeToRelativePath(str);
		imgLocation=new File(str);
		Image img=new Image(str);
		return img;
	}
	private String changeToRelativePath(String str)
	{
		String cwd=new File(".").getAbsolutePath();
		cwd=cwd.substring(0,cwd.lastIndexOf("."));
		if(str.contains(cwd))
		{
			str=str.replace(cwd,"");
			if(str.contains("\\")) str=str.replace("\\","/");
		}
		else throw new InvalidPathException("Image object must have a relative path!");
		return str;
	}
	private File getFile()
	{
		JFileChooser chooser=new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setCurrentDirectory(new File("."));
		File file=null;
		do{chooser.showOpenDialog(null); file=chooser.getSelectedFile(); }while(file==null);
		return file;
	}
	private static File[] getFiles()
	{
		JFileChooser chooser=new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(true);
		chooser.setCurrentDirectory(new File("."));
		File[] files=null;
		do{chooser.showOpenDialog(null); files=(chooser.getSelectedFiles().length>0)?chooser.getSelectedFiles():files; }while(files==null);
		return files;	
	}
	//stays static
	public static ArrayList<PixelParser> makeImgArray()
	{
		ArrayList<PixelParser> imgArr=new ArrayList<>();
		File[] files=getFiles();
		for(int i=0;i<files.length;i++)
		{
			imgArr.add(new PixelParser(files[i]));
		}	
		return imgArr;
	}
	//stays static, oveload
	public static ArrayList<PixelParser> makeImgArray(File[] files)
	{
		ArrayList<PixelParser> imgArr=new ArrayList<>();
		for(int i=0;i<files.length;i++)
		{
			imgArr.add(new PixelParser(files[i]));
		}	
		return imgArr;
	}

	private void getPixels()
	{
		if(imgFile==null) throw new ImageNotLoadedException("the image file was not loaded sucessfully");
		pxArr=fillPxArr();
	}
	private ArrayList<Color> fillPxArr()
	{
		ArrayList<Color> pixels=new ArrayList<Color>();
		double n=imgFile.getHeight();
		double m=imgFile.getWidth();
		for(int y=0;y<n;y++)
		{
			for(int x=0;x<m;x++)
			{
				pixels.add(imgFile.getPixelReader().getColor(x,y));
			}
		}
		return pixels;
	}
	
	public Color[] getPxIterable()
	{
		if(pxArr==null)throw new PixelsNotLoadedException("Pixels were not loaded sucessfully as the image may be unreadable!");
		return pxArr.toArray(new Color[pxArr.size()]);
	}
	
	public String getFileName()
	{
		if(imgFile==null) throw new ImageNotLoadedException("the image file was not loaded sucessfully");
		return imgLocation.getPath().substring(imgLocation.getPath().lastIndexOf("\\")+1);
	}
	public String getFilePath()
	{
		if(imgFile==null) throw new ImageNotLoadedException("the image file was not loaded sucessfully");
		return imgLocation.getPath();
	}
}