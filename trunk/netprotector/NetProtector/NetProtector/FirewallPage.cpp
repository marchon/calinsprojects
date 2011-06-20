// CFirewallPage message handlers
// FirewallPage.cpp : implementation file
//

#include "stdafx.h"
#include "NetProtector.h"
#include "FirewallPage.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

// CFirewallPage dialog

IMPLEMENT_DYNAMIC(CFirewallPage, CColoredDlg)

CFirewallPage *CFirewallPage::ThisInstance = NULL;

CFirewallPage::CFirewallPage(CWnd* pParent /*=NULL*/)
	: CColoredDlg(CFirewallPage::IDD, pParent)
{
	/* calin - open rules key */
	if(RegOpenKeyEx(HKEY_LOCAL_MACHINE, FI_RULES_REGISTRY_PATH, 0, KEY_ALL_ACCESS, &m_rulesRegistryKey) != ERROR_SUCCESS)
	{
		RegCreateKeyEx(HKEY_LOCAL_MACHINE, FI_RULES_REGISTRY_PATH, 0,
			NULL, REG_OPTION_NON_VOLATILE, KEY_ALL_ACCESS, NULL, &m_rulesRegistryKey, NULL);
	}
	/* calin - open rules key */

	/* calin - open option key */
	if(RegOpenKeyEx(HKEY_LOCAL_MACHINE, NP_OPTIONS_REGISTRY_PATH, 0, KEY_ALL_ACCESS, &m_fireStateKey) != ERROR_SUCCESS)
	{
		RegCreateKeyEx(HKEY_LOCAL_MACHINE, NP_OPTIONS_REGISTRY_PATH, 0,
			NULL, REG_OPTION_NON_VOLATILE, KEY_ALL_ACCESS, NULL, &m_fireStateKey, NULL);
	}
	/* calin - open option key */
	ThisInstance = this;
}

CFirewallPage::~CFirewallPage()
{
	/* calin - close rules key */
	RegCloseKey(m_rulesRegistryKey);
	/* calin - close rules key */

	/* calin - close option key */
	RegCloseKey(m_fireStateKey);
	/* calin - close option key */

	rulesMap.clear();

	ThisInstance = NULL;
}

void CFirewallPage::DoDataExchange(CDataExchange* pDX)
{
	CColoredDlg::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_RULELIST, m_ruleList);
	DDX_Control(pDX, IDC_BUTTONADD, m_addRuleButton);
	DDX_Control(pDX, IDC_BUTTONEDIT, m_editRuleButton);
	DDX_Control(pDX, IDC_BUTTONDELETE, m_deleteRuleButton);
	DDX_Control(pDX, IDC_BUTTONDELETEALL, m_deleteAllRulesButton);
	DDX_Control(pDX, IDC_PFR_STATIC, m_pfrTitle);
}

