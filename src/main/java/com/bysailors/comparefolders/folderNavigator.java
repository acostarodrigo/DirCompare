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

import static com.bysailors.comparefolders.compareFiles.compareFiles;
import static com.bysailors.comparefolders.compareFiles.getTreeList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class folderNavigator {    
    private File folder1, folder2;    
    private Set<File> tree1, tree2;    
    private Map<String, String> filenamesToCompare;
    public int numFilesToCompare;
    
    public folderNavigator (File Folder1 ,File Folder2) throws FileNotFoundException{
        if (!Folder1.exists() || !Folder2.exists())
            throw new FileNotFoundException("The folder specified does not exists");
        else
        {
            if (!Folder1.isDirectory() || !Folder2.isDirectory())
                throw new FileNotFoundException ("The specified name is not a directory.");
            else                
                {
                    this.folder1 = Folder1;
                    this.folder2 = Folder2;
                }
        }
        this.getMatchingNames();
    }

    public int getNumFilesToCompare() {
        return numFilesToCompare;
    }
    
    
    
//    private Set<File> getFolderTree (File root){
//        //devuelve el arbol de archivos para el root que se le pase        
//        for (File archivo : root.listFiles()){
//            if (archivo.isFile())
//                tree.add(root);
//            else
//                getFolderTree(archivo);                    
//        }        
//        return tree;
//    }
    
    private synchronized void getMatchingNames(){
        // obtengo la lista de archivos de ambos directorios        
        tree1 = getTreeList(folder1);        
        tree2 = getTreeList(folder2);
        
        //navego ambos árboles buscando por nombres. Si encuentro nombres iguales, entonces meto ambos en el mapa absolutPath1 - absolutPath2
        filenamesToCompare = new HashMap<String, String>();
        for (File archivo1 : tree1){
            //System.out.println("Archivo 1: " + archivo1.getName());
            for (File archivo2 : tree2){
                //System.out.println("Archivo 2: " + archivo2.getName());
                if (archivo1.getName().toString().matches(archivo2.getName()))
                {
                    filenamesToCompare.put(archivo1.getAbsolutePath(), archivo2.getAbsolutePath());
                    //System.out.println("Archivo igual. " + archivo2.getName());
                    break;
                }
            }
        }
        
        //seteo el tamaño del mapa con los archivos a comparar
        this.numFilesToCompare = this.filenamesToCompare.size();
    }
    
    public synchronized Map<String, String>  getDifferentFilesInFolders() throws IOException{        
        Iterator it = filenamesToCompare.entrySet().iterator();
        Map<String, String> distintos = new HashMap<String, String>();
        while (it.hasNext())
        {
            Map.Entry archivos = (Map.Entry)it.next();
            File archivo1 = new File(archivos.getKey().toString());
            File archivo2 = new File(archivos.getValue().toString());
            
            if (!compareFiles(archivo1,archivo2))
                distintos.put(archivo1.getAbsolutePath(), archivo2.getAbsolutePath());
        }
        
        return distintos;
    }
    
}
