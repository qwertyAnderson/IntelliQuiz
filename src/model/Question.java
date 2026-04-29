package model;

public class Question {

    private int id;
    private String questionText;

    private String option1;
    private String option2;
    private String option3;
    private String option4;

    // 1–4 (matches DB correct_answer)
    private int correctAnswerIndex;

    private int difficulty;

    // Default constructor
    public Question() {
    }

    // Full constructor (JDBC use)
    public Question(int id, String questionText,
                    String option1, String option2,
                    String option3, String option4,
                    int correctAnswerIndex, int difficulty) {

        this.id = id;
        this.questionText = questionText;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswerIndex = correctAnswerIndex;
        this.difficulty = difficulty;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public int getDifficulty() {
        return difficulty;
    }

    // Utility: check answer
    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctAnswerIndex;
    }

    // Convert to array for UI (IMPORTANT for your QuizUI)
    public String[] getOptions() {
        return new String[]{option1, option2, option3, option4};
    }

    @Override
    public String toString() {
        return questionText + " | Difficulty: " + difficulty;
    }
}