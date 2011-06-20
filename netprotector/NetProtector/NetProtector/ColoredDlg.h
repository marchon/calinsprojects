//////////////////////////////////////////////////////////////////////////
//
// CColoredDlg - a simple dialog wrapper with OnCtlColor support
//
// Coded by dzolee
// http://dzolee.blogspot.com
//
//////////////////////////////////////////////////////////////////////////

#pragma once

// CColoredDlg dialog

#ifndef RGB_AVERAGE
	#define RGB_AVERAGE(a, b)   ( ((((a) ^ (b)) & 0xfffefefeL) >> 1) + ((a) & (b)) )
#endif

class CColoredDlg : public CDialog
{
	DECLARE_DYNAMIC(CColoredDlg)

public:
	CColoredDlg(CWnd* pParent = NULL);   // standard constructor
	CColoredDlg(UINT nDialogID, CWnd* pParent = NULL);
	virtual ~CColoredDlg();

	void SetBkgColor(COLORREF crBkg);
	void SetFrgColor(COLORREF crFrg);

protected:
	DECLARE_MESSAGE_MAP()
	CBrush m_brBkg;

	COLORREF m_crBkg;
	COLORREF m_crFrg;
	bool m_bmodifyForground;
	afx_msg HBRUSH OnCtlColor(CDC* pDC, CWnd* pWnd, UINT nCtlColor);
};
