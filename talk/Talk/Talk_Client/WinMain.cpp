#include "WinMain.h"

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance,
    LPSTR lpCmdLine, int nCmdShow)
{
	if(!DefineWindow(
		APP_CLASS_NAME, 
		hInstance, 
		ParentMessageProcessingHandler, 
		LoadIcon(hInstance, MAKEINTRESOURCE(IDI_ICON_TALK)),
		LoadIcon(hInstance, MAKEINTRESOURCE(IDI_ICON_TALK)),
		HBRUSH(COLOR_3DSHADOW),
		MAKEINTRESOURCE(IDR_APPMENU),
		LoadCursor(NULL, IDC_ARROW)))
	{
		ShowError("Error", NULL);
		return 0;
	}

	int screenWidth = GetSystemMetrics(SM_CXSCREEN);
	int screenHeight = GetSystemMetrics(SM_CYSCREEN);

	int x = screenWidth - APP_X_WDT;
	int height = screenHeight - 2 * APP_Y_SPC;
	
	if(!InstantiateWindow(
		APP_CLASS_NAME,
		hInstance,
		APP_NAME,
		x,
		APP_Y_SPC,
		APP_X_WDT,
		height,
		NULL,
		nCmdShow))
	{
		ShowError("Error", NULL);
		return 0;
	}

	int winMainRetValue;

	winMainRetValue = DoMessageLoop();

	return winMainRetValue;
}

