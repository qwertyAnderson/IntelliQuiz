package ui;

import javax.swing.*;
import service.QuestionDAO;
import model.Question;

import java.awt.*;
import java.util.List;
import java.util.Collections;

public class QuizUI {

    JFrame frame;

    QuestionDAO qb = new QuestionDAO();

    List<Question> beginner;
    List<Question> intermediate;
    List<Question> expert;

    int bIndex = 0, iIndex = 0, eIndex = 0;

    int timeLeft = 10;
    Timer timer;
    JLabel timerLabel;

    int score = 0;

    // 🔥 Adaptive system
    double skill = 1.0;
    int level = 1;

    int correctAnswers = 0;
    int wrongAnswers = 0;

    int beginnerCorrect = 0;
    int intermediateCorrect = 0;
    int expertCorrect = 0;

    int beginnerTotal = 0;
    int intermediateTotal = 0;
    int expertTotal = 0;

    JLabel feedbackLabel;

    Color bgMain = new Color(245, 240, 255);
    Color bgCard = new Color(230, 220, 255);
    Color primary = new Color(120, 90, 200);
    Color dark = new Color(70, 50, 140);
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

    // 🔥 LEVEL LOGIC
    public void updateLevel() {
        if (skill < 1.5) level = 1;
        else if (skill < 2.3) level = 2;
        else level = 3;
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

    public void showTemporaryMessage(String msg, Color color) {
        feedbackLabel.setForeground(color);
        feedbackLabel.setText(msg);

        Timer t = new Timer(900, e -> feedbackLabel.setText(" "));
        t.setRepeats(false);
        t.start();
    }

    public void showQuestion(Question q) {

        if (timer != null) timer.stop();

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(bgMain);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bgCard);

        JLabel levelLabel = new JLabel("Level: " + level + " | Score: " + score);
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        levelLabel.setForeground(dark);

        timerLabel = new JLabel("Time Left: 10");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        timerLabel.setForeground(dark);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));

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

        // CENTER
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(bgMain);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(bgMain);

        JLabel questionLabel = new JLabel(
                "<html><div style='text-align:center; width:100%;'>" + q.getQuestionText() + "</div></html>"
        );

        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        questionLabel.setForeground(dark);
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);

        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        centerPanel.add(questionLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(feedbackLabel);

        centerWrapper.add(centerPanel);
        frame.add(centerWrapper, BorderLayout.CENTER);

        // OPTIONS
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 30, 30));

        String[] options = q.getOptions();

        for (int i = 0; i < options.length; i++) {

            int selectedIndex = i;

            JButton btn = new JButton(options[i]);
            styleButton(btn);
            btn.setPreferredSize(new Dimension(650, 90));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 26));

            btn.addActionListener(e -> {

                if (level == 1) beginnerTotal++;
                else if (level == 2) intermediateTotal++;
                else expertTotal++;

                if (timer != null) timer.stop();

                boolean isCorrect = (selectedIndex == q.getCorrectAnswerIndex() - 1);

                if (isCorrect) {

                    score++;
                    correctAnswers++;

                    if (level == 1) beginnerCorrect++;
                    else if (level == 2) intermediateCorrect++;
                    else expertCorrect++;

                    skill += 0.10;
                    showTemporaryMessage("Correct!", correctColor);

                } else {

                    wrongAnswers++;
                    skill -= 0.07;
                    showTemporaryMessage("Wrong!", wrongColor);
                }

                skill = Math.max(0.5, Math.min(3.0, skill));

                int prev = level;
                updateLevel();

                if (level > prev) {
                    if (level == 2)
                        showTemporaryMessage("🚀 Moved to INTERMEDIATE", dark);
                    else if (level == 3)
                        showTemporaryMessage("🔥 Moved to EXPERT", dark);
                }

                Timer t = new Timer(900, ev -> {
                    ((Timer) ev.getSource()).stop();
                    showQuestion(getNextQuestion());
                });

                t.setRepeats(false);
                t.start();
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

        if (timer != null) timer.stop();

        timer = new Timer(1000, e -> {

            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft);

            if (timeLeft <= 0) {

                timer.stop();
                wrongAnswers++;
                skill -= 0.05;

                showTemporaryMessage("Time's up!", wrongColor);

                Timer t = new Timer(900, ev -> {
                    ((Timer) ev.getSource()).stop();
                    showQuestion(getNextQuestion());
                });

                t.setRepeats(false);
                t.start();
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
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        frame.add(title, BorderLayout.NORTH);

        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setBackground(bgMain);

        double bAcc = beginnerTotal == 0 ? 0 : (beginnerCorrect * 100.0 / beginnerTotal);
        double iAcc = intermediateTotal == 0 ? 0 : (intermediateCorrect * 100.0 / intermediateTotal);
        double eAcc = expertTotal == 0 ? 0 : (expertCorrect * 100.0 / expertTotal);

        JLabel scoreLabel = new JLabel("Final Score: " + score);
        JLabel skillLabel = new JLabel("Final Skill: " + String.format("%.2f", skill) + " / 3.00");

        JLabel bLabel = new JLabel("Beginner Accuracy: " + String.format("%.1f", bAcc) + "%");
        JLabel iLabel = new JLabel("Intermediate Accuracy: " + String.format("%.1f", iAcc) + "%");
        JLabel eLabel = new JLabel("Expert Accuracy: " + String.format("%.1f", eAcc) + "%");

        JLabel feedbackTitle = new JLabel("Learning Feedback:");
        feedbackTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        feedbackTitle.setForeground(dark);
        feedbackTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel feedback = new JLabel("<html><div style='text-align:center;'>"
                + (bAcc < 50 ? "• Improve basics<br>" : "• Strong basics<br>")
                + (iAcc < 50 ? "• Work on intermediate concepts<br>" : "• Good intermediate level<br>")
                + (eAcc < 50 ? "• Practice advanced topics<br>" : "• Excellent expert performance<br>")
                + "</div></html>");

        feedback.setHorizontalAlignment(SwingConstants.CENTER);

        feedback.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        feedback.setForeground(dark);
        feedback.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel[] labels = {scoreLabel, skillLabel, bLabel, iLabel, eLabel};

        for (JLabel l : labels) {
            l.setFont(new Font("Segoe UI", Font.BOLD, 24));
            l.setForeground(dark);
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        reportPanel.add(Box.createVerticalStrut(20));
        reportPanel.add(scoreLabel);
        reportPanel.add(Box.createVerticalStrut(10));
        reportPanel.add(skillLabel);

        reportPanel.add(Box.createVerticalStrut(20));
        reportPanel.add(bLabel);
        reportPanel.add(iLabel);
        reportPanel.add(eLabel);

        reportPanel.add(Box.createVerticalStrut(25));
        reportPanel.add(feedbackTitle);
        reportPanel.add(Box.createVerticalStrut(10));
        reportPanel.add(feedback);

        frame.add(reportPanel, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    public void styleButton(JButton btn) {
        btn.setBackground(primary);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    public static void main(String[] args) {
        new QuizUI();
    }
}