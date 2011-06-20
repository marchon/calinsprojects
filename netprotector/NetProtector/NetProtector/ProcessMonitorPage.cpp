// ProcessMonitorPage.cpp : implementation file
//

#include "stdafx.h"

#include "NetProtector.h"
#include "ProcessMonitorPage.h"
#include "ProcessMonitorQueryDialog.h"
#include "AddAppRuleDlg.h"

#include <psapi.h>

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

// CProcessMonitorPage dialog

IMPLEMENT_DYNAMIC(CProcessMonitorPage, CColoredDlg)

CProcessMonitorPage *CProcessMonitorPage::ThisInstance = NULL;

CProcessMonitorPage::CProcessMonitorPage(CWnd* pParent /*=NULL*/)
	: CColoredDlg(CProcessMonitorPage::IDD, pParent)
{
	/* calin - open option key */
	if(RegOpenKeyEx(HKEY_LOCAL_MACHINE, NP_OPTIONS_REGISTRY_PATH, 0, KEY_ALL_ACCESS, &m_pMonStateKey) != ERROR_SUCCESS)
	{
		RegCreateKeyEx(HKEY_LOCAL_MACHINE, NP_OPTIONS_REGISTRY_PATH, 0,
			NULL, REG_OPTION_NON_VOLATILE, KEY_ALL_ACCESS, NULL, &m_pMonStateKey, NULL);
	}
	/* calin - open option key */

	ThisInstance = this;
}

CProcessMonitorPage::~CProcessMonitorPage()
{
	/* calin - close option key */
	RegCloseKey(m_pMonStateKey);
	/* calin - close option key */

	ThisInstance = NULL;
}

BEGIN_MESSAGE_MAP(CProcessMonitorPage, CColoredDlg)
	ON_MESSAGE(CONNECTION_QUERY, &CProcessMonitorPage::OnQueryProcessMonitor)
	ON_MESSAGE(PROCESS_ATTACHED, &CProcessMonitorPage::OnProcessAttached)
	ON_MESSAGE(PROCESS_DETACHED, &CProcessMonitorPage::OnProcessDetached)
	ON_WM_DESTROY()
	ON_BN_CLICKED(IDC_BUTTONADD_PM, &CProcessMonitorPage::OnBnClickedButtonaddPm)
	ON_BN_CLICKED(IDC_BUTTONDELETE_PM, &CProcessMonitorPage::OnBnClickedButtondeletePm)
	ON_BN_CLICKED(IDC_BUTTONDELETEALL_PM, &CProcessMonitorPage::OnBnClickedButtondeleteallPm)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_RULELIST_PM, &CProcessMonitorPage::OnLvnItemchangedRulelistPm)
	ON_NOTIFY(NM_RCLICK, IDC_RULELIST_PM, &CProcessMonitorPage::OnNMRClickRulelistPm)
	ON_COMMAND(ID_POPUP_ALLOW, &CProcessMonitorPage::OnPopupAllow)
	ON_COMMAND(ID_POPUP_BLOCK, &CProcessMonitorPage::OnPopupBlock)
	ON_COMMAND(ID_POPUP_APPLICATIONINFO, &CProcessMonitorPage::OnPopupApplicationinfo)
	ON_NOTIFY(NM_CLICK, IDC_RULELIST_PM, &CProcessMonitorPage::OnNMClickRulelistPm)
	ON_WM_LBUTTONDOWN()
END_MESSAGE_MAP()


void CProcessMonitorPage::DoDataExchange(CDataExchange* pDX)
{
	CColoredDlg::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_RULELIST_PM, m_AppRuleList);
	DDX_Control(pDX, IDC_BUTTONADD_PM, m_addButton);
	DDX_Control(pDX, IDC_BUTTONDELETE_PM, m_deleteButton);
	DDX_Control(pDX, IDC_BUTTONDELETEALL_PM, m_deleteAllButton);
	DDX_Control(pDX, IDC_APPRUL_STATIC, m_apprulTitle);
	DDX_Control(pDX, IDC_SESSIONRUL_STATIC, m_sessionrulTitle);
	DDX_Control(pDX, IDC_SESSIONRUL_STATIC2, m_sessionrul2Title);
}

