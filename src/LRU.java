// Created by Ayami.N
import javax.swing.*;
import java.util.ArrayList;

public class LRU<Fin>
{
    private ArrayList<page> []cpu;
    private static ArrayList<page>[] original;
    private ArrayList<page> ReadyQ;
    private ArrayList<Integer>[] faults;
    private int[] Turnaround;

    private int time = 0 ;
    private int Ftime = 6;
    private int Frames;
    private int size = 0;
    private int head = 0;
    private int quantum;
    private int pframes;
    private boolean accessOrder;

    public LRU() {}  // not use

    public LRU(int new_size, int new_Frames, int quantum_size)  // overloading
    {
        size = new_size;
        Frames = new_Frames;
        quantum = quantum_size;
        pframes = (Frames / size);

        ReadyQ = new ArrayList<>();
        original = new ArrayList[size];
        cpu = new ArrayList[size];
        faults = new ArrayList[size];
        Turnaround = new int[size];

        for(int i=0; i<size; i++)
        {
            original[i] = new ArrayList<page>(); // class need new construct / def
            cpu[i] = new ArrayList<page>();
            faults[i] = new ArrayList<Integer>();
            Turnaround[i] =0;// int type just need number
        }
        this.accessOrder = false;
    }

    public void LruPrint(String[] name)
    {
        System.out.println("LRU - Fixed:");
        System.out.println("PID  Process Name      Turnaround Time  # Faults  Fault Times");

        for (int i = 0; i < size; i++)
        {
            String Id = cpu[i].get(head).getPID();
            int id = Integer.parseInt(Id.substring(1,2));

            String faultTime = "";
            for(int k=0; k<faults[i].size(); k++)
            {
                 faultTime += faults[i].get(k);
                 if(k < faults[i].size()-1)
                 {
                     faultTime += ", ";
                 }
            }
            System.out.println(String.format("%-3s  %-12s      %-15s  %-7s   %-11s", id, name[i], Turnaround[i],faults[i].size(),"{"+faultTime+"}"));
        }
    }

    // setter

    public void setProcess(int index, String PID, int page)
    {
        original[index].add(new page(PID,page));
    }

    public void lru_sim()
    {
        boolean stop = false;

        do{

            for(int k = 0; k < size; k++)
            {
                if(!original[k].isEmpty())
                {
                    if(!cpu[k].isEmpty() && original[k].get(head).getFtime() == -999)
                    {
                        for(int l = 0; l < cpu[k].size(); l++)
                        {
                            if(cpu[k].get(l).getPage() == original[k].get(head).getPage())
                            {
                                ReadyQ.add(original[k].get(head));
                                original[k].remove(head);
                                accessOrder = true;
                                break;
                            }
                            if(!accessOrder)  //l == cpu[k].size()-1  l size 8 , cpu[k].size()-1 8 same
                            {
                                block(k);
                                break;
                            }
                        }
                    }

                   if(cpu[k].isEmpty())
                   {
                       if(original[k].get(head).getFtime() == -999)
                       {
                           block(k);
                       }
                   }
               }
            }

            while(ReadyQ.isEmpty())
            {
              time();
            }

            while(!ReadyQ.isEmpty())
            {
                int limit = 0;
                boolean found = false;
                String Id = ReadyQ.get(head).getPID();
                int index = Integer.parseInt(Id.substring(1,2)) -1;

                if(accessOrder)
                {
                    if(!cpu[index].isEmpty())
                    {
                        for(int k = 0; k < cpu[index].size(); k++)
                        {
                            if(cpu[index].get(k).getPage() == ReadyQ.get(head).getPage())
                            {
                                cpu[index].remove(k);
                                break;
                            }
                        }
                    }
                    cpu[index].add((page) ReadyQ.get(head));
                    ReadyQ.remove(head);
                    limit++;
                    time();
                    Turnaround[index] = time;
                }

                if(cpu[index].size() > pframes)
                {
                    replace(index);
                }

                while(limit < quantum)
                {
                    if(!original[index].isEmpty())
                    {
                        for(int k = 0; k < cpu[index].size(); k++)
                        {
                            if(cpu[index].get(k).getPage() == original[index].get(head).getPage())
                            {
                                cpu[index].remove(k);
                                cpu[index].add(original[index].get(head));
                                original[index].remove(head);
                                found = true;
                                limit++;
                                time();
                                Turnaround[index] = time;

                                if(cpu[index].size() > pframes)
                                {
                                    replace(index);
                                }
                                break;
                            }
                        }

                        if(!found)
                        {
                            block(index);
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }

            int sum = 0;
            for(int i = 0; i < size; i++)
            {
                if(original[i].size() == 0)
                {
                    sum++;
                }
                if(sum == size)
                {
                    stop = true;
                }
            }

        }while(!stop);
    }

    // purpose; set time? time++? turnaround time?
    // items;turnaround, process,,,,,,,,cpu?, ready?, original?,?

    public void time()
    {
       for(int demo = 0; demo < size; demo++)
       {
           if(!original[demo].isEmpty())
           {
               if(original[demo].get(head).getFtime() > 0)
               {
                   original[demo].get(head).setFtime(original[demo].get(head).getFtime() - 1);
               }

               if(original[demo].get(head).getFtime() == 0)
               {
                   ReadyQ.add(original[demo].get(head));
                   original[demo].remove(head);
                   accessOrder = true;
               }
           }
           if(!cpu[demo].isEmpty())
           {
               for(int k = 0; k < cpu[demo].size(); k++)
               {
                   cpu[demo].get(k).setCount(cpu[demo].get(k).getCount() + 1);
               }
           }
       }
      time++;
    }

    public void replace(int index)
    {
        int max = cpu[index].get(head).getCount();
        int swap = 0;

        for(int k = 1; k < cpu[index].size(); k++)
        {
            if(max < cpu[index].get(k).getCount())
            {
                max = cpu[index].get(k).getCount();
                swap = k;
            }
        }
        cpu[index].remove(swap);
    }

    // purpose: set 6 unit/sec for failed process each
    // items: failed process, 6, cpu?, ready?, original?, total time?, set

    public void block(int k)
    {
        original[k].get(head).setFtime(Ftime);
        faults[k].add(time);
    }
}