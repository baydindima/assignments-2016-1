package ru.spbau.mit;

import java.io.*;

public class StringSetImpl implements StringSet, StreamSerializable {
    private Node root = new Node();
    private int count = 0;

    @Override
    public boolean add(String element) {
        if (!root.contains(element)) {
            count += 1;
            root.add(element);
            return true;
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
            root.remove(element);
            return true;
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

    @Override
    public void serialize(OutputStream out) {
        DataOutputStream stream = new DataOutputStream(out);
        try {
            stream.writeInt(count);
            root.serialize(out);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public void deserialize(InputStream in) {
        DataInputStream stream = new DataInputStream(in);
        try {
            count = stream.readInt();
            root.deserialize(in);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    private static final class Node implements StreamSerializable {
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

        public void add(String str) {
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
        }

        public void remove(String str) {
            Node curNode = this;
            curNode.count -= 1;

            for (int offset = 0; offset < str.length(); offset++) {
                int charIndex = getIndexByChar(str.charAt(offset));
                Node nextNode = curNode.nodes[charIndex];

                nextNode.count -= 1;
                if (nextNode.count == 0) {
                    curNode.nodes[charIndex] = null;
                    return;
                }

                curNode = nextNode;
            }

            curNode.wordEnd = false;
        }

        public int howManyStartsWithPrefix(String prefix) {
            Node curNode = getNodeByString(prefix);
            if (curNode == null) {
                return 0;
            }
            return curNode.count;
        }

        private int getNotNullNodeCount() {
            int count = 0;
            for (Node node : nodes) {
                if (node != null) {
                    count += 1;
                }
            }
            return count;
        }

        @Override
        public void serialize(OutputStream out) {
            DataOutputStream stream = new DataOutputStream(out);
            try {
                stream.writeInt(count);
                stream.writeBoolean(wordEnd);

                stream.writeInt(getNotNullNodeCount());

                for (int i = 0; i < nodes.length; i++) {
                    Node node = nodes[i];
                    if (node != null) {
                        stream.writeInt(i);
                        node.serialize(out);
                    }
                }
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        }


        @Override
        public void deserialize(InputStream in) {
            DataInputStream stream = new DataInputStream(in);
            try {
                count = stream.readInt();
                wordEnd = stream.readBoolean();

                int nodeCount = stream.readInt();
                for (int i = 0; i < nodeCount; i++) {
                    int charIndex = stream.readInt();
                    Node node = new Node();
                    nodes[charIndex] = node;
                    node.deserialize(in);
                }
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        }
    }
}
