package aed.trees;

import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class FTree<Key extends Comparable<Key>, Value>
{
    private Node<Key, Value> root;

    public FTree(int degree)
    {
        assert(degree >= 2);
        root = new Node<>(degree);
    }

    public int size()
    {
        return root.size();
    }

    public int height()
    {
        if (root.size() == 0)
            return 0;
        return root.height();
    }

    public boolean contains(Key k)
    {
        if (root == null)
            return false;
        return root.contains(k);
    }

    public Value get(Key k)
    {
        if (root == null)
            return null;
        return root.get(k);
    }

    public void put(Key k, Value v)
    {
        root = root.putInRoot(k, v);
    }

    public Iterable<Key> keys()
    {
        Queue<Key> queue = new LinkedList<>();
        root.inOrderTraversalKeys(queue);
        return queue;
    }

    public Iterable<Value> values()
    {
        Queue<Value> queue = new LinkedList<>();
        root.inOrderTraversalValues(queue);
        return queue;
    }

    public Key min()
    {
        if (root.size() == 0)
            return null;
        return root.min();
    }

    public Key max()
    {
        if (root.size() == 0)
            return null;
        return root.max();
    }

    public int rank(Key k)
    {
        if (root == null) {
            return 0;
        }
        return root.rank(k);
    }

    public Key select(int n)
    {
        if (root == null || n < 0 || n >= root.size()) {
            return null;
        }
        return root.selectNth(n);
    }

    public int size(Key min, Key max)
    {
        if (root == null) {
            return 0;
        }
        return root.sizeInRange(min, max);
    }

    public Key floor(Key k)
    {
        if (root == null || root.size() == 0) {
            return null;
        }
        return root.findFloor(k);
    }

    public Key ceiling(Key k)
    {
        if (root == null || root.size() == 0) {
            return null;
        }
        return root.findCeiling(k);
    }

    public Iterable<Key> keys(Key min, Key max)
    {
        Queue<Key> queue = new LinkedList<>();
        root.collectKeysInRange(min, max, queue);
        return queue;
    }

    public Iterable<Value> values(Key min, Key max)
    {
        Queue<Value> queue = new LinkedList<>();
        root.collectValuesInRange(min, max, queue);
        return queue;
    }

    public void printlnKeys()
    {
        if(root == null){
            return;
        }
        breadthFirstPrint(root);
    }

    public String makeString (){
        return root.makeString();
    }

    private void breadthFirstPrint(Node<Key, Value> root) {
        Queue<Node<Key, Value>> queue = initializeQueue(root);
        int currentDepth = 0;

        boolean firstInteration = true;;

        while (!queue.isEmpty()) {
            Node<Key, Value> node = queue.poll();
            int nodeDepth = node.height();

            if (nodeDepth != currentDepth) {
                if (!firstInteration) {
                    System.out.println();
                }
                currentDepth = nodeDepth;
            }

            node.printKeys();

            if (firstInteration) {
                firstInteration = false;
            }

            if (!node.isLeaf()) {
                addChildrenToQueue(node, queue);
            }
        }

        System.out.println();
    }

    private Queue<Node<Key, Value>> initializeQueue(Node<Key, Value> root) {
        Queue<Node<Key, Value>> queue = new ArrayDeque<>();
        queue.add(root);
        return queue;
    }

    private void addChildrenToQueue(Node<Key, Value> node, Queue<Node<Key, Value>> queue) {
        for (int i = 0; i <= node.getCountKeys(); i++) {
            queue.add(node.getChildren()[i]);
        }
    }

    public static void unitTestPut (){
        FTree<String, Integer> tree = new FTree<>(2);

        tree.put("jjj", 4);
        tree.put("uuu", 9);
        tree.put("ddd", 25);
        assert tree.root.makeString().equals("[ddd, jjj, uuu][25, 4, 9]3 3\n");

        tree.put("aaa", 352);
        assert tree.makeString().equals("""
                [jjj, null, null][4, null, null]4 1
                    [aaa, ddd, null][352, 25, null]2 2
                    [uuu, null, null][9, null, null]1 1
                """) : tree.makeString();
        tree.put("ggg", 698);
        assert tree.makeString().equals("""
                [jjj, null, null][4, null, null]5 1
                    [aaa, ddd, ggg][352, 25, 698]3 3
                    [uuu, null, null][9, null, null]1 1
                """) : tree.makeString();
        tree.put("www", 76);
        assert tree.makeString().equals("""
                [jjj, null, null][4, null, null]6 1
                    [aaa, ddd, ggg][352, 25, 698]3 3
                    [uuu, www, null][9, 76, null]2 2
                """) : tree.makeString();
        tree.put("zzz", 66);
        assert tree.makeString().equals("""
                [jjj, null, null][4, null, null]7 1
                    [aaa, ddd, ggg][352, 25, 698]3 3
                    [uuu, www, zzz][9, 76, 66]3 3
                """) : tree.makeString();
        tree.put("ccc", 66);
        assert tree.makeString().equals("""
                [ddd, jjj, null][25, 4, null]8 2
                    [aaa, ccc, null][352, 66, null]2 2
                    [ggg, null, null][698, null, null]1 1
                    [uuu, www, zzz][9, 76, 66]3 3
                """) : tree.makeString();
        tree.put("iii", 88);
        assert tree.makeString().equals("""
                [ddd, jjj, null][25, 4, null]9 2
                    [aaa, ccc, null][352, 66, null]2 2
                    [ggg, iii, null][698, 88, null]2 2
                    [uuu, www, zzz][9, 76, 66]3 3
                """) : tree.makeString();
        tree.put("lll", 987);
        assert tree.makeString().equals("""
                [ddd, jjj, www][25, 4, 76]10 3
                    [aaa, ccc, null][352, 66, null]2 2
                    [ggg, iii, null][698, 88, null]2 2
                    [lll, uuu, null][987, 9, null]2 2
                    [zzz, null, null][66, null, null]1 1
                """) : tree.makeString();

        tree.put("mmm", 77);
        assert tree.makeString().equals("""
                [jjj, null, null][4, null, null]11 1
                    [ddd, null, null][25, null, null]5 1
                        [aaa, ccc, null][352, 66, null]2 2
                        [ggg, iii, null][698, 88, null]2 2
                    [www, null, null][76, null, null]5 1
                        [lll, mmm, uuu][987, 77, 9]3 3
                        [zzz, null, null][66, null, null]1 1
                """) : tree.makeString();
        tree.put("nnn", 11);
        assert tree.makeString().equals("""
                [jjj, null, null][4, null, null]12 1
                    [ddd, null, null][25, null, null]5 1
                        [aaa, ccc, null][352, 66, null]2 2
                        [ggg, iii, null][698, 88, null]2 2
                    [mmm, www, null][77, 76, null]6 2
                        [lll, null, null][987, null, null]1 1
                        [nnn, uuu, null][11, 9, null]2 2
                        [zzz, null, null][66, null, null]1 1
                """) : tree.makeString();
        tree.put("ooo", 362);
        assert tree.makeString().equals("""
                [jjj, null, null][4, null, null]13 1
                    [ddd, null, null][25, null, null]5 1
                        [aaa, ccc, null][352, 66, null]2 2
                        [ggg, iii, null][698, 88, null]2 2
                    [mmm, www, null][77, 76, null]7 2
                        [lll, null, null][987, null, null]1 1
                        [nnn, ooo, uuu][11, 362, 9]3 3
                        [zzz, null, null][66, null, null]1 1
                """) : tree.makeString();
        tree.put("ppp", 55);
        assert tree.makeString().equals("""
                [jjj, ooo, null][4, 362, null]14 2
                    [ddd, null, null][25, null, null]5 1
                        [aaa, ccc, null][352, 66, null]2 2
                        [ggg, iii, null][698, 88, null]2 2
                    [mmm, null, null][77, null, null]3 1
                        [lll, null, null][987, null, null]1 1
                        [nnn, null, null][11, null, null]1 1
                    [www, null, null][76, null, null]4 1
                        [ppp, uuu, null][55, 9, null]2 2
                        [zzz, null, null][66, null, null]1 1
                """) : tree.makeString();

    }


    public static FTree<String, Integer> tree3(){
        FTree<String, Integer> tree = new FTree<>(2);
        assert tree.height() == 0 : tree.height();
        tree.put("jjj", 4);
        assert tree.height() == 0 : tree.height();
        tree.put("uuu", 9);
        assert tree.height() == 0 : tree.height();
        tree.put("ddd", 25);
        tree.put("aaa", 352);
        assert tree.height() == 1 : tree.height();
        tree.put("ggg", 698);
        tree.put("www", 76);
        tree.put("zzz", 66);
        tree.put("ccc", 66);
        tree.put("iii", 88);
        tree.put("lll", 987);
        tree.put("mmm", 77);
        tree.put("nnn", 11);
        tree.put("ooo", 362);
        tree.put("ppp", 55);
        assert tree.makeString().equals("""
                [jjj, ooo, null][4, 362, null]14 2
                    [ddd, null, null][25, null, null]5 1
                        [aaa, ccc, null][352, 66, null]2 2
                        [ggg, iii, null][698, 88, null]2 2
                    [mmm, null, null][77, null, null]3 1
                        [lll, null, null][987, null, null]1 1
                        [nnn, null, null][11, null, null]1 1
                    [www, null, null][76, null, null]4 1
                        [ppp, uuu, null][55, 9, null]2 2
                        [zzz, null, null][66, null, null]1 1
                """) : tree.makeString();
        return tree;
    }

    public static void unitTestContains (){
        FTree <String, Integer> tree = tree3 ();
        assert tree.height() == 2;
        assert tree.contains("jjj");
        assert tree.contains("uuu");
        assert tree.contains("ddd");
        assert tree.contains("aaa");
        assert tree.contains("ggg");
        assert tree.contains("www");
        assert tree.contains("zzz");
        assert tree.contains("ccc");
        assert tree.contains("iii");
        assert tree.contains("lll");
        assert tree.contains("mmm");
        assert tree.contains("nnn");
        assert tree.contains("ooo");
        assert tree.contains("ppp");

        assert !tree.contains("a");
        assert !tree.contains("zzzzz");
        assert !tree.contains("mmmm");

    }

    public static void unitTestGet (){
        FTree<String, Integer> tree = tree3 ();
        assert tree.get("jjj").equals(4);
        assert tree.get("uuu").equals(9);
        assert tree.get("ddd").equals(25);
        assert tree.get("aaa").equals(352);
        assert tree.get("ggg").equals(698);
        assert tree.get("www").equals(76);
        assert tree.get("zzz").equals(66);
        assert tree.get("ccc").equals(66);
        assert tree.get("iii").equals(88);
        assert tree.get("lll").equals(987);
        assert tree.get("mmm").equals(77);
        assert tree.get("nnn").equals(11);
        assert tree.get("ooo").equals(362);
        assert tree.get("ppp").equals(55);

        assert tree.get("kkk") == null;
        assert tree.get("aa") == null;
        assert tree.get("h") == null;
        assert tree.get("x") == null;

    }

    public static void unitTestMin(){
        FTree<String, Integer> tree = new FTree<>(2);
        tree.put("jjj", 4);
        assert tree.min().equals("jjj");
        tree.put("uuu", 9);
        assert tree.min().equals("jjj");
        tree.put("ddd", 25);
        assert tree.min().equals("ddd");
        tree.put("aaa", 352);
        assert tree.min().equals("aaa");
        tree.put("ggg", 698);
        assert tree.min().equals("aaa");
        tree.put("www", 76);
        assert tree.min().equals("aaa");
        tree.put("zzz", 66);
        assert tree.min().equals("aaa");
        tree.put("ccc", 66);
        assert tree.min().equals("aaa");
        tree.put("iii", 88);
        assert tree.min().equals("aaa");
        tree.put("lll", 987);
        assert tree.min().equals("aaa");
        tree.put("mmm", 77);
        assert tree.min().equals("aaa");
        tree.put("nnn", 11);
        assert tree.min().equals("aaa");
        tree.put("ooo", 362);
        assert tree.min().equals("aaa");
        tree.put("ppp", 55);
        assert tree.min().equals("aaa");
    }

    public static void unitTestMax(){
        FTree<String, Integer> tree = new FTree<>(2);
        tree.put("jjj", 4);
        assert tree.max().equals("jjj");
        tree.put("uuu", 9);
        assert tree.max().equals("uuu");
        tree.put("ddd", 25);
        assert tree.max().equals("uuu");
        tree.put("aaa", 352);
        assert tree.max().equals("uuu");
        tree.put("ggg", 698);
        assert tree.max().equals("uuu");
        tree.put("www", 76);
        assert tree.max().equals("www");
        tree.put("zzz", 66);
        assert tree.max().equals("zzz");
        tree.put("ccc", 66);
        assert tree.max().equals("zzz");
        tree.put("iii", 88);
        assert tree.max().equals("zzz");
        tree.put("lll", 987);
        assert tree.max().equals("zzz");
        tree.put("mmm", 77);
        assert tree.max().equals("zzz");
        tree.put("nnn", 11);
        assert tree.max().equals("zzz");
        tree.put("ooo", 362);
        assert tree.max().equals("zzz");
        tree.put("ppp", 55);
        assert tree.max().equals("zzz");
    }

    public static void testSize() {
        FTree<String, Integer> tree = new FTree<>(2);

        System.out.println(tree.size() == 0); // Empty tree

        tree.put("a", 1);
        System.out.println(tree.size() == 1); // One key-value pair

        tree.put("b", 2);
        tree.put("c", 3);
        System.out.println(tree.size() == 3); // Three key-value pairs

        tree.put("d", 4);
        tree.put("e", 5);
        System.out.println(tree.size() == 5); // Five key-value pairs

        System.out.println("Size test passed!");
    }

    public static void testSize2() {
        FTree<String, Integer> tree = new FTree<>(2);
        tree.put("a", 1);
        tree.put("b", 2);
        tree.put("c", 3);
        tree.put("d", 4);
        tree.put("e", 5);

        System.out.println(tree.size("b", "d"));  //print 3

        System.out.println(tree.size("a", "e"));  //print 5

        System.out.println(tree.size("g", "z"));  //print 0

    }

    public static void testString(){
        FTree<String, Integer> tree = new FTree<>(2);
        System.out.println("Testing String");
        tree.printlnKeys();
        tree.put("jjj", 4);
        tree.put("uuu", 9);
        tree.put("ddd", 25);
        tree.put("aaa", 352);
        tree.put("ggg", 698);
        tree.put("www", 76);
        tree.put("zzz", 66);
        tree.put("ccc", 66);
        tree.put("iii", 88);;
        tree.put("lll", 987);
        tree.put("mmm", 77);
        tree.put("nnn", 11);
        tree.put("ooo", 362);
        tree.put("ppp", 55);
        tree.put ("eee", 6);
        tree.put ("fff", 8);
        tree.put("bbb", 1);
        tree.put("hhh", 2);
        tree.put ("kkk", 3);
        tree.put("qqq", 3);
        tree.put("rrr", 2);
        tree.put ("sss", 6);
        tree.printlnKeys();
        System.out.println("_______________________________");
        tree.put ("ttt", 8);
        tree.printlnKeys();
        System.out.println("Ok");
    }

    public static void unitTests(){
        //unitTestPut();
        //unitTestContains();
        //unitTestGet();
        unitTestMin();
        unitTestMax();
        //tree3();
        testSize();
        testSize2();
        testString();
    }

    public static void main(String[] args)
    {
        System.out.println("Class FTree\n");
        Node.unitTests();
        unitTests();
        System.out.println("All tests passed\n");
    }
}

