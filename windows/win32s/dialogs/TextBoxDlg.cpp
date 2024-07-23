//  TextBoxDlg.cpp : implementation file
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

#include "stdafx.h"
#include "..\Tinelix IRC.h"
#include "TextBoxDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CTextBoxDlg dialog


CTextBoxDlg::CTextBoxDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CTextBoxDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CTextBoxDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}


void CTextBoxDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CTextBoxDlg)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CTextBoxDlg, CDialog)
	//{{AFX_MSG_MAP(CTextBoxDlg)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

int action;

/////////////////////////////////////////////////////////////////////////////
// CTextBoxDlg message handlers

BOOL CTextBoxDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	if(action == 0) {	// if creating profile
		CWnd* title = (CWnd*)GetDlgItem(IDC_EDITTEXT);
		title->SetWindowText("Profile name:");
	}
	
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

void CTextBoxDlg::SetAction(int _action) {
	action = _action;
}

void CTextBoxDlg::CreateProfile() {
	CIRCApplication* app = (CIRCApplication*)AfxGetApp();
	CEdit* text_area = (CEdit*)GetDlgItem(IDC_EDITTEXTAREA);
	CString profile_name = "";
	text_area->GetWindowText(profile_name);
	char app_path[256];
	sprintf(app_path, app->GetAppPath());
	CString profile_path = "";
	profile_path.Format("%s\\profiles\\%s.ini", app_path, profile_name);
	WritePrivateProfileString("Main", "ProfileName", profile_name, profile_path);
	WritePrivateProfileString("Main", "MainNick", "irc_member", profile_path);
	WritePrivateProfileString("Main", "Server", "irc.tinelix.ru", profile_path);
	WritePrivateProfileString("Main", "Port", "6667", profile_path);
	if((UINT)ShellExecute(
		NULL, "open", "notepad.exe", profile_path, NULL, SW_SHOWNORMAL) <= 32) {
		MessageBox("Notepad not found. "
			"Use another editor to modify this configuration file.", 
			"Error", MB_OK | MB_ICONSTOP);
	}
}

void CTextBoxDlg::OnOK() 
{
	if(action == 0) {
		CreateProfile();
	}
	
	CDialog::OnOK();
}


