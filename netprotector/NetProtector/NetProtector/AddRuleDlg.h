#pragma once
#include "afxwin.h"
#include "restrictededit.h"
#include "btnst.h"


// CAddRuleDlg dialog

class CAddRuleDlg : public CDialog
{
	DECLARE_DYNAMIC(CAddRuleDlg)

public:
	CAddRuleDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CAddRuleDlg();

// Dialog Data
	enum { IDD = IDD_ADDRULEDIALOG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	virtual BOOL OnInitDialog();

	DECLARE_MESSAGE_MAP()
public:
	CString m_srcIp;
	CString m_srcPort;
	CString m_destIp;
	CString m_destPort;
	int m_protocol;

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnCbnSelchangeProto();
	
	BOOL VerifyIpAddress(CString &str);

	CComboBox m_protocolCombo;
	CRestrictedEdit m_srcIpRestricted;
	CRestrictedEdit m_destIpRestricted;
	CRestrictedEdit m_srcPortRestricted;
	CRestrictedEdit m_destPortRestricted;

	afx_msg void OnBnClickedButtonSrcipPopup();
	afx_msg void OnBnClickedButtonDestipPopup();
	afx_msg void OnBnClickedButtonSrcportPopup();
	afx_msg void OnBnClickedButtonDestportPopup();

	CButtonST m_srcIpButton;
	CButtonST m_destIpButton;
	CButtonST m_srcPortButton;
	CButtonST m_destPortButton;
	CButtonST m_okButton;
	CButtonST m_cancelButton;

	CEdit *mp_ipEdit;
	CEdit *mp_portEdit;
public:
	afx_msg void OnPopupIpMymachine();
	afx_msg void OnPopupIpAnymachine();
	afx_msg void OnPopupAnyport();
	afx_msg void OnPopupHttp();
	afx_msg void OnPopupFtp();
	afx_msg void OnPopupSsh();
	afx_msg void OnPopupTelnet();
	afx_msg void OnPopupSmtp();
	afx_msg void OnPopupDns();
	afx_msg void OnPopupDhcp();
	afx_msg void OnPopupPop3();
	afx_msg void OnPopupNntp();
	afx_msg void OnPopupNetbios();
	afx_msg void OnPopupSocks();
};
