#include "StdAfx.h"
#include "Firewall.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

/* must assure ip filter driver is loaded for filter hook driver to work */
TDriver CFirewall::m_ipFilterDriver;
TDriver CFirewall::m_filterHookDriver;
/* must assure ip filter driver is loaded for filter hook driver to work */

bool CFirewall::m_isLoaded = false;
bool CFirewall::m_isStarted = false;

/* start and stop rutines */
bool CFirewall::LoadFirewallModule()
{
	if(m_isLoaded)
		return true;

	/* load and start drivers */
	if(m_ipFilterDriver.LoadDriver(IP_FILTER_DRIVER_NAME, IP_FILTER_DRIVER_PATH, NULL, TRUE) != DRV_SUCCESS)
	{
		AfxMessageBox(_T("Unable to load IP_FILTER_DRIVER!"),MB_ICONEXCLAMATION);
		return false;
	}
	m_ipFilterDriver.SetRemovable(FALSE); //could be used by other apps

	if(m_filterHookDriver.LoadDriver(FILTER_HOOK_DRIVER_NAME, FILTER_HOOK_DRIVER_PATH, NULL, TRUE) != DRV_SUCCESS)
	{
		AfxMessageBox(_T("Unable to load FILTER_HOOK_DRIVER!"),MB_ICONEXCLAMATION);
		return false;
	}
	/* load and start drivers */

	m_isLoaded = true;

	return true;
}

void CFirewall::UnloadFirewallModule()
{
	if(!m_isLoaded)
		return;

	m_filterHookDriver.UnloadDriver(TRUE);
	m_isLoaded = false;
}

bool CFirewall::StartFirewall()
{
	if(m_isStarted)
		return true;

	if(!m_isLoaded)
		return false;

	/* start hook driver */
	if(m_filterHookDriver.WriteIo(START_IP_HOOK, NULL, 0) == DRV_ERROR_IO)
		return false;
	/* start hook driver */

	m_isStarted = true;

	return true;
}

void CFirewall::StopFirewall()
{
	if(!m_isLoaded || !m_isStarted)
		return;

	m_filterHookDriver.WriteIo(STOP_IP_HOOK, NULL, 0);
	m_isStarted = false;
}
/* start and stop rutines */

/* add/remove rutines */
ULONG CFirewall::AddRule(IPFilter *filter)
{
	ULONG ret;

	if(!m_isLoaded)
		return 0;

	if(m_filterHookDriver.RawIo(ADD_FILTER, filter, sizeof(IPFilter), (PVOID)&ret, sizeof(ULONG)) == DRV_ERROR_IO)
		return 0; //the id can't be 0

	return ret;
}

bool CFirewall::RemoveRule(ULONG ruleNumber)
{
	if(!m_isLoaded)
		return false;

	if(m_filterHookDriver.WriteIo(CLEAR_FILTER, (PVOID)&ruleNumber, sizeof(ULONG)) == DRV_ERROR_IO)
		return false;

	return true;
}

void CFirewall::RemoveAllRules()
{
	if(!m_isLoaded)
		return;

	m_filterHookDriver.WriteIo(CLEAR_FILTER, (PVOID)"\0\0\0\0", sizeof(ULONG));
}
/* add/remove rutines */

/* my ip */
CString CFirewall::GetMachineIp()
{
	WSADATA wsaData;
	struct hostent * hostinfo;
	CString strIp;
	WSAStartup( MAKEWORD(2,1), &wsaData );
	char name[MAX_PATH];
	if( gethostname ( name, sizeof(name)) == 0)
	{
		if((hostinfo = gethostbyname(name)) != NULL)
		{
			for( int i = 0;  hostinfo->h_addr_list[i]!= NULL; i++ )
			{
				strIp = inet_ntoa (*(struct in_addr *)hostinfo->h_addr_list[i]);
			}
		}
	}
	
	WSACleanup( );
	return strIp;
}
/* my ip */
