import java.util.jar.*;
import java.net.*;
import java.awt.datatransfer.*;
import javax.imageio.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
public class Sudoku{
    
    MyCanvas mc = new MyCanvas();
    JFrame jf = new JFrame();
    int[][] gridNum = new int[10][10];
    
    int[][][] cand = new int[10][10][10];
    int candW = 15;
    int blockW = 136;
    int blockH = 136;
    int gridW = blockW / 3;
    int gridH = blockH / 3;
    int num = 0;
    int xPos = 0;
    int yPos = 0;
    boolean flag = false;
    
    final Toolkit TLK = Toolkit.getDefaultToolkit();
//    Image sudokuImg = TLK.getImage(Sudoku.class.getClassLoader().getResource("resource/sudoku.png"));
//    Image[] arrowImg = {TLK.getImage(Sudoku.class.getClassLoader().getResource("resource/arrow1.png")),
//                        TLK.getImage(Sudoku.class.getClassLoader().getResource("resource/arrow2.png")),
//                        TLK.getImage(Sudoku.class.getClassLoader().getResource("resource/arrow3.png")),
//                        TLK.getImage(Sudoku.class.getClassLoader().getResource("resource/arrow4.png"))};
//    Image icon = TLK.getImage(Sudoku.class.getClassLoader().getResource("resource/sudokuicon.png"));
    Image sudokuImg = new ImageIcon(Sudoku.class.getClassLoader().getResource("resource/sudoku.png")).getImage();
    Image[] arrowImg = {new ImageIcon(Sudoku.class.getClassLoader().getResource("resource/arrow1.png")).getImage(),
                        new ImageIcon(Sudoku.class.getClassLoader().getResource("resource/arrow2.png")).getImage(),
                        new ImageIcon(Sudoku.class.getClassLoader().getResource("resource/arrow3.png")).getImage(),
                        new ImageIcon(Sudoku.class.getClassLoader().getResource("resource/arrow4.png")).getImage()};
    Image icon = new ImageIcon(Sudoku.class.getClassLoader().getResource("resource/sudokuicon.png")).getImage();
    int[][][] grid={{{2,2},{2,3},{2,4},{3,2},{3,3},{3,4},{4,2},{4,3},{4,4}},
                    {{2,6},{2,7},{2,8},{3,6},{3,7},{3,8},{4,6},{4,7},{4,8}},
                    {{6,2},{6,3},{6,4},{7,2},{7,3},{7,4},{8,2},{8,3},{8,4}},
                    {{6,6},{6,7},{6,8},{7,6},{7,7},{7,8},{8,6},{8,7},{8,8}}};
    
    int [][] gridColor = new int[10][10];
    int [][] numColor = new int[10][10];
    int [][][] candColor = new int[10][10][10];
    
    Mouse mouse = new Mouse();
    Key key = new Key();
    Listener listener = new Listener();
    
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Game");
    String[] itemStr = {"New Game","Clear All","Clear Number","Solution","Finish Input","Candidate","Arrow","Input","Output","Sava to PNG"};
    char[] shortkey = {'N','C','V','S','F','D','A','I','O','P'};
    JMenuItem[] item = new JMenuItem[itemStr.length];
    
    boolean candFlag = false;
    boolean arrowFlag = false;
    
//    int[][][] arrow = { new int[10][9], new int[9][10] };
    int[][][] arrow = {{{0,0,0,0,0,0,0,0,0},
                        {0,2,2,1,2,1,2,1,1},
                        {0,2,2,1,1,2,1,2,1},
                        {0,2,1,2,1,2,2,1,1},
                        {0,1,1,2,2,1,2,1,2},
                        {0,2,1,2,1,2,1,2,1},
                        {0,1,2,1,2,2,1,2,2},
                        {0,2,2,1,2,1,2,1,2},
                        {0,1,2,2,2,1,1,2,2},
                        {0,1,2,1,2,2,1,2,1}},

                       {{0,0,0,0,0,0,0,0,0,0},
                        {0,1,1,1,1,1,2,1,2,2},
                        {0,2,2,1,2,2,1,2,1,1},
                        {0,2,1,1,1,2,1,2,1,2},
                        {0,1,2,2,2,1,2,1,2,1},
                        {0,2,1,2,1,2,2,1,2,2},
                        {0,1,2,2,2,2,1,2,1,1},
                        {0,2,1,1,2,2,2,1,2,1},
                        {0,1,2,2,1,1,2,2,2,2}}};
    
