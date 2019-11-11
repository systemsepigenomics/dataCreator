// 
// @author: systemsepigenomics
// 

package acecreator;

import aceStyle.ace_progress;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public abstract class TableFile
{
    byte[] buffer;
    int[] starts;
    int[] lengths;
    String[] column_names;
    String[] assay_names;
    FileInputStream fis;
    FileOutputStream fos;
    int columns;
    int assays;
    int geneID_col;
    int signal_col;
    int flags_col;
    int cv_col;
    boolean ready_read;
    boolean ready_write;
    boolean f_end;
    boolean line_has_data;
    boolean all_data_read;
    static int BUFFER_SIZE;
    int row_start;
    int data_remain;
    int cursor;
    boolean lengthtwo;
    int row_index;
    int filling;
    
    public TableFile() {
        this.ready_read = false;
        this.ready_write = false;
        this.buffer = new byte[TableFile.BUFFER_SIZE];
        this.row_index = 0;
    }
    
    public TableFile(final File readFile) {
        this();
        this.setReadFile(readFile);
    }
    
    public boolean readRow() {
        if (!this.ready_read || this.f_end) {
            return false;
        }
        int n = 0;
        int i = 0;
        this.row_start = this.cursor;
        this.starts[0] = this.row_start;
        while (true) {
            if (this.cursor == this.filling) {
                for (int j = 0; j <= i; ++j) {
                    this.starts[j] -= this.row_start;
                }
                final int n2 = this.cursor - this.row_start;
                for (int k = 0; k < n2; ++k) {
                    this.buffer[k] = this.buffer[k + this.row_start];
                }
                this.filling = n2;
                int read;
                try {
                    read = this.fis.read(this.buffer, n2, TableFile.BUFFER_SIZE - n2);
                }
                catch (Exception ex) {
                    System.out.println(ex.toString());
                    System.out.println("file read error in data refresh in TableFile");
                    System.out.println("data remain: " + this.data_remain);
                    return false;
                }
                if (read == -1) {
                    try {
                        this.fis.close();
                    }
                    catch (Exception ex2) {
                        System.out.println(ex2.toString());
                    }
                    this.f_end = true;
                    if (this.cursor - this.row_start < 3) {
                        this.line_has_data = false;
                    }
                    else {
                        this.line_has_data = true;
                    }
                    return true;
                }
                this.filling += read;
                this.cursor -= this.row_start;
                this.row_start = 0;
            }
            if (this.buffer[this.cursor] == 9) {
                if (i == this.columns - 1) {
                    this.lengths[i] = n;
                    System.out.println("too many tabs: " + i + " , " + this.columns + " columns, row " + this.row_index);
                }
                else {
                    this.lengths[i] = n;
                    n = -1;
                    ++i;
                    this.starts[i] = this.cursor + 1;
                }
            }
            else if (this.buffer[this.cursor] == 10 || this.buffer[this.cursor] == 13) {
                if (i == 0 && n == 0) {
                    ++this.starts[0];
                    ++this.cursor;
                    continue;
                }
                this.lengths[i] = n;
                ++this.cursor;
                ++this.row_index;
                return true;
            }
            ++this.cursor;
            ++n;
        }
    }
    
    public abstract boolean writeRow();
    
    public abstract boolean writeToFile(final File p0, final ace_progress p1);
    
    public abstract boolean setReadFile(final File p0);
    
    static {
        TableFile.BUFFER_SIZE = 90000;
    }
}
