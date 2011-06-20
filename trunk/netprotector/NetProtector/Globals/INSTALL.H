/*++


     Copyright c 1996 Intel Corporation
     All Rights Reserved

     Permission is granted to use, copy and distribute this software and
     its documentation for any purpose and without fee, provided, that
     the above copyright notice and this statement appear in all copies.
     Intel makes no representations about the suitability of this
     software for any purpose.  This software is provided "AS IS."

     Intel specifically disclaims all warranties, express or implied,
     and all liability, including consequential and other indirect
     damages, for the use of this software, including liability for
     infringement of any proprietary rights, and including the
     warranties of merchantability and fitness for a particular purpose.
     Intel does not assume any responsibility for any errors which may
     appear in this software nor any responsibility to update it.


Module Name:

    install.h

Abstract:

    This module contains the defininitions common the the winsock2 layered
    service provider sample and its installation application

--*/

#ifndef _INSTALL_H_
#define _INSTALL_H_

#include <windows.h>

/* CALIN was here */
#define CONFIGURATION_KEY TEXT("SOFTWARE\\NetProtector\\LSP")

// {117EE2BD-63C2-4750-B008-C876689AAE80}
static GUID LayeredProviderGuid = { 
	0x117ee2bd, 0x63c2, 0x4750, 
	{ 0xb0, 0x8, 0xc8, 0x76, 0x68, 0x9a, 0xae, 0x80 } 
};

/* CALIN was here */

#endif // _INSTALL_H_