class Node <Key extends Comparable<Key>, Value>{

    private Key[] keys;
    private Value[] values;
    private Node<Key, Value>[] children;
    private int size;
    private int countKeys;

    @SuppressWarnings("unchecked")
    public Node (int degree){
        this.keys = (Key[]) new Comparable[2*degree - 1];
        this.values = (Value[]) new Object[2*degree - 1];
        this.children = new Node[2*degree];
        this.size = 0; //numero de chaves na arvore
        this.countKeys = 0; // numero de chaves no nó
    }

    public boolean isLeaf (){
        return children[0] == null;
    }

    public int size () {
        return size;
    }

    public int height (){
        int result = 0;
        if (isLeaf())
            result = 0;
        else {
            result = children[0].height () + 1;
        }
        return result;
    }

    public boolean isFull (){
        return countKeys == this.keys.length;
    }


    public static <T> int find(T[] a, int n, T x){
        for (int i = 0; i < n; i++)
            if (a[i].equals(x))
                return i;
        return -1;
    }


    public Value get(Key k)
    {
        Value result;
        int x = find (keys, countKeys, k);
        if (x != -1)
            result = values [x];
        else if (isLeaf())
            result = null;
        else {
            int r = rank(keys, countKeys, k);
            result = children[r].get(k);
        }
        return result;
    }

