import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class GameBoard extends JFrame implements ActionListener{

	private final int ROWS = 100 ,COLUMNS = 100;
	private final int cellSize = 9;
	private JPanel gridPanel;
	private Cell[][] cells;
	private GameLoop gameLoop;
	
	
	private JLabel generations;
	private int turnNr;

	public GameBoard (){
		this.setLayout(new BorderLayout());

		this.gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(ROWS, COLUMNS));
		this.add(gridPanel, BorderLayout.CENTER);
		this.gridPanel.setPreferredSize(new Dimension(cellSize*COLUMNS,cellSize*COLUMNS));
		
		initCells();
		this.add(gridPanel);

		JPanel controlPanel = new JPanel();
		//Use GridPanels width and a dummy height.
		controlPanel.setPreferredSize(new Dimension(gridPanel.getWidth(), 30));
		this.add(controlPanel, BorderLayout.SOUTH);
		
		generations = new JLabel("Generations ");
		controlPanel.add(generations);
		
		JButton start = new JButton("Start");
		start.setActionCommand("Start");
		start.addActionListener(this);
		controlPanel.add(start);

		JButton reset = new JButton("Reset");
		reset.setActionCommand("Reset");
		reset.addActionListener(this);
		controlPanel.add(reset);
		
		JButton stop = new JButton("Stop");
		stop.setActionCommand("Stop");
		stop.addActionListener(this);
		controlPanel.add(stop);
		
		JButton changeSpeed = new JButton ("Change Speed");
		changeSpeed.setActionCommand("ChangeSpeed");
		changeSpeed.addActionListener(this);
		controlPanel.add(changeSpeed);
		
		this.turnNr = 0;

		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	private void initCells() {
		this.cells = new Cell [ROWS][COLUMNS];
		for (int i = 0; i < ROWS; i++){
			for (int l = 0; l < COLUMNS; l++){
				this.cells[i][l] = new Cell(i,l);
				this.cells[i][l].addActionListener(this);
				this.cells[i][l].setActionCommand(i + "," + l);
				this.gridPanel.add(this.cells[i][l]);
			}
		}
	}

	public static void main (String [] args){
		System.out.println("Initializing new GameBoard...");
		new GameBoard();
	}

	/**
	 * Given a cell in the gameboard, return the number of alive neighbors.
	 * @param cell
	 * @return Number of alive neighbors.
	 */
	private int countNeighborsAlive(Cell cell) {
		int top = cell.getColumn() + 1;
		int buttom = cell.getColumn() - 1;
		int right = cell.getRow() + 1;
		int left = cell.getRow() - 1;

		int count = 0;

		if (top < COLUMNS) {
			if (cells[cell.getRow()][top].getState() == State.ALIVE) {
				count +=1;
			}
			if (right < ROWS){
				if (cells[right][top].getState() == State.ALIVE){
					count+=1;
				}
			}
			if (left >= 0 ) {
				if (cells[left][top].getState() == State.ALIVE){
					count +=1;
				}
			}
		}
		if (buttom >= 0 ) {
			if (cells[cell.getRow()][buttom].getState() == State.ALIVE){
				count +=1;
			}

			if (right < ROWS) {
				if (cells[right][buttom].getState() == State.ALIVE){
					count +=1;
				}
			}

			if (left >= 0) {
				if (cells[left][buttom].getState() == State.ALIVE){
					count +=1;
				}
			}
		}
		if (right < ROWS ) {
			if (cells[right][cell.getColumn()].getState() == State.ALIVE){
				count +=1;
			}
		}
		if (left >= 0) {
			if (cells[left][cell.getColumn()].getState() == State.ALIVE){
				count +=1;
			}

		}
		return count;
	}//End countNeighborsAlive

	/**
	 * For each cell in the gameboard, check for number of neighbors alive, then change states of each cell
	 * depending on number of neighbors alive.
	 */
	public void doTurn (){
		updateNeighbors();
		updateCells();
		this.turnNr++;
		this.generations.setText("Generation nr " + turnNr);
	}

	private void updateCells() {
		for (int i = 0; i < this.ROWS; i ++){
			for (int l = 0; l < this.COLUMNS; l ++){
				cells[i][l].newTurn();
			}
		}
	}

	private void updateNeighbors() {
		for (int i = 0; i < this.ROWS; i ++){
			for (int l = 0; l < this.COLUMNS; l ++){
				cells[i][l].setNeighbors(countNeighborsAlive(cells[i][l]));
			}
		}
	}
	
	private int getSpeedInput(){
		boolean badInput = true;
		int newSpeed = 0;
		while (badInput){
			badInput = false;
			String input = JOptionPane.showInputDialog("Specify new intervall time, in milisecs");
			try {
				newSpeed = Integer.parseInt(input);
			}catch (NumberFormatException ex){
				System.out.println("Illigal input.");
				badInput = true;
			}
		}
		return newSpeed;
	}


	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand() == "Start"){
			gameLoop = new GameLoop(this);
		}
		else if (evt.getActionCommand() == "Reset"){
			if (gameLoop != null){
				gameLoop.stop();
			}
			for (int i = 0; i < this.ROWS; i ++){
				for (int l = 0; l < this.COLUMNS; l ++){
					if (this.cells[i][l].getState() == State.ALIVE){
						this.cells[i][l].changeState();
					}
				}
			}
		}
		else if (evt.getActionCommand() == "Stop"){
			if (gameLoop != null){
				gameLoop.stop();
			}
		}
		else if (evt.getActionCommand() == "ChangeSpeed"){
			int newSpeed = getSpeedInput();
			gameLoop.setSleepTime(newSpeed);
			
		}
		for (int i = 0; i < this.ROWS; i ++){
			for (int l = 0; l < this.COLUMNS; l ++){
				if (evt.getActionCommand().equals(((i + "," + l)))){
					this.cells[i][l].changeState();
				}
			}
		}
	}//End actionPerfocmed
	

private class GameLoop implements Runnable{

	private GameBoard board;
	private boolean running;
	private int sleepTime;
	Thread thread;

	public GameLoop (GameBoard board){
		this.board = board;
		thread = new Thread(this);
		this.sleepTime = 100;
		thread.start();
	}

	public void stop(){
		this.running = false;
	}
	
	public void setSleepTime(int sleepTime){
		this.sleepTime = sleepTime;
	}
	

	@Override
	public void run() {
		this.running = true;
		while(running){
			board.doTurn();
			try{
				Thread.sleep(sleepTime);
			}catch (InterruptedException ex){
			}
		}
	}

}

}
