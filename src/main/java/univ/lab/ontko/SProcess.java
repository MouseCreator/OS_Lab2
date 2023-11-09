package univ.lab.ontko;

public class SProcess {
  public int cpuTime;
  public int ioBlocking;
  public int cpuDone;
  public int ioNext;
  public int numBlocked;

  public SProcess(int cpuTime, int ioBlocking, int cpuDone, int ioNext, int numBlocked) {
    this.cpuTime = cpuTime;
    this.ioBlocking = ioBlocking;
    this.cpuDone = cpuDone;
    this.ioNext = ioNext;
    this.numBlocked = numBlocked;
  } 	
}
