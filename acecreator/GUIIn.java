// 
// @author: systemsepigenomics
// 

package acecreator;

import maModel.denspan;
import maModel.pLNDist;
import maModel.var_estimSig;
import acecreatorModel.LNDMix;
import acemapCore.utils;
import acemapCore.AbstractCompositeMf;
import aceStyle.ace_progress;
import acemapCore.mTable;
import acecreatorCore.CompositeMfCreator;
import acecreatorModel.MadSynthSig;
import java.awt.Container;
import acecreatorStyle.SplitFrame1311;
import acecreatorStyle.CreatorFileChooser;
import acecreatorCore.CreatorUtils;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Color;
import java.awt.LayoutManager;
import acecreatorCore.ThCreatorCursor;
import maModel.LNDp;
import java.io.File;
import aceStyle.ace_assaychooser_ab;
import aceStyle.ace_button;
import aceStyle.ace_textfield;
import aceStyle.ace_label;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class GUIIn extends JPanel implements ActionListener
{
    final ace_label inputLab;
    public final ace_textfield inputTxt;
    final ace_button inputBut;
    ace_assaychooser_ab assaychooser;
    int assay_nr;
    String[] date_back;
    File saveFile;
    float scalingM;
    private LNDp[] signalModel;
    boolean mTabFresh;
    final String[] tech_abbr;
    String savedate;
    ThCreatorCursor thCursor;
    
    public GUIIn() {
        super(null);
        this.inputLab = new ace_label("Input:");
        this.inputTxt = new ace_textfield("");
        this.inputBut = new ace_button("browse");
        this.tech_abbr = new String[] { "p", "f", "g" };
        this.setBackground(Color.white);
        this.inputBut.addActionListener((ActionListener)this);
        this.inputTxt.setEnabled(false);
        this.add((Component)this.inputLab);
        this.add((Component)this.inputTxt);
        this.add((Component)this.inputBut);
        this.thCursor = new ThCreatorCursor(this);
    }
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, width, height);
        final int n = 36;
        final int n2 = width * 3 / 5;
        final int n3 = 38;
        final int n4 = (width - n - n2 - n3 - 10) / 2;
        this.inputLab.setBounds(n4, height / 2 - 10, n, 20);
        this.inputTxt.setBounds(n4 + n + 5, height / 2 - 10, n2, 20);
        this.inputBut.setBounds(n4 + n + 5 + n2 + 5, height / 2 - 10, n3, 20);
    }
    
    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource() != this.inputBut) {
            return;
        }
        final CreatorFileChooser creatorFileChooser = new CreatorFileChooser(CreatorUtils.currentFrame, 0);
        creatorFileChooser.setCurrentDirectory(new File("input"));
        creatorFileChooser.setSelectedFile(new File(""));
        final File file = creatorFileChooser.getFile();
        if (file == null) {
            return;
        }
        CreatorUtils.parameters = new double[21];
        final SplitFrame1311 splitFrame1311 = (SplitFrame1311)CreatorUtils.currentFrame;
        final GUIMiddle guiMiddle = new GUIMiddle();
        guiMiddle.setProgression(true);
        final StringBuffer sb = new StringBuffer();
        ((GUIOut)splitFrame1311.getRightPane()).clear();
        final GUIBottom guiBottom = (GUIBottom)splitFrame1311.getBottomPane();
        guiBottom.setParameters(null);
        splitFrame1311.setMiddlePane(guiMiddle);
        splitFrame1311.adjustPanels();
        try {
            this.thCursor.initCursor();
            this.thCursor.launchThread();
            MadSynthSig.synthNb = 0;
            CreatorUtils.currentInFile = file;
            System.out.println("INPUT FILE : " + file.getName());
            if (file.getName().endsWith(".ma0") || file.getName().endsWith(".ma1")) {
                final CompositeMfCreator currentInCompMf = new CompositeMfCreator();
                try {
                    if (currentInCompMf == null || !currentInCompMf.load(file, true, false, false)) {
                        this.thCursor.joinThread();
                        System.out.println("INPUTFILE NOT LOADED");
                        return;
                    }
                    System.out.println("INPUTFILE WELL LOADED");
                    CreatorUtils.currentInCompMf = currentInCompMf;
                }
                catch (Exception ex) {
                    this.thCursor.joinThread();
                    System.out.println("ERROR WHEN LOADING THE FILE");
                    ex.printStackTrace();
                    return;
                }
                System.out.println("NAME MA :" + currentInCompMf.name_ma);
                this.inputTxt.setText(file.getName());
                this.inputTxt.getCaret().setDot(0);
                final mTable currentMTable = CreatorUtils.currentMTable = new mTable(currentInCompMf.F_ma, (ace_progress)null, false, (AbstractCompositeMf)currentInCompMf, true);
                guiMiddle.setDensPanIn(CreatorUtils.createDensPan(currentInCompMf, currentMTable));
                splitFrame1311.adjustPanels();
                CreatorUtils.currentFrame.paint(CreatorUtils.currentFrame.getGraphics());
                if (currentMTable.info.kvm != null && currentMTable.info.kvm.valueFor("SIGNAL_DISTRIBUTION") != null && currentMTable.info.kvm.valueFor("VAR_BLEND") != null && currentMTable.info.kvm.valueFor("VAR_MODEL1") != null && currentMTable.info.kvm.valueFor("VAR_MODEL2_m") != null && currentMTable.info.kvm.valueFor("VAR_MODEL2_s") != null) {
                    final String[] elementArray = utils.getElementArray(currentMTable.info.kvm.valueFor("SIGNAL_DISTRIBUTION"));
                    CreatorUtils.parameters[0] = utils.parseFast(elementArray[0]);
                    CreatorUtils.parameters[1] = utils.parseFast(elementArray[1]);
                    CreatorUtils.parameters[2] = utils.parseFast(elementArray[2]);
                    CreatorUtils.parameters[3] = utils.parseFast(elementArray[3]);
                    CreatorUtils.parameters[4] = utils.parseFast(elementArray[4]);
                    CreatorUtils.parameters[5] = utils.parseFast(elementArray[5]);
                    CreatorUtils.parameters[6] = utils.parseFast(elementArray[6]);
                    CreatorUtils.parameters[7] = utils.parseFast(elementArray[7]);
                    final String[] elementArray2 = utils.getElementArray(currentMTable.info.kvm.valueFor("VAR_BLEND"));
                    CreatorUtils.parameters[8] = utils.parseFast(elementArray2[0]);
                    CreatorUtils.parameters[9] = utils.parseFast(elementArray2[1]);
                    final String[] elementArray3 = utils.getElementArray(currentMTable.info.kvm.valueFor("VAR_MODEL1"));
                    CreatorUtils.parameters[10] = utils.parseFast(elementArray3[0]);
                    CreatorUtils.parameters[11] = utils.parseFast(elementArray3[1]);
                    CreatorUtils.parameters[12] = utils.parseFast(elementArray3[2]);
                    final String[] elementArray4 = utils.getElementArray(currentMTable.info.kvm.valueFor("VAR_MODEL2_m"));
                    CreatorUtils.parameters[13] = utils.parseFast(elementArray4[0]);
                    CreatorUtils.parameters[14] = utils.parseFast(elementArray4[1]);
                    CreatorUtils.parameters[15] = utils.parseFast(elementArray4[2]);
                    CreatorUtils.parameters[16] = utils.parseFast(elementArray4[3]);
                    final String[] elementArray5 = utils.getElementArray(currentMTable.info.kvm.valueFor("VAR_MODEL2_s"));
                    CreatorUtils.parameters[17] = utils.parseFast(elementArray5[0]);
                    CreatorUtils.parameters[18] = utils.parseFast(elementArray5[1]);
                    CreatorUtils.parameters[19] = utils.parseFast(elementArray5[2]);
                    CreatorUtils.parameters[20] = utils.parseFast(elementArray5[3]);
                }
                else {
                    final LNDMix lndMix = new LNDMix(CreatorUtils.currentMTable);
                    sb.append("Estimating signal distribution parameters..." + utils.lineend);
                    guiMiddle.setProgStringBuf(sb);
                    CreatorUtils.currentFrame.paint(CreatorUtils.currentFrame.getGraphics());
                    lndMix.optimize(null);
                    this.signalModel = lndMix.getParams();
                    sb.append("Estimating variance distribution parameters..." + utils.lineend);
                    guiMiddle.setProgStringBuf(sb);
                    CreatorUtils.currentFrame.paint(CreatorUtils.currentFrame.getGraphics());
                    final var_estimSig var_estimSig = new var_estimSig(CreatorUtils.currentMTable);
                    var_estimSig.run(1.0E-4, 80, (File)null);
                    var_estimSig.updateAN(CreatorUtils.getTemp());
                    final double[] parBlend = var_estimSig.getParBlend();
                    final LNDp parD1 = var_estimSig.getParD1();
                    final double[] parD2_Mean = var_estimSig.getParD2_Mean();
                    final double[] parD2_Var = var_estimSig.getParD2_Var();
                    CreatorUtils.parameters[0] = this.signalModel[0].getF();
                    CreatorUtils.parameters[1] = this.signalModel[0].getM();
                    CreatorUtils.parameters[2] = this.signalModel[0].getS();
                    CreatorUtils.parameters[3] = this.signalModel[0].getX0();
                    CreatorUtils.parameters[4] = this.signalModel[1].getF();
                    CreatorUtils.parameters[5] = this.signalModel[1].getM();
                    CreatorUtils.parameters[6] = this.signalModel[1].getS();
                    CreatorUtils.parameters[7] = this.signalModel[1].getX0();
                    CreatorUtils.parameters[8] = parBlend[0];
                    CreatorUtils.parameters[9] = parBlend[1];
                    CreatorUtils.parameters[10] = parD1.getX0();
                    CreatorUtils.parameters[11] = parD1.getM();
                    CreatorUtils.parameters[12] = parD1.getS();
                    CreatorUtils.parameters[13] = parD2_Mean[0];
                    CreatorUtils.parameters[14] = parD2_Mean[1];
                    CreatorUtils.parameters[15] = parD2_Mean[2];
                    CreatorUtils.parameters[16] = parD2_Mean[3];
                    CreatorUtils.parameters[17] = parD2_Var[0];
                    CreatorUtils.parameters[18] = parD2_Var[1];
                    CreatorUtils.parameters[19] = parD2_Var[2];
                    CreatorUtils.parameters[20] = parD2_Var[3];
                }
                CreatorUtils.currentMTable = currentMTable;
                guiBottom.setParameters(CreatorUtils.parameters);
                splitFrame1311.setMiddlePane(guiMiddle);
                splitFrame1311.adjustPanels();
            }
            else if (file.getName().endsWith(".txt")) {
                final CompositeMfCreator compositeMfCreator = new CompositeMfCreator();
                String savedate;
                if (this.savedate == null) {
                    savedate = "000000000000";
                }
                else {
                    savedate = this.savedate;
                }
                final TF_applied tf_applied = new TF_applied(8191);
                if (tf_applied.setReadFile(file)) {
                    final String[] assayNames = tf_applied.getAssayNames();
                    this.assaychooser = new ace_assaychooser_ab(assayNames);
                    this.paintAll(this.getGraphics());
                    this.assay_nr = this.assaychooser.getSelected();
                    if (this.assay_nr != -1) {
                        this.date_back = utils.getCurrentTime();
                        try {
                            this.saveFile = compositeMfCreator.newMa(this.generateFileName(savedate, "user", 0, true));
                            compositeMfCreator.newTxt(this.generateFileName(savedate, "user", 0, true));
                        }
                        catch (Exception ex3) {
                            this.saveFile = null;
                            System.out.println("saveFile set to null for applied biosystems file");
                        }
                        if (this.saveFile != null) {
                            System.out.println("SAVEFILE : " + this.saveFile.getPath());
                            if (tf_applied.setAssay(this.assay_nr)) {
                                if (tf_applied.writeToFile(this.saveFile, null)) {
                                    final mTable mTable = new mTable(this.saveFile, (ace_progress)null, false, (AbstractCompositeMf)null, false);
                                    mTable.cmf = compositeMfCreator;
                                    mTable.tech = 1;
                                    this.scalingM = mTable.normalize();
                                    final denspan densPan = CreatorUtils.createDensPan(compositeMfCreator, mTable);
                                    CreatorUtils.currentMTable = mTable;
                                    guiMiddle.setDensPanIn(densPan);
                                    CreatorUtils.currentFrame.paint(CreatorUtils.currentFrame.getGraphics());
                                    splitFrame1311.adjustPanels();
                                    final LNDMix lndMix2 = new LNDMix(mTable);
                                    sb.append("Estimating signal distribution parameters..." + utils.lineend);
                                    guiMiddle.setProgStringBuf(sb);
                                    CreatorUtils.currentFrame.paint(CreatorUtils.currentFrame.getGraphics());
                                    lndMix2.optimize(null);
                                    this.signalModel = lndMix2.getParams();
                                    final pLNDist plnDist = new pLNDist(this.signalModel);
                                    for (int i = 0; i < mTable.size; ++i) {
                                        mTable.data[i].a = plnDist.a_value(mTable.data[i].signal, mTable.data[i].moq);
                                    }
                                    mTable.enableA();
                                    mTable.saveAsMA0(this.saveFile);
                                    System.out.println("final saveFile = " + this.saveFile.getName());
                                    this.mTabFresh = true;
                                    System.out.println("save successful");
                                    if (assayNames[this.assay_nr].length() >= 7) {
                                        this.inputTxt.setText(utils.cutExt(file.getName()) + "_" + assayNames[this.assay_nr].substring(0, 7));
                                    }
                                    else {
                                        this.inputTxt.setText(utils.cutExt(file.getName()) + "_" + assayNames[this.assay_nr]);
                                    }
                                    this.inputTxt.getCaret().setDot(0);
                                    CreatorUtils.currentInCompMf = compositeMfCreator;
                                    sb.append("Estimating variance distribution parameters..." + utils.lineend);
                                    guiMiddle.setProgStringBuf(sb);
                                    CreatorUtils.currentFrame.paint(CreatorUtils.currentFrame.getGraphics());
                                    final var_estimSig var_estimSig2 = new var_estimSig(CreatorUtils.currentMTable);
                                    var_estimSig2.run(1.0E-4, 80, (File)null);
                                    var_estimSig2.updateAN(CreatorUtils.getTemp());
                                    final double[] parBlend2 = var_estimSig2.getParBlend();
                                    final LNDp parD2 = var_estimSig2.getParD1();
                                    final double[] parD2_Mean2 = var_estimSig2.getParD2_Mean();
                                    final double[] parD2_Var2 = var_estimSig2.getParD2_Var();
                                    CreatorUtils.parameters[0] = this.signalModel[0].getF();
                                    CreatorUtils.parameters[1] = this.signalModel[0].getM();
                                    CreatorUtils.parameters[2] = this.signalModel[0].getS();
                                    CreatorUtils.parameters[3] = this.signalModel[0].getX0();
                                    CreatorUtils.parameters[4] = this.signalModel[1].getF();
                                    CreatorUtils.parameters[5] = this.signalModel[1].getM();
                                    CreatorUtils.parameters[6] = this.signalModel[1].getS();
                                    CreatorUtils.parameters[7] = this.signalModel[1].getX0();
                                    CreatorUtils.parameters[8] = parBlend2[0];
                                    CreatorUtils.parameters[9] = parBlend2[1];
                                    CreatorUtils.parameters[10] = parD2.getX0();
                                    CreatorUtils.parameters[11] = parD2.getM();
                                    CreatorUtils.parameters[12] = parD2.getS();
                                    CreatorUtils.parameters[13] = parD2_Mean2[0];
                                    CreatorUtils.parameters[14] = parD2_Mean2[1];
                                    CreatorUtils.parameters[15] = parD2_Mean2[2];
                                    CreatorUtils.parameters[16] = parD2_Mean2[3];
                                    CreatorUtils.parameters[17] = parD2_Var2[0];
                                    CreatorUtils.parameters[18] = parD2_Var2[1];
                                    CreatorUtils.parameters[19] = parD2_Var2[2];
                                    CreatorUtils.parameters[20] = parD2_Var2[3];
                                    CreatorUtils.currentMTable = mTable;
                                    guiBottom.setParameters(CreatorUtils.parameters);
                                    splitFrame1311.adjustPanels();
                                }
                                else {
                                    System.out.println("Problem : impossible to create the file");
                                }
                            }
                        }
                    }
                }
                else {
                    System.out.println("failure in set read file");
                }
            }
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            utils.message("Impossible to open the file " + file.getName());
        }
        finally {
            guiMiddle.setProgression(false);
            CreatorUtils.currentFrame.paint(CreatorUtils.currentFrame.getGraphics());
            this.thCursor.joinThread();
        }
        CreatorUtils.currentOutFile = null;
    }
    
    public String generateFileName(final String str, final String str2, final int n, final boolean b) {
        return new String(str + str2 + "_" + this.tech_abbr[n] + (b ? ".dat" : ".txt"));
    }
}
