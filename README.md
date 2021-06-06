# LargeFileSort
Sorting algorithm for a large file that needs to be sorted using 10% memory size of the file size

Code to sort a large file containing integers on a machine with limited memory(1/10th the file size)
Example - Suppose there is a 100 GB file containing integers and machine has 10 GB memory max, and 4 CPU cores available at max, how do you sort them?

**Simulations** 
1. To create a large file, I wrote a method that can generate a file containing integers that grows to 100GB in size
2. To simulate limited memory, I run the sorting code with a max memory heap size of 10 GB. This will prevent the code from consuming more memory on the machine even if available.
3. To be able to run it on my student laptop that does not have free 100GB space, I had to reduce scale of the problem by 10. I will test it first with file size of 10GB and a max memory footprint of 1GB.

**Approach**
PART-1 SORTING IN CHUNKS
1. Read the large file in chunks and store the read numbers as an integer array.
2. Each chunk is no more than 1 GB in size(10% of the file size)
3. Sort the numbers in the chunk using QuickSort. Quicksort can sort the array in-place and help reduce further memory consumption. This step would have asymptotic average case runtime of O(NLOGN) where N is the size of the read array.
4. Write the sorted array back to disk

PART-2 MERGE SORTED CHUNKS
1. This is similar to a K-way sort problem. 
2. Read one number each from the chunks and write the smallest to the output file.
3. The output file has all sorted integers

**Instructions to run the code-**
1. Simply clone the code and open the project in your Java IDE (I used free community edition Intellij) or just compile and run Main.java file like any other java program
2. Make sure you set the maximum heap size to no more than 1 GB to simulate the limited memory scenarios.

![image](https://user-images.githubusercontent.com/56744800/120925921-9eb1af80-c68f-11eb-87fa-34b51084b898.png)

**Output files from my local run**
![image](https://user-images.githubusercontent.com/56744800/120914191-3c39be80-c651-11eb-825c-6579402c10ae.png)
The file largeText_10GB contains randomly generated Integers
The files chunk_10_* contains 1GB chunks of integers that have been sorted
The file output_sorted contains the entire Integer set but sorted.


