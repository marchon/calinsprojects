// FirewallApp.cpp : Defines the class behaviors for the application.
//

#include "stdafx.h"
#include "NetProtector.h"
#include "Firewall.h"


#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

#pragma comment(linker, "/SECTION:.shr,RWS")
#pragma data_seg(".shr")
HWND hGlobal = NULL;
#pragma data_seg()


// CFirewallApp

BEGIN_MESSAGE_MAP(CNetProtector, CWinApp)
	ON_COMMAND(ID_HELP, &CWinApp::OnHelp)
END_MESSAGE_MAP()


// CFirewallApp construction

CNetProtector::CNetProtector()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
#ifdef _DEBUG
	_CrtSetReportMode( _CRT_ERROR, _CRTDBG_MODE_DEBUG );
	_CrtSetDbgFlag ( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF );
#endif

	//default font
	m_defautlFont.CreatePointFont(90, "Orator Std");
}

// The one and only CFirewallApp object

CNetProtector theApp;


// CFirewallApp initialization

BOOL CNetProtector::InitInstance()
{
	/* calin - one instance only */
	if(hGlobal != NULL)
	{
		if(hGlobal != (HWND)1)
		{
			::SetForegroundWindow(hGlobal);
            ::ShowWindow(hGlobal, SW_RESTORE);
		}
		return FALSE;
	}
	hGlobal = (HWND)1;
	/* calin - one instance only */

	// InitCommonControlsEx() is required on Windows XP if an application
	// manifest specifies use of ComCtl32.dll version 6 or later to enable
	// visual styles.  Otherwise, any window creation will fail.
	INITCOMMONCONTROLSEX InitCtrls;
	InitCtrls.dwSize = sizeof(InitCtrls);
	// Set this to include all the common control classes you want to use
	// in your application.
	InitCtrls.dwICC = ICC_WIN95_CLASSES;
	InitCommonControlsEx(&InitCtrls);

	CWinApp::InitInstance();

	AfxEnableControlContainer();

	//SetRegistryKey(_T("Calin-NetProtector"));
	//using old fashion win32 calls for registry

	

	/* CALIN - create the dialog but do not show it */
	CNetProtectorDlg dlg;
	m_pMainWnd = &dlg;

	dlg.Create(IDD_NETPROTECTOR);

	/* calin - one instance only */
	hGlobal = m_pMainWnd->GetSafeHwnd();
	/* calin - one instance only */

	/* enter loop */
	if(!dlg.mb_error)
		dlg.RunModalLoop();
	/* enter loop */
	/* CALIN - create the dialog but do not show it */

	//return false so it doesn't begin message pump loop
	return FALSE;
}
