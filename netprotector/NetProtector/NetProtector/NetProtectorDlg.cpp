// FirewallDlg.cpp : implementation file
//

#include "stdafx.h"
#include "NetProtector.h"
#include "NetProtectorDlg.h"
#include "SysInfo.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

// CAboutDlg dialog used for App About

class CAboutDlg : public CColoredDlg
{
public:
	CAboutDlg();

// Dialog Data
	enum { IDD = IDD_ABOUTBOX };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

// Implementation
protected:
	DECLARE_MESSAGE_MAP()
};

CAboutDlg::CAboutDlg() : CColoredDlg(CAboutDlg::IDD)
{
}

void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CColoredDlg::DoDataExchange(pDX);
}

BEGIN_MESSAGE_MAP(CAboutDlg, CColoredDlg)
END_MESSAGE_MAP()


// CFirewallDlg dialog




CNetProtectorDlg::CNetProtectorDlg(CWnd* pParent /*=NULL*/)
	: CColoredDlg(CNetProtectorDlg::IDD, pParent)
{
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);

	mb_error = false;

	m_currentPage = FIREWALL_PAGE;
}

void CNetProtectorDlg::DoDataExchange(CDataExchange* pDX)
{
	CColoredDlg::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_GUIDE, m_guideControl);
	DDX_Control(pDX, IDC_FIREWALLBTN, m_firewallButton);
	DDX_Control(pDX, IDC_PMONBTN, m_pmonButton);
	DDX_Control(pDX, IDC_OPTIONSBTN, m_optionsButton);
	DDX_Control(pDX, IDC_STATUS, m_statusBar);
	DDX_Control(pDX, IDC_SYSINFO_OS, m_sysOs);
	DDX_Control(pDX, IDC_SYSINFO_CPU, m_sysCpu);
	DDX_Control(pDX, IDC_SYSINFO_MEM, m_sysMem);
}

BEGIN_MESSAGE_MAP(CNetProtectorDlg, CColoredDlg)
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_MESSAGE(WM_NOTIFYICON, &CNetProtectorDlg::OnNotifyIcon)
	ON_WM_DESTROY()
	//}}AFX_MSG_MAP
	ON_COMMAND(ID_POPUP_EXIT, &CNetProtectorDlg::OnPopupExit)
	ON_COMMAND(ID_POPUP_SHOWNETPROTECTOR, &CNetProtectorDlg::OnPopupShownetprotector)

	ON_STN_CLICKED(IDC_FIREWALLBTN, &CNetProtectorDlg::OnStnClickedFirewallbtn)
	ON_STN_CLICKED(IDC_PMONBTN, &CNetProtectorDlg::OnStnClickedPmonbtn)
	ON_STN_CLICKED(IDC_OPTIONSBTN, &CNetProtectorDlg::OnStnClickedOptionsbtn)
	ON_COMMAND(ID_POPUP_HIDENETPROTECTOR, &CNetProtectorDlg::OnPopupHidenetprotector)
END_MESSAGE_MAP()


// CFirewallDlg message handlers

