package classes;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.HashMap;
import java.util.Map;


public class Pieces {
    Color DARK_WOOD_BROWN = Color.web("#724a2f");

    //Color MY_BEIGE = Color.web("#e4d5b7");
    Color MY_BEIGE = Color.web("#EAD5BF");
    Map<StackPane, chessPiece> board = new HashMap<>();

    static class chessPiece {
        String pieceType;
        int color;
        Label label;
        Pane pane;
        chessPiece(String pieceType, int color, Label label, Pane pane) {
            this.pieceType = pieceType;
            this.color = color;
            this.label = label;
            this.pane = pane;
        }
    }

    public void initializePieces(StackPane[][] fylki){


        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                Label label = new Label("");
                chessPiece nothing = new chessPiece("",2,label,createNothing(1));
                board.put(fylki[i][j],nothing);
            }
        }

        for(int i = 0; i < 8; i++){
            Label pawnlabel = new Label("whitePawn");
            pawnlabel.getStyleClass().add("aPiece");
            chessPiece pawn = new chessPiece("pawn",0, pawnlabel, createPawn(0));
            board.put(fylki[i][1],pawn);
            pawnlabel = new Label("blackPawn");
            pawnlabel.getStyleClass().add("aPiece");
            pawn = new chessPiece("pawn",1, pawnlabel, createPawn(1));
            board.put(fylki[i][6],pawn);
        }



        int therow = 0;
        int thecolumn = 0;
        int plusminus = 1;

        /*
        Label pawnlabel = new Label("whitePawn");
        pawnlabel.getStyleClass().add("aPiece");
        chessPiece pawn = new chessPiece("pawn",0, pawnlabel);
        board.put(fylki[3][3],pawn);

        Label pawnls = new Label("blackPawn");
        pawnls.getStyleClass().add("aPiece");
        chessPiece pawns = new chessPiece("pawn",1, pawnls);
        board.put(fylki[5][5],pawns);

        Label rookl = new Label("whiteQueen");
        rookl.getStyleClass().add("aPiece");
        chessPiece rook = new chessPiece("queen",0, rookl);
        board.put(fylki[0][0],rook);

        */




        for (int i = 0; i < 4; i ++){
            Label rlabel, klabel, blabel, kilabel, qlabel;

            if (i % 2 == 0) {
                rlabel = new Label("whiteRook");
                klabel = new Label("whiteKnight");
                blabel = new Label("whiteBishop");
                kilabel = new Label("whiteKing");
                qlabel = new Label("whiteQueen");
            } else {
                rlabel = new Label("blackRook");
                klabel = new Label("blackKnight");
                blabel = new Label("blackBishop");
                kilabel = new Label("blackKing");
                qlabel = new Label("blackQueen");
            }

            rlabel.getStyleClass().add("aPiece");
            klabel.getStyleClass().add("aPiece");
            blabel.getStyleClass().add("aPiece");
            kilabel.getStyleClass().add("aPiece");
            qlabel.getStyleClass().add("aPiece");


            chessPiece rook = new chessPiece("rook",i%2, rlabel, createRook(i%2));
            chessPiece knight = new chessPiece("knight",i%2,klabel, createKnight(i%2));
            chessPiece bishop = new chessPiece("bishop",i%2,blabel, createBishop(i%2));
            board.put(fylki[thecolumn][therow],rook);
            board.put(fylki[thecolumn + plusminus][therow],knight);
            board.put(fylki[thecolumn + plusminus * 2][therow],bishop);
            chessPiece king = new chessPiece("king",i%2, kilabel, createKing(i%2));
            chessPiece queen = new chessPiece("queen",i%2, qlabel, createQueen((i%2)));
            board.put(fylki[4][therow],king);
            board.put(fylki[3][therow],queen);

            therow = Math.abs(4 - therow + 4) - 1;

            if (i == 1) {
                thecolumn = 7;
                plusminus = -1;
            }
        }

    }

    public String getPieceType(StackPane square){
        return board.get(square).pieceType;
    }
    public int getPieceColor(StackPane square){
        return board.get(square).color;
    }

    public Label getPieceLabel(StackPane square){
        return board.get(square).label;
    }

    public Pane getPiecePane(StackPane square){
        return board.get(square).pane;
    }

    public void swapPlaces(StackPane pane1, StackPane pane2) {
        chessPiece piece1 = board.get(pane1);
        chessPiece piece2 = board.get(pane2);

        board.put(pane1, piece2);
        board.put(pane2, piece1);
    }


    public Pane createPawn(int color) {
        Color thecolor;

        if(color == 1){
            thecolor = DARK_WOOD_BROWN;
        } else {
            thecolor = MY_BEIGE;
        }

        Circle circle = new Circle();
        circle.setRadius(12.0f);
        circle.setFill(thecolor);

        Polygon equidestrialTriangle = new Polygon();
        equidestrialTriangle.getPoints().addAll(
                0.0, -17.0,
                -17.0, 20.0,
                17.0, 20.0
        );
        equidestrialTriangle.setFill(thecolor);

        Group root = new Group(circle, equidestrialTriangle);

        circle.setTranslateY(30);
        circle.setTranslateX(42);
        equidestrialTriangle.setTranslateY(50);
        equidestrialTriangle.setTranslateX(42);

        root.setTranslateY(1);

        Pane wrapperPane = new Pane(root);

        return wrapperPane;
    }

    public Pane createRook(int color) {
        Color thecolor;

        if(color == 1){
            thecolor = DARK_WOOD_BROWN;
        } else {
            thecolor = MY_BEIGE;
        }

        Rectangle slit = new Rectangle(7, 10);

        Rectangle top = new Rectangle(40, 20);
        Rectangle middle = new Rectangle(15, 30, thecolor);
        Rectangle base = new Rectangle(35, 8, thecolor);

        top.setTranslateX(42 - top.getWidth()/2);
        middle.setTranslateX(42 - middle.getWidth()/2);
        base.setTranslateX(42 - base.getWidth()/2);

        top.setTranslateY(20);
        middle.setTranslateY(35);
        base.setTranslateY(63);

        slit.setTranslateX(42 - slit.getWidth()/2 - 8);
        slit.setTranslateY(16);

        Shape newTop = Shape.subtract(top, slit);


        slit.setTranslateX(42 - slit.getWidth()/2 + 8);

        Shape newNewTop = Shape.subtract(newTop, slit);

        newNewTop.setFill(thecolor);

        Group root = new Group(middle, base, newNewTop);


        Pane wrapperPane = new Pane(root);

        return wrapperPane;
    }

    public Pane createQueen(int color) {
        Color thecolor;
        Color thecolor2;

        if(color == 1){
            thecolor = DARK_WOOD_BROWN;
            thecolor2 = MY_BEIGE;
        } else {
            thecolor = MY_BEIGE;
            thecolor2 = DARK_WOOD_BROWN;
        }
        Polygon queenHead = new Polygon();
        queenHead.getPoints().addAll(
                0.0, 40.0,
                -20.0, 10.0,
                20.0, 10.0
        );
        queenHead.setFill(thecolor);

        Polygon subTract = new Polygon();
        subTract.getPoints().addAll(
                1.0, 0.0,
                1.0, -10.0,
                -8.0, -10.0
        );
        subTract.setTranslateX(37);
        subTract.setTranslateY(30);


        Rectangle middle = new Rectangle(10, 20, thecolor);
        Rectangle base = new Rectangle(35, 7, thecolor);
        Circle crown1 = new Circle(6, thecolor2);
        Circle crown2 = new Circle(6, thecolor2);
        Circle crown3 = new Circle(6, thecolor2);

        middle.setTranslateX(42 - middle.getWidth()/2);
        base.setTranslateX(42 - base.getWidth()/2);
        crown1.setTranslateX(24);
        crown2.setTranslateX(42);
        crown3.setTranslateX(60);
        queenHead.setTranslateX(42);

        middle.setTranslateY(45);
        base.setTranslateY(65);
        crown1.setTranslateY(20);
        crown2.setTranslateY(20);
        crown3.setTranslateY(20);
        queenHead.setTranslateY(10);

        Shape newHead = Shape.subtract(queenHead,subTract);

        Polygon subTract2becausewhynot = new Polygon();

        subTract2becausewhynot.getPoints().addAll(
                -1.0, 0.0,
                -1.0, -10.0,
                8.0, -10.0
        );
        subTract2becausewhynot.setTranslateX(47);
        subTract2becausewhynot.setTranslateY(30);

        Shape newNewHead = Shape.subtract(newHead,subTract2becausewhynot);

        newNewHead.setFill(thecolor);

        newNewHead.setTranslateY(5);

        Group root = new Group(middle, base, newNewHead, crown1, crown2, crown3);

        root.setTranslateY(-1);

        Pane wrapperPane = new Pane(root);

        return wrapperPane;
    }

    public Pane createKing(int color) {
        Color thecolor;

        if(color == 1){
            thecolor = DARK_WOOD_BROWN;
        } else {
            thecolor = MY_BEIGE;
        }

        Rectangle top = new Rectangle(32, 20, thecolor);
        Rectangle middle = new Rectangle(15, 30, thecolor);
        Rectangle base = new Rectangle(35, 7, thecolor);
        Rectangle crownHor = new Rectangle(15, 5, thecolor);
        Rectangle crownVer = new Rectangle(5, 15, thecolor);

        Group root = new Group(top, middle, base, crownVer, crownHor);

        top.setTranslateX(42 - top.getWidth()/2);
        middle.setTranslateX(42 - middle.getWidth()/2);
        base.setTranslateX(42 - base.getWidth()/2);
        crownVer.setTranslateX(42 - crownVer.getWidth()/2);
        crownHor.setTranslateX(42 - crownHor.getWidth()/2);

        top.setTranslateY(20);
        middle.setTranslateY(38);
        base.setTranslateY(65);
        crownVer.setTranslateY(14 - crownVer.getHeight()/2);
        crownHor.setTranslateY(14 - crownHor.getHeight()/2);

        root.setTranslateY(-1);

        Pane wrapperPane = new Pane(root);

        return wrapperPane;
    }

    public Pane createBishop(int color) {
        Color thecolor;

        if(color == 1){
            thecolor = DARK_WOOD_BROWN;
        } else {
            thecolor = MY_BEIGE;
        }

        Circle head = new Circle();
        head.setRadius(13.0f);

        Rectangle slit = new Rectangle(12, 4);
        slit.setTranslateX(43);
        slit.setTranslateY(30);

        Circle circleHat = new Circle(5, thecolor);

        Polygon equidestrialTriangle = new Polygon();
        equidestrialTriangle.getPoints().addAll(
                0.0, -25.0,
                -16.0, 23.0,
                16.0, 23.0
        );
        circleHat.setCenterX(42);
        circleHat.setCenterY(16);
        head.setTranslateY(33);
        head.setTranslateX(42);
        equidestrialTriangle.setTranslateY(50);
        equidestrialTriangle.setTranslateX(42);

        Shape bishopBase = Shape.union(equidestrialTriangle, head);
        Shape bishopBaseWithHat = Shape.union(bishopBase, circleHat);

        Shape bishopWithaSlit = Shape.subtract(bishopBaseWithHat, slit);



        bishopWithaSlit.setFill(thecolor);


        Group root = new Group(bishopWithaSlit);

        circleHat.setCenterX(42);
        circleHat.setCenterY(15);
        equidestrialTriangle.setTranslateY(50);
        equidestrialTriangle.setTranslateX(42);

        root.setTranslateY(-2);

        Pane wrapperPane = new Pane(root);

        return wrapperPane;
    }

    public Pane createKnight(int color) {
        Color thecolor;

        if(color == 1){
            thecolor = DARK_WOOD_BROWN;
        } else {
            thecolor = MY_BEIGE;
        }
        Polygon knightHead = new Polygon();
        knightHead.getPoints().addAll(
                0.0, -31.0,
                0.0, 0.0,
                23.0, 5.0,
                23.0, -10.0
        );
        knightHead.setFill(thecolor);

        Polygon knightEar = new Polygon();
        knightEar.getPoints().addAll(
                0.0, -7.0,
                0.0, 9.0,
                -5.0, 0.0
        );
        knightEar.setFill(thecolor);
        knightEar.setTranslateX(43);
        knightEar.setTranslateY(20);

        Circle knightEye = new Circle(3, Color.BLACK);


        Circle circle = new Circle(25);
        Circle cSubtract = new Circle(13);
        Rectangle rectangle = new Rectangle(50, 50);
        Rectangle rSubtract = new Rectangle(25,20);

        cSubtract.setTranslateY(-5);

        rSubtract.setTranslateX(-10);
        rSubtract.setTranslateY(13);

        Shape cShape = Shape.subtract(circle,cSubtract);
        Shape rShape = Shape.subtract(rectangle,rSubtract);

        cShape.setTranslateX(circle.getRadius());
        cShape.setTranslateY(circle.getRadius());

        Shape curve = Shape.subtract(cShape,rShape);

        curve.setFill(thecolor);

        curve.setTranslateY(28);
        curve.setTranslateX(34);

        curve.setScaleX(1.5);
        curve.setScaleY(2);


        Rectangle middle = new Rectangle(8, 30, thecolor);
        Rectangle base = new Rectangle(28, 7, thecolor);
        Ellipse knightNose = new Ellipse(2, 5, 2, 5);

        knightNose.setFill(thecolor);

        knightNose.setTranslateX(54);
        knightNose.setTranslateY(30);

        Group root = new Group(base, knightHead, knightEar, curve, knightEye, knightNose);

        middle.setTranslateX(42 - middle.getWidth()/2);
        base.setTranslateX(42 - base.getWidth()/2);
        knightHead.setTranslateX(35);

        middle.setTranslateY(40);
        base.setTranslateY(65);
        knightHead.setTranslateY(45);

        knightEye.setTranslateX(42);
        knightEye.setTranslateY(31);

        root.setTranslateY(-1);

        Pane wrapperPane = new Pane(root);

        return wrapperPane;
    }

    public Pane createNothing(int color) {

        Pane wrapperPane = new Pane();

        return wrapperPane;
    }
}