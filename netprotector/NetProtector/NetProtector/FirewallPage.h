#pragma once
#include "afxcmn.h"
#include "ColoredDlg.h"
#include "Firewall.h"
#include "AddRuleDlg.h"
#include "WinXPButtonST.h"
#include <map>
#include "gradientstatic.h"
using namespace std;
// CFirewallPage dialog

class CFirewallPage : public CColoredDlg
{
	DECLARE_DYNAMIC(CFirewallPage)

public:
	CFirewallPage(CWnd* pParent = NULL);   // standard constructor
	virtual ~CFirewallPage();

// Dialog Data
	enum { IDD = IDD_FIREWALLPAGE };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	/* CALIN - override for avoiding default behaviour */
	virtual void OnOK() {};
	virtual void OnCancel(){};

	virtual BOOL OnInitDialog();
	afx_msg void OnDestroy();
	/* CALIN - override for avoiding default behaviour */

	
	DECLARE_MESSAGE_MAP()
private:
	CListCtrl m_ruleList;
	void AddRuleToList(int, CString &, CString &, CString &, CString &, ULONG);
	void AddRuleToList(IPFilter *, ULONG);


	HKEY m_rulesRegistryKey;
	map<ULONG, GUID> rulesMap;
	void CreateRule(int, CString &, CString &, CString &, CString &);
	void RemoveRule(int);


	IPFilter StringsToIPFilter(int, CString &, CString &, CString &, CString &);
	void IPFilterToStrings(IPFilter *, int&, CString &, CString &, CString &, CString &);

	
	void LoadRulesFromRegistry();

#define MAKE_GUID_STR(_guid, _guidstr)	int _guidLen = sizeof(GUID);				\
										char *_guidstr = new char[_guidLen + 1];	\
										_guidstr[_guidLen] = '\0';					\
										memcpy(_guidstr, (void *) &_guid, _guidLen)
#define FREE_GUID_STR(_guidstr)	delete[] _guidstr

#define MAKE_GUID_FROM_STR(_guidstr, _guid)	GUID _guid;									\
											memcpy((void *) &_guid, (void *)_guidstr, sizeof(GUID))

	afx_msg void OnBnClickedButtonadd();
	afx_msg void OnBnClickedButtonedit();
	afx_msg void OnBnClickedButtonDelete();
	afx_msg void OnCbnSelchangeCombodefaultaction();
	afx_msg void OnLvnItemchangedRulelist(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnBnClickedButtondeleteall();

	CWinXPButtonST m_addRuleButton;
	CWinXPButtonST m_editRuleButton;
	CWinXPButtonST m_deleteRuleButton;
	CWinXPButtonST m_deleteAllRulesButton;
	CGradientStatic m_pfrTitle;

	/* disable / enable firewall */
private:
	BOOL mb_firewallEnabled;
	HKEY m_fireStateKey;
public:
	void EnableFirewall();
	void DisableFirewall();
	BOOL IsEnabled();
	static CFirewallPage *ThisInstance;
	/* disable / enable firewall */
};
