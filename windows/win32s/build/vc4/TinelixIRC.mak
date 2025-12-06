# Microsoft Developer Studio Generated NMAKE File, Format Version 4.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Application" 0x0101

!IF "$(CFG)" == ""
CFG=Tinelix IRC - Win32 Debug
!MESSAGE No configuration specified.  Defaulting to Tinelix IRC - Win32 Debug.
!ENDIF 

!IF "$(CFG)" != "Tinelix IRC - Win32 Release" && "$(CFG)" !=\
 "Tinelix IRC - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE on this makefile
!MESSAGE by defining the macro CFG on the command line.  For example:
!MESSAGE 
!MESSAGE NMAKE /f "Tinelix IRC.mak" CFG="Tinelix IRC - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "Tinelix IRC - Win32 Release" (based on "Win32 (x86) Application")
!MESSAGE "Tinelix IRC - Win32 Debug" (based on "Win32 (x86) Application")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 
################################################################################
# Begin Project
# PROP Target_Last_Scanned "Tinelix IRC - Win32 Debug"
CPP=cl.exe
RSC=rc.exe
MTL=mktyplib.exe

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 1
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "../../out/vc4/x86/bin"
# PROP Intermediate_Dir "../../out/vc4/x86/interm"
# PROP Target_Dir ""
OUTDIR=.\../../out/vc4/x86/bin
INTDIR=.\../../out/vc4/x86/interm

ALL : "$(OUTDIR)\tlxirc.exe"

CLEAN : 
	-@erase "..\..\out\vc4\x86\bin\tlxirc.exe"
	-@erase "..\..\out\vc4\x86\interm\TinelixIRC.obj"
	-@erase "..\..\out\vc4\x86\interm\TextBoxDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\StatisticsDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\ProgressDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\MainDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\ConnManDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\AboutDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\AppThreadTab.obj"
	-@erase "..\..\out\vc4\x86\interm\StdAfx.obj"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

"$(INTDIR)" :
    if not exist "$(INTDIR)/$(NULL)" mkdir "$(INTDIR)"

# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /YX /c
# ADD CPP /nologo /MT /W3 /GX /O2 /I "../../res" /I "../../include" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /YX /c
CPP_PROJ=/nologo /MT /W3 /GX /O2 /I "../../res" /I "../../include" /D "WIN32"\
 /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /Fp"$(INTDIR)/Tinelix IRC.pch" /YX\
 /Fo"$(INTDIR)/" /c 
CPP_OBJS=.\../../out/vc4/x86/interm/
CPP_SBRS=
# ADD BASE MTL /nologo /D "NDEBUG" /win32
# ADD MTL /nologo /D "NDEBUG" /win32
MTL_PROJ=/nologo /D "NDEBUG" /win32 
# ADD BASE RSC /l 0x419 /d "NDEBUG"
# ADD RSC /l 0x41c /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/Tinelix IRC.bsc" 
BSC32_SBRS=
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /machine:I386
# ADD LINK32 /nologo /subsystem:windows /pdb:"../../out/vc4/x86/pdb/tlxirc.pdb" /machine:I386 /out:"../../out/vc4/x86/bin/tlxirc.exe"
LINK32_FLAGS=/nologo /subsystem:windows /incremental:no\
 /pdb:"../../out/vc4/x86/pdb/tlxirc.pdb" /machine:I386\
 /out:"$(OUTDIR)/tlxirc.exe" 
LINK32_OBJS= \
	"$(INTDIR)/TinelixIRC.obj" \
	"$(INTDIR)/TextBoxDlg.obj" \
	"$(INTDIR)/StatisticsDlg.obj" \
	"$(INTDIR)/ProgressDlg.obj" \
	"$(INTDIR)/MainDlg.obj" \
	"$(INTDIR)/ConnManDlg.obj" \
	"$(INTDIR)/AboutDlg.obj" \
	"$(INTDIR)/AppThreadTab.obj" \
	"$(INTDIR)/StdAfx.obj"

"$(OUTDIR)\tlxirc.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 1
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "../../out/vc4/x86/bin"
# PROP Intermediate_Dir "../../out/vc4/x86/interm"
# PROP Target_Dir ""
OUTDIR=.\../../out/vc4/x86/bin
INTDIR=.\../../out/vc4/x86/interm

ALL : "$(OUTDIR)\tlxirc.exe" "$(OUTDIR)\Tinelix IRC.bsc"

