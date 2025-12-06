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
!MESSAGE NMAKE /f "TinelixIRC.mak" CFG="Tinelix IRC - Win32 Debug"
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
RSC=rc.exe
MTL=mktyplib.exe
CPP=cl.exe

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
	-@erase "..\..\out\vc4\x86\interm\StatisticsDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\TinelixIRC.obj"
	-@erase "..\..\out\vc4\x86\interm\StdAfx.obj"
	-@erase "..\..\out\vc4\x86\interm\ConnManDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\ProgressDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\AboutDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\MainDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\AppThreadTab.obj"
	-@erase "..\..\out\vc4\x86\interm\TextBoxDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\TinelixIRC.res"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

"$(INTDIR)" :
    if not exist "$(INTDIR)/$(NULL)" mkdir "$(INTDIR)"

# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /YX /c
# ADD CPP /nologo /MT /W3 /GX /O2 /I "../../res" /I "../../include" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /YX /c
CPP_PROJ=/nologo /MT /W3 /GX /O2 /I "../../res" /I "../../include" /D "WIN32"\
 /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /Fp"$(INTDIR)/TinelixIRC.pch" /YX\
 /Fo"$(INTDIR)/" /c 
CPP_OBJS=.\../../out/vc4/x86/interm/
CPP_SBRS=
# ADD BASE MTL /nologo /D "NDEBUG" /win32
# ADD MTL /nologo /D "NDEBUG" /win32
MTL_PROJ=/nologo /D "NDEBUG" /win32 
# ADD BASE RSC /l 0x419 /d "NDEBUG"
# ADD RSC /l 0x41c /i "../../res" /i "../../include" /d "NDEBUG"
RSC_PROJ=/l 0x41c /fo"$(INTDIR)/TinelixIRC.res" /i "../../res" /i\
 "../../include" /d "NDEBUG" 
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/TinelixIRC.bsc" 
BSC32_SBRS=
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /machine:I386
# ADD LINK32 /nologo /subsystem:windows /pdb:"../../out/vc4/x86/pdb/tlxirc.pdb" /machine:I386 /out:"../../out/vc4/x86/bin/tlxirc.exe"
# SUBTRACT LINK32 /pdb:none
LINK32_FLAGS=/nologo /subsystem:windows /incremental:no\
 /pdb:"../../out/vc4/x86/pdb/tlxirc.pdb" /machine:I386\
 /out:"$(OUTDIR)/tlxirc.exe" 
LINK32_OBJS= \
	"..\..\out\vc4\x86\interm\StatisticsDlg.obj" \
	"..\..\out\vc4\x86\interm\TinelixIRC.obj" \
	"..\..\out\vc4\x86\interm\StdAfx.obj" \
	"..\..\out\vc4\x86\interm\ConnManDlg.obj" \
	"..\..\out\vc4\x86\interm\ProgressDlg.obj" \
	"..\..\out\vc4\x86\interm\AboutDlg.obj" \
	"..\..\out\vc4\x86\interm\MainDlg.obj" \
	"..\..\out\vc4\x86\interm\AppThreadTab.obj" \
	"..\..\out\vc4\x86\interm\TextBoxDlg.obj" \
	"..\..\out\vc4\x86\interm\TinelixIRC.res"

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

ALL : "$(OUTDIR)\tlxirc.exe" "$(OUTDIR)\TinelixIRC.bsc"

CLEAN : 
	-@erase "..\..\out\vc4\x86\interm\vc40.pdb"
	-@erase "..\..\out\vc4\x86\interm\vc40.idb"
	-@erase "..\..\out\vc4\x86\bin\TinelixIRC.bsc"
	-@erase "..\..\out\vc4\x86\interm\ConnManDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\ProgressDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\AboutDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\MainDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\AppThreadTab.sbr"
	-@erase "..\..\out\vc4\x86\interm\TextBoxDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\StatisticsDlg.sbr"
	-@erase "..\..\out\vc4\x86\interm\TinelixIRC.sbr"
	-@erase "..\..\out\vc4\x86\interm\StdAfx.sbr"
	-@erase "..\..\out\vc4\x86\bin\tlxirc.exe"
	-@erase "..\..\out\vc4\x86\interm\StatisticsDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\TinelixIRC.obj"
	-@erase "..\..\out\vc4\x86\interm\StdAfx.obj"
	-@erase "..\..\out\vc4\x86\interm\ConnManDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\ProgressDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\AboutDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\MainDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\AppThreadTab.obj"
	-@erase "..\..\out\vc4\x86\interm\TextBoxDlg.obj"
	-@erase "..\..\out\vc4\x86\interm\TinelixIRC.res"
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
 /Fp"$(INTDIR)/TinelixIRC.pch" /YX /Fo"$(INTDIR)/" /Fd"$(INTDIR)/" /c 
