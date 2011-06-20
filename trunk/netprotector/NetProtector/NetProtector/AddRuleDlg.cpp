// AddRuleDlg.cpp : implementation file
//

#include "stdafx.h"
#include "NetProtector.h"
#include "AddRuleDlg.h"
#include "Firewall.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif


// CAddRuleDlg dialog

IMPLEMENT_DYNAMIC(CAddRuleDlg, CDialog)

CAddRuleDlg::CAddRuleDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CAddRuleDlg::IDD, pParent)
{
	m_protocol = 1;
}

CAddRuleDlg::~CAddRuleDlg()
{
}

void CAddRuleDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);

	DDX_Control(pDX, IDC_PROTO, m_protocolCombo);

	DDX_Text(pDX, IDC_SRCIP, m_srcIp);
	DDV_MaxChars(pDX, m_srcIp, 15);

	DDX_Text(pDX, IDC_SRCPORT, m_srcPort);

	DDX_Text(pDX, IDC_DESTIP, m_destIp);
	DDV_MaxChars(pDX, m_destIp, 15);

	DDX_Text(pDX, IDC_DESTPORT, m_destPort);

	DDX_Control(pDX, IDC_SRCIP, m_srcIpRestricted);
	DDX_Control(pDX, IDC_DESTIP, m_destIpRestricted);
	DDX_Control(pDX, IDC_SRCPORT, m_srcPortRestricted);
	DDX_Control(pDX, IDC_DESTPORT, m_destPortRestricted);
	DDX_Control(pDX, IDC_BUTTON_SRCIP_POPUP, m_srcIpButton);
	DDX_Control(pDX, IDC_BUTTON_DESTIP_POPUP, m_destIpButton);
	DDX_Control(pDX, IDC_BUTTON_SRCPORT_POPUP, m_srcPortButton);
	DDX_Control(pDX, IDC_BUTTON_DESTPORT_POPUP, m_destPortButton);
	DDX_Control(pDX, IDOK, m_okButton);
	DDX_Control(pDX, IDCANCEL, m_cancelButton);
}

BOOL CAddRuleDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_protocolCombo.SetCurSel(m_protocol);

	m_srcIpRestricted.SetAcceptedChars("0123456789.");
	m_destIpRestricted.SetAcceptedChars("0123456789.");

	m_srcPortRestricted.SetAcceptedChars("0123456789");
	m_srcPortRestricted.SetMaxIntAccepted(65536);
	m_destPortRestricted.SetAcceptedChars("0123456789");
	m_destPortRestricted.SetMaxIntAccepted(65536);

	m_srcIpButton.SetMenu(IDR_IPMENU, m_hWnd, TRUE);
	m_destIpButton.SetMenu(IDR_IPMENU, m_hWnd, TRUE);
	m_srcPortButton.SetMenu(IDR_PORTMENU, m_hWnd, TRUE);
	m_destPortButton.SetMenu(IDR_PORTMENU, m_hWnd, TRUE);

	return TRUE;
}


BEGIN_MESSAGE_MAP(CAddRuleDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CAddRuleDlg::OnBnClickedOk)
	ON_CBN_SELCHANGE(IDC_PROTO, &CAddRuleDlg::OnCbnSelchangeProto)
	ON_BN_CLICKED(IDC_BUTTON_SRCIP_POPUP, &CAddRuleDlg::OnBnClickedButtonSrcipPopup)
	ON_BN_CLICKED(IDC_BUTTON_DESTIP_POPUP, &CAddRuleDlg::OnBnClickedButtonDestipPopup)
	ON_BN_CLICKED(IDC_BUTTON_SRCPORT_POPUP, &CAddRuleDlg::OnBnClickedButtonSrcportPopup)
	ON_BN_CLICKED(IDC_BUTTON_DESTPORT_POPUP, &CAddRuleDlg::OnBnClickedButtonDestportPopup)
	ON_COMMAND(ID_POPUP_IP_MYMACHINE, &CAddRuleDlg::OnPopupIpMymachine)
	ON_COMMAND(ID_POPUP_IP_ANYMACHINE, &CAddRuleDlg::OnPopupIpAnymachine)
	ON_COMMAND(ID_POPUP_ANYPORT, &CAddRuleDlg::OnPopupAnyport)
	ON_COMMAND(ID_POPUP_HTTP, &CAddRuleDlg::OnPopupHttp)
	ON_COMMAND(ID_POPUP_FTP, &CAddRuleDlg::OnPopupFtp)
	ON_COMMAND(ID_POPUP_SSH, &CAddRuleDlg::OnPopupSsh)
	ON_COMMAND(ID_POPUP_TELNET, &CAddRuleDlg::OnPopupTelnet)
	ON_COMMAND(ID_POPUP_SMTP, &CAddRuleDlg::OnPopupSmtp)
	ON_COMMAND(ID_POPUP_DNS, &CAddRuleDlg::OnPopupDns)
	ON_COMMAND(ID_POPUP_DHCP, &CAddRuleDlg::OnPopupDhcp)
	ON_COMMAND(ID_POPUP_POP3, &CAddRuleDlg::OnPopupPop3)
	ON_COMMAND(ID_POPUP_NNTP, &CAddRuleDlg::OnPopupNntp)
	ON_COMMAND(ID_POPUP_NETBIOS, &CAddRuleDlg::OnPopupNetbios)
	ON_COMMAND(ID_POPUP_SOCKS, &CAddRuleDlg::OnPopupSocks)
