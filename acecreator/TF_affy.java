// 
// @author: systemsepigenomics
// 

package acecreator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import aceStyle.ace_progress;
import java.io.File;

class TF_affy extends TableFile
{
    public boolean writeRow() {
        if (!this.ready_write) {
            return false;
        }
        try {
            this.fos.write(this.buffer, this.starts[this.geneID_col], this.lengths[this.geneID_col]);
            this.fos.write(9);
            this.fos.write(this.buffer, this.starts[this.signal_col], this.lengths[this.signal_col]);
            this.fos.write(9);
            this.fos.write(this.buffer, this.starts[this.cv_col], this.lengths[this.cv_col]);
            this.fos.write(13);
            this.fos.write(10);
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
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
        ace_progress.setPart(0.0);
        try {
            this.fos.write(new String("PROBE").getBytes("US-ASCII"));
            this.fos.write(9);
            this.fos.write(new String("SIGNAL").getBytes("US-ASCII"));
            this.fos.write(9);
            this.fos.write(new String("P_VALUE").getBytes("US-ASCII"));
            this.fos.write(13);
            this.fos.write(10);
        }
        catch (Exception ex2) {
            return false;
        }
        if (!this.writeRow()) {
            return false;
        }
        while (this.readRow()) {
            if (this.f_end) {
                if (this.line_has_data && !this.writeRow()) {
                    return false;
                }
                try {
                    this.fos.flush();
                    this.fos.close();
                }
                catch (Exception ex3) {}
                ace_progress.setPart(0.0);
                return true;
            }
            else {
                if (!this.writeRow()) {
                    return false;
                }
                if (this.row_index % 300 != 0) {
                    continue;
                }
                ace_progress.setPart(this.row_index / 30000.0);
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
            catch (Exception ex3) {}
        }
        this.f_end = false;
        this.geneID_col = -1;
        this.signal_col = -1;
        this.cv_col = -1;
        try {
            this.fis = new FileInputStream(file);
        }
        catch (Exception ex) {
            System.out.println(ex.toString());
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
        catch (Exception ex4) {
            return false;
        }
        if (this.columns < 3 || this.columns > 500) {
            System.out.println("wrong format");
            try {
                this.fis.close();
            }
            catch (Exception ex2) {
                System.out.println(ex2.toString());
            }
            return false;
        }
        this.column_names = new String[this.columns];
        this.starts = new int[this.columns];
        this.lengths = new int[this.columns];
        int n = 0;
        this.assays = 0;
        for (int i = 0; i < this.columns; ++i) {
            final int offset = n;
            while (this.buffer[n] != 9 && this.buffer[n] != 10 && this.buffer[n] != 13) {
                ++n;
            }
            try {
                this.column_names[i] = new String(this.buffer, offset, n - offset, "US-ASCII");
            }
            catch (Exception ex5) {
                return false;
            }
            if (i == 0) {
                if (this.column_names[i].compareTo("") != 0) {
                    System.out.println("first column does not match empty string : " + this.column_names[i]);
                    return false;
                }
                this.geneID_col = i;
            }
            if (this.column_names[i].endsWith("_Signal")) {
                if (this.signal_col != -1) {
                    System.out.println("more than one column with suffix matching \"_Signal\" found at index " + i);
                    return false;
                }
                this.signal_col = i;
            }
            if (this.column_names[i].endsWith("_Detection p-value")) {
                if (this.cv_col != -1) {
                    System.out.println("more than one column with suffix matching \"_Detection p-value\" found at index " + i);
                    return false;
                }
                this.cv_col = i;
            }
            ++n;
        }
        if (this.signal_col == -1 || this.cv_col == -1 || this.geneID_col == -1) {
            System.out.println("column(s) not found");
            return false;
        }
        System.out.println("gene id found at column " + this.geneID_col);
        System.out.println("signal  found at column " + this.signal_col);
        System.out.println("MOQ     found at column " + this.cv_col);
        this.ready_read = true;
        this.ready_write = true;
        this.cursor = 0;
        this.filling = 0;
        return this.readRow();
    }
}
