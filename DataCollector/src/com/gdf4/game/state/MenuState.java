package com.gdf4.game.state;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.List;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import com.gdf4.framework.util.RandNumGen;
import com.gdf4.game.main.Game;
import com.gdf4.game.main.GameMain;
import com.gdf4.game.main.Resource;
import com.gdf4.game.model.*;
import com.gdf4.game.model.button.classicButton;

public class MenuState extends State {
	
	
	ArrayList<String[]> data;
	
	int r,g,b;
	int choice;
	FileWriter writer;
	
	int whiteBtnX=270, whiteBtnY=400, whiteBtnW=150, whiteBtnH=50;
	int blackBtnX=80, blackBtnY=400, blackBtnW=150, blackBtnH=50;
	
	@Override
	public void init() {
		r = RandNumGen.getRandInt(255);
		g = RandNumGen.getRandInt(255);
		b = RandNumGen.getRandInt(255);
		
		data = new ArrayList<>();
		
		try {
			writer = new FileWriter("TrainingData.csv", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//read current data
		try (BufferedReader br = new BufferedReader(new FileReader("TrainingData.csv"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String val = line.substring(0, line.length()-2);
		        String[] values = val.split(",");
		        data.add(values);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//print output
		/*for(String[] s : data) {
			for(String d : s) {
				System.out.print(d + " ");
			}
			System.out.println();
		}*/
		
		
		//find duplicates
		for(int i = 0; i < data.size(); i++) {
			String[] s = data.get(i);
			for(int j = i; j < data.size(); j++) {
				String[] d = data.get(j);
				if( (s[0].equals(d[0]) && s[1].equals(d[1]) && s[2].equals(d[2]))
						&& i!=j) {
					System.err.println("caught duplicate: Pos1=" + (i+1) +";val=" + s[0]+"-"+s[1]+"-"+s[2] + ", Pos2="+(j+1)+";val=" + d[0]+"-"+d[1]+"-"+d[2]);
				}
			}
		}
	}

	@Override
	public void update(float delta) {
		
	}

	Color whiteBtnClr = Color.WHITE;
	Color blackBtnClr = Color.WHITE;
	
	@Override
	public void render(Graphics g) {
		//DO NOT DELETE THIS LINE
		Graphics2D g2 = (Graphics2D) g;
		
		RenderingHints rh = new RenderingHints( //anti-aliasing
	             RenderingHints.KEY_TEXT_ANTIALIASING,
	             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g2.setRenderingHints(rh);
		
		//render bg
	    
		g2.setColor(new Color(this.r, this.g, this.b));
		g2.fillRect(0, 0, GameMain.GAME_WIDTH, GameMain.GAME_HEIGHT);
		g2.setFont(new Font("Arial Bold", Font.BOLD, 20));
		g2.setColor(Color.BLACK);
		g2.drawString("DU I LOOK GUD ON DIS COLOR??", GameMain.GAME_WIDTH/2-160, GameMain.GAME_HEIGHT/2-150);
		g2.setColor(Color.WHITE);
		g2.drawString("OR AM I BETTA ON DIS COLOR??", GameMain.GAME_WIDTH/2-160, GameMain.GAME_HEIGHT/2-80);
	
		
		//render button
		
		g2.setColor(new Color(220,220,220));
		g2.fillRect(0, 250, GameMain.GAME_WIDTH, GameMain.GAME_HEIGHT);
		
		g2.setColor(whiteBtnClr);
		g2.fillRect(whiteBtnX, whiteBtnY, whiteBtnW, whiteBtnH);
		g2.setColor(blackBtnClr);
		g2.fillRect(blackBtnX, blackBtnY, blackBtnW, blackBtnH);
		g2.setColor(Color.BLACK);
		g2.drawString("I vote black", 100, 433);
		g2.drawString("I vote white", 290, 433);
		
		try {
			g2.drawString("Data Collected(" + count("TrainingData.csv") + ") / Goal (900)", 100, 340);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g2.setColor(Color.RED);
		g2.drawString("+", GameMain.GAME_WIDTH/2-10, 135);
	}

	public int count(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	    byte[] c = new byte[1024];
	    int count = 0;
	    int readChars = 0;
	    boolean empty = true;
	    while ((readChars = is.read(c)) != -1) {
	        empty = false;
	        for (int i = 0; i < readChars; ++i) {
	            if (c[i] == '\n') {
	                ++count;
	            }
	        }
	    }
	    return (count == 0 && !empty) ? 1 : count;
	    } finally {
	    is.close();
	   }
	}
	
	//store data
	private void storeData(int userInput, int r, int g, int b) {
		try {
			writer.append(Integer.toString(r));
			writer.append(',');
			writer.append(Integer.toString(g));
			writer.append(',');
			writer.append(Integer.toString(b));
			writer.append(',');
			writer.append(Integer.toString(userInput));
			writer.append('\n');
	
			writer.flush();
		} catch (IOException e) {
			System.err.println("error occoured while writing data");
		}
		updateColour();
	}
	
	private void updateColour() {
		boolean flag = true;
		while(flag) {
			r = RandNumGen.getRandInt(255);
			g = RandNumGen.getRandInt(255);
			b = RandNumGen.getRandInt(255);
			String[] com = {String.valueOf(r), String.valueOf(g), String.valueOf(b)};
			boolean tempF = false;
			for(String[] data : data) {
				tempF = true;
				if(data.equals(com)) {
					break;
				}
				tempF = false;
			}
			if(tempF == false) {
				flag = false;
			}
		}
	}
	
	@Override
	public void onClick(MouseEvent e) {
		if(e.getX() > whiteBtnX && e.getX() < whiteBtnX+whiteBtnW &&
				e.getY() > whiteBtnY && e.getY() < whiteBtnY + whiteBtnH) {
			storeData(1, this.r, this.g, this.b);
		}
		
		if(e.getX() > blackBtnX && e.getX() < blackBtnX+blackBtnW &&
				e.getY() > blackBtnY && e.getY() < blackBtnY + blackBtnH) {
			storeData(0, this.r, this.g, this.b);
		}
	}

	@Override
	public void onKeyPress(KeyEvent e) {
		
	}

	@Override
	public void onKeyRelease(KeyEvent e) {
	}

	@Override
	public void onMousePress(MouseEvent e) {
	}

	@Override
	public void onMouseRelease(MouseEvent e) {
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if(e.getX() > whiteBtnX && e.getX() < whiteBtnX+whiteBtnW &&
				e.getY() > whiteBtnY && e.getY() < whiteBtnY + whiteBtnH) {
			whiteBtnClr = Color.LIGHT_GRAY;
		} else {			
			whiteBtnClr = Color.WHITE;
		}
		
		if(e.getX() > blackBtnX && e.getX() < blackBtnX+blackBtnW &&
				e.getY() > blackBtnY && e.getY() < blackBtnY + blackBtnH) {
			blackBtnClr = Color.LIGHT_GRAY;
		} else {			
			blackBtnClr = Color.WHITE;
		}
	}

}
