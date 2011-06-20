// Setup CustomAction.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "msiquery.h"
#include "sporder.h"


#include "..\Globals\install.h"
#include "..\Globals\Global_vars.h"


//////////////////////////////////////////////////////////////////////////////
//					UNINSTALL THE LAYERED SERVICE PROVIDER					//
//////////////////////////////////////////////////////////////////////////////
VOID UninstallMyProvider()
{
    INT Errno;

    WSCDeinstallProvider(
        &LayeredProviderGuid,
        &Errno);
}
//////////////////////////////////////////////////////////////////////////////
//					UNINSTALL THE LAYERED SERVICE PROVIDER					//
//////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////
//					INSTALL THE LAYERED SERVICE PROVIDER					//
//////////////////////////////////////////////////////////////////////////////
INT InstallMyProvider(PDWORD CatalogId)
{
    WSAPROTOCOL_INFOW  proto_info;
    int               install_result;
    int               install_error;

	/* CALIN - fill the WSAPROTOCOL_INFOW struct */
    proto_info.dwServiceFlags1 = 0;
    proto_info.dwServiceFlags2 = 0;
    proto_info.dwServiceFlags3 = 0;
    proto_info.dwServiceFlags4 = 0;
    proto_info.dwProviderFlags = PFL_HIDDEN;
    proto_info.ProviderId      = LayeredProviderGuid;
    proto_info.dwCatalogEntryId = 0;   // filled in by system
    proto_info.ProtocolChain.ChainLen = LAYERED_PROTOCOL;
        // Do  not  need  to  fill  in  chain  for LAYERED_PROTOCOL or
        // BASE_PROTOCOL
    proto_info.iVersion = 0;
    proto_info.iAddressFamily = AF_INET;
    proto_info.iMaxSockAddr = 16;
    proto_info.iMinSockAddr = 16;
    proto_info.iSocketType = SOCK_STREAM;
    proto_info.iProtocol = IPPROTO_TCP;   // mimic TCP/IP
    proto_info.iProtocolMaxOffset = 0;
    proto_info.iNetworkByteOrder = BIGENDIAN;
    proto_info.iSecurityScheme = SECURITY_PROTOCOL_NONE;
    proto_info.dwMessageSize = 0;  // stream-oriented
    proto_info.dwProviderReserved = 0;
    wcscpy(
        proto_info.szProtocol,
        LAYERED_PROVIDER_NAME_W);
	/* CALIN - fill the WSAPROTOCOL_INFOW struct */
	

	/* CALIN - install the provider and retain the catalog id */
    install_result = WSCInstallProvider(
        &LayeredProviderGuid,
        DLL_PATH_W,                   // lpszProviderDllPath
        & proto_info,                 // lpProtocolInfoList
        1,                            // dwNumberOfEntries
        & install_error);             // lpErrno
    *CatalogId = proto_info.dwCatalogEntryId;
	/* CALIN - install the provider and retain the catalog id */

	/* CALIN - return the result */
    return(install_result);
	/* CALIN - return the result */
}
//////////////////////////////////////////////////////////////////////////////
//					INSTALL THE LAYERED SERVICE PROVIDER					//
//////////////////////////////////////////////////////////////////////////////




