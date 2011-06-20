// AddAppRuleDlg.cpp : implementation file
//

#include "stdafx.h"
#include "NetProtector.h"
#include "AddAppRuleDlg.h"
#include "path.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

// CAddAppRuleDlg dialog

IMPLEMENT_DYNAMIC(CAddAppRuleDlg, CDialog)

CAddAppRuleDlg::CAddAppRuleDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CAddAppRuleDlg::IDD, pParent)
{
	action = 0;
	path = "";
}

CAddAppRuleDlg::~CAddAppRuleDlg()
{
}

void CAddAppRuleDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);

	DDX_Text(pDX, IDC_PATH_EDIT, path);
	DDX_ComboBox(pDX, IDC_ACTION_COMBO, action);
	DDX_Control(pDX, IDC_BROWSE_BUTTON, m_buttonBrowse);
	DDX_Control(pDX, IDOK, m_buttonOk);
	DDX_Control(pDX, IDCANCEL, m_buttonCancel);
}


BEGIN_MESSAGE_MAP(CAddAppRuleDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CAddAppRuleDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDC_BROWSE_BUTTON, &CAddAppRuleDlg::OnBnClickedBrowseButton)
END_MESSAGE_MAP()


void CAddAppRuleDlg::DDX_ComboBox(CDataExchange *pDX, int nIDC, int &selItem)
{
	int sel = ((CComboBox *)GetDlgItem(nIDC))->GetCurSel();
	if(sel != -1)
		selItem = sel;
	else
		((CComboBox *)GetDlgItem(nIDC))->SetCurSel(selItem);
}


// CAddAppRuleDlg message handlers

void CAddAppRuleDlg::OnBnClickedOk()
{
	UpdateData();

	CPath checkPath(path);

	if(!checkPath.ExistFile())
	{
		MessageBox("This is not a valid file name!");
		GetDlgItem(IDC_PATH_EDIT)->SetFocus();
		return;
	}

	if(checkPath.GetExtension().MakeLower().Compare(".exe")) // == 0 if equal
	{
		MessageBox("This is not a valid executable file!");
		GetDlgItem(IDC_PATH_EDIT)->SetFocus();
		return;
	}

	OnOK();
}

void CAddAppRuleDlg::OnBnClickedBrowseButton()
{
	CFileDialog fileDialog(TRUE, NULL, NULL, OFN_OVERWRITEPROMPT, "Executable files(*.exe)|*.exe||");

	if(fileDialog.DoModal() == IDOK)
	{
		GetDlgItem(IDC_PATH_EDIT)->SetWindowText(fileDialog.GetPathName());
	}
}
