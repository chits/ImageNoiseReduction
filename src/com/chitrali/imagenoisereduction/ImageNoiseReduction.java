package com.chitrali.imagenoisereduction;

import java.io.*;
import java.util.Arrays;
/*
 * Author : Chitrali Rai
 */
public class ImageNoiseReduction 
{
	/*
	 * This is the global array that stores all the input files
	 * as a big array matrix. Column size is the number of columns in the file.
	 * Row is the number of files plus the output
	 */
	static int[][] imageMatrix = new int[726408][19];
    static final String inputPath = "/Users/chitrali/Dropbox/Birds";
    static final String outputPath = inputPath + "/Clear.ppm";
    /*
     * This method runs through the image matrix generated from all the files, 
     * goes through each row, finds the median and uses that as the normalized value
     * for that particular pixel (row index will be an individual pixel).
     * Rule for finding median is :
     * 1. If length of array is even, then it takes the average of the middle two 
     * (n/2 and (n/2)-1) elements
     * 2. If its odd, then it takes the middle element
     */
    static void normalizePixelValue(){
        int j = imageMatrix.length;
        for(int i =0;i<j;i++){
            int[] tempArray = new int[17];
            for(int m=0;m<17;m++)
            {
                tempArray[m]= imageMatrix[i][m];
            }
            Arrays.sort(tempArray);
            int median;
            if (tempArray.length % 2 == 0)
            	median = (tempArray[tempArray.length/2] + tempArray[(tempArray.length/2) - 1])/2;
            else
            	median = tempArray[tempArray.length/2];
            imageMatrix[i][18] = median;
        }
    }
    /*
     * After each pixel is normalized and written to the last column of each row,
     * we output that last column along with ppm configurations to the output file
     * in this method
     */
    static void generateNoiseReducedFile()
    {
        normalizePixelValue();
        BufferedWriter writer = null;
        int endIndex = imageMatrix.length;
        File outputFileObject = new File(outputPath);
        try {
            outputFileObject.createNewFile();
            writer = new BufferedWriter(new FileWriter(outputFileObject));
            String normalizedPixelValue = "";
            int startIndex = 0;
            writer.write("P3\n" + "684 354\n" + "255\n");
            while(startIndex<endIndex) 
            {
                normalizedPixelValue = new Integer(imageMatrix[startIndex][18]).toString();
                writer.write(normalizedPixelValue+"\n");
                startIndex++;
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }finally 
        {
            try 
            {
                writer.close();
            } catch (IOException ex) 
            {
                ex.printStackTrace();
            }
        }
    }
    /*
     * This method takes the file as input, reads it through buffer 
     * reader and creates a column in the imageMatrix
     */
    static void loadFileToImageMatrix(String filePath, int fileIndex) 
    {
        BufferedReader reader = null;
        File inputFileObject = new File(inputPath + "/" + filePath);
        try 
        {
            reader = new BufferedReader(new FileReader(inputFileObject));
            String inputText = null;
            int i = 0, j = 0;
            while((inputText=reader.readLine())!=null)
            {
            	//First 3 string are neglected as they are the configuration values
                if(i>2)
                {
                    imageMatrix[j][fileIndex]=Integer.parseInt(inputText);
                    j++;
                }
                i++;
            }
        } catch (FileNotFoundException ex) 
        {
            ex.printStackTrace();
        } catch (IOException ex) 
        {
            ex.printStackTrace();
        }
        finally 
        {
            try 
            {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public static void main(String args[])  {
    	/*
    	 * Following reads a list of files from a folder location and calls
    	 * loadFileToImageMatrix for each of them. At the end it starts with
    	 * the normalization process of the image
    	 */
        File[] files = new File(inputPath).listFiles();
        int fileIndex = 0;
        for (File file : files) 
        {
            if (file.isFile() && file.getName().contains("birds")) 
            {
                loadFileToImageMatrix(file.getName(), fileIndex);
                fileIndex++;
            }
        }
        generateNoiseReducedFile();
    }
}