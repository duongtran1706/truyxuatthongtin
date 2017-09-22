/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorspace;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.util.*;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;  
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Map.Entry;

public class FileText {
    /**
     * properties of class file text
     */   
    String strS;
    String strStopWord;
    String strIndex;
    String strQuery;
    String strQueryResult;
    String strKeyWord;
    String strDictionary;
    String strKeyWord1;
    String strRES;
    StringTokenizer myTokens;
    Scanner scan=null;
    Scanner scanS=null;
     Scanner scanSC=null;
    boolean checkCancelAll=false;
    String strStest;
    public static boolean DESC = false;
    StemmerText ST=new StemmerText();
      ArrayList<ArrayList<String>> result = new ArrayList<>();
     ArrayList<ArrayList<String>> posting = new ArrayList<>();
   static TreeMap<String,Double>idf= new TreeMap<String,Double>();
   static HashMap<String,Double> calcDocument = new HashMap<String,Double>();
    static HashMap<String,Integer> calcDocument1 = new HashMap<String,Integer>();
     static HashMap<String,Double> calcDocumentQuery = new HashMap<String,Double>();
      static HashMap<String,Integer> calcDocumentQuery1 = new HashMap<String,Integer>();
   static HashMap<String,HashMap<String,Double>> tfidfweightQuery = new HashMap<String,HashMap<String,Double>>();
   static HashMap<String,HashMap<String,Double>> tfidfweight = new HashMap<String,HashMap<String,Double>>();
   static HashMap<String,HashMap<String,Double>> smartweight = new HashMap<String,HashMap<String,Double>>();
    static HashMap<String,HashMap<String,Double>> smartweightQuery = new HashMap<String,HashMap<String,Double>>();
    static HashMap<String,HashMap<String,Double>> DistantSim = new HashMap<String,HashMap<String,Double>>();
     static HashMap<String,HashMap<String,Double>> DistantSimsmart = new HashMap<String,HashMap<String,Double>>();
    static HashMap<String,ArrayList<Integer>> Res = new HashMap<String,ArrayList<Integer>>();
    static HashMap<String,ArrayList<Integer>> ResultDic = new HashMap<String,ArrayList<Integer>>();
    static HashMap<String,ArrayList<Integer>> ResultDicSim = new HashMap<String,ArrayList<Integer>>();
    static HashMap<Integer,Integer> sokhop = new HashMap<Integer,Integer>();
      static HashMap<Integer,Integer> sokhop2 = new HashMap<Integer,Integer>();
    public FileText()
    {
        
        File directory=new File("");
        strStest  =directory.getAbsolutePath()+"\\src\\Cranfield";
        strStopWord=directory.getAbsolutePath()+"\\src\\Data\\Stopwords\\659-English-stopwords.txt";
        strIndex=directory.getAbsolutePath()+"\\src\\Data\\Index\\";
        strKeyWord=directory.getAbsolutePath()+"\\src\\Data\\KeyWord\\Dictionary.txt";   
        strKeyWord1=directory.getAbsolutePath()+"\\src\\Posting\\";
        strRES=directory.getAbsolutePath()+"\\src\\RES\\";
        strDictionary=directory.getAbsolutePath()+"\\src\\Dictionary\\";
        strQuery=directory.getAbsolutePath()+"\\src\\Data\\Query\\";
        strQueryResult=directory.getAbsolutePath()+"\\src\\Data\\QueryResult\\";
    }    
    public boolean checkStopWords(String str)
    {      
        File fStopWords=new File(strStopWord);        
        try{
            scan=new Scanner(fStopWords);
        }
        catch(FileNotFoundException e)
        {
        }
        while(scan.hasNextLine())
        {
            Scanner sc=new Scanner(scan.nextLine());
            while(sc.hasNext())
            {
                this.strS=sc.next().toUpperCase();
                if(this.strS.equals(str.toUpperCase()))
                    return true;
            }
        }
        return false;
    } 
    public boolean checkWords(String strWord,File fFile)
    {       
        try{
            scan=new Scanner(fFile);
        }
        catch(FileNotFoundException e)
        {
        }
        while(scan.hasNextLine())
        {
            Scanner sc=new Scanner(scan.nextLine());
            while(sc.hasNext())
            {
                this.strS=sc.next();
                this.strS=formatString(this.strS);
                if(this.strS.equals(strWord.toUpperCase()))
                    return true;
            }
        }
        return false;
    }   
    public boolean checkStrFormat(String str)
    { 
        if(str.equals(".")==true)
            return false;
       for(int i=0;i<str.length();i++)
       {
           Character c=new Character(str.charAt(i));
           try{
               int n=Integer.parseInt(c.toString());
               return false;
           }
           catch(Exception e)
           {
           }           
       }    
       return true;
    }   
    
