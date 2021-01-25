package com.jenkins.demo.list.reverse;

import com.jenkins.demo.list.ListNode;

/**
 * 输入: 1->2->3->4->5->NULL, m = 2, n = 4
 * 输出: 1->4->3->2->5->NULL
 * -1  ->  1  ->  2  <->  3  <->  4  ->  5  ->  NULL, m = 2, n = 4
 *       prevM   mNode
 *               nNode
 *                      postN
 *                               next
 *
 *                      nNode    postN
 *                                      next
 *                               nNode  postN    next
 *
 *
 */
public class Solution {
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if (head == null || head.getNext() == null) {
            return head;
        }
        ListNode dummy = new ListNode(-1, head);
        head = dummy;
        for (int i = 1; i < m; i++) {
            head = head.getNext();
        }
        ListNode prevM = head;
        ListNode mNode = head.getNext();
        ListNode nNode = mNode;
        ListNode postN = nNode.getNext();
        for (int i = m; i < n; i++) {
            ListNode next = postN.getNext();
            postN.setNext(nNode);
            nNode = postN;
            postN = next;
        }
        prevM.setNext(nNode);
        mNode.setNext(postN);
        return dummy.getNext();
    }

    /**
     * Java 1ms，时间空间均90+。 实现思路 ：以1->2->3->4->5, m = 2, n=4 为例:
     *
     * 定位到要反转部分的头节点 2，head = 2；前驱结点 1，pre = 1；
     * 当前节点的下一个节点3调整为前驱节点的下一个节点 1->3->2->4->5,
     * 当前结点仍为2， 前驱结点依然是1，重复上一步操作。。。
     * 1->4->3->2->5.
     * @param head
     * @param m
     * @param n
     * @return
     */
    public ListNode reverseBetween2(ListNode head, int m, int n) {
        if (head == null || head.getNext() == null) {
            return head;
        }
        ListNode dummy = new ListNode(-1, head);
        head = dummy;
        ListNode prev =dummy;
        for (int i = 1; i < m; i++) {
            prev = prev.getNext();
        }
        head = prev.getNext();
        for (int i = m; i < n; i++) {
            ListNode next = head.getNext();
            head.setNext(next.getNext());
            next.setNext(prev.getNext());
            prev.setNext(next);
        }

        return dummy.getNext();
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);
        head.setNext(node2);
        node2.setNext(node3);
        node3.setNext(node4);
        node4.setNext(node5);

        Solution solution = new Solution();
        ListNode listNode = solution.reverseBetween2(head, 2, 4);
        while (listNode != null) {
            System.out.println(listNode.getVal());
            listNode = listNode.getNext();
        }
    }
}