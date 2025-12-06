# Microsoft Developer Studio Generated NMAKE File, Format Version 4.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Dynamic-Link Library" 0x0102

!IF "$(CFG)" == ""
CFG=Tinelix IRC Parser - Win32 Debug
!MESSAGE No configuration specified.  Defaulting to Tinelix IRC Parser - Win32\
 Debug.
!ENDIF 

!IF "$(CFG)" != "Tinelix IRC Parser - Win32 Release" && "$(CFG)" !=\
 "Tinelix IRC Parser - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE on this makefile
!MESSAGE by defining the macro CFG on the command line.  For example:
!MESSAGE 
!MESSAGE NMAKE /f "IRCParser.mak" CFG="Tinelix IRC Parser - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "Tinelix IRC Parser - Win32 Release" (based on\
 "Win32 (x86) Dynamic-Link Library")
!MESSAGE "Tinelix IRC Parser - Win32 Debug" (based on\
 "Win32 (x86) Dynamic-Link Library")
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
# PROP Target_Last_Scanned "Tinelix IRC Parser - Win32 Debug"
CPP=cl.exe
RSC=rc.exe
MTL=mktyplib.exe

!IF  "$(CFG)" == "Tinelix IRC Parser - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "../../out/vc4/x86/bin"
# PROP Intermediate_Dir "../../out/vc4/x86/interm"
# PROP Target_Dir ""
OUTDIR=.\../../out/vc4/x86/bin
INTDIR=.\../../out/vc4/x86/interm

ALL : "$(OUTDIR)\IRCPars.dll"

CLEAN : 
	-@erase "..\..\out\vc4\x86\bin\IRCPars.dll"
	-@erase "..\..\out\vc4\x86\interm\ircpars.obj"
	-@erase "..\..\out\vc4\x86\interm\ircpars.res"
	-@erase "..\..\out\vc4\x86\bin\IRCPars.lib"
	-@erase "..\..\out\vc4\x86\bin\IRCPars.exp"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

"$(INTDIR)" :
    if not exist "$(INTDIR)/$(NULL)" mkdir "$(INTDIR)"

# ADD BASE CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /YX /c
# ADD CPP /nologo /MT /W3 /GX /O2 /I "../../include" /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /YX /c
CPP_PROJ=/nologo /MT /W3 /GX /O2 /I "../../include" /D "WIN32" /D "NDEBUG" /D\
 "_WINDOWS" /Fp"$(INTDIR)/IRCParser.pch" /YX /Fo"$(INTDIR)/" /c 
CPP_OBJS=.\../../out/vc4/x86/interm/
CPP_SBRS=
# ADD BASE MTL /nologo /D "NDEBUG" /win32
# ADD MTL /nologo /D "NDEBUG" /win32
MTL_PROJ=/nologo /D "NDEBUG" /win32 
# ADD BASE RSC /l 0x419 /d "NDEBUG"
# ADD RSC /l 0x41c /d "NDEBUG"
RSC_PROJ=/l 0x41c /fo"$(INTDIR)/ircpars.res" /d "NDEBUG" 
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/IRCParser.bsc" 
BSC32_SBRS=
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /dll /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib wsock32.lib /nologo /subsystem:windows /dll /machine:I386 /out:"../../out/vc4/x86/bin/IRCPars.dll"
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib\
 advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib\
 odbccp32.lib wsock32.lib /nologo /subsystem:windows /dll /incremental:no\
 /pdb:"$(OUTDIR)/IRCPars.pdb" /machine:I386\
 /def:"\users\tretdm\Sources\irc-client-legacy\windows\win32s\libs\IRCParser\src\ircpars.def"\
 /out:"$(OUTDIR)/IRCPars.dll" /implib:"$(OUTDIR)/IRCPars.lib" 
DEF_FILE= \
	"..\..\src\ircpars.def"
LINK32_OBJS= \
	"$(INTDIR)/ircpars.obj" \
	"$(INTDIR)/ircpars.res"

"$(OUTDIR)\IRCPars.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "Tinelix IRC Parser - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "../../out/vc4/x86/bin"
# PROP Intermediate_Dir "../../out/vc4/x86/interm"
# PROP Target_Dir ""
OUTDIR=.\../../out/vc4/x86/bin
INTDIR=.\../../out/vc4/x86/interm

ALL : "$(OUTDIR)\IRCPars.dll"

