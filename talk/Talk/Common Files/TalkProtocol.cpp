////Author///////////////////////////////////////////////////////////////////////////
//Calin Avasilcai
////Date/////////////////////////////////////////////////////////////////////////////
//Friday, November 09, 2007
////File/////////////////////////////////////////////////////////////////////////////
//TalkProtocol.cpp
/////////////////////////////////////////////////////////////////////////////////////

////Forword//////////////////////////////////////////////////////////////////////////
//This module implements the interface defined in TalkProtocol.h
/////////////////////////////////////////////////////////////////////////////////////


////Dependencies/////////////////////////////////////////////////////////////////////
#include "TalkProtocol.h"
#include <Windows.h>

#include <map>
using namespace std;
/////////////////////////////////////////////////////////////////////////////////////

////Defines//////////////////////////////////////////////////////////////////////////
#define TALK_DEFAULT_PORT	22551
#define BUFFER_LENGTH		512
/////////////////////////////////////////////////////////////////////////////////////

////Data needed to be stored/////////////////////////////////////////////////////////
struct ReadWriteBuffers
{
	char readBuffer[BUFFER_LENGTH];
	char *partialDataBegin;
	size_t partialDataSize;
	vector<char *> pointerArray;

	vector<char *> writeBuffer;

	//Little C++ help
	ReadWriteBuffers()
	{
		partialDataBegin = NULL;
		partialDataSize = 0;
	}
	~ReadWriteBuffers()
	{
		size_t size = writeBuffer.size();

		for(size_t i = 0; i < size; i++)
			delete[] writeBuffer[i];

		pointerArray.clear();
		writeBuffer.clear();
	}
};

BOOL IsWinSockInitialized = FALSE;
map<SOCKET, ReadWriteBuffers*> SocketMap;
SOCKET AcceptSocket = NULL;
/////////////////////////////////////////////////////////////////////////////////////

////Aditional procedures/////////////////////////////////////////////////////////////
int StringLength(char *str, int maxLen)
{
	register int i;

	for(i = 0; str[i] && i < maxLen; ++i);

	if(maxLen == 0 || (i == maxLen && str[i]))
		return -1;

	return i + 1;
}

/////////////////////////////////////////////////////////////////////////////////////

////Interface Implementation/////////////////////////////////////////////////////////
BOOL TalkInitWinSock()
{
	if(IsWinSockInitialized)
		return FALSE;

	WSADATA winSockData;

	if(WSAStartup(0x0202, &winSockData))
		return FALSE;

	if(winSockData.wVersion != 0x0202)
	{
		WSACleanup();
		return FALSE;
	}

	IsWinSockInitialized = TRUE;

	return TRUE;
}
void TalkShutDownWinSock()
{
	if(!IsWinSockInitialized)
		return;

	map<SOCKET, ReadWriteBuffers*>::iterator itr, itre;
	itre = SocketMap.end();

	for(itr = SocketMap.begin(); itr != itre; itr++)
	{
		delete itr->second;
		closesocket(itr->first);
	}

	SocketMap.clear();

	TalkStopAccept();

	WSACleanup();

	IsWinSockInitialized = FALSE;
}


SOCKET TalkConnectToServer(const char *hostName, HWND windowHandle)
{
	if(!IsWinSockInitialized)
		return NULL;

	SOCKET newSocket;

	if((newSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) == INVALID_SOCKET)
		return NULL;

	SOCKADDR_IN newTarget;

	newTarget.sin_family = AF_INET;
	newTarget.sin_port = htons(TALK_DEFAULT_PORT);
	newTarget.sin_addr.s_addr = inet_addr(hostName);

	if(connect(newSocket, (SOCKADDR *)&newTarget, sizeof(newTarget)) == SOCKET_ERROR)
	{
		closesocket(newSocket);
		return NULL;
	}

	ReadWriteBuffers *newReadWriteBuffers = new ReadWriteBuffers;
	SocketMap[newSocket] = newReadWriteBuffers;

	WSAAsyncSelect(newSocket, windowHandle, WM_SOCKET_MESSAGE, FD_READ | FD_WRITE | FD_CONNECT | FD_CLOSE);

	return newSocket;

}
BOOL TalkSetUpServerForAccept(HWND windowHandle)
{
	if(!IsWinSockInitialized)
		return FALSE;
	
	if(AcceptSocket != NULL)
		return FALSE;

	if((AcceptSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) == INVALID_SOCKET)
		return FALSE;

	sockaddr_in service;
	service.sin_family = AF_INET;
	service.sin_port = htons(TALK_DEFAULT_PORT);
	service.sin_addr.s_addr = htonl(INADDR_ANY);

	if(bind(AcceptSocket, (LPSOCKADDR)&service, sizeof(service)) == SOCKET_ERROR)
	{
		TalkStopAccept();
		return FALSE;
	}
	
	if(listen(AcceptSocket, SOMAXCONN) == SOCKET_ERROR)
	{
		TalkStopAccept();
		return FALSE;
	}

	WSAAsyncSelect(AcceptSocket, windowHandle, WM_SOCKET_MESSAGE, FD_READ | FD_WRITE | FD_ACCEPT | FD_CLOSE);

	return TRUE;
}

