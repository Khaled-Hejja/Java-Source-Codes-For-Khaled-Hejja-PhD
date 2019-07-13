import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class NewChawdAnyVNR {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
double[] Load = {1.0};//,0.2,0.4,0.6,0.8,};
int[] Time = {8};//1,2,3,

for (int Lloop=0 ; Lloop<Load.length ; Lloop++)
 {

 for (int Tloop=0 ; Tloop<Time.length ; Tloop++)
  {
	double migtrig  = 6.57;
	int Max_Runs	= 1;				   
	int MaximumVNRs = 500 * Time[Tloop]; // this is would give 2000, 2500, 3000, 3500, 4000
	long startTime;
	long endTime;
	int be=15;

//-------------------------------------------------------------------------------------------------------------------	
//------------------------------------------- Initializing the SN settings ------------------------------------------	
//-------------------------------------------------------------------------------------------------------------------	
double[][] Runs_output = new double[MaximumVNRs][15];

for (int Runs=0 ; Runs<Max_Runs ; Runs++)
{
int Rem = 0;

//-------------------------------------------------------------------------------
//--------------------------------- Create SN ----------------------------------
//-------------------------------------------------------------------------------
int VNodes		      = 10; // this is to indicate that maximum nodes per biggest VNR are (VNodes).
int SN_Nodes	      = 50;
double alpha		  = 0.7;
double beta		      = 0.9;
double Prob_Thrreshod = 0.5;

double Max_X_corr = 1; // Max X-corr for any VN node
double Min_X_corr = 0; // Min X-corr for any VN node
double Max_Y_corr = 1; // Max Y-corr for any VN node
double Min_Y_corr = 0; // Min Y-corr for any VN node	

double SN_Max_x = Max_X_corr - Min_X_corr; // to be used in calculating the Max distance between the most distant SN nodes
double SN_Max_y = Max_Y_corr - Min_Y_corr; // to be used in calculating the Max distance between the most distant SN nodes

double VN_Max_X_corr = Max_X_corr; // Max X-corr for any VN node.
double VN_Min_X_corr = Min_X_corr; // Min X-corr for any VN node.
double VN_Max_Y_corr = Max_Y_corr; // Max Y-corr for any VN node.
double VN_Min_Y_corr = Min_Y_corr; // Min Y-corr for any VN node.	
			
double[] VN_X_corr	= new double[SN_Nodes];  // A matrix to save the X_corr for the VN node.
double[] VN_Y_corr	= new double[SN_Nodes];  // A matrix to save the Y_corr for the VN node.

double Max_Distance_Between_SNodes     = 1;//(double) Math.sqrt(Math.pow(SN_Max_x, 2)+ Math.pow(SN_Max_y, 2));  // The max distance between the most distant SN nodes. This will be used in calculating the demanded distance for a VN Node.
double Min_Distance_Between_SNodes     = 0;// Max_Distance_Between_SNodes/10; // the minimum distance between SN nodes. This will be used in calculating the demanded distance for a VN Node
double[][] Distance_SN_Nodes 	       = new double[SN_Nodes][SN_Nodes];		
double[][] Propability_Links_SN_Nodes  = new double[SN_Nodes][SN_Nodes];		
int[][] adjacencyMatrix	               = new int[SN_Nodes][SN_Nodes];

double[][] SN_Nodes_Locations   = new double[SN_Nodes][2];				

int edgesnum=0;		

for (int i=0 ; i<SN_Nodes ; i++)
{

SN_Nodes_Locations[i][0]	= (Min_X_corr + (int)(Math.random() * ((Max_X_corr  - Min_X_corr)  + 1))); // generating a random X-corr for the SN node i.
SN_Nodes_Locations[i][1]	= (Min_Y_corr + (int)(Math.random() * ((Max_Y_corr  - Min_Y_corr)  + 1))); // generating a random Y-corr for the SN node i.

}

for (int i=0 ; i<SN_Nodes ; i++)
for (int j=0 ; j<SN_Nodes ; j++)				
Distance_SN_Nodes[i][j] = (Math.random() * Max_Distance_Between_SNodes);

for (int i=0 ; i<SN_Nodes ; i++)
{
for (int j=0 ; j<SN_Nodes ; j++)
{
Propability_Links_SN_Nodes[i][j]  =  (alpha * Math.exp(-Distance_SN_Nodes[i][j] / (beta * Max_Distance_Between_SNodes)));

if (Propability_Links_SN_Nodes[i][j] >= Prob_Thrreshod)
{
if (j>i)
{
  adjacencyMatrix[i][j] = 1;
	   edgesnum++;
}
}

System.out.print(Math.round(adjacencyMatrix[i][j]) + "-");
}
System.out.println();
}

System.out.println("Number of links\t" + edgesnum/SN_Nodes);
//System.out.println("Run Number \t" + Runs);


/* Initialize the SN Nodes and Edges with random values 
* This is done just once in the first time to run the network.
*/		
double[] SN_Nodes_MAX_Power_Cap  = new double[ SN_Nodes ];  // a matrix to store the Maximum power capacity for each SN as a stand alone Data Center
double[] SN_Nodes_MIN_Power_Cap  = new double[ SN_Nodes ];  // a matrix to store the Maximum power capacity for each SN as a stand alone Data Center
double[] SN_Nodes_idle_PCons     = new double[ SN_Nodes ];	// a matrix to store the Power Consumption values.
double   SNTotidlePCons          = 0;			 			// a matrix to save the total ideal power.
double[] SN_Nodes_Current_PCons  = new double[ SN_Nodes ];  // a matrix to store the Power Consumption values.
double   Alfa_CPU                = 1;
double   Beta_BW                 = 1;

double[] SN_Nodes_MAX_CPU_Cap    = new double[ SN_Nodes ];  // a matrix to store the Maximum CPU capacity for each SN as a stand alone Data Center
double[] SN_Nodes_CPU_Cons       = new double[ SN_Nodes ];  // a matrix to store the CPU values.
double[] SN_Selected_Path_CPU_Utilization = new double[SN_Nodes];
double[] SN_Nodes_CPU_Utilization= new double[ SN_Nodes ];  // a matrix to store the initial CPU utilization.
double[] SN_Nodes_CPU_Cur_Util   = new double[ SN_Nodes ];  // a matrix to store the current CPU utilization after each iteration
double[][] SN_Edges_MAX_BW_Cap   = new double[SN_Nodes][SN_Nodes]; // a matrix to save the maximum BW for each edge.
double[][] SN_BWmatrix           = new double[SN_Nodes][SN_Nodes]; // a matrix to store edges BW the size of the adjacency matrix.
double[][] SN_BW_Util            = new double[SN_Nodes][SN_Nodes]; // a matrix to store edges current BW utilization the size of the adjacency matrix.
int[][] SN_DelayMatrix           = new int  [SN_Nodes][SN_Nodes];  // a matrix to store edges Delay the size of the adjacency matrix.				
int[][] SN_used_paths            = new int  [MaximumVNRs][SN_Nodes];  // to save the used paths before triggering power compensation
int[][] SN_Selected_Path    	 = new int  [MaximumVNRs][SN_Nodes];  // to save the nodes of the SN path that was selected to embed a VNR.

for (int i=0 ; i<MaximumVNRs ; i++)
{
for (int j=0 ; j<10 ; j++)
{
	SN_Selected_Path[i][j] = -1; // this is to fill in the nodes positions of the array with 100. 
	//System.out.print(SN_Selected_Path[i][j]);
}
//System.out.println();
}

double Sum_SN_Utilization = 0.0;
double nodes_utilization;
double edges_utilization;
double MaxPWCap          = 0; // Maximum PW capacity of all SN nodes 
int edges                = 0;
double Sum_edges_BW      = 0.0;
double Min_PWCONS        = 165;	
int DealyRange           = 50; // ranges of propagation delay in msec.
int Minsd                = 0;  //15 minimum delay in a SN path.
int Maxsd                = 0;  // 25 Maximum delay in a SN path.

//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------------  Define SN parameters  -----------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------

for (int i=0 ; i < SN_Nodes ; i++)
{

SN_Nodes_MAX_CPU_Cap [i] = 75;//(50 + Math.random() * ((100 - 50)  + 1));  // Initialize CPU values for each SN node randomly 50-100.
SN_Nodes_CPU_Cons    [i] = SN_Nodes_MAX_CPU_Cap [i];  // Initialize consumed CPU
SN_Nodes_CPU_Cur_Util[i] = ((( SN_Nodes_MAX_CPU_Cap[i] - SN_Nodes_CPU_Cons[i])/ SN_Nodes_MAX_CPU_Cap[i]));

SN_Nodes_MAX_Power_Cap   [i] =  15 * SN_Nodes_MAX_CPU_Cap[i];  //(double) Math.round((Min_SN_DCPw   + (double)(Math.random() * ((Max_SN_DCPw     -   Min_SN_DCPw)        + 1)))); // fill the SN node's power with random values between 0-20.
SN_Nodes_MIN_Power_Cap   [i] =  165; // fill the SN node's minimum power with random values.
SN_Nodes_idle_PCons      [i] =  SN_Nodes_MIN_Power_Cap[i]; // Idle power consumption each SN node.
SN_Nodes_Current_PCons   [i] =  SN_Nodes_MIN_Power_Cap[i] 
	                     +((SN_Nodes_MAX_Power_Cap[i]
	                     -  SN_Nodes_MIN_Power_Cap[i])
	                     *  SN_Nodes_CPU_Cur_Util [i]); // Current Power consumption per each SN node. 
SNTotidlePCons += SN_Nodes_idle_PCons[i];

MaxPWCap			  += SN_Nodes_MAX_Power_Cap[i];


System.out.println(" MAX_PCons = " + i + "\t" + SN_Nodes_MAX_Power_Cap [i] +
			   " MIN_PCons = " + i + "\t" + SN_Nodes_MIN_Power_Cap [i] +
			   " Cur_PCons = " + i + "\t" + SN_Nodes_Current_PCons [i] +
			   " CPUCap    = " + i + "\t" + SN_Nodes_CPU_Cons      [i] +
			   " CPU_Utili = " + i + "\t" + SN_Nodes_CPU_Cur_Util  [i]);	

}// End of for (int i=0 ; i < SN_Nodes ; i++)

//-----------------------------------------------------------------------------------------------------------
//---------------------------  Initialization for the SN Edges BW and Delay  --------------------------------  

//System.out.println();
//System.out.println("***************** BW and Delay values ************");
//System.out.println();						

//----------------------- Heterogeneous SN BW and Delay initiation	-------------------------------------------	
	
for (int i=0 ; i<SN_Nodes ; i++)
for (int j=0 ; j<SN_Nodes ; j++)
{
if (adjacencyMatrix[i][j] == 1)
{//
SN_Edges_MAX_BW_Cap [i][j] =75;//(50 + Math.random() * ((100 - 50)  + 1));// fill the SN BW for the edges with random values between 0-20.
SN_BWmatrix         [i][j] =  SN_Edges_MAX_BW_Cap [i][j];
SN_DelayMatrix      [i][j] = (int)(adjacencyMatrix[i][j] * Math.round((Minsd  + (Math.random() * ((Maxsd  - Minsd)  + 1)))));  // fill the SN Delay for the edges with random values between 0-5.
				
	//System.out.println("SN_Edge--(" + i + ")-->(" + j + ")\t" +  "BW=\t" + SN_BWmatrix[i][j] + "\t" + 
   //                           "                               Delay=\t" + SN_DelayMatrix[i][j]);

}// End if (adjacencyMatrix[i][j] == 1)	
//System.out.println();
}// End of for (int j=0 ; j<SN_Nodes ; j++)		

//System.out.println();
//System.out.println("__________________ End of a SN declaration __________________");		


//-------------------------------------------------------------------------------------------------------------------
//---------------------------------------  Start Creating and Initializing VNRs  -----------------------------------.
//-------------------------------------------------------------------------------------------------------------------

//70 max
//double[] demcpu    = {23.0,31.0,36.0,39.0,49.0,62.0,80.0,62.0,49.0,39.0,36.0,31.0,23.0,31.0,36.0,39.0,49.0};
//double[] dembw     = {14.0,12.0,10.0,8.0,6.5,7.0,8.5,10.0,11.0,12.0,15.0,17.0,14.0,12.0,10.0,8.0,6.5};

// 50 max
//double[] demcpu    = {3.0,11.0,16.0,19.0,23.0,32.0,50.0,32.0,23.0,19.0,16.0,11.0,03.0,11.0,16.0,19.0,23.0};
//double[] dembw     = {14.0,12.0,10.0,8.0,6.5,7.0,8.5,10.0,11.0,12.0,15.0,17.0,14.0,12.0,10.0,8.0,6.5};

//20 cpu max. 50 bw max
//double[] demcpu    = {6.57,8.86,10.29,11.14,12.29,14.86,20.00,14.86,12.86,11.14,10.29,8.86,6.57,8.86,10.29,11.14,12.29};
//double[] dembw     = {41.18,35.29,29.41,23.53,19.12,20.59,25.00,29.41,32.35,36.76,44.12,50.00,41.18,35.29,29.41,23.53,19.12};

double[] demcpu    = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
double[] dembw     = {1,3.5,6,8.5,11,13.5,16,18.5,21,23.5,26,28.5,31,33.5,36,38.5,41,43.5,46,48.5};

// Next for loop is the starting of generating VNRs and the Embedding.

//VNR: define controlling parameters for the random equations generating power, CPU, BW and delay matrices.

/* Create a grand loop of 10 instances, in each loop create new SN and follow on as follows:
* 1. Create simulation loop of length equal to 'VNR'
* 2. At each 'VNR' generate 4 VNRs of different number of nodes per each
* 3. Start the embedding process for each one of the 4 VNRs
* 4. After the first 5 VNRs, start expiring VNRs.
* 5. Calculate number of active SN nodes, power consumption, revenue, and acceptance ratio.	
*/

double[][] D_VNRsNodes_CPU   = new double[MaximumVNRs][10]; // Matrix to save the CPU values for the VNRs Nodes
double[][] D_VNRsEdges_BW    = new double[MaximumVNRs][45];  // Matrix to save the BW values for the VNRs Edges
double[][] D_VNRsEdges_Delay = new double[MaximumVNRs][45];  // Matrix to save the Delay values for the VNRs Edges.
int[] NumberofnodesinVNR     = new int[MaximumVNRs];

int V = 0;
int D = 0; // to control the loop of the daily demands.
int count = 0;
/*	
double clmax = 0.4;
double clmin = 0.2;
double cpu_load_adjustment = clmin + (Math.random() * (clmax  - clmin));
System.out.print( cpu_load_adjustment);

double blmax = 0.;
double blmin = 0.2;
double bw_load_adjustment = blmin + (Math.random() * (blmax  - blmin));
System.out.print( bw_load_adjustment);
*/
int Maxnodespervnr = 10; // Maximum number of nodes per a VNR.
			int Minnodespervnr = 2;  // Minimum number of nodes per a VNR.

for ( int Tot_Num_of_VNRs = 0; Tot_Num_of_VNRs < MaximumVNRs; Tot_Num_of_VNRs++ )
{
	if ( D < demcpu.length )
	{		
	for ( int VNRs=0 ; VNRs < Time[Tloop] ; VNRs++ )
	{    					
			
			int NodesinVNRpath = (Minnodespervnr + (int)(Math.random() * ((Maxnodespervnr  - Minnodespervnr)  + 1)));
			NumberofnodesinVNR[Tot_Num_of_VNRs] = NodesinVNRpath; // a matrix saving the number of nodes per each VNR.
			
			int EdgesinVNRpath = NodesinVNRpath - 1 ;
			int Maxvd	       = 100;  // maximum delay in msec. (http://www.ciscopress.com/articles/article.asp?p=606583&seqNum=4)
			int Minvd	       = 20;  // minimum delay in msec.(3GPP TR 38.801 V14.0.0 (2017-03))
			//Nokia: powell_3ca_1a_0917
		
			//End of VNRs controlling parameters 
			/*
			System.out.println();	
			System.out.println("This is VNR number---------- = " + Tot_Num_of_VNRs);
			System.out.println("Nodes in the current VNR are = " + NodesinVNRpath);
			System.out.println();
			*/
			//-----------------------------------------------------------------------------------------------------------------------

			//System.out.println();
			//System.out.println("***************** Nodes' Power and CPU values in this VNR ************");
			//System.out.println();
			//					
				
			for ( int j=0 ; j < NodesinVNRpath ; j++ )
				{ /// I need to generate the 2000 VNRs, each 4 VNR has different number of nodes
					// and each VNR has different demands according to the traffic profile of the day.
					// so make 
					
					D_VNRsNodes_CPU  [Tot_Num_of_VNRs][j] = demcpu[D];   // (0 + Math.random() * ((20 - 0)  + 1))*Load[Lloop]; *fill the node's demanded cpu capacity with random values between 0-20.
		
					//System.out.print( "\t" + D_VNRsNodes_CPU[count][j] + "\t");
			
				} // end of part filling CPU demands for the VNRs
			
			//System.out.println();	
			
					//System.out.println();
					//System.out.println("*****************  Edges' BW and Delay values in this VNR ************");
					//System.out.println();
					//

			for ( int j=0 ; j < 45 ; j++ )
				{
					
				D_VNRsEdges_BW[Tot_Num_of_VNRs][j]   = dembw[D];      //(0 + Math.random() * ((50 - 0)  + 1))*Load[Lloop]  * fill the demanded BW for the edges with random values between 0-20.
				D_VNRsEdges_Delay[Tot_Num_of_VNRs][j] = (int) Math.round((Minvd  + (Math.random() * ((Maxvd  - Minvd)  + 1))));  // fill the demanded Delay for the edges with random values between 0-5.
			
				} // end of part filling BW and Delay demands for the VNRs			
						
				//Tot_Num_of_VNRs++;
				//System.out.print(Tot_Num_of_VNRs);
			
			//System.out.println( "VNR#\t" + Tot_Num_of_VNRs+ "Count\t" + count + "#Nods\t" + NumberofnodesinVNR[count] + "\t" + D );

			V = VNRs;
			count++;
			} // end of creating 4 VNRs for loop
		
			D++;
		//System.out.print(Tot_Num_of_VNRs);

			} else 
			{
			D=0;
		//load_adjustment = lmin + (Math.random() * (lmax  - lmin));
		//System.out.print( load_adjustment);
		
			};// end of D which checks for the total number of daily demands.
	
			//System.out.println( Tot_Num_of_VNRs+ "\t" + NumberofnodesinVNR[Tot_Num_of_VNRs] + "\t" + D );

	//Tot_Num_of_VNRs = count;
} // end of For loop which fills in the 2000 VNRs matrices.

//System.out.print(Tot_Num_of_VNRs);

//for (int i=0 ; i<count ; i++)
	//System.out.println("Count\t" + count + " NumberofnodesinVNR[count] \t" + NumberofnodesinVNR[i] );
for (int i = 0 ; i < 100 ; i++)
for (int j = 0 ; j < 10 ; j++)
{            
	
System.out.print(" CPU=\t " + "VNR #\t" + i );

System.out.print( "\t" + D_VNRsNodes_CPU[i][j] + "\t");

System.out.println();	
}
			
for (int i = 0 ; i < 100 ; i++)
	for (int j = 0 ; j < 45 ; j++)
{            

System.out.print("BW=\t" + "VNR #\t" + i );

System.out.print("\t" + D_VNRsEdges_BW[i][j] + "\t");
	
System.out.println();
	
} // End of i for loop for printing the VNRs

/*

for (int i = 0 ; i < MaximumVNRs ; i++)
{            

System.out.print("Delay=\t" + "VNR #\t" + i );

System.out.print("\t" + D_VNRsEdges_Delay[i] + "\t");
	
System.out.println();

} // End of i for loop for printing the VNRs
*/	
System.out.println("Number of links\t" + edgesnum/SN_Nodes);

//_________________________________________  End of VNRs declaration  ___________________________________________________________________	

//____________________________________________  Create SN Paths   ________________________________________________________
//_________________________SN paths are classified according to the number of nodes per path_____________________________
//------------------------------------------------------------------------------------------------------------------------

//__________________________________________________  SN Paths   ________________________________________________________
//_________________________SN paths are classified according to the number of nodes per path_____________________________
//------------------------------------------------------------------------------------------------------------------------

		//---------------- THis section is to generate the two nodes edges from the Adjacency Matrix  ------------

		int SNP2factrowsize = 5; // to specify the initial rows size in the 3,4,5 paths types matrices.
		int[][] SNP2nodes = new int[adjacencyMatrix.length*100][2];
		int SNP2=0;
		int SNP2length = 0;
				
				for (int i=0 ; i<(adjacencyMatrix.length); i++)
				{
					for (int j=0 ; j<adjacencyMatrix.length ; j++)
					{
						if (adjacencyMatrix[i][j] == 1)
						{				
							SNP2nodes[SNP2length][0]= i; // save the node in row i that has "1" value with column j.
							SNP2nodes[SNP2length][1]= j; // save the node in the column j that has "1" value with row i.
							SNP2length++;
							SNP2 = SNP2length; // this is a counter to get the length of the SN_P2_nodes matrix.
							//System.out.println(SNP2 + "   " + SNP2length);
						}
					}
				}
			
				//System.out.println("------------------------- SN Paths Type 2 Nodes  ---------------------------------------");

				int[][] SN_P2_nodes = new int [SNP2][2];
				int[][] SN_P2_nodesr = new int [SNP2][2];

				// we make the SN_P2_nodes as per the new new length saved in SNP2
				for (int i=0 ; i<SNP2 ; i++)			
				{
					SN_P2_nodes[i][0] = SNP2nodes [i][0];	
					SN_P2_nodes[i][1] = SNP2nodes [i][1];
					
					SN_P2_nodesr[i][0] = SNP2nodes [i][1];	
					SN_P2_nodesr[i][1] = SNP2nodes [i][0];
					//System.out.println(i + "   " + SN_P2_nodes.length + "   " + SN_P2_nodes[i][0] + 
					//		                                            " , " + SN_P2_nodes[i][1]);
				
				}		
			
				//System.out.println();
			
			// -------------------------   End of generating the SN_P2_nodes paths from the Adjacency Matrix   ----------- 
				

				int[] sorted_CPUsum_paths2 = new int[SN_P2_nodes.length];// this matrix saves the edges of type two nodes
				int a2=0;
				
				for (int a=0 ; a<SN_P2_nodes.length ; a++ )
				{
					sorted_CPUsum_paths2[a]=a2;
					a2++;
					//System.out.println(sorted_CPUsum_paths2[a]);
				} // end of for loop to print all paths of type 2 nodes
				System.out.println( "Type-2 paths\t" +SN_P2_nodes.length);
		//-----------------------------------------------------------------------------------------------------------------------
		//-----------------------------------------------------------------------------------------------------------------------
		//------------------------------------------ Paths of Type - 3 - Nodes --------------------------------------------------

				int[][] PT3 = new int[11000][3];// a matrix for paths of type 3 nodes
				
				int k3 = 0 ; // counter used in indexing new paths of type 3 nodes 
				int j3 = 0 ; // counter for the nodes in the matrix of paths type 3 nodes
				
				PT3[k3][j3]   = SN_P2_nodes[k3][j3];   // a matrix to save the first node of the path to start searching
				PT3[k3][j3+1] = SN_P2_nodes[k3][j3+1]; // a matrix to save the second node from the first path to start searching 
						
				//System.out.println(" k  = " + k3 + " j = " + j3 );
				//System.out.println(PT3[k3][j3]+ "   " + PT3[k3][j3+1]+ "   "+ PT3[k3][j3+2]);

				for (int z=0 ; z<SN_P2_nodes.length; z++)
				{			
				for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
				{	
					PT3[k3][j3]   = SN_P2_nodes[z][j3];   // a matrix used while searching for the next connected node
					PT3[k3][j3+1] = SN_P2_nodes[z][j3+1]; // a matrix used while searching for the next connected node
					
				//System.out.println(" k3  = " + k3 + " j3 = " + j3 +  " z = "+ z + " i =  " + i);
				//	System.out.println("---------------------");		
						if (PT3[k3][j3+1]== SN_P2_nodes[i+1][j3]&& k3<10000)
						{
							
							PT3[k3][j3+2] = SN_P2_nodes[i+1][j3+1]; // this saves the selected connecting node
						 /*   
							  System.out.println(PT3[k3][j3]   + "   " + 
							                     PT3[k3][j3+1] + "   " + 
							                     PT3[k3][j3+2]);
						 */   
						    k3++; // increase the counter to the next path in search
			
							PT3[k3][j3]   = SN_P2_nodes[z][j3];   // initialize the first node in the next path
							PT3[k3][j3+1] = SN_P2_nodes[z][j3+1]; // initialize the second node in the next path
							
						//System.out.println(" k3  = " +k3 + " j3 = " + j3 +  " z = "+ z + " i =  " + i);
						//System.out.println("........................");		

						}else				
							if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j3+1] != 0))
							{
continue; // go to the next path in the paths of type 2 to search for the next connecting node
							}else
if (SN_P2_nodes[z][j3+1] != 0)
{
	System.out.println("next z");
}else
	break;

				 } // end of i for loop
			
				}// end of z for loop		
				
			//	System.out.println("------------------------- SN Paths Type 3 Nodes  ---------------------------------------");

				int[][] SN_P3_nodes = new int[k3][3];
				
				for (int i=0 ; i<k3 ; i++)
				{
					//System.out.print(" SNP3-->"+ i + "  ");
					
					for (int jj=0 ; jj<3 ; jj++)
					{
						SN_P3_nodes[i][jj]=PT3[i][jj];             // save the new paths of type 3 nodes					
						//System.out.print(SN_P3_nodes[i][jj]+ ","); // print the path of type 3 nodes
					}
				//System.out.println();
				}
			
				int[][] SN_P3_nodesr = new int[k3][3];

				for (int i=0 ; i<k3 ; i++)
				{
					//System.out.print(" SNP3-->"+ i + "  ");
					
					for (int jj=2 ; jj>-1 ; jj--)
					{
						SN_P3_nodesr[i][2-jj]=PT3[i][jj];             // save the new paths of type 3 nodes					
						//System.out.print(SN_P3_nodesr[i][2-jj]+ ","); // print the path of type 3 nodes
					}
				//System.out.println();
				}
				
				int[] sorted_CPUsum_paths3 = new int[SN_P3_nodes.length]; // a matrix to save the path numbers of formulated type 3 nodes
				int a3=0;
				
				for (int a=0 ; a<SN_P3_nodes.length ; a++ )
				{
					sorted_CPUsum_paths3[a]=a3;// initialize the matrix that contains the path numbers of the newly formulated paths of type 3 nodes
					a3++;
					//System.out.println(sorted_CPUsum_paths3[a]); // print the path numbers of newly formulated paths type 3 nodes.
				}			
				System.out.println( "Type-3 paths\t" +SN_P3_nodes.length);

				//-----------------------------------------------------------------------------------------------------------------------
				//-----------------------------------------------------------------------------------------------------------------------
				//------------------------------------------ Paths of Type - 31 Star - Nodes --------------------------------------------------

						int SN31 =  SN_P2_nodes.length * SNP2factrowsize;
						int[][] PT31 = new int[15000][3];// a matrix for paths of type 3 nodes
						
						int k31 = 0 ; // counter used in indexing new paths of type 3 nodes 
						int j31 = 0 ; // counter for the nodes in the matrix of paths type 3 nodes
						
						PT31[k31][j31]   = SN_P2_nodes[k31][j31];   // a matrix to save the first node of the path to start searching
						PT31[k31][j31+1] = SN_P2_nodes[k31][j31+1]; // a matrix to save the second node from the first path to start searching 

						//System.out.println(" k  = " + k3 + " j = " + j3 );
						//System.out.println(PT3[k3][j3]+ "   " + PT3[k3][j3+1]+ "   "+ PT3[k3][j3+2]);

						for (int z=0 ; z<SN_P2_nodes.length; z++)
						{			
						for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
						{	
							PT31[k31][j31]   = SN_P2_nodes[z][j31];   // a matrix used while searching for the next connected node
							PT31[k31][j31+1] = SN_P2_nodes[z][j31+1]; // a matrix used while searching for the next connected node
							
						//System.out.println(" k3  = " + k3 + " j3 = " + j3 +  " z = "+ z + " i =  " + i);
						//	System.out.println("---------------------");		
if ((PT31[k31][j31]    == SN_P2_nodes[i+1][j31]) 
 && (PT31[k31][1]      != SN_P2_nodes[i+1][1])  && k31<10000)
{
	
	PT31[k31][j31+2] = SN_P2_nodes[i+1][1]; // this saves the selected connecting node
 /*   
	  System.out.println(PT3[k3][j3]   + "   " + 
	                     PT3[k3][j3+1] + "   " + 
	                     PT3[k3][j3+2]);
 */   
    k31++; // increase the counter to the next path in search
					
	PT31[k31][j31]   = SN_P2_nodes[z][j31];   // initialize the first node in the next path
	PT31[k31][j31+1] = SN_P2_nodes[z][j31+1]; // initialize the second node in the next path
	
//System.out.println(" k3  = " +k3 + " j3 = " + j3 +  " z = "+ z + " i =  " + i);
//System.out.println("........................");		

}else				
	if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j31+1] != 0))
	{
		continue; // go to the next path in the paths of type 3 to search for the next connecting node
	}else
		if (SN_P2_nodes[z][j31+1] != 0)
		{
			System.out.println("next z");
		}else
			break;

						 } // end of i for loop
					
						}// end of z for loop		
						
					//	System.out.println("------------------------- SN Paths Type 3 Nodes  ---------------------------------------");

						int[][] SN_P31_nodes = new int[k31][3];
						
						for (int i=0 ; i<k31 ; i++)
						{
							//System.out.print(" SNP31-->"+ i + "  ");
							
							for (int jj=0 ; jj<3 ; jj++)
							{
SN_P31_nodes[i][jj]=PT31[i][jj];             // save the new paths of type 3 nodes					
//System.out.print(SN_P31_nodes[i][jj]+ ","); // print the path of type 3 nodes
							}
						//System.out.println();
						}
					
						int[][] SN_P31_nodesr = new int[k31][3];

						for (int i=0 ; i<k31 ; i++)
						{
							//System.out.print(" SNP31-->"+ i + "  ");
							
							for (int jj=2 ; jj>-1 ; jj--)
							{
SN_P31_nodesr[i][2-jj]=PT31[i][jj];             // save the new paths of type 31 nodes					
//System.out.print(SN_P31_nodesr[i][2-jj]+ ","); // print the path of type 31 nodes
							}
						//System.out.println();
						}
						
						int[] sorted_CPUsum_paths31 = new int[SN_P31_nodes.length]; // a matrix to save the path numbers of formulated type 3 nodes
						int a31=0;
						
						for (int a=0 ; a<SN_P31_nodes.length ; a++ )
						{
							sorted_CPUsum_paths31[a]=a31;// initialize the matrix that contains the path numbers of the newly formulated paths of type 3 nodes
							a31++;
							//System.out.println(sorted_CPUsum_paths31[a]); // print the path numbers of newly formulated paths type 3 nodes.
						}			
						System.out.println( "Type-31 paths\t" +SN_P31_nodes.length);

						
//-----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------ Paths of Type - 311 loop - Nodes --------------------------------------------------

int[][] PT311 = new int[15000][3];// a matrix for paths of type 3 nodes

int k311 = 0 ; // counter used in indexing new paths of type 3 nodes 
int j311 = 0 ; // counter for the nodes in the matrix of paths type 3 nodes

PT311[k311][j311]   = SN_P2_nodes[k311][j311];   // a matrix to save the first node of the path to start searching
PT311[k311][j311+1] = SN_P2_nodes[k311][j311+1]; // a matrix to save the second node from the first path to start searching 
		
//System.out.println(" k  = " + k3 + " j = " + j3 );
//System.out.println(PT3[k3][j3]+ "   " + PT3[k3][j3+1]+ "   "+ PT3[k3][j3+2]);

for (int z=0 ; z<SN_P2_nodes.length; z++)
{			
for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
{	
	PT311[k311][j311]   = SN_P2_nodes[z][j311];   // a matrix used while searching for the next connected node
	PT311[k311][j311+1] = SN_P2_nodes[z][j311+1]; // a matrix used while searching for the next connected node
	
//System.out.println(" k3  = " + k3 + " j3 = " + j3 +  " z = "+ z + " i =  " + i);
//	System.out.println("---------------------");		

	for (int r=1 ; r<SN_P2_nodes.length; r++)	
	if ((i+r+1) < SN_P2_nodes.length && r+1>k311 && k311<10000
		 &&	(PT311[k311][0]       == SN_P2_nodes[i+1][0]) 
		 && (PT311[k311][1]       == SN_P2_nodes[i+r+1][0]) 
		 && (SN_P2_nodes[i+1][1]  == SN_P2_nodes[i+r+1][1])
		 && (PT311[k311][1]       != SN_P2_nodes[i+1][1])
		 && (PT311[k311][1]       < SN_P2_nodes[i+1][1]) 
		 )
		{
			
			PT311[k311][j311+2] = SN_P2_nodes[i+1][1]; // this saves the selected connecting node
		 /*   
			  System.out.println(PT3[k3][j3]   + "   " + 
			                     PT3[k3][j3+1] + "   " + 
			                     PT3[k3][j3+2]);
		 */   
		    k311++; // increase the counter to the next path in search
							
			PT311[k311][j311]   = SN_P2_nodes[z][j311];   // initialize the first node in the next path
			PT311[k311][j311+1] = SN_P2_nodes[z][j311+1]; // initialize the second node in the next path
			
		//System.out.println(" k3  = " +k3 + " j3 = " + j3 +  " z = "+ z + " i =  " + i);
		//System.out.println("........................");		

		}else				
			if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j311+1] != 0))
			{
				continue; // go to the next path in the paths of type 3 to search for the next connecting node
			}else
				if (SN_P2_nodes[z][j311+1] != 0)
				{
					System.out.println("next z");
				}else
					break;

 } // end of i for loop
							
}// end of z for loop		

							//	System.out.println("------------------------- SN Paths Type 3 Nodes  ---------------------------------------");

int[][] SN_P311_nodes = new int[k311][3];

for (int i=0 ; i<k311 ; i++)
{
	//System.out.print(" SNP311-->"+ i + "  ");
	
	for (int jj=0 ; jj<3 ; jj++)
	{
		SN_P311_nodes[i][jj]=PT311[i][jj];             // save the new paths of type 3 nodes					
		//System.out.print(SN_P311_nodes[i][jj]+ ","); // print the path of type 3 nodes
	}
//System.out.println();
}
							
int[][] SN_P311_nodesr = new int[k311][3];

for (int i=0 ; i<k311 ; i++)
{
	//System.out.print(" SNP31-->"+ i + "  ");
	
	for (int jj=2 ; jj>-1 ; jj--)
	{
		SN_P311_nodesr[i][2-jj]=PT311[i][jj];             // save the new paths of type 31 nodes					
		//System.out.print(SN_P31_nodesr[i][2-jj]+ ","); // print the path of type 31 nodes
	}
//System.out.println();
}

int[] sorted_CPUsum_paths311 = new int[SN_P311_nodes.length]; // a matrix to save the path numbers of formulated type 3 nodes
int a311=0;

for (int a=0 ; a<SN_P311_nodes.length ; a++ )
{
	sorted_CPUsum_paths311[a]=a311;// initialize the matrix that contains the path numbers of the newly formulated paths of type 3 nodes
	a311++;
	//System.out.println(sorted_CPUsum_paths311[a]); // print the path numbers of newly formulated paths type 3 nodes.
}			
System.out.println( "Type-311 paths\t" +SN_P311_nodes.length);
							

//----------------------------------------------------- Paths of Type - 4-10 - Nodes -------------------------------------
//------------------------------------------------here we crate any type of VNR topology-------------------------------------------
//---------------------------------------------------------------------------------------------------------------------
			

//These matrixes save the topologies per each type of VNRs 
int nn=5000;

int[] Tleng = {6,10,15,21,28,36,45};
int[][] TsE4  = new int[nn][60];
int[][] TsE5  = new int[nn][60];
int[][] TsE6  = new int[nn][60];
int[][] TsE7  = new int[nn][60];
int[][] TsE8  = new int[nn][60];
int[][] TsE9  = new int[nn][60];
int[][] TsE10 = new int[nn][60];

for(int i=0; i<TsE4.length ; i++)
	for(int j=0; j<60 ; j++)
	{
		TsE4[i][j]=-1;
		TsE5[i][j]=-1;
		TsE6[i][j]=-1;
		TsE7[i][j]=-1;
		TsE8[i][j]=-1;
		TsE9[i][j]=-1;
		TsE10[i][j]=-1;
	}


int[] TSN4  = new int[12];
int[] TSN5  = new int[20];
int[] TSN6  = new int[30];
int[] TSN7  = new int[42];
int[] TSN8  = new int[56];
int[] TSN9  = new int[72];
int[] TSN10 = new int[90];

int[] TSN4s  = new int[4];
int[] TSN5s  = new int[5];
int[] TSN6s  = new int[6];
int[] TSN7s  = new int[7];
int[] TSN8s  = new int[8];
int[] TSN9s  = new int[9];
int[] TSN10s = new int[10];

int[][] TvE4  = new int[nn][60];
int[][] TvE5  = new int[nn][60];
int[][] TvE6  = new int[nn][60];
int[][] TvE7  = new int[nn][60];
int[][] TvE8  = new int[nn][60];
int[][] TvE9  = new int[nn][60];
int[][] TvE10 = new int[nn][60];

int[] TvN4  = new int[8];
int[] TvN5  = new int[10];
int[] TvN6  = new int[12];
int[] TvN7  = new int[14];
int[] TvN8  = new int[16];
int[] TvN9  = new int[18];
int[] TvN10 = new int[20];

//---------------------------------------------------------------------------------------------------------------------
/*
* From here is phase to get all types of topologies for any VNR topology having
* 4-10 nodes. It saves the topologies in the (T4-T10) matrixes at the end of loop (c) 
*/

int j4 =0;
int j5 =0;
int j6 =0;
int j7 =0;
int j8 =0;
int j9 =0;
int j10=0;

int jp4 =0;
int jp5 =0;
int jp6 =0;
int jp7 =0;
int jp8 =0;
int jp9 =0;
int jp10=0;

int[] TP4  = new int[SNP2];
int[] TP5  = new int[SNP2];
int[] TP6  = new int[SNP2];
int[] TP7  = new int[SNP2];
int[] TP8  = new int[SNP2];
int[] TP9  = new int[SNP2];
int[] TP10 = new int[SNP2];

//int c=4;

