package ru.spbau.mit;

public class StringSetImpl implements StringSet {
    private Node root = new Node();
    private int count = 0;

    @Override
    public boolean add(String element) {
        if (!root.contains(element)) {
            count += 1;
            return root.add(element);
        }
        return false;
    }

    @Override
    public boolean contains(String element) {
        return root.contains(element);
    }

    @Override
    public boolean remove(String element) {
        if (root.contains(element)) {
            count -= 1;
            return root.remove(element);
        }
        return false;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        return root.howManyStartsWithPrefix(prefix);
    }

    private static final class Node {
        private static final int ALPHABET_LENGTH = 'Z' - 'A' + 1 + 'z' - 'a' + 1;
        private int count;
        private boolean wordEnd;
        private Node[] nodes = new Node[ALPHABET_LENGTH];

        private static int getIndexByChar(char value) {
            if (Character.isLowerCase(value)) {
                return value - 'a';
            }
            return (value - 'A') + 'z' - 'a' + 1;
        }

        private boolean isNodeExist(int index) {
            return nodes[index] != null;
        }

        private Node getNodeByString(String str) {
            Node curNode = this;

            for (int offset = 0; offset < str.length(); offset++) {
                int charIndex = getIndexByChar(str.charAt(offset));

                if (curNode.isNodeExist(charIndex)) {
                    curNode = curNode.nodes[charIndex];
                } else {
                    return null;
                }
            }
            return curNode;
        }


        public boolean contains(String str) {
            Node curNode = getNodeByString(str);
            return curNode != null && curNode.wordEnd;
        }

        public boolean add(String str) {
            Node curNode = this;
            curNode.count += 1;

            for (int offset = 0; offset < str.length(); offset++) {
                int charIndex = getIndexByChar(str.charAt(offset));

                if (!curNode.isNodeExist(charIndex)) {
                    curNode.nodes[charIndex] = new Node();
                }

                curNode = curNode.nodes[charIndex];
                curNode.count += 1;
            }

            curNode.wordEnd = true;
            return true;
        }

        public boolean remove(String str) {
            Node curNode = this;
            curNode.count -= 1;

            for (int offset = 0; offset < str.length(); offset++) {
                int charIndex = getIndexByChar(str.charAt(offset));
                Node nextNode = curNode.nodes[charIndex];

                nextNode.count -= 1;
                if (nextNode.count == 0) {
                    curNode.nodes[charIndex] = null;
                    return true;
                }

                curNode = nextNode;
            }

            curNode.wordEnd = false;
            return true;
        }

        public int howManyStartsWithPrefix(String prefix) {
            Node curNode = getNodeByString(prefix);
            if (curNode == null) {
                return 0;
            }
            return curNode.count;
        }
    }
}