BOOL CNetProtectorDlg::OnInitDialog()
{
	CColoredDlg::OnInitDialog();

	SetBkgColor(RGB(95, 119, 131));
	//SetFrgColor(RGB(255, 0, 255));

	// Add "About..." menu item to system menu.

	// IDM_ABOUTBOX must be in the system command range.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// TODO: Add extra initialization here

	/* CALIN - make a tray icon */
	/* CALIN - populate try icon structure */
	CString sTip;
	sTip.LoadString(IDS_APPNAME);

	m_tryIcon.cbSize	= sizeof(NOTIFYICONDATA);
	m_tryIcon.hWnd		= this->GetSafeHwnd();
	m_tryIcon.uID		= IDR_MAINFRAME;
	m_tryIcon.uFlags	= NIF_MESSAGE | NIF_ICON;
	m_tryIcon.uCallbackMessage = WM_NOTIFYICON;
	m_tryIcon.hIcon		= m_hIcon;
	m_tryIcon.uFlags	= NIF_MESSAGE | NIF_ICON | NIF_TIP;

	lstrcpyn(m_tryIcon.szTip, (LPCTSTR)sTip, sizeof(m_tryIcon.szTip)/sizeof(m_tryIcon.szTip[0]));
	/* CALIN - populate try icon structure */
	if(!Shell_NotifyIcon(NIM_ADD, &m_tryIcon))
	{
		CString s_errorMessage;
		s_errorMessage.LoadString(IDS_APPERROR);

		MessageBox(s_errorMessage);
		mb_error = true;
		SendMessage(WM_DESTROY);
	}
	/* CALIN - make a tray icon */

	/* CALIN - initialize pages */
	CRect rect;
	m_guideControl.GetClientRect(rect);
	m_guideControl.MapWindowPoints(this, rect);

	m_firewallPage.Create(CFirewallPage::IDD, this);
	m_firewallPage.MoveWindow(rect);
	m_firewallPage.ShowWindow(SW_SHOW);
	
	m_processMonitorPage.Create(CProcessMonitorPage::IDD, this);
	m_processMonitorPage.MoveWindow(rect);
	m_processMonitorPage.ShowWindow(SW_HIDE);

	m_optionPage.Create(COptionPage::IDD, this);
	m_optionPage.MoveWindow(rect);
	m_optionPage.ShowWindow(SW_HIDE);
	/* CALIN - initialize pages */

	/* CALIN - load the bitmaps for the buttons */
	m_firewallButtonUp = ::LoadBitmap(GetModuleHandle(NULL), MAKEINTRESOURCE(IDB_FIREWALLBUTTON_UP));
	m_firewallButtonDown = ::LoadBitmap(GetModuleHandle(NULL), MAKEINTRESOURCE(IDB_FIREWALLBUTTON_DOWN));

	m_pmonButtonUp = ::LoadBitmap(GetModuleHandle(NULL), MAKEINTRESOURCE(IDB_PMONITORBUTTON_UP));
	m_pmonButtonDown = ::LoadBitmap(GetModuleHandle(NULL), MAKEINTRESOURCE(IDB_PMONITORBUTTON_DOWN));

	m_optionsButtonUp = ::LoadBitmap(GetModuleHandle(NULL), MAKEINTRESOURCE(IDB_OPTIONSBUTTON_UP));
	m_optionsButtonDown = ::LoadBitmap(GetModuleHandle(NULL), MAKEINTRESOURCE(IDB_OPTIONSBUTTON_DOWN));
	/* CALIN - load the bitmaps for the buttons */

	/* CALIN - set buttons bitmaps */
	m_firewallButton.SetBitmap(m_firewallButtonDown);
	m_pmonButton.SetBitmap(m_pmonButtonUp);
	m_optionsButton.SetBitmap(m_optionsButtonUp);
	/* CALIN - set buttons bitmaps */

	/* CALIN - button cursors */
	m_default = ::LoadCursor(NULL, IDC_ARROW);
	m_hand = ::LoadCursor(NULL, IDC_HAND);

	m_pmonButton.SetCrsr(m_hand);
	m_optionsButton.SetCrsr(m_hand);
	/* CALIN - button cursors */

	/* CALIN - status bar */
	m_statusBar.SetColor2(RGB(63, 91, 92));
	m_statusBar.SetTextColor(RGB(10, 25, 32));
	m_statusBar.SetFont(&theApp.m_defautlFont);
	
	m_firewallButton.SetStatusBarText("Firewall", &m_statusBar);
	m_pmonButton.SetStatusBarText("Process Monitor", &m_statusBar);
	m_optionsButton.SetStatusBarText("Options", &m_statusBar);
	/* CALIN - status bar */

	/* CALIN - system info */	
	m_sysOs.SetColor2(RGB(95, 119, 131));
	m_sysOs.SetTextColor(RGB(10, 25, 32));
	m_sysOs.SetLeftSpacing(0);
	m_sysOs.SetFont(&theApp.m_defautlFont);

	m_sysMem.SetColor2(RGB(95, 119, 131));
	m_sysMem.SetTextColor(RGB(10, 25, 32));
	m_sysMem.SetLeftSpacing(0);
	m_sysMem.SetFont(&theApp.m_defautlFont);

	m_sysCpu.SetColor2(RGB(95, 119, 131));
	m_sysCpu.SetTextColor(RGB(10, 25, 32));
	m_sysCpu.SetLeftSpacing(0);
	m_sysCpu.SetFont(&theApp.m_defautlFont);

	CSysInfo sysInf;
	sysInf.SetShowLoopback(TRUE);
	sysInf.Init();

	CString s;

	s.Format("OS: %s", sysInf.GetOSType());
	m_sysOs.SetWindowText(s);

	s.Format("CPU: %s, %dMhz", sysInf.GetCPUNameString(), sysInf.GetCPUSpeed());
	m_sysCpu.SetWindowText(s);

	s.Format("Memory: %dMB", sysInf.GetTotalPhys() / (1024 * 1024));
	m_sysMem.SetWindowText(s);
	
	/* CALIN - system info */

	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CNetProtectorDlg::OnDestroy()
{
	/* CALIN - destroy all the pages */
	m_optionPage.SendMessage(WM_DESTROY);
	m_firewallPage.SendMessage(WM_DESTROY);
	m_processMonitorPage.SendMessage(WM_DESTROY);
	/* CALIN - destroy all the pages */

	/* CALIN - delete all the bitmaps */
	DeleteObject(m_firewallButtonUp);
	DeleteObject(m_firewallButtonDown);

	DeleteObject(m_pmonButtonUp);
	DeleteObject(m_pmonButtonDown);

	DeleteObject(m_optionsButtonUp);
	DeleteObject(m_optionsButtonDown);
	/* CALIN - delete all the bitmaps */

	/* CALIN - delete tray icon */
	Shell_NotifyIcon(NIM_DELETE, &m_tryIcon);
	/* CALIN - delete tray icon */

	CColoredDlg::OnOK(); //exit the modal loop
}

void CNetProtectorDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CAboutDlg dlgAbout;
		dlgAbout.DoModal();
	}
	else
	{
		CColoredDlg::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CNetProtectorDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CColoredDlg::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CNetProtectorDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

/* CALIN - hide dialog on ok and cancel events */
void CNetProtectorDlg::OnOK()
{
	ShowWindow(SW_HIDE);
}
void CNetProtectorDlg::OnCancel()
{
	ShowWindow(SW_HIDE);
}
/* CALIN - hide dialog on ok and cancel events */

/* CALIN - handlers */
/* CALIN - try icon message handler*/
LRESULT CNetProtectorDlg::OnNotifyIcon(WPARAM wParam, LPARAM lParam)
{
	if(wParam == IDR_MAINFRAME)
	{
		switch (lParam)
		{
		case WM_LBUTTONUP:
				ShowWindow(SW_NORMAL);
				SetForegroundWindow();
				SetFocus();
			break;
		case WM_RBUTTONUP:
			{
				CMenu trayMenuPopupBase;
				trayMenuPopupBase.LoadMenu(IDR_TRAYMENU);

				CMenu *trayMenuPopup = trayMenuPopupBase.GetSubMenu(0);
				ASSERT(trayMenuPopup);

				POINT pos;
				GetCursorPos(&pos);
				
				trayMenuPopup->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, pos.x, pos.y, this);
			}
			break;
		}
	}

	return TRUE;
}