for (int c=4 ; c<=VNodes ; c++)
{

	/*
	 * The next for-loop, scan the SN for all topologies per 
	 * each VNR topology type.
	 */
	
for (int d=0 ; d<(SNP2) ; d++)
{


//------------------------------------------------------------------------------------------------------------
//---------------------------------------------- Create VN ---------------------------------------------------
//------------------------------------------------------------------------------------------------------------
int vN_Nodes	       = c; // the nodes per VNR are given by loop variable (c)
double valpha		   = 1.0;
double vbeta		   = 1.0;
double vProb_Thrreshod = 0.0;

double vMax_X_corr = 1; // Max X-corr for any VN node
double vMin_X_corr = 0; // Min X-corr for any VN node
double vMax_Y_corr = 1; // Max Y-corr for any VN node
double vMin_Y_corr = 0; // Min Y-corr for any VN node	

double vN_Max_x = vMax_X_corr - vMin_X_corr; // to be used in calculating the Max distance between the most distant vN nodes
double vN_Max_y = vMax_Y_corr - vMin_Y_corr; // to be used in calculating the Max distance between the most distant vN nodes

double Max_Distance_Between_vNodes     = 1;//(double) Math.sqrt(Math.pow(vN_Max_x, 2)+ Math.pow(vN_Max_y, 2));  // The max distance between the most distant vN nodes. This will be used in calculating the demanded distance for a VN Node.
double Min_Distance_Between_vNodes     = 0;// Max_Distance_Between_vNodes/10; // the minimum distance between vN nodes. This will be used in calculating the demanded distance for a VN Node
double[][] Distance_vN_Nodes 	       = new double[vN_Nodes][vN_Nodes];		
double[][] Propability_Links_vN_Nodes  = new double[vN_Nodes][vN_Nodes];		
int[][] vadjacencyMatrix	           = new int[vN_Nodes][vN_Nodes];

double[][] vN_Nodes_Locations   = new double[vN_Nodes][2];

int vedgevNum=0;		

for (int i=0 ; i<vN_Nodes ; i++)
{

vN_Nodes_Locations[i][0]	= (vMin_X_corr + (int)(Math.random() * ((vMax_X_corr  - vMin_X_corr)  + 1))); // generating a random X-corr for the vN node i.
vN_Nodes_Locations[i][1]	= (vMin_Y_corr + (int)(Math.random() * ((vMax_Y_corr  - vMin_Y_corr)  + 1))); // generating a random Y-corr for the vN node i.

}

for (int i=0 ; i<vN_Nodes ; i++)
for (int j=0 ; j<vN_Nodes ; j++)
Distance_vN_Nodes[i][j] = (Math.random() * Max_Distance_Between_vNodes);

for (int i=0 ; i<vN_Nodes ; i++)
{
for (int j=0 ; j<vN_Nodes ; j++)
{
Propability_Links_vN_Nodes[i][j]  =  (valpha * Math.exp(-Distance_vN_Nodes[i][j] / (vbeta * Max_Distance_Between_vNodes)));

if (Propability_Links_vN_Nodes[i][j] >= vProb_Thrreshod)
{
if (j>i)
{
vadjacencyMatrix[i][j] = 1;
	   vedgevNum++;
}
}

//System.out.print(Math.round(vadjacencyMatrix[i][j]) + "-");
}
//System.out.println();
}

//System.out.println("Number of virtual links\t" + vedgevNum/vN_Nodes);
//System.out.println("Run Number \t" + Runs);


//__________________________________________________  vN Paths   ________________________________________________________
//_________________________vN paths are classified according to the number of nodes per path_____________________________
//------------------------------------------------------------------------------------------------------------------------

//---------------- THis section is to generate the two nodes edges from the Adjacency Matrix  ------------

double[][] vCPUNP2nodes = new double[vadjacencyMatrix.length*10][2];
double[] vBWNP2nodes  = new double[vadjacencyMatrix.length*10];

double vCPU = 10;
double vBW  = 10;

int[][] vNP2nodes = new int[vadjacencyMatrix.length*10][2];
int vNP2=0;
int vNP2length = 0;

for (int i=0 ; i<(vadjacencyMatrix.length); i++)
	{
		for (int j=0 ; j<vadjacencyMatrix.length ; j++)
		{
		 if (vadjacencyMatrix[i][j] == 1)
		 {
		 	vNP2nodes[vNP2length][0]= i; // save the node in row i that has "1" value with column j.
		 	vNP2nodes[vNP2length][1]= j; // save the node in the column j that has "1" value with row i.
		 	vNP2length++;
		 	vNP2 = vNP2length; // this is a counter to get the length of the vN_P2_nodes matrix.
		 	//System.out.println(vNP2 + "\t" + vNP2length+ "\t" + i + "\t" + j);
		 
		 	vCPUNP2nodes[vNP2][0]= vCPU; // CPU value of the 1st node in path vNP2 
		 	vCPUNP2nodes[vNP2][1]= vCPU; // CPU value of the 2nd node in path vNP2 

		 	vBWNP2nodes [vNP2]= vBW; // BW value between 1st and 2nd nodes in path vNP2		 
		 
		 }//if (vadjacencyMatrix[i][j] == 1)
		}
	}
			
//System.out.println("------------------------- vN Paths Type 2 Nodes  ---------------------------------------");

int[][] vN_P2_nodes = new int [vNP2][2];

int vN=0;
int SN=0;
int[][] ePath = new int[vNP2][2];
int eP=0;
int nV=0;
int e=0;
int t=0;

int[] nodesV= new int[vNP2];

for(int i=1 ; i<vNP2 ; i++)			
for(int n=1 ; n<vN_Nodes ; n++)
	nodesV[i]=i;

//we make the vN_P2_nodes as per the new new length saved in vNP2

//System.out.println("-----------------");


/*
* The nest for loop finds all topologies in the SN similar to the VNR 
*/


/*
* the next section finds the SN links for virtual links that start by 0
Tleng[c-4] 

*/

for (int i=0 ; i<vN_Nodes ; i++)			
{
	for (int ii=eP+d ; ii<(SNP2-79) ; ii++)
	{			
		
		if (vNP2nodes[i][0]  == vNP2nodes[i+1][0]&&
			SNP2nodes[ii][0] == SNP2nodes[ii+1][0])
			{
				ePath[eP][0]=SNP2nodes[ii][0]; // saves the 1st selected SN node
				ePath[eP][1]=SNP2nodes[ii][1]; // saves the 2nd selected SN node				
				
				// System.out.println(ePath[eP][0]    +","+ePath[eP][1]    +"\t"+
				//                   vNP2nodes[eP][0]+","+vNP2nodes[eP][1]+"\t"+"eP=\t"+eP+"\t"+ii+"\t"+i);
				eP++;
				//if(vNP2nodes[i+1][0]!= i)
				break;
				 
			}else
				continue; 
		
	}
}

/*
* the next section fills next SN links in ePath[eP][0] matrix 
* with the SN node selected for the corresponding vNP2nodes[i][0]
*/

for (int i=eP ; i<vNP2 ; i++)
{
	if(vNP2nodes[i][0]  == vNP2nodes[t][1])
	{//System.out.println("t-------"+t);
	if(t<Tleng[c-4])
	{
	ePath[i][0]=ePath[t][1];
	}else
		break;
	}else
	{
		//t=t-1;
		t++;
		i--;
	}
}

/*
* the next section fills other side of ePath[eP][1] matrix 
* with the SN node selected for the corresponding vNP2nodes[i][1]
*/

for (int i=eP ; i<vNP2 ; i++)
{
	if(vNP2nodes[i][1]  == vNP2nodes[e][1])
	{//System.out.println("e-------"+e);
		ePath[i][1]=ePath[e][1];
	
	}else
	{
		//t=t-1;
		e++;
		i--;
	}
}

/*
for (int p=0 ; p<vNP2 ; p++)
	System.out.println(ePath[p][0]    +","+ePath[p][1]    +"\t"+
         vNP2nodes[p][0]+","+vNP2nodes[p][1] + "\t" + t);
*/



int t_break=0;

if(c==4)
if ((ePath[0][0]  ==ePath[1][0] && ePath[0][1]  ==ePath[1][1]) ||
		(ePath[1][0]  ==ePath[2][0] && ePath[1][1]  ==ePath[2][1]) ||
		(ePath[2][0]  ==ePath[3][0] && ePath[2][1]  ==ePath[3][1]) ||
		(ePath[3][0]  ==ePath[4][0] && ePath[3][1]  ==ePath[4][1]) ||
		(ePath[4][0]  ==ePath[5][0] && ePath[4][1]  ==ePath[5][1]) )
{	
	//System.out.println("t_break------="+t_break);
	
t_break=1;
}

if(c==5)
if ((ePath[0][0]  ==ePath[1][0] && ePath[0][1]  ==ePath[1][1]) ||
		(ePath[1][0]  ==ePath[2][0] && ePath[1][1]  ==ePath[2][1]) ||
		(ePath[2][0]  ==ePath[3][0] && ePath[2][1]  ==ePath[3][1]) ||
		(ePath[3][0]  ==ePath[4][0] && ePath[3][1]  ==ePath[4][1]) ||
		(ePath[4][0]  ==ePath[5][0] && ePath[4][1]  ==ePath[5][1]) ||
		(ePath[5][0]  ==ePath[6][0] && ePath[5][1]  ==ePath[6][1]) ||
		(ePath[6][0]  ==ePath[7][0] && ePath[6][1]  ==ePath[7][1]) ||
		(ePath[7][0]  ==ePath[8][0] && ePath[7][1]  ==ePath[8][1]) ||
		(ePath[8][0]  ==ePath[9][0] && ePath[8][1]  ==ePath[9][1]) )
{	
	//System.out.println("t_break------="+t_break);
	
t_break=1;
}

if(c==6)
if ((ePath[0][0]  ==ePath[1][0] && ePath[0][1]  ==ePath[1][1]) ||
		(ePath[1][0]  ==ePath[2][0] && ePath[1][1]  ==ePath[2][1]) ||
		(ePath[2][0]  ==ePath[3][0] && ePath[2][1]  ==ePath[3][1]) ||
		(ePath[3][0]  ==ePath[4][0] && ePath[3][1]  ==ePath[4][1]) ||
		(ePath[4][0]  ==ePath[5][0] && ePath[4][1]  ==ePath[5][1]) ||
		(ePath[5][0]  ==ePath[6][0] && ePath[5][1]  ==ePath[6][1]) ||
		(ePath[6][0]  ==ePath[7][0] && ePath[6][1]  ==ePath[7][1]) ||
		(ePath[7][0]  ==ePath[8][0] && ePath[7][1]  ==ePath[8][1]) ||
		(ePath[8][0]  ==ePath[9][0] && ePath[8][1]  ==ePath[9][1]) ||
		(ePath[9][0]  ==ePath[10][0] && ePath[9][1]  ==ePath[10][1]) ||
		(ePath[10][0]  ==ePath[11][0] && ePath[10][1]  ==ePath[11][1]) ||
		(ePath[11][0]  ==ePath[12][0] && ePath[11][1]  ==ePath[12][1]) ||
		(ePath[12][0]  ==ePath[13][0] && ePath[12][1]  ==ePath[13][1]) ||
		(ePath[13][0]  ==ePath[14][0] && ePath[13][1]  ==ePath[14][1]))
{	
	//System.out.println("t_break------="+t_break);
	
t_break=1;
}

if(c==7 || c==8 || c==9 || c==10 )
if ((ePath[0][0]  ==ePath[1][0] && ePath[0][1]  ==ePath[1][1]) ||
	(ePath[1][0]  ==ePath[2][0] && ePath[1][1]  ==ePath[2][1]) ||
	(ePath[2][0]  ==ePath[3][0] && ePath[2][1]  ==ePath[3][1]) ||
	(ePath[3][0]  ==ePath[4][0] && ePath[3][1]  ==ePath[4][1]) ||
	(ePath[4][0]  ==ePath[5][0] && ePath[4][1]  ==ePath[5][1]) ||
	(ePath[5][0]  ==ePath[6][0] && ePath[5][1]  ==ePath[6][1]) ||
	(ePath[6][0]  ==ePath[7][0] && ePath[6][1]  ==ePath[7][1]) ||
	(ePath[7][0]  ==ePath[8][0] && ePath[7][1]  ==ePath[8][1]) ||
	(ePath[8][0]  ==ePath[9][0] && ePath[8][1]  ==ePath[9][1]) ||
	(ePath[9][0]  ==ePath[10][0] && ePath[9][1]  ==ePath[10][1]) ||
	(ePath[10][0]  ==ePath[11][0] && ePath[10][1]  ==ePath[11][1]) ||
	(ePath[11][0]  ==ePath[12][0] && ePath[11][1]  ==ePath[12][1]) ||
	(ePath[12][0]  ==ePath[13][0] && ePath[12][1]  ==ePath[13][1]) ||
	(ePath[13][0]  ==ePath[14][0] && ePath[13][1]  ==ePath[14][1]) ||
	(ePath[14][0]  ==ePath[15][0] && ePath[14][1]  ==ePath[15][1]) ||
	(ePath[15][0]  ==ePath[16][0] && ePath[15][1]  ==ePath[16][1]) ||
	(ePath[16][0]  ==ePath[17][0] && ePath[16][1]  ==ePath[17][1]) ||
	(ePath[17][0]  ==ePath[18][0] && ePath[17][1]  ==ePath[18][1]) ||
	(ePath[18][0]  ==ePath[19][0] && ePath[18][1]  ==ePath[19][1]) ||
	(ePath[19][0]  ==ePath[20][0] && ePath[19][1]  ==ePath[20][1]) )
{	
	//System.out.println("t_break------="+t_break);
	
t_break=1;
}


//System.out.println("t_break------="+t_break);

/*
* This if statement checks if the topology has no similar edges
*/

if(t_break==0)
{

	/*
	 * This part checks if each pair of pairs in ePath matrix are connected
	 */
	
	int bedges=0;

	for (int i=0 ; i<vNP2 ; i++)
	{
	if (adjacencyMatrix[ePath[i][0]][ePath[i][1]] == 1 && bedges<=be)
	{	
		
		ePath[i][0]=ePath[i][0];
		ePath[i][1]=ePath[i][1];
		bedges++;
	
	
}else
{
	ePath[i][0]=-1;
	ePath[i][1]=-1;
	
}
}

/*
for (int p=0 ; p<vNP2 ; p++)
	System.out.println(ePath[p][0]    +","+ePath[p][1]    +"\t"+
         vNP2nodes[p][0]+","+vNP2nodes[p][1] + "\t" + t);

*/


/*
* In this part, save all topologies per each type of VNR.
*/

if (c==4)
{	
	
	
	/*
	 * this part orders the SN nodes for each topology, and saves them in the grands 
	 * matrix TsE4, which will be the main matrix including all topologies of this 
	 * type.
	 */
	
	int pi4=0;
	for(int nS4=0 ; nS4<TSN4.length ; nS4=nS4+2)
	{	// this part lists all used SN node in the topology pi4.
		TSN4[nS4]   = ePath[pi4][0];
		TSN4[nS4+1] = ePath[pi4][1];	
		
		//System.out.print(TSN4[nS4]+"-"+TSN4[nS4+1]+"-"); 
		pi4++;		
	}

	
	int temp4;
	// this part sorts the used SN nodes small to big and saves them in TSN4 again.
	for (int i1 = 0; i1 < TSN4.length - 1; i1++)  
		{ 
		for ( int j1 = i1+1; j1 < TSN4.length; j1++)
			{
			if(  TSN4[i1] >  TSN4[j1]) // && (SNP10_cpu_utilization_sum[i1] < SNP10_cpu_utilization_sum [j1]]))    //sorting into descending order
				{
				temp4      = TSN4[i1];   // swapping CPU values
				TSN4[i1]  = TSN4[j1]; 
				TSN4[j1]  = temp4;
			
				} // end of -if- for sorting           
			} // end of j1 for-loop 
		} // end of i1 for-loop

/*		
	for (int i1 = 0; i1 < TSN4.length; i1++ )  
		System.out.print(TSN4[i1]+"-"); 

	System.out.println(); 
*/

int SN4=0;

for(int i=0 ; i<TSN4.length ; i++)
{ // This part selects the first sorted node for topology 4 nodes.
	if(TSN4[i]>=0)
		{ 
		// System.out.println(TSN4s[0]+"------------"+SN4);
		TSN4s[0]=TSN4[i];
		SN4=i;
		// System.out.println(TSN4s[0]+"------------"+SN4);
		break;
		}else
			continue;	
}

for(int s4=1 ; s4<4 ; s4++)
{ // this part selects the remaining sorted nodes for topology 4.
	for(int i=SN4 ; i<TSN4.length ; i++)
	{
		if(TSN4[i]>TSN4s[s4-1])
		{
			TSN4s[s4]=TSN4[i];
			// System.out.println(TSN4s[s4]+"--"+s4+"\t"+TSN4[i]+"--"+i);
			
			break;
		}else
			continue;
	}
	
}
/*
for (int u = 0; u < TSN4s.length; u++ )  
	System.out.print(TSN4s[u]+"-"); 

System.out.println();
*/	
int rej=0;

for (int u = 0; u < TSN4.length; u++ )
{
if(TSN4s[TSN4s.length-1]>=TSN4[u])
{
	//System.out.println("rej==-------"+rej);
	continue;							
}else
{
	for (int uu = 0; uu < TSN4s.length; uu++ )  
		//System.out.print(TSN4s[uu]+"-");
	
	rej=1;
	//System.out.println("------\t"+TSN4s[TSN4s.length-1]+"rej==-------"+rej);
	break;
}}

	if( rej==0 && 
	    TSN4s[0]!=TSN4s[1] &&
		TSN4s[1]!=TSN4s[2] &&
		TSN4s[2]!=TSN4s[3] &&
		(TSN4s[3]-TSN4s[2])<=vNP2)
	{
		
	for(int s4=0 ; s4<4 ; s4++)
		{
			TsE4[j4][s4] = TSN4s[s4]; // saving the nodes of the topology
			//System.out.print(TsE4[j4][s4]+"-");
		}
	
	TP4[jp4]=j4; // This counter saves the topology number, pointing to the row of the topology nodes (j4), and topology edges (j4+1 and j4+2).
	jp4++;
	
	//System.out.println("d----------"+d+"----------"+TP4[d]);
	
	for(int i=0 ; i<vNP2 ; i++)
			{ 
				TsE4[j4+1][i] = ePath[i][0]; // saving the edges of the topology.
				TsE4[j4+2][i] = ePath[i][1];
				
			//System.out.print(TsE4[j4+1][i]+"-"+TsE4[j4+2][i]+"-");
			}j4=j4+3;
	
		//System.out.println();	
	
			//System.out.println("d----"+d+"----"+TP4[d]);
			
	
	}else 
		continue;

}else
	if (c==5)
	{
		
			
		/*
		 * this part orders the SN nodes for each topology, and saves them in the grands 
		 * matrix TsE5, which will be the main matrix including all topologies of this 
		 * type.
		 */
		
		int pi5=0;
		for(int nS5=0 ; nS5<TSN5.length ; nS5=nS5+2)
		{	// this part lists all used SN node in the topology pi5.
			TSN5[nS5]   = ePath[pi5][0];
			TSN5[nS5+1] = ePath[pi5][1];	
			
			//System.out.print(TSN5[nS5]+"-"+TSN5[nS5+1]+"-"); 
			pi5++;			
			
		}

		
		int temp5;
		// this part sorts the used SN nodes small to big and saves them in TSN5 again.
		for (int i1 = 0; i1 < TSN5.length - 1; i1++)  
			{ 
			for ( int j1 = i1+1; j1 < TSN5.length; j1++)
				{
				if(  TSN5[i1] >  TSN5[j1]) // && (SNP10_cpu_utilization_sum[i1] < SNP10_cpu_utilization_sum [j1]]))    //sorting into descending order
					{
					temp5      = TSN5[i1];   // swapping CPU values
					TSN5[i1]  = TSN5[j1]; 
					TSN5[j1]  = temp5;
				
					} // end of -if- for sorting           
				} // end of j1 for-loop 
			} // end of i1 for-loop

	/*		
		for (int i1 = 0; i1 < TSN5.length; i1++ )  
			System.out.print(TSN5[i1]+"-"); 

		System.out.println(); 
	*/

	int SN5=0;

	for(int i=0 ; i<TSN5.length ; i++)
	{ // This part selects the first sorted node for topology 5 nodes.
		if(TSN5[i]>=0)
			{ 
			// System.out.println(TSN5s[0]+"------------"+SN5);
			TSN5s[0]=TSN5[i];
			SN5=i;
			// System.out.println(TSN5s[0]+"------------"+SN5);
			break;
			}else
				continue;	
	}

	for(int s5=1 ; s5<5 ; s5++)
	{ // this part selects the remaining sorted nodes for topology 5.
		for(int i=SN5 ; i<TSN5.length ; i++)
		{
			if(TSN5[i]>TSN5s[s5-1])
			{
				TSN5s[s5]=TSN5[i];
				// System.out.println(TSN5s[s5]+"--"+s5+"\t"+TSN5[i]+"--"+i);
				
				break;
			}else
				continue;
		}
		
	}
	/*
	for (int u = 0; u < TSN5s.length; u++ )  
		System.out.print(TSN5s[u]+"-"); 

	System.out.println();
	*/	
	int rej=0;
	
	for (int u = 0; u < TSN5.length; u++ )
	{
	if(TSN5s[TSN5s.length-1]>=TSN5[u])
	{
		//System.out.println("rej==-------"+rej);
		continue;							
	}else
	{
		for (int uu = 0; uu < TSN5s.length; uu++ )  
			//System.out.print(TSN5s[uu]+"-");
		
		rej=1;
		//System.out.println("------\t"+TSN5s[TSN5s.length-1]+"rej==-------"+rej);
		break;
	}}
	
		if( rej==0 && 
			TSN5s[0]!=TSN5s[1] &&
			TSN5s[1]!=TSN5s[2] &&
			TSN5s[2]!=TSN5s[3] &&
			TSN5s[3]!=TSN5s[4] &&
		   (TSN5s[4]-TSN5s[3])<=vNP2)
		{
			
		for(int s5=0 ; s5<5 ; s5++)
			{
				TsE5[j5][s5] = TSN5s[s5]; // saving the nodes of the topology
				//System.out.print(TsE5[j5][s5]+"-");
			}
		
		TP5[jp5]=j5; // This counter saves the topology number, pointing to the row of the topology nodes (j5), and topology edges (j5+1 and j5+2).
		jp5++;
		//System.out.println("d----------"+d+"----------"+TP5[d]);
		
		for(int i=0 ; i<vNP2 ; i++)
				{ 
					TsE5[j5+1][i] = ePath[i][0]; // saving the edges of the topology.
					TsE5[j5+2][i] = ePath[i][1];
					
				//System.out.print(TsE5[j5+1][i]+"-"+TsE5[j5+2][i]+"-");
				}j5=j5+3;
		
			//System.out.println();	
		
				//System.out.println("d----"+d+"----"+TP5[d]);
				
		
		}else 
			continue;

	}else
		if (c==6)
		{	/*
			 * this part orders the SN nodes for each topology, and saves them in the grands 
			 * matrix TsE6, which will be the main matrix including all topologies of this 
			 * type.
			 */
			
			int pi6=0;
			for(int nS6=0 ; nS6<TSN6.length ; nS6=nS6+2)
			{	
				TSN6[nS6]   = ePath[pi6][0];
				TSN6[nS6+1] = ePath[pi6][1];	
					
				//System.out.print(TSN6[nS6]+"-"+TSN6[nS6+1]+"-"); 
				pi6++;			
			}

			int temp6;
			
			for (int i1 = 0; i1 < TSN6.length - 1; i1++)  
				{
				for ( int j1 = i1+1; j1 < TSN6.length; j1++)
					{
					if(  TSN6[i1] >  TSN6[j1]) // && (SNP10_cpu_utilization_sum[i1] < SNP10_cpu_utilization_sum [j1]]))    //sorting into descending order
						{
						temp6      = TSN6[i1];   // swapping CPU values
						TSN6[i1]  = TSN6[j1]; 
						TSN6[j1]  = temp6;
					
						} // end of -if- for sorting           
					} // end of j1 for-loop 
				} // end of i1 for-loop

			/*	
			for (int i1 = 0; i1 < TSN6.length; i1++ )  
				System.out.print(TSN6[i1]+"-"); 

			System.out.println(); 
		*/

		int SN6=0;

		for(int i=0 ; i<TSN6.length ; i++)
		{
			if(TSN6[i]>=0)
				{
				// System.out.println(TSN6s[0]+"------------"+SN6);
				TSN6s[0]=TSN6[i];
				SN6=i;
				// System.out.println(TSN6s[0]+"------------"+SN6);
				break;
				}else
					continue;	
		}

		for(int s6=1 ; s6<6 ; s6++)
		{
			for(int i=SN6 ; i<TSN6.length ; i++)
			{
				if(TSN6[i]>TSN6s[s6-1])
				{
					TSN6s[s6]=TSN6[i];
					// System.out.println(TSN6s[s6]+"--"+s6+"\t"+TSN6[i]+"--"+i);
					
					break;
				}else
					continue;
			}
			
		}
		/*
		for (int u = 0; u < TSN6s.length; u++ )  
			System.out.print(TSN6s[u]+"-"); 

		System.out.println();
		*/	
		int rej=0;
		
		for (int u = 0; u < TSN6.length; u++ )
		{
		if(TSN6s[TSN6s.length-1]>=TSN6[u])
		{
			//System.out.println("rej==-------"+rej);
			continue;							
		}else
		{
			for (int uu = 0; uu < TSN6s.length; uu++ )  
				//System.out.print(TSN6s[uu]+"-");
			
			rej=1;
			//System.out.println("------\t"+TSN6s[TSN6s.length-1]+"rej==-------"+rej);
			break;
		}}
		
			if( rej==0 && 
  				TSN6s[0]!=TSN6s[1] &&
					TSN6s[1]!=TSN6s[2] &&
					TSN6s[2]!=TSN6s[3] &&
					TSN6s[3]!=TSN6s[4] &&
					TSN6s[4]!=TSN6s[5])
				{

		
		for(int s6=0 ; s6<6 ; s6++)
			{
				TsE6[j6][s6] = TSN6s[s6]; // saving the nodes of the topology
				//	System.out.print(TsE6[j6][s6]+"-");
			}
		TP6[jp6]=j6;
		jp6++;
		
		//	System.out.println("d----------"+d+"----------"+TP6[d]);

			for(int i=0 ; i<vNP2 ; i++)
				{ 		
				TsE6[j6+1][i] = ePath[i][0]; // saving the edges of the topology.
				TsE6[j6+2][i] = ePath[i][1];
				//	System.out.print(TsE6[j6+1][i]+"-"+TsE6[j6+2][i]+"-");
				}j6=j6+3;	
				//		System.out.println();

		}else 
			continue;

				
		}else
			if (c==7)
			{


				/*
				 * this part orders the SN nodes for each topology, and saves them in the grands 
				 * matrix TsE7, which will be the main matrix including all topologies of this 
				 * type.
				 */
				
				int pi7=0;
				for(int nS7=0 ; nS7<TSN7.length ; nS7=nS7+2)
				{	
					TSN7[nS7]   = ePath[pi7][0];
					TSN7[nS7+1] = ePath[pi7][1];	
						
					//System.out.print(TSN7[nS7]+"-"+TSN7[nS7+1]+"-"); 
					pi7++;			
				}

				int temp7;
				
				for (int i1 = 0; i1 < TSN7.length - 1; i1++)  
					{
					for ( int j1 = i1+1; j1 < TSN7.length; j1++)
						{
						if(  TSN7[i1] >  TSN7[j1]) // && (SNP10_cpu_utilization_sum[i1] < SNP10_cpu_utilization_sum [j1]]))    //sorting into descending order
							{
							temp7      = TSN7[i1];   // swapping CPU values
							TSN7[i1]  = TSN7[j1]; 
							TSN7[j1]  = temp7;
						
							} // end of -if- for sorting           
						} // end of j1 for-loop 
					} // end of i1 for-loop

				/*	
				for (int i1 = 0; i1 < TSN7.length; i1++ )  
					System.out.print(TSN7[i1]+"-"); 

				System.out.println(); 
			*/

			int SN7=0;

			for(int i=0 ; i<TSN7.length ; i++)
			{
				if(TSN7[i]>=0)
					{
					// System.out.println(TSN7s[0]+"------------"+SN7);
					TSN7s[0]=TSN7[i];
					SN7=i;
					// System.out.println(TSN7s[0]+"------------"+SN7);
					break;
					}else
						continue;	
			}

			for(int s7=1 ; s7<7 ; s7++)
			{
				for(int i=SN7 ; i<TSN7.length ; i++)
				{
					if(TSN7[i]>TSN7s[s7-1])
					{
						TSN7s[s7]=TSN7[i];
						// System.out.println(TSN7s[s7]+"--"+s7+"\t"+TSN7[i]+"--"+i);
						
						break;
					}else
						continue;
				}
				
			}
			/*
			for (int u = 0; u < TSN7s.length; u++ )  
				System.out.print(TSN7s[u]+"-"); 

			System.out.println();
			*/	
			int rej=0;
			
			for (int u = 0; u < TSN7.length; u++ )
			{
			if(TSN7s[TSN7s.length-1]>=TSN7[u])
			{
				//System.out.println("rej==-------"+rej);
				continue;							
			}else
			{
				for (int uu = 0; uu < TSN7s.length; uu++ )  
					//System.out.print(TSN7s[uu]+"-");
				
				rej=1;
				//System.out.println("------\t"+TSN7s[TSN7s.length-1]+"rej==-------"+rej);
				break;
			}}
			
				if( rej==0 && 
 				TSN7s[0]!=TSN7s[1] &&
					TSN7s[1]!=TSN7s[2] &&
					TSN7s[2]!=TSN7s[3] &&
					TSN7s[3]!=TSN7s[4] &&
					TSN7s[4]!=TSN7s[5] &&
					TSN7s[5]!=TSN7s[6])
				{

			
			for(int s7=0 ; s7<7 ; s7++)
				{
					TsE7[j7][s7] = TSN7s[s7]; // saving the nodes of the topology
					//	System.out.print(TsE7[j7][s7]+"-");
				}
			TP7[jp7]=j7;
			jp7++;
			
			//System.out.println("d----------"+d+"----------"+TP7[d]);

				for(int i=0 ; i<vNP2 ; i++)
					{ 		
					TsE7[j7+1][i] = ePath[i][0]; // saving the edges of the topology.
					TsE7[j7+2][i] = ePath[i][1];
					//	System.out.print(TsE7[j7+1][i]+"-"+TsE7[j7+2][i]+"-");
					}j7=j7+3;	
					//		System.out.println();			

			}else 
				continue;


			}else
				if (c==8)
				{

					// TODO Auto-generated method stub



					/*
					 * this part orders the SN nodes for each topology, and saves them in the grands 
					 * matrix TsE8, which will be the main matrix including all topologies of this 
					 * type.
					 */
					
					int pi8=0;
					for(int nS8=0 ; nS8<TSN8.length ; nS8=nS8+2)
					{	
						TSN8[nS8]   = ePath[pi8][0];
						TSN8[nS8+1] = ePath[pi8][1];	
							
						//System.out.print(TSN8[nS8]+"-"+TSN8[nS8+1]+"-"); 
						pi8++;			
					}

					int temp8;
					
					for (int i1 = 0; i1 < TSN8.length - 1; i1++)  
						{
						for ( int j1 = i1+1; j1 < TSN8.length; j1++)
							{
							if(  TSN8[i1] >  TSN8[j1]) // && (SNP10_cpu_utilization_sum[i1] < SNP10_cpu_utilization_sum [j1]]))    //sorting into descending order
								{
								temp8      = TSN8[i1];   // swapping CPU values
								TSN8[i1]  = TSN8[j1]; 
								TSN8[j1]  = temp8;
							
								} // end of -if- for sorting           
							} // end of j1 for-loop 
						} // end of i1 for-loop

					/*	
					for (int i1 = 0; i1 < TSN8.length; i1++ )  
						System.out.print(TSN8[i1]+"-"); 

					System.out.println(); 
				*/

				int SN8=0;

				for(int i=0 ; i<TSN8.length ; i++)
				{
					if(TSN8[i]>=0)
						{
						// System.out.println(TSN8s[0]+"------------"+SN8);
						TSN8s[0]=TSN8[i];
						SN8=i;
						// System.out.println(TSN8s[0]+"------------"+SN8);
						break;
						}else
							continue;	
				}

				for(int s8=1 ; s8<8 ; s8++)
				{
					for(int i=SN8 ; i<TSN8.length ; i++)
					{
						if(TSN8[i]>TSN8s[s8-1])
						{
							TSN8s[s8]=TSN8[i];
							// System.out.println(TSN8s[s8]+"--"+s8+"\t"+TSN8[i]+"--"+i);
							
							break;
						}else
							continue;
					}
					
				}
				/*
				for (int u = 0; u < TSN8s.length; u++ )  
					System.out.print(TSN8s[u]+"-"); 

				System.out.println();
				*/	
				int rej=0;
				
				for (int u = 0; u < TSN8.length; u++ )
				{
				if(TSN8s[TSN8s.length-1]>=TSN8[u])
				{
					//System.out.println("rej==-------"+rej);
					continue;							
				}else
				{
					for (int uu = 0; uu < TSN8s.length; uu++ )  
						//	System.out.print(TSN8s[uu]+"-");
					
					rej=1;
					//System.out.println("------\t"+TSN8s[TSN8s.length-1]+"rej==-------"+rej);
					break;
				}}
				
					if( rej==0 &&
						TSN8s[0]!=TSN8s[1] &&
						TSN8s[1]!=TSN8s[2] &&
						TSN8s[2]!=TSN8s[3] &&
						TSN8s[3]!=TSN8s[4] &&
						TSN8s[4]!=TSN8s[5] &&
						TSN8s[5]!=TSN8s[6] &&
						TSN8s[6]!=TSN8s[7])
					{

				
				for(int s8=0 ; s8<8 ; s8++)
					{
						TsE8[j8][s8] = TSN8s[s8]; // saving the nodes of the topology
						//	System.out.print(TsE8[j8][s8]+"-");
					}
				TP8[jp8]=j8;
				jp8++;
				
				//	System.out.println("d----------"+d+"----------"+TP8[d]);

					for(int i=0 ; i<vNP2 ; i++)
						{ 		
						TsE8[j8+1][i] = ePath[i][0]; // saving the edges of the topology.
						TsE8[j8+2][i] = ePath[i][1];
						//	System.out.print(TsE8[j8+1][i]+"-"+TsE8[j8+2][i]+"-");
						}j8=j8+3;	
						//	System.out.println();

				}else 
					continue;

						
				}else
					if (c==9)
					{

						// TODO Auto-generated method stub



						/*
						 * this part orders the SN nodes for each topology, and saves them in the grands 
						 * matrix TsE9, which will be the main matrix including all topologies of this 
						 * type.
						 */
						
						int pi9=0;
						for(int nS9=0 ; nS9<TSN9.length ; nS9=nS9+2)
						{	
							TSN9[nS9]   = ePath[pi9][0];
							TSN9[nS9+1] = ePath[pi9][1];	
								
							//System.out.print(TSN9[nS9]+"-"+TSN9[nS9+1]+"-"); 
							pi9++;			
						}

						int temp9;
						
						for (int i1 = 0; i1 < TSN9.length - 1; i1++)  
							{
							for ( int j1 = i1+1; j1 < TSN9.length; j1++)
								{
								if(  TSN9[i1] >  TSN9[j1]) // && (SNP10_cpu_utilization_sum[i1] < SNP10_cpu_utilization_sum [j1]]))    //sorting into descending order
									{
									temp9      = TSN9[i1];   // swapping CPU values
									TSN9[i1]  = TSN9[j1]; 
									TSN9[j1]  = temp9;
								
									} // end of -if- for sorting           
								} // end of j1 for-loop 
							} // end of i1 for-loop

						/*	
						for (int i1 = 0; i1 < TSN9.length; i1++ )  
							System.out.print(TSN9[i1]+"-"); 

						System.out.println(); 
					*/

					int SN9=0;

					for(int i=0 ; i<TSN9.length ; i++)
					{
						if(TSN9[i]>=0)
							{
							// System.out.println(TSN9s[0]+"------------"+SN9);
							TSN9s[0]=TSN9[i];
							SN9=i;
							// System.out.println(TSN9s[0]+"------------"+SN9);
							break;
							}else
								continue;	
					}

					for(int s9=1 ; s9<9 ; s9++)
					{
						for(int i=SN9 ; i<TSN9.length ; i++)
						{
							if(TSN9[i]>TSN9s[s9-1])
							{
								TSN9s[s9]=TSN9[i];
								// System.out.println(TSN9s[s9]+"--"+s9+"\t"+TSN9[i]+"--"+i);
								
								break;
							}else
								continue;
						}
						
					}
					/*
					for (int u = 0; u < TSN9s.length; u++ )  
						System.out.print(TSN9s[u]+"-"); 

					System.out.println();
					*/	
					int rej=0;
					
					for (int u = 0; u < TSN9.length; u++ )
					{
					if(TSN9s[TSN9s.length-1]>=TSN9[u])
					{
						//System.out.println("rej==-------"+rej);
						continue;							
					}else
					{
						for (int uu = 0; uu < TSN9s.length; uu++ )  
							//System.out.print(TSN9s[uu]+"-");
						
						rej=1;
						//System.out.println("------\t"+TSN9s[TSN9s.length-1]+"rej==-------"+rej);
						break;
					}}
					
						if( rej==0 &&
							TSN9s[0]!=TSN9s[1] &&
							TSN9s[1]!=TSN9s[2] &&
							TSN9s[2]!=TSN9s[3] &&
							TSN9s[3]!=TSN9s[4] &&
							TSN9s[4]!=TSN9s[5] &&
							TSN9s[5]!=TSN9s[6] &&
							TSN9s[6]!=TSN9s[7] &&
							TSN9s[7]!=TSN9s[8])
						{

					
					for(int s9=0 ; s9<9 ; s9++)
						{
							TsE9[j9][s9] = TSN9s[s9]; // saving the nodes of the topology
							//	System.out.print(TsE9[j9][s9]+"-");
						}
					TP9[jp9]=j9;
					jp9++;
					
					//	System.out.println("d----------"+d+"----------"+TP9[d]);

						for(int i=0 ; i<vNP2 ; i++)
							{ 		
							TsE9[j9+1][i] = ePath[i][0]; // saving the edges of the topology.
							TsE9[j9+2][i] = ePath[i][1];
							//	System.out.print(TsE9[j9+1][i]+"-"+TsE9[j9+2][i]+"-");
							}j9=j9+3;	
							//	System.out.println();
						}else 
							continue;
	
							
					
					}else
						if (c==10)
						{
							
							
							/*
							 * this part orders the SN nodes for each topology, and saves them in the grands 
							 * matrix TsE10, which will be the main matrix including all topologies of this 
							 * type.
							 */
							
							int pi10=0;
							for(int nS10=0 ; nS10<TSN10.length ; nS10=nS10+2)
							{	// this part lists all used SN node in the topology pi10.
								TSN10[nS10]   = ePath[pi10][0];
								TSN10[nS10+1] = ePath[pi10][1];	
								
								//System.out.print(TSN10[nS10]+"-"+TSN10[nS10+1]+"-"); 
								pi10++;			
								
								
							}

							
							int temp10;
							// this part sorts the used SN nodes small to big and saves them in TSN10 again.
							for (int i1 = 0; i1 < TSN10.length - 1; i1++)  
								{ 
								for ( int j1 = i1+1; j1 < TSN10.length; j1++)
									{
									if(  TSN10[i1] >  TSN10[j1]) // && (SNP10_cpu_utilization_sum[i1] < SNP10_cpu_utilization_sum [j1]]))    //sorting into descending order
										{
										temp10      = TSN10[i1];   // swapping CPU values
										TSN10[i1]  = TSN10[j1]; 
										TSN10[j1]  = temp10;
									
										} // end of -if- for sorting           
									} // end of j1 for-loop 
								} // end of i1 for-loop

						/*		
							for (int i1 = 0; i1 < TSN10.length; i1++ )  
								System.out.print(TSN10[i1]+"-"); 

							System.out.println(); 
						*/

						int SN10=0;

						for(int i=0 ; i<TSN10.length ; i++)
						{ // This part selects the first sorted node for topology 10 nodes.
							if(TSN10[i]>=0)
								{ 
								// System.out.println(TSN10s[0]+"------------"+SN10);
								TSN10s[0]=TSN10[i];
								SN10=i;
								// System.out.println(TSN10s[0]+"------------"+SN10);
								break;
								}else
									continue;	
						}

						for(int s10=1 ; s10<10 ; s10++)
						{ // this part selects the remaining sorted nodes for topology 10.
							for(int i=SN10 ; i<TSN10.length ; i++)
							{
								if(TSN10[i]>TSN10s[s10-1])
								{
									TSN10s[s10]=TSN10[i];
									// System.out.println(TSN10s[s10]+"--"+s10+"\t"+TSN10[i]+"--"+i);
									
									break;
								}else
									continue;
							}
							
						}
						/*
						for (int u = 0; u < TSN10s.length; u++ )  
							System.out.print(TSN10s[u]+"-"); 

						System.out.println();
						*/
					
						int rej=0;
						
						for (int u = 0; u < TSN10.length; u++ )
						{
						if(TSN10s[TSN10s.length-1]>=TSN10[u])
						{
							//System.out.println("rej==-------"+rej);
							continue;							
						}else
						{
							for (int uu = 0; uu < TSN10s.length; uu++ )  
								//System.out.print(TSN10s[uu]+"-");
							
							rej=1;
							//System.out.println("------\t"+TSN10s[TSN10s.length-1]+"rej==-------"+rej);
							break;
						}}
						
							if( rej==0 &&
								TSN10s[0]!=TSN10s[1] &&
								TSN10s[1]!=TSN10s[2] &&
								TSN10s[2]!=TSN10s[3] &&
								TSN10s[3]!=TSN10s[4] &&
								TSN10s[4]!=TSN10s[5] &&
								TSN10s[5]!=TSN10s[6] &&
								TSN10s[6]!=TSN10s[7] &&
								TSN10s[7]!=TSN10s[8] &&
								TSN10s[8]!=TSN10s[9])
							{
								
							for(int s10=0 ; s10<10 ; s10++)
								{
									TsE10[j10][s10] = TSN10s[s10]; // saving the nodes of the topology
									//System.out.print(TsE10[j10][s10]+"-");
								}
							
							TP10[jp10]=j10; // This counter saves the topology number, pointing to the row of the topology nodes (j10), and topology edges (j10+1 and j10+2).
							jp10++;
							
							//System.out.println("d----------"+d+"----------"+TP10[d]);
							
							for(int i=0 ; i<vNP2 ; i++)
									{ 
										TsE10[j10+1][i] = ePath[i][0]; // saving the edges of the topology.
										TsE10[j10+2][i] = ePath[i][1];
										
										//System.out.print(TsE10[j10+1][i]+"-"+TsE10[j10+2][i]+"-");
									}j10=j10+3;
							
									//System.out.println();	
							
									//System.out.println("d----"+d+"----"+TP10[d]);
									
							
							}else 
								continue;

						}
							
							
						

}else
	continue;


}// End of for (int d=0 ; d<(SNP2-79) ; d++)

}// End of for (int c=4 ; c<=VNodes ; c++)



