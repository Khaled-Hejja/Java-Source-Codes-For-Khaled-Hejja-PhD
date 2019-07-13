import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PathsBasedOnSegmentationUsingWaxman {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

			
		System.out.println("Note, all paths will be saved in separate files on the directory of the Java code");
		System.out.println("clasified according to the number of their nodes.");
		
		System.out.println("At anytime you face an error message, it could be due to no paths found. So try again.");
		
		
		System.out.println();
		
		    Scanner sN = new Scanner(System.in);
	        System.out.println("Enter number of Nodes in the Physical Network (Please select > 10) . ---MUST BE INTEGER and POSITIVE---");
	        int NetNodes = sN.nextInt();

	        Scanner ALPHA = new Scanner(System.in);
	        System.out.println("For Waxman Topology. Please enter value of ALPHA (Recommended 0.6). Please it must be between (0.0-1.0)");
	        double Alpha = ALPHA.nextDouble();

	        Scanner BETA = new Scanner(System.in);
	        System.out.println("For Waxman Topology. Please enter value of Beta (Recommended 0.7). Please it must be between (0.0-1.0)");
	        double Beta = BETA.nextDouble();
	        
	        Scanner THRESHOLD = new Scanner(System.in);
	        System.out.println("For Waxman Topology, please enter value of the probability of two nodes been connected (Recommended 0.2). Please it must be between (0.0-1.0)");
	        double Threshold = THRESHOLD.nextDouble();
	        
	        
	        
	        int SN_Nodes	      = NetNodes;
	        double alpha		  = Alpha;
	        double beta		      = Beta;
	        double Prob_Thrreshod = Threshold;        

	
	//-------------------------------------------------------------------------------
	//--------------------------------- Create SN ----------------------------------
	//-------------------------------------------------------------------------------


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

	
	System.out.println("Please wait until all paths are saved, and the finish statement appears.");


	//---------------- THis section is to generate the two nodes edges from the Adjacency Matrix  ------------
	
	int SNP2factrowsize = 5; // to specify the initial rows size in the 3,4,5 paths types matrices.
	int[][] SNP2nodes = new int[adjacencyMatrix.length*20][2];
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
				//System.out.println( SN_P2_nodes[i][0] + " , "  + "\t" + SN_P2_nodes[i][1]);
			
			}		
		
			//System.out.println();
			
			
			for (int i=0 ; i<SNP2 ; i++)
				  for (int j=0 ; j<2 ; j++)
					{					
						try {	
							BufferedWriter writerSFCOUT =						
									new BufferedWriter ( new FileWriter("P2.txt", true));
																		
										writerSFCOUT.write(String.valueOf(
														SN_P2_nodes[i][0] + " \t " + 
														SN_P2_nodes[i][1]  ));
														writerSFCOUT.newLine();				
										
									writerSFCOUT.close();
							
								} catch (IOException e) 
									{
										e.printStackTrace();
									}					
					      }
	
		
		// -------------------------   End of generating the SN_P2_nodes paths from the Adjacency Matrix   ----------- 
			
	
			int[] sorted_CPUsum_paths2 = new int[SN_P2_nodes.length];// this matrix saves the edges of type two nodes
			int a2=0;
			
			for (int a=0 ; a<SN_P2_nodes.length ; a++ )
			{
				sorted_CPUsum_paths2[a]=a2;
				a2++;
				//System.out.println(sorted_CPUsum_paths2[a]);
			} // end of for loop to print all paths of type 2 nodes


