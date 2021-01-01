package by.bsuir.wildboom.lab3;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BattleFieldLogic implements Serializable {
    int size = 10;
    private Integer[][] battleField = new Integer[size][size];
    private ArrayList<Integer> shipsNum = new ArrayList<>();

    public BattleFieldLogic() {
        for (int i = 4; i > 0; i--) {
            shipsNum.add(i);
        }

        for (Integer[] row : battleField)
            Arrays.fill(row, 0);
    }

    public List<Integer> serialize(int index) {
        return Arrays.asList(battleField[index]);
    }

    private ArrayList<Pair<Integer, Integer>> getBorderCellsHorizontal(int deckNum, int i, int j) {
        ArrayList<Pair<Integer, Integer>> borderCoords = new ArrayList<>();
        int[] arr = {-1, 0, 1};

        for (int val : arr) {
            borderCoords.add(new Pair<>(i + val, j - 1));
        }

        for (int val : arr) {
            borderCoords.add(new Pair<>(i + val, j + deckNum));
        }

        for (int k = deckNum - 1; k >= 0; k--) {
            borderCoords.add(new Pair<>(i - 1, j + k));
            borderCoords.add(new Pair<>(i + 1, j + k));
        }

        return borderCoords;
    }

    private boolean bordersCheck(int i, int j) {
        if ((i >= 0 && i < 10) && (j >= 0 && j < 10)) {
            return battleField[i][j] != 1;
        }
        return true;
    }

    private ArrayList<Pair<Integer, Integer>> getBorderCellsVertical(int deckNum, int i, int j) {
        ArrayList<Pair<Integer, Integer>> borderCoords = new ArrayList<>();
        int[] arr = {-1, 0, 1};

        for (int val : arr) {
            borderCoords.add(new Pair<>(i - 1, j + val));
        }

        for (int val : arr) {
            borderCoords.add(new Pair<>(i + deckNum, j + val));
        }

        for (int k = deckNum - 1; k >= 0; k--) {
            borderCoords.add(new Pair<>(i + k, j - 1));
            borderCoords.add(new Pair<>(i + k, j + 1));
        }

        return borderCoords;
    }

    public boolean setShip(int i, int j, ShipType shipType, boolean orientation) {
        ArrayList<Pair<Integer, Integer>> borderCoords;
        ArrayList<Pair<Integer, Integer>> shipCoords;
        int deckNum = 0;

        switch (shipType) {
            case singleDecker:
                deckNum = 1;
                break;
            case doubleDecker:
                deckNum = 2;
                break;
            case threeDecker:
                deckNum = 3;
                break;
            case fourDecker:
                deckNum = 4;
                break;
        }

        shipCoords = getShipCoordinates(i, j, deckNum, orientation);
        if (orientation) {
            borderCoords = getBorderCellsHorizontal(deckNum, i, j);
        } else {
            borderCoords = getBorderCellsVertical(deckNum, i, j);
        }

        boolean isFail = false;
        for (int l = 0; l < borderCoords.size(); l++) {
            if (!bordersCheck(borderCoords.get(l).first, borderCoords.get(l).second)) {
                isFail = true;
                break;
            }
        }

        if (!isFail) {
            if (checkShipPosition(shipCoords) && checkShipNum(deckNum)) {
                for (Pair<Integer, Integer> coord : shipCoords) {
                    battleField[coord.first][coord.second] = 1;
                }
                changeShipNum(deckNum);

                return true;
            }
        }
        return false;
    }

    private ArrayList<Pair<Integer, Integer>> getShipCoordinates(int i, int j, int deckNum, boolean orientation) {
        ArrayList<Pair<Integer, Integer>> shipCoords = new ArrayList<>();

        if (orientation) {
            for (int l = deckNum - 1; l >= 0; l--) {
                shipCoords.add(new Pair<>(i, j + l));
            }
        } else {
            for (int l = deckNum - 1; l >= 0; l--) {
                shipCoords.add(new Pair<>(i + l, j));
            }
        }

        return shipCoords;
    }

    private boolean checkShipPosition(ArrayList<Pair<Integer, Integer>> coords) {
        for (Pair<Integer, Integer> coord : coords) {
            if (!((coord.first >= 0 && coord.first < 10) &&
                    (coord.second >= 0 && coord.second < 10))) {
                return false;
            }
            if (battleField[coord.first][coord.second] == 1) {
                return false;
            }
        }
        return true;
    }

    public String[] getShipTypes() {
        return new String[] {
                "Single-deck", "Double-deck", "Three-decker", "Four-decker"
        };
    }

    private boolean checkShipNum(int deckNum) {
        return shipsNum.get(deckNum - 1) != 0;
    }

    private void changeShipNum(Integer type) {
        shipsNum.set(type - 1, shipsNum.get(type - 1) - 1);
    }

    public boolean isReady() {
        for (int element : shipsNum) {
            if (element != 0) {
                return false;
            }
        }
        return true;
    }

    public int isAttacked(int i, int j) {
        return battleField[i][j];
    }

    public int EnemyAttack(int i, int j) {
        if (battleField[i][j] == 1) {
            battleField[i][j] = 3;
            return 1;
        } else if (battleField[i][j] == 0) {
            battleField[i][j] = 2;
            return 0;
        }
        return 2;
    }

    public boolean isAllShipsKilled() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (battleField[i][j] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setFieldFromMap(Map<String, Object> data) {
        ArrayList<Long> row;
        int value;

        for (int i = 0; i < 10; i++) {
            row = (ArrayList<Long>)data.get("row " + i);
            for (int j = 0; j < 10; j++) {
                value = Objects.requireNonNull(row).get(j).intValue();
                battleField[i][j] = value;
            }
        }
    }

    public Map<String, Object> getFieldAsMap() {
        Map<String, Object> data = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            data.put("row " + i, serialize(i));
        }

        return data;
    }
}
