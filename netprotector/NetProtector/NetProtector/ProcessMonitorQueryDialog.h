#pragma once

#include "path.h"
#include "btnst.h"

// CProcessMonitorQueryDialog dialog

class CProcessMonitorQueryDialog : public CDialog
{
	DECLARE_DYNAMIC(CProcessMonitorQueryDialog)

public:
	CProcessMonitorQueryDialog(CWnd* pParent = NULL);   // standard constructor
	virtual ~CProcessMonitorQueryDialog();

// Dialog Data
	enum { IDD = IDD_PROCESSMONITORQUERYDIALOG };

	CPath programIdentifier;
	BOOL m_thisSessionOnly;

	HICON m_appIcon;



protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual BOOL OnInitDialog();

	DECLARE_MESSAGE_MAP()
private:
	afx_msg void OnBnClickedCancel();
	afx_msg void OnBnClickedOk();

	CButtonST m_buttonAllow;
	CButtonST m_buttonBlock;
};
