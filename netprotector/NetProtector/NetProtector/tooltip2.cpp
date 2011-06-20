//File: ToolTip2.cpp
//Implementation file for class CToolTip2
//Copyright (C) 2000  Dhandapani Ammasai( dammasai@hotmail.com )
//Mail your comments, criticism, bugs to the above e-mail address.
#include "stdafx.h"
#include "tooltip2.h"

IMPLEMENT_DYNAMIC( CToolTip2, CWnd );

BEGIN_MESSAGE_MAP(CToolTip2, CWnd)
	//{{AFX_MSG_MAP(CToolTip2)
	ON_WM_PAINT()
	ON_WM_TIMER()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

//Default Constructor
CToolTip2::CToolTip2()
{
	m_pParentWnd = NULL;
	m_bShowStatus = FALSE;
	m_timer = -1;
}

//Destructor
CToolTip2::~CToolTip2()
{
	m_szTextArray.RemoveAll();
}

//Create Tool Tip
BOOL CToolTip2::Create(CWnd* pParentWnd)
{
	ASSERT(this != NULL );
	ASSERT(pParentWnd != NULL);

	m_pParentWnd = pParentWnd;
	//Create font
	m_font.CreateFont(15, 0, 0, 0, FW_REGULAR, 0, 0, 0, 0, 0, 0, 0, 0, "MS Sans Serif");
	CRect rectInitialSize(0,0,0,0);//Initial Window size. Will be dynamically changed later.
	return CreateEx(NULL, NULL, NULL,WS_POPUP |  WS_CHILD | WS_CLIPSIBLINGS,
		rectInitialSize,pParentWnd, NULL, NULL);
}

//Set tooltip text
void CToolTip2::SetText(const CStringArray& rsTextArray)
{
	ASSERT(this != NULL );

	m_szTextArray.RemoveAll();
	m_szTextArray.Append(rsTextArray);

	int size = m_szTextArray.GetSize();
	m_indMax = 0;
	for(int i = 0; i < size; i++)
	{
		m_szTextArray[i].TrimRight();
		
		if(m_szTextArray[m_indMax].GetLength() < m_szTextArray[i].GetLength())
			m_indMax = i;
	}

	DisplayToolTip(m_ptCurrent);
}

//Show tooltip
//rCurrentPoint specifies current mouse position and it is in client coordinates of parent window(Not in screen coordinates).
BOOL CToolTip2::Show(const CPoint& rCurrentPoint, int delay)
{
	ASSERT(this != NULL );
	ASSERT(m_hWnd != NULL );


	//if ( m_szText.IsEmpty())
	//	return FALSE;

	if(m_bShowStatus)
		Close();

	m_ptCurrent = rCurrentPoint;
	m_bShowStatus = TRUE;
	//Show tool tip
	DisplayToolTip(rCurrentPoint);

	m_timer = SetTimer(IDT_TIMER, delay, NULL);
	return TRUE;
}

//Close the tooltip
void CToolTip2::Close()
{
	ASSERT(this != NULL );
	ASSERT(m_hWnd != NULL );

	ShowWindow(SW_HIDE); //Hide tooltip
	m_bShowStatus = FALSE;

	if(m_timer != -1)
	{
		KillTimer(m_timer);
		m_timer = -1;
	}
}

void CToolTip2::OnPaint()
{
	CPaintDC dc(this);

	DisplayToolTip(m_ptCurrent);
}

void CToolTip2::OnTimer(UINT TimerVal)
{
	Close();
}

//Display tooltip
void CToolTip2::DisplayToolTip(const CPoint& rCurrentPoint)
{
	CDC* pDC = GetDC();

	CBrush		*pOldBrush;

	CFont *pOldFont;
	pOldFont = pDC->SelectObject(&m_font);

	CSize size = pDC->GetTextExtent(m_szTextArray[m_indMax]);
	int rowHeight = size.cy;
	size.cy *= m_szTextArray.GetSize();

	pDC->LPtoDP(&size);
	//Form tooltip rectangle
	CRect rectToolTip(rCurrentPoint.x, rCurrentPoint.y, 
		rCurrentPoint.x + size.cx + 12, rCurrentPoint.y + size.cy + 12);

	//Draw Tooltip Rect and Text
	//
	pDC->SetBkMode(TRANSPARENT);
	CBrush brushToolTip(GetSysColor(COLOR_INFOBK));
	pOldBrush = pDC->SelectObject(&brushToolTip);

	//Create and select thick black pen
	CPen penBlack(PS_SOLID, 0, COLORREF(RGB(0, 0, 0)));
	CPen* pOldPen = pDC->SelectObject(&penBlack);

	//Draw rectangle filled with COLOR_INFOBK
	pDC->Rectangle(0,0,rectToolTip.Width(),rectToolTip.Height());

	//Draw tooltip text
   	
	pDC->SetTextAlign(TA_LEFT);

	pDC->SetTextColor( GetSysColor(COLOR_HIGHLIGHT) );//Tool Tip color set in control panel settings
	pDC->TextOut(size.cx / 2 - pDC->GetTextExtent(m_szTextArray[0]).cx / 2 + 2, 1, m_szTextArray[0]);

	pDC->SetTextColor( GetSysColor(COLOR_INFOTEXT) );//Tool Tip color set in control panel settings
	for(int i = 1; i < m_szTextArray.GetSize(); i++)
		pDC->TextOut(3, 8 + i * rowHeight, m_szTextArray[i]);

	CRect rectWnd = rectToolTip;
	m_pParentWnd->ClientToScreen(rectWnd); //Convert from client to screen coordinates
	CPoint ptToolTipLeft = rectWnd.TopLeft();

	//Now display tooltip
	SetWindowPos(&wndTop,ptToolTipLeft.x+1, ptToolTipLeft.y+1, rectWnd.Width(), 
		rectWnd.Height(),SWP_SHOWWINDOW|SWP_NOOWNERZORDER|SWP_NOACTIVATE);

	//put back old objects
	pDC->SelectObject(pOldBrush);
	pDC->SelectObject(pOldPen);
	pDC->SelectObject(pOldFont);

	ReleaseDC(pDC);
}


