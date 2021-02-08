package application;

import java.awt.MouseInfo;
import java.awt.Point;

// BASIC INSTRUCTIONS.

/*
 * 		OPEN a source directory that contains .wav files using "source directory" button.
 * 		My zipped src comes with a few wavs so link that for testing.
 *
 *
 * 		Hit Sound button to make a sound object. Left click to kill it. Make more to layer.
 *
 *
 * 		The Text Field is used for setting an "Inverse Scaling Constant", which will alter the loop.
 * 		A smaller number will yield longer loops and a larger number shorter loops.
 * 		Really large numbers will make tones. Often not pleasant ones so test at your own peril.
 *
 *
 *
 * 		Use "Open File" button to link a wav file to visualize using "Draw" button.
 * 		Still in crude stages.
 *
 *
 *
 */


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application
{

	// Variables
	int pouchSize;
	int magic = 2;
	double pitchDivisor = 8;
	int c1Cycle = 4;
	int c2Up = 5;
	int c3Down = 6;
	int c4Mute = 7;
	int c5Unmute = 2;
	double mouseX;
	double mouseY;
	boolean isClickedR;
	boolean isClickedL;
	boolean holding = false;
	SoundCircle heldSound;
	SimpleDoubleProperty mouseDX = new SimpleDoubleProperty();
	SimpleDoubleProperty mouseDY = new SimpleDoubleProperty();

	File myFile = new File("F:\\PDsourceAudio\\2topNormal16.wav"); // for draw function
	File myDir;  //directory link
	
	int folderIndex = 1;
	int directorySize;
	
	SoundPouch p;
	
	ArrayList<File> directory = new ArrayList<File>();   //array of File objects made from wav files from linked directory
	ArrayList<SoundCircle> runningSounds = new ArrayList<SoundCircle>();   //list of currently running sound circles 
	
	
	
	// Panes
	Pane ROOT = new Pane();
	StackPane stack = new StackPane();
	FlowPane stackFlow = new FlowPane();
	BorderPane bp = new BorderPane();
	Pane geo = new Pane();
	GridPane gp = new GridPane();
	FlowPane lf = new FlowPane();
	Workshop workshop = new Workshop();

	
	
	//-------------Buttons---------------------

	
	
			Button btKasra = new Button("K-Window");
		    Button btRandomSound = new Button("Mystery Sound");
		    Button btPlayArray = new Button("PlayArray");
			final Slider pitchSlider = new Slider(0, 200, 4);
			Button btFractal = new Button("Open Fractal");
			Button btSound = new Button("Sound");
			Button btPouch = new Button("Make SoundPouch");
			Button btOpen = new Button("Open File");
			Button btOpenDir = new Button("Source Directory");
			Button btMagic = new Button("Magic Number: ");
			Button btRec = new Button("Coming Soon! (Record)");
			Button applyCounter = new Button("Apply");
			Button applyCounterAll = new Button("Apply All");
			Button cycleReset = new Button("Reset Cycles");
			Button cycleRandom = new Button("Randomize Cycles");
			
	

	@Override
	public synchronized void start(Stage pStage) throws Exception
	{
		
		
		//--------Other Functional Nodes-----------
		TextField magicInput = new TextField();
		magicInput.setText("" + magic);
		magicInput.setMaxWidth(75);
		
		Label c1Label = new Label("Cycle");
		Label c2Label = new Label("+%");
		Label c3Label = new Label("-%");
		Label c4Label = new Label("Mute");
		Label c5Label = new Label("Unmute");
		Label pouchSizeLabel = new Label("Pouch Size");
		
		TextField pitchInput = new TextField();
		pitchInput.setText("" + pitchDivisor);
		pitchInput.setMaxWidth(50);
		
		TextField pouchSizeInput = new TextField();
		pouchSizeInput.setText("" + 10);
		pouchSizeInput.setMaxWidth(50);
		
		TextField counter1Input = new TextField();
		counter1Input.setText("" + c1Cycle);
		counter1Input.setMaxWidth(50);
		
		TextField counter2Input = new TextField();
		counter2Input.setText("" + c2Up);
		counter2Input.setMaxWidth(50);
		
		TextField counter3Input = new TextField();
		counter3Input.setText("" + c3Down);
		counter3Input.setMaxWidth(50);
		
		TextField counter4Input = new TextField();
		counter4Input.setText("" + c4Mute);
		counter4Input.setMaxWidth(50);
		
		TextField counter5Input = new TextField();
		counter5Input.setText("" + c5Unmute);
		counter5Input.setMaxWidth(50);
		

		
		pitchSlider.setShowTickMarks(true);
		pitchSlider.setShowTickLabels(true);
		pitchSlider.setMajorTickUnit(0.25f);
		pitchSlider.setBlockIncrement(1);
		
		pitchSlider.valueProperty().addListener(
				event ->
				{
					pitchDivisor = pitchSlider.getValue();
					pitchInput.setText(Double.toString(pitchDivisor));
				}
	
		);
		
		
		
		
		//________________________________________________________________________
		//________________________________________________________________________
		//_________________________BUTTON FUNCTIONS_______________________________
		//________________________________________________________________________
	
		
		btFractal.setOnAction
		(
				e->
				{
					try 
					{
						FractalWindow.display();
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				
				
				
				
				);
		
		btKasra.setOnAction
		(
			event ->
			{
				try
				{
					KasraWindow.display();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		);
		
		btPlayArray.setOnAction
		(
				event ->
				{
					try
					{
						pitchDivisor = Double.parseDouble(pitchInput.getText());
						SoundArray pa = new SoundArray();
						pa.openLine(pitchDivisor);
					}
					catch(Exception e)
					{
						
					}
				}
		);
		
		//---------SOUND BUTTON ------------------



				btSound.setOnAction
				(
					event ->
					{
						try
						{
							DeepWav bob = randomBob(false);
							bob.magic = magic;
							bob.randomPlay();
							makeSoundCircle(bob);
							folderIndex++;
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				);

				
				//----------RANDOMIZE BUTTON------------

				cycleRandom.setOnAction
				(
					event ->
					{
						try
						{
							for(SoundCircle s: runningSounds)
							{
								s.randomize();
							}
								
								
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				);
		
				
				

		//--------------OPEN BUTTON------------------------------

		btOpen.setOnAction
		(
				event ->
				{

					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Open Wav File");
					File file = fileChooser.showOpenDialog(pStage);
					try
					{
						myFile = file;
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
		);

		//----------OPEN DIRECTORY------------

		btOpenDir.setOnAction
		(
				event ->
				{

					DirectoryChooser dirChooser = new DirectoryChooser();
					dirChooser.setTitle("Link Source Directory");
					File dir = dirChooser.showDialog(pStage);
					getWavFilesFromFolder(dir);

					try
					{
						myDir = dir;
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
		);

		//-----------APPLY MAGIC NUMBER------------

		btMagic.setOnAction
			(
				event ->
				{
					try
					{
						magic = Integer.parseInt(magicInput.getText());
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			);
		
		//-----------APPLY MAGIC NUMBER------------

		applyCounter.setOnAction
		(
			event ->
			{
				try
				{
					c1Cycle = Integer.parseInt(counter1Input.getText());
					c2Up = Integer.parseInt(counter2Input.getText());
					c3Down = Integer.parseInt(counter3Input.getText());
					c4Mute = Integer.parseInt(counter4Input.getText());
					c5Unmute = Integer.parseInt(counter5Input.getText());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		);
		
		applyCounterAll.setOnAction
		(
			event ->
			{
				try
				{
					c1Cycle = Integer.parseInt(counter1Input.getText());
					c2Up = Integer.parseInt(counter2Input.getText());
					c3Down = Integer.parseInt(counter3Input.getText());
					c4Mute = Integer.parseInt(counter4Input.getText());
					c5Unmute = Integer.parseInt(counter5Input.getText());
					for(SoundCircle p: runningSounds)
					{
						p.bob.c1Cycle = c1Cycle;
						p.bob.c2Up = c2Up;
						p.bob.c3Down = c3Down;
						p.bob.c4Mute = c4Mute;
						p.bob.c5Unmute = c5Unmute;
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		);
		
		
		
		//----------VISUALIZING SAMPLE------------

				btPouch.setOnAction
				(
					event ->
					{
						pouchSize = Integer.parseInt(pouchSizeInput.getText());
						p = new SoundPouch(directory, pouchSize,  10000); // last is length
					}

				);

				
				btRandomSound.setOnAction
				(
					event ->
					{
						int index = (int)(Math.random()*pouchSize);
						System.out.println(p.sounds[index].source);
						p.play(index, 5);
					}

				);
				
		
		//-----------------REC BUTTON------------

		btRec.setOnAction
		(
			event ->
			{
				final WavRecorder recorder = new WavRecorder();

			    Thread stopper = new Thread
			    (
			    	new Runnable()
			        {
			    		public void run()
			    		{
			    			try
			    			{
			    				Thread.sleep(recorder.RECORD_FOR);
			    			}
			    			catch (InterruptedException ex)
			    			{
			    				ex.printStackTrace();
			    			}
			    			recorder.finish();
			    		}
			        }
			    );
			    stopper.start();
			    // start recording
			    recorder.start();
			}
		);
		
		//________________________________________________________________________
		//________________________________________________________________________
		//________________________________________________________________________
		//________________________________________________________________________
		
		
		gp.setHgap(5);
		gp.setVgap(10);
		gp.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		gp.add(btPouch, 3, 2);
		gp.add(pouchSizeInput, 3, 4);
		gp.add(pouchSizeLabel, 3, 3);
		gp.add(btRandomSound, 2, 5);
		
		gp.add(btFractal, 6, 2);
		gp.add(pitchSlider, 2, 4);
		gp.add(pitchInput, 2, 3);
		gp.add(btPlayArray, 2, 2);
		gp.add(btSound, 3, 5);
		gp.add(btKasra, 6, 3);
		gp.add(btOpen, 5, 2);
		gp.add(btOpenDir, 3, 7);
		gp.add(btRec, 10, 19);
		gp.add(btMagic, 3, 11);
		gp.add(magicInput, 3, 12);
		gp.add(stack, 3, 10);
		gp.add(c1Label, 5, 12);
		gp.add(counter1Input, 5, 13);
		gp.add(c2Label, 6, 12);
		gp.add(counter2Input, 6, 13);
		gp.add(c3Label, 5, 14);
		gp.add(counter3Input, 5, 15);
		gp.add(c4Label, 6, 14);
		gp.add(counter4Input, 6, 15);
		gp.add(c5Label, 5, 16);
		gp.add(counter5Input, 5, 17);
		gp.add(applyCounter, 4, 13);
		gp.add(applyCounterAll, 4, 14);
		gp.add(cycleReset, 3, 16);
		gp.add(cycleRandom, 3, 17);
		
		lf.setMinWidth(200);  
		lf.setMaxWidth(200);
		lf.setVgap(10);
		lf.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		
		stack.setMaxWidth(160);
		stack.setMinWidth(160);
		stack.setMaxHeight(80);
		stack.setMinHeight(80);
		stack.setAlignment(Pos.TOP_LEFT);
		stack.setBorder(new Border(new BorderStroke(Color.RED, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		stack.getChildren().add(stackFlow);
		stack.getChildren().add(workshop);
		
		//geo.getChildren().add(gp); 
		
		bp.setLeft(lf);
		bp.setCenter(gp);

		ROOT.getChildren().add(bp);
		ROOT.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		Scene sc = new Scene(ROOT);
		pStage.setScene(sc);
		
		//Stage listens to mouse movement, stores mouse X and Y coordinates as SimpleDoubleProperty mouseDX and mouseDY in main class
		pStage.addEventFilter   
		(
			MouseEvent.ANY, e -> 
			{
				mouseDX.set(e.getX() -5);
				mouseDY.set(e.getY() -5);
				mouseX = e.getX();
				mouseY = e.getY();
				isClickedR = e.isSecondaryButtonDown();
				isClickedL = e.isPrimaryButtonDown();
			}
		);
		// Set Stage parameters and show
		pStage.setTitle("doob");
		pStage.setHeight(800);
		pStage.setWidth(1000);
		pStage.setAlwaysOnTop(true);
		pStage.show();
	}
	
	

	public void makeSoundCircle(DeepWav bob)
	{

		SoundCircle p = new SoundCircle(bob);
		
		
		p.setOnMouseClicked
		(

			event  ->
				{
					if(event.getButton() == MouseButton.PRIMARY)
					{
						try
						{
							bob.mediaPlayer.stop();
							bob.mediaPlayer.dispose();
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
						try
						{
							if(p.getParent().equals(lf))
							{
								lf.getChildren().remove(p);
							}
							else ROOT.getChildren().remove(p);
							
					
						runningSounds.remove(p);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						
					}
					
					if(event.getButton() == MouseButton.SECONDARY)
					{
						Window.display(p);
					}
				}
		);
		runningSounds.add(p);
		lf.getChildren().add(p);	
	}
	
	public File randomFile()
	{
		return new File(directory.get((int)(directorySize * Math.random())).getAbsolutePath());
	}

	public DeepWav randomBob(boolean array)
	{
		DeepWav bob = new DeepWav();
		folderIndex = (int)(directorySize * Math.random());
		if (!array)
		{
		bob = new DeepWav(directory.get(folderIndex).getAbsolutePath());
		}
		else
		{
		bob = new DeepWav(directory.get(folderIndex).getAbsolutePath(), true);
		}
		return bob;
	}
	public void getWavFilesFromFolder(final File folder)
	{

		String ext1 = ".wav"; //currently supported extensions
		String ext2 = ".mp3";
		try
		{
			for (final File file : folder.listFiles())
	    	{
	        	if (file.isDirectory())
	        	{
	            	getWavFilesFromFolder(file);
	        	}
	        	else
	        	{
	        		int i = file.getAbsolutePath().lastIndexOf('.');
	        		int j = file.getAbsolutePath().lastIndexOf('/');


	    			if (i > 0)
	    			{
	    		    	if ((file.getAbsolutePath().substring(i).equals(ext1)) || (file.getAbsolutePath().substring(i).equals(ext2)))
	    		    	{
	    		    		System.out.println(file.getAbsolutePath().substring(j));
	    		    		directory.add(file);
	    		    		folderIndex++;
	    		    	} 
	    			}
	        	}
	    	}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	    directorySize = directory.size();
	}

	public static void main(String[] args)
	{
		launch(args);
	}	
}



