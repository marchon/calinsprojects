// RestrictedEdit.cpp : implementation file
//

#include "stdafx.h"
#include "NetProtector.h"
#include "RestrictedEdit.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif


// CRestrictedEdit

IMPLEMENT_DYNAMIC(CRestrictedEdit, CEdit)

CRestrictedEdit::CRestrictedEdit()
{
	acceptedChars = NULL;
	maxIntAccepted = -1;
}

CRestrictedEdit::~CRestrictedEdit()
{
	if(acceptedChars)
		delete[] acceptedChars;
}


BEGIN_MESSAGE_MAP(CRestrictedEdit, CEdit)
	ON_WM_CHAR()
END_MESSAGE_MAP()



// CRestrictedEdit message handlers

void CRestrictedEdit::OnChar( UINT nChar, UINT nRepCnt, UINT nFlags )
{
	bool accept = false;

	if(nChar == '\b')
		accept = true;

	if(acceptedChars)
	{
		int i = 0;
		while(acceptedChars[i] != '\0')
		{
			if(nChar == acceptedChars[i])
			{
				accept = true;
				break;
			}

			i++;
		}
	}

	if(accept)
		CEdit::OnChar(nChar, nRepCnt, nFlags);

	if(accept && maxIntAccepted != -1)
	{
		char str[20];
		GetWindowText(str, 20);

		int nb = atoi(str);

		if(nb > maxIntAccepted)
		{
			sprintf(str, "%d", maxIntAccepted);
			SetWindowText(str);
		}
	}
}


void CRestrictedEdit::SetAcceptedChars(char *acc)
{
	int len = strlen(acc) + 1;
	acceptedChars = new char[len];

	memcpy(acceptedChars, acc, len);
}

void CRestrictedEdit::SetMaxIntAccepted(int acc)
{
	if(acc >= 0)
		maxIntAccepted = acc;
}
