// 
// @author: systemsepigenomics
// 

package acecreatorModel;

import java.io.OutputStreamWriter;
import acecreatorCore.CreatorUtils;
import java.io.FileWriter;
import java.io.File;
import acemapCore.utils;
import acemapCore.mTable;
import maModel.LNDp;

public class LNDMix
{
    private LNDp[] lndp;
    private double[] dat;
    private double[][] logdat;
    private double logL;
    private mTable mTab;
    
    private double ABS(final double n) {
        return (n < 0.0) ? (-n) : n;
    }
    
    public LNDMix(final mTable mTab) {
        this.mTab = mTab;
        this.dat = new double[mTab.size];
        for (int i = 0; i < this.dat.length; ++i) {
            this.dat[i] = mTab.data[i].signal;
        }
        this.lndp = new LNDp[2];
    }
    
    public void setParameters(final LNDp[] lndp) {
        this.lndp = lndp;
    }
    
    private double SQR(final double n) {
        return n * n;
    }
    
    private double Pi(final int n, final int n2) {
        final LNDp lnDp = this.lndp[n];
        if (this.dat[n2] <= lnDp.x0) {
            return 0.0;
        }
        return lnDp.f / ((this.dat[n2] - lnDp.x0) * 2.506628274631 * lnDp.s) * Math.exp(-this.SQR(this.logdat[n][n2] - lnDp.logm) / (2.0 * this.SQR(lnDp.s)));
    }
    
    private double Pt(final int n) {
        double n2 = 0.0;
        for (int i = 0; i < this.lndp.length; ++i) {
            n2 += this.Pi(i, n);
        }
        return n2;
    }
    
    public double L() {
        double n = 0.0;
        for (int i = 0; i < this.dat.length; ++i) {
            n += Math.log(this.Pt(i));
        }
        return n;
    }
    
    public double L(final LNDp[] lndp) {
        final LNDp[] lndp2 = this.lndp;
        this.lndp = lndp;
        this.logdat = new double[this.lndp.length][this.dat.length];
        for (int i = 0; i < this.lndp.length; ++i) {
            for (int j = 0; j < this.dat.length; ++j) {
                if (this.dat[j] > this.lndp[i].x0) {
                    this.logdat[i][j] = Math.log(this.dat[j] - this.lndp[i].x0);
                }
                else {
                    this.logdat[i][j] = 0.0;
                }
            }
        }
        final double l = this.L();
        this.lndp = lndp2;
        return l;
    }
    
    private void dataOut() {
        System.out.println("D1 (" + utils.getDecs(this.lndp[0].f, 3, false) + "; " + utils.getDecs(this.lndp[0].m, 3, false) + ", " + utils.getDecs(this.lndp[0].s, 3, false) + ", " + utils.getDecs(this.lndp[0].x0, 3, false) + "), D2 (" + utils.getDecs(this.lndp[1].f, 3, false) + "; " + utils.getDecs(this.lndp[1].m, 3, false) + ", " + utils.getDecs(this.lndp[1].s, 3, false) + ", " + utils.getDecs(this.lndp[1].x0, 3, false) + ")");
    }
    
