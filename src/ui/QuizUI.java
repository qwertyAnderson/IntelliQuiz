package ui;

import javax.swing.*;
import service.QuestionBank;
import model.Question;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Collections;

public class QuizUI {

    JFrame frame;

    QuestionBank qb = new QuestionBank();

    List<Question> beginner;
    List<Question> intermediate;
    List<Question> expert;

    int bIndex = 0, iIndex = 0, eIndex = 0;

    int timeLeft = 10;
    Timer timer;
    JLabel timerLabel;

    int score = 0;

    int level = 1;
    int streak = 0;
    int levelScore = 0;
    int levelQuestions = 0;

    public QuizUI() {


        frame = new JFrame("IntelliQuiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(20, 20));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        //  HEADER
        JLabel title = new JLabel("Welcome to IntelliQuiz", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        frame.add(title, BorderLayout.NORTH);

        //  CENTER BUTTON
        JPanel centerPanel = new JPanel(new GridBagLayout());

        JButton startBtn = new JButton("Start Quiz");
        startBtn.setFont(new Font("Arial", Font.BOLD, 18));
        startBtn.setPreferredSize(new Dimension(200, 60));
        startBtn.setFocusPainted(false);
        startBtn.setBackground(new Color(70, 130, 180));
        startBtn.setForeground(Color.WHITE);

        centerPanel.add(startBtn);
        frame.add(centerPanel, BorderLayout.CENTER);

        //  Load questions
        beginner = qb.getQuestionsByDifficulty(1);
        intermediate = qb.getQuestionsByDifficulty(2);
        expert = qb.getQuestionsByDifficulty(3);

        Collections.shuffle(beginner);
        Collections.shuffle(intermediate);
        Collections.shuffle(expert);

        //  Button click
        startBtn.addActionListener(e -> showQuestion(getNextQuestion()));

        frame.setVisible(true);
    }

    //  Get next question
    public Question getNextQuestion() {

        if (level == 1) {
            if (bIndex >= beginner.size()) {
                Collections.shuffle(beginner);
                bIndex = 0;
            }
            return beginner.get(bIndex++);
        }

        else if (level == 2) {
            if (iIndex >= intermediate.size()) {
                Collections.shuffle(intermediate);
                iIndex = 0;
            }
            return intermediate.get(iIndex++);
        }

        else {
            if (eIndex >= expert.size()) {
                Collections.shuffle(expert);
                eIndex = 0;
            }
            return expert.get(eIndex++);
        }
    }

    //  Show question UI
    public void showQuestion(Question q) {

        if (timer != null) timer.stop();

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout(20, 20));

        //  TOP PANEL
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel levelLabel = new JLabel("Level: " + level + " | Score: " + score);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));

        timerLabel = new JLabel("Time Left: 10");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));

        topPanel.add(levelLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);

        //  QUESTION
        JLabel questionLabel = new JLabel(q.getQuestionText(), SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 28));

        frame.add(questionLabel, BorderLayout.CENTER);

        //  OPTIONS
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        optionsPanel.setPreferredSize(new Dimension(700, 300));

        String[] options = q.getOptions();

        for (int i = 0; i < options.length; i++) {

            int selectedIndex = i;

            JButton btn = new JButton(options[i]);
            btn.setFont(new Font("Arial", Font.BOLD, 25));
            btn.setFocusPainted(false);

            btn.addActionListener(e -> {

                timer.stop();

                if (selectedIndex == q.getCorrectAnswerIndex()) {
                    score++;
                    streak++;
                    levelScore++;
                    JOptionPane.showMessageDialog(frame, "Correct!");
                } else {
                    streak = 0;
                    JOptionPane.showMessageDialog(frame, "Wrong!");
                }

                levelQuestions++;

                //  LEVEL LOGIC
                if (level == 1 && streak >= 5) {
                    level = 2;
                    resetStats();
                    JOptionPane.showMessageDialog(frame, "Moved to INTERMEDIATE");
                }

                else if (level == 2 && levelQuestions >= 10) {

                    if (levelScore <= 2) {
                        level = 1;
                        JOptionPane.showMessageDialog(frame, "Dropped to BEGINNER");
                    }

                    else if (streak >= 5) {
                        level = 3;
                        JOptionPane.showMessageDialog(frame, "Moved to EXPERT");
                    }

                    else {
                        JOptionPane.showMessageDialog(frame, "Stay in INTERMEDIATE");
                    }

                    resetStats();
                }

                else if (level == 3 && levelQuestions >= 5) {

                    if (streak >= 5) {
                        JOptionPane.showMessageDialog(frame,
                                "Quiz Completed!\nFinal Score: " + score);
                        return;
                    }

                    else if (levelScore <= 2) {
                        level = 1;
                        JOptionPane.showMessageDialog(frame, "Dropped to BEGINNER");
                    }

                    else {
                        level = 2;
                        JOptionPane.showMessageDialog(frame, "Back to INTERMEDIATE");
                    }

                    resetStats();
                }

                showQuestion(getNextQuestion());
            });

            optionsPanel.add(btn);
        }

        wrapper.add(optionsPanel);
        frame.add(wrapper, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();

        startTimer();
    }

    //  TIMER
    public void startTimer() {

        timeLeft = 10;

        timer = new Timer(1000, e -> {

            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft);

            if (timeLeft <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(frame, "Time's up!");

                levelQuestions++;
                streak = 0;

                showQuestion(getNextQuestion());
            }
        });

        timer.start();
    }

    //  RESET
    public void resetStats() {
        levelQuestions = 0;
        levelScore = 0;
        streak = 0;
    }

    public static void main(String[] args) {
        new QuizUI();
    }
}