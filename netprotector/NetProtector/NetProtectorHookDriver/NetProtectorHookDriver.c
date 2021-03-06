/*

  NetProtectorHookDriver.C

  Author: your name
  Last Updated: 2001-01-01/0101

  This framework is generated by QuickSYS.

*/

#include <string.h>
#include <stdio.h>
#include <ntddk.h>
#include <ntddndis.h>
#include <pfhook.h>
#include "NetProtectorHookDriver.h"

#if DBG
#define dprintf DbgPrint
#else
#define dprintf(x)
#endif

NTSTATUS DrvDispatch(IN PDEVICE_OBJECT DeviceObject, IN PIRP Irp);
VOID DrvUnload(IN PDRIVER_OBJECT DriverObject);

NTSTATUS SetFilterFunction(PacketFilterExtensionPtr filterFunction);

NTSTATUS AddFilterToList(IPFilter *pf, 
						 /* calin */
						 ULONG *id
						 /* calin */);
void ClearFilterList(void);
/* calin */
NTSTATUS RemoveFilter(ULONG id);
/* calin */
PF_FORWARD_ACTION cbFilterFunction(IN unsigned char *PacketHeader,IN unsigned char *Packet, IN unsigned int PacketLength, IN unsigned int RecvInterfaceIndex, IN unsigned int SendInterfaceIndex, IN unsigned long RecvLinkNextHop, IN unsigned long SendLinkNextHop);

#define NT_DEVICE_NAME L"\\Device\\NetProtectorHookDriver"
#define DOS_DEVICE_NAME L"\\DosDevices\\NetProtectorHookDriver"


struct filterList *first = NULL;
struct filterList *last = NULL;

/*++

Routine Description:

    Installable driver initialization entry point.
    This entry point is called directly by the I/O system.

Arguments:

    DriverObject - pointer to the driver object

    RegistryPath - pointer to a unicode string representing the path
                   to driver-specific key in the registry

Return Value:

    STATUS_SUCCESS if successful,
    STATUS_UNSUCCESSFUL otherwise

--*/
NTSTATUS DriverEntry(IN PDRIVER_OBJECT DriverObject, IN PUNICODE_STRING RegistryPath)
{

    PDEVICE_OBJECT         deviceObject = NULL;
    NTSTATUS               ntStatus;
    UNICODE_STRING         deviceNameUnicodeString;
    UNICODE_STRING         deviceLinkUnicodeString;
	
	dprintf("NetProtectorHookDriver.SYS: entering DriverEntry\n");


	//we have to create the device
	RtlInitUnicodeString(&deviceNameUnicodeString, NT_DEVICE_NAME);

	ntStatus = IoCreateDevice(DriverObject, 
								0,
								&deviceNameUnicodeString, 
								FILE_DEVICE_DRVFLTIP,
								0,
								FALSE,
								&deviceObject);


    if ( NT_SUCCESS(ntStatus) )
    {
    
        // Create a symbolic link that Win32 apps can specify to gain access
        // to this driver/device
        RtlInitUnicodeString(&deviceLinkUnicodeString, DOS_DEVICE_NAME);

        ntStatus = IoCreateSymbolicLink(&deviceLinkUnicodeString, &deviceNameUnicodeString);

        if ( !NT_SUCCESS(ntStatus) )
        {
            dprintf("NetProtectorHookDriver.SYS: IoCreateSymbolicLink failed\n");
        }

        //
        // Create dispatch points for device control, create, close.
        //

        DriverObject->MajorFunction[IRP_MJ_CREATE]         =
        DriverObject->MajorFunction[IRP_MJ_CLOSE]          =
        DriverObject->MajorFunction[IRP_MJ_DEVICE_CONTROL] = DrvDispatch;
        DriverObject->DriverUnload                         = DrvUnload;
    }

    if ( !NT_SUCCESS(ntStatus) )
    {
		dprintf("NetProtectorHookDriver.SYS: Error in initialization. Unloading...");

		DrvUnload(DriverObject);
    }

    return ntStatus;
}


