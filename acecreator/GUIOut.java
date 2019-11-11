// 
// @author: systemsepigenomics
// 

package acecreator;

import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import acemapCore.bbReader;
import acecreatorCore.CompositeMfCreator;
import java.util.Vector;
import acecreatorModel.MadSynthSig;
import acemapCore.utils;
import java.io.File;
import acecreatorStyle.CreatorFileChooser;
import acecreatorCore.CreatorUtils;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Color;
import java.awt.LayoutManager;
import aceStyle.ace_button;
import aceStyle.ace_textfield;
import aceStyle.ace_label;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class GUIOut extends JPanel implements ActionListener
{
    final ace_label outputLab;
    final ace_textfield outputTxt;
    ace_button outputBut;
    
    public GUIOut() {
        super(null);
        this.outputLab = new ace_label("Output:");
        this.outputTxt = new ace_textfield("");
        this.outputBut = new ace_button("save");
        this.setBackground(Color.white);
        this.outputBut.addActionListener((ActionListener)this);
        this.outputTxt.setEnabled(false);
        this.add((Component)this.outputLab);
        this.add((Component)this.outputTxt);
        this.add((Component)this.outputBut);
    }
    
    public void clear() {
        this.outputTxt.setText("");
        this.enableSave(false);
    }
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        super.setBounds(x, y, width, height);
        final int n = 45;
        final int n2 = width * 3 / 5;
        final int n3 = 38;
        final int n4 = (width - n - n2 - n3 - 10) / 2;
        this.outputLab.setBounds(n4, height / 2 - 10, n, 20);
        this.outputTxt.setBounds(n4 + n + 5, height / 2 - 10, n2, 20);
        this.outputBut.setBounds(n4 + n + 5 + n2 + 5, height / 2 - 10, n3, 20);
    }
    
    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.outputBut) {
            if (CreatorUtils.currentInFile.getName().endsWith(".ma0")) {
                final CreatorFileChooser creatorFileChooser = new CreatorFileChooser(CreatorUtils.currentFrame, 2);
                creatorFileChooser.setCurrentDirectory(new File("output"));
                creatorFileChooser.setSelectedFile(new File(CreatorUtils.currentOutFile.getName()));
                File file = creatorFileChooser.getFile();
                if (file == null) {
                    return;
                }
                if (file.getName().endsWith(".ma0") || file.getName().endsWith(".ma1") || file.getName().endsWith(".txt")) {
                    file = new File(utils.cutExt(file.getPath()));
                }
                this.outputTxt.setContent(file.getName());
                this.outputTxt.getCaret().setDot(0);
                if (CreatorUtils.currentInFile.getName().endsWith(".ma0")) {
                    for (int i = 0; i < MadSynthSig.synthNb; ++i) {
                        final File file2 = new File(CreatorUtils.currentOutFile.getPath() + "_" + i + ".ma0");
                        File file3;
                        if (i >= 0 && i < 10) {
                            file3 = new File(file.getPath() + "_0" + i + ".ma0");
                        }
                        else {
                            file3 = new File(file.getPath() + "_" + i + ".ma0");
                        }
                        utils.copy(file2, file3);
                    }
                }
            }
            else if (CreatorUtils.currentInFile.getName().endsWith(".ma1")) {
                final CreatorFileChooser creatorFileChooser2 = new CreatorFileChooser(CreatorUtils.currentFrame, 3);
                creatorFileChooser2.setCurrentDirectory(new File("output"));
                creatorFileChooser2.setSelectedFile(new File(CreatorUtils.currentOutFile.getName()));
                File file4 = creatorFileChooser2.getFile();
                if (file4 == null) {
                    return;
                }
                if (file4.getName().endsWith(".ma0") || file4.getName().endsWith(".ma1") || file4.getName().endsWith(".txt")) {
                    file4 = new File(utils.cutExt(file4.getPath()));
                }
                this.outputTxt.setContent(file4.getName());
                this.outputTxt.getCaret().setDot(0);
                for (int j = 0; j < MadSynthSig.synthNb; ++j) {
                    final File file5 = new File(CreatorUtils.currentOutFile.getPath() + "_" + j + ".ma1");
                    File file6;
                    if (j >= 0 && j < 10) {
                        file6 = new File(file4.getPath() + "_0" + j + ".ma1");
                    }
                    else {
                        file6 = new File(file4.getPath() + "_" + j + ".ma1");
                    }
                    utils.copy(file5, file6);
                }
            }
            else if (CreatorUtils.currentInFile.getName().endsWith(".txt")) {
                final CreatorFileChooser creatorFileChooser3 = new CreatorFileChooser(CreatorUtils.currentFrame, 1);
                creatorFileChooser3.setCurrentDirectory(new File("output"));
                creatorFileChooser3.setSelectedFile(new File(CreatorUtils.currentOutFile.getName()));
                File file7 = creatorFileChooser3.getFile();
                if (file7 == null) {
                    return;
                }
                if (file7.getName().endsWith(".ma0") || file7.getName().endsWith(".ma1") || file7.getName().endsWith(".txt")) {
                    file7 = new File(utils.cutExt(file7.getPath()));
                }
                this.outputTxt.setContent(file7.getName());
                this.outputTxt.getCaret().setDot(0);
                final char c = '\t';
                try {
                    final Vector<StringBuffer> vector = new Vector<StringBuffer>();
                    final String path = file7.getPath();
                    for (int k = 0; k < CreatorUtils.currentSynthPaths.length; ++k) {
                        final CompositeMfCreator compositeMfCreator = new CompositeMfCreator();
                        compositeMfCreator.load(new File(CreatorUtils.currentSynthPaths[k]), true, true, true);
                        final bbReader bbReader = new bbReader(compositeMfCreator.F_ma);
                        String s = bbReader.getToNextRET();
                        if (k % 25 == 0) {
                            int length = k + 24;
                            if (length > CreatorUtils.currentSynthPaths.length) {
                                length = CreatorUtils.currentSynthPaths.length;
                            }
                            file7 = new File(path + "_" + k + "_to_" + length + ".txt");
                            vector.removeAllElements();
                            final StringBuffer sb = new StringBuffer();
                            sb.append("Probe_ID" + c + "Gene_ID");
                            if (k + 25 < CreatorUtils.currentSynthPaths.length) {
                                for (int l = k; l < k + 25; ++l) {
                                    if (l >= 0 && l < 10) {
                                        sb.append(c + "Assay_Normalized_Signal_synth_0" + l);
                                        sb.append(c + "CV_synth_0" + l);
                                        sb.append(c + "Flags_synth_0" + l);
                                    }
                                    else {
                                        sb.append(c + "Assay_Normalized_Signal_synth_" + l);
                                        sb.append(c + "CV_synth_" + l);
                                        sb.append(c + "Flags_synth_" + l);
                                    }
                                }
                            }
                            else {
                                for (int n = k; n < CreatorUtils.currentSynthPaths.length; ++n) {
                                    if (n >= 0 && n < 10) {
                                        sb.append(c + "Assay_Normalized_Signal_synth_0" + n);
                                        sb.append(c + "CV_synth_0" + n);
                                        sb.append(c + "Flags_synth_0" + n);
                                    }
                                    else {
                                        sb.append(c + "Assay_Normalized_Signal_synth_" + n);
                                        sb.append(c + "CV_synth_" + n);
                                        sb.append(c + "Flags_synth_" + n);
                                    }
                                }
                            }
                            utils.saveToFile(sb.toString(), file7.getPath());
                        }
                        final FileWriter out = new FileWriter(file7.getPath(), true);
                        final BufferedWriter bufferedWriter = new BufferedWriter(out);
                        int index = 0;
                        while (s != null) {
                            final StringBuffer sb2 = new StringBuffer();
                            s = bbReader.getToNextRET();
                            try {
                                final String[] split = s.split("\t");
                                if (split == null || split.length < 3) {
                                    continue;
                                }
                                if (k % 25 == 0) {
                                    sb2.append(split[0] + c + "null");
                                }
                                sb2.append(c + split[1]);
                                sb2.append(c + split[2]);
                                sb2.append(c + "0");
                                if (k % 25 == 0) {
                                    vector.add(sb2);
                                }
                                else if (vector.elementAt(index) != null) {
                                    vector.add(vector.elementAt(index).append(sb2));
                                }
                                if ((k + 1) % 25 == 0 || k + 1 == CreatorUtils.currentSynthPaths.length) {
                                    bufferedWriter.write(vector.elementAt(index).toString() + utils.lineend);
                                }
                                ++index;
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Impossible to split");
                            }
                        }
                        bbReader.close();
                        bufferedWriter.close();
                        out.close();
                    }
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                    utils.message("Error when saving the synthetic file : " + file7.getName() + utils.lineend + "Check the file name, it cannot contain /\\:*?\"><|");
                }
            }
        }
    }
    
    public void enableSave(final boolean b) {
        if (b) {
            this.outputBut.setEnabled(true);
        }
        else {
            this.outputBut.setEnabled(false);
        }
    }
}
