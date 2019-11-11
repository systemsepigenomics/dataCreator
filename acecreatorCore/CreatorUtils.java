// 
// @author: systemsepigenomics
// 

package acecreatorCore;

import java.awt.Color;
import acemapCore.utils;
import maModel.plot;
import acemapCore.AbstractCompositeMf;
import aceStyle.ace_progress;
import maModel.denspan;
import java.awt.Frame;
import acemapCore.mTable;
import java.io.File;

public class CreatorUtils
{
    public static File currentInFile;
    public static File currentOutFile;
    public static CompositeMfCreator currentInCompMf;
    public static CompositeMfCreator[] currentOutCompMf;
    public static String[] currentSynthPaths;
    public static mTable currentMTable;
    public static Frame currentFrame;
    public static File tempPath;
    public static double[] parameters;
    private static int tempCnt;
    public static final String inputPath = "input";
    public static final String outputPath = "output";
    public static final String imagePath = "images";
    public static final String versionString = "1.0";
    public static final String httpLink = "http://www.iri.cnrs.fr/seg";
    
    private CreatorUtils() {
    }
    
    public static denspan createDensPan(final CompositeMfCreator compositeMfCreator, final mTable mTable) {
        mTable mTab;
        if (mTable != null) {
            mTab = mTable;
        }
        else {
            mTab = new mTable(compositeMfCreator.F_ma, (ace_progress)null, false, (AbstractCompositeMf)compositeMfCreator, true);
        }
        final denspan denspan = new denspan(utils.filterGauss(plot.sample(mTab, -4.0, 4.0, -4.0, 4.0, 160), 9));
        denspan.inp.mTab = mTab;
        denspan.setBackground(Color.white);
        return denspan;
    }
    
    public static File getTemp() {
        int tempCnt;
        File file;
        for (tempCnt = CreatorUtils.tempCnt, file = new File(CreatorUtils.tempPath, "temp" + tempCnt + ".txt"); file.exists(); file = new File(CreatorUtils.tempPath, "temp" + tempCnt + ".txt")) {
            ++tempCnt;
        }
        CreatorUtils.tempCnt = tempCnt + 1;
        return file;
    }
    
    public static void init(final Frame frame) {
        utils.init(frame);
        if (CreatorUtils.tempPath == null || !CreatorUtils.tempPath.exists()) {
            CreatorUtils.tempPath.mkdir();
            System.out.println("creating temp path " + CreatorUtils.tempPath.getAbsolutePath());
        }
        else {
            System.out.println("found valid temp path " + CreatorUtils.tempPath.getAbsolutePath());
            cleanTmp();
        }
        if (CreatorUtils.tempPath == null) {
            utils.message("temp path not properly set");
        }
    }
    
    public static void cleanTmp() {
        if (CreatorUtils.tempPath == null) {
            return;
        }
        final File[] listFiles = CreatorUtils.tempPath.listFiles();
        if (listFiles == null) {
            return;
        }
        for (int i = 0; i < listFiles.length; ++i) {
            try {
                listFiles[i].delete();
            }
            catch (Exception ex) {
                System.out.println("error deleting " + listFiles[i].getName() + " : " + ex.toString());
            }
        }
    }
    
    static {
        CreatorUtils.tempPath = new File("temp");
        CreatorUtils.parameters = new double[21];
        CreatorUtils.tempCnt = 1;
    }
}
