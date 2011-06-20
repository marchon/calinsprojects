#include <windows.h>
#include <map>
using namespace std;

#include "resource.h"

#include "..\Common Files\WindowCreation.h"

#include "..\Common Files\TalkProtocol.h"

#define DIALOG_POS_Y		100

#define UNCONNECTED								0
#define TRYING_TO_CONNECT						1
#define CONNECTION_ACCEPTED_GETTING_LIST		2
#define CONNECTED								3

#define GOT_MSG									(WM_USER + 15)

LRESULT CALLBACK ParentMessageProcessingHandler(HWND, UINT, WPARAM, LPARAM);
LRESULT CALLBACK InstantTalkProcessingHandler(HWND, UINT, WPARAM, LPARAM);
LRESULT CALLBACK ConnectDialogHandler(HWND, UINT, WPARAM, LPARAM);