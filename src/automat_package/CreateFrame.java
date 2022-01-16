package automat_package;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class CreateFrame implements WindowListener {

	public final int WIDTH = 100, HEIGHT = 400;
	private JFrame frame;
	private boolean hasAccess, windowStillOpen;
	private int[] moneyAmounts, slotsAmounts;
	private Costumer costumer;
	private Object obj;

	public CreateFrame(String frameName, boolean hasAccess, Object obj, boolean terminateProgram) {
		this.hasAccess = hasAccess;
		this.obj = obj;

		initCFrame(frameName, terminateProgram);
		if (obj != null) {
			synchronized (obj) {
				try {
					obj.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initCFrame(String frameName, boolean terminateProgram) {
		frame = new JFrame(frameName);
		frame.setSize(400, 500);
		frame.addWindowListener(this);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(terminateProgram ? JFrame.EXIT_ON_CLOSE : JFrame.HIDE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		frame.setVisible(true);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, "Hola");

		moneyAmounts = new int[8];
		createBudget(tabbedPane, "Budget");

		if (hasAccess) {
			slotsAmounts = new int[6];
			createAutomatSlots(tabbedPane, "Inhalt");
		}
		tabbedPane.setVisible(true);
		tabbedPane.repaint();
		tabbedPane.validate();
	}

	private void createBudget(JTabbedPane tabbedPane, String tabName) {
		JPanel tab1 = new JPanel();
		tabbedPane.addTab(tabName, null, tab1, null);
		tab1.setLayout(new BorderLayout(0, 0));

		JLabel titleLbl = new JLabel(" ");
		titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		titleLbl.setFont(new Font("Cambria", Font.PLAIN, 20));
		tab1.add(titleLbl, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		tab1.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(8, 2, 0, 10));
		panel.setVisible(true);
		panel.repaint();

		String[] valueNames = new String[] { "10ct", "20ct", "50ct", "1"+Money.MONEYMARK_Bigger, "2"+Money.MONEYMARK_Bigger, "5"+Money.MONEYMARK_Bigger, "10"+Money.MONEYMARK_Bigger, "20"+Money.MONEYMARK_Bigger };

		JSpinner[] spinners = new JSpinner[moneyAmounts.length];
		for (int i = 0; i < spinners.length; i++) {

			JLabel valueLbl = new JLabel(valueNames[i] + "  x");
			valueLbl.setHorizontalAlignment(SwingConstants.CENTER);
			valueLbl.setFont(new Font("Cambria", Font.BOLD, 15));
			panel.add(valueLbl);

			spinners[i] = new JSpinner();
			spinners[i].setModel(new SpinnerNumberModel(hasAccess ? 500 : 50, 0, 1000000, 1));
			spinners[i].setFont(new Font("Cambria", Font.PLAIN, 15));
			panel.add(spinners[i]);

		}

		JButton submitBtn = new JButton("SPEICHERN");
		submitBtn.setBackground(Color.LIGHT_GRAY);
		submitBtn.setFont(new Font("Cambria", Font.PLAIN, 20));
		tab1.add(submitBtn, BorderLayout.SOUTH);
		submitBtn.addActionListener(e -> {
			for (int i = 0; i < spinners.length; i++) {
				moneyAmounts[i] = (int) spinners[i].getValue();
			}
			if (hasAccess) {
				tabbedPane.setSelectedIndex(1);
			} else {
				runAfterwards();
				frame.dispose();
			}
		});

		JLabel notContentE = new JLabel("   ");
		notContentE.setFont(new Font("Tahoma", Font.PLAIN, 74));
		tab1.add(notContentE, BorderLayout.EAST);

		tab1.setVisible(true);
		tab1.repaint();
		tab1.validate();

	}

	private void createAutomatSlots(JTabbedPane tabbedPane, String tabName) {
		JPanel tab2 = new JPanel();
		tabbedPane.addTab(tabName, null, tab2, null);
		tab2.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		tab2.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(6, 2, 0, 10));

		JSlider[] sliders = new JSlider[slotsAmounts.length];

		for (int i = 0; i < slotsAmounts.length; i++) {

			JLabel slotLbl = new JLabel("Fach " + (i / 2 + 1) + (i % 2 + 1));
			slotLbl.setHorizontalAlignment(SwingConstants.CENTER);
			slotLbl.setFont(new Font("Cambria", Font.BOLD, 15));
			panel_1.add(slotLbl);

			sliders[i] = new JSlider();
			sliders[i].setValue(5);
			sliders[i].setSnapToTicks(true);
			sliders[i].setPaintLabels(true);
			sliders[i].setForeground(Color.BLUE);
			sliders[i].setMinorTickSpacing(1);
			sliders[i].setMajorTickSpacing(1);
			sliders[i].setMaximum(10);
			sliders[i].setPaintTicks(true);
			panel_1.add(sliders[i]);
		}

		JLabel notContent = new JLabel("                         ");
		tab2.add(notContent, BorderLayout.NORTH);

		JLabel noContent_1 = new JLabel("                  ");
		tab2.add(noContent_1, BorderLayout.EAST);

		JButton submitBtnA = new JButton("SPEICHERN");
		submitBtnA.setBackground(Color.LIGHT_GRAY);
		submitBtnA.setFont(new Font("Cambria", Font.PLAIN, 20));
		tab2.add(submitBtnA, BorderLayout.SOUTH);
		submitBtnA.addActionListener(e -> {

			for (int i = 0; i < slotsAmounts.length; i++) {
				slotsAmounts[i] = sliders[i].getValue();
			}

			runAfterwards();
			frame.dispose();
		});
	}

	private void runAfterwards() {
		if (obj != null) {
			synchronized (obj) {
				if (!hasAccess)
					costumer = new Costumer(moneyAmounts);
				obj.notify();
			}
		} else {
			costumer = new Costumer(moneyAmounts);
			Costumer.existingCostumer = costumer;
		}
	}

	public int[] getAmounts(int index) {
		if (hasAccess) {
			return index == 0 ? moneyAmounts : index == 1 ? slotsAmounts : null;
		}
		return null;
	}

	public Costumer getCostumer() {
		return costumer;
	}

	public boolean getIsWindowStillOpen() {
		return windowStillOpen;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		windowStillOpen = true;

	}

	@Override
	public void windowClosed(WindowEvent e) {
		windowStillOpen = false;
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
