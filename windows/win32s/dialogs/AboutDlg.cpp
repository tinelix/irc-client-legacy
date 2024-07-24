//  AboutDlg.cpp : implementation file
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
#include "AboutDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

CString src_code_repo;

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg dialog


CAboutDlg::CAboutDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CAboutDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CAboutDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}


void CAboutDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CAboutDlg)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(CAboutDlg, CDialog)
	//{{AFX_MSG_MAP(CAboutDlg)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CAboutDlg message handlers

BOOL CAboutDlg::Create(LPCTSTR lpszClassName, LPCTSTR lpszWindowName, 
					   DWORD dwStyle, const RECT& rect, CWnd* pParentWnd, 
					   UINT nID, CCreateContext* pContext) 
{
	
	return CDialog::Create(IDD, pParentWnd);
}

BOOL CAboutDlg::OnInitDialog() 
{
	CDialog::OnInitDialog();
	
	src_code_repo = CString("https://github.com/tinelix/irc-client-legacy/tree/main/windows/win32s");
	CEdit* source_code_addr = (CEdit*)GetDlgItem(IDC_SOURCE_ADDRESS);
	source_code_addr->SetWindowText(src_code_repo);

	GetSystemInfo();
	
	return TRUE;  // return TRUE unless you set the focus to a control
	              // EXCEPTION: OCX Property Pages should return FALSE
}

void CAboutDlg::GetSystemInfo() {
	char winverStr[48];
	char win32sStr[32];
	char ramFree[48];

	CIRCApplication *app = (CIRCApplication*)AfxGetApp();
	OSVERSIONINFO winverInfo;
	MEMORYSTATUS memStatus;

	ZeroMemory(&winverInfo, sizeof(OSVERSIONINFO));
	winverInfo.dwOSVersionInfoSize = sizeof(OSVERSIONINFO);

	BOOL result = GetVersionEx(&winverInfo);

	if(result) {
		if(app->CheckIsWin32s()) {
			sprintf(
				winverStr, "Windows 3.1/3.11" 
			);

			sprintf(
				win32sStr, "Win32s %d.%d", 
				winverInfo.dwMajorVersion, winverInfo.dwMinorVersion
			);
		} else if(winverInfo.dwPlatformId == VER_PLATFORM_WIN32_NT) {
			sprintf(
				winverStr, "Windows NT %d.%d", 
				winverInfo.dwMajorVersion, winverInfo.dwMinorVersion
			);

			sprintf(win32sStr, "Win32");
		} else {
			sprintf(
				winverStr, "Windows %d.%d", 
				winverInfo.dwMajorVersion, winverInfo.dwMinorVersion
			);

			sprintf(win32sStr, "Win32 compatible");
		}

		GetDlgItem(IDC_WINVER_LABEL)->SetWindowText(winverStr);

		GetDlgItem(IDC_WIN32S_LABEL)->ShowWindow(SW_NORMAL);
		GetDlgItem(IDC_WIN32S_LABEL)->SetWindowText(win32sStr);
	}

	ZeroMemory(&memStatus, sizeof(MEMORYSTATUS));
	memStatus.dwLength = sizeof(MEMORYSTATUS);

	GlobalMemoryStatus(&memStatus);

	if(memStatus.dwAvailPhys >= 1024 * 1024 * 1024)
		sprintf(
			ramFree, "%.2f GB RAM free", 
			(float)memStatus.dwAvailPhys / 1024 / 1024 / 1024
		);
	else if(memStatus.dwAvailPhys >= 1024 * 1024)
		sprintf(
			ramFree, "%.2f MB RAM free", 
			(float)memStatus.dwAvailPhys / 1024 / 1024
		);
	else
		sprintf(
			ramFree, "%.2f KB RAM free", 
			(float)memStatus.dwAvailPhys / 1024
		);

	GetDlgItem(IDC_FREE_RAM_MEM)->SetWindowText(ramFree);
}
