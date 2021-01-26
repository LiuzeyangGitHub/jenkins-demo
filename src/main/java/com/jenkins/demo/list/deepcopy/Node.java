package com.jenkins.demo.list.deepcopy;

public class Node {
    int val;
    Node next;
    Node random;
    String tag;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }

    public Node(int val, String tag) {
        this.val = val;
        this.tag = tag;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getRandom() {
        return random;
    }

    public void setRandom(Node random) {
        this.random = random;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}