package model;

public class Question {

    private final String questionText;
    private final String[] options;
    private final int correctAnswerIndex;
    private final int difficulty;

    public Question(String questionText, String[] options, int correctAnswerIndex, int difficulty) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.difficulty = difficulty;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options.clone(); // safer
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctAnswerIndex;
    }

    @Override
    public String toString() {
        return questionText + " | Difficulty: " + difficulty;
    }
}