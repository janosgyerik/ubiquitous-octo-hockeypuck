package com.janosgyerik.utils.algorithm.unionfind;

import org.assertj.core.util.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;

public class UnionFindImpl implements UnionFind {

    private final Map<Integer, Integer> ids = new HashMap<>();

    @Override
    public void union(int p, int q) {
        add(p);
        add(q);
        ids.put(root(p), root(q));
    }

    private void add(int p) {
        if (!ids.containsKey(p)) {
            ids.put(p, p);
        }
    }

    private int root(int p) {
        int root = p;
        while (root != ids.get(root)) {
            int parent = ids.get(ids.get(root));
            ids.put(root, parent);
            root = parent;
        }
        return root;
    }

    @Override
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    @Override
    public int find(int p) {
        return ids.get(p);
    }

    @VisibleForTesting
    Map<Integer, Integer> getIds() {
        return ids;
    }
}