    public void EM() {
        final double[] array = new double[this.lndp.length];
        final double[] array2 = new double[this.lndp.length];
        final double[] array3 = new double[this.lndp.length];
        final LNDp[] lndp = new LNDp[this.lndp.length];
        final double n = this.dat.length;
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (array[i] = 0.0);
            lndp[i] = new LNDp();
            lndp[i].x0 = this.lndp[i].x0;
            lndp[i].s = 0.0;
            lndp[i].f = 0.0;
        }
        for (int j = 0; j < this.dat.length; ++j) {
            double n2 = 0.0;
            for (int k = 0; k < this.lndp.length; ++k) {
                array3[k] = this.Pi(k, j);
                n2 += array3[k];
            }
            for (int l = 0; l < lndp.length; ++l) {
                final double n3 = array3[l] / n2;
                final double[] array4 = array;
                final int n4 = l;
                array4[n4] += n3;
                final double[] array5 = array2;
                final int n5 = l;
                array5[n5] += n3 * this.logdat[l][j];
            }
        }
        for (int n6 = 0; n6 < this.lndp.length; ++n6) {
            array2[n6] /= array[n6];
            array[n6] = 0.0;
        }
        for (int n7 = 0; n7 < this.dat.length; ++n7) {
            double n8 = 0.0;
            for (int n9 = 0; n9 < this.lndp.length; ++n9) {
                array3[n9] = this.Pi(n9, n7);
                n8 += array3[n9];
            }
            for (int n10 = 0; n10 < lndp.length; ++n10) {
                final double n11 = array3[n10] / n8;
                final double[] array6 = array;
                final int n12 = n10;
                array6[n12] += n11;
                final LNDp lnDp = lndp[n10];
                lnDp.f += n11;
                final LNDp lnDp2 = lndp[n10];
                lnDp2.s += n11 * this.SQR(this.logdat[n10][n7] - array2[n10]);
            }
        }
        for (int n13 = 0; n13 < this.lndp.length; ++n13) {
            lndp[n13].logm = array2[n13];
            lndp[n13].m = Math.exp(array2[n13]);
            lndp[n13].s = Math.sqrt(lndp[n13].s / array[n13]);
            lndp[n13].f /= n;
        }
        this.lndp = lndp;
    }
    
    public double EMlike() {
        this.EM();
        return this.L();
    }
    
    public void run() {
        final double n = utils.getNumber("enter x0");
        this.lndp[0] = new LNDp(0.5, 0.2, 1.0, 0.0);
        this.lndp[1] = new LNDp(0.5, 2.0, 1.0, n);
        final double n2 = 1.0E-6;
        final int n3 = 150;
        int i = 0;
        double eMlike = -1000000.0;
        double n4;
        do {
            n4 = eMlike;
            eMlike = this.EMlike();
            System.out.println("step " + i + ": L=" + utils.getDecs(eMlike, 3, false));
        } while (++i < n3 && eMlike - n4 > n2);
        if (utils.request("once more?", "yes", "no")) {
            this.run();
        }
    }
    
    public double run(final double n, final double n2) {
        this.lndp[0] = new LNDp(0.5, 0.2, 1.0, 0.0);
        this.lndp[1] = new LNDp(0.5, 2.0, 1.0, n);
        this.logdat = new double[this.lndp.length][this.dat.length];
        for (int i = 0; i < this.lndp.length; ++i) {
            for (int j = 0; j < this.dat.length; ++j) {
                if (this.dat[j] > this.lndp[i].x0) {
                    this.logdat[i][j] = Math.log(this.dat[j] - this.lndp[i].x0);
                }
                else {
                    this.logdat[i][j] = 0.0;
                }
            }
        }
        final int n3 = 150;
        int k = 0;
        double eMlike = -1.0E8;
        double n4;
        do {
            n4 = eMlike;
            eMlike = this.EMlike();
            System.out.println("step " + k + ": L=" + utils.getDecs(eMlike, 3, false));
        } while (++k < n3 && eMlike - n4 > n2);
        System.out.println("EM converged. logL= " + utils.getDecs(eMlike, 3, false) + " model:");
        this.dataOut();
        return eMlike;
    }
    
    public void optimize(final File parent) {
        OutputStreamWriter outputStreamWriter = null;
        File file;
        if (parent != null) {
            final String filename = this.mTab.cmf.getFilename();
            if (this.mTab.cmf == null) {
                file = null;
            }
            else {
                file = new File(parent, filename.substring(0, filename.length() - 4) + "_log.txt");
            }
        }
        else {
            file = null;
        }
        if (file != null) {
            try {
                outputStreamWriter = new FileWriter(file);
                outputStreamWriter.write("D1_x0\tD1_w\tD1_m\tD1_v\tD2_x0\tD2_w\tD2_m\tD2_v\tlogL" + utils.lineend);
            }
            catch (Exception ex) {}
        }
        final int n = 20;
        final int n2 = 4;
        final double n3 = 0.06;
        final double[] array = new double[n];
        final double[] array2 = new double[n2];
        for (int i = 0; i < n; ++i) {
            array[i] = this.run(n3 * i, 0.1);
            if (file != null) {
                try {
                    outputStreamWriter.write(utils.getDecs(this.lndp[0].x0, 3, false) + "\t" + utils.getDecs(this.lndp[0].f, 3, false) + "\t" + utils.getDecs(this.lndp[0].m, 3, false) + "\t" + utils.getDecs(this.lndp[0].s, 3, false) + "\t" + utils.getDecs(this.lndp[1].x0, 3, false) + "\t" + utils.getDecs(this.lndp[1].f, 3, false) + "\t" + utils.getDecs(this.lndp[1].m, 3, false) + "\t" + utils.getDecs(this.lndp[1].s, 3, false) + "\t" + utils.getDecs(array[i], 3, false) + utils.lineend);
                }
                catch (Exception ex2) {}
            }
        }
        if (file != null) {
            try {
                outputStreamWriter.close();
            }
            catch (Exception ex3) {
                System.out.println("ii");
            }
        }
        int n4 = 0;
        double n5 = array[0];
        for (int j = 1; j < n; ++j) {
            if (array[j] > n5) {
                n5 = array[j];
                n4 = j;
            }
        }
        final double n6 = (n4 - 1) * n3;
        final double n7 = n3 * 2.0 / n2;
        for (int k = 0; k < n2; ++k) {
            array2[k] = this.run(n6 + n7 * k, 0.001);
            if (file != null) {
                try {
                    outputStreamWriter.write(utils.getDecs(this.lndp[0].x0, 4, false) + "\t" + utils.getDecs(this.lndp[0].f, 4, false) + "\t" + utils.getDecs(this.lndp[0].m, 4, false) + "\t" + utils.getDecs(this.lndp[0].s, 4, false) + "\t" + utils.getDecs(this.lndp[1].x0, 4, false) + "\t" + utils.getDecs(this.lndp[1].f, 4, false) + "\t" + utils.getDecs(this.lndp[1].m, 4, false) + "\t" + utils.getDecs(this.lndp[1].s, 4, false) + "\t" + utils.getDecs(array2[k], 4, false) + utils.lineend);
                }
                catch (Exception ex4) {}
            }
        }
        int n8 = 0;
        double n9 = array2[0];
        for (int l = 1; l < n2; ++l) {
            if (array2[l] > n9) {
                n9 = array2[l];
                n8 = l;
            }
        }
        this.run(n6 + n7 * n8, 1.0E-5);
        System.out.println(">>>>>>>>>>>> FINAL MODEL:");
        this.dataOut();
        if (file != null) {
            try {
                outputStreamWriter.close();
            }
            catch (Exception ex5) {}
        }
        if (this.mTab.cmf == null || this.mTab.cmf.F_txt == null) {
            return;
        }
        final File f_txt = this.mTab.cmf.F_txt;
        final File temp = CreatorUtils.getTemp();
        utils.insertBehind(f_txt, temp, "MIXTURE_D_MODEL", new String("SIGNAL_DISTRIBUTION\t" + utils.getDecs(this.lndp[0].f, 4, false) + "\t" + utils.getDecs(this.lndp[0].m, 4, false) + "\t" + utils.getDecs(this.lndp[0].s, 4, false) + "\t" + utils.getDecs(this.lndp[0].x0, 4, false) + "\t" + utils.getDecs(this.lndp[1].f, 4, false) + "\t" + utils.getDecs(this.lndp[1].m, 4, false) + "\t" + utils.getDecs(this.lndp[1].s, 4, false) + "\t" + utils.getDecs(this.lndp[1].x0, 4, false) + "\t" + utils.getDecs(n9, 4, false)));
        if (this.mTab.cmf != null) {
            this.mTab.cmf.F_txt = temp;
            if (this.mTab.cmf.z_file != null) {
                this.mTab.cmf.save();
            }
        }
    }
    
    public LNDp[] getParams() {
        return this.lndp;
    }
}
