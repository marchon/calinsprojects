@echo off
:: $Id$
setlocal
:: Perform post-build steps
:: An example follows on the next two lines ...
:: xcopy /y ".\obj%BUILD_ALT_DIR%\i386\*.sys" "..\"
:: xcopy /y ".\obj%BUILD_ALT_DIR%\i386\*.pdb" "..\"
	
	::calin has been here
	xcopy /y ".\obj%BUILD_ALT_DIR%\i386\NetProtectorHookDriver.sys" "..\Debug\"
	xcopy /y ".\obj%BUILD_ALT_DIR%\i386\NetProtectorHookDriver.sys" "..\Release\"
endlocal