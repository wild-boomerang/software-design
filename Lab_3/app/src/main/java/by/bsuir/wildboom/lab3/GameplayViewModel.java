package by.bsuir.wildboom.lab3;

import androidx.lifecycle.ViewModel;

public class GameplayViewModel extends ViewModel {
    private BattleFieldLogic ourField;
    private BattleFieldLogic enemyField;

    private boolean horizontalOrientation = true;
    private String documentId;
    private GameStatus gameStatus = GameStatus.create;
    private ShipType selectedShipType = ShipType.fourDecker;

    private String connectionId;
    private UserConnect userConnect;
    private Enemy enemy;

    private boolean gameStart = false;

    public GameplayViewModel(Enemy enemy) {
        this.enemy = enemy;
    }

    public BattleFieldLogic getOurField() {
        return ourField;
    }

    public void setOurField(BattleFieldLogic ourField) {
        this.ourField = ourField;
    }

    public BattleFieldLogic getEnemyField() {
        return enemyField;
    }

    public void setEnemyField(BattleFieldLogic enemyField) {
        this.enemyField = enemyField;
    }

    public boolean isHorizontalOrientation() {
        return horizontalOrientation;
    }

    public void setHorizontalOrientation(boolean horizontalOrientation) {
        this.horizontalOrientation = horizontalOrientation;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public ShipType getSelectedShipType() {
        return selectedShipType;
    }

    public void setSelectedShipType(ShipType selectedShipType) {
        this.selectedShipType = selectedShipType;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public UserConnect getUserConnect() {
        return userConnect;
    }

    public void setUserConnect(UserConnect user) {
        this.userConnect = user;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public boolean isGameStart() {
        return gameStart;
    }

    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }
}