//////////////////////////////////////////////////////////////////////////////
//							INSTALL PROVIDER CHAIN							//
//		Arguments:															//
//			1. LPWSAPROTOCOL_INFOW BaseProtocolInfoBuff						//
//				- information about the base protocol						//
//			2. DWORD LayeredProviderCatalogId								//
//				- the id of the provider to put on the base provider		//
//			3. HKEY ConfigRegisteryKey										//
//				- a handle to a registry key where to save the new GUID		//
//				- used at removing the chain								//
//////////////////////////////////////////////////////////////////////////////
INT InstallNewChain(
    LPWSAPROTOCOL_INFOW BaseProtocolInfoBuff,
    DWORD               LayeredProviderCatalogId,
    HKEY                ConfigRegisteryKey)
{

	/* CALIN - some variables */
    WSAPROTOCOL_INFOW ProtocolChainProtoInfo;	//new chain protocol info
    WCHAR             DebugPrefix[] = L"LAYERED ";	//prefix added to the base provider to form new chain name
    INT               ReturnCode;	//
    INT               Errno;		//
    UUID              NewChainId;	// the new chain id(in fact is a guid)
    RPC_STATUS        Status;		//
    PUCHAR            GuidString;	// guid string to save in registry 
    HKEY              NewKey;		// will be a handle to the new registry key
    DWORD             KeyDisposition; //
    BOOL              Continue; //
	/* CALIN - some variables */

    ReturnCode = NO_ERROR;
	

    // We are only layering on top of base providers
    if (BaseProtocolInfoBuff->ProtocolChain.ChainLen == BASE_PROTOCOL) //must be base protocol(len = 1)
	{
        Continue = FALSE;

        // Get a new GUID for the protocol chain we are about to install
        Status = UuidCreate(&NewChainId); //create a unique identifier(using RPC api :D)

        if (RPC_S_OK == Status) //if guid created ok
		{

            //Get the string representaion of the GUID
            Status = UuidToString(&NewChainId, &GuidString); //convert it to string for storing

            if (RPC_S_OK == Status) //if converted ok
			{
                // Write down the GUID  in the registry so we know who to
                // uninstall
                RegCreateKeyEx(
                    ConfigRegisteryKey,                 // hkey
                    (LPCSTR)GuidString,                 // lpszSubKey
                    0,                                  // dwReserved
                    NULL,                               // lpszClass
                    REG_OPTION_NON_VOLATILE,            // fdwOptions
                    KEY_ALL_ACCESS,                     // samDesired
                    NULL,                               // lpSecurityAttributes
                    & NewKey,                           // phkResult
                    & KeyDisposition                    // lpdwDisposition
                    );
                RpcStringFree(&GuidString);

                Continue =TRUE;
            } //if
            else
			{
				MessageBox(NULL, "UuidToString() Failed!", "Error", MB_OK); //error
            } //else
        } //if
        else
		{
			MessageBox(NULL, "UuidCreate() Failed!", "Error", MB_OK); //error
        } //else

        if (Continue) //everything whent OK...
		{
            ProtocolChainProtoInfo = *BaseProtocolInfoBuff; //asign the base protocol info which will be slightly modifyed

			//the new chain will not suport IFS(Installable File System)
            ProtocolChainProtoInfo.dwServiceFlags1 &= ~XP1_IFS_HANDLES; //reset this flag

            ProtocolChainProtoInfo.ProviderId = NewChainId; //the new id

            wcscpy(ProtocolChainProtoInfo.szProtocol, DebugPrefix); //copy the prefix
            wcscat(ProtocolChainProtoInfo.szProtocol, BaseProtocolInfoBuff->szProtocol); //attach base provider name

            ProtocolChainProtoInfo.ProtocolChain.ChainLen = 2;	//the length of the chain is 2

            ProtocolChainProtoInfo.ProtocolChain.ChainEntries[0] = LayeredProviderCatalogId; //the id of the layered provider
            ProtocolChainProtoInfo.ProtocolChain.ChainEntries[1] = BaseProtocolInfoBuff->dwCatalogEntryId; //the id of the base provider

			// finally, install the new chain
            ReturnCode = WSCInstallProvider(
                &NewChainId,	//the guid
                DLL_PATH_W,		//path to my dll
                &ProtocolChainProtoInfo, //protocol information
                1,				//entries
                &Errno);		//errcode
      
			if (ReturnCode != 0)//error
			{
				char str[100];
                sprintf (str, "Installation over %ls failed with error %ld.",
                     BaseProtocolInfoBuff->szProtocol, Errno);

				MessageBox(NULL, str, "Error", MB_OK);
			}
        } //if
    } //if
    return(ReturnCode);
}
//////////////////////////////////////////////////////////////////////////////
//							INSTALL PROVIDER CHAIN							//
//////////////////////////////////////////////////////////////////////////////




