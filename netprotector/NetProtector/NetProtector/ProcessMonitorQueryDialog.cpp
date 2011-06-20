// ProcessMonitorQueryDialog.cpp : implementation file
//

#include "stdafx.h"
#include "NetProtector.h"
#include "ProcessMonitorQueryDialog.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

// CProcessMonitorQueryDialog dialog

IMPLEMENT_DYNAMIC(CProcessMonitorQueryDialog, CDialog)

CProcessMonitorQueryDialog::CProcessMonitorQueryDialog(CWnd* pParent /*=NULL*/)
	: CDialog(CProcessMonitorQueryDialog::IDD, pParent)
{
	programIdentifier = "";
	m_appIcon = NULL;
}

CProcessMonitorQueryDialog::~CProcessMonitorQueryDialog()
{
	if(m_appIcon)
		::DestroyIcon(m_appIcon);
}

void CProcessMonitorQueryDialog::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);

	DDX_Check(pDX, IDC_THIS_SESSION_ONLY, m_thisSessionOnly);

//	pDX->m_bSaveAndValidate = 0;
	DDX_Control(pDX, IDOK, m_buttonAllow);
	DDX_Control(pDX, IDCANCEL, m_buttonBlock);
}


BEGIN_MESSAGE_MAP(CProcessMonitorQueryDialog, CDialog)
	ON_BN_CLICKED(IDCANCEL, &CProcessMonitorQueryDialog::OnBnClickedCancel)
	ON_BN_CLICKED(IDOK, &CProcessMonitorQueryDialog::OnBnClickedOk)
END_MESSAGE_MAP()

BOOL CProcessMonitorQueryDialog::OnInitDialog()
{
	CDialog::OnInitDialog();

	((CStatic *)GetDlgItem(IDC_QUERRY_NAME))->SetWindowText(programIdentifier.GetFileName());
	((CStatic *)GetDlgItem(IDC_QUERRY_PATH))->SetWindowText(programIdentifier.GetPath());
	((CStatic *)GetDlgItem(IDC_QUERRY_PUBLISHER))->SetWindowText(
		programIdentifier.GetFileVersionInfoString(VERINFO_CompanyName));
	((CStatic *)GetDlgItem(IDC_QUERRY_DESCRIPTION))->SetWindowText(
		programIdentifier.GetFileVersionInfoString(VERINFO_FileDescrib));

	m_appIcon = ::ExtractIcon(NULL, programIdentifier.GetPath(), 0);
	if(!m_appIcon)
		m_appIcon = ::LoadIcon(NULL, IDI_WINLOGO);
	((CStatic *)GetDlgItem(IDC_APPICON))->SetIcon(m_appIcon);
	
	SetWindowPos(&wndTopMost, 0, 0, 0, 0, SWP_NOMOVE | SWP_NOSIZE);
	//always on top....yeaaahh

	return TRUE;
}

// CProcessMonitorQueryDialog message handlers

void CProcessMonitorQueryDialog::OnBnClickedCancel()
{
	UpdateData();
	OnCancel();
}

void CProcessMonitorQueryDialog::OnBnClickedOk()
{
	//UpdateData(); //just in case :-??
	OnOK();
}