BOOL CProcessMonitorPage::OnInitDialog()
{
	CColoredDlg::OnInitDialog();

	SetBkgColor(RGB(63, 91, 92));

	/* CALIN - init rule module */
	AppRule_InitModule();
	/* CALIN - init rule module */

	/* CALIN - prepare lsp for communication if pm enabled */
	DWORD size = sizeof(mb_pMonEnabled);
	if(RegQueryValueExA(m_pMonStateKey, NP_OPTION_REGISTRY_PM, 
		NULL, NULL, (LPBYTE)&mb_pMonEnabled, &size) != ERROR_SUCCESS)	//there is no such value, create it
	{
		mb_pMonEnabled = TRUE; //default behaviour is started
		RegSetValueEx(m_pMonStateKey, NP_OPTION_REGISTRY_PM, 0, 
			REG_BINARY, (LPBYTE)&mb_pMonEnabled, sizeof(mb_pMonEnabled));
	}

	if(mb_pMonEnabled)
		LspComm_InitComm();
	/* CALIN - prepare lsp for communication if pm enabled */

	m_infoToolTip.Create(this);

	m_apprulTitle.SetColor2(RGB(63, 91, 92));
	m_apprulTitle.SetTextColor(RGB(10, 25, 32));
	m_apprulTitle.SetLeftSpacing(0);
	m_apprulTitle.SetFont(&theApp.m_defautlFont);

	m_sessionrulTitle.SetColor2(RGB(63, 91, 92));
	m_sessionrulTitle.SetTextColor(RGB(10, 25, 32));
	m_sessionrulTitle.SetLeftSpacing(0);
	m_sessionrulTitle.SetFont(&theApp.m_defautlFont);

	m_sessionrul2Title.SetColor2(RGB(63, 91, 92));
	m_sessionrul2Title.SetTextColor(RGB(10, 25, 32));
	m_sessionrul2Title.SetLeftSpacing(0);
	m_sessionrul2Title.SetFont(&theApp.m_defautlFont);

	m_addButton.SetIcon(IDI_BUT_ADD);
	m_addButton.SetFont(&theApp.m_defautlFont);
	m_addButton.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_addButton.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_addButton.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_addButton.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_addButton.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_addButton.SetRounded(TRUE);


	m_deleteButton.SetIcon(IDI_BUT_DEL);
	m_deleteButton.SetFont(&theApp.m_defautlFont);
	m_deleteButton.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_deleteButton.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_deleteButton.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_deleteButton.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_deleteButton.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_deleteButton.SetRounded(TRUE);

	m_deleteAllButton.SetIcon(IDI_BUT_DELALL);
	m_deleteAllButton.SetFont(&theApp.m_defautlFont);
	m_deleteAllButton.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_deleteAllButton.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_deleteAllButton.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_deleteAllButton.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_deleteAllButton.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_deleteAllButton.SetRounded(TRUE);

	return TRUE;
}


// CProcessMonitorPage message handlers

void CProcessMonitorPage::OnDestroy()
{
	/* CALIN - shutdown lsp comm if enabled */
	if(mb_pMonEnabled)
		LspComm_ShutdownComm();
	/* CALIN - shutdown lsp comm if enabled */

	/* CALIN - close rule module */
	AppRule_CloseModule();
	/* CALIN - close rule module */
}

/* disable / enable firewall */
void CProcessMonitorPage::EnablePMon()
{
	if(mb_pMonEnabled)
		return;

	mb_pMonEnabled = TRUE;

	LspComm_InitComm();

	RegSetValueEx(m_pMonStateKey, NP_OPTION_REGISTRY_PM, 0, 
			REG_BINARY, (LPBYTE)&mb_pMonEnabled, sizeof(mb_pMonEnabled));
}

