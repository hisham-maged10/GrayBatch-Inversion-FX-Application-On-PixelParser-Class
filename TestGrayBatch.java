/**
 * @author ${hisham_maged10}
 *https://github.com/hisham-maged10
 * ${DesktopApps}
 */
import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.WritableImage;
import java.util.ArrayList;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.geometry.Insets;
public class TestGrayBatch extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		for(Stage e:makeStages(makeScenes(makeImageViews(PixelParser.makeImgArray()))))
		e.show();
	}	
	public ArrayList<ImageView> makeImageViews(ArrayList<PixelParser> imgArr)
	{
		ArrayList<ImageView> imgs=new ArrayList<>();
		for(int i=0,n=imgArr.size();i<n;i++)
		{
			imgs.add(new ImageView(imgArr.get(i).getImageFile()));		
		}
		addWritableImages(imgs,imgArr,Choice.GRAYSCALE);
		addWritableImages(imgs,imgArr,Choice.INVERSION);
		return imgs;
	}
	public void addWritableImages(ArrayList<ImageView> imgs,ArrayList<PixelParser> imgArr,Choice choice)
	{
		for(int i=0,n=imgArr.size();i<n;i++)
		{
			imgs.add(new ImageView(makeWritableImage(imgArr.get(i),choice)));
		}		
	}
	public ArrayList<Scene> makeScenes(ArrayList<ImageView> imageViews)
	{	
		ArrayList<Scene> scenes=new ArrayList<>();
		for(int i=0,n=imageViews.size()/3;i<n;i++)
		{
			final int positionOG=i;
			final int positionGray=i+n;
			final int positionInverted=i+(n*2);
			Label lblOGImage=new Label("Original Image");
			lblOGImage.setGraphic(imageViews.get(positionOG));
			lblOGImage.setContentDisplay(ContentDisplay.BOTTOM);
			
			Label lblConvertedImage=new Label("Gray Scaled Image");
			lblConvertedImage.setGraphic(imageViews.get(positionGray));
			lblConvertedImage.setContentDisplay(ContentDisplay.BOTTOM);
			
			ToggleGroup radioGP=new ToggleGroup();						
			RadioButton grayScale=new RadioButton("GrayScale");
			grayScale.setToggleGroup(radioGP);
			grayScale.setSelected(true);
			grayScale.setTooltip(new Tooltip("This will converted the image to gray scale colors"));
			RadioButton inverted=new RadioButton("Inverted");
			inverted.setToggleGroup(radioGP);
			inverted.setTooltip(new Tooltip("This will converted the image to inverted colors"));
			grayScale.setOnAction(e->{
				lblConvertedImage.setText("Gray Scaled Image");
				lblConvertedImage.setGraphic(imageViews.get(positionGray));
				lblConvertedImage.setContentDisplay(ContentDisplay.BOTTOM);
			});
			inverted.setOnAction(e->{
				lblConvertedImage.setText("Color Inverted Image");
				lblConvertedImage.setGraphic(imageViews.get(positionInverted));
				lblConvertedImage.setContentDisplay(ContentDisplay.BOTTOM);
			});

			Button saveBtn=new Button("save");
			saveBtn.setTooltip(new Tooltip("This will save the image in the current directory"));
			saveBtn.setOnAction(e->{
				if(grayScale.isSelected())
				{
					save(imageViews.get(positionGray),Choice.GRAYSCALE);
				}
				else if(inverted.isSelected())
				{
					save(imageViews.get(positionGray),Choice.INVERSION);
				}
			});
			VBox vb=new VBox(10);
			vb.setStyle("-fx-border-color:black");
			vb.setPadding(new Insets(10));
			vb.getChildren().addAll(grayScale,inverted,saveBtn);
			HBox hbox=new HBox(10);
			hbox.setStyle("-fx-border-color:black");
			hbox.setPadding(new Insets(5,0,0,0));
			hbox.getChildren().addAll(lblOGImage,lblConvertedImage,vb);
			hbox.setAlignment(Pos.CENTER);
			
			Pane pane=new Pane();
			pane.getChildren().add(hbox);
			Scene scene=new Scene(pane);
			scenes.add(scene);
		}
		return scenes;
	}
	public ArrayList<Stage> makeStages(ArrayList<Scene> scenes)
	{
		ArrayList<Stage> stages=new ArrayList<>();
		for(int i=0,n=scenes.size();i<n;i++)
		{
			stages.add(new Stage());
			stages.get(i).setScene(scenes.get(i));
			stages.get(i).setResizable(false);
			stages.get(i).setTitle("Convert to GrayScale/Inverted FX");
		}	
		return stages;	
	}
	
	public WritableImage makeWritableImage(PixelParser img,Choice choice)
	{
		WritableImage imgWritable=new WritableImage((int)img.getImageFile().getWidth(),(int)img.getImageFile().getHeight());
		changePx(img,imgWritable,choice);
		return imgWritable;
	}
	public void save(ImageView imgView,Choice choice)
	{
		String type=(choice.toString().equals("GRAYSCALE"))?"-GrayScale":"-inverted";
		try{
		ImageIO.write(SwingFXUtils.fromFXImage(imgView.getImage(),null),"png",new File(type+" "+".png"));	
		}catch(IOException ex){ ex.printStackTrace(); }
					
	}
	public void changePx(PixelParser oldImg,WritableImage img,Choice choice)
	{
		double x,y,w,h;
		int i=0;
		Color[] oldColors=oldImg.getPxIterable();
		for(y=0,h=oldImg.getImageFile().getHeight();y<h;y++)
		{
			for(x=0,w=oldImg.getImageFile().getWidth();x<w;x++)
			{
				switch(choice)
				{
				case GRAYSCALE:img.getPixelWriter().setColor((int)x,(int)y,oldColors[i++].grayscale());break;
				case INVERSION:img.getPixelWriter().setColor((int)x,(int)y,oldColors[i++].invert());break;
				}
			}

		}
	
	}

}