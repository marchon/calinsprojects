#include "..\Common Files\WindowCreation.h"
#include "MessageProcessing.h"

#include "resource.h"

int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance,
    LPSTR lpCmdLine, int nCmdShow)
{
	CrateDialogFromTemplate(hInstance, MAKEINTRESOURCE(ID_PARENT_DIALOG), 
		NULL, DLGPROC(MainDialogHandler));

	return DoMessageLoop();
}
