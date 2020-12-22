import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class BuscaIngenua {
	protected final int MAX = 1000000;
	static String[] arranjo;
	static int size = 0;
	
	BuscaIngenua(){ arranjo = new String[MAX]; }
	
	void insert(String s) {
		arranjo[size] = s;
		size++;
	}
	
	boolean find(String a) {
		for(int i = 0; i < size; i++) {
			if(arranjo[i].equals(a)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception{
		long inicio = System.currentTimeMillis();
		BuscaIngenua busca = new BuscaIngenua();
		String caminho = "C:\\Users\\Usuario\\Desktop\\epAed\\tarefas99000.txt"; // inserir aqui o caminho da entrada
		File entrada = new File(caminho);
		Scanner scan = new Scanner(entrada);
		String saida = "";
		while(scan.hasNextLine()) {
			String tarefa = scan.nextLine();
			if (tarefa.equals("")) {
			}
			else { 
				busca.insert(tarefa);
			}
		}
		caminho = "C:\\Users\\Usuario\\Desktop\\epAed\\verificar\\verificar99000.txt"; // inserir aqui o caminho da verificacao
		entrada = new File(caminho);
		scan.close();
		scan = new Scanner(entrada);
		while(scan.hasNextLine()) {
			String tarefa = scan.nextLine();
			if(busca.find(tarefa)) {
				saida += tarefa + "\n";
			}
		}
		Files.write(Paths.get("C:\\Users\\Usuario\\Desktop\\saida\\tarefas99000.txt"), saida.getBytes()); // inserir aqui o caminho da saída
		long fim = System.currentTimeMillis();
		System.out.println(fim - inicio);
		scan.close();
		
	}

}