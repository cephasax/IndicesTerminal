package si;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SiTerminal {
	
	private static List<File> AbrirTodos(String urlBase) {

		List<File> filess = new ArrayList<File>();
		
		String url = urlBase;
		File f = new File(url);
		File files[];
		files = f.listFiles();
		
		for(int i = 0; i < files.length; i++) {
			filess.add(files[i]);
		}
		
		return filess;
	}
	
	public static void main(String[] args) throws IOException {
		
		String base = new String(args[0]);
		
		StringBuilder sb = new StringBuilder();
		List<File> files = new ArrayList<File>();
		files = AbrirTodos(base);
	    
	    sb.append("o cabeçalho csv abaixo segue a ordem: nome do arquivo; silhouette geral; silhouette do grupo 1; do grupo 2; ..." +"\n\n");
	    sb.append("file;geral;g1;g2;g3;g4;g5;g6;g7;g8;g9;g10;g11;g12;g13;g14;g15;g16;g17;g18;g19;g20,..." + "\n");
		
	    System.out.print(sb.toString());
		
	    // Calcular silhouette p/ os arquivos selecionados
		for(File f: files) {
			String result = new String(CalcSilhouette.CalcularSilhouette(f) + "\n");
			sb.append(result);
			System.out.print(result);
		}
		
		File saida = new File("S_index.txt");
		
		FileOutputStream fos = new FileOutputStream(saida);    
        fos.write(sb.toString().getBytes());  
        fos.close();
        System.out.println();
        System.out.println("Arquivo " + saida + " salvo!");
	}
}
