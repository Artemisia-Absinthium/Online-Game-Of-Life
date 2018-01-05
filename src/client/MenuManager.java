package client;

public class MenuManager {
	
	MenuState myCurrentState;
	
	public MenuManager()
	{
		myCurrentState = MenuState.MAIN_MENU;
	}
	
	public void PrintCurrentPage() {
		PrintPage(myCurrentState);
	}
	
	public void PrintPage(MenuState menuState) {
		switch(menuState)
		{
			case MAIN_MENU:
			{
				PrintMainMenu();
			}
			break;
			
			case CREDIT_MENU:
			{
				PrintCreditMenu();
			}
			break;
			
			case WELCOME_MENU:
			{
				PrintWelcomeMenu();
			}
			break;
		}
	}
	
	private void PrintWelcomeMenu()
	{
		System.out.println("..₪.₪.₪.₪...•..");
		System.out.println("..₪.....₪...₪..");
		System.out.println("..₪...₪.₪...₪..");
		System.out.println("..₪...₪.....₪..");
		System.out.println("..•...₪.₪.₪.₪..");
	}
	
	private void PrintMainMenu()
	{
		
	}

	private void PrintCreditMenu()
	{
		
	}

	public MenuSelection GetClientAction() {
		return null;
		//
		/// If we need
	}

}
