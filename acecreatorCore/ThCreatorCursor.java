// 
// @author: systemsepigenomics
// 

package acecreatorCore;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class ThCreatorCursor implements Runnable
{
    String imgPath;
    Thread aThread;
    boolean execute;
    JPanel curJP;
    boolean wait;
    public boolean refresh;
    
    public ThCreatorCursor(final String imgPath, final JPanel curJP) {
        this.refresh = true;
        this.imgPath = imgPath;
        this.curJP = curJP;
        this.wait = false;
        this.execute = false;
    }
    
    public ThCreatorCursor(final JPanel curJP) {
        this.refresh = true;
        this.curJP = curJP;
        this.wait = true;
        this.execute = false;
    }
    
    public void run() {
        int i = 0;
        try {
            while (this.execute) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                if (Thread.currentThread().getName().equals("waitCur")) {
                    Image cursor;
                    if (this.wait) {
                        cursor = defaultToolkit.getImage("images" + File.separator + "kin" + i + ".gif");
                        if (++i == 5) {
                            i = 0;
                        }
                    }
                    else {
                        cursor = defaultToolkit.getImage(this.imgPath);
                    }
                    final Cursor customCursor = defaultToolkit.createCustomCursor(cursor, new Point(16, 16), "cursor");
                    if (CreatorUtils.currentFrame != null) {
                        CreatorUtils.currentFrame.setCursor(customCursor);
                    }
                    this.curJP.setCursor(customCursor);
                    if (this.curJP.getParent() != null) {
                        this.curJP.getParent().setCursor(customCursor);
                    }
                    Thread.sleep(100L);
                }
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void initCursor() {
        try {
            final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            Image cursor;
            if (!this.wait) {
                cursor = defaultToolkit.getImage(this.imgPath);
            }
            else {
                cursor = defaultToolkit.getImage("images" + File.separator + "kin0.gif");
            }
            final Cursor customCursor = defaultToolkit.createCustomCursor(cursor, new Point(16, 16), "wait");
            if (CreatorUtils.currentFrame != null) {
                CreatorUtils.currentFrame.setCursor(customCursor);
            }
            this.curJP.setCursor(customCursor);
            if (this.curJP.getParent() != null) {
                this.curJP.getParent().setCursor(customCursor);
            }
        }
        catch (Exception ex) {
            System.out.println("PB with the CURSOR");
        }
    }
    
    public void launchThread() {
        this.execute = true;
        try {
            (this.aThread = new Thread(this)).setName("waitCur");
            this.aThread.start();
            Thread.sleep(50L);
        }
        catch (Exception ex) {
            System.out.println("Error when launching Thread for cursor : " + ex.toString());
        }
    }
    
    public boolean joinThread() {
        this.execute = false;
        try {
            this.aThread.join();
            if (CreatorUtils.currentFrame != null) {
                CreatorUtils.currentFrame.setCursor(Cursor.getPredefinedCursor(0));
            }
            this.curJP.setCursor(Cursor.getPredefinedCursor(0));
            if (this.curJP.getParent() != null) {
                this.curJP.getParent().setCursor(Cursor.getPredefinedCursor(0));
            }
            return this.refresh = true;
        }
        catch (Exception ex) {
            System.out.println("Impossible to stop the thread...");
            if (CreatorUtils.currentFrame != null) {
                CreatorUtils.currentFrame.setCursor(Cursor.getPredefinedCursor(0));
            }
            this.curJP.setCursor(Cursor.getPredefinedCursor(0));
            if (this.curJP.getParent() != null) {
                this.curJP.getParent().setCursor(Cursor.getPredefinedCursor(0));
            }
            return false;
        }
    }
}
