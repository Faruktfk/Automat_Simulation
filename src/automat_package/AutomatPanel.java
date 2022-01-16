package automat_package;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class AutomatPanel extends JPanel implements MouseListener, ActionListener {

	public final int WIDTH = 400, HEIGHT = 700, DELAY = 100;

	private final int AutoW = 400, AutoH = 700, BotW = 50, BotH = 82;
	private Font fontPrice, fontDisplay;
	private BufferedImage automatImg, bottles[][];
	private Automat automat;
	private String input = "";
	private LinkedList<String> inputQ = new LinkedList<>();
	private InvisibleButton btns[] = new InvisibleButton[5], notBtns[] = new InvisibleButton[4], lightDownBtn;
	private Timer timer;
	private int timerCount = 0;

	private CostumerPanel costumerPanel;

	public AutomatPanel(Automat automat, BufferedImage[][] bottleImgs) {
		this.bottles = bottleImgs;
		timer = new Timer(DELAY, this);
		this.automat = automat;

		setBounds(50, 50, WIDTH, HEIGHT);
		addMouseListener(this);

		initAutomatVisual();

	}

	private void initAutomatVisual() {
		fontPrice = new Font("Cambria", Font.PLAIN, 10);
		fontDisplay = new Font("Cambria", Font.BOLD, 13);
		bottles = new BufferedImage[3][2];
		try {
			automatImg = ImageIO.read(getClass().getResourceAsStream("/Automat_400x700(2).png"));
			for (int y = 0; y < bottles.length; y++) {
				for (int x = 0; x < bottles[y].length; x++) {
					int ix = x + 1;
					int iy = y + 1;
					bottles[y][x] = ImageIO.read(getClass().getResourceAsStream("/Bottle" + iy + ix + "_50x82.png"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		btns[0] = new InvisibleButton("1", 286, 265, 26, 25);
		btns[1] = new InvisibleButton("2", 317, 265, 25, 25);
		btns[2] = new InvisibleButton("3", 346, 265, 26, 25);
		btns[3] = new InvisibleButton("O", 303, 292, 26, 25);
		btns[4] = new InvisibleButton("C", 332, 292, 26, 25);
		notBtns[0] = new InvisibleButton(Automat.PRODUCT_SLOT, 55, 590, 145, 68);
		notBtns[1] = new InvisibleButton(Automat.CHANGE_SLOT, 312, 480, 32, 40);
		notBtns[2] = new InvisibleButton(Automat.COIN_SLOT, 290, 355, 28, 30);
		notBtns[3] = new InvisibleButton(Automat.CASH_SLOT, 333, 355, 50, 30);

	}

	public void update() {
		repaint();
	}

	// RENDERING STUFF
	// -------------------------------------------------------------------------------------------------------------

	@Override
	protected void paintComponent(Graphics a) {
		super.paintComponent(a);
		Graphics2D g = (Graphics2D) a;

		drawAutomat(g);
		drawButtons(g);
		drawDisplay(g);
	}

	private void drawAutomat(Graphics2D g) {
		g.setFont(fontPrice);
		g.fillRect(0, 0, 30, 30);
		g.drawImage(automatImg, 0, 0, AutoW, AutoH, null);

		for (int by = 0; by < bottles.length; by++) {
			for (int bx = 0; bx < bottles[by].length; bx++) {
				Slot currentS = automat.getSlots()[by][bx];
				int amount = currentS.getCurrentAmount();
				double dist = 2;
				double x = (45 + bx * 100) + amount * dist;
				double y = (125 + by * 174) - amount * dist;
				for (int i = 0; i < amount; i++) {
					g.drawImage(bottles[by][bx], (int) (x - i * dist), (int) (y + i * dist), BotW, BotH, null);
					if (i == 0) {
						Money bottlePrice = currentS.getContaining().peek().getPrice();
						g.drawString(bottlePrice.toString(), bx * 100 + 65, by * 175 + 135 + BotH);
					}

				}
			}
		}
	}

	private void drawDisplay(Graphics2D g) {
		g.setColor(Color.yellow);
		g.setFont(fontDisplay);
		String total = automat.getTempBudgetAmount() + "";
		g.drawString(total, 323 - total.length() * 2, 200);
		g.drawString(automat.getOutput(), 318, 225);
	}

	private void drawButtons(Graphics2D g) {
		g.setColor(Color.yellow);
		for (InvisibleButton b : btns) {
			if (b.lightUp) {
				g.fill(b.getRectangle());
			} else {
				g.draw(b.getRectangle());
			}
		}
		for (InvisibleButton b : notBtns) {
			if (b.lightUp) {
				g.draw(b.getRectangle());
			}
		}

		if (automat.getNotification(Automat.CHANGE_SLOT)) {
			int x = 306;
			int y = 475;
			g.setColor(Color.red);
			g.fillArc(x, y, 15, 15, 0, 360);
			g.setColor(Color.white);
			g.setFont(new Font("Arial Black", Font.PLAIN, 10));
			g.drawString("!", x + 6, y + 11);
		}
		if (automat.getNotification(Automat.PRODUCT_SLOT)) {
			int x = 50;
			int y = 590;
			g.setColor(Color.red);
			g.fillArc(x, y, 15, 15, 0, 360);
			g.setColor(Color.white);
			g.setFont(new Font("Arial Black", Font.PLAIN, 10));
			g.drawString("!", x + 6, y + 11);
		}

	}

	// BUTTONS & MOUSE CLICKS
	// -------------------------------------------------------------------------------------------------------------

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(timer)) {
			timerCount++;
			if (timerCount == 2) {
				lightDownBtn.lightUp = false;
				timerCount = 0;
				repaint();
				timer.stop();

			}
		}
	}

	private InvisibleButton getClickedArea(Point click, boolean isBtn) {
		for (InvisibleButton b : isBtn ? btns : notBtns) {
			if (b.checkClicked(click)) {
				return b;
			}
		}
		return null;
	}

	private void manageInput(String value) {
		inputQ.add(value);
		if (inputQ.size() == 3) {
			inputQ.remove();
		}
		input = "";
		for (int i = 0; i < inputQ.toArray().length; i++) {
			input += inputQ.toArray()[i];
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		InvisibleButton b = getClickedArea(e.getPoint(), true);
		InvisibleButton nb = getClickedArea(e.getPoint(), false);
		if (b != null) {
			b.lightUp = true;
			timer.start();
			lightDownBtn = b;
			if (b.name.equals("O")) {
				automat.confirm(costumerPanel);
				inputQ.clear();
				input = "";
				costumerPanel.buttonGroup.clearSelection();
			} else if (b.name.equals("C")) {
				automat.cancel(costumerPanel);
				inputQ.clear();
				input = "";
				costumerPanel.buttonGroup.clearSelection();
			} else {
				manageInput(b.name);

				automat.setInput(input);
			}
			repaint();
		}
		if (nb != null) {
			nb.lightUp = true;
			timer.start();
			lightDownBtn = nb;
			if (nb.name.equals(Automat.PRODUCT_SLOT)) {

				automat.getProduct(costumerPanel);

			} else if (nb.name.equals(Automat.CHANGE_SLOT)) {

				automat.getChange(costumerPanel);

			} else if (nb.name.equals(Automat.COIN_SLOT)) {

				automat.insertMoney(costumerPanel.getClickedBtnIndex(), costumerPanel, true);

			} else if (nb.name.equals(Automat.CASH_SLOT)) {
				automat.insertMoney(costumerPanel.getClickedBtnIndex(), costumerPanel, false);
			}

			repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (this.costumerPanel == null) {
			this.costumerPanel = CostumerPanel.costumerPanel;
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
