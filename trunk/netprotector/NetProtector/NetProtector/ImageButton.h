#pragma once
#include "GradientStatic.h"

// CImageButton

class CImageButton : public CStatic
{
	DECLARE_DYNAMIC(CImageButton)
private:
	HCURSOR m_cursor;
	bool m_bMouseIn;
	char *m_szText;
	CGradientStatic *m_pstatusBar;
public:
	CImageButton();
	virtual ~CImageButton();

	virtual BOOL OnSetCursor(CWnd*, UINT, UINT);

	afx_msg void OnMouseMove(UINT nFlags, CPoint point);
	afx_msg void OnMouseLeave();

	void SetCrsr(HCURSOR);
	void SetStatusBarText(char*, CGradientStatic *);

protected:
	DECLARE_MESSAGE_MAP()
};