void CProcessMonitorPage::DisablePMon()
{
	if(!mb_pMonEnabled)
		return;

	mb_pMonEnabled = FALSE;

	LspComm_ShutdownComm();

	RegSetValueEx(m_pMonStateKey, NP_OPTION_REGISTRY_PM, 0, 
			REG_BINARY, (LPBYTE)&mb_pMonEnabled, sizeof(mb_pMonEnabled));
}

BOOL CProcessMonitorPage::IsEnabled()
{
	return mb_pMonEnabled;
}
/* disable / enable firewall */


/* CALIN - Layered service provider communication */
void CProcessMonitorPage::LspComm_InitComm()
{
	m_NetProtectorLspDll = LoadLibrary(DLL_PATH);
	
	if(m_NetProtectorLspDll == NULL)
		MessageBox("Error Loading NetProtectorLsp Library!");
	else
	{
		m_RegisterProcessMonitor = (register_pm_fp)GetProcAddress(m_NetProtectorLspDll,
			DLL_REGISTER_FUNCTION);

		if(m_RegisterProcessMonitor == NULL)
			MessageBox("Error while getting pointer to dll function RegisterProcessMonitor!");
		else
			/* CALIN - tell lsp the handle for window */
			m_RegisterProcessMonitor(this->GetSafeHwnd());
			/* CALIN - tell lsp the handle for window */
	}
}

void CProcessMonitorPage::LspComm_ShutdownComm()
{
	/* CALIN - tell lsp pm has closed */
	if(m_RegisterProcessMonitor)
		m_RegisterProcessMonitor(NULL);
	if(m_NetProtectorLspDll)
		FreeLibrary(m_NetProtectorLspDll);
	/* CALIN - tell lsp pm has closed */
}

/* CALIN - the message handlers for the messages from lsp */
LRESULT CProcessMonitorPage::OnQueryProcessMonitor(WPARAM wParam, LPARAM lParam)
{
	return AppRule_Check((DWORD)wParam);
}

LRESULT CProcessMonitorPage::OnProcessAttached(WPARAM wParam, LPARAM lParam)
{
	return 0;
}

LRESULT CProcessMonitorPage::OnProcessDetached(WPARAM wParam, LPARAM lParam)
{
	return 0;
}
/* CALIN - the message handlers for the messages from lsp */
/* CALIN - Layered service provider communication */



/* CALIN - process internet access request interface */
void CProcessMonitorPage::AppRule_InitModule()
{
	/* CALIN - add columns to rule list */
	RECT rc;

	m_AppRuleList.GetClientRect(&rc);

	int width = rc.right - rc.left;
	
	CString column;

	column.LoadString(IDS_NAME);
	m_AppRuleList.InsertColumn(0, column, LVCFMT_LEFT, 140, 0);
	column.LoadString(IDS_PATH);
	m_AppRuleList.InsertColumn(1, column, LVCFMT_LEFT, width - 200, 1);
	column.LoadString(IDS_ACTION);
	m_AppRuleList.InsertColumn(2, column, LVCFMT_LEFT, 60, 2);

	m_AppRuleList.SetExtendedStyle(LVS_EX_FULLROWSELECT | LVS_EX_GRIDLINES);
	m_AppRuleList.SetFont(&theApp.m_defautlFont);
	/* CALIN - add columns to rule list */

	//open reg key
	if(RegOpenKeyEx(HKEY_LOCAL_MACHINE, PM_RULES_REGISTRY_PATH, 0, KEY_ALL_ACCESS,
		&m_AppRuleRegistryKey) != ERROR_SUCCESS)
	{
		RegCreateKeyEx(HKEY_LOCAL_MACHINE, PM_RULES_REGISTRY_PATH, 0,
			NULL, REG_OPTION_NON_VOLATILE, KEY_ALL_ACCESS, NULL, &m_AppRuleRegistryKey, NULL);
	}

	//image list
	m_imageList.Create(16, 16, ILC_COLOR8 | ILC_MASK, 8, 1);
	m_AppRuleList.SetImageList(&m_imageList, LVSIL_SMALL);
	m_imageList.Add(::LoadIcon(NULL, IDI_WINLOGO));

	//load rules from registry
	int ind = 0;
	char appPath[MAX_PATH];
	DWORD appPathLength = MAX_PATH;
	DWORD type;
	byte action;
	DWORD size_action = sizeof(byte);

	while(RegEnumValue(m_AppRuleRegistryKey, ind++, appPath, 
		&appPathLength, NULL, &type, (LPBYTE)&action, &size_action) != ERROR_NO_MORE_ITEMS)
	{
		AppRule *appRule = new AppRule;

		appRule->path = appPath;
		appRule->action = action;

		m_AppRuleVector.push_back(appRule);
		AppRule_AddRuleToList(appRule);

		appPathLength = MAX_PATH;
	}
}