/*
* The following matrixes are the random VNRs topologies as created from the SN.  
*/

TvE4 =TsE4;   // VNRs topologies of  4 nodes.
TvE5 =TsE5;   // VNRs topologies of  5 nodes.
TvE6 =TsE6;   // VNRs topologies of  6 nodes.
TvE7 =TsE7;   // VNRs topologies of  7 nodes.
TvE8 =TsE8;   // VNRs topologies of  8 nodes.
TvE9 =TsE9;   // VNRs topologies of  9 nodes.
TvE10=TsE10; // VNRs topologies of 10 nodes.			

int[] Tjp4  = new int[jp4]; // saves the topology number to be used for embedding. 
int[] Tjp5  = new int[jp5]; // saves the topology number to be used for embedding. 
int[] Tjp6  = new int[jp6]; // saves the topology number to be used for embedding. 
int[] Tjp7  = new int[jp7]; // saves the topology number to be used for embedding. 
int[] Tjp8  = new int[jp8]; // saves the topology number to be used for embedding. 
int[] Tjp9  = new int[jp9]; // saves the topology number to be used for embedding. 
int[] Tjp10 = new int[jp10];// saves the topology number to be used for embedding. 

for (int i=0 ; i<jp4 ; i++)
{
	Tjp4[i]=TP4[i]; // saves the topology number.
	System.out.println(Tjp4[i]+"\t"+jp4);
}

for (int i=0 ; i<jp5 ; i++)
{
	Tjp5[i]=TP5[i]; // saves the topology number.
	System.out.println(Tjp5[i]+"\t"+jp5);
}
	
for (int i=0 ; i<jp6 ; i++)
{
	Tjp6[i]=TP6[i]; // saves the topology number.
	System.out.println(Tjp6[i]+"\t"+jp6);
}

for (int i=0 ; i<jp7 ; i++)
{
	Tjp7[i]=TP7[i]; // saves the topology number.
	System.out.println(Tjp7[i]+"\t"+jp7);
}

for (int i=0 ; i<jp8 ; i++)
{
	Tjp8[i]=TP8[i]; // saves the topology number.
	System.out.println(Tjp8[i]+"\t"+jp8);
}

for (int i=0 ; i<jp9 ; i++)
{
	Tjp9[i]=TP9[i]; // saves the topology number.
	System.out.println(Tjp9[i]+"\t"+jp9);
}

for (int i=0 ; i<jp10 ; i++)
{
	Tjp10[i]=TP10[i]; // saves the topology number.
	System.out.println(Tjp10[i]+"\t"+jp10);
}

/*
for (int p=0 ; p<j4 ; p++)
{
	for(int j=0 ; j<6 ; j=j+2)
	System.out.print(TsE4[p][j]+","+TsE4[p][j+1]+"\t");
	
	System.out.println(jp4);
}
for (int p=0 ; p<j5 ; p++)
{
	for(int j=0 ; j<10 ; j=j+2)
	System.out.print(TsE5[p][j]+","+TsE5[p][j+1]+"\t");
	
	System.out.println(j5);
}
for (int p=0 ; p<j6 ; p++)
{
	for(int j=0 ; j<15 ; j=j+2)
	System.out.print(Ts6[p][j]+","+TsE6[p][j+1]+"\t");
	
	System.out.println(j6);
}
for (int p=0 ; p<j7 ; p++)
{
	for(int j=0 ; j<21 ; j=j+2)
	System.out.print(TsE7[p][j]+","+TsE7[p][j+1]+"\t");
	
	System.out.println(j7);
}
for (int p=0 ; p<j8 ; p++)
{
	for(int j=0 ; j<28 ; j=j+2)
	System.out.print(TsE8[p][j]+","+TsE8[p][j+1]+"\t");
	
	System.out.println(j8);
}
for (int p=0 ; p<j9 ; p++)
{
	for(int j=0 ; j<36 ; j=j+2)
	System.out.print(TsE9[p][j]+","+TsE9[p][j+1]+"\t");
	
	System.out.println(j9);
}
for (int p=0 ; p<j10 ; p++)
{
	for(int j=0 ; j<45 ; j=j+2)
	System.out.print(TsE10[p][j]+","+TsE10[p][j+1]+"\t");
			
	System.out.println(j10);
}*/	


//------------------------------------------------------------------------------------------------------------------------
//__________________________________  End of Initializing the SN paths ________________________________________________
//---------------------------------------------------------------------------------------------------------------------

	
//----------------------------------------------------------------------------------------------------------------
//--------------------------Run each load setting for a total of Max_Runs times----------------------------------
//----------------------------------------------------------------------------------------------------------------

double[][] VNRs_output = new double[MaximumVNRs][15];
int[] Used_Paths       = new int[MaximumVNRs]; // Array to save the path number of a successful VNR embedding
int[] SNOffNodes       = new int[MaximumVNRs]; // saves the number of turned off SN after each VNR embedding.
int   successfulvnr	   = 0;                    // Counter for the successful VNRs.

for (int i=0 ; i<MaximumVNRs ; i++)
for (int j=0 ; j<10 ; j++)
VNRs_output[i][j] = 0;	// Reset the VNRs array with zeros.	

//------------------------------------------------------------------------------------------------------------------------		    
//---------------------------------------------Start Injecting VNRs-------------------------------------------------------	
//------------------------------------------------------------------------------------------------------------------------		    

double[] SN_Total_PW_Consumption = new double[MaximumVNRs];	// a matrix to save total PW consumption.
double[] Embedding_cost          = new double[MaximumVNRs]; // a matrix to save revenue to cost ratio for each iteration.
double[] VNRs_Acceptance_Ratio   = new double[MaximumVNRs]; // Array to VNRs acceptance ratio after each Max_VNRs iteration.
double[] PowerSaving             = new double[MaximumVNRs]; // Array to save the power saving ratio after each Max_VNRs iteration.
double[] MaxPWCapacity			 = new double[MaximumVNRs];
double[] nodes_average_CPU_utili = new double[MaximumVNRs]; // a matrix to save the average CPU utilization per each iteration 
double[] edges_average_BW_utili  = new double[MaximumVNRs];	// a matrix to save the average BW utilization per each iteration 
double[] SNPC                    = new double[MaximumVNRs]; // This to save the total power consumption in all SN after each iteration.
double[] Consumed_CPU	 		 = new double[MaximumVNRs];
double[] Consumed_BW	 		 = new double[MaximumVNRs];

for (int i=0 ; i<MaximumVNRs ; i++)
{
SN_Total_PW_Consumption[i] = 0;	// Reset with zeros.	
PowerSaving            [i] = 0;	// Reset with zeros.	
MaxPWCapacity          [i] = 0; // Reset with zeros.
VNRs_Acceptance_Ratio  [i] = 0;	// Reset with zeros.	
Embedding_cost         [i] = 0;	// Reset with zeros.	
nodes_average_CPU_utili[i] = 0;	// Reset with zeros.	
edges_average_BW_utili [i] = 0;	// Reset with zeros.	
SNPC                   [i] = 0;
Consumed_CPU           [i] = 0;
Consumed_BW            [i] = 0;
}
double rejectedVNRs    = 0;                 // this is a counter to be used in calculating the VNRs acceptance ratio (rejectedVNRS/numberofVNRs)
int[]   Succ_Used_Paths 		 = new int[MaximumVNRs]; // Array to save the path number of a successful VNR embedding
int[]   Turnedoff_SN_Nodes       = new int[SN_Nodes];   // A matrix to save SN nodes that will be turned off.
int[]   SN_All_Nodes			 = new int[SN_Nodes]; // All nodes in the SN.
int[]   SN_Selected_path_Nodes	 = new int[SN_Nodes]; // Total Nodes in the Selected SN path.
int[]   SN_Nodes_tobe_Turnedoff  = new int[SN_Nodes];	// A matrix to save SN nodes that will be turned off.
double[] Turnoff_SN_Nodes_PCons  = new double[SN_Nodes_tobe_Turnedoff.length]; // A matrix to save the power consumption of each SN that is turned off.

for (int i=0 ; i<MaximumVNRs ; i++)
Succ_Used_Paths[i] = 0;

/*	for (int i=0 ; i<10 ; i++ )	
{
Turnedoff_SN_Nodes[i]     =-1;
SN_Selected_path_Nodes [i]=-1;
SN_Nodes_tobe_Turnedoff[i]=0;			
}
*/
for (int i = 0 ; i < SN_Nodes ; i++)
{
SN_All_Nodes[i] = i ; // Saving the node number for all SN nodes.
//System.out.print(SN_All_Nodes[i] + " ,");
}					

for (int VNR = 0 ; VNR < MaximumVNRs ; VNR++)
{ // This is a for loop over the total simulation time frame.

System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);

//-------------------------------------------------------------------------------------------------------------------				
//--------------------------------- Definition of Power Compensation parameters -------------------------------------
//-------------------------------------------------------------------------------------------------------------------
long[] totalTime = new long[MaximumVNRs];
double Sum_Nodes_CPU_Cons= 0.0;
double Sum_edges_BW_Cons = 0.0;

double VNRs_PC         = 0;
double VNRs_PS         = 0;
double VNRs_AR         = 0;
double VNRs_Co         = 0;
double VNRs_CPUtil     = 0;
double VNRs_BWUtil     = 0;	
double Tot_Num_of_VNRs = 0;                 // total number of received VNRs.

double  Tot_SN_consumed_power    = 0;    // Total consumed power by all SN nodes after each VNR embedding
double  Selected_paths_PC        = 0;						
int     Turnedoffnodes           = 0;
int     NumberofTurndoffSNNodes  = 0;
int     Turned_off_nodes         = 0 ; // A counter to be used to set the size of the matrix that contains the SN nodes that will be turned off.
int     Nodeinselectedpath       = 0 ;
double  PC_forSN_TurnedOff_Nodes = 0 ; // to save the total power consumption of the SN turned off nodes
double  Saved_Power 	 		 = 0;

//-------------------------------------------------------------------------------------------------------------------				
//--------------------------------- End of Definition of Power Compensation parameters -------------------------------------
//-------------------------------------------------------------------------------------------------------------------

//*************************************************************************************************************************
//*************************************************************************************************************************
//*********************           Beginning of EMBEDDING on SN paths          *************************************************
//*************************************************************************************************************************
//*************************************************************************************************************************

int VNRnodes = NumberofnodesinVNR[VNR]; // NodesinVNRpath is an input from the VNR section. But here for testing only	

System.out.println(" Number of nodes in VNR\t("+ VNR + ")\t" + VNRnodes);

startTime = System.currentTimeMillis();

switch(VNRnodes)

