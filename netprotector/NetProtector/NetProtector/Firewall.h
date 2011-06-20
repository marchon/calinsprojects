#pragma once

#include "NetProtectorHookDriver.h"
#include "Tdriver.h"

#define IP_FILTER_DRIVER_NAME	"IpFilterDriver"
#define IP_FILTER_DRIVER_PATH	"System32\\Drivers\\IpFltDrv.sys"
#define FILTER_HOOK_DRIVER_NAME	"NetProtectorHookDriver"
#define FILTER_HOOK_DRIVER_PATH	"System32\\Drivers\\NetProtectorHookDriver.sys"

#define	ANY_IP			_T("0.0.0.0")
#define	ANY_PORT		_T("0")
#define	FTP_PORT		_T("21")
#define SSH_PORT		_T("22")
#define	TELNET_PORT		_T("23")
#define SMTP_PORT		_T("25")
#define	DNS_PORT		_T("53")
#define	DHCP_PORT		_T("68")
#define	HTTP_PORT		_T("80")
#define	POP3_PORT		_T("110")
#define	NNTP_PORT		_T("119")
#define	NETBIOS_PORT	_T("137")
#define	SOCKS_PORT		_T("1080")


class CFirewall
{
private:
	/* must assure ip filter driver is loaded for filter hook driver to work */
	static TDriver m_ipFilterDriver;
	static TDriver m_filterHookDriver;
	/* must assure ip filter driver is loaded for filter hook driver to work */

	static bool m_isLoaded;
	static bool m_isStarted;

public:
	
	/* start and stop rutines */
	static bool LoadFirewallModule();
	static void UnloadFirewallModule();
	static bool StartFirewall();
	static void StopFirewall();
	/* start and stop rutines */

	/* add/remove rutines */
	static ULONG AddRule(IPFilter *filter);
	static bool RemoveRule(ULONG ruleNumber);
	static void RemoveAllRules();
	/* add/remove rutines */

	/* my ip */
	static CString GetMachineIp();
	/* my ip */
};
