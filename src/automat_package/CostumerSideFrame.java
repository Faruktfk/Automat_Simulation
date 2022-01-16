package automat_package;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CostumerSideFrame implements ActionListener {

	public static CostumerSideFrame costumerSideFrame;
	public final int WIDTH = 1000, HEIGHT = 850;

	private AutomatPanel automatPanel;
	private CostumerPanel costumerPanel;

	public CostumerSideFrame(Automat automat) {
		if (costumerSideFrame == null) {
			Font font = new Font("Cambria", Font.PLAIN, 15);
			costumerSideFrame = this;
			JFrame frame = new JFrame("Automat-CostumerSide");
			frame.setSize(WIDTH, HEIGHT);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setLayout(null);

			BufferedImage[][] bottles = new BufferedImage[3][2];
			try {
				for (int y = 0; y < bottles.length; y++) {
					for (int x = 0; x < bottles[y].length; x++) {
						int ix = x + 1;
						int iy = y + 1;
						bottles[y][x] = ImageIO
								.read(getClass().getResourceAsStream("/Bottle" + iy + ix + "_50x82.png"));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			automatPanel = new AutomatPanel(automat, bottles);
			frame.add(automatPanel);
			automatPanel.setVisible(true);
			automatPanel.repaint();

			JSeparator separator = new JSeparator(JSeparator.VERTICAL);
			separator.setBounds(470, 70, 2, HEIGHT - 200);
			separator.setForeground(new Color(160, 160, 160));
			frame.add(separator);
			separator.setVisible(true);
			separator.repaint();

			costumerPanel = new CostumerPanel(Costumer.existingCostumer, bottles);
			frame.add(costumerPanel);
			costumerPanel.setVisible(true);
			costumerPanel.repaint();
			
			JButton createCostumerBtn = new JButton("Neuer Kunde");
			createCostumerBtn.setBounds(650,15,150,30);
			createCostumerBtn.setBackground(Color.LIGHT_GRAY);
			createCostumerBtn.setFocusable(false);
			createCostumerBtn.setFont(font);
			createCostumerBtn.setActionCommand("createCostumerBtn");
			createCostumerBtn.addActionListener(this);
			frame.add(createCostumerBtn);
			createCostumerBtn.setVisible(true);
			

			JMenuBar menuBar = new JMenuBar();
			menuBar.setBackground(Color.WHITE);
			frame.setJMenuBar(menuBar);

			JMenu firstMenu = new JMenu("Einstellungen");
			firstMenu.setFont(font);
			firstMenu.setBackground(Color.WHITE);
			menuBar.add(firstMenu);

			JMenuItem createCostumerM = new JMenuItem("Neuer Kunde");
			createCostumerM.setBackground(Color.WHITE);
			createCostumerM.setFont(font);
			firstMenu.add(createCostumerM);
			createCostumerM.addActionListener(this);
			createCostumerM.setActionCommand("createCostumerMenu");

			JMenuItem exitM = new JMenuItem("Schlie"+ "\u1E9E" +"en");
			exitM.setBackground(Color.WHITE);
			exitM.setFont(font);
			firstMenu.add(exitM);
			exitM.addActionListener(this);
			exitM.setActionCommand("exitMenu");

			menuBar.setVisible(true);
			menuBar.repaint();
			menuBar.revalidate();

		}
	}

	public void update() {
		automatPanel.update();
		costumerPanel.update();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if (command.equals("exitMenu")) {
			
			System.exit(0);
			
		} else if (command.equals("createCostumerMenu") || command.equals("createCostumerBtn")) {

			Thread ct = new Thread(new Runnable() {

				@Override
				public void run() {
					CreateFrame createFrame = new CreateFrame("Neuer Kunde", false, null, false);

					while (createFrame.getIsWindowStillOpen()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					if (costumerPanel.getCostumer() != null) {
						costumerPanel.update();
					}

				}
			});

			ct.start();

		}

	}

}