{ 

case 10: 
{

	

	int P10 = (0 + (int)(Math.random() * ((Tjp10.length-1)  + 1))); // Randomly selecting the SN topology to try embed on it.

	int P_10_Nodes = 10;	
	
	//******************************************************* Start Comparisons Phase **************************************************  
	//******************************************* First create the paths segments for SN and VNRs **************************************
	//************************************************              VNR Segment                 ***************************************
		
	/* Here we define the VNR segment for each demanded path, that includes
	* 	- Power Consumption and CPU for VNR nodes, and
	*  - BW and Delay for VNR edges.
	*/

	System.out.println("P10--\t"+P10+"----\t"+Tjp10[P10]);

	int sE10p=Tjp10[P10];
	
	for (int p=0 ; p<3 ; p++)
	{
		for(int j=0 ; j<45 ; j++)	
			System.out.print(TsE10[sE10p+p][j]+",");			
			
		System.out.println();		
	}	
		
			
			
		double[] VNRT10_segment_nodes = new double[P_10_Nodes];

		for (int cpu=0 ; cpu < P_10_Nodes ; cpu++)
		{
			VNRT10_segment_nodes[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
			System.out.println(" VNR_CPU---" + TvE10[Tjp10[P10]][cpu] + "------> " + VNRT10_segment_nodes[cpu]);
		}// End of cpu for loop

		double[] VNRT10_segment_edges_BW = new double[45];
		
		for (int ibw = 0 ; ibw < VNRT10_segment_edges_BW.length  ; ibw++)	
		{
			if (TvE10[sE10p+1][ibw]==-1 && TvE10[sE10p+2][ibw]==-1)
			{
				VNRT10_segment_edges_BW[ibw]=0;
				System.out.println(" VNR_BW----" + TvE10[sE10p+1][ibw] + "--> " + TvE10[sE10p+2][ibw] + " --> " + VNRT10_segment_edges_BW[ibw]);

			}else
			{
				VNRT10_segment_edges_BW[ibw]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW		
			
				System.out.println(" VNR_BW----" + TvE10[sE10p+1][ibw] + "--> " + TvE10[sE10p+2][ibw] + " --> " + VNRT10_segment_edges_BW[ibw]);
			
			}
		}// End of ibw for loop

		double[] VNRT10_segment_edges_Delay = new double[45];
		
		for (int i_d = 0 ; i_d < VNRT10_segment_edges_Delay.length ; i_d++)
		{
				if (TvE10[sE10p+1][i_d]==-1 && TvE10[sE10p+2][i_d]==-1)
				{
					VNRT10_segment_edges_Delay[i_d]=0;
					System.out.println(" VNR_Delay----" + TvE10[sE10p+1][i_d] + "--> " + TvE10[sE10p+2][i_d] + " --> " + VNRT10_segment_edges_Delay[i_d]);

				}else
				{
					VNRT10_segment_edges_Delay[i_d]   = D_VNRsEdges_Delay[VNR][i_d]; // VNR edges BW			
				
					System.out.println(" VNR_Delay----" + TvE10[sE10p+1][i_d] + "--> " + TvE10[sE10p+2][i_d] + " --> " + VNRT10_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

		//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

		//********************************************* End of VNR Segment initialization **********************************

		//******************************************************* SN Segment  ********************************************

		/* Here we define the SN segment for each path, that includes
		* 	- Power Consumption and CPU for SN nodes, and
		*  - BW and Delay for SN edges
		*/

	double[] SNT10_segment_nodes = new double[P_10_Nodes];

		for (int cpu=0 ; cpu < P_10_Nodes ; cpu++)
		{
			SNT10_segment_nodes[cpu] = SN_Nodes_CPU_Cons[TsE10[Tjp10[P10]][cpu]]; // VNR nodes CPU
			System.out.println(" SN_CPU---" + TsE10[Tjp10[P10]][cpu] + "----------> " + (int)SNT10_segment_nodes[cpu]);
		}// End of cpu for loop	
		
	double[] SNT10_segment_edges_BW = new double[45];
		
		for (int ibw = 0 ; ibw < SNT10_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE10[sE10p+1][ibw]==-1 && TsE10[sE10p+2][ibw]==-1)
			{
				SNT10_segment_edges_BW[ibw]=0;
				System.out.println(" SN_edge_BW----" + TsE10[sE10p+1][ibw] + "--> " + TsE10[sE10p+2][ibw] + " --> " + (int)SNT10_segment_edges_BW[ibw]);

			}else
			{
				SNT10_segment_edges_BW[ibw]   = SN_BWmatrix[TsE10[sE10p+1][ibw]][TsE10[sE10p+2][ibw]]; // VNR edges BW
			
				System.out.println(" SN_edge_BW----" + TsE10[sE10p+1][ibw] + "--> " + TsE10[sE10p+2][ibw] + " --> " + (int)SNT10_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop

		double[] SNT10_segment_edges_Delay = new double[45];
		
		for (int i_d = 0 ; i_d < SNT10_segment_edges_Delay.length ; i_d++)
		{
				if (TsE10[sE10p+1][i_d]==-1 && TsE10[sE10p+2][i_d]==-1)
				{
					SNT10_segment_edges_Delay[i_d]=0;
					System.out.println(" SN_edge_Delay----" + TsE10[sE10p+1][i_d] + "--> " + TsE10[sE10p+2][i_d] + " --> " + SNT10_segment_edges_Delay[i_d]);

				}else
				{
					SNT10_segment_edges_Delay[i_d]   = SN_DelayMatrix[TsE10[sE10p+1][i_d]][TsE10[sE10p+2][i_d]]; // VNR edges BW
				
					System.out.println(" SN_edge_Delay----" + TsE10[sE10p+1][i_d] + "--> " + TsE10[sE10p+2][i_d] + " --> " + SNT10_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//
		/*	for (int i1=0 ; i1<SNPT10_segment.length ; i1++)
		{
		System.out.print(" SNSeg10\t" + SNPT10_segment[i1]+ "\t" + "VNRSeg\t" + VNRT10_segment[i1]); // this prints the SN formulated segment.				 	
		System.out.println();		
		} 
		*/
		//System.out.println("----------------------------------------------");

		//***********************************  SN to VNR comparisons to check capacity    *****************************

		/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
		* are sufficient to accommodate the VNR demands:
		* 
		* 		- First we compare SN metric to the demanded VNR metric. 
		* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
		* 		  the maximum capacity for that node or edge.
		*/	

		int if_count=0;
		
		for (int cpu=0 ; cpu < P_10_Nodes ; cpu++)
		if ((SNT10_segment_nodes[cpu]-VNRT10_segment_nodes[cpu]) >= 0)
			if_count++;	
		
		for (int ibw = 0 ; ibw < SNT10_segment_edges_BW.length  ; ibw++)	
		if (SNT10_segment_edges_BW[ibw] -  VNRT10_segment_edges_BW[ibw] >= 0) 
			if_count++;	
		
		for (int i_d = 0 ; i_d < SNT10_segment_edges_Delay.length  ; i_d++)	
			if (VNRT10_segment_edges_Delay[i_d] - SNT10_segment_edges_Delay[i_d]  >= 0) 
				if_count++;
					       
		// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
							  
		// To print the values of the SN after updating the SN given a successful embedding
		//System.out.println("---------------------------------------------------------------------------------------------------------");	
		//System.out.println("-------------------------------");

		if(if_count>=100)
		{
		
		// Saving the number of the selected SN topology used for embedding
			
		SN_Selected_Path[VNR][49]=10;  // this is the number of nodes in the SN topology
		SN_Selected_Path[VNR][48]=10;  // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][47]=P10; // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][46]=45;  // This is the number of the SN edges in the selected topology. It will be used for online updating.
			
		// Updating the SN nodes after the embedding
			
		for (int i_cpu = 0 ; i_cpu < P_10_Nodes ; i_cpu++)
		{
			SN_Nodes_CPU_Cons[TsE10[Tjp10[P10]][i_cpu]] = SNT10_segment_nodes[i_cpu] - VNRT10_segment_nodes[i_cpu];
				System.out.println("CPU_b\t("  + TsE10[Tjp10[P10]][i_cpu] + ")\t" + (int)SNT10_segment_nodes[i_cpu] + "\t" +
		    	                   "CPU_a\t"   + (int)SN_Nodes_CPU_Cons[TsE10[Tjp10[P10]][i_cpu]] + "\t" +
				                   "demanded_VNRcpu\t" + (int)VNRT10_segment_nodes[i_cpu]);	

		SN_Selected_Path[VNR][i_cpu]	 = TsE10[Tjp10[P10]][i_cpu];  // to save the SN path nodes that were selected to embed a VNR.

		}// end of i_cpu for loop

		// Updating the SN edges after the embedding
		
		for (int ibw = 0 ; ibw < SNT10_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE10[sE10p+1][ibw]==-1 && TsE10[sE10p+2][ibw]==-1)
			{
				continue;
			}else
			if (TsE10[sE10p+1][ibw]!=-1 && TsE10[sE10p+2][ibw]!=-1)
			{		
				SN_BWmatrix[TsE10[sE10p+1][ibw]][TsE10[sE10p+2][ibw]] = SNT10_segment_edges_BW[ibw] -
						                                                  VNRT10_segment_edges_BW[ibw];		
			
			System.out.println(" SN_edge_BW_b\t----" + TsE10[sE10p+1][ibw] + "--> " + TsE10[sE10p+2][ibw] + " --> " + 
					(int)SNT10_segment_edges_BW[ibw]+"\t"+"SN_edge_BW_a\t----"+ (int)SN_BWmatrix[TsE10[sE10p+1][ibw]][TsE10[sE10p+2][ibw]]+
			                     "\t" + "Demanded_VNRbw\t" + (int)VNRT10_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop
		    		 
		//-------------------
		//-------Calculating the new CPU utilization.
		//------------------					  

		for (int i_cpu = 0 ; i_cpu < P_10_Nodes ; i_cpu++)
		{	
		SN_Nodes_CPU_Cur_Util     [TsE10[Tjp10[P10]][i_cpu]] = 
		((SN_Nodes_MAX_CPU_Cap    [TsE10[Tjp10[P10]][i_cpu]] - 
		SN_Nodes_CPU_Cons         [TsE10[Tjp10[P10]][i_cpu]])/
		SN_Nodes_MAX_CPU_Cap      [TsE10[Tjp10[P10]][i_cpu]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
					
		SN_Nodes_Current_PCons    [TsE10[Tjp10[P10]][i_cpu]] = 
		SN_Nodes_MIN_Power_Cap    [TsE10[Tjp10[P10]][i_cpu]] 
		+((SN_Nodes_MAX_Power_Cap [TsE10[Tjp10[P10]][i_cpu]] 
		-  SN_Nodes_MIN_Power_Cap [TsE10[Tjp10[P10]][i_cpu]]) 
		*  SN_Nodes_CPU_Cur_Util  [TsE10[Tjp10[P10]][i_cpu]]); // this updates the SN current power consumption		

		/*
		System.out.println("-----------------------------------------------");
		System.out.println(" SN_Nodes_CPU_Cur_Util----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cur_Util     [TsE10[Tjp10[P10]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_CPU_Cap-----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_CPU_Cap      [TsE10[Tjp10[P10]][i_cpu]]);
		System.out.println(" SN_Nodes_CPU_Cons--------\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cons         [TsE10[Tjp10[P10]][i_cpu]]);	
		System.out.println(" SN_Nodes_Current_PCons---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_Current_PCons    [TsE10[Tjp10[P10]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_Power_Cap    [TsE10[Tjp10[P10]][i_cpu]]);
		System.out.println(" SN_Nodes_MIN_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MIN_Power_Cap [TsE10[Tjp10[P10]][i_cpu]]);
		*/
		} // End of for (int i_cpu = 0 ; i_cpu < P_10_Nodes ; i_cpu++)		

		/*--------------------------------------------------------------------------------------------
		* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
		* -----------------------------------------------------------------------------------------*/
							  	 
		//System.out.println("SN Nodes of the Selected Path "); 
		for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
		{
		SN_Selected_path_Nodes[i_path] = TsE10[Tjp10[P10]][i_path];
		/* System.out.println("-----------------------------------------------");
		System.out.println(" successfulvnr----" + successfulvnr); 
		System.out.println(" SN_P10_nodes------" + SN_P10_nodes[SNP10_sorted_CPUsum_paths[i]][i_path]); 
		System.out.println(" Path Number------" + SNP10_sorted_CPUsum_paths[i]);
		System.out.println("-----------------------------------------------");
		*/ 
		}
				  
		Succ_Used_Paths[successfulvnr] = Tjp10[P10];   // This saves the path number of the successful embedding
		Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
				  
		//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
		//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
		//System.out.println();
				  
		successfulvnr++;
				  
		double Selected_path_PC10 = 0 ; // includes the total power consumption in the selected path.	

		for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
		Selected_path_PC10 += SN_Nodes_Current_PCons[TsE10[Tjp10[P10]][i_update]]; // this to calculate the total Power Consumption in this selected path

		SNPC[VNR] = Selected_path_PC10; // This save the power consumption according to the selected path.

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("-  End of Successful Embedding: 10-Nodes and links are updated  after VNR embedding  -");
		System.out.println("-----------------------------------------------------------------------------------");				    	
		System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					    	
		} else 
				{
					System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
					System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					System.out.println("Go to next VNR");
			        System.out.println();
					
			        for (int cpu=0 ; cpu < P_10_Nodes ; cpu++)
			        {
			        	D_VNRsNodes_CPU[VNR][cpu] = 0; // VNR nodes CPU
			        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT10_segment[cpu]);
			        	
			        	//SN_Selected_Path[VNR][cpu]	 = SN_P10_nodes[SNP10_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
			        
			        }// End of cpu for loop

			        for (int ibw = 0 ; ibw < (P_10_Nodes-1) ; ibw++)	
			        {
			        	D_VNRsEdges_BW[VNR][ibw] = 0; // VNR edges BW
			        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT10_segment[ibw + (P_10_Nodes)]);
			        }// End of ibw for loop
		            
			        
			        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			    			
			}// End of -if- statement 	
		
		endTime = System.currentTimeMillis();
		totalTime[VNR] = endTime - startTime;
	
} break; // End of Case 10


case 9: 
{

	

	int P9 = (0 + (int)(Math.random() * ((Tjp9.length-1)  + 1))); // Randomly selecting the SN topology to try embed on it.

	int P_9_Nodes = 9;	
	
	//******************************************************* Start Comparisons Phase **************************************************  
	//******************************************* First create the paths segments for SN and VNRs **************************************
	//************************************************              VNR Segment                 ***************************************
		
	/* Here we define the VNR segment for each demanded path, that includes
	* 	- Power Consumption and CPU for VNR nodes, and
	*  - BW and Delay for VNR edges.
	*/

	System.out.println("P9--\t"+P9+"----\t"+Tjp9[P9]);

	int sE9p=Tjp9[P9];
	
	for (int p=0 ; p<3 ; p++)
	{
		for(int j=0 ; j<36 ; j++)	
			System.out.print(TsE9[sE9p+p][j]+",");			
			
		System.out.println();		
	}	
		
			
			
		double[] VNRT9_segment_nodes = new double[P_9_Nodes];

		for (int cpu=0 ; cpu < P_9_Nodes ; cpu++)
		{
			VNRT9_segment_nodes[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
			System.out.println(" VNR_CPU---" + TvE9[Tjp9[P9]][cpu] + "------> " + VNRT9_segment_nodes[cpu]);
		}// End of cpu for loop

		double[] VNRT9_segment_edges_BW = new double[36];
		
		for (int ibw = 0 ; ibw < VNRT9_segment_edges_BW.length  ; ibw++)	
		{
			if (TvE9[sE9p+1][ibw]==-1 && TvE9[sE9p+2][ibw]==-1)
			{
				VNRT9_segment_edges_BW[ibw]=0;
				System.out.println(" VNR_BW----" + TvE9[sE9p+1][ibw] + "--> " + TvE9[sE9p+2][ibw] + " --> " + VNRT9_segment_edges_BW[ibw]);

			}else
			{
				VNRT9_segment_edges_BW[ibw]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW		
			
				System.out.println(" VNR_BW----" + TvE9[sE9p+1][ibw] + "--> " + TvE9[sE9p+2][ibw] + " --> " + VNRT9_segment_edges_BW[ibw]);
			
			}
		}// End of ibw for loop

		double[] VNRT9_segment_edges_Delay = new double[36];
		
		for (int i_d = 0 ; i_d < VNRT9_segment_edges_Delay.length ; i_d++)
		{
				if (TvE9[sE9p+1][i_d]==-1 && TvE9[sE9p+2][i_d]==-1)
				{
					VNRT9_segment_edges_Delay[i_d]=0;
					System.out.println(" VNR_Delay----" + TvE9[sE9p+1][i_d] + "--> " + TvE9[sE9p+2][i_d] + " --> " + VNRT9_segment_edges_Delay[i_d]);

				}else
				{
					VNRT9_segment_edges_Delay[i_d]   = D_VNRsEdges_Delay[VNR][i_d]; // VNR edges BW			
				
					System.out.println(" VNR_Delay----" + TvE9[sE9p+1][i_d] + "--> " + TvE9[sE9p+2][i_d] + " --> " + VNRT9_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

		//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

		//********************************************* End of VNR Segment initialization **********************************

		//******************************************************* SN Segment  ********************************************

		/* Here we define the SN segment for each path, that includes
		* 	- Power Consumption and CPU for SN nodes, and
		*  - BW and Delay for SN edges
		*/

	double[] SNT9_segment_nodes = new double[P_9_Nodes];

		for (int cpu=0 ; cpu < P_9_Nodes ; cpu++)
		{
			SNT9_segment_nodes[cpu] = SN_Nodes_CPU_Cons[TsE9[Tjp9[P9]][cpu]]; // VNR nodes CPU
			System.out.println(" SN_CPU---" + TsE9[Tjp9[P9]][cpu] + "----------> " + (int)SNT9_segment_nodes[cpu]);
		}// End of cpu for loop	
		
	double[] SNT9_segment_edges_BW = new double[36];
		
		for (int ibw = 0 ; ibw < SNT9_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE9[sE9p+1][ibw]==-1 && TsE9[sE9p+2][ibw]==-1)
			{
				SNT9_segment_edges_BW[ibw]=0;
				System.out.println(" SN_edge_BW----" + TsE9[sE9p+1][ibw] + "--> " + TsE9[sE9p+2][ibw] + " --> " + (int)SNT9_segment_edges_BW[ibw]);

			}else
			{
				SNT9_segment_edges_BW[ibw]   = SN_BWmatrix[TsE9[sE9p+1][ibw]][TsE9[sE9p+2][ibw]]; // VNR edges BW
			
				System.out.println(" SN_edge_BW----" + TsE9[sE9p+1][ibw] + "--> " + TsE9[sE9p+2][ibw] + " --> " + (int)SNT9_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop

		double[] SNT9_segment_edges_Delay = new double[36];
		
		for (int i_d = 0 ; i_d < SNT9_segment_edges_Delay.length ; i_d++)
		{
				if (TsE9[sE9p+1][i_d]==-1 && TsE9[sE9p+2][i_d]==-1)
				{
					SNT9_segment_edges_Delay[i_d]=0;
					System.out.println(" SN_edge_Delay----" + TsE9[sE9p+1][i_d] + "--> " + TsE9[sE9p+2][i_d] + " --> " + SNT9_segment_edges_Delay[i_d]);

				}else
				{
					SNT9_segment_edges_Delay[i_d]   = SN_DelayMatrix[TsE9[sE9p+1][i_d]][TsE9[sE9p+2][i_d]]; // VNR edges BW
				
					System.out.println(" SN_edge_Delay----" + TsE9[sE9p+1][i_d] + "--> " + TsE9[sE9p+2][i_d] + " --> " + SNT9_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//
		/*	for (int i1=0 ; i1<SNPT9_segment.length ; i1++)
		{
		System.out.print(" SNSeg9\t" + SNPT9_segment[i1]+ "\t" + "VNRSeg\t" + VNRT9_segment[i1]); // this prints the SN formulated segment.				 	
		System.out.println();		
		} 
		*/
		//System.out.println("----------------------------------------------");

		//***********************************  SN to VNR comparisons to check capacity    *****************************

		/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
		* are sufficient to accommodate the VNR demands:
		* 
		* 		- First we compare SN metric to the demanded VNR metric. 
		* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
		* 		  the maximum capacity for that node or edge.
		*/	

		int if_count=0;
		
		for (int cpu=0 ; cpu < P_9_Nodes ; cpu++)
		if ((SNT9_segment_nodes[cpu]-VNRT9_segment_nodes[cpu]) >= 0)
			if_count++;	
		
		for (int ibw = 0 ; ibw < SNT9_segment_edges_BW.length  ; ibw++)	
		if (SNT9_segment_edges_BW[ibw] -  VNRT9_segment_edges_BW[ibw] >= 0) 
			if_count++;	
		
		for (int i_d = 0 ; i_d < SNT9_segment_edges_Delay.length  ; i_d++)	
			if (VNRT9_segment_edges_Delay[i_d] - SNT9_segment_edges_Delay[i_d]  >= 0) 
				if_count++;
					       
		// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
							  
		// To print the values of the SN after updating the SN given a successful embedding
		//System.out.println("---------------------------------------------------------------------------------------------------------");	
		//System.out.println("-------------------------------");

		if(if_count>=81)
		{
		
		// Saving the number of the selected SN topology used for embedding
			
		SN_Selected_Path[VNR][49]=9;  // this is the number of nodes in the SN topology
		SN_Selected_Path[VNR][48]=9;  // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][47]=P9; // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][46]=36;  // This is the number of the SN edges in the selected topology. It will be used for online updating.
			
		// Updating the SN nodes after the embedding
			
		for (int i_cpu = 0 ; i_cpu < P_9_Nodes ; i_cpu++)
		{
			SN_Nodes_CPU_Cons[TsE9[Tjp9[P9]][i_cpu]] = SNT9_segment_nodes[i_cpu] - VNRT9_segment_nodes[i_cpu];
				System.out.println("CPU_b\t("  + TsE9[Tjp9[P9]][i_cpu] + ")\t" + (int)SNT9_segment_nodes[i_cpu] + "\t" +
		    	                   "CPU_a\t"   + (int)SN_Nodes_CPU_Cons[TsE9[Tjp9[P9]][i_cpu]] + "\t" +
				                   "demanded_VNRcpu\t" + (int)VNRT9_segment_nodes[i_cpu]);	

		SN_Selected_Path[VNR][i_cpu]	 = TsE9[Tjp9[P9]][i_cpu];  // to save the SN path nodes that were selected to embed a VNR.

		}// end of i_cpu for loop

		// Updating the SN edges after the embedding
		
		for (int ibw = 0 ; ibw < SNT9_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE9[sE9p+1][ibw]==-1 && TsE9[sE9p+2][ibw]==-1)
			{
				continue;
			}else
			if (TsE9[sE9p+1][ibw]!=-1 && TsE9[sE9p+2][ibw]!=-1)
			{		
				SN_BWmatrix[TsE9[sE9p+1][ibw]][TsE9[sE9p+2][ibw]] = SNT9_segment_edges_BW[ibw] -
						                                                  VNRT9_segment_edges_BW[ibw];		
			
			System.out.println(" SN_edge_BW_b\t----" + TsE9[sE9p+1][ibw] + "--> " + TsE9[sE9p+2][ibw] + " --> " + 
					(int)SNT9_segment_edges_BW[ibw]+"\t"+"SN_edge_BW_a\t----"+ (int)SN_BWmatrix[TsE9[sE9p+1][ibw]][TsE9[sE9p+2][ibw]]+
			                     "\t" + "Demanded_VNRbw\t" + (int)VNRT9_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop
		    		 
		//-------------------
		//-------Calculating the new CPU utilization.
		//------------------					  

		for (int i_cpu = 0 ; i_cpu < P_9_Nodes ; i_cpu++)
		{	
		SN_Nodes_CPU_Cur_Util     [TsE9[Tjp9[P9]][i_cpu]] = 
		((SN_Nodes_MAX_CPU_Cap    [TsE9[Tjp9[P9]][i_cpu]] - 
		SN_Nodes_CPU_Cons         [TsE9[Tjp9[P9]][i_cpu]])/
		SN_Nodes_MAX_CPU_Cap      [TsE9[Tjp9[P9]][i_cpu]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
					
		SN_Nodes_Current_PCons    [TsE9[Tjp9[P9]][i_cpu]] = 
		SN_Nodes_MIN_Power_Cap    [TsE9[Tjp9[P9]][i_cpu]] 
		+((SN_Nodes_MAX_Power_Cap [TsE9[Tjp9[P9]][i_cpu]] 
		-  SN_Nodes_MIN_Power_Cap [TsE9[Tjp9[P9]][i_cpu]]) 
		*  SN_Nodes_CPU_Cur_Util  [TsE9[Tjp9[P9]][i_cpu]]); // this updates the SN current power consumption		

		/*
		System.out.println("-----------------------------------------------");
		System.out.println(" SN_Nodes_CPU_Cur_Util----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cur_Util     [TsE9[Tjp9[P9]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_CPU_Cap-----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_CPU_Cap      [TsE9[Tjp9[P9]][i_cpu]]);
		System.out.println(" SN_Nodes_CPU_Cons--------\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cons         [TsE9[Tjp9[P9]][i_cpu]]);	
		System.out.println(" SN_Nodes_Current_PCons---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_Current_PCons    [TsE9[Tjp9[P9]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_Power_Cap    [TsE9[Tjp9[P9]][i_cpu]]);
		System.out.println(" SN_Nodes_MIN_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MIN_Power_Cap [TsE9[Tjp9[P9]][i_cpu]]);
		*/
		} // End of for (int i_cpu = 0 ; i_cpu < P_9_Nodes ; i_cpu++)		

		/*--------------------------------------------------------------------------------------------
		* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
		* -----------------------------------------------------------------------------------------*/
							  	 
		//System.out.println("SN Nodes of the Selected Path "); 
		for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
		{
		SN_Selected_path_Nodes[i_path] = TsE9[Tjp9[P9]][i_path];
		/* System.out.println("-----------------------------------------------");
		System.out.println(" successfulvnr----" + successfulvnr); 
		System.out.println(" SN_P9_nodes------" + SN_P9_nodes[SNP9_sorted_CPUsum_paths[i]][i_path]); 
		System.out.println(" Path Number------" + SNP9_sorted_CPUsum_paths[i]);
		System.out.println("-----------------------------------------------");
		*/ 
		}
				  
		Succ_Used_Paths[successfulvnr] = Tjp9[P9];   // This saves the path number of the successful embedding
		Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
				  
		//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
		//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
		//System.out.println();
				  
		successfulvnr++;
				  
		double Selected_path_PC9 = 0 ; // includes the total power consumption in the selected path.	

		for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
		Selected_path_PC9 += SN_Nodes_Current_PCons[TsE9[Tjp9[P9]][i_update]]; // this to calculate the total Power Consumption in this selected path

		SNPC[VNR] = Selected_path_PC9; // This save the power consumption according to the selected path.

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("-  End of Successful Embedding: 9-Nodes and links are updated  after VNR embedding  -");
		System.out.println("-----------------------------------------------------------------------------------");				    	
		System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					    	
		} else 
				{
					System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
					System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					System.out.println("Go to next VNR");
			        System.out.println();
					
			        for (int cpu=0 ; cpu < P_9_Nodes ; cpu++)
			        {
			        	D_VNRsNodes_CPU[VNR][cpu] = 0; // VNR nodes CPU
			        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT9_segment[cpu]);
			        	
			        	//SN_Selected_Path[VNR][cpu]	 = SN_P9_nodes[SNP9_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
			        
			        }// End of cpu for loop

			        for (int ibw = 0 ; ibw < (P_9_Nodes-1) ; ibw++)	
			        {
			        	D_VNRsEdges_BW[VNR][ibw] = 0; // VNR edges BW
			        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT9_segment[ibw + (P_9_Nodes)]);
			        }// End of ibw for loop
		            
			        
			        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			    			
			}// End of -if- statement 	
		
		endTime = System.currentTimeMillis();
		totalTime[VNR] = endTime - startTime;
		
} break; // End of Case 9


//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------  End Of SN paths type 9 Embedding   ----------------------------------------
//-----------------------------------------------------------------------------------------------------------------------

case 8: 
{

	

	int P8 = (0 + (int)(Math.random() * ((Tjp8.length-1)  + 1))); // Randomly selecting the SN topology to try embed on it.

	int P_8_Nodes = 8;	
	
	//******************************************************* Start Comparisons Phase **************************************************  
	//******************************************* First create the paths segments for SN and VNRs **************************************
	//************************************************              VNR Segment                 ***************************************
		
	/* Here we define the VNR segment for each demanded path, that includes
	* 	- Power Consumption and CPU for VNR nodes, and
	*  - BW and Delay for VNR edges.
	*/

	System.out.println("P8--\t"+P8+"----\t"+Tjp8[P8]);

	int sE8p=Tjp8[P8];
	
	for (int p=0 ; p<3 ; p++)
	{
		for(int j=0 ; j<28 ; j++)	
			System.out.print(TsE8[sE8p+p][j]+",");			
			
		System.out.println();		
	}	
		
			
			
		double[] VNRT8_segment_nodes = new double[P_8_Nodes];

		for (int cpu=0 ; cpu < P_8_Nodes ; cpu++)
		{
			VNRT8_segment_nodes[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
			System.out.println(" VNR_CPU---" + TvE8[Tjp8[P8]][cpu] + "------> " + VNRT8_segment_nodes[cpu]);
		}// End of cpu for loop

		double[] VNRT8_segment_edges_BW = new double[28];
		
		for (int ibw = 0 ; ibw < VNRT8_segment_edges_BW.length  ; ibw++)	
		{
			if (TvE8[sE8p+1][ibw]==-1 && TvE8[sE8p+2][ibw]==-1)
			{
				VNRT8_segment_edges_BW[ibw]=0;
				System.out.println(" VNR_BW----" + TvE8[sE8p+1][ibw] + "--> " + TvE8[sE8p+2][ibw] + " --> " + VNRT8_segment_edges_BW[ibw]);

			}else
			{
				VNRT8_segment_edges_BW[ibw]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW		
			
				System.out.println(" VNR_BW----" + TvE8[sE8p+1][ibw] + "--> " + TvE8[sE8p+2][ibw] + " --> " + VNRT8_segment_edges_BW[ibw]);
			
			}
		}// End of ibw for loop

		double[] VNRT8_segment_edges_Delay = new double[28];
		
		for (int i_d = 0 ; i_d < VNRT8_segment_edges_Delay.length ; i_d++)
		{
				if (TvE8[sE8p+1][i_d]==-1 && TvE8[sE8p+2][i_d]==-1)
				{
					VNRT8_segment_edges_Delay[i_d]=0;
					System.out.println(" VNR_Delay----" + TvE8[sE8p+1][i_d] + "--> " + TvE8[sE8p+2][i_d] + " --> " + VNRT8_segment_edges_Delay[i_d]);

				}else
				{
					VNRT8_segment_edges_Delay[i_d]   = D_VNRsEdges_Delay[VNR][i_d]; // VNR edges BW			
				
					System.out.println(" VNR_Delay----" + TvE8[sE8p+1][i_d] + "--> " + TvE8[sE8p+2][i_d] + " --> " + VNRT8_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

		//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

		//********************************************* End of VNR Segment initialization **********************************

		//******************************************************* SN Segment  ********************************************

		/* Here we define the SN segment for each path, that includes
		* 	- Power Consumption and CPU for SN nodes, and
		*  - BW and Delay for SN edges
		*/

	double[] SNT8_segment_nodes = new double[P_8_Nodes];

		for (int cpu=0 ; cpu < P_8_Nodes ; cpu++)
		{
			SNT8_segment_nodes[cpu] = SN_Nodes_CPU_Cons[TsE8[Tjp8[P8]][cpu]]; // VNR nodes CPU
			System.out.println(" SN_CPU---" + TsE8[Tjp8[P8]][cpu] + "----------> " + (int)SNT8_segment_nodes[cpu]);
		}// End of cpu for loop	
		
	double[] SNT8_segment_edges_BW = new double[28];
		
		for (int ibw = 0 ; ibw < SNT8_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE8[sE8p+1][ibw]==-1 && TsE8[sE8p+2][ibw]==-1)
			{
				SNT8_segment_edges_BW[ibw]=0;
				System.out.println(" SN_edge_BW----" + TsE8[sE8p+1][ibw] + "--> " + TsE8[sE8p+2][ibw] + " --> " + (int)SNT8_segment_edges_BW[ibw]);

			}else
			{
				SNT8_segment_edges_BW[ibw]   = SN_BWmatrix[TsE8[sE8p+1][ibw]][TsE8[sE8p+2][ibw]]; // VNR edges BW
			
				System.out.println(" SN_edge_BW----" + TsE8[sE8p+1][ibw] + "--> " + TsE8[sE8p+2][ibw] + " --> " + (int)SNT8_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop

		double[] SNT8_segment_edges_Delay = new double[28];
		
		for (int i_d = 0 ; i_d < SNT8_segment_edges_Delay.length ; i_d++)
		{
				if (TsE8[sE8p+1][i_d]==-1 && TsE8[sE8p+2][i_d]==-1)
				{
					SNT8_segment_edges_Delay[i_d]=0;
					System.out.println(" SN_edge_Delay----" + TsE8[sE8p+1][i_d] + "--> " + TsE8[sE8p+2][i_d] + " --> " + SNT8_segment_edges_Delay[i_d]);

				}else
				{
					SNT8_segment_edges_Delay[i_d]   = SN_DelayMatrix[TsE8[sE8p+1][i_d]][TsE8[sE8p+2][i_d]]; // VNR edges BW
				
					System.out.println(" SN_edge_Delay----" + TsE8[sE8p+1][i_d] + "--> " + TsE8[sE8p+2][i_d] + " --> " + SNT8_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//
		/*	for (int i1=0 ; i1<SNPT8_segment.length ; i1++)
		{
		System.out.print(" SNSeg8\t" + SNPT8_segment[i1]+ "\t" + "VNRSeg\t" + VNRT8_segment[i1]); // this prints the SN formulated segment.				 	
		System.out.println();		
		} 
		*/
		//System.out.println("----------------------------------------------");

		//***********************************  SN to VNR comparisons to check capacity    *****************************

		/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
		* are sufficient to accommodate the VNR demands:
		* 
		* 		- First we compare SN metric to the demanded VNR metric. 
		* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
		* 		  the maximum capacity for that node or edge.
		*/	

		int if_count=0;
		
		for (int cpu=0 ; cpu < P_8_Nodes ; cpu++)
		if ((SNT8_segment_nodes[cpu]-VNRT8_segment_nodes[cpu]) >= 0)
			if_count++;	
		
		for (int ibw = 0 ; ibw < SNT8_segment_edges_BW.length  ; ibw++)	
		if (SNT8_segment_edges_BW[ibw] -  VNRT8_segment_edges_BW[ibw] >= 0) 
			if_count++;	
		
		for (int i_d = 0 ; i_d < SNT8_segment_edges_Delay.length  ; i_d++)	
			if (VNRT8_segment_edges_Delay[i_d] - SNT8_segment_edges_Delay[i_d]  >= 0) 
				if_count++;
					       
		// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
							  
		// To print the values of the SN after updating the SN given a successful embedding
		//System.out.println("---------------------------------------------------------------------------------------------------------");	
		//System.out.println("-------------------------------");

		if(if_count>=64)
		{
		
		// Saving the number of the selected SN topology used for embedding
			
		SN_Selected_Path[VNR][49]=8;  // this is the number of nodes in the SN topology
		SN_Selected_Path[VNR][48]=8;  // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][47]=P8; // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][46]=28;  // This is the number of the SN edges in the selected topology. It will be used for online updating.
			
		// Updating the SN nodes after the embedding
			
		for (int i_cpu = 0 ; i_cpu < P_8_Nodes ; i_cpu++)
		{
			SN_Nodes_CPU_Cons[TsE8[Tjp8[P8]][i_cpu]] = SNT8_segment_nodes[i_cpu] - VNRT8_segment_nodes[i_cpu];
				System.out.println("CPU_b\t("  + TsE8[Tjp8[P8]][i_cpu] + ")\t" + (int)SNT8_segment_nodes[i_cpu] + "\t" +
		    	                   "CPU_a\t"   + (int)SN_Nodes_CPU_Cons[TsE8[Tjp8[P8]][i_cpu]] + "\t" +
				                   "demanded_VNRcpu\t" + (int)VNRT8_segment_nodes[i_cpu]);	

		SN_Selected_Path[VNR][i_cpu]	 = TsE8[Tjp8[P8]][i_cpu];  // to save the SN path nodes that were selected to embed a VNR.

		}// end of i_cpu for loop

		// Updating the SN edges after the embedding
		
		for (int ibw = 0 ; ibw < SNT8_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE8[sE8p+1][ibw]==-1 && TsE8[sE8p+2][ibw]==-1)
			{
				continue;
			}else
			if (TsE8[sE8p+1][ibw]!=-1 && TsE8[sE8p+2][ibw]!=-1)
			{		
				SN_BWmatrix[TsE8[sE8p+1][ibw]][TsE8[sE8p+2][ibw]] = SNT8_segment_edges_BW[ibw] -
						                                                  VNRT8_segment_edges_BW[ibw];		
			
			System.out.println(" SN_edge_BW_b\t----" + TsE8[sE8p+1][ibw] + "--> " + TsE8[sE8p+2][ibw] + " --> " + 
					(int)SNT8_segment_edges_BW[ibw]+"\t"+"SN_edge_BW_a\t----"+ (int)SN_BWmatrix[TsE8[sE8p+1][ibw]][TsE8[sE8p+2][ibw]]+
			                     "\t" + "Demanded_VNRbw\t" + (int)VNRT8_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop
		    		 
		//-------------------
		//-------Calculating the new CPU utilization.
		//------------------					  

		for (int i_cpu = 0 ; i_cpu < P_8_Nodes ; i_cpu++)
		{	
		SN_Nodes_CPU_Cur_Util     [TsE8[Tjp8[P8]][i_cpu]] = 
		((SN_Nodes_MAX_CPU_Cap    [TsE8[Tjp8[P8]][i_cpu]] - 
		SN_Nodes_CPU_Cons         [TsE8[Tjp8[P8]][i_cpu]])/
		SN_Nodes_MAX_CPU_Cap      [TsE8[Tjp8[P8]][i_cpu]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
					
		SN_Nodes_Current_PCons    [TsE8[Tjp8[P8]][i_cpu]] = 
		SN_Nodes_MIN_Power_Cap    [TsE8[Tjp8[P8]][i_cpu]] 
		+((SN_Nodes_MAX_Power_Cap [TsE8[Tjp8[P8]][i_cpu]] 
		-  SN_Nodes_MIN_Power_Cap [TsE8[Tjp8[P8]][i_cpu]]) 
		*  SN_Nodes_CPU_Cur_Util  [TsE8[Tjp8[P8]][i_cpu]]); // this updates the SN current power consumption		

		/*
		System.out.println("-----------------------------------------------");
		System.out.println(" SN_Nodes_CPU_Cur_Util----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cur_Util     [TsE8[Tjp8[P8]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_CPU_Cap-----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_CPU_Cap      [TsE8[Tjp8[P8]][i_cpu]]);
		System.out.println(" SN_Nodes_CPU_Cons--------\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cons         [TsE8[Tjp8[P8]][i_cpu]]);	
		System.out.println(" SN_Nodes_Current_PCons---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_Current_PCons    [TsE8[Tjp8[P8]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_Power_Cap    [TsE8[Tjp8[P8]][i_cpu]]);
		System.out.println(" SN_Nodes_MIN_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MIN_Power_Cap [TsE8[Tjp8[P8]][i_cpu]]);
		*/
		} // End of for (int i_cpu = 0 ; i_cpu < P_8_Nodes ; i_cpu++)		

		/*--------------------------------------------------------------------------------------------
		* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
		* -----------------------------------------------------------------------------------------*/
							  	 
		//System.out.println("SN Nodes of the Selected Path "); 
		for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
		{
		SN_Selected_path_Nodes[i_path] = TsE8[Tjp8[P8]][i_path];
		/* System.out.println("-----------------------------------------------");
		System.out.println(" successfulvnr----" + successfulvnr); 
		System.out.println(" SN_P8_nodes------" + SN_P8_nodes[SNP8_sorted_CPUsum_paths[i]][i_path]); 
		System.out.println(" Path Number------" + SNP8_sorted_CPUsum_paths[i]);
		System.out.println("-----------------------------------------------");
		*/ 
		}
				  
		Succ_Used_Paths[successfulvnr] = Tjp8[P8];   // This saves the path number of the successful embedding
		Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
				  
		//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
		//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
		//System.out.println();
				  
		successfulvnr++;
				  
		double Selected_path_PC8 = 0 ; // includes the total power consumption in the selected path.	

		for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
		Selected_path_PC8 += SN_Nodes_Current_PCons[TsE8[Tjp8[P8]][i_update]]; // this to calculate the total Power Consumption in this selected path

		SNPC[VNR] = Selected_path_PC8; // This save the power consumption according to the selected path.

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("-  End of Successful Embedding: 8-Nodes and links are updated  after VNR embedding  -");
		System.out.println("-----------------------------------------------------------------------------------");				    	
		System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					    	
		} else 
				{
					System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
					System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					System.out.println("Go to next VNR");
			        System.out.println();
					
			        for (int cpu=0 ; cpu < P_8_Nodes ; cpu++)
			        {
			        	D_VNRsNodes_CPU[VNR][cpu] = 0; // VNR nodes CPU
			        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT8_segment[cpu]);
			        	
			        	//SN_Selected_Path[VNR][cpu]	 = SN_P8_nodes[SNP8_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
			        
			        }// End of cpu for loop

			        for (int ibw = 0 ; ibw < (P_8_Nodes-1) ; ibw++)	
			        {
			        	D_VNRsEdges_BW[VNR][ibw] = 0; // VNR edges BW
			        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT8_segment[ibw + (P_8_Nodes)]);
			        }// End of ibw for loop
		            
			        
			        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			    			
			}// End of -if- statement 	
		
		endTime = System.currentTimeMillis();
		totalTime[VNR] = endTime - startTime;
	

} break; // End of Case 8




//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------  End Of SN paths type 8 Embedding   ----------------------------------------
//-----------------------------------------------------------------------------------------------------------------------

case 7: 
{

	

	int P7 = (0 + (int)(Math.random() * ((Tjp7.length-1)  + 1))); // Randomly selecting the SN topology to try embed on it.

	int P_7_Nodes = 7;	
	
	//******************************************************* Start Comparisons Phase **************************************************  
	//******************************************* First create the paths segments for SN and VNRs **************************************
	//************************************************              VNR Segment                 ***************************************
		
	/* Here we define the VNR segment for each demanded path, that includes
	* 	- Power Consumption and CPU for VNR nodes, and
	*  - BW and Delay for VNR edges.
	*/

	System.out.println("P7--\t"+P7+"----\t"+Tjp7[P7]);

	int sE7p=Tjp7[P7];
	
	for (int p=0 ; p<3 ; p++)
	{
		for(int j=0 ; j<21 ; j++)	
			System.out.print(TsE7[sE7p+p][j]+",");			
			
		System.out.println();		
	}	
		
			
			
		double[] VNRT7_segment_nodes = new double[P_7_Nodes];

		for (int cpu=0 ; cpu < P_7_Nodes ; cpu++)
		{
			VNRT7_segment_nodes[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
			System.out.println(" VNR_CPU---" + TvE7[Tjp7[P7]][cpu] + "------> " + VNRT7_segment_nodes[cpu]);
		}// End of cpu for loop

		double[] VNRT7_segment_edges_BW = new double[21];
		
		for (int ibw = 0 ; ibw < VNRT7_segment_edges_BW.length  ; ibw++)	
		{
			if (TvE7[sE7p+1][ibw]==-1 && TvE7[sE7p+2][ibw]==-1)
			{
				VNRT7_segment_edges_BW[ibw]=0;
				System.out.println(" VNR_BW----" + TvE7[sE7p+1][ibw] + "--> " + TvE7[sE7p+2][ibw] + " --> " + VNRT7_segment_edges_BW[ibw]);

			}else
			{
				VNRT7_segment_edges_BW[ibw]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW		
			
				System.out.println(" VNR_BW----" + TvE7[sE7p+1][ibw] + "--> " + TvE7[sE7p+2][ibw] + " --> " + VNRT7_segment_edges_BW[ibw]);
			
			}
		}// End of ibw for loop

		double[] VNRT7_segment_edges_Delay = new double[21];
		
		for (int i_d = 0 ; i_d < VNRT7_segment_edges_Delay.length ; i_d++)
		{
				if (TvE7[sE7p+1][i_d]==-1 && TvE7[sE7p+2][i_d]==-1)
				{
					VNRT7_segment_edges_Delay[i_d]=0;
					System.out.println(" VNR_Delay----" + TvE7[sE7p+1][i_d] + "--> " + TvE7[sE7p+2][i_d] + " --> " + VNRT7_segment_edges_Delay[i_d]);

				}else
				{
					VNRT7_segment_edges_Delay[i_d]   = D_VNRsEdges_Delay[VNR][i_d]; // VNR edges BW			
				
					System.out.println(" VNR_Delay----" + TvE7[sE7p+1][i_d] + "--> " + TvE7[sE7p+2][i_d] + " --> " + VNRT7_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

		//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

		//********************************************* End of VNR Segment initialization **********************************

		//******************************************************* SN Segment  ********************************************

		/* Here we define the SN segment for each path, that includes
		* 	- Power Consumption and CPU for SN nodes, and
		*  - BW and Delay for SN edges
		*/

	double[] SNT7_segment_nodes = new double[P_7_Nodes];

		for (int cpu=0 ; cpu < P_7_Nodes ; cpu++)
		{
			SNT7_segment_nodes[cpu] = SN_Nodes_CPU_Cons[TsE7[Tjp7[P7]][cpu]]; // VNR nodes CPU
			System.out.println(" SN_CPU---" + TsE7[Tjp7[P7]][cpu] + "----------> " + (int)SNT7_segment_nodes[cpu]);
		}// End of cpu for loop	
		
	double[] SNT7_segment_edges_BW = new double[21];
		
		for (int ibw = 0 ; ibw < SNT7_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE7[sE7p+1][ibw]==-1 && TsE7[sE7p+2][ibw]==-1)
			{
				SNT7_segment_edges_BW[ibw]=0;
				System.out.println(" SN_edge_BW----" + TsE7[sE7p+1][ibw] + "--> " + TsE7[sE7p+2][ibw] + " --> " + (int)SNT7_segment_edges_BW[ibw]);

			}else
			{
				SNT7_segment_edges_BW[ibw]   = SN_BWmatrix[TsE7[sE7p+1][ibw]][TsE7[sE7p+2][ibw]]; // VNR edges BW
			
				System.out.println(" SN_edge_BW----" + TsE7[sE7p+1][ibw] + "--> " + TsE7[sE7p+2][ibw] + " --> " + (int)SNT7_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop

		double[] SNT7_segment_edges_Delay = new double[21];
		
		for (int i_d = 0 ; i_d < SNT7_segment_edges_Delay.length ; i_d++)
		{
				if (TsE7[sE7p+1][i_d]==-1 && TsE7[sE7p+2][i_d]==-1)
				{
					SNT7_segment_edges_Delay[i_d]=0;
					System.out.println(" SN_edge_Delay----" + TsE7[sE7p+1][i_d] + "--> " + TsE7[sE7p+2][i_d] + " --> " + SNT7_segment_edges_Delay[i_d]);

				}else
				{
					SNT7_segment_edges_Delay[i_d]   = SN_DelayMatrix[TsE7[sE7p+1][i_d]][TsE7[sE7p+2][i_d]]; // VNR edges BW
				
					System.out.println(" SN_edge_Delay----" + TsE7[sE7p+1][i_d] + "--> " + TsE7[sE7p+2][i_d] + " --> " + SNT7_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//
		/*	for (int i1=0 ; i1<SNPT7_segment.length ; i1++)
		{
		System.out.print(" SNSeg7\t" + SNPT7_segment[i1]+ "\t" + "VNRSeg\t" + VNRT7_segment[i1]); // this prints the SN formulated segment.				 	
		System.out.println();		
		} 
		*/
		//System.out.println("----------------------------------------------");

		//***********************************  SN to VNR comparisons to check capacity    *****************************

		/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
		* are sufficient to accommodate the VNR demands:
		* 
		* 		- First we compare SN metric to the demanded VNR metric. 
		* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
		* 		  the maximum capacity for that node or edge.
		*/	

		int if_count=0;
		
		for (int cpu=0 ; cpu < P_7_Nodes ; cpu++)
		if ((SNT7_segment_nodes[cpu]-VNRT7_segment_nodes[cpu]) >= 0)
			if_count++;	
		
		for (int ibw = 0 ; ibw < SNT7_segment_edges_BW.length  ; ibw++)	
		if (SNT7_segment_edges_BW[ibw] -  VNRT7_segment_edges_BW[ibw] >= 0) 
			if_count++;	
		
		for (int i_d = 0 ; i_d < SNT7_segment_edges_Delay.length  ; i_d++)	
			if (VNRT7_segment_edges_Delay[i_d] - SNT7_segment_edges_Delay[i_d]  >= 0) 
				if_count++;
					       
		// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
							  
		// To print the values of the SN after updating the SN given a successful embedding
		//System.out.println("---------------------------------------------------------------------------------------------------------");	
		//System.out.println("-------------------------------");

		if(if_count>=49)
		{
		
		// Saving the number of the selected SN topology used for embedding
			
		SN_Selected_Path[VNR][49]=7;  // this is the number of nodes in the SN topology
		SN_Selected_Path[VNR][48]=7;  // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][47]=P7; // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][46]=21;  // This is the number of the SN edges in the selected topology. It will be used for online updating.
			
		// Updating the SN nodes after the embedding
			
		for (int i_cpu = 0 ; i_cpu < P_7_Nodes ; i_cpu++)
		{
			SN_Nodes_CPU_Cons[TsE7[Tjp7[P7]][i_cpu]] = SNT7_segment_nodes[i_cpu] - VNRT7_segment_nodes[i_cpu];
				System.out.println("CPU_b\t("  + TsE7[Tjp7[P7]][i_cpu] + ")\t" + (int)SNT7_segment_nodes[i_cpu] + "\t" +
		    	                   "CPU_a\t"   + (int)SN_Nodes_CPU_Cons[TsE7[Tjp7[P7]][i_cpu]] + "\t" +
				                   "demanded_VNRcpu\t" + (int)VNRT7_segment_nodes[i_cpu]);	

		SN_Selected_Path[VNR][i_cpu]	 = TsE7[Tjp7[P7]][i_cpu];  // to save the SN path nodes that were selected to embed a VNR.

		}// end of i_cpu for loop

		// Updating the SN edges after the embedding
		
		for (int ibw = 0 ; ibw < SNT7_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE7[sE7p+1][ibw]==-1 && TsE7[sE7p+2][ibw]==-1)
			{
				continue;
			}else
			if (TsE7[sE7p+1][ibw]!=-1 && TsE7[sE7p+2][ibw]!=-1)
			{		
				SN_BWmatrix[TsE7[sE7p+1][ibw]][TsE7[sE7p+2][ibw]] = SNT7_segment_edges_BW[ibw] -
						                                                  VNRT7_segment_edges_BW[ibw];		
			
			System.out.println(" SN_edge_BW_b\t----" + TsE7[sE7p+1][ibw] + "--> " + TsE7[sE7p+2][ibw] + " --> " + 
					(int)SNT7_segment_edges_BW[ibw]+"\t"+"SN_edge_BW_a\t----"+ (int)SN_BWmatrix[TsE7[sE7p+1][ibw]][TsE7[sE7p+2][ibw]]+
			                     "\t" + "Demanded_VNRbw\t" + (int)VNRT7_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop
		    		 
		//-------------------
		//-------Calculating the new CPU utilization.
		//------------------					  

		for (int i_cpu = 0 ; i_cpu < P_7_Nodes ; i_cpu++)
		{	
		SN_Nodes_CPU_Cur_Util     [TsE7[Tjp7[P7]][i_cpu]] = 
		((SN_Nodes_MAX_CPU_Cap    [TsE7[Tjp7[P7]][i_cpu]] - 
		SN_Nodes_CPU_Cons         [TsE7[Tjp7[P7]][i_cpu]])/
		SN_Nodes_MAX_CPU_Cap      [TsE7[Tjp7[P7]][i_cpu]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
					
		SN_Nodes_Current_PCons    [TsE7[Tjp7[P7]][i_cpu]] = 
		SN_Nodes_MIN_Power_Cap    [TsE7[Tjp7[P7]][i_cpu]] 
		+((SN_Nodes_MAX_Power_Cap [TsE7[Tjp7[P7]][i_cpu]] 
		-  SN_Nodes_MIN_Power_Cap [TsE7[Tjp7[P7]][i_cpu]]) 
		*  SN_Nodes_CPU_Cur_Util  [TsE7[Tjp7[P7]][i_cpu]]); // this updates the SN current power consumption		

		/*
		System.out.println("-----------------------------------------------");
		System.out.println(" SN_Nodes_CPU_Cur_Util----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cur_Util     [TsE7[Tjp7[P7]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_CPU_Cap-----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_CPU_Cap      [TsE7[Tjp7[P7]][i_cpu]]);
		System.out.println(" SN_Nodes_CPU_Cons--------\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cons         [TsE7[Tjp7[P7]][i_cpu]]);	
		System.out.println(" SN_Nodes_Current_PCons---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_Current_PCons    [TsE7[Tjp7[P7]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_Power_Cap    [TsE7[Tjp7[P7]][i_cpu]]);
		System.out.println(" SN_Nodes_MIN_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MIN_Power_Cap [TsE7[Tjp7[P7]][i_cpu]]);
		*/
		} // End of for (int i_cpu = 0 ; i_cpu < P_7_Nodes ; i_cpu++)		

		/*--------------------------------------------------------------------------------------------
		* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
		* -----------------------------------------------------------------------------------------*/
							  	 
		//System.out.println("SN Nodes of the Selected Path "); 
		for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
		{
		SN_Selected_path_Nodes[i_path] = TsE7[Tjp7[P7]][i_path];
		/* System.out.println("-----------------------------------------------");
		System.out.println(" successfulvnr----" + successfulvnr); 
		System.out.println(" SN_P7_nodes------" + SN_P7_nodes[SNP7_sorted_CPUsum_paths[i]][i_path]); 
		System.out.println(" Path Number------" + SNP7_sorted_CPUsum_paths[i]);
		System.out.println("-----------------------------------------------");
		*/ 
		}
				  
		Succ_Used_Paths[successfulvnr] = Tjp7[P7];   // This saves the path number of the successful embedding
		Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
				  
		//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
		//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
		//System.out.println();
				  
		successfulvnr++;
				  
		double Selected_path_PC7 = 0 ; // includes the total power consumption in the selected path.	

		for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
		Selected_path_PC7 += SN_Nodes_Current_PCons[TsE7[Tjp7[P7]][i_update]]; // this to calculate the total Power Consumption in this selected path

		SNPC[VNR] = Selected_path_PC7; // This save the power consumption according to the selected path.

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("-  End of Successful Embedding: 7-Nodes and links are updated  after VNR embedding  -");
		System.out.println("-----------------------------------------------------------------------------------");				    	
		System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					    	
		} else 
				{
					System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
					System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					System.out.println("Go to next VNR");
			        System.out.println();
					
			        for (int cpu=0 ; cpu < P_7_Nodes ; cpu++)
			        {
			        	D_VNRsNodes_CPU[VNR][cpu] = 0; // VNR nodes CPU
			        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT7_segment[cpu]);
			        	
			        	//SN_Selected_Path[VNR][cpu]	 = SN_P7_nodes[SNP7_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
			        
			        }// End of cpu for loop

			        for (int ibw = 0 ; ibw < (P_7_Nodes-1) ; ibw++)	
			        {
			        	D_VNRsEdges_BW[VNR][ibw] = 0; // VNR edges BW
			        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT7_segment[ibw + (P_7_Nodes)]);
			        }// End of ibw for loop
		            
			        
			        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			    			
			}// End of -if- statement 	
		
		endTime = System.currentTimeMillis();
		totalTime[VNR] = endTime - startTime;
		
	


	

} break; // End of Case 7


//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------  End Of SN paths type 7 Embedding   ----------------------------------------
//-----------------------------------------------------------------------------------------------------------------------

case 6: 
{

	

	int P6 = (0 + (int)(Math.random() * ((Tjp6.length-1)  + 1))); // Randomly selecting the SN topology to try embed on it.

	int P_6_Nodes = 6;	
	
	//******************************************************* Start Comparisons Phase **************************************************  
	//******************************************* First create the paths segments for SN and VNRs **************************************
	//************************************************              VNR Segment                 ***************************************
		
	/* Here we define the VNR segment for each demanded path, that includes
	* 	- Power Consumption and CPU for VNR nodes, and
	*  - BW and Delay for VNR edges.
	*/

	System.out.println("P6--\t"+P6+"----\t"+Tjp6[P6]);

	int sE6p=Tjp6[P6];
	
	for (int p=0 ; p<3 ; p++)
	{
		for(int j=0 ; j<15 ; j++)	
			System.out.print(TsE6[sE6p+p][j]+",");			
			
		System.out.println();		
	}	
		
			
			
		double[] VNRT6_segment_nodes = new double[P_6_Nodes];

		for (int cpu=0 ; cpu < P_6_Nodes ; cpu++)
		{
			VNRT6_segment_nodes[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
			System.out.println(" VNR_CPU---" + TvE6[Tjp6[P6]][cpu] + "------> " + VNRT6_segment_nodes[cpu]);
		}// End of cpu for loop

		double[] VNRT6_segment_edges_BW = new double[15];
		
		for (int ibw = 0 ; ibw < VNRT6_segment_edges_BW.length  ; ibw++)	
		{
			if (TvE6[sE6p+1][ibw]==-1 && TvE6[sE6p+2][ibw]==-1)
			{
				VNRT6_segment_edges_BW[ibw]=0;
				System.out.println(" VNR_BW----" + TvE6[sE6p+1][ibw] + "--> " + TvE6[sE6p+2][ibw] + " --> " + VNRT6_segment_edges_BW[ibw]);

			}else
			{
				VNRT6_segment_edges_BW[ibw]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW		
			
				System.out.println(" VNR_BW----" + TvE6[sE6p+1][ibw] + "--> " + TvE6[sE6p+2][ibw] + " --> " + VNRT6_segment_edges_BW[ibw]);
			
			}
		}// End of ibw for loop

		double[] VNRT6_segment_edges_Delay = new double[15];
		
		for (int i_d = 0 ; i_d < VNRT6_segment_edges_Delay.length ; i_d++)
		{
				if (TvE6[sE6p+1][i_d]==-1 && TvE6[sE6p+2][i_d]==-1)
				{
					VNRT6_segment_edges_Delay[i_d]=0;
					System.out.println(" VNR_Delay----" + TvE6[sE6p+1][i_d] + "--> " + TvE6[sE6p+2][i_d] + " --> " + VNRT6_segment_edges_Delay[i_d]);

				}else
				{
					VNRT6_segment_edges_Delay[i_d]   = D_VNRsEdges_Delay[VNR][i_d] ; // VNR edges BW			
				
					System.out.println(" VNR_Delay----" + TvE6[sE6p+1][i_d] + "--> " + TvE6[sE6p+2][i_d] + " --> " + VNRT6_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

		//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

		//********************************************* End of VNR Segment initialization **********************************

		//******************************************************* SN Segment  ********************************************

		/* Here we define the SN segment for each path, that includes
		* 	- Power Consumption and CPU for SN nodes, and
		*  - BW and Delay for SN edges
		*/

	double[] SNT6_segment_nodes = new double[P_6_Nodes];

		for (int cpu=0 ; cpu < P_6_Nodes ; cpu++)
		{
			SNT6_segment_nodes[cpu] = SN_Nodes_CPU_Cons[TsE6[Tjp6[P6]][cpu]]; // VNR nodes CPU
			System.out.println(" SN_CPU---" + TsE6[Tjp6[P6]][cpu] + "----------> " + (int)SNT6_segment_nodes[cpu]);
		}// End of cpu for loop	
		
	double[] SNT6_segment_edges_BW = new double[15];
		
		for (int ibw = 0 ; ibw < SNT6_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE6[sE6p+1][ibw]==-1 && TsE6[sE6p+2][ibw]==-1)
			{
				SNT6_segment_edges_BW[ibw]=0;
				System.out.println(" SN_edge_BW----" + TsE6[sE6p+1][ibw] + "--> " + TsE6[sE6p+2][ibw] + " --> " + (int)SNT6_segment_edges_BW[ibw]);

			}else
			{
				SNT6_segment_edges_BW[ibw]   = SN_BWmatrix[TsE6[sE6p+1][ibw]][TsE6[sE6p+2][ibw]]; // VNR edges BW
			
				System.out.println(" SN_edge_BW----" + TsE6[sE6p+1][ibw] + "--> " + TsE6[sE6p+2][ibw] + " --> " + (int)SNT6_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop

		double[] SNT6_segment_edges_Delay = new double[15];
		
		for (int i_d = 0 ; i_d < SNT6_segment_edges_Delay.length ; i_d++)
		{
				if (TsE6[sE6p+1][i_d]==-1 && TsE6[sE6p+2][i_d]==-1)
				{
					SNT6_segment_edges_Delay[i_d]=0;
					System.out.println(" SN_edge_Delay----" + TsE6[sE6p+1][i_d] + "--> " + TsE6[sE6p+2][i_d] + " --> " + SNT6_segment_edges_Delay[i_d]);

				}else
				{
					SNT6_segment_edges_Delay[i_d]   = SN_DelayMatrix[TsE6[sE6p+1][i_d]][TsE6[sE6p+2][i_d]]; // VNR edges BW
				
					System.out.println(" SN_edge_Delay----" + TsE6[sE6p+1][i_d] + "--> " + TsE6[sE6p+2][i_d] + " --> " + SNT6_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//
		/*	for (int i1=0 ; i1<SNPT6_segment.length ; i1++)
		{
		System.out.print(" SNSeg6\t" + SNPT6_segment[i1]+ "\t" + "VNRSeg\t" + VNRT6_segment[i1]); // this prints the SN formulated segment.				 	
		System.out.println();		
		} 
		*/
		//System.out.println("----------------------------------------------");

		//***********************************  SN to VNR comparisons to check capacity    *****************************

		/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
		* are sufficient to accommodate the VNR demands:
		* 
		* 		- First we compare SN metric to the demanded VNR metric. 
		* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
		* 		  the maximum capacity for that node or edge.
		*/	

		int if_count=0;
		
		for (int cpu=0 ; cpu < P_6_Nodes ; cpu++)
		if ((SNT6_segment_nodes[cpu]-VNRT6_segment_nodes[cpu]) >= 0)
			if_count++;	
		
		for (int ibw = 0 ; ibw < SNT6_segment_edges_BW.length  ; ibw++)	
		if (SNT6_segment_edges_BW[ibw] -  VNRT6_segment_edges_BW[ibw] >= 0) 
			if_count++;	
		
		for (int i_d = 0 ; i_d < SNT6_segment_edges_Delay.length  ; i_d++)	
			if (VNRT6_segment_edges_Delay[i_d] - SNT6_segment_edges_Delay[i_d]  >= 0) 
				if_count++;
					       
		// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
							  
		// To print the values of the SN after updating the SN given a successful embedding
		//System.out.println("---------------------------------------------------------------------------------------------------------");	
		//System.out.println("-------------------------------");

		if(if_count>=36)
		{
		
		// Saving the number of the selected SN topology used for embedding
			
		SN_Selected_Path[VNR][49]=6;  // this is the number of nodes in the SN topology
		SN_Selected_Path[VNR][48]=6;  // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][47]=P6; // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][46]=15;  // This is the number of the SN edges in the selected topology. It will be used for online updating.
			
		// Updating the SN nodes after the embedding
			
		for (int i_cpu = 0 ; i_cpu < P_6_Nodes ; i_cpu++)
		{
			SN_Nodes_CPU_Cons[TsE6[Tjp6[P6]][i_cpu]] = SNT6_segment_nodes[i_cpu] - VNRT6_segment_nodes[i_cpu];
				System.out.println("CPU_b\t("  + TsE6[Tjp6[P6]][i_cpu] + ")\t" + (int)SNT6_segment_nodes[i_cpu] + "\t" +
		    	                   "CPU_a\t"   + (int)SN_Nodes_CPU_Cons[TsE6[Tjp6[P6]][i_cpu]] + "\t" +
				                   "demanded_VNRcpu\t" + (int)VNRT6_segment_nodes[i_cpu]);	

		SN_Selected_Path[VNR][i_cpu]	 = TsE6[Tjp6[P6]][i_cpu];  // to save the SN path nodes that were selected to embed a VNR.

		}// end of i_cpu for loop

		// Updating the SN edges after the embedding
		
		for (int ibw = 0 ; ibw < SNT6_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE6[sE6p+1][ibw]==-1 && TsE6[sE6p+2][ibw]==-1)
			{
				continue;
			}else
			if (TsE6[sE6p+1][ibw]!=-1 && TsE6[sE6p+2][ibw]!=-1)
			{		
				SN_BWmatrix[TsE6[sE6p+1][ibw]][TsE6[sE6p+2][ibw]] = SNT6_segment_edges_BW[ibw] -
						                                                  VNRT6_segment_edges_BW[ibw];		
			
			System.out.println(" SN_edge_BW_b\t----" + TsE6[sE6p+1][ibw] + "--> " + TsE6[sE6p+2][ibw] + " --> " + 
					(int)SNT6_segment_edges_BW[ibw]+"\t"+"SN_edge_BW_a\t----"+ (int)SN_BWmatrix[TsE6[sE6p+1][ibw]][TsE6[sE6p+2][ibw]]+
			                     "\t" + "Demanded_VNRbw\t" + (int)VNRT6_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop
		    		 
		//-------------------
		//-------Calculating the new CPU utilization.
		//------------------					  

		for (int i_cpu = 0 ; i_cpu < P_6_Nodes ; i_cpu++)
		{	
		SN_Nodes_CPU_Cur_Util     [TsE6[Tjp6[P6]][i_cpu]] = 
		((SN_Nodes_MAX_CPU_Cap    [TsE6[Tjp6[P6]][i_cpu]] - 
		SN_Nodes_CPU_Cons         [TsE6[Tjp6[P6]][i_cpu]])/
		SN_Nodes_MAX_CPU_Cap      [TsE6[Tjp6[P6]][i_cpu]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
					
		SN_Nodes_Current_PCons    [TsE6[Tjp6[P6]][i_cpu]] = 
		SN_Nodes_MIN_Power_Cap    [TsE6[Tjp6[P6]][i_cpu]] 
		+((SN_Nodes_MAX_Power_Cap [TsE6[Tjp6[P6]][i_cpu]] 
		-  SN_Nodes_MIN_Power_Cap [TsE6[Tjp6[P6]][i_cpu]]) 
		*  SN_Nodes_CPU_Cur_Util  [TsE6[Tjp6[P6]][i_cpu]]); // this updates the SN current power consumption		

		/*
		System.out.println("-----------------------------------------------");
		System.out.println(" SN_Nodes_CPU_Cur_Util----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cur_Util     [TsE6[Tjp6[P6]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_CPU_Cap-----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_CPU_Cap      [TsE6[Tjp6[P6]][i_cpu]]);
		System.out.println(" SN_Nodes_CPU_Cons--------\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cons         [TsE6[Tjp6[P6]][i_cpu]]);	
		System.out.println(" SN_Nodes_Current_PCons---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_Current_PCons    [TsE6[Tjp6[P6]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_Power_Cap    [TsE6[Tjp6[P6]][i_cpu]]);
		System.out.println(" SN_Nodes_MIN_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MIN_Power_Cap [TsE6[Tjp6[P6]][i_cpu]]);
		*/
		} // End of for (int i_cpu = 0 ; i_cpu < P_6_Nodes ; i_cpu++)		

		/*--------------------------------------------------------------------------------------------
		* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
		* -----------------------------------------------------------------------------------------*/
							  	 
		//System.out.println("SN Nodes of the Selected Path "); 
		for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
		{
		SN_Selected_path_Nodes[i_path] = TsE6[Tjp6[P6]][i_path];
		/* System.out.println("-----------------------------------------------");
		System.out.println(" successfulvnr----" + successfulvnr); 
		System.out.println(" SN_P6_nodes------" + SN_P6_nodes[SNP6_sorted_CPUsum_paths[i]][i_path]); 
		System.out.println(" Path Number------" + SNP6_sorted_CPUsum_paths[i]);
		System.out.println("-----------------------------------------------");
		*/ 
		}
				  
		Succ_Used_Paths[successfulvnr] = Tjp6[P6];   // This saves the path number of the successful embedding
		Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
				  
		//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
		//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
		//System.out.println();
				  
		successfulvnr++;
				  
		double Selected_path_PC6 = 0 ; // includes the total power consumption in the selected path.	

		for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
		Selected_path_PC6 += SN_Nodes_Current_PCons[TsE6[Tjp6[P6]][i_update]]; // this to calculate the total Power Consumption in this selected path

		SNPC[VNR] = Selected_path_PC6; // This save the power consumption according to the selected path.

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("-  End of Successful Embedding: 6-Nodes and links are updated  after VNR embedding  -");
		System.out.println("-----------------------------------------------------------------------------------");				    	
		System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					    	
		} else 
				{
					System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
					System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					System.out.println("Go to next VNR");
			        System.out.println();
					
			        for (int cpu=0 ; cpu < P_6_Nodes ; cpu++)
			        {
			        	D_VNRsNodes_CPU[VNR][cpu]  = 0; // VNR nodes CPU
			        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT6_segment[cpu]);
			        	
			        	//SN_Selected_Path[VNR][cpu]	 = SN_P6_nodes[SNP6_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
			        
			        }// End of cpu for loop

			        for (int ibw = 0 ; ibw < (P_6_Nodes-1) ; ibw++)	
			        {
			        	D_VNRsEdges_BW[VNR][ibw]  = 0; // VNR edges BW
			        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT6_segment[ibw + (P_6_Nodes)]);
			        }// End of ibw for loop
		            
			        
			        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			    			
			}// End of -if- statement 	
		
		endTime = System.currentTimeMillis();
		totalTime[VNR] = endTime - startTime;
		
	


	

} break; // End of Case 6


//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------  End Of SN paths type 6 Embedding   ----------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
case 5: 
{

	

	int P5 = (0 + (int)(Math.random() * ((Tjp5.length-1)  + 1))); // Randomly selecting the SN topology to try embed on it.

	int P_5_Nodes = 5;	
	
	//******************************************************* Start Comparisons Phase **************************************************  
	//******************************************* First create the paths segments for SN and VNRs **************************************
	//************************************************              VNR Segment                 ***************************************
		
	/* Here we define the VNR segment for each demanded path, that includes
	* 	- Power Consumption and CPU for VNR nodes, and
	*  - BW and Delay for VNR edges.
	*/

	System.out.println("P5--\t"+P5+"----\t"+Tjp5[P5]);

	int sE5p=Tjp5[P5];
	
	for (int p=0 ; p<3 ; p++)
	{
		for(int j=0 ; j<10 ; j++)	
			System.out.print(TsE5[sE5p+p][j]+",");			
			
		System.out.println();		
	}	
		
			
			
		double[] VNRT5_segment_nodes = new double[P_5_Nodes];

		for (int cpu=0 ; cpu < P_5_Nodes ; cpu++)
		{
			VNRT5_segment_nodes[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
			System.out.println(" VNR_CPU---" + TvE5[Tjp5[P5]][cpu] + "------> " + VNRT5_segment_nodes[cpu]);
		}// End of cpu for loop

		double[] VNRT5_segment_edges_BW = new double[10];
		
		for (int ibw = 0 ; ibw < VNRT5_segment_edges_BW.length  ; ibw++)	
		{
			if (TvE5[sE5p+1][ibw]==-1 && TvE5[sE5p+2][ibw]==-1)
			{
				VNRT5_segment_edges_BW[ibw]=0;
				System.out.println(" VNR_BW----" + TvE5[sE5p+1][ibw] + "--> " + TvE5[sE5p+2][ibw] + " --> " + VNRT5_segment_edges_BW[ibw]);

			}else
			{
				VNRT5_segment_edges_BW[ibw]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW		
			
				System.out.println(" VNR_BW----" + TvE5[sE5p+1][ibw] + "--> " + TvE5[sE5p+2][ibw] + " --> " + VNRT5_segment_edges_BW[ibw]);
			
			}
		}// End of ibw for loop

		double[] VNRT5_segment_edges_Delay = new double[10];
		
		for (int i_d = 0 ; i_d < VNRT5_segment_edges_Delay.length ; i_d++)
		{
				if (TvE5[sE5p+1][i_d]==-1 && TvE5[sE5p+2][i_d]==-1)
				{
					VNRT5_segment_edges_Delay[i_d]=0;
					System.out.println(" VNR_Delay----" + TvE5[sE5p+1][i_d] + "--> " + TvE5[sE5p+2][i_d] + " --> " + VNRT5_segment_edges_Delay[i_d]);

				}else
				{
					VNRT5_segment_edges_Delay[i_d]   = D_VNRsEdges_Delay[VNR][i_d] ; // VNR edges BW			
				
					System.out.println(" VNR_Delay----" + TvE5[sE5p+1][i_d] + "--> " + TvE5[sE5p+2][i_d] + " --> " + VNRT5_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

		//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

		//********************************************* End of VNR Segment initialization **********************************

		//******************************************************* SN Segment  ********************************************

		/* Here we define the SN segment for each path, that includes
		* 	- Power Consumption and CPU for SN nodes, and
		*  - BW and Delay for SN edges
		*/

	double[] SNT5_segment_nodes = new double[P_5_Nodes];

		for (int cpu=0 ; cpu < P_5_Nodes ; cpu++)
		{
			SNT5_segment_nodes[cpu] = SN_Nodes_CPU_Cons[TsE5[Tjp5[P5]][cpu]]; // VNR nodes CPU
			System.out.println(" SN_CPU---" + TsE5[Tjp5[P5]][cpu] + "----------> " + (int)SNT5_segment_nodes[cpu]);
		}// End of cpu for loop	
		
	double[] SNT5_segment_edges_BW = new double[10];
		
		for (int ibw = 0 ; ibw < SNT5_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE5[sE5p+1][ibw]==-1 && TsE5[sE5p+2][ibw]==-1)
			{
				SNT5_segment_edges_BW[ibw]=0;
				System.out.println(" SN_edge_BW----" + TsE5[sE5p+1][ibw] + "--> " + TsE5[sE5p+2][ibw] + " --> " + (int)SNT5_segment_edges_BW[ibw]);

			}else
			{
				SNT5_segment_edges_BW[ibw]   = SN_BWmatrix[TsE5[sE5p+1][ibw]][TsE5[sE5p+2][ibw]]; // VNR edges BW
			
				System.out.println(" SN_edge_BW----" + TsE5[sE5p+1][ibw] + "--> " + TsE5[sE5p+2][ibw] + " --> " + (int)SNT5_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop

		double[] SNT5_segment_edges_Delay = new double[10];
		
		for (int i_d = 0 ; i_d < SNT5_segment_edges_Delay.length ; i_d++)
		{
				if (TsE5[sE5p+1][i_d]==-1 && TsE5[sE5p+2][i_d]==-1)
				{
					SNT5_segment_edges_Delay[i_d]=0;
					System.out.println(" SN_edge_Delay----" + TsE5[sE5p+1][i_d] + "--> " + TsE5[sE5p+2][i_d] + " --> " + SNT5_segment_edges_Delay[i_d]);

				}else
				{
					SNT5_segment_edges_Delay[i_d]   = SN_DelayMatrix[TsE5[sE5p+1][i_d]][TsE5[sE5p+2][i_d]]; // VNR edges BW
				
					System.out.println(" SN_edge_Delay----" + TsE5[sE5p+1][i_d] + "--> " + TsE5[sE5p+2][i_d] + " --> " + SNT5_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//
		/*	for (int i1=0 ; i1<SNPT5_segment.length ; i1++)
		{
		System.out.print(" SNSeg5\t" + SNPT5_segment[i1]+ "\t" + "VNRSeg\t" + VNRT5_segment[i1]); // this prints the SN formulated segment.				 	
		System.out.println();		
		} 
		*/
		//System.out.println("----------------------------------------------");

		//***********************************  SN to VNR comparisons to check capacity    *****************************

		/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
		* are sufficient to accommodate the VNR demands:
		* 
		* 		- First we compare SN metric to the demanded VNR metric. 
		* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
		* 		  the maximum capacity for that node or edge.
		*/	

		int if_count=0;
		
		for (int cpu=0 ; cpu < P_5_Nodes ; cpu++)
		if ((SNT5_segment_nodes[cpu]-VNRT5_segment_nodes[cpu]) >= 0)
			if_count++;	
		
		for (int ibw = 0 ; ibw < SNT5_segment_edges_BW.length  ; ibw++)	
		if (SNT5_segment_edges_BW[ibw] -  VNRT5_segment_edges_BW[ibw] >= 0) 
			if_count++;	
		
		for (int i_d = 0 ; i_d < SNT5_segment_edges_Delay.length  ; i_d++)	
			if (VNRT5_segment_edges_Delay[i_d] - SNT5_segment_edges_Delay[i_d]  >= 0) 
				if_count++;
					       
		// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
							  
		// To print the values of the SN after updating the SN given a successful embedding
		//System.out.println("---------------------------------------------------------------------------------------------------------");	
		//System.out.println("  Selected path   " + sorted_CPUsum_paths10[i]);
		//System.out.println("-------------------------------");

		if(if_count>=25)
		{
		
		// Saving the number of the selected SN topology used for embedding
			
		SN_Selected_Path[VNR][49]=5;  // this is the number of nodes in the SN topology
		SN_Selected_Path[VNR][48]=5;  // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][47]=P5; // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][46]=10;  // This is the number of the SN edges in the selected topology. It will be used for online updating.
			
		// Updating the SN nodes after the embedding
			
		for (int i_cpu = 0 ; i_cpu < P_5_Nodes ; i_cpu++)
		{
			SN_Nodes_CPU_Cons[TsE5[Tjp5[P5]][i_cpu]] = SNT5_segment_nodes[i_cpu] - VNRT5_segment_nodes[i_cpu];
				System.out.println("CPU_b\t("  + TsE5[Tjp5[P5]][i_cpu] + ")\t" + (int)SNT5_segment_nodes[i_cpu] + "\t" +
		    	                   "CPU_a\t"   + (int)SN_Nodes_CPU_Cons[TsE5[Tjp5[P5]][i_cpu]] + "\t" +
				                   "demanded_VNRcpu\t" + (int)VNRT5_segment_nodes[i_cpu]);	

		SN_Selected_Path[VNR][i_cpu]	 = TsE5[Tjp5[P5]][i_cpu];  // to save the SN path nodes that were selected to embed a VNR.

		}// end of i_cpu for loop

		// Updating the SN edges after the embedding
		
		for (int ibw = 0 ; ibw < SNT5_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE5[sE5p+1][ibw]==-1 && TsE5[sE5p+2][ibw]==-1)
			{
				continue;
			}else
			if (TsE5[sE5p+1][ibw]!=-1 && TsE5[sE5p+2][ibw]!=-1)
			{		
				SN_BWmatrix[TsE5[sE5p+1][ibw]][TsE5[sE5p+2][ibw]] = SNT5_segment_edges_BW[ibw] -
						                                                  VNRT5_segment_edges_BW[ibw];		
			
			System.out.println(" SN_edge_BW_b\t----" + TsE5[sE5p+1][ibw] + "--> " + TsE5[sE5p+2][ibw] + " --> " + 
					(int)SNT5_segment_edges_BW[ibw]+"\t"+"SN_edge_BW_a\t----"+ (int)SN_BWmatrix[TsE5[sE5p+1][ibw]][TsE5[sE5p+2][ibw]]+
			                     "\t" + "Demanded_VNRbw\t" + (int)VNRT5_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop
		    		 
		//-------------------
		//-------Calculating the new CPU utilization.
		//------------------					  

		for (int i_cpu = 0 ; i_cpu < P_5_Nodes ; i_cpu++)
		{	
		SN_Nodes_CPU_Cur_Util     [TsE5[Tjp5[P5]][i_cpu]] = 
		((SN_Nodes_MAX_CPU_Cap    [TsE5[Tjp5[P5]][i_cpu]] - 
		SN_Nodes_CPU_Cons         [TsE5[Tjp5[P5]][i_cpu]])/
		SN_Nodes_MAX_CPU_Cap      [TsE5[Tjp5[P5]][i_cpu]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
					
		SN_Nodes_Current_PCons    [TsE5[Tjp5[P5]][i_cpu]] = 
		SN_Nodes_MIN_Power_Cap    [TsE5[Tjp5[P5]][i_cpu]] 
		+((SN_Nodes_MAX_Power_Cap [TsE5[Tjp5[P5]][i_cpu]] 
		-  SN_Nodes_MIN_Power_Cap [TsE5[Tjp5[P5]][i_cpu]]) 
		*  SN_Nodes_CPU_Cur_Util  [TsE5[Tjp5[P5]][i_cpu]]); // this updates the SN current power consumption		

		/*
		System.out.println("-----------------------------------------------");
		System.out.println(" SN_Nodes_CPU_Cur_Util----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cur_Util     [TsE5[Tjp5[P5]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_CPU_Cap-----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_CPU_Cap      [TsE5[Tjp5[P5]][i_cpu]]);
		System.out.println(" SN_Nodes_CPU_Cons--------\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cons         [TsE5[Tjp5[P5]][i_cpu]]);	
		System.out.println(" SN_Nodes_Current_PCons---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_Current_PCons    [TsE5[Tjp5[P5]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_Power_Cap    [TsE5[Tjp5[P5]][i_cpu]]);
		System.out.println(" SN_Nodes_MIN_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MIN_Power_Cap [TsE5[Tjp5[P5]][i_cpu]]);
		*/
		} // End of for (int i_cpu = 0 ; i_cpu < P_5_Nodes ; i_cpu++)		

		/*--------------------------------------------------------------------------------------------
		* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
		* -----------------------------------------------------------------------------------------*/
							  	 
		//System.out.println("SN Nodes of the Selected Path "); 
		for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
		{
		SN_Selected_path_Nodes[i_path] = TsE5[Tjp5[P5]][i_path];
		/* System.out.println("-----------------------------------------------");
		System.out.println(" successfulvnr----" + successfulvnr); 
		System.out.println(" SN_P5_nodes------" + SN_P5_nodes[SNP5_sorted_CPUsum_paths[i]][i_path]); 
		System.out.println(" Path Number------" + SNP5_sorted_CPUsum_paths[i]);
		System.out.println("-----------------------------------------------");
		*/ 
		}
				  
		Succ_Used_Paths[successfulvnr] = Tjp5[P5];   // This saves the path number of the successful embedding
		Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
				  
		//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
		//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
		//System.out.println();
				  
		successfulvnr++;
				  
		double Selected_path_PC5 = 0 ; // includes the total power consumption in the selected path.	

		for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
		Selected_path_PC5 += SN_Nodes_Current_PCons[TsE5[Tjp5[P5]][i_update]]; // this to calculate the total Power Consumption in this selected path

		SNPC[VNR] = Selected_path_PC5; // This save the power consumption according to the selected path.

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("-  End of Successful Embedding: 5-Nodes and links are updated  after VNR embedding  -");
		System.out.println("-----------------------------------------------------------------------------------");				    	
		System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					    	
		} else 
				{
					System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
					System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					System.out.println("Go to next VNR");
			        System.out.println();
					
			        for (int cpu=0 ; cpu < P_5_Nodes ; cpu++)
			        {
			        	D_VNRsNodes_CPU[VNR][cpu]  = 0; // VNR nodes CPU
			        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT5_segment[cpu]);
			        	
			        	//SN_Selected_Path[VNR][cpu]	 = SN_P5_nodes[SNP5_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
			        
			        }// End of cpu for loop

			        for (int ibw = 0 ; ibw < (P_5_Nodes-1) ; ibw++)	
			        {
			        	D_VNRsEdges_BW[VNR][ibw]  = 0; // VNR edges BW
			        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT5_segment[ibw + (P_5_Nodes)]);
			        }// End of ibw for loop
		            
			        
			        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			    			
			}// End of -if- statement 	
		
		endTime = System.currentTimeMillis();
		totalTime[VNR] = endTime - startTime;
		
	


	
} break; // End of Case 5


//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------  End Of SN paths type 5 Embedding   ----------------------------------------
//-----------------------------------------------------------------------------------------------------------------------


case 4: 
{
	int P4 = (0 + (int)(Math.random() * ((Tjp4.length-1)  + 1))); // Randomly selecting the SN topology to try embed on it.

	int P_4_Nodes = 4;	
	
	//******************************************************* Start Comparisons Phase **************************************************  
	//******************************************* First create the paths segments for SN and VNRs **************************************
	//************************************************              VNR Segment                 ***************************************
		
	/* Here we define the VNR segment for each demanded path, that includes
	* 	- Power Consumption and CPU for VNR nodes, and
	*  - BW and Delay for VNR edges.
	*/

	System.out.println("P4--\t"+P4+"----\t"+Tjp4[P4]);

	int sE4p=Tjp4[P4];
	
	for (int p=0 ; p<3 ; p++)
	{
		for(int j=0 ; j<6 ; j++)	
			System.out.print(TsE4[sE4p+p][j]+",");			
			
		System.out.println();		
	}	
		
			
			
		double[] VNRT4_segment_nodes = new double[P_4_Nodes];

		for (int cpu=0 ; cpu < P_4_Nodes ; cpu++)
		{
			VNRT4_segment_nodes[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
			System.out.println(" VNR_CPU---" + TvE4[Tjp4[P4]][cpu] + "------> " + VNRT4_segment_nodes[cpu]);
		}// End of cpu for loop

		double[] VNRT4_segment_edges_BW = new double[6];
		
		for (int ibw = 0 ; ibw < VNRT4_segment_edges_BW.length  ; ibw++)	
		{
			if (TvE4[sE4p+1][ibw]==-1 && TvE4[sE4p+2][ibw]==-1)
			{
				VNRT4_segment_edges_BW[ibw]=0;
				System.out.println(" VNR_BW----" + TvE4[sE4p+1][ibw] + "--> " + TvE4[sE4p+2][ibw] + " --> " + VNRT4_segment_edges_BW[ibw]);

			}else
			{
				VNRT4_segment_edges_BW[ibw]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW		
			
				System.out.println(" VNR_BW----" + TvE4[sE4p+1][ibw] + "--> " + TvE4[sE4p+2][ibw] + " --> " + VNRT4_segment_edges_BW[ibw]);
			
			}
		}// End of ibw for loop

		double[] VNRT4_segment_edges_Delay = new double[6];
		
		for (int i_d = 0 ; i_d < VNRT4_segment_edges_Delay.length ; i_d++)
		{
				if (TvE4[sE4p+1][i_d]==-1 && TvE4[sE4p+2][i_d]==-1)
				{
					VNRT4_segment_edges_Delay[i_d]=0;
					System.out.println(" VNR_Delay----" + TvE4[sE4p+1][i_d] + "--> " + TvE4[sE4p+2][i_d] + " --> " + VNRT4_segment_edges_Delay[i_d]);

				}else
				{
					VNRT4_segment_edges_Delay[i_d]   = D_VNRsEdges_Delay[VNR][i_d]; // VNR edges BW			
				
					System.out.println(" VNR_Delay----" + TvE4[sE4p+1][i_d] + "--> " + TvE4[sE4p+2][i_d] + " --> " + VNRT4_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

		//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

		//********************************************* End of VNR Segment initialization **********************************

		//******************************************************* SN Segment  ********************************************

		/* Here we define the SN segment for each path, that includes
		* 	- Power Consumption and CPU for SN nodes, and
		*  - BW and Delay for SN edges
		*/

	double[] SNT4_segment_nodes = new double[P_4_Nodes];

		for (int cpu=0 ; cpu < P_4_Nodes ; cpu++)
		{
			SNT4_segment_nodes[cpu] = SN_Nodes_CPU_Cons[TsE4[Tjp4[P4]][cpu]]; // VNR nodes CPU
			System.out.println(" SN_CPU---" + TsE4[Tjp4[P4]][cpu] + "----------> " + (int)SNT4_segment_nodes[cpu]);
		}// End of cpu for loop	
		
	double[] SNT4_segment_edges_BW = new double[6];
		
		for (int ibw = 0 ; ibw < SNT4_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE4[sE4p+1][ibw]==-1 && TsE4[sE4p+2][ibw]==-1)
			{
				SNT4_segment_edges_BW[ibw]=0;
				System.out.println(" SN_edge_BW----" + TsE4[sE4p+1][ibw] + "--> " + TsE4[sE4p+2][ibw] + " --> " + (int)SNT4_segment_edges_BW[ibw]);

			}else
			{
				SNT4_segment_edges_BW[ibw]   = SN_BWmatrix[TsE4[sE4p+1][ibw]][TsE4[sE4p+2][ibw]]; // VNR edges BW
			
				System.out.println(" SN_edge_BW----" + TsE4[sE4p+1][ibw] + "--> " + TsE4[sE4p+2][ibw] + " --> " + (int)SNT4_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop

		double[] SNT4_segment_edges_Delay = new double[6];
		
		for (int i_d = 0 ; i_d < SNT4_segment_edges_Delay.length ; i_d++)
		{
				if (TsE4[sE4p+1][i_d]==-1 && TsE4[sE4p+2][i_d]==-1)
				{
					SNT4_segment_edges_Delay[i_d]=0;
					System.out.println(" SN_edge_Delay----" + TsE4[sE4p+1][i_d] + "--> " + TsE4[sE4p+2][i_d] + " --> " + SNT4_segment_edges_Delay[i_d]);

				}else
				{
					SNT4_segment_edges_Delay[i_d]   = SN_DelayMatrix[TsE4[sE4p+1][i_d]][TsE4[sE4p+2][i_d]]; // VNR edges BW
				
					System.out.println(" SN_edge_Delay----" + TsE4[sE4p+1][i_d] + "--> " + TsE4[sE4p+2][i_d] + " --> " + SNT4_segment_edges_Delay[i_d]);
				
				}
				
		}// End of i_d for loop

		//
		/*	for (int i1=0 ; i1<SNPT4_segment.length ; i1++)
		{
		System.out.print(" SNSeg4\t" + SNPT4_segment[i1]+ "\t" + "VNRSeg\t" + VNRT4_segment[i1]); // this prints the SN formulated segment.				 	
		System.out.println();		
		} 
		*/
		//System.out.println("----------------------------------------------");

		//***********************************  SN to VNR comparisons to check capacity    *****************************

		/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
		* are sufficient to accommodate the VNR demands:
		* 
		* 		- First we compare SN metric to the demanded VNR metric. 
		* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
		* 		  the maximum capacity for that node or edge.
		*/	

		int if_count=0;
		
		for (int cpu=0 ; cpu < P_4_Nodes ; cpu++)
		if ((SNT4_segment_nodes[cpu]-VNRT4_segment_nodes[cpu]) >= 0)
			if_count++;	
		
		for (int ibw = 0 ; ibw < SNT4_segment_edges_BW.length  ; ibw++)	
		if (SNT4_segment_edges_BW[ibw] -  VNRT4_segment_edges_BW[ibw] >= 0) 
			if_count++;	
		
		for (int i_d = 0 ; i_d < SNT4_segment_edges_Delay.length  ; i_d++)	
			if (VNRT4_segment_edges_Delay[i_d] - SNT4_segment_edges_Delay[i_d]  >= 0) 
				if_count++;
					       
		// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
							  
		// To print the values of the SN after updating the SN given a successful embedding
		//System.out.println("---------------------------------------------------------------------------------------------------------");	
		//System.out.println("  Selected path   " + sorted_CPUsum_paths10[i]);
		//System.out.println("-------------------------------");

		if(if_count>=16)
		{
		
		// Saving the number of the selected SN topology used for embedding
			
		SN_Selected_Path[VNR][49]=4;  // this is the number of nodes in the SN topology
		SN_Selected_Path[VNR][48]=4;  // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][47]=P4; // This is the number of the used SN topology. It will be used for online updating.
		SN_Selected_Path[VNR][46]=6;  // This is the number of the SN edges in the selected topology. It will be used for online updating.
			
		// Updating the SN nodes after the embedding
			
		for (int i_cpu = 0 ; i_cpu < P_4_Nodes ; i_cpu++)
		{
			SN_Nodes_CPU_Cons[TsE4[Tjp4[P4]][i_cpu]] = SNT4_segment_nodes[i_cpu] - VNRT4_segment_nodes[i_cpu];
				System.out.println("CPU_b\t("  + TsE4[Tjp4[P4]][i_cpu] + ")\t" + (int)SNT4_segment_nodes[i_cpu] + "\t" +
		    	                   "CPU_a\t"   + (int)SN_Nodes_CPU_Cons[TsE4[Tjp4[P4]][i_cpu]] + "\t" +
				                   "demanded_VNRcpu\t" + (int)VNRT4_segment_nodes[i_cpu]);	

		SN_Selected_Path[VNR][i_cpu]	 = TsE4[Tjp4[P4]][i_cpu];  // to save the SN path nodes that were selected to embed a VNR.

		}// end of i_cpu for loop

		// Updating the SN edges after the embedding
		
		for (int ibw = 0 ; ibw < SNT4_segment_edges_BW.length  ; ibw++)	
		{
			if (TsE4[sE4p+1][ibw]==-1 && TsE4[sE4p+2][ibw]==-1)
			{
				continue;
			}else
			if (TsE4[sE4p+1][ibw]!=-1 && TsE4[sE4p+2][ibw]!=-1)
			{		
				SN_BWmatrix[TsE4[sE4p+1][ibw]][TsE4[sE4p+2][ibw]] = SNT4_segment_edges_BW[ibw] -
						                                                  VNRT4_segment_edges_BW[ibw];		
			
			System.out.println(" SN_edge_BW_b\t----" + TsE4[sE4p+1][ibw] + "--> " + TsE4[sE4p+2][ibw] + " --> " + 
					(int)SNT4_segment_edges_BW[ibw]+"\t"+"SN_edge_BW_a\t----"+ (int)SN_BWmatrix[TsE4[sE4p+1][ibw]][TsE4[sE4p+2][ibw]]+
			                     "\t" + "Demanded_VNRbw\t" + (int)VNRT4_segment_edges_BW[ibw]);
			}
		}// End of ibw for loop
		    		 
		//-------------------
		//-------Calculating the new CPU utilization.
		//------------------					  

		for (int i_cpu = 0 ; i_cpu < P_4_Nodes ; i_cpu++)
		{	
		SN_Nodes_CPU_Cur_Util     [TsE4[Tjp4[P4]][i_cpu]] = 
		((SN_Nodes_MAX_CPU_Cap    [TsE4[Tjp4[P4]][i_cpu]] - 
		SN_Nodes_CPU_Cons         [TsE4[Tjp4[P4]][i_cpu]])/
		SN_Nodes_MAX_CPU_Cap      [TsE4[Tjp4[P4]][i_cpu]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
					
		SN_Nodes_Current_PCons    [TsE4[Tjp4[P4]][i_cpu]] = 
		SN_Nodes_MIN_Power_Cap    [TsE4[Tjp4[P4]][i_cpu]] 
		+((SN_Nodes_MAX_Power_Cap [TsE4[Tjp4[P4]][i_cpu]] 
		-  SN_Nodes_MIN_Power_Cap [TsE4[Tjp4[P4]][i_cpu]]) 
		*  SN_Nodes_CPU_Cur_Util  [TsE4[Tjp4[P4]][i_cpu]]); // this updates the SN current power consumption		

		/*
		System.out.println("-----------------------------------------------");
		System.out.println(" SN_Nodes_CPU_Cur_Util----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cur_Util     [TsE4[Tjp4[P4]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_CPU_Cap-----\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_CPU_Cap      [TsE4[Tjp4[P4]][i_cpu]]);
		System.out.println(" SN_Nodes_CPU_Cons--------\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_CPU_Cons         [TsE4[Tjp4[P4]][i_cpu]]);	
		System.out.println(" SN_Nodes_Current_PCons---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_Current_PCons    [TsE4[Tjp4[P4]][i_cpu]]);
		System.out.println(" SN_Nodes_MAX_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MAX_Power_Cap    [TsE4[Tjp4[P4]][i_cpu]]);
		System.out.println(" SN_Nodes_MIN_Power_Cap---\t(" + i_cpu + ")\t" 
		                   + SN_Nodes_MIN_Power_Cap [TsE4[Tjp4[P4]][i_cpu]]);
		*/
		} // End of for (int i_cpu = 0 ; i_cpu < P_4_Nodes ; i_cpu++)		

		/*--------------------------------------------------------------------------------------------
		* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
		* -----------------------------------------------------------------------------------------*/
							  	 
		//System.out.println("SN Nodes of the Selected Path "); 
		for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
		{
		SN_Selected_path_Nodes[i_path] = TsE4[Tjp4[P4]][i_path];
		/* System.out.println("-----------------------------------------------");
		System.out.println(" successfulvnr----" + successfulvnr); 
		System.out.println(" SN_P4_nodes------" + SN_P4_nodes[SNP5_sorted_CPUsum_paths[i]][i_path]); 
		System.out.println(" Path Number------" + SNP4_sorted_CPUsum_paths[i]);
		System.out.println("-----------------------------------------------");
		*/ 
		}
				  
		Succ_Used_Paths[successfulvnr] = Tjp4[P4];   // This saves the path number of the successful embedding
		Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
				  
		//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
		//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
		//System.out.println();
				  
		successfulvnr++;
				  
		double Selected_path_PC4 = 0 ; // includes the total power consumption in the selected path.	

		for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
		Selected_path_PC4 += SN_Nodes_Current_PCons[TsE4[Tjp4[P4]][i_update]]; // this to calculate the total Power Consumption in this selected path

		SNPC[VNR] = Selected_path_PC4; // This save the power consumption according to the selected path.

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("-  End of Successful Embedding: 4-Nodes and links are updated  after VNR embedding  -");
		System.out.println("-----------------------------------------------------------------------------------");				    	
		System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					    	
		} else 
				{
					System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
					System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
					System.out.println("Go to next VNR");
			        System.out.println();
					
			        for (int cpu=0 ; cpu < P_4_Nodes ; cpu++)
			        {
			        	D_VNRsNodes_CPU[VNR][cpu] = 0; // VNR nodes CPU
			        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT4_segment[cpu]);
			        	
			        	//SN_Selected_Path[VNR][cpu]	 = SN_P4_nodes[SNP4_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
			        
			        }// End of cpu for loop

			        for (int ibw = 0 ; ibw < (P_4_Nodes-1) ; ibw++)	
			        {
			        	D_VNRsEdges_BW[VNR][ibw] = 0; // VNR edges BW
			        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT4_segment[ibw + (P_4_Nodes)]);
			        }// End of ibw for loop
		            
			        
			        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			    			
			}// End of -if- statement 	
		
		endTime = System.currentTimeMillis();
		totalTime[VNR] = endTime - startTime;
		
	
} break; // End of Case 4

//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------  End Of SN paths type 4 Embedding   ----------------------------------------
//-----------------------------------------------------------------------------------------------------------------------

case 3: 
{

int n=(int) (0  + (Math.random() * ((2  - 0)  + 1))); // this to select the type of 3 nodes graph randomly (0-line, 1-fish bone, 2-ring)
	
switch(n)
{
case 0:		
{
//---------------------------------------------  Paths of Type 3 nodes  --------------------------------------------------		
//------------------------------------------Initialize the Power and CPU matrices ---------------------------------------- 
//------------------------- Initialize Power and CPU matrices for the SN nodes in paths of type 3 ------------------------

double tempCPUrsum = 0;                   // this is a temporary store to save the value of the sorted power value.
int tempsorted     = 0;                   // this is temporary store to save the value of the index for the sorted power value.
int P_3_Paths     = SN_P3_nodes.length; // number of paths that have three nodes
int P_3_Nodes     = 3;                  // number of nodes per each path

double[][] SNPaths_3_Max_Power       = new double[P_3_Paths][P_3_Nodes];    // this is a matrix that includes the Maximum power capacity per each SN node
double[][] SNPaths_3_Min_Power       = new double[P_3_Paths][P_3_Nodes];    // this is a matrix that includes the Minimum power capacity per each SN node
double[][] SNPaths_3_Consmed_Power   = new double[P_3_Paths][P_3_Nodes];    // this is a matrix that includes the Maximum power capacity per each SN node

double[][] SNPaths_3_Max_CPU         = new double[P_3_Paths][P_3_Nodes];    // this is a matrix that includes the current CPU per each SN node	
double[][] SNPaths_3_CPU             = new double[P_3_Paths][P_3_Nodes];    // this is a matrix that includes the current CPU per each SN node
double[][] SNPaths_3_CPU_Utilization = new double[P_3_Paths][P_3_Nodes]; // this is a matrix to save the CPU utilization for each SN node in this path.			
double[]   SNP3_cpu_utilization_sum  = new double[P_3_Paths];                // SORTED matrix to save the sum of the CPU values for the Path nodes 
double[]   SNP3_Consmed_Power_sum  = new double[P_3_Paths];                // SORTED matrix to save the sum of the CPU values for the Path nodes 
double[]   SNP3_CPU_util_not_sorted  = new double[P_3_Paths];                // NOT SORTED matrix to save the sum of the power values for the Path nodes
int[]      SNP3_sorted_CPUsum_paths  = sorted_CPUsum_paths3;                 // a matrix to save the index of the sorted Paths based on power consumption. We first initialize by the number of not sorted paths numbers.
  
for (int i = 0 ; i < P_3_Paths ; i++)
{
SNP3_cpu_utilization_sum[i] = 0; // to reset the CPU utilization for each path after sorting the paths
SNP3_Consmed_Power_sum[i] = 0; // to reset the CPU utilization for each path after sorting the paths
SNP3_CPU_util_not_sorted[i] = 0; // to resent the CPU utilization for each path before sorting the paths.

// Here we initialize the Power COnsumption and CPU matrices for all paths in this type.
for (int j = 0 ; j < P_3_Nodes ; j++)
{
	SNPaths_3_Max_Power[i][j]       = SN_Nodes_MAX_Power_Cap[SN_P3_nodes[i][j]]; // This saves the Max power per each SN node
	SNPaths_3_Min_Power[i][j]       = SN_Nodes_MIN_Power_Cap[SN_P3_nodes[i][j]]; // This saves the Min power per each SN node.
	SNPaths_3_Max_CPU  [i][j]		= SN_Nodes_MAX_CPU_Cap  [SN_P3_nodes[i][j]]; // fill the Maximum CPU for the SN node j in path i to calculate CPU utilization 
	SNPaths_3_CPU      [i][j]       = SN_Nodes_CPU_Cons  [SN_P3_nodes[i][j]]; // fill the CPU matrix by the current CPU value.
//------
	SNPaths_3_CPU_Utilization[i][j] = ((SNPaths_3_Max_CPU  [i][j] - SNPaths_3_CPU[i][j]) / SNPaths_3_Max_CPU  [i][j] ); // This to calculate the CPU utilization per each SN node in the path
	SNPaths_3_Consmed_Power  [i][j] =   SNPaths_3_Min_Power[i][j]  
			                         +((SNPaths_3_Max_Power[i][j] 
			                         -  SNPaths_3_Min_Power[i][j])  
			                         *  SNPaths_3_CPU_Utilization[i][j]); // This calculates the current power consumption per each SN node	
//------				
	
/*  System.out.print("SN PT3 node #" + SN_P3_nodes[i][j] + " in Path " + i + " -->  "
		       + "  Cons_Power-" + SN_P3_nodes[i][j] + " = " + SNPaths_3_Consmed_Power  [i][j] 
		       + "     Cur_CPU-" + SN_P3_nodes[i][j] + " = " + SNPaths_3_CPU[i][j]
		       + "     Max_CPU-" + SN_P3_nodes[i][j] + " = " + SNPaths_3_Max_CPU  [i][j]
		       + "    CPU_Util-" + SN_P3_nodes[i][j] + " = " + SNPaths_3_CPU_Utilization[i][j]);			
System.out.println();
*/			
SNP3_cpu_utilization_sum[i] += (SNPaths_3_CPU_Utilization[i][j] * 100); // get the sum of the SNPaths_3_CPU_Utilization[i][j] from all nodes in the i path
SNP3_Consmed_Power_sum[i] += (SNPaths_3_Consmed_Power[i][j]); // get the sum of the SNPaths_3_CPU_Utilization[i][j] from all nodes in the i path
SNP3_CPU_util_not_sorted[i] += (SNPaths_3_CPU_Utilization[i][j] * 100); // this is the original matrix for the sum of power in the paths type.

} // End j for-loop	

}
/*
for (int i = 0 ; i < P_3_Paths ; i++)
System.out.println("  SNP3_cpu_utilization_sum  = " + SNP3_cpu_utilization_sum[i] + 
	           "  SNP3_CPU_util_not_sorted  = " + SNP3_CPU_util_not_sorted[i]);
*/				
//System.out.println();
		
//00 sorting the paths total CPU utilization and finding the index that represents the paths of the sorted power matrix  000

for (int i1 = 0; i1 < SNP3_cpu_utilization_sum.length - 1; i1 ++ )  
{
for ( int j1 = i1 + 1; j1 < SNP3_cpu_utilization_sum.length; j1 ++ )
{
if(SNP3_Consmed_Power_sum[ i1 ] < SNP3_Consmed_Power_sum[ j1 ])//( (SNP3_cpu_utilization_sum[ i1 ] < SNP3_cpu_utilization_sum[ j1 ]) &&)    //sorting into descending order
{
  tempCPUrsum                     = SNP3_cpu_utilization_sum[ i1 ];   // swapping CPU values
  tempsorted                      = SNP3_sorted_CPUsum_paths[i1];	  // swapping the index
SNP3_cpu_utilization_sum[i1]   = SNP3_cpu_utilization_sum[ j1 ]; 
SNP3_sorted_CPUsum_paths[i1]   = SNP3_sorted_CPUsum_paths[j1];
SNP3_cpu_utilization_sum[j1]   = tempCPUrsum;
SNP3_sorted_CPUsum_paths[j1]   = tempsorted;			
				   
} // end of -if- for sorting           
} // end of j1 for-loop 
} // end of i1 for-loop
	
//System.out.println();		
//System.out.println("Not Sorted Path " + "   CPUSum " + " ----- " + " Sorted Path " + " CPUSum ");
//System.out.println();
	
//Here we print the values of the total cpu in the path, once before sorting, and then after after sorting.
/*	for (int i1=0; i1 < SNP3_sorted_CPUsum_paths.length ; i1++)
System.out.println("      " + i1                            + "     " + SNP3_CPU_util_not_sorted[i1] + 
             "      " + SNP3_sorted_CPUsum_paths[i1] + "     " + SNP3_cpu_utilization_sum[i1]); // to print power consumption values not sorted and after being sorted
*/		          
//000000000000000000000000000000000000000 End of finding the index  000000000000000000000000000000000000000000000000000          
         
//____________________________   Initialize the BW and Delay matrices   _______________________________ 
//System.out.println();
//System.out.println("____________   Initialize BW and Delay matrices for SN edges  _______________");
//System.out.println();		

double[][]SNPaths_3_BW    = new double[P_3_Paths][P_3_Nodes-1]; // this is a matrix that includes the current BW per each SN edge per each path type_2. And there is 1 edge less than the number of nodes, that is why the (-1)
int[][]   SNPaths_3_Delay = new int   [P_3_Paths][P_3_Nodes-1];   // this is a matrix that includes the delay per each SN edge per each path type_2. And there is 1 edges less than the number of nodes, that is why the (-1)
int[]     SNPT3_Delay     = new int   [P_3_Paths];

//Here we initialize the BW and Delay matrices for this type of paths.
for (int i=0 ; i < P_3_Paths ; i++)
{
for (int j=0 ; j < P_3_Nodes-1 ; j++)
{
SNPaths_3_BW   [i][j] = SN_BWmatrix    [SN_P3_nodes[i][j]][SN_P3_nodes[i][j+1]]; // Initialize the BW for each SN link in this SN path type
SNPaths_3_Delay[i][j] = SN_DelayMatrix [SN_P3_nodes[i][j]][SN_P3_nodes[i][j+1]]; // Initialize the delay for each link in this SN path type
SNPT3_Delay[i]       += SNPaths_3_Delay[i][j]; // get the sum of the delay in the SN path
} // End of j for loop	
}
//System.out.println("____________________________   End Of SN paths type 3 Initializations   _______________________________");

//******************************************************* Start Comparisons Phase **************************************************  
//******************************************* First create the paths segments for SN and VNRs **************************************
//************************************************              VNR Segment                 ***************************************
	
/* Here we define the VNR segment for each demanded path, that includes
* 	- Power Consumption and CPU for VNR nodes, and
*  - BW and Delay for VNR edges.
*/

double[] VNRT3_segment = new double[P_3_Nodes * 2];

for (int cpu=0 ; cpu < P_3_Nodes ; cpu++)
{
VNRT3_segment[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT3_segment[cpu]);
}// End of cpu for loop

for (int ibw = 0 ; ibw < (P_3_Nodes-1) ; ibw++)	
{
VNRT3_segment[ibw + (P_3_Nodes)]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW
//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT3_segment[ibw + (P_3_Nodes)]);
}// End of ibw for loop

int vnrdel = 0; // used to get the sum of the total delays in all VNR edges.

for (int i_d = 0 ; i_d < (P_3_Nodes-1) ; i_d++)
{
vnrdel += D_VNRsEdges_Delay[VNR][i_d];
}// End of i_d for loop

VNRT3_segment[(P_3_Nodes*2)-1] = vnrdel; // VNR edges total delay
//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

//********************************************* End of VNR Segment initialization **********************************

//******************************************************* SN Segment  ********************************************

/* Here we define the SN segment for each path, that includes
* 	- Power Consumption and CPU for SN nodes, and
*  - BW and Delay for SN edges
*/

double[] SNPT3_segment = new double[P_3_Nodes * 2];

for (int i=0; i<sorted_CPUsum_paths3.length; i++)
{	
for (int cpu=0 ; cpu < P_3_Nodes ; cpu++)
{
SNPT3_segment[cpu] = SNPaths_3_CPU  [SNP3_sorted_CPUsum_paths[i]][cpu]; // SN Segment Nodes' CPU
//System.out.println(" SN_CPU---" + cpu + "---------> " + SNPT3_segment[cpu]);

}// end of cpu for loop

for (int i_bw=0 ; i_bw < (P_3_Nodes - 1) ; i_bw++)
{
SNPT3_segment[i_bw + (P_3_Nodes)] =   SNPaths_3_BW[SNP3_sorted_CPUsum_paths[i]][i_bw]; // Edges BW
//System.out.println(" SN_BW----" + i_bw + "--> " + (i_bw + 1) + " ---> " + SNPT3_segment[i_bw + (P_3_Nodes)]);

}// end of ibw_d for loop
			
SNPT3_segment[(P_3_Nodes*2)-1]      = SNPT3_Delay[SNP3_sorted_CPUsum_paths[i]]; // edges delay
//System.out.println(" SNseg_Delay------------------> " + SNPT3_segment[(P_3_Nodes * 2)-1]);

//
/*	for (int i1=0 ; i1<SNPT3_segment.length ; i1++)
{
System.out.print(" SNSeg3\t" + SNPT3_segment[i1]+ "\t" + "VNRSeg\t" + VNRT3_segment[i1]); // this prints the SN formulated segment.				 	
System.out.println();		
} 
*/
//System.out.println("----------------------------------------------");

//***********************************  SN to VNR comparisons to check capacity    *****************************

/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
* are sufficient to accommodate the VNR demands:
* 
* 		- First we compare SN metric to the demanded VNR metric. 
* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
* 		  the maximum capacity for that node or edge.
*/	

if (	
(SNPT3_segment[0] -  VNRT3_segment[0])  >=  0 &&
(SNPT3_segment[1] -  VNRT3_segment[1])  >=  0 &&
(SNPT3_segment[2] -  VNRT3_segment[2])  >=  0 &&
(SNPT3_segment[3] -  VNRT3_segment[3])  >=  0 &&
(SNPT3_segment[4] -  VNRT3_segment[4])  >=  0 &&
(SNPT3_segment[5]<= VNRT3_segment[5]))				
{					
			       
// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
					  
// To print the values of the SN after updating the SN given a successful embedding
//System.out.println("---------------------------------------------------------------------------------------------------------");	
//System.out.println("  Selected path   " + sorted_CPUsum_paths10[i]);
//System.out.println("-------------------------------");
	
for (int i_cpu = 0 ; i_cpu < P_3_Nodes ; i_cpu++)
{
SN_Nodes_CPU_Cons[SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][i_cpu]] = SNPT3_segment[i_cpu] -  VNRT3_segment[i_cpu];
/*		System.out.println("CPU_b\t"  + SNPT3_segment[i_cpu] + "\t" +
    	           "CPU_a\t"  + SN_Nodes_CPUCapacity[SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][i_cpu]] + "\t"
		         + "VNRcpu\t" + VNRT3_segment[i_cpu]);
*/

SN_Selected_Path[VNR][i_cpu]	 = SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][i_cpu];  // to save the SN path that was selected to embed a VNR.

}// end of i_cpu for loop

SN_Selected_Path[VNR][49]=3;
SN_Selected_Path[VNR][48]=3;
	
for (int i_bw = 0 ; i_bw < (P_3_Nodes - 1) ; i_bw++)
{
SN_BWmatrix[SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][i_bw]][SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][i_bw+1]]=
	                            SNPT3_segment[i_bw + (P_3_Nodes)] - 
	                            VNRT3_segment[i_bw + (P_3_Nodes)];
/*		     System.out.println("BW_b\t"  + SNPT3_segment[i_bw + (P_3_Nodes)]   
		   + "\t" + "BW_a\t"  + SN_BWmatrix[SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][i_bw]]
					    		           [SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][i_bw+1]]   
		   + "\t" + "VNRbw\t" + VNRT3_segment[i_bw + (P_3_Nodes)]);
*/    
}// end of ibw_d for loop		    	
/*	 
for (int cpu = 0 ; cpu <= 2 ; cpu++)
Consumed_CPU +=VNRT3_segment[cpu];

for (int cbw = 3 ; cbw <= 4 ; cbw++)
Consumed_BW +=VNRT3_segment[cbw];
*/	 
//-------------------
//-------Calculating the new CPu utilization.
//------------------					  

for (int utili = 0 ; utili < P_3_Nodes ; utili++)
{	
SNPaths_3_CPU_Utilization  [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]][utili]= 
((SN_Nodes_MAX_CPU_Cap     [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]] - 
SN_Nodes_CPU_Cons          [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]])/
SN_Nodes_MAX_CPU_Cap       [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
			
SN_Nodes_Current_PCons       [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]] = 
SN_Nodes_MIN_Power_Cap       [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]] 
+((SN_Nodes_MAX_Power_Cap    [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]] 
-  SN_Nodes_MIN_Power_Cap    [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]]) 
*  SNPaths_3_CPU_Utilization [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]][utili]); // this updates the SN current power consumption		

/*
System.out.println("-----------------------------------------------");
System.out.println(" SNPaths_3_CPU_Utilization----------------(" + utili + ") " 
                + SNPaths_3_CPU_Utilization[SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]][utili]);
System.out.println(" SN_Nodes_CPUCapacity-----(" + utili + ") " 
                + SN_Nodes_CPUCapacity     [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MAX_CPU_Cap-----(" + utili + ") " 
                + SN_Nodes_MAX_CPU_Cap     [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_Current_PCons-------------------(" + utili + ") " 
                + SN_Nodes_Current_PCons   [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MAX_Power_Cap---(" + utili + ") " 
                + SN_Nodes_MAX_Power_Cap   [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MIN_Power_Cap---(" + utili + ") " 
                + SN_Nodes_MIN_Power_Cap   [SN_P3_nodes[SNP3_sorted_CPUsum_paths [i]][utili]]);
*/
} // End of utili for loop		

/*--------------------------------------------------------------------------------------------
* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
* -----------------------------------------------------------------------------------------*/
					  	 
//System.out.println("SN Nodes of the Selected Path "); 
for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
{
SN_Selected_path_Nodes[i_path] = SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][i_path];
/* System.out.println("-----------------------------------------------");
System.out.println(" successfulvnr----" + successfulvnr); 
System.out.println(" SN_P3_nodes------" + SN_P3_nodes[SNP5_sorted_CPUsum_paths[i]][i_path]); 
System.out.println(" Path Number------" + SNP3_sorted_CPUsum_paths[i]);
System.out.println("-----------------------------------------------");
*/ 
}
		  
Succ_Used_Paths[successfulvnr] = SNP3_sorted_CPUsum_paths[i];   // This saves the path number of the successful embedding
Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
		  
//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
//System.out.println();
		  
successfulvnr++;
		  
/*  for (int usedpaths = 0 ; usedpaths < successfulvnr ; usedpaths++ )
{
	System.out.println(Used_Paths[usedpaths]);
}
*/  
//	System.out.println();

double Selected_path_PC3 = 0 ; // includes the total power consumption in the selected path.	

for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
Selected_path_PC3 += SN_Nodes_Current_PCons[SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][i_update]]; // this to calculate the total Power Consumption in this selected path

SNPC[VNR] = Selected_path_PC3; // This save the power consumption according to the selected path.

System.out.println("-----------------------------------------------------------------------------------");
System.out.println("-  End of Successful Embedding: 3-Nodes and links are updated  after VNR embedding  -");
System.out.println("-----------------------------------------------------------------------------------");				    	
System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
			    	
break;	// exit the sorted path for-loop, and go for the next VNR		   					   
} else 
		if (i < SNP3_sorted_CPUsum_paths.length-1)
		{
   		continue; // force the next iteration from the for-loop.  
			  
		}else 	    			
	{
			System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
			System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
			System.out.println("Go to next VNR");
	        System.out.println();
			
	        for (int cpu=0 ; cpu < P_3_Nodes ; cpu++)
	        {
	        	D_VNRsNodes_CPU[VNR][cpu] = 0; // VNR nodes CPU
	        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT3_segment[cpu]);
	        	
	        	//SN_Selected_Path[VNR][cpu]	 = SN_P3_nodes[SNP3_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
	        
	        }// End of cpu for loop

	        for (int ibw = 0 ; ibw < (P_3_Nodes-1) ; ibw++)	
	        {
	        	D_VNRsEdges_BW[VNR][ibw] = 0; // VNR edges BW
	        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT3_segment[ibw + (P_3_Nodes)]);
	        }// End of ibw for loop
            
	        
	        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			break;            // This breaks the if statement, and directly go for the immediate next VNR.
	    			
	}// End of -if- statement  
}// End of for-loop on the sorted paths

endTime = System.currentTimeMillis();
totalTime[VNR] = endTime - startTime;

} break; // end of case 3-0	line graph topology

case 1:
{
//---------------------------------------------  Paths of Type 31 nodes  --------------------------------------------------		
//------------------------------------------Initialize the Power and CPU matrices ---------------------------------------- 
//------------------------- Initialize Power and CPU matrices for the SN nodes in paths of type 31 ------------------------

double tempCPUrsum = 0;                   // this is a temporary store to save the value of the sorted power value.
int tempsorted     = 0;                   // this is temporary store to save the value of the index for the sorted power value.
int P_31_Paths     = SN_P31_nodes.length; // number of paths that have two nodes
int P_31_Nodes     = 3;                  // number of nodes per each path

double[][] SNPaths_31_Max_Power       = new double[P_31_Paths][P_31_Nodes];    // this is a matrix that includes the Maximum power capacity per each SN node
double[][] SNPaths_31_Min_Power       = new double[P_31_Paths][P_31_Nodes];    // this is a matrix that includes the Minimum power capacity per each SN node
double[][] SNPaths_31_Consmed_Power   = new double[P_31_Paths][P_31_Nodes];    // this is a matrix that includes the Maximum power capacity per each SN node

double[][] SNPaths_31_Max_CPU         = new double[P_31_Paths][P_31_Nodes];    // this is a matrix that includes the current CPU per each SN node	
double[][] SNPaths_31_CPU             = new double[P_31_Paths][P_31_Nodes];    // this is a matrix that includes the current CPU per each SN node
double[][] SNPaths_31_CPU_Utilization = new double[P_31_Paths][P_31_Nodes]; // this is a matrix to save the CPU utilization for each SN node in this path.			
double[]   SNP31_cpu_utilization_sum  = new double[P_31_Paths];                // SORTED matrix to save the sum of the CPU values for the Path nodes 
double[]   SNP31_Consmed_Power_sum    = new double[P_31_Paths];                // SORTED matrix to save the sum of the CPU values for the Path nodes 
double[]   SNP31_CPU_util_not_sorted  = new double[P_31_Paths];                // NOT SORTED matrix to save the sum of the power values for the Path nodes
int[]      SNP31_sorted_CPUsum_paths  = sorted_CPUsum_paths31;                 // a matrix to save the index of the sorted Paths based on power consumption. We first initialize by the number of not sorted paths numbers.
  
for (int i = 0 ; i < P_31_Paths ; i++)
{
SNP31_cpu_utilization_sum[i] = 0; // to reset the CPU utilization for each path after sorting the paths
SNP31_Consmed_Power_sum[i] = 0; // to reset the CPU utilization for each path after sorting the paths
SNP31_CPU_util_not_sorted[i] = 0; // to resent the CPU utilization for each path before sorting the paths.

// Here we initialize the Power COnsumption and CPU matrices for all paths in this type.
for (int j = 0 ; j < P_31_Nodes ; j++)
{
	SNPaths_31_Max_Power[i][j]       = SN_Nodes_MAX_Power_Cap[SN_P31_nodes[i][j]]; // This saves the Max power per each SN node
	SNPaths_31_Min_Power[i][j]       = SN_Nodes_MIN_Power_Cap[SN_P31_nodes[i][j]]; // This saves the Min power per each SN node.
	SNPaths_31_Max_CPU  [i][j]		 = SN_Nodes_MAX_CPU_Cap  [SN_P31_nodes[i][j]]; // fill the Maximum CPU for the SN node j in path i to calculate CPU utilization 
	SNPaths_31_CPU      [i][j]       = SN_Nodes_CPU_Cons     [SN_P31_nodes[i][j]]; // fill the CPU matrix by the current CPU value.
//------
	SNPaths_31_CPU_Utilization[i][j] = ((SNPaths_31_Max_CPU  [i][j] - SNPaths_31_CPU[i][j]) / SNPaths_31_Max_CPU  [i][j] ); // This to calculate the CPU utilization per each SN node in the path
	SNPaths_31_Consmed_Power  [i][j] =   SNPaths_31_Min_Power[i][j]  
			                          +((SNPaths_31_Max_Power[i][j] 
			                          -  SNPaths_31_Min_Power[i][j])  
			                          *  SNPaths_31_CPU_Utilization[i][j]); // This calculates the current power consumption per each SN node	
//------				
	
/*  System.out.print("SN PT31 node #" + SN_P31_nodes[i][j] + " in Path " + i + " -->  "
		       + "  Cons_Power-" + SN_P31_nodes[i][j] + " = " + SNPaths_31_Consmed_Power  [i][j] 
		       + "     Cur_CPU-" + SN_P31_nodes[i][j] + " = " + SNPaths_31_CPU[i][j]
		       + "     Max_CPU-" + SN_P31_nodes[i][j] + " = " + SNPaths_31_Max_CPU  [i][j]
		       + "    CPU_Util-" + SN_P31_nodes[i][j] + " = " + SNPaths_31_CPU_Utilization[i][j]);			
System.out.println();
*/			
SNP31_cpu_utilization_sum[i] += (SNPaths_31_CPU_Utilization[i][j] * 100); // get the sum of the SNPaths_31_CPU_Utilization[i][j] from all nodes in the i path
SNP31_Consmed_Power_sum[i]   += (SNPaths_31_Consmed_Power[i][j]); // get the sum of the SNPaths_31_CPU_Utilization[i][j] from all nodes in the i path
SNP31_CPU_util_not_sorted[i] += (SNPaths_31_CPU_Utilization[i][j] * 100); // this is the original matrix for the sum of power in the paths type.

} // End j for-loop	

}
/*
for (int i = 0 ; i < P_31_Paths ; i++)
System.out.println("  SNP31_cpu_utilization_sum  = " + SNP31_cpu_utilization_sum[i] + 
	           "  SNP31_CPU_util_not_sorted  = " + SNP31_CPU_util_not_sorted[i]);
*/				
//System.out.println();
		
//00 sorting the paths total CPU utilization and finding the index that represents the paths of the sorted power matrix  000

for (int i1 = 0; i1 < SNP31_cpu_utilization_sum.length - 1; i1 ++ )  
{
for ( int j1 = i1 + 1; j1 < SNP31_cpu_utilization_sum.length; j1 ++ )
{
if(SNP31_Consmed_Power_sum[ i1 ] < SNP31_Consmed_Power_sum[ j1 ])//( (SNP31_cpu_utilization_sum[ i1 ] < SNP31_cpu_utilization_sum[ j1 ]) &&)    //sorting into descending order
{
  tempCPUrsum                     = SNP31_cpu_utilization_sum[ i1 ];   // swapping CPU values
  tempsorted                      = SNP31_sorted_CPUsum_paths[i1];	  // swapping the index
SNP31_cpu_utilization_sum[i1]   = SNP31_cpu_utilization_sum[ j1 ]; 
SNP31_sorted_CPUsum_paths[i1]   = SNP31_sorted_CPUsum_paths[j1];
SNP31_cpu_utilization_sum[j1]   = tempCPUrsum;
SNP31_sorted_CPUsum_paths[j1]   = tempsorted;			
				   
} // end of -if- for sorting           
} // end of j1 for-loop 
} // end of i1 for-loop
	
//System.out.println();		
//System.out.println("Not Sorted Path " + "   CPUSum " + " ----- " + " Sorted Path " + " CPUSum ");
//System.out.println();
	
//Here we print the values of the total cpu in the path, once before sorting, and then after after sorting.
/*	for (int i1=0; i1 < SNP31_sorted_CPUsum_paths.length ; i1++)
System.out.println("      " + i1                            + "     " + SNP31_CPU_util_not_sorted[i1] + 
             "      " + SNP31_sorted_CPUsum_paths[i1] + "     " + SNP31_cpu_utilization_sum[i1]); // to print power consumption values not sorted and after being sorted
*/		          
//000000000000000000000000000000000000000 End of finding the index  000000000000000000000000000000000000000000000000000          
         
//____________________________   Initialize the BW and Delay matrices   _______________________________ 
//System.out.println();
//System.out.println("____________   Initialize BW and Delay matrices for SN edges  _______________");
//System.out.println();		

double[][]SNPaths_31_BW    = new double[P_31_Paths][P_31_Nodes-1]; // this is a matrix that includes the current BW per each SN edge per each path type_2. And there is 1 edge less than the number of nodes, that is why the (-1)
int[][]   SNPaths_31_Delay = new int   [P_31_Paths][P_31_Nodes-1];   // this is a matrix that includes the delay per each SN edge per each path type_2. And there is 1 edges less than the number of nodes, that is why the (-1)
int[]     SNPT31_Delay     = new int   [P_31_Paths];

//Here we initialize the BW and Delay matrices for this type of paths.
for (int i=0 ; i < P_31_Paths ; i++)
{
	
SNPaths_31_BW   [i][0] = SN_BWmatrix    [SN_P31_nodes[i][0]][SN_P31_nodes[i][1]]; // Initialize the BW for each SN link in this SN path type
SNPaths_31_BW   [i][1] = SN_BWmatrix    [SN_P31_nodes[i][0]][SN_P31_nodes[i][2]]; // Initialize the BW for each SN link in this SN path type
SNPaths_31_Delay[i][0] = SN_DelayMatrix [SN_P31_nodes[i][0]][SN_P31_nodes[i][1]]; // Initialize the delay for each link in this SN path type	
SNPaths_31_Delay[i][1] = SN_DelayMatrix [SN_P31_nodes[i][0]][SN_P31_nodes[i][2]]; // Initialize the delay for each link in this SN path type
SNPT31_Delay[i]        = SNPaths_31_Delay[i][0]+
		                 SNPaths_31_Delay[i][1]; // get the sum of the delay in the SN path

}
//System.out.println("____________________________   End Of SN paths type 31 Initializations   _______________________________");

//******************************************************* Start Comparisons Phase **************************************************  
//******************************************* First create the paths segments for SN and VNRs **************************************
//************************************************              VNR Segment                 ***************************************
	
/* Here we define the VNR segment for each demanded path, that includes
* 	- Power Consumption and CPU for VNR nodes, and
*  - BW and Delay for VNR edges.
*/

double[] VNRT31_segment = new double[P_31_Nodes * 2];

for (int cpu=0 ; cpu < P_31_Nodes ; cpu++)
{
VNRT31_segment[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT31_segment[cpu]);
}// End of cpu for loop

for (int ibw = 0 ; ibw < (P_31_Nodes-1) ; ibw++)	
{
VNRT31_segment[ibw + (P_31_Nodes)]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW
//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT31_segment[ibw + (P_31_Nodes)]);
}// End of ibw for loop

int vnrdel = 0; // used to get the sum of the total delays in all VNR edges.

for (int i_d = 0 ; i_d < (P_31_Nodes-1) ; i_d++)
{
vnrdel += D_VNRsEdges_Delay[VNR][i_d];
}// End of i_d for loop

VNRT31_segment[(P_31_Nodes*2)-1] = vnrdel; // VNR edges total delay
//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

//********************************************* End of VNR Segment initialization **********************************

//******************************************************* SN Segment  ********************************************

/* Here we define the SN segment for each path, that includes
* 	- Power Consumption and CPU for SN nodes, and
*  - BW and Delay for SN edges
*/

double[] SNPT31_segment = new double[P_31_Nodes * 2];

for (int i=0; i<sorted_CPUsum_paths31.length; i++)
{	
for (int cpu=0 ; cpu < P_31_Nodes ; cpu++)
{
SNPT31_segment[cpu] = SNPaths_31_CPU  [SNP31_sorted_CPUsum_paths[i]][cpu]; // SN Segment Nodes' CPU
//System.out.println(" SN_CPU---" + cpu + "---------> " + SNPT31_segment[cpu]);

}// end of cpu for loop

for (int i_bw=0 ; i_bw < (P_31_Nodes - 1) ; i_bw++)
{
SNPT31_segment[i_bw + (P_31_Nodes)] =   SNPaths_31_BW[SNP31_sorted_CPUsum_paths[i]][i_bw]; // Edges BW
//System.out.println(" SN_BW----" + i_bw + "--> " + (i_bw + 1) + " ---> " + SNPT31_segment[i_bw + (P_31_Nodes)]);

}// end of ibw_d for loop
			
SNPT31_segment[(P_31_Nodes*2)-1]      = SNPT31_Delay[SNP31_sorted_CPUsum_paths[i]]; // edges delay
//System.out.println(" SNseg_Delay------------------> " + SNPT31_segment[(P_31_Nodes * 2)-1]);

//
/*	for (int i1=0 ; i1<SNPT31_segment.length ; i1++)
{
System.out.print(" SNSeg31\t" + SNPT31_segment[i1]+ "\t" + "VNRSeg\t" + VNRT31_segment[i1]); // this prints the SN formulated segment.				 	
System.out.println();		
} 
*/
//System.out.println("----------------------------------------------");

//***********************************  SN to VNR comparisons to check capacity    *****************************

/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
* are sufficient to accommodate the VNR demands:
* 
* 		- First we compare SN metric to the demanded VNR metric. 
* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
* 		  the maximum capacity for that node or edge.
*/	

if (	
(SNPT31_segment[0] -  VNRT31_segment[0])  >=  0 &&
(SNPT31_segment[1] -  VNRT31_segment[1])  >=  0 &&
(SNPT31_segment[2] -  VNRT31_segment[2])  >=  0 &&
(SNPT31_segment[3] -  VNRT31_segment[3])  >=  0 &&
(SNPT31_segment[4] -  VNRT31_segment[4])  >=  0 &&
(SNPT31_segment[5]<=  VNRT31_segment[5]))				
{					
			       
// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
					  
// To print the values of the SN after updating the SN given a successful embedding
//System.out.println("---------------------------------------------------------------------------------------------------------");	
//System.out.println("  Selected path   " + sorted_CPUsum_paths10[i]);
//System.out.println("-------------------------------");
	
for (int i_cpu = 0 ; i_cpu < P_31_Nodes ; i_cpu++)
{
SN_Nodes_CPU_Cons[SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][i_cpu]] = SNPT31_segment[i_cpu] -  VNRT31_segment[i_cpu];
/*		System.out.println("CPU_b\t"  + SNPT31_segment[i_cpu] + "\t" +
    	           "CPU_a\t"  + SN_Nodes_CPUCapacity[SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][i_cpu]] + "\t"
		         + "VNRcpu\t" + VNRT31_segment[i_cpu]);
*/

SN_Selected_Path[VNR][i_cpu]	 = SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][i_cpu];  // to save the SN path that was selected to embed a VNR.

}// end of i_cpu for loop

SN_Selected_Path[VNR][49]=31;		
SN_Selected_Path[VNR][48]=3;

SN_BWmatrix[SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][0]][SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][1]]=
	    SNPT31_segment[3] - 
	    VNRT31_segment[3];
SN_BWmatrix[SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][0]][SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][2]]=
        SNPT31_segment[4] - 
        VNRT31_segment[4];	

/*		     System.out.println("BW_b\t"  + SNPT31_segment[i_bw + (P_31_Nodes)]   
		   + "\t" + "BW_a\t"  + SN_BWmatrix[SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][i_bw]]
					    		           [SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][i_bw+1]]   
		   + "\t" + "VNRbw\t" + VNRT31_segment[i_bw + (P_31_Nodes)]);
*/    
/*	 
for (int cpu = 0 ; cpu <= 2 ; cpu++)
Consumed_CPU +=VNRT31_segment[cpu];

for (int cbw = 31 ; cbw <= 4 ; cbw++)
Consumed_BW +=VNRT31_segment[cbw];
*/	 
//-------------------
//-------Calculating the new CPu utilization.
//------------------					  

for (int utili = 0 ; utili < P_31_Nodes ; utili++)
{	
SNPaths_31_CPU_Utilization   [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]][utili]= 
((SN_Nodes_MAX_CPU_Cap       [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]] - 
SN_Nodes_CPU_Cons            [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]])/
SN_Nodes_MAX_CPU_Cap         [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
			
SN_Nodes_Current_PCons       [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]] = 
SN_Nodes_MIN_Power_Cap       [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]] 
+((SN_Nodes_MAX_Power_Cap    [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]] 
-  SN_Nodes_MIN_Power_Cap    [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]]) 
*  SNPaths_31_CPU_Utilization[SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]][utili]); // this updates the SN current power consumption		

/*
System.out.println("-----------------------------------------------");
System.out.println(" SNPaths_31_CPU_Utilization----------------(" + utili + ") " 
                + SNPaths_31_CPU_Utilization[SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]][utili]);
System.out.println(" SN_Nodes_CPUCapacity-----(" + utili + ") " 
                + SN_Nodes_CPUCapacity     [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MAX_CPU_Cap-----(" + utili + ") " 
                + SN_Nodes_MAX_CPU_Cap     [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_Current_PCons-------------------(" + utili + ") " 
                + SN_Nodes_Current_PCons   [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MAX_Power_Cap---(" + utili + ") " 
                + SN_Nodes_MAX_Power_Cap   [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MIN_Power_Cap---(" + utili + ") " 
                + SN_Nodes_MIN_Power_Cap   [SN_P31_nodes[SNP31_sorted_CPUsum_paths [i]][utili]]);
*/
} // End of utili for loop		

/*--------------------------------------------------------------------------------------------
* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
* -----------------------------------------------------------------------------------------*/
					  	 
//System.out.println("SN Nodes of the Selected Path "); 
for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
{
SN_Selected_path_Nodes[i_path] = SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][i_path];
/* System.out.println("-----------------------------------------------");
System.out.println(" successfulvnr----" + successfulvnr); 
System.out.println(" SN_P31_nodes------" + SN_P31_nodes[SNP5_sorted_CPUsum_paths[i]][i_path]); 
System.out.println(" Path Number------" + SNP31_sorted_CPUsum_paths[i]);
System.out.println("-----------------------------------------------");
*/ 
}
		  
Succ_Used_Paths[successfulvnr] = SNP31_sorted_CPUsum_paths[i];   // This saves the path number of the successful embedding
Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
		  
//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
//System.out.println();
		  
successfulvnr++;
		  
/*  for (int usedpaths = 0 ; usedpaths < successfulvnr ; usedpaths++ )
{
	System.out.println(Used_Paths[usedpaths]);
}
*/  
//	System.out.println();

double Selected_path_PC31 = 0 ; // includes the total power consumption in the selected path.	

for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
Selected_path_PC31 += SN_Nodes_Current_PCons[SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][i_update]]; // this to calculate the total Power Consumption in this selected path

SNPC[VNR] = Selected_path_PC31; // This save the power consumption according to the selected path.

System.out.println("-----------------------------------------------------------------------------------");
System.out.println("-  End of Successful Embedding: 31-Nodes and links are updated  after VNR embedding  -");
System.out.println("-----------------------------------------------------------------------------------");				    	
System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
			    	
break;	// exit the sorted path for-loop, and go for the next VNR		   					   
} else 
		if (i < SNP31_sorted_CPUsum_paths.length-1)
		{
   		continue; // force the next iteration from the for-loop.  
			  
		}else 	    			
	{
			System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
			System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
			System.out.println("Go to next VNR");
	        System.out.println();
			
	        for (int cpu=0 ; cpu < P_31_Nodes ; cpu++)
	        {
	        	D_VNRsNodes_CPU[VNR][cpu] = 0; // VNR nodes CPU
	        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT31_segment[cpu]);
	        	
	        	//SN_Selected_Path[VNR][cpu]	 = SN_P31_nodes[SNP31_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
	        
	        }// End of cpu for loop

	        for (int ibw = 0 ; ibw < (P_31_Nodes-1) ; ibw++)	
	        {
	        	D_VNRsEdges_BW[VNR][ibw] = 0; // VNR edges BW
	        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT31_segment[ibw + (P_31_Nodes)]);
	        }// End of ibw for loop
	        

	        
	        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			break;            // This breaks the if statement, and directly go for the immediate next VNR.
	    			
	}// End of -if- statement  
}// End of for-loop on the sorted paths	

endTime = System.currentTimeMillis();
totalTime[VNR] = endTime - startTime;

}break; // end of case 3-1 fish bone topology

case 2: 
{
//---------------------------------------------  Paths of Type 311 nodes  --------------------------------------------------		
//------------------------------------------Initialize the Power and CPU matrices ---------------------------------------- 
//------------------------- Initialize Power and CPU matrices for the SN nodes in paths of type 311 ------------------------

double tempCPUrsum = 0;                   // this is a temporary store to save the value of the sorted power value.
int tempsorted     = 0;                   // this is temporary store to save the value of the index for the sorted power value.
int P_311_Paths     = SN_P311_nodes.length; // number of paths that have two nodes
int P_311_Nodes     = 3;                  // number of nodes per each path

double[][] SNPaths_311_Max_Power       = new double[P_311_Paths][P_311_Nodes];    // this is a matrix that includes the Maximum power capacity per each SN node
double[][] SNPaths_311_Min_Power       = new double[P_311_Paths][P_311_Nodes];    // this is a matrix that includes the Minimum power capacity per each SN node
double[][] SNPaths_311_Consmed_Power   = new double[P_311_Paths][P_311_Nodes];    // this is a matrix that includes the Maximum power capacity per each SN node

double[][] SNPaths_311_Max_CPU         = new double[P_311_Paths][P_311_Nodes];    // this is a matrix that includes the current CPU per each SN node	
double[][] SNPaths_311_CPU             = new double[P_311_Paths][P_311_Nodes];    // this is a matrix that includes the current CPU per each SN node
double[][] SNPaths_311_CPU_Utilization = new double[P_311_Paths][P_311_Nodes]; // this is a matrix to save the CPU utilization for each SN node in this path.			
double[]   SNP311_cpu_utilization_sum  = new double[P_311_Paths];                // SORTED matrix to save the sum of the CPU values for the Path nodes 
double[]   SNP311_Consmed_Power_sum    = new double[P_311_Paths];                // SORTED matrix to save the sum of the CPU values for the Path nodes 
double[]   SNP311_CPU_util_not_sorted  = new double[P_311_Paths];                // NOT SORTED matrix to save the sum of the power values for the Path nodes
int[]      SNP311_sorted_CPUsum_paths  = sorted_CPUsum_paths311;                 // a matrix to save the index of the sorted Paths based on power consumption. We first initialize by the number of not sorted paths numbers.
  
for (int i = 0 ; i < P_311_Paths ; i++)
{
SNP311_cpu_utilization_sum[i] = 0; // to reset the CPU utilization for each path after sorting the paths
SNP311_Consmed_Power_sum[i] = 0; // to reset the CPU utilization for each path after sorting the paths
SNP311_CPU_util_not_sorted[i] = 0; // to resent the CPU utilization for each path before sorting the paths.

// Here we initialize the Power COnsumption and CPU matrices for all paths in this type.
for (int j = 0 ; j < P_311_Nodes ; j++)
{
	SNPaths_311_Max_Power[i][j]       = SN_Nodes_MAX_Power_Cap[SN_P311_nodes[i][j]]; // This saves the Max power per each SN node
	SNPaths_311_Min_Power[i][j]       = SN_Nodes_MIN_Power_Cap[SN_P311_nodes[i][j]]; // This saves the Min power per each SN node.
	SNPaths_311_Max_CPU  [i][j]		  = SN_Nodes_MAX_CPU_Cap  [SN_P311_nodes[i][j]]; // fill the Maximum CPU for the SN node j in path i to calculate CPU utilization 
	SNPaths_311_CPU      [i][j]       = SN_Nodes_CPU_Cons     [SN_P311_nodes[i][j]]; // fill the CPU matrix by the current CPU value.
//------
	SNPaths_311_CPU_Utilization[i][j] = ((SNPaths_311_Max_CPU  [i][j] - SNPaths_311_CPU[i][j]) / SNPaths_311_Max_CPU  [i][j] ); // This to calculate the CPU utilization per each SN node in the path
	SNPaths_311_Consmed_Power  [i][j] =   SNPaths_311_Min_Power[i][j]  
			                          +((SNPaths_311_Max_Power[i][j] 
			                          -  SNPaths_311_Min_Power[i][j])  
			                          *  SNPaths_311_CPU_Utilization[i][j]); // This calculates the current power consumption per each SN node	
//------				
	
/*  System.out.print("SN PT311 node #" + SN_P311_nodes[i][j] + " in Path " + i + " -->  "
		       + "  Cons_Power-" + SN_P311_nodes[i][j] + " = " + SNPaths_311_Consmed_Power  [i][j] 
		       + "     Cur_CPU-" + SN_P311_nodes[i][j] + " = " + SNPaths_311_CPU[i][j]
		       + "     Max_CPU-" + SN_P311_nodes[i][j] + " = " + SNPaths_311_Max_CPU  [i][j]
		       + "    CPU_Util-" + SN_P311_nodes[i][j] + " = " + SNPaths_311_CPU_Utilization[i][j]);			
System.out.println();
*/			
SNP311_cpu_utilization_sum[i] += (SNPaths_311_CPU_Utilization[i][j] * 100); // get the sum of the SNPaths_311_CPU_Utilization[i][j] from all nodes in the i path
SNP311_Consmed_Power_sum[i]   += (SNPaths_311_Consmed_Power[i][j]); // get the sum of the SNPaths_311_CPU_Utilization[i][j] from all nodes in the i path
SNP311_CPU_util_not_sorted[i] += (SNPaths_311_CPU_Utilization[i][j] * 100); // this is the original matrix for the sum of power in the paths type.

} // End j for-loop	

}
/*
for (int i = 0 ; i < P_311_Paths ; i++)
System.out.println("  SNP311_cpu_utilization_sum  = " + SNP311_cpu_utilization_sum[i] + 
	           "  SNP311_CPU_util_not_sorted  = " + SNP311_CPU_util_not_sorted[i]);
*/				
//System.out.println();
		
//00 sorting the paths total CPU utilization and finding the index that represents the paths of the sorted power matrix  000

for (int i1 = 0; i1 < SNP311_cpu_utilization_sum.length - 1; i1 ++ )  
{
for ( int j1 = i1 + 1; j1 < SNP311_cpu_utilization_sum.length; j1 ++ )
{
if(SNP311_Consmed_Power_sum[ i1 ] < SNP311_Consmed_Power_sum[ j1 ])//( (SNP311_cpu_utilization_sum[ i1 ] < SNP311_cpu_utilization_sum[ j1 ]) &&)    //sorting into descending order
{
  tempCPUrsum                     = SNP311_cpu_utilization_sum[ i1 ];   // swapping CPU values
  tempsorted                      = SNP311_sorted_CPUsum_paths[i1];	  // swapping the index
SNP311_cpu_utilization_sum[i1]   = SNP311_cpu_utilization_sum[ j1 ]; 
SNP311_sorted_CPUsum_paths[i1]   = SNP311_sorted_CPUsum_paths[j1];
SNP311_cpu_utilization_sum[j1]   = tempCPUrsum;
SNP311_sorted_CPUsum_paths[j1]   = tempsorted;			
				   
} // end of -if- for sorting           
} // end of j1 for-loop 
} // end of i1 for-loop
	
//System.out.println();		
//System.out.println("Not Sorted Path " + "   CPUSum " + " ----- " + " Sorted Path " + " CPUSum ");
//System.out.println();
	
//Here we print the values of the total cpu in the path, once before sorting, and then after after sorting.
/*	for (int i1=0; i1 < SNP311_sorted_CPUsum_paths.length ; i1++)
System.out.println("      " + i1                            + "     " + SNP311_CPU_util_not_sorted[i1] + 
             "      " + SNP311_sorted_CPUsum_paths[i1] + "     " + SNP311_cpu_utilization_sum[i1]); // to print power consumption values not sorted and after being sorted
*/		          
//000000000000000000000000000000000000000 End of finding the index  000000000000000000000000000000000000000000000000000          
         
//____________________________   Initialize the BW and Delay matrices   _______________________________ 
//System.out.println();
//System.out.println("____________   Initialize BW and Delay matrices for SN edges  _______________");
//System.out.println();		

double[][]SNPaths_311_BW    = new double[P_311_Paths][P_311_Nodes]; // this is a matrix that includes the current BW per each SN edge per each path type_2. And there is 1 edge less than the number of nodes, that is why the (-1)
int[][]   SNPaths_311_Delay = new int   [P_311_Paths][P_311_Nodes];   // this is a matrix that includes the delay per each SN edge per each path type_2. And there is 1 edges less than the number of nodes, that is why the (-1)
int[]     SNPT311_Delay     = new int   [P_311_Paths];

//Here we initialize the BW and Delay matrices for this type of paths.
for (int i=0 ; i < P_311_Paths ; i++)
{

SNPaths_311_BW   [i][0] = SN_BWmatrix    [SN_P311_nodes[i][0]][SN_P311_nodes[i][1]]; // Initialize the BW for each SN link in this SN path type
SNPaths_311_BW   [i][1] = SN_BWmatrix    [SN_P311_nodes[i][1]][SN_P311_nodes[i][2]]; // Initialize the BW for each SN link in this SN path type
SNPaths_311_BW   [i][2] = SN_BWmatrix    [SN_P311_nodes[i][0]][SN_P311_nodes[i][2]]; // Initialize the BW for each SN link in this SN path type
SNPaths_311_Delay[i][0] = SN_DelayMatrix [SN_P311_nodes[i][0]][SN_P311_nodes[i][1]]; // Initialize the delay for each link in this SN path type	
SNPaths_311_Delay[i][1] = SN_DelayMatrix [SN_P311_nodes[i][1]][SN_P311_nodes[i][2]]; // Initialize the delay for each link in this SN path type
SNPaths_311_Delay[i][2] = SN_DelayMatrix [SN_P311_nodes[i][0]][SN_P311_nodes[i][2]]; // Initialize the delay for each link in this SN path type
SNPT311_Delay[i]        = SNPaths_311_Delay[i][0]+
		                  SNPaths_311_Delay[i][1]+
		                  SNPaths_311_Delay[i][2]; // get the sum of the delay in the SN path
						
}
//System.out.println("____________________________   End Of SN paths type 311 Initializations   _______________________________");

//******************************************************* Start Comparisons Phase **************************************************  
//******************************************* First create the paths segments for SN and VNRs **************************************
//************************************************              VNR Segment                 ***************************************
	
/* Here we define the VNR segment for each demanded path, that includes
* 	- Power Consumption and CPU for VNR nodes, and
*  - BW and Delay for VNR edges.
*/

double[] VNRT311_segment = new double[(P_311_Nodes * 2)+1];

for (int cpu=0 ; cpu < P_311_Nodes ; cpu++)
{
VNRT311_segment[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT311_segment[cpu]);
}// End of cpu for loop

for (int ibw = 0 ; ibw < (P_311_Nodes) ; ibw++)	
{
VNRT311_segment[ibw + (P_311_Nodes)]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW
//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT311_segment[ibw + (P_311_Nodes)]);
}// End of ibw for loop

int vnrdel = 0; // used to get the sum of the total delays in all VNR edges.

for (int i_d = 0 ; i_d < (P_311_Nodes) ; i_d++)
{
vnrdel += D_VNRsEdges_Delay[VNR][i_d];
}// End of i_d for loop

VNRT311_segment[(P_311_Nodes*2)] = vnrdel; // VNR edges total delay
//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

//********************************************* End of VNR Segment initialization **********************************

//******************************************************* SN Segment  ********************************************

/* Here we define the SN segment for each path, that includes
* 	- Power Consumption and CPU for SN nodes, and
*  - BW and Delay for SN edges
*/

double[] SNPT311_segment = new double[(P_311_Nodes * 2)+1];

for (int i=0; i<sorted_CPUsum_paths311.length; i++)
{	
for (int cpu=0 ; cpu < P_311_Nodes ; cpu++)
{
SNPT311_segment[cpu] = SNPaths_311_CPU  [SNP311_sorted_CPUsum_paths[i]][cpu]; // SN Segment Nodes' CPU
//System.out.println(" SN_CPU---" + cpu + "---------> " + SNPT311_segment[cpu]);

}// end of cpu for loop

for (int i_bw=0 ; i_bw < (P_311_Nodes) ; i_bw++)
{
SNPT311_segment[i_bw + (P_311_Nodes)] =   SNPaths_311_BW[SNP311_sorted_CPUsum_paths[i]][i_bw]; // Edges BW
//System.out.println(" SN_BW----" + i_bw + "--> " + (i_bw + 1) + " ---> " + SNPT311_segment[i_bw + (P_311_Nodes)]);

}// end of ibw_d for loop
			
SNPT311_segment[(P_311_Nodes*2)]      = SNPT311_Delay[SNP311_sorted_CPUsum_paths[i]]; // edges delay
//System.out.println(" SNseg_Delay------------------> " + SNPT311_segment[(P_311_Nodes * 2)-1]);

//
/*	for (int i1=0 ; i1<SNPT311_segment.length ; i1++)
{
System.out.print(" SNSeg311\t" + SNPT311_segment[i1]+ "\t" + "VNRSeg\t" + VNRT311_segment[i1]); // this prints the SN formulated segment.				 	
System.out.println();		
} 
*/
//System.out.println("----------------------------------------------");

//***********************************  SN to VNR comparisons to check capacity    *****************************

/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
* are sufficient to accommodate the VNR demands:
* 
* 		- First we compare SN metric to the demanded VNR metric. 
* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
* 		  the maximum capacity for that node or edge.
*/	

if (	
(SNPT311_segment[0] -  VNRT311_segment[0])  >=  0 &&
(SNPT311_segment[1] -  VNRT311_segment[1])  >=  0 &&
(SNPT311_segment[2] -  VNRT311_segment[2])  >=  0 &&
(SNPT311_segment[3] -  VNRT311_segment[3])  >=  0 &&
(SNPT311_segment[4] -  VNRT311_segment[4])  >=  0 &&
(SNPT311_segment[5] -  VNRT311_segment[5])  >=  0 &&
(SNPT311_segment[6]<=  VNRT311_segment[6]))				
{					
			       
// update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
					  
// To print the values of the SN after updating the SN given a successful embedding
//System.out.println("---------------------------------------------------------------------------------------------------------");	
//System.out.println("  Selected path   " + sorted_CPUsum_paths10[i]);
//System.out.println("-------------------------------");
	
for (int i_cpu = 0 ; i_cpu < P_311_Nodes ; i_cpu++)
{
SN_Nodes_CPU_Cons[SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][i_cpu]] = SNPT311_segment[i_cpu] -  
		                                                                 VNRT311_segment[i_cpu];
/*		System.out.println("CPU_b\t"  + SNPT311_segment[i_cpu] + "\t" +
    	           "CPU_a\t"  + SN_Nodes_CPUCapacity[SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][i_cpu]] + "\t"
		         + "VNRcpu\t" + VNRT311_segment[i_cpu]);
*/

SN_Selected_Path[VNR][i_cpu]	 = SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][i_cpu];  // to save the SN path that was selected to embed a VNR.

}// end of i_cpu for loop	

SN_Selected_Path[VNR][49]=311;	
SN_Selected_Path[VNR][48]=3;

SN_BWmatrix[SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][0]][SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][1]]=
	    SNPT311_segment[3] - 
	    VNRT311_segment[3];
SN_BWmatrix[SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][1]][SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][2]]=
        SNPT311_segment[4] - 
        VNRT311_segment[4];
SN_BWmatrix[SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][0]][SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][2]]=
        SNPT311_segment[5] - 
        VNRT311_segment[5];
/*		     System.out.println("BW_b\t"  + SNPT311_segment[i_bw + (P_311_Nodes)]   
		   + "\t" + "BW_a\t"  + SN_BWmatrix[SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][i_bw]]
					    		           [SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][i_bw+1]]   
		   + "\t" + "VNRbw\t" + VNRT311_segment[i_bw + (P_311_Nodes)]);
*/    
		    	
/*	 
for (int cpu = 0 ; cpu <= 2 ; cpu++)
Consumed_CPU +=VNRT311_segment[cpu];

for (int cbw = 311 ; cbw <= 4 ; cbw++)
Consumed_BW +=VNRT311_segment[cbw];
*/	 
//-------------------
//-------Calculating the new CPu utilization.
//------------------					  

for (int utili = 0 ; utili < P_311_Nodes ; utili++)
{	
SNPaths_311_CPU_Utilization  [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]][utili]= 
((SN_Nodes_MAX_CPU_Cap       [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]] - 
SN_Nodes_CPU_Cons            [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]])/
SN_Nodes_MAX_CPU_Cap         [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
			
SN_Nodes_Current_PCons        [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]] = 
SN_Nodes_MIN_Power_Cap        [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]] 
+((SN_Nodes_MAX_Power_Cap     [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]] 
-  SN_Nodes_MIN_Power_Cap     [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]]) 
*  SNPaths_311_CPU_Utilization[SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]][utili]); // this updates the SN current power consumption		

/*
System.out.println("-----------------------------------------------");
System.out.println(" SNPaths_311_CPU_Utilization----------------(" + utili + ") " 
                + SNPaths_311_CPU_Utilization[SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]][utili]);
System.out.println(" SN_Nodes_CPUCapacity-----(" + utili + ") " 
                + SN_Nodes_CPUCapacity     [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MAX_CPU_Cap-----(" + utili + ") " 
                + SN_Nodes_MAX_CPU_Cap     [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_Current_PCons-------------------(" + utili + ") " 
                + SN_Nodes_Current_PCons   [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MAX_Power_Cap---(" + utili + ") " 
                + SN_Nodes_MAX_Power_Cap   [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MIN_Power_Cap---(" + utili + ") " 
                + SN_Nodes_MIN_Power_Cap   [SN_P311_nodes[SNP311_sorted_CPUsum_paths [i]][utili]]);
*/
} // End of utili for loop		

/*--------------------------------------------------------------------------------------------
* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
* -----------------------------------------------------------------------------------------*/
					  	 
//System.out.println("SN Nodes of the Selected Path "); 
for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
{
SN_Selected_path_Nodes[i_path] = SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][i_path];
/* System.out.println("-----------------------------------------------");
System.out.println(" successfulvnr----" + successfulvnr); 
System.out.println(" SN_P311_nodes------" + SN_P311_nodes[SNP5_sorted_CPUsum_paths[i]][i_path]); 
System.out.println(" Path Number------" + SNP311_sorted_CPUsum_paths[i]);
System.out.println("-----------------------------------------------");
*/ 
}
		  
Succ_Used_Paths[successfulvnr] = SNP311_sorted_CPUsum_paths[i];   // This saves the path number of the successful embedding
Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
		  
//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
//System.out.println();
		  
successfulvnr++;
		  
/*  for (int usedpaths = 0 ; usedpaths < successfulvnr ; usedpaths++ )
{
	System.out.println(Used_Paths[usedpaths]);
}
*/  
//	System.out.println();

double Selected_path_PC311 = 0 ; // includes the total power consumption in the selected path.	

for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
Selected_path_PC311 += SN_Nodes_Current_PCons[SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][i_update]]; // this to calculate the total Power Consumption in this selected path

SNPC[VNR] = Selected_path_PC311; // This save the power consumption according to the selected path.

System.out.println("-----------------------------------------------------------------------------------");
System.out.println("-  End of Successful Embedding: 311-Nodes and links are updated  after VNR embedding  -");
System.out.println("-----------------------------------------------------------------------------------");				    	
System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
			    	
break;	// exit the sorted path for-loop, and go for the next VNR		   					   
} else 
		if (i < SNP311_sorted_CPUsum_paths.length-1)
		{
   		continue; // force the next iteration from the for-loop.  
			  
		}else 	    			
	{
			System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
			System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
			System.out.println("Go to next VNR");
	        System.out.println();
			
	        for (int cpu=0 ; cpu < P_311_Nodes ; cpu++)
	        {
	        	D_VNRsNodes_CPU[VNR][cpu] = 0; // VNR nodes CPU
	        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT311_segment[cpu]);
	        	
	        	//SN_Selected_Path[VNR][cpu]	 = SN_P311_nodes[SNP311_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
	        
	        }// End of cpu for loop

	        for (int ibw = 0 ; ibw < (P_311_Nodes) ; ibw++)	
	        {
	        	D_VNRsEdges_BW[VNR][ibw] = 0; // VNR edges BW
	        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT311_segment[ibw + (P_311_Nodes)]);
	        }// End of ibw for loop
	        

	        
	        rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			break;            // This breaks the if statement, and directly go for the immediate next VNR.
	    			
	}// End of -if- statement  
}// End of for-loop on the sorted paths	

endTime = System.currentTimeMillis();
totalTime[VNR] = endTime - startTime;

}break; // end of case 3-2 ring topology


} // End of switch for Case 3

} break; //end of case 3

//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------  End Of SN paths type 3 Embedding   ----------------------------------------
//-----------------------------------------------------------------------------------------------------------------------

case 2: 
{
//---------------------------------------------  Paths of Type 2 nodes  --------------------------------------------------		
//------------------------------------------Initialize the Power and CPU matrices ---------------------------------------- 
//------------------------- Initialize Power and CPU matrices for the SN nodes in paths of type 22 ------------------------

double tempCPUrsum = 0;                   // this is a temporary store to save the value of the sorted power value.
int tempsorted     = 0;                   // this is temporary store to save the value of the index for the sorted power value.
int P_2_Paths     = SN_P2_nodes.length; // number of paths that have two nodes
int P_2_Nodes     = 2;                  // number of nodes per each path

double[][] SNPaths_2_Max_Power       = new double[P_2_Paths][P_2_Nodes];    // this is a matrix that includes the Maximum power capacity per each SN node
double[][] SNPaths_2_Min_Power       = new double[P_2_Paths][P_2_Nodes];    // this is a matrix that includes the Minimum power capacity per each SN node
double[][] SNPaths_2_Consmed_Power   = new double[P_2_Paths][P_2_Nodes];    // this is a matrix that includes the Maximum power capacity per each SN node

double[][] SNPaths_2_Max_CPU         = new double[P_2_Paths][P_2_Nodes];    // this is a matrix that includes the current CPU per each SN node	
double[][] SNPaths_2_CPU             = new double[P_2_Paths][P_2_Nodes];    // this is a matrix that includes the current CPU per each SN node
double[][] SNPaths_2_CPU_Utilization = new double[P_2_Paths][P_2_Nodes]; // this is a matrix to save the CPU utilization for each SN node in this path.			
double[]   SNP2_cpu_utilization_sum  = new double[P_2_Paths];                // SORTED matrix to save the sum of the CPU values for the Path nodes 
double[]   SNP2_Consmed_Power_sum  = new double[P_2_Paths];                // SORTED matrix to save the sum of the CPU values for the Path nodes 
double[]   SNP2_CPU_util_not_sorted  = new double[P_2_Paths];                // NOT SORTED matrix to save the sum of the power values for the Path nodes
int[]      SNP2_sorted_CPUsum_paths  = sorted_CPUsum_paths2;                 // a matrix to save the index of the sorted Paths based on power consumption. We first initialize by the number of not sorted paths numbers.
  
for (int i = 0 ; i < P_2_Paths ; i++)
{
SNP2_cpu_utilization_sum[i] = 0; // to reset the CPU utilization for each path after sorting the paths
SNP2_Consmed_Power_sum[i] = 0; // to reset the CPU utilization for each path after sorting the paths
SNP2_CPU_util_not_sorted[i] = 0; // to resent the CPU utilization for each path before sorting the paths.

// Here we initialize the Power COnsumption and CPU matrices for all paths in this type.
for (int j = 0 ; j < P_2_Nodes ; j++)
{
	SNPaths_2_Max_Power[i][j]       = SN_Nodes_MAX_Power_Cap[SN_P2_nodes[i][j]]; // This saves the Max power per each SN node
	SNPaths_2_Min_Power[i][j]       = SN_Nodes_MIN_Power_Cap[SN_P2_nodes[i][j]]; // This saves the Min power per each SN node.
	SNPaths_2_Max_CPU  [i][j]		= SN_Nodes_MAX_CPU_Cap  [SN_P2_nodes[i][j]]; // fill the Maximum CPU for the SN node j in path i to calculate CPU utilization 
	SNPaths_2_CPU      [i][j]       = SN_Nodes_CPU_Cons  [SN_P2_nodes[i][j]]; // fill the CPU matrix by the current CPU value.
//------
	SNPaths_2_CPU_Utilization[i][j] = ((SNPaths_2_Max_CPU  [i][j] - SNPaths_2_CPU[i][j]) / SNPaths_2_Max_CPU  [i][j] ); // This to calculate the CPU utilization per each SN node in the path
	SNPaths_2_Consmed_Power  [i][j] =   SNPaths_2_Min_Power[i][j]  
			                         +((SNPaths_2_Max_Power[i][j] 
			                         -  SNPaths_2_Min_Power[i][j])  
			                         *  SNPaths_2_CPU_Utilization[i][j]); // This calculates the current power consumption per each SN node	
//------				
	
/*  System.out.print("SN PT2 node #" + SN_P2_nodes[i][j] + " in Path " + i + " -->  "
		       + "  Cons_Power-" + SN_P2_nodes[i][j] + " = " + SNPaths_2_Consmed_Power  [i][j] 
		       + "     Cur_CPU-" + SN_P2_nodes[i][j] + " = " + SNPaths_2_CPU[i][j]
		       + "     Max_CPU-" + SN_P2_nodes[i][j] + " = " + SNPaths_2_Max_CPU  [i][j]
		       + "    CPU_Util-" + SN_P2_nodes[i][j] + " = " + SNPaths_2_CPU_Utilization[i][j]);			
System.out.println();
*/			
SNP2_cpu_utilization_sum[i] += (SNPaths_2_CPU_Utilization[i][j] * 100); // get the sum of the SNPaths_3_CPU_Utilization[i][j] from all nodes in the i path
SNP2_Consmed_Power_sum[i] += (SNPaths_2_CPU_Utilization[i][j]); // get the sum of the SNPaths_3_CPU_Utilization[i][j] from all nodes in the i path
SNP2_CPU_util_not_sorted[i] += (SNPaths_2_CPU_Utilization[i][j] * 100); // this is the original matrix for the sum of power in the paths type.

} // End j for-loop	

}
/*
for (int i = 0 ; i < P_2_Paths ; i++)
System.out.println("  SNP2_cpu_utilization_sum  = " + SNP2_cpu_utilization_sum[i] + 
	           "  SNP2_CPU_util_not_sorted  = " + SNP2_CPU_util_not_sorted[i]);
*/				
//System.out.println();
		
//00 sorting the paths total CPU utilization and finding the index that represents the paths of the sorted power matrix  000

for (int i1 = 0; i1 < SNP2_cpu_utilization_sum.length - 1; i1 ++ )  
{
for ( int j1 = i1 + 1; j1 < SNP2_cpu_utilization_sum.length; j1 ++ )
{
if(SNP2_Consmed_Power_sum[ i1 ] < SNP2_Consmed_Power_sum[ j1 ])//( (SNP2_cpu_utilization_sum[ i1 ] < SNP2_cpu_utilization_sum[ j1 ]) && )    //sorting into descending order
{
  tempCPUrsum                  = SNP2_cpu_utilization_sum[ i1 ];   // swapping CPU values
  tempsorted                   = SNP2_sorted_CPUsum_paths[i1];	  // swapping the index
SNP2_cpu_utilization_sum[i1]   = SNP2_cpu_utilization_sum[ j1 ]; 
SNP2_sorted_CPUsum_paths[i1]   = SNP2_sorted_CPUsum_paths[j1];
SNP2_cpu_utilization_sum[j1]   = tempCPUrsum;
SNP2_sorted_CPUsum_paths[j1]   = tempsorted;			
				   
} // end of -if- for sorting           
} // end of j1 for-loop 
} // end of i1 for-loop
	
//System.out.println();		
//System.out.println("Not Sorted Path " + "   CPUSum " + " ----- " + " Sorted Path " + " CPUSum ");
//System.out.println();
	
//Here we print the values of the total cpu in the path, once before sorting, and then after sorting.
/*	for (int i1=0; i1 < SNP2_sorted_CPUsum_paths.length ; i1++)
System.out.println("      " + i1                            + "     " + SNP2_CPU_util_not_sorted[i1] + 
             "      " + SNP2_sorted_CPUsum_paths[i1] + "     " + SNP2_cpu_utilization_sum[i1]); // to print power consumption values not sorted and after being sorted
*/		          
//000000000000000000000000000000000000000 End of finding the index  000000000000000000000000000000000000000000000000000          
         
//____________________________   Initialize the BW and Delay matrices   _______________________________ 
//System.out.println();
//System.out.println("____________   Initialize BW and Delay matrices for SN edges  _______________");
//System.out.println();		

double[][]SNPaths_2_BW    = new double[P_2_Paths][P_2_Nodes-1]; // this is a matrix that includes the current BW per each SN edge per each path type_2. And there is 1 edge less than the number of nodes, that is why the (-1)
int[][]   SNPaths_2_Delay = new int   [P_2_Paths][P_2_Nodes-1];   // this is a matrix that includes the delay per each SN edge per each path type_2. And there is 1 edges less than the number of nodes, that is why the (-1)
int[]     SNPT2_Delay     = new int   [P_2_Paths];

//Here we initialize the BW and Delay matrices for this type of paths.
for (int i=0 ; i < P_2_Paths ; i++)
{
for (int j=0 ; j < P_2_Nodes-1 ; j++)
{
SNPaths_2_BW   [i][j] = SN_BWmatrix    [SN_P2_nodes[i][j]][SN_P2_nodes[i][j+1]]; // Initialize the BW for each SN link in this SN path type
SNPaths_2_Delay[i][j] = SN_DelayMatrix [SN_P2_nodes[i][j]][SN_P2_nodes[i][j+1]]; // Initialize the delay for each link in this SN path type
SNPT2_Delay[i]       += SNPaths_2_Delay[i][j]; // get the sum of the delay in the SN path
} // End of j for loop	
}
//System.out.println("____________________________   End Of SN paths type 2 Initializations   _______________________________");

//******************************************************* Start Comparisons Phase **************************************************  
//******************************************* First create the paths segments for SN and VNRs **************************************
//************************************************              VNR Segment                 ***************************************
	
/* Here we define the VNR segment for each demanded path, that includes
* 	- Power Consumption and CPU for VNR nodes, and
*  - BW and Delay for VNR edges.
*/

double[] VNRT2_segment = new double[P_2_Nodes * 2];

for (int cpu=0 ; cpu < P_2_Nodes ; cpu++)
{
VNRT2_segment[cpu] = D_VNRsNodes_CPU[VNR][cpu]; // VNR nodes CPU
//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT2_segment[cpu]);
}// End of cpu for loop

for (int ibw = 0 ; ibw < (P_2_Nodes-1) ; ibw++)	
{
VNRT2_segment[ibw + (P_2_Nodes)]   = D_VNRsEdges_BW[VNR][ibw]; // VNR edges BW
//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT2_segment[ibw + (P_2_Nodes)]);
}// End of ibw for loop

int vnrdel = 0; // used to get the sum of the total delays in all VNR edges.

for (int i_d = 0 ; i_d < (P_2_Nodes-1) ; i_d++)
{
vnrdel += D_VNRsEdges_Delay[VNR][i_d];
}// End of i_d for loop

VNRT2_segment[(P_2_Nodes*2)-1] = vnrdel; // VNR edges total delay
//System.out.println(" VNR_Delay--------------------------> " + vnrdel);

//System.out.println(" ------------------------------------  SN Segment  ---------------------------------------");

//********************************************* End of VNR Segment initialization **********************************

//******************************************************* SN Segment  ********************************************

/* Here we define the SN segment for each path, that includes
* 	- Power Consumption and CPU for SN nodes, and
*  - BW and Delay for SN edges
*/

double[] SNPT2_segment = new double[P_2_Nodes * 2];

for (int i=0; i<sorted_CPUsum_paths2.length; i++)
{	
for (int cpu=0 ; cpu < P_2_Nodes ; cpu++)
{
SNPT2_segment[cpu] = SNPaths_2_CPU  [SNP2_sorted_CPUsum_paths[i]][cpu]; // SN Segment Nodes' CPU
//System.out.println(" SN_CPU---" + cpu + "---------> " + SNPT2_segment[cpu]);

}// end of cpu for loop

for (int i_bw=0 ; i_bw < (P_2_Nodes - 1) ; i_bw++)
{
SNPT2_segment[i_bw + (P_2_Nodes)] =   SNPaths_2_BW[SNP2_sorted_CPUsum_paths[i]][i_bw]; // Edges BW
//System.out.println(" SN_BW----" + i_bw + "--> " + (i_bw + 1) + " ---> " + SNPT2_segment[i_bw + (P_2_Nodes)]);

}// end of ibw_d for loop
			
SNPT2_segment[(P_2_Nodes*2)-1]      = SNPT2_Delay[SNP2_sorted_CPUsum_paths[i]]; // edges delay
//System.out.println(" SNseg_Delay------------------> " + SNPT3_segment[(P_2_Nodes * 2)-1]);

//
/*	for (int i1=0 ; i1<SNPT2_segment.length ; i1++)
{
System.out.print(" SNSeg2\t" + SNPT2_segment[i1]+ "\t" + "VNRSeg\t" + VNRT2_segment[i1]); // this prints the SN formulated segment.				 	
System.out.println();		
} 
*/
//System.out.println("----------------------------------------------");

//***********************************  SN to VNR comparisons to check capacity    *****************************

/* To validate capacity constraints, here we check if the SN nodes' power and CPU, and SN edges' BW and Delay 
* are sufficient to accommodate the VNR demands:
* 
* 		- First we compare SN metric to the demanded VNR metric. 
* 		- Second we make sure that the capacity of the SN nodes and edges does not exceed
* 		  the maximum capacity for that node or edge.
*/	

if (	
(SNPT2_segment[0] -  VNRT2_segment[0])  >=  0 &&
(SNPT2_segment[1] -  VNRT2_segment[1])  >=  0 &&
(SNPT2_segment[2] -  VNRT2_segment[2])  >=  0 &&
(SNPT2_segment[3]<= VNRT2_segment[3]))				
{					
			       
//update the values of the selected SN power, CPU, BW, and Delay matrices as per the VNR corresponding values
					  
//To print the values of the SN after updating the SN given a successful embedding
//System.out.println("---------------------------------------------------------------------------------------------------------");	
//System.out.println("  Selected path   " + sorted_CPUsum_paths10[i]);
//System.out.println("-------------------------------");
	
for (int i_cpu = 0 ; i_cpu < P_2_Nodes ; i_cpu++)
{
SN_Nodes_CPU_Cons[SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][i_cpu]] = SNPT2_segment[i_cpu] -  VNRT2_segment[i_cpu];
/*		System.out.println("CPU_b\t"  + SNPT2_segment[i_cpu] + "\t" +
    	           "CPU_a\t"  + SN_Nodes_CPUCapacity[SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][i_cpu]] + "\t"
		         + "VNRcpu\t" + VNRT2_segment[i_cpu]);
*/

SN_Selected_Path[VNR][i_cpu]	 = SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][i_cpu];  // to save the SN path that was selected to embed a VNR.


}// end of i_cpu for loop

SN_Selected_Path[VNR][49]=2;
SN_Selected_Path[VNR][48]=2;
	
for (int i_bw = 0 ; i_bw < (P_2_Nodes - 1) ; i_bw++)
{
SN_BWmatrix[SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][i_bw]][SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][i_bw+1]]=
	                            SNPT2_segment[i_bw + (P_2_Nodes)] - 
	                            VNRT2_segment[i_bw + (P_2_Nodes)];
/*		     System.out.println("BW_b\t"  + SNPT2_segment[i_bw + (P_2_Nodes)]   
		   + "\t" + "BW_a\t"  + SN_BWmatrix[SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][i_bw]]
					    		           [SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][i_bw+1]]   
		   + "\t" + "VNRbw\t" + VNRT2_segment[i_bw + (P_2_Nodes)]);
*/    
}// end of ibw_d for loop		    	
/*	 
for (int cpu = 0 ; cpu <= 1 ; cpu++)
Consumed_CPU +=VNRT2_segment[cpu];

Consumed_BW +=VNRT2_segment[2];
*/	 
//-------------------
//-------Calculating the new CPu utilization.
//------------------					  

for (int utili = 0 ; utili < P_2_Nodes ; utili++)
{	
SNPaths_2_CPU_Utilization  [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]][utili]= 
((SN_Nodes_MAX_CPU_Cap     [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]] - 
SN_Nodes_CPU_Cons          [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]])/
SN_Nodes_MAX_CPU_Cap       [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
			
SN_Nodes_Current_PCons       [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]] = 
SN_Nodes_MIN_Power_Cap       [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]] 
+((SN_Nodes_MAX_Power_Cap    [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]] 
-  SN_Nodes_MIN_Power_Cap    [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]]) 
*  SNPaths_2_CPU_Utilization [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]][utili]); // this updates the SN current power consumption		

/*
System.out.println("-----------------------------------------------");
System.out.println(" SNPaths_2_CPU_Utilization----------------(" + utili + ") " 
                + SNPaths_2_CPU_Utilization[SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]][utili]);
System.out.println(" SN_Nodes_CPUCapacity-----(" + utili + ") " 
                + SN_Nodes_CPUCapacity     [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MAX_CPU_Cap-----(" + utili + ") " 
                + SN_Nodes_MAX_CPU_Cap     [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_Current_PCons-------------------(" + utili + ") " 
                + SN_Nodes_Current_PCons   [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MAX_Power_Cap---(" + utili + ") " 
                + SN_Nodes_MAX_Power_Cap   [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]]);
System.out.println(" SN_Nodes_MIN_Power_Cap---(" + utili + ") " 
                + SN_Nodes_MIN_Power_Cap   [SN_P2_nodes[SNP2_sorted_CPUsum_paths [i]][utili]]);
*/
} // End of utili for loop		

/*--------------------------------------------------------------------------------------------
* --- This section finds the SN nodes to be Turned off and their total power consumption  ---
* -----------------------------------------------------------------------------------------*/
					  	 
//System.out.println("SN Nodes of the Selected Path "); 
for (int i_path = 0 ; i_path < VNRnodes ; i_path++)
{
SN_Selected_path_Nodes[i_path] = SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][i_path];
/* System.out.println("-----------------------------------------------");
System.out.println(" successfulvnr----" + successfulvnr); 
System.out.println(" SN_P2_nodes------" + SN_P2_nodes[SNP5_sorted_CPUsum_paths[i]][i_path]); 
System.out.println(" Path Number------" + SNP2_sorted_CPUsum_paths[i]);
System.out.println("-----------------------------------------------");
*/ 
}
		  
Succ_Used_Paths[successfulvnr] = SNP2_sorted_CPUsum_paths[i];   // This saves the path number of the successful embedding
Used_Paths     [successfulvnr] = Succ_Used_Paths[successfulvnr];// This save the path number for a successful VNR embedding.
		  
//System.out.println(" Succ_Used_Paths[successfulvnr] - " + Succ_Used_Paths[successfulvnr]); 
//System.out.println(" Used_Paths     [successfulvnr] - " + Used_Paths     [successfulvnr]);
//System.out.println();
		  
successfulvnr++;
		  
/*  for (int usedpaths = 0 ; usedpaths < successfulvnr ; usedpaths++ )
{
	System.out.println(Used_Paths[usedpaths]);
}
*/  
//	System.out.println();

double Selected_path_PC2 = 0 ; // includes the total power consumption in the selected path.	

for (int i_update = 0 ; i_update < VNRnodes ; i_update++)
Selected_path_PC2 += SN_Nodes_Current_PCons[SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][i_update]]; // this to calculate the total Power Consumption in this selected path

SNPC[VNR] = Selected_path_PC2; // This save the power consumption according to the selected path.

System.out.println("-----------------------------------------------------------------------------------");
System.out.println("-  End of Successful Embedding: 2-Nodes and links are updated  after VNR embedding  -");
System.out.println("-----------------------------------------------------------------------------------");				    	
System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
			    	
break;	// exit the sorted path for-loop, and go for the next VNR		   					   
} else 
		if (i < SNP2_sorted_CPUsum_paths.length-1)
		{
   		continue; // force the next iteration from the for-loop.  
			  
		}else 	    			
	       {
			System.out.println("Embedding is Failed, there is no SN path to accomodate the demands of this VNR");
			System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR);
			System.out.println("Go to next VNR");
	        System.out.println();
	        
	        for (int cpu=0 ; cpu < P_2_Nodes ; cpu++)
	        {
	        	D_VNRsNodes_CPU[VNR][cpu] = 0; // VNR nodes CPU
	        	//System.out.println(" VNR_CPU---" + cpu + "----------> " + VNRT2_segment[cpu]);
	        
	        	//SN_Selected_Path[VNR][cpu]	 = SN_P2_nodes[SNP2_sorted_CPUsum_paths[i]][cpu];  // to save the SN path that was selected to embed a VNR.
	        
	        }// End of cpu for loop

	        for (int ibw = 0 ; ibw < (P_2_Nodes-1) ; ibw++)	
	        {
	        	D_VNRsEdges_BW[VNR][ibw] = 0; // VNR edges BW
	        	//System.out.println(" VNR_BW----" + ibw + "--> " + (ibw + 1) + " --> " + VNRT2_segment[ibw + (P_2_Nodes)]);
	        }// End of ibw for loop
	        
			rejectedVNRs++;   // This is a counter to calculate the total rejected VNRs. Will be used in calculating VNRs acceptance ratio.
			break;            // This breaks the if statement, and directly go for the immediate next VNR.
	    			
	}// End of -if- statement  
}// End of for-loop on the sorted paths

endTime = System.currentTimeMillis();
totalTime[VNR] = endTime - startTime;

} break; // End of Case 2

//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------  End Of SN paths type 2 Embedding   ----------------------------------------
//-----------------------------------------------------------------------------------------------------------------------

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   End of All CASES  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%		
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   End of All CASES  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%		
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   End of All CASES  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%		

} // End of the Switch function

//----------------------------------------------- Online Version --------------------------------------------------
//-------------------------------------     Compensation for Expired VNRs     -------------------------------------
//-----------------------------------------------------------------------------------------------------------------

int L =  10 * Time[Tloop];//(int) (2 + Math.random() * ((10 - 2)  + 1)) *
if (VNR >= L )
{ System.out.println("Online Check");
	if ( (VNR % Time[Tloop]) == 0 )
	{ System.out.println("Online Started to expire VNRs");
		
		for (int i=0 ; i < Time[Tloop] ; i++)
		{ 
			//int Rem = VNR-L+i;
			System.out.println("Run"+Runs+"\t"+"Load\t"+Load[Lloop]+"\t"+"Time"+Time[Tloop]+"\t"+"VNR#\t" + VNR +"\t"+ "Rem" + Rem + " SN_Selected_Path[Rem][0]\t"+SN_Selected_Path[Rem][0]);
	
	System.out.println("Removing VNR number\t"+ Rem + "****************************************************");

	/*
	 * This part updates the CPUs of topologies 2 and 3, by removing the VNR's CPUs after 
	 * they are expired.
	 */
	
	if(SN_Selected_Path[Rem][48]==2 || SN_Selected_Path[Rem][48]==3)
	{	
	for (int CPU = 0 ; CPU < SN_Selected_Path[Rem][48] ; CPU++)
	{	
	System.out.println("Rem"+ Rem + "\t" + "NodesinVNR\t" + SN_Selected_Path[Rem][48] +"\t"
					    + "Sel_Path\t"  + SN_Selected_Path[Rem][CPU]);
	System.out.print("CPU_Cons\t"  + (int)SN_Nodes_CPU_Cons[SN_Selected_Path[Rem][CPU]]+"before\t");
			
	SN_Nodes_CPU_Cons[SN_Selected_Path[Rem][CPU]] += D_VNRsNodes_CPU[Rem][CPU]; // to remove the CPU of the embedded VNR
			
	System.out.println((int)SN_Nodes_CPU_Cons[SN_Selected_Path[Rem][CPU]]+"\t after");
						
	}// End of for (int CPU = 0 ; CPU< SN_Nodes ; CPU++)
	
	for (int utili = 0 ; utili < SN_Selected_Path[Rem][48] ; utili++)
	{	
	SN_Selected_Path_CPU_Utilization    [SN_Selected_Path[Rem][utili]]= 
			((SN_Nodes_MAX_CPU_Cap      [SN_Selected_Path[Rem][utili]] - 
			 SN_Nodes_CPU_Cons          [SN_Selected_Path[Rem][utili]])/
			 SN_Nodes_MAX_CPU_Cap       [SN_Selected_Path[Rem][utili]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
	
	System.out.print(SN_Selected_Path[Rem][utili] + "\t" + 
			(int)SN_Nodes_Current_PCons[SN_Selected_Path[Rem][utili]] + "\t" + "NodePC_b\t");
	
	    System.out.println("Util\t"      + 100*(int)SN_Selected_Path_CPU_Utilization    [SN_Selected_Path[Rem][utili]] +
				           "MAX_CPU\t"   + (int)SN_Nodes_MAX_CPU_Cap [SN_Selected_Path[Rem][utili]]+
				           "CPU_Cons\t"  + (int)SN_Nodes_CPU_Cons    [SN_Selected_Path[Rem][utili]]+
				           "Sel_Path\t"  + SN_Selected_Path[Rem][utili]+
				           "Rem\t"       + Rem +
				           "Nodeinpath\t"+ utili);
	
	SN_Nodes_Current_PCons             [SN_Selected_Path[Rem][utili]] = 
	   SN_Nodes_MIN_Power_Cap          [SN_Selected_Path[Rem][utili]] 
	+((SN_Nodes_MAX_Power_Cap          [SN_Selected_Path[Rem][utili]] 
   -  SN_Nodes_MIN_Power_Cap           [SN_Selected_Path[Rem][utili]]) 
   *  SN_Selected_Path_CPU_Utilization [SN_Selected_Path[Rem][utili]]); // this updates the SN current power consumption		
	System.out.println((int)SN_Nodes_Current_PCons[SN_Selected_Path[Rem][utili]]+ "\t" + "NodePC_a\t"+
                        100*(int)SN_Selected_Path_CPU_Utilization [SN_Selected_Path[Rem][utili]]+"util\t");
	System.out.println();

	}
	}else
		{
		
		/*
		 * This part updates the topologies 4-10, by removing the VNR's CPUs after they
		 * are expired.
		 */
		
		
		if(SN_Selected_Path[Rem][48]==4)
		{
		int ExpNode4=Tjp4[SN_Selected_Path[Rem][47]];
		
			for (int p=0 ; p<3 ; p++)
			{
				for(int j=0 ; j<SN_Selected_Path[Rem][46] ; j++)	
					System.out.print(TsE4[ExpNode4+p][j]+",");	// Print the selected topology		
					
				System.out.println();		
			}	
		}else
			if(SN_Selected_Path[Rem][48]==5)
			{
			int ExpNode5=Tjp5[SN_Selected_Path[Rem][47]];
			
				for (int p=0 ; p<3 ; p++)
				{
					for(int j=0 ; j<SN_Selected_Path[Rem][46] ; j++)	
						System.out.print(TsE5[ExpNode5+p][j]+",");// Print the selected topology			
						
					System.out.println();		
				}
			}else
				if(SN_Selected_Path[Rem][48]==6)
				{
					int ExpNode6=Tjp6[SN_Selected_Path[Rem][47]];
					
					for (int p=0 ; p<3 ; p++)
					{
						for(int j=0 ; j<SN_Selected_Path[Rem][46] ; j++)	
							System.out.print(TsE6[ExpNode6+p][j]+",");// Print the selected topology			
							
						System.out.println();		
					}
				}else
					if(SN_Selected_Path[Rem][48]==7)
					{
					int ExpNode7=Tjp7[SN_Selected_Path[Rem][47]];
					
						for (int p=0 ; p<3 ; p++)
						{
							for(int j=0 ; j<SN_Selected_Path[Rem][46] ; j++)	
								System.out.print(TsE7[ExpNode7+p][j]+",");// Print the selected topology			
								
							System.out.println();		
						}
					}else					
						if(SN_Selected_Path[Rem][48]==8)
						{
						int ExpNode8=Tjp8[SN_Selected_Path[Rem][47]];
						
						for (int p=0 ; p<3 ; p++)
						{
							for(int j=0 ; j<SN_Selected_Path[Rem][46] ; j++)	
								System.out.print(TsE8[ExpNode8+p][j]+",");// Print the selected topology			
								
							System.out.println();		
						}
						}else
							if(SN_Selected_Path[Rem][48]==9)
							{
							int ExpNode9=Tjp9[SN_Selected_Path[Rem][47]];
							
							for (int p=0 ; p<3 ; p++)
							{
								for(int j=0 ; j<SN_Selected_Path[Rem][46] ; j++)	
									System.out.print(TsE9[ExpNode9+p][j]+",");// Print the selected topology			
									
								System.out.println();		
							}
							}else
								if(SN_Selected_Path[Rem][48]==10)
								{
								int ExpNode10=Tjp10[SN_Selected_Path[Rem][47]];
								
								for (int p=0 ; p<3 ; p++)
								{
									for(int j=0 ; j<SN_Selected_Path[Rem][46] ; j++)	
										System.out.print(TsE10[ExpNode10+p][j]+",");// Print the selected topology			
										
									System.out.println();		
								}
								}
		
		
		for (int CPU = 0 ; CPU < SN_Selected_Path[Rem][48] ; CPU++)
		{		System.out.println("Rem"+ Rem + "\t" + 
		           "NodesinVNR\t" + SN_Selected_Path[Rem][48] +"\t"
			      + "Sel_node\t"  + SN_Selected_Path[Rem][CPU]);
		
		System.out.print("CPU_Cons\t"  + (int)SN_Nodes_CPU_Cons[SN_Selected_Path[Rem][CPU]]+"\t"+"before\t");
		
		SN_Nodes_CPU_Cons[SN_Selected_Path[Rem][CPU]] += D_VNRsNodes_CPU[Rem][CPU]; // to remove the CPU of the embedded VNR
				
		System.out.println((int)SN_Nodes_CPU_Cons[SN_Selected_Path[Rem][CPU]]+"\t after");
							
		}// End of for (int CPU = 0 ; CPU< SN_Nodes ; CPU++)
		
		for (int utili = 0 ; utili < SN_Selected_Path[Rem][48] ; utili++)
		{	
		SN_Selected_Path_CPU_Utilization    [SN_Selected_Path[Rem][utili]]= 
				((SN_Nodes_MAX_CPU_Cap      [SN_Selected_Path[Rem][utili]] - 
				 SN_Nodes_CPU_Cons          [SN_Selected_Path[Rem][utili]])/
				 SN_Nodes_MAX_CPU_Cap       [SN_Selected_Path[Rem][utili]]); // Calculates the CPU utilization after updating the CPU for a successful embedding                                    // This to calculate the CPU utilization per each SN node in the path
		
		
	    System.out.println("Util_before\t"+ 100*(int)SN_Selected_Path_CPU_Utilization    [SN_Selected_Path[Rem][utili]] +
		    		      "\t"+"MAX_CPU\t"    + (int)SN_Nodes_MAX_CPU_Cap [SN_Selected_Path[Rem][utili]]+
		    	          "\t"+"CPU_Cons\t"   + (int)SN_Nodes_CPU_Cons    [SN_Selected_Path[Rem][utili]]);
			System.out.println("Sel_Path\t"   + SN_Selected_Path[Rem][utili]+
					      "\t"+"Rem\t"        + Rem +
					      "\t"+"Utili\t"      + utili);
		    
          System.out.print(SN_Selected_Path[Rem][utili] + "\t" + 
				          (int)SN_Nodes_Current_PCons[SN_Selected_Path[Rem][utili]] + "\t" + "NodePC_b\t");
			
		  SN_Nodes_Current_PCons           [SN_Selected_Path[Rem][utili]] = 
		  SN_Nodes_MIN_Power_Cap           [SN_Selected_Path[Rem][utili]] 
	   +((SN_Nodes_MAX_Power_Cap           [SN_Selected_Path[Rem][utili]] 
	   -  SN_Nodes_MIN_Power_Cap           [SN_Selected_Path[Rem][utili]]) 
	   *  SN_Selected_Path_CPU_Utilization [SN_Selected_Path[Rem][utili]]); // this updates the SN current power consumption		
		System.out.println((int)SN_Nodes_Current_PCons[SN_Selected_Path[Rem][utili]]+ "\t" + "NodePC_b\t"+
				       100*(int)SN_Selected_Path_CPU_Utilization [SN_Selected_Path[Rem][utili]]+"util_after\t");
		System.out.println();

		}
			
		}	
	
	/*
	 * This part updates the edges of topologies 2 and 3, by removing the VNR edges from the SN edges
	 * after they are expired.
	 */
	
	if ( SN_Selected_Path[Rem][49]==3 || SN_Selected_Path[Rem][49]==2)
	{		
	int BWc = SN_Selected_Path[Rem][48]-1;
	for (int BW = 0 ; BW < BWc ; BW++)
		{
		System.out.println(SN_Selected_Path[Rem][BW]);
		System.out.print((int)SN_BWmatrix[SN_Selected_Path[Rem][BW]][SN_Selected_Path[Rem][BW+1]] + "\t" + "after\t");
		SN_BWmatrix[SN_Selected_Path[Rem][BW]][SN_Selected_Path[Rem][BW+1]] += D_VNRsEdges_BW[Rem][BW]; // to remove the BW of the embedded VNR
		System.out.println((int)SN_BWmatrix[SN_Selected_Path[Rem][BW]][SN_Selected_Path[Rem][BW+1]] + "\t" + "after\t");
		}
	}				
		
	if (SN_Selected_Path[Rem][49]==31)
					{
						System.out.print((int)SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][1]] + "\t" + "befor\t");
						SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][1]] += D_VNRsEdges_BW[Rem][0]; // to remove the BW of the embedded VNR
						System.out.println((int)SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][1]] + "\t" + "after\t");
						
						System.out.print((int)SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][2]] + "\t" + "befor\t");
						SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][2]] += D_VNRsEdges_BW[Rem][1]; // to remove the BW of the embedded VNR
						System.out.println((int)SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][2]] + "\t" + "after\t");
					}
					
			if (SN_Selected_Path[Rem][49]==311)
						{
							System.out.print((int)SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][1]] + "\t" + "befor\t");
							SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][1]] += D_VNRsEdges_BW[Rem][0]; // to remove the BW of the embedded VNR
							System.out.println((int)SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][1]] + "\t" + "after\t");
							
							System.out.print((int)SN_BWmatrix[SN_Selected_Path[Rem][1]][SN_Selected_Path[Rem][2]] + "\t" + "befor\t");
							SN_BWmatrix[SN_Selected_Path[Rem][1]][SN_Selected_Path[Rem][2]] += D_VNRsEdges_BW[Rem][1]; // to remove the BW of the embedded VNR
							System.out.println((int)SN_BWmatrix[SN_Selected_Path[Rem][1]][SN_Selected_Path[Rem][2]] + "\t" + "after\t");
							
							System.out.print((int)SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][2]] + "\t" + "befor\t");
							SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][2]] += D_VNRsEdges_BW[Rem][2]; // to remove the BW of the embedded VNR
							System.out.println((int)SN_BWmatrix[SN_Selected_Path[Rem][0]][SN_Selected_Path[Rem][2]] + "\t" + "after\t");

						}
	
	/*
	 * This part updates the SN edges of topologies 4-10, by removing the VNR's edges
	 * after they are expired.
	 */
	
			if (SN_Selected_Path[Rem][49]==4)
			{	
				int exp4=Tjp4[SN_Selected_Path[Rem][47]];
				
				for (int ibw = 0 ; ibw < SN_Selected_Path[Rem][46]; ibw++)	
				{
					if (TsE4[exp4+1][ibw]==-1 && 
						TsE4[exp4+2][ibw]==-1)
					{
						System.out.println("There was no edge here to update in the original topology-----------");
					}else					
					if (TsE4[exp4+1][ibw]!=-1 && 
						TsE4[exp4+2][ibw]!=-1)
					{
						
					System.out.print(TsE4[exp4+1][ibw] + "-> " + 
	                                 TsE4[exp4+2][ibw] + "-> " +
	                                 (int)SN_BWmatrix[TsE4[exp4+1][ibw]]
							                         [TsE4[exp4+2][ibw]] + "\t" + "befor\t");					
							
						SN_BWmatrix[TsE4[exp4+1][ibw]]
								   [TsE4[exp4+2][ibw]] += D_VNRsEdges_BW[Rem][ibw];
										
						
					System.out.print(TsE4[exp4+1][ibw] + "-> " + 
	                                 TsE4[exp4+2][ibw] + "-> " +
	                                 (int)SN_BWmatrix[TsE4[exp4+1][ibw]]
					                                 [TsE4[exp4+2][ibw]] + "\t" + "after\t" +
					         "DemBW\t" + (int)D_VNRsEdges_BW[Rem][ibw]);
					
					System.out.println();

					}//End of if (TsE4[Tjp4[SN_Selected_Path[Rem][47]+1]][ibw]!=-1 &&
				}// End of ibw for loop					
			}// End of if (SN_Selected_Path[Rem][49]==4)
	

			
			if (SN_Selected_Path[Rem][49]==5)
			{	
				int exp5=Tjp5[SN_Selected_Path[Rem][47]];
				
				for (int ibw = 0 ; ibw < SN_Selected_Path[Rem][46]; ibw++)	
				{
					if (TsE5[exp5+1][ibw]==-1 && 
						TsE5[exp5+2][ibw]==-1)
					{
						System.out.println("There was no edge here to update in the original topology-----------");
					}else					
					if (TsE5[exp5+1][ibw]!=-1 && 
						TsE5[exp5+2][ibw]!=-1)
					{
						
					System.out.print(TsE5[exp5+1][ibw] + "-> " + 
	                                 TsE5[exp5+2][ibw] + "-> " +
	                                 (int)SN_BWmatrix[TsE5[exp5+1][ibw]]
							                         [TsE5[exp5+2][ibw]] + "\t" + "befor\t");					
							
						SN_BWmatrix[TsE5[exp5+1][ibw]]
								   [TsE5[exp5+2][ibw]] += D_VNRsEdges_BW[Rem][ibw];
										
						
					System.out.print(TsE5[exp5+1][ibw] + "-> " + 
	                                 TsE5[exp5+2][ibw] + "-> " +
	                                 (int)SN_BWmatrix[TsE5[exp5+1][ibw]]
					                                 [TsE5[exp5+2][ibw]] + "\t" + "after\t" +
					         "DemBW\t" + (int)D_VNRsEdges_BW[Rem][ibw]);
					
					System.out.println();

					}//End of if (TsE5[Tjp5[SN_Selected_Path[Rem][47]+1]][ibw]!=-1 &&
				}// End of ibw for loop					
			}// End of if (SN_Selected_Path[Rem][49]==5)
			
			if (SN_Selected_Path[Rem][49]==6)
			{	
				int exp6=Tjp6[SN_Selected_Path[Rem][47]];
				
				for (int ibw = 0 ; ibw < SN_Selected_Path[Rem][46]; ibw++)	
				{
					if (TsE6[exp6+1][ibw]==-1 && 
						TsE6[exp6+2][ibw]==-1)
					{
						System.out.println("There was no edge here to update in the original topology-----------");
					}else					
					if (TsE6[exp6+1][ibw]!=-1 && 
						TsE6[exp6+2][ibw]!=-1)
					{
						
					System.out.print(TsE6[exp6+1][ibw] + "-> " + 
		                             TsE6[exp6+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE6[exp6+1][ibw]]
							                         [TsE6[exp6+2][ibw]] + "\t" + "befor\t");					
							
						SN_BWmatrix[TsE6[exp6+1][ibw]]
								   [TsE6[exp6+2][ibw]] += D_VNRsEdges_BW[Rem][ibw];
										
						
					System.out.print(TsE6[exp6+1][ibw] + "-> " + 
		                             TsE6[exp6+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE6[exp6+1][ibw]]
					                                 [TsE6[exp6+2][ibw]] + "\t" + "after\t" +
					         "DemBW\t" + (int)D_VNRsEdges_BW[Rem][ibw]);
					
					System.out.println();

					}//End of if (TsE6[Tjp6[SN_Selected_Path[Rem][47]+1]][ibw]!=-1 &&
				}// End of ibw for loop					
			}// End of if (SN_Selected_Path[Rem][49]==6)}

			if (SN_Selected_Path[Rem][49]==7)
			{	
				int exp7=Tjp7[SN_Selected_Path[Rem][47]];
				
				for (int ibw = 0 ; ibw < SN_Selected_Path[Rem][46]; ibw++)	
				{
					if (TsE7[exp7+1][ibw]==-1 && 
						TsE7[exp7+2][ibw]==-1)
					{
						System.out.println("There was no edge here to update in the original topology-----------");
					}else					
					if (TsE7[exp7+1][ibw]!=-1 && 
						TsE7[exp7+2][ibw]!=-1)
					{
						
					System.out.print(TsE7[exp7+1][ibw] + "-> " + 
		                             TsE7[exp7+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE7[exp7+1][ibw]]
							                         [TsE7[exp7+2][ibw]] + "\t" + "befor\t");					
							
						SN_BWmatrix[TsE7[exp7+1][ibw]]
								   [TsE7[exp7+2][ibw]] += D_VNRsEdges_BW[Rem][ibw];
										
						
					System.out.print(TsE7[exp7+1][ibw] + "-> " + 
		                             TsE7[exp7+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE7[exp7+1][ibw]]
					                                 [TsE7[exp7+2][ibw]] + "\t" + "after\t" +
					         "DemBW\t" + (int)D_VNRsEdges_BW[Rem][ibw]);
					
					System.out.println();

					}//End of if (TsE7[Tjp7[SN_Selected_Path[Rem][47]+1]][ibw]!=-1 &&
				}// End of ibw for loop					
			}// End of if (SN_Selected_Path[Rem][49]==7)}

			if (SN_Selected_Path[Rem][49]==8)
			{	
				int exp8=Tjp8[SN_Selected_Path[Rem][47]];
				
				for (int ibw = 0 ; ibw < SN_Selected_Path[Rem][46]; ibw++)	
				{
					if (TsE8[exp8+1][ibw]==-1 && 
						TsE8[exp8+2][ibw]==-1)
					{
						System.out.println("There was no edge here to update in the original topology-----------");
					}else					
					if (TsE8[exp8+1][ibw]!=-1 && 
						TsE8[exp8+2][ibw]!=-1)
					{
						
					System.out.print(TsE8[exp8+1][ibw] + "-> " + 
		                             TsE8[exp8+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE8[exp8+1][ibw]]
							                         [TsE8[exp8+2][ibw]] + "\t" + "befor\t");					
							
						SN_BWmatrix[TsE8[exp8+1][ibw]]
								   [TsE8[exp8+2][ibw]] += D_VNRsEdges_BW[Rem][ibw];
										
						
					System.out.print(TsE8[exp8+1][ibw] + "-> " + 
		                             TsE8[exp8+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE8[exp8+1][ibw]]
					                                 [TsE8[exp8+2][ibw]] + "\t" + "after\t" +
					         "DemBW\t" + (int)D_VNRsEdges_BW[Rem][ibw]);
					
					System.out.println();

					}//End of if (TsE8[Tjp8[SN_Selected_Path[Rem][48]+1]][ibw]!=-1 &&
				}// End of ibw for loop					
			}// End of if (SN_Selected_Path[Rem][49]==8)}

			if (SN_Selected_Path[Rem][49]==9)
			{	
				int exp9=Tjp9[SN_Selected_Path[Rem][47]];
				
				for (int ibw = 0 ; ibw < SN_Selected_Path[Rem][46]; ibw++)	
				{
					if (TsE9[exp9+1][ibw]==-1 && 
						TsE9[exp9+2][ibw]==-1)
					{
						System.out.println("There was no edge here to update in the original topology-----------");
					}else					
					if (TsE9[exp9+1][ibw]!=-1 && 
						TsE9[exp9+2][ibw]!=-1)
					{
						
					System.out.print(TsE9[exp9+1][ibw] + "-> " + 
		                             TsE9[exp9+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE9[exp9+1][ibw]]
							                         [TsE9[exp9+2][ibw]] + "\t" + "befor\t");					
							
						SN_BWmatrix[TsE9[exp9+1][ibw]]
								   [TsE9[exp9+2][ibw]] += D_VNRsEdges_BW[Rem][ibw];
										
						
					System.out.print(TsE9[exp9+1][ibw] + "-> " + 
		                             TsE9[exp9+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE9[exp9+1][ibw]]
					                                 [TsE9[exp9+2][ibw]] + "\t" + "after\t" +
					         "DemBW\t" + (int)D_VNRsEdges_BW[Rem][ibw]);
					
					System.out.println();

					}//End of if (TsE9[Tjp9[SN_Selected_Path[Rem][49]+1]][ibw]!=-1 &&
				}// End of ibw for loop					
			}// End of if (SN_Selected_Path[Rem][49]==9)}

			if (SN_Selected_Path[Rem][49]==10)
			{	
				int exp10=Tjp10[SN_Selected_Path[Rem][47]];
				
				for (int ibw = 0 ; ibw < SN_Selected_Path[Rem][46]; ibw++)	
				{
					if (TsE10[exp10+1][ibw]==-1 && 
						TsE10[exp10+2][ibw]==-1)
					{
						System.out.println("There was no edge here to update in the original topology-----------");
					}else					
					if (TsE10[exp10+1][ibw]!=-1 && 
						TsE10[exp10+2][ibw]!=-1)
					{
						
					System.out.print(TsE10[exp10+1][ibw] + "-> " + 
		                             TsE10[exp10+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE10[exp10+1][ibw]]
							                         [TsE10[exp10+2][ibw]] + "\t" + "befor\t");					
							
						SN_BWmatrix[TsE10[exp10+1][ibw]]
								   [TsE10[exp10+2][ibw]] += D_VNRsEdges_BW[Rem][ibw];
										
						
					System.out.print(TsE10[exp10+1][ibw] + "-> " + 
		                             TsE10[exp10+2][ibw] + "-> " +
		                             (int)SN_BWmatrix[TsE10[exp10+1][ibw]]
					                                 [TsE10[exp10+2][ibw]] + "\t" + "after\t" +
					         "DemBW\t" + (int)D_VNRsEdges_BW[Rem][ibw]);
					
					System.out.println();

					}//End of if (TsE10[Tjp10[SN_Selected_Path[Rem][49]+1]][ibw]!=-1 &&
				}// End of ibw for loop					
			}// End of if (SN_Selected_Path[Rem][49]==10)}


			
		
	
	Rem++; // This is a counter to point to the VNR that will be removed. It increases inside one step during the i-4-for loop.

}// End of for (int i=0 ; i<4 ; i++) that compensates for 4 VNRs each 500 time unites



} //End of if ( (VNR % 5) == 0 ) 

} // End of if ( VNR >= 20 )