CPP_OBJS=.\../../out/vc4/x86/interm/
CPP_SBRS=.\../../out/vc4/x86/interm/
# ADD BASE MTL /nologo /D "_DEBUG" /win32
# ADD MTL /nologo /D "_DEBUG" /win32
MTL_PROJ=/nologo /D "_DEBUG" /win32 
# ADD BASE RSC /l 0x419 /d "_DEBUG"
# ADD RSC /l 0x41c /i "../../res" /i "../../include" /d "_DEBUG"
RSC_PROJ=/l 0x41c /fo"$(INTDIR)/TinelixIRC.res" /i "../../res" /i\
 "../../include" /d "_DEBUG" 
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/TinelixIRC.bsc" 
BSC32_SBRS= \
	"..\..\out\vc4\x86\interm\ConnManDlg.sbr" \
	"..\..\out\vc4\x86\interm\ProgressDlg.sbr" \
	"..\..\out\vc4\x86\interm\AboutDlg.sbr" \
	"..\..\out\vc4\x86\interm\MainDlg.sbr" \
	"..\..\out\vc4\x86\interm\AppThreadTab.sbr" \
	"..\..\out\vc4\x86\interm\TextBoxDlg.sbr" \
	"..\..\out\vc4\x86\interm\StatisticsDlg.sbr" \
	"..\..\out\vc4\x86\interm\TinelixIRC.sbr" \
	"..\..\out\vc4\x86\interm\StdAfx.sbr"

"$(OUTDIR)\TinelixIRC.bsc" : "$(OUTDIR)" $(BSC32_SBRS)
    $(BSC32) @<<
  $(BSC32_FLAGS) $(BSC32_SBRS)
<<

LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /debug /machine:I386
# ADD LINK32 /nologo /subsystem:windows /pdb:"../../out/vc4/x86/pdb/tlxirc.pdb" /debug /machine:I386 /out:"../../out/vc4/x86/bin/tlxirc.exe"
# SUBTRACT LINK32 /pdb:none
LINK32_FLAGS=/nologo /subsystem:windows /incremental:yes\
 /pdb:"../../out/vc4/x86/pdb/tlxirc.pdb" /debug /machine:I386\
 /out:"$(OUTDIR)/tlxirc.exe" 
LINK32_OBJS= \
	"..\..\out\vc4\x86\interm\StatisticsDlg.obj" \
	"..\..\out\vc4\x86\interm\TinelixIRC.obj" \
	"..\..\out\vc4\x86\interm\StdAfx.obj" \
	"..\..\out\vc4\x86\interm\ConnManDlg.obj" \
	"..\..\out\vc4\x86\interm\ProgressDlg.obj" \
	"..\..\out\vc4\x86\interm\AboutDlg.obj" \
	"..\..\out\vc4\x86\interm\MainDlg.obj" \
	"..\..\out\vc4\x86\interm\AppThreadTab.obj" \
	"..\..\out\vc4\x86\interm\TextBoxDlg.obj" \
	"..\..\out\vc4\x86\interm\TinelixIRC.res"

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
	".\../../include\dialogs/MainDlg.h"\
	

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
	".\../../include\dialogs/MainDlg.h"\
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
	".\../../include\dialogs/MainDlg.h"\
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
	".\../../include\dialogs/MainDlg.h"\
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
	".\../../include\dialogs/MainDlg.h"\
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
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\res\TinelixIRC.rc"

!IF  "$(CFG)" == "Tinelix IRC - Win32 Release"


"$(INTDIR)\TinelixIRC.res" : $(SOURCE) "$(INTDIR)"
   $(RSC) /l 0x41c /fo"$(INTDIR)/TinelixIRC.res" /i "../../res" /i\
 "../../include" /i "\users\tretdm\Sources\irc-client-legacy\windows\win32s\res"\
 /d "NDEBUG" $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC - Win32 Debug"


"$(INTDIR)\TinelixIRC.res" : $(SOURCE) "$(INTDIR)"
   $(RSC) /l 0x41c /fo"$(INTDIR)/TinelixIRC.res" /i "../../res" /i\
 "../../include" /i "\users\tretdm\Sources\irc-client-legacy\windows\win32s\res"\
 /d "_DEBUG" $(SOURCE)


!ENDIF 

# End Source File
# End Target
# End Project
################################################################################
