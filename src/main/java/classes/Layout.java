package classes;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Layout {
    //region
    ArrayList<String> mathlist;
    //called mathlist because I copied from a previous project of mine. This is what the menulist reads from
    String currentFood = "";
    //called currentFood because I copied from a previous project of mine. Current selected item in menu
    boolean whichrotation = false;
    //which rotation the board currently is. Probably a way around this, but it's so I can keep track of it for the clocks and killed pieces
    StackPane[][] reitaFylki = new StackPane[8][8];
    //the squares
    GridPane disgustingGridPane;
    //so I can initialize everything again on reset. Literally the only reason I had to pass it
    boolean flipboard = false;
    //if board flips after every move
    private Moves moves;
    boolean lockGame = false;
    //locks the game, like after checkmate
    boolean runTimer = false;
    //if the timer runs or not
    int whitePieceCount = 0;
    //for killed pieces, to move them the right amount to the side and they dont overlap
    int blackPieceCount = 0;
    StackPane currentStackpane;
    //which stackpane was last clicked on
    StackPane placeholder = new StackPane();
    //just to avoid a null thing I guess
    private Pieces pieces;
    boolean aPieceIsSelected = false;
    //to differentiate between selecting a piece and moving it
    Circle[] moveIndicators = new Circle[27];
    //max amount of moves is a queen on an empty board is 27. Container for the move indicator circles
    int oldColumnValue = 4;
    //just has to be whatever number
    int oldRowNumber = 4;
    int whoseTurn = 0;
    //white is 0
    boolean haveTurns = true;
    //if swap whoseTurn
    boolean allowTimer = true;
    //I dont even remember
    Label feedback1;
    Label feedback2;
    Pane topPieceContainer;
    Pane bottomPieceContainer;
    ListView menuList;
    Label oneLabel;
    Label twoLabel;
    HBox oneTimerBox;
    HBox twoTimerBox;
    Pane oneTimeContainer;
    Pane twoTimeContainer;
    double whiteTimer = 300 * 8;
    double blackTimer = 300 * 8;
    //multiplying or dividing the things by 8 seems to bring me close to a second so yeah
    boolean showTimers = false;
    //show the buttons I think, for increasing or decreasing time on clock
    boolean gameHasStarted = false;
    //something for the ai stuff
    boolean vsAI = false;
    //opens a bunch of if statements for AI
    boolean checkMateChecking = false;
    boolean checkmateReturnBoolean = false;
    //checkmates were a whole thing and they used the update thingy to check for every piece, so this is to avoid recursion
    int AIcolor = 1;
    boolean AIvsAI = false;
    double AImoveInterval = 0.0;
    //for making the AI not instantly finish the game
    boolean justMoved = false;
    //also to avoid recursion or something
    int amountofMoves = 0;
    //so AI games dont go on forever king vs king
    int nGames = 0;
    //for counting the number of AI games. Was gonna do more of this and calculate what percentage of games ended in checkmate

    //endregion
    public void initializeBoard(GridPane theGrid, Label feed1, Label feed2, Pane top, Pane bottom, ListView mensu, Label oneT, Label twoT, HBox sss, HBox ttt, Pane tim, Pane tina){
        feedback1 = feed1;
        feedback2 = feed2;
        topPieceContainer = top;
        bottomPieceContainer = bottom;
        menuList = mensu;
        oneLabel = oneT;
        twoLabel = twoT;
        oneTimerBox = sss;
        twoTimerBox = ttt;
        oneTimeContainer = tim;
        twoTimeContainer = tina;
        disgustingGridPane = theGrid;

        topPieceContainer.setTranslateY(-335);
        bottomPieceContainer.setTranslateY(335);

        oneTimeContainer.setTranslateY(335);
        twoTimeContainer.setTranslateY(-335);
        //puts the things in the right place, so it doesnt look weird when flipped

        oneTimerBox.setVisible(false);
        twoTimerBox.setVisible(false);

        initializetest();
    }

    public void updatefunc(int columnLetter, int rowNumber){
        justMoved = false;

        feedback2.setText("");

        if(!gameHasStarted){
            if(currentFood.equals("play vs advanced AI?") || currentFood.equals("are you black?") || currentFood.equals("are you not even playing?")){
                currentFood = "";
            }
            mathlist.remove("play vs advanced AI?");
            mathlist.remove("are you black?");
            menuList.getItems().clear();
            menuList.getItems().addAll(mathlist);
        }
        //I don't even understand this part anymore. It made sense at the time I think
        //obviously to remove those options once the game started

        gameHasStarted = true;


        for(int i = 0; i < 27; i++){
            if(moves.availableMoves[i] == null){
                break;
            }
            reitaFylki[moves.availableMoves[i].x][moves.availableMoves[i].y].getChildren().remove(moveIndicators[i]);

            moveIndicators[i].setFill(Color.ORANGE);
        }
        //move indicators are reset. Makes sense to do after clicking any square in any context, so happens every time

        if ((oldColumnValue == columnLetter && oldRowNumber == rowNumber) && aPieceIsSelected){
            currentStackpane = placeholder;
            aPieceIsSelected = false;
            return;
        }
        //if same square is clicked again; deselected


        //System.out.println(pieces.getPieceType(currentStackpane));
        //System.out.println(pieces.getPieceColor(currentStackpane));
        //System.out.println(moves.squareStatus[columnLetter][rowNumber]);

        String selectedPiece = pieces.getPieceType(currentStackpane);
        if ((!selectedPiece.equals("") && !aPieceIsSelected && moves.squareStatus[columnLetter][rowNumber] == whoseTurn) || ((moves.squareStatus[oldColumnValue][oldRowNumber] != 2) && (moves.squareStatus[oldColumnValue][oldRowNumber] == moves.squareStatus[columnLetter][rowNumber]) && aPieceIsSelected)){
            //a piece is not selected
            moves.getMoves(columnLetter, rowNumber, selectedPiece, pieces.getPieceColor(currentStackpane));
            aPieceIsSelected = true;
        } else {
            if(aPieceIsSelected){
                for(int i = 0; i<27; i++){
                    if (moves.availableMoves[i] == null){
                        break;
                    }
                    //breaks the loop once there are no more available moves
                    if(moves.availableMoves[i].x == columnLetter && moves.availableMoves[i].y == rowNumber){
                        //if the current selected square equals an available move

                        //System.out.println("White King Position: " + moves.whiteKingPos.x + ", " + moves.whiteKingPos.y);
                        //System.out.println("Black King Position: " + moves.blackKingPos.x + ", " + moves.blackKingPos.y);

                        if(pieces.getPieceType(reitaFylki[oldColumnValue][oldRowNumber]).equals("king")){
                            //castling check
                            //DO NOT LOOK AT CASTLING CHECK. IT'S HARDCODED AND DISGUSTING. THERE IS NOTHING TO BE GAINED
                            if(moves.castlingMove){
                                if(Math.abs(columnLetter - oldColumnValue) == 2) {
                                    //checks if king is going 2 spaces, which is impossible in other contexts
                                    //whitelong
                                    if (moves.canCastle[0]) {
                                        //System.out.println("hello0");
                                        if(moves.availableMoves[i].x == 2 && moves.availableMoves[i].y == 0){
                                            //System.out.println("hello1");
                                            if(!(isItAttacked(4,0,0) || isItAttacked(3,0,0) || isItAttacked(2,0,0) || isItAttacked(1,0,0))){
                                                //System.out.println("hello2");

                                                reitaFylki[4][0].getChildren().remove(pieces.getPiecePane(reitaFylki[4][0]));
                                                reitaFylki[2][0].getChildren().remove(pieces.getPiecePane(reitaFylki[2][0]));
                                                pieces.swapPlaces(reitaFylki[4][0],reitaFylki[2][0]);
                                                reitaFylki[4][0].getChildren().add(pieces.getPiecePane(reitaFylki[4][0]));
                                                reitaFylki[2][0].getChildren().add(pieces.getPiecePane(reitaFylki[2][0]));


                                                reitaFylki[0][0].getChildren().remove(pieces.getPiecePane(reitaFylki[0][0]));
                                                reitaFylki[3][0].getChildren().remove(pieces.getPiecePane(reitaFylki[3][0]));
                                                pieces.swapPlaces(reitaFylki[0][0], reitaFylki[3][0]);
                                                reitaFylki[0][0].getChildren().add(pieces.getPiecePane(reitaFylki[0][0]));
                                                reitaFylki[3][0].getChildren().add(pieces.getPiecePane(reitaFylki[3][0]));

                                                moves.squareStatus[0][0] = 2;
                                                moves.squareStatus[4][0] = 2;
                                                moves.squareStatus[2][0] = 0;
                                                moves.squareStatus[3][0] = 0;

                                                if(haveTurns){
                                                    whoseTurn = (whoseTurn + 1) % 2;
                                                }


                                                if(flipboard){
                                                    rotatePieces(disgustingGridPane);
                                                }
                                                justMoved = true;

                                                break;
                                            }
                                        }
                                    }

                                    //whiteshort
                                    if (moves.canCastle[1]) {
                                        if(moves.availableMoves[i].x == 6 && moves.availableMoves[i].y == 0){
                                            if(!(isItAttacked(4,0,0) || isItAttacked(5,0,0) || isItAttacked(6,0,0))){

                                                reitaFylki[4][0].getChildren().remove(pieces.getPiecePane(reitaFylki[4][0]));
                                                reitaFylki[6][0].getChildren().remove(pieces.getPiecePane(reitaFylki[6][0]));
                                                pieces.swapPlaces(reitaFylki[4][0],reitaFylki[6][0]);
                                                reitaFylki[4][0].getChildren().add(pieces.getPiecePane(reitaFylki[4][0]));
                                                reitaFylki[6][0].getChildren().add(pieces.getPiecePane(reitaFylki[6][0]));


                                                reitaFylki[7][0].getChildren().remove(pieces.getPiecePane(reitaFylki[7][0]));
                                                reitaFylki[5][0].getChildren().remove(pieces.getPiecePane(reitaFylki[5][0]));
                                                pieces.swapPlaces(reitaFylki[7][0], reitaFylki[5][0]);
                                                reitaFylki[7][0].getChildren().add(pieces.getPiecePane(reitaFylki[7][0]));
                                                reitaFylki[5][0].getChildren().add(pieces.getPiecePane(reitaFylki[5][0]));

                                                moves.squareStatus[7][0] = 2;
                                                moves.squareStatus[4][0] = 2;
                                                moves.squareStatus[5][0] = 0;
                                                moves.squareStatus[6][0] = 0;

                                                if(haveTurns){
                                                    whoseTurn = (whoseTurn + 1) % 2;
                                                }

                                                if(flipboard){
                                                    rotatePieces(disgustingGridPane);
                                                }
                                                justMoved = true;

                                                break;
                                            }
                                        }
                                    }

                                    //blacklong
                                    if (moves.canCastle[2]) {
                                        if(moves.availableMoves[i].x == 2 && moves.availableMoves[i].y == 7){
                                            if(!(isItAttacked(4,7,1) || isItAttacked(3,7,1) || isItAttacked(2,7,1) || isItAttacked(1,7,1))){

                                                reitaFylki[4][7].getChildren().remove(pieces.getPiecePane(reitaFylki[4][7]));
                                                reitaFylki[2][7].getChildren().remove(pieces.getPiecePane(reitaFylki[2][7]));
                                                pieces.swapPlaces(reitaFylki[4][7],reitaFylki[2][7]);
                                                reitaFylki[4][7].getChildren().add(pieces.getPiecePane(reitaFylki[4][7]));
                                                reitaFylki[2][7].getChildren().add(pieces.getPiecePane(reitaFylki[2][7]));


                                                reitaFylki[0][7].getChildren().remove(pieces.getPiecePane(reitaFylki[0][7]));
                                                reitaFylki[3][7].getChildren().remove(pieces.getPiecePane(reitaFylki[3][7]));
                                                pieces.swapPlaces(reitaFylki[0][7], reitaFylki[3][7]);
                                                reitaFylki[0][7].getChildren().add(pieces.getPiecePane(reitaFylki[0][7]));
                                                reitaFylki[3][7].getChildren().add(pieces.getPiecePane(reitaFylki[3][7]));

                                                moves.squareStatus[0][7] = 2;
                                                moves.squareStatus[4][7] = 2;
                                                moves.squareStatus[2][7] = 1;
                                                moves.squareStatus[3][7] = 1;

                                                if(haveTurns){
                                                    whoseTurn = (whoseTurn + 1) % 2;
                                                }

                                                if(flipboard){
                                                    rotatePieces(disgustingGridPane);
                                                }
                                                justMoved = true;

                                                break;
                                            }
                                        }
                                    }

                                    //blackshort
                                    if (moves.canCastle[3]) {
                                        if(moves.availableMoves[i].x == 6 && moves.availableMoves[i].y == 7){
                                            //System.out.println("hello1");
                                            if(!(isItAttacked(4,7,1) || isItAttacked(5,7,1) || isItAttacked(6,7,1))){
                                                //System.out.println("hello2");

                                                reitaFylki[4][7].getChildren().remove(pieces.getPiecePane(reitaFylki[4][7]));
                                                reitaFylki[6][7].getChildren().remove(pieces.getPiecePane(reitaFylki[6][7]));
                                                pieces.swapPlaces(reitaFylki[4][7],reitaFylki[6][7]);
                                                reitaFylki[4][7].getChildren().add(pieces.getPiecePane(reitaFylki[4][7]));
                                                reitaFylki[6][7].getChildren().add(pieces.getPiecePane(reitaFylki[6][7]));


                                                reitaFylki[7][7].getChildren().remove(pieces.getPiecePane(reitaFylki[7][7]));
                                                reitaFylki[5][7].getChildren().remove(pieces.getPiecePane(reitaFylki[5][7]));
                                                pieces.swapPlaces(reitaFylki[7][7], reitaFylki[5][7]);
                                                reitaFylki[7][7].getChildren().add(pieces.getPiecePane(reitaFylki[7][7]));
                                                reitaFylki[5][7].getChildren().add(pieces.getPiecePane(reitaFylki[5][7]));

                                                moves.squareStatus[7][7] = 2;
                                                moves.squareStatus[4][7] = 2;
                                                moves.squareStatus[5][7] = 1;
                                                moves.squareStatus[6][7] = 1;

                                                if(haveTurns){
                                                    whoseTurn = (whoseTurn + 1) % 2;
                                                }

                                                if(flipboard){
                                                    rotatePieces(disgustingGridPane);
                                                }
                                                justMoved = true;

                                                break;
                                            }
                                        }
                                    }
                                    feedback2.setText("ILLEGAL MOVE");
                                    break;
                                }
                            }

                            Pieces.chessPiece tempPiece;
                            int tempcolor;
                            int tempsquare;
                            tempPiece = pieces.board.get(reitaFylki[oldColumnValue][oldRowNumber]);
                            tempcolor = tempPiece.color;
                            tempsquare = moves.squareStatus[oldColumnValue][oldRowNumber];
                            tempPiece.color = 2;
                            moves.squareStatus[oldColumnValue][oldRowNumber] = 2;
                            //turns the king "invisible" because he can block the checked square he's moving to, making it "legal"
                            //was a very annoying bug to discover

                            if(isItAttacked(columnLetter,rowNumber,tempcolor)){
                                tempPiece.color = tempcolor;
                                moves.squareStatus[oldColumnValue][oldRowNumber] = tempsquare;
                                break;
                            } else {
                                if(tempcolor == 0){
                                    tempPiece.color = tempcolor;
                                    moves.squareStatus[oldColumnValue][oldRowNumber] = tempsquare;
                                    moves.whiteKingPos.x = columnLetter;
                                    moves.whiteKingPos.y = rowNumber;
                                } else {
                                    tempPiece.color = tempcolor;
                                    moves.squareStatus[oldColumnValue][oldRowNumber] = tempsquare;
                                    moves.blackKingPos.x = columnLetter;
                                    moves.blackKingPos.y = rowNumber;
                                }
                            }
                            //checks if the square that the king tries to move to is checked
                        }
                        //System.out.println("White King Position: " + moves.whiteKingPos.x + ", " + moves.whiteKingPos.y);
                        //System.out.println("Black King Position: " + moves.blackKingPos.x + ", " + moves.blackKingPos.y);
                        pieces.swapPlaces(reitaFylki[oldColumnValue][oldRowNumber],reitaFylki[columnLetter][rowNumber]);
                        Pieces.chessPiece checkSwapPiece = pieces.board.get(reitaFylki[oldColumnValue][oldRowNumber]);
                        String tempType = checkSwapPiece.pieceType;
                        checkSwapPiece.pieceType = "";
                        int tempColor = checkSwapPiece.color;
                        checkSwapPiece.color = 2;

                        if(pieces.getPieceColor(currentStackpane) == 0){
                            if(isItAttacked(moves.whiteKingPos.x,moves.whiteKingPos.y,0)){
                                pieces.swapPlaces(reitaFylki[oldColumnValue][oldRowNumber],reitaFylki[columnLetter][rowNumber]);
                                if(checkMateChecking){
                                    return;
                                }
                                feedback2.setText("ILLEGAL MOVE");
                                checkSwapPiece.pieceType = tempType;
                                checkSwapPiece.color = tempColor;
                                break;
                            }
                        } else {
                            if(isItAttacked(moves.blackKingPos.x,moves.blackKingPos.y,1)){
                                pieces.swapPlaces(reitaFylki[oldColumnValue][oldRowNumber],reitaFylki[columnLetter][rowNumber]);
                                feedback2.setText("ILLEGAL MOVE");
                                checkSwapPiece.pieceType = tempType;
                                checkSwapPiece.color = tempColor;
                                if(checkMateChecking){
                                    break;
                                }
                                break;
                            }
                        }
                        //tries to move the piece, and swaps back if new position results in check

                        pieces.swapPlaces(reitaFylki[oldColumnValue][oldRowNumber],reitaFylki[columnLetter][rowNumber]);
                        checkSwapPiece.pieceType = tempType;
                        checkSwapPiece.color = tempColor;
                        if(checkMateChecking){
                            checkMateChecking = false;
                            checkmateReturnBoolean = false;
                            break;
                        }
                        //idk man. For the checkmate checking.



                        //en passant
                        if (pieces.getPieceType(reitaFylki[oldColumnValue][oldRowNumber]).equals("pawn") && (rowNumber == moves.enPassant.y && columnLetter == moves.enPassant.x) && moves.enPassant.x != -4){
                            //checks if move is marked en passant move
                            Pieces.chessPiece enPpiece;
                            if (pieces.getPieceColor(reitaFylki[oldColumnValue][oldRowNumber]) == 0){
                                enPpiece = pieces.board.get(reitaFylki[columnLetter][rowNumber - 1]);
                                moves.squareStatus[moves.enPassant.x][moves.enPassant.y - 1] = 2;
                                bottomPieceContainer.getChildren().add(enPpiece.pane);
                                enPpiece.pane.setTranslateX(blackPieceCount*40);
                                blackPieceCount++;
                            } else {
                                enPpiece = pieces.board.get(reitaFylki[columnLetter][rowNumber + 1]);
                                moves.squareStatus[moves.enPassant.x][moves.enPassant.y + 1] = 2;
                                topPieceContainer.getChildren().add(enPpiece.pane);
                                enPpiece.pane.setTranslateX(whitePieceCount*40);
                                whitePieceCount++;
                            }
                            //only kill in chess that is not on the same square
                            if(whichrotation){
                                enPpiece.pane.setRotate(enPpiece.pane.getRotate() + 180);
                            }
                            //idk why I had to do this but they were like upside down I guess sometimes when killed

                            enPpiece.pieceType = "";
                            enPpiece.label.setText("");
                            enPpiece.label.getStyleClass().clear();
                            enPpiece.color = 2;
                            enPpiece.pane = new Pane();
                        }



                        reitaFylki[oldColumnValue][oldRowNumber].getChildren().remove(pieces.getPiecePane(reitaFylki[oldColumnValue][oldRowNumber]));


                        pieces.swapPlaces(reitaFylki[oldColumnValue][oldRowNumber],reitaFylki[columnLetter][rowNumber]);

                        reitaFylki[oldColumnValue][oldRowNumber].getChildren().add(pieces.getPiecePane(reitaFylki[oldColumnValue][oldRowNumber]));
                        reitaFylki[columnLetter][rowNumber].getChildren().add(pieces.getPiecePane(reitaFylki[columnLetter][rowNumber]));



                        moves.squareStatus[columnLetter][rowNumber] = moves.squareStatus[oldColumnValue][oldRowNumber];
                        moves.squareStatus[oldColumnValue][oldRowNumber] = 2;

                        Pieces.chessPiece piece = pieces.board.get(reitaFylki[oldColumnValue][oldRowNumber]);

                        if(whichrotation){
                            piece.pane.setRotate(piece.pane.getRotate() + 180);
                        }
                        //killed piece is rotated based on board rotation. It's actually kinda fucked up, as when you look at the board
                        //you'd think the pieces never rotate even though I obviously had to program them to because the board rotates and they gotta
                        //account for that, but I guess I forgot and was dumbfounded why they were upside down when killed

                        //killed piece, add to side
                        if(!pieces.getPieceType(reitaFylki[oldColumnValue][oldRowNumber]).equals("")){
                            if(pieces.getPieceColor(reitaFylki[oldColumnValue][oldRowNumber]) == 0){
                                topPieceContainer.getChildren().add(piece.pane);




                                piece.pane.setTranslateX(whitePieceCount*40);
                                pieces.board.get(reitaFylki[oldColumnValue][oldRowNumber]).pane = new Pane();
                                //the panes actually make it very easy to rotate things, which was a nice surprise and not preplanned
                                //sometimes life is good to you, it's rare but you gotta appreciate it
                                whitePieceCount++;
                            } else {
                                bottomPieceContainer.getChildren().add(piece.pane);
                                piece.pane.setTranslateX(blackPieceCount*40);
                                pieces.board.get(reitaFylki[oldColumnValue][oldRowNumber]).pane = new Pane();
                                blackPieceCount++;
                            }
                        }



                        moves.enPassant.x = -4;
                        moves.enPassant.y = -4;
                        //marked en passant spot placed out of bounds

                        if (pieces.getPieceType(currentStackpane).equals("pawn") && Math.abs(oldRowNumber - rowNumber) == 2){
                            moves.enPassant.x = columnLetter;
                            if(pieces.getPieceColor(currentStackpane) == 0){
                                moves.enPassant.y = rowNumber - 1;
                            } else {
                                moves.enPassant.y = rowNumber + 1;
                            }
                        }
                        //plants the en passant seed. Only available for one move of course, so just have to account for one square at a time

                        piece.pieceType = "";
                        piece.label.setText("");
                        piece.color = 2;
                        piece.label.getStyleClass().clear();
                        //old square is made neutral
                        //the pieces used to be labels. Call me nostalgic but I don't want to remove it

                        if(haveTurns && !checkMateChecking){
                            whoseTurn = (whoseTurn + 1) % 2;
                        }
                        //some wonkiness through trial and error. All these random booleans were not particularly thought out
                        //and there was a lot of frustration

                        if(flipboard){
                            rotatePieces(disgustingGridPane);
                        }
                        justMoved = true;

                        //System.out.println("i should not talk");
                        checkMateChecking = false;
                        /*
                        if(pieces.getPieceType(reitaFylki[columnLetter][rowNumber]).equals("king")){
                            if(pieces.getPieceColor(reitaFylki[columnLetter][rowNumber]) == 0){
                                moves.whiteKingPos.x = columnLetter;
                                moves.whiteKingPos.y = rowNumber;
                            } else {
                                moves.blackKingPos.x = columnLetter;
                                moves.blackKingPos.y = rowNumber;
                            }
                        }
                        */

                        break;
                    }

                }
            }

            moves.getMoves(columnLetter, rowNumber, "", pieces.getPieceColor(currentStackpane));
            //I think it's so it doesn't break when a non piece square is selected, or when checking for checkmates


            aPieceIsSelected = false;
        }
        for(int i = 0; i < 27; i++){
            if(moves.availableMoves[i] == null){
                break;
            }
            if(moves.squareStatus[moves.availableMoves[i].x][moves.availableMoves[i].y] != 2){
                moveIndicators[i].setFill(Color.RED);
            }
            if(moves.enPassant.x != -4){
                if(moves.availableMoves[i].x == moves.enPassant.x && moves.availableMoves[i].y == moves.enPassant.y && pieces.getPieceType(reitaFylki[columnLetter][rowNumber]).equals("pawn")){
                    moveIndicators[i].setFill(Color.RED);
                }
            }

            reitaFylki[moves.availableMoves[i].x][moves.availableMoves[i].y].getChildren().add(moveIndicators[i]);
        }
        //places and colors the move indicators

        if(checkMateChecking){
            return;
        }
        //avoids anything disturbing happening during the checkmate checking, probably would be an infinite loop as well me thinks

        if(whoseTurn == 0){
            feedback1.setText("white to move");
        } else {
            feedback1.setText("black to move");
        }
        oldColumnValue = columnLetter;
        oldRowNumber = rowNumber;

        //castling checker
        if(!pieces.getPieceType(reitaFylki[0][0]).equals("rook")){
            moves.canCastle[0] = false;
        }
        if(!pieces.getPieceType(reitaFylki[7][0]).equals("rook")){
            moves.canCastle[1] = false;
        }
        if(!pieces.getPieceType(reitaFylki[0][7]).equals("rook")){
            moves.canCastle[2] = false;
        }
        if(!pieces.getPieceType(reitaFylki[7][7]).equals("rook")){
            moves.canCastle[3] = false;
        }
        //if rooks are ever not on original squares, that specific castling is disabled for the rest of the game

        //System.out.println(moves.whiteKingPos.x + " " + moves.whiteKingPos.y);
        //System.out.println(moves.blackKingPos.x + " " + moves.blackKingPos.y);
        if(moves.whiteKingPos.x != 4 || moves.whiteKingPos.y != 0){
            moves.canCastle[0] = false;
            moves.canCastle[1] = false;
        }
        if(moves.blackKingPos.x != 4 || moves.blackKingPos.y != 7){
            moves.canCastle[2] = false;
            moves.canCastle[3] = false;
        }
        //same deal as with rooks

        //promotion
        if(pieces.getPieceType(currentStackpane).equals("pawn")) {
            if (rowNumber == 7 || rowNumber == 0) {
                if ((!vsAI || AIcolor != whoseTurn) && !AIvsAI) {
                    //this logic was a surprising amount of headache
                    feedback2.setText("select a piece");
                    lockGame = true;
                    mathlist.add("queen");
                    mathlist.add("rook");
                    mathlist.add("bishop");
                    mathlist.add("knight");
                    menuList.getItems().clear();
                    menuList.getItems().addAll(mathlist);
                } else {
                    Pieces.chessPiece tempPiece = pieces.board.get(currentStackpane);
                    tempPiece.pieceType = "queen";
                    currentStackpane.getChildren().remove(pieces.getPiecePane(currentStackpane));
                    tempPiece.pane = pieces.createQueen(pieces.getPieceColor(currentStackpane));
                    currentStackpane.getChildren().add(pieces.getPiecePane(currentStackpane));
                    if (whichrotation) {
                        tempPiece.pane.setRotate(tempPiece.pane.getRotate() + 180);
                    }
                }
                //AI always just gets a queen
            }
        }


        //checkCheck
        //white
        Pieces.chessPiece tempPiece;
        if(isItAttacked(moves.whiteKingPos.x, moves.whiteKingPos.y, 0) && !checkMateChecking && justMoved){
            //check if king is attacked
            feedback2.setText("white king is checked");
            tempPiece = pieces.board.get(reitaFylki[moves.whiteKingPos.x][moves.whiteKingPos.y]);
            tempPiece.color = 2;
            tempPiece.pieceType = "";
            moves.squareStatus[moves.whiteKingPos.x][moves.whiteKingPos.y] = 2;
            //System.out.println(pieces.getPieceColor(reitaFylki[4][1]));
            //System.out.println(pieces.getPieceType(reitaFylki[4][1]));
            if(maybeCheckmate(0)){
                //check if all pieces around king are checked
                moves.squareStatus[moves.whiteKingPos.x][moves.whiteKingPos.y] = 0;
                tempPiece.color = 0;
                if (haveTurns) {
                    whoseTurn = 0;
                }
                moves.getAllTeamPieces(0);
                //gets a list of all the pieces
                if(secondCheckmateCheck(0)){
                    //checks if checkmate is for real and ends games
                    feedback2.setText("checkmate");
                    feedback1.setText("black wins");
                    lockGame = true;
                    runTimer = false;
                    AIvsAI = false;
                    for(int i = 0; i < 27; i++){
                        if(moves.availableMoves[i] == null){
                            break;
                        }
                        reitaFylki[moves.availableMoves[i].x][moves.availableMoves[i].y].getChildren().remove(moveIndicators[i]);

                        moveIndicators[i].setFill(Color.ORANGE);
                    }
                    return;
                } else {
                    feedback2.setText("white king is checked");
                }
                checkMateChecking = false;
            }
            tempPiece.pieceType = "king";
            moves.squareStatus[moves.whiteKingPos.x][moves.whiteKingPos.y] = 0;
            tempPiece.color = 0;
        }

        if(isItAttacked(moves.blackKingPos.x, moves.blackKingPos.y, 1)  && !checkMateChecking && justMoved){
            //System.out.println("check");
            feedback2.setText("black king is checked");
            tempPiece = pieces.board.get(reitaFylki[moves.blackKingPos.x][moves.blackKingPos.y]);
            tempPiece.color = 2;
            moves.squareStatus[moves.blackKingPos.x][moves.blackKingPos.y] = 2;
            tempPiece.pieceType = "";
            if(maybeCheckmate(1)){
                //System.out.println("maybecheckmate");
                tempPiece.color = 1;
                moves.squareStatus[moves.blackKingPos.x][moves.blackKingPos.y] = 1;
                if (haveTurns) {
                    whoseTurn = 1;
                }
                moves.getAllTeamPieces(1);
                if(secondCheckmateCheck(1)){
                    feedback2.setText("checkmate");
                    feedback1.setText("white wins");
                    lockGame = true;
                    runTimer = false;
                    AIvsAI = false;
                    for(int i = 0; i < 27; i++){
                        if(moves.availableMoves[i] == null){
                            break;
                        }
                        reitaFylki[moves.availableMoves[i].x][moves.availableMoves[i].y].getChildren().remove(moveIndicators[i]);

                        moveIndicators[i].setFill(Color.ORANGE);
                    }
                    return;
                } else {
                    feedback2.setText("black king is checked");
                }
                checkMateChecking = false;
            }
            tempPiece.pieceType = "king";
            moves.squareStatus[moves.blackKingPos.x][moves.blackKingPos.y] = 1;
            tempPiece.color = 1;
        }
        if(allowTimer){
            runTimer = true;
        }

        oneTimerBox.setVisible(false);
        twoTimerBox.setVisible(false);
        //hide clock change buttons

        showTimers = false;

        //System.out.println(whoseTurn);


        //System.out.println(isItAttacked(0,3,0));
        //System.out.println(pieces.getPieceType(currentStackpane));
        //System.out.println(pieces.getPieceColor(currentStackpane));
        if(vsAI){
            if ((AIcolor == whoseTurn && !lockGame)) {
                Platform.runLater(() -> AImove());
            }
        }
        //makes opposite AI move
    }

    public boolean isItAttacked(int column, int row, int color){
        //I'm not gonna comment everything here
        //All I'll say is that the idea is pretty fucking neat and I want to share it as I'm proud of it
        //turns the square into a sort of queen+knight, and then checks if it can "attack" the relevant pieces on
        //the relevant squares. If so, it's attacked
        //really should've been in Moves.java, but there was some annoying reason that I couldn't do it. Don't remember what it was

        //for this and the moves, I did a lot of debugging and repetition via chatgpt. So if it looks too clean, that's why
        //however, this is all me and my ideas. It was just a nightmare to debug on my own as one mistyped 1 or 0 or whatever
        //was so hard to find.
        //All the ideas and core implementation was mine, and I had to do all the initial entries to repeat off of anyway

        int oppositeColor = (color + 1) % 2;

        // up
        for (int i = row + 1; i < 8; i++) {
            if (pieces.getPieceColor(reitaFylki[column][i]) == color) break;
            if (pieces.getPieceType(reitaFylki[column][i]).equals("pawn")) break;
            if (pieces.getPieceColor(reitaFylki[column][i]) == oppositeColor) {
                if (pieces.getPieceType(reitaFylki[column][i]).equals("queen") || pieces.getPieceType(reitaFylki[column][i]).equals("rook")) {
                    return true;
                }
                break;
            }
        }

        // down
        for (int i = row - 1; i >= 0; i--) {
            if (pieces.getPieceColor(reitaFylki[column][i]) == color) break;
            if (pieces.getPieceType(reitaFylki[column][i]).equals("pawn")) break;
            if (pieces.getPieceColor(reitaFylki[column][i]) == oppositeColor) {
                if (pieces.getPieceType(reitaFylki[column][i]).equals("queen") || pieces.getPieceType(reitaFylki[column][i]).equals("rook")) {
                    return true;
                }
                break;
            }
        }

        // left
        for (int i = column - 1; i >= 0; i--) {
            if (pieces.getPieceColor(reitaFylki[i][row]) == color) break;
            if (pieces.getPieceType(reitaFylki[i][row]).equals("pawn")) break;
            if (pieces.getPieceColor(reitaFylki[i][row]) == oppositeColor) {
                if (pieces.getPieceType(reitaFylki[i][row]).equals("queen") || pieces.getPieceType(reitaFylki[i][row]).equals("rook")) {
                    return true;
                }
                break;
            }
        }

        // right
        for (int i = column + 1; i < 8; i++) {
            if (pieces.getPieceColor(reitaFylki[i][row]) == color) break;
            if (pieces.getPieceType(reitaFylki[i][row]).equals("pawn")) break;
            if (pieces.getPieceColor(reitaFylki[i][row]) == oppositeColor) {
                if (pieces.getPieceType(reitaFylki[i][row]).equals("queen") || pieces.getPieceType(reitaFylki[i][row]).equals("rook")) {
                    return true;
                }
                break;
            }
        }



        // up right
        for (int i = 1; row + i < 8 && column + i < 8; i++) {
            if (pieces.getPieceColor(reitaFylki[column + i][row + i]) == color) break;
            if (pieces.getPieceType(reitaFylki[column + i][row + i]).equals("pawn")) break;
            if (pieces.getPieceColor(reitaFylki[column + i][row + i]) == oppositeColor) {
                if (pieces.getPieceType(reitaFylki[column + i][row + i]).equals("queen") || pieces.getPieceType(reitaFylki[column + i][row + i]).equals("bishop")) {
                    return true;
                }
                break;
            }
        }

        // up left
        for (int i = 1; row + i < 8 && column - i >= 0; i++) {
            if (pieces.getPieceColor(reitaFylki[column - i][row + i]) == color) break;
            if (pieces.getPieceType(reitaFylki[column - i][row + i]).equals("pawn")) break;
            if (pieces.getPieceColor(reitaFylki[column - i][row + i]) == oppositeColor) {
                if (pieces.getPieceType(reitaFylki[column - i][row + i]).equals("queen") || pieces.getPieceType(reitaFylki[column - i][row + i]).equals("bishop")) {
                    return true;
                }
                break;
            }
        }

        // down right
        for (int i = 1; row - i >= 0 && column + i < 8; i++) {
            if (pieces.getPieceColor(reitaFylki[column + i][row - i]) == color) break;
            if (pieces.getPieceType(reitaFylki[column + i][row - i]).equals("pawn")) break;
            if (pieces.getPieceColor(reitaFylki[column + i][row - i]) == oppositeColor) {
                if (pieces.getPieceType(reitaFylki[column + i][row - i]).equals("queen") || pieces.getPieceType(reitaFylki[column + i][row - i]).equals("bishop")) {
                    return true;
                }
                break;
            }
        }

        // down left
        for (int i = 1; row - i >= 0 && column - i >= 0; i++) {
            if (pieces.getPieceColor(reitaFylki[column - i][row - i]) == color) break;
            if (pieces.getPieceType(reitaFylki[column - i][row - i]).equals("pawn")) break;
            if (pieces.getPieceColor(reitaFylki[column - i][row - i]) == oppositeColor) {
                if (pieces.getPieceType(reitaFylki[column - i][row - i]).equals("queen") || pieces.getPieceType(reitaFylki[column - i][row - i]).equals("bishop")) {
                    return true;
                }
                break;
            }
        }

        if (row < 6) {
            if (column > 0) {
                if (pieces.getPieceColor(reitaFylki[column - 1][row + 2]) == oppositeColor && pieces.getPieceType(reitaFylki[column - 1][row + 2]).equals("knight")) {
                    return true;
                }
            }
            if ( column < 7) {
                if (pieces.getPieceColor(reitaFylki[column + 1][row + 2]) == oppositeColor && pieces.getPieceType(reitaFylki[column + 1][row + 2]).equals("knight")) {
                    return true;
                }
            }
        }

        if (column < 6) {
            if (row < 7) {
                if (pieces.getPieceColor(reitaFylki[column + 2][row + 1]) == oppositeColor && pieces.getPieceType(reitaFylki[column + 2][row + 1]).equals("knight")) {
                    return true;
                }
            }
            if(row > 0) {
                if (pieces.getPieceColor(reitaFylki[column + 2][row - 1]) == oppositeColor && pieces.getPieceType(reitaFylki[column + 2][row - 1]).equals("knight")) {
                    return true;
                }
            }
        }
        if (row > 1) {
            if(column > 0) {
                if (pieces.getPieceColor(reitaFylki[column - 1][row - 2]) == oppositeColor && pieces.getPieceType(reitaFylki[column - 1][row - 2]).equals("knight")) {
                    return true;
                }
            }
            if(column < 7) {
                if (pieces.getPieceColor(reitaFylki[column + 1][row - 2]) == oppositeColor && pieces.getPieceType(reitaFylki[column + 1][row - 2]).equals("knight")) {
                    return true;
                }
            }
        }
        if (column > 1) {
            if (row < 7) {
                if (pieces.getPieceColor(reitaFylki[column - 2][row + 1]) == oppositeColor && pieces.getPieceType(reitaFylki[column - 2][row + 1]).equals("knight")) {
                    return true;
                }
            }
            if (row > 1) {
                if (pieces.getPieceColor(reitaFylki[column - 2][row - 1]) == oppositeColor && pieces.getPieceType(reitaFylki[column - 2][row - 1]).equals("knight")) {
                    return true;
                }
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {
                    continue;
                }
                if (column - 1 + i >= 0 && column - 1 + i < 8) {
                    if (row - 1 + j >= 0 && row - 1 + j < 8) {
                        if (pieces.getPieceColor(reitaFylki[column - 1 + i][row - 1 + j]) == oppositeColor) {
                            if (pieces.getPieceType(reitaFylki[column - 1 + i][row - 1 + j]).equals("king")) {
                                return true;
                            }
                        }
                    }
                }
            }
        }




        //white pawn
        if (color == 0) {
            if (row != 7) {
                if (column != 0) {
                    if (pieces.getPieceColor(reitaFylki[column - 1][row + 1]) == oppositeColor
                            && pieces.getPieceType(reitaFylki[column - 1][row + 1]).equals("pawn")) {
                        return true;
                    }
                }
                if (column != 7) {
                    if (pieces.getPieceColor(reitaFylki[column + 1][row + 1]) == oppositeColor
                            && pieces.getPieceType(reitaFylki[column + 1][row + 1]).equals("pawn")) {
                        return true;
                    }
                }
            }
        }


        // Black pawn
        if (color == 1) {
            if (row != 0) {
                if (column != 0) {
                    if (pieces.getPieceColor(reitaFylki[column - 1][row - 1]) == oppositeColor
                            && pieces.getPieceType(reitaFylki[column - 1][row - 1]).equals("pawn")) {
                        return true;
                    }
                }
                if (column != 7) {
                    if (pieces.getPieceColor(reitaFylki[column + 1][row - 1]) == oppositeColor
                            && pieces.getPieceType(reitaFylki[column + 1][row - 1]).equals("pawn")) {
                        return true;
                    }
                }
            }
        }


        return false;
    }

    public void rotatePieces(Pane gridcont){
        gridcont.setRotate(gridcont.getRotate() + 180);

        double howFar = 335;
        double howFar2 = 335;
        if(!whichrotation){
            topPieceContainer.setTranslateY(335);
            bottomPieceContainer.setTranslateY(-howFar);

            oneTimeContainer.setTranslateY(-howFar2);
            twoTimeContainer.setTranslateY(howFar2);
        } else {
            topPieceContainer.setTranslateY(-howFar);
            bottomPieceContainer.setTranslateY(335);

            oneTimeContainer.setTranslateY(howFar2);
            twoTimeContainer.setTranslateY(-howFar2);
        }


        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                pieces.getPiecePane(reitaFylki[i][j]).setRotate(pieces.getPiecePane(reitaFylki[i][j]).getRotate() + 180);
            }
        }


        whichrotation = !whichrotation;
    }

    public boolean maybeCheckmate(int color) {
        if (color == 0) {
            //int testInt = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (moves.whiteKingPos.x - 1 + i >= 0 && moves.whiteKingPos.x - 1 + i < 8) {
                        if (moves.whiteKingPos.y - 1 + j >= 0 && moves.whiteKingPos.y - 1 + j < 8) {
                            //testInt++;
                            //System.out.print(testInt);
                            if (moves.squareStatus[moves.whiteKingPos.x - 1 + i][moves.whiteKingPos.y - 1 + j] != color) {
                                if (!isItAttacked(moves.whiteKingPos.x - 1 + i, moves.whiteKingPos.y - 1 + j, 0)) return false;
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (moves.blackKingPos.x - 1 + i >= 0 && moves.blackKingPos.x - 1 + i < 8) {
                        if (moves.blackKingPos.y - 1 + j >= 0 && moves.blackKingPos.y - 1 + j < 8) {
                            if (moves.squareStatus[moves.blackKingPos.x - 1 + i][moves.blackKingPos.y - 1 + j] != color) {
                                if (!isItAttacked(moves.blackKingPos.x - 1 + i, moves.blackKingPos.y - 1 + j, 1)) return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
    }

    public void resetBoard(boolean forever){
        if(whichrotation){
            rotatePieces(disgustingGridPane);
        }
        amountofMoves = 0;
        whoseTurn = 0;
        oldColumnValue = 4;
        oldRowNumber = 4;
        lockGame = false;
        feedback2.setText("");
        whitePieceCount = 0;
        blackPieceCount = 0;
        topPieceContainer.getChildren().clear();
        bottomPieceContainer.getChildren().clear();
        runTimer = false;
        whiteTimer = 300*8;
        blackTimer = 300*8;
        AIcolor = 1;
        gameHasStarted = false;
        checkMateChecking = false;
        if(!forever){
            vsAI = false;
            AIvsAI = false;
            AIcolor = 0;
        }
        //I want AI to stop if I press reset button
        checkmateReturnBoolean = false;
        nGames++;
        System.out.println(nGames);
    }

    public void listSelect(){
        if(currentFood.equals("flip board")){
            rotatePieces(disgustingGridPane);
        } else if (currentFood.equals("toggle board flip after move")){
            flipboard = !flipboard;
            if(flipboard){
                feedback2.setText("board will now flip after each move");
            } else {
                feedback2.setText("board now won't flip after each move");
            }
        } else if (currentFood.equals("toggle if turns")){
            haveTurns = !haveTurns;
            if(!haveTurns){
                feedback2.setText("you can play with yourself now");
            } else {
                feedback2.setText("both can now move");
            }
        } else if (currentFood.equals("edit timer")) {
            runTimer = false;
            showTimers = !showTimers;
            oneTimerBox.setVisible(showTimers);
            twoTimerBox.setVisible(showTimers);
        } else if (currentFood.equals("toggle timer")) {
            if(runTimer){
                runTimer = false;
            }
            allowTimer = !allowTimer;
            if(allowTimer){
                feedback2.setText("clock is resumed");
            } else {
                feedback2.setText("clock is paused");
            }
        } else if (currentFood.equals("queen")){
            currentFood="";
            //bugs could arise if I didn't change currentFood to nothing, and you could maybe click something when not supposed to
            Pieces.chessPiece tempPiece = pieces.board.get(currentStackpane);
            tempPiece.pieceType = "queen";
            currentStackpane.getChildren().remove(pieces.getPiecePane(currentStackpane));
            tempPiece.pane = pieces.createQueen(pieces.getPieceColor(currentStackpane));
            currentStackpane.getChildren().add(pieces.getPiecePane(currentStackpane));
            if(whichrotation){
                tempPiece.pane.setRotate(tempPiece.pane.getRotate() + 180);
            }
            lockGame = false;
            if(vsAI){
                AImove();
            }
            mathlist.remove("queen");
            mathlist.remove("rook");
            mathlist.remove("knight");
            mathlist.remove("bishop");
            menuList.getItems().clear();
            menuList.getItems().addAll(mathlist);
        } else if (currentFood.equals("rook")){
            currentFood="";
            Pieces.chessPiece tempPiece = pieces.board.get(currentStackpane);
            tempPiece.pieceType = "rook";
            currentStackpane.getChildren().remove(pieces.getPiecePane(currentStackpane));
            tempPiece.pane = pieces.createRook(pieces.getPieceColor(currentStackpane));
            currentStackpane.getChildren().add(pieces.getPiecePane(currentStackpane));
            if(whichrotation){
                tempPiece.pane.setRotate(tempPiece.pane.getRotate() + 180);
            }
            lockGame = false;
            if(vsAI){
                AImove();
            }
            mathlist.remove("queen");
            mathlist.remove("rook");
            mathlist.remove("knight");
            mathlist.remove("bishop");
            menuList.getItems().clear();
            menuList.getItems().addAll(mathlist);
        } else if (currentFood.equals("bishop")){
            currentFood="";
            Pieces.chessPiece tempPiece = pieces.board.get(currentStackpane);
            tempPiece.pieceType = "bishop";
            currentStackpane.getChildren().remove(pieces.getPiecePane(currentStackpane));
            tempPiece.pane = pieces.createBishop(pieces.getPieceColor(currentStackpane));
            currentStackpane.getChildren().add(pieces.getPiecePane(currentStackpane));
            if(whichrotation){
                tempPiece.pane.setRotate(tempPiece.pane.getRotate() + 180);
            }
            lockGame = false;
            if(vsAI){
                AImove();
            }
            mathlist.remove("queen");
            mathlist.remove("rook");
            mathlist.remove("knight");
            mathlist.remove("bishop");
            menuList.getItems().clear();
            menuList.getItems().addAll(mathlist);
        } else if (currentFood.equals("knight")){
            currentFood="";
            Pieces.chessPiece tempPiece = pieces.board.get(currentStackpane);
            tempPiece.pieceType = "knight";
            currentStackpane.getChildren().remove(pieces.getPiecePane(currentStackpane));
            tempPiece.pane = pieces.createKnight(pieces.getPieceColor(currentStackpane));
            currentStackpane.getChildren().add(pieces.getPiecePane(currentStackpane));
            if(whichrotation){
                tempPiece.pane.setRotate(tempPiece.pane.getRotate() + 180);
            }
            lockGame = false;
            if(vsAI){
                AImove();
            }
            mathlist.remove("queen");
            mathlist.remove("rook");
            mathlist.remove("knight");
            mathlist.remove("bishop");
            menuList.getItems().clear();
            menuList.getItems().addAll(mathlist);
        } else if (currentFood.equals("play vs advanced AI?")){
            currentFood="";
            vsAI = true;
            mathlist.add("are you black?");
            mathlist.remove("play vs advanced AI?");
            menuList.getItems().clear();
            menuList.getItems().addAll(mathlist);
        } else if (currentFood.equals("are you black?")){
            currentFood="";
            mathlist.add("are you not even playing?");
            mathlist.remove("are you black?");
            menuList.getItems().clear();
            AIcolor = 0;
            AImove();
        } else if (currentFood.equals("are you not even playing?")){
            currentFood="";
            mathlist.remove("are you not even playing?");
            AIvsAI = true;
            menuList.getItems().clear();
            menuList.getItems().addAll(mathlist);
            AIcolor = whoseTurn;
            AImove();
        }
    }

    public void LtheClock(){
        //everything in this function is very self explanatory

        if(AIvsAI){
            AImoveInterval = (AImoveInterval + 1) % 2;
            //System.out.println("hello" + AImoveInterval);
            if(AImoveInterval % 0.5 == 0){
                amountofMoves++;
                AIcolor = (AIcolor + 1)%2;
                if(amountofMoves > 350){
                    resetBoard(true);
                    initializetest();
                }
                AImove();
            }
        }


        if(!runTimer){
            return;
        }

        if(whiteTimer <= 0){
            lockGame = true;
            feedback1.setText("black won");
            feedback2.setText("white flagged");
            runTimer = false;
            lockGame = true;
        }
        if(blackTimer <= 0){
            lockGame = true;
            feedback1.setText("white won");
            feedback2.setText("black flagged");
            runTimer = false;
            lockGame = true;
        }

        if(runTimer){
            if(whoseTurn == 0){
                whiteTimer--;
                if(whiteTimer <= 0){
                    whiteTimer = 0;
                }
                int conversion = (int) whiteTimer/8;
                if (conversion >= 3600) {
                    oneLabel.setText(String.format("%d:%02d:%02d",
                            (int) Math.floor(conversion / 3600),
                            (conversion / 60 % 60),
                            (conversion % 60)
                            //got this from chatgpt and am not ashamed of it
                    ));
                } else {
                    oneLabel.setText(String.format("%02d:%02d",
                            (conversion / 60 % 60),
                            (conversion % 60)
                    ));
                }
            } else {
                blackTimer--;
                if(blackTimer <= 0){
                    blackTimer = 0;
                }
                int conversion = (int) blackTimer/8;
                if (conversion >= 3600) {
                    twoLabel.setText(String.format("%d:%02d:%02d",
                            (int) Math.floor(conversion / 3600),
                            (conversion / 60 % 60),
                            (conversion % 60)
                    ));
                } else {
                    twoLabel.setText(String.format("%02d:%02d",
                            (conversion / 60 % 60),
                            (conversion % 60)
                    ));
                }
            }
        }
    }

    public void addtoclocks(int howmuch, int whichcolor){

        if(whichcolor==0){
            whiteTimer = whiteTimer + howmuch;
            if(whiteTimer>288000){
                whiteTimer = 288000 - 1;
                //can't be higher than 9:59:59 or lower than 0
            }
            if(whiteTimer<0){
                whiteTimer = 0;
            }
        } else {
            blackTimer = blackTimer + howmuch;
            if(blackTimer>288000){
                blackTimer = 288000 - 1;
            }
            if(blackTimer<0){
                blackTimer = 0;
            }
        }
        int conversion = (int) whiteTimer/8;
        int conversion2 = (int) blackTimer/8;
        //dividing by 8 seemed to work well enough
        if (conversion >= 3600) {
            oneLabel.setText(String.format("%d:%02d:%02d",
                    (int) Math.floor(conversion / 3600),
                    (conversion / 60 % 60),
                    (conversion % 60)
            ));
        } else {
            oneLabel.setText(String.format("%02d:%02d",
                    (conversion / 60 % 60),
                    (conversion % 60)
            ));
        }
        if (conversion2 >= 3600) {
            twoLabel.setText(String.format("%d:%02d:%02d",
                    (int) Math.floor(conversion2 / 3600),
                    (conversion2 / 60 % 60),
                    (conversion2 % 60)
            ));
        } else {
            twoLabel.setText(String.format("%02d:%02d",
                    (conversion2 / 60 % 60),
                    (conversion2 % 60)
            ));
        }
    }

    public void AImove(){
        //System.out.println(whoseTurn);
        //System.out.println(AIcolor);
        //System.out.println();

        //AI thing
        Random rand = new Random();
        int ifruntoolong = 0;
        feedback2.setText("the AI is thinking");
        while(whoseTurn == AIcolor){
            ifruntoolong++;
            if(ifruntoolong > 100000){
                lockGame = true;
                runTimer = false;
                feedback2.setText("calculating that it has lost, the AI has forfeited");
                if (AIcolor == 0){
                    feedback1.setText("black has won");
                } else {
                    feedback1.setText("white has won");
                }

            }
            int column = rand.nextInt(8);
            int row = rand.nextInt(8);
            currentStackpane = reitaFylki[column][row];
            updatefunc(column,row);
        }
        feedback2.setText("");
    }

    public boolean secondCheckmateCheck(int color){
        //tries every move available to see if still check on king. Ugly, but it works and is fast enough

        StackPane tempCurrentPane = currentStackpane;
        runTimer = false;
        checkMateChecking = true;
        checkmateReturnBoolean = true;
        if(color == 0){
            whoseTurn = 0;
        } else {
            whoseTurn = 1;
        }

        for(int i = 0; i<16; i++){
            if(moves.tempPieceLocations[i] != null){
                for(int j = 0; j<27; j++){
                    if(!checkMateChecking){
                        break;
                    }
                    currentStackpane = reitaFylki[moves.tempPieceLocations[i].x][moves.tempPieceLocations[i].y];
                    updatefunc(moves.tempPieceLocations[i].x,moves.tempPieceLocations[i].y);
                    if(moves.availableMoves[j] != null){
                        oldColumnValue = moves.tempPieceLocations[i].x;
                        oldRowNumber = moves.tempPieceLocations[i].y;
                        currentStackpane = reitaFylki[moves.availableMoves[j].x][moves.availableMoves[j].y];
                        updatefunc(moves.availableMoves[j].x,moves.availableMoves[j].y);
                    }
                }
            }
        }
        if(!haveTurns){
            whoseTurn = (whoseTurn + 1) % 2;
        }
        runTimer = true;
        currentStackpane = tempCurrentPane;
        checkMateChecking = false;
        return checkmateReturnBoolean;
    }

    public void initializetest(){
        //called initalizetest because I really really was not expecting this to work, but it did
        //I wanted a clean reset without having to exit out of and go back into the chess game

        int conversion = (int) whiteTimer/8;
        int conversion2 = (int) blackTimer/8;
        if (conversion >= 3600) {
            oneLabel.setText(String.format("%d:%02d:%02d",
                    (int) Math.floor(conversion / 3600),
                    (conversion / 60 % 60),
                    (conversion % 60)
            ));
        } else {
            oneLabel.setText(String.format("%02d:%02d",
                    (conversion / 60 % 60),
                    (conversion % 60)
            ));
        }
        if (conversion2 >= 3600) {
            twoLabel.setText(String.format("%d:%02d:%02d",
                    (int) Math.floor(conversion2 / 3600),
                    (conversion2 / 60 % 60),
                    (conversion2 % 60)
            ));
        } else {
            twoLabel.setText(String.format("%02d:%02d",
                    (conversion2 / 60 % 60),
                    (conversion2 % 60)
            ));
        }



        mathlist = new ArrayList<>();

        menuList.getItems().clear();

        mathlist.add("flip board");
        mathlist.add("toggle board flip after move");
        mathlist.add("toggle if turns");
        mathlist.add("toggle timer");
        mathlist.add("edit timer");
        mathlist.add("play vs advanced AI?");

        menuList.getItems().addAll(mathlist);

        menuList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentFood = (String) newValue;
            }
        });
        //side menu

        feedback1.setText("white to move");
        for (int j = 0; j < 8; j++){
            for (int i = 0; i < 8; i++){
                final int currenti = i;
                final int currentj = j;
                StackPane stackpane = new StackPane();
                Rectangle square = new Rectangle(85,85);
                stackpane.setPrefSize(85, 85);
                if ((i + j) % 2 != 0){
                    square.setFill(Color.WHITE);
                }
                reitaFylki[i][j] = stackpane;
                stackpane.getChildren().add(square);

                stackpane.setOnMouseClicked(event -> {
                    if(!lockGame && (AIcolor != whoseTurn || !vsAI) && !AIvsAI){
                        currentStackpane = reitaFylki[currenti][currentj];
                        //System.out.println(currenti + " " + currentj);
                        updatefunc(currenti, currentj);
                    }
                });


                disgustingGridPane.add(stackpane, i, 8-j);
            }
        }
        //creates board and staggers colors. And makes squares clickable.

        pieces = new Pieces();
        moves = new Moves();
        pieces.initializePieces(reitaFylki);
        //places pieces

        for (int i = 0; i<27; i++){
            Circle movablecircle = new Circle(15, Color.ORANGE);
            movablecircle.setOpacity(0.6);
            moveIndicators[i] = movablecircle;
        }
        //move indicators created

        for (int j = 0; j<8; j++){
            for (int i = 0; i<8; i++){
                if(pieces.getPiecePane(reitaFylki[i][j]) != null){
                    reitaFylki[i][j].getChildren().add(pieces.getPiecePane(reitaFylki[i][j]));
                }
                //reitaFylki[i][j].getChildren().add(pieces.getPieceLabel(reitaFylki[i][j]));
            }
        }
        //places pieces on board. Not pretty, but works.

        /*
        reitaFylki[3][3].getChildren().add(pieces.getPieceLabel(reitaFylki[3][3]));
        reitaFylki[5][5].getChildren().add(pieces.getPieceLabel(reitaFylki[5][5]));
        reitaFylki[0][0].getChildren().add(pieces.getPieceLabel(reitaFylki[0][0]));
        */

        moves.initalizeMoves();
        /*
        for (int j = 0; j<8; j++){
            for (int i = 0; i<8; i++){
                Circle attackCircle = new Circle(13);
                if (moves.attackStatus[i][j] == 2){
                    attackCircle.setFill(Color.TRANSPARENT);
                } else if (moves.attackStatus[i][j] == 0){
                    attackCircle.setFill(Color.BEIGE);
                } else if (moves.attackStatus[i][j] == 1) {
                    attackCircle.setFill(Color.DARKGRAY);
                } else {
                    attackCircle.setFill(Color.BROWN);
                }
                tempAttackStatus[i][j] = attackCircle;
                reitaFylki[i][j].getChildren().add(attackCircle);
            }
        }

        */

        /*
        for (int i = 0; i<8; i++){
            moves.arrayofthePieces[i] = pieces.board.get(reitaFylki[i][1]);
        }
        for (int i = 0; i<8; i++){
            moves.arrayofthePieces[i+8] = pieces.board.get(reitaFylki[i][0]);
        }
        for (int i = 0; i<8; i++){
            moves.arrayofthePieces[i+16] = pieces.board.get(reitaFylki[i][6]);
        }
        for (int i = 0; i<8; i++){
            moves.arrayofthePieces[i+24] = pieces.board.get(reitaFylki[i][7]);
        }
        */





        //reitaFylki[4][4].getChildren().add(wrapperPane);
    }
}