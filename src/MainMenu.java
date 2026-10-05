import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel{
    private GameFrame gameFrame;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;

    private Image titleImage;
    private int logoWidth = 400;
    private int titleY = 80;

    public MainMenu(GameFrame gameFrame){
        this.gameFrame = gameFrame;
        setLayout(null);
        setBackground(Color.BLACK);

        int btnWidth = 350;
        int btnHeight = 65;
        int centerX = (boardWidth / 2) - (btnWidth / 2);

        titleImage = new ImageIcon(getClass().getResource("/Assets/logo.png")).getImage();
        ImageIcon singleNormal = createScaledIcon("/Buttons/singlePlayer.png", btnWidth, btnHeight);
        ImageIcon singleHover = createScaledIcon("/Buttons/singlePlayerSelected.png", btnWidth, btnHeight);
        ImageIcon multiNormal = createScaledIcon("/Buttons/multiPlayer.png", btnWidth, btnHeight);
        ImageIcon multiHover = createScaledIcon("/Buttons/multiPlayerSelected.png", btnWidth, btnHeight);
        ImageIcon quitNormal = createScaledIcon("/Buttons/quit.png", btnWidth, btnHeight);
        ImageIcon quitHover = createScaledIcon("/Buttons/quitSelected.png", btnWidth, btnHeight);

        Dimension buttonSize = new Dimension(1000, 50);

        JButton singlePlayerButton = new JButton(singleNormal);
        singlePlayerButton.setBorderPainted(false);
        singlePlayerButton.setContentAreaFilled(false);
        singlePlayerButton.setFocusPainted(false);
        singlePlayerButton.setFocusable(false);
        singlePlayerButton.setOpaque(false);
        singlePlayerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        singlePlayerButton.setPreferredSize(buttonSize);

        if(singleHover != null){
            singlePlayerButton.setRolloverIcon(singleHover);
        }

        singlePlayerButton.setBounds(centerX, 270, btnWidth, btnHeight);
        add(singlePlayerButton);

        singlePlayerButton.addActionListener(e -> {
            gameFrame.startGame(false);
        });

        JButton multiPlayerButton = new JButton(multiNormal);
        multiPlayerButton.setBorderPainted(false);
        multiPlayerButton.setContentAreaFilled(false);
        multiPlayerButton.setFocusPainted(false);
        multiPlayerButton.setFocusable(false);
        multiPlayerButton.setOpaque(false);
        multiPlayerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        multiPlayerButton.setPreferredSize(buttonSize);

        if(multiHover != null){
            multiPlayerButton.setRolloverIcon(multiHover);
        }

        multiPlayerButton.setBounds(centerX, 370, btnWidth, btnHeight);
        add(multiPlayerButton);

        multiPlayerButton.addActionListener(e -> {
            gameFrame.startGame(true);
        });

        JButton quitButton = new JButton(quitNormal);
        quitButton.setBorderPainted(false);
        quitButton.setContentAreaFilled(false);
        quitButton.setFocusPainted(false);
        quitButton.setFocusable(false);
        quitButton.setOpaque(false);
        quitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quitButton.setPreferredSize(buttonSize);

        if(quitHover != null){
            quitButton.setRolloverIcon(quitHover);
        }

        quitButton.setBounds(centerX, 470, btnWidth, btnHeight);
        add(quitButton);

        quitButton.addActionListener(e -> {
            System.exit(0);
        });
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        if(titleImage != null){
            int titleX = (boardWidth / 2) - (logoWidth / 2);
            g.drawImage(titleImage, titleX, titleY, this);
        }
    }

    private ImageIcon createScaledIcon(String path, int width, int height){
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if(imgURL != null){
                Image rawImg = new ImageIcon(imgURL).getImage();
                Image scaledImg = rawImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }
        }catch(Exception e){
            System.err.println("Could not load asset: " + path);
        }
        return null;
    }
}