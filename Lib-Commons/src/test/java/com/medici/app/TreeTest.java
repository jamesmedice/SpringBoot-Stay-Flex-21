package com.medici.app;

import java.util.*;

public class TreeTest {

    public static Tree builtTree(Map<String, Integer> mappingTree) {

        Map<Integer, List<String>> treeAux = new HashMap<>();

        for (Map.Entry<String, Integer> tree : mappingTree.entrySet()) {

            treeAux.putIfAbsent(tree.getValue(), new LinkedList<>());
            treeAux.get(tree.getValue()).add(tree.getKey());
        }

        List<Node> nodes = new ArrayList<>();

        for (Map.Entry<Integer, List<String>> tree : treeAux.entrySet()) {

            Node node = null;

            int index = 1;

            for (String name : tree.getValue()) {
                node = new Node();
                node.setName(name);
                node.setLength(tree.getKey());

                char textChar = '0';

                if (index % 2 == 0)
                    textChar = '1';

                StringBuilder text = new StringBuilder();
                String nodeVal = text.repeat('1', tree.getKey().intValue() - 1).append(textChar).toString();
                node.setValue(nodeVal);

                nodes.add(node);

                index++;
            }

        }

        Tree mein = new Tree();
        mein.setNodes(nodes);

        return mein;
    }

    public static void main(String[] args) {
        Map<String, Integer> mappingTree = new LinkedHashMap<>();
        mappingTree.put("a", 3);
        mappingTree.put("c", 3);
        mappingTree.put("b", 3);
        mappingTree.put("d", 4);
        mappingTree.put("e", 5);
        mappingTree.put("g", 6);
        mappingTree.put("f", 6);

        Tree tree = builtTree(mappingTree);
        tree.getNodes().stream().forEach(t -> System.out.println(t.toString()));

    }

}

class Tree {
    private List<Node> nodes;

    public List<Node> getNodes() {
        if (nodes == null)
            nodes = new LinkedList<>();

        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

}


class Node {

    private String name;
    private int length;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", value='" + value + '\'' +
                '}';
    }
}

/**
 *
 * node {a:3, c:3, b:3, d:4, e:5, g:6, f:6 }
 *
 */