BOOL CFirewallPage::OnInitDialog()
{
	CColoredDlg::OnInitDialog();

	SetBkgColor(RGB(63, 91, 92));

	/* start firewall */
	CFirewall::LoadFirewallModule();

	DWORD size = sizeof(mb_firewallEnabled);
	if(RegQueryValueExA(m_fireStateKey, NP_OPTION_REGISTRY_FIRE, 
		NULL, NULL, (LPBYTE)&mb_firewallEnabled, &size) != ERROR_SUCCESS)	//there is no such value, create it
	{
		mb_firewallEnabled = TRUE; //default behaviour is started
		RegSetValueEx(m_fireStateKey, NP_OPTION_REGISTRY_FIRE, 0, 
			REG_BINARY, (LPBYTE)&mb_firewallEnabled, sizeof(mb_firewallEnabled));
	}

	if(mb_firewallEnabled)
		CFirewall::StartFirewall();
	/* start firewall */

	/* CALIN - add columns to rule list */

	RECT rc;
	m_ruleList.GetClientRect(&rc);

	int width = rc.right - rc.left;

	CString column;

	column.LoadString(IDS_SRCIP);
	m_ruleList.InsertColumn(0, column,	LVCFMT_LEFT, width / 5 - 5, 0);
	column.LoadString(IDS_DESTIP);
	m_ruleList.InsertColumn(1, column,	LVCFMT_LEFT, width / 5    , 1);
	column.LoadString(IDS_SRCPORT);
	m_ruleList.InsertColumn(2, column,	LVCFMT_LEFT, width / 5 - 5   , 2);
	column.LoadString(IDS_DESTPORT);
	m_ruleList.InsertColumn(3, column,	LVCFMT_LEFT, width / 5 + 15, 3);
	column.LoadString(IDS_PROTOCOL);
	m_ruleList.InsertColumn(4, column,	LVCFMT_LEFT, width / 5 - 2   , 4);

	m_ruleList.SetExtendedStyle(LVS_EX_FULLROWSELECT | LVS_EX_GRIDLINES);
	m_ruleList.SetFont(&theApp.m_defautlFont);
	/* CALIN - add columns to rule list */

	m_pfrTitle.SetColor2(RGB(63, 91, 92));
	m_pfrTitle.SetTextColor(RGB(10, 25, 32));
	m_pfrTitle.SetLeftSpacing(0);
	m_pfrTitle.SetFont(&theApp.m_defautlFont);

	m_addRuleButton.SetIcon(IDI_BUT_ADD);
	m_addRuleButton.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_addRuleButton.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_addRuleButton.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_addRuleButton.SetFont(&theApp.m_defautlFont);
	m_addRuleButton.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_addRuleButton.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_addRuleButton.SetRounded(TRUE);

	m_editRuleButton.SetIcon(IDI_BUT_EDIT);
	m_editRuleButton.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_editRuleButton.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_editRuleButton.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_editRuleButton.SetFont(&theApp.m_defautlFont);
	m_editRuleButton.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_editRuleButton.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_editRuleButton.SetRounded(TRUE);

	m_deleteRuleButton.SetIcon(IDI_BUT_DEL);
	m_deleteRuleButton.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_deleteRuleButton.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_deleteRuleButton.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_deleteRuleButton.SetFont(&theApp.m_defautlFont);
	m_deleteRuleButton.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_deleteRuleButton.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_deleteRuleButton.SetRounded(TRUE);

	m_deleteAllRulesButton.SetIcon(IDI_BUT_DELALL);
	m_deleteAllRulesButton.SetColor(CButtonST::BTNST_COLOR_BK_IN, RGB(63, 91, 92));
	m_deleteAllRulesButton.SetColor(CButtonST::BTNST_COLOR_BK_OUT, RGB(63, 91, 92));
	m_deleteAllRulesButton.SetColor(CButtonST::BTNST_COLOR_BK_FOCUS, RGB(63, 91, 92));
	m_deleteAllRulesButton.SetFont(&theApp.m_defautlFont);
	m_deleteAllRulesButton.SetColor(CButtonST::BTNST_COLOR_FG_IN, RGB(100, 25, 32));
	m_deleteAllRulesButton.SetColor(CButtonST::BTNST_COLOR_FG_OUT, RGB(10, 25, 32));
	m_deleteAllRulesButton.SetRounded(TRUE);

	m_editRuleButton.EnableWindow(FALSE);
	m_deleteRuleButton.EnableWindow(FALSE);

	LoadRulesFromRegistry();

	return TRUE;
}

void CFirewallPage::OnDestroy()
{
	CColoredDlg::OnDestroy();

	/* stop firewall */
	if(mb_firewallEnabled)
		CFirewall::StopFirewall();
	CFirewall::UnloadFirewallModule();
	/* stop firewall */
}

/* disable / enable firewall */
void CFirewallPage::EnableFirewall()
{
	if(mb_firewallEnabled)
		return;

	mb_firewallEnabled = TRUE;

	CFirewall::StartFirewall();

	RegSetValueEx(m_fireStateKey, NP_OPTION_REGISTRY_FIRE, 0, 
			REG_BINARY, (LPBYTE)&mb_firewallEnabled, sizeof(mb_firewallEnabled));
}

void CFirewallPage::DisableFirewall()
{
	if(!mb_firewallEnabled)
		return;

	mb_firewallEnabled = FALSE;

	CFirewall::StopFirewall();

	RegSetValueEx(m_fireStateKey, NP_OPTION_REGISTRY_FIRE, 0, 
			REG_BINARY, (LPBYTE)&mb_firewallEnabled, sizeof(mb_firewallEnabled));
}

BOOL CFirewallPage::IsEnabled()
{
	return mb_firewallEnabled;
}
/* disable / enable firewall */


BEGIN_MESSAGE_MAP(CFirewallPage, CColoredDlg)
	ON_BN_CLICKED(IDC_BUTTONADD, &CFirewallPage::OnBnClickedButtonadd)
	ON_BN_CLICKED(IDC_BUTTONEDIT, &CFirewallPage::OnBnClickedButtonedit)
	ON_BN_CLICKED(IDC_BUTTONDELETE, &CFirewallPage::OnBnClickedButtonDelete)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_RULELIST, &CFirewallPage::OnLvnItemchangedRulelist)
	ON_BN_CLICKED(IDC_BUTTONDELETEALL, &CFirewallPage::OnBnClickedButtondeleteall)
	ON_WM_DESTROY()
