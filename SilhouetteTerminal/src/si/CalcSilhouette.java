package si;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;

public class CalcSilhouette{

	public static double [] silhouette;
	public static double [] menor;
	public static double [][] matDist;
	public static int colunas;
	public static int [] qtd;
	public static int indice; 
	
	public static String CalcularSilhouette(File file) throws IOException {
	
	StringBuilder sbIni = new StringBuilder();
	StringBuilder sbFim = new StringBuilder();
	
	DecimalFormat df = new DecimalFormat();
	df.setMinimumFractionDigits(4);
	
	FileInputStream inFile = new FileInputStream( file );
	InputStreamReader in = new InputStreamReader( inFile );
	Instances base = new Instances( in );
	base.setClassIndex( base.numAttributes() - 1 );
	
	AttributeStats as = base.attributeStats(base.classIndex());
	indice = base.numAttributes() - 1;
	
	qtd = new int [as.nominalCounts.length];
    
	for (int i = 0; i < as.nominalCounts.length; i++) {
    	qtd[i]= new Integer(as.nominalCounts[i]);
    }
	
    colunas = 2;
    colunas += base.numClasses();
    matDist = new double [base.numInstances()][colunas];
    
    for (int i=0; i < base.numInstances(); i++){
    	for (int j=0; j < base.numInstances(); j++){
    		
    		if (i == j) continue;
    		
    		//agrupamentos com o atributo cluster com uma string iniciada por "cluster"
    		if(base.instance(i).stringValue(indice).contains("cluster")) {
	    		//cluster começando em 1
	    		try{
	        		if (base.get(i).stringValue(indice).equalsIgnoreCase(base.get(j).stringValue(indice))){
	        			matDist[i][0] += 
	        					Distancia (base.instance(i), base.instance(j)) / 
	        			        (qtd[Integer.parseInt(base.get(i).stringValue(indice).substring(7)) -1 ] - 1); 
	        		}
	        		else {
	        			matDist[i][Integer.parseInt(base.get(j).stringValue(indice).substring(7))] += 
	        					Distancia (base.instance(i), base.instance(j)) /
	        			        qtd[Integer.parseInt(base.get(j).stringValue(indice).substring(7)) -1 ];
	        		}
	    		}
	    		//cluster começando em 0
	    		catch (Exception e) {
	  
	        		if (base.get(i).stringValue(indice).equalsIgnoreCase(base.get(j).stringValue(indice))){
	        			matDist[i][0] += 
	    					Distancia (base.instance(i), base.instance(j)) / 
	    			        (qtd[Integer.parseInt(base.get(i).stringValue(indice).substring(7))] - 1); 
	        		}
	        		else {
	        			matDist[i][Integer.parseInt(base.get(j).stringValue(indice).substring(7))+1] += 
	    					Distancia (base.instance(i), base.instance(j)) /
	    			        qtd[Integer.parseInt(base.get(j).stringValue(indice).substring(7))];
	        		}
				}
    		}
    		
    		//agrupamentos com o atributo cluster representado apenas pelo numero do cluster
    		else {
    			//cluster começando em 1
    			try{
	        		if (base.get(i).stringValue(indice).equalsIgnoreCase(base.get(j).stringValue(indice))){
	        			matDist[i][0] += 
	        					Distancia (base.instance(i), base.instance(j)) / 
	        			        (qtd[Integer.parseInt(base.get(i).stringValue(indice)) -1 ] - 1); 
	        		}
	        		else {
	        			matDist[i][Integer.parseInt(base.get(j).stringValue(indice))] += 
	        					Distancia (base.instance(i), base.instance(j)) /
	        			        qtd[Integer.parseInt(base.get(j).stringValue(indice)) -1 ];
	        		}
	    		}
	    		//cluster começando em 0
	    		catch (Exception e) {
	  
	        		if (base.get(i).stringValue(indice).equalsIgnoreCase(base.get(j).stringValue(indice))){
	        			matDist[i][0] += 
	    					Distancia (base.instance(i), base.instance(j)) / 
	    			        (qtd[Integer.parseInt(base.get(i).stringValue(indice))] - 1); 
	        		}
	        		else {
	        			matDist[i][Integer.parseInt(base.get(j).stringValue(indice))+1] += 
	    					Distancia (base.instance(i), base.instance(j)) /
	    			        qtd[Integer.parseInt(base.get(j).stringValue(indice))];
	        		}
				}
    		}
    		
    	}
    }
    
    for (int i=0; i < base.numInstances(); i++){
    	double bi=0;
    	bi = Calcula_bi(i);
    	matDist[i][colunas-1] = ( bi - matDist[i][0]) / Math.max(matDist[i][0], bi);
    }
    
    silhouette = new double [base.numClasses() + 1];
    for (int i=0; i < base.numInstances(); i++){
    	for (int j=1; j < colunas; j++){
    		if (matDist[i][j] == 0) {
    			silhouette[j-1] += matDist[i][colunas - 1]; 
    		}
    	}
    }
    
    for (int i=0; i < base.numClasses(); i++){
    	silhouette[i] = silhouette[i] / qtd[i]; 
    	silhouette[base.numClasses()] += silhouette[i]; 
    }
    silhouette[base.numClasses()] =  silhouette[base.numClasses()] / base.numClasses();
    
    //System.out.println(file.getName());
    //output += file.getName() + "\n";
    
    //Mudança para saída em linhas
    sbIni.append(file.getName() + ";");
    
    for (int i=0; i < silhouette.length - 1; i++){
    	//output +="Silhouette cluster" + (i) +  ": " + df.format(silhouette[i]) + "\n";
    	
    	//Mudança para saída em linhas
    	sbFim.append(";" + df.format(silhouette[i]));
    }
    
    //output += "File: " + fileAtual.getName() + " - Silhouette Geral: " + df.format(silhouette[base.numClasses()]) + "\n";
    //output += "-----------------------------------------------------------------------------------" + "\n";

    //Mudança para saída em linhas
    sbIni.append(df.format(silhouette[base.numClasses()]));
    sbIni.append(sbFim.toString());
    
	return sbIni.toString();
}