//-----------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------
//------------------------------------------ Paths of Type - 3 - Nodes --------------------------------------------------

			int SN =  SN_P2_nodes.length * SNP2factrowsize;
			int[][] PT3 = new int[SN*100][3];// a matrix for paths of type 3 nodes
			
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
					if (PT3[k3][j3+1]== SN_P2_nodes[i+1][j3])
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
					//System.out.print(SN_P3_nodes[i][jj]+ "," +"\t"); // print the path of type 3 nodes
				}
			//System.out.println();
			}
			
			for (int i=0 ; i<k3 ; i++)
				  for (int j=0 ; j<3 ; j++)
					{					
						try {	
							BufferedWriter writerSFCOUT =						
									new BufferedWriter ( new FileWriter("P3.txt", true));
																		
										writerSFCOUT.write(String.valueOf(
														SN_P3_nodes[i][0] + " \t " + 
														SN_P3_nodes[i][1] + " \t " + 
														SN_P3_nodes[i][2] ));
														writerSFCOUT.newLine();				
										
									writerSFCOUT.close();
							
								} catch (IOException e) 
									{
										e.printStackTrace();
									}					
					      }
	
			
			
					
			int[] sorted_CPUsum_paths3 = new int[SN_P3_nodes.length]; // a matrix to save the path numbers of formulated type 3 nodes
			int a3=0;
			
			for (int a=0 ; a<k3 ; a++ )
			{
				sorted_CPUsum_paths3[a]=a3;// initialize the matrix that contains the path numbers of the newly formulated paths of type 3 nodes
				a3++;
				//System.out.println(sorted_CPUsum_paths3[a]); // print the path numbers of newly formulated paths type 3 nodes.
			}			

			//---------------------------------- Paths of Type - 4 - Nodes -------------------------------------
		
			int SNP4factrowsize = 2; // to specify the initial rows size in the 3,4,5 paths types matrices.
			int[][] PT4 = new int[SN*100][4];// a matrix for paths of type 4 nodes
		
			
			int k4 = 0 ;// counter used in indexing new paths of type 4 nodes 
			int j4 = 0 ;// counter for the nodes in the matrix of paths type 4 nodes
			
			PT4[k4][j4]   = SN_P3_nodes[k4][j4];  // a matrix to save the first node of the path to start searching
			PT4[k4][j4+1] = SN_P3_nodes[k4][j4+1];// a matrix to save the second node of the path to start searching
			PT4[k4][j4+2] = SN_P3_nodes[k4][j4+2];// a matrix to save the third node of the path to start searching
		/*	
			System.out.println(" kk  = " + k4 + " jj = " + j4 );
			System.out.println(PT4[k4][j4]+ "   " + 
			                   PT4[k4][j4+1]+ "   "+ 
			                   PT4[k4][j4+2]);
          */
			for (int z=0 ; z<SN_P3_nodes.length; z++)
			{			
			for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
			{	
				PT4[k4][j4]   = SN_P3_nodes[z][j4];
				PT4[k4][j4+1] = SN_P3_nodes[z][j4+1];
				PT4[k4][j4+2] = SN_P3_nodes[z][j4+2];
				
				//System.out.println(" k4  = " + k4 + " j4 = " + j4 +  " z = "+ z + " i =  " + i);
				//System.out.println("---------------------");		
					if (PT4[k4][j4+2]== SN_P2_nodes[i+1][j4])
					{
						
						PT4[k4][j4+3] = SN_P2_nodes[i+1][j4+1];// this saves the selected connecting node
				/*	    
						System.out.println(PT4[k4][j4]   + "   " + 
						                   PT4[k4][j4+1] + "   " + 
								           PT4[k4][j4+2] + "   " + 
						                   PT4[k4][j4+3]);
				*/	    
					    k4++;
										
						PT4[k4][j4]   = SN_P3_nodes[z][j4];  // initialize the first node in the next path
						PT4[k4][j4+1] = SN_P3_nodes[z][j4+1];// initialize the second node in the next path
						PT4[k4][j4+2] = SN_P3_nodes[z][j4+2];// initialize the third node in the next path
						
						//System.out.println(" k4  = " +k4 + " j4 = " + j4 +  " z = "+ z + " i =  " + i);
						//System.out.println("........................");		

					}else				
						if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j4+1] != 0))
						{
							continue; // go to next path in the list
						}else
							if (SN_P2_nodes[z][j4+1] != 0)
							{								
								System.out.println("next z");
							}else
								break;

			 } // end of i for loop
		
			}// end of z for loop		
			
			//System.out.println("------------------------- SN Paths Type 4 Nodes  ---------------------------------------");
	
			int[][] SN_P4_nodes = new int[k4][4];
			
			for (int i=0 ; i<k4 ; i++)
			{
				//System.out.print(" SNP4-->"+ i + "  ");
				
				for (int e=0 ; e<4 ; e++)
				{
					
					SN_P4_nodes[i][e]=PT4[i][e];              // save the newly formulated paths type 4 nodes
     			    //System.out.print(SN_P4_nodes[i][e]+ "," +"\t"); // print these paths type 4 nodes
					
				}
			//System.out.println();
			}
			
			for (int i=0 ; i<k4 ; i++)
				  for (int j=0 ; j<4 ; j++)
					{					
						try {	
							BufferedWriter writerSFCOUT =						
									new BufferedWriter ( new FileWriter("P4.txt", true));
																		
										writerSFCOUT.write(String.valueOf(
														SN_P4_nodes[i][0] + " \t " + 
														SN_P4_nodes[i][1] + " \t " + 
														SN_P4_nodes[i][2] + " \t " + 
														SN_P4_nodes[i][3] ));
														writerSFCOUT.newLine();				
										
									writerSFCOUT.close();
							
								} catch (IOException e) 
									{
										e.printStackTrace();
									}					
					      }
	
			
			
					
			int[] sorted_CPUsum_paths4 = new int[SN_P4_nodes.length]; // a matrix to save the path numbers of the newly formulated paths type 4 nodes
			int a4=0;
			
			for (int a=0 ; a<SN_P4_nodes.length ; a++ )
			{
				sorted_CPUsum_paths4[a]=a4; // this gives the path numbers 
				a4++;
				//System.out.println(sorted_CPUsum_paths4[a]); // print these paths numbers
			}				


			//---------------------------------- Paths of Type - 5 - Nodes -------------------------------------

			int k5 = 0 ;// counter used in indexing new paths of type 5 nodes 
			int j5 = 0 ;// counter for the nodes in the matrix of paths type 5 nodes
			int[][] PT5 = new int[SN*10][5];// a matrix for paths of type 5 nodes		
								
			PT5[k5][j5]   = SN_P4_nodes[k5][j5];  // a matrix to save the first node of the path to start searching
			PT5[k5][j5+1] = SN_P4_nodes[k5][j5+1];// a matrix to save the second node of the path to start searching
			PT5[k5][j5+2] = SN_P4_nodes[k5][j5+2];// a matrix to save the third node of the path to start searching
			PT5[k5][j5+3] = SN_P4_nodes[k5][j5+3];// a matrix to save the fourth node of the path to start searching
			
			//System.out.println(" k5  = " + k5 + " j5 = " + j5 );
			//System.out.println(PT5[k5][j5]+ "   " + PT5[k5][j5+1]+ "   "+ PT5[k5][j5+2]+ "   "+ PT5[k5][j5+3]);

			for (int z=0 ; z<SN_P4_nodes.length; z++)
			{			
			for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
			{	
				PT5[k5][j5]   = SN_P4_nodes[z][j5];  //  initialize the first node of the path searched
				PT5[k5][j5+1] = SN_P4_nodes[z][j5+1];//  initialize the second node of the path searched
				PT5[k5][j5+2] = SN_P4_nodes[z][j5+2];//  initialize the third node of the path searched
				PT5[k5][j5+3] = SN_P4_nodes[z][j5+3];//  initialize the fourth node of the path searched
				
				//System.out.println(" k5  = " + k5 + " j5 = " + j5 +  " z = "+ z + " i =  " + i);
				//System.out.println("---------------------");		
					if (PT5[k5][j5+3]== SN_P2_nodes[i+1][j5])
					{
						
						PT5[k5][j5+4] = SN_P2_nodes[i+1][j5+1];
				/*	    
						System.out.println(PT5[k5][j5]   + "   " + 
						                   PT5[k5][j5+1] + "   " + 
								           PT5[k5][j5+2] + "   " + 
						                   PT5[k5][j5+3]);
				*/	    
					    k5++;
										
						PT5[k5][j5]   = SN_P4_nodes[z][j5];  //  save the first node of the path searched
						PT5[k5][j5+1] = SN_P4_nodes[z][j5+1];//  save the second node of the path searched
						PT5[k5][j5+2] = SN_P4_nodes[z][j5+2];//  save the third node of the path searched
						PT5[k5][j5+3] = SN_P4_nodes[z][j5+3];//  save the fourth node of the path searched
						
						//System.out.println(" k5  = " +k5 + " j5 = " + j5 +  " z = "+ z + " i =  " + i);
						//System.out.println("........................");		

					}else				
						if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j5+1] != 0))
						{
							continue; // go to next path in the list
						}else
							if (SN_P2_nodes[z][j5+1] != 0)
							{								
								System.out.println("next z");
							}else
								break;

			 } // end of i for loop
		
			}// end of z for loop		
			
		//	System.out.println("------------------------- SN Paths Type 5 Nodes  ---------------------------------------");
	
			int[][] SN_P5_nodes = new int[k5][5]; // a matrix to save the newly formulated paths of type 5 nodes
			
			for (int i=0 ; i<k5 ; i++)
			{
		//		System.out.print(" SNP5-->"+ i + "  ");

				for (int e=0 ; e<5 ; e++)
				{
					SN_P5_nodes[i][e]= PT5[i][e];             // save the matrix of paths type 5 nodes
					//System.out.print(SN_P5_nodes[i][e]+ "," +"\t"); // print this path
					
				}
			//System.out.println();
			}
			
			for (int i=0 ; i<k5 ; i++)
				  for (int j=0 ; j<5 ; j++)
					{					
						try {	
							BufferedWriter writerSFCOUT =						
									new BufferedWriter ( new FileWriter("P5.txt", true));
																		
										writerSFCOUT.write(String.valueOf(
														SN_P5_nodes[i][0] + " \t " + 
														SN_P5_nodes[i][1] + " \t " + 
														SN_P5_nodes[i][2] + " \t " + 
														SN_P5_nodes[i][3] + " \t " +
														SN_P5_nodes[i][4] ));
														writerSFCOUT.newLine();				
										
									writerSFCOUT.close();
							
								} catch (IOException e) 
									{
										e.printStackTrace();
									}					
					      }
	
			
			
			
			
			int[] sorted_CPUsum_paths5 = new int[SN_P5_nodes.length]; // a matrix to save the path numbers of the newly formulated paths type 5 nodes
			int a5=0;
			
			for (int a=0 ; a<k5 ; a++ )
			{
				sorted_CPUsum_paths5[a]=a5; // initialize the path numbers for paths of type 5 nodes 
				a5++;
				//System.out.println(sorted_CPUsum_paths5[a]); // print this path
			}	
	
