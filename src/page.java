// Created by Ayami.N

public class page
{
    private String PID;
    private int page;
    private int time;
    private int Ftime;
    private int Frames;
    private int quantum;
    private int pframes;
    private int count;

    public page(String pid, int page)
    {
        this.PID = pid;
        this.page = page;
        this.time = -999;
        this.Ftime = -999;
        this.Frames = -999;
        this.quantum =-999;
        this.pframes =-999;
        this.count =0;
    }

    // setter

    public void setPID(String new_PID) { this.PID = new_PID;}

    public void setPage(int new_page) { this.page = new_page;}

    public void setTime(int time) { this.time = time;}

    public void setFtime(int ftime) { Ftime = ftime;}

    public void setFrames(int frames) { Frames = frames;}

    public void setQuantum(int quantum) { this.quantum = quantum;}

    public void setPframes(int pframes) { this.pframes = pframes;}

    public void setCount(int count) { this.count = count;}

    //  getter

    public String getPID() { return PID;}

    public int getPage() { return page;}

    public int getTime() { return time;}

    public int getFtime() { return Ftime;}

    public int getFrames() { return Frames;}

    public int getQuantum() { return quantum;}

    public int getPframes() { return pframes;}

    public int getCount() { return count;}
}
