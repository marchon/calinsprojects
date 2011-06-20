// FirewallApp.h : main header file for the PROJECT_NAME application
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols
#include "NetProtectorDlg.h"


// CFirewallApp:
// See FirewallApp.cpp for the implementation of this class
//

class CNetProtector : public CWinApp
{
public:
	CNetProtector();

// Overrides
public:
	virtual BOOL InitInstance();

	CFont m_defautlFont;
// Implementation

	DECLARE_MESSAGE_MAP()
};

extern CNetProtector theApp;