    public String formatString(String str)
    {
        str=str.replace(".", "");
        if(str.indexOf('\'')>0)
            str=str.substring(0, str.indexOf('\''));
        if(str.indexOf('/')>0)
            str=str.substring(0, str.indexOf('/'));
        str=str.replace("\"", "");
        str=str.replace("/", "");
        str=str.replace(",", "");
        str=str.replace("(", "");
        str=str.replace(")", "");
        str=str.replace(";", "");
        str=str.replace("'", "");
        str=str.replace("*", "");
        str=str.replace("=", "");
        str=str.replace(">", "");
        str=str.replace("<", "");
        str=str.replace("=", "");
        str=str.replace("[", "");
        str=str.replace("]", "");
        str=str.replace("=", "");
        return str;
    }
    public void tachtu(File file,String strindex){
         String strFile;
        strS=file.getPath();
        int index=0;       
      strFile=strindex+strS.substring(strS.lastIndexOf('\\'), strS.lastIndexOf('.'))+".txt";  
        File nFile=new File(strFile);
        
        if(nFile.exists()==true)
        {
            if(checkCancelAll==true)
                return;
            int reply=JOptionPane.showConfirmDialog(null, "The file "+strS.substring(strS.lastIndexOf('\\')+1, strS.lastIndexOf('.'))
                    +".txt already exists. Do you want to replace the existing file ?", "Existing file.", JOptionPane.YES_NO_CANCEL_OPTION);
            if(reply==JOptionPane.NO_OPTION)
                return;
            if(reply==JOptionPane.CANCEL_OPTION)
                checkCancelAll=true;
        }
        FileWriter fw=null;        
        try
        {
            fw=new FileWriter(nFile);
        }
        catch(Exception e)
        {
        }   
        try{
                    fw.close();
                }
                catch(Exception e)
                {
                } 
        ArrayList<String>Arrterm= new ArrayList<String>();
         try {
            FileInputStream fis = new FileInputStream(file);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line != null && !line.isEmpty()) {
                      StringTokenizer st = new StringTokenizer(line," ");  
                        while (st.hasMoreTokens()) {  
                           String temp= st.nextToken();
                           // temp=formatString(temp);
                           temp= temp.replaceAll("[^A-Za-z0-9]", "");
                            if(checkStopWords(temp)==true)
                                continue;
                            if(checkStrFormat(temp)==false)
                                continue;
                            if(temp.equals("")==true)
                                continue;
                            temp=ST.steming(temp);  
                           Arrterm.add(temp);
                        }  
                    }
                }
                //code tai đây
                                             
                 try
                {
                    fw=new FileWriter(nFile, true);
                }
                catch(Exception e)
                {
                }
                  for(int i=0;i<Arrterm.size();i++){
                      int count=1;
                    for(int j=i+1;j<Arrterm.size();j++){
                         if (Arrterm.get(i).equals(Arrterm.get(j))==true) {
                                count++;
                                 Arrterm.remove(j);
                                Arrterm.trimToSize();
                         }   
                    }
                    try{            
                    fw.write(Arrterm.get(i)+" "+count);
                    fw.write("\r\n");   
                }
                catch(Exception e)
                {                 
                }
                    
                  }
                    try{
                    fw.close();
                }
                catch(Exception e)
                {
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }

    public boolean checkFileOriginalExist(File file)
    {
        File directory=new File(strDictionary);
        File[] listOfFileOriginal=directory.listFiles();
        for(int i=0;i<listOfFileOriginal.length;i++)
        {
            if(listOfFileOriginal[i].equals(file)==true)
                return true;
        }
        return false;
    }
    
