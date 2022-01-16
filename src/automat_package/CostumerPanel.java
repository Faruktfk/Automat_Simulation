package automat_package;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class CostumerPanel extends JPanel {

	public static CostumerPanel costumerPanel;
	public final int WIDTH = 450, HEIGHT = 700;
	private JLabel totalLbl, itemAmountLbls[] = new JLabel[6];

	private JToggleButton moneyBtns[];
	ButtonGroup buttonGroup;

	private BufferedImage[][] bottleImgs;
	private Costumer costumer;

	public CostumerPanel(Costumer costumer, BufferedImage[][] bottleImgs) {

		if (costumerPanel == null) {
			costumerPanel = this;

			this.bottleImgs = bottleImgs;
			this.costumer = costumer;

			moneyBtns = new JToggleButton[Money.MONEY_NAMES.length];

			setLayout(null);
			setBounds(490, 50, WIDTH, HEIGHT);
			setBackground(new Color(170, 250, 255));
			initPanel();

		}

	}

	private void initPanel() {
		Font font = new Font("Arial Black", Font.PLAIN, 20);
		Font moneyFont = new Font("Cambria", Font.PLAIN, 10);
		Font itemsFont = new Font("Cambria", Font.PLAIN, 15);

		JLabel budgetLbl = new JLabel("BUDGET:");
		budgetLbl.setBounds(10, 0, WIDTH, 60);
		budgetLbl.setFont(font);
		add(budgetLbl);
		budgetLbl.setVisible(true);

		totalLbl = new JLabel(costumer.getTotal());
		totalLbl.setBounds(150, 0, WIDTH - 151, 60);
		totalLbl.setHorizontalAlignment(SwingConstants.CENTER);
		totalLbl.setFont(font);
		add(totalLbl);
		totalLbl.setVisible(true);

		buttonGroup = new ButtonGroup();

		for (int i = 0; i < moneyBtns.length; i++) {

			int x = 10 + (i % 3) * (143 - (i / 3) * 15 + (i / 6) * 30) + (i / 3) * 23 + (i / 6) * 40;
			int y = (i / 3) * 66 + 95 + ((i / 3) % 2) * 10;
			int w = 125 - (i / 3) * 7;
			int h = 45 - (i / 3) * 7;
			int index = i < 3 ? i + 5 : i - 3;

			moneyBtns[index] = new JToggleButton(costumer.getAmount(index) + " x " + Money.MONEY_NAMES[index]);
			buttonGroup.add(moneyBtns[index]);
			moneyBtns[index].setBackground(Color.WHITE);
			moneyBtns[index].setFont(moneyFont);
			moneyBtns[index].setFocusable(false);
			moneyBtns[index].setBounds(x, y, w, h);
			moneyBtns[index].setActionCommand("moneyN_" + index);
			add(moneyBtns[index]);
		}

		// ----

		JLabel cartLbl = new JLabel("GEKAUFT:");
		cartLbl.setBounds(10, HEIGHT / 2 - 20, WIDTH, 60);
		cartLbl.setFont(font);
		add(cartLbl);
		cartLbl.setVisible(true);

		JPanel boughtItemsPanel = new JPanel();
		boughtItemsPanel.setBounds(34, 388, 365, 279);
		boughtItemsPanel.setBackground(new Color(170, 230, 255));
		add(boughtItemsPanel);
		boughtItemsPanel.setLayout(new GridLayout(2, 3, 5, 5));

		for (int i = 0; i < 6; i++) {
			JPanel itemBlockP = new JPanel();
			boughtItemsPanel.add(itemBlockP);
			itemBlockP.setBackground(new Color(210, 230, 255));
			itemBlockP.setLayout(new BorderLayout(0, 0));

			JLabel itemImgLbl = new JLabel(new ImageIcon(bottleImgs[i % 3][i / 3]));
			itemImgLbl.setVerticalAlignment(JLabel.CENTER);
			itemBlockP.add(itemImgLbl, BorderLayout.CENTER);

			itemAmountLbls[i] = new JLabel("0");
			itemAmountLbls[i].setFont(itemsFont);
			itemAmountLbls[i].setHorizontalAlignment(JLabel.CENTER);
			itemBlockP.add(itemAmountLbls[i], BorderLayout.SOUTH);

		}

	}

	public int getClickedBtnIndex() {
		for (int i = 0; i < moneyBtns.length; i++) {
			if (moneyBtns[i].isSelected() && costumer.getAmount(i) > 0) {
				return i;
			}
		}
		return -1;

	}

	public void update() {
		if (Costumer.existingCostumer != costumer) {
			costumer = Costumer.existingCostumer;
		}
		totalLbl.setText(costumer.getTotal());
		for (int i = 0; i < moneyBtns.length; i++) {
			int amount = costumer.getAmount(i);
			moneyBtns[i].setText(amount + " x " + Money.MONEY_NAMES[i]);
			if (amount == 0) {
				moneyBtns[i].setEnabled(false);
				buttonGroup.clearSelection();
			} else {
				moneyBtns[i].setEnabled(true);
			}
		}

		for (int i = 0; i < itemAmountLbls.length; i++) {
			itemAmountLbls[i].setText(costumer.getBottleAmount(i / 3, i % 3) + "");

		}

		repaint();

	}

	public Costumer getCostumer() {
		return costumer;
	}

	public void setCostumer(Costumer costumer) {
		this.costumer = costumer;
	}

	public void adjustCostumer(Costumer costumer) {
		this.costumer = costumer;
		this.removeAll();
		initPanel();
		this.repaint();
	}

}
