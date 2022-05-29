package com.example.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class test1 implements Serializable {
    public ArrayList<String> right_answers = new ArrayList<>();
    public ArrayList<String> qestions = new ArrayList<>();
    public HashMap<Integer ,ArrayList<String>> answers = new HashMap<>();

    public test1(ArrayList<String> right_answers, ArrayList<String> qestions, HashMap<Integer ,ArrayList<String>> answers) {
        this.right_answers = right_answers;
        this.qestions = qestions;
        this.answers = answers;
    }


    public ArrayList<String> getRight_answers() {
        return right_answers;
    }

    public ArrayList<String>  getQestions() {
        return qestions;
    }

    public HashMap<Integer ,ArrayList<String>> getAnswers() {
        return answers;
    }


    public void setRight_answer(ArrayList<String> right_answer) {
        this.right_answers = right_answer;
    }

    public void setQestions(ArrayList<String>  qestions) {
        this.qestions = qestions;
    }

    public void setAnswers(HashMap<Integer ,ArrayList<String>> answers) {
        this.answers = answers;
    }

}
