// 
// @author: systemsepigenomics
// 

package acecreatorCore;

import java.io.File;
import acemapCore.AbstractCompositeMf;

public class CompositeMfCreator extends AbstractCompositeMf
{
    public CompositeMfCreator() {
    }
    
    public CompositeMfCreator(final File z_file) {
        this.z_file = z_file;
    }
    
    public File getTemp() {
        return CreatorUtils.getTemp();
    }
}
