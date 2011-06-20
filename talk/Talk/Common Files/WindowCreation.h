#include <windows.h>
#include <vector>
using namespace std;

#define ERR_CAP "Error"

extern vector<HWND> dialogVector;

BOOL DefineWindow(LPCSTR, HINSTANCE, WNDPROC, HICON, HICON, HBRUSH, LPCSTR, HCURSOR);
HWND InstantiateWindow(LPCSTR, HINSTANCE, LPCSTR, int, int, int, int, HWND, int);

HWND CrateDialogFromTemplate(HINSTANCE, LPSTR, HWND, DLGPROC);
void DestroyDialog(HWND);

int DoMessageLoop();
void ShowError(LPCSTR, HWND);