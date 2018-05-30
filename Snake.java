import java.awt.EventQueue;
import javax.swing.JFrame;

public class Snake extends JFrame 
{
	private static final long serialVersionUID = 1L;

	public Snake() 
    {        
        initUI();
    }
    
    private void initUI() 
    {        
        add(new Board());
        setTitle("Snake");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }
    
    public static void main(String[] args) 
    {        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}