CLEAN : 
	-@erase "..\..\out\vc4\x86\bin\Tinelix IRC.bsc"
	-@erase "..\..\out\vc4\x86\interm\TinelixIRC.sbr"
	-@erase "..\..\out\vc4\x86\interm\TextBoxDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\StatisticsDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\ProgressDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\MainDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\ConnManDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\AboutDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\AppThreadTab.sbr"
	-@erase "..\..\out\vc4\x86\interm\StdAfx.sbr"
	-@erase "..\..\out\vc4\x86\interm\vc40.pdb"
	-@erase "..\..\out\vc4\x86\interm\vc40.idb"
	-@erase "..\..\out\vc4\x86\bin\tlxirc.exe"
	-@erase "..\..\out\vc4\x86\interm\TinelixIRC.obj"
	-@erase "..\..\out\vc4\x86\interm\TextBoxDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\StatisticsDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\ProgressDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\MainDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\ConnManDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\AboutDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\AppThreadTab.obj"
	-@erase "..\..\out\vc4\x86\interm\StdAfx.obj"
	-@erase "..\..\out\vc4\x86\bin\tlxirc.ilk"
	-@erase "..\..\out\vc4\x86\pdb\tlxirc.pdb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

"$(INTDIR)" :
    if not exist "$(INTDIR)/$(NULL)" mkdir "$(INTDIR)"

# ADD BASE CPP /nologo /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /YX /c
# ADD CPP /nologo /MTd /W3 /Gm /GX /Zi /Od /I "../../res" /I "../../include" /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /FR /YX /c
CPP_PROJ=/nologo /MTd /W3 /Gm /GX /Zi /Od /I "../../res" /I "../../include" /D\
 "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /FR"$(INTDIR)/"\
 /Fp"$(INTDIR)/Tinelix IRC.pch" /YX /Fo"$(INTDIR)/" /Fd"$(INTDIR)/" /c 
CPP_OBJS=.\../../out/vc4/x86/interm/
CPP_SBRS=.\../../out/vc4/x86/interm/
# ADD BASE MTL /nologo /D "_DEBUG" /win32
# ADD MTL /nologo /D "_DEBUG" /win32
MTL_PROJ=/nologo /D "_DEBUG" /win32 
# ADD BASE RSC /l 0x419 /d "_DEBUG"
# ADD RSC /l 0x41c /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/Tinelix IRC.bsc" 
BSC32_SBRS= \
	"$(INTDIR)/TinelixIRC.sbr" \
	"$(INTDIR)/TextBoxDlg.sbr" \
	"$(INTDIR)/StatisticsDlg.sbr" \
	"$(INTDIR)/ProgressDlg.sbr" \
	"$(INTDIR)/MainDlg.sbr" \
	"$(INTDIR)/ConnManDlg.sbr" \
	"$(INTDIR)/AboutDlg.sbr" \
	"$(INTDIR)/AppThreadTab.sbr" \
	"$(INTDIR)/StdAfx.sbr"

"$(OUTDIR)\Tinelix IRC.bsc" : "$(OUTDIR)" $(BSC32_SBRS)
    $(BSC32) @<<
  $(BSC32_FLAGS) $(BSC32_SBRS)
<<

LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /debug /machine:I386
# ADD LINK32 /nologo /subsystem:windows /pdb:"../../out/vc4/x86/pdb/tlxirc.pdb" /debug /machine:I386 /out:"../../out/vc4/x86/bin/tlxirc.exe"
LINK32_FLAGS=/nologo /subsystem:windows /incremental:yes\
 /pdb:"../../out/vc4/x86/pdb/tlxirc.pdb" /debug /machine:I386\
 /out:"$(OUTDIR)/tlxirc.exe" 
LINK32_OBJS= \
	"$(INTDIR)/TinelixIRC.obj" \
	"$(INTDIR)/TextBoxDlg.obj" \
	"$(INTDIR)/StatisticsDlg.obj" \
	"$(INTDIR)/ProgressDlg.obj" \
	"$(INTDIR)/MainDlg.obj" \
	"$(INTDIR)/ConnManDlg.obj" \
	"$(INTDIR)/AboutDlg.obj" \
	"$(INTDIR)/AppThreadTab.obj" \
	"$(INTDIR)/StdAfx.obj"

"$(OUTDIR)\tlxirc.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 

.c{$(CPP_OBJS)}.obj:
   $(CPP) $(CPP_PROJ) $<  

.cpp{$(CPP_OBJS)}.obj:
   $(CPP) $(CPP_PROJ) $<  

.cxx{$(CPP_OBJS)}.obj:
   $(CPP) $(CPP_PROJ) $<  

.c{$(CPP_SBRS)}.sbr:
   $(CPP) $(CPP_PROJ) $<  

.cpp{$(CPP_SBRS)}.sbr:
   $(CPP) $(CPP_PROJ) $<  

.cxx{$(CPP_SBRS)}.sbr:
   $(CPP) $(CPP_PROJ) $<  

################################################################################
# Begin Target

# Name "Tinelix IRC - Win32 Release"
# Name "Tinelix IRC - Win32 Debug"

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"

!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"

!ENDIF 