/*++

Routine Description:

    Process the IRPs sent to this device. IRP = I/O request packet

Arguments:

    DeviceObject - pointer to a device object

    Irp          - pointer to an I/O Request Packet

Return Value:

--*/
NTSTATUS DrvDispatch(IN PDEVICE_OBJECT DeviceObject, IN PIRP Irp)
{

    PIO_STACK_LOCATION  irpStack;
    PVOID               ioBuffer;
    ULONG               inputBufferLength;
    ULONG               outputBufferLength;
    ULONG               ioControlCode;
    NTSTATUS            ntStatus;

    Irp->IoStatus.Status      = STATUS_SUCCESS;
    Irp->IoStatus.Information = 0;


 
    // Get a pointer to the current location in the Irp. This is where
    //     the function codes and parameters are located.
    irpStack = IoGetCurrentIrpStackLocation(Irp);


    // Get the pointer to the input/output buffer and it's length
    ioBuffer           = Irp->AssociatedIrp.SystemBuffer;
    inputBufferLength  = irpStack->Parameters.DeviceIoControl.InputBufferLength;
    outputBufferLength = irpStack->Parameters.DeviceIoControl.OutputBufferLength;

    switch (irpStack->MajorFunction)
    {
    case IRP_MJ_CREATE:

        dprintf("NetProtectorHookDriver.SYS: IRP_MJ_CREATE\n");

        break;

    case IRP_MJ_CLOSE:

        dprintf("NetProtectorHookDriver.SYS: IRP_MJ_CLOSE\n");

        break;

    case IRP_MJ_DEVICE_CONTROL:

        dprintf("NetProtectorHookDriver.SYS: IRP_MJ_DEVICE_CONTROL\n");

        ioControlCode = irpStack->Parameters.DeviceIoControl.IoControlCode;

        switch (ioControlCode)
        {
			// ioctl code to start filtering
			case START_IP_HOOK:
			{
           		SetFilterFunction(cbFilterFunction);

				break;
			}

			// ioctl to stop filtering
			case STOP_IP_HOOK:
			{
				SetFilterFunction(NULL);
	            
				break;
			}

            // ioctl to add a filter rule
			case ADD_FILTER:
			{
				/* calin */
				if(inputBufferLength == sizeof(IPFilter))
				{
					IPFilter *nf;
					ULONG id;

					if(outputBufferLength >= sizeof(ULONG))
					{
						nf = (IPFilter *)ioBuffer;

						AddFilterToList(nf, &id);

						RtlCopyMemory(ioBuffer, (PVOID)&id, sizeof(ULONG));

						Irp->IoStatus.Status = STATUS_SUCCESS;
						Irp->IoStatus.Information = sizeof(ULONG);
#if DBG
						dprintf("NetProtectorHookDriver.SYS: Filter with id %p added.", id);
#endif
					}
				}
				/* calin */

				break;
			}

			// ioctl to free filter rule list
			case CLEAR_FILTER:
			{
				/* calin */
				if(inputBufferLength == sizeof(ULONG))
				{
					ULONG *id = (ULONG *)ioBuffer;

					if(*id == 0) //can't be 0....it's a pointer....so to remove all filters pass NULL
					{
						dprintf("NetProtectorHookDriver.SYS: Removing all filters.");
						ClearFilterList();
						Irp->IoStatus.Status = STATUS_SUCCESS;
					}
					else
					{
#if DBG
						dprintf("NetProtectorHookDriver.SYS: Removeing filter with id %p.", *id);
#endif
						Irp->IoStatus.Status = RemoveFilter(*id);		
					}
				}
				/* calin */

				break;
			}

			default:
				Irp->IoStatus.Status = STATUS_INVALID_PARAMETER;

				dprintf("NetProtectorHookDriver.SYS: unknown IRP_MJ_DEVICE_CONTROL\n");

				break;
        }

        break;
    }


    //
    // DON'T get cute and try to use the status field of
    // the irp in the return status.  That IRP IS GONE as
    // soon as you call IoCompleteRequest.
    //

    ntStatus = Irp->IoStatus.Status;
	
    IoCompleteRequest(Irp, IO_NO_INCREMENT);


    //
    // We never have pending operation so always return the status code.
    //

    return ntStatus;
}