//-------------------------------------------------------------------------------------------------------------------- 
//------------- This sections finds Power Consumption of the SN nodes that are turned off ----------------------------
//--------------------------------------------------------------------------------------------------------------------

for (int SNNODES = 0 ; SNNODES < SN_Nodes ; SNNODES++ )
{
/*  if (SN_All_Nodes[SNNODES] == SN_Selected_path_Nodes[Nodeinselectedpath])
{
//System.out.println("---- 1st if -----" + Nodeinselectedpath);
Nodeinselectedpath++; // This gives the SN Node number in the selected path 				
} else 
{
*/
if ((SN_Nodes_Current_PCons[SNNODES]) <= Min_PWCONS )
 {					  	
 	SN_Nodes_tobe_Turnedoff[Turned_off_nodes] = SN_All_Nodes[SNNODES]; // This is gives the SN number that is not in the selected path which should be turned off.
 	Turnedoff_SN_Nodes[Turned_off_nodes] = SN_Nodes_tobe_Turnedoff[Turned_off_nodes];
 	Turned_off_nodes++;					  	
 }// End of second if
// }// End of else
}// End of for_loop "SNNODES"	
		
NumberofTurndoffSNNodes = Turned_off_nodes; 

SNOffNodes[VNR] = NumberofTurndoffSNNodes;

