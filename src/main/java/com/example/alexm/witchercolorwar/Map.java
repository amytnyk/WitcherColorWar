package com.example.alexm.witchercolorwar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alexm on 14.01.2018.
 */

public class Map {
    public List<List<Integer>> map;
    public List<List<Integer>> Strenght;
    public void Generate(int w, int h) {
        map = new ArrayList<>();
        Strenght = new ArrayList<>();
        for (int i = 0;i < h;i++){
            map.add(new ArrayList<Integer>());
            Strenght.add(new ArrayList<Integer>());
            for (int j = 0;j < w;j++){
                map.get(i).add((j % 2 == 0) ? 3 : 1);
                Strenght.get(i).add(1);
            }
        }
        Strenght.get(4).set(3, 20);
    }
}
