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
!MESSAGE NMAKE /f "Tinelix IRC Parser.mak"\
 CFG="Tinelix IRC Parser - Win32 Debug"
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
# PROP Output_Dir "out/obj"
# PROP Intermediate_Dir "out/interm"
# PROP Target_Dir ""
OUTDIR=.\out/obj
INTDIR=.\out/interm

ALL : "$(OUTDIR)\ircpars.dll"

CLEAN : 
	-@erase ".\out\bin\ircpars.dll"
	-@erase ".\out\interm\ircpars.obj"
	-@erase ".\out\interm\ircpars.res"
	-@erase ".\out\obj\ircpars.lib"
	-@erase ".\out\obj\ircpars.exp"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

"$(INTDIR)" :
    if not exist "$(INTDIR)/$(NULL)" mkdir "$(INTDIR)"

# ADD BASE CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /YX /c
# ADD CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /YX /c
CPP_PROJ=/nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS"\
 /Fp"$(INTDIR)/Tinelix IRC Parser.pch" /YX /Fo"$(INTDIR)/" /c 
CPP_OBJS=.\out/interm/
CPP_SBRS=
# ADD BASE MTL /nologo /D "NDEBUG" /win32
# ADD MTL /nologo /D "NDEBUG" /win32
MTL_PROJ=/nologo /D "NDEBUG" /win32 
# ADD BASE RSC /l 0x419 /d "NDEBUG"
# ADD RSC /l 0x419 /d "NDEBUG"
RSC_PROJ=/l 0x419 /fo"$(INTDIR)/ircpars.res" /d "NDEBUG" 
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/Tinelix IRC Parser.bsc" 
BSC32_SBRS=
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /dll /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /dll /machine:I386 /out:"out/bin/ircpars.dll"
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib\
 advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib\
 odbccp32.lib /nologo /subsystem:windows /dll /incremental:no\
 /pdb:"$(OUTDIR)/ircpars.pdb" /machine:I386 /def:".\src\ircpars.def"\
 /out:"out/bin/ircpars.dll" /implib:"$(OUTDIR)/ircpars.lib" 
DEF_FILE= \
	".\src\ircpars.def"
LINK32_OBJS= \
	"$(INTDIR)/ircpars.obj" \
	"$(INTDIR)/ircpars.res"

"$(OUTDIR)\ircpars.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "Tinelix IRC Parser - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Tinelix_"
# PROP BASE Intermediate_Dir "Tinelix_"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "out/obj"
# PROP Intermediate_Dir "out/interm"
# PROP Target_Dir ""
OUTDIR=.\out/obj
INTDIR=.\out/interm

ALL : "$(OUTDIR)\ircpars.dll"

CLEAN : 
	-@erase ".\out\bin\ircpars.dll"
	-@erase ".\out\interm\ircpars.obj"
	-@erase ".\out\interm\ircpars.res"
	-@erase ".\out\bin\ircpars.ilk"
	-@erase ".\out\obj\ircpars.lib"
	-@erase ".\out\obj\ircpars.exp"
	-@erase ".\out\obj\ircpars.pdb"
	-@erase ".\out\interm\vc40.pdb"
	-@erase ".\out\interm\vc40.idb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

"$(INTDIR)" :
    if not exist "$(INTDIR)/$(NULL)" mkdir "$(INTDIR)"

# ADD BASE CPP /nologo /MTd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /YX /c
# ADD CPP /nologo /MTd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /YX /c
CPP_PROJ=/nologo /MTd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS"\
 /Fp"$(INTDIR)/Tinelix IRC Parser.pch" /YX /Fo"$(INTDIR)/" /Fd"$(INTDIR)/" /c 
CPP_OBJS=.\out/interm/
CPP_SBRS=
# ADD BASE MTL /nologo /D "_DEBUG" /win32
# ADD MTL /nologo /D "_DEBUG" /win32
MTL_PROJ=/nologo /D "_DEBUG" /win32 
# ADD BASE RSC /l 0x419 /d "_DEBUG"
# ADD RSC /l 0x419 /d "_DEBUG"
RSC_PROJ=/l 0x419 /fo"$(INTDIR)/ircpars.res" /d "_DEBUG" 
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/Tinelix IRC Parser.bsc" 
BSC32_SBRS=
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /dll /debug /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:windows /dll /debug /machine:I386 /out:"out/bin/ircpars.dll"
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib\
 advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib\
 odbccp32.lib /nologo /subsystem:windows /dll /incremental:yes\
 /pdb:"$(OUTDIR)/ircpars.pdb" /debug /machine:I386 /def:".\src\ircpars.def"\
 /out:"out/bin/ircpars.dll" /implib:"$(OUTDIR)/ircpars.lib" 
DEF_FILE= \
	".\src\ircpars.def"
LINK32_OBJS= \
	"$(INTDIR)/ircpars.obj" \
	"$(INTDIR)/ircpars.res"

"$(OUTDIR)\ircpars.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
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

SOURCE=.\src\ircpars.cpp
DEP_CPP_IRCPA=\
	".\include\ircpars.h"\
	

"$(INTDIR)\ircpars.obj" : $(SOURCE) $(DEP_CPP_IRCPA) "$(INTDIR)"
   $(CPP) $(CPP_PROJ) $(SOURCE)


# End Source File
################################################################################
# Begin Source File

SOURCE=.\res\ircpars.rc
NODEP_RSC_IRCPAR=\
	".\res\resource.h"\
	

!IF  "$(CFG)" == "Tinelix IRC Parser - Win32 Release"


"$(INTDIR)\ircpars.res" : $(SOURCE) "$(INTDIR)"
   $(RSC) /l 0x419 /fo"$(INTDIR)/ircpars.res" /i "res" /d "NDEBUG" $(SOURCE)


!ELSEIF  "$(CFG)" == "Tinelix IRC Parser - Win32 Debug"


"$(INTDIR)\ircpars.res" : $(SOURCE) "$(INTDIR)"
   $(RSC) /l 0x419 /fo"$(INTDIR)/ircpars.res" /i "res" /d "_DEBUG" $(SOURCE)


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\src\ircpars.def

!IF  "$(CFG)" == "Tinelix IRC Parser - Win32 Release"

!ELSEIF  "$(CFG)" == "Tinelix IRC Parser - Win32 Debug"

!ENDIF 

# End Source File
# End Target
# End Project
################################################################################