/*++

Routine Description:

    Free all the allocated resources, etc.

Arguments:

    DriverObject - pointer to a driver object

Return Value:


--*/
VOID DrvUnload(IN PDRIVER_OBJECT DriverObject)
{
    UNICODE_STRING         deviceLinkUnicodeString;

	dprintf("NetProtectorHookDriver.SYS: Unloading\n");

    SetFilterFunction(NULL);

	// Free any resources
	ClearFilterList();
   
    // Delete the symbolic link
    RtlInitUnicodeString(&deviceLinkUnicodeString, DOS_DEVICE_NAME);
    IoDeleteSymbolicLink(&deviceLinkUnicodeString);

    
	// Delete the device object
    IoDeleteDevice(DriverObject->DeviceObject);
}



/*++

Routine Description:

    Get a reference to IpFilterDriver so we will be able to install the filter

Arguments:

    pDeviceObject - pointer to a pointer of device object

    pFileObject   - pointer to a pointer of file object

Return Value:

    STATUS_SUCCESS if successful,
    STATUS_UNSUCCESSFUL otherwise

--*/
NTSTATUS SetFilterFunction(PacketFilterExtensionPtr filterFunction)
{
	NTSTATUS status = STATUS_SUCCESS, waitStatus=STATUS_SUCCESS;
	UNICODE_STRING filterName;
	PDEVICE_OBJECT ipDeviceObject=NULL;
	PFILE_OBJECT ipFileObject=NULL;

	PF_SET_EXTENSION_HOOK_INFO filterData;

	KEVENT event;
	IO_STATUS_BLOCK ioStatus;
	PIRP irp;

	dprintf("NetProtectorHookDriver.SYS: Getting pointer to IpFilterDriver\n");
	
	//first of all, we have to get a pointer to IpFilterDriver Device
	RtlInitUnicodeString(&filterName, DD_IPFLTRDRVR_DEVICE_NAME);
	status = IoGetDeviceObjectPointer(&filterName,STANDARD_RIGHTS_ALL, &ipFileObject, &ipDeviceObject);
	
	if(NT_SUCCESS(status))
	{
		//initialize the struct with functions parameters
		filterData.ExtensionPointer = filterFunction;

		//we need initialize the event used later by the IpFilterDriver to signal us
		//when it finished its work
		KeInitializeEvent(&event, NotificationEvent, FALSE);

		//we build the irp needed to establish fitler function
		irp = IoBuildDeviceIoControlRequest(IOCTL_PF_SET_EXTENSION_POINTER,
			  							    ipDeviceObject,
											(PVOID) &filterData,
											sizeof(PF_SET_EXTENSION_HOOK_INFO),
											NULL,
											0,
											FALSE,
											&event,
											&ioStatus);


		if(irp != NULL)
		{
			// we send the IRP
			status = IoCallDriver(ipDeviceObject, irp);

			//and finally, we wait for "acknowledge" of IpDriverFilter
			if (status == STATUS_PENDING) 
			{
				waitStatus = KeWaitForSingleObject(&event, Executive, KernelMode, FALSE, NULL);

				if (waitStatus 	!= STATUS_SUCCESS ) 
					dprintf("NetProtectorHookDriver.SYS: Error waiting for IpFilterDriver response.");
			}

			status = ioStatus.Status;

			if(!NT_SUCCESS(status))
				dprintf("NetProtectorHookDriver.SYS: Error, IO error with ipFilterDriver\n");
		}
		
		else
		{
			//if we cant allocate the space, we return the corresponding code error
			status = STATUS_INSUFFICIENT_RESOURCES;

			dprintf("NetProtectorHookDriver.SYS: Error building IpFilterDriver IRP\n");
		}

		if(ipFileObject != NULL)
			ObDereferenceObject(ipFileObject);
		
		ipFileObject = NULL;
		ipDeviceObject = NULL;
	}
	
	else
		dprintf("NetProtectorHookDriver.SYS: Error while getting the pointer\n");
	
	return status;
}




