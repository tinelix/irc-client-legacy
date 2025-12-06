/*    PortTool v2.2     TextBoxDlg.h          */

//  TextBoxDlg.h : header file
//
//  Copyright © 2023, 2024 Dmitry Tretyakov (aka. Tinelix)
//  
//	This file is part of Tinelix IRC Client.
//
//  Tinelix IRC Client is free software: you can redistribute it and/or modify it under 
//  the terms of the GNU General Public License as published by the Free Software Foundation, 
//  either version 3 of the License, or (at your option) any later version.
//  Tinelix IRC Client is distributed in the hope that it will be useful, but WITHOUT ANY 
//  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
//  PARTICULAR PURPOSE.
//  See the GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License along with this
//  program. If not, see https://www.gnu.org/licenses/.
//
//  Source code: https://github.com/tinelix/irc-client-legacy/tree/main/windows/win32s

/////////////////////////////////////////////////////////////////////////////
// CTextBoxDlg dialog

class CTextBoxDlg : public CDialog
{
// Construction
public:
	CTextBoxDlg(CWnd* pParent = NULL);   // standard constructor
	void SetAction(int _action);

// Dialog Data
	//{{AFX_DATA(CTextBoxDlg)
	enum { IDD = IDD_TEXTDIALOG };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CTextBoxDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:
	void CreateProfile();
	// Generated message map functions
	//{{AFX_MSG(CTextBoxDlg)
	virtual void OnOK();
	virtual BOOL OnInitDialog();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};
