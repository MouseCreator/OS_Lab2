package univ.lab.ontko;// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

import univ.lab.scheduling.SchedulingAlgorithm;

import java.io.*;
import java.util.*;

public class Scheduling {

  private static int processNum = 5;
  private static int meanDev = 1000;
  private static int standardDev = 100;
  private static int runtime = 1000;
  private static final Vector<SProcess> processVector = new Vector<>();
  private static Results result = new Results("null","null",0);
  private static final String resultsFile = "src/main/resources/Summary-Results.txt";
  private static final String SAMPLE_FILE = "src/main/resources/test.conf";

  private static void init(String file) {
    File f = new File(file);
    String line;
    String tmp;
    int cputime;
    int ioblocking;
    double X;

    try {   
      //BufferedReader in = new BufferedReader(new FileReader(f));
      DataInputStream in = new DataInputStream(new FileInputStream(f));
      while ((line = in.readLine()) != null) {
        if (line.startsWith("numprocess")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          processNum = Common.s2i(st.nextToken());
        }
        if (line.startsWith("run_time_average")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          meanDev = Common.s2i(st.nextToken());
        }
        if (line.startsWith("run_time_stddev")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          standardDev = Common.s2i(st.nextToken());
        }
        if (line.startsWith("process")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          ioblocking = Common.s2i(st.nextToken());
          X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
          cputime = (int) X + meanDev;
          processVector.addElement(new SProcess(cputime, ioblocking, 0, 0, 0));
        }
        if (line.startsWith("runtime")) {
          StringTokenizer st = new StringTokenizer(line);
          st.nextToken();
          runtime = Common.s2i(st.nextToken());
        }
      }
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void debug() {
    int i;

    System.out.println("processnum " + processNum);
    System.out.println("meandevm " + meanDev);
    System.out.println("standdev " + standardDev);
    int size = processVector.size();
    for (i = 0; i < size; i++) {
      SProcess process = processVector.elementAt(i);
      System.out.println("process " + i + " " + process.cpuTime + " " + process.ioBlocking + " " + process.cpuDone + " " + process.numBlocked);
    }
    System.out.println("runtime " + runtime);
  }

  public static void main(String[] args) {
    int i;
    String filename;
    if (args.length == 0) {
      filename = SAMPLE_FILE;
    }
    else if (args.length == 1) {
      filename = args[0];
    } else {
      System.out.println("Usage: 'java Scheduling <INIT FILE>'");
      System.exit(-1);
      return;
    }
    File f = new File(filename);
    if (!(f.exists())) {
      System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
      System.exit(-1);
    }  
    if (!(f.canRead())) {
      System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
      System.exit(-1);
    }
    System.out.println("Working...");
    init(filename);
    if (processVector.size() < processNum) {
      i = 0;
      while (processVector.size() < processNum) {
          double X = Common.R1();
          while (X == -1.0) {
            X = Common.R1();
          }
          X = X * standardDev;
        int cputime = (int) X + meanDev;
        processVector.addElement(new SProcess(cputime,i*100,0,0,0));
        i++;
      }
    }
    SchedulingAlgorithm algorithm = new FirstComeFirstServedSchedulingAlgorithm();
    result = algorithm.run(runtime, processVector, result);
    try {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      out.println("Scheduling Type: " + result.schedulingType);
      out.println("Scheduling Name: " + result.schedulingName);
      out.println("Simulation Run Time: " + result.compuTime);
      out.println("Mean: " + meanDev);
      out.println("Standard Deviation: " + standardDev);
      out.println("Process #\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked");
      for (i = 0; i < processVector.size(); i++) {
        SProcess process = processVector.elementAt(i);
        out.print(i);
        if (i < 100) { out.print("\t\t"); } else { out.print("\t"); }
        out.print(process.cpuTime);
        if (process.cpuTime < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(process.ioBlocking);
        if (process.ioBlocking < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.print(process.cpuDone);
        if (process.cpuDone < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
        out.println(process.numBlocked + " times");
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
  System.out.println("Completed.");
  }
}