/*++

Routine Description:

    Add a rule to the filter list

Arguments:

      pf - pointer to filter rule


Return Value:

    STATUS_SUCCESS if successful,
    STATUS_INSUFFICIENT_RESOURCES otherwise
 
--*/
NTSTATUS AddFilterToList(IPFilter *pf, 
						 /* calin */
						 ULONG *id)
						 /* calin */
{
	struct filterList *aux=NULL;

	// first, we reserve memory (non paged) to the new filter
	aux = (struct filterList *)ExAllocatePool(NonPagedPool, sizeof(struct filterList));
	
	if(aux == NULL)
	{
		dprintf("NetProtectorHookDriver.SYS: Problem reserving memory\n");
	
		return STATUS_INSUFFICIENT_RESOURCES;
	}

	//fill the new structure
	aux->ipf.destinationIp = pf->destinationIp;
	aux->ipf.sourceIp = pf->sourceIp;

	aux->ipf.destinationMask = pf->destinationMask;
	aux->ipf.sourceMask = pf->sourceMask;

	aux->ipf.destinationPort = pf->destinationPort;
	aux->ipf.sourcePort = pf->sourcePort;

	aux->ipf.protocol = pf->protocol;

	aux->ipf.drop=pf->drop;

	/* calin - the id will be the memory address where the filter is copyed - it's unique!!!!*/
	*id = (ULONG)aux;
	/* calin */

	//Add the new filter to the filter list
	if(first == NULL)
	{
		first = last = aux;
		
		first->next = NULL;
	}
	
	else
	{
		last->next = aux;
		last = aux;
		last->next = NULL;
	}

#if DBG
	dprintf("NetProtectorHookDriver.SYS: Rule Added\n\t%x %x\n\t%x %x\n\t%x\n\t%x", aux->ipf.sourceIp
													  , aux->ipf.sourceMask
												      , aux->ipf.destinationIp
												      , aux->ipf.destinationMask
													  , aux->ipf.sourcePort
													  , aux->ipf.destinationPort);
#endif

	return STATUS_SUCCESS;
}




/*++

Routine Description:

    Remove the linked list where the rules were saved.

Arguments:


Return Value:

 
--*/
void ClearFilterList(void)
{
	struct filterList *aux = NULL;

	//free the linked list
	dprintf("NetProtectorHookDriver.SYS: Removing the filter List...");
	
	while(first != NULL)
	{
		aux = first;
		first = first->next;
		ExFreePool(aux);
	}

	first = last = NULL;
}

/* calin did some fine things here */
NTSTATUS RemoveFilter(ULONG id)
{
	struct filterList *ptrToRemove = (struct filterList *)id;

	struct filterList *aux = first;

#if DBG
	dprintf("NetProtectorHookDriver.SYS: Current filter id is %p", aux);
#endif
	if(aux == ptrToRemove) //we remove the first item in the linked list
	{
		first = aux->next;

		ExFreePool(aux);
#if DBG
		dprintf("NetProtectorHookDriver.SYS: Remove filter with id %p successfull. Was first!", id);
#endif

		return STATUS_SUCCESS;
	}
	
	while(aux->next != NULL)
	{
#if DBG
		dprintf("NetProtectorHookDriver.SYS: Current filter id is %p", aux->next);
#endif
		if(aux->next == ptrToRemove) //aux->next is what we are looking for
		{
			///struct filterList *tmp = aux->next;

			aux->next = ptrToRemove->next;

			ExFreePool(ptrToRemove);
#if DBG
			dprintf("NetProtectorHookDriver.SYS: Remove filter with id %p successfull.", id);
#endif

			return STATUS_SUCCESS;
		}

		aux = aux->next;
	}
#if DBG
	dprintf("NetProtectorHookDriver.SYS: Remove filter with id %p not successfull. Did not find such filter!", id);
#endif

	return STATUS_UNSUCCESSFUL;
}
/* calin did some fine things here */


