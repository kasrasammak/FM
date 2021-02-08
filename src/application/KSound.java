package application;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.canvas.Canvas;

public class KSound 
{
	static boolean b;
    private static int sampleRate = 44100;
    final static AudioFormat FORMAT = new AudioFormat(sampleRate, 8, 1, true, true); // for now
    static int i = 0;
    
    public KSound()
    {
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
    
    
    public static void openLine2(double xCor, double yCor) throws LineUnavailableException
    {
   	try (SourceDataLine line = AudioSystem.getSourceDataLine(FORMAT))
        {  
    			float rate = 200;
    		    byte[] buf = new byte[1];
    		    double attack = 0.05;
           // SourceDataLine line = AudioSystem.getSourceDataLine(FORMAT);
            line.open(FORMAT);
            line.start();
            
          //  while (KasraWindow.mouseDown && !b) 
           //{
               // play(line, generateRandomArray2(xCor, yCor));
            	  for(int i=0; i<rate; i++){
            		  double angle = (i/rate)*yCor*2.0*Math.PI;
            		  double angle2 = (i/rate)*yCor*3.0*2.0*Math.PI;
            		  double angle3 = (i/rate)*yCor*5.0*2.0*Math.PI;
            		  double angle4 = (i/rate)*yCor*7.0*2.0*Math.PI;
            		  
   	    	    //  double angle2 = (i/rate)*yCor*2.0*Math.PI;
   	    	      //buf[0] = (byte) ((byte) Math.sin(angle)*Math.min(100, attack) + Math.sin(angle2)* Math.min(100, attack) + Math.sin(angle3)*Math.min(100, attack));
   	    	      buf[0] = (byte)(Math.cos((i/(xCor) + yCor*Math.sin(i/(Math.pow(Math.E,(yCor))*113*Math.PI))))*Math.min(127, attack));
   	    	      line.write(buf,0,1);
   	    	      attack = attack + 0.05;
   	    	  // }
            }
            
            line.drain();
            line.stop();
            line.close();
        } 
       catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
  
   // (Math.sin(((2*Math.PI*i)/number))*100)
   // (magicNumber)*((Math.sin(((i/number*(3/8)))))*100)
    
    
  /*  static byte[] generateRandomArray2(double number, double magicNumber) 
    {
        int size = 44100;
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) 
        {
       //	byte b = (byte)((Math.sin((i/(number))*100))+ (magicNumber)*(Math.sin((i/(number))*100)));
            byte b = (byte)(magicNumber * Math.sin((i/size)*number*2.0*Math.PI));
        		byteArray[i] = b;
        }
        return byteArray;
    }*/
    
    static byte[] generateRandomArray2(double number, double magicNumber) 
    {
        int size = 2000;
        byte[] byteArray = new byte[size];
       
        for (int i = 0; i < size; i++) 
        {
        	
        	byte b = (byte)(((Math.sin(i/(number))*100) + (magicNumber)*(Math.sin((i/number))))*100);
        byteArray[i] = b;
        } 
  
        return byteArray;
    }
    
    static byte[] generateArray(WavFile myFile, int size)
    {
    	byte[] wavData = myFile.getData();
    	byte[] chunk = new byte[size];
    	System.arraycopy(wavData, (int) (wavData.length / (10*Math.random() + 1)), chunk, 0, size);
    	return chunk;
    }
    
    private static void play(SourceDataLine line, byte[] array) 
    {
        line.write(array, 0, array.length);
    }

   /*public static void miniLine(double x, double y)
    {
        int i = 0;
        byte[] singleByte = new byte[1];

        try (SourceDataLine line = AudioSystem.getSourceDataLine(FORMAT))
        {
            line.open(FORMAT);
            line.start();
            while (KasraWindow.mouseDown && !b) 
            {
                if( i > y * 2 * Math.PI )
                {
                    i = 0;
                }
                singleByte[0] = (byte)((Math.sin((i/(x)))*100));// + (y)*(Math.sin(i/x*(3/8))*50));
                line.write(singleByte, 0, 1);
                i++;
            }

            line.drain();
            line.close();

        } 

        catch (Exception e) 
        {
            e.printStackTrace();
        }


    }*/

    public static void miniLine(double xCor, double yCor)
    {	
    	double attack = 0.1;
    	byte[] singleByte = new byte[1];
    	
		try (SourceDataLine line = AudioSystem.getSourceDataLine(FORMAT))
		{
			line.open(FORMAT);
			line.start();
			while (KasraWindow.mouseDown && !b) 
			{
				
				if((yCor < 350) && (yCor > 22) && (i > yCor*2*Math.PI))
				{
						i = (int)Math.abs((yCor -(i - yCor)));
				}
				singleByte[0] = (byte)((Math.sin((i/(xCor)))*Math.min(127, attack)));
				
				line.write(singleByte, 0, 1);
				i++;
				attack = attack + 0.05;
			}
			
			for(double release = 127; release > 0; release = release - 2)
			{
				singleByte[0] = (byte)((Math.sin((i/(xCor)))*Math.min(127, release)));
				line.write(singleByte, 0, 1);
			}
			
			line.drain();
			line.close();
       
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    	
    }
    
    


public static void fmLine(double xCor, double yCor)
    {	
    	double attack = 0.1;
    	byte[] singleByte = new byte[1];
    	
		try (SourceDataLine line = AudioSystem.getSourceDataLine(FORMAT))
		{
			line.open(FORMAT);
			line.start();
			while (KasraWindow.mouseDown && !b) 
			{
				
				if(i >= Integer.MAX_VALUE)
				{
						i = 0;
						attack = 0.01;
				}
				singleByte[0] = (byte)(Math.cos((i/(xCor) + yCor*Math.sin(i/(Math.pow(Math.E,(yCor))*113*Math.PI))))*Math.min(127, attack));
				line.write(singleByte, 0, 1);
				i++;
				attack = attack + 0.05;
			}
			
			for(double release = 127; release > 0; release = release - 2)
			{
				singleByte[0] = (byte)((Math.sin((i/(xCor)))*Math.min(127, release)));
				line.write(singleByte, 0, 1);
			}
			
			line.drain();
			line.close();
       
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    	
    }

    
    
public static void createTone(int Hertz, int volume)
	    throws LineUnavailableException {
	 
	    float rate = 44100;
	    byte[] buf;
	    buf = new byte[1];
	    double[] sines = new double[(int)rate];
	    int vol = volume;
	 
	    AudioFormat audioF;
	    audioF = new AudioFormat(rate,8,1,true,false);
	 
	    SourceDataLine sourceDL = AudioSystem.getSourceDataLine(audioF);
	    sourceDL = AudioSystem.getSourceDataLine(audioF);
	    sourceDL.open(audioF);
	    sourceDL.start();
	 
	    	  for(int i=0; i<rate; i++){
	    	      double angle = (i/rate)*Hertz*2.0*Math.PI;
	    	      buf[0]=(byte)(Math.sin(angle)*volume);
	    	      sourceDL.write(buf,0,1);
	    	    }
	    
	  /*  	double angle1 = i/rate*Hertz*1.0*2.0*Math.PI;
	    	  double angle2 = i/rate*Hertz*3.0*2.0*Math.PI;
	    	  double angle3 = i/rate*Hertz*5.0*2.0*Math.PI;
	    	  double angle4 = i/rate*Hertz*7.0*2.0*Math.PI;
	    	 
	    	  buf[0]=(byte)(Math.sin(angle1)*vol+
	    	     Math.sin(angle2)*vol/3+Math.sin(angle3)*vol/5+
	    	     Math.sin(angle4)*vol/7);
	    	  sourceDL.write(buf,0,1);
	    	  sines[i]=(double)(Math.sin(angle1)*vol+
	    	     Math.sin(angle2)*vol/3+Math.sin(angle3)*vol/5+
	    	     Math.sin(angle4)*vol/7);
	    }*/
	 
	    sourceDL.drain();
	    sourceDL.stop();
	    sourceDL.close();
	  }
}