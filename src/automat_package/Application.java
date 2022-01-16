package automat_package;

public class Application {
	public static void main(String[] args) {
		new Application();
	}

	public Application() {

		System.out.println("\u20ac");
		System.out.println("\u1E9E");
		Costumer.existingCostumer =  new CreateFrame("Neuer Kunde", false, this, true).getCostumer();

		CreateFrame adminCreateFrame = new CreateFrame("Neuer Automat", true, this, true);
		
		new AdminFrame(adminCreateFrame.getAmounts(0), adminCreateFrame.getAmounts(1));

	}

}