CLEAN : 
	-@erase "..\..\out\vc4\x86\bin\IRCPars.dll"
	-@erase "..\..\out\vc4\x86\interm\ircpars.obj"
	-@erase "..\..\out\vc4\x86\interm\ircpars.res"
	-@erase "..\..\out\vc4\x86\bin\IRCPars.ilk"
	-@erase "..\..\out\vc4\x86\bin\IRCPars.lib"
	-@erase "..\..\out\vc4\x86\bin\IRCPars.exp"
	-@erase "..\..\out\vc4\x86\bin\IRCPars.pdb"
	-@erase "..\..\out\vc4\x86\interm\vc40.pdb"
	-@erase "..\..\out\vc4\x86\interm\vc40.idb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

"$(INTDIR)" :
    if not exist "$(INTDIR)/$(NULL)" mkdir "$(INTDIR)"

# ADD BASE CPP /nologo /MTd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /YX /c
# ADD CPP /nologo /MTd /W3 /Gm /GX /Zi /Od /I "../../include" /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /YX /c
CPP_PROJ=/nologo /MTd /W3 /Gm /GX /Zi /Od /I "../../include" /D "WIN32" /D\
 "_DEBUG" /D "_WINDOWS" /Fp"$(INTDIR)/IRCParser.pch" /YX /Fo"$(INTDIR)/"\
 /Fd"$(INTDIR)/" /c 
CPP_OBJS=.\../../out/vc4/x86/interm/
CPP_SBRS=
# ADD BASE MTL /nologo /D "_DEBUG" /win32
# ADD MTL /nologo /D "_DEBUG" /win32
MTL_PROJ=/nologo /D "_DEBUG" /win32 
# ADD BASE RSC /l 0x419 /d "_DEBUG"
# ADD RSC /l 0x41c /d "_DEBUG"
RSC_PROJ=/l 0x41c /fo"$(INTDIR)/ircpars.res" /d "_DEBUG" 
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/IRCParser.bsc" 
BSC32_SBRS=
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /dll /debug /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib wsock32.lib /nologo /subsystem:windows /dll /debug /machine:I386 /out:"../../out/vc4/x86/bin/IRCPars.dll"
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib\
 advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib\
 odbccp32.lib wsock32.lib /nologo /subsystem:windows /dll /incremental:yes\
 /pdb:"$(OUTDIR)/IRCPars.pdb" /debug /machine:I386\
 /def:"\users\tretdm\Sources\irc-client-legacy\windows\win32s\libs\IRCParser\src\ircpars.def"\
 /out:"$(OUTDIR)/IRCPars.dll" /implib:"$(OUTDIR)/IRCPars.lib" 
DEF_FILE= \
	"..\..\src\ircpars.def"
LINK32_OBJS= \
	"$(INTDIR)/ircpars.obj" \
	"$(INTDIR)/ircpars.res"

"$(OUTDIR)\IRCPars.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
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

# Name "Tinelix IRC Parser - Win32 Release"
# Name "Tinelix IRC Parser - Win32 Debug"

!IF  "$(CFG)" == "Tinelix IRC Parser - Win32 Release"

!ELSEIF  "$(CFG)" == "Tinelix IRC Parser - Win32 Debug"

!ENDIF 

################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\libs\IRCParser\include\ircpars.h"

!IF  "$(CFG)" == "Tinelix IRC Parser - Win32 Release"

!ELSEIF  "$(CFG)" == "Tinelix IRC Parser - Win32 Debug"

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\libs\IRCParser\src\ircpars.cpp"

"$(INTDIR)\ircpars.obj" : $(SOURCE) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\libs\IRCParser\src\ircpars.def"

!IF  "$(CFG)" == "Tinelix IRC Parser - Win32 Release"

!ELSEIF  "$(CFG)" == "Tinelix IRC Parser - Win32 Debug"

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=\
"\users\tretdm\Sources\irc-client-legacy\windows\win32s\libs\IRCParser\res\ircpars.rc"

!IF  "$(CFG)" == "Tinelix IRC Parser - Win32 Release"


"$(INTDIR)\ircpars.res" : $(SOURCE) "$(INTDIR)"
   $(RSC) /l 0x41c /fo"$(INTDIR)/ircpars.res" /i\
 "\users\tretdm\Sources\irc-client-legacy\windows\win32s\libs\IRCParser\res" /d\
 "NDEBUG" $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC Parser - Win32 Debug"


"$(INTDIR)\ircpars.res" : $(SOURCE) "$(INTDIR)"
   $(RSC) /l 0x41c /fo"$(INTDIR)/ircpars.res" /i\
 "\users\tretdm\Sources\irc-client-legacy\windows\win32s\libs\IRCParser\res" /d\
 "_DEBUG" $(SOURCE)


!ENDIF 

# End Source File
# End Target
# End Project
################################################################################
