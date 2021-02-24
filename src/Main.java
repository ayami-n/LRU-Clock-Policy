// Created by Ayami.N

import java.io.*;
import java.util.*;

public class Main
{
    private static LRU lru;
    private static clockPolicy clock;
    private static String[] FileName;
    private static int index = 0;
    private static int size;
    private static int Frames;
    private static int quantum;
    private static String PID;
    private static ArrayList<page>[] original;
    private static ArrayList<LRU>[] LRU;
    private static ArrayList<clockPolicy>[] Clock;


    private static void FileDemo(String FileName) // <-same (FileName[i]) //add static then it works
    {
        String importName = null;
        Scanner importStream = null;
        int num = 1;

        try {
            importName = FileName;
            importStream = new Scanner(new BufferedReader(new FileReader(importName)));

            while (importStream.hasNextLine()) // read next line....
            {
                try {
                    String line = importStream.nextLine();

                    if (line.equals(""))
                    {
                        continue;
                    }

                    String[] box = line.split(" "); //split the lines by space

                    if (!box[0].equalsIgnoreCase("begin") && !box[0].equalsIgnoreCase("end"))
                    {
                        int page = Integer.parseInt(box[0]);

                        PID = 'P'+ Integer.toString( index +1) +'-' + Integer.toString(num);
                        lru.setProcess(index, PID, page);
                        clock.setProcess(index, PID, page);
                        //original[index].add(new list(PID,page));
                        //LRU[index].add(new LRU(PID,page));
                        //Clock[index].add(new clockPolicy(PID,page));
                        num++;

                        continue;
                    }

                } catch (ArrayIndexOutOfBoundsException ex)
                {
                    ex.printStackTrace(); // for error
                    System.out.println("Warning: ArrayIndexOutOfBoundsException");
                }
                catch (NoSuchElementException | NullPointerException n)
                {
                    System.out.println(n.getMessage());
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("ERROR!!");
        }
        finally
        {
            if (importStream != null)
            {
                importStream.close(); //closing import stream for the next
                index++;
            }
        }
    }


    private static void run(String[] FileName)
    {
        System.out.println("...Importing...");
        for(int i =0; i<size; i++)
        {
            FileDemo(FileName[i]);
        }

        System.out.println("importing is done");

        lru.lru_sim();
        lru.LruPrint(FileName);

        System.out.println("\n------------------------------------------------------------"); //  "\n" is line break

        clock.clock_sim();
        clock.clockPrint(FileName);
    }


    public static void main(String[] args)
    {
        size = 3; // args.length-2;

        try
        {
             Frames = 30; //Integer.parseInt(args[0]);   // changed string-> int
             quantum = 3;//Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e)
        {
            System.out.println("NUM ERROR!!");
        }

        FileName = new String[size]; //filename -> array
//        for(int j=0; j < size; j++)
//        {
//            FileName[j] = args[j+2];
//        }
        FileName[0] = "src/P1.txt";
        FileName[1] = "src/P2.txt";
        FileName[2] = "src/P3.txt";
       // FileName[3] = "src/Process4.txt";

        // constructions
        Main demo = new Main();
        lru = new LRU(size,Frames,quantum);
        clock = new clockPolicy(size,Frames,quantum);

       // String  // args[2];

        demo.run(FileName); // run(String[] FileName); run for one time
    }
}