//---------------------------------- Paths of Type - 6 - Nodes -------------------------------------

			int k6 = 0 ;// counter used in indexing new paths of type 6 nodes 
			int j6 = 0 ;// counter for the nodes in the matrix of paths type 6 nodes
			int[][] PT6 = new int[SN*50][6];// a matrix for paths of type 6 nodes

			PT6[k6][j6]   = SN_P5_nodes[k6][j6];  // a matrix to save the first node of the path to start searching
			PT6[k6][j6+1] = SN_P5_nodes[k6][j6+1];// a matrix to save the second node of the path to start searching
			PT6[k6][j6+2] = SN_P5_nodes[k6][j6+2];// a matrix to save the third node of the path to start searching
			PT6[k6][j6+3] = SN_P5_nodes[k6][j6+3];// a matrix to save the fourth node of the path to start searching
			PT6[k6][j6+4] = SN_P5_nodes[k6][j6+4];// a matrix to save the fifth node of the path to start searching

			//System.out.println(" k6  = " + k6 + " j6 = " + j6 );
			//System.out.println(PT6[k6][j6]+ "   " + PT6[k6][j6+1]+ "   " + PT6[k6][j6+2]+ "   " + PT6[k6][j6+3]+ "   " + PT6[k6][j6+4]);

			for (int z=0 ; z<SN_P5_nodes.length; z++)
			{			
			for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
			{	
				PT6[k6][j6]   = SN_P5_nodes[z][j6];  //  initialize the first node of the path searched
				PT6[k6][j6+1] = SN_P5_nodes[z][j6+1];//  initialize the second node of the path searched
				PT6[k6][j6+2] = SN_P5_nodes[z][j6+2];//  initialize the third node of the path searched
				PT6[k6][j6+3] = SN_P5_nodes[z][j6+3];//  initialize the fourth node of the path searched
				PT6[k6][j6+4] = SN_P5_nodes[z][j6+4];//  initialize the fifth node of the path searched

				//System.out.println(" k5  = " + k5 + " j5 = " + j5 +  " z = "+ z + " i =  " + i);
				//System.out.println("---------------------");		
					if (PT6[k6][j6+4]== SN_P2_nodes[i+1][j6])
					{
						
						PT6[k6][j6+5] = SN_P2_nodes[i+1][j6+1];
				/*	    
						System.out.println(PT6[k6][j6]   + "   " + 
						                   PT6[k6][j6+1] + "   " + 
								           PT6[k6][j6+2] + "   " + 
						                   PT6[k6][j6+3] + "   " +
						                   PT6[k6][j6+4]);
				*/	    
					    k6++;
										
						PT6[k6][j6]   = SN_P5_nodes[z][j6];  //  save the first node of the path searched
						PT6[k6][j6+1] = SN_P5_nodes[z][j6+1];//  save the second node of the path searched
						PT6[k6][j6+2] = SN_P5_nodes[z][j6+2];//  save the third node of the path searched
						PT6[k6][j6+3] = SN_P5_nodes[z][j6+3];//  save the forth node of the path searched
						PT6[k6][j6+4] = SN_P5_nodes[z][j6+4];//  save the fifth node of the path searched

						//System.out.println(" k6  = " +k6 + " j6 = " + j6 +  " z = "+ z + " i =  " + i);
						//System.out.println("........................");		

					}else				
						if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j6+1] != 0))
						{
							continue; // go to next path in the list of paths type 3
						}else
							if (SN_P2_nodes[z][j6+1] != 0)
							{								
								System.out.println("next z");
							}else
								break;

			 } // end of i for loop
		
			}// end of z for loop		
		
			//System.out.println("------------------------- SN Paths Type 6 Nodes  ---------------------------------------");
		
			int[][] SN_P6_nodes = new int[k6][6]; // a matrix to save the newly formulated paths of type 6 nodes
			
			for (int i=0 ; i<k6 ; i++)
			{
				//System.out.print(" SNP6-->"+ i + "  ");

				for (int e=0 ; e<6 ; e++)
				{
					SN_P6_nodes[i][e]=PT6[i][e];              // save the matrix of paths type 6 nodes
					//System.out.print(SN_P6_nodes[i][e]+ "," +"\t"); // print this path
					
				}
			//System.out.println();
			}
			
			for (int i=0 ; i<k6 ; i++)
				  for (int j=0 ; j<6 ; j++)
					{					
						try {	
							BufferedWriter writerSFCOUT =						
									new BufferedWriter ( new FileWriter("P6.txt", true));
																		
										writerSFCOUT.write(String.valueOf(
														SN_P6_nodes[i][0] + " \t " + 
														SN_P6_nodes[i][1] + " \t " + 
														SN_P6_nodes[i][2] + " \t " + 
														SN_P6_nodes[i][3] + " \t " +
														SN_P6_nodes[i][4] + " \t " +
														SN_P6_nodes[i][5] ));
														writerSFCOUT.newLine();				
										
									writerSFCOUT.close();
							
								} catch (IOException e) 
									{
										e.printStackTrace();
									}					
					      }
	
			
			
			
			
			int[] sorted_CPUsum_paths6 = new int[SN_P6_nodes.length]; // a matrix to save the path numbers of the newly formulated paths type 6 nodes
			int a6=0;
			
			for (int a=0 ; a<SN_P6_nodes.length ; a++ )
			{
				sorted_CPUsum_paths6[a]=a6; // initialize the path numbers for paths of type 6 nodes 
				a6++;
				//System.out.println(sorted_CPUsum_paths6[a]); // print this path
			}


			
