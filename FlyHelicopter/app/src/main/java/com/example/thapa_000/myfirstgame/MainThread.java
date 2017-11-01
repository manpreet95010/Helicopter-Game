package com.example.thapa_000.myfirstgame;

import android.graphics.Canvas;
import android.provider.Settings;
import android.view.SurfaceHolder;

/**
 * Created by thapa_000 on 03-10-2017.
 */

public class MainThread extends Thread
{
    private int FPS=30;
    private double averageFPS;
    private SurfaceHolder surfaceholder;
    private GamePanel gamepanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceholder,GamePanel gamepanel)
    {
        super();
        this.surfaceholder = surfaceholder;
        this.gamepanel = gamepanel;
    }
    @Override
    public void run()
    {
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        long frameCount = 0;
        long targetTime = 1000/FPS;

        while(running)
        {
            startTime = System.nanoTime();
            canvas = null;
            //use try to lock the canvas for pixel editing
            try
            {
                canvas = this.surfaceholder.lockCanvas();
                synchronized (surfaceholder)
                {
                    // these two functions are the main functions which is going to update and draw your game once again
                    this.gamepanel.update();
                    this.gamepanel.draw(canvas);
                }

            } catch(Exception e){}
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try
            {
                this.sleep(waitTime);
            }
            catch(Exception e){}
            finally {
                if(canvas != null)
                {
                    try
                    {
                        surfaceholder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){e.printStackTrace();}
                }
            }
            totalTime += System.nanoTime() - startTime;
            frameCount++ ;
            if(frameCount == FPS)
            {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }

        }
    }
    public void setRunning(boolean b)
    {
        running = b;
    }
}
