// FirewallDlg.h : header file
//

#pragma once
#include "afxcmn.h"

#include "OptionPage.h"
#include "FirewallPage.h"
#include "ProcessMonitorPage.h"
#include "afxwin.h"
#include "ColoredDlg.h"
#include "ImageButton.h"
#include "gradientstatic.h"

#define WM_NOTIFYICON		(WM_APP + 1)	//used for try icon	

#define FIREWALL_PAGE	0
#define PMON_PAGE		1
#define OPTIONS_PAGE	2


// CFirewallDlg dialog
class CNetProtectorDlg : public CColoredDlg
{
// Construction
public:
	CNetProtectorDlg(CWnd* pParent = NULL);	// standard constructor

// Dialog Data
	enum { IDD = IDD_NETPROTECTOR };

	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support


// Implementation
protected:
	HICON m_hIcon;

	/* CALIN - tray icon */
	NOTIFYICONDATA m_tryIcon;
	/* CALIN - tray icon */

	/* CALIN - the buttons and pages */
	HBITMAP m_firewallButtonUp;
	HBITMAP m_firewallButtonDown;
	HBITMAP m_pmonButtonUp;
	HBITMAP m_pmonButtonDown;
	HBITMAP m_optionsButtonUp;
	HBITMAP m_optionsButtonDown;

	HCURSOR m_default;
	HCURSOR m_hand;

	CImageButton m_firewallButton;
	CImageButton m_pmonButton;
	CImageButton m_optionsButton;

	CStatic m_guideControl;
	
	CFirewallPage m_firewallPage;
	CProcessMonitorPage m_processMonitorPage;
	COptionPage m_optionPage;

	int m_currentPage;
	/* CALIN - the buttons and pages */

	/* CALIN - status bar */
	CGradientStatic m_statusBar;
	/* CALIN - status bar */

	/* CALIN - system info */
	CGradientStatic m_sysOs;
	CGradientStatic m_sysCpu;
	CGradientStatic m_sysMem;
	/* CALIN - system info */

	// Generated message map functions
	virtual BOOL OnInitDialog();

	/* CALIN - handle those messages */
	virtual void OnOK();
	virtual void OnCancel();
	/* CALIN - handle those messages */

	afx_msg void OnDestroy();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();

	/* Calin - try icon */
	afx_msg LRESULT OnNotifyIcon(WPARAM wParam, LPARAM lParam);
	afx_msg void OnTcnSelchangeTab(NMHDR *pNMHDR, LRESULT *pResult);
	/* Calin - try icon */

	DECLARE_MESSAGE_MAP()	
public:
	bool mb_error;
	afx_msg void OnPopupExit();
	afx_msg void OnPopupShownetprotector();
	afx_msg void OnStnClickedFirewallbtn();
	afx_msg void OnStnClickedPmonbtn();
	afx_msg void OnStnClickedOptionsbtn();	
	afx_msg void OnPopupHidenetprotector();
};