    public boolean contains(Key k)
    {
        boolean result;
        result = find (keys, countKeys, k) != -1;
        if (!isLeaf() && !result) {
            int x = rank(keys, countKeys, k);
            result = children[x].contains(k);
        }
        return result;
    }


    public Key min (){
        return isLeaf() ? keys[0] : children[0].min();
    }

    public Key max (){
        return isLeaf() ? keys[countKeys - 1] : children[countKeys].max();
    }

    public static Node<String, Integer> onlyRoot() {
        Node<String, Integer> root = new Node<>(2);
        root.keys = new String[]{"ddd", "jjj", "uuu"};
        root.values = new Integer[]{25, 4, 9};
        root.size = 3;
        root.countKeys = 3;
        return root;
    }

    public static Node<String, Integer> node2() {
        Node<String, Integer> n1 = new Node<>(2);
        n1.keys = new String[]{"bbb", "eee", null};
        n1.values = new Integer[]{3, 1, null};
        n1.size = 2;
        n1.countKeys = 2;

        Node<String, Integer> n2 = new Node<>(2);
        n2.keys = new String[]{"ppp", null, null};
        n2.values = new Integer[]{54, null, null};
        n2.size = 1;
        n2.countKeys = 1;

        Node<String, Integer> n3 = new Node<>(2);
        n3.keys = new String[]{"kkk", null, null};
        n3.values = new Integer[]{98, null, null};
        n3.size = 4;
        n3.countKeys = 1;
        n3.children[0] = n1;
        n3.children[1] = n2;

        return n3;
    }