//---------------------------------- Paths of Type - 7 - Nodes -------------------------------------

			int k7 = 0 ;// counter used in indexing new paths of type 7 nodes 
			int j7 = 0 ;// counter for the nodes in the matrix of paths type 7 nodes
			int[][] PT7 = new int[SN*50][7];// a matrix for paths of type 7 nodes

			PT7[k7][j7]   = SN_P6_nodes[k7][j7];  // a matrix to save the first node of the path to start searching
			PT7[k7][j7+1] = SN_P6_nodes[k7][j7+1];// a matrix to save the second node of the path to start searching
			PT7[k7][j7+2] = SN_P6_nodes[k7][j7+2];// a matrix to save the third node of the path to start searching
			PT7[k7][j7+3] = SN_P6_nodes[k7][j7+3];// a matrix to save the fourth node of the path to start searching
			PT7[k7][j7+4] = SN_P6_nodes[k7][j7+4];// a matrix to save the fifth node of the path to start searching
			PT7[k7][j7+5] = SN_P6_nodes[k7][j7+5];// a matrix to save the fifth node of the path to start searching

			//System.out.println(" k7  = " + k7 + " j7 = " + j7 );
			//System.out.println(PT7[k7][j7]+ "   " + PT7[k7][j7+1]+ "   " + PT7[k7][j7+2]+ "   " + PT7[k7][j7+3]+ "   " + PT7[k7][j7+4]+ "   " + PT7[k7][j7+5]);

			for (int z=0 ; z<SN_P6_nodes.length; z++)
			{			
			for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
			{	
				PT7[k7][j7]   = SN_P6_nodes[z][j7];  //  initialize the first node of the path searched
				PT7[k7][j7+1] = SN_P6_nodes[z][j7+1];//  initialize the second node of the path searched
				PT7[k7][j7+2] = SN_P6_nodes[z][j7+2];//  initialize the third node of the path searched
				PT7[k7][j7+3] = SN_P6_nodes[z][j7+3];//  initialize the fourth node of the path searched
				PT7[k7][j7+4] = SN_P6_nodes[z][j7+4];//  initialize the fifth node of the path searched
				PT7[k7][j7+5] = SN_P6_nodes[z][j7+5];//  initialize the fifth node of the path searched

				//System.out.println(" k7  = " + k7 + " j7 = " + j7 +  " z = "+ z + " i =  " + i);
				//System.out.println("---------------------");		
					if (PT7[k7][j7+5]== SN_P2_nodes[i+1][j7])
					{
						
						PT7[k7][j7+6] = SN_P2_nodes[i+1][j7+1];
				/*	    
						System.out.println(PT7[k7][j7]   + "   " + 
						                   PT7[k7][j7+1] + "   " + 
								           PT7[k7][j7+2] + "   " + 
						                   PT7[k7][j7+3] + "   " +
						                   PT7[k7][j7+4] + "   " +
						                   PT7[k7][j7+5]);
				*/	    
					    k7++;
										
						PT7[k7][j7]   = SN_P6_nodes[z][j7];  //  save the first node of the path searched
						PT7[k7][j7+1] = SN_P6_nodes[z][j7+1];//  save the second node of the path searched
						PT7[k7][j7+2] = SN_P6_nodes[z][j7+2];//  save the third node of the path searched
						PT7[k7][j7+3] = SN_P6_nodes[z][j7+3];//  save the forth node of the path searched
						PT7[k7][j7+4] = SN_P6_nodes[z][j7+4];//  save the fifth node of the path searched
						PT7[k7][j7+5] = SN_P6_nodes[z][j7+5];//  save the fifth node of the path searched

						//System.out.println(" k7  = " +k7 + " j7 = " + j7 +  " z = "+ z + " i =  " + i);
						//System.out.println("........................");		

					}else				
						if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j7+1] != 0))
						{
							continue; // go to next path in the list of paths type 3
						}else
							if (SN_P2_nodes[z][j7+1] != 0)
							{								
								System.out.println("next z");
							}else
								break;

			 } // end of i for loop
		
			}// end of z for loop		
		
			//System.out.println("------------------------- SN Paths Type 7 Nodes  ---------------------------------------");
		
			int[][] SN_P7_nodes = new int[k7][7]; // a matrix to save the newly formulated paths of type 7 nodes
			
			for (int i=0 ; i<k7 ; i++)
			{
				//System.out.print(" SNP7-->"+ i + "  ");

				for (int e=0 ; e<7 ; e++)
				{
					SN_P7_nodes[i][e]=PT7[i][e];              // save the matrix of paths type 7 nodes
					//System.out.print(SN_P7_nodes[i][e]+ "," +"\t"); // print this path
					
				}
			//System.out.println();
			}
	
			for (int i=0 ; i<k7 ; i++)
				  for (int j=0 ; j<7 ; j++)
					{					
						try {	
							BufferedWriter writerSFCOUT =						
									new BufferedWriter ( new FileWriter("P7.txt", true));
																		
										writerSFCOUT.write(String.valueOf(
														SN_P7_nodes[i][0] + " \t " + 
														SN_P7_nodes[i][1] + " \t " + 
														SN_P7_nodes[i][2] + " \t " + 
														SN_P7_nodes[i][3] + " \t " +
														SN_P7_nodes[i][4] + " \t " +
														SN_P7_nodes[i][5] + " \t " +
														SN_P7_nodes[i][6] ));
														writerSFCOUT.newLine();				
										
									writerSFCOUT.close();
							
								} catch (IOException e) 
									{
										e.printStackTrace();
									}					
					      }
		
			
			
			
			
			int[] sorted_CPUsum_paths7 = new int[SN_P7_nodes.length]; // a matrix to save the path numbers of the newly formulated paths type 7 nodes
			int a7=0;
			
			for (int a=0 ; a<SN_P7_nodes.length ; a++ )
			{
				sorted_CPUsum_paths7[a]=a7; // initialize the path numbers for paths of type 7 nodes 
				a7++;
				//System.out.println(sorted_CPUsum_paths7[a]); // print this path
			}			

			