/*
*  save the number of turned off SN nodes to be used in updated their power   		 
*  consumption when calculating the power saving ratio at the end of the code.
*/

/*	System.out.println("---------------------------------------------------");
System.out.println("SN nodes that are turned off  ");
 		
for (int SNoffnodes = 0 ; SNoffnodes < Turned_off_nodes ; SNoffnodes++ )
System.out.println(" "+ Turnedoff_SN_Nodes[SNoffnodes]);
*/

//System.out.println();	  	
//System.out.print( "  Power Consumption of the SN nodes that are turned off  ");
					  	
for (int OffNodes = 0 ; OffNodes < Turned_off_nodes ; OffNodes++)
{	
Turnoff_SN_Nodes_PCons[OffNodes] = SN_Nodes_idle_PCons[SN_Nodes_tobe_Turnedoff[OffNodes]]; // A matrix to save the power consumption of the SN nodes that are turned off.
//System.out.println(" Node (-" + SN_Nodes_tobe_Turnedoff[OffNodes] + "-) " + Turnoff_SN_Nodes_PowerConsumption[OffNodes] );

PC_forSN_TurnedOff_Nodes	    += Turnoff_SN_Nodes_PCons[OffNodes]; // Total sum of the power consumption in all SN nodes that are turned off.
					  		
} // End of "OffNodes" for_loop.
//	System.out.println();
//System.out.println("---------------------------------------------------");

