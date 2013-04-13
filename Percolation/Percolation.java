public class Percolation
{
    private WeightedQuickUnionUF uf;
    private int dimension;
    private int virtualTopSite;
    private int virtualBottomSite;
    private SET<Integer> openSites;
    
    // Constructor. Creates an N by N Grid with all sites blocked.
    public Percolation (int N)
    {
        dimension = N;
        
        // Calls quickUnionFind class to create an array on N^2 disjointed objects.
        uf = new WeightedQuickUnionUF(N*N + 2);
        
        // Set virtual top site value to N^2+2. And add connections from all top rows.
        virtualTopSite = N*N+1;
        for (int i=0; i<N; ++i)
            uf.union(virtualTopSite, i);
        
        // Set virtual bottom site value to N^2 + 1. And add connections from all bottom rows.
        virtualBottomSite = N*N;
        
        for (int i=virtualBottomSite - N; i<virtualBottomSite; ++i)
            uf.union(virtualBottomSite, i);
        
        openSites = new SET<Integer>();
    }
    
    // Opens a site row i, column j if not already
    public void open (int i, int j)
    {
        // If row value is not within size of grid.
        if (i <= 0 || i > dimension)
            throw new IndexOutOfBoundsException(" Row value is out of bounds ");
        
        // If column value is not within size of grid.
        if (j <= 0 || j > dimension)
            throw new IndexOutOfBoundsException(" Column value is out of bounds ");
        
        // Check if the site is already open
        if (isOpen(i, j))
                return;
            
        // get the site represented by row i and Column j
        int givenSite = getSite (i, j);
              
        if (givenSite % dimension == 0) // The laftmost row
        {
            int top = getSite(i-1, j);
            int bottom = getSite(i+1, j);
            int right = getSite(i, j+1);
            
            // connect to top
            if (top >= 0 && top < (dimension * dimension) && isOpen(i-1, j))
               uf.union(top, givenSite);
        
            // connect to bottom
            if (bottom >= 0 && bottom < (dimension * dimension) && isOpen(i+1, j)) 
               uf.union(bottom, givenSite);
        
            // connect to right
            if (right >= 0 && right < (dimension * dimension) && isOpen(i, j+1)) 
               uf.union(right, givenSite);
        }      
        else if ((givenSite+1) % dimension == 0) // The rightmost row
        {
            int top = getSite(i-1, j);
            int bottom = getSite(i+1, j);
            int left = getSite(i, j-1);
            
            // connect to top
            if (top >= 0 && top < (dimension * dimension) && isOpen(i-1, j))
               uf.union(top, givenSite);
        
            // connect to bottom
            if (bottom >= 0 && bottom < (dimension * dimension) && isOpen(i+1, j)) 
               uf.union(bottom, givenSite);
        
            // connect to left
            if (left >= 0 && left < (dimension * dimension) && isOpen(i, j-1)) 
               uf.union(left, givenSite);
        }     
        else if (givenSite < dimension) // The topmost row
        {
            int bottom = getSite(i+1, j);
            int left = getSite(i, j-1);
            int right = getSite(i, j+1);
        
            // connect to bottom
            if (bottom >= 0 && bottom < (dimension * dimension) && isOpen(i+1, j)) 
               uf.union(bottom, givenSite);
        
            // connect to left
            if (left >= 0 && left < (dimension * dimension) && isOpen(i, j-1)) 
               uf.union(left, givenSite);
        
            // connect to right
            if (right >= 0 && right < (dimension * dimension) && isOpen(i, j+1)) 
               uf.union(right, givenSite);
        }      
        else if (givenSite >= dimension * (dimension-1) ) // The bottommost row
        {
            int top = getSite(i-1, j);
            int left = getSite(i, j-1);
            int right = getSite(i, j+1);
            
            // connect to top
            if (top >= 0 && top < (dimension * dimension) && isOpen(i-1, j)) //&& !uf.connnected(givenSite, top))
               uf.union(top, givenSite);
        
            // connect to left
            if (left >= 0 && left < (dimension * dimension) && isOpen(i, j-1)) 
               uf.union(left, givenSite);
        
            // connect to right
            if (right >= 0 && right < (dimension * dimension) && isOpen(i, j+1)) 
               uf.union(right, givenSite);
        }      
        else
        {          
            // check if site is connected to all its adjecent sites.
            int top = getSite(i-1, j);
            int bottom = getSite(i+1, j);
            int left = getSite(i, j-1);
            int right = getSite(i, j+1);
            
            // connect to top
            if (top >= 0 && top < (dimension * dimension) && isOpen(i-1, j)) //&& !uf.connnected(givenSite, top))
               uf.union(top, givenSite);
        
            // connect to bottom
            if (bottom >= 0 && bottom < (dimension * dimension) && isOpen(i+1, j)) 
               uf.union(bottom, givenSite);
        
            // connect to left
            if (left >= 0 && left < (dimension * dimension) && isOpen(i, j-1)) 
               uf.union(left, givenSite);
        
            // connect to right
            if (right >= 0 && right < (dimension * dimension) && isOpen(i, j+1)) 
               uf.union(right, givenSite);
        }
        
        openSites.add(givenSite);
    }


    // is site (row i, column j) full?
    // A site is a full site if it is open and it is connected to a site in top row via adjecent open sites.
    public boolean isFull (int i, int j)
    {
        // If row value is not within size of grid.
        if (i <= 0 || i > dimension)
            throw new IndexOutOfBoundsException(" Row value is out of bounds ");
        
        // If column value is not within size of grid.
        if (j <= 0 || j > dimension)
            throw new IndexOutOfBoundsException(" Column value is out of bounds ");
        
        // If site is not Open, it cannot be full.
        if (!isOpen(i,j))
                return false;
        
        // check if any of the adjecent sites is connected to the top virtual site.
        // top
        int top = getSite(i-1, j);
        if (top >= 0 && top < (dimension * dimension) && uf.connected(top, virtualTopSite))
               return true;
        
        // bottom
        int bottom = getSite(i+1, j);
        if (bottom >= 0 && bottom < (dimension * dimension) && uf.connected(bottom, virtualTopSite))
               return true;
        
        // left
        int left = getSite(i, j-1);
        if (left >= 0 && left < (dimension * dimension) && uf.connected(left, virtualTopSite))
               return true;
        
        // right
        int right = getSite(i, j+1);
        if (right >= 0 && right < (dimension * dimension) && uf.connected(right, virtualTopSite)) 
               return true;

        return false;
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j)
    {
        // If row value is not within size of grid.
        if (i <= 0 || i > dimension)
            throw new IndexOutOfBoundsException(" Row value is out of bounds ");
        
        // If column value is not within size of grid.
        if (j <= 0 || j > dimension)
            throw new IndexOutOfBoundsException(" Column value is out of bounds ");
        
       int givenSite = getSite(i, j);
       
       if (openSites.contains(givenSite))
           return true;
       
       return false;
    }

    // Gets the site represented by row i and column j
    private int getSite (int i, int j)
    {
        return ((i-1)*dimension - 1)+j;
    }

    public boolean percolates()
    {
        if (uf.connected(virtualTopSite, virtualBottomSite))
            return true;
        
        return false;
    }
}