//---------------------------------- Paths of Type - 8 - Nodes -------------------------------------

			int k8 = 0 ;// counter used in indexing new paths of type 8 nodes 
			int j8 = 0 ;// counter for the nodes in the matrix of paths type 8 nodes
			int[][] PT8 = new int[SN*50][8];// a matrix for paths of type 8 nodes

			PT8[k8][j8]   = SN_P7_nodes[k8][j8];  // a matrix to save the first node of the path to start searching
			PT8[k8][j8+1] = SN_P7_nodes[k8][j8+1];// a matrix to save the second node of the path to start searching
			PT8[k8][j8+2] = SN_P7_nodes[k8][j8+2];// a matrix to save the third node of the path to start searching
			PT8[k8][j8+3] = SN_P7_nodes[k8][j8+3];// a matrix to save the fourth node of the path to start searching
			PT8[k8][j8+4] = SN_P7_nodes[k8][j8+4];// a matrix to save the fifth node of the path to start searching
			PT8[k8][j8+5] = SN_P7_nodes[k8][j8+5];// a matrix to save the fifth node of the path to start searching
			PT8[k8][j8+6] = SN_P7_nodes[k8][j8+6];// a matrix to save the fifth node of the path to start searching

			//System.out.println(" k8  = " + k8 + " j8 = " + j8 );
			//System.out.println(PT8[k8][j8]+ "   " + PT8[k8][j8+1]+ "   " + PT8[k8][j8+2]+ "   " + PT8[k8][j8+3]+ "   " + PT8[k8][j8+4]+ "   " + PT8[k8][j8+5]+ "   " + PT8[k8][j8+6]);

			for (int z=0 ; z<SN_P7_nodes.length; z++)
			{			
			for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
			{	
				PT8[k8][j8]   = SN_P7_nodes[z][j8];  //  initialize the first node of the path searched
				PT8[k8][j8+1] = SN_P7_nodes[z][j8+1];//  initialize the second node of the path searched
				PT8[k8][j8+2] = SN_P7_nodes[z][j8+2];//  initialize the third node of the path searched
				PT8[k8][j8+3] = SN_P7_nodes[z][j8+3];//  initialize the fourth node of the path searched
				PT8[k8][j8+4] = SN_P7_nodes[z][j8+4];//  initialize the fifth node of the path searched
				PT8[k8][j8+5] = SN_P7_nodes[z][j8+5];//  initialize the fifth node of the path searched
				PT8[k8][j8+6] = SN_P7_nodes[z][j8+6];//  initialize the fifth node of the path searched

				//System.out.println(" k8  = " + k8 + " j8 = " + j8 +  " z = "+ z + " i =  " + i);
				//System.out.println("---------------------");		
					if (PT8[k8][j8+6]== SN_P2_nodes[i+1][j8])
					{
						
						PT8[k8][j8+7] = SN_P2_nodes[i+1][j8+1];
				/*	    
						System.out.println(PT8[k8][j8]   + "   " + 
						                   PT8[k8][j8+1] + "   " + 
								           PT8[k8][j8+2] + "   " + 
						                   PT8[k8][j8+3] + "   " +
						                   PT8[k8][j8+4] + "   " +
						                   PT8[k8][j8+5] + "   " +
						                   PT8[k8][j8+6]);
				*/	    
					    k8++;
										
						PT8[k8][j8]   = SN_P7_nodes[z][j8];  //  save the first node of the path searched
						PT8[k8][j8+1] = SN_P7_nodes[z][j8+1];//  save the second node of the path searched
						PT8[k8][j8+2] = SN_P7_nodes[z][j8+2];//  save the third node of the path searched
						PT8[k8][j8+3] = SN_P7_nodes[z][j8+3];//  save the forth node of the path searched
						PT8[k8][j8+4] = SN_P7_nodes[z][j8+4];//  save the fifth node of the path searched
						PT8[k8][j8+5] = SN_P7_nodes[z][j8+5];//  save the fifth node of the path searched
						PT8[k8][j8+6] = SN_P7_nodes[z][j8+6];//  save the fifth node of the path searched

						//System.out.println(" k8  = " + k8 + " j8 = " + j8 +  " z = "+ z + " i =  " + i);
						//System.out.println("........................");		

					}else				
						if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j8+1] != 0))
						{
							continue; // go to next path in the list of paths
						}else
							if (SN_P2_nodes[z][j8+1] != 0)
							{								
								System.out.println("next z");
							}else
								break;

			 } // end of i for loop
		
			}// end of z for loop		
		
			//System.out.println("------------------------- SN Paths Type 8 Nodes  ---------------------------------------");
		
			int[][] SN_P8_nodes = new int[k8][8]; // a matrix to save the newly formulated paths of type 8 nodes
			
			for (int i=0 ; i<k8 ; i++)
			{
				//System.out.print(" SNP8-->"+ i + "  ");

				for (int e=0 ; e<8 ; e++)
				{
					SN_P8_nodes[i][e]=PT8[i][e];                // save the matrix of paths type 8 nodes
					//System.out.print(SN_P8_nodes[i][e]+ "," +"\t"); // print this path
					
				}
			//System.out.println();
			}
			
			for (int i=0 ; i<k8 ; i++)
				  for (int j=0 ; j<8 ; j++)
					{					
						try {	
							BufferedWriter writerSFCOUT =						
									new BufferedWriter ( new FileWriter("P8.txt", true));
																		
										writerSFCOUT.write(String.valueOf(
														SN_P8_nodes[i][0] + " \t " + 
														SN_P8_nodes[i][1] + " \t " + 
														SN_P8_nodes[i][2] + " \t " + 
														SN_P8_nodes[i][3] + " \t " +
														SN_P8_nodes[i][4] + " \t " +
														SN_P8_nodes[i][5] + " \t " +
														SN_P8_nodes[i][6] + " \t " +
														SN_P8_nodes[i][7] ));
														writerSFCOUT.newLine();				
										
									writerSFCOUT.close();
							
								} catch (IOException e) 
									{
										e.printStackTrace();
									}					
					      }
		
			
			
										
			int[] sorted_CPUsum_paths8 = new int[SN_P8_nodes.length]; // a matrix to save the path numbers of the newly formulated paths type 8 nodes
			int a8=0;
			
			for (int a=0 ; a<SN_P8_nodes.length ; a++ )
			{
				sorted_CPUsum_paths8[a]=a8; // initialize the path numbers for paths of type 8 nodes 
				a8++;
				//System.out.println(sorted_CPUsum_paths8[a]); // print this path
			}			