void CProcessMonitorPage::AppRule_CloseModule()
{
	size_t len = m_AppRuleVector.size();

	for(size_t t = 0; t < len; t++)
		delete m_AppRuleVector[t];

	m_AppRuleVector.clear();

	RegCloseKey(m_AppRuleRegistryKey);

	m_AppRuleList.DeleteAllItems();

	m_imageList.DeleteImageList();
}

void CProcessMonitorPage::AppRule_AddRule(CString &appPath, byte action, byte isSessionRule /* = 0*/)
{
	AppRule *appRule = new AppRule;

	appRule->path = appPath;
	appRule->action = action;
	appRule->isSessionRule = isSessionRule;

	m_AppRuleVector.push_back(appRule);
	AppRule_AddRuleToList(appRule);

	if(!isSessionRule) //if is not a session rule seve it
		RegSetValueEx(m_AppRuleRegistryKey, (LPCSTR)appPath, 0, REG_BINARY, (BYTE *)&action, sizeof(byte));
}

void CProcessMonitorPage::AppRule_ModifySelectedRule(byte action)
{
	int index = m_AppRuleList.GetSelectionMark();

	if(index == -1)
		return;

	AppRule *appRule = m_AppRuleVector[index];

	appRule->action = action;
	if(action == LRESULT_ALLOW)
		m_AppRuleList.SetItem(index, 2, LVIF_TEXT, "Allow", 0, 0, 0, NULL);
	else
		m_AppRuleList.SetItem(index, 2, LVIF_TEXT, "Block", 0, 0, 0, NULL);

	if(!appRule->isSessionRule) //if is not a session rule seve it
		RegSetValueEx(m_AppRuleRegistryKey, (LPCSTR)appRule->path, 0, REG_BINARY, (BYTE *)&action, sizeof(byte));
}

void CProcessMonitorPage::AppRule_DelSelectedRule()
{
	int index = m_AppRuleList.GetSelectionMark();

	if(index == -1)
		return;

	m_AppRuleList.DeleteItem(index);

	if(!m_AppRuleVector[index]->isSessionRule)//if is not a session rule delete it
		RegDeleteValue(m_AppRuleRegistryKey, (LPCSTR)m_AppRuleVector[index]->path);

	delete m_AppRuleVector[index];

	m_AppRuleVector.erase(m_AppRuleVector.begin() + index);
	//todo: delete icon from imagelist and do proper modifications to image offsets for each item
	//...orelse...memory leak when readding the deleted application
	//(will load the same icon even if it already exists in the image list)
}

CString CProcessMonitorPage::AppRule_GetSelectedApp()
{
	int index = m_AppRuleList.GetSelectionMark();

	if(index == -1)
		return CString("");

	return CString(m_AppRuleVector[index]->path);
}

