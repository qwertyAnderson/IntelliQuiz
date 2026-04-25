package ui;

import javax.swing.*;
import service.QuestionBank;
import model.Question;

import java.awt.*;
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

    int correctAnswers = 0;
    int wrongAnswers = 0;

    int beginnerCorrect = 0;
    int intermediateCorrect = 0;
    int expertCorrect = 0;

    int beginnerTotal = 0;
    int intermediateTotal = 0;
    int expertTotal = 0;

    JLabel feedbackLabel;

    // COLOR PALETTE
    Color bgMain = new Color(245, 240, 255);
    Color bgCard = new Color(230, 220, 255);
    Color primary = new Color(120, 90, 200);
    Color dark = new Color(70, 50, 140);
    Color accent = new Color(170, 140, 255);
    Color correctColor = new Color(80, 180, 120);
    Color wrongColor = new Color(200, 80, 100);

    public QuizUI() {

        frame = new JFrame("IntelliQuiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(bgMain);

        JLabel title = new JLabel("Welcome to IntelliQuiz", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(dark);
        title.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));

        frame.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(bgMain);

        JButton startBtn = new JButton("Start Quiz");
        startBtn.setFont(new Font("Arial", Font.BOLD, 24));
        styleButton(startBtn);

        startBtn.setPreferredSize(new Dimension(220, 70));

        centerPanel.add(startBtn);
        frame.add(centerPanel, BorderLayout.CENTER);

        beginner = qb.getQuestionsByDifficulty(1);
        intermediate = qb.getQuestionsByDifficulty(2);
        expert = qb.getQuestionsByDifficulty(3);

        Collections.shuffle(beginner);
        Collections.shuffle(intermediate);
        Collections.shuffle(expert);

        startBtn.addActionListener(e -> showQuestion(getNextQuestion()));

        frame.setVisible(true);
    }

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

    public void showQuestion(Question q) {

        if (timer != null) timer.stop();

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(bgMain);

        // 🔝 TOP PANEL
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bgCard);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel levelLabel = new JLabel("Level: " + level + " | Score: " + score);
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        levelLabel.setForeground(dark);

        timerLabel = new JLabel("Time Left: 10");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        timerLabel.setForeground(dark);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        exitBtn.setFocusPainted(false);

        // stop timer and show result
        exitBtn.addActionListener(e -> {
            if (timer != null) timer.stop();
            showResult();
        });

        topPanel.add(levelLabel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.add(timerLabel);
        rightPanel.add(exitBtn);

        topPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);

        //center
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(bgMain);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(bgMain);

        JLabel questionLabel = new JLabel(
                "<html><div style='text-align:center; width:900px;'>" + q.getQuestionText() + "</div></html>"
        );
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        questionLabel.setForeground(dark);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(25));
        centerPanel.add(feedbackLabel);

        centerWrapper.add(centerPanel);
        frame.add(centerWrapper, BorderLayout.CENTER);

        //  OPTIONS
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 60, 100));
        optionsPanel.setBackground(bgMain);

        String[] options = q.getOptions();

        for (int i = 0; i < options.length; i++) {

            int selectedIndex = i;

            JButton btn = new JButton(options[i]);
            styleButton(btn);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
            btn.setPreferredSize(new Dimension(300, 80));

            btn.addActionListener(e -> {
                if (level == 1) beginnerTotal++;
                else if (level == 2) intermediateTotal++;
                else expertTotal++;
                timer.stop();

                if (selectedIndex == q.getCorrectAnswerIndex()) {

                    score++;
                    streak++;
                    levelScore++;
                    correctAnswers++;

                    if (level == 1) beginnerCorrect++;
                    else if (level == 2) intermediateCorrect++;
                    else expertCorrect++;

                    feedbackLabel.setForeground(correctColor);
                    feedbackLabel.setText(" Correct!");

                } else {
                    streak = 0;
                    wrongAnswers++;

                    feedbackLabel.setForeground(wrongColor);
                    feedbackLabel.setText(" Wrong!");
                }

                levelQuestions++;

                if (level == 1 && streak >= 5) {
                    level = 2;
                    resetStats();
                    feedbackLabel.setText(" Moved to INTERMEDIATE");
                }

                else if (level == 2 && levelQuestions >= 10) {

                    if (levelScore <= 2) {
                        level = 1;
                        feedbackLabel.setText(" Dropped to BEGINNER");
                    }
                    else if (streak >= 5) {
                        level = 3;
                        feedbackLabel.setText(" Moved to EXPERT");
                    }
                    else {
                        feedbackLabel.setText(" Stay in INTERMEDIATE");
                    }

                    resetStats();
                }

                else if (level == 3 && levelQuestions >= 5) {
                    showResult();
                    return;
                }

                new Timer(1000, ev -> {
                    ((Timer) ev.getSource()).stop();
                    showQuestion(getNextQuestion());
                }).start();
            });

            optionsPanel.add(btn);
        }

        frame.add(optionsPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();

        startTimer();
    }

    public void startTimer() {

        timeLeft = 10;

        timer = new Timer(1000, e -> {

            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft);

            if (timeLeft <= 0) {
                timer.stop();

                wrongAnswers++;
                streak = 0;
                levelQuestions++;

                if (level == 1) beginnerTotal++;
                else if (level == 2) intermediateTotal++;
                else expertTotal++;

                feedbackLabel.setForeground(dark);
                feedbackLabel.setText(" Time's up!");

                new Timer(1000, ev -> {
                    ((Timer) ev.getSource()).stop();
                    showQuestion(getNextQuestion());
                }).start();
            }
        });

        timer.start();
    }

    public void showResult() {

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(bgMain);

        JLabel title = new JLabel("Quiz Completed!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setForeground(dark);

        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        resultArea.setEditable(false);
        resultArea.setBackground(bgCard);
        resultArea.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        double bAcc = calculateAccuracy(beginnerCorrect, beginnerTotal);
        double iAcc = calculateAccuracy(intermediateCorrect, intermediateTotal);
        double eAcc = calculateAccuracy(expertCorrect, expertTotal);

        String feedback = "";

        if (bAcc < 50) feedback += "• Improve your basics.\n";
        else if (bAcc < 75) feedback += "• Decent basics.\n";
        else feedback += "• Strong basics.\n";

        if (iAcc < 50) feedback += "• Work on core concepts.\n";
        else if (iAcc < 75) feedback += "• Good understanding.\n";
        else feedback += "• Strong intermediate level.\n";

        if (eAcc < 50) feedback += "• Practice advanced topics.\n";
        else if (eAcc < 75) feedback += "• Nearly expert.\n";
        else feedback += "• Excellent expert performance!\n";

        resultArea.setText(
                " QUIZ REPORT \n\n" +
                        "Score: " + score + "\n\n" +

                        "Correct: " + correctAnswers + "\n" +
                        "Wrong: " + wrongAnswers + "\n\n" +

                        "Beginner: " + beginnerCorrect + "/" + beginnerTotal +
                        " (" + String.format("%.1f", bAcc) + "%)\n" +

                        "Intermediate: " + intermediateCorrect + "/" + intermediateTotal +
                        " (" + String.format("%.1f", iAcc) + "%)\n" +

                        "Expert: " + expertCorrect + "/" + expertTotal +
                        " (" + String.format("%.1f", eAcc) + "%)\n\n" +

                        "Feedback:\n" + feedback
        );

        JButton exitBtn = new JButton("Exit");
        styleButton(exitBtn);
        exitBtn.addActionListener(e -> System.exit(0));

        frame.add(title, BorderLayout.NORTH);
        frame.add(resultArea, BorderLayout.CENTER);
        frame.add(exitBtn, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    public void resetStats() {
        levelQuestions = 0;
        levelScore = 0;
        streak = 0;
    }

    public double calculateAccuracy(int correct, int total) {
        if (total == 0) return 0;
        return (correct * 100.0) / total;
    }

    // Button Styling
    public void styleButton(JButton btn) {
        btn.setBackground(primary);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(accent);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(primary);
            }
        });
    }

    public static void main(String[] args) {
        new QuizUI();
    }
}
