// 
// @author: systemsepigenomics
// 

package acecreatorStyle;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class CreatorFilter extends FileFilter
{
    final String[] exts;
    final String[] descs;
    static final int TXT_MA0_MA1 = 0;
    static final int TXT = 1;
    static final int MA0 = 2;
    static final int MA1 = 3;
    String ext;
    String des;
    int ECode;
    
    CreatorFilter(final int eCode) {
        this.exts = new String[] { "txt", "ma0", "ma1" };
        this.descs = new String[] { "txt/ma0/ma1 files", "*.txt", "*.ma0", "*.ma1" };
        this.ECode = eCode;
        this.des = this.descs[eCode];
    }
    
    public boolean accept(final File file) {
        if (file.isDirectory()) {
            return true;
        }
        final String name = file.getName();
        final int lastIndex = name.lastIndexOf(46);
        if (lastIndex > 0 && lastIndex < name.length() - 1) {
            final String lowerCase = name.substring(lastIndex + 1).toLowerCase();
            switch (this.ECode) {
                case 0: {
                    if (lowerCase.compareTo(this.exts[0]) == 0 || lowerCase.compareTo(this.exts[1]) == 0 || lowerCase.compareTo(this.exts[2]) == 0) {
                        return true;
                    }
                    break;
                }
                case 1: {
                    if (lowerCase.compareTo(this.exts[0]) == 0) {
                        return true;
                    }
                }
                case 2: {
                    if (lowerCase.compareTo(this.exts[1]) == 0) {
                        return true;
                    }
                }
                case 3: {
                    if (lowerCase.compareTo(this.exts[2]) == 0) {
                        return true;
                    }
                    break;
                }
                default: {
                    return true;
                }
            }
        }
        return false;
    }
    
    public String getDescription() {
        return this.des;
    }
}