END_MESSAGE_MAP()


// CFirewallPage message handlers

/* calin - handlers */
void CFirewallPage::OnBnClickedButtonadd()
{
	CAddRuleDlg ruleDlg;

	if(ruleDlg.DoModal() == IDOK)
	{
		CreateRule(ruleDlg.m_protocol,
			ruleDlg.m_srcIp,
			ruleDlg.m_destIp,
			ruleDlg.m_srcPort,
			ruleDlg.m_destPort);
	}
}

void CFirewallPage::OnBnClickedButtonedit()
{

	int index = m_ruleList.GetSelectionMark();

	CAddRuleDlg ruleDlg;

	ruleDlg.m_srcIp = m_ruleList.GetItemText(index, 0);
	ruleDlg.m_destIp = m_ruleList.GetItemText(index, 1);
	ruleDlg.m_srcPort = m_ruleList.GetItemText(index, 2);
	ruleDlg.m_destPort = m_ruleList.GetItemText(index, 3);

	CString proto = m_ruleList.GetItemText(index, 4);
	if(!proto.Compare("ICMP"))
		ruleDlg.m_protocol = 0;
	else if(!proto.Compare("TCP"))
		ruleDlg.m_protocol = 1;
	else //udp
		ruleDlg.m_protocol = 2;

	if(ruleDlg.DoModal() == IDOK)
	{
		RemoveRule(index); //very ugly but...

		CreateRule(ruleDlg.m_protocol,
			ruleDlg.m_srcIp,
			ruleDlg.m_destIp,
			ruleDlg.m_srcPort,
			ruleDlg.m_destPort);
	}

}

void CFirewallPage::OnBnClickedButtonDelete()
{
	RemoveRule(m_ruleList.GetSelectionMark());
}

void CFirewallPage::OnBnClickedButtondeleteall()
{
	if(m_ruleList.GetItemCount() > 0)
	{
		if(MessageBox("Are you sure you want to delete all the rules?",
			"Confirm...", MB_YESNO) == IDYES)
		{		
			while(m_ruleList.GetItemCount())
				RemoveRule(0);
		}
	}
}

void CFirewallPage::OnLvnItemchangedRulelist(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);

	int nItemCount = m_ruleList.GetSelectedCount();
	if(pNMLV->iItem >= 0 && nItemCount == 1)
	{
		m_editRuleButton.EnableWindow(TRUE);
		m_deleteRuleButton.EnableWindow(TRUE);
	}
	else
	{
		m_editRuleButton.EnableWindow(FALSE);
		m_deleteRuleButton.EnableWindow(FALSE);
	}

	*pResult = 0;
}
/* calin - handlers */




/* calin - some usefull rutines */
IPFilter CFirewallPage::StringsToIPFilter(int proto, CString &srcIp, CString &destIp, 
										  CString &srcPort, CString &destPort)
{
	IPFilter ipf;
	
	switch(proto)
	{
	case 0:
		ipf.protocol = 1;	//icmp
		break;
	case 1:
		ipf.protocol = 6;	//tcp
		break;
	case 2:
		ipf.protocol = 17;	//udp
		break;
	default:
		ipf.protocol = 0;	//all
		break;
	}

	ipf.sourceIp = inet_addr(srcIp);
	ipf.destinationIp = inet_addr(destIp);

	ipf.sourceMask = inet_addr("255.255.255.255");
	ipf.destinationMask = inet_addr("255.255.255.255");

	ipf.sourcePort = static_cast<USHORT>(htons((USHORT)_ttoi((LPCTSTR) srcPort)));;
	ipf.destinationPort = static_cast<USHORT>(htons((USHORT)_ttoi((LPCTSTR) destPort)));;;

	ipf.drop = TRUE;

	return ipf;
}

void CFirewallPage::IPFilterToStrings(IPFilter *ipFilter, int &proto, CString &srcIp, 
									  CString &destIp, CString &srcPort, CString &destPort)
{
	switch(ipFilter->protocol)
	{
	case 1:
		proto = 0;	//icmp
		break;
	case 6:
		proto = 1;	//tcp
		break;
	case 17:
		proto = 2;	//udp
		break;
	default:
		proto = -1;	//error
		break;
	}
	
	in_addr stIpAddr;

	stIpAddr.s_addr = ipFilter->sourceIp;
	srcIp = inet_ntoa(stIpAddr);

	stIpAddr.s_addr = ipFilter->destinationIp;
	destIp = inet_ntoa(stIpAddr);

	USHORT portNb;

	portNb = ntohs(ipFilter->sourcePort);
	srcPort.Format("%d", portNb);

	portNb = ntohs(ipFilter->destinationPort);
	destPort.Format("%d", portNb);
}