// ---------------------------------------------------------------------------
					  
Turnedoffnodes = Turned_off_nodes;
Selected_paths_PC = PC_forSN_TurnedOff_Nodes; // This saves the sum of the consumed power in the selected path in the global variable Selected_path_PC
//  System.out.println("---------------------");			  	
//  System.out.println("Power Consumption of the Turnned Off SN Nodes-----------------------  =  " +  Selected_paths_PC);
//----------------------------------------------------------------------------

for (int ii=0 ; ii < SN_Nodes ; ii++)
{
	Tot_SN_consumed_power += SN_Nodes_Current_PCons[ii];
//		System.out.println("SN Current Total Power Consumption\t(" + ii +")  =\t"  + Tot_SN_consumed_power + 
//                       " SN_Nodes_Current_PCons =\t" + SN_Nodes_Current_PCons[ii] );

}

//------------------------------------------------------------------------------------------------------------------------
//------------------------------------   Calculating Power Saving Ratio   -----------------------------------------------

	SN_Total_PW_Consumption[VNR] = Tot_SN_consumed_power - Selected_paths_PC;	// this save the SN total PC in all SN in this iteration
				
	Saved_Power = Selected_paths_PC;
	
	//System.out.println("SN nodes to be turned off total power consumption = " + Selected_paths_PC + "    after Embedding VNR number (- " + Tot_Num_of_VNRs + " -)  ");
	//System.out.println("SN Current Total Power Consumption in all nodes   = " + Tot_SN_consumed_power);
	
	//System.out.println(" Power Saving Ratio after Embedding VNR number---------- (-" + Tot_Num_of_VNRs + "-)  = " + Power_Saving_Ratio);
	//System.out.println(" Updated Power Consumption of the Turned Off SN Nodes after VNRs expire  = " + Selected_paths_PC);
	//System.out.println(" Updated Total Power Consumption in all SN nodes after VNRs expire        = " + Tot_SN_consumed_power);
	
	MaxPWCapacity[VNR] = MaxPWCap;
	PowerSaving[VNR] = Saved_Power;

