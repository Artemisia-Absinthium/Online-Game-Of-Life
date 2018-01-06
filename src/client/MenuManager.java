package client;

/**
 * Menu manager.
 * Display the menus and handle the client inputs
 * @author Amélia Chavot
 *
 */
public class MenuManager {
	//
	/// Class attributs
	MenuState myCurrentState;
	
	//
	/// Default constructor
	public MenuManager()
	{
		myCurrentState = MenuState.MAIN_MENU;
	}
	
	/**
	 * Print the current menu page
	 */
	public void PrintCurrentPage() {
		PrintPage(myCurrentState);
	}
	
	/**
	 * Print a specified page and update the menu state.
	 * @param menuState the menuState to display.
	 */
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
			case CONNECTION_MENU:
			{
				PrintWelcomeMenu();
			}
			default:
				break;
		}
	}
	
	/**
	 * Print the welcome menu
	 */
	private void PrintWelcomeMenu()
	{
		System.out.println("..₪.₪.₪.₪...•..");
		System.out.println("..₪.....₪...₪..");
		System.out.println("..₪...₪.₪...₪..");
		System.out.println("..₪...₪.....₪..");
		System.out.println("..•...₪.₪.₪.₪..");
	}
	
	/**
	 * Print the main menu
	 */
	private void PrintMainMenu()
	{
		
	}
	
	/**
	 * Print the credit menu
	 */
	private void PrintCreditMenu()
	{
		
	}

	/**
	 * Wait for a client action
	 * @return the action as a {@link MenuSelection} enum
	 */
	public MenuSelection GetClientAction() {
		return null;
		//
		/// If we need
	}

}
