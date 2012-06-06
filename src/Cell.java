import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;


public class Cell extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2448776321075965870L;
	private State state;
	private int neighbors;
	
	private int row,column;
	
	public Cell (int row, int column){
		this.state = State.DEAD;
		this.setRow(row);
		this.setColumn(column);
		
		setBackground(Color.WHITE);
		this.setBorderPainted(false);
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(10,10));

	}
	
	public void setState(State state){
		this.state = state;
	}
	
	public State getState(){
		return this.state;
	}

	public void changeState() {
		if (this.state == State.ALIVE){
			this.state = State.DEAD;
			this.setBackground(Color.WHITE);
		}else {
			this.state = State.ALIVE;
			this.setBackground(Color.BLACK);
		}
	}

	public int getNeighbors() {
		return this.neighbors;
	}

	public void setNeighbors(int neighbors) {
		this.neighbors = neighbors;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void newTurn() {
		if (this.state == State.ALIVE){
			if (this.neighbors < 2){
				this.changeState();
				
			}else if (this.neighbors > 3){
				this.changeState();
			}
			
		}else if (this.state == State.DEAD){
			if (this.neighbors == 3){
				this.changeState();
			}
		}
	}
	
}
