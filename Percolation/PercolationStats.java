public class PercolationStats 
{
    Percolation p;
    double dimension;
    double numExperiments;
    double[] meanArray;
    
   // perform T independent computational experiments on an N-by-N grid
   public PercolationStats(int N, int T) 
   {
       if (N <= 0 || T <=0)
           throw new java.lang.IllegalArgumentException(" The values for N or T cannot be negative. ");
       
       dimension = N;
       numExperiments = T;
       meanArray = new double[T];
                    
       for (int i=1; i<=T; ++i)
       {
          p = new Percolation(N);
          double openSites = 0; 
          while (!p.percolates())
          {
                int rowRandom = StdRandom.uniform(1, N+1);      
                int colRandom = StdRandom.uniform(1, N+1);         
                if (!p.isOpen(rowRandom, colRandom))
                {
                    p.open(rowRandom, colRandom);
                    openSites++;
                }
          }
          meanArray[i-1] = openSites/(dimension*dimension);
       }
   }
   
   // sample mean of percolation threshold
   public double mean() 
   {
       double mean = 0;
       
       mean = StdStats.mean(meanArray);
           
       return mean;
   }
       
   // sample standard deviation of percolation threshold
   public double stddev() 
   {
       double stdDev = 0;
       
       stdDev = StdStats.stddev(meanArray);
       
       return stdDev;
   }
   
   // returns lower bound of the 95% confidence interval
   public double confidenceLo()  
   {
       double confLo = 0;
       double mean = mean();
       double stddev = stddev();
       
       confLo = mean - ((1.96 * stddev)/Math.sqrt(numExperiments));
       
       return confLo;
   }
   
   // returns upper bound of the 95% confidence interval
   public double confidenceHi() 
   {
       double confHi = 0;
       double mean = mean();
       double stddev = stddev();
       
       confHi = mean + ((1.96 * stddev)/Math.sqrt(numExperiments));
       
       return confHi;
   }
       
   // test client, described below
   public static void main(String[] args)   
   {
       if (args.length <2)
       {
           StdOut.println ( " Please specify size of grid and desired number of computational experiments respectively");
           return;
       }
                   
       int N = Integer.parseInt(args[0]);
       int T= Integer.parseInt(args[1]);
       PercolationStats pStats = new PercolationStats(N, T);
       
       if (pStats == null)
       {
           StdOut.println ( " Something went wrong while initializing Percolation Stats. ");
           return;
       }
       
       // Mean
       double dMean = pStats.mean();
       StdOut.printf (" mean \t\t =");
       StdOut.printf("%f\n", dMean);
       
       // Standard Deviation
       double dStdDev = pStats.stddev();
       StdOut.printf (" stddev \t\t =");
       StdOut.printf("%f\n", dStdDev);
       
       // 95% confidence
       double dConfHi = pStats.confidenceHi();
       double dConfLo = pStats.confidenceLo();
       
       StdOut.printf (" 95%% confidence \t =");
       StdOut.printf("%f , %f\n", dConfLo, dConfHi);                
   }
}