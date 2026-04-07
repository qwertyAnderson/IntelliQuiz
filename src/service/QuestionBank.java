package service;

import model.Question;
import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    public static final int BEGINNER = 1;
    public static final int INTERMEDIATE = 2;
    public static final int EXPERT = 3;

    private final List<Question> questions;

    public QuestionBank() {
        questions = new ArrayList<>();
        loadSampleQuestions();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestionsByDifficulty(int difficulty) {

        List<Question> filtered = new ArrayList<>();

        for (Question q : questions) {
            if (q.getDifficulty() == difficulty) {
                filtered.add(q);
            }
        }

        return filtered;
    }

    private void loadSampleQuestions() {

        // ================= BEGINNER =================
        addQuestion(new Question("Java is ___ language",
                new String[]{"Compiled", "Interpreted", "Both", "None"}, 2, BEGINNER));

        addQuestion(new Question("Which symbol ends a statement?",
                new String[]{".", ";", ":", ","}, 1, BEGINNER));

        addQuestion(new Question("Size of int in Java?",
                new String[]{"2 bytes", "4 bytes", "8 bytes", "Depends"}, 1, BEGINNER));

        addQuestion(new Question("Default value of boolean?",
                new String[]{"true", "false", "0", "null"}, 1, BEGINNER));

        addQuestion(new Question("Which keyword creates object?",
                new String[]{"create", "new", "make", "class"}, 1, BEGINNER));

        addQuestion(new Question("Entry point of Java program?",
                new String[]{"start()", "main()", "run()", "init()"}, 1, BEGINNER));

        addQuestion(new Question("Which is not primitive?",
                new String[]{"int", "float", "String", "char"}, 2, BEGINNER));

        addQuestion(new Question("Print method?",
                new String[]{"System.print", "Console.log", "System.out.println", "echo"}, 2, BEGINNER));

        addQuestion(new Question("Java file extension?",
                new String[]{".js", ".java", ".class", ".txt"}, 1, BEGINNER));

        addQuestion(new Question("Operator for addition?",
                new String[]{"+", "-", "*", "/"}, 0, BEGINNER));


        // ================= INTERMEDIATE =================
        addQuestion(new Question("Inheritance keyword?",
                new String[]{"this", "super", "extends", "implements"}, 2, INTERMEDIATE));

        addQuestion(new Question("Which is interface keyword?",
                new String[]{"class", "interface", "abstract", "implements"}, 1, INTERMEDIATE));

        addQuestion(new Question("Collection without duplicates?",
                new String[]{"List", "Set", "Map", "Array"}, 1, INTERMEDIATE));

        addQuestion(new Question("Method overloading is ___ polymorphism",
                new String[]{"Compile-time", "Run-time", "Dynamic", "Static"}, 0, INTERMEDIATE));

        addQuestion(new Question("Thread class method to start?",
                new String[]{"run()", "start()", "execute()", "init()"}, 1, INTERMEDIATE));

        addQuestion(new Question("Which is unchecked exception?",
                new String[]{"IOException", "SQLException", "NullPointerException", "FileNotFound"}, 2, INTERMEDIATE));

        addQuestion(new Question("JVM stands for?",
                new String[]{"Java Variable Machine", "Java Virtual Machine", "Joint VM", "None"}, 1, INTERMEDIATE));

        addQuestion(new Question("Which collection is synchronized?",
                new String[]{"ArrayList", "Vector", "HashSet", "TreeSet"}, 1, INTERMEDIATE));

        addQuestion(new Question("Keyword for abstraction?",
                new String[]{"abstract", "interface", "extends", "final"}, 0, INTERMEDIATE));

        addQuestion(new Question("Which map allows null key?",
                new String[]{"HashMap", "Hashtable", "TreeMap", "LinkedHashMap"}, 0, INTERMEDIATE));


        // ================= EXPERT =================
        addQuestion(new Question("Time complexity of HashMap get() average?",
                new String[]{"O(1)", "O(n)", "O(log n)", "O(n log n)"}, 0, EXPERT));

        addQuestion(new Question("Garbage collection happens in?",
                new String[]{"Heap", "Stack", "Method Area", "Register"}, 0, EXPERT));

        addQuestion(new Question("Which is marker interface?",
                new String[]{"Serializable", "Runnable", "Comparable", "CloneableThread"}, 0, EXPERT));

        addQuestion(new Question("Functional interface annotation?",
                new String[]{"@FunctionalInterface", "@Override", "@Lambda", "@Func"}, 0, EXPERT));

        addQuestion(new Question("Stream API introduced in?",
                new String[]{"Java 5", "Java 7", "Java 8", "Java 11"}, 2, EXPERT));

        addQuestion(new Question("Concurrent collection?",
                new String[]{"ArrayList", "CopyOnWriteArrayList", "Vector", "LinkedList"}, 1, EXPERT));

        addQuestion(new Question("Which sorting is stable?",
                new String[]{"QuickSort", "HeapSort", "MergeSort", "SelectionSort"}, 2, EXPERT));

        addQuestion(new Question("JIT compiler does?",
                new String[]{"Interpret", "Compile bytecode", "Optimize runtime", "Debug"}, 2, EXPERT));

        addQuestion(new Question("Lambda expressions implement?",
                new String[]{"Class", "Functional Interface", "Abstract Class", "Thread"}, 1, EXPERT));

        addQuestion(new Question("Optional class introduced in?",
                new String[]{"Java 6", "Java 7", "Java 8", "Java 9"}, 2, EXPERT));
    }
}