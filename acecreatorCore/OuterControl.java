// 
// @author: systemsepigenomics
// 

package acecreatorCore;

import java.awt.event.WindowEvent;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Color;
import aceStyle.ace_panel;
import acemapCore.utils;
import java.io.File;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import acecreator.GUIBottom;
import acecreator.GUIOut;
import acecreator.GUICreate;
import acecreator.GUIIn;
import acecreator.GUITop;
import acecreatorStyle.SplitFrame1311;
import java.awt.event.WindowListener;
import java.awt.event.ComponentListener;
import java.awt.event.ActionListener;

public class OuterControl implements ActionListener, ComponentListener, WindowListener
{
    final SplitFrame1311 mainFrame;
    final GUITop guiTop;
    final GUIIn guiInput;
    final GUICreate guiCreate;
    final GUIOut guiOutput;
    final GUIBottom guiBot;
    ImageIcon windowicon;
    
    public OuterControl() {
        this.mainFrame = new SplitFrame1311("ace.map creator 1.0 - synthetic data creator for AB1700", 800, 550);
        CreatorUtils.currentFrame = this.mainFrame;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        this.mainFrame.setDefaultCloseOperation(0);
        if (!new File("input").exists()) {
            new File("input").mkdir();
        }
        if (!new File("output").exists()) {
            new File("output").mkdir();
        }
        if (CreatorUtils.tempPath == null || !CreatorUtils.tempPath.exists()) {
            (CreatorUtils.tempPath = new File("temp")).mkdir();
            System.out.println("creating temp path " + CreatorUtils.tempPath.getAbsolutePath());
        }
        else {
            System.out.println("found valid temp path " + CreatorUtils.tempPath.getAbsolutePath());
            CreatorUtils.cleanTmp();
        }
        if (CreatorUtils.tempPath == null) {
            utils.message("temp path not properly set");
        }
        this.guiTop = new GUITop();
        this.guiInput = new GUIIn();
        this.guiCreate = new GUICreate();
        this.guiOutput = new GUIOut();
        this.guiBot = new GUIBottom();
        final ace_panel middlePane = new ace_panel();
        this.windowicon = new ImageIcon("images" + File.separator + "w_icon.jpg");
        this.mainFrame.setIconImage(this.windowicon.getImage());
        this.mainFrame.addComponentListener(this);
        this.mainFrame.addWindowListener(this);
        middlePane.setBackground(Color.white);
        this.mainFrame.setTopPane(this.guiTop);
        this.mainFrame.setLeftPane(this.guiInput);
        this.mainFrame.setCenterPane(this.guiCreate);
        this.mainFrame.setRightPane(this.guiOutput);
        this.mainFrame.setMiddlePane((Container)middlePane);
        this.mainFrame.setBottomPane(this.guiBot);
        CreatorUtils.init(CreatorUtils.currentFrame);
        this.mainFrame.pack();
        this.mainFrame.setSize(new Dimension(800, 550));
        this.mainFrame.setVisible(true);
        this.guiOutput.enableSave(false);
        System.out.println("initiation for class " + this.getClass().getName() + " accomplished");
    }
    
    public void actionPerformed(final ActionEvent actionEvent) {
        System.out.println("some action");
    }
    
    public void componentHidden(final ComponentEvent componentEvent) {
    }
    
    public void componentMoved(final ComponentEvent componentEvent) {
    }
    
    public void componentShown(final ComponentEvent componentEvent) {
    }
    
    public void componentResized(final ComponentEvent componentEvent) {
        this.mainFrame.setSize(800, 550);
        this.mainFrame.adjustPanels();
        this.guiCreate.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, new Color(220, 220, 220)));
    }
    
    public void windowActivated(final WindowEvent windowEvent) {
    }
    
    public void windowClosed(final WindowEvent windowEvent) {
    }
    
    public void windowDeactivated(final WindowEvent windowEvent) {
    }
    
    public void windowDeiconified(final WindowEvent windowEvent) {
    }
    
    public void windowIconified(final WindowEvent windowEvent) {
    }
    
    public void windowOpened(final WindowEvent windowEvent) {
    }
    
    public void windowClosing(final WindowEvent windowEvent) {
        CreatorUtils.cleanTmp();
        System.exit(0);
    }
}