    public String makeString (){
        return makeString("");
    }


    public String makeString (String margin){
        String result = margin;
        if (isLeaf()){
            result += Arrays.toString(keys) + Arrays.toString(values) + size + " " + countKeys + "\n";
        }
        else{
            result += Arrays.toString(keys) + Arrays.toString(values) + size + " " + countKeys + "\n";
            for (int i = 0; i < countKeys + 1; i++){
                result += children[i].makeString(margin + "    ");
            }
        }
        return result;
    }

    private static <T extends Comparable<T>> int rank(T[] a, int n, T s){
        int result = 0;
        for (int i = 0; i < n; i++){
            if (a[i].compareTo(s) < 0)
                result++;
        }
        return result;
    }

    // Number of keys in this node which are (strictly) less than ´key`.
    private int localRank(Key key)
    {
        int result = 0;
        while (result < countKeys && keys[result].compareTo(key) < 0)
            result++;
        return result;
    }


    private static <T> void shift(T[] a, int n, int x) {
        for (int i = 0; i < x; i++)
            a[n-i] = a[n-i-1];
    }

    public int putInLeaf(Key k, Value v) {
        int result = 0;
        //assert isLeaf();
        assert !isFull();
        int z = localRank(k); // Substituímos por localRank
        if (z < countKeys && keys[z].compareTo(k) == 0)
            values[z] = v;
        else {
            shift(keys, countKeys, countKeys - z);
            keys[z] = k;
            shift(values, countKeys, countKeys - z);
            values[z] = v;
            result = 1;
            countKeys++;
            size++;
        }
        return result;
    }

    public int tryPutBelow(Key k, Value v) {
        int result;
        int z = localRank(k); // Substituímos por localRank
        Node<Key, Value> zNode = children[z];
        result = insertInNode(zNode, k, v);
        if (result != -1) {
            size += result;
        }

        return result;
    }

    private int insertInNode(Node<Key, Value> zNode, Key k, Value v) {
        int result;

        int z = rank(zNode.keys, zNode.countKeys, k); // Mantido, pois usamos outro nó (zNode)
        if (z < zNode.countKeys && zNode.keys[z].compareTo(k) == 0) {
            zNode.values[z] = v;
            return 0;
        }

        if (zNode.isFull()) {
            Node<Key, Value> sNode = zNode.split();
            this.merge(sNode);

            z = localRank(k); // Substituímos por localRank
            zNode = children[z];

            result = insertInNode(zNode, k, v);
        } else if (zNode.isLeaf() && !zNode.isFull()) {
            result = zNode.putInLeaf(k, v);
        } else {
            result = zNode.tryPutBelow(k, v);
        }

        return result;
    }

    public Node<Key, Value> putInRoot(Key k, Value v) {
        if (isFull()) {
            Node<Key, Value> newRoot = split();
            newRoot.putInRoot(k, v);
            return newRoot;
        } else if (isLeaf()) {
            assert !isFull();
            this.putInLeaf(k, v);
            return this;
        } else {
            assert !isLeaf();
            assert !isFull();
            this.tryPutBelow(k, v);
            return this;
        }
    }


