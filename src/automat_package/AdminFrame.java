package automat_package;

import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminFrame implements ActionListener {

	public static AdminFrame adminFrame;
	public final int WIDTH = 300, HEIGHT = 880;

	private JProgressBar[][] pBars = new JProgressBar[3][2];
	private JButton[][] addBtns = new JButton[3][2];
	private JSpinner[] spinners = new JSpinner[Money.MONEY_VALUES.length - 3];
	private JLabel totalLbl, cashAmountLbls[] = new JLabel[Money.MONEY_VALUES.length - spinners.length];
	private int tab1[][] = new int[3][2], tab2[];

	private Automat automat;
	private Bottle[] bottles = new Bottle[6];
	private Slot[][] slots = new Slot[3][2];

	public AdminFrame(int[] moneyAmounts, int[] slotsAmounts) {
		if (adminFrame == null) {
			adminFrame = this;
			tab2 = moneyAmounts.clone();
			JFrame frame = new JFrame("Automat-AdminSide");
			frame.setSize(WIDTH, HEIGHT);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocation(40, 70);
			frame.getContentPane().setLayout(new CardLayout(0, 0));
			frame.setVisible(true);

			initAutomat(moneyAmounts, slotsAmounts);

			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			frame.getContentPane().add(tabbedPane, "name_83890764499600");

			initAdminSide(tabbedPane);
			tabbedPane.repaint();
		}
	}

	private void initAutomat(int[] moneyAmounts, int[] slotsAmounts) {
		bottles[0] = new Bottle(0.5, 0, 0);
		bottles[1] = new Bottle(1.5, 1, 0);
		bottles[2] = new Bottle(3.2, 0, 1);
		bottles[3] = new Bottle(2.8, 1, 1);
		bottles[4] = new Bottle(3.3, 0, 2);
		bottles[5] = new Bottle(4.5, 1, 2);

		for (int y = 0; y < slots.length; y++) {
			for (int x = 0; x < slots[y].length; x++) {
				int index = slotsAmounts[y * 2 + x * 1];
				slots[y][x] = new Slot(x, y);
				fillSlot(x, y, index);
			}
		}
		automat = new Automat(slots, moneyAmounts);
		new CostumerSideFrame(automat);
	}

	private void initAdminSide(JTabbedPane tabbedPane) {

		JPanel tab1Panel = new JPanel();
		tab1Panel.setBounds(0, 0, WIDTH, HEIGHT);
		tab1Panel.setLayout(null);
		tabbedPane.addTab("Stand", null, tab1Panel, null);

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 2; x++) {

				this.tab1[y][x] = slots[y][x].getCurrentAmount();

				JPanel blockPanel = new JPanel();
				blockPanel.setBounds(30 + x * 130, 20 + y * 230, 92, 203);
				tab1Panel.add(blockPanel);
				blockPanel.setLayout(new BorderLayout(0, 0));

				pBars[y][x] = new JProgressBar();
				pBars[y][x].setValue(slots[y][x].getCurrentAmount());
				pBars[y][x].setStringPainted(true);
				pBars[y][x].setOrientation(SwingConstants.VERTICAL);
				pBars[y][x].setMaximum(10);
				pBars[y][x].setForeground(new Color(50, 205, 50));
				blockPanel.add(pBars[y][x], BorderLayout.CENTER);

				JLabel lblNewLabel = new JLabel("S" + (y + 1) + (x + 1));
				lblNewLabel.setBackground(Color.WHITE);
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel.setFont(new Font("Cambria", Font.PLAIN, 20));
				blockPanel.add(lblNewLabel, BorderLayout.NORTH);

				addBtns[y][x] = new JButton("+");
				addBtns[y][x].setFont(new Font("Cambria", Font.BOLD, 20));
				addBtns[y][x].setBackground(Color.WHITE);
				addBtns[y][x].setFocusable(false);
				addBtns[y][x].addActionListener(this);
				addBtns[y][x].setActionCommand(y + "_" + x);
				blockPanel.add(addBtns[y][x], BorderLayout.SOUTH);

			}
		}
		JButton updateBtn1 = new JButton("AKTUALISIEREN");
		updateBtn1.setBackground(Color.LIGHT_GRAY);
		updateBtn1.setFocusable(false);
		updateBtn1.setFont(new Font("Cambria", Font.PLAIN, 15));
		updateBtn1.setBounds(51, 725, 198, 39);
		updateBtn1.addActionListener(this);
		updateBtn1.setActionCommand("updateBtn_1");
		tab1Panel.add(updateBtn1);

		JButton refreshBtn1 = new JButton("Neu Laden");
		refreshBtn1.setBackground(Color.LIGHT_GRAY);
		refreshBtn1.setFocusable(false);
		refreshBtn1.setFont(new Font("Cambria", Font.PLAIN, 15));
		refreshBtn1.setBounds(51, 772, 198, 25);
		refreshBtn1.addActionListener(this);
		refreshBtn1.setActionCommand("refreshBtn_1");
		tab1Panel.add(refreshBtn1);

		// ----------------------

		JPanel tab2Panel = new JPanel();
		tab2Panel.setBounds(0, 0, WIDTH, HEIGHT);
		tab2Panel.setBackground(Color.white);
		tab2Panel.setLayout(null);
		tabbedPane.addTab("Geld", null, tab2Panel, null);

		JLabel budgetLbl = new JLabel("TOTAL:");
		budgetLbl.setBounds(20, 10, 100, 60);
		budgetLbl.setFont(new Font("Arial", Font.PLAIN, 20));
		tab2Panel.add(budgetLbl);
		budgetLbl.setVisible(true);

		totalLbl = new JLabel(automat.getTotal());
		totalLbl.setBounds(100, 10, 150, 60);
		totalLbl.setHorizontalAlignment(SwingConstants.CENTER);
		totalLbl.setFont(new Font("Arial", Font.PLAIN, 20));
		tab2Panel.add(totalLbl);
		totalLbl.setVisible(true);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 95, 250, HEIGHT - 300);
		tab2Panel.add(panel);
		panel.setLayout(new GridLayout(8, 2, 0, 5));

		String[] valueNames = new String[] { "10ct", "20ct", "50ct", "1�", "2�", "5�", "10�", "20�" };

		for (int i = Money.MONEY_VALUES.length - 1; i >= 0; i--) {

			JLabel moneyNameLbl = new JLabel(valueNames[i]);
			moneyNameLbl.setFont(new Font("Cambria", Font.PLAIN, 15));
			moneyNameLbl.setHorizontalAlignment(JLabel.CENTER);
			panel.add(moneyNameLbl);

			if (i < 5) {
				spinners[i] = new JSpinner();
				// current value | minimum value | maximum value | step
				spinners[i].setModel(new SpinnerNumberModel(this.tab2[i], this.tab2[i], 1000000, 1));
				spinners[i].setFont(new Font("Cambria", Font.PLAIN, 15));
				panel.add(spinners[i]);
			} else {
				cashAmountLbls[i - spinners.length] = new JLabel(this.tab2[i] + "       ");
				cashAmountLbls[i - spinners.length].setFont(new Font("Cambria", Font.PLAIN, 15));
				cashAmountLbls[i - spinners.length].setHorizontalAlignment(JLabel.RIGHT);
				panel.add(cashAmountLbls[i - spinners.length]);
			}

		}

		JButton updateBtn2 = new JButton("AKTUALISIEREN");
		updateBtn2.setBackground(Color.LIGHT_GRAY);
		updateBtn2.setFont(new Font("Cambria", Font.PLAIN, 15));
		updateBtn2.setBounds(51, 725, 198, 39);
		updateBtn2.addActionListener(this);
		updateBtn2.setActionCommand("updateBtn_2");
		tab2Panel.add(updateBtn2);

		JButton refreshBtn2 = new JButton("Neu Laden");
		refreshBtn2.setBackground(Color.LIGHT_GRAY);
		refreshBtn2.setFocusable(false);
		refreshBtn2.setFont(new Font("Cambria", Font.PLAIN, 15));
		refreshBtn2.setBounds(51, 772, 198, 25);
		refreshBtn2.addActionListener(this);
		refreshBtn2.setActionCommand("refreshBtn_2");
		tab2Panel.add(refreshBtn2);

		tab1Panel.setVisible(true);
		tab1Panel.repaint();

	}

	private void fillSlot(int x, int y, int amount) {
		int index = x * 1 + y * 2;
		Bottle[] delivery = new Bottle[amount];
		for (int i = 0; i < amount; i++) {
			delivery[i] = new Bottle(bottles[index].getPrice().getValue(), bottles[index].getLoc().x,
					bottles[index].getLoc().y);
		}
		slots[y][x].fillSlot(delivery);

	}

	public Automat getAutomat() {
		return automat;
	}

	public void update() {
		updateAdminTab(1, false);
		updateAdminTab(2, false);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String source = e.getActionCommand();

		if (source.contains("_")) {
			int index = Integer.parseInt(source.split("_")[1]);

			if (source.contains("update")) {
				updateAdminTab(index, true);

			} else if (source.contains("refresh")) {
				updateAdminTab(index, false);

			} else {
				int y = Integer.parseInt(source.split("_")[0]);
				int x = index;

				if (tab1[y][x] < pBars[y][x].getMaximum()) {
					tab1[y][x]++;
					pBars[y][x].setValue(tab1[y][x]);
				}
			}

		}
	}

	private void updateAdminTab(int tab, boolean wishforNew) {
		if (tab == 1) {
			boolean needForChange = false;
			for (int y = 0; y < addBtns.length; y++) {
				for (int x = 0; x < addBtns[y].length; x++) {
					if (!wishforNew) {
						tab1[y][x] = slots[y][x].getCurrentAmount();
						pBars[y][x].setValue(tab1[y][x]);
					} else {
						if (tab1[y][x] != slots[y][x].getCurrentAmount()) {
							needForChange = true;
							fillSlot(x, y, tab1[y][x]);
						}
					}
				}
			}
			if (needForChange) {
				automat.setSlots(slots);
				CostumerSideFrame.costumerSideFrame.update();

			}

		} else if (tab == 2) {
			for (int i = 0; i < spinners.length; i++) {
				if (wishforNew) {
					tab2[i] = (int) spinners[i].getValue();
				} else {
					tab2[i] = automat.getAmount(i);

				}

				spinners[i].setModel(new SpinnerNumberModel(tab2[i], tab2[i], 1000000, 1));

			}

			for (int i = 0; i < spinners.length; i++) {
				int times = tab2[i] - automat.getAmount(i);
				for (int k = 0; k < times; k++) {
					automat.inMoney(new Money(Money.MONEY_VALUES[i], i < 5 ? true : false));
				}
			}

			totalLbl.setText(automat.getTotal());
			for (int i = 0; i < cashAmountLbls.length; i++) {
				int index = i + spinners.length;
				if (tab2[index] != automat.getAmount(index)) {
					tab2[index] = automat.getAmount(index);
					cashAmountLbls[i].setText(this.tab2[index] + "       ");
				}
			}

		}
	}

}
