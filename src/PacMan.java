import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

import net.java.games.input.*;
import net.java.games.input.Component;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class PacMan extends JPanel implements ActionListener, KeyListener{
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;

        char direction = 'R';
        int velocityX = 0;
        int velocityY = 0;

        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction){
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velocityX;
            this.y += this.velocityY;

            for(Block wall : walls){
                if(collision(this, wall)){
                    this.x -= this.velocityX;
                    this.y -= this.velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity(){
            if(this.direction == 'U'){
                this.velocityX = 0;
                this.velocityY = -tileSize / 4;
            }else if(this.direction == 'D'){
                this.velocityX = 0;
                this.velocityY = tileSize/4;
            }else if(this.direction == 'L'){
                this.velocityX = -tileSize/4;
                this.velocityY = 0;
            }else if(this.direction == 'R'){
                this.velocityX = tileSize/4;
                this.velocityY = 0;
            }
        }

        void reset(){
            this.x = this.startX;
            this.y = this.startY;
        }
    }
    private GameFrame gameFrame;

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image blueGhost;
    private Image redGhost;
    private Image orangeGhost;
    private Image pinkGhost;
    private Image cherryImage;
    private Image scaredGhost;

    //easter egg
    private boolean secret = false;
    private Image khloe;
    private Image kim;
    private Image kourtney;
    private Image kylie;
    private Image background;
    private Image secretBackground;

    private Image player1Image;
    private Image player2Image;
    private Block player1;
    private Block player2;
    int player1Lives = 3;
    int player2Lives = 3;
    boolean player1Dead = false;
    boolean player2Dead = false;
    boolean player1Invincible = false;
    boolean player2Invincible = false;

    private boolean paused = false;
    private boolean powerUp = false;
    private boolean gameStarted = false;

    public String[] tileMap;
    public String[] multiMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X        c        X",
        "X XX X XXXXX X XX X",
        "X    X   c   X    X",
        "XXXX XXXXDXXXX XXXX",
        "OOOX X  ABC  X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X  1  c  2  X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    public String[] singleMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X        c        X",
        "X XX X XXXXX X XX X",
        "X    X   c   X    X",
        "XXXX XXXXDXXXX XXXX",
        "OOOX X  ABC  X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     1     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    HashSet<Block> players;
    HashSet<Block> cherries;

    Timer gameLoop;
    Timer powerUpTimer;

    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();

    int score = 0;
    int foodScore = 10;
    int level = 1;
    boolean gameOver = false;

    private JButton easterEgg;

    private Controller gamepad;
    private EventQueue eventQueue;
    private Event event = new Event();

    private boolean controllerConnected = false;

    PacMan(GameFrame gameFrame){
        this.gameFrame = gameFrame;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        //load Images
        wallImage = new ImageIcon(getClass().getResource("/Assets/wall.png")).getImage();
        blueGhost = new ImageIcon(getClass().getResource("/Assets/blueGhost.png")).getImage();
        redGhost = new ImageIcon(getClass().getResource("/Assets/redGhost.png")).getImage();
        orangeGhost = new ImageIcon(getClass().getResource("/Assets/orangeGhost.png")).getImage();
        pinkGhost = new ImageIcon(getClass().getResource("/Assets/pinkGhost.png")).getImage();
        cherryImage = new ImageIcon(getClass().getResource("/Assets/cherry.png")).getImage();
        scaredGhost = new ImageIcon(getClass().getResource("/Assets/scaredGhost.png")).getImage();

        player1Image = new ImageIcon(getClass().getResource("/Assets/pacman1.png")).getImage();
        player2Image = new ImageIcon(getClass().getResource("/Assets/pacman2.png")).getImage();

        //easter egg
        khloe = new ImageIcon(getClass().getResource("/Assets/khloe.gif")).getImage();
        kim = new ImageIcon(getClass().getResource("/Assets/kim.gif")).getImage();
        kourtney = new ImageIcon(getClass().getResource("/Assets/kourtney.gif")).getImage();
        kylie = new ImageIcon(getClass().getResource("/Assets/kylie.gif")).getImage();
        secretBackground = new ImageIcon(getClass().getResource("/Assets/krissed.gif")).getImage();

        easterEgg = new JButton(new ImageIcon(wallImage));
        easterEgg.setBounds((boardWidth - tileSize), (boardHeight - tileSize), tileSize, tileSize);
        easterEgg.setBorderPainted(false);
        easterEgg.setContentAreaFilled(false);
        easterEgg.setFocusPainted(false);
        easterEgg.setFocusable(false);
        easterEgg.setOpaque(false);
        easterEgg.setCursor(new Cursor(Cursor.HAND_CURSOR));

        easterEgg.addActionListener(e -> {
            System.out.println("Secret Activated!");
            secret = true;
            foodScore = 100;
            background = secretBackground;
            repaint();
            loadMap();
            resetPositions();
            requestFocusInWindow();
        });

        setLayout(null);
        add(easterEgg);
        tileMap = singleMap;

        loadMap();
        setupController();

        for(Block ghost : ghosts){
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }

        gameLoop = new Timer(50, this);
    }

    public void loadMap(){
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        players = new HashSet<Block>();
        cherries = new HashSet<Block>();

        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                if(!secret){
                    if (tileMapChar == 'X') {
                        Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                        walls.add(wall);
                    }else if(tileMapChar == 'b'){
                        Block ghost = new Block(blueGhost, x, y, tileSize, tileSize);
                        ghosts.add(ghost);
                    }else if(tileMapChar == 'o'){
                        Block ghost = new Block(orangeGhost, x, y, tileSize, tileSize);
                        ghosts.add(ghost);
                    }else if(tileMapChar == 'p'){
                        Block ghost = new Block(pinkGhost, x, y, tileSize, tileSize);
                        ghosts.add(ghost);
                    }else if(tileMapChar == 'r'){
                        Block ghost = new Block(redGhost, x, y, tileSize, tileSize);
                        ghosts.add(ghost);
                    }else if(tileMapChar == '1'){
                        player1 = new Block(player1Image, x, y, tileSize, tileSize);
                        players.add(player1);
                    }else if(tileMapChar == '2'){
                        player2 = new Block(player2Image, x, y, tileSize, tileSize);
                        players.add(player2);
                    }else if(tileMapChar == ' '|| tileMapChar == 'A' || tileMapChar == 'B' || tileMapChar == 'C' || tileMapChar == 'D'){
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                    }else if(tileMapChar == 'c'){
                        Block cherry = new Block(cherryImage, x, y, tileSize, tileSize);
                        cherries.add(cherry);
                    }
                }else{
                    if(tileMapChar == 'X'){
                        Block wall = new Block(wallImage, x, y, tileSize, tileSize);
                        walls.add(wall);
                    }else if(tileMapChar == '1'){
                        player1 = new Block(player1Image, x, y, tileSize, tileSize);
                        players.add(player1);
                    }else if(tileMapChar == '2'){
                        player2 = new Block(player2Image, x, y, tileSize, tileSize);
                        players.add(player2);
                    }else if(tileMapChar == ' '){
                        Block food = new Block(null, x + 14, y + 14, 4, 4);
                        foods.add(food);
                    }else if(tileMapChar == 'A'){
                        Block ghost = new Block(khloe, x, y, tileSize, tileSize);
                        ghosts.add(ghost);
                    }else if(tileMapChar == 'B'){
                        Block ghost = new Block(kim, x, y, tileSize, tileSize);
                        ghosts.add(ghost);
                    }else if(tileMapChar == 'C'){
                        Block ghost = new Block(kourtney, x, y, tileSize, tileSize);
                        ghosts.add(ghost);
                    }else if(tileMapChar == 'D'){
                        Block ghost = new Block(kylie, x, y, tileSize, tileSize);
                        ghosts.add(ghost);
                    }else if(tileMapChar == 'c'){
                        Block cherry = new Block(cherryImage, x, y, tileSize, tileSize);
                        cherries.add(cherry);
                    }
               }
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(background != null){
            g.drawImage(background, 0, 0, boardWidth, boardHeight, null);
        }

        draw(g);
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        for(Block player : players){
            if(player == player1 && player1Dead){
                continue;
            }
            if(player == player2 && player2Dead){
                continue;
            }

            double rotation = 0;

            if(player.direction == 'R'){
                rotation = 0;
            }else if(player.direction == 'D'){
                rotation = Math.PI / 2;
            }else if(player.direction == 'L'){
                rotation = Math.PI;
            }else if(player.direction == 'U'){
                rotation = 3 * Math.PI / 2;
            }

            g2.rotate(rotation, player.x + player.width / 2.0, player.y + player.height / 2.0);
            g2.drawImage(player.image, player.x, player.y, player.width, player.height, null);
            g2.rotate(-rotation, player.x + player.width / 2.0, player.y + player.height / 2.0);
        }
        
        if(!powerUp){
            for(Block ghost : ghosts){
                g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
            }
        }else{
            for(Block ghost : ghosts){
                g.drawImage(scaredGhost, ghost.x, ghost.y, ghost.width, ghost.height, null);
            }
        }

        for(Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        for(Block cherry : cherries){
            g.drawImage(cherry.image, cherry.x, cherry.y, cherry.width, cherry.height, null);
        }

        g.setColor(Color.white);
        
        for(Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        g.setFont(new Font("Emulogic", Font.PLAIN, 13));
        if(tileMap == singleMap){
            g.drawString("Lives: " + String.valueOf(player1Lives) + ", Score: " + String.valueOf(score) + ", Level " + String.valueOf(level), tileSize/2, tileSize/2);
        }else{
            g.drawString("P1 Lives: " + String.valueOf(player1Lives) + ", P2 Lives: " + String.valueOf(player2Lives) +", Score: " + String.valueOf(score) + ", Level " + String.valueOf(level), tileSize/2, tileSize/2);
        }
    }

    public void move(){
        for(Block player : players){
            if(player == null){
                continue;
            }

            if(player == player1 && player1Dead){
                continue;
            }

            if(player == player2 && player2Dead){
                continue;
            }
            player.x += player.velocityX;
            player.y += player.velocityY;
        }

        for(Block wall : walls){
            for(Block player : players){
                if(collision(player, wall)){
                    player.x -= player.velocityX;
                    player.y -= player.velocityY;
                    break;
                }

                if(player.x + tileSize < 0){
                    player.x = boardWidth;
                }else if(player.x > boardWidth){
                    player.x = 0 - tileSize;
                    break;
                }
            }
        }

        HashSet<Block> ghostsToRemove = new HashSet<>();
        for(Block ghost : ghosts){
            if(!powerUp){
                if(!player1Dead && !player1Invincible && collision(ghost, player1)){
                    player1Lives--;

                    if(player1Lives <= 0){
                        player1Dead = true;
                        player1.velocityX = 0;
                        player1.velocityY = 0;
                    }else{
                        resetPlayer(player1);
                        giveGracePeriod(player1);
                    }
                }
            
                if(!player2Dead && !player2Invincible && collision(ghost, player2)){
                    player2Lives--;
                
                    if(player2Lives <= 0){
                        player2Dead = true;
                        player2.velocityX = 0;
                        player2.velocityY = 0;
                    }else{
                        resetPlayer(player2);
                        giveGracePeriod(player2);
                    }
                }
            }else{
                boolean ghostEaten = false;

                if(!player1Dead && collision(player1, ghost)){
                    score += 100;
                    ghostEaten = true;
                }
            
                if(!player2Dead && collision(player2, ghost)){
                    score += 100;
                    ghostEaten = true;
                }
            
                if(ghostEaten){
                    ghostsToRemove.add(ghost);
                    respawnGhost(ghost);
                    continue;
                }
            }

            if(ghost.y == tileSize * 9 && ghost.direction != 'U' && ghost.direction != 'D'){
                ghost.updateDirection('U');
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            for(Block wall : walls){
                if(collision(ghost, wall) || ghost.x <= 0 || ghost.x + ghost.width >= boardWidth){
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    char newDirection = directions[random.nextInt(4)];
                    ghost.updateDirection(newDirection);
                }
            }
        }
        ghosts.removeAll(ghostsToRemove);

        HashSet<Block> cherriesEaten = new HashSet<>();
        for(Block cherry : cherries){
            if(!player1Dead && collision(player1, cherry)){
                cherriesEaten.add(cherry);
                activatePowerUp();
            }

            if(!player2Dead && collision(player2, cherry)){
                cherriesEaten.add(cherry);
                activatePowerUp();
            }
        }
        cherries.removeAll(cherriesEaten);

        Block foodEaten = null;
        for(Block food : foods){
            for(Block player : players){
                if(collision(player, food)){
                    foodEaten = food;
                    score += foodScore;
                }
            }
        }
        foods.remove(foodEaten);

        if(foods.isEmpty()){
            loadMap();
            resetPositions();
            level++;
            player1Dead = false;
            player2Dead = false;
            if(player1Lives < 3){
                player1Lives++;
            }
            if(player2Lives < 3){
                player2Lives++;
            }
            
        }

        if(player1Dead && player2Dead){
            gameFrame.showGameOver(score);
            return;
        }

        if(tileMap == singleMap && player1Dead){
            gameFrame.showGameOver(score);
            return;
        }
    }

    public boolean collision(Block a, Block b){
        if (a == null || b == null) {
            return false;
        }

        return  a.x < b.x + b.width && 
                a.x + a.width > b.x && 
                a.y < b.y + b.height && 
                a.y + a.height > b.y;
    }

    public void resetPositions(){
        for(Block player : players){
            player.reset();
            player.velocityX = 0;
            player.velocityY = 0;
        }

        for(Block ghost : ghosts){
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    public void resetPlayer(Block player){
        player.reset();

        player.velocityX = 0;
        player.velocityY = 0;
    }

    public void giveGracePeriod(Block player){
        if(player == player1){
            player1Invincible = true;

            Timer timer = new Timer(2000, e ->{
                player1Invincible = false;
            });

            timer.setRepeats(false);
            timer.start();
        }else if(player == player2){
            player2Invincible = true;

            Timer timer = new Timer(2000, e ->{
                player2Invincible = false;
            });

            timer.setRepeats(false);
            timer.start();
        }
    }

    public void activatePowerUp(){
        powerUp = true;

        if(powerUpTimer != null){
            powerUpTimer.stop();
        }

        powerUpTimer = new Timer(10000, e ->{
            powerUp = false;
            repaint();
        });

        powerUpTimer.setRepeats(false);
        powerUpTimer.start();
    }

    public void respawnGhost(Block ghost){
        Timer timer = new Timer(5000, e ->{
            ghost.reset();
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);

            ghosts.add(ghost);
            repaint();
        });

        timer.setRepeats(false);
        timer.start();
    }

    public void stopGame(){
        gameLoop.stop();
    }

    public void resetGame(){
        gameLoop.stop();

        loadMap();
        resetPositions();

        player1Lives = 3;
        player2Lives = 3;
        player1Dead = false;
        player2Dead = false;
        player1Invincible = false;
        player2Invincible = false;

        score = 0;
        level = 1;

        if(powerUpTimer != null){
            powerUpTimer.stop();
            powerUpTimer = null;
        }

        gameOver = false;
        paused = false;
        gameStarted = true;

        gameLoop.start();
        requestFocusInWindow();
    }

    public void actionPerformed(ActionEvent e){
        controllerControlls();
        move();
        repaint();

        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_R){
            loadMap();
            resetPositions();
            powerUp = false;
            player1Lives = 3;
            player2Lives = 3;
            player1Dead = false;
            player2Dead = false;
            player1Invincible = false;
            player2Invincible = false;
            score = 0;
            level = 1;
            gameOver = false;
            gameLoop.start();
            return;
        }

        if(!player1Dead){
            if(e.getKeyCode() == KeyEvent.VK_W){
                player1.updateDirection('U');
            }else if(e.getKeyCode() == KeyEvent.VK_S){
                player1.updateDirection('D');
            }else if(e.getKeyCode() == KeyEvent.VK_A){
                player1.updateDirection('L');
            }else if(e.getKeyCode() == KeyEvent.VK_D){
                player1.updateDirection('R');
            }
        }

        if(!player1Dead && tileMap == singleMap){
            if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP){
                player1.updateDirection('U');
            }else if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN){
                player1.updateDirection('D');
            }else if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT){
                player1.updateDirection('L');
            }else if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT){
                player1.updateDirection('R');
            }
        }

        if(!player2Dead){
            if(e.getKeyCode() == KeyEvent.VK_UP){
                player2.updateDirection('U');
            }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                player2.updateDirection('D');
            }else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                player2.updateDirection('L');
            }else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                player2.updateDirection('R');
            }
        }
        
        if(e.getKeyCode() == KeyEvent.VK_P){
            paused = !paused;
            if(paused){
                gameLoop.stop();
            }else{
                gameLoop.start();
            }
        }

        if(!gameStarted){
            gameStarted = true;
            gameLoop.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public void setupController(){
        java.util.logging.Logger.getLogger("net.java.games.input").setLevel(java.util.logging.Level.OFF);

        DirectInputEnvironmentPlugin directInput = new DirectInputEnvironmentPlugin();

        Controller[] devices = directInput.getControllers();

        for(Controller device : devices){
            if(device.getType() == Controller.Type.GAMEPAD || device.getType() == Controller.Type.STICK){
                gamepad = device;
                eventQueue = gamepad.getEventQueue();

                controllerConnected = true;

                System.out.println("Gamepad found: " + gamepad.getName());
                break;
            }
        }

        if(!controllerConnected){
            System.out.println("No gamepad found.");
        }
    }

    public void controllerControlls(){
        if(!controllerConnected || gamepad == null){
            return;
        }

        gamepad.poll();
        while(eventQueue.getNextEvent(event)){
            Component component = event.getComponent();
            float value = event.getValue();
            if(component.getIdentifier() == Component.Identifier.Axis.POV){
                if(value == 0.25f){
                    if(!player1Dead){
                        player1.updateDirection('U');
                    }
                }else if(value == 0.50f){
                    if(!player1Dead){
                        player1.updateDirection('R');
                    }
                }else if(value == 0.75f){
                    if(!player1Dead){
                        player1.updateDirection('D');
                    }
                }else if(value == 1.0f){
                    if(!player1Dead){
                        player1.updateDirection('L');
                    }
                }
            }
        }
    }
}