void CFirewallPage::CreateRule(int proto, CString &srcIp, CString &destIp, 
							   CString &srcPort, CString &destPort)
{
	/* make an ip filter */
	IPFilter ipf = StringsToIPFilter(proto, srcIp, destIp, srcPort, destPort);
	/* make an ip filter */

	/* add rule to firewall */
	ULONG id = CFirewall::AddRule(&ipf);
	/* add rule to firewall */

	/* add it to the list */
	AddRuleToList(proto, srcIp, destIp, srcPort, destPort, id);
	/* add it to the list */

	/* add entry in the registry */
	GUID guid;
	CoCreateGuid(&guid);

	rulesMap[id] = guid;

	MAKE_GUID_STR(guid, guidStr);

	RegSetValueEx(m_rulesRegistryKey, guidStr, 0, REG_BINARY, (BYTE *)&ipf, sizeof(IPFilter));

	FREE_GUID_STR(guidStr);
	/* add entry in the registry */
}

void CFirewallPage::AddRuleToList(int proto, CString &srcIp, CString &destIp, 
								  CString &srcPort, CString &destPort, ULONG id)
{
	int index = m_ruleList.InsertItem(LVIF_TEXT | LVIF_PARAM, m_ruleList.GetItemCount(), 
		srcIp, 0, 0, 0, (LPARAM)id);
	m_ruleList.SetItem(index, 1, LVIF_TEXT, destIp, 0, 0, 0, NULL);
	m_ruleList.SetItem(index, 2, LVIF_TEXT, srcPort, 0, 0, 0, NULL);
	m_ruleList.SetItem(index, 3, LVIF_TEXT, destPort, 0, 0, 0, NULL);

	CString sProto;
	if(proto == 0)
		sProto = "ICMP";
	else if(proto == 1)
		sProto = "TCP";
	else sProto = "UDP";

	m_ruleList.SetItem(index, 4, LVIF_TEXT, sProto, 0, 0, 0, NULL);
}

void CFirewallPage::AddRuleToList(IPFilter *ipFilter, ULONG id)
{
	int proto;
	CString srcIp;
	CString destIp;
	CString srcPort;
	CString destPort;

	IPFilterToStrings(ipFilter, proto, srcIp, destIp, srcPort, destPort);

	AddRuleToList(proto, srcIp, destIp, srcPort, destPort, id);
}

void CFirewallPage::RemoveRule(int index)
{
	/* get rule id */
	ULONG id = (ULONG)m_ruleList.GetItemData(index);
	/* get rule id */

	/* remove from list control */
	m_ruleList.DeleteItem(index);
	/* remove from list control */

	/* remove form firewall */
	CFirewall::RemoveRule(id);
	/* remove form firewall */

	/* remove entry from registry */
	MAKE_GUID_STR(rulesMap[id], guidStr);
	RegDeleteValue(m_rulesRegistryKey, guidStr);
	FREE_GUID_STR(guidStr);
	/* remove entry from registry */

	/* remove rule from map */
	rulesMap.erase(rulesMap.find(id));
	/* remove rule from map */
}

void CFirewallPage::LoadRulesFromRegistry()
{
	int ind = 0;

	char *guidStr = new char[sizeof(GUID) + 1];
	DWORD size_guid = sizeof(GUID) + 1;

	IPFilter ipf;
	DWORD size_ipf = sizeof(IPFilter);

	DWORD type;

	while(RegEnumValue(m_rulesRegistryKey, ind++, guidStr, 
		&size_guid, NULL, &type, (LPBYTE)&ipf, &size_ipf) != ERROR_NO_MORE_ITEMS)
	{
		/* add rule to firewall */
		ULONG id = CFirewall::AddRule(&ipf);
		/* add rule to firewall */

		/* add rule in list */
		AddRuleToList(&ipf, id);
		/* add rule in list */

		/* add to asociative array */
		MAKE_GUID_FROM_STR(guidStr, guid);
		rulesMap[id] = guid;
		/* add to asociative array */

		size_guid = sizeof(GUID) + 1;
	}

	delete[] guidStr;
}
/* calin - some usefull rutines */
