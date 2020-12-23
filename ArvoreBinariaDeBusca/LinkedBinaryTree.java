import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LinkedBinaryTree<E> implements BinaryTree<E> {
  protected BTPosition<E> root;
  protected int size;

  public LinkedBinaryTree() {
    root = null;
    size = 0;
  }

  protected BTPosition<E> checkPosition(Position<E> p)
    throws InvalidPositionException {
    if(p == null || !(p instanceof BTPosition))
      throw new InvalidPositionException("Posição inválida.");
    return (BTPosition<E>) p;
  }

  public int size() { return size; }

  public boolean isEmpty() { return size == 0; }

  public Iterable<Position <E> > positions() {
    List<Position<E> > positions = new LinkedList<Position<E> >();
    if(!isEmpty()) preorderPositions(root(), positions);
    return positions;
  }

  private void preorderPositions(Position<E> v, List<Position<E> > positions) {
    positions.add(v);
    if(hasLeft(v)) preorderPositions(left(v), positions);
    if(hasRight(v)) preorderPositions(right(v), positions);
  }

  public Iterator<E> iterator() {
    Iterable<Position<E> > Positions = positions();
    List<E> elements = new LinkedList<E>();
    for(Position<E> p : Positions)
      elements.add(p.element());
    return elements.iterator();
  }

  public E replace(Position<E> p, E e) throws InvalidPositionException {
    BTPosition<E> tmp = checkPosition(p);
    E result = tmp.element();
    tmp.setElement(e);
    return result;
  }

  public Position<E> root() throws EmptyTreeException {
    if(root == null) throw new EmptyTreeException("Árvore vazia.");
    return root;
  }

  public Iterable<Position<E> > children(Position<E> p)
    throws InvalidPositionException {
    BTPosition<E> tmp = checkPosition(p);
    List<Position<E> > children =  new LinkedList<Position<E> >();
    if(hasLeft(tmp)) children.add(left(tmp));
    if(hasRight(tmp)) children.add(right(tmp));
    return children;
  }

  public boolean isInternal(Position<E> p) throws InvalidPositionException {
    BTPosition<E> tmp = checkPosition(p);
    return (hasLeft(tmp) || hasRight(tmp));
  }

  public boolean isExternal(Position<E> p) throws InvalidPositionException {
    return !isInternal(p);
  }

  public boolean isRoot(Position<E> p) throws InvalidPositionException {
    BTPosition<E> tmp = checkPosition(p);
    return tmp == root;
  }

  public Position<E> left(Position<E> v)
    throws InvalidPositionException, BoundaryViolationException {
    BTPosition<E> tmp = checkPosition(v);
    Position<E> left = tmp.getLeft();

    if(left == null)
      throw new BoundaryViolationException("Não possui filho esquerdo");
    return left;
  }

  public Position<E> right(Position<E> v)
    throws InvalidPositionException, BoundaryViolationException {
    BTPosition<E> tmp = checkPosition(v);
    Position<E> right = tmp.getRight();

    if(right == null)
      throw new BoundaryViolationException("Não possui filho direito");
    return right;
  }

  public Position<E> parent(Position<E> v)
    throws InvalidPositionException, BoundaryViolationException {
    BTPosition<E> tmp = checkPosition(v);
    Position<E> parent = tmp.getParent();

    if(parent == null)
      throw new BoundaryViolationException("Não possui pai");
    return parent;
  }

  public boolean hasLeft(Position<E> p) throws InvalidPositionException {
    BTPosition<E> tmp = checkPosition(p);
    return tmp.getLeft() != null;
  }

  public boolean hasRight(Position<E> p) throws InvalidPositionException {
    BTPosition<E> tmp = checkPosition(p);
    return tmp.getRight() != null;
  }

  public Position<E> sibling(Position<E> v)
    throws InvalidPositionException, BoundaryViolationException {
    BTPosition<E> tmp = checkPosition(v);
    BTPosition<E> parent = tmp.getParent();
    if(parent != null){
      BTPosition<E> sib = parent.getLeft();
      if(sib == tmp) sib = parent.getRight();
      if(sib != null) return sib;
    }
    throw new BoundaryViolationException("Não tem irmão.");
  }

  protected BTPosition<E> createNode(Comparable<E> e, BTNode<E> p, BTNode<E> l, BTNode<E> r) {
    return new BTNode<E>((E)e, p, l, r);
  }

  public Position<E> addRoot(Comparable<E> e) throws NonEmptyTreeException {
    if(!isEmpty()) throw new NonEmptyTreeException("A árvore já tem raiz");
    size = 1;
    root = createNode(e, null, null, null);
    return root;
  }

  public Position<E> insertLeft(Position<E> p, Comparable<E> e)
    throws InvalidPositionException {
    BTPosition<E> tmp = checkPosition(p);
    if(tmp.getLeft() != null)
      throw new InvalidPositionException("Nó já possui filho esquerdo");
    BTPosition<E> left = createNode(e, null, null, null);
    tmp.setLeft(left);
    size++;
    return left;
  }

  public Position<E> insertRight(Position<E> p, Comparable<E> e)
    throws InvalidPositionException {
    BTPosition<E> tmp = checkPosition(p);
    if(tmp.getRight() != null)
      throw new InvalidPositionException("Nó já possui filho esquerdo");
    BTPosition<E> right = createNode(e, null, null, null);
    tmp.setRight(right);
    size++;
    return right;
  }
  
  public Position<E> insert(Position <E> p, Comparable<E> e)
  	throws InvalidPositionException {
	  BTPosition<E> tmp = checkPosition(p);
	  if(e.compareTo(tmp.element()) < 0) {
		  if(tmp.getLeft() != null) {
			  insert(tmp.getLeft(), e);
		  }
		  else {
			  this.insertLeft(p, e);
		  }
	  }
	  else {
		  if(tmp.getRight() != null) {
			  insert(tmp.getRight(), e);
		  }
		  else {
			  this.insertRight(p, e);
		  }
	  }
	  return tmp;
  }
  
  boolean find(Position <E> p, Comparable<E> e) {
	  BTPosition<E> tmp = checkPosition(p);
	  if(e.compareTo(tmp.element()) == 0) {
		  return true;
	  }
	  else if (e.compareTo(tmp.element()) < 0) {
		  if(tmp.getLeft() != null) {
		  find(tmp.getLeft(), e);
		  }
	  }
	  else if (e.compareTo(tmp.element()) > 0) {
		  if(tmp.getRight() != null) {
		  find(tmp.getRight(), e);
		  }
	  }
	  return false;
  }

  public static void main(String[] args) throws Exception{
	long inicio = System.currentTimeMillis();
    LinkedBinaryTree<Integer> BT = new LinkedBinaryTree<Integer>();
    String caminho = "C:\\Users\\Usuario\\Desktop\\epAed\\tarefas99000.txt"; // inserir aqui o caminho da entrada
    File entrada = new File(caminho);
	Scanner scan = new Scanner(entrada);
	String saida = "";
	Integer tarefa = 0;
	BTPosition<Integer> root = (BTPosition<Integer>) BT.addRoot(500000);
	while(scan.hasNextLine()) {
		String linha = scan.nextLine();
		if (linha.equals("")) {
			continue;
		}
		tarefa = Integer.parseInt(linha);
		BT.insert(root, tarefa);
	}
	caminho = "C:\\Users\\Usuario\\Desktop\\epAed\\verificar\\verificar99000.txt"; // inserir aqui o caminho da verificacao
	entrada = new File(caminho);
	scan.close();
	scan = new Scanner(entrada);
	while(scan.hasNextLine()) {
		String linha = scan.nextLine();
		tarefa = Integer.parseInt(linha);
		if(BT.find(root, tarefa)) {
			String tarefaString = Integer.toString(tarefa);
			saida += tarefaString + "\n";
			System.out.println(saida + "saida");
		}
	}
	Files.write(Paths.get("C:\\Users\\Usuario\\Desktop\\saida\\tarefas99000.txt"), saida.getBytes()); // inserir aqui o caminho da saída
    for(Position<Integer> e : BT.positions())
      System.out.println(e.element());
    long fim = System.currentTimeMillis();
    System.out.println(fim - inicio);
    scan.close();
  }
}