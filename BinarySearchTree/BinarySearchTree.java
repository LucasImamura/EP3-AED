import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

public class BinarySearchTree<K,V>
  extends LinkedBinaryTree<Entry<K,V> > implements Dictionary<K,V> {
  protected Comparator<K> C;
  protected Position<Entry<K,V> > actionPos; //pai do nó a ser removido/inserido
  protected static int numEntries;
private static Scanner scan2;
  //static Scanner scan;

  public BinarySearchTree(Comparator<K> c) {
    addRoot(null);
    C = c;
    numEntries = 0;
   
  }

  protected K key(Position<Entry<K,V> > position) {
    return position.element().getKey();
  }

  protected V value(Position<Entry<K,V> > position) {
    return position.element().getValue();
  }

  protected Entry<K,V> entry(Position<Entry<K,V> > position) {
    return position.element();
  }

  protected void replaceEntry(Position <Entry<K,V> > pos, Entry<K,V> ent) {
    ((BSTEntry <K,V>) ent).pos = pos;
    replace(pos,ent);
  }

  protected void checkKey(K key) throws InvalidKeyException {
    if(key == null) throw new InvalidKeyException("chave nula");
  }

  protected void checkEntry(Entry<K,V> ent) throws InvalidEntryException {
    if(ent == null || !(ent instanceof BSTEntry))
      throw new InvalidKeyException("entrada inválida");
  }

  protected Entry<K,V> insertAtExternal(Position<Entry<K,V> > v, Entry<K,V> e) {
    expandExternal(v, null, null);
    replace(v, e);
    numEntries++;

    return e;
  }

  protected void removeExternal(Position<Entry<K,V> > v) {
    removeAboveExternal(v);
    numEntries--;
  }

  protected Position<Entry<K,V> > treeSearch(K key, Position<Entry<K,V> > pos) {
    if(isExternal(pos)) return pos;
    else {
      K curKey = key(pos);
      int comp = C.compare(key, curKey);
      if(comp < 0) return treeSearch(key, left(pos));
      else if(comp > 0) return treeSearch(key, right(pos));
      return pos;
    }
  }

  protected void addAll(LinkedList<Entry<K,V> > L, Position<Entry<K,V> > v, K k) {
    if (isExternal(v)) return;
    Position<Entry<K, V> > pos = treeSearch(k,v);
    if(!isExternal(pos)) {
      addAll(L, left(pos), k);
      L.addLast(pos.element());
      addAll(L, right(pos), k);
    }
  }

  // aqui começam os métodos públicos

  public int size() { return numEntries; }

  public boolean isEmpty() { return size() == 0; }

  public Entry<K,V> find(K key) throws InvalidKeyException {
    checkKey(key);
    Position<Entry<K,V> > curPos = treeSearch(key, root());
    actionPos = curPos;
    if(isInternal(curPos)) return entry(curPos);
    return null;
  }

  public Iterable<Entry<K,V> > findAll(K key) throws InvalidKeyException {
    checkKey(key);
    LinkedList<Entry<K, V> > L = new LinkedList<Entry<K,V> >();
    addAll(L, root(), key);
    return L;
  }

  public Entry<K,V> insert(K k, V x) throws InvalidKeyException {
    checkKey(k);
    Position<Entry<K,V> > insPos = treeSearch(k, root());
    while(!isExternal(insPos))
      insPos = treeSearch(k, left(insPos));
    actionPos = insPos;
    return insertAtExternal(insPos, new BSTEntry<K,V>(k, x, insPos));
  }

  public Entry<K,V> remove(Entry<K,V> ent) throws InvalidEntryException {
    checkEntry(ent);
    Position<Entry<K,V> > rmPos = ((BSTEntry<K,V>)ent).position();
    Entry<K,V> toReturn = entry(rmPos);
    if(isExternal(left(rmPos))) rmPos = right(rmPos);
    else if(isExternal(right(rmPos))) rmPos = left(rmPos);
    else {
      Position<Entry<K,V> > swapPos = rmPos;
      rmPos = right(swapPos);
      do {
        rmPos = left(rmPos);
      } while (isInternal(rmPos));
      replaceEntry(swapPos, (Entry<K,V>) parent(rmPos).element());
    }
    actionPos = sibling(rmPos);
    removeExternal(rmPos);

    return toReturn;
  }

  protected static class IntComparator implements Comparator<Integer> {
      public int compare(Integer v1, Integer v2) {
        return Integer.compare(v1, v2);
      }
  }

  protected void expandExternal(Position<Entry<K,V>> v, Entry<K,V> l, Entry<K,V> r) throws InvalidPositionException {
		    if (!isExternal(v)) 
		      throw new InvalidPositionException("Node não é externo");
		    insertLeft(v, l);
		    insertRight(v, r);
  }
  
  protected void removeAboveExternal(Position <Entry<K,V> > v) throws InvalidPositionException {
			    if (!isExternal(v)) 
			      throw new InvalidPositionException("Node não é externo");
			    if (isRoot(v))
			      remove(entry(v));
			    else {
			      Position<Entry<K,V>> u = parent(v);
			      remove(entry(v));
			      remove(entry(u));
			    }
			  }
  
  public static void main(String[] args){
	long inicio = System.currentTimeMillis();
	BinarySearchTree<Integer, String> bst = new BinarySearchTree<Integer, String>(new IntComparator());
    try {
    	String tarefa = "/home/tamires/Desktop/2020.2/AED/EP3/entradas/tarefas1000.txt"; // inserir aqui o caminho da entrada
	    File entrada = new File(tarefa);
		Scanner scan = new Scanner(entrada);
		
		String saida = "";
		while(scan.hasNextLine()) {
			String linha = scan.nextLine();
			String tarefa2 = "/home/tamires/Desktop/2020.2/AED/EP3/verificar/verificar1000.txt"; // inserir aqui o caminho da verificacao
			File entrada2 = new File(tarefa2);
			scan2 = new Scanner(entrada2);
			while(scan2.hasNextLine()) {
				String linha2 = scan2.nextLine();
				if (linha.equals(linha2)) {
					bst.insert(numEntries, linha);
					saida += linha + "\n";
					break;
				}
				else {
					continue;
				}
			}
		}
		Files.write(Paths.get("/home/tamires/Desktop/2020.2/AED/EP3/saidas/saidas1000.txt"), saida.getBytes());
		long fim = System.currentTimeMillis();
		System.out.println("saida "+ saida);
		System.out.println("Tempo de execução, em milisegundos: "+ (fim - inicio));
		scan.close();
		scan2.close();
    }catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
}
