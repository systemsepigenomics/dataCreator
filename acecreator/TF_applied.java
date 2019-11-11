// 
// @author: systemsepigenomics
// 

package acecreator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import aceStyle.ace_progress;
import java.io.File;
import acemapCore.utils;

class TF_applied extends TableFile
{
    final byte[] nullgene;
    int flag_border;
    int probe_col;
    boolean atEnd;
    boolean atBeg;
    
    TF_applied(final int flag_border) {
        this.nullgene = new byte[] { 78, 85, 76, 76, 45, 71, 69, 78, 69 };
        this.flag_border = flag_border;
    }
    
    private boolean isNumber(final byte[] array, final int n, final int n2) {
        for (int i = 0; i < n2; ++i) {
            if (array[n + i] < 48 || array[n + i] > 57) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isNullGene(final byte[] array, final int n, final int n2) {
        if (n2 != 9) {
            return false;
        }
        for (int i = 0; i < 4; ++i) {
            if (array[n + i] != this.nullgene[i]) {
                return false;
            }
        }
        for (int j = 5; j < 9; ++j) {
            if (array[n + j] != this.nullgene[j]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean outOfFlagRange(final byte[] array, final int n, final int n2) {
        final int fastI = utils.parseFastI(array, n, n2);
        return fastI < 0 || fastI > this.flag_border;
    }
    
    public boolean writeRow() {
        if (!this.ready_write) {
            return false;
        }
        try {
            this.fos.write(this.buffer, this.starts[this.probe_col], this.lengths[this.probe_col]);
            this.fos.write(9);
            this.fos.write(this.buffer, this.starts[this.signal_col], this.lengths[this.signal_col]);
            this.fos.write(9);
            this.fos.write(this.buffer, this.starts[this.cv_col], this.lengths[this.cv_col]);
            this.fos.write(13);
            this.fos.write(10);
        }
        catch (Exception ex) {
            System.out.println("Exception in writeRow : " + ex.toString());
            return false;
        }
        return true;
    }
    
    public boolean writeToFile(final File file, final ace_progress ace_progress) {
        if (!this.ready_write) {
            System.out.println("write ready false");
            return false;
        }
        try {
            this.fos = new FileOutputStream(file);
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
            return false;
        }
        if (ace_progress != null) {
            ace_progress.setPart(0.0);
        }
        try {
            this.fos.write(new String("PROBE").getBytes("US-ASCII"));
            this.fos.write(9);
            this.fos.write(new String("NORMALIZED_SIGNAL").getBytes("US-ASCII"));
            this.fos.write(9);
            this.fos.write(new String("CV").getBytes("US-ASCII"));
            this.fos.write(13);
            this.fos.write(10);
        }
        catch (Exception ex2) {
            return false;
        }
        if (!this.isNullGene(this.buffer, this.starts[this.geneID_col], this.lengths[this.geneID_col]) && this.isNumber(this.buffer, this.starts[this.probe_col], this.lengths[this.probe_col]) && !this.outOfFlagRange(this.buffer, this.starts[this.flags_col], this.lengths[this.flags_col]) && !this.writeRow()) {
            return false;
        }
        while (this.readRow()) {
            if (this.f_end) {
                if (this.line_has_data && !this.isNullGene(this.buffer, this.starts[this.geneID_col], this.lengths[this.geneID_col]) && !this.outOfFlagRange(this.buffer, this.starts[this.flags_col], this.lengths[this.flags_col]) && !this.writeRow()) {
                    return false;
                }
                try {
                    this.fos.flush();
                    this.fos.close();
                }
                catch (Exception ex3) {}
                if (ace_progress != null) {
                    ace_progress.setPart(0.0);
                }
                return true;
            }
            else {
                if (!this.isNullGene(this.buffer, this.starts[this.geneID_col], this.lengths[this.geneID_col]) && this.isNumber(this.buffer, this.starts[this.probe_col], this.lengths[this.probe_col]) && !this.outOfFlagRange(this.buffer, this.starts[this.flags_col], this.lengths[this.flags_col]) && !this.writeRow()) {
                    return false;
                }
                if (this.row_index % 400 != 0 || ace_progress == null) {
                    continue;
                }
                ace_progress.setPart(this.row_index / 40000.0);
            }
        }
        return false;
    }
    
    public boolean setReadFile(final File file) {
        if (this.ready_read) {
            this.ready_read = false;
            this.ready_write = false;
            this.buffer = new byte[20000];
            try {
                this.fis.close();
            }
            catch (Exception ex) {
                System.out.println("in setReadFile()[1]: " + ex.toString());
            }
        }
        this.f_end = false;
        this.geneID_col = -1;
        this.signal_col = -1;
        this.probe_col = -1;
        this.flags_col = -1;
        this.cv_col = -1;
        try {
            this.fis = new FileInputStream(file);
        }
        catch (Exception ex2) {
            System.out.println("in setReadFile()[2]: " + ex2.toString());
            return false;
        }
        int off = 0;
        this.columns = 1;
        try {
            do {
                this.fis.read(this.buffer, off, 1);
                if (this.buffer[off] == 9) {
                    ++this.columns;
                }
                if (off > 5000) {
                    System.out.println("wrong format");
                    this.fis.close();
                    return false;
                }
                ++off;
            } while (this.buffer[off - 1] != 10 && this.buffer[off - 1] != 13);
        }
        catch (Exception ex6) {
            return false;
        }
        if (this.columns < 5 || this.columns > 500) {
            System.out.println("wrong format");
            try {
                this.fis.close();
            }
            catch (Exception ex3) {
                System.out.println("in setReadFile()[3]: " + ex3.toString());
            }
            return false;
        }
        this.column_names = new String[this.columns];
        this.starts = new int[this.columns];
        this.lengths = new int[this.columns];
        int n = 0;
        this.assays = 0;
        this.atEnd = false;
        this.atBeg = false;
        for (int i = 0; i < this.columns; ++i) {
            final int offset = n;
            while (this.buffer[n] != 9 && (this.buffer[n] != 10 & this.buffer[n] != 13)) {
                ++n;
            }
            try {
                this.column_names[i] = new String(this.buffer, offset, n - offset, "US-ASCII");
            }
            catch (Exception ex4) {
                System.out.println("in setReadFile()[4]: " + ex4.toString());
                return false;
            }
            if (this.column_names[i].compareToIgnoreCase("PROBE") == 0 || this.column_names[i].compareToIgnoreCase("PROBE_ID") == 0) {
                this.probe_col = i;
            }
            if (this.column_names[i].compareToIgnoreCase("GENE") == 0 || this.column_names[i].compareToIgnoreCase("GENE_ID") == 0) {
                this.geneID_col = i;
            }
            if (this.column_names[i].toUpperCase().endsWith("ASSAY_NORMALIZED_SIGNAL")) {
                this.atEnd = true;
                ++this.assays;
            }
            if (this.column_names[i].toUpperCase().startsWith("ASSAY_NORMALIZED_SIGNAL")) {
                this.atBeg = true;
                ++this.assays;
            }
            ++n;
        }
        if (this.atBeg == this.atEnd) {
            utils.error("not supported headline format");
            return false;
        }
        System.out.println("assay name at " + (this.atBeg ? "start" : "end"));
        if (this.assays == 0) {
            try {
                this.fis.close();
            }
            catch (Exception ex5) {
                System.out.println("in setReadFile()[5]: " + ex5.toString());
            }
            System.out.println("no assay found");
            return false;
        }
        if (this.probe_col == -1 || this.geneID_col == -1) {
            utils.error("column missing");
            return false;
        }
        this.assay_names = new String[this.assays];
        int n2 = 0;
        if (this.atEnd) {
            for (int j = 0; j < this.assays; ++j) {
                while (!this.column_names[n2].toUpperCase().endsWith("ASSAY_NORMALIZED_SIGNAL")) {
                    ++n2;
                }
                this.assay_names[j] = this.column_names[n2].substring(0, this.column_names[n2].length() - 24);
                ++n2;
            }
        }
        else {
            for (int k = 0; k < this.assays; ++k) {
                while (!this.column_names[n2].toUpperCase().startsWith("ASSAY_NORMALIZED_SIGNAL")) {
                    ++n2;
                }
                this.assay_names[k] = this.column_names[n2].substring(24);
                ++n2;
            }
        }
        this.ready_read = true;
        this.cursor = 0;
        this.filling = 0;
        if (!this.readRow()) {
            System.out.println(" read row failure");
            return false;
        }
        return true;
    }
    
    public String[] getAssayNames() {
        final String[] array = new String[this.assay_names.length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new String(this.assay_names[i]);
        }
        return array;
    }
    
    public int getNumberOfAssays() {
        return this.assays;
    }
    
    public boolean setAssay(final int n) {
        if (n < 0 || n >= this.assays) {
            return false;
        }
        this.signal_col = -1;
        this.flags_col = -1;
        this.cv_col = -1;
        if (this.assays == 1) {
            this.assay_names[0] = new String("");
        }
        if (this.atEnd) {
            for (int i = 0; i < this.columns; ++i) {
                if (this.column_names[i].startsWith(this.assay_names[n])) {
                    if (this.column_names[i].toUpperCase().endsWith("ASSAY_NORMALIZED_SIGNAL")) {
                        if (this.signal_col != -1) {
                            System.out.println("signal column key not unique");
                            return false;
                        }
                        this.signal_col = i;
                    }
                    if (this.column_names[i].endsWith("FLAGS")) {
                        if (this.flags_col != -1) {
                            System.out.println("flags column key not unique");
                            return false;
                        }
                        this.flags_col = i;
                    }
                    if (this.column_names[i].endsWith("CV")) {
                        if (this.cv_col != -1) {
                            System.out.println("cv column key not unique");
                            return false;
                        }
                        this.cv_col = i;
                    }
                }
            }
        }
        else {
            for (int j = 0; j < this.columns; ++j) {
                if (this.column_names[j].endsWith(this.assay_names[n])) {
                    if (this.column_names[j].toUpperCase().startsWith("ASSAY_NORMALIZED_SIGNAL")) {
                        if (this.signal_col != -1) {
                            System.out.println("signal column key not unique");
                            return false;
                        }
                        this.signal_col = j;
                    }
                    if (this.column_names[j].toUpperCase().startsWith("FLAGS")) {
                        if (this.flags_col != -1) {
                            System.out.println("flags column key not unique");
                            return false;
                        }
                        this.flags_col = j;
                    }
                    if (this.column_names[j].toUpperCase().startsWith("CV")) {
                        if (this.cv_col != -1) {
                            System.out.println("cv column key not unique");
                            return false;
                        }
                        this.cv_col = j;
                    }
                }
            }
        }
        System.out.println("probe col=" + this.probe_col + ", signal col=" + this.signal_col + ", var col=" + this.cv_col);
        if (this.signal_col == -1 || this.cv_col == -1 || this.flags_col == -1) {
            System.out.println("column keys missing");
            return false;
        }
        this.ready_write = true;
        System.out.println("fine: " + this.ready_write);
        return true;
    }
}