//---------------------------------- Paths of Type - 9 - Nodes -------------------------------------

			int k9 = 0 ;// counter used in indexing new paths of type 9 nodes 
			int j9 = 0 ;// counter for the nodes in the matrix of paths type 9 nodes
			int[][] PT9 = new int[SN*50][9];// a matrix for paths of type 9 nodes

			PT9[k9][j9]   = SN_P8_nodes[k9][j9];  // a matrix to save the first node of the path to start searching
			PT9[k9][j9+1] = SN_P8_nodes[k9][j9+1];// a matrix to save the second node of the path to start searching
			PT9[k9][j9+2] = SN_P8_nodes[k9][j9+2];// a matrix to save the third node of the path to start searching
			PT9[k9][j9+3] = SN_P8_nodes[k9][j9+3];// a matrix to save the fourth node of the path to start searching
			PT9[k9][j9+4] = SN_P8_nodes[k9][j9+4];// a matrix to save the fifth node of the path to start searching
			PT9[k9][j9+5] = SN_P8_nodes[k9][j9+5];// a matrix to save the fifth node of the path to start searching
			PT9[k9][j9+6] = SN_P8_nodes[k9][j9+6];// a matrix to save the fifth node of the path to start searching
			PT9[k9][j9+7] = SN_P8_nodes[k9][j9+7];// a matrix to save the fifth node of the path to start searching

			//System.out.println(" k9  = " + k9 + " j9 = " + j9 );
			//System.out.println(PT9[k9][j9]+ "   " + PT9[k9][j9+1]+ "   " + PT9[k9][j9+2]+ "   " + PT9[k9][j9+3] + "   " + PT9[k9][j9+4]+ "   " + PT9[k9][j9+5]+ "   " + PT9[k9][j9+6]+ "   " + PT9[k9][j9+7]);

			for (int z=0 ; z<SN_P8_nodes.length; z++)
			{			
			for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
			{	
				PT9[k9][j9]   = SN_P8_nodes[z][j9];  //  initialize the first node of the path searched
				PT9[k9][j9+1] = SN_P8_nodes[z][j9+1];//  initialize the second node of the path searched
				PT9[k9][j9+2] = SN_P8_nodes[z][j9+2];//  initialize the third node of the path searched
				PT9[k9][j9+3] = SN_P8_nodes[z][j9+3];//  initialize the fourth node of the path searched
				PT9[k9][j9+4] = SN_P8_nodes[z][j9+4];//  initialize the fifth node of the path searched
				PT9[k9][j9+5] = SN_P8_nodes[z][j9+5];//  initialize the fifth node of the path searched
				PT9[k9][j9+6] = SN_P8_nodes[z][j9+6];//  initialize the fifth node of the path searched
				PT9[k9][j9+7] = SN_P8_nodes[z][j9+7];//  initialize the fifth node of the path searched

				//System.out.println(" k9  = " + k9 + " j9 = " + j9 +  " z = "+ z + " i =  " + i);
				//System.out.println("---------------------");		
					if (PT9[k9][j9+7]== SN_P2_nodes[i+1][j9])
					{
						
						PT9[k9][j9+8] = SN_P2_nodes[i+1][j9+1];
				/*	    
						System.out.println(PT9[k9][j9]   + "   " + 
						                   PT9[k9][j9+1] + "   " + 
								           PT9[k9][j9+2] + "   " + 
						                   PT9[k9][j9+3] + "   " +
						                   PT9[k9][j9+4] + "   " +
						                   PT9[k9][j9+5] + "   " +
						                   PT9[k9][j9+6] + "   " +
						                   PT9[k9][j9+7]);
				*/	    
					    k9++;
										
						PT9[k9][j9]   = SN_P8_nodes[z][j9];  //  save the first node of the path searched
						PT9[k9][j9+1] = SN_P8_nodes[z][j9+1];//  save the second node of the path searched
						PT9[k9][j9+2] = SN_P8_nodes[z][j9+2];//  save the third node of the path searched
						PT9[k9][j9+3] = SN_P8_nodes[z][j9+3];//  save the forth node of the path searched
						PT9[k9][j9+4] = SN_P8_nodes[z][j9+4];//  save the fifth node of the path searched
						PT9[k9][j9+5] = SN_P8_nodes[z][j9+5];//  save the fifth node of the path searched
						PT9[k9][j9+6] = SN_P8_nodes[z][j9+6];//  save the fifth node of the path searched
						PT9[k9][j9+7] = SN_P8_nodes[z][j9+7];//  save the fifth node of the path searched

						//System.out.println(" k9  = " + k9 + " j9 = " + j9 +  " z = "+ z + " i =  " + i);
						//System.out.println("........................");		

					}else				
						if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j9+1] != 0))
						{
							continue; // go to next path in the list of paths
						}else
							if (SN_P2_nodes[z][j9+1] != 0)
							{								
								System.out.println("next z");
							}else
								break;

			 } // end of i for loop
		
			}// end of z for loop		
		
			//System.out.println("------------------------- SN Paths Type 9 Nodes  ---------------------------------------");
		
			int[][] SN_P9_nodes = new int[k9][9]; // a matrix to save the newly formulated paths of type 9 nodes
			
			for (int i=0 ; i<k9 ; i++)
			{
				//System.out.print(" SNP8-->"+ i + "  ");

				for (int e=0 ; e<9 ; e++)
				{
					SN_P9_nodes[i][e]=PT9[i][e];                // save the matrix of paths type 9 nodes
					//System.out.print(SN_P9_nodes[i][e]+ "," +"\t"); // print this path
					
				}
		//	System.out.println();
			}
			
			
			for (int i=0 ; i<k9 ; i++)
			  for (int j=0 ; j<9 ; j++)
				{					
					try {	
						BufferedWriter writerSFCOUT =						
								new BufferedWriter ( new FileWriter("P9.txt", true));
																	
									writerSFCOUT.write(String.valueOf(
													SN_P9_nodes[i][0] + " \t " + 
													SN_P9_nodes[i][1] + " \t " + 
													SN_P9_nodes[i][2] + " \t " + 
													SN_P9_nodes[i][3] + " \t " +
													SN_P9_nodes[i][4] + " \t " +
													SN_P9_nodes[i][5] + " \t " +
													SN_P9_nodes[i][6] + " \t " +
													SN_P9_nodes[i][7] + " \t " +
													SN_P9_nodes[i][8] ));
													writerSFCOUT.newLine();				
									
								writerSFCOUT.close();
						
							} catch (IOException e) 
								{
									e.printStackTrace();
								}					
				      }
							
	
			
			
			int[] sorted_CPUsum_paths9 = new int[SN_P9_nodes.length]; // a matrix to save the path numbers of the newly formulated paths type 9 nodes
			int a9=0;
			
			for (int a=0 ; a<SN_P9_nodes.length ; a++ )
			{
				sorted_CPUsum_paths9[a]=a9; // initialize the path numbers for paths of type 9 nodes 
				a9++;
				//System.out.println(sorted_CPUsum_paths9[a]); // print this path
			}			

	

