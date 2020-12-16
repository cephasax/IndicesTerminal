package db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DbTerminal {

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

		// CalcDB cdb = new CalcDB();
		// Calcular DB p/ os arquivos selecionados
		for (File file : files) {
			String result = CalcDB.calcularDB(file);
			sb.append(file.getName() + " \t " + result + "\n");
			System.out.print(file.getName() + " \t " + result + "\n");
		}

		File saida = new File("db_jcx.txt");
		FileOutputStream fos = new FileOutputStream(saida);
		fos.write(sb.toString().getBytes());
		fos.close();
		System.out.println("\nArquivo " + saida + " salvo!");
	}

}