/*++

Routine Description:

    Filter each packet is received or sended

	To see parameters and return you can read it in MSDN
--*/

PF_FORWARD_ACTION cbFilterFunction(IN unsigned char *PacketHeader,IN unsigned char *Packet, IN unsigned int PacketLength, IN unsigned int RecvInterfaceIndex, IN unsigned int SendInterfaceIndex, IN unsigned long RecvLinkNextHop, IN unsigned long SendLinkNextHop)
{
	IPPacket *ipp;
	TCPHeader *tcph;
	UDPHeader *udph;

	

	struct filterList *aux = first;

	//we "extract" the ip Header 
	ipp=(IPPacket *)PacketHeader;
	
	//TCP -> protocol = 6
	//we accept all packets of established connections
	
	if(ipp->ipProtocol == 6)
	{
		tcph = (TCPHeader *)Packet; 
		
		//if we havent got the bit SYN activate, we pass the packets
		/* CALIN: I REALLY DON'T SEE WHY WE SHOULD ACCEPT ALL ESTABLISHED CONNECTIONS; FUK'EM ALL:D

		if(!(tcph->flags & 0x02)) 
			return PF_FORWARD;

		*/
	}

	//otherwise, we compare the packet with our rules
	while(aux != NULL)
	{
		//dprintf("Comparing with Rule %d", countRule);

		//if protocol is the same....
		if(aux->ipf.protocol == 0 || ipp->ipProtocol == aux->ipf.protocol)
		{
			//we look in source Address
			if(aux->ipf.sourceIp != 0 && (ipp->ipSource & aux->ipf.sourceMask) != aux->ipf.sourceIp)
			{
				aux=aux->next;
			
				continue;
			}
									
			// we look in destination address
			if(aux->ipf.destinationIp != 0 && (ipp->ipDestination & aux->ipf.destinationMask) != aux->ipf.destinationIp)
			{
				aux=aux->next;

				continue;
			}
			
			//if we have a tcp packet, we look in ports
			//tcp, protocol = 6
			if(ipp->ipProtocol == 6) 
			{
				if(aux->ipf.sourcePort == 0 || tcph->sourcePort == aux->ipf.sourcePort)
				{ 
					if(aux->ipf.destinationPort == 0 || tcph->destinationPort == aux->ipf.destinationPort) //puerto tcp destino
					{
						//now we decided what to do with the packet
						if(aux->ipf.drop)
								 return  PF_DROP;
							else
								return PF_FORWARD;
					}
				}
			}
				
			//udp, protocol = 17
			else if(ipp->ipProtocol == 17) 
			{
				udph=(UDPHeader *)Packet; 

				if(aux->ipf.sourcePort == 0 || udph->sourcePort == aux->ipf.sourcePort) 
				{ 
					if(aux->ipf.destinationPort == 0 || udph->destinationPort == aux->ipf.destinationPort) 
					{
						//now we decided what to do with the packet
						if(aux->ipf.drop)
							return  PF_DROP;
						
						else
							return PF_FORWARD;
					}
				}
			}	
			
			else
			{
				//for other packet we dont look more and ....
				//now we decided what to do with the packet
				if(aux->ipf.drop)
					return  PF_DROP;
				else
					return PF_FORWARD;
			}	
		}
		
		//compare with the next rule
		aux=aux->next;
	}

	//we accept all not registered
	return PF_FORWARD;
}