//-------------------------------------------------------- Paths of Type - 10 - Nodes -------------------------------------

			int k10 = 0 ;// counter used in indexing new paths of type 10 nodes 
			int j10 = 0 ;// counter for the nodes in the matrix of paths type 10 nodes
			int[][] PT10 = new int[SN*50][10];// a matrix for paths of type 10 nodes

			PT10[k10][j10]   = SN_P9_nodes[k10][j10];  // a matrix to save the first node of the path to start searching
			PT10[k10][j10+1] = SN_P9_nodes[k10][j10+1];// a matrix to save the second node of the path to start searching
			PT10[k10][j10+2] = SN_P9_nodes[k10][j10+2];// a matrix to save the third node of the path to start searching
			PT10[k10][j10+3] = SN_P9_nodes[k10][j10+3];// a matrix to save the fourth node of the path to start searching
			PT10[k10][j10+4] = SN_P9_nodes[k10][j10+4];// a matrix to save the fifth node of the path to start searching
			PT10[k10][j10+5] = SN_P9_nodes[k10][j10+5];// a matrix to save the fifth node of the path to start searching
			PT10[k10][j10+6] = SN_P9_nodes[k10][j10+6];// a matrix to save the fifth node of the path to start searching
			PT10[k10][j10+7] = SN_P9_nodes[k10][j10+7];// a matrix to save the fifth node of the path to start searching
			PT10[k10][j10+8] = SN_P9_nodes[k10][j10+8];// a matrix to save the fifth node of the path to start searching

			//System.out.println(" k10  = " + k10 + " j10 = " + j10 );
			//System.out.println(PT10[k10][j10]+ "   " + PT10[k10][j10+1]+ "   " + PT10[k10][j10+2]+ "   " + PT10[k10][j10+3] + "   " + PT10[k10][j10+4]+ "   " + PT10[k10][j10+5]+ "   " + PT10[k10][j10+6]+ "   " + PT10[k10][j10+7]);

			for (int z=0 ; z<SN_P9_nodes.length; z++)
			{			
			for (int i=0 ; i<SN_P2_nodes.length-1 ; i++)
			{	
				PT10[k10][j10]   = SN_P9_nodes[z][j10];  //  initialize the first node of the path searched
				PT10[k10][j10+1] = SN_P9_nodes[z][j10+1];//  initialize the second node of the path searched
				PT10[k10][j10+2] = SN_P9_nodes[z][j10+2];//  initialize the third node of the path searched
				PT10[k10][j10+3] = SN_P9_nodes[z][j10+3];//  initialize the fourth node of the path searched
				PT10[k10][j10+4] = SN_P9_nodes[z][j10+4];//  initialize the fifth node of the path searched
				PT10[k10][j10+5] = SN_P9_nodes[z][j10+5];//  initialize the fifth node of the path searched
				PT10[k10][j10+6] = SN_P9_nodes[z][j10+6];//  initialize the fifth node of the path searched
				PT10[k10][j10+7] = SN_P9_nodes[z][j10+7];//  initialize the fifth node of the path searched
				PT10[k10][j10+8] = SN_P9_nodes[z][j10+8];//  initialize the fifth node of the path searched

				//System.out.println(" k10  = " + k10 + " j10 = " + j10 +  " z = "+ z + " i =  " + i);
				//System.out.println("---------------------");		
					if (PT10[k10][j10+8]== SN_P2_nodes[i+1][j10])
					{
						
						PT10[k10][j10+9] = SN_P2_nodes[i+1][j10+1];
				/*	    
						System.out.println(PT10[k10][j10]   + "   " + 
						                   PT10[k10][j10+1] + "   " + 
								           PT10[k10][j10+2] + "   " + 
						                   PT10[k10][j10+3] + "   " +
						                   PT10[k10][j10+4] + "   " +
						                   PT10[k10][j10+5] + "   " +
						                   PT10[k10][j10+6] + "   " +
						                   PT10[k10][j10+7] + "   " +
						                   PT10[k10][j10+8] + "   " +
						                   PT10[k10][j10+9]);
				*/	    
					    k10++;
										
						PT10[k10][j10]   = SN_P9_nodes[z][j10];  //  save the first node of the path searched
						PT10[k10][j10+1] = SN_P9_nodes[z][j10+1];//  save the second node of the path searched
						PT10[k10][j10+2] = SN_P9_nodes[z][j10+2];//  save the third node of the path searched
						PT10[k10][j10+3] = SN_P9_nodes[z][j10+3];//  save the forth node of the path searched
						PT10[k10][j10+4] = SN_P9_nodes[z][j10+4];//  save the fifth node of the path searched
						PT10[k10][j10+5] = SN_P9_nodes[z][j10+5];//  save the fifth node of the path searched
						PT10[k10][j10+6] = SN_P9_nodes[z][j10+6];//  save the fifth node of the path searched
						PT10[k10][j10+7] = SN_P9_nodes[z][j10+7];//  save the fifth node of the path searched
						PT10[k10][j10+8] = SN_P9_nodes[z][j10+8];//  save the fifth node of the path searched

						//System.out.println(" k10  = " + k10 + " j10 = " + j10 +  " z = "+ z + " i =  " + i);
						//System.out.println("........................");		

					}else				
						if ( (i<SN_P2_nodes.length*2) && (SN_P2_nodes[i+1][j10+1] != 0))
						{
							continue; // go to next path in the list of paths
						}else
							if (SN_P2_nodes[z][j10+1] != 0)
							{								
							   System.out.println("next z");
							}else
								break;

			 } // end of i for loop
		
			}// end of z for loop		

