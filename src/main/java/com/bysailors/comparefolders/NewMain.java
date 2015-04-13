/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bysailors.comparefolders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rodrigoa
 */
public class NewMain extends Thread{

    private String dir1, dir2;
    private folderNavigator nav;
    private int cantArchivos = 0;
    private boolean enProgreso = false;
    private boolean enProgreso2 = false;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        
        validarParametros(args);
        validarArchivos(new File(args[0]), new File(args[1]));
                       
        NewMain iniciar = new NewMain();
        iniciar.dir1 = args[0];
        iniciar.dir2 = args[1];
        iniciar.calcularCantidadArchivos(iniciar);

        NewMain iniciar2 = new NewMain();
        iniciar2.nav = iniciar.nav;    
        iniciar2.dir1 = iniciar.dir1;
        iniciar2.dir2 = iniciar.dir2;
        iniciar2.iniciarComparacion(iniciar2);            
    }
    
    private static void validarArchivos(File dir1, File dir2)
    {
        try
        {
                if (!dir1.exists() || !dir2.exists())
            {
                throw new FileNotFoundException("el directorio especificado no existe.");            
            } 
                
                if (!dir1.isDirectory() || !dir2.isDirectory())
                {
                    throw new IOException("El directorio especificado no es un directorio válido.");
                }
        } catch(Exception Ex)
        {
            System.out.println("");
            System.out.println(Ex.getMessage());           
            showUsage();
            System.exit(1);
        }         
    }
    
    private static void showUsage()
    {
        // si el primer parámetro no es -Help y la cantidad de parametros no es 2, entonces muestro el uso.
        System.out.println("");
        System.out.println("Usage:");
        System.out.println("------");
        System.out.println("compareFolder [Directorio1] [Directorio2]");
        System.out.println("");
        System.out.println("Busca recursivamente los nombres de archivos que se repiten en [Directorio1] y [Directorio2] y"
                + "compara el contenido de los mismos."); 
        System.out.println("El output es la lista de archivos que están distintos.");
        System.out.println("");
    }  
    
    private static void validarParametros(String[] args)
    {
        try
        {
            if (args.length != 2)
                throw new Exception("Cantidad de parámetros incorrecta.");           
            
        } 
        catch (Exception Ex)
        {
            System.out.println("");
            System.out.println(Ex.getMessage());           
            showUsage();
            System.exit(1);
        }
        
    }
    
    
    private void calcularCantidadArchivos(NewMain clase) throws FileNotFoundException
    {
        System.out.print("Calculando cantidad de archivos a comparar.");
                  
        enProgreso2 = true;
        clase.start();
        while (enProgreso2)
        {
            showProgress();
        }        
                
        System.out.println("Se compararan " + cantArchivos + " archivos.");
    }
    private void iniciarComparacion(NewMain clase) throws FileNotFoundException
    {
        //Muestra la cantidad de archivos a comparar.
        //inicia el Thread y comienza con la comparación.
        //Muestra el progreso de la comparación hasta que termine.                    
        System.out.println("Iniciando comparación.");
        enProgreso = true;
        clase.start();
        
        //hasta que no termine muestro el progreso
        while (enProgreso)
        {
            showProgress();
        }
        
    }
    
    private void showProgress() 
    {
        try
        {
            System.out.print(".");
            Thread.sleep(500);
        }
        catch(InterruptedException ex)
        {
              System.out.println(ex.toString());
        }
        
    }
    
    @Override
    public void run()
    {        
        try {
            if (enProgreso2)
            {
                nav = new folderNavigator(new File (dir1), new File(dir2)); 
                cantArchivos = nav.getNumFilesToCompare();
                System.out.println("");
                enProgreso2 = false;
            } else
            {
            Iterator distintos = nav.getDifferentFilesInFolders().entrySet().iterator();
            while (distintos.hasNext())
            {
                Map.Entry archivos = (Map.Entry)distintos.next();
                System.out.println(archivos.getKey() + " != " + archivos.getValue());
            }       
            }            
        } catch (IOException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            enProgreso = false;         
            enProgreso2 = false;
        }
    }
    
}
