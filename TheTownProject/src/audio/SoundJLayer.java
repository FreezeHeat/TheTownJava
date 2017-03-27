package audio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

/**
 * MP3 Player class
 * <p> Plays mp3's on a {@code Thread}
 * <ul>
 * <li> * Pause / Play function </li>
 * <li> * Volume control (Mute as well) </li>
 * </ul>
 * @author Ben Gilad and Asaf Yeshayahu
 * @version %I%
 * @since 1.0
 */
public class SoundJLayer extends PlaybackListener implements Runnable
{
    /**
     * File path to the mp3
     */
    private final String filePath;
    
    /**
     * The mp3 player component used (CREDIT TO JAVAZOOM!)
     * @see javazoom.jl.player.advanced.AdvancedPlayer
     */
    private AdvancedPlayer player;
    
    /**
     * {@code Thread} used for playing the mp3
     */
    private Thread playerThread;    
    
    /**
     * Total frames of bytes inside the mp3 stream, used for play/pause
     * @see #play() 
     * @see #pause()
     */
    private long totalFrames;
    
    /**
     * The remaining frames of the mp3 stream, used for play/pause
     * @see #play() 
     * @see #pause()
     */
    private long pausedOnFrame;
    
    /**
     * Used with {@code filePath} to create a URL for the mp3 file
     * @see java.net.URL
     */
    private URL url;
    
    /**
     * {@code InputStream} used as a buffer for the mp3 file, in order to play it
     * @see java.io.InputStream
     */
    private InputStream stream;
    
    /**
     * Determines whether the player is paused or not
     * @see audio.SoundJLayer The player
     */
    public static boolean isPaused = false;
    
    /**
     * Determines whether the player is muted or not
     * @see audio.SoundJLayer The player
     */
    public static boolean isMuted = false;
    
    /**
     * Determines the player's volume
     * @see audio.SoundJLayer The player
     */
    public static int volumeLevel = 100;

    /**
     * Constructor for {@code SoundJLayer}
     * <p> Initializes the file path for layer operations
     */
    public SoundJLayer()
    {
        this.filePath = "src\\audio\\bgMusic.mp3";
    }

    /**
     * Plays the mp3 file with the use of {@code Thread}
     */
    public void play()
    {
        try
        {
            // set file path
            String urlAsString = 
                "file:///" 
                + new java.io.File(".").getCanonicalPath() 
                + "/" 
                + this.filePath;
            
            //set file URL and open a stream
            url = new URL(urlAsString);
            stream = url.openStream();
            
            //hold how many frames are in the mp3 total
            totalFrames = stream.available();
            
            this.player = new AdvancedPlayer
            (
                stream,
                javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice()
            );
            
            this.player.setPlayBackListener(this);
            this.playerThread = new Thread(this, "AudioPlayerThread");
            
            System.out.println("Starting Player");
            
            //set unpaused and start the Thread
            isPaused = false;
            this.playerThread.start();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    // PlaybackListener members

    /**
     * Event in case the mp3 has finished playing
     * <p>Used for looping</p>
     * @param playbackEvent The event when a player finishes to play
     */
    public void playbackFinished(PlaybackEvent playbackEvent)
    {
        try {
            System.out.println("playbackEnded()");
            System.out.println((int)pausedOnFrame);
            
            //loop again if the mp3 finished playing
            if(isPaused != true){
                pausedOnFrame = 0;
                play();
            }
            else{
                playerThread.stop();
                playerThread = null;
                stream.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(SoundJLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    /**
     * Pause the player
     * <p>Termination of thread, and calculating the current frame for resume</p>
     */
    public  void pause(){
        try {
            isPaused = true;
            pausedOnFrame = stream.available();
            player.stop();
        } catch (IOException ex) {
            Logger.getLogger(SoundJLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This functions set's the volume of the system's speakers
     * @param gain  the volume gain to be set (between 0.0 to 1.0)
     */
    public static void setGain(float gain)  
    {          
        try {  
        Mixer.Info[] infos = AudioSystem.getMixerInfo();    
        for (Mixer.Info info: infos)    
        {    
           Mixer mixer = AudioSystem.getMixer(info);  
           if (mixer.isLineSupported(Port.Info.SPEAKER))    
           {    
              Port port = (Port)mixer.getLine(Port.Info.SPEAKER);    
              port.open();    
              if (port.isControlSupported(FloatControl.Type.VOLUME))    
              {    
                 FloatControl volume =  (FloatControl)port.getControl(FloatControl.Type.VOLUME);                    
                 volume.setValue(gain);  
              }    
              port.close();    
           }    
         }    
        } catch (Exception e) {   

        }  
    } 
    // Runnable members

    /**
     * (Overridden)run() method for mp3 player
     */
    @Override
    public void run()
    {
        try
        {
            System.out.println(pausedOnFrame + " " +  totalFrames);
            if(pausedOnFrame != 0){
                stream.skip(totalFrames - pausedOnFrame);
            }
            player.play();
        } catch (IOException ex) {
            Logger.getLogger(SoundJLayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JavaLayerException ex) {
            Logger.getLogger(SoundJLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}