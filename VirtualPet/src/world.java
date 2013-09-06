import javax.swing.*;


public class world {
	
	public static void main(String[] args){
	new world();
	}

	public world (){
	JFrame frame = new JFrame();
	frame.setTitle("The World");
	//JPanel panel1 = new JPanel();
	//frame.add(panel1);
	frame.add(new homeworld());
	frame.setResizable(false);
	
	frame.setSize(845,588);
	//panel1.setVisible(true);
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}

}
