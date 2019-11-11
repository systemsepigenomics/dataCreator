// 
// @author: systemsepigenomics
// 

package acecreator;

import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import acemapCore.utils;
import java.awt.Component;
import acemapCore.mTable;
import acecreatorCore.CompositeMfCreator;
import acecreatorCore.CreatorUtils;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.io.File;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.LayoutManager;
import aceStyle.ace_button;
import aceStyle.ace_textfield;
import aceStyle.ace_label;
import java.awt.Image;
import maModel.denspan;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class GUIMiddle extends JPanel implements ActionListener, MouseListener
{
    private denspan dspIn;
    private denspan activeDenspan;
    Image arrowUp;
    Image arrowDown;
    final ace_label inputLab;
    public final ace_textfield inputTxt;
    final ace_button inputBut;
    int denspanNb;
    int denspanSel;
    float sliderPos;
    private boolean progression;
    private StringBuffer progStringBuf;
    
    public GUIMiddle() {
        super(null);
        this.dspIn = null;
        this.activeDenspan = null;
        this.inputLab = new ace_label("Input:");
        this.inputTxt = new ace_textfield("");
        this.inputBut = new ace_button("browse");
        this.denspanNb = 0;
        this.denspanSel = 0;
        this.sliderPos = 0.0f;
        this.progression = false;
        this.setBackground(Color.white);
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        this.arrowUp = defaultToolkit.getImage("images" + File.separator + "arrUp.jpg");
        this.arrowDown = defaultToolkit.getImage("images" + File.separator + "arrDown.jpg");
        this.prepareImage(this.arrowUp, null);
        this.prepareImage(this.arrowDown, null);
        this.progStringBuf = new StringBuffer();
        this.addMouseListener(this);
    }
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, width, height);
        if (this.dspIn != null) {
            this.dspIn.setBounds(25, 10, 2 * width / 5 - 25, height - 10);
        }
        if (this.activeDenspan != null) {
            this.activeDenspan.setBounds(3 * width / 5, 10, 2 * width / 5 - 25, height - 10);
            if (this.denspanNb > 1) {
                final Graphics graphics = this.getGraphics();
                graphics.drawImage(this.arrowUp, 3 * width / 5 - 20, this.dspIn.inp.getSize().height / 2 - 12 - 10 + 10, 10, 10, Color.white, null);
                graphics.drawImage(this.arrowDown, 3 * width / 5 - 20, this.dspIn.inp.getSize().height / 2 + 12 + 10, 10, 10, Color.white, null);
                graphics.setColor(Color.gray);
                if (this.denspanSel < 10) {
                    graphics.drawString("0" + Integer.toString(this.denspanSel), 3 * width / 5 - 20 - 1, this.dspIn.inp.getSize().height / 2 + 10 + 4);
                }
                else {
                    graphics.drawString(Integer.toString(this.denspanSel), 3 * width / 5 - 20 - 1, this.dspIn.inp.getSize().height / 2 + 10 + 4);
                }
            }
        }
    }
    
    public void setDensPanIn(final denspan dspIn) {
        this.dspIn = dspIn;
        this.paint(this.getGraphics());
    }
    
    public void setDefaultDensPan() {
        if (CreatorUtils.currentSynthPaths != null && CreatorUtils.currentSynthPaths[0] != null) {
            this.denspanNb = CreatorUtils.currentSynthPaths.length;
            this.denspanSel = 0;
            final CompositeMfCreator compositeMfCreator = new CompositeMfCreator();
            compositeMfCreator.load(new File(CreatorUtils.currentSynthPaths[0]), true, true, true);
            this.setActiveDensPanOut(CreatorUtils.createDensPan(compositeMfCreator, null));
        }
    }
    
    private void setActiveDensPanOut(final denspan activeDenspan) {
        if (activeDenspan != null) {
            final int width = this.getSize().width;
            final int height = this.getSize().height;
            this.activeDenspan = activeDenspan;
        }
        this.paint(this.getGraphics());
    }
    
    public void paintComponent(final Graphics graphics) {
        this.removeAll();
        final int width = this.getSize().width;
        final int height = this.getSize().height;
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, width, height);
        if (this.dspIn != null) {
            this.add((Component)this.dspIn);
        }
        if (!this.progression) {
            if (this.activeDenspan != null) {
                this.add((Component)this.activeDenspan);
                this.activeDenspan.setBounds(3 * width / 5, 10, 2 * width / 5 - 25, height - 10);
                if (this.denspanNb > 1) {
                    graphics.drawImage(this.arrowUp, 3 * width / 5 - 20, this.dspIn.inp.getSize().height / 2 - 12 - 10 + 10, 10, 10, Color.white, null);
                    graphics.drawImage(this.arrowDown, 3 * width / 5 - 20, this.dspIn.inp.getSize().height / 2 + 12 + 10, 10, 10, Color.white, null);
                    graphics.setColor(Color.gray);
                    if (this.denspanSel < 10) {
                        graphics.drawString("0" + Integer.toString(this.denspanSel), 3 * width / 5 - 20 - 1, this.dspIn.inp.getSize().height / 2 + 10 + 4);
                    }
                    else {
                        graphics.drawString(Integer.toString(this.denspanSel), 3 * width / 5 - 20 - 1, this.dspIn.inp.getSize().height / 2 + 10 + 4);
                    }
                }
            }
        }
        else {
            graphics.setColor(Color.gray);
            final int ascent = this.getFontMetrics(this.getFont()).getAscent();
            final String[] split = this.progStringBuf.toString().split(utils.lineend);
            int n = 20;
            if (split.length >= 1) {
                for (int i = 15 * ((split.length - 1) / 15); i < split.length; ++i) {
                    graphics.drawString(split[i], 3 * width / 5, n);
                    n = n + ascent + 3;
                }
            }
        }
    }
    
    public void actionPerformed(final ActionEvent actionEvent) {
        actionEvent.getSource();
    }
    
    public void SliderDChanged(final Object o) {
    }
    
    public void setProgression(final boolean progression) {
        this.progression = progression;
    }
    
    public void setProgStringBuf(final StringBuffer progStringBuf) {
        this.progStringBuf = progStringBuf;
    }
    
    public void mouseClicked(final MouseEvent mouseEvent) {
        if (this.denspanNb == 0) {
            return;
        }
        final int width = this.getSize().width;
        final int height = this.getSize().height;
        final int x = mouseEvent.getX();
        final int y = mouseEvent.getY();
        System.out.println("X : " + x + "\tY : " + y);
        if (x >= 3 * width / 5 - 20 && x <= 3 * width / 5 - 20 + 10 && y >= this.dspIn.inp.getSize().height / 2 - 12 - 10 + 10 && y <= this.dspIn.inp.getSize().height / 2 - 12 - 10 + 10 + 10) {
            System.out.println("UP BUTTON PRESSED");
            if (this.denspanSel == 0) {
                return;
            }
            --this.denspanSel;
            final CompositeMfCreator compositeMfCreator = new CompositeMfCreator();
            compositeMfCreator.load(new File(CreatorUtils.currentSynthPaths[this.denspanSel]), true, true, true);
            this.setActiveDensPanOut(CreatorUtils.createDensPan(compositeMfCreator, null));
        }
        else if (x >= 3 * width / 5 - 20 && x <= 3 * width / 5 - 20 + 10 && y >= this.dspIn.inp.getSize().height / 2 + 12 + 10 && y <= this.dspIn.inp.getSize().height / 2 + 12 + 10 + 10) {
            System.out.println("DOWN BUTTON PRESSED");
            if (this.denspanSel == this.denspanNb - 1) {
                return;
            }
            ++this.denspanSel;
            final CompositeMfCreator compositeMfCreator2 = new CompositeMfCreator();
            compositeMfCreator2.load(new File(CreatorUtils.currentSynthPaths[this.denspanSel]), true, true, true);
            this.setActiveDensPanOut(CreatorUtils.createDensPan(compositeMfCreator2, null));
        }
    }
    
    public void mouseEntered(final MouseEvent mouseEvent) {
    }
    
    public void mouseExited(final MouseEvent mouseEvent) {
    }
    
    public void mousePressed(final MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(final MouseEvent mouseEvent) {
    }
}
