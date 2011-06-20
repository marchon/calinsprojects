#include "WindowCreation.h"

vector<HWND> dialogVector;

BOOL DefineWindow(LPCSTR className, HINSTANCE appInstance, WNDPROC messageHandler, HICON smallIcon, 
				  HICON bigIcon, HBRUSH backgroundColor, LPCSTR menuName, HCURSOR cursor)
{
	WNDCLASSEX windowClass;

	windowClass.cbSize        = sizeof(WNDCLASSEX);
    windowClass.style         = CS_DBLCLKS;
    windowClass.cbClsExtra    = 0;
    windowClass.cbWndExtra    = 0;

	windowClass.lpszClassName = className;
	windowClass.hInstance     = appInstance;	
	windowClass.lpfnWndProc   = messageHandler;
	windowClass.hIconSm       = smallIcon;
    windowClass.hIcon         = bigIcon;
	windowClass.hbrBackground = backgroundColor;
	windowClass.lpszMenuName  = menuName;
    windowClass.hCursor       = cursor;

	if(!RegisterClassEx(&windowClass))
        return FALSE;

	return TRUE;
}

HWND InstantiateWindow(LPCSTR className, HINSTANCE appInstance, LPCSTR caption,
					   int x, int y, int width, int height, HWND parent, int show)
{
	HWND windowHandle;

    windowHandle = CreateWindowExA(
		WS_EX_CLIENTEDGE,
		className,
		caption,
		WS_OVERLAPPEDWINDOW,
		x, y, width, height,
		parent, NULL, appInstance, NULL);

    if(windowHandle == NULL)
		return NULL;

    ShowWindow(windowHandle, show);
    UpdateWindow(windowHandle);

	return windowHandle;
}

HWND CrateDialogFromTemplate(HINSTANCE appInst, LPSTR resource, HWND parentHandle, DLGPROC handlerPtr)
{
	HWND dlgHandle;

	if(!(dlgHandle = CreateDialog(appInst, resource,
				parentHandle, handlerPtr)))
				return NULL;

	dialogVector.push_back(dlgHandle);

	return dlgHandle;
}
void DestroyDialog(HWND dlgHandle)
{
	size_t i;
	size_t size = dialogVector.size();

	for(i = 0; i < size; i++)
		if(dialogVector[i] == dlgHandle)
		{
			vector<HWND>::iterator itr = dialogVector.begin() + i;
			dialogVector.erase(itr);
			break;
		}

	DestroyWindow(dlgHandle);
}

int DoMessageLoop()
{
	MSG message;

	while(GetMessage(&message, NULL, 0, 0) > 0)
    {
		size_t i;
		size_t size = dialogVector.size();
		bool isDlgMsg = false;
		
		for(i = 0; i < size; i++)
			if(IsDialogMessage(dialogVector[i], &message))
			{
				isDlgMsg = true;
				break;
			}

		if(!isDlgMsg)
		{
			TranslateMessage(&message);
			DispatchMessage(&message);
		}
    }

    return int(message.wParam);
}

void ShowError(LPCSTR message, HWND parentHandle)
{
	MessageBox(parentHandle, message, ERR_CAP, MB_OK | MB_ICONERROR);
}