	private static double Calcula_bi(int i) {
		menor = new double[colunas - 3];
		int k=0;
		for (int j=1; j <= colunas - 2; j++){
			if (matDist[i][j] > 0) {
				menor[k] = matDist[i][j];
				k++;
			}
		}
		Bubblesort(menor); 
		return menor[0];
	}
	
	private static void Bubblesort(double[] menor) {
		double aux;
		for (int i=1; i < menor.length; i++){
			 for (int j=0; j < menor.length-1; j++){
			      if (menor[j] > menor[j+1]){
			         aux = menor[j+1];
			         menor[j+1] = menor[j];
			         menor[j] = aux;
			      }
			 }
		}	   
	}
	
	private static double Distancia(Instance a, Instance b) {
		double diff=0;
		
		for ( int i = 1; i < a.numAttributes() -1; i++ ) {
			if( a.attribute( i ).isNominal() ){
				// testa se atributo é hieráquico
				if (a.stringValue(i).startsWith("R.")){
					//diff += Math.pow(computeDistance(a.stringValue(i), b.stringValue(i)), 2);
					diff += computeDistance(a.stringValue(i), b.stringValue(i));
				}
				// atributo nao hieraquico
				else {
					//if (a.value( i ) == b.value( i )) 	diff += Math.pow(0,2);
					//else 									diff += Math.pow(1,2);
					diff += a.value( i ) == b.value( i ) ? 0 : 1;
				}
			}
			else {
				//diff += Math.pow( a.value( i ) - b.value( i ), 2 );
				diff += Math.abs( a.value( i ) - b.value( i ) );
			}
		}
		//diff = Math.sqrt(diff);
		return diff;
	}
	
	private static double computeDistance(String s1, String s2) {
		String[] t1 = s1.split("[.]");
		String[] t2 = s2.split("[.]");
		if (s1.compareTo(s2) == 0){
			return 0;
		}
		else {
			if (t1[1].compareToIgnoreCase(t2[1]) == 0){
				if (t1[2].compareToIgnoreCase(t2[2]) == 0){
					if (t1[3].compareToIgnoreCase(t2[3]) == 0){
						if (t1[4].compareToIgnoreCase(t2[4]) == 0) {
							if (t1[5].compareToIgnoreCase(t2[5]) == 0) {
								if (t1[6].compareToIgnoreCase(t2[6]) == 0) {
									return 0.015625;
								}
								else {
									return 0.03125;
								}
							}
							else {
								return 0.0625;
							}
						}
						else {
							return 0.125;
						}		
					}
					else {
						return 0.25;
					}						
				}
				else {
					return 0.5;
				}	
			}
			else {
				return 1;
			}
		}
	}

}