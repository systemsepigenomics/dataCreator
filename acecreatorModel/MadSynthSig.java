// 
// @author: systemsepigenomics
// 

package acecreatorModel;

import java.util.Date;
import acemapCore.bbReader;
import java.io.FileWriter;
import acemapCore.key_value_map;
import acemapCore.ace_util;
import acecreatorCore.CompositeMfCreator;
import java.io.File;
import maModel.pLNDist;
import acecreatorCore.CreatorUtils;
import acemapCore.utils;
import acemapCore.mTable;
import maModel.fSigmoid2p;
import maModel.fNeonex;
import maModel.LNDp;
import java.util.Random;

public class MadSynthSig
{
    double m_xo;
    double m_scl;
    double m_yo;
    double m_sl;
    double s_xo;
    double s_scl;
    double s_yo;
    double s_sl;
    public static int synthNb;
    private Random rng;
    private LNDp[] sigMod;
    private double D1_x0;
    private double D1_m;
    private double D1_s;
    private fNeonex f_s;
    private fNeonex f_m;
    private fSigmoid2p fBlend;
    private mTable dataReal;
    private mTable dataSynth;
    
    public MadSynthSig() {
        this.rng = new Random();
    }
    
    private double draw(final double n, final double n2) {
        return n + n2 * this.rng.nextGaussian();
    }
    
    private double draw(final LNDp lnDp) {
        return lnDp.x0 + Math.exp(this.draw(Math.log(lnDp.m), lnDp.s));
    }
    
    public void setRealData(final mTable dataReal) {
        this.dataReal = dataReal;
        if (this.dataReal.info.kvm == null) {
            return;
        }
        this.sigMod = new LNDp[2];
        final String value = this.dataReal.info.kvm.valueFor("SIGNAL_DISTRIBUTION");
        System.out.println("PATH : " + this.dataReal.info.path);
        System.out.println("BUFF : " + value);
        final String[] elementArray = utils.getElementArray(value);
        this.sigMod[0] = new LNDp((double)utils.parseFast(elementArray[0]), (double)utils.parseFast(elementArray[1]), (double)utils.parseFast(elementArray[2]), (double)utils.parseFast(elementArray[3]));
        this.sigMod[1] = new LNDp((double)utils.parseFast(elementArray[4]), (double)utils.parseFast(elementArray[5]), (double)utils.parseFast(elementArray[6]), (double)utils.parseFast(elementArray[7]));
        final String value2 = this.dataReal.info.kvm.valueFor("VAR_BLEND");
        System.out.println(value2);
        final String[] elementArray2 = utils.getElementArray(value2);
        final double[] array = new double[2];
        for (int i = 0; i < 2; ++i) {
            array[i] = utils.parseFast(elementArray2[i]);
        }
        this.fBlend = new fSigmoid2p(array);
        final String[] elementArray3 = utils.getElementArray(this.dataReal.info.kvm.valueFor("VAR_MODEL1"));
        this.D1_x0 = utils.parseFast(elementArray3[0]);
        this.D1_m = utils.parseFast(elementArray3[1]);
        this.D1_s = utils.parseFast(elementArray3[2]);
        final String[] elementArray4 = utils.getElementArray(this.dataReal.info.kvm.valueFor("VAR_MODEL2_m"));
        final double[] array2 = new double[elementArray4.length];
        for (int j = 0; j < 4; ++j) {
            array2[j] = utils.parseFast(elementArray4[j]);
        }
        this.m_yo = array2[0];
        this.m_scl = array2[1];
        this.m_xo = array2[2];
        this.m_sl = array2[3];
        final String[] elementArray5 = utils.getElementArray(this.dataReal.info.kvm.valueFor("VAR_MODEL2_s"));
        final double[] array3 = new double[elementArray5.length];
        for (int k = 0; k < 4; ++k) {
            array3[k] = utils.parseFast(elementArray5[k]);
        }
        this.s_yo = array3[0];
        this.s_scl = array3[1];
        this.s_xo = array3[2];
        this.s_sl = array3[3];
    }
    
