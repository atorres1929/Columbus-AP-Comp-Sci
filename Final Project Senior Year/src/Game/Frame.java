/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Game;

import javax.swing.JFrame;

   
public class Frame extends JFrame{
    
    public Frame(){
        setTitle("Video Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(new Panel());
        setLocation(50,50);
        pack();
        setVisible(true);
    }

    
    public static void main(String[] args) {
        
        new Frame();
    }
    
}