//////////////////////////////////////////////////////////////////////////////
//					NETPROTECTOR SETUP CUSTOM ACTION HANDLER				//
//////////////////////////////////////////////////////////////////////////////
UINT __stdcall PerformCustomAction(MSIHANDLE hInstall)
{
	/* CALIN - some variables */
	LPWSAPROTOCOL_INFOW   ProtocolInfoBuff = NULL;
    DWORD                ProtocolInfoBuffSize = 0;
    INT                  ErrorCode;
    INT                  EnumResult;
    LONG                 lresult;
    HKEY                 NewKey;
    DWORD                KeyDisposition;
    GUID                 ProviderID;
    INT                  Index;
    DWORD                CatalogEntryId;
    CHAR                 GuidStringBuffer[40];
    DWORD                GuidStringBufferLen;
    FILETIME             FileTime;
    BOOL                 EntryIdFound;
    DWORD                *CatIdBuff;
    DWORD                nCatIds;
	/* CALIN - some variables */


	/* CALIN - install or uninstall the layered service provider */

    /* check the registry; if the key exists it means we must uninstall the lsp,
		else we install it */
    lresult = RegOpenKeyEx(
        HKEY_LOCAL_MACHINE,                     // hkey
        CONFIGURATION_KEY,                      // lpszSubKey
        0,                                      // dwReserved
        KEY_ALL_ACCESS,                        // samDesired
        & NewKey                               // phkResult
        );
	 /* check the registry; if the key exists it means we must uninstall the lsp,
		else we install it */

	/* CALIN - uninstall the provider and chains */
    if (ERROR_SUCCESS == lresult)
	{
        // The layered provider is installed so we are going uninstall.

        //
        // Enumerate all the provider IDs we stored on install and deinstall
        // the providers
        //
		// For every subkey in CONFIGURATION_KEY get the guid and uninstall the coresponding chain

        Index = 0;
        GuidStringBufferLen = sizeof(GuidStringBuffer);
        lresult = RegEnumKeyEx(
            NewKey,               //hKey
            Index,                // Index of subkey
            &GuidStringBuffer[0],    // Buffer to hold key name
            &GuidStringBufferLen,  // Length of buffer
            NULL,                 // Reserved
            NULL,                 // Class buffer
            NULL,                 // Class buffer length
            &FileTime              // Last write time
            );

        while (lresult != ERROR_NO_MORE_ITEMS)
		{
			//the key name is a string; make it a GUID
            UuidFromString((PUCHAR) GuidStringBuffer, &ProviderID);

            // Deinstall the provider chain we installed
            WSCDeinstallProvider(&ProviderID, &ErrorCode);

            // Delete our registry key
            RegDeleteKey(NewKey, &GuidStringBuffer[0]); //calin: waw, very intelligent(&GuidStringBuffer[0] == GuidStringBuffer)

			//go on to the next key
            GuidStringBufferLen = sizeof(GuidStringBuffer);
			lresult = RegEnumKeyEx(
				NewKey,               //hKey
				Index,                // Index of subkey
				&GuidStringBuffer[0],    // Buffer to hold key name
				&GuidStringBufferLen,  // Length of buffer
				NULL,                 // Reserved
				NULL,                 // Class buffer
				NULL,                 // Class buffer length
				&FileTime              // Last write time
				);
        } //while

        // Clen up the registry
        RegCloseKey(NewKey); //close the key
        RegDeleteKey(HKEY_LOCAL_MACHINE, CONFIGURATION_KEY); //delete it

        // Uninstall the real provider
        UninstallMyProvider(); //finally uninstall the provider from the catalog
    } //if
	/* CALIN - uninstall the provider and chains */

	/* CALIN - install the provider and create the new chains */
    else
	{
		//create the key to store the new chains guid
        RegCreateKeyEx(
            HKEY_LOCAL_MACHINE,                 // hkey
            CONFIGURATION_KEY,                  // lpszSubKey
            0,                                  // dwReserved
            NULL,                               // lpszClass
            REG_OPTION_NON_VOLATILE,            // fdwOptions
            KEY_ALL_ACCESS,                     // samDesired
            NULL,                               // lpSecurityAttributes
            & NewKey,                           // phkResult
            & KeyDisposition                    // lpdwDisposition
            );

        // Install a dummy PROTOCOL_INFO for the layered provider.
		// calin: dummy?...why dummy?...this installs the real provider
        lresult = InstallMyProvider(&CatalogEntryId);

        if (NO_ERROR == lresult) //if ok...install the chains
		{
            //
            // Enumerate the installed providers and chains
            //
            // Call WSCEnumProtocols with a zero length buffer so we know what
            // size to  send in to get all the installed PROTOCOL_INFO
            // structs.

            WSCEnumProtocols(
                NULL,                     // lpiProtocols
                ProtocolInfoBuff,         // lpProtocolBuffer - this is NULL
                & ProtocolInfoBuffSize,   // lpdwBufferLength - this is 0
                & ErrorCode);             // lpErrno

			//alloc memory for provider info
            ProtocolInfoBuff = (LPWSAPROTOCOL_INFOW)malloc(ProtocolInfoBuffSize);

            if (ProtocolInfoBuff) //if ok
			{

				//this will really fill the struct
                EnumResult = WSCEnumProtocols(
                    NULL,                     // lpiProtocols
                    ProtocolInfoBuff,         // lpProtocolBuffer
                    & ProtocolInfoBuffSize,   // lpdwBufferLength
                    & ErrorCode);

                if (SOCKET_ERROR != EnumResult) //if ok
				{
                    // Find our provider entry to get our catalog entry ID
                    EntryIdFound = FALSE;

					//calin: this is stupid because we already got CatalogEntryId 
					//from the call to InstallMyProvider()
                    for (Index = 0; Index < EnumResult; Index++){
                        if (memcmp(&ProtocolInfoBuff[Index].ProviderId, &LayeredProviderGuid,
                                sizeof (LayeredProviderGuid)) == 0)
						{
							//found our provider
                            CatalogEntryId = ProtocolInfoBuff[Index].dwCatalogEntryId; 
                            EntryIdFound = TRUE;
                        } //if
                    } //for


                    if (EntryIdFound) //if found our provider
					{
                    	//calin: i will install it over mswsock.dll base provider
						// over tcp and over udp(because of iexplore bug)
                        for (Index = 0; Index < EnumResult; Index++)
						{
							if(!wcscmp(ProtocolInfoBuff[Index].szProtocol, L"MSAFD Tcpip [TCP/IP]") ||
								!wcscmp(ProtocolInfoBuff[Index].szProtocol, L"MSAFD Tcpip [UDP/IP]"))
							{
									InstallNewChain(
										&ProtocolInfoBuff[Index],
										CatalogEntryId,
										NewKey);
							}
                        } //for

						//free memory
                        free (ProtocolInfoBuff);

                        //
                        // Enumerate the installed providers and chains
                        //

                        // Call WSCEnumProtocols with a zero length buffer so we know what
                        // size to  send in to get all the installed PROTOCOL_INFO
                        // structs.

						//again the same procedure
                        ProtocolInfoBuffSize = 0;
                        WSCEnumProtocols(
                            NULL,                     // lpiProtocols
                            NULL,                     // lpProtocolBuffer
                            & ProtocolInfoBuffSize,   // lpdwBufferLength
                            & ErrorCode);             // lpErrno

						//alloc memory for structures
                        ProtocolInfoBuff = (LPWSAPROTOCOL_INFOW)malloc(ProtocolInfoBuffSize);

                        if (ProtocolInfoBuff)//if ok
						{
							//fill the structs
                            EnumResult = WSCEnumProtocols(
                                NULL,                     // lpiProtocols
                                ProtocolInfoBuff,         // lpProtocolBuffer
                                & ProtocolInfoBuffSize,   // lpdwBufferLength
                                & ErrorCode);

                            if (SOCKET_ERROR != EnumResult)//if ok
							{
                                // Allocate buffer to hold catalog ID array
								//catalog id is the id assigned by the operating system
								//we will fill an array with those ids in the order we want them
                                CatIdBuff = (DWORD *)malloc(sizeof (DWORD)*EnumResult);

                                if (CatIdBuff!=NULL) 
								{
                                    // Put our catalog chains first
                                    nCatIds = 0;
                                    for (Index = 0; Index < EnumResult; Index++)
									{
                                        if ((ProtocolInfoBuff[Index].ProtocolChain.ChainLen > 1) //it is a chain
                                                && (ProtocolInfoBuff[Index].ProtocolChain.ChainEntries[0] == CatalogEntryId)) //and it is our chain
                                            CatIdBuff[nCatIds++] = ProtocolInfoBuff[Index].dwCatalogEntryId;
                                    }

                                    // Put the rest next
                                    for (Index = 0; Index < EnumResult; Index++)
									{
                                        if ((ProtocolInfoBuff[Index].ProtocolChain.ChainLen <= 1) //it is not a chain
                                                || (ProtocolInfoBuff[Index].ProtocolChain.ChainEntries[0] != CatalogEntryId)) //or it isn't our chain
                                            CatIdBuff[nCatIds++] = ProtocolInfoBuff[Index].dwCatalogEntryId;
                                    }

                                    // Save new protocol order
									//this api reorders the catalog entries
                                    ErrorCode = WSCWriteProviderOrder(CatIdBuff, nCatIds);

                                    if (ErrorCode != NO_ERROR) //error reoredering
										MessageBoxA(NULL, "Reodering failed!", "Erorr", MB_OK);

									//calin: ...that's all folks!!!
                                }
                            }
                        }
                    } //if
                } //if
            } //if
        } //if
    } //else
	/* CALIN - install the provider and create the new chains */

	/* CALIN - install or uninstall the layered service provider */


	/* CALIN - if installing register for startup else unregister */

	char buffer[MAX_PATH + 20]; //to be sure
	DWORD len = MAX_PATH + 20;
	HKEY runKey;

	if(MsiGetProperty(hInstall, "CustomActionData", buffer, &len) == ERROR_SUCCESS)
	{
		if(RegOpenKeyEx(HKEY_LOCAL_MACHINE, NP_BOOT_REGISTRY_PATH, 
			0, KEY_ALL_ACCESS, &runKey) == ERROR_SUCCESS)
		{
			if(!strcmp(buffer, "uninstall")) //uninstalling
			{
				RegDeleteValue(runKey, NP_BOOT_REGISTRY_VALUE);
			}
			else	//installing
			{
				if(ERROR_SUCCESS != RegSetValueEx(runKey, NP_BOOT_REGISTRY_VALUE, 0, 
					REG_SZ, (BYTE *)buffer, strlen(buffer) + 1))
				{
					MessageBox(NULL, "Can't add path to registry!", "Error", MB_OK);
				}

			}
			RegCloseKey(runKey);
		}
		else //error opening key
		{
			MessageBox(NULL, "Can't open registry key!", 
				"Error", MB_OK);
		}
	}
	else //error getting arguments
	{
		MessageBox(NULL, "Can't get arguments!", "Error", MB_OK);
	}
	/* CALIN - if installing register for startup else unregister */

	// for rebooting http://support.microsoft.com/kb/827020

	return 0;
}
//////////////////////////////////////////////////////////////////////////////
//					NETPROTECTOR SETUP CUSTOM ACTION HANDLER				//
//////////////////////////////////////////////////////////////////////////////