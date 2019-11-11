// 
// @author: systemsepigenomics
// 

package acecreator;

import acemapCore.mTable;
import java.io.File;
import acecreatorCore.CompositeMfCreator;
import acemapCore.utils;
import acecreatorCore.CreatorUtils;
import acecreatorStyle.SplitFrame1311;
import acecreatorModel.MadSynthSig;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Color;
import java.awt.LayoutManager;
import acecreatorCore.ThCreatorCursor;
import aceStyle.ace_combobox;
import aceStyle.ace_button;
import aceStyle.ace_label;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class GUICreate extends JPanel implements ActionListener
{
    final ace_label createLab1;
    final ace_button createBut;
    final ace_label createLab2;
    private final String[] nbToCreate;
    final ace_combobox selectNbToCreate;
    ThCreatorCursor thCursor;
    
    public GUICreate() {
        super(null);
        this.createLab1 = new ace_label("|");
        this.createBut = new ace_button("create");
        this.createLab2 = new ace_label("|");
        this.nbToCreate = new String[] { "1", "2", "5", "25", "50", "100" };
        this.selectNbToCreate = new ace_combobox(this.nbToCreate, false);
        this.setBackground(Color.white);
        this.createBut.addActionListener((ActionListener)this);
        this.add((Component)this.createLab1);
        this.add((Component)this.createBut);
        this.add((Component)this.createLab2);
        this.add((Component)this.selectNbToCreate);
        this.thCursor = new ThCreatorCursor(this);
    }
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, width, height);
        final int n = 37;
        final int n2 = (width - n - 14) / 2;
        this.createLab1.setBounds(n2, 25, 5, 20);
        this.createBut.setBounds(n2 + 7, 25, n, 20);
        this.createLab2.setBounds(n2 + n + 7 + 2, 25, 5, 20);
        this.selectNbToCreate.setBounds(n2, height - 45, n + 14, 20);
    }
    
    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.createBut) {
            this.thCursor.initCursor();
            this.thCursor.launchThread();
            try {
                MadSynthSig.synthNb = 0;
                final SplitFrame1311 splitFrame1311 = (SplitFrame1311)CreatorUtils.currentFrame;
                final GUIMiddle guiMiddle = (GUIMiddle)splitFrame1311.getMiddlePane();
                guiMiddle.setProgression(true);
                final StringBuffer progStringBuf = new StringBuffer();
                if (CreatorUtils.currentInCompMf != null && CreatorUtils.currentInFile != null) {
                    final mTable currentMTable = CreatorUtils.currentMTable;
                    final int int1 = Integer.parseInt(this.selectNbToCreate.getContent());
                    final String[] currentSynthPaths = new String[int1];
                    final MadSynthSig madSynthSig = new MadSynthSig();
                    madSynthSig.setRealData(currentMTable);
                    for (int i = 0; i < int1; ++i) {
                        progStringBuf.append("Creating synthetic data file " + i + utils.lineend);
                        guiMiddle.setProgStringBuf(progStringBuf);
                        CreatorUtils.currentFrame.paint(CreatorUtils.currentFrame.getGraphics());
                        madSynthSig.create();
                        final CompositeMfCreator compositeMfCreator = new CompositeMfCreator();
                        CompositeMfCreator compositeMfCreator2;
                        if (CreatorUtils.currentInFile.getName().endsWith(".ma0") || CreatorUtils.currentInFile.getName().endsWith(".txt")) {
                            compositeMfCreator2 = madSynthSig.saveAsMA0(CreatorUtils.tempPath, utils.cutExt(CreatorUtils.currentInFile.getName()));
                        }
                        else {
                            if (!CreatorUtils.currentInFile.getName().endsWith(".ma1")) {
                                this.thCursor.joinThread();
                                utils.message("Error, treatment of input file failed");
                                return;
                            }
                            compositeMfCreator2 = madSynthSig.saveAsMA1(CreatorUtils.tempPath);
                        }
                        currentSynthPaths[i] = compositeMfCreator2.z_file.getPath();
                    }
                    CreatorUtils.currentSynthPaths = currentSynthPaths;
                    final GUIIn guiIn = (GUIIn)splitFrame1311.getLeftPane();
                    final GUIOut guiOut = (GUIOut)splitFrame1311.getRightPane();
                    guiOut.outputTxt.setContent(utils.cutExt(guiIn.inputTxt.getContent()) + "_synth");
                    guiOut.outputTxt.getCaret().setDot(0);
                    guiOut.enableSave(true);
                    CreatorUtils.currentOutFile = new File(CreatorUtils.tempPath + File.separator + utils.cutExt(guiIn.inputTxt.getContent()) + "_synth");
                    guiMiddle.setDefaultDensPan();
                    guiMiddle.setProgression(false);
                    ((GUIBottom)splitFrame1311.getBottomPane()).setParameters(CreatorUtils.parameters);
                    CreatorUtils.currentFrame.paint(CreatorUtils.currentFrame.getGraphics());
                    splitFrame1311.adjustPanels();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                utils.message("Impossible to create synthetic files");
            }
            finally {
                this.thCursor.joinThread();
            }
        }
    }
}
