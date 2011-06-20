#include "MessageProcessing.h"
#include "..\Common Files\TalkMessageProtocol.h"

HWND mainDialogHandle = NULL;
HWND currentDialogHandle = NULL;
u_int connectionState = UNCONNECTED;
SOCKET serverSocket = NULL;
char *nickName = NULL;

void resetNickName()
{
	if(nickName)
	{
		delete[] nickName;
		nickName = NULL;
	}
}

void setNickName(char *nick)
{
	if(nickName)
		resetNickName();

	nickName = new char[strlen(nick) + 1];
	strcpy(nickName, nick);
}


struct ltstr
{
  bool operator()(const char* s1, const char* s2) const
  {
    return strcmp(s1, s2) < 0;
  }
};
map<char *, HWND, ltstr> nickList;
map<HWND, char *> openConversationList;

void addNick(char *nick)
{
	char *newNick = new char[strlen(nick) + 1];
	strcpy(newNick, nick);

	nickList[newNick] = NULL;
}
void removeNick(char *nick)
{
	map<char *, HWND, ltstr>::iterator itr;

	if((itr = nickList.find(nick)) != nickList.end())
	{
		DestroyDialog(itr->second);
		delete[] itr->first;

		nickList.erase(itr);
	}
}
void clearNickList()
{
	map<char *, HWND, ltstr>::iterator itr, itre;
	itre = nickList.end();

	for(itr = nickList.begin(); itr != itre; itr++)
	{
		DestroyDialog(itr->second);
		delete[] itr->first;
	}

	nickList.clear();
}

u_int selectedEntry = 0;

HBITMAP talkPic = NULL;


/* Connect Dialog */
BOOL CreateConnectDialog(HWND hwnd)
{
	if(currentDialogHandle)
		return FALSE;

	HWND connectDialog;
	RECT mainWinRect;
	RECT connWinRect;

	GetClientRect(hwnd, &mainWinRect);			

	connectDialog = CrateDialogFromTemplate(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_DIALOG_CONNECT),
		hwnd, DLGPROC(ConnectDialogHandler));

	if(connectDialog == NULL)
		return FALSE;
	else
	{
		currentDialogHandle = connectDialog;
		GetClientRect(connectDialog, &connWinRect);

		int x = mainWinRect.right / 2 - connWinRect.right / 2;

		SetWindowPos(connectDialog, NULL, x, DIALOG_POS_Y, 
			0, 0, SWP_NOSIZE);
	}
}

void SizeConnectDialog(HWND hwnd)
{
	if(currentDialogHandle)
	{
		RECT mainWinRect;
		RECT connWinRect;

		GetClientRect(hwnd, &mainWinRect);
		GetClientRect(currentDialogHandle, &connWinRect);

		int x = mainWinRect.right / 2 - connWinRect.right / 2;

		SetWindowPos(currentDialogHandle, NULL, x, DIALOG_POS_Y, 0, 0, SWP_NOSIZE);
	}
}

void DistroyConnectDialog()
{
	DestroyDialog(currentDialogHandle);
	currentDialogHandle = NULL;
}

/* Connect Dialog */


void GetTextandAddLine(HWND hParent, int IDC, char *Line)
{
    HWND hEdit = GetDlgItem(hParent, IDC);
	int nTxtLen = GetWindowTextLength(hEdit);
	SendMessage(hEdit, EM_SETSEL, nTxtLen, nTxtLen);
	SendMessage(hEdit, EM_REPLACESEL, 0, (LPARAM)Line);
	SendMessage(hEdit, EM_SCROLLCARET, 0, 0);
}