    public int degree(){
        return children.length/2;
    }


    public Node<Key, Value> splitLeaf (){
        assert isFull();
        assert isLeaf();
        Node<Key, Value> result = new Node<>(degree());
        result.putInLeaf(keys[degree() - 1], values[degree() - 1]);
        Node<Key, Value> left = new Node<>(degree()) ;
        for (int i = 0; i < degree() - 1; i++)
            left.putInLeaf(keys[i], values[i]);
        Node<Key, Value> right = new Node<>(degree()) ;
        for (int i = 0; i < degree() - 1; i++)
            right.putInLeaf(keys[i + degree()], values[i + degree()]);

        result.children[0]=left;
        result.children[1]=right;
        result.size = this.size;

        return result;
    }

    public Node<Key, Value> leftNode (){
        Node<Key, Value> result = new Node<>(degree());
        for (int i = 0; i < degree() - 1; i++)
            result.putInLeaf(keys[i], values[i]);
        result.size = degree() - 1;
        if (!isLeaf()) {
            for (int i = 0; i < degree(); i++) {
                result.children[i] = this.children[i];
                result.size+= this.children[i].size;
            }
        }
        return result;
    }

    public Node<Key, Value> rightNode (){
        int start = degree();
        Node<Key, Value> result = new Node<>(degree());
        for (int i = 0; i < degree() - 1; i++)
            result.putInLeaf(keys[start + i], values[start + i]);
        if (!isLeaf()) {
            for (int i = 0; i < degree(); i++) {
                result.children[i] = this.children[i + start];
                result.size+= this.children[i + start].size;
            }
        }
        return result;
    }

    public Node<Key, Value> split (){
        assert isFull();
        Node<Key, Value> left = leftNode();
        Node<Key, Value> right = rightNode();
        Node<Key, Value> result = new Node<Key, Value>(degree());
        result.putInLeaf(keys[degree() - 1], values[degree() - 1]);

        result.children[0]=left;
        result.children[1]=right;
        result.size += left.size + right.size;

        return result;
    }

    public void merge (Node<Key, Value> other){
        //other is split result
        assert !isFull();
        int r = rank(this.keys, this.countKeys, other.keys[0]);
        shift(this.keys, this.countKeys, this.countKeys - r);
        shift(this.values, this.countKeys, this.countKeys - r);
        this.keys[r] = other.keys[0];
        this.values[r] = other.values[0];
        shift(this.children, this.countKeys + 1, (this.countKeys + 1) - r);
        this.children[r] = other.children[0];
        this.children[r+1] = other.children[1];
        this.countKeys++;
    }

    public void printKeys() {
        System.out.print("[");
        for (int i = 0; i < countKeys; i++) {
            System.out.print(keys[i]);
            if (i < countKeys - 1) {
                System.out.print(",");
            }
        }
        System.out.print("]");
    }

    public Node<Key, Value>[] getChildren() {
        return children;
    }

    public int getCountKeys() {
        return countKeys;
    }

    public void inOrderTraversalKeys(Queue<Key> queue) {
        if (isLeaf()) {
            for (int i = 0; i < countKeys; i++) {
                queue.add(keys[i]);
            }
        } else {
            for (int i = 0; i < countKeys; i++) {
                children[i].inOrderTraversalKeys(queue);
                queue.add(keys[i]);
            }
            children[countKeys].inOrderTraversalKeys(queue);
        }
    }

    public void collectKeysInRange(Key min, Key max, Queue<Key> queue) {
        if (isLeaf()) {
            for (int i = 0; i < countKeys; i++) {
                if (keys[i].compareTo(min) >= 0 && keys[i].compareTo(max) <= 0) {
                    queue.add(keys[i]);
                }
            }
        } else {
            for (int i = 0; i < countKeys; i++) {
                if (keys[i].compareTo(min) >= 0) {
                    children[i].collectKeysInRange(min, max, queue);
                }
                if (keys[i].compareTo(min) >= 0 && keys[i].compareTo(max) <= 0) {
                    queue.add(keys[i]);
                }
            }
            if (children[countKeys] != null && keys[countKeys - 1].compareTo(max) <= 0) {
                children[countKeys].collectKeysInRange(min, max, queue);
            }
        }
    }

    public void inOrderTraversalValues(Queue<Value> queue) {
        if (isLeaf()) {
            for (int i = 0; i < countKeys; i++) {
                queue.add(values[i]);
            }
        } else {
            for (int i = 0; i < countKeys; i++) {
                children[i].inOrderTraversalValues(queue);
                queue.add(values[i]);
            }
            children[countKeys].inOrderTraversalValues(queue);
        }
    }

    public void collectValuesInRange(Key min, Key max, Queue<Value> queue) {
        if (isLeaf()) {
            for (int i = 0; i < countKeys; i++) {
                if (keys[i].compareTo(min) >= 0 && keys[i].compareTo(max) <= 0) {
                    queue.add(values[i]);
                }
            }
        } else {
            for (int i = 0; i < countKeys; i++) {
                if (keys[i].compareTo(min) >= 0) {
                    children[i].collectValuesInRange(min, max, queue);
                }
                if (keys[i].compareTo(min) >= 0 && keys[i].compareTo(max) <= 0) {
                    queue.add(values[i]);
                }
            }
            if (children[countKeys] != null && keys[countKeys - 1].compareTo(max) <= 0) {
                children[countKeys].collectValuesInRange(min, max, queue);
            }
        }
    }

