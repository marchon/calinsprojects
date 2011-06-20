// ImageButton.cpp : implementation file
//

#include "stdafx.h"
#include "NetProtector.h"
#include "ImageButton.h"

#ifdef _DEBUG
#undef THIS_FILE
static char THIS_FILE[]=__FILE__;
#define new DEBUG_NEW
#endif

// CImageButton

IMPLEMENT_DYNAMIC(CImageButton, CStatic)

CImageButton::CImageButton()
{
	m_cursor = NULL;
	m_bMouseIn = false;

	m_pstatusBar = NULL;
	m_szText = NULL;
}

CImageButton::~CImageButton()
{
	if(m_szText)
		delete[] m_szText;
}

BOOL CImageButton::OnSetCursor(
CWnd* pWnd, UINT nHitTest, UINT nMsg)
{
	if(!m_cursor)
		return FALSE;

	::SetCursor(m_cursor);
	return TRUE;
}

void CImageButton::SetCrsr(HCURSOR cur)
{
	m_cursor = cur;
	SendMessage(WM_SETCURSOR);
}

void CImageButton::SetStatusBarText(char* text, CGradientStatic *statusBar)
{
	m_szText = new char[strlen(text) + 1];
	strcpy(m_szText, text);

	m_pstatusBar = statusBar;
}

void CImageButton::OnMouseMove(UINT nFlags, CPoint point)
{
	if(m_bMouseIn || !m_pstatusBar)
		return;

	TRACKMOUSEEVENT EventTrack;
	EventTrack.cbSize = sizeof(TRACKMOUSEEVENT);
	EventTrack.dwFlags = TME_LEAVE;
	EventTrack.dwHoverTime = 0;
	EventTrack.hwndTrack = this->GetSafeHwnd();
	TrackMouseEvent(&EventTrack);

	m_bMouseIn = true;

	m_pstatusBar->SetWindowText(m_szText);
}
void CImageButton::OnMouseLeave()
{
	if(m_pstatusBar)
		m_pstatusBar->SetWindowText("Ready");

	TRACKMOUSEEVENT EventTrack;
	EventTrack.cbSize = sizeof(TRACKMOUSEEVENT);
	EventTrack.dwFlags = TME_CANCEL | TME_LEAVE;
	EventTrack.dwHoverTime = 0;
	EventTrack.hwndTrack = this->GetSafeHwnd();
	TrackMouseEvent(&EventTrack);

	m_bMouseIn = false;
}

BEGIN_MESSAGE_MAP(CImageButton, CStatic)
	ON_WM_SETCURSOR()
	ON_WM_MOUSEMOVE()
	ON_WM_MOUSELEAVE()
END_MESSAGE_MAP()



// CImageButton message handlers