LRESULT CALLBACK ParentMessageProcessingHandler(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
    switch(msg)
    {	
	case WM_CREATE:
		{
			mainDialogHandle = hwnd;

			if(!TalkInitWinSock())
			{
				ShowError("An error ocured!\nThe program will now exit.", hwnd);
				DestroyWindow(hwnd);
			}

			if(!CreateConnectDialog(hwnd))
			{
				ShowError("The Application has encountered an internal error!", hwnd);
				DestroyWindow(hwnd);
			}
			else
			{
				talkPic = LoadBitmap(GetModuleHandle(NULL), MAKEINTRESOURCE(IDB_TALK));
				if(talkPic == NULL)
				{
					ShowError("The Application has encountered an internal error!", hwnd);
					DestroyWindow(hwnd);
				}
			}
		}
		break;

	case WM_SIZE:
			SizeConnectDialog(hwnd);
		break;

	case WM_SOCKET_MESSAGE:	//aici sunt mesajele winsock
		switch(WSAGETSELECTEVENT(lParam))
		{
		case FD_CONNECT: //server Connect
			break;
		case FD_READ:
			switch(connectionState)
			{
			case TRYING_TO_CONNECT:
				{
					vector<char *> *msgList;

					if((msgList = TalkReceiveMessage(wParam)) != NULL)
					{
						if(!msgList->empty())
						{
							if(TALK_MSG_BOOL_IS_MSG((*msgList)[0], TALK_MSG_TYPE_BEGIN_NICK_LIST))
							{
								size_t size = msgList->size();

								for(size_t i = 1; i < size; i++)
								{
									if(i == size - 1 && TALK_MSG_BOOL_IS_MSG((*msgList)[i], TALK_MSG_TYPE_END_NICK_LIST))
									{
										connectionState = CONNECTED;
										//TalkSendMessage(serverSocket, "Connected", 10);
										DistroyConnectDialog();
										SetClassLong(hwnd, GCL_HBRBACKGROUND, LONG(GetStockObject(WHITE_BRUSH)));
										InvalidateRect(hwnd, NULL, TRUE);
									}
									else
										addNick((*msgList)[i]);
								}

								if(connectionState != CONNECTED)
									connectionState = CONNECTION_ACCEPTED_GETTING_LIST;
							}

							else
							{
								ShowError("Nick name already in use!", hwnd);
								connectionState = UNCONNECTED;
							}
						}
					}
					else
					{
						ShowError("TRYING_TO_CONNECT error!", hwnd);
						connectionState = UNCONNECTED;
					}
				}
				break;
			case CONNECTION_ACCEPTED_GETTING_LIST:
				{
					vector<char *> *msgList;

					if((msgList = TalkReceiveMessage(wParam)) != NULL)
					{
						size_t size = msgList->size();

						for(size_t i = 0; i < size; i++)
							if(i == size - 1 && TALK_MSG_BOOL_IS_MSG((*msgList)[i], TALK_MSG_TYPE_END_NICK_LIST))
							{
								connectionState = CONNECTED;
								//TalkSendMessage(serverSocket, "Connected", 10);
								DistroyConnectDialog();
								SetClassLong(hwnd, GCL_HBRBACKGROUND, LONG(GetStockObject(WHITE_BRUSH)));
								InvalidateRect(hwnd, NULL, TRUE);
							}
							else
								addNick((*msgList)[i]);
					}
					else
					{
						ShowError("CONNECTION_ACCEPTED_GETTING_LIST error!", hwnd);
						connectionState = UNCONNECTED;
					}
				}
				break;
			case CONNECTED:
				{
					vector<char *> *msgList;

					if((msgList = TalkReceiveMessage(wParam)) != NULL)
					{
						size_t size = msgList->size();

						for(size_t i = 0; i < size; i++)
						{
							switch(TALK_MSG_VAL_MSG_TYPE((*msgList)[i]))
							{
							case TALK_MSG_TYPE_NORMAL_MSG:
								{
									char *src;
									char *msg;
									map<char *, HWND, ltstr>::iterator itr;

									TALK_GET_MSG_NORMAL_MSG_DEST_MSG((*msgList)[i], src, msg);

									itr = nickList.find(src);

									if(itr->second == NULL)
									{
										itr->second = CrateDialogFromTemplate(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_DIALOG_CHAT),
											NULL, DLGPROC(InstantTalkProcessingHandler));
										
										if(itr->second == NULL)
										{
											ShowError("The Application has encountered an internal error!", hwnd);
											break;
										}
										else
										{
											char instDlgCapt[256];
											strcpy(instDlgCapt, itr->first);
											strcat(instDlgCapt, " - Instant Talk");

											SetWindowText(itr->second, instDlgCapt);

											openConversationList[itr->second] = itr->first;
										}
									}

									SendMessage(itr->second, GOT_MSG, (WPARAM)(msg), 0);
								}
								break;
							case TALK_MSG_TYPE_CLIENT_CONN_NOTIF:
								addNick(TALK_GET_MSG_CLIENT_CONN_NOTIF((*msgList)[i]));
								InvalidateRect(hwnd, NULL, TRUE);
								break;
							case TALK_MSG_TYPE_CLIENT_DISC_NOTIF:
								removeNick(TALK_GET_MSG_CLIENT_CONN_NOTIF((*msgList)[i]));
								InvalidateRect(hwnd, NULL, TRUE);
								break;
							}
						}
					}
				}
				break;
			}
			break;
		case FD_CLOSE:
			{
				if(serverSocket == wParam)
				{
					resetNickName();
					TalkShutDownConnection(wParam);

					if(connectionState == CONNECTED)
					{
						connectionState = UNCONNECTED;
						if(!CreateConnectDialog(hwnd))
						{
							ShowError("The Application has encountered an internal error!", hwnd);
							DestroyWindow(hwnd);
						}

						SetClassLong(hwnd, GCL_HBRBACKGROUND, LONG(COLOR_3DSHADOW));
						InvalidateRect(hwnd, NULL, TRUE);

						ShowError("Connection with server lost!", hwnd);
					}
				}
			}
			break;
		}
		break;

	case WM_SYSKEYDOWN:
	case WM_KEYDOWN:
		if(connectionState == CONNECTED)
		{
			switch (wParam) 
			{
			case VK_UP:
				if(selectedEntry > 0)
				{
					selectedEntry --;
					InvalidateRect(hwnd, NULL, TRUE);
				}
				break;
			case VK_DOWN:
				if(selectedEntry < nickList.size() - 1)
				{
					selectedEntry ++;
					InvalidateRect(hwnd, NULL, TRUE);
				}
				break;
			case VK_RETURN:
				if(connectionState == CONNECTED)
				{
					map<char *, HWND, ltstr>::iterator itr = nickList.begin();
					for(unsigned int i = 0; i < selectedEntry; i++, itr++);
					
					if(itr->second == NULL)
					{
						itr->second = CrateDialogFromTemplate(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_DIALOG_CHAT),
							NULL, DLGPROC(InstantTalkProcessingHandler));
						
						if(itr->second == NULL)
						{
							ShowError("The Application has encountered an internal error!", hwnd);
						}
						else
						{
							char instDlgCapt[256];
							strcpy(instDlgCapt, itr->first);
							strcat(instDlgCapt, " - Instant Talk");

							SetWindowText(itr->second, instDlgCapt);
						}
					}
					else
						SetFocus(itr->second);
				}
				break;
			}
		}
		break;

	case WM_LBUTTONDOWN:
			if(connectionState == CONNECTED)
			{
				BITMAP bitmap;
				GetObject(talkPic, sizeof(bitmap), &bitmap);

				int i = (HIWORD(lParam) - 5) / (bitmap.bmHeight + 5);
				if(i < nickList.size())
				{
					selectedEntry = i;
					InvalidateRect(hwnd, NULL, TRUE);
				}
			}
		break;
	
	case WM_LBUTTONDBLCLK:
		if(connectionState == CONNECTED)
		{
			map<char *, HWND, ltstr>::iterator itr = nickList.begin();
			for(unsigned int i = 0; i < selectedEntry; i++, itr++);
			
			if(itr->second == NULL)
			{
				itr->second = CrateDialogFromTemplate(GetModuleHandle(NULL), MAKEINTRESOURCE(IDD_DIALOG_CHAT),
					NULL, DLGPROC(InstantTalkProcessingHandler));
				
				if(itr->second == NULL)
				{
					ShowError("The Application has encountered an internal error!", hwnd);
				}
				else
				{
					char instDlgCapt[256];
					strcpy(instDlgCapt, itr->first);
					strcat(instDlgCapt, " - Instant Talk");

					SetWindowText(itr->second, instDlgCapt);

					openConversationList[itr->second] = itr->first;
				}
			}
			else
				SetFocus(itr->second);
		}
		break;

	case WM_PAINT://aici e paintu
		{
			PAINTSTRUCT paintStr;

			HDC hdc = BeginPaint(hwnd, &paintStr);

			if(connectionState == CONNECTED)
			{
				BITMAP bitmap;

				HDC hdcMem = CreateCompatibleDC(hdc);
				HBITMAP oldBitmap = (HBITMAP)SelectObject(hdcMem, talkPic);

				GetObject(talkPic, sizeof(bitmap), &bitmap);
				
				//size_t listSize = nickList.size();
				map<char *, HWND, ltstr>::iterator itr, itrend;
				itrend = nickList.end();
				unsigned int i = 0;
				for(itr = nickList.begin(); itr != itrend; itr++, i++)
				{
					BitBlt(hdc, 15, 5 + (bitmap.bmHeight + 5) * i, bitmap.bmWidth, bitmap.bmHeight, hdcMem, 0, 0, SRCCOPY);
					if(i == selectedEntry)
					{
						SetTextColor(hdc, RGB(255, 255, 255));
						SetBkColor(hdc, RGB(100, 20, 180));
					}
					TextOut(hdc, 20 + bitmap.bmWidth, 5 + (bitmap.bmHeight + 5) * i, (*itr).first, strlen((*itr).first));
					if(i == selectedEntry)
					{
						SetBkColor(hdc, RGB(255, 255, 255));
						SetTextColor(hdc, RGB(0, 0, 0));
					}
				}

				SelectObject(hdcMem, oldBitmap);
				DeleteDC(hdcMem);
			}

			EndPaint(hwnd, &paintStr);
		}
		break;
    case WM_CLOSE:
        DestroyWindow(hwnd);
    break;
    case WM_DESTROY:
		{
			//daca se inchide aplicatia inchide toate conversatiile si elibereaza memoria 
			//ocupata de lista de nickuri
			resetNickName();
			clearNickList();

			if(talkPic != NULL)
			{
				DeleteObject(talkPic);
			}

			TalkShutDownWinSock();

			PostQuitMessage(0);
		}
    break;
    default:
        return DefWindowProc(hwnd, msg, wParam, lParam);
    }

    return FALSE;
}



