package com.jenkins.demo.list.deepcopy;


import java.util.HashMap;
import java.util.Map;

/**
 * 深拷贝带有随机指针的列表
 * 解决思路：
 * 构建新老数据关系，这种关系能实现通过老数据拿到新数据
 */
public class Solution {

    /**
     * 实现思路：
     * 1.保存随机指针节点映射关系，通过老节点能拿到对应的新节点，Map<Node,Node>
     * 2.复制列表中的每个节点，结合Map映射关系
     *
     * @param head
     * @return
     */
    public Node copyRandomList(Node head) {
        if (head == null) {
            return head;
        }
        // 保存老的节点和新的节点关系
        Map<Node, Node> map = new HashMap();
        Node newHead = head;
        while (newHead != null) {
            if (!map.containsKey(newHead)) {
                Node copyNode = new Node(newHead.getVal(), "new");
                map.put(newHead, copyNode);
            }
            if (newHead.getRandom() != null) {
                Node random = newHead.getRandom();
                if (!map.containsKey(random)) {
                    Node newRandom = new Node(random.getVal(), "new");
                    map.put(random, newRandom);
                }
                map.get(newHead).setRandom(map.get(random));
            }
            newHead = newHead.getNext();
        }
        newHead = head;
        while (newHead != null) {
            Node next = newHead.getNext();
            // 组装新的数据引用
            map.get(newHead).setNext(map.get(next));
            newHead = newHead.next;
        }

        return map.get(head);
    }

    /**
     * 思路：
     * 1.将老数据和新数据合并为一个链表
     * 2.从合并后的链表中拆分出新数据
     * 1  ->  2  ->  3  ->  4  ->  5
     * 1  ->  1`  ->  2  ->  2`  ->  3  ->  3`  ->  4  ->  4`  ->  5  ->  5`
     *
     * @param head
     * @return
     */
    public Node copyRandomList2(Node head) {
        if (head == null) {
            return head;
        }
        copy(head);
        copyRandom(head);
        Node node = split(head);

        return node;
    }

    /**
     * 拆分数据
     *
     * @param head
     * @return
     */
    private Node split(Node head) {
        Node result = head.getNext();
        Node move = head.getNext();

        while (head != null && head.getNext() != null) {
            head.setNext(head.getNext().getNext());
            head = head.getNext();
            if (move != null && move.getNext() != null) {
                move.setNext(move.getNext().getNext());
                move = move.getNext();
            }
        }
        return result;
    }

    /**
     * 复制随机指针
     *
     * @param head
     */
    private void copyRandom(Node head) {
        Node node = head;
        while (node != null && node.getNext() != null) {
            if (node.getRandom() != null) {
                Node random = node.getRandom();
                node.getNext().setRandom(random.getNext());
            }
            node = node.getNext().getNext();
        }
    }

    /**
     * 复制基础数据，不复制随机指针
     *
     * @param head
     */
    private void copy(Node head) {
        Node node = head;
        while (node != null) {
            Node copy = new Node(node.getVal(), "new");
            copy.setNext(node.getNext());
            node.setNext(copy);
            node = copy.getNext();
        }
    }


    public static void main(String[] args) {
        Node head = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);
        head.setNext(node2);
        node2.setNext(node3);
        node3.setNext(node4);
        node4.setNext(node5);
        node2.setRandom(node4);
        head.setRandom(node5);

        Solution solution = new Solution();
        Node node = solution.copyRandomList2(head);
        while (node != null) {
            Node next = node.getNext();
            Node random = node.getRandom();
            System.out.println(node.getVal() + "->(" + (next != null ? next.getVal() : "") + "," + (random != null ? random.getVal() : "") + ")");
            node = node.getNext();
        }
    }
}