################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\src\TinelixIRC.cpp"
DEP_CPP_TINEL=\
	".\../../include\stdafx.h"\
	".\../../include\TinelixIRC.h"\
	".\../../include\dialogs\MainDlg.h"\
	

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\TinelixIRC.obj" : $(SOURCE) $(DEP_CPP_TINEL) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\TinelixIRC.obj" : $(SOURCE) $(DEP_CPP_TINEL) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\TinelixIRC.sbr" : $(SOURCE) $(DEP_CPP_TINEL) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\src\dialogs\TextBoxDlg.cpp"
DEP_CPP_TEXTB=\
	".\../../include\stdafx.h"\
	".\../../include\TinelixIRC.h"\
	".\../../include\dialogs/TextBoxDlg.h"\
	

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\TextBoxDlg.obj" : $(SOURCE) $(DEP_CPP_TEXTB) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\TextBoxDlg.obj" : $(SOURCE) $(DEP_CPP_TEXTB) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\TextBoxDlg.sbr" : $(SOURCE) $(DEP_CPP_TEXTB) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\src\dialogs\StatisticsDlg.cpp"
DEP_CPP_STATI=\
	".\../../include\stdafx.h"\
	".\../../include\TinelixIRC.h"\
	".\../../include\dialogs\MainDlg.h"\
	".\../../include\dialogs/StatisticsDlg.h"\
	

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\StatisticsDlg.obj" : $(SOURCE) $(DEP_CPP_STATI) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\StatisticsDlg.obj" : $(SOURCE) $(DEP_CPP_STATI) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\StatisticsDlg.sbr" : $(SOURCE) $(DEP_CPP_STATI) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\src\dialogs\ProgressDlg.cpp"
DEP_CPP_PROGR=\
	".\../../include\stdafx.h"\
	".\../../include\TinelixIRC.h"\
	".\../../include\dialogs/ProgressDlg.h"\
	

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\ProgressDlg.obj" : $(SOURCE) $(DEP_CPP_PROGR) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\ProgressDlg.obj" : $(SOURCE) $(DEP_CPP_PROGR) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\ProgressDlg.sbr" : $(SOURCE) $(DEP_CPP_PROGR) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\src\dialogs\MainDlg.cpp"
DEP_CPP_MAIND=\
	".\../../include\stdafx.h"\
	{$(INCLUDE)}"\sys\TYPES.H"\
	{$(INCLUDE)}"\sys\TIMEB.H"\
	".\../../include\TinelixIRC.h"\
	".\../../include\tabs/AppThreadTab.h"\
	".\../../include\dialogs\MainDlg.h"\
	".\../../include\dialogs/ConnManDlg.h"\
	".\../../include\dialogs/ProgressDlg.h"\
	".\../../include\dialogs/StatisticsDlg.h"\
	".\../../include\dialogs/AboutDlg.h"\
	

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\MainDlg.obj" : $(SOURCE) $(DEP_CPP_MAIND) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\MainDlg.obj" : $(SOURCE) $(DEP_CPP_MAIND) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\MainDlg.sbr" : $(SOURCE) $(DEP_CPP_MAIND) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\src\dialogs\ConnManDlg.cpp"
DEP_CPP_CONNM=\
	".\../../include\stdafx.h"\
	".\../../include\TinelixIRC.h"\
	".\../../include\dialogs\MainDlg.h"\
	".\../../include\dialogs/ConnManDlg.h"\
	".\../../include\dialogs/TextBoxDlg.h"\
	

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\ConnManDlg.obj" : $(SOURCE) $(DEP_CPP_CONNM) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\ConnManDlg.obj" : $(SOURCE) $(DEP_CPP_CONNM) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\ConnManDlg.sbr" : $(SOURCE) $(DEP_CPP_CONNM) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\src\dialogs\AboutDlg.cpp"
DEP_CPP_ABOUT=\
	".\../../include\stdafx.h"\
	".\../../include\TinelixIRC.h"\
	".\../../include\dialogs/AboutDlg.h"\
	

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\AboutDlg.obj" : $(SOURCE) $(DEP_CPP_ABOUT) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\AboutDlg.obj" : $(SOURCE) $(DEP_CPP_ABOUT) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\AboutDlg.sbr" : $(SOURCE) $(DEP_CPP_ABOUT) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\src\tabs\AppThreadTab.cpp"
DEP_CPP_APPTH=\
	".\../../include\stdafx.h"\
	".\../../include\TinelixIRC.h"\
	".\../../include\dialogs\MainDlg.h"\
	".\../../include\tabs/AppThreadTab.h"\
	

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\AppThreadTab.obj" : $(SOURCE) $(DEP_CPP_APPTH) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\AppThreadTab.obj" : $(SOURCE) $(DEP_CPP_APPTH) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\AppThreadTab.sbr" : $(SOURCE) $(DEP_CPP_APPTH) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE="\users\tretdm\Sources\irc-client-legacy\windows\win32s\res\resource.h"

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"

!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE="\users\tretdm\Sources\irc-client-legacy\windows\win32s\src\StdAfx.cpp"
DEP_CPP_STDAF=\
	".\../../include\stdafx.h"\
	

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\StdAfx.obj" : $(SOURCE) $(DEP_CPP_STDAF) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\StdAfx.obj" : $(SOURCE) $(DEP_CPP_STDAF) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\StdAfx.sbr" : $(SOURCE) $(DEP_CPP_STDAF) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
# End Target
# End Project
################################################################################
