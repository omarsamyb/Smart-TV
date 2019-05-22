import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JRootPane;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class Process extends Thread
{
	static JFrame x;
	MediaPlayer mediaPlayer=null;
	static volatile boolean isRunning = true;
	private String type;
	private String pID;
	private int memorySize;
	private CurrentState state;
	public Process(String pID, String type, int memorySize) 
	{
		this.state=CurrentState.NEW;
		TV.updateLog(pID + " state is "+this.state);
		this.pID=pID;
		this.type=type;
		this.memorySize=memorySize;
		setState(CurrentState.READY);
	}
	public void run() 
	{
		isRunning=true;
		JFXPanel jfxPanel =new JFXPanel();
		jfxPanel.setPreferredSize(new Dimension(1280, 720));
    	Platform.setImplicitExit(false);
	    Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	Group  root  =  new  Group();
		        Scene  scene  =  new  Scene(root, javafx.scene.paint.Color.ALICEBLUE);
		        
		        String bip = "src/"+type+"/"+pID;
				Media hit = new Media(new File(bip).toURI().toString());
				mediaPlayer = new MediaPlayer(hit);
				MediaView view = new MediaView(mediaPlayer);
			    mediaPlayer.setAutoPlay(true);
				mediaPlayer.setOnEndOfMedia(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						mediaPlayer.seek(Duration.ZERO);
					}
				});
		        root.getChildren().add(view);
		        jfxPanel.setScene(scene);
            }
       });
	    x = new JFrame(pID);
	    x.add(jfxPanel);
	    x.setPreferredSize(new Dimension(1280, 720));
	    x.setBounds(0, 0, 1280, 720);
		x.setAlwaysOnTop(true);
		x.setUndecorated(true);
		x.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
		x.setDefaultCloseOperation(0);
	    x.setVisible(true);
	    setState(CurrentState.RUNNING);
	}
	public void Block()
	{
		mediaPlayer.dispose();
		x.dispose();
		setState(CurrentState.BLOCKED);
	}
	public void kill()
	{
		mediaPlayer.dispose();
		x.dispose();
		setState(CurrentState.FINISHED);
	}
	public String getpID() {
		return pID;
	}
	public String getType() {
		return type;
	}
	public void setpID(String pID) {
		this.pID = pID;
	}
	public void setState(CurrentState state) {
		this.state = state;
		TV.updateLog(pID + " state is "+this.state);
	}
	public int getMemorySize() {
		return memorySize;
	}
}