    public int rank(Key k) {
        int count = 0;

        for (int i = 0; i < countKeys; i++) {
            if (keys[i].compareTo(k) < 0) {
                count++;
                if (children[i] != null) {
                    count += children[i].size;
                }
            }else if (keys[i].compareTo(k) == 0) {
                if (children[i] != null) {
                    count += children[i].size;
                }
                return count;
            }else {
                if (children[i] != null) {
                    count += children[i].rank(k);
                }
                return count;
            }
        }
        if (children[countKeys] != null) {
            count += children[countKeys].rank(k);
        }

        return count;
    }

    public int sizeInRange(Key min, Key max) {
        int count = 0;

        if (isLeaf()) {
            count += countInLeaf(min, max);
        } else {
            count += countInChildren(min, max);
        }

        return count;
    }

    private int countInLeaf(Key min, Key max) {
        int count = 0;

        for (int i = 0; i < countKeys; i++) {
            if (keys[i].compareTo(min) >= 0 && keys[i].compareTo(max) <= 0) {
                count++;
            }
        }
        return count;
    }

    private int countInChildren(Key min, Key max) {
        int count = 0;

        for (int i = 0; i < countKeys; i++) {
            if (keys[i].compareTo(min) >= 0 && keys[i].compareTo(max) <= 0) {
                count++;
            }

            if (children[i] != null) {
                count += children[i].sizeInRange(min, max);
            }

            if (keys[i].compareTo(max) > 0) {
                break;
            }
        }

        if (children[countKeys] != null) {
            count += children[countKeys].sizeInRange(min, max);
        }

        return count;
    }

    public Key findFloor(Key k) {
        Key floorKey = null;

        for (int i = 0; i < countKeys; i++) {
            if (keys[i].compareTo(k) == 0) {
                return keys[i];
            } else if (keys[i].compareTo(k) < 0) {
                floorKey = keys[i];
            } else {
                if (children[i] != null) {
                    return children[i].findFloor(k);
                }
                break;
            }
        }

        if (children[countKeys] != null) {
            Key candidate = children[countKeys].findFloor(k);
            if (candidate != null && (floorKey == null || candidate.compareTo(floorKey) > 0)) {
                floorKey = candidate;
            }
        }

        return floorKey;
    }

    public Key findCeiling(Key k) {
        Key ceilingKey = null;

        for (int i = 0; i < countKeys; i++) {
            if (keys[i].compareTo(k) == 0) {
                return keys[i];
            } else if (keys[i].compareTo(k) > 0) {
                ceilingKey = keys[i];
                if (children[i] != null) {
                    Key candidate = children[i].findCeiling(k);
                    if (candidate != null && candidate.compareTo(ceilingKey) < 0) {
                        ceilingKey = candidate;
                    }
                }
                break;
            }
        }

        if (ceilingKey == null && children[countKeys] != null) {
            ceilingKey = children[countKeys].findCeiling(k);
        }

        return ceilingKey;
    }

    public Key selectNth(int n) {
        int keysOnLeft = 0;

        for (int i = 0; i < countKeys; i++) {
            if (children[i] != null) {
                keysOnLeft = children[i].size();
            }

            if (n < keysOnLeft) {
                assert children[i] != null;
                return children[i].selectNth(n);
            } else if (n == keysOnLeft) {
                return keys[i];
            } else {
                n -= (keysOnLeft + 1);
            }
        }

        if (children[countKeys] != null) {
            return children[countKeys].selectNth(n);
        }

        return null;
    }

    /////////////-----------------------------------------///////////////////////////////////////////////


    public static void unitTestOnlyRoot (){
        Node<String, Integer> root = onlyRoot();
        assert root.makeString().equals("[ddd, jjj, uuu][25, 4, 9]3 3\n") : root.makeString();
    }

    public static void unitTestNode2 (){
        Node<String, Integer> node = node2();
        assert node.makeString().equals("""
                [kkk, null, null][98, null, null]4 1
                    [bbb, eee, null][3, 1, null]2 2
                    [ppp, null, null][54, null, null]1 1
                """) : node.makeString();
    }

    public static void unitTestPutInLeaf () {
        Node<String, Integer> node = new Node<>(2);
        assert node.makeString().equals ("""
                [null, null, null][null, null, null]0 0
                """) : node.makeString();
        node.putInLeaf("ppp", 25);
        assert node.makeString().equals ("""
                [ppp, null, null][25, null, null]1 1
                """) : node.makeString();
        node.putInLeaf("ccc", 4);
        assert node.makeString().equals ("""
                [ccc, ppp, null][4, 25, null]2 2
                """) : node.makeString();
        node.putInLeaf("ppp", 78);
        assert node.makeString().equals ("""
                [ccc, ppp, null][4, 78, null]2 2
                """) : node.makeString();
        node.putInLeaf("eee", 46);
        assert node.makeString().equals ("""
                [ccc, eee, ppp][4, 46, 78]3 3
                """) : node.makeString();

    }


