/* CALIN was here */

#include <windows.h>

/* Lsp name and paths */
#define LAYERED_PROVIDER_NAME_W	L"NET_PROTECTOR_LAYERED_SERVICE_PROVIDER"
#define DLL_PATH_W				L"NetProtectorLsp.dll"
#define LAYERED_PROVIDER_NAME	"NET_PROTECTOR_LAYERED_SERVICE_PROVIDER"
#define DLL_PATH				"NetProtectorLsp.dll"
#define DLL_REGISTER_FUNCTION	"RegisterProcessMonitor"
/* Lsp name and paths */

/* Registry paths */
#define FI_RULES_REGISTRY_PATH		"Software\\NetProtector\\FRules"
#define PM_RULES_REGISTRY_PATH		"Software\\NetProtector\\PMRules"
#define NP_OPTIONS_REGISTRY_PATH	"Software\\NetProtector\\Options"
#define NP_BOOT_REGISTRY_PATH		"Software\\Microsoft\\Windows\\CurrentVersion\\Run"
#define NP_BOOT_REGISTRY_VALUE		"NetProtector"
#define NP_OPTION_REGISTRY_FIRE		"Firewall"
#define NP_OPTION_REGISTRY_PM		"ProcessMonitor"

/* Registry paths */

/* Windows messages */
#define CONNECTION_QUERY			(WM_APP + 1)
#define PROCESS_ATTACHED			(WM_APP + 2)
#define PROCESS_DETACHED			(WM_APP + 3)
/* Windows messages */

/* Results for lsp process internet access request */
#define LRESULT_ALLOW				0
#define LRESULT_BLOCK				1
/* Results for lsp process internet access request */

/* CALIN was here */