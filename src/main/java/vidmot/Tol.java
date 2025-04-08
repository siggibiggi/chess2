package vidmot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import classes.Tols;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import static vidmot.ViewSwitcher.switchTo;

public class Tol {
    //region
    @FXML
    TextField input;
    @FXML
    Label outputs;
    @FXML
    ListView menuList;
    @FXML
    Pane contentPane;

    String currentFood = "";
    ArrayList<String> mathlist;
    WeightedQuickUnionUF uf;

    private Tols tols;

    public void initialize(){
        mathlist = new ArrayList<>();

        menuList.getItems().clear();

        mathlist.add("insertionsort");
        mathlist.add("selectionsort");
        mathlist.add("quicksort");
        mathlist.add("mergesort");
        mathlist.add("unionfind");
        mathlist.add("linkedlist");
        mathlist.add("shellsort");
        mathlist.add("test");


        menuList.getItems().addAll(mathlist);

        clockAnimation();

        menuList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentFood = (String) newValue;
            }
        });

        tols = new Tols();

        tols.initializePane(contentPane);
    }
    LinkedListOfStrings list = new LinkedListOfStrings();

    //endregion
    public void action(){
        //region
        if(currentFood.equals("linkedlist")){
            list.addFront(input.getText());
            input.setText("");
            outputs.setText(list.printList());
        } else if (currentFood.equals("unionfind")){
            if(uf == null){
                try {
                    uf = new WeightedQuickUnionUF(Integer.parseInt(input.getText()));
                } catch (NumberFormatException e) {
                }
                input.setText("");
            } else {
                String s = input.getText();
                String[] parts = s.split(" ");
                try {
                    uf.union(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                } catch (NumberFormatException e) {
                }
                outputs.setText(Arrays.toString(uf.parent));
                input.setText("");
            }
        } else if (currentFood.equals("selectionsort")) {
            String s = input.getText();
            String[] parts = s.split(" ");
            int[] temp = new int[parts.length];
            try {
                for(int i = 0; i<parts.length; i++){
                    temp[i] = Integer.parseInt(parts[i]);
                }
            } catch (NumberFormatException e) {
            }
            outputs.setText(selectionsort(temp));
            input.setText("");
        } else if (currentFood.equals("insertionsort")){
            String s = input.getText();
            String[] parts = s.split(" ");
            int[] temp = new int[parts.length];
            try {
                for(int i = 0; i<parts.length; i++){
                    temp[i] = Integer.parseInt(parts[i]);
                }
            } catch (NumberFormatException e) {
            }
            outputs.setText(insertionsort(temp));
            input.setText("");
        } else if (currentFood.equals("mergesort2")){
            String s = input.getText();
            String[] parts = s.split(" ");
            int[] temp = new int[(parts.length)/2];
            int[] temp2 = new int[(parts.length)/2];
            try {
                for(int i = 0; i<(parts.length)/2; i++){
                    temp[i] = Integer.parseInt(parts[i]);
                }
                for(int i = 0; i<parts.length/2; i++){
                    temp2[i] = Integer.parseInt(parts[parts.length/2 + i]);
                }
            } catch (NumberFormatException e) {
            }
            System.out.println(Arrays.toString(temp));
            System.out.println(Arrays.toString(temp2));
            outputs.setText(merge2(temp,temp2));
            input.setText("");
        } else if (currentFood.equals("shellsort")){
            String s = input.getText();
            String[] parts = s.split("_");
            String function = parts[0];
            int variable = Integer.parseInt(parts[1]);
            shellsort(variable,function);
        } else if (currentFood.equals("mergesort")){
            String s = input.getText();
            String[] parts = s.split(" ");
            int[] temp = new int[parts.length];
            try {
                for(int i = 0; i<parts.length; i++){
                    temp[i] = Integer.parseInt(parts[i]);
                }
            } catch (NumberFormatException e) {
            }
            /*
            System.out.println(Arrays.toString(temp));
            temp = merge(temp, 0, 2);
            System.out.println(Arrays.toString(temp));
            temp = merge(temp, 2, 4);
            System.out.println(Arrays.toString(temp));
            temp = merge(temp, 0, 4);
            System.out.println(Arrays.toString(temp));
            */
            System.out.println(Arrays.toString(mergesort(temp)));
            input.setText("");

        }
        //endregion
        else if (currentFood.equals("quicksort")){
            String s = input.getText();
            String[] parts = s.split(" ");
            int[] temp = new int[parts.length];
            try {
                for(int i = 0; i<parts.length; i++){
                    temp[i] = Integer.parseInt(parts[i]);
                }
            } catch (NumberFormatException e) {
            }
            System.out.println(Arrays.toString(quicksort(temp)));
        } else if (currentFood.equals("test")){
            currentFood = "testActivated";
            tols.binaryTree();
        } else if (currentFood.equals("testActivated")){
            tols.binaryTreeAdd(Integer.parseInt(input.getText()));
            tols.dothething();
            input.setText("");
        }
    }
    //region
    public void quit(){
        switchTo(View.Intro);
    }

    public class LinkedListOfStrings {
        private int N;          // size of list
        private Node first;     // first node of list

        // helper Node class
        private static class Node {
            private String item;
            private Node next;
        }

        public LinkedListOfStrings() {
            N = 0;
            first = null;
        }

        // is the list empty?
        public boolean isEmpty() {
            return first == null;
        }

        // number of elements on the stack
        public int size() {
            return N;
        }


        // add an element to the front of the list
        public void addFront(String item) {
            Node oldfirst = first;
            first = new Node();
            first.item = item;
            first.next = oldfirst;
            N++;
        }

        public String printList() {
            Node x = first;
            String s = "";
            for (int i=0; i<N; i++) {
                System.out.print(x.item + "->");
                s =  s + x.item + "->";
                x = x.next;
            }
            System.out.println();
            return s;
        }

        public String delFront() {
            if (isEmpty()) throw new NoSuchElementException("No items in list");
            String item = first.item;      // save item to return
            first = first.next;            // delete first node
            N--;
            return item;                   // return the saved item
        }
    }

    public class WeightedQuickUnionUF {
        private int[] parent;   // parent[i] = parent of i
        private int[] size;     // size[i] = number of elements in subtree rooted at i
        private int count;      // number of components

        /**
         * Initializes an empty union-find data structure with
         * {@code n} elements {@code 0} through {@code n-1}.
         * Initially, each element is in its own set.
         *
         * @param n the number of elements
         * @throws IllegalArgumentException if {@code n < 0}
         */
        public WeightedQuickUnionUF(int n) {
            count = n;
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        /**
         * Returns the number of sets.
         *
         * @return the number of sets (between {@code 1} and {@code n})
         */
        public int count() {
            return count;
        }

        /**
         * Returns the canonical element of the set containing element {@code p}.
         *
         * @param p an element
         * @return the canonical element of the set containing {@code p}
         * @throws IllegalArgumentException unless {@code 0 <= p < n}
         */
        public int find(int p) {
            validate(p);
            while (p != parent[p])
                p = parent[p];
            return p;
        }

        /**
         * Returns true if the two elements are in the same set.
         *
         * @param p one element
         * @param q the other element
         * @return {@code true} if {@code p} and {@code q} are in the same set;
         * {@code false} otherwise
         * @throws IllegalArgumentException unless
         *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
         * @deprecated Replace with two calls to {@link #find(int)}.
         */
        @Deprecated
        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }

        // validate that p is a valid index
        private void validate(int p) {
            int n = parent.length;
            if (p < 0 || p >= n) {
                throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
            }
        }

        /**
         * Merges the set containing element {@code p} with the set
         * containing element {@code q}.
         *
         * @param p one element
         * @param q the other element
         * @throws IllegalArgumentException unless
         *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
         */
        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) return;

            // make smaller root point to larger one
            if (size[rootP] < size[rootQ]) {
                parent[rootP] = rootQ;
                size[rootQ] += size[rootP];
            } else {
                parent[rootQ] = rootP;
                size[rootP] += size[rootQ];
            }
            count--;
        }
    }

    public String insertionsort(int[] array){
        System.out.println(Arrays.toString(array));
        for(int i = 1; i<array.length; i++){
            int temp = array[i];
            for(int j = i-1; j>=0; j--){
                if(temp < array[j]){
                    array[j+1] = array[j];
                    array[j] = temp;
                }
                System.out.println(Arrays.toString(array));
            }
        }
        return "";
    }

    public String selectionsort(int[] array){
        int smallest = 0;
        for(int i = 0; i<array.length; i++){
            for(int j = i; j<array.length; j++){
                if (j == i) smallest = j;
                if (array[smallest] > array[j]) smallest = j;
            }
            int temp = array[smallest];
            array[smallest] = array[i];
            array[i] = temp;
            System.out.println(Arrays.toString(array));
        }
        return Arrays.toString(array);
    }

    public int findSmallest(int[] array){
        int smallest = 0;
        for(int i = 0; i<array.length; i++){
            if(i == 0 || array[i] < smallest){
                smallest = array[i];
            }
        }
        return smallest;
    }

    public String merge2(int[] a, int[] b){
        int[] combined = new int[a.length * 2];
        int k = 0;
        int z = 0;
        for(int i=0; i<a.length*2; i++){
            System.out.println(k);
            System.out.println(z);
            if (k == a.length) combined[i] = b[z++];
            else if(z == a.length) combined[i] = a[k++];
            else if(a[k]<b[z]) combined[i] = a[k++];
            else if(a[k]>b[z]) combined[i] = b[z++];
            else{
                combined[i++] = b[z++];
                combined[i] = a[k++];
            }
            System.out.println(Arrays.toString(combined));
        }
        return "";
    }

    public int[] merge3(int[] array, int lo, int hi){
        int[] aux = new int[hi-lo];
        int mid = hi - (hi-lo)/2;
        int temp = mid;
        int length = hi-lo;
        for(int i = 0; i<(length);){
            System.out.println(lo);
            System.out.println(mid);
            if (lo == temp) aux[i++] = array[mid++];
            else if (mid == hi) aux[i] = array[lo++];
            else if(array[lo] > array[mid]) aux[i++] = array[mid++];
            else if (array[lo] < array[mid]) aux[i++] = array[lo++];
            else {
                aux[i++] = array[mid++];
                aux[i++] = array[lo++];
            }
        }
        for(int i = aux.length-1; i!=-1; i--){
            array[hi-- -1] = aux[i];
        }

        return array;
    }

    public int[] merge(int[] array, int lo, int hi){
        int[] aux = new int[hi-lo];
        int a = lo;
        int b = lo + (hi-lo)/2;
        for(int i = 0; i<aux.length;){
            if(a == lo + (hi-lo)/2) aux[i++] = array[b++];
            else if (b == hi) aux[i++] = array[a++];
            else if(array[a] > array[b]) aux[i++] = array[b++];
            else if (array[b] > array[a]) aux[i++] = array[a++];
            else if (array[a] == array[b]){
                aux[i++] = array[b++];
                aux[i++] = array[a++];
            }
        }
        for(int i = 0; i<aux.length;i++){
            array[lo++] = aux[i];
        }

        return array;
    }

    public int[] mergesort(int[] array){
        int length = array.length;
        int count = 0;
        while(length != 1){
            length = length/2;
            count++;
        }
        for(int i = 0; i<count;i++){
            for(int j = 0; j<array.length/Math.pow(2,i+1);j++){
                System.out.println(Arrays.toString(array));
                array = merge(array,j*(int)Math.pow(2,i+1),(j+1)*(int)Math.pow(2,i+1));
            }
        }
        return array;
    }

    public String shellsort(int array, String sequence){
        Expression expression = new ExpressionBuilder(sequence).variable("x").build();

        for(int i=0; i<array+1; i++){
            expression.setVariable("x",i);
            System.out.println(expression.evaluate());
        }

        return "";
    }

    //endregion

    public int[] quicksort(int[] array){
        quick(array,0,array.length-1);

        return array;
    }


    public void quick(int[] array, int lo, int hi){
        if (lo >= hi) return;
        int temp;
        int i = lo+1;
        int j = hi;
        while(true){
            while(i <= hi && array[i] < array[lo]){
                i++;
                System.out.print(i + "i ");
            }
            System.out.println();
            while(j > lo && array[j] > array[lo]){
                j--;
                System.out.print(j + "j ");
            }
            System.out.println();
            if(i >= j){
                break;
            }
            System.out.println("i = " + i + " j = " + j);
            temp = array[i];
            array[i] = array[j];
            array[j] = temp;

            outputs.setText(i + "");
        }
        temp = array[j];
        array[j] = array[lo];
        array[lo] = temp;




        quick(array,lo,j-1);
        quick(array,j+1,hi);
    }



    private void clockAnimation() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), e -> theClock())
        );

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }
    private void theClock(){

    }
}