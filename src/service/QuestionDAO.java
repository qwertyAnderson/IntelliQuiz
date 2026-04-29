package service;

import model.Question;
import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    public static final int BEGINNER = 1;
    public static final int INTERMEDIATE = 2;
    public static final int EXPERT = 3;

    private Connection con;

    public QuestionDAO() {
        this.con = DBConnection.getConnection();
    }

    // GET QUESTIONS BY DIFFICULTY (JDBC VERSION)
    public List<Question> getQuestionsByDifficulty(int difficulty) {

        List<Question> questions = new ArrayList<>();

        try {
            String sql = "SELECT * FROM questions WHERE difficulty = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, difficulty);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Question q = new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getInt("correct_answer"),
                        rs.getInt("difficulty")
                );

                questions.add(q);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }

    // OPTIONAL: ADD QUESTION (future admin feature)
    public void addQuestion(Question q) {

        try {
            String sql = "INSERT INTO questions (question, option1, option2, option3, option4, correct_answer, difficulty) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, q.getQuestionText());
            ps.setString(2, q.getOption1());
            ps.setString(3, q.getOption2());
            ps.setString(4, q.getOption3());
            ps.setString(5, q.getOption4());
            ps.setInt(6, q.getCorrectAnswerIndex());
            ps.setInt(7, q.getDifficulty());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}