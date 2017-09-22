/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorspace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
/**
 *
 * @author duong
 */
public class VectorSpace {
File directory=new File("");
String strS=directory.getAbsolutePath()+"\\src\\Cranfield";
String strS1=directory.getAbsolutePath()+"\\src\\Data\\Index\\";
String strS2=directory.getAbsolutePath()+"\\src\\Posting\\Postingtest.txt";
String strDemo=directory.getAbsolutePath()+"\\src\\Data\\KeyWord\\1.txt";
String strDictionary=directory.getAbsolutePath()+"\\src\\Dictionary\\";
String strPosting=directory.getAbsolutePath()+"\\src\\Posting\\";
String strquery=directory.getAbsolutePath()+"\\src\\Data\\query.txt";
 String strRES1=directory.getAbsolutePath()+"\\src\\RES\\";
 String strKQRES=directory.getAbsolutePath()+"\\src\\KQRES\\";
 String strKQRES2=directory.getAbsolutePath()+"\\src\\KQRES2\\";
 String strKQP=directory.getAbsolutePath()+"\\src\\KQP\\KQP.txt";
 String strKQR=directory.getAbsolutePath()+"\\src\\KQP\\KQR.txt";
 String strKQF=directory.getAbsolutePath()+"\\src\\KQP\\KQF.txt";
    
  
    /**
     * @param args the command line arguments
     */
    File file=new File(strDemo); 
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        VectorSpace VT= new VectorSpace();
      //  VT.tachtu();
     //   VT.Dictionary();   
     //   VT.Posting();
  // VT.querytest();
     VT.tinhtfidf();
    VT.xulyquery();
     VT.tinhw();
     VT.Query();
      VT.TestRes();
      VT.KQRES();
    }
    public void tachtu(){
        
         File forder = new File(strS);
            File[] listOfFiles = forder.listFiles();
            FileText ft = new FileText();

            for (int i = 0; i < listOfFiles.length; i++) {
                ft.tachtu(listOfFiles[i],strS1);             
           }
    }
    public void Dictionary( ){
         FileText ft = new FileText();
          String    strFile=strDictionary+"Dictionary.txt"; 
         ft.Dictionary(strFile);

    }
    public void AddText( ){
         FileText ft = new FileText();
         File file= new File(strDemo);
       //  ft.thongkeKeyWord(file,file);

    }
    public void xulyquery(){
          FileText ft = new FileText();
          ft.calcDocument();
    }
    public  void tinhtfidf()throws IOException {
          FileText ft = new FileText();
          ft.createtfidf();
    }
    public void Posting(){
        
         File forder = new File(strS1);
     //   String  strS1 = strS.getPath().substring(0, strS.getPath().lastIndexOf('\\') + 1);
           // File[] listOfFiles = forder.listFiles();
            FileText ft = new FileText();
            String    strFile=strPosting+"Posting.txt"; 
           
              
               ft.Posting(strFile);
    }
    public void tinhw(){
      FileText ft = new FileText();
      ft.weighttfidf();
    }
    
    public void Query(){
    FileText ft = new FileText();
      //ft.Query();
      ft.normQuery();
      ft.calcWeightQuery();
      ft.DistanstSim();
      ft.DistanstSimsmart();
    }
    public void TestRes(){
        FileText ft = new FileText();
       File file = new File(strRES1);
        ft.RES(file);
       // ft.TempREs();
    }
       public void KQRES(){
        FileText ft = new FileText();
      // File file = new File(strRES1);
        ft.TempREs(strKQRES);
         ft.TempREs2(strKQRES2);
         ft.sokhop();
         ft.sokhop2();
         ft.tinhdochinhxac(strKQP);
         ft.tinhdophu(strKQR);
         ft.tinhdoF(strKQF);
    }
}
