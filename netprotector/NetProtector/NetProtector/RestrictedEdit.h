#pragma once


// CRestrictedEdit

class CRestrictedEdit : public CEdit
{
	DECLARE_DYNAMIC(CRestrictedEdit)
private:
	char *acceptedChars;
	int maxIntAccepted;
public:
	CRestrictedEdit();
	virtual ~CRestrictedEdit();

	afx_msg void OnChar( UINT nChar, UINT nRepCnt, UINT nFlags );

	void SetAcceptedChars(char *); 
	void SetMaxIntAccepted(int);

protected:
	DECLARE_MESSAGE_MAP()
};