    int[][] gridBlock = new int[10][10];
    Color[] color = {null, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};
    
    URL url = Sudoku.class.getProtectionDomain().getCodeSource().getLocation();    
    File f = new File(URLDecoder.decode(Sudoku.class.getClassLoader().getResource("resource/17sudoku.txt").getPath()));
    
    RandomAccessFile rdf;
    Random rand = new Random();
    File picture;
    BufferedImage bfimg;
    JFileChooser jfc = new JFileChooser();
    
    int c;
        
    Sudoku() {
        jf.setVisible(true);
        jf.setSize(500,550);
        jf.setLocation(250,100);
        jf.add(mc);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jf.getContentPane().setBackground(Color.WHITE);
        jf.setIconImage(icon);
        jf.setTitle("NEC DISCS DWD Sudoku 2012");
        jf.setResizable(false);
        mc.setBackground(Color.WHITE);
        
        for (int i = 0; i < itemStr.length; i++) {
            item[i] = new JMenuItem(itemStr[i]);
            item[i].addActionListener(listener);
            menu.add(item[i]);
            item[i].setAccelerator(KeyStroke.getKeyStroke(shortkey[i],CTRL_DOWN_MASK));
//            item[i].setAccelerator(KeyStroke.getKeyStroke(itemStr[i].charAt(0),CTRL_DOWN_MASK));
            item[i].setMnemonic(itemStr[i].charAt(0));
        }
        menu.setMnemonic('G');
        menuBar.add(menu);
        jf.setJMenuBar(menuBar);
        
        try {
            rdf = new RandomAccessFile(f,"r");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        init();
    }
    
    
    void init(){
        
//        initColor();
//        checkCandidate();
        mc.repaint();
        
        mc.addMouseMotionListener(mouse);
        mc.addMouseListener(mouse);
        jf.addKeyListener(key);
        
        
    }
    
    void newgame(){
        
        byte[] b = new byte[81];
        
        try {
            rdf.seek(83*rand.nextInt(47793));
            for (int i = 0; i < b.length; i++) {
                b[i] = rdf.readByte();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = new String(b);
        for (int i = 1, n=0; i < gridNum.length; i++) {
            for (int j = 1; j < gridNum[i].length; j++) {
                gridNum[i][j] = Integer.parseInt(String.valueOf(s.charAt(n++)));
            }
        }
        
    }
    
    void input(){
        String input = JOptionPane.showInputDialog(jf,"Example: 000040001090700000000000000406300500000200090100000000000061080270000400000000000");
        
        for (int i = 1, n=0; i < gridNum.length; i++) {
            for (int j = 1; j < gridNum[i].length; j++) {
                gridNum[i][j] = Integer.parseInt(String.valueOf(input.charAt(n++)));
            }
        }
    }
    
    void output(){
        String s = "";
        for (int i = 1; i < gridNum.length; i++) {
            for (int j = 1; j < gridNum[i].length; j++) {
                s += gridNum[i][j];
            }
        }
        
        StringSelection ss = new StringSelection(s);
        Clipboard clipboard = TLK.getSystemClipboard();
        clipboard.setContents(ss, null);
    }
    
    void clearNum(){
        for (int i = 1; i < gridNum.length; i++) {
            for (int j = 1; j < gridNum[i].length; j++) {
                if(numColor[i][j] != 1)
                    gridNum[i][j] = 0;
            }
        }
    }
    
    void savePicture(){
        int result = jfc.showSaveDialog(jf);
        if(result == JFileChooser.APPROVE_OPTION){
            String path = jfc.getSelectedFile().getAbsolutePath();
            File f = new File(path+".png");
            if(f.exists())
                f.delete();
            bfimg = new BufferedImage(500,500,BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D)bfimg.getGraphics();
            g2.setBackground(Color.WHITE);
            g2.clearRect(0, 0, 500, 500);
            mc.paint(g2);
            try {
                ImageIO.write(bfimg,"PNG",f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    class Listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==item[0]){             //New Game
                newgame();
                initNumColor();
                checkCandidate();
                jf.repaint();
            }else if(e.getSource()==item[1]){       //Clear All
                candFlag = false;
                initGridNum();
                initNumColor();
                initCandidate();
                initCandColor();
                initArrow();
                mc.repaint();
            }else if(e.getSource()==item[2]){       //clear num
                clearNum();
                checkCandidate();
                mc.repaint();
            }else if(e.getSource()==item[3]){       //solution
                long t1 = System.currentTimeMillis();
                search();
                long t2 = System.currentTimeMillis();
                System.out.println("time: "+(t2-t1));
                mc.repaint();
            }else if(e.getSource()==item[4]){       //finish input
                initNumColor();
                mc.repaint();
            }else if(e.getSource()==item[5]){       //candidate
                candFlag = (candFlag==true) ? false : true;
                mc.repaint();
            }else if(e.getSource()==item[6]){       //arrow
                arrowFlag = (arrowFlag==true) ? false : true;
            }else if(e.getSource()==item[7]){       //input
                input();
                mc.repaint();
            }else if(e.getSource()==item[8]){       //output
                output();
            }else if(e.getSource()==item[9]){       //Save to PNG
                savePicture();
            }
        }
    }
    int m=1,n=1;
    class Mouse extends MouseAdapter{
        public void mouseMoved(MouseEvent e){

            inGrid(e.getX(),e.getY());
        }

        public void mousePressed(MouseEvent e) {
            if(e.getButton()==MouseEvent.BUTTON3){

                if(gridNum[yPos][xPos] != 0 && numColor[yPos][xPos]!=1){
                    gridNum[yPos][xPos] = 0;
                    checkCandidate();
                    
                }else if(candFlag==true){
                    inCand(e.getX(),e.getY());
                    
                }
                mc.repaint();
            }else if(e.getButton()==MouseEvent.BUTTON1){
                if(arrowFlag==true){
                    inArrow(e.getX(),e.getY());
                    mc.repaint();
                }else{
//                    gridBlock[yPos][xPos] = m;
//                    if(n++==9){ m++; n=1; }
                }
            }
        }
    }
    
    class Key extends KeyAdapter{
        public void keyTyped(KeyEvent e){
            
            if(Character.isDigit(e.getKeyChar())){
                num = Integer.parseInt(String.valueOf(e.getKeyChar()));

                if(numColor[yPos][xPos] != 1){
                    if(cand[yPos][xPos][num] != 0){
                        gridNum[yPos][xPos] = num;
                    }else if(num == 0){
                        gridNum[yPos][xPos] = 0;
                    }

                    checkCandidate();
                    mc.repaint();
                }
            }
        }
        
        public void keyPressed(KeyEvent e){
            
            int code = e.getKeyCode();
            if(e.isControlDown() && code >= 48 && code <=57){
                c = code - 48;
                mc.repaint();
            }
        }
    }
    
    void initNumColor(){
        
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                if(gridNum[i][j] != 0)
                    numColor[i][j] = 1;
                else
                    numColor[i][j] = 0;
            }
        }

    }
    
    void initColor(){
        int p,q;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                p = grid[i][j][0];
                q = grid[i][j][1];
                gridColor[p][q] = 1;
                
            }
        }
    }
    
