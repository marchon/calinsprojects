#pragma once
#include "afxcmn.h"
#include "ColoredDlg.h"
#include "WinXPButtonST.h"
#include "tooltip2.h"

#include <vector>
#include "gradientstatic.h"
using namespace std;

// CProcessMonitorPage dialog
struct AppRule
{
	CString path;
	byte action;
	byte isSessionRule;

	AppRule()
	{
		isSessionRule = 0;
	}
};

//function prototype for registering pm
typedef void (*register_pm_fp)(HWND);

class CProcessMonitorPage : public CColoredDlg
{
	DECLARE_DYNAMIC(CProcessMonitorPage)

public:
	CProcessMonitorPage(CWnd* pParent = NULL);   // standard constructor
	virtual ~CProcessMonitorPage();

// Dialog Data
	enum { IDD = IDD_PROCESSMONITORPAGE };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	/* CALIN - override for avoiding default behaviour */
	virtual void OnOK() {};
	virtual void OnCancel() {};

	virtual BOOL OnInitDialog();
	/* CALIN - override for avoiding default behaviour */

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnDestroy();
	static CString GetProcessNameByPid(DWORD);
private:
	/* CALIN - Layered service provider communication */
	register_pm_fp m_RegisterProcessMonitor;
	HINSTANCE m_NetProtectorLspDll;

	void LspComm_InitComm();
	void LspComm_ShutdownComm();

	/* CALIN - the message handlers for the messages from lsp */
	afx_msg LRESULT OnQueryProcessMonitor(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnProcessAttached(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnProcessDetached(WPARAM wParam, LPARAM lParam);
	/* CALIN - the message handlers for the messages from lsp */
	/* CALIN - Layered service provider communication */

	/* CALIN - process internet access request interface */
	CListCtrl m_AppRuleList; //GUI rulelist
	vector<AppRule *> m_AppRuleVector; //rule vector
	HKEY m_AppRuleRegistryKey; //reg key
	CImageList m_imageList; //image list

	//the interface
	void AppRule_InitModule();
	void AppRule_CloseModule();

	void AppRule_AddRule(CString &, byte, byte = 0);
	void AppRule_ModifySelectedRule(byte);
	void AppRule_DelSelectedRule();
	CString AppRule_GetSelectedApp();
	void AppRule_DelAllRules();
	byte AppRule_Check(DWORD);

	void AppRule_AddRuleToList(AppRule *);
	/* CALIN - process internet access request interface */

	/* CALIN - buttons and handlers */
	CToolTip2 m_infoToolTip; //info tooltip

	CWinXPButtonST m_addButton;
	CWinXPButtonST m_deleteButton;
	CWinXPButtonST m_deleteAllButton;

	afx_msg void OnBnClickedButtonaddPm();
	afx_msg void OnBnClickedButtondeletePm();
	afx_msg void OnBnClickedButtondeleteallPm();

	afx_msg void OnLvnItemchangedRulelistPm(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRClickRulelistPm(NMHDR *pNMHDR, LRESULT *pResult);

	afx_msg void OnPopupAllow();
	afx_msg void OnPopupBlock();
	afx_msg void OnPopupApplicationinfo();
	/* CALIN - buttons and handlers */
	afx_msg void OnNMClickRulelistPm(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLButtonDown(UINT nFlags, CPoint point);

	CGradientStatic m_apprulTitle;
	CGradientStatic m_sessionrulTitle;
	CGradientStatic m_sessionrul2Title;

/* disable / enable firewall */
private:
	BOOL mb_pMonEnabled;
	HKEY m_pMonStateKey;
public:
	void EnablePMon();
	void DisablePMon();
	BOOL IsEnabled();
	static CProcessMonitorPage *ThisInstance;
/* disable / enable firewall */
};
