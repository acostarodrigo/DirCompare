/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bysailors.comparefolders;

/**
 *
 * @author rodrigoa
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;


class compareFiles {
    public static boolean compareFiles(File File1, File File2) throws FileNotFoundException, IOException{
        if (!File1.exists() || !File2.exists())
            throw new FileNotFoundException ("the file specified does not exists.");
        else        
            return FileUtils.contentEquals(File1, File2);
    }
    
    public static Set<File> getTreeList(File root)
    {
        Set<File> ret = new HashSet<File>(FileUtils.listFiles(root, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
//        for (File archivo : ret)
//        {
//            System.out.println(archivo.getAbsolutePath());
//        }
        return ret;
    }
    
    
    
}