    public static void unitTestSplitLeaf (){
        Node<String, Integer> root = onlyRoot();
        assert root.makeString().equals("[ddd, jjj, uuu][25, 4, 9]3 3\n") : root.makeString();
        Node<String, Integer> s = root.splitLeaf();
        assert s.makeString().equals("""
                [jjj, null, null][4, null, null]3 1
                    [ddd, null, null][25, null, null]1 1
                    [uuu, null, null][9, null, null]1 1
                """) : s.makeString();
    }


    public static void unitTestPutInRoot (){
        Node<String, Integer> node = onlyRoot();
        assert node.makeString().equals("[ddd, jjj, uuu][25, 4, 9]3 3\n") : node.makeString();
        Node<String, Integer> p = node.putInRoot("bbb", 2);
        assert p.makeString().equals("""
                [jjj, null, null][4, null, null]4 1
                    [bbb, ddd, null][2, 25, null]2 2
                    [uuu, null, null][9, null, null]1 1
                """) : p.makeString();
        p.putInRoot("ddd", 107);
        assert p.makeString().equals("""
                [jjj, null, null][4, null, null]4 1
                    [bbb, ddd, null][2, 107, null]2 2
                    [uuu, null, null][9, null, null]1 1
                """) : p.makeString();
        p.putInRoot("ccc", 48);
        assert p.makeString().equals("""
                [jjj, null, null][4, null, null]5 1
                    [bbb, ccc, ddd][2, 48, 107]3 3
                    [uuu, null, null][9, null, null]1 1
                """) : p.makeString();
        Node <String, Integer> q = p.children[0];
        Node <String, Integer> qLeft = q.leftNode();
        assert qLeft.makeString().equals("[bbb, null, null][2, null, null]1 1\n") : qLeft.makeString();
        Node <String, Integer> qRight = q.rightNode();
        assert qRight.makeString().equals("[ddd, null, null][107, null, null]1 1\n") : qRight.makeString();
        Node <String, Integer> s = q.split();
        assert s.makeString().equals("""
                [ccc, null, null][48, null, null]3 1
                    [bbb, null, null][2, null, null]1 1
                    [ddd, null, null][107, null, null]1 1
                """) : s.makeString();

        p.merge(s);
        assert p.makeString().equals("""
                [ccc, jjj, null][48, 4, null]5 2
                    [bbb, null, null][2, null, null]1 1
                    [ddd, null, null][107, null, null]1 1
                    [uuu, null, null][9, null, null]1 1
                """) : p.makeString();

        p.putInRoot("aaa", 465);
        assert p.makeString().equals("""
                [ccc, jjj, null][48, 4, null]6 2
                    [aaa, bbb, null][465, 2, null]2 2
                    [ddd, null, null][107, null, null]1 1
                    [uuu, null, null][9, null, null]1 1
                """) : p.makeString();

    }

