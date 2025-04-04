package vidmot;

public enum View {
    Intro("Intro.fxml"),
    Board("Board.fxml"),
    Tol("Tol.fxml");;


    private String fileName;

    View(String fileName){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