void CNetProtectorDlg::OnPopupShownetprotector()
{
	ShowWindow(SW_NORMAL);
	SetForegroundWindow();
	SetFocus();
}
void CNetProtectorDlg::OnPopupHidenetprotector()
{
	ShowWindow(SW_HIDE);
}

void CNetProtectorDlg::OnPopupExit()
{
	if(MessageBox("Are you sure you want to exit NetProtector?\nYour computer will remain vulnerable.",
		"Exit?", MB_YESNO) == IDYES)
	{
		 SendMessage(WM_DESTROY);
	}
}
/* CALIN - try icon message handler*/

/* CALIN - page change handlers */
void CNetProtectorDlg::OnStnClickedFirewallbtn()
{
	if(m_currentPage == FIREWALL_PAGE)
		return;

	m_firewallButton.SetBitmap(m_firewallButtonDown);
	m_firewallButton.SetCrsr(m_default);
	m_firewallPage.SetWindowPos(&wndTop, 0, 0, 0, 0, 
		SWP_NOMOVE | SWP_NOSIZE | SWP_SHOWWINDOW);


	if(m_currentPage == PMON_PAGE)
	{
		m_pmonButton.SetBitmap(m_pmonButtonUp);
		m_pmonButton.SetCrsr(m_hand);
		m_processMonitorPage.ShowWindow(SW_HIDE);
	}
	else
	{
		m_optionsButton.SetBitmap(m_optionsButtonUp);
		m_optionsButton.SetCrsr(m_hand);
		m_optionPage.ShowWindow(SW_HIDE);
	}

	m_currentPage = FIREWALL_PAGE;

	m_firewallButton.Invalidate(); //odd
}

void CNetProtectorDlg::OnStnClickedPmonbtn()
{
	if(m_currentPage == PMON_PAGE)
		return;

	m_pmonButton.SetBitmap(m_pmonButtonDown);
	m_pmonButton.SetCrsr(m_default);
	m_processMonitorPage.SetWindowPos(&wndTop, 0, 0, 0, 0, 
		SWP_NOMOVE | SWP_NOSIZE | SWP_SHOWWINDOW);

	if(m_currentPage == FIREWALL_PAGE)
	{
		m_firewallButton.SetBitmap(m_firewallButtonUp);
		m_firewallButton.SetCrsr(m_hand);
		m_firewallPage.ShowWindow(SW_HIDE);
	}
	else
	{
		m_optionsButton.SetBitmap(m_optionsButtonUp);
		m_optionsButton.SetCrsr(m_hand);
		m_optionPage.ShowWindow(SW_HIDE);
	}

	m_currentPage = PMON_PAGE;

	m_pmonButton.Invalidate(); //odd
}

void CNetProtectorDlg::OnStnClickedOptionsbtn()
{
	if(m_currentPage == OPTIONS_PAGE)
		return;

	m_optionsButton.SetBitmap(m_optionsButtonDown);
	m_optionsButton.SetCrsr(m_default);
	m_optionPage.SetWindowPos(&wndTop, 0, 0, 0, 0, 
		SWP_NOMOVE | SWP_NOSIZE | SWP_SHOWWINDOW);

	if(m_currentPage == FIREWALL_PAGE)
	{
		m_firewallButton.SetBitmap(m_firewallButtonUp);
		m_firewallButton.SetCrsr(m_hand);
		m_firewallPage.ShowWindow(SW_HIDE);
	}
	else
	{
		m_pmonButton.SetBitmap(m_pmonButtonUp);
		m_pmonButton.SetCrsr(m_hand);
		m_processMonitorPage.ShowWindow(SW_HIDE);
	}

	m_currentPage = OPTIONS_PAGE;

	m_optionsButton.Invalidate(); //odd
}
/* CALIN - handlers */
