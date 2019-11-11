// 
// @author: systemsepigenomics
// 

package acecreator;

import acemapCore.utils;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Font;
import javax.swing.JPanel;

public class GUIBottom extends JPanel
{
    private double[] parameters;
    final String[] parametersTitles;
    Font bottomFont;
    Font bottomTitleFont;
    int cellWidth;
    
    public GUIBottom() {
        super(null);
        this.parametersTitles = new String[] { "weight", "mean", "var", "x0", "weight", "mean", "var", "x0", "x_offset", "slope", "x0", "mean", "var", "y_offset", "scale", "x_offset", "slope", "y_offset", "scale", "x_offset", "slope" };
        this.cellWidth = 50;
        this.setBackground(Color.white);
        this.bottomFont = new Font("Verdana", 0, 10);
        this.bottomTitleFont = new Font("Arial Bold", 0, 12);
        this.paint(this.getGraphics());
    }
    
    public void setParameters(final double[] parameters) {
        this.parameters = parameters;
        this.paint(this.getGraphics());
    }
    
    public void paintComponent(final Graphics graphics) {
        final int width = this.getSize().width;
        final int height = this.getSize().height;
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, width, height);
        if (this.parameters != null) {
            this.drawParameters(graphics);
        }
    }
    
    private void drawParameters(final Graphics graphics) {
        final int width = this.getSize().width;
        final int height = this.getSize().height;
        final int n = height / 9;
        final int n2 = width / 2 - 325;
        this.getFontMetrics(this.getFont()).getAscent();
        graphics.setColor(new Color(90, 90, 90));
        graphics.fill3DRect(n2 - n + 3, 4, 2, height - 5 - 3, true);
        graphics.fill3DRect(n2 - n + 3, 3, width - (n2 - 20) * 2, 2, true);
        graphics.setColor(Color.black);
        graphics.setFont(this.bottomTitleFont);
        graphics.drawString("AB1700 Input Data Parameter", n2 + 450 + 17, (int)(2.5 * n));
        graphics.drawString("Estimates", n2 + 450 + 17 + 57, (int)(3.5 * n));
        graphics.setFont(this.bottomFont);
        graphics.drawLine(n2, n, 8 * this.cellWidth + n2, n);
        graphics.drawLine(n2, 2 * n, 8 * this.cellWidth + n2, 2 * n);
        graphics.drawLine(n2, 3 * n, 8 * this.cellWidth + n2, 3 * n);
        graphics.drawLine(n2, 4 * n, 8 * this.cellWidth + n2, 4 * n);
        graphics.drawLine(n2, 5 * n, 13 * this.cellWidth + n2, 5 * n);
        graphics.drawLine(n2, 6 * n, 13 * this.cellWidth + n2, 6 * n);
        graphics.drawLine(n2, 7 * n, 13 * this.cellWidth + n2, 7 * n);
        graphics.drawLine(n2, 8 * n, 13 * this.cellWidth + n2, 8 * n);
        graphics.drawLine(n2, n, n2, 4 * n);
        graphics.drawLine(n2, 5 * n, n2, 8 * n);
        graphics.drawString("signal distribution 1", n2 + 2 * this.cellWidth - utils.stdWidth("signal distribution 1") / 2, 2 * n - 3);
        graphics.drawString("signal distribution 2", n2 + 6 * this.cellWidth - utils.stdWidth("signal distribution 2") / 2, 2 * n - 3);
        graphics.drawString("var blending", n2 + this.cellWidth - utils.stdWidth("var blending") / 2, 6 * n - 3);
        graphics.drawString("var distribution 1", (int)(n2 + 3.5 * this.cellWidth) - utils.stdWidth("var distribution 1") / 2, 6 * n - 3);
        graphics.drawString("var distribution 2 - mean", n2 + 7 * this.cellWidth - utils.stdWidth("var distribution 2 - mean") / 2, 6 * n - 3);
        graphics.drawString("var distribution 2 - var", n2 + 11 * this.cellWidth - utils.stdWidth("var distribution 2 - var") / 2, 6 * n - 3);
        for (int i = 0; i < 8; ++i) {
            graphics.setColor(Color.black);
            final String decs = utils.getDecs(this.parameters[i], 4, false);
            graphics.drawString(this.parametersTitles[i], n2 + i * this.cellWidth + 24 - utils.stdWidth(this.parametersTitles[i]) / 2, 3 * n - 3);
            if (i != 3 && i != 7) {
                graphics.drawLine(n2 + (i + 1) * this.cellWidth, 2 * n, n2 + (i + 1) * this.cellWidth, 4 * n);
            }
            else {
                graphics.drawLine(n2 + (i + 1) * this.cellWidth, n, n2 + (i + 1) * this.cellWidth, 4 * n);
            }
            graphics.setColor(Color.gray);
            graphics.drawString(utils.getDecs(this.parameters[i], 4, false), n2 + i * this.cellWidth + 24 - utils.stdWidth(decs) / 2, 4 * n - 3);
        }
        for (int j = 8; j < this.parameters.length; ++j) {
            graphics.setColor(Color.black);
            final String decs2 = utils.getDecs(this.parameters[j], 4, false);
            graphics.drawString(this.parametersTitles[j], n2 + (j - 8) * this.cellWidth + 24 - utils.stdWidth(this.parametersTitles[j]) / 2, 7 * n - 3);
            if (j != 9 && j != 12 && j != 16 && j != this.parameters.length - 1) {
                graphics.drawLine(n2 + (j - 8 + 1) * this.cellWidth, 6 * n, n2 + (j - 8 + 1) * this.cellWidth, 8 * n);
            }
            else {
                graphics.drawLine(n2 + (j - 8 + 1) * this.cellWidth, 5 * n, n2 + (j - 8 + 1) * this.cellWidth, 8 * n);
            }
            graphics.setColor(Color.gray);
            graphics.drawString(utils.getDecs(this.parameters[j], 4, false), n2 + (j - 8) * this.cellWidth + 24 - utils.stdWidth(decs2) / 2, 8 * n - 3);
        }
    }
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, width, height);
    }
}