    void initGridNum(){
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                gridNum[i][j] = 0;
            }
        }
    }
    
    void initCandidate(){
        for(int i = 1; i < 10; i++){
            for (int j = 1; j < 10; j++) {
                for (int k = 1; k < 10; k++) {
                    cand[i][j][k] = 1;
                }
            }
        }
    }
    
    void initCandColor(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    candColor[i][j][k] = 0;
                }
            }
        }
    }
    
    void initArrow(){
        for (int i = 0; i < arrow.length; i++) {
            for (int j = 0; j < arrow[i].length; j++) {
                for (int k = 0; k < arrow[i][j].length; k++) {
                    arrow[i][j][k] = 0;
                }
            }
        }
    }
    
    void inGrid(int x,int y){
        int prevXPos = xPos;
        int prevYPos = yPos;
        
        xPos = (x-45) / (3*candW) + 1;
        yPos = (y-35) / (3*candW) + 1;
        
        if(prevXPos!=xPos || prevYPos!=yPos){
            mc.repaint();
        }
    }
    
    void inCand(int x, int y){
        int xx = (x-45) / candW + 1;
        int yy = (y-35) / candW + 1;
        int num = ((yy-1) % 3) *3  +  (xx-1) % 3 + 1;
        candColor[yPos][xPos][num] = (candColor[yPos][xPos][num]==0) ? 1 : 0;
        
    }
    
    void inArrow(int X, int Y){
        int temp, xx, yy, x=82, y=53;
        
        for (int i = 0; i < 2; i++) {
            if(inRect((X-x)%45+x,(Y-y)%45+y,x,y,20,20)){
                xx = (X-x)/45+1;
                yy = (Y-y)/45+1;
                switch(arrow[i][yy][xx]){
                    case 0: arrow[i][yy][xx] = 1; break;
                    case 1: arrow[i][yy][xx] = 2; break;
                    case 2: arrow[i][yy][xx] = 0; break;
                }
            }
            x = 60;
            y = 73;
        }
    }
    
    boolean inRect(int X,int Y,int x,int y,int w,int h){
        if(X>=x && X<=x+w && Y>=y && Y<=y+h){
            return true;
        }else
            return false;
    }
    
    void checkRow(int r, int n){
        for (int i = 1; i < 10; i++) {
            cand[r][i][n] = 0;
        }
    }
    
    void checkColumn(int c, int n){
        for (int i = 1; i < 10; i++) {
            cand[i][c][n] = 0;
        }
    }
    
    void checkBlock(int r, int c, int n){
        for (int i = 0,y = (r-1)/3*3+1; i < 3; i++,y++) {
            for (int j = 0,x = (c-1)/3*3+1; j < 3; j++,x++) {
                cand[y][x][n] = 0;
            }
        }
    }
    
    void gridBlock(int r, int c, int n){
        for (int i = 1, b=gridBlock[r][c]; i < 10; i++) {
            if(gridBlock[r][c]==i){
                
            }
        }
    }
    
    void checkCandidate(){
        initCandidate();
        for (int p = 1; p < 10; p++) {
            for (int q = 1; q < 10; q++) {
                if(gridNum[p][q]!=0){

                    checkRow(p,gridNum[p][q]);
                    checkColumn(q,gridNum[p][q]);
                    checkBlock(p,q,gridNum[p][q]);
                }
            }
        }
//        checkWindow();
    }
    
    void checkCandidate(int p,int q){
        if(gridNum[p][q]!=0){

            checkRow(p,gridNum[p][q]);
            checkColumn(q,gridNum[p][q]);
            checkBlock(p,q,gridNum[p][q]);
        }
//        checkWindow();
    }
    
    void checkWindow(){
        int r, c, n;
        
        for (int b = 0; b < grid.length; b++) {
            for (int g = 0; g < grid[b].length; g++) {
                
                r = grid[b][g][0];
                c = grid[b][g][1];
                n = gridNum[r][c];
                if(n != 0){
                    updateCand(b,n);
                }
            }
        } 
    }
    
    void updateCand(int b,int n){
        int r,c;
        
        for (int i = 0; i < grid[b].length; i++) {
            r = grid[b][i][0];
            c = grid[b][i][1];
            cand[r][c][n] = 0;
        }
    }

    void solution(int r,int c){
        if(r==10 && c==1) flag = true;
        if(flag == false){
            if(gridNum[r][c] == 0){
                for (int i = 1; i < 10; i++) {
                    if(cand[r][c][i] != 0){
                        gridNum[r][c] = i;
                        checkCandidate(r,c);
                        if(c==9) solution(r+1,1);
                        else     solution(r,c+1);
                    }
                    if(flag != true){
                        gridNum[r][c] = 0;
                        checkCandidate();
                    }
                }
                
                
            }else{
                if(c==9) solution(r+1,1);
                else     solution(r,c+1);
            }
        }
        
//        System.out.printf("r: %d ,c: %d\n",r,c);
        
    }
    int[][] rows = new int[10][10];
    int[][] cols = new int[10][10];
    int[][] blocks = new int[10][10];
    
    void check(){
        int temp;
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                temp = gridNum[i][j];
                if(temp > 0){
                    setNum(i,j,temp,1);
                }
            }
        }
        
    }
    
    void initNum(){
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                rows[i][j] = 0;
                cols[i][j] = 0;
                blocks[i][j] = 0;
            }
        }
    }
    void search(){
        initNum();
        check();
        solution2(1,1);
    }
    
    void setNum(int r,int c,int i,int n){
        rows[r][i] = n;
        cols[c][i] = n;
        blocks[(r-1)/3*3+1 + (c-1)/3][i] = n;
    }
    
    boolean solution2(int r,int c){
        boolean result = false;
        if(r==10 && c==1) return true;
        
        if(gridNum[r][c] == 0){
            
            for (int i = 1; i < 10; i++) {
//                if(isExist(r,c,i)){
                if(rows[r][i] == 0 && cols[c][i] == 0 && blocks[(r-1)/3*3+1+(c-1)/3][i] == 0 && candColor[r][c][i]!=1){
                    if( isGT(r,c,i) ){
                        gridNum[r][c] = i;
                        setNum(r,c,i,1);

//                        if(c==9) result = solution2(r+1,1);
//                        else     result = solution2(r,c+1);
                        
                        result = (c==9) ? solution2(r+1,1) : solution2(r,c+1);

                        if(result==true) return result;
                
                        gridNum[r][c] = 0;
                        setNum(r,c,i,0);
                    }
                }
            }

        }else{
            if(c==9) result = solution2(r+1,1);
            else     result = solution2(r,c+1);
            return result;
        }
        
        return false;
    }
    
    boolean isGT(int r,int c,int i){
        if(( arrow[0][r][c-1]==0  || ( i>gridNum[r][c-1] && arrow[0][r][c-1]==1 ) || ( i<gridNum[r][c-1] && arrow[0][r][c-1]==2 )) && 
           ( arrow[1][r-1][c]==0  || ( i>gridNum[r-1][c] && arrow[1][r-1][c]==1 ) || ( i<gridNum[r-1][c] && arrow[1][r-1][c]==2 )) &&
           ( c==9 || gridNum[r][c+1]==0 || arrow[0][r][c]==0  || ( i>gridNum[r][c+1] && arrow[0][r][c]==2 ) || ( i<gridNum[r][c+1] && arrow[0][r][c]==1 )) && 
           ( r==9 || gridNum[r+1][c]==0 || arrow[1][r][c]==0  || ( i>gridNum[r+1][c] && arrow[1][r][c]==2 ) || ( i<gridNum[r+1][c] && arrow[1][r][c]==1 )) )
            return true;
        return false;
    }
    
    boolean isExist(int r,int c,int n){
        for (int i = 1; i < 10; i++) {
            if(gridNum[r][i] == n)
                return false;
        }

        for (int i = 1; i < 10; i++) {
            if(gridNum[i][c] == n)
                return false;
        }

        for (int i = 0,y = (r-1)/3*3+1; i < 3; i++,y++) {
            for (int j = 0,x = (c-1)/3*3+1; j < 3; j++,x++) {
                if(gridNum[y][x] == n)
                    return false;
            }
        }
        return true;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private class MyCanvas extends JPanel{
        
        int sX, sY;
        int blockX, blockY;
        int gridX, gridY;
        
        Image bkImage;
//        public void update(Graphics g){
//            
//            
//            if(null==bkImage) { bkImage = this.createImage(500,500); }
//            Graphics gBkImg = bkImage.getGraphics();
//            gBkImg.clearRect(0,0,500,500);
//            this.paint(gBkImg);
//            g.drawImage(bkImage,0,0,null);
//
//        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            
            sX=50;
            sY=50;
            blockX = sX-6;
            blockY = sY-16;
            gridX = blockX;
            gridY = blockY;
//            System.out.println("test"+this);
            Graphics2D g2 = (Graphics2D) g;
            g2.setBackground(Color.WHITE);
            drawGridColor(g);
            drawNum(g);

            
            g.drawImage(sudokuImg,blockX,blockY,null);
            
            if(candFlag==true) 
                drawCandidate(g);
            drawArrow(g);
        }
        
        void drawArrow(Graphics g){
            for (int i = 1, y=73; i < arrow[1].length; i++, y+=45) {
                for (int j = 1, x=60; j < arrow[1][i].length; j++, x+=45) {
                    if(arrow[1][i][j]==1)
                        g.drawImage(arrowImg[0],x,y,null);
                    else if(arrow[1][i][j]==2)
                        g.drawImage(arrowImg[1],x,y,null);
                }
            }
            
            for (int i = 1, y=53; i < arrow[0].length; i++, y+=45) {
                for (int j = 1, x=82; j < arrow[0][i].length; j++, x+=45) {
                    if(arrow[0][i][j]==1)
                        g.drawImage(arrowImg[2],x,y,null);
                    else if(arrow[0][i][j]==2)
                        g.drawImage(arrowImg[3],x,y,null);
                }
            }
            
            
        }
        
        void drawGridColor(Graphics g){

            g.setColor(new Color(176,204,255));
            for (int i = 1; i < 10; i++) {
                for (int j = 1; j < 10; j++) {
                    if(gridBlock[i][j] != 0){
                        g.setColor(color[gridBlock[i][j]]);
                        g.fillRect(gridX + ((j-1)*gridW+4), gridY + ((i-1)*gridH+4), gridW-1, gridH-1);
                    }
                }
            }
        }
        
        void drawNum(Graphics g){
            g.setColor(Color.YELLOW);
            g.fillRect(gridX + ((xPos-1)*gridW+4), gridY + ((yPos-1)*gridH+4), gridW-1, gridH-1);
            
            g.setColor(Color.BLUE);
                
            g.setFont(new Font(null,Font.BOLD,30));
            for (int i = 1; i < 10; i++) {
                for (int j = 1; j < 10; j++,gridX+=gridW) {
                    if(gridNum[i][j] != 0){
//                        if(numColor[i][j] == 1) g.setColor(Color.BLACK);
//                        else                    g.setColor(Color.BLUE);
                        
                        g.setColor(numColor[i][j] == 1 ? Color.BLACK : Color.BLUE);
                        
                        g.drawString(""+gridNum[i][j],gridX+16,gridY+37);
                    }
                }
                gridX -= 9*gridW;
                gridY += gridH;
            }
            
            gridX = blockX;
            gridY = blockY;
        }

        
        void drawCandidate(Graphics g){
            g.setColor(Color.BLACK);
            g.setFont(new Font(null,Font.PLAIN,12));
            sX-=1;sY-=1;
            for (int i = 1; i < 10; i++, sX -= 27*candW, sY += 3*candW) {
                for (int j = 1; j < 10; j++, sX += 3*candW, sY -= 3*candW) {
                    for (int k = 1; k < 10; k++,sX+=candW) {
                        if(gridNum[i][j]==0 && cand[i][j][k]!=0 && (c==0 || k==c)){
                            if(candColor[i][j][k] == 1) g.setColor(Color.GRAY);
                            else                        g.setColor(Color.BLACK);
                            
                            g.drawString(""+k,sX,sY);
                        }
                        if(k%3==0) {
                            sY += candW; 
                            sX -= 3*candW;
                        }
                    }
                }
            }
        }
        
//        void drawGrid(Graphics g){
//            for (int i = 1; i <= 81; i++, gridX+=gridW) {
//                g.setColor(Color.GRAY);
//                g.drawRect(gridX, gridY, gridW, gridH);
//                if(i%9==0) {
//                    gridX -= 9*gridW;
//                    gridY += gridH;
//                }
//            }
//            
//            for (int i = 1; i < 10; i++, blockX+=blockW) {
//                g.setColor(Color.BLACK);
//                g.drawRect(blockX,blockY,blockW,blockH);
//                if(i%3==0) {
//                    blockX -= 3*blockW;
//                    blockY += blockH;
//                }
//            }
//        }
        

    }
    
    public static void main(String[] args) throws Exception{
        new Sudoku();
    }
}