//--------------------------------------------------------------------------------------------------------------------
//System.out.println("------------------------- SN Paths Type 10 Nodes  --------------------------------------------");
//--------------------------------------------------------------------------------------------------------------------				
	int[][] SN_P10_nodes = new int[2*k10][10]; // a matrix to save the newly formulated paths of type 10 nodes
		for (int i=0 ; i<k10 ; i++)
				for (int e=0 ; e<10 ; e++)
					SN_P10_nodes[i][e]=PT10[i][e];      // This matrix saves the paths type 10 nodes on forward directions
			

			for (int i=0 ; i<k10 ; i++)
			{
				//System.out.print(" SNP10-->"+ i + "  ");

				for (int j=0 ; j<10 ; j++)
				{
					//System.out.print(SN_P10_nodes[i][j]+ "," +"\t"); // print this path
				
				}
			//System.out.println();
			}				
			
			for (int i=0 ; i<k10 ; i++)
			  for (int j=0 ; j<10 ; j++)
				{					
					try {	
						BufferedWriter writerSFCOUT =						
								new BufferedWriter ( new FileWriter("P10.txt", true));
																	
									writerSFCOUT.write(String.valueOf(
													SN_P10_nodes[i][0] + " \t " + 
													SN_P10_nodes[i][1] + " \t " + 
													SN_P10_nodes[i][2] + " \t " + 
													SN_P10_nodes[i][3] + " \t " +
													SN_P10_nodes[i][4] + " \t " +
													SN_P10_nodes[i][5] + " \t " +
													SN_P10_nodes[i][6] + " \t " +
													SN_P10_nodes[i][7] + " \t " +
													SN_P10_nodes[i][8] + " \t " +
													SN_P10_nodes[i][9] ));
													writerSFCOUT.newLine();				
									
								writerSFCOUT.close();
						
							} catch (IOException e) 
								{
									e.printStackTrace();
								}					
				      }
							
	
			
			
			
			int[] sorted_CPUsum_paths10 = new int[SN_P10_nodes.length]; // a matrix to save the path numbers of the newly formulated paths type 9 nodes
			int a10=0;
			
			for (int a=0 ; a<SN_P10_nodes.length ; a++ )
			{
				sorted_CPUsum_paths10[a]=a10; // initialize the path numbers for paths of type 10 nodes 
				a10++;
				//System.out.println(sorted_CPUsum_paths10[a]); // print this path
			}			

			
			
			System.out.println( "Number of links per node\t" + edgesnum/SN_Nodes);
			System.out.println( "Type-2 paths saved in P2.txt\t" +SN_P2_nodes.length);
			System.out.println( "Type-3 paths saved in P3.txt\t" +SN_P3_nodes.length);
			System.out.println( "Type-4 paths saved in P4.txt\t" +SN_P4_nodes.length);
			System.out.println( "Type-5 paths saved in P5.txt\t" +SN_P5_nodes.length);
    		System.out.println( "Type-6 paths saved in P6.txt\t" +SN_P6_nodes.length);
			System.out.println( "Type-7 paths saved in P7.txt\t" +SN_P7_nodes.length);
			System.out.println( "Type-8 paths saved in P8.txt\t" +SN_P8_nodes.length);
			System.out.println( "Type-9 paths saved in P9.txt\t" +SN_P9_nodes.length);
			System.out.println( "Type-10 paths saved in P10.txt\t" +SN_P10_nodes.length);
			
			System.out.println("FINISHED. All paths are generated and successfully saved.");

			
			
								
//------------------------------------------------------------------------------------------------------------------------				
			
//__________________________________  End of Initializing the SN paths ___________________________________________________
//-------------------------------------------------------------------------------------------------------------------------

			
			
			
	
	
	
	
	
	}

}