    public mTable create() {
        final int size = this.dataReal.size;
        this.dataSynth = new mTable(size, 0);
        this.dataSynth.tech = this.dataReal.tech;
        for (int i = 0; i < size; ++i) {
            final double nextDouble = this.rng.nextDouble();
            final double nextDouble2 = this.rng.nextDouble();
            final double draw = this.draw(this.sigMod[nextDouble >= this.sigMod[0].f]);
            final double log = Math.log(draw);
            double exp;
            if (nextDouble2 < this.fBlend.f(log)) {
                exp = this.D1_x0 + Math.exp(this.draw(this.D1_m, this.D1_s));
            }
            else {
                exp = Math.exp(this.draw(this.m_yo + this.m_scl / (1.0 + Math.exp(-(log - this.m_xo) * this.m_sl)), Math.sqrt(this.s_yo + this.s_scl / (1.0 + Math.exp(-(log - this.s_xo) * this.s_sl)))));
            }
            this.dataSynth.data[i].signal = (float)draw;
            this.dataSynth.data[i].moq = (float)exp;
        }
        this.dataReal.sortBySignal();
        this.dataSynth.sortBySignal();
        for (int j = 0; j < size; ++j) {
            this.dataSynth.data[j].name = new String(this.dataReal.data[j].name);
        }
        if (CreatorUtils.currentInFile.getName().endsWith(".ma0") || CreatorUtils.currentInFile.getName().endsWith(".ma1")) {
            final pLNDist plnDist = new pLNDist(this.sigMod);
            for (int k = 0; k < this.dataSynth.size; ++k) {
                this.dataSynth.data[k].a = plnDist.a_value(this.dataSynth.data[k].signal, this.dataSynth.data[k].moq);
            }
            this.dataSynth.enableA();
        }
        return this.dataSynth;
    }
    
    public CompositeMfCreator saveAsMA0(final File parent, final String str) {
        if (this.dataReal.cmf == null) {
            System.out.println("no information found in source file");
            return null;
        }
        final CompositeMfCreator compositeMfCreator = new CompositeMfCreator();
        compositeMfCreator.F_ma = CreatorUtils.getTemp();
        compositeMfCreator.F_txt = this.writeAnnotation((CompositeMfCreator)this.dataReal.cmf);
        this.dataSynth.saveAsMA0(compositeMfCreator.F_ma);
        final String s = utils.getCurrentTime()[0];
        compositeMfCreator.name_ma = ace_util.generateFileName(s, "synth", this.dataSynth.tech, true);
        compositeMfCreator.name_txt = ace_util.generateFileName(s, "synth", this.dataSynth.tech, false);
        File file;
        if (str != null) {
            file = new File(parent, str + "_synth_" + MadSynthSig.synthNb + ".ma0");
        }
        else if (this.dataReal.cmf != null && this.dataReal.cmf.z_file != null) {
            final String filename = this.dataReal.cmf.getFilename();
            file = new File(parent, filename.substring(0, filename.length() - 4) + "_synth_" + MadSynthSig.synthNb + ".ma0");
        }
        else {
            file = new File(parent, "temp_synth_" + MadSynthSig.synthNb + ".ma0");
        }
        compositeMfCreator.saveTo(file);
        ++MadSynthSig.synthNb;
        return compositeMfCreator;
    }
    
    public CompositeMfCreator saveAsMA1(final File parent) {
        if (this.dataReal.cmf == null) {
            System.out.println("no information found in source file");
            return null;
        }
        final CompositeMfCreator compositeMfCreator = new CompositeMfCreator();
        compositeMfCreator.F_ma = CreatorUtils.getTemp();
        compositeMfCreator.F_txt = this.writeAnnotation((CompositeMfCreator)this.dataReal.cmf);
        this.dataSynth.saveAsMA1(compositeMfCreator.F_ma);
        final String s = utils.getCurrentTime()[0];
        compositeMfCreator.name_ma = ace_util.generateFileName(s, "synth", this.dataSynth.tech, true);
        compositeMfCreator.name_txt = ace_util.generateFileName(s, "synth", this.dataSynth.tech, false);
        final String filename = this.dataReal.cmf.getFilename();
        compositeMfCreator.saveTo(new File(parent, filename.substring(0, filename.length() - 4) + "_synth_" + MadSynthSig.synthNb + ".ma1"));
        ++MadSynthSig.synthNb;
        return compositeMfCreator;
    }
    