//-------------------------------------------- End of Calculating Power Saving Ratio  -----------------------------------
//-----------------------------------------------------------------------------------------------------------------------	

	VNRs_Acceptance_Ratio[VNR] = (((MaximumVNRs - rejectedVNRs)/ MaximumVNRs))*100;  // the acceptance ratio formula
	System.out.println();
	System.out.println("-----------------------------------   VNRs_Acceptance_Ratio   ---------------------------  ");
	System.out.println();        
	System.out.println("  VNRs_Acceptance_Ratio =\t" + VNRs_Acceptance_Ratio[VNR] + " %\t" + "Total number of VNRs =\t" + Tot_Num_of_VNRs + "\t" + "Rejected VNRs  =\t" + rejectedVNRs);

	Sum_SN_Utilization = 0;
	
	for (int ii=0 ; ii < SN_Nodes ; ii++)
	{
		SN_Nodes_CPU_Cur_Util[ii] = (100*( SN_Nodes_MAX_CPU_Cap[ii] - SN_Nodes_CPU_Cons[ii])/ SN_Nodes_MAX_CPU_Cap[ii] );
		//Consumed_CPU             += (SN_Nodes_MAX_CPU_Cap[ii] - SN_Nodes_CPUCapacity[ii]); // calculate the total consumed CPU
		Sum_SN_Utilization       += SN_Nodes_CPU_Cur_Util[ii];
		Sum_Nodes_CPU_Cons		 += (SN_Nodes_MAX_CPU_Cap[ii] - SN_Nodes_CPU_Cons[ii]);
		//System.out.println("  SN_Node -(" + ii + ")- CPU Current Utilization  =  \t" + SN_Nodes_CPU_Cur_Util[ii] + " \t " + " Sum_SN_Utilization \t" + Sum_SN_Utilization);
	}
	
	nodes_utilization            = (Sum_SN_Utilization / SN_Nodes ); // calculate the average CPU utilization per each SN node.
	nodes_average_CPU_utili[VNR] = nodes_utilization;
	Consumed_CPU[VNR]            = Sum_Nodes_CPU_Cons;
	
	//System.out.println("nodes_average_CPU_utili[VNR]\t" + nodes_average_CPU_utili[VNR]);
	
	Sum_edges_BW = 0;
	edges        = 0;
	
	for (int i=0 ; i<SN_Nodes ; i++)
		for (int j=0 ; j<SN_Nodes ; j++)
		{
			if (adjacencyMatrix[i][j] == 1)
			{   
				SN_BW_Util [i][j]  = (100*(SN_Edges_MAX_BW_Cap[i][j] - SN_BWmatrix[i][j]) / SN_Edges_MAX_BW_Cap[i][j] );	
				//Consumed_BW     += (SN_Edges_MAX_BW_Cap[i][j] - SN_BWmatrix[i][j]); // calculate the total consumed BW .
				if(SN_BW_Util [i][j]>0)
					edges++;
				Sum_edges_BW      += SN_BW_Util[i][j];
				Sum_edges_BW_Cons += (SN_Edges_MAX_BW_Cap[i][j] - SN_BWmatrix[i][j]);
		/*		System.out.println("   SN_Edge "             + i + "-->" + j + 
						                                     " BW    =\t" + SN_BWmatrix[i][j] + 
						                                     " Max BW =\t" + Maxsbw + 
						                                     " SN_Edge_Utilization\t" + i + "-->" + j + "  =\t" + SN_BW_Util [i][j] + "\t" + 
						                                     " Sum_edges_BW\t"+ Sum_edges_BW);
		*/	
			}	
		//System.out.println();
	    }		

	Consumed_BW[VNR]			= Sum_edges_BW_Cons;
	edges_utilization           = (Sum_edges_BW/edges);// this calculates the average utilization per each SN edge.
	edges_average_BW_utili[VNR] = edges_utilization ;
	
	Embedding_cost        [VNR]	= (Consumed_CPU[VNR]) + (Consumed_BW[VNR]); // this is where we calculate the embedding cost. Alfa and Beta are defined as equal to 0.5.
	double Embedding_VNRcost	= Embedding_cost[VNR]/(10*Time[Tloop]); // this is where we calculate the embedding cost. Alfa and Beta are defined as equal to 0.5.
	double Revnew	= Embedding_cost[VNR]/successfulvnr; // this is where we calculate the embedding cost. Alfa and Beta are defined as equal to 0.5.

	double PC = (SN_Total_PW_Consumption[VNR]- (165 *(SN_Nodes-Turned_off_nodes)))/(4*(SN_Nodes-Turned_off_nodes));

		
	VNRs_output[VNR][0]  = PC;
	VNRs_output[VNR][1]  = (100* (SN_Total_PW_Consumption[VNR]/Tot_SN_consumed_power)); //SN_Total_PW_Consumption_withTurnOFF[VNR];
	VNRs_output[VNR][2]  = (100* (PowerSaving     [VNR]/Tot_SN_consumed_power));
	VNRs_output[VNR][3]  = VNRs_Acceptance_Ratio  [VNR];
	VNRs_output[VNR][4]  = Embedding_cost         [VNR];
	VNRs_output[VNR][5]  = nodes_average_CPU_utili[VNR];
	VNRs_output[VNR][6]  = edges_average_BW_utili [VNR];
	VNRs_output[VNR][7]  = SNOffNodes             [VNR];
	VNRs_output[VNR][8]  = VNR;
	VNRs_output[VNR][9]  = NumberofnodesinVNR     [VNR];
	VNRs_output[VNR][10] = totalTime              [VNR];
	VNRs_output[VNR][11] = Load[Lloop];
	VNRs_output[VNR][12] = Time[Tloop];
	VNRs_output[VNR][13] = Embedding_VNRcost;
	VNRs_output[VNR][14] = Revnew;

	
		System.out.println(Runs+"\t" +  VNRs_output[VNR][12]+"\t T\t"  +
		VNRs_output[VNR][11]+"\t L\t"  +
 	    VNRs_output[VNR][8] +"\t VNR\t"+
						                VNRs_output[VNR][9] +"\t VN\t" +		             
		VNRs_output[VNR][10]+"\t ET\t" +
		VNRs_output[VNR][3] +"\t AR\t" +
		VNRs_output[VNR][7] +"\t Of\t" +
   (int)VNRs_output[VNR][0] +"\t TC\t" +
   (int)VNRs_output[VNR][1] +"\t C\t"  +
   (int)VNRs_output[VNR][2] +"\t S\t"  +
   (int)VNRs_output[VNR][4] +"\t Co\t" +
   (int)VNRs_output[VNR][5] +"\t CU\t" +
   (int)VNRs_output[VNR][6] +"\t BU\t"+
   (int)VNRs_output[VNR][13] +"\t CoVNR\t"+
   (int)VNRs_output[VNR][14] +"\t Rev\t");
		
	
	
	
	try {	
		BufferedWriter writerVNROUT =						
				new BufferedWriter ( new FileWriter("Average-1000ch-New_new_Chawdhury.txt", true));
					
					writerVNROUT.write(String.valueOf(Runs + "\t" + VNR + "\t" + VNRs_output[VNR][0]  + " \t " + 
	             VNRs_output[VNR][1]  + " \t " + 
			     VNRs_output[VNR][2]  + " \t " + 
			     VNRs_output[VNR][3]  + " \t " +
			     VNRs_output[VNR][4]  + " \t " +
			     VNRs_output[VNR][5]  + " \t " +
			     VNRs_output[VNR][6]  + " \t " +
			     VNRs_output[VNR][7]  + " \t " +
			     VNRs_output[VNR][8]  + " \t " +
			     VNRs_output[VNR][9]  + " \t " +
			     VNRs_output[VNR][10] + " \t " +
				 VNRs_output[VNR][11] + " \t " +
				 VNRs_output[VNR][12] + " \t " +
				 VNRs_output[VNR][13] + " \t " +
				 VNRs_output[VNR][14]));
	writerVNROUT.newLine();				
					
				writerVNROUT.close();
		
			} catch (IOException e) 
				{
					e.printStackTrace();
				}					
	
}// End of VNR for loop that injected the VNRs.

try {	
BufferedWriter writerVNROUT =						
	new BufferedWriter ( new FileWriter("Average-1000ch-New_new_ChawdhuryFinal.txt", true));
		
		writerVNROUT.write(String.valueOf(Runs + "\t" + VNRs_output[MaximumVNRs-1][0] + " \t " + 
						VNRs_output[MaximumVNRs-1][1] + " \t " + 
						VNRs_output[MaximumVNRs-1][2] + " \t " + 
						VNRs_output[MaximumVNRs-1][3] + " \t " +
						VNRs_output[MaximumVNRs-1][4] + " \t " +
						VNRs_output[MaximumVNRs-1][5] + " \t " +
						VNRs_output[MaximumVNRs-1][6] + " \t " +
						VNRs_output[MaximumVNRs-1][7] + " \t " +
						VNRs_output[MaximumVNRs-1][8] + " \t " +
						VNRs_output[MaximumVNRs-1][9] + " \t " +
						VNRs_output[MaximumVNRs-1][10]+ " \t " +
						VNRs_output[MaximumVNRs-1][11] + " \t " +
						VNRs_output[MaximumVNRs-1][12] + " \t " +
					    VNRs_output[MaximumVNRs-1][13] + " \t " +
						VNRs_output[MaximumVNRs-1][14]));
						writerVNROUT.newLine();				
		
	writerVNROUT.close();

} catch (IOException e) 
	{
		e.printStackTrace();
	}	


/*
for (int w=0 ; w<MaximumVNRs ; w++)
System.out.println("R\t" + Runs + " \t " + "VNR\t" + w +
                   "TP\t"  + VNRs_output[w][0] + " \t " +
                   "PO\t"  + VNRs_output[w][1] + " \t " +
                   "PN\t"  + VNRs_output[w][2] + " \t " +
                   "PS\t"  + VNRs_output[w][3] + " \t " +
                   "AR\t"  + VNRs_output[w][4] + " \t " +
                   "Co\t"  + VNRs_output[w][5] + " \t " +
                   "CU\t"  + VNRs_output[w][6] + " \t " +
				   "BU\t"  + VNRs_output[w][7] + " \t " +
				   "OF\t"  + VNRs_output[w][8]);

*/	

for (int i=0 ; i<MaximumVNRs ; i++)
System.out.println(Runs+"\t" +  VNRs_output[i][12]+"\t T\t"  +
							VNRs_output[i][11]+"\t L\t"  +
					 	    VNRs_output[i][8] +"\t VNR\t"+
			                VNRs_output[i][9] +"\t VN\t" +		             
							VNRs_output[i][10]+"\t ET\t" +
							VNRs_output[i][3] +"\t AR\t" +
							VNRs_output[i][7] +"\t Of\t" +
					   (int)VNRs_output[i][0] +"\t TC\t" +
					   (int)VNRs_output[i][1] +"\t C\t"  +
					   (int)VNRs_output[i][2] +"\t S\t"  +
					    	VNRs_output[i][4] +"\t Co\t" +
							VNRs_output[i][5] +"\t CU\t" +
							VNRs_output[i][6] +"\t BU\t");


Runs_output[Runs][12]=VNRs_output[MaximumVNRs-1][12];
Runs_output[Runs][11]=VNRs_output[MaximumVNRs-1][11];
Runs_output[Runs][8]=VNRs_output[MaximumVNRs-1][8];
Runs_output[Runs][9]=VNRs_output[MaximumVNRs-1][9];
Runs_output[Runs][10]=VNRs_output[MaximumVNRs-1][10];
Runs_output[Runs][3]=VNRs_output[MaximumVNRs-1][3];
Runs_output[Runs][7]=VNRs_output[MaximumVNRs-1][7];
Runs_output[Runs][0]=VNRs_output[MaximumVNRs-1][0];
Runs_output[Runs][1]=VNRs_output[MaximumVNRs-1][1];
Runs_output[Runs][2]=VNRs_output[MaximumVNRs-1][2];
Runs_output[Runs][4]=VNRs_output[MaximumVNRs-1][4];
Runs_output[Runs][5]=VNRs_output[MaximumVNRs-1][5];
Runs_output[Runs][6]=VNRs_output[MaximumVNRs-1][6];


try {	
BufferedWriter writerVNROUT =						
	new BufferedWriter ( new FileWriter("Average-1000ch-RunsFinal.txt", true));
		
		writerVNROUT.write(String.valueOf(Runs + "\t" + Runs_output[Runs][0] + " \t " + 
				Runs_output[Runs][1] + " \t " + 
				Runs_output[Runs][2] + " \t " + 
				Runs_output[Runs][3] + " \t " +
				Runs_output[Runs][4] + " \t " +
				Runs_output[Runs][5] + " \t " +
				Runs_output[Runs][6] + " \t " +
				Runs_output[Runs][7] + " \t " +
				Runs_output[Runs][8] + " \t " +
				Runs_output[Runs][9] + " \t " +
				Runs_output[Runs][10]+ " \t " +
				Runs_output[Runs][11] + " \t " +
				Runs_output[Runs][12]));
						
		
		writerVNROUT.newLine();				
		
	writerVNROUT.close();

} catch (IOException e) 
	{
		e.printStackTrace();
	}


} // End of for (int Runs=0 ; Runs< Max_Runs ; Runs++ )

	


} // End of for (int Tloop=0 ; Tloop<Time.length ; Tloop++)
} // End of for (int Lloop=0 ; Lloop<Load.length ; Lloop++)
    

	
	
	
}

}