void TalkStopAccept()
{
	if(AcceptSocket)
	{
		closesocket(AcceptSocket);
		AcceptSocket = NULL;
	}
}

SOCKET TalkOnAcceptConnection(SOCKET listenSocket)
{
	SOCKET newSocket = accept(listenSocket, NULL, NULL);

	if(newSocket == SOCKET_ERROR)
		return NULL;

	ReadWriteBuffers *newReadWriteBuffers = new ReadWriteBuffers;
	SocketMap[newSocket] = newReadWriteBuffers;

	return newSocket;
}

BOOL TalkSendMessage(SOCKET socket, const char *data, unsigned int length)
{
	if(!IsWinSockInitialized)
		return FALSE;

	/*if(length > BUFFER_LENGTH - 1)
		return FALSE;*/

	send(socket, data, length, 0);

	return TRUE;

	/*ReadWriteBuffers *rwb = SocketMap[socket];

	//ceck for errors
	if(rwb == NULL)			
		return FALSE;

	if(!rwb->writeBuffer.empty())
	{
		
		char *buf;

		if(!data[length - 1])
		{
			buf = new char[length];
			memcpy(buf, data, length);
		}
		else
		{
			buf = new char[length + 1];
			memcpy(buf, data, length);
			buf[length] = '\0';
		}

		rwb->writeBuffer.push_back(buf);
	}
	else
	{
		int senderr = 0;
		unsigned int len = length;
		char *ptr = data;

		while(len > BUFFER_LENGTH - 1)
		{
			int amount = send(socket, ptr, BUFFER_LENGTH - 1, 0);

			if(amount == SOCKET_ERROR)
				return FALSE;

			len -= amount;
			ptr += amount;

			if(amount < BUFFER_LENGTH - 1)
			{
				senderr = 1;
				break;
			}
			else
			{
				if(send(socket, "\0", 1, 0) != 1)
				{
					senderr = 2;
					break;
				}
			}
		}

		if(!senderr)
		{
			int amount = send(socket, ptr, BUFFER_LENGTH - 1, 0);
		}
	}*/
}
void TalkSendMessageOnFD_WRITE(SOCKET socket)
{
}

vector<char *> *TalkReceiveMessage(SOCKET socket)
{
	if(!IsWinSockInitialized)
		return NULL;

	ReadWriteBuffers *rwb = SocketMap[socket];

	//ceck for errors
	if(rwb == NULL)			
		return NULL; 
	
	//clear list of pointers to last messages
	rwb->pointerArray.clear(); 

	//if we got an unfinished message from last call
	if(rwb->partialDataSize > 0) 
	{
		//move all data at the beginig of the buffer
		memcpy(rwb->readBuffer, rwb->partialDataBegin, 
			rwb->partialDataSize);

		rwb->partialDataBegin = rwb->readBuffer;
	}

	//put the incoming data after leftovers
	int recvBytes = recv(socket, rwb->readBuffer + rwb->partialDataSize, 
		int(BUFFER_LENGTH - rwb->partialDataSize), 0);

	//if an error ocured or the connection was closed
	if(recvBytes == SOCKET_ERROR || recvBytes == 0)	
	{
		rwb->partialDataBegin = NULL;
		rwb->partialDataSize = 0;

		return &rwb->pointerArray;
	}

	char *ptr = rwb->readBuffer;
	int curlen;
	int buflen = recvBytes + int(rwb->partialDataSize);

	//put all the complete messages in the vector
	while((curlen = StringLength(ptr, buflen)) > 1)	
	{
		if(buflen - curlen >= 0)	//vezi aici ca e dubios!!!!!!!!!
		{
			rwb->pointerArray.push_back(ptr);
			ptr += curlen;
			buflen -= curlen;
		}
		else 
			break;
	}

	//if there are leftovers...
	if(buflen)
	{
		rwb->partialDataBegin = ptr;
		rwb->partialDataSize = buflen;
	}
	else
	{
		rwb->partialDataBegin = NULL;
		rwb->partialDataSize = 0;
	}

	//return the array
	return &rwb->pointerArray;
}

void TalkShutDownConnection(SOCKET socket)
{
	map<SOCKET, ReadWriteBuffers*>::iterator itr = SocketMap.find(socket);

	if(itr != SocketMap.end())
	{	
		delete itr->second;
		closesocket(itr->first);

		SocketMap.erase(itr);
	}
}
/////////////////////////////////////////////////////////////////////////////////////