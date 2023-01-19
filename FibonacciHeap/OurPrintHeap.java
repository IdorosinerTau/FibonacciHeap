package FibonacciHeap;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class OurPrintHeap {

    public static void main(String[] args) {
    	
    	FibonacciHeap fib = new FibonacciHeap();
        int[] arr = {Integer.MIN_VALUE, 0};
        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();
        
        for (int i = 0; i < arr.length; i++) {
        	nodes.add(fib.insert(arr[i]));
        }
        
        System.out.println(nodes.get(0).getKey());
        
        printFibHeap(fib);
        fib.delete(nodes.get(1));
        printFibHeap(fib);
        
        System.out.println(nodes.get(1));
        System.out.println(fib.getMin());
        System.out.println(fib.getFirst());
        
        /*Map<Integer, FibonacciHeap.HeapNode> nodes = testInsertion(heap, Integer.MIN_VALUE, 0);
        testDeletion(heap, nodes.get(0));
        assertSame(nodes.get(Integer.MIN_VALUE), heap.findMin());
        assertSame(nodes.get(Integer.MIN_VALUE), heap.getFirst());*/
    }

    /**
     * print methods - delete before submitting
     */
    public static void printFibHeap(FibonacciHeap fib) {
        System.out.println("Heap's details: ");
        System.out.println("• Is empty? - " + fib.isEmpty());
        if (fib.isEmpty())
            System.out.println("• Min,first and last nodes doesn't exists because the heap is empty");
        else {
            System.out.println("• Min node - " + fib.getMin().getKey());
            System.out.println("• First node - " + fib.getFirst().getKey());
            System.out.println("• Last node - " + fib.getFirst().getPrev().getKey());
        }
        System.out.println("• Heap's size - " + fib.size());
        System.out.println("• Number of trees in the heap - " + fib.getNumOfTrees());
        System.out.println("• Number of marked nodes - " + (fib.getSize() - fib.getNonMarked()));
        System.out.println("• Number of non-marked nodes - " + fib.getNonMarked());
        System.out.println("• Potential function value - " + fib.potential());
        System.out.println("• Counter repeats array - " + Arrays.toString(fib.countersRep()));

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("The heap itself: ");
        FibonacciHeap.HeapNode x = fib.getFirst();
        printHeap(fib,x);
        System.out.println("");
        System.out.println("The nodes' details: ");
        for (int i = 0; i < fib.getNumOfTrees(); i++){
            System.out.println("---------");
            System.out.println("Tree number " + i + ":");
            printTreeDetails(x);
            x = x.getNext();
        }
    }

    public static void printNode(FibonacciHeap.HeapNode node) {
        System.out.print("• Node's Key = " + node.key +", Rank = " +
                node.getRank() + ", is marked? - " + node.getMark() + ", next - " + node.getNext().getKey()
                + ", prev - " + node.getPrev().getKey());
        if(node.getParent() != null)
        	System.out.print(", parent - " + node.getParent().getKey());
        else
        	System.out.print(", parent - null");
        System.out.println("");
    }

    public static void printTreeDetails(FibonacciHeap.HeapNode root){
        System.out.println("The tree's level - 0:");
        printNode(root);
        FibonacciHeap.HeapNode x = root.getChild();
        int depth = 1;
        // while child isn't null
        while (x != null){
            System.out.println("The tree's level - " + depth + ":");
            FibonacciHeap.HeapNode start = x; // saving the child to move to the next level
            do{
                printNode(x);
                x = x.getNext();
            } while(x != start);
            x = start.getChild();
            depth++;
        }
    }
    public static void printHeap(FibonacciHeap fib, FibonacciHeap.HeapNode root){
        printHeapRec(fib, root, root,0);
    }
    private static void printHeapRec(FibonacciHeap fib,FibonacciHeap.HeapNode startNode, FibonacciHeap.HeapNode currentNode, int level){
        if (currentNode == null)
            return;
        for (int i = 0; i < level-1; i++)
            System.out.print("| ");
        if (level !=0)
            System.out.print("|_");
        else
            System.out.println("");
        System.out.print(currentNode.getKey());
        if (currentNode.getMark())
            System.out.println("*");
        else
            System.out.println("");
        printHeapRec(fib,currentNode.getChild(),currentNode.getChild(),level+1);
        if (currentNode.getNext() != startNode)
            printHeapRec(fib,startNode,currentNode.getNext(),level);
    }

}


