#pragma once
#include "ColoredDlg.h"
#include "WinXPButtonST.h"

// GeneralPage dialog

class COptionPage : public CColoredDlg
{
	DECLARE_DYNAMIC(COptionPage)

public:
	COptionPage(CWnd* pParent = NULL);   // standard constructor
	virtual ~COptionPage();

// Dialog Data
	enum { IDD = IDD_OPTIONPAGE };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	
	/* CALIN - override for avoiding default behaviour */
	virtual void OnOK() {};
	virtual void OnCancel(){};

	virtual BOOL OnInitDialog();
	/* CALIN - override for avoiding default behaviour */

	DECLARE_MESSAGE_MAP()

private:

	PHKEY m_runKey;
	HKEY m_optionKey;

	CWinXPButtonST m_startBootCheck;
	CWinXPButtonST m_enableFireCheck;
	CWinXPButtonST m_enablePmCheck;

	afx_msg void OnBnClickedStartBoot();
	afx_msg void OnBnClickedEnableFire();
	afx_msg void OnBnClickedEnablePm();
};