END_MESSAGE_MAP()


// CAddRuleDlg message handlers

void CAddRuleDlg::OnBnClickedOk()
{

	UpdateData();

	m_protocol = m_protocolCombo.GetCurSel();

	if(!VerifyIpAddress(m_srcIp) || !VerifyIpAddress(m_destIp))
	{
		MessageBox("Please provide correct ip address numbers!");
		return;
	}
	if(m_protocol > 0 && (m_srcPort.GetLength() == 0 || m_destPort.GetLength() == 0))
	{
		MessageBox("Please provide port numbers!");
		return;
	}

	OnOK();
}

BOOL CAddRuleDlg::VerifyIpAddress(CString &str)
{
	int		pos = 0, prevpos = -1;	// Keeps track of current and previous

	// positins in the string
	CString str1;

	// if string doesn't contains any . it means it is invalid IP
	if(str.Find('.') == -1)
	{
		return(FALSE);
	}

	// if string contains . but not at the right position this will
	// return false for that
	int _pos = 0;
	_pos = str.Find('.');
	if((0 > _pos) || (_pos > 3))
	{
		return(FALSE);
	}

	int newpos = _pos;
	_pos = str.Find('.', _pos + 1);
	if((newpos + 1 >= _pos) || (_pos > newpos + 4))
	{
		return(FALSE);
	}

	newpos = _pos;
	_pos = str.Find('.', _pos + 1);
	if((newpos + 1 >= _pos) || (_pos > newpos + 4))
	{
		return(FALSE);
	}

	//if a dot is found verify that the ip address is within valid
	// range 0.0.0.0  & 255.255.255.255
	for(int cnt = 0; cnt <= 3; cnt++)
	{
		if(cnt < 3)
		{
			pos = str.Find('.', pos + 1);
		}
		else
		{
			pos = str.GetLength();
		}

		str1 = str.Left(pos);

		//char	ch[30];

		str1 = str1.Right(pos - (prevpos + 1));

		unsigned int	a = _ttoi(LPCTSTR(str1));
		if((0 > a) || (a > 255))
		{
			return(FALSE);
		}

		prevpos = pos;
	}

	return(TRUE);
}

void CAddRuleDlg::OnCbnSelchangeProto()
{
	// TODO: Add your control notification handler code here
	if(m_protocolCombo.GetCurSel() == 0)
	{
		//no ports for icmp
		m_srcPortRestricted.EnableWindow(FALSE);
		m_destPortRestricted.EnableWindow(FALSE);
	}
	else
	{
		m_srcPortRestricted.EnableWindow();
		m_destPortRestricted.EnableWindow();
	}
}

void CAddRuleDlg::OnBnClickedButtonSrcipPopup()
{
	mp_ipEdit = (CEdit *)GetDlgItem(IDC_SRCIP);
}

void CAddRuleDlg::OnBnClickedButtonDestipPopup()
{
	mp_ipEdit = (CEdit *)GetDlgItem(IDC_DESTIP);
}

void CAddRuleDlg::OnBnClickedButtonSrcportPopup()
{
	mp_portEdit = (CEdit *)GetDlgItem(IDC_SRCPORT);
}

void CAddRuleDlg::OnBnClickedButtonDestportPopup()
{
	mp_portEdit = (CEdit *)GetDlgItem(IDC_DESTPORT);
}

void CAddRuleDlg::OnPopupIpMymachine()
{
	mp_ipEdit->SetWindowText(CFirewall::GetMachineIp());
}

void CAddRuleDlg::OnPopupIpAnymachine()
{
	mp_ipEdit->SetWindowText(ANY_IP);
}

void CAddRuleDlg::OnPopupAnyport()
{
	mp_portEdit->SetWindowText(ANY_PORT);
}

void CAddRuleDlg::OnPopupHttp()
{
	mp_portEdit->SetWindowText(HTTP_PORT);
}

void CAddRuleDlg::OnPopupFtp()
{
	mp_portEdit->SetWindowText(FTP_PORT);
}

void CAddRuleDlg::OnPopupSsh()
{
	mp_portEdit->SetWindowText(SSH_PORT);
}

void CAddRuleDlg::OnPopupTelnet()
{
	mp_portEdit->SetWindowText(TELNET_PORT);
}

void CAddRuleDlg::OnPopupSmtp()
{
	mp_portEdit->SetWindowText(SMTP_PORT);
}

void CAddRuleDlg::OnPopupDns()
{
	mp_portEdit->SetWindowText(DNS_PORT);
}

void CAddRuleDlg::OnPopupDhcp()
{
	mp_portEdit->SetWindowText(DHCP_PORT);
}

void CAddRuleDlg::OnPopupPop3()
{
	mp_portEdit->SetWindowText(POP3_PORT);
}

void CAddRuleDlg::OnPopupNntp()
{
	mp_portEdit->SetWindowText(NNTP_PORT);
}

void CAddRuleDlg::OnPopupNetbios()
{
	mp_portEdit->SetWindowText(NETBIOS_PORT);
}

void CAddRuleDlg::OnPopupSocks()
{
	mp_portEdit->SetWindowText(SOCKS_PORT);
}
