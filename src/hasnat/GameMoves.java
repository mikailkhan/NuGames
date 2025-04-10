package hasnat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Timer;
import javax.swing.JPanel;

public class GameMoves extends JPanel implements KeyListener, ActionListener, MouseListener {
    
    private static final long serialVersionUID = 1L;
    Layout la;
    Build_Player p;
    Timer time;
    int delay = 10;
    int current_player;
    int dice;
    int flag = 0, roll, kill = 0;
    
    private String[] playerNames = {"RED_K", "GREEN_S", "YELLOW_H", "BLUE_N"};
    
    public GameMoves() {
        setFocusTraversalKeysEnabled(false);
        requestFocus();
        current_player = 0;
        la = new Layout(80, 50);
        p = new Build_Player(la.height, la.width);
        dice = 0;
        flag = 0;
        roll = 0;
        kill = 0;
    }

    @Override
    public void paint(Graphics g) {
        la.draw((Graphics2D) g);
        p.draw((Graphics2D) g);
        if (p.pl[current_player].coin == 4) {
            g.setColor(Color.WHITE);
            g.fillRect(590, 100, 380, 130);
            setColorForCurrentPlayer(g);
            g.setFont(new Font("monospaced", Font.BOLD, 40));
            g.drawString(playerNames[current_player] + " wins.", 600, 150);
            g.drawString("Congratulations.", 600, 200);
            resetGame();
        } else if (dice != 0) {
            g.fillRect(590, 100, 380, 130);
            setColorForCurrentPlayer(g);
            g.setFont(new Font("monospaced", Font.BOLD, 20));
            g.drawString(playerNames[current_player], 600, 150);
            g.drawString("Number on dice is " + dice, 600, 200);
        }
        if (flag == 0 && dice != 0 && dice != 6 && kill == 0) {
            current_player = (current_player + 1) % 4;
        }
        kill = 0;
    }

    private void setColorForCurrentPlayer(Graphics g) {
        switch (current_player) {
            case 0:
                g.setColor(Color.RED);
                break;
            case 1:
                g.setColor(Color.GREEN);
                break;
            case 2:
                g.setColor(Color.YELLOW);
                break;
            case 3:
                g.setColor(Color.BLUE);
                break;
        }
    }

    private void resetGame() {
        current_player = 0;
        la = new Layout(80, 50);
        p = new Build_Player(la.height, la.width);
        dice = 0;
        flag = 0;
        roll = 0;
        kill = 0;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && flag == 0) {
            roll = 0;
            dice = 1 + (int) (Math.random() * 6);
            repaint();
            for (int i = 0; i < 4; i++) {
                if (p.pl[current_player].pa[i].current != -1 && p.pl[current_player].pa[i].current != 56 && (p.pl[current_player].pa[i].current + dice) <= 56) {
                    flag = 1;
                    break;
                }
            }
            if (flag == 0 && dice == 6) {
                for (int i = 0; i < 4; i++) {
                    if (p.pl[current_player].pa[i].current == -1) {
                        flag = 1;
                        break;
                    }
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (flag == 1) {
            int x = e.getX();
            int y = e.getY();
            x = x - 80;
            y = y - 50;
            x = x / 30;
            y = y / 30;
            int value = -1;
            if (dice == 6) {
                value = handleDiceSix(x, y);
            } else {
                value = handleOtherDice(x, y);
            }
            repaint();
        }
    }

    private int handleDiceSix(int x, int y) {
        int value = -1;
        for (int i = 0; i < 4; i++) {
            if (p.pl[current_player].pa[i].x == x && p.pl[current_player].pa[i].y == y && (p.pl[current_player].pa[i].current + dice) <= 56) {
                value = i;
                flag = 0;
                break;
            }
        }
        if (value != -1) {
            updatePlayerPosition(value);
        } else {
            for (int i = 0; i < 4; i++) {
                if (p.pl[current_player].pa[i].current == -1) {
                    p.pl[current_player].pa[i].current = 0;
                    flag = 0;
                    break;
                }
            }
        }
        return value;
    }

    private int handleOtherDice(int x, int y) {
        int value = -1;
        for (int i = 0; i < 4; i++) {
            if (p.pl[current_player].pa[i].x == x && p.pl[current_player].pa[i].y == y && (p.pl[current_player].pa[i].current + dice) <= 56) {
                value = i;
                flag = 0;
                break;
            }
        }
        if (value != -1) {
            updatePlayerPosition(value);
        }
        return value;
    }

    private void updatePlayerPosition(int value) {
        p.pl[current_player].pa[value].current += dice;
        if (p.pl[current_player].pa[value].current == 56) {
            p.pl[current_player].coin++;
        }
        int k = 0;
        int hou = p.pl[current_player].pa[value].current;
        if ((hou % 13) != 0 && (hou % 13) != 8 && hou < 51) {
            for (int i = 0; i < 4; i++) {
                if (i != current_player) {
                    for (int j = 0; j < 4; j++) {
                        int tem1 = Path.ax[current_player][p.pl[current_player].pa[value].current], tem2 = Path.ay[current_player][p.pl[current_player].pa[value].current];
                        if (p.pl[i].pa[j].x == tem1 && p.pl[i].pa[j].y == tem2) {
                            p.pl[i].pa[j].current = -1;
                            kill = 1;
                            k = 1;
                            break;
                        }
                    }
                }
                if (k == 1)
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }
}
