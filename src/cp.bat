@ECHO OFF
echo. > conc.txt
for /r    %%i in (*.java) do (
   echo BBEEGGIINN %%i >> conc.txt
   type %%i >> conc.txt
   echo. >> conc.txt
   echo EENNDD  >> conc.txt
   echo. >> conc.txt
)
ECHO