    public void copyFileToOriginalDirectory(File file)
    {
        if(checkFileOriginalExist(file)==false)
        {
            
        }
    }
    public void  Dictionary(String file){
           int index=0; 
        File nFile=new File(file);
        FileText ft = new FileText();          
         FileWriter fw=null;   
       
           File forder = new File(strIndex);
            File[] listOfFiles = forder.listFiles();
            String    strFile=strDictionary+"Dictionary.txt"; 
            for (int i = 0; i < listOfFiles.length; i++) {
              

                    try{
                        scan=new Scanner(listOfFiles[i]);
                    }
                    catch(Exception e)
                    {
                    }   
                    while(scan.hasNextLine())
                    {          
                         ArrayList<String> nodeList = new ArrayList<String>();
                        if(!scan.hasNext()) break;
                        String str=scan.next();
                        nodeList.add(str);
                        nodeList.add("1");
                        String strs= scan.next();
                         nodeList.add(strs);
                       
                        
                        result.add(nodeList);
                   }
            }
        try
        {
            fw=new FileWriter(nFile);
        }
        catch(Exception e)
        {
        }   
         try
        {
            fw.close();
        }
        catch(Exception e)
        {
        }
        String Filescan="";
        Filescan=ReadFile(file);
       ArrayList<ArrayList<String>> file1Split = result;
       ArrayList<ArrayList<String>> demo = new ArrayList<ArrayList<String>>();
       int n= file1Split.size();
       for(int i=0;i<file1Split.size();i++){
           
           for(int j=i+1;j<file1Split.size();j++){
                if (file1Split.get(i).get(0).equals(file1Split.get(j).get(0))==true) {
                    int temp= Integer.parseInt(file1Split.get(i).get(1))+Integer.parseInt(file1Split.get(j).get(1));                  
                  file1Split.get(i).get(1).replace(file1Split.get(i).get(1), String.valueOf(temp));
                   int temp2= Integer.parseInt(file1Split.get(i).get(2))+Integer.parseInt(file1Split.get(j).get(2));                  
                   file1Split.get(i).get(2).replace(file1Split.get(i).get(2), String.valueOf(temp2)); 
                     ArrayList<String> demo1 = new ArrayList<String>();
                  demo1.add(file1Split.get(i).get(0));
                  demo1.add(String.valueOf(temp));
                  demo1.add(String.valueOf(temp2));
                  file1Split.set(i, demo1);
                   file1Split.remove(j);
                  file1Split.trimToSize();      
                   
                }
                
         }

       }
            try
                  {
                      fw=new FileWriter(nFile,true);
                  }
                  catch(Exception e)
                  {
                  }  
      for(int i=0;i<file1Split.size();i++){
          String temp=file1Split.get(i).get(0)+" "+file1Split.get(i).get(1)+" "+file1Split.get(i).get(2);
    
          try
        {
            fw.write(temp);
            fw.write("\r\n");
        }
        catch(Exception e)
        {
        }   
      }
         try
        {
            fw.close();
        }
        catch(Exception e)
        {
        }  
    }
static String ReadFile(String fileName){
			
		try {
			return new String(Files.readAllBytes(Paths.get(fileName)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
static ArrayList<String> SplitFile(String str){
            String[]abc;
		 abc = str.split("\r\n");                             
		ArrayList<String> result = new ArrayList<>();
		for (String s : abc) {
			result.add(s);
		}
		return result;
	}     
static ArrayList<ArrayList<String>> SplitFileText(String str){
            String[]abc;
		 abc = str.split("\r\n");                             
		ArrayList<ArrayList<String>> result = new ArrayList<>();

		for (String s : abc) {
                   ArrayList<String> nodeList = new ArrayList<String>();
                    String[] temp= s.split(" ");
			//nodeList=temp;
                      for(int i=0;i<temp.length;i++){

                          nodeList.add(temp[i]);
                      }
                        result.add(nodeList);
		}
		return result;
	}           

	static String ConverArrayToString(ArrayList<String> arr){
		String str ="";
		for (int i = 0; i < arr.size(); i++) {
			if(i%3==0){
				str+="\n";
			}
			str+=arr.get(i)+" ";
		}
		return str;
	}

public void posting(File file,String fileDiC){
    String strTitle="";
        File f=new File(fileDiC);
        FileWriter fw=null;
        try{
            fw=new FileWriter(f);
            fw.close();
        }
        catch(Exception e)
        {
        }
        try{
            scan=new Scanner(file);
        }
        catch(Exception e)
        {
        }       
         //String name=file.getName();
        // name =name.substring(0, name.lastIndexOf('.'));
        while(scan.hasNextLine())
        {     
              try{
            fw=new FileWriter(f,true);
            }
            catch(Exception e)
            {
            }
            if(!scan.hasNext()) break;
            String str=scan.next(); 
            String strs= scan.next();
            String strs1= scan.next();
           
            try{
                fw.write(strs+" "+strs1);
                fw.write("\r\n");
            }
            catch(Exception e)
            {
            }
       }
        try{
            fw.close();
        }
        catch(Exception e)
        {
        }
    }
 
public void querytest(String strfile,File file){
      String strFile;
        strS=file.getPath();
        int index=0; 
        strFile=strIndex+strS.substring(strS.lastIndexOf('\\'), strS.lastIndexOf('.'))+".txt";        
        File nFile=new File(strFile);
        
        if(nFile.exists()==true)
        {
            if(checkCancelAll==true)
                return;
            int reply=JOptionPane.showConfirmDialog(null, "The file "+strS.substring(strS.lastIndexOf('\\')+1, strS.lastIndexOf('.'))
                    +".txt already exists. Do you want to replace the existing file ?", "Existing file.", JOptionPane.YES_NO_CANCEL_OPTION);
            if(reply==JOptionPane.NO_OPTION)
                return;
            if(reply==JOptionPane.CANCEL_OPTION)
                checkCancelAll=true;
        }
        FileWriter fw=null;        
        try
        {
            fw=new FileWriter(nFile);
        }
        catch(Exception e)
        {
        }      
        try{
            scan=new Scanner(file);
        }
        catch(FileNotFoundException e)
        {        
        }
        while(scan.hasNextLine())
        {
            Scanner s=null;          

        }
        try{
            fw.close();
        }
        catch(Exception e)
        {
        }
    }
    
public void  Dictionaryposting(String file){
           int index=0; 
        File nFile=new File(strKeyWord1);
       // FileText ft = new FileText();          
         FileWriter fw=null;        
        try
        {
            fw=new FileWriter(nFile,true);
        }
        catch(IOException e)
        {
        }      
        String Filescan="";
        Filescan=ReadFile(file);
       ArrayList<ArrayList<String>> file1Split = SplitFileText(Filescan);
       ArrayList<ArrayList<String>> demo = new ArrayList<ArrayList<String>>();
       int n= file1Split.size();
        
       for(int i=0;i<file1Split.size()-1;i++){
         try
        {
            fw.write(file1Split.get(i).get(0).toString()+" "+file1Split.get(i).get(1).toString()+" "+file1Split.get(i).get(2).toString());
            fw.write("\r\n");
        }
        catch(IOException e)
        {
        }  
         for(int j=i+1;j<file1Split.size();j++){
                if (file1Split.get(i).get(0).equals(file1Split.get(j).get(0))==true) {
                      try
                        {
                            fw.write(file1Split.get(i).get(0).toString()+" "+file1Split.get(j).get(1).toString()+" "+file1Split.get(j).get(2).toString());
                            fw.write("\r\n");
                        }
                        catch(IOException e)
                        {
                        }  
                 // file1Split.remove(j);
                 // file1Split.trimToSize();
                }
                
         }
          //  file1Split.remove(i);
               //   file1Split.trimToSize();
       }
         try
        {
            fw.close();
        }
        catch(IOException e)
        {
        }  
    }

public void Posting(String strFile){
        
         File forder = new File(strIndex);
          int index=0; 
        File nFile=new File(strFile);
       // FileText ft = new FileText();          
         FileWriter fw=null;        
        try
        {
            fw=new FileWriter(nFile);
        }
        catch(IOException e)
        {
        }  
          try
        {
            fw.close();
        }
        catch(IOException e)
        {
        }  
     //   String  strS1 = strS.getPath().substring(0, strS.getPath().lastIndexOf('\\') + 1);
            File[] listOfFiles = forder.listFiles();
            //FileText ft = new FileText();
           // String    strFile=strKeyWord1+"Posting.txt"; 
            for (int i = 0; i < listOfFiles.length; i++) {
                        try{
                     scan=new Scanner(listOfFiles[i]);
                 }
                 catch(Exception e)
                 {
                 }   
                 String name=listOfFiles[i].getName();
                  name =name.substring(0, name.lastIndexOf('.'));
                 while(scan.hasNextLine())
                 {      

                     if(!scan.hasNext()) break;
                     String str=scan.next(); 
                     String strs= scan.next();
                      ArrayList<String> nodeList = new ArrayList<String>();
                        nodeList.add(str);
                        nodeList.add(name);
                         nodeList.add(strs);
                       
                        
                        posting.add(nodeList);
                }
                    int n=0;
            }
         //   Dictionaryposting(strFile);
         
       ArrayList<ArrayList<String>> file1Split = posting;
       ArrayList<ArrayList<String>> demo = new ArrayList<ArrayList<String>>();
       int n= file1Split.size();
         try
        {
            fw=new FileWriter(nFile,true);
        }
        catch(IOException e)
        {
        } 
       for(int i=0;i<file1Split.size();i++){
         try
        {
            fw.write(file1Split.get(i).get(1).toString()+" "+file1Split.get(i).get(2).toString());
            fw.write("\r\n");
        }
        catch(IOException e)
        {
        }  
         for(int j=i+1;j<file1Split.size();j++){
                if (file1Split.get(i).get(0).equals(file1Split.get(j).get(0))==true) {
                      try
                        {
                            fw.write(file1Split.get(j).get(1).toString()+" "+file1Split.get(j).get(2).toString());
                            fw.write("\r\n");
                        }
                        catch(IOException e)
                        {
                        }  
                  file1Split.remove(j);
                  file1Split.trimToSize();
                }
                
         }
                file1Split.remove(i);
                file1Split.trimToSize();
       }
         try
        {
            fw.close();
        }
        catch(IOException e)
        {
        }  
         
    }
public void createtfidf(){
              File forder = new File(strStest);
            File[] listOfFiles = forder.listFiles();
            int lenght =listOfFiles.length;
          HashMap<String,Integer> dictionary = new HashMap<String,Integer>();
            String    strFile=strDictionary+"Dictionary.txt"; 
            int count =0;
            File FileDic =new File(strFile);
             try{
                        scan=new Scanner(FileDic);
                    }
                    catch(Exception e)
                    {
                    }   
                    while(scan.hasNextLine())
                    {          
                        count++;
                        if(count==4160)
                            count++;
                       if(!scan.hasNext()) break;
                        String str=scan.next();
                        String strs= scan.next();
                        String strs1= scan.next();
                        int temp=Integer.parseInt(strs);
                        double idf1=Math.log10(lenght/temp);
                        idf.put(str,idf1);
                   }
            } 

public void calcDocument(){
     File forder = new File(strIndex);
            File[] listOfFiles = forder.listFiles();//1400
            int lenght =listOfFiles.length;
          for(int i=0;i<lenght;i++){
             try{
                        scan=new Scanner(listOfFiles[i]);
                    }
                    catch(Exception e)
                    {
                    } 
             double calcDoc=0.0;
            //  double calcDoc2=0.0;
             int maxterm=0;
                    while(scan.hasNextLine())
                    {       
                      
                        if(!scan.hasNext()) break;
                        String str=scan.next();
                        String strs= scan.next();
                         int temp=Integer.parseInt(strs);
                         double temp1;
                         if (idf.containsKey(str)) {		
                         temp1=idf.get(str).doubleValue();
                         }else temp1=0;
                          calcDoc+=Math.pow(temp*temp1,2);
                          //tính cách 2
                          if(temp>maxterm)
                                 maxterm=temp;
                          
                       
                   }
                    calcDoc= Math.sqrt(calcDoc);
                    calcDocument.put(listOfFiles[i].getName(), calcDoc);
                   calcDocument1.put(listOfFiles[i].getName(), maxterm);
          }
}
public void weighttfidf(){
    File forder = new File(strIndex);
            File[] listOfFiles = forder.listFiles();//1400

            int lenght =listOfFiles.length;
          for(int i=0;i<lenght;i++){
             try{
                        scan=new Scanner(listOfFiles[i]);
                    }
                    catch(Exception e)
                    {
                    } 
            HashMap<String,Double>weight= new  HashMap<String,Double>();
             HashMap<String,Double>weight2= new  HashMap<String,Double>();
             double calcDoc=0.0;
                    while(scan.hasNextLine())
                    {       
                        if(!scan.hasNext()) break;
                        String str=scan.next();
                        String strs= scan.next();
                         int temp=Integer.parseInt(strs);
                    double temp1=idf.get(str).doubleValue();
                    double doc=calcDocument.get(listOfFiles[i].getName()).doubleValue();
                    double weightdouble=(temp*temp1)/doc;
                         weight.put(str,weightdouble);
                         //tính cách 2
                      double doc2=calcDocument.get(listOfFiles[i].getName()).doubleValue();
                       double weightsmart=0.5+0.5*temp1/doc2;
                        weight2.put(str,weightsmart);
                   }
                    tfidfweight.put(listOfFiles[i].getName(), weight);
                    smartweight.put(listOfFiles[i].getName(),weight2);
}

}
public void Query(){
     File forder = new File(strQuery);
            File[] listOfFiles = forder.listFiles();//1400

            int lenght =listOfFiles.length;
          for(int i=0;i<lenght;i++){
              tachtu(listOfFiles[i],strQueryResult);
          }

}
public void normQuery(){//sửa biến
    File forder = new File(strQueryResult);
            File[] listOfFiles = forder.listFiles();//1400
            int lenght =listOfFiles.length;
          for(int i=0;i<lenght;i++){
             try{
                        scan=new Scanner(listOfFiles[i]);
                    }
                    catch(Exception e)
                    {
                    } 
             double calcDoc=0.0;
             int maxterm=0;
                    while(scan.hasNextLine())
                    {       
                      
                        if(!scan.hasNext()) break;
                        String str=scan.next();
                        String strs= scan.next();
                         int temp=Integer.parseInt(strs);
                         double temp1;
                         if (idf.containsKey(str)) {		
                         temp1=idf.get(str).doubleValue();
                         }else temp1=0;
                          calcDoc+=Math.pow(temp*temp1,2);
                          //tính cách 2
                          if(temp>maxterm)
                                 maxterm=temp;
                          
                       
                   }
                    calcDoc= Math.sqrt(calcDoc);
                    calcDocumentQuery.put(listOfFiles[i].getName(), calcDoc);
                   calcDocumentQuery1.put(listOfFiles[i].getName(), maxterm);
          }
}//tính query
public void calcWeightQuery(){
     File forder = new File(strQueryResult);
            File[] listOfFiles = forder.listFiles();//1400

            int lenght =listOfFiles.length;
          for(int i=0;i<lenght;i++){
             try{
                        scan=new Scanner(listOfFiles[i]);
                    }
                    catch(Exception e)
                    {
                    } 
            HashMap<String,Double>weight= new  HashMap<String,Double>();
             HashMap<String,Double>weight2= new  HashMap<String,Double>();
             double calcDoc=0.0;
                    while(scan.hasNextLine())
                    {       
                        if(!scan.hasNext()) break;
                        String str=scan.next();
                        String strs= scan.next();
                         int temp=Integer.parseInt(strs);
                    double temp1;
                    if (idf.containsKey(str)) {		
                         temp1=idf.get(str).doubleValue();
                         }else temp1=0;
                    double doc=calcDocumentQuery.get(listOfFiles[i].getName()).doubleValue();
                    double weightdouble=(temp*temp1)/doc;
                         weight.put(str,weightdouble);
                         //tính cách 2
                      double doc2=calcDocumentQuery.get(listOfFiles[i].getName()).doubleValue();
                       double weightsmart=0.5+0.5*temp1/doc2;
                        weight2.put(str,weightsmart);
                   }
                    tfidfweightQuery.put(listOfFiles[i].getName(), weight);
                    smartweightQuery.put(listOfFiles[i].getName(),weight2);

            }
    }
 private static HashMap<String, Double> sortByComparator(HashMap<String, Double> unsortMap, final boolean order)
    {

        LinkedList<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>()
        {
            public int compare(Entry<String, Double> o1,
                    Entry<String, Double> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
 private static HashMap<Integer, Integer> sortByComparatorkey(HashMap<Integer, Integer> unsortMap, final boolean order)
    {

        LinkedList<Entry<Integer, Integer>> list = new LinkedList<Entry<Integer, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<Integer, Integer>>()
        {
            public int compare(Entry<Integer, Integer> o1,
                    Entry<Integer, Integer> o2)
            {
                if (order)
                {
                    return o1.getKey().compareTo(o2.getKey());
                }
                else
                {
                    return o2.getKey().compareTo(o1.getKey());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        for (Entry<Integer, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
public void DistanstSim(){
     Set setquery = tfidfweightQuery.entrySet();
     Set setDoc=tfidfweight.entrySet();
      Iterator iteratorquery = setquery.iterator();
        while(iteratorquery.hasNext()){
            HashMap<String, Double> TempQuery;
            Map.Entry mentry = (Map.Entry)iteratorquery.next();
            String namequery = mentry.getKey().toString();
             TempQuery=(HashMap)mentry.getValue();
             Set setDoc1=TempQuery.entrySet();
              HashMap<String, Double> TempDistant= new HashMap<String, Double>();
               Iterator iteratordoc = setDoc.iterator();
                     while(iteratordoc.hasNext()){
                    HashMap<String, Double> Tempdoc;
                    Map.Entry mentrydoc = (Map.Entry)iteratordoc.next();
                    String namedoc = mentrydoc.getKey().toString();
                     Tempdoc=(HashMap)mentrydoc.getValue();
                                Iterator iteratorquery1 = setDoc1.iterator();
                                double result=0.0;
                                    while(iteratorquery1.hasNext()){
                                   Map.Entry mentry1 = (Map.Entry)iteratorquery1.next();
                                     Double namequery1 = Double.parseDouble(mentry1.getValue().toString());
                                      String namequery2 = mentry1.getKey().toString();
                                   
                                        if(Tempdoc.containsKey(namequery2)){
                                                 result+= namequery1*Tempdoc.get(namequery2);
                                       }
                                     }
                                    
                                    TempDistant.put(namedoc,result);
                                  
  
                                 }
                       TempDistant=sortByComparator(TempDistant,DESC);
                     DistantSim.put(namequery,TempDistant);
                     

        }
        
}
public void DistanstSimsmart(){
     Set setquery = smartweightQuery.entrySet();
     Set setDocsmart=tfidfweight.entrySet();// tính smart
      Iterator iteratorquery = setquery.iterator();
        while(iteratorquery.hasNext()){
            HashMap<String, Double> TempQuery;
            Map.Entry mentry = (Map.Entry)iteratorquery.next();
            String namequery = mentry.getKey().toString();
             TempQuery=(HashMap)mentry.getValue();
             Set setDoc1=TempQuery.entrySet();
              HashMap<String, Double> TempDistant= new HashMap<String, Double>();
               Iterator iteratordoc = setDocsmart.iterator();
                     while(iteratordoc.hasNext()){
                    HashMap<String, Double> Tempdoc;
                    Map.Entry mentrydoc = (Map.Entry)iteratordoc.next();
                    String namedoc = mentrydoc.getKey().toString();
                     Tempdoc=(HashMap)mentrydoc.getValue();
                                Iterator iteratorquery1 = setDoc1.iterator();
                                double result=0.0;
                                    while(iteratorquery1.hasNext()){
                                   Map.Entry mentry1 = (Map.Entry)iteratorquery1.next();
                                     Double namequery1 = Double.parseDouble(mentry1.getValue().toString());
                                      String namequery2 = mentry1.getKey().toString();
                                   
                                        if(Tempdoc.containsKey(namequery2)){
                                                 result+= namequery1*Tempdoc.get(namequery2);
                                       }
                                     }
                                    
                                    TempDistant.put(namedoc,result);
                                  
  
                                 }
                       TempDistant=sortByComparator(TempDistant,DESC);
                     DistantSimsmart.put(namequery,TempDistant);
                     

        }
        
}
public void RES(File file){
   
    
            File[] listOfFiles = file.listFiles();
            int lenght =listOfFiles.length;
          for(int i=0;i<lenght;i++){
             try{
                        scan=new Scanner(listOfFiles[i]);
                    }
                    catch(Exception e)
                    {
                    } 
             ArrayList<Integer> TempRes= new ArrayList<Integer>();
             String temp1=null;
       
                    while(scan.hasNextLine())
                    {       
                      
                        if(!scan.hasNext()) break;
                        String str=scan.next();
                        temp1=str;
                        String strs= scan.next();
                        String strs2= scan.next();
                         int temp=Integer.parseInt(strs);  
                        int temp3=Integer.parseInt(strs2);
                         if(temp3!=-1)
                         TempRes.add(temp);     
                   }
                Res.put(temp1,TempRes);   
                
          }
}
public void TempREs(String str){
    HashMap<String,HashMap<String,Double>> DistantTemp = new HashMap<String,HashMap<String,Double>>();// khi chuyền qua distanl
    HashMap<String,Double> DistantTemp2 = new HashMap<String,Double>();
     HashMap<String, Double> TempDistant= new HashMap<String, Double>();
    Set setDocsmart=DistantSim.entrySet();
    Iterator iteratorRes = setDocsmart.iterator();
      while(iteratorRes.hasNext()){
                    HashMap<String, Double> Tempdoc;
                    Map.Entry mentrydoc = (Map.Entry)iteratorRes.next();
                    String namedoc = mentrydoc.getKey().toString();
                    namedoc= namedoc.substring(0, namedoc.indexOf("."));
                     Tempdoc=(HashMap)mentrydoc.getValue();
                      Set setDoc1=Tempdoc.entrySet();   
                      Iterator iteratorRes1 = setDoc1.iterator();
                      int count=0;
                       HashMap<String, Double> result=new HashMap<String, Double>();
                         String strFile=str+namedoc+".txt";  
                         File nFile=new File(strFile);    
                        FileWriter fw=null;        
                        try
                        {
                            fw=new FileWriter(nFile);
                        }
                        catch(Exception e)
                        {
                        }   
                        try{
                                    fw.close();
                                }
                                catch(Exception e)
                                {
                                } 
                        ArrayList<Integer> TempRes= new  ArrayList<Integer>();
                      while(iteratorRes1.hasNext()&&count<30){                          
                             Map.Entry mentry1 = (Map.Entry)iteratorRes1.next();
                                     Double values = Double.parseDouble(mentry1.getValue().toString());
                                      String name = mentry1.getKey().toString();  
                                       name= name.substring(0, name.indexOf("."));
                                       TempRes.add(Integer.parseInt(name));
                                      result.put(name, values);
                                        try
                                    {
                                        fw=new FileWriter(nFile, true);
                                    }
                                    catch(Exception e)
                                    {
                                    }
                                      try{            
                                fw.write(name);
                                fw.write("\r\n");   
                                    }
                                    catch(Exception e)
                                    {                 
                                    }
                                      count++;
                                           try{
                                    fw.close();
                                }
                                catch(Exception e)
                                {
                                } 
                      }
                  
                      result=sortByComparator(result,DESC);
                      ResultDic.put(namedoc, TempRes);
                    DistantTemp.put(namedoc,result);   
                    
                      
                 }
      DistantSim=DistantTemp;
}
public void TempREs2(String str){
    HashMap<String,HashMap<String,Double>> DistantTemp = new HashMap<String,HashMap<String,Double>>();// khi chuyền qua distanl
    HashMap<String,Double> DistantTemp2 = new HashMap<String,Double>();
     HashMap<String, Double> TempDistant= new HashMap<String, Double>();
    Set setDocsmart=DistantSimsmart.entrySet();
    Iterator iteratorRes = setDocsmart.iterator();
      while(iteratorRes.hasNext()){
                    HashMap<String, Double> Tempdoc;
                    Map.Entry mentrydoc = (Map.Entry)iteratorRes.next();
                    String namedoc = mentrydoc.getKey().toString();
                    namedoc= namedoc.substring(0, namedoc.indexOf("."));
                     Tempdoc=(HashMap)mentrydoc.getValue();
                      Set setDoc1=Tempdoc.entrySet();   
                      Iterator iteratorRes1 = setDoc1.iterator();
                      int count=0;
                       HashMap<String, Double> result=new HashMap<String, Double>();
                         String strFile=str+namedoc+".txt";  
                         File nFile=new File(strFile);    
                        FileWriter fw=null;        
                        try
                        {
                            fw=new FileWriter(nFile);
                        }
                        catch(Exception e)
                        {
                        }   
                        try{
                                    fw.close();
                                }
                                catch(Exception e)
                                {
                                } 
                        ArrayList<Integer> TempRes= new  ArrayList<Integer>();
                      while(iteratorRes1.hasNext()&&count<30){                          
                             Map.Entry mentry1 = (Map.Entry)iteratorRes1.next();
                                     Double values = Double.parseDouble(mentry1.getValue().toString());
                                      String name = mentry1.getKey().toString();  
                                       name= name.substring(0, name.indexOf("."));
                                       TempRes.add(Integer.parseInt(name));
                                      result.put(name, values);
                                        try
                                    {
                                        fw=new FileWriter(nFile, true);
                                    }
                                    catch(Exception e)
                                    {
                                    }
                                      try{            
                                fw.write(name);
                                fw.write("\r\n");   
                                    }
                                    catch(Exception e)
                                    {                 
                                    }
                                      count++;
                                           try{
                                    fw.close();
                                }
                                catch(Exception e)
                                {
                                } 
                      }
                  
                      result=sortByComparator(result,DESC);
                      ResultDicSim.put(namedoc, TempRes);
                    DistantTemp.put(namedoc,result);   
                    
                      
                 }
      DistantSimsmart=DistantTemp;
}
public void sokhop(){
    HashMap<String,Double> tempsokhop= new HashMap<String,Double>();
    Set setDocsmart=ResultDic.entrySet();
    Set setRes=Res.entrySet();
    Iterator iteratorDic = setDocsmart.iterator();
    while(iteratorDic.hasNext()){
          ArrayList<Integer> Tempdoc= new ArrayList<Integer>();
          Map.Entry mentrydocDic = (Map.Entry)iteratorDic.next();
          String namedoc =mentrydocDic.getKey().toString();
          Tempdoc=(ArrayList)mentrydocDic.getValue();
           int count=0;
            Iterator iteratorRes = setRes.iterator();
           while(iteratorRes.hasNext()){
          ArrayList<Integer> TempdocRes= new ArrayList<Integer>();
                Map.Entry mentrydocres = (Map.Entry)iteratorRes.next();
                String nameres =mentrydocres.getKey().toString();
                TempdocRes=(ArrayList)mentrydocres.getValue();
                if(namedoc.equals(nameres)==true){
                   
                    for(int i=0;i<TempdocRes.size();i++){
                        if(Tempdoc.contains(TempdocRes.get(i))==true){
                        count++;
                        }
                    }
                    break;
                }
                
    }
           int name= Integer.parseInt(namedoc);
           sokhop.put(name, count);
    }
    sokhop=sortByComparatorkey(sokhop,true);
}
public void sokhop2(){
    HashMap<String,Double> tempsokhop= new HashMap<String,Double>();
    Set setDocsmart=ResultDicSim.entrySet();
    Set setRes=Res.entrySet();
    Iterator iteratorDic = setDocsmart.iterator();
    while(iteratorDic.hasNext()){
          ArrayList<Integer> Tempdoc= new ArrayList<Integer>();
          Map.Entry mentrydocDic = (Map.Entry)iteratorDic.next();
          String namedoc =mentrydocDic.getKey().toString();
          Tempdoc=(ArrayList)mentrydocDic.getValue();
           int count=0;
            Iterator iteratorRes = setRes.iterator();
           while(iteratorRes.hasNext()){
          ArrayList<Integer> TempdocRes= new ArrayList<Integer>();
                Map.Entry mentrydocres = (Map.Entry)iteratorRes.next();
                String nameres =mentrydocres.getKey().toString();
                TempdocRes=(ArrayList)mentrydocres.getValue();
                if(namedoc.equals(nameres)==true){
                   
                    for(int i=0;i<TempdocRes.size();i++){
                        if(Tempdoc.contains(TempdocRes.get(i))==true){
                        count++;
                        }
                    }
                    break;
                }
                
    }
           int name= Integer.parseInt(namedoc);
           sokhop2.put(name, count);
    }
    sokhop2=sortByComparatorkey(sokhop2,true);
}
public void tinhdochinhxac(String str){
    double Tong=0.0;
    double Tong2=0.0;
    File nFile=new File(str);    
     FileWriter fw=null;        
    try
    {
         fw=new FileWriter(nFile);
    }
    catch(Exception e)
    {
     }   
     try{
           fw.close();
        }
         catch(Exception e)
         {
         } 
    HashMap<String,Double> tempsokhop= new HashMap<String,Double>();
    Set setDocsmart=sokhop.entrySet();
    Iterator iteratorResDic = setDocsmart.iterator();
     Set setDocsmart2=sokhop2.entrySet();
    Iterator iteratorResDic2 = setDocsmart2.iterator();
    while(iteratorResDic.hasNext()&&iteratorResDic2.hasNext()){
         try
    {
         fw=new FileWriter(nFile,true);
    }
    catch(Exception e)
    {
     } 
     Map.Entry mentrydocDic = (Map.Entry)iteratorResDic.next();
     String namedoc =mentrydocDic.getKey().toString();
      int tempsokhop1= Integer.parseInt(mentrydocDic.getValue().toString());
     Map.Entry mentrydocDic2 = (Map.Entry)iteratorResDic2.next();
     String namedoc2 =mentrydocDic2.getKey().toString();
      int tempsokhop2= Integer.parseInt(mentrydocDic2.getValue().toString());
        if(namedoc.equals(namedoc2)==true){
      // tính độ chính xác
        double result=(double)tempsokhop1/30;
        Tong+=result;
        //cách 2
        double result2=(double)tempsokhop2/30;
        Tong2+=result2;
        try{
           fw.write("   "+result+" "+result2);
           fw.write("\r\n");
        }
         catch(Exception e)
         {
         } 
        
      try{
           fw.close();
        }
         catch(Exception e)
         {
         } 
      }
          
          
    }
    try
    {
         fw=new FileWriter(nFile,true);
    }
    catch(Exception e)
    {
     } 
          try{
           fw.write("   "+Tong+" "+Tong2);
           fw.write("\r\n");
           fw.write("   "+Tong/225+" "+Tong2/225);
           fw.write("\r\n");
           
        }
         catch(Exception e)
         {
         } 
        
      try{
           fw.close();
        }
         catch(Exception e)
         {
         } 
}
public void tinhdophu(String str){
    double Tong=0.0;
    double Tong2=0.0;
    File nFile=new File(str);    
     FileWriter fw=null;        
    try
    {
         fw=new FileWriter(nFile);
    }
    catch(Exception e)
    {
     }   
     try{
           fw.close();
        }
         catch(Exception e)
         {
         } 
    HashMap<String,Double> tempsokhop= new HashMap<String,Double>();
    Set setDocsmart=sokhop.entrySet();
    Set setRes=Res.entrySet();
     Set setDocsmart2=sokhop2.entrySet();
    Iterator iteratorResDic = setDocsmart.iterator();
     Iterator iteratorResDic2 = setDocsmart2.iterator();
    while(iteratorResDic.hasNext()&&iteratorResDic2.hasNext()){
         try
    {
         fw=new FileWriter(nFile,true);
    }
    catch(Exception e)
    {
     } 
      Iterator iteratorRes = setRes.iterator();
     Map.Entry mentrydocDic = (Map.Entry)iteratorResDic.next();
      Map.Entry mentrydocDic2 = (Map.Entry)iteratorResDic2.next();
     String namedoc =mentrydocDic.getKey().toString();
     String namedoc2 =mentrydocDic2.getKey().toString();
            while(iteratorRes.hasNext()){
                Map.Entry mentryRes = (Map.Entry)iteratorRes.next();
                 String nameres =mentryRes.getKey().toString();
                 if(namedoc.equals(nameres)==true&&namedoc2.equals(nameres)==true){
                int tempsokhop1= Integer.parseInt(mentrydocDic.getValue().toString());
                 int tempsokhop2= Integer.parseInt(mentrydocDic2.getValue().toString());
                //tính độ phủ
                ArrayList<Integer> temp1= new  ArrayList<Integer>();
                 temp1=(ArrayList)mentryRes.getValue();
                      double result=(double)tempsokhop1/temp1.size();
                      Tong+=result;
                      double result2=(double)tempsokhop2/temp1.size(); 
                      Tong2+=result2;
                // tính độ chính xác
                  try{
                     fw.write(namedoc+" "+result+" "+result2);
                     fw.write("\r\n");
                  }
                   catch(Exception e)
                   {
                   } 

                try{
                     fw.close();
                  }
                   catch(Exception e)
                   {
                   } 
              }
       }
    }
         try
    {
         fw=new FileWriter(nFile,true);
    }
    catch(Exception e)
    {
     } 
          try{
                     fw.write("  "+Tong+" "+Tong2);
                     fw.write("\r\n");
                      fw.write("  "+Tong/225+" "+Tong2/225);
                     fw.write("\r\n");
                  }
                   catch(Exception e)
                   {
                   } 

                try{
                     fw.close();
                  }
                   catch(Exception e)
                   {
                   } 
}
public void tinhdoF(String str){
    double Tong=0.0;
    double Tong2=0.0;
     double Tong3=0.0;
    double Tong4=0.0;
    File nFile=new File(str);    
     FileWriter fw=null;        
    try
    {
         fw=new FileWriter(nFile);
    }
    catch(Exception e)
    {
     }   
     try{
           fw.close();
        }
         catch(Exception e)
         {
         } 
    HashMap<String,Double> tempsokhop= new HashMap<String,Double>();
    Set setDocsmart=sokhop.entrySet();
    Set setRes=Res.entrySet();
     Set setDocsmart2=sokhop2.entrySet();
    Iterator iteratorResDic = setDocsmart.iterator();
     Iterator iteratorResDic2 = setDocsmart2.iterator();
    while(iteratorResDic.hasNext()&&iteratorResDic2.hasNext()){
         try
    {
         fw=new FileWriter(nFile,true);
    }
    catch(Exception e)
    {
     } 
      Iterator iteratorRes = setRes.iterator();
     Map.Entry mentrydocDic = (Map.Entry)iteratorResDic.next();
      Map.Entry mentrydocDic2 = (Map.Entry)iteratorResDic2.next();
     String namedoc =mentrydocDic.getKey().toString();
     String namedoc2 =mentrydocDic2.getKey().toString();
            while(iteratorRes.hasNext()){
                Map.Entry mentryRes = (Map.Entry)iteratorRes.next();
                 String nameres =mentryRes.getKey().toString();
                 if(namedoc.equals(nameres)==true&&namedoc2.equals(nameres)==true){
                int tempsokhop1= Integer.parseInt(mentrydocDic.getValue().toString());
                 int tempsokhop2= Integer.parseInt(mentrydocDic2.getValue().toString());
                //tính độ phủ
                ArrayList<Integer> temp1= new  ArrayList<Integer>();
                 temp1=(ArrayList)mentryRes.getValue();
                      double R=(double)tempsokhop1/temp1.size();
                      
                      double R2=(double)tempsokhop2/temp1.size(); 
                     
                        //tính độ chính xác
                        double P=(double)tempsokhop1/30;
                        
                        //cách 2
                        double P2=(double)tempsokhop2/30;
                        
                        double F=0.0;
                         double F1=0.0;
                         if(R!=0.0||P!=0.0){
                         F=(2*R*P)/(P+R);
                         Tong+=F;}
                          if(R2!=0.0&&P2!=0.0){
                         F1=(2*R2*P2)/(P2+R2);
                          Tong2+=F1;
                          }
                         
                  try{
                     fw.write(namedoc+" "+F+" "+F1);
                     fw.write("\r\n");
                  }
                   catch(Exception e)
                   {
                   } 

                try{
                     fw.close();
                  }
                   catch(Exception e)
                   {
                   } 
              }
       }
    }
         try
    {
         fw=new FileWriter(nFile,true);
    }
    catch(Exception e)
    {
     } 
          try{
                     fw.write("  "+Tong+" "+Tong2);
                     fw.write("\r\n");
                      fw.write("  "+Tong/225+" "+Tong2/225);
                     fw.write("\r\n");
                  }
                   catch(Exception e)
                   {
                   } 

                try{
                     fw.close();
                  }
                   catch(Exception e)
                   {
                   } 
}
}