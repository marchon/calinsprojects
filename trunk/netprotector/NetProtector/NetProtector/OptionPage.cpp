// GeneralPage.cpp : implementation file
//

#include "stdafx.h"
#include "NetProtector.h"
#include "OptionPage.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

// GeneralPage dialog

IMPLEMENT_DYNAMIC(COptionPage, CColoredDlg)

COptionPage::COptionPage(CWnd* pParent /*=NULL*/)
	: CColoredDlg(COptionPage::IDD, pParent)
{
	m_runKey = new HKEY;
	if(RegOpenKeyEx(HKEY_LOCAL_MACHINE, NP_BOOT_REGISTRY_PATH, 
		0, KEY_ALL_ACCESS, m_runKey) != ERROR_SUCCESS)
	{
		delete m_runKey;
		m_runKey = NULL;
	}
}

COptionPage::~COptionPage()
{
	if(m_runKey)
	{
		RegCloseKey(*m_runKey);
		delete m_runKey;
	}
}

void COptionPage::DoDataExchange(CDataExchange* pDX)
{
	CColoredDlg::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_START_BOOT, m_startBootCheck);
	DDX_Control(pDX, IDC_ENABLE_FIRE, m_enableFireCheck);
	DDX_Control(pDX, IDC_ENABLE_PM, m_enablePmCheck);
}

BOOL COptionPage::OnInitDialog()
{
	CColoredDlg::OnInitDialog();

	SetBkgColor(RGB(63, 91, 92));

	m_startBootCheck.DrawBorder(FALSE);
	m_enableFireCheck.DrawBorder(FALSE);
	m_enablePmCheck.DrawBorder(FALSE);

	m_startBootCheck.SetIcon(IDI_CHECK_ON, IDI_CHECK_OFF);
	m_enableFireCheck.SetIcon(IDI_CHECK_ON, IDI_CHECK_OFF);
	m_enablePmCheck.SetIcon(IDI_CHECK_ON, IDI_CHECK_OFF);

	m_startBootCheck.SetFont(&theApp.m_defautlFont);
	m_startBootCheck.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_startBootCheck.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_startBootCheck.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_startBootCheck.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_startBootCheck.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_startBootCheck.SetRounded(TRUE);

	m_enableFireCheck.SetFont(&theApp.m_defautlFont);
	m_enableFireCheck.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_enableFireCheck.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_enableFireCheck.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_enableFireCheck.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_enableFireCheck.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_enableFireCheck.SetRounded(TRUE);

	m_enablePmCheck.SetFont(&theApp.m_defautlFont);
	m_enablePmCheck.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_enablePmCheck.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_enablePmCheck.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_enablePmCheck.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_enablePmCheck.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_enablePmCheck.SetRounded(TRUE);

	m_startBootCheck.SetBtnCursor(IDC_HAND2);
	m_enableFireCheck.SetBtnCursor(IDC_HAND2);
	m_enablePmCheck.SetBtnCursor(IDC_HAND2);

	GetDlgItem(IDC_STATIC_NP)->SetFont(&theApp.m_defautlFont);
	GetDlgItem(IDC_STATIC_FI)->SetFont(&theApp.m_defautlFont);
	GetDlgItem(IDC_STATIC_PM)->SetFont(&theApp.m_defautlFont);

	/* boot check box */
	int check = 0;
	if(m_runKey && 
		(ERROR_SUCCESS == RegQueryValueEx(*m_runKey, NP_BOOT_REGISTRY_VALUE, 
		NULL, NULL, NULL, NULL)))
		check = 1;
	m_startBootCheck.SetCheck(check);
	if(!m_runKey)
		m_startBootCheck.EnableWindow(FALSE);
	/* boot check box */

	/* enable / disable firewall check box */
	if(CFirewallPage::ThisInstance)
		m_enableFireCheck.SetCheck(CFirewallPage::ThisInstance->IsEnabled());
	/* enable / disable firewall check box */

	/* enable / disable pm check box */
	if(CProcessMonitorPage::ThisInstance)
		m_enablePmCheck.SetCheck(CProcessMonitorPage::ThisInstance->IsEnabled());
	/* enable / disable pm check box */

	return TRUE;
}


BEGIN_MESSAGE_MAP(COptionPage, CColoredDlg)
	ON_BN_CLICKED(IDC_START_BOOT, &COptionPage::OnBnClickedStartBoot)
	ON_BN_CLICKED(IDC_ENABLE_FIRE, &COptionPage::OnBnClickedEnableFire)
	ON_BN_CLICKED(IDC_ENABLE_PM, &COptionPage::OnBnClickedEnablePm)
END_MESSAGE_MAP()


// GeneralPage message handlers

void COptionPage::OnBnClickedStartBoot()
{
	if(ERROR_SUCCESS == RegQueryValueEx(*m_runKey, NP_BOOT_REGISTRY_VALUE, 
		NULL, NULL, NULL, NULL)) //exists, delete it
	{
		RegDeleteValue(*m_runKey, NP_BOOT_REGISTRY_VALUE);
	}
	else//create it
	{
		CString processPath = CProcessMonitorPage::GetProcessNameByPid(GetCurrentProcessId());
		char *buffer = (char *)(LPCSTR)processPath;
		RegSetValueEx(*m_runKey, NP_BOOT_REGISTRY_VALUE, 0, 
					REG_SZ, (BYTE *)buffer, strlen(buffer) + 1);
	}
}

void COptionPage::OnBnClickedEnableFire()
{
	if(CFirewallPage::ThisInstance)
		if(CFirewallPage::ThisInstance->IsEnabled()) //stop firewall
		{
			CFirewallPage::ThisInstance->DisableFirewall();
		}
		else	//start firewall
		{
			CFirewallPage::ThisInstance->EnableFirewall();
		}
}

void COptionPage::OnBnClickedEnablePm()
{
	if(CProcessMonitorPage::ThisInstance)
		if(CProcessMonitorPage::ThisInstance->IsEnabled()) //stop pm
		{
			CProcessMonitorPage::ThisInstance->DisablePMon();
		}
		else	//start pm
		{
			CProcessMonitorPage::ThisInstance->EnablePMon();
		}
}
