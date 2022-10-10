package model;

public enum Team {
    WHITE, BLACK;

    @Override
    public String toString() {
        String teamName = "";
        switch (this) {
            case WHITE:
                teamName = "White";
                break;
            case BLACK:
                teamName = "Black";
                break;
        }
        return teamName;
    }
}
