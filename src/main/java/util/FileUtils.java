/*
 * Author Stuart Davies (802996013)
 * Local Test Server development 15/09/2017
 * BT eCommerce 
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Utility to manage the file system
 *
 * @author - Stuart Davies
 */
public class FileUtils {

    /**
     * Recursive directory crawler
     *
     * @param f Start in this directory
     * @param infileTypes Filter the files returned (eg .java)
     * @param recursive To recurse or not to recurse?
     * @return List of files
     */
    public static List<File> getFileList(File f, List<String> infileTypes, boolean recursive) {
        List<File> files = new ArrayList<>();
        if ((infileTypes == null) || (infileTypes.isEmpty())) {
            listFilesRecursive(f, files, null, recursive);
        } else {
            listFilesRecursive(f, files, new FileExtFilter(infileTypes), recursive);
        }
        return files;
    }

    private static void listFilesRecursive(File f, List<File> list, FileExtFilter filter, boolean recursive) {
        File[] files;
        if (filter == null) {
            files = f.listFiles();
        } else {
            files = f.listFiles(filter);
        }
        for (File fil : files) {
            if (fil.isDirectory()) {
                if (recursive) {
                    listFilesRecursive(fil, list, filter, recursive);
                }
            } else {
                if (fil.isFile()) {
                    list.add(fil);
                }
            }
        }
    }

