#include "MessageProcessing.h"
#include "..\Common Files\WindowCreation.h"
#include "..\Common Files\TalkProtocol.h"
#include "..\Common Files\TalkMessageProtocol.h"

#include "resource.h"

#include <map>
#include <vector>
using namespace std;

SOCKET newConnection = NULL;

struct ltstr
{
  bool operator()(const char* s1, const char* s2) const
  {
    return strcmp(s1, s2) < 0;
  }
};
map<char *, SOCKET, ltstr> nickList;
map<SOCKET, char *> socketList;

BOOL addClient(char *nick, SOCKET socket)
{
	if(nickList.find(nick) != nickList.end())
		return FALSE;

	char *newNick = new char[strlen(nick) + 1];
	strcpy(newNick, nick);

	nickList[newNick] = socket;
	socketList[socket] = newNick;
	
	return TRUE;
}

char *getNickFromSocket(SOCKET socket)
{
	return socketList[socket];
}

void removeClient(SOCKET socket)
{
	map<SOCKET, char *>::iterator itr;

	if((itr = socketList.find(socket)) == socketList.end())
		return;

	char *ch = itr->second;
	socketList.erase(itr);

	map<char *, SOCKET, ltstr>::iterator itr1;

	if((itr1 = nickList.find(ch)) == nickList.end())
		return;

	nickList.erase(itr1);

	delete[] ch;
}

void clearClientList()
{
	map<char *, SOCKET, ltstr>::iterator itr, itre;
	itre = nickList.end();

	for(itr = nickList.begin(); itr != itre; itr++)
		delete[] itr->first;

	nickList.clear();
	socketList.clear();
}

void GetTextandAddLine(HWND hParent, int IDC, char *Line)
{
    HWND hEdit = GetDlgItem(hParent, IDC);
	int nTxtLen = GetWindowTextLength(hEdit);
	SendMessage(hEdit, EM_SETSEL, nTxtLen, nTxtLen);
	SendMessage(hEdit, EM_REPLACESEL, 0, (LPARAM)Line);
	SendMessage(hEdit, EM_SCROLLCARET, 0, 0);
}

LRESULT CALLBACK MainDialogHandler(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	switch(msg)
	{
	case WM_INITDIALOG:
		if(!TalkInitWinSock())
		{
			ShowError("An error ocured!\nThe program will now exit.", hwnd);
			DestroyDialog(hwnd);
		}

		if(!TalkSetUpServerForAccept(hwnd))
		{
			ShowError("An error ocured!\nThe program will now exit.", hwnd);
			DestroyDialog(hwnd);
		}
		break;

	case WM_SOCKET_MESSAGE:
		switch(WSAGETSELECTEVENT(lParam))
		{
			case FD_READ:
			{	
				if(wParam == newConnection)
				{
					vector<char *> *msgList;

					if((msgList = TalkReceiveMessage(wParam)) != NULL)
					{
						if(!msgList->empty())
						{
							if(addClient((*msgList)[0], wParam))
							{
								map<char *, SOCKET, ltstr>::iterator itr, itre;
								itre = nickList.end();
	
								char *newClientNotify = new char[TALK_MSG_VAL_MAKE_NOTIF_LEN((*msgList)[0])];
								TALK_MAKE_MSG_CLIENT_CONN_NOTIF((*msgList)[0], newClientNotify);

								TalkSendMessage(wParam, TALK_MSG_TYPE_BEGIN_NICK_LIST, 
									TALK_MSG_VAL_LENGTH(TALK_MSG_TYPE_BEGIN_NICK_LIST));

								for(itr = nickList.begin(); itr != itre; itr++)
								{
									TalkSendMessage(wParam, itr->first, TALK_MSG_VAL_LENGTH(itr->first));
									
									if(itr->second != wParam)
										TalkSendMessage(itr->second, newClientNotify, 
											TALK_MSG_VAL_LENGTH(newClientNotify));
								}

								TalkSendMessage(wParam, TALK_MSG_TYPE_END_NICK_LIST, 
									TALK_MSG_VAL_LENGTH(TALK_MSG_TYPE_END_NICK_LIST));

								delete[] newClientNotify;

								GetTextandAddLine(hwnd, IDC_EDIT, "Nick received: ");
								GetTextandAddLine(hwnd, IDC_EDIT, (*msgList)[0]);
								GetTextandAddLine(hwnd, IDC_EDIT, "\nConnected!\n");
							}
							else
							{
								TalkSendMessage(wParam, TALK_MSG_TYPE_BAD_NICKNAME, 
									TALK_MSG_VAL_LENGTH(TALK_MSG_TYPE_BAD_NICKNAME));
								TalkShutDownConnection(wParam);

								GetTextandAddLine(hwnd, IDC_EDIT, "\nBad nick name. Connection closed!\n");
							}

							newConnection = NULL;
						}
					}
					else
					{
						ShowError("FD_READ error!", hwnd);
						TalkShutDownConnection(newConnection);
						newConnection = NULL;
					}
				}
				else
				{
					vector<char *> *msgList;

					if((msgList = TalkReceiveMessage(wParam)) != NULL)
					{
						char *dest;
						char *msg;
						char *src;
						char *dest_msg;

						size_t size = msgList->size();

						for(size_t i = 0; i < size; i++)
						{
							TALK_GET_MSG_NORMAL_MSG_DEST_MSG((*msgList)[i], dest, msg);
							
							src = getNickFromSocket(wParam);

							dest_msg = new char[TALK_MSG_VAL_MAKE_NORMAL_LEN(msg, src)];

							TALK_MAKE_MSG_NORMAL_MSG(src, msg, dest_msg);

							TalkSendMessage(nickList[dest], dest_msg, 
								TALK_MSG_VAL_LENGTH(dest_msg));

							delete[] dest_msg;
						}
					}
				}
			} 
			break;
			case FD_WRITE:
			{
			}
			break;
			case FD_ACCEPT:
				newConnection = TalkOnAcceptConnection(wParam);
				GetTextandAddLine(hwnd, IDC_EDIT, "New connection accepted. Waiting nick...\n");
			break;
			case FD_CLOSE:
				char *name = getNickFromSocket(wParam);

				map<char *, SOCKET, ltstr>::iterator itr, itre;
				itre = nickList.end();

				char *clientLeftNotify = new char[TALK_MSG_VAL_MAKE_NOTIF_LEN(name)];
				TALK_MAKE_MSG_CLIENT_DISC_NOTIF(name, clientLeftNotify);

				for(itr = nickList.begin(); itr != itre; itr++)
				{
					if(wParam != itr->second)
						TalkSendMessage(itr->second, clientLeftNotify, 
							TALK_MSG_VAL_LENGTH(clientLeftNotify));
				}

				delete[] clientLeftNotify;

				GetTextandAddLine(hwnd, IDC_EDIT, name);
				GetTextandAddLine(hwnd, IDC_EDIT, " has left.\n");

				removeClient(wParam);
				TalkShutDownConnection(wParam);
			break;
		}
		break;
	case WM_CLOSE:
		DestroyDialog(hwnd);
		break;
	case WM_DESTROY:
		clearClientList();
		TalkShutDownWinSock();
		PostQuitMessage(0);
		break;
	default:
		return FALSE;
	}
	
	return TRUE;
}