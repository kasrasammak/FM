package application;

import javax.sound.sampled.LineUnavailableException;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class KasraWindow {
	//stage shit
	static Stage stage;
	static Scene sc;
	static Canvas canvas;
	static GridPane gp;
	static GraphicsContext gc;
	
	//integers and other stuff
	static double somethingElse = 1;
	
	//buttons
	final Slider pitchSlider = new Slider(0, 100, 4);
	static Button btkasraButton = new Button();
	static Button applyCounter6 = new Button("Apply");
	static Button justasound = new Button("Click for Sound");
	static double pitchDivisor = 8;
	static boolean mouseDown = false;
	static boolean mouseChange;
	static double xBounds;
	static double yBounds;
	static double xNow;
	static double yNow;
	static StackPane stackpane = new StackPane();
	
	static Canvas seeWave()
	{
		Canvas tmp = new Canvas(2000,1000);
		gc = tmp.getGraphicsContext2D();
		gc.setLineWidth(.5);
		byte[] myArray = KSound.seeLine((xNow- xBounds)/100, (yNow - yBounds));
				//KSound.generateRandomArray2((xNow- xBounds)/100, (yNow - yBounds)/200);
		double avg = myArray[0];
				//(myArray[0] + myArray[1] + myArray[2] + myArray[3])/4;
		gc.strokeOval(0, avg, 1, 1);
		
		for (int i = 1; i < 999; i++) 
		{
			
			//double avg1 = (myArray[(i*4)] + myArray[(i*4)+1] + myArray[(i*4)+2] + myArray[(i*4)+3])/4;
			//gc.strokeOval(i, (avg1 +127), 1, 1);
			gc.strokeOval(i, (Math.atan2(((myArray[(i*4)] + myArray[(i*4)+1] + myArray[(i*4)+2] + myArray[(i*4)+3])/4) , yNow)*127) +127 , 1, 1);

		}
			return tmp;
	} 
	
	static Canvas seeBB(byte[] bb)
	{
		Canvas tmp = new Canvas(2000,1000);
		gc = tmp.getGraphicsContext2D();
		gc.setLineWidth(.5);
		byte[] myArray = bb;
		for (int i = 1; i <499; i++)
		{
			double avg1 = (myArray[(i*4)] + myArray[(i*4)+1] + myArray[(i*4)+2] + myArray[(i*4)+3])/4;
			gc.strokeOval(i, (avg1 +127), 1, 1);
		}
		return tmp;
	}
	
	public static byte[] seeLine(double xCor, double yCor)
    {	
    	double attack = 0.1;
    	byte[] bigByte = new byte[4000];
    	int otheri = 0;
		for(int i = 0; i < 4000; i++)
		{
			bigByte[i] = (byte)(Math.cos((i/(xCor) + yCor*Math.sin(i/yCor)))*Math.min(127, attack));
			attack = attack + 0.5;
		}	
    	return bigByte;
    }

//	@SuppressWarnings("deprecation")
	public static void display() 
	{
		
		Pane pane = new Pane();
		gp = new GridPane();
		stage = new Stage();
		sc = new Scene(pane);
		
		Rectangle rect = new Rectangle();
		rect.setHeight(200);
		rect.setWidth(600);
		rect.setFill(Color.ALICEBLUE);
		canvas = new Canvas(500, 500);
		gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(2.0);
		
		//byte[] byteArray2 = new byte[2000];
		/*for (int i = 0; i <500; i++) 
		{
			if (i % 4 == 0) 
			{
				gc.strokeOval(i, 200-i, 1, 1);
			}
			else if (i < 400 && i > 200) {
			gc.strokeOval(i, 400-i, 1, 1);
		}
		else 
		{
			gc.strokeOval(i, 500-i, 1, 1);
		}
		}*/
		pane.setBackground(new Background((new BackgroundFill(Color.DARKOLIVEGREEN, CornerRadii.EMPTY, Insets.EMPTY))));

		TextField counter6Input = new TextField();
		counter6Input.setText("" + somethingElse);
		counter6Input.setMaxWidth(50);
		
		Slider pitchSlider = new Slider(0, 100, 4);
		
		pitchSlider.setShowTickMarks(true);
		pitchSlider.setShowTickLabels(true);
		pitchSlider.setMajorTickUnit(0.5f);
		pitchSlider.setBlockIncrement(1);
		
		pitchSlider.valueProperty().addListener(
				event ->
				{
					pitchDivisor = pitchSlider.getValue();
					counter6Input.setText(Double.toString(pitchDivisor));
				}
	
		);
		
		btkasraButton.setOnAction
	(
			event ->
			{
				try
				{
					pitchDivisor = Double.parseDouble(counter6Input.getText());
					KSound pa = new KSound();
					pa.openLine2(pitchDivisor, somethingElse);
			
				}
				catch(Exception e)
				{
					
				}
			}
	);
		

	applyCounter6.setOnAction
	(
		event ->
		{
			try
			{
				somethingElse = Integer.parseInt(counter6Input.getText());
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	);
	
	justasound.setOnAction(event -> {
		try
		{
			KSound kSound = new KSound();
			//kSound.createTone(1000, 10);
			kSound.createTone(3200, 10); 
			kSound.createTone(400, 10);
			kSound.createTone(800, 10);
			kSound.createTone(1600, 10);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	});
	
	
	pane.addEventFilter(MouseEvent.MOUSE_PRESSED, e ->
	{ 
		mouseDown = true;
	}
	);	
	
	pane.addEventFilter(MouseEvent.MOUSE_DRAGGED, e ->
	{ 
		KSound.b = true;
	}
	);
	
	pane.addEventFilter(MouseEvent.ANY, e ->
	{ 
		
		KSound.b = true;
		xNow = e.getSceneX();
		yNow = e.getSceneY();
			/*if(e.isPrimaryButtonDown() 
				&& (e.getSceneX() > rect.boundsInParentProperty().getValue().getMinX())
				&& (e.getSceneX() < rect.boundsInParentProperty().getValue().getMaxX())
				&& (e.getSceneY() < rect.boundsInParentProperty().getValue().getMaxY())
				&& (e.getSceneY() > rect.boundsInParentProperty().getValue().getMinY())
				) */
		{
		try
		{
			Thread.sleep(5);
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
			if (e.isPrimaryButtonDown()) {
			stackpane.getChildren().clear();
			stackpane.getChildren().add(seeWave());
			Runnable myRunnable = new Runnable()
			{
				public void run()
				{
					
					KSound.b = false;	
			
						KSound.fmLine((e.getSceneX() - xBounds)/12, (e.getSceneY() - yBounds));
					
					//KSound.openLine2((e.getSceneX() - rect.getBoundsInParent().getMinX())/10), (e.getSceneY() - rect.getBoundsInParent().getMinY()));
					
					//System.out.println(e.getSceneX()- rect.getBoundsInParent().getMinX());
					//System.out.println(e.getSceneY()- rect.getBoundsInParent().getMinY());
					//System.out.println(e.getSceneX()- rect.getBoundsInParent().getMinX()/10);
					//Thread.currentThread().interrupt();
				}
			};
			Thread sound = new Thread(myRunnable);
			sound.start();
			//Thread sound2 = new Thread(myRunnable);
			
			//sound2.start();
				
			
			}
		}
	}
	);

	pane.addEventFilter(MouseEvent.MOUSE_RELEASED, e ->
	{ 
		mouseDown = false;
		
	}
);
		
		pane.getChildren().add(gp);
		gp.add(btkasraButton, 5, 6);
		gp.add(counter6Input, 5, 5);
		gp.add(applyCounter6, 5, 7);
		gp.add(pitchSlider, 5, 8);
		gp.add(rect, 5, 9);
		gp.add(justasound, 5, 10);
		gp.add(stackpane, 5, 11);
		
		xBounds = rect.getBoundsInParent().getMinX();
		yBounds = rect.getBoundsInParent().getMinY();
		
		gp.setHgap(10);
		gp.setVgap(10);
		
		stage.setWidth(1150);
		stage.setHeight(900);
		stage.setScene(sc);
		stage.setAlwaysOnTop(true);
		stage.show();
	};
	
	
	/*
	
	
	*/


/*

private static Node seeWave() {
	// TODO Auto-generated method stub
	return null;
}
*/
}
