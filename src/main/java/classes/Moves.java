package classes;

public class Moves {
    int[][] squareStatus = new int[8][8];
    //0 white, 1 black, 2 nothing
    Tuple enPassant;
    Tuple whiteKingPos;
    Tuple blackKingPos;
    boolean[] canCastle = new boolean[4];
    boolean castlingMove = false;
    public void initalizeMoves(){
        enPassant = new Tuple(-4,-4);
        whiteKingPos = new Tuple(4,0);
        blackKingPos = new Tuple(4,7);

        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                squareStatus[j][i] = 2;
            }
        }
        /*
        squareStatus[0][0] = 0;
        squareStatus[3][3] = 0;
        squareStatus[5][5] = 1;
        */


        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if(1 < i && i < 6){
                    squareStatus[j][i] = 2;
                } else if (2 < i) {
                    squareStatus[j][i] = 1;
                } else {
                    squareStatus[j][i] = 0;
                }
            }
        }
        for(int i = 0; i<4; i++){
            canCastle[i] = true;
        }
    }
    public class Tuple {
        public int x;
        public int y;

        public Tuple(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    Tuple[] tempPieceLocations = new Tuple[16];
    Tuple[] availableMoves = new Tuple[27];
    public void getMoves(int columnLetter, int rowNumber, String type, int color){
        castlingMove = false;
        for(int i = 0; i<27; i++){
            availableMoves[i] = null;
        }
        int kNum = 0;

        if(type.equals("pawn")){
            //white
            if (color == 0){
                if(squareStatus[columnLetter][rowNumber + 1] == 2){
                    availableMoves[kNum] = new Tuple(columnLetter, rowNumber + 1);
                    kNum++;
                    //two forward
                    if (rowNumber == 1 && squareStatus[columnLetter][rowNumber + 2] == 2){
                        availableMoves[kNum] = new Tuple(columnLetter, rowNumber + 2);
                        kNum++;
                    }
                }


                //1 0
                if (columnLetter != 0){
                    if (squareStatus[columnLetter - 1][rowNumber + 1] == 1){
                        availableMoves[kNum] = new Tuple(columnLetter - 1, rowNumber + 1);
                        kNum++;
                    }
                }
                //0 1
                if (columnLetter != 7){
                    if (squareStatus[columnLetter + 1][rowNumber + 1] == 1){
                        availableMoves[kNum] = new Tuple(columnLetter + 1, rowNumber + 1);
                        kNum++;
                    }
                }
            }
            //black
            if (color == 1){
                if(squareStatus[columnLetter][rowNumber - 1] == 2){
                    availableMoves[kNum] = new Tuple(columnLetter, rowNumber - 1);
                    kNum++;
                    //two forward
                    if (rowNumber == 6 && squareStatus[columnLetter][rowNumber - 2] == 2){
                        availableMoves[kNum] = new Tuple(columnLetter, rowNumber - 2);
                        kNum++;
                    }
                }
                //1 0
                if (columnLetter != 0){
                    if (squareStatus[columnLetter - 1][rowNumber - 1] == 0){
                        availableMoves[kNum] = new Tuple(columnLetter - 1, rowNumber - 1);
                        kNum++;
                    }
                }
                //0 1
                if (columnLetter != 7){
                    if (squareStatus[columnLetter + 1][rowNumber - 1] == 0){
                        availableMoves[kNum] = new Tuple(columnLetter + 1, rowNumber - 1);
                        kNum++;
                    }
                }
            }


            //en passant
            if (enPassant.x != -4){
                //white
                if(rowNumber - 1 == enPassant.y){
                    if (columnLetter + 1 == enPassant.x || columnLetter - 1 == enPassant.x){
                        availableMoves[kNum] = new Tuple(enPassant.x,enPassant.y);
                        kNum++;
                    }
                }
                //black
                if(rowNumber + 1 == enPassant.y){
                    if (columnLetter + 1 == enPassant.x || columnLetter - 1 == enPassant.x){
                        availableMoves[kNum] = new Tuple(enPassant.x,enPassant.y);
                        kNum++;
                    }
                }
            }
        }
        if(type.equals("rook")){

            // Up
            for (int i = rowNumber + 1; i < 8; i++) {  // Move up the board
                if (squareStatus[columnLetter][i] == color) {
                    break;
                }
                availableMoves[kNum++] = new Tuple(columnLetter, i);
                if (squareStatus[columnLetter][i] != 2) { // Stop at first non-empty square
                    break;
                }
            }

            // Down
            for (int i = rowNumber - 1; i >= 0; i--) {  // Move down the board
                if (squareStatus[columnLetter][i] == color) {
                    break;
                }
                availableMoves[kNum++] = new Tuple(columnLetter, i);
                if (squareStatus[columnLetter][i] != 2) {
                    break;
                }
            }

            // Left
            for (int i = columnLetter - 1; i >= 0; i--) {  // Move left on the board
                if (squareStatus[i][rowNumber] == color) {
                    break;
                }
                availableMoves[kNum++] = new Tuple(i, rowNumber);
                if (squareStatus[i][rowNumber] != 2) {
                    break;
                }
            }

            // Right
            for (int i = columnLetter + 1; i < 8; i++) {  // Move right on the board
                if (squareStatus[i][rowNumber] == color) {
                    break;
                }
                availableMoves[kNum++] = new Tuple(i, rowNumber);
                if (squareStatus[i][rowNumber] != 2) {
                    break;
                }
            }
        }

        if(type.equals("bishop")){

            // up right
            for (int i = 1; columnLetter + i < 8 && rowNumber + i < 8; i++) {
                if (squareStatus[columnLetter + i][rowNumber + i] == color) {
                    break;
                }
                availableMoves[kNum++] = new Tuple(columnLetter + i, rowNumber + i);
                if (squareStatus[columnLetter + i][rowNumber + i] != 2) {
                    break;
                }
            }

            // up left
            for (int i = 1; columnLetter - i >= 0 && rowNumber + i < 8; i++) {
                if (squareStatus[columnLetter - i][rowNumber + i] == color) {
                    break;
                }
                availableMoves[kNum++] = new Tuple(columnLetter - i, rowNumber + i);
                if (squareStatus[columnLetter - i][rowNumber + i] != 2) {
                    break;
                }
            }

            // down right
            for (int i = 1; columnLetter + i < 8 && rowNumber - i >= 0; i++) {
                if (squareStatus[columnLetter + i][rowNumber - i] == color) {
                    break;
                }
                availableMoves[kNum++] = new Tuple(columnLetter + i, rowNumber - i);
                if (squareStatus[columnLetter + i][rowNumber - i] != 2) {
                    break;
                }
            }

            // down left
            for (int i = 1; columnLetter - i >= 0 && rowNumber - i >= 0; i++) {
                if (squareStatus[columnLetter - i][rowNumber - i] == color) {
                    break;
                }
                availableMoves[kNum++] = new Tuple(columnLetter - i, rowNumber - i);
                if (squareStatus[columnLetter - i][rowNumber - i] != 2) {
                    break;
                }
            }
        }

        if (type.equals("queen")) {

            // up
            for (int i = rowNumber + 1; i < 8; i++) {
                if (squareStatus[columnLetter][i] == color) break;
                availableMoves[kNum++] = new Tuple(columnLetter, i);
                if (squareStatus[columnLetter][i] != 2) break;
            }

            // down
            for (int i = rowNumber - 1; i >= 0; i--) {
                if (squareStatus[columnLetter][i] == color) break;
                availableMoves[kNum++] = new Tuple(columnLetter, i);
                if (squareStatus[columnLetter][i] != 2) break;
            }

            // left
            for (int i = columnLetter - 1; i >= 0; i--) {
                if (squareStatus[i][rowNumber] == color) break;
                availableMoves[kNum++] = new Tuple(i, rowNumber);
                if (squareStatus[i][rowNumber] != 2) break;
            }

            // right
            for (int i = columnLetter + 1; i < 8; i++) {
                if (squareStatus[i][rowNumber] == color) break;
                availableMoves[kNum++] = new Tuple(i, rowNumber);
                if (squareStatus[i][rowNumber] != 2) break;
            }

            // up right
            for (int i = 1; columnLetter + i < 8 && rowNumber + i < 8; i++) {
                if (squareStatus[columnLetter + i][rowNumber + i] == color) break;
                availableMoves[kNum++] = new Tuple(columnLetter + i, rowNumber + i);
                if (squareStatus[columnLetter + i][rowNumber + i] != 2) break;
            }

            // up left
            for (int i = 1; columnLetter - i >= 0 && rowNumber + i < 8; i++) {
                if (squareStatus[columnLetter - i][rowNumber + i] == color) break;
                availableMoves[kNum++] = new Tuple(columnLetter - i, rowNumber + i);
                if (squareStatus[columnLetter - i][rowNumber + i] != 2) break;
            }

            // down right
            for (int i = 1; columnLetter + i < 8 && rowNumber - i >= 0; i++) {
                if (squareStatus[columnLetter + i][rowNumber - i] == color) break;
                availableMoves[kNum++] = new Tuple(columnLetter + i, rowNumber - i);
                if (squareStatus[columnLetter + i][rowNumber - i] != 2) break;
            }

            // down left
            for (int i = 1; columnLetter - i >= 0 && rowNumber - i >= 0; i++) {
                if (squareStatus[columnLetter - i][rowNumber - i] == color) break;
                availableMoves[kNum++] = new Tuple(columnLetter - i, rowNumber - i);
                if (squareStatus[columnLetter - i][rowNumber - i] != 2) break;
            }
        }

        if (type.equals("knight")) {
            //1 0
            //
            if (rowNumber < 6){
                if (columnLetter > 0  && squareStatus[columnLetter - 1][rowNumber + 2] != color){
                    availableMoves[kNum] = new Tuple(columnLetter - 1, rowNumber + 2);
                    kNum++;
                }
                //0 1
                //
                if (columnLetter < 7 && squareStatus[columnLetter + 1][rowNumber + 2] != color){
                    availableMoves[kNum] = new Tuple(columnLetter + 1, rowNumber + 2);
                    kNum++;
                }
            }






            //  1
            //  0
            if (columnLetter < 6){
                if (rowNumber < 7 && squareStatus[columnLetter + 2][rowNumber + 1] != color){
                    availableMoves[kNum] = new Tuple(columnLetter + 2, rowNumber + 1);
                    kNum++;
                }
                //  0
                //  1
                if (rowNumber > 0 && squareStatus[columnLetter + 2][rowNumber - 1] != color){
                    availableMoves[kNum] = new Tuple(columnLetter + 2, rowNumber - 1);
                    kNum++;
                }
            }





            //
            //0 1
            if (rowNumber > 1){
                if (columnLetter < 7  && squareStatus[columnLetter + 1][rowNumber - 2] != color){
                    availableMoves[kNum] = new Tuple(columnLetter + 1, rowNumber - 2);
                    kNum++;
                }
                //
                //1 0
                if (columnLetter > 0 && squareStatus[columnLetter - 1][rowNumber - 2] != color){
                    availableMoves[kNum] = new Tuple(columnLetter - 1, rowNumber - 2);
                    kNum++;
                }
            }


            //1
            //0
            if (columnLetter > 1){
                if (rowNumber < 7 && squareStatus[columnLetter - 2][rowNumber + 1] != color){
                    availableMoves[kNum] = new Tuple(columnLetter - 2, rowNumber + 1);
                    kNum++;
                }
                //0
                //1
                if (rowNumber > 0 && squareStatus[columnLetter - 2][rowNumber - 1] != color){
                    availableMoves[kNum] = new Tuple(columnLetter - 2, rowNumber - 1);
                    kNum++;
                }
            }
        }

        if (type.equals("king")) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (i == 1 && j == 1) {
                        continue;
                    }
                    if (columnLetter - 1 + i >= 0 && columnLetter - 1 + i < 8) {
                        if (rowNumber - 1 + j >= 0 && rowNumber - 1 + j < 8) {
                            if (squareStatus[columnLetter - 1 + i][rowNumber - 1 + j] != color) {
                                availableMoves[kNum] = new Tuple(columnLetter - 1 + i, rowNumber - 1 + j);
                                kNum++;
                            }
                        }
                    }
                }
            }

            //castling
            //white
            if(color==0){
                if(canCastle[0] || canCastle[1]){
                    //long
                    if(canCastle[0]){
                        if(squareStatus[0][0] == 0 && squareStatus[1][0] == 2 && squareStatus[2][0] == 2 && squareStatus[3][0] == 2){
                            availableMoves[kNum] = new Tuple(2, 0);
                            kNum++;
                            castlingMove = true;
                        }
                    }
                    //short
                    if(canCastle[1]){
                        if(squareStatus[7][0] == 0 && squareStatus[6][0] == 2 && squareStatus[5][0] == 2){
                            availableMoves[kNum] = new Tuple(6, 0);
                            castlingMove = true;
                        }
                    }
                }
            } else {
                //black
                if(canCastle[1] || canCastle[2]){
                    //long
                    if(canCastle[2]){
                        if(squareStatus[0][7] == 1 && squareStatus[1][7] == 2 && squareStatus[2][7] == 2 && squareStatus[3][7] == 2){
                            availableMoves[kNum] = new Tuple(2, 7);
                            kNum++;
                            castlingMove = true;
                        }
                    }
                    //short
                    if(canCastle[3]){
                        if(squareStatus[7][7] == 1 && squareStatus[6][7] == 2 && squareStatus[5][7] == 2){
                            availableMoves[kNum] = new Tuple(6, 7);
                            castlingMove = true;
                        }
                    }
                }
            }
        }


        for (int i = 0; i<27; i++){
            if (availableMoves[i] == null){
                break;
            }
            //System.out.println(availableMoves[i].x + " " + availableMoves[i].y);
        }
    }
    public void getAllTeamPieces(int color){
        int kNum = 0;
        for(int i = 0; i<16; i++){
            tempPieceLocations[i] = null;
        }
        if(color == 0){
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    if(squareStatus[i][j] == 0){
                        tempPieceLocations[kNum] = new Moves.Tuple(i,j);
                        kNum++;
                    }
                }
            }
        } else {
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    if(squareStatus[i][j] == 1){
                        tempPieceLocations[kNum] = new Moves.Tuple(i,j);
                        kNum++;
                    }
                }
            }
        }
    }
}
