// 
// @author: systemsepigenomics
// 

package acecreatorStyle;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.awt.Component;
import javax.swing.JFileChooser;

public class CreatorFileChooser extends JFileChooser
{
    Component parent;
    public static final int OPEN_TXT_MA0_MA1 = 0;
    public static final int SAVE_TXT = 1;
    public static final int SAVE_MA0 = 2;
    public static final int SAVE_MA1 = 3;
    boolean save;
    
    public CreatorFileChooser(final Component parent, final int n) {
        this.parent = parent;
        this.save = false;
        switch (n) {
            case 0: {
                this.setFileSelectionMode(0);
                this.setMultiSelectionEnabled(false);
                this.setDialogTitle("select an input file");
                this.setFileFilter(new CreatorFilter(0));
                break;
            }
            case 1: {
                this.setFileSelectionMode(0);
                this.setMultiSelectionEnabled(false);
                this.setDialogTitle("save txt file as");
                this.setFileFilter(new CreatorFilter(1));
                this.save = true;
                break;
            }
            case 2: {
                this.setFileSelectionMode(0);
                this.setMultiSelectionEnabled(false);
                this.setDialogTitle("save ma0 file as");
                this.setFileFilter(new CreatorFilter(2));
                this.save = true;
                break;
            }
            case 3: {
                this.setFileSelectionMode(0);
                this.setMultiSelectionEnabled(false);
                this.setDialogTitle("save ma1 file as");
                this.setFileFilter(new CreatorFilter(3));
                this.save = true;
                break;
            }
            default: {
                this.setFileSelectionMode(0);
                this.setMultiSelectionEnabled(false);
                this.setDialogTitle("choose file");
                break;
            }
        }
    }
    
    public CreatorFileChooser() {
    }
    
    public File getFile() {
        int n;
        try {
            if (this.save) {
                n = this.showDialog(this.parent, "save");
            }
            else {
                n = this.showDialog(this.parent, "open");
            }
        }
        catch (Exception ex) {
            return null;
        }
        if (n == 0) {
            return this.getSelectedFile();
        }
        return null;
    }
    
    public File[] getFiles() {
        int n;
        try {
            if (this.save) {
                n = this.showDialog(this.parent, "save");
            }
            else {
                n = this.showDialog(this.parent, "open");
            }
        }
        catch (Exception ex) {
            return null;
        }
        if (n == 0) {
            return this.getSelectedFiles();
        }
        return null;
    }
}
