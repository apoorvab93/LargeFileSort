package com.company;

import java.io.*;
import java.util.Scanner;

public class Main {

    private final String largeFilePath;

    private final String outputPath;

    private final String chunkPath;

    private final int chunkSize;

    private final int numberOfChunks;


    public Main()
    {
        this.largeFilePath = "D:\\largeText_10GB"; //Replace with your own paths
        this.chunkPath = "D:\\chunk_";
        this.outputPath = "D:\\output.txt";
        this.chunkSize = 200000000; // Assuming that machine has RAM size which is 10% of file size.
        this.numberOfChunks = 10;
    }

    public static void main(String[] args) throws IOException
    {
        Main main = new Main();
        main.execute();
    }

    private void execute() throws IOException
    {
        this.createLargeFile();
        this.createSortedChunks();
        this.mergeSortedChunks();
    }

    private void createLargeFile()
    {
        try {
            File temp = File.createTempFile(this.largeFilePath, ".txt");
            boolean exists = temp.exists();
            if(exists) {
                return;
            }
            Writer writer = new FileWriter(this.largeFilePath+".txt");
            for(int j =0; j < this.chunkSize*this.numberOfChunks; j++) {
                int num = (int)(Math.random() * 100000);
                writer.write(num+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSortedChunks() throws FileNotFoundException
    {
        Scanner input = new Scanner(new File(this.largeFilePath+".txt"));
        for(int i=0; i<this.numberOfChunks; i++)
        {
            int startIndex = this.chunkSize*i;
            int endIndex = startIndex + this.chunkSize;
            int[] array = new int[this.chunkSize];
            int counter = startIndex;
            int j=0;
            while(input.hasNextLine() && counter < endIndex) // 1 GB
            {
                array[j]= Integer.parseInt(input.nextLine());
                counter++;
                j++;
            }
            quickSort(array, 0, array.length-1);
            writeChunkToDisk(array,startIndex);
        }
    }

    private void writeChunkToDisk(int[] array, int index)
    {
        try {
            Writer writer = new FileWriter(this.chunkPath+index+".txt");
            for (int each : array ) {
                writer.write(each+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mergeSortedChunks() throws IOException
    {
        Scanner[] scanArray = new Scanner[this.numberOfChunks];
        for(int i=0; i<10; i++)
        {
            int index = this.chunkSize*i;
            scanArray[i] = new Scanner(new File(this.chunkPath+index+".txt"));
        }

        K_WayMerge(scanArray);
    }

    private void K_WayMerge(Scanner[] scanArray) throws IOException
    {
        Writer writer = new FileWriter(this.outputPath);
        Integer[] current = new Integer[this.numberOfChunks];
        int previousIndex = -1;
        while(isThereMoreData(scanArray))
        {
            for (int i = 0; i < this.numberOfChunks; i++) {
                if(scanArray[i].hasNext())
                {
                    if(shouldReadFromIthStream(scanArray, previousIndex, i))
                    {
                        current[i] = Integer.parseInt((scanArray[i].nextLine()));
                    }
                }
                else
                {
                    current[i] = null;
                }
            }
            Integer minVal = Integer.MAX_VALUE;
            for(int i =0; i< this.numberOfChunks; i++)
            {
                if(current[i] != null && current[i]<minVal){
                    minVal = current[i];
                    previousIndex = i;
                }
            }

            if(minVal != Integer.MAX_VALUE) {
                writer.write(minVal + "\n");
            }
        }
        writer.close();
    }

    private boolean shouldReadFromIthStream(Scanner[] scanArray, int previousIndex, int i)
    {
        return previousIndex == i ||
                isCurrentStreamOnlyRemainingStreamWithData(scanArray, i) ||
                previousIndex == -1;
    }

    private boolean isThereMoreData(Scanner[] scanArray)
    {
        for (Scanner scanner : scanArray) {
            if(scanner.hasNext())
                return true;
        }
        return false;
    }

    private boolean isCurrentStreamOnlyRemainingStreamWithData(Scanner[] scanArray, int i)
    {
        for (int j=0; j<10;j++) {
            if (i == j) continue;;
            if (scanArray[j].hasNext()) return false;
        }
        return true;
    }

    // sorting method where I use in-place quick sort
    // TODO: attempt iterative quick sort
    private void quickSort(int[] array, int startIndex, int endIndex)
    {
        if (startIndex < endIndex) {
            int partitionIndex = partition(array, startIndex, endIndex);

            quickSort(array, startIndex, partitionIndex-1);
            quickSort(array, partitionIndex+1, endIndex);
        }
    }

    private int partition(int[] array, int startIndex, int endIndex)
    {
        int pivot = array[endIndex];
        int i = startIndex-1;

        for (int j = startIndex; j < endIndex; j++) {
            if (array[j] <= pivot) {
                i++;

                int swapTemp = array[i];
                array[i] = array[j];
                array[j] = swapTemp;
            }
        }

        int swapTemp = array[i+1];
        array[i+1] = array[endIndex];
        array[endIndex] = swapTemp;

        return i+1;
    }
}
