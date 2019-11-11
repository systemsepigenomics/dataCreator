// 
// @author: systemsepigenomics
// 

package acecreatorStyle;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.JFrame;

public class SplitFrame1311 extends JFrame
{
    private int HSplit1;
    private int HSplit2;
    private int HSplit3;
    private int VSplit1;
    private int VSplit2;
    private Container topPane;
    private Container leftPane;
    private Container centerPane;
    private Container rightPane;
    private Container middlePane;
    private Container bottomPane;
    
    public SplitFrame1311(final String title) {
        super(title);
        this.topPane = null;
        this.leftPane = null;
        this.centerPane = null;
        this.rightPane = null;
        this.middlePane = null;
        this.bottomPane = null;
        this.getContentPane().setLayout(null);
        this.HSplit1 = 60;
        this.HSplit2 = 170;
        this.HSplit3 = 600;
        this.VSplit1 = 200;
        this.VSplit2 = 400;
    }
    
    public SplitFrame1311(final String title, final int width, final int height) {
        super(title);
        this.topPane = null;
        this.leftPane = null;
        this.centerPane = null;
        this.rightPane = null;
        this.middlePane = null;
        this.bottomPane = null;
        this.getContentPane().setLayout(null);
        this.setSize(width, height);
        this.HSplit1 = 60;
        this.HSplit2 = 170;
        this.HSplit3 = height - 120;
        this.VSplit1 = width * 2 / 5;
        this.VSplit2 = width * 3 / 5;
    }
    
    public void setHSplit1(final int hSplit1) {
        this.HSplit1 = hSplit1;
    }
    
    public void setHSplit2(final int hSplit2) {
        this.HSplit2 = hSplit2;
    }
    
    public void setHSplit3(final int hSplit3) {
        this.HSplit3 = hSplit3;
    }
    
    public void setVSplit1(final int vSplit1) {
        this.VSplit1 = vSplit1;
    }
    
    public void setVSplit2(final int vSplit2) {
        this.VSplit2 = vSplit2;
    }
    
    public int getWidth() {
        return this.getContentPane().getSize().width;
    }
    
    public int getHeight() {
        return this.getContentPane().getSize().height;
    }
    
    public void adjustPanels() {
        final Dimension size = this.getContentPane().getSize();
        final int width = size.width;
        final int height = size.height;
        this.VSplit1 = width * 2 / 5;
        this.VSplit2 = width * 3 / 5;
        this.HSplit3 = height - 120;
        if (this.topPane != null) {
            this.topPane.setBounds(0, 0, width, this.HSplit1);
        }
        if (this.leftPane != null) {
            this.leftPane.setBounds(0, this.HSplit1, this.VSplit1, this.HSplit2 - this.HSplit1);
        }
        if (this.centerPane != null) {
            this.centerPane.setBounds(this.VSplit1, this.HSplit1, this.VSplit2 - this.VSplit1, this.HSplit2 - this.HSplit1);
        }
        if (this.rightPane != null) {
            this.rightPane.setBounds(this.VSplit2, this.HSplit1, width - this.VSplit2, this.HSplit2 - this.HSplit1);
        }
        if (this.middlePane != null) {
            this.middlePane.setBounds(0, this.HSplit2, width, this.HSplit3 - this.HSplit2);
        }
        if (this.bottomPane != null) {
            this.bottomPane.setBounds(0, this.HSplit3, width, height - this.HSplit3);
        }
    }
    
    public void setTopPane(final Container container) {
        if (this.topPane != null) {
            this.getContentPane().remove(this.topPane);
        }
        this.getContentPane().add(container);
        (this.topPane = container).setBounds(0, 0, this.getContentPane().getSize().width, this.HSplit1);
    }
    
    public void setLeftPane(final Container container) {
        if (this.leftPane != null) {
            this.getContentPane().remove(this.leftPane);
        }
        this.getContentPane().add(container);
        (this.leftPane = container).setBounds(0, this.HSplit1, this.VSplit1, this.HSplit2 - this.HSplit1);
    }
    
    public void setCenterPane(final Container container) {
        if (this.centerPane != null) {
            this.getContentPane().remove(this.centerPane);
        }
        this.getContentPane().add(container);
        (this.centerPane = container).setBounds(this.VSplit1, this.HSplit1, this.VSplit2 - this.VSplit1, this.HSplit2 - this.HSplit1);
    }
    
    public void setRightPane(final Container container) {
        if (this.rightPane != null) {
            this.getContentPane().remove(this.rightPane);
        }
        this.getContentPane().add(container);
        (this.rightPane = container).setBounds(this.VSplit2, this.HSplit1, this.getContentPane().getSize().width - this.VSplit2, this.HSplit2 - this.HSplit1);
    }
    
    public void setMiddlePane(final Container container) {
        if (this.middlePane != null) {
            this.getContentPane().remove(this.middlePane);
        }
        this.getContentPane().add(container);
        this.middlePane = container;
        final Dimension size = this.getContentPane().getSize();
        final int width = size.width;
        final int width2 = size.width;
        this.middlePane.setBounds(0, this.HSplit2, width, this.HSplit3 - this.HSplit2);
    }
    
    public void setBottomPane(final Container container) {
        if (this.bottomPane != null) {
            this.getContentPane().remove(this.bottomPane);
        }
        this.getContentPane().add(container);
        this.bottomPane = container;
        final Dimension size = this.getContentPane().getSize();
        this.bottomPane.setBounds(0, this.HSplit3, size.width, size.width - this.HSplit3);
    }
    
    public Container getTopPane() {
        return this.topPane;
    }
    
    public Container getLeftPane() {
        return this.leftPane;
    }
    
    public Container getCenterPane() {
        return this.centerPane;
    }
    
    public Container getRightPane() {
        return this.rightPane;
    }
    
    public Container getBottomPane() {
        return this.bottomPane;
    }
    
    public Container getMiddlePane() {
        return this.middlePane;
    }
}
