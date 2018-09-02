/*
 * Author Stuart Davies (802996013)
 * Local Test Server development 15/09/2017
 * BT eCommerce 
 */
package util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Used by FileUtils to filter the files returned from recursive directory scans
 * 
 * @author - Stuart Davies
 */
public class FileExtFilter implements FileFilter {

    private final List<String> ext;

    public FileExtFilter(List<String> in) {
        ext = new ArrayList<>();
        for (String e : in) {
            ext.add(e.toLowerCase());
        }
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String s = file.getName().toLowerCase();
        for (String e : ext) {
            if (s.endsWith(e)) {
                return true;
            }
        }
        return false;
    }

}
