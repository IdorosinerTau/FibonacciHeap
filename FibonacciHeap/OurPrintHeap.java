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
        int[] arr = {9, 10, 11, 12, 13, 14, 15, 16};
        ArrayList<FibonacciHeap.HeapNode> nodes = new ArrayList<>();
        
        for (int i = 0; i < arr.length; i++) {
        	nodes.add(fib.insert(arr[i]));
        }
        
        FibonacciHeap fib2 = new FibonacciHeap();
        int[] arr1 = {1,2,3,4,5,6,7,8};
        
        for (int i = 0; i < arr1.length; i++) {
        	nodes.add(fib2.insert(arr1[i]));
        }
        
        FibonacciHeap fib3 = new FibonacciHeap();
        int[] arr2 = {17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32};
        
        for (int i = 0; i < arr2.length; i++) {
        	nodes.add(fib3.insert(arr2[i]));
        }
        
        for (FibonacciHeap h : Arrays.asList(fib, fib2, fib3)) {
            h.insert(Integer.MIN_VALUE);
            h.deleteMin();
            printFibHeap(h);
        }
        
        fib.meld(fib2);
        printFibHeap(fib);
        
        fib.meld(fib3);
        printFibHeap(fib);

        fib.deleteMin();
        printFibHeap(fib);
        
        /*Map<Integer, FibonacciHeap.HeapNode> nodes = testInsertion(heap, IntStream.rangeClosed(9, 16)::iterator);

        FibonacciHeap heap2 = new FibonacciHeap();
        nodes.putAll(testInsertion(heap2, IntStream.rangeClosed(1, 8)::iterator));

        FibonacciHeap heap3 = new FibonacciHeap();
        nodes.putAll(testInsertion(heap3, IntStream.rangeClosed(17, 32)::iterator));

        for (FibonacciHeap h : Arrays.asList(heap, heap2, heap3)) {
            testInsertion(h, Integer.MIN_VALUE);
            h.deleteMin();
            assertValidHeap(h);
        }

        heap.meld(heap2);
        assertValidHeap(heap);

        heap.meld(heap3);
        assertValidHeap(heap);

        heap.deleteMin();
        assertValidHeap(heap);
        assertSame(nodes.get(2), heap.findMin());

        FibonacciHeap.HeapNode node = heap.getFirst();
        assertSame(nodes.get(2), heap.getFirst());
        node = node.getNext();
        assertSame(nodes.get(3), node);
        node = node.getNext();
        assertSame(nodes.get(5), node);
        node = node.getNext();
        assertSame(nodes.get(9), node);
        node = node.getNext();
        assertSame(nodes.get(17), node);*/
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
        System.out.println("• Number of marked nodes - " + (fib.getSize() - fib.getHeapNonMarked()));
        System.out.println("• Number of non-marked nodes - " + fib.getHeapNonMarked());
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