void CProcessMonitorPage::AppRule_DelAllRules()
{
	size_t len = m_AppRuleVector.size();

	for(size_t t = 0; t < len; t++)
	{
		if(!m_AppRuleVector[t]->isSessionRule)//if is not a session rule delete it
			RegDeleteValue(m_AppRuleRegistryKey, (LPCSTR)m_AppRuleVector[t]->path);
		delete m_AppRuleVector[t];
	}

	m_AppRuleList.DeleteAllItems();

	m_AppRuleVector.clear();
}

byte CProcessMonitorPage::AppRule_Check(DWORD pid)
{
	CString path = GetProcessNameByPid(pid);

	size_t len = m_AppRuleVector.size();
	
	CString lowerPath = path;
	lowerPath.MakeLower();

	for(size_t t = 0; t < len; t++)
	{
		CString ruleLowerPath = m_AppRuleVector[t]->path;
		ruleLowerPath.MakeLower();

		if(!lowerPath.Compare(ruleLowerPath))
			return m_AppRuleVector[t]->action;
	}

	CProcessMonitorQueryDialog queryDlg;
	queryDlg.programIdentifier = path;

	if(queryDlg.DoModal() == IDOK)
	{
		AppRule_AddRule(path, LRESULT_ALLOW, queryDlg.m_thisSessionOnly);
		return LRESULT_ALLOW;
	}
	else
		AppRule_AddRule(path, LRESULT_BLOCK, queryDlg.m_thisSessionOnly);

	return LRESULT_BLOCK;
}

void CProcessMonitorPage::AppRule_AddRuleToList(AppRule *appRule)
{
	CPath pt(appRule->path);

	//load icon
	HICON icon = NULL;

	icon = ::ExtractIcon(NULL, appRule->path, 0);
	
	int iconOffset = 0;
	if(icon)
		iconOffset = m_imageList.Add(icon);

	::DestroyIcon(icon);

	//add item
	CString name = pt.GetFileName();
	if(appRule->isSessionRule)
		name.Append("(*)");

	int index = m_AppRuleList.InsertItem(LVIF_TEXT | LVIF_IMAGE, m_AppRuleList.GetItemCount(), 
		name, 0, 0, iconOffset, NULL);

	m_AppRuleList.SetItem(index, 1, LVIF_TEXT, pt.GetFolderPath(), 0, 0, 0, NULL);

	if(appRule->action == LRESULT_ALLOW)
		m_AppRuleList.SetItem(index, 2, LVIF_TEXT, "Allow", 0, 0, 0, NULL);
	else
		m_AppRuleList.SetItem(index, 2, LVIF_TEXT, "Block", 0, 0, 0, NULL);
}

CString CProcessMonitorPage::GetProcessNameByPid(DWORD pid)
{
	HANDLE hProc = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, FALSE, pid);

	if(hProc == NULL)
		return 0;

	char path[MAX_PATH];

	GetModuleFileNameExA(hProc, NULL, path, MAX_PATH);

	CString retVal = path;

	return retVal;
}
/* CALIN - process internet access request interface */



/* CALIN - buttons and handlers */
void CProcessMonitorPage::OnBnClickedButtonaddPm()
{
	m_infoToolTip.Close();
	CAddAppRuleDlg appRuleDlg;

	if(appRuleDlg.DoModal() == IDOK)
	{
		size_t len = m_AppRuleVector.size();
		
		CString lowerPath = appRuleDlg.path;
		lowerPath.MakeLower();

		for(size_t t = 0; t < len; t++)
		{
			CString ruleLowerPath = m_AppRuleVector[t]->path;
			ruleLowerPath.MakeLower();

			if(!lowerPath.Compare(ruleLowerPath))
			{
				char message[MAX_PATH + 100];
				sprintf(message, "There is already a rule specified for %s!\nNo rule will be added.", 
					(char *)(LPCSTR)appRuleDlg.path); 

				MessageBox(message);

				return;
			}				
		}

		AppRule_AddRule(appRuleDlg.path, appRuleDlg.action);
	}
}

void CProcessMonitorPage::OnBnClickedButtondeletePm()
{
	m_infoToolTip.Close();
	AppRule_DelSelectedRule();
}

