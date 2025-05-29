package View.Interface;

public interface IEndView {
    void setWinner(int playerId);
    void addRestartButtonListener(Object listener);
    void addExitButtonListener(Object listener);
}