    /**
     * Return the MD5 checksum of the contents of a file
     *
     * @param file The file
     * @return The MD5 checksum
     */
    public static String StringMD5(File file) {
        String checksum = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int numOfBytesRead;
            while ((numOfBytesRead = fis.read(buffer)) > 0) {
                md.update(buffer, 0, numOfBytesRead);
            }
            byte[] hash = md.digest();
            checksum = new BigInteger(1, hash).toString(16); //don't use this, truncates leading zero
        } catch (IOException | NoSuchAlgorithmException ex) {
            throw new FileUtilsException("MD5 in file ["+file.getAbsolutePath()+"] failed", ex);
        }
        return checksum;
    }

    /**
     * Unpack the contents of a ZIP or JAR file
     *
     * @param zFil The ZIP or JAR file
     * @param list The list to be updated (added to)
     */
    public static void getZipFileList(File zFil, List<ContainerFile> list) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(zFil);
            Enumeration<? extends ZipEntry> e = zip.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze = e.nextElement();
                if (!ze.isDirectory()) {
                    list.add(new ContainerFile(zFil.getAbsolutePath(), ze.getName()));
                }
            }
        } catch (IOException ex) {
            throw new FileUtilsException("Zip file ["+zFil.getAbsolutePath()+"] List failed", ex);
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException ex) {
                    throw new FileUtilsException("Zip file ["+zFil.getAbsolutePath()+"] List failed to close", ex);
                }
            }
        }
    }

    /**
     * Read a file from the resource directory relative to a specific class.
     *
     * @param fileName The name of the file. Use '/' to indicate the root of the
     * package
     * @param clazz The package this class is in will be the package searched.
     * @return The contents of the file
     */
    public static String getResource(String fileName, Class clazz) {
        StringBuilder result = new StringBuilder("");
        try {
            InputStream is = clazz.getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundUtilsException("Failed to find resource file[" + fileName + "]");
            }
            while (is.available() > 0) {
                result.append((char) is.read());
            }
        } catch (IOException e) {
            throw new FileUtilsException("Failed to read resource file[" + fileName + "]", e);
        }
        return result.toString();
    }

    /**
     * Optimal read the contents of a file
     *
     * @param fil The file to be read
     * @return The contents
     * @throws IOException If the file cannot be read
     */
    public static String loadFile(File fil) throws IOException {
        return new String(Files.readAllBytes(fil.toPath()));
    }

    /**
     * Optimal read the contents of a file as a list
     *
     * @param fil The file to be read
     * @return The contents of the file
     * @throws IOException If the file cannot be read
     */
    public static List<String> loadFileAsLines(File fil) throws IOException {
        try {
            return Files.readAllLines(fil.toPath(), Charset.forName("Cp1252"));
        } catch (MalformedInputException ex) {
            /**
             * Ignore ex and default to to UTF-8
             */
            return Files.readAllLines(fil.toPath(), Charset.forName("UTF-8"));
        }
    }

    /**
     * Optimal read the contents of a file through a basic filter
     *
     * @param fil The file to be read
     * @return The contents filtered
     * @throws IOException If the file cannot be read
     */
    public static List<LineWithNumber> loadFileAsLines(File fil, List<String> filter) throws IOException {
        return filter(loadFileAsLines(fil), filter);
    }

    /**
     * Write a file and overwrite the existing file
     *
     * @param s New contents of the file
     * @param fil The file
     */
    public static void writeFileOverwrite(String s, File fil) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fil);
            fos.write(s.getBytes());
        } catch (IOException ex) {
            throw new FileUtilsException("Unable to write file [" + fil.getAbsolutePath() + "]", ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    throw new FileUtilsException("Unable to close file [" + fil.getAbsolutePath() + "]", ex);
                }
            }
        }
    }
    /**
     * Write a file and overwrite the existing file
     *
     * @param s New contents of the file
     * @param fil The file
     */
    public static void writeFileAppend(String s, File fil) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fil, true);
            fos.write(s.getBytes());
        } catch (IOException ex) {
            throw new FileUtilsException("Unable to append file [" + fil.getAbsolutePath() + "]", ex);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    throw new FileUtilsException("Unable to close file [" + fil.getAbsolutePath() + "]", ex);
                }
            }
        }
    }

    /**
     * Write a file but back it up first
     *
     * @param s New contents of the file
     * @param fil The file
     * @throws IOException If the file cannot be backed up
     */
    public static void backupAndWriteFile(String s, File fil) throws IOException {
        File temp = new File(fil.getAbsolutePath() + ".temp");
        if (temp.exists()) {
            if (!temp.delete()) {
                throw new IOException("Unable to delete previous temporary file [" + temp.getAbsolutePath() + "]");
            }
        }
        writeFileOverwrite(s, temp);
        File back = new File(fil.getAbsolutePath() + ".back");
        if (back.exists()) {
            if (!back.delete()) {
                throw new IOException("Unable to delete previous backup file [" + back.getAbsolutePath() + "]");
            }
        }
        fil.renameTo(back);
        temp.renameTo(fil);
    }

    /**
     * Optimal copy the contents of a file
     *
     * @param fil The file to be copied
     * @param file The file to copy too.
     * @throws IOException If the file cannot be copied
     */
    public static void copyFile(File fil, File file) throws IOException {
        Files.copy(fil.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Strip a string down to a proper file name.
     *
     * @param name The file name
     * @return The proper file name
     */
    public static String formatAsFileName(String name) {
        StringBuilder sb = new StringBuilder();
        String n = name.trim();
        if (n.length() == 0) {
            return "ANY";
        }
        for (char c : n.toCharArray()) {
            if (c <= ' ') {
                sb.append('_');
            } else {
                if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || ((c >= '0') && (c <= '9')) || (c == '-') || (c == '_')) {
                    sb.append(c);
                } else {
                    sb.append("-");
                }
            }
        }
        return sb.toString();
    }

    /**
     * Does the file with this name exist!
     * @param fileName The name of the file as a string
     * @return To exist or not to exist!
     */
    public static boolean exists(String fileName) {
        if ((fileName == null) || (fileName.trim().length() == 0)) {
            return false;
        }
        return exists(newFile(fileName));
    }

    /**
     * Does the file exist! (not a lot of point to this!)
     * @param fileName The file 
     * @return To exist or not to exist!
     */
    public static boolean exists(File f) {
        return f.exists();
    }

    /**
     * Create a file.
     * If you create a file with a file name such as 'a.txt' then the file name returned is 'a.txt'.
     * Doing this will always return the full path of the file
     * @param fileName The file name as a string
     * @return 
     */
    public static File newFile(String fileName) {
        File f = new File(fileName);
        return new File(f.getAbsolutePath());
    }

    private static List<LineWithNumber> filter(List<String> lines, List<String> filter) {
        List<LineWithNumber> linesOut = new ArrayList<>();
        int lineNum = 0;
        for (String line : lines) {
            lineNum++;
            String l = line.trim().toLowerCase();
            if (l.length() > 1) {
                int count = 0;
                for (String searchString : filter) {
                    String searchFor = searchString.trim();
                    if (searchFor.trim().equals("")) {
                        count++;
                    } else {
                        if (searchFor.startsWith("*")) {
                            if (l.endsWith(searchFor.substring(1))) {
                                count++;
                            }
                        } else {
                            if (searchFor.endsWith("*")) {
                                if (l.startsWith(searchFor.substring(0, searchFor.length() - 1))) {
                                    count++;
                                }
                            } else {
                                if (l.contains(searchFor)) {
                                    count++;
                                }
                            }
                        }
                    }
                }
                if (count == 3) {
                    linesOut.add(new LineWithNumber(lineNum, line));
                }
            }
        }
        return linesOut;
    }

}
