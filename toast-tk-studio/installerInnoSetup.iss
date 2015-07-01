<<<<<<< HEAD
; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{B7953978-6037-420A-9BC4-294352D1E5CA}
AppName=Toast Tk Studio
AppVersion=1.0
;AppVerName=Toast Tk Studio 1.0
AppPublisher=Synaptix Labs
AppPublisherURL=http://www.synaptix-labs.com/
AppSupportURL=http://www.synaptix-labs.com/
AppUpdatesURL=http://www.synaptix-labs.com/
DefaultDirName={pf}\Toast Tk Studio
DefaultGroupName=Toast Tk Studio
LicenseFile=D:\workspace\toast-tk\toast-tk-studio\COPYING.TXT
OutputDir=D:\workspace\toast-tk\toast-tk-studio\setup
OutputBaseFilename=toast-tk-studio-setup
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
[Files]
Source: "install\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "D:\Apps\jdk1.6.0_31\jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\Toast Tk Studio"; Filename: "{app}\Toast Studio.exe"
Name: "{group}\{cm:UninstallProgram,Toast Tk Studio}"; Filename: "{uninstallexe}"
Name: "{commondesktop}\Toast Tk Studio"; Filename: "{app}\Toast Studio.exe"; Tasks: desktopicon

[Run]
Filename: "{app}\Toast Studio.exe"; Description: "{cm:LaunchProgram,Toast Tk Studio}"; Flags: nowait postinstall skipifsilent

=======
; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{B7953978-6037-420A-9BC4-294352D1E5CA}
AppName=Toast Tk Studio
AppVersion=1.0
;AppVerName=Toast Tk Studio 1.0
AppPublisher=Synaptix Labs
AppPublisherURL=http://www.synaptix-labs.com/
AppSupportURL=http://www.synaptix-labs.com/
AppUpdatesURL=http://www.synaptix-labs.com/
DefaultDirName={pf}\Toast Tk Studio
DefaultGroupName=Toast Tk Studio
LicenseFile=D:\workspace\toast-tk\toast-tk-studio\COPYING.TXT
OutputDir=D:\workspace\toast-tk\toast-tk-studio\setup
OutputBaseFilename=toast-tk-studio-setup
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
[Files]
Source: "install\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "D:\Apps\jdk1.6.0_31\jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\Toast Tk Studio"; Filename: "{app}\Toast Studio.exe"
Name: "{group}\{cm:UninstallProgram,Toast Tk Studio}"; Filename: "{uninstallexe}"
Name: "{commondesktop}\Toast Tk Studio"; Filename: "{app}\Toast Studio.exe"; Tasks: desktopicon

[Run]
Filename: "{app}\Toast Studio.exe"; Description: "{cm:LaunchProgram,Toast Tk Studio}"; Flags: nowait postinstall skipifsilent

>>>>>>> 6ed019c57c271921833600693ca3d8daefd66b04
