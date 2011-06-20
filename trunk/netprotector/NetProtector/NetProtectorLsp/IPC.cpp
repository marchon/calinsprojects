#include "IPC.h"
#include "precomp.h"

#define DECLARE_CRITICAL_SECTION(cs)	int cs = 0
#define ENTER_CRITICAL_SECTION(cs)		while(cs == 1) Sleep(1); \
										cs = 1
#define LEAVE_CRITICAL_SECTION(cs)		cs = 0

#pragma data_seg(".SHARED")

HWND processMonitorWindowHandle = NULL;

DECLARE_CRITICAL_SECTION(criticalSection);

#pragma data_seg()

#pragma comment(linker, "/section:.SHARED,RWS")

DWORD currentProcessPid = 0;

void ProcessAttachPrepareIPCModule()
{
	currentProcessPid = GetCurrentProcessId();

	PostMessage(processMonitorWindowHandle, PROCESS_ATTACHED, (WPARAM)currentProcessPid, NULL);
}

void ProcessDetachCloseIPCModule()
{
	PostMessage(processMonitorWindowHandle, PROCESS_DETACHED, (WPARAM)currentProcessPid, NULL);
}

bool QueryProcessMonitorApprovall()
{
	if(processMonitorWindowHandle == NULL)
		return true;

	ENTER_CRITICAL_SECTION(criticalSection);

	if(SendMessage(processMonitorWindowHandle, CONNECTION_QUERY, 
		(WPARAM)currentProcessPid, NULL) == LRESULT_ALLOW)
	{

		LEAVE_CRITICAL_SECTION(criticalSection);

		return true;
	}

	LEAVE_CRITICAL_SECTION(criticalSection);
	
	return false;
}


void RegisterProcessMonitor(HWND hwnd)
{
	processMonitorWindowHandle = hwnd;
}