package service;

import model.Question;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class QuestionBank {

    public static final int BEGINNER = 1;
    public static final int INTERMEDIATE = 2;
    public static final int EXPERT = 3;

    private static final String FILE_PATH = "C:/MyProjects/Intelliquiz/data/questions.txt";

    private final List<Question> questions;


    public QuestionBank() {
        questions = new ArrayList<>();
        loadFromFile();
        System.out.println("Loaded questions: " + questions.size());
    }

    // Get questions by difficulty
    public List<Question> getQuestionsByDifficulty(int difficulty) {

        List<Question> filtered = new ArrayList<>();

        for (Question q : questions) {
            if (q.getDifficulty() == difficulty) {
                filtered.add(q);
            }
        }

        return filtered;
    }

    public void loadFromFile() {
        questions.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split("\\|");

                if (parts.length < 4) continue; // safety check

                String questionText = parts[0];
                String[] options = parts[1].split(",");
                int correctIndex = Integer.parseInt(parts[2]);
                int difficulty = Integer.parseInt(parts[3]);

                questions.add(new Question(questionText, options, correctIndex, difficulty));
            }

        } catch (IOException e) {
            System.out.println("No file found, starting empty database.");
        }
    }

    public void saveToFile() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Question q : questions) {

                bw.write(q.getQuestionText() + "|" +
                        String.join(",", q.getOptions()) + "|" +
                        q.getCorrectAnswerIndex() + "|" +
                        q.getDifficulty());

                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}