    public static void unitTestPutMoreLevels (){
        {
            Node<String, Integer> node = new Node<>(2);
            node.keys = new String[]{"aaa", "bbb", "ccc"};
            node.values = new Integer[]{25, 4, 9};
            node.size = 3;
            node.countKeys = 3;
            Node<String, Integer> root = node.putInRoot("ddd", 352);
            assert root.makeString().equals("""
                    [bbb, null, null][4, null, null]4 1
                        [aaa, null, null][25, null, null]1 1
                        [ccc, ddd, null][9, 352, null]2 2
                    """) : root.makeString();
            root = root.putInRoot("eee", 6);
            assert root.makeString().equals("""
                    [bbb, null, null][4, null, null]5 1
                        [aaa, null, null][25, null, null]1 1
                        [ccc, ddd, eee][9, 352, 6]3 3
                    """) : root.makeString();
            root = root.putInRoot("fff", 78);
            assert root.makeString().equals("""
                    [bbb, ddd, null][4, 352, null]6 2
                        [aaa, null, null][25, null, null]1 1
                        [ccc, null, null][9, null, null]1 1
                        [eee, fff, null][6, 78, null]2 2
                    """) : root.makeString();
            root = root.putInRoot("ggg", 134);
            assert root.makeString().equals("""
                    [bbb, ddd, null][4, 352, null]7 2
                        [aaa, null, null][25, null, null]1 1
                        [ccc, null, null][9, null, null]1 1
                        [eee, fff, ggg][6, 78, 134]3 3
                    """) : root.makeString();
            root = root.putInRoot("hhh", 789);
            assert root.makeString().equals("""
                    [bbb, ddd, fff][4, 352, 78]8 3  
                        [aaa, null, null][25, null, null]1 1
                        [ccc, null, null][9, null, null]1 1
                        [eee, null, null][6, null, null]1 1
                        [ggg, hhh, null][134, 789, null]2 2
                    """) : root.makeString();
//            Node <String, Integer> q = root.split();
//            assert q.makeString().equals("""
//                    [ddd, null, null][352, null, null]8 1
//                        [bbb, null, null][4, null, null]3 1
//                            [aaa, null, null][25, null, null]1 1
//                            [ccc, null, null][9, null, null]1 1
//                        [fff, null, null][78, null, null]4 1
//                            [eee, null, null][6, null, null]1 1
//                            [ggg, hhh, null][134, 789, null]2 2
//                    """) : q.makeString();
//            q = q.putInRoot("iii", 927);
//            assert q.makeString().equals("""
//                    [ddd, null, null][352, null, null]9 1
//                        [bbb, null, null][4, null, null]3 1
//                            [aaa, null, null][25, null, null]1 1
//                            [ccc, null, null][9, null, null]1 1
//                        [fff, null, null][78, null, null]5 1
//                            [eee, null, null][6, null, null]1 1
//                            [ggg, hhh, iii][134, 789, 927]3 3
//                    """) : q.makeString();


            root = root.putInRoot("iii", 927);
            assert root.makeString().equals("""
                    [ddd, null, null][352, null, null]9 1
                        [bbb, null, null][4, null, null]3 1
                            [aaa, null, null][25, null, null]1 1
                            [ccc, null, null][9, null, null]1 1
                        [fff, null, null][78, null, null]5 1
                            [eee, null, null][6, null, null]1 1
                            [ggg, hhh, iii][134, 789, 927]3 3
                    """) : root.makeString();



        }
        {
            Node<String, Integer> node = onlyRoot();
            Node<String, Integer> root = node.putInRoot("aaa", 352);
            assert root.makeString().equals("""
                [jjj, null, null][4, null, null]4 1
                    [aaa, ddd, null][352, 25, null]2 2
                    [uuu, null, null][9, null, null]1 1
                """) : root.makeString();
            root = root.putInRoot("ggg", 698);
            assert root.makeString().equals("""
                [jjj, null, null][4, null, null]5 1
                    [aaa, ddd, ggg][352, 25, 698]3 3
                    [uuu, null, null][9, null, null]1 1
                """) : root.makeString();
            root = root.putInRoot("www", 76);
            assert root.makeString().equals("""
                [jjj, null, null][4, null, null]6 1
                    [aaa, ddd, ggg][352, 25, 698]3 3
                    [uuu, www, null][9, 76, null]2 2
                """) : root.makeString();
            root = root.putInRoot("zzz", 66);
            assert root.makeString().equals("""
                [jjj, null, null][4, null, null]7 1
                    [aaa, ddd, ggg][352, 25, 698]3 3
                    [uuu, www, zzz][9, 76, 66]3 3
                """) : root.makeString();
            root = root.putInRoot("ccc", 66);
            assert root.makeString().equals("""
                [ddd, jjj, null][25, 4, null]8 2
                    [aaa, ccc, null][352, 66, null]2 2
                    [ggg, null, null][698, null, null]1 1
                    [uuu, www, zzz][9, 76, 66]3 3
                """) : root.makeString();
            root = root.putInRoot("iii", 88);
            assert root.makeString().equals("""
                [ddd, jjj, null][25, 4, null]9 2
                    [aaa, ccc, null][352, 66, null]2 2
                    [ggg, iii, null][698, 88, null]2 2
                    [uuu, www, zzz][9, 76, 66]3 3
                """) : root.makeString();
            root = root.putInRoot("lll", 987);
            assert root.makeString().equals("""
                [ddd, jjj, www][25, 4, 76]10 3
                    [aaa, ccc, null][352, 66, null]2 2
                    [ggg, iii, null][698, 88, null]2 2
                    [lll, uuu, null][987, 9, null]2 2
                    [zzz, null, null][66, null, null]1 1
                """) : root.makeString();

            root = root.putInRoot("mmm", 77);
            assert root.makeString().equals("""
                [jjj, null, null][4, null, null]11 1
                    [ddd, null, null][25, null, null]5 1
                        [aaa, ccc, null][352, 66, null]2 2
                        [ggg, iii, null][698, 88, null]2 2
                    [www, null, null][76, null, null]5 1
                        [lll, mmm, uuu][987, 77, 9]3 3
                        [zzz, null, null][66, null, null]1 1
                """) : root.makeString();
            root = root.putInRoot("nnn", 11);
            assert root.makeString().equals("""
                [jjj, null, null][4, null, null]12 1
                    [ddd, null, null][25, null, null]5 1
                        [aaa, ccc, null][352, 66, null]2 2
                        [ggg, iii, null][698, 88, null]2 2
                    [mmm, www, null][77, 76, null]6 2
                        [lll, null, null][987, null, null]1 1
                        [nnn, uuu, null][11, 9, null]2 2
                        [zzz, null, null][66, null, null]1 1
                """) : root.makeString();
            root = root.putInRoot("ooo", 362);
            assert root.makeString().equals("""
                [jjj, null, null][4, null, null]13 1
                    [ddd, null, null][25, null, null]5 1
                        [aaa, ccc, null][352, 66, null]2 2
                        [ggg, iii, null][698, 88, null]2 2
                    [mmm, www, null][77, 76, null]7 2
                        [lll, null, null][987, null, null]1 1
                        [nnn, ooo, uuu][11, 362, 9]3 3
                        [zzz, null, null][66, null, null]1 1
                """) : root.makeString();
            root = root.putInRoot("ppp", 55);
            assert root.makeString().equals("""
                [jjj, ooo, null][4, 362, null]14 2
                    [ddd, null, null][25, null, null]5 1
                        [aaa, ccc, null][352, 66, null]2 2
                        [ggg, iii, null][698, 88, null]2 2
                    [mmm, null, null][77, null, null]3 1
                        [lll, null, null][987, null, null]1 1
                        [nnn, null, null][11, null, null]1 1
                    [www, null, null][76, null, null]4 1
                        [ppp, uuu, null][55, 9, null]2 2
                        [zzz, null, null][66, null, null]1 1
                """) : root.makeString();

        }



    }

    public static void unitTests (){
        unitTestOnlyRoot();
        unitTestNode2();
        unitTestPutInLeaf();
        unitTestSplitLeaf();
        unitTestPutInRoot();
        //unitTestPutMoreLevels();

    }


}