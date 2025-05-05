package View;

import javax.security.auth.callback.ConfirmationCallback;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class StartView extends JPanel{
    private Image startBackground, characterBackground;

    private JButton startButton, confirmButton;
    private JButton squareBtn, pentagonBtn, hexagonBtn;

    private JComboBox<String> playerCountBox;
    private Map<String, JButton> horseButtons;

    private String selectedBoard = null;
    private int playerCount = 2;
    private List<String> selectedColors = new ArrayList<>();

    private Yut yut = new Yut(); // Yut 클래스가 있어야 함

    public StartView() { //생성자
        setLayout(null); //수동으로 버튼 위치를 지정하기 위해 레이아웃 매니저를 쓰지 않겠다는 의미
        loadImages();
        setupInitialView();
    }

    private JButton createButton(String imagePath, int x, int y, int width, int height) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(imagePath).
                getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        button.setBounds(x, y, width, height); //버튼 위치, 크기 지정
        button.setBorderPainted(false); //버튼 테두리 제거
        button.setContentAreaFilled(false); //버튼 내부 배경 색상 채우기 비활성화 (기본 회색 배경으로 채워질 수도 있음)
        button.setFocusPainted(false); //포커스 표시 그리지 않게 하기 (버튼 클릭 후 생기는 이상한 외곽선 없애기)
        button.setOpaque(false); //버튼을 투명하게 만들기 (배경과 잘 어울리게 하기 위해)
        return button;
    }

    private void setupInitialView() { //start 버튼만 보이게 초기 세팅 후, 버튼을 누르면 나머지 설정 UI를 보여줄 수 있도록 준비
        startButton = createButton("image/.png", 160, 275, 175, 45);
        add(startButton);

        squareBtn = createButton("image/사각형.png", );
        pentagonBtn = createButton("image/오각형.png", );
        hexagonBtn = createButton("image/육각형.png", );
        hideButtons(squareBtn, pentagonBtn, hexagonBtn);

        playerCountBox = new JComboBox<>(new String[]{"2","3", "4"});
        playerCountBox.setBounds(160, 300, 100, 30);
        playerCountBox.setVisible(false);
        add(playerCountBox);

        horseButtons = new HashMap<>(); //key-value
        addHorseButton("red", 50);
        addHorseButton("blue", 130);
        addHorseButton("yellow", 210);
        addHorseButton("green", 290);

        confirmButton = createButton("image/.png", );
        confirmButton.setVisible(false);
        add(confirmButton);
    }

    private void addHorseButton(String color, int x) {
        JButton btn = createButton("image/" + color + ".png", x, 350, 60, 60);
        btn.setVisible(false);
        horseButtons.put(color, btn);
        add(btn);
    }

    private void hideButtons(JButton... buttons) {
        for (JButton btn : buttons) {
            btn.setVisible(false);
            add(btn);
        }
    }

    private void loadImages() {
        startBackground = new ImageIcon("image/.png").getImage();
        characterBackground = new ImageIcon("image/.png").getImage();
        yut.loadImages("image/yut/");
    }

    //JPanel이 화면에 그려질 때 호출되는 메서드
    //이 메서드 안에서 배경 이미지를 그림
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(startBackground, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(characterBackground, 0, 0, getWidth(), getHeight(), null);
    }

    //MVC 분리의 핵심 포인트
    //View는 ActionListener를 직접 구현하지 X -> Controller에서 만든 리스너 객체를 매개변수로 받아서 버튼에 연결
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void addConfirmButtonListener(ActionListener listener) {
        confirmButton.addActionListener(listener);
    }

    public void showSettings() { //시작 버튼 클릭하면 나머지 UI가 나타나도록 하는 메서드
        startButton.setVisible(false);
        squareBtn.setVisible(true);
        pentagonBtn.setVisible(true);
        hexagonBtn.setVisible(true);
        playerCountBox.setVisible(true);
        confirmButton.setVisible(true);

        for (JButton btn : horseButtons.values()) {
            btn.setVisible(true);
        }
    }

    public String getSelectedBoard() {
        return selectedBoard;
    }

    public int getPlayerCount() {
        return Integer.parseInt((String) playerCountBox.getSelectedItem());
    }

    public List<String> getSelectedColors() {
        return selectedColors;
    }

    public void setBoardSelectionListeners(ActionListener square, ActionListener pentagon, ActionListener hexagon) {
        squareBtn.addActionListener(square);
        pentagonBtn.addActionListener(pentagon);
        hexagonBtn.addActionListener(hexagon);
    }

    public void setHorseSelectionListener(ActionListener listener) {
        for (JButton btn : horseButtons.values()) {
            btn.addActionListener(listener);
        }
    }

    public void selectBoard(String boardType) {
        this.selectedBoard = boardType;
    }

    public void toggleHorseSelection(String color) {
        if (selectedColors.contains(color)) {
            selectedColors.remove(color);  // 선택 해제
        } else if (selectedColors.size() < getPlayerCount()) {
            selectedColors.add(color);     // 선택 추가
        }
    }
}