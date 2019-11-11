// 
// @author: systemsepigenomics
// 

package acecreator;

import acemapCore.BareBonesBrowserLaunch;
import java.awt.event.ActionEvent;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Component;
import javax.swing.Icon;
import java.awt.Color;
import java.io.File;
import java.awt.LayoutManager;
import aceStyle.ace_button;
import aceStyle.ace_label;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class GUITop extends JPanel implements ActionListener
{
    final ImageIcon logo;
    ace_label logoLabel;
    final ace_label infoLab1;
    final ace_button infoBut;
    final ace_label infoLab2;
    final ace_label helpLab1;
    final ace_button helpBut;
    final ace_label helpLab2;
    int lineAscent;
    
    public GUITop() {
        super(null);
        this.logo = new ImageIcon("images" + File.separator + "title2.jpg");
        this.infoLab1 = new ace_label("|");
        this.infoBut = new ace_button("info");
        this.infoLab2 = new ace_label("|");
        this.helpLab1 = new ace_label("|");
        this.helpBut = new ace_button("help");
        this.helpLab2 = new ace_label("|");
        this.lineAscent = this.getFontMetrics(this.getFont()).getAscent();
        this.setBackground(Color.white);
        this.logoLabel = new ace_label((Icon)this.logo);
        this.add((Component)this.infoLab1);
        this.add((Component)this.infoBut);
        this.add((Component)this.infoLab2);
        this.add((Component)this.helpLab1);
        this.add((Component)this.helpBut);
        this.add((Component)this.helpLab2);
        this.add((Component)this.logoLabel);
        this.infoBut.addActionListener((ActionListener)this);
        this.helpBut.addActionListener((ActionListener)this);
    }
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, width, height);
        this.logoLabel.setBounds(width / 2 - this.logo.getIconWidth() / 2, 0, this.logo.getIconWidth(), height);
        this.logoLabel.setBorder((Border)BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(220, 220, 220)));
        this.infoLab1.setBounds(16, height / 2 - this.lineAscent, 5, 20);
        this.infoBut.setBounds(20, height / 2 - this.lineAscent, 25, 20);
        this.infoLab2.setBounds(47, height / 2 - this.lineAscent, 5, 20);
        this.helpLab1.setBounds(width - 57, height / 2 - this.lineAscent, 5, 20);
        this.helpBut.setBounds(width - 50, height / 2 - this.lineAscent, 25, 20);
        this.helpLab2.setBounds(width - 23, height / 2 - this.lineAscent, 5, 20);
    }
    
    public void actionPerformed(final ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        if (source == this.infoBut) {
            BareBonesBrowserLaunch.openURL("file:///" + new File("info" + File.separator + "acemapCreator_info.html").getAbsolutePath());
        }
        else if (source == this.helpBut) {
            BareBonesBrowserLaunch.openURL("file:///" + new File("info" + File.separator + "acemapCreatorUsersGuide.pdf").getAbsolutePath());
        }
    }
}