void CProcessMonitorPage::OnBnClickedButtondeleteallPm()
{
	m_infoToolTip.Close();
	if(m_AppRuleList.GetItemCount() > 0)
	{
		if(MessageBox("Are you sure you want to delete all the rules?",
			"Confirm...", MB_YESNO) == IDYES)
		{
			AppRule_DelAllRules();
		}
	}
}

void CProcessMonitorPage::OnLvnItemchangedRulelistPm(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);

	int nItemCount = m_AppRuleList.GetSelectedCount();
	if(pNMLV->iItem >= 0 && nItemCount == 1)
	{
		m_deleteButton.EnableWindow(TRUE);
	}
	else
	{
		m_deleteButton.EnableWindow(FALSE);
	}
	
	*pResult = 0;
}

void CProcessMonitorPage::OnNMRClickRulelistPm(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMITEMACTIVATE pNMItemActivate = reinterpret_cast<LPNMITEMACTIVATE>(pNMHDR);
	
	m_infoToolTip.Close();
	if(pNMItemActivate->iItem != -1)
	{
		CMenu m;
		m.LoadMenu(IDR_ACTIONMENU);

		CMenu *menu = m.GetSubMenu(0);
		ASSERT(menu);

		POINT pos;
		GetCursorPos(&pos);
		
		menu->CheckMenuItem(m_AppRuleVector[pNMItemActivate->iItem]->action, MF_CHECKED | MF_BYPOSITION);
		menu->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, pos.x, pos.y, this);	
	}

	*pResult = 0;
}

void CProcessMonitorPage::OnPopupAllow()
{
	AppRule_ModifySelectedRule(LRESULT_ALLOW);
}

void CProcessMonitorPage::OnPopupBlock()
{
	AppRule_ModifySelectedRule(LRESULT_BLOCK);
}

void CProcessMonitorPage::OnPopupApplicationinfo()
{
	CPath appPath = AppRule_GetSelectedApp();
	CStringArray appInfo;

	CString str;
	str.Format("%s version info:", appPath.GetFileName());
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_ProductName, appPath.GetFileVersionInfoString(VERINFO_ProductName));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_CompanyName, appPath.GetFileVersionInfoString(VERINFO_CompanyName));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_ProductVersion, appPath.GetFileVersionInfoString(VERINFO_ProductVersion));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_comments, appPath.GetFileVersionInfoString(VERINFO_comments));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_FileDescrib, appPath.GetFileVersionInfoString(VERINFO_FileDescrib));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_FileVersion, appPath.GetFileVersionInfoString(VERINFO_FileVersion));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_LegalCopyright, appPath.GetFileVersionInfoString(VERINFO_LegalCopyright));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_LegalTrademarks, appPath.GetFileVersionInfoString(VERINFO_LegalTrademarks));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_OriginalFilename, appPath.GetFileVersionInfoString(VERINFO_OriginalFilename));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_PrivateBuild, appPath.GetFileVersionInfoString(VERINFO_PrivateBuild));
	appInfo.Add(str);
	str.Format("%s: %s", VERINFO_SpecialBuild, appPath.GetFileVersionInfoString(VERINFO_SpecialBuild));
	appInfo.Add(str);

	m_infoToolTip.SetText(appInfo);

	POINT pos;
	GetCursorPos(&pos);
	::ScreenToClient(this->GetSafeHwnd(), &pos);

	m_infoToolTip.Show(pos, 10000);
}

void CProcessMonitorPage::OnNMClickRulelistPm(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMITEMACTIVATE pNMItemActivate = reinterpret_cast<LPNMITEMACTIVATE>(pNMHDR);

	m_infoToolTip.Close();

	*pResult = 0;
}

void CProcessMonitorPage::OnLButtonDown(UINT nFlags, CPoint point)
{

	m_infoToolTip.Close();
	CColoredDlg::OnLButtonDown(nFlags, point);
}
/* CALIN - buttons and handlers */