LRESULT CALLBACK InstantTalkProcessingHandler(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	switch(msg)
	{
	case WM_CLOSE:
		DestroyDialog(hwnd);
		break;
	case WM_DESTROY:
		{
			map<HWND, char*>::iterator itr;

			itr = openConversationList.find(hwnd);
			nickList[itr->second] = NULL;
			openConversationList.erase(itr);
		}
		break;
	case WM_COMMAND:
        switch(LOWORD(wParam))
        {
		case ID_BUTTON_SEND:
			{
				int lenMsg = GetWindowTextLength(GetDlgItem(hwnd, IDC_EDIT_SEND));

				if(lenMsg > 0)
				{
					char *msgData = new char[lenMsg + 1];

					GetDlgItemText(hwnd, IDC_EDIT_SEND, msgData, lenMsg + 1);
					
					GetTextandAddLine(hwnd, IDC_EDIT_CONVERSATION, nickName);
					GetTextandAddLine(hwnd, IDC_EDIT_CONVERSATION, ": ");
					GetTextandAddLine(hwnd, IDC_EDIT_CONVERSATION, msgData);
					GetTextandAddLine(hwnd, IDC_EDIT_CONVERSATION, "\r\n");

					char *dest_msg;
					char *dest = openConversationList[hwnd];

					dest_msg = new char[TALK_MSG_VAL_MAKE_NORMAL_LEN(msgData, dest)];
					
					TALK_MAKE_MSG_NORMAL_MSG(dest, msgData, dest_msg);

					TalkSendMessage(serverSocket, dest_msg, 
						TALK_MSG_VAL_LENGTH(dest_msg));

					delete[] dest_msg;

					SetDlgItemText(hwnd, IDC_EDIT_SEND, "");

					delete[] msgData;
				}
			}
			break;
		}
		break;
	case GOT_MSG:
		{
					GetTextandAddLine(hwnd, IDC_EDIT_CONVERSATION, openConversationList[hwnd]);
					GetTextandAddLine(hwnd, IDC_EDIT_CONVERSATION, ": ");
					GetTextandAddLine(hwnd, IDC_EDIT_CONVERSATION, (char *)(wParam));
					GetTextandAddLine(hwnd, IDC_EDIT_CONVERSATION, "\r\n");
		}
		break;
	default:
		return FALSE;
	}
	
	return TRUE;
}

