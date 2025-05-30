package View.Swing;

import Controller.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class StartView extends JPanel{
    private Image startBackground, horseSelectionBackground, boardSelectionBackground;

    private JButton startButton, nextButton;
    private JButton squareBtn, pentagonBtn, hexagonBtn;

    private JComboBox<String> playerCountBox, horseCountBox;
    private Map<String, JButton> horseButtons;

    private String selectedBoard = null;
    private List<String> selectedColors = new ArrayList<>();

    private Map<String, Point> horseButtonPositions = new HashMap<>();

    private GameState currentState = GameState.START_SCREEN;

    public StartView() { //생성자
        setLayout(null); //수동으로 버튼 위치를 지정하기 위해 레이아웃 매니저를 쓰지 않겠다는 의미
        loadImages();
        setupInitialView();
    }

    //필요한 이미지 불러오기
    private void loadImages() {
        startBackground = new ImageIcon(getClass().getResource("/image/시작 화면.png")).getImage(); //시작 배경화면
        horseSelectionBackground = new ImageIcon(getClass().getResource("/image/말 선택.png")).getImage(); //말 고를 때 배경화면
        boardSelectionBackground = new ImageIcon(getClass().getResource("/image/판 선택.png")).getImage();
    }

    //버튼 생성 메서드
    private JButton createButton(String imagePath, int x, int y) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/" + imagePath));
        JButton button = new JButton(icon);
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        button.setBounds(x, y, width, height); //버튼 위치, 크기 지정
        button.setBorderPainted(false); //버튼 테두리 제거
        button.setContentAreaFilled(false); //버튼 내부 배경 색상 채우기 비활성화 (기본 회색 배경으로 채워질 수도 있음)
        button.setFocusPainted(false); //포커스 표시 그리지 않게 하기 (버튼 클릭 후 생기는 이상한 외곽선 없애기)
        button.setOpaque(false); //버튼을 투명하게 만들기 (배경과 잘 어울리게 하기 위해)

        return button;
    }

    //게임 시작 화면 세팅 (game start 버튼만 화면에 보이고 나머지는 가려진 상태)
    private void setupInitialView() { //start 버튼만 보이게 초기 세팅 후, 버튼을 누르면 나머지 설정 UI를 보여줄 수 있도록 준비
        startButton = createButton("image/시작 버튼.png", 386, 420);
        add(startButton);

        squareBtn = createButton("image/사각형.png", 50, 217);
        pentagonBtn = createButton("image/오각형.png", 396, 217);
        hexagonBtn = createButton("image/육각형.png", 742, 217);
        add(squareBtn);
        add(pentagonBtn);
        add(hexagonBtn);

        playerCountBox = new JComboBox<>(new String[]{"2","3", "4"});
        playerCountBox.setBounds(200, 265, 280, 71);
        playerCountBox.setVisible(false);
        add(playerCountBox);

        horseCountBox = new JComboBox<>(new String[]{"2", "3", "4", "5"});
        horseCountBox.setBounds(621, 265, 280, 71);
        horseCountBox.setVisible(false);
        add(horseCountBox);

        horseButtons = new HashMap<>(); //key-value 형태로 저장 -> 색깔을 key로 사용해 각 말에 대응하는 버튼을 쉽게 찾을 수 있음
        addHorseButton("red", 148);
        addHorseButton("blue", 363);
        addHorseButton("yellow", 578);
        addHorseButton("green", 793);

        nextButton = createButton("image/다음.png", 432, 575);
        nextButton.setVisible(false);
        add(nextButton);
    }

    //말 버튼
    private void addHorseButton(String color, int x) {
        JButton btn = createButton("image/" + color + " 말.png", x, 401);
        btn.setActionCommand(color);
        btn.setVisible(false);
        horseButtons.put(color, btn);
        horseButtonPositions.put(color, new Point(x, 401)); // 위치 저장
        add(btn);
    }

    //JPanel이 화면에 그려질 때 호출되는 메서드
    //이 메서드 안에서 배경 이미지를 그림
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (currentState) {
            case START_SCREEN:
                g.drawImage(startBackground, 0, 0, getWidth(), getHeight(), null);
                break;
            case HORSE_SELECTION:
                g.drawImage(horseSelectionBackground, 0, 0, getWidth(), getHeight(), null);
                break;
            case BOARD_SELECTION:
                g.drawImage(boardSelectionBackground, 0, 0, getWidth(), getHeight(), null);
                break;
            default:
                break;
        }
    }

    public void setState(GameState state) {
        this.currentState = state;

        switch (state) {
            case START_SCREEN:
                showOnly(startButton);
                break;

            case HORSE_SELECTION:
                showOnly(horseButtons.values().toArray(new JButton[0]));
                playerCountBox.setVisible(true);
                horseCountBox.setVisible(true);
                break;

            case BOARD_SELECTION:
                showOnly(squareBtn, pentagonBtn, hexagonBtn, nextButton);
                break;

            default:
                break;
        }
        repaint();
    }

    private void showOnly(JComponent... visibleComponents) {
        Set<JComponent> show = new HashSet<>(Arrays.asList(visibleComponents));

        for (Component comp : getComponents()) {
            comp.setVisible(show.contains(comp));
        }
    }

    //MVC 분리의 핵심 포인트
    //View는 ActionListener를 직접 구현하지 X -> Controller에서 만든 리스너 객체를 매개변수로 받아서 버튼에 연결
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void addNextButtonListener(ActionListener listener) {
        nextButton.addActionListener(listener);
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

    public int getPlayerCount() {
        return Integer.parseInt((String) playerCountBox.getSelectedItem());
    }

    public int getHorseCount() {
        return Integer.parseInt((String) horseCountBox.getSelectedItem());
    }

    public void selectBoard(String boardType) {
        this.selectedBoard = boardType;
    }

    //말을 누르면 선택 한 번 더 누르면 선택 취소
    public void toggleHorseSelection(String color) {
        if (selectedColors.contains(color)) {
            selectedColors.remove(color);  // 선택 해제
        } else if (selectedColors.size() < getPlayerCount()) {
            selectedColors.add(color);     // 선택 추가
        }
    }

    public List<String> getSelectedColors() {
        return selectedColors;
    }

    public String getSelectedBoard() {
        return selectedBoard;
    }

    public Map<String, JButton> getHorseButtons() {
        return horseButtons;
    }

    public void resetSelectionState() {
        selectedColors.clear();       // 말 색상 초기화
        selectedBoard = null;        // 보드 선택 초기화

        // 콤보박스 초기화
        playerCountBox.setSelectedIndex(0);
        horseCountBox.setSelectedIndex(0);

        // 말 버튼 초기화
        for (JButton btn : horseButtons.values()) {
            btn.setEnabled(true);
            btn.setVisible(false); // 시작할 때는 안 보이게
        }

        // 보드 선택 버튼 숨기기
        squareBtn.setVisible(false);
        pentagonBtn.setVisible(false);
        hexagonBtn.setVisible(false);
        nextButton.setVisible(false);

        // 상태를 다시 시작 화면으로
        setState(GameState.START_SCREEN);
    }
}