    private File writeAnnotation(final CompositeMfCreator compositeMfCreator) {
        final File temp = CreatorUtils.getTemp();
        final key_value_map key_value_map = new key_value_map();
        key_value_map.loadFrom(compositeMfCreator.F_txt, false);
        final String lineend = utils.lineend;
        utils.getCurrentTime();
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(temp);
        }
        catch (Exception ex3) {
            return null;
        }
        final bbReader bbReader = new bbReader(compositeMfCreator.F_txt);
        bbReader.getToNextRET();
        int n = 0;
        try {
            fileWriter.write("-----------------------------------------------" + lineend);
            fileWriter.write("ace.map synthetic output generated " + new Date().toString() + lineend);
            fileWriter.write("ace.map creator version: 1.0" + lineend);
            fileWriter.write("http://www.iri.cnrs.fr/seg" + lineend);
            fileWriter.write("-----------------------------------------------" + lineend + lineend);
        }
        catch (Exception ex) {
            System.out.println("Error in writeAnnotation(...) : " + ex.toString());
            return null;
        }
        int i = 1;
        while (true) {
            do {
                final String toNextRET = bbReader.getToNextRET();
                if (toNextRET != null) {
                    final int index = toNextRET.indexOf(9);
                    if (index == -1) {
                        try {
                            if (toNextRET.equals("")) {
                                fileWriter.write(toNextRET + lineend);
                            }
                            else if (toNextRET.equals("GENERAL")) {
                                fileWriter.write(toNextRET + lineend + "-------" + lineend);
                            }
                            else if (toNextRET.equals("SAMPLE")) {
                                fileWriter.write(toNextRET + lineend + "------" + lineend);
                            }
                            else if (toNextRET.equals("PROCESSING")) {
                                fileWriter.write(toNextRET + lineend + "----------" + lineend);
                            }
                            else {
                                if (!toNextRET.equals("DEPLOYMENT")) {
                                    continue;
                                }
                                fileWriter.write(toNextRET + lineend + "----------" + lineend);
                            }
                        }
                        catch (Exception ex4) {
                            i = 0;
                        }
                        continue;
                    }
                    final String substring = toNextRET.substring(0, index);
                    String value = key_value_map.valueFor(substring);
                    if (value == null) {
                        utils.error("problem");
                    }
                    else {
                        i = 1;
                        if (substring.equals("COMMENT_GENERAL")) {
                            value = "Synthetic data";
                        }
                        if (substring.equals("SIGNAL_DISTRIBUTION") || substring.equals("VAR_BLEND") || substring.startsWith("VAR_MODEL")) {
                            System.out.println("key omitted : " + substring);
                        }
                        else if (i != 0) {
                            try {
                                fileWriter.write(substring + "\t" + value + lineend);
                                ++n;
                            }
                            catch (Exception ex2) {
                                System.out.println(ex2.toString() + " in writeAnnotation()");
                                i = 0;
                            }
                            continue;
                        }
                    }
                }
                try {
                    if (CreatorUtils.currentInFile.getName().endsWith(".ma1")) {
                        fileWriter.write(lineend + "-------------------------------------------------" + lineend + lineend);
                    }
                    bbReader.close();
                    fileWriter.close();
                    return temp;
                }
                catch (Exception ex5) {
                    return null;
                }
            } while (i != 0);
            continue;
        }
    }
    
    public mTable getDataReal() {
        return this.dataReal;
    }
    
    static {
        MadSynthSig.synthNb = 0;
    }
}