LRESULT CALLBACK ConnectDialogHandler(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	switch(msg)
	{
	case WM_COMMAND:
		switch(LOWORD(wParam))
		{
		case IDC_CONN_BUTTON:

			int lenIp = GetWindowTextLength(GetDlgItem(hwnd, IDC_EDIT_IP_NAME));

			if(lenIp > 0)
			{
				int lenNick = GetWindowTextLength(GetDlgItem(hwnd, IDC_EDIT_NICK_NAME));

				if(lenNick > 0)
				{
					char *ipData, *nickData;

					ipData = new char[lenIp + 1];
					nickData = new char[lenNick + 1];

					GetDlgItemText(hwnd, IDC_EDIT_IP_NAME, ipData, lenIp + 1);
					GetDlgItemText(hwnd, IDC_EDIT_NICK_NAME, nickData, lenNick + 1);
					
					if((serverSocket = TalkConnectToServer(ipData, mainDialogHandle)) != NULL)
					{
						connectionState = TRYING_TO_CONNECT;
						TalkSendMessage(serverSocket, nickData, lenNick + 1);
						setNickName(nickData);
					}
					else
						ShowError("Unable to connect to remote host!", mainDialogHandle);
					
					delete[] nickData;
					delete[] ipData;
				}
				else
					ShowError("Type in a nick name first!", mainDialogHandle);
			}
			else
				ShowError("Type in an ip adress first!", mainDialogHandle);
			break;
		}
		break;
	default:
		return FALSE;
	}
	
	return TRUE;
}