package classes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tols {
    //contentPane
    Pane contentPane;
    Label[] labelArray = new Label[31];
    BinarySearchTree treeTest = new BinarySearchTree();
    public void initializePane(Pane cont){
        contentPane = cont;

        //Node hello = new Node(2);
        //Node hello2 = new Node(3);

        //hello.left = hello2;

        //System.out.println(hello.left.value);
    }

    public void drawBinaryTree(BinarySearchTree tree) {
        if (tree.root == null) {
            return;
        }

        // Assumption: labelArray is already defined as Label[31].
        int countUp = 0;

        // Set the root value at labelArray[0]
        labelArray[countUp++].setText(tree.root.value + "");

        // maxBits = 5 generates 2^1 + 2^2 + 2^3 + 2^4 + 2^5 = 2+4+8+16 = 30 nodes,
        // which plus the root makes 31 entries total.
        int maxBits = 5;

        // For each level (bit-string length) from 1 to maxBits.
        for (int bits = 1; bits <= maxBits; bits++) {
            int limit = (int) Math.pow(2, bits);  // There are 2^bits bit patterns for this depth
            for (int i = 0; i < limit; i++) {
                // Create a bit pattern with exactly 'bits' characters (leading zeros added)
                String bitPattern = String.format("%" + bits + "s",
                        Integer.toBinaryString(i)).replace(' ', '0');

                // Start at the root and traverse according to the bit pattern.
                // Remember: '0' means go left, '1' means go right.
                Node current = tree.root;
                for (char dir : bitPattern.toCharArray()) {
                    if (current == null) {
                        break;
                    }
                    if (dir == '0') {
                        current = current.left;
                    } else if (dir == '1') {
                        current = current.right;
                    }
                }

                // Only update if there's room in the labelArray.
                if (countUp >= labelArray.length) {
                    break;
                }

                // If we reached a valid node, display its value; otherwise, leave blank.
                if (current != null) {
                    labelArray[countUp].setText(current.value + "");
                } else {
                    labelArray[countUp].setText("");
                }
                countUp++;
            }
        }
    }

    public void binaryTreeAdd(int value){
        treeTest.insert(value);
    }


    public void binaryTree(){
        VBox vbox = new VBox();
        int arrayc = 0;

        for(int i = 0; i<5;i++){
            HBox ok = new HBox();
            HBox lines = new HBox();
            for(int j = 0; j<Math.pow(2,i);j++){
                Label okay = new Label();
                labelArray[arrayc] = okay;
                Region space = new Region();
                HBox.setHgrow(space, Priority.ALWAYS);
                arrayc++;
                ok.getChildren().add(space);
                ok.getChildren().add(okay);

                if(i != 4){
                    Region space2 = new Region();
                    HBox.setHgrow(space2, Priority.ALWAYS);
                    Label V = new Label("/\\");
                    Pane vPane = new Pane();
                    vPane.getChildren().add(V);
                    vPane.setScaleY(1.1);
                    vPane.setScaleX(1.1);
                    lines.getChildren().add(space2);
                    lines.getChildren().add(vPane);
                    if(i == 0){
                        vPane.setScaleX(10);
                    }
                    if(i == 1){
                        vPane.setScaleX(5);
                    }
                    if(i == 2){
                        vPane.setScaleX(3);
                    }
                }
            }
            Region space = new Region();
            HBox.setHgrow(space, Priority.ALWAYS);
            ok.getChildren().add(space);
            vbox.getChildren().add(ok);

            if(i != 4){
                Region space2 = new Region();
                HBox.setHgrow(space2, Priority.ALWAYS);
                lines.getChildren().add(space2);
                vbox.getChildren().add(lines);
            }
        }
        Rectangle space = new Rectangle(300,2);
        space.setFill(Color.BLUE);
        vbox.getChildren().add(space);
        contentPane.getChildren().add(vbox);

        drawBinaryTree(treeTest);
    }

    class Node {
        int value, height;
        Node left, right;

        Node(int value) {
            this.value = value;
            this.left = this.right = null;
            this.height = 1; // New node starts with height 1
        }
    }

    class BinarySearchTree {
        Node root;

        void insert(int value) {
            root = insertRec(root, value);
        }

        private Node insertRec(Node node, int value) {
            // Standard BST insertion
            if (node == null) {
                return new Node(value);
            }
            if (value < node.value) {
                node.left = insertRec(node.left, value);
            } else if (value > node.value) {
                node.right = insertRec(node.right, value);
            } else {
                // Duplicate value; do not insert
                return node;
            }

            // Update height
            node.height = 1 + Math.max(height(node.left), height(node.right));

            // Get balance factor to check if node is unbalanced
            int balance = getBalance(node);

            // Left Left Case
            if (balance > 1 && value < node.left.value) {
                return rightRotate(node);
            }

            // Right Right Case
            if (balance < -1 && value > node.right.value) {
                return leftRotate(node);
            }

            // Left Right Case
            if (balance > 1 && value > node.left.value) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            // Right Left Case
            if (balance < -1 && value < node.right.value) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            // Return the (possibly updated) node pointer
            return node;
        }

        // Returns the height of a node, handling null as 0.
        private int height(Node node) {
            return (node == null) ? 0 : node.height;
        }

        // Returns the balance factor of the node.
        private int getBalance(Node node) {
            return (node == null) ? 0 : height(node.left) - height(node.right);
        }

        // Right rotation around y
        private Node rightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;

            // Perform rotation
            x.right = y;
            y.left = T2;

            // Update heights
            y.height = 1 + Math.max(height(y.left), height(y.right));
            x.height = 1 + Math.max(height(x.left), height(x.right));

            // Return new root
            return x;
        }

        // Left rotation around x
        private Node leftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;

            // Perform rotation
            y.left = x;
            x.right = T2;

            // Update heights
            x.height = 1 + Math.max(height(x.left), height(x.right));
            y.height = 1 + Math.max(height(y.left), height(y.right));

            // Return new root
            return y;
        }
    }


    public void dothething(){
        drawBinaryTree(treeTest);
    }
}
