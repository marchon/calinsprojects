#pragma once
#include "btnst.h"


// CAddAppRuleDlg dialog

class CAddAppRuleDlg : public CDialog
{
	DECLARE_DYNAMIC(CAddAppRuleDlg)

public:
	CAddAppRuleDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CAddAppRuleDlg();

// Dialog Data
	enum { IDD = IDD_ADDAPPRULEDLG };

	CString path;
	int action;

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	void DDX_ComboBox(CDataExchange *, int, int &);

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedBrowseButton();
private:
	CButtonST m_buttonBrowse;
	CButtonST m_buttonOk;
	CButtonST m